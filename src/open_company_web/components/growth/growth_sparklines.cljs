(ns open-company-web.components.growth.growth-sparklines
  (:require [rum.core :as rum]
            [open-company-web.dispatcher :as dis]))

(rum/defc growth-sparkline < {:did-mount (fn [s] (.tooltip (js/$ "[data-toggle=tooltip]")) s)}
  [{:keys [metric-data metric-metadata]}]
  [:div.growth-sparkline.group
    {:id (str "growth-sparkline-" (:slug metric-metadata))}
    [:div.left-box
      [:div.inline (:name metric-metadata)]
      [:div.inline (:description metric-metadata)]]
    [:div.center-box
      [:div "~~~~~"]]
    [:div.actions.right
      [:button.btn-reset
        {:data-placement "right"
         :data-container "body"
         :data-toggle "tooltip"
         :title "Edit chart"}
        [:i.fa.fa-pencil]]
      [:button.btn-reset
        {:data-placement "right"
         :data-container "body"
         :data-toggle "tooltip"
         :title "Remove chart"}
        [:i.fa.fa-times]]]])

(rum/defc growth-sparklines
  [{:keys [growth-data growth-metrics growth-metric-slugs] :as data}]
  [:div.growth-sparklines
    {:class (when (= (dis/foce-section-key) :growth) "editing")}
    (for [slug growth-metric-slugs]
      (rum/with-key
        (growth-sparkline {:metric-data (filter #(= (:slug %) slug) growth-data)
                           :metric-metadata (get growth-metrics slug)})
        (name slug)))])