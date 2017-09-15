(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]))

(rum/defcs empty-board < rum/reactive
                         (drv/drv :board-data)
  [s]
  (let [board-data (drv/react s :board-data)
        storyboard? (= (:type board-data) "story")]
    [:div.empty-board.group
      (if (:read-only board-data)
        [:div.empty-board-headline
          (str "There aren't " (if storyboard? "entries" "updates") " in " (:name board-data) " yet. ")]
        [:div.empty-board-headline
          (str "You don’t have any " (if storyboard? "entries" "updates") " in " (:name board-data) " yet. ")
          [:button.mlb-reset
            {:on-click #(if storyboard?
                          (dis/dispatch! [:story-create board-data])
                          (dis/dispatch! [:entry-edit {}]))}
            "Add one?"]])
      [:div
        {:class (utils/class-set {:empty-board-image (not storyboard?)
                                  :empty-storyboard-image storyboard?})}]]))