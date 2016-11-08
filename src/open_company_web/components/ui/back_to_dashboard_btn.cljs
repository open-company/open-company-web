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
                                  {:did-mount (fn [s]
                                                (when-not (utils/is-test-env?)
                                                  (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                                s)}
  [{:keys [button-cta click-cb button-offset-left]
    :or {button-cta "BACK TO DASHBOARD"
         click-cb   btn-clicked
         button-offset-left (- (/ (.-clientWidth (.-body js/document)) 2) 100)}}]
  (assert (fn? click-cb) "back-to-dashboard callback not fn?")
  (let [button-cta (or button-cta "BACK TO DASHBOARD")]
    [:div.back-to-dashboard-row.center
     [:button.back-to-dashboard.btn-reset.btn-outline
      {:on-click #(do (.preventDefault %) (click-cb))
       :data-placement "left"
       :data-container "body"
       :data-toggle "tooltip"
       :title button-cta
       :style {:margin-left (str button-offset-left "px")}}
      ; (str "← " button-cta)
      (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.8)" :size 24 :stroke 4 :accent-color "rgba(78, 90, 107, 1.0)"})]]))