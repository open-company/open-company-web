(ns open-company-web.components.ui.back-to-dashboard-btn
  (:require [rum.core :as rum]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.icon :as i]))

(defn btn-clicked []
  (.back (.-history js/window)))

(rum/defc back-to-dashboard-btn < rum/static
  [{:keys [click-cb button-offset-left]
    :or {click-cb   btn-clicked
         button-offset-left (- (/ (.-clientWidth (.-body js/document)) 2) 100)}}]
  (assert (fn? click-cb) "back-to-dashboard callback not fn?")
  [:div.back-to-dashboard-row.center
   [:button.back-to-dashboard.btn-reset.btn-outline
    {:on-click #(do (.preventDefault %) (click-cb))
     :style {:margin-left (str button-offset-left "px")}}
    (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.8)" :size 24 :stroke 4 :accent-color "rgba(78, 90, 107, 1.0)"})]])