(ns oc.web.components.pricing
  "Pricing page component, this is copied into oc.pages/pricing and
   every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.shared-misc :as shared-misc]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn track-event [event-name]
  (.trackEvent js/CarrotGA #js {:eventCategory "purchase-click"
                                :eventAction "click"
                                :eventLabel event-name}))

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
          [:h1.pricing-headline
            "Pricing guide"]

          [:div.pricing-block.group
            [:div.pricing-block-column.free-column
              [:div.price-column-title
                "Free"]
              [:div.price-column-price
                "0"]
              [:div.price-column-description
                "Free for small teams"]
              [:button.mlb-reset.price-button
                {:on-click #(track-event "Free")}
                "Create a digest"]]

            [:div.pricing-block-column.standard-column
              [:div.price-column-title
                "Standard"]
              [:div.price-column-price
                "8"]
              [:div.price-column-description
                "Per user, per month, billed annually"]
              [:div.price-column-description.second-line
                "Or $10 monthly"]
              [:button.mlb-reset.price-button
                {:on-click #(track-event "Standard")}
                "Buy standard"]]

            [:div.pricing-block-column.plus-column
              [:div.price-column-title
                "Plus"]
              [:div.price-column-price
                "12"]
              [:div.price-column-description
                "Per user, per month, billed annually"]
              [:div.price-column-description.second-line
                "Or $14 monthly"]
              [:button.mlb-reset.price-button
                {:on-click #(track-event "Plus")}
                "Buy plus"]]]

          [:div.enterprise-block
            [:span.enterprise-block-title
              "Enterprise Edition"]
            [:span.enterprise-block-copy
              "Let’s create a plan that’s right for your organization."]
            [:a.enterprise-block-link
              {:href oc-urls/contact-mail-to}
              "Contact us"]]]
        [:section.second-section
          [:h1.compare-plans
            "Compare Plans"]

          [:div.pricing-table
            [:table
              {:cellpadding "0"
               :cellspacing "0"}
              [:thead
                [:tr
                  [:th]
                  [:th
                    [:div
                      "Free"]]
                  [:th
                    [:div
                      "Standard"]]
                  [:th
                    [:div
                      "Plus"]]]]
              [:tbody
                [:tr
                  [:td
                    [:div.more-info
                      "Searchable posts"
                      [:span.more-info-icon]
                      [:div.more-info-bubble
                        [:div.more-info-title
                          "Searchable posts"]
                        [:div.more-info-desc
                          (str
                            "Lorem ipsum dolor sit amet, consectetur adipiscing "
                            "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                            "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                  [:td
                    [:div "100"]]
                  [:td
                    [:div "Unlimited"]]
                  [:td
                    [:div "Unlimited"]]]
                [:tr
                  [:td
                    [:div.more-info
                      "History kept"
                      [:span.more-info-icon]
                      [:div.more-info-bubble
                        [:div.more-info-title
                          "History kept"]
                        [:div.more-info-desc
                          (str
                            "Lorem ipsum dolor sit amet, consectetur adipiscing "
                            "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                            "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                  [:td
                    [:div "Last 12 months"]]
                  [:td
                    [:div "Unlimited"]]
                  [:td
                    [:div "Unlimited"]]]
                [:tr
                  [:td
                    [:div "File storage"]]
                  [:td
                    [:div "1 GB"]]
                  [:td
                    [:div "10 GB"]]
                  [:td
                    [:div "50 GB"]]]
                [:tr
                  [:td
                    [:div "File upload"]]
                  [:td
                    [:div "25 MB"]]
                  [:td
                    [:div "50 MB"]]
                  [:td
                    [:div "100 MB"]]]
                [:tr
                  [:td
                    [:div "Slack single sign-on"]]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div.more-info
                      "Slack sync"
                      [:span.more-info-icon]
                      [:div.more-info-bubble
                        [:div.more-info-title
                          "Slack sync"]
                        [:div.more-info-desc
                          (str
                            "Lorem ipsum dolor sit amet, consectetur adipiscing "
                            "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                            "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div.more-info
                      "Dropbox, Google Drive and other integrations"
                      [:span.more-info-icon]
                      [:div.more-info-bubble
                        [:div.more-info-title
                          "Dropbox, Google Drive and other integrations"]
                        [:div.more-info-desc
                          (str
                            "Lorem ipsum dolor sit amet, consectetur adipiscing "
                            "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                            "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                  [:td]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div.more-info
                      "OAuth with Google"
                      [:span.more-info-icon]
                      [:div.more-info-bubble
                        [:div.more-info-title
                          "OAuth with Google"]
                        [:div.more-info-desc
                          (str
                            "Lorem ipsum dolor sit amet, consectetur adipiscing "
                            "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                            "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                  [:td]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div.more-info
                      "Private and public visibility"
                      [:span.more-info-icon]
                      [:div.more-info-bubble
                        [:div.more-info-title
                          "Private and public visibility"]
                        [:div.more-info-desc
                          (str
                            "Lorem ipsum dolor sit amet, consectetur adipiscing "
                            "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                            "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                  [:td]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div.more-info
                      "Who read what"
                      [:span.more-info-icon]
                      [:div.more-info-bubble
                        [:div.more-info-title
                          "Who read what"]
                        [:div.more-info-desc
                          (str
                            "Lorem ipsum dolor sit amet, consectetur adipiscing "
                            "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                            "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                  [:td]
                  [:td
                    [:div.check]]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div "Analytics"]]
                  [:td]
                  [:td]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div "Priority support"]]
                  [:td]
                  [:td]
                  [:td
                    [:div.check]]]
                [:tr
                  [:td
                    [:div "Uptime SLA"]]
                  [:td]
                  [:td]
                  [:td
                    [:div.check]]]]]]]

        shared-misc/carrot-testimonials

        (when-not (jwt/jwt)
          shared-misc/keep-aligned)
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])