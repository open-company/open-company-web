(ns oc.web.components.ui.collect-chart-popover
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defcs collect-chart-popover < rum/static
                                   rum/reactive
                                   (drv/drv :foce-data)
                                   (rum/local nil ::chart-url)
                                   {:did-mount (fn [s]
                                                 (let [topic-data @(drv/get-ref s :foce-data)]
                                                   (reset! (::chart-url s) (:chart-url topic-data)))
                                                 s)}
  [s]
  (let [topic-data (drv/react s :foce-data)
        props (first (:rum/args s))
        hide-popover-cb (:hide-popover-cb props)
        collect-chart-cb (:collect-chart-cb props)]
    [:div.oc-popover-container-internal
      [:div.collect-chart-popover.oc-popover
        [:button.btn-reset.close-button
          {:on-click #(hide-popover-cb %)}
          [:i.fa.fa-times]]
        [:h3.title "Include a chart from "
          [:b "Google Sheets"]
          ":"]
        [:ol
          [:li "Open the spreadsheet in Google Sheets"]
          [:li "Click the desired chart"]
          [:li "In the top right, click the down arrow and " [:b "Publish Chart"]]
          [:li "Click the " [:b "Publish"] " button and paste the link URL provided"]]
        [:hr.divider-line]
        [:div
          [:label.left "Chart URL"]
          [:input.right
            {:type "text"
             :style {:width "360px"}
             :value @(::chart-url s)
             :placeholder "https://docs.google.com/spreadsheets/d/unique-url?oid=unique"
             :on-change #(reset! (::chart-url s) (.. % -target -value))}]]
        [:div.right
          [:button.btn-reset.btn-outline
            {:on-click #(hide-popover-cb %)}
            "CANCEL"]
          [:button.btn-reset.btn-solid
            {:disabled (not (utils/check-google-chart-url @(::chart-url s)))
             :on-click (fn [e]
                         (dis/dispatch! [:foce-input [:chart-url (utils/clean-google-chart-url @(::chart-url s))]])
                         (when (fn? collect-chart-cb)
                           (collect-chart-cb e))
                         (hide-popover-cb e))}
            "ADD CHART"]]]]))