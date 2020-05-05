(ns oc.web.components.ui.loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc loading < rum/static
  [data]
  [:div.oc-loading
    {:class (utils/class-set {:active (:loading data)})}
    [:div.oc-loading-inner
      "WUT!"]
    [:div.loading-drop
      {:class (str "drop-" (int (rand 100)))}]
    [:div.loading-drop
      {:class (str "drop-" (int (rand 100)))}]
    [:div.loading-drop
      {:class (str "drop-" (int (rand 100)))}]])