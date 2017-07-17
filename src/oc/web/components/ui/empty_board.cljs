(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]))

(rum/defcs empty-board < rum/reactive
                         (drv/drv :board-data)
  [s]
  (let [board-data (drv/react s :board-data)]
    [:div.empty-board.group
      (if (:read-only board-data)
        [:div.empty-board-headline
          (str "There aren't updates in " (:name board-data) " yet. ")]
        [:div.empty-board-headline
          (str "You don’t have any updates in " (:name board-data) " yet. ")
          [:button.mlb-reset
            {:on-click #(dis/dispatch! [:new-entry-toggle true])}
            "Add one?"]])
      [:div.empty-board-image]]))