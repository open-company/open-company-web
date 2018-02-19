(ns oc.web.components.ui.loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc loading < rum/static
  [data]
  [:div.oc-loading
    {:class (utils/class-set {:active (:loading data)
                              :setup-screen (:nux data)})}
    [:div.oc-loading-inner
      [:div.oc-loading-heart]
      [:div.oc-loading-body]]
    [:div.setup-cta
      "Hang tight, we’re just getting set up…"]])