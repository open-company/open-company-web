(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.mixins.section :as section-mixins]))

(def empty-states
  [["🍊" "Orange you glad you’re all caught up?"]
   ["👻" "How unex-spectred! You’re all caught up."]
   ["🎩" "Hats off to ya! You’re all caught up."]
   ["💅" "Nailed it! You’re all caught up."]
   ["👀" "Keep your eyes on the prize! You’re all caught up."]
   ["🍭" "What a treat, you’re all caught up!"]])

(rum/defcs empty-board < rum/reactive
                         section-mixins/container-nav-in
                         (rum/local nil ::empty-state-num)
                         {:will-mount (fn [s]
                          (reset! (::empty-state-num s) (-> empty-states count rand int))
                          s)}
  [s]
  (let [empty-state-num @(::empty-state-num s)
        empty-state     (get empty-states empty-state-num)]
    [:div.empty-board.group
      [:div.empty-board-grey-box
        {:class (str "empty-board-box-" empty-state-num)}
        [:div.empty-board-illustration
          (get empty-state 0)]
        [:div.empty-board-title
          (get empty-state 1)]]]))