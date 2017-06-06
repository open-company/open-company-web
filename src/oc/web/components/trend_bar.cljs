(ns oc.web.components.trend-bar
  (:require [rum.core :as rum]))

(rum/defcs trend-bar < rum/static
  [s org-name]
  [:div.trend-bar.group
    [:button.mlb-reset.expand-bt
      "Expand"]
    [:div.trending-org
      [:button.mlb-reset
        (str "Trending in " org-name)]]])