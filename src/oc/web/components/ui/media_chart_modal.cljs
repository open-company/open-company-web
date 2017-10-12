(ns oc.web.components.ui.media-chart-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.mixins :refer (first-render-mixin)]))

(defn dismiss-modal [s]
  (dis/dispatch! [:input [:media-input :media-chart] :dismiss]))

(defn close-clicked [s & [no-dismiss]]
  (reset! (::dismiss s) true)
  (when-not no-dismiss
    (utils/after 180 #(dismiss-modal s))))

(rum/defcs media-chart-modal < rum/reactive
                               ;; Locals
                               (rum/local false ::dismiss)
                               (rum/local "" ::chart-url)
                               ;; Derivatives
                               (drv/drv :current-user-data)
                               ;; Mixins
                               first-render-mixin
                               {:did-mount (fn [s]
                                            (utils/after 100
                                             #(when-let [input-field (rum/ref-node s "chart-input")]
                                                (.focus input-field)))
                                            s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)]
    [:div.media-chart-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
       :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
          {:on-click #(close-clicked s)}]
        [:div.media-chart-modal
          [:div.media-chart-modal-header.group
            [:div.title "Adding a chart"]]
          [:div.media-chart-modal-content
            [:div.media-chart-modal-content-description
              [:div.media-chart-modal-content-title "You can insert any chart from Google Sheets by following these steps:"]
              [:div.media-chart-modal-content-ps
                [:div.content-description-p "1. Open the spreadsheet in Google Sheets"]
                [:div.content-description-p "2. Click the chart you’d like to insert"]
                [:div.content-description-p "3. In the top right of the chart, click the ellipse and Publish Chart"]
                [:div.content-description-p "4. Click the Publish button and copy and paste the link URL provided"]]]
            [:div.content-title "CHART LINK"]
            [:input.media-chart-modal-input
              {:type "text"
               :ref "chart-input"
               :value @(::chart-url s)
               :on-change #(reset! (::chart-url s) (.. % -target -value))
               :placeholder "Link from Google Sheet"}]]
          [:div.media-chart-modal-buttons.group
            [:button.mlb-reset.mlb-default
              {:on-click #(when (utils/valid-google-chart-url? @(::chart-url s))
                            (let [chart-data (utils/clean-google-chart-url @(::chart-url s))]
                              (dis/dispatch! [:input [:media-input :media-chart] chart-data]))
                            (close-clicked s true))
               :disabled (not (utils/valid-google-chart-url? @(::chart-url s)))}
              "Add"]
            [:button.mlb-reset.mlb-link-black
              {:on-click #(close-clicked s)}
              "Cancel"]]]]]))