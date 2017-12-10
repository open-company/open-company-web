(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]))

(rum/defcs empty-board < rum/reactive
                         (drv/drv :board-data)
  [s]
  (let [board-data (drv/react s :board-data)]
    [:div.empty-board.group
      [:div.empty-board-headline
        (str "There aren’t any posts in " (:name board-data) " yet. ")
        (when-not (:read-only board-data)
          [:button.mlb-reset
            {:on-click #(dis/dispatch! [:entry-edit {:board-slug (:slug board-data) :board-name (:name board-data)}])}
            "Add one?"])]
      [:div.empty-board-image]]))