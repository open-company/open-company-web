(ns oc.web.components.pricing
  "Pricing page component, this is copied into oc.pages/pricing and
   every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.user :as user]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs pricing
  [s]
  [:div
    [:div.pricing-wrap
      {:id "wrap"} ; <!-- used to push footer to the bottom -->

      (site-header)
      (site-mobile-menu)
      (login-overlays-handler)

      [:div.main.pricing
        [:section.pricing-header.group
          [:div.balloon.big-red]
          [:div.balloon.big-green]
          [:div.balloon.small-blue]
          [:div.balloon.big-purple]

          [:h1.pricing-headline
            "Pricing guide"]

          [:div.pricing-block.group
            [:div.pricing-block-column.free-column
              [:div.price-column-title
                "Free"]
              [:div.price-column-price
                "0"]
              [:div.price-column-description
                "Free for small teams for an unlimited period of time"]
              [:button.mlb-reset.price-button
                {}
                "Create a digest"]]

            [:div.pricing-block-column.standard-column
              [:div.price-column-title
                "Standard"]
              [:div.price-column-price
                "8"]
              [:div.price-column-description
                "Per contributor per month billed annually"]
              [:div.price-column-description.second-line
                "Or $10 billed monthly"]
              [:div.price-column-description.more-info
                "Viewers are always free"]
              [:button.mlb-reset.price-button
                {}
                "Buy standard"]]

            [:div.pricing-block-column.plus-column
              [:div.price-column-title
                "Plus"]
              [:div.price-column-price
                "12"]
              [:div.price-column-description
                "Per contributor per month billed annually"]
              [:div.price-column-description.second-line
                "Or $14 billed monthly"]
              [:div.price-column-description.more-info
                "Viewers are always free"]
              [:button.mlb-reset.price-button
                {}
                "Buy plus"]]]

        [:div.enterprise-block
          [:span.enterprise-block-title
            "Enterprise Edition"]
          [:span.enterprise-block-copy
            "Let’s create a plan that’s right for your organization."]
          [:a.enterprise-block-link
            {:href oc-urls/contact-mail-to}
            "Contact us"]]]
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])