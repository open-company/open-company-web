(ns oc.web.components.ui.loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc shaky-carrot < rum/static
  [data]
  [:div.oc-loading
   {:class (utils/class-set {:active (:loading data)})}
   [:div.oc-loading-inner
    [:div.oc-loading-heart]
    [:div.oc-loading-body]]])

(rum/defc ghost-screen < rum/static
  [data]
  [:div.oc-loading-ghost-screen
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
     [:div.oc-loading-feed-title]
     [:div.oc-loading-feed
      {:class (when (= (:current-board-slug data) "replies") "collapsed")}]
     [:div.oc-loading-tabbar
      [:div.oc-loading-tabbar-item]
      [:div.oc-loading-tabbar-item]
      [:div.oc-loading-tabbar-item]
      [:div.oc-loading-tabbar-item]]]]])

(rum/defc loading < rum/static
  [{:keys [current-org-slug current-board-slug jwt loading]}]
  (if (and current-org-slug
           jwt)
    (ghost-screen {:loading loading :current-board-slug current-board-slug})
    (shaky-carrot {:loading loading})))