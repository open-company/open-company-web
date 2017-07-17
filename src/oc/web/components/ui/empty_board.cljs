(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]))

(rum/defcs empty-board < rum/reactive
                         (drv/drv :board-data)
  [s]
  (let [board-name (:name (drv/react s :board-data))]
    [:div.empty-board.group
      [:div.empty-board-headline
        (str "You don’t have any updates in " board-name " yet. ")
        [:button.mlb-reset
          {:on-click #(dis/dispatch! [:add-topic-show true])}
          "Add one?"]]
      [:div.empty-board-image]]))