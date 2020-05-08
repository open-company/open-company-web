(ns oc.web.components.ui.loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc loading < rum/static
  [data]
  [:div.oc-loading
    {:class (utils/class-set {:active (:loading data)})}
    [:div.oc-loading-inner
      "Wut!"]])