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
        [:section.pricing-header
          [:h1.pricing-headline
            "Pricing guide"]

          [:table.pricing-table.big-web-only
            [:thead
              [:tr
                [:th]
                [:th
                  [:div.tire-title
                    "Free"]
                  [:div.tire-price
                    "$0"]
                  [:button.mlb-reset.price-button
                    "Get started"]]
                [:th
                  [:div.tire-title
                    "Team"]
                  [:div.tire-price
                    "$45"
                    [:span.per-month
                      "per month"]]
                  [:button.mlb-reset.price-button
                    "Try for free"]]
                [:th
                  [:div.tire-title
                    "Enterprise"]
                  [:div.tire-price
                    "$125"
                    [:span.per-month
                      "per month"]]
                  [:button.mlb-reset.price-button
                    "Try for free"]]]]
            [:tbody
              [:tr
                [:td.pricing-description
                  "Number of users included"]
                [:td.pricing-value
                  "10"]
                [:td.pricing-value
                  "25"]
                [:td.pricing-value
                  "50"]]

              [:tr
                [:td.pricing-description
                  "Additional users"]
                [:td.pricing-value
                  "—"]
                [:td.pricing-value
                  "—"]
                [:td.pricing-value
                  "$2 per user"]]

              [:tr
                [:td.pricing-description
                  "Number of new posts"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Number of new posts"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
                [:td.pricing-value
                  "50"]
                [:td.pricing-value
                  "Unlimited"]
                [:td.pricing-value
                  "Unlimited"]]

              [:tr
                [:td.pricing-description
                  "History retained"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "History retained"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
                [:td.pricing-value
                  "6 months"]
                [:td.pricing-value
                  "Unlimited"]
                [:td.pricing-value
                  "Unlimited"]]

              [:tr
                [:td.pricing-description
                  "File upload"]
                [:td.pricing-value
                  "25 MB"]
                [:td.pricing-value
                  "Unlimited"]
                [:td.pricing-value
                  "Unlimited"]]

              [:tr
                [:td.pricing-description
                  "File storage"]
                [:td.pricing-value
                  "500 MB"]
                [:td.pricing-value
                  "5 TB"]
                [:td.pricing-value
                  "Unlimited"]]

              [:tr
                [:td.pricing-description
                  "G suite single sign-on"]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "Slack single sign-on"]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "Sync with Slack"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Sync with Slack"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "Dropbox, Google Drive and other integrations"]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "Advanced permissions"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Advanced permissions"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
                [:td.pricing-value]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "Priority support"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Priority support"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
                [:td.pricing-value]
                [:td.pricing-value
                  [:div.price-checkmark]]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "Analytics"]
                [:td.pricing-value]
                [:td.pricing-value]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "Uptime SLA"]
                [:td.pricing-value]
                [:td.pricing-value]
                [:td.pricing-value
                  [:div.price-checkmark]]]

              [:tr
                [:td.pricing-description
                  "On premise"]
                [:td.pricing-value]
                [:td.pricing-value]
                [:td.pricing-value
                  [:div.price-checkmark]]]]
            [:thead
              [:tr
                [:th]
                [:th
                  [:div.tire-title
                    "Free"]
                  [:button.mlb-reset.price-button
                    "Get started"]]
                [:th
                  [:div.tire-title
                    "Team"]
                  [:button.mlb-reset.price-button
                    "Try for free"]]
                [:th
                  [:div.tire-title
                    "Enterprise"]
                  [:button.mlb-reset.price-button
                    "Try for free"]]]]]

          [:table.pricing-table.mobile-only
            [:thead
              [:tr
                [:th
                  [:div.tire-title
                    "FREE"]
                  [:div.tire-price
                    [:span.dollar "$"]
                    "0"]
                  [:button.mlb-reset.price-button
                    "Get started"]]]]
            [:tbody
              [:tr
                [:td.pricing-description
                  "Up to 10 users"]]
              [:tr
                [:td.pricing-description
                  "50 new post cap"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "50 new post cap"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "6 months of history retained"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "6 months of history retained"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "25 MB upload limit"]]
              [:tr
                [:td.pricing-description
                  "500 MB file storage"]]
              [:tr
                [:td.pricing-description
                  "G suite single sign-on"]]
              [:tr
                [:td.pricing-description
                  "Slack single sign-on"]]
              [:tr
                [:td.pricing-description
                  "Sync with Slack"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Sync with Slack"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "Dropbox, Google Drive, and other integrations"]]]]

          [:table.pricing-table.mobile-only
            [:thead
              [:tr
                [:th
                  [:div.tire-title
                    "TEAM"]
                  [:div.tire-price
                    [:span.dollar "$"]
                    "45"
                    [:span.per-month\
                      "per month"]]
                  [:button.mlb-reset.price-button
                    "Get started"]]]]
            [:tbody
              [:tr
                [:td.pricing-description
                  "Up to 25 users"]]
              [:tr
                [:td.pricing-description
                  "Unlimited posts"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Unlimited posts"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "No history limit"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "No history limit"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "No file size upload limit"]]
              [:tr
                [:td.pricing-description
                  "5 TB file storage"]]
              [:tr
                [:td.pricing-description
                  "G suite single sign-on"]]
              [:tr
                [:td.pricing-description
                  "Slack single sign-on"]]
              [:tr
                [:td.pricing-description
                  "Sync with Slack"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Sync with Slack"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "Dropbox, Google Drive, and other integrations"]]
              [:tr
                [:td.pricing-description
                  "Advanced permissions"]]
              [:tr
                [:td.pricing-description
                  "Priority support"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Priority support"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]]]

          [:table.pricing-table.mobile-only
            [:thead
              [:tr
                [:th
                  [:div.tire-title
                    "ENTERPRISE"]
                  [:div.tire-price
                    [:span.dollar "$"]
                    "125"
                    [:span.per-month\
                      "per month"]]
                  [:button.mlb-reset.price-button
                    "Get started"]]]]
            [:tbody
              [:tr
                [:td.pricing-description
                  "Includes everthing in "
                  [:span.strong "team"]
                  " and:"]]
              [:tr
                [:td.pricing-description
                  "Up to 50 users"]]
              [:tr
                [:td.pricing-description
                  "$2 per additional user "]]
              [:tr
                [:td.pricing-description
                  "Analytics"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Analytics"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "Uptime SLA"
                  [:span.info
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Uptime SLA"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:tr
                [:td.pricing-description
                  "On premise"]]]]

          ; [:div.pricing-faq
          ;   [:h2.faq-header
          ;     "Frequently asked questions"]

          ;   [:div.faq-row
          ;     [:div.faq-question
          ;       "What are my payment options (credit card and/or invoicing)?"]
          ;     [:div.faq-response
          ;       (str
          ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
          ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
          ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
          ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
          ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
          ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
          ;   [:div.faq-row
          ;     [:div.faq-question
          ;       "My team has credits. How do we use them?"]
          ;     [:div.faq-response
          ;       (str
          ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
          ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
          ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
          ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
          ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
          ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
          ;   [:div.faq-row
          ;     [:div.faq-question
          ;       "We need to add new users to our team. How will that be billed?"]
          ;     [:div.faq-response
          ;       (str
          ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
          ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
          ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
          ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
          ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
          ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
          ;   [:div.faq-row
          ;     [:div.faq-question
          ;       "My team wasnts to cancel its subscription. How do we do that? Can we get a refund?"]
          ;     [:div.faq-response
          ;       (str
          ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
          ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
          ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
          ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
          ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
          ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
          ;   [:div.faq-row
          ;     [:div.faq-question
          ;       "What are my payment options (credit card and/or invoicing)?"]
          ;     [:div.faq-response
          ;       (str
          ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
          ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
          ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
          ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
          ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
          ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
          ;   [:div.contact-us-row
          ;     "If you have more questions, don’t hesitate to "
          ;     [:a.contact
          ;       {:href oc-urls/contact-mail-to}
          ;       "contact us"]]]
        ]

        shared-misc/testimonials-section

        (when-not (jwt/jwt)
          shared-misc/keep-aligned)
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])