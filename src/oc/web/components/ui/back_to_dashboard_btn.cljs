(ns oc.web.components.ui.back-to-dashboard-btn
  (:require [rum.core :as rum]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.icon :as i]))

(defn btn-clicked []
  (.back (.-history js/window)))

(rum/defc back-to-dashboard-btn < rum/static
  [{:keys [click-cb title]
    :or {click-cb btn-clicked
         title    ""}}]
  (assert (fn? click-cb) "back-to-dashboard callback not fn?")
  [:div.back-to-dashboard-row.group
   [:h3.back-to-dashboard-title.left.mt0.mb0 title]
   [:button.back-to-dashboard.btn-reset.btn-outline.right
    {:on-click #(do (.preventDefault %) (click-cb))}
    (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.8)" :size 24 :stroke 4 :accent-color "rgba(78, 90, 107, 1.0)"})]])