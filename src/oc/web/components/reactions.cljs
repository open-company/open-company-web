(ns oc.web.components.reactions
  (:require-macros [dommy.core :refer (sel1)]
                   [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.react-utils :as react-utils]
            [cljsjs.web-animations]
            [cljsjs.react]
            [cljsjs.react.dom]
            [cljsjs.emoji-mart]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn animate-reaction [e s]
  (when-let* [target (.-currentTarget e)
              span-reaction (sel1 target :span.reaction)]
    (doseq [i (range 5)]
      (let [cloned-el (.cloneNode span-reaction true)
            translate-y {:transform #js ["translateY(0px)" "translateY(-80px)"]
                         :opacity #js [1 0]}
            v (+ 7 (* 3 (int (rand 4))))]
        (set! (.-opacity (.-style cloned-el)) 0)
        (set! (.-position (.-style cloned-el)) "absolute")
        (set! (.-left (.-style cloned-el)) (str v "px"))
        (set! (.-top (.-style cloned-el)) "2px")
        (.appendChild (.-parentElement span-reaction) cloned-el)
        (.animate
         cloned-el
         (clj->js translate-y)
         (clj->js {:duration 800 :delay (* 150 i) :fill "forwards" :easing "ease-out"}))
        (utils/after (+ 800 200 (* 4 150)) #(.removeChild (.-parentNode cloned-el) cloned-el))))))

(def default-reaction-number 5)

(rum/defcs reactions < (rum/local false ::show-picker)
                       (rum/local nil ::window-click)
                       {:did-mount (fn [s]
                         (reset!
                          (::window-click s)
                          (events/listen
                           js/window
                           EventType/CLICK
                           #(when-not (utils/event-inside? % (rum/dom-node s))
                              (reset! (::show-picker s) false))))
                         s)
                        :will-unmount (fn [s]
                         (when @(::window-click s)
                           (events/unlistenByKey @(::window-click s))
                           (reset! (::window-click s) nil))
                         s)}
  [s entry-data]
  (let [reactions-data (vec (:reactions entry-data))
        reactions-loading (:reactions-loading entry-data)
        react-link (utils/link-for (:links entry-data) "react")
        should-show-picker? (and react-link
                                 (< (count reactions-data) default-reaction-number))]
    ;; If there are reactions to render or there is at least the link to add a reaction from the picker
    (when (or (seq reactions-data)
              should-show-picker?)
      [:div.reactions
        [:div.reactions-list
          (when (seq reactions-data)
            (for [idx (range (count reactions-data))
                  :let [reaction-data (get reactions-data idx)
                        is-loading (utils/in? reactions-loading (:reaction reaction-data))
                        read-only-reaction (not (utils/link-for (:links reaction-data) "react" ["PUT" "DELETE"]))
                        r (if is-loading
                            (merge reaction-data {:count (if (:reacted reaction-data)
                                                          (dec (:count reaction-data))
                                                          (inc (:count reaction-data)))
                                                  :reacted (not (:reacted reaction-data))})
                            reaction-data)]]
              [:button.reaction-btn.btn-reset
                {:key (str "reaction-" (:uuid entry-data) "-" idx)
                 :class (utils/class-set {:reacted (:reacted r)
                                          :can-react (not read-only-reaction)
                                          :has-reactions (pos? (:count r))})
                 :on-click (fn [e]
                             (when (and (not is-loading) (not read-only-reaction))
                               (when (and (not (:reacted r))
                                          (not (js/isSafari))
                                          (not (js/isEdge))
                                          (not (js/isIE)))
                                 (animate-reaction e s))
                               (dis/dispatch! [:reaction-toggle (:uuid entry-data) r])))}
                [:span.reaction (:reaction r)]
                [:div.count
                  (when (pos? (:count r))
                    (:count r))]]))
          (when should-show-picker?
            [:button.reaction-btn.btn-reset.can-react.reaction-picker
              {:key (str "reaction-" (:uuid entry-data) "-picker")
               :on-click #(reset! (::show-picker s) (not @(::show-picker s)))}
              [:span.reaction "😀"]
              [:div.count "+"]])]
        [:div.reactions-picker-container
          {:class (utils/class-set {:visible @(::show-picker s)})}
          (when-not (utils/is-test-env?)
            (react-utils/build (.-Picker js/EmojiMart)
              {:native true
               :onClick (fn [emoji event]
                          (dis/dispatch! [:react-from-picker entry-data (.-native emoji)])
                          (reset! (::show-picker s) false))}))]])))