(ns oc.web.components.ui.activity-removed
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defcs activity-removed < rum/static
  [s]
  [:div.activity-removed-container
    [:div.activity-removed-wrapper
      [:div.activity-removed-left
        [:div.activity-removed-logo]
        [:div.activity-removed-box]]
      [:div.activity-removed-right
        [:div.activity-removed-right-content
          [:div.info-icon]
          [:div.content-title
            "Post Not Available"]
          [:div.content-description
            "Looks like you don't have access to the post or it doesn't exist."]
          [:button.mlb-reset.go-to-homepage-btn
            {:aria-label "Homepage"
             :on-click #(router/nav! (utils/your-digest-url))}
            "Go to homepage"]]]]])