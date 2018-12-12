(ns oc.web.components.ui.activity-removed
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]))

(defn- close-clicked []
  (router/nav! (oc-urls/all-posts)))

(rum/defc activity-removed < rum/static
                      no-scroll-mixin
  []
  [:div.activity-removed-container

    [:div.activity-removed

      [:button.settings-modal-close.mlb-reset
        {:on-click #(close-clicked)}]

      [:div.activity-removed-mobile-header
        [:button.mlb-reset.activity-removed-mobile-close
          {:on-click #(close-clicked)}]
        [:div.activity-removed-mobile-header-logo]]

      [:div.activity-removed-icon]

      [:div.activity-removed-title
        "Post not available"]

      [:div.activity-removed-description
        "Looks like you don’t have access to the post or it doesn’t exist."]

      [:button.mlb-reset.activity-removed-button
        {:on-click #(close-clicked)}
        "Return to all posts"]]])