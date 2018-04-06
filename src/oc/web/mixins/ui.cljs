(ns oc.web.mixins.ui
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

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
            total-scroll-height (.. js/document -body -scrollHeight)
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