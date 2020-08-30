(ns oc.web.components.ui.loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc loading < rum/static
  [data]
  [:div.oc-loading
    {:class (utils/class-set {:active (:loading data)})}
    [:div.oc-loading-navbar]
    [:div.oc-loading-page
      [:div.oc-loading-navigation-sidebar
        [:div.oc-loading-navigation-sidebar-row]
        [:div.oc-loading-navigation-sidebar-row]
        [:div.oc-loading-navigation-sidebar-row]
        [:div.oc-loading-navigation-sidebar-row]
        [:div.oc-loading-navigation-sidebar-row]]
      [:div.oc-loading-dashboard
        [:div.oc-loading-qp]
        [:div.oc-loading-feed]]]])