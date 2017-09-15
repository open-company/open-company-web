(ns oc.web.components.ui.media-chart-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn dismiss-modal [s]
  (let [dispatch-input-key (first (:rum/args s))]
    (dis/dispatch! [:input [dispatch-input-key :media-chart] false])))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal s)))

(rum/defcs media-chart-modal < (rum/local false ::first-render-done)
                               (rum/local false ::dismiss)
                               (rum/local "" ::chart-url)
                               rum/reactive
                               (drv/drv :current-user-data)
                               {:after-render (fn [s]
                                                (when (not @(::first-render-done s))
                                                  (reset! (::first-render-done s) true))
                                                s)
                                :did-mount (fn [s]
                                            (utils/after 100 #(.focus (sel1 [:input.media-chart-modal-input])))
                                            s)}
  [s dispatch-input-key]
  (let [current-user-data (drv/react s :current-user-data)]
    [:div.media-chart-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
       :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      [:div.media-chart-modal
        [:div.media-chart-modal-header.group
          (user-avatar-image current-user-data)
          [:div.title "Adding a chart"]]
        [:div.media-chart-modal-content
          [:div.media-chart-modal-content-description
            [:div.media-chart-modal-content-title "You can insert any chart from Google Sheets by following these steps:"]
            [:div.media-chart-modal-content-ps
              [:div.content-description-p "1. Open the spreadsheet in Google Sheets"]
              [:div.content-description-p "2. Click the chart you’d like to insert"]
              [:div.content-description-p "3. In the top right of the chart, click the down arrow and Publish Chart"]
              [:div.content-description-p "4. Click the Publish button and copy and paste the link URL provided"]]]
          [:div.content-title "CHART LINK"]
          [:input.media-chart-modal-input
            {:type "text"
             :value @(::chart-url s)
             :on-change #(reset! (::chart-url s) (.. % -target -value))
             :placeholder "Link from Google Sheet"}]]
        [:div.media-chart-modal-buttons.group
          [:button.mlb-reset.mlb-default
            {:on-click #(when (utils/valid-google-chart-url? @(::chart-url s))
                          (let [chart-data (utils/clean-google-chart-url @(::chart-url s))]
                            (dis/dispatch! [:input [dispatch-input-key :temp-chart] chart-data]))
                          (close-clicked s))
             :disabled (not (utils/valid-google-chart-url? @(::chart-url s)))}
            "Add"]
          [:button.mlb-reset.mlb-link-black
            {:on-click #(close-clicked s)}
            "Cancel"]]]]))