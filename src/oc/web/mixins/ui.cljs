(ns oc.web.mixins.ui
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]))

(def -no-scroll-mixin-class :no-scroll)

(def no-scroll-mixin
  "Mixin used to check if the body has aleady the no-scroll class, if it does it's a no-op.
   If it doesn't it remember to remove it once the component is going to unmount."
  {:did-mount
    (fn [state]
      ;; Add no-scroll to the body if it doesn't has it already
      ;; to avoid scrolling while showing this modal
      (let [body (sel1 [:body])]
        (if (and body
                 (dommy/has-class? body -no-scroll-mixin-class))
          state
          (do ;; if no-scroll is not preset
            ;; add it
            (dommy/add-class! body -no-scroll-mixin-class)
            ;; remember to remove it on unmount
            (assoc state ::-no-scroll-mixin-remove-body-class true)))))
   :will-unmount
    (fn [state]
      (when (and (contains? state ::-no-scroll-mixin-remove-body-class)
                 (::-no-scroll-mixin-remove-body-class state))
        (dommy/remove-class! (sel1 [:body]) -no-scroll-mixin-class)
        (dissoc state ::-no-scroll-mixin-remove-body-class))
      state)})

(def first-render-mixin
  "This mixin will add a :first-render-done atom to your component state. It will
   be false when the component is not mounted, and true when it is. Very useful for
   appear or disappear animations or to track down the component state."
  {:init (fn [state]
           (assoc state :first-render-done (atom false)))
   :after-render
    (fn [state]
      (when-not @(:first-render-done state)
        (reset! (:first-render-done state) true)
        (rum/request-render (:rum/react-component state)))
      state)
   :will-unmount
    (fn [state]
      (reset! (:first-render-done state) false)
      state)})

(def previous-scrolls (atom []))

(defn load-more-items-did-scroll [s e]
  (let [load-next-fn @(:load-more-items-next-fn s)
        load-prev-fn @(:load-more-items-prev-fn s)]
    (when (or (fn? load-next-fn)
              (fn? load-prev-fn))
      (let [win-height (.-innerHeight js/window)
            total-scroll-height (.. js/window -document -body -scrollHeight)
            scroll-offset (- total-scroll-height win-height)
            current-scroll (.-scrollY js/window)
            scroll-index (:-scroll-index s)
            previous-scroll (get @previous-scrolls scroll-index)
            direction (if (> current-scroll @previous-scroll)
                        :down
                        :up)
            scroll-limit (:-scroll-offset s)]
        (reset! (get @previous-scrolls scroll-index) current-scroll)
        (if (= direction :up)
          (when (and (fn? load-next-fn)
                     (< current-scroll scroll-limit))
            (load-next-fn current-scroll))
          (when (and (fn? load-prev-fn)
                     (> current-scroll (- scroll-offset scroll-limit)))
            (load-prev-fn current-scroll)))))))

(defn load-more-items
  "Given a scroll offset, listen for the page scroll and when they are set calls the callback functions.
   The callback functions must be set as atoms at :load-more-items-next-fn and :load-more-items-prev-fn.
   It's responsible of the component to remove the functions to avoid multiple calls.
   The functions are called when the scroll direction is right and the give offset is reached.

   Save :-scroll-index for the last scroll array atom. Use :-scroll-offset to save the passed offset. Use
   :-scroll-listener to save the scroll listener."
  [offset]
  {:init (fn [s]
    (let [scroll-index (count @previous-scrolls)]
      (reset! previous-scrolls (vec (conj @previous-scrolls (atom 0))))
      (-> s
       (assoc :-scroll-index scroll-index)
       (assoc :-scroll-offset offset))))
   :will-mount (fn [s]
    (let [scroll-listener (events/listen js/window EventType/SCROLL (partial load-more-items-did-scroll s))]
      (assoc s :-scroll-listener scroll-listener)))
   :will-unmount (fn [s]
    (if (:-scroll-listener s)
      (do
        (events/unlistenByKey (:-scroll-listener s))
        (reset! previous-scrolls (assoc @previous-scrolls (:-scroll-index s) nil))
        (dissoc s :-scroll-listener))
      s))})

(defn render-on-resize

  "Trigger a re-render when the window resizes.

  IMPORTANT: add this mixin at the bottom of your component mixins' list
  if you want to use a rum/local in the passed callback.

  Example, note when :my-key is setup and used:

  (defn my-callback [state event]
    (reset! (:my-key state) 1))

  (rum/defc component < ;; Mixins: note the order of the following
                        (rum/local 0 :my-key)
                        (render-on-resize my-callback)
    [s]
    [:div])"

  [resize-cb]
  {:will-mount (fn [s]
    (assoc s :render-on-resize-listener
     (events/listen js/window EventType/RESIZE
      (fn [e]
        (when (fn? resize-cb)
          (resize-cb s e))
        (rum/request-render (:rum/react-component s))))))
   :will-unmount (fn [s]
    (let [resize-listener (:render-on-resize-listener s)]
      (when resize-listener
        (events/unlistenByKey (:render-on-resize-listener s)))
      (dissoc s :render-on-resize-listener)))})

(defn ap-seen-mixin
  "Give a selector for the items to check under the component root.
  On each scroll event checks which items are visible with the given selector
  and call the passed callback with the element uuid (got via the data-itemuuid property)."
   [items-selector item-is-visible-cb]
   (let [scroll-listener-kw :ap-seen-mixin-scroll-listener
         mounted-kw :ap-seen-mixin-is-mounted
         check-items-fn (fn [s & [_]]
                         (when @(get s mounted-kw)
                           (let [dom-node (rum/dom-node s)
                                 $all-items (js/$ items-selector dom-node)]
                             (.each $all-items (fn [idx el]
                               (when (and (fn? item-is-visible-cb)
                                          (au/is-element-visible? el))
                                 (item-is-visible-cb s (.attr (js/$ el) "data-itemuuid"))))))))]
     {:init (fn [s]
       (assoc s mounted-kw (atom false)))
      :will-mount (fn [s]
       (assoc s scroll-listener-kw
        (events/listen js/window EventType/SCROLL
         (fn [e] (utils/after 0 #(check-items-fn s e))))))
      :did-mount (fn [s]
       (reset! (get s mounted-kw) true)
       (utils/after 500 #(check-items-fn s))
       s)
      :did-remount (fn [_ s]
       (utils/after 1500 #(check-items-fn s))
       s)
      :will-unmount (fn [s]
       (reset! (get s mounted-kw) false)
       (check-items-fn s)
       (let [scroll-listener (get s scroll-listener-kw)
             next-state (if scroll-listener
                         (do
                          (events/unlistenByKey scroll-listener)
                          (dissoc s scroll-listener-kw))
                         s)
             next-state (dissoc next-state mounted-kw)]
        next-state))}))

(defn wrt-stream-item-mixin
  "Give a selector for the items to check under the component root.
  On each scroll event checks which items are:
  - not truncated
  - the top or the bottom of the element is visible at screen
  The stream-item component uses 2 different bodies, one is shown initially and truncated if needed,
  the second is shown if it was truncated and expanded by the user."
   [items-selector item-read-cb]
   (let [scroll-listener-kw :wrt-stream-mixin-scroll-listener
         mounted-kw :wrt-mixin-is-mounted
         check-item-fn (fn [s idx el]
                         ;; Check if we need to send the item read
                         ;; when the element is visible in the viewport
                         (when (au/is-element-visible? el)
                            (item-read-cb s (.attr (js/$ el) "data-itemuuid"))))
         check-items-fn (fn [s & [_]]
                         (when @(get s mounted-kw)
                           (let [dom-node (rum/dom-node s)
                                 $all-items (js/$ items-selector dom-node)]
                             (.each $all-items (partial check-item-fn s)))))]
     {:init (fn [s]
       (assoc s mounted-kw (atom false)))
      :will-mount (fn [s]
       (-> s
        (assoc scroll-listener-kw
         (events/listen js/window EventType/SCROLL
          (utils/throttled-debounced-fn (partial check-items-fn s) 500)))))
      :did-mount (fn [s]
       (reset! (get s mounted-kw) true)
       (utils/after 2500 #(check-items-fn s))
       s)
      :did-remount (fn [_ s]
       ;; Let's delay to give the time to render
       (utils/after 2500 #(check-items-fn s))
       s)
      :will-unmount (fn [s]
       (reset! (get s mounted-kw) false)
       (check-items-fn s)
       (let [next-state (if-let [scroll-listener (get s scroll-listener-kw)]
                          (do
                            (events/unlistenByKey scroll-listener)
                            (dissoc s scroll-listener-kw))
                          s)
            next-state (dissoc next-state mounted-kw)]
         next-state))}))

(defn on-window-click-mixin [callback]
  {:did-mount (fn [s]
   (let [on-click-listener (events/listen (.getElementById (.-document js/window) "app") EventType/CLICK
                            (fn [e]
                             (callback s e)))]
    (assoc s :on-click-out-listener on-click-listener)))
   :will-unmount (fn [s]
   (if (:on-click-out-listener s)
    (do
      (events/unlistenByKey (:on-click-out-listener s))
      (dissoc s :on-click-out-listener))
    s))})

(defn on-window-resize-mixin [callback]
  {:did-mount (fn [s]
   (let [on-resize-listener (events/listen (.getElementById (.-document js/window) "app") EventType/RESIZE
                             (fn [e]
                              (callback s e)))]
    (assoc s :on-resize-listener on-resize-listener)))
   :will-unmount (fn [s]
   (if (:on-resize-listener s)
    (do
      (events/unlistenByKey (:on-resize-listener s))
      (dissoc s :on-resize-listener))
    s))})

(defn autoresize-textarea
  "Given a React reference to a component node, listens on all the events on that textarea element
   and resize its frame to make sure it doesn't scroll and the no extra blank space."
  [ref & [initially-focused]]
  (letfn [(init-textarea [el]
            (let [observe (utils/observe)
                  resize-fn (fn []
                              (set! (.-height (.-style el)) "auto")
                              (set! (.-height (.-style el)) (str (.-scrollHeight el) "px")))
                  delayed-resize-fn (fn [] (utils/after 0 resize-fn))]
            (observe el "change" resize-fn)
            (observe el "cut" delayed-resize-fn)
            (observe el "paste" delayed-resize-fn)
            (observe el "drop" delayed-resize-fn)
            (observe el "keydown" delayed-resize-fn)
            (when initially-focused
              (.focus el))
            (resize-fn)))]
    {:did-mount (fn [s]
      (when-let [el (rum/ref-node s ref)]
        (init-textarea el))
      s)}))