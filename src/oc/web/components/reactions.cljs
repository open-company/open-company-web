(ns oc.web.components.reactions
  (:require-macros [dommy.core :refer (sel1)]
                   [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.react-utils :as react-utils]
            [cljsjs.web-animations]
            [cljsjs.react]
            [cljsjs.react.dom]
            [cljsjs.emoji-mart]
            [goog.events :as events]
            [goog.object :as gobj]
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

(defn can-pick-reaction
  "Given an emoji and the list of the current reactions
   check if the user can react.
   A user can react if:
   - the reaction is NOT already in the reactions list
   - the reaction is already in the reactions list and its not reacted"
  [emoji reactions-data]
  (let [reaction-map (first (filter #(= (:reaction %) emoji) reactions-data))]
    (or (not reaction-map)
        (and (map? reaction-map)
             (not (:reacted reaction-map))))))

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
        is-mobile? (responsive/is-tablet-or-mobile?)
        should-show-picker? (and (not is-mobile?)
                                 react-link
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
                        reacted (:reacted reaction-data)
                        reaction-authors (:authors reaction-data)
                        reaction-start (if (> 1 (count reaction-authors)) "Reactions" "Reaction")
                        reaction-attribution (if (empty? reaction-authors)
                          ""
                          (str reaction-start " by " (clojure.string/join "," reaction-authors)))
                        read-only-reaction (not (utils/link-for
                                                 (:links reaction-data)
                                                 "react"
                                                 (if reacted "DELETE" "PUT")))
                        r (if is-loading
                            (merge reaction-data {:count (if reacted
                                                          (dec (:count reaction-data))
                                                          (inc (:count reaction-data)))
                                                  :reacted (not reacted)})
                            reaction-data)]]

              [:button.reaction-btn.btn-reset
                {:key (str "reaction-" (:uuid entry-data) "-" idx)
                 :class (utils/class-set {:reacted (:reacted r)
                                          :can-react (not read-only-reaction)
                                          :has-reactions (pos? (:count r))})
                 :title reaction-attribution
                 :data-placement (if (empty? reaction-authors) "none" "top")
                 :data-container "body"
                 :data-toggle "tooltip"
                 :on-click (fn [e]
                             (when (and (not is-loading) (not read-only-reaction))
                               (when (and (not (:reacted r))
                                          (not (js/isSafari))
                                          (not (js/isEdge))
                                          (not (js/isIE)))
                                 (animate-reaction e s))
                               (dis/dispatch! [:reaction-toggle entry-data r (not reacted)])))}
                [:span.reaction
                  {:class (when (pos? (:count r)) "has-count")}
                  (:reaction r)]
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
                          (when (can-pick-reaction (gobj/get emoji "native") reactions-data)
                            (dis/dispatch! [:react-from-picker entry-data (gobj/get emoji "native")]))
                          (reset! (::show-picker s) false))}))]])))