(ns oc.web.components.pricing
  "Pricing page component, this is copied into oc.pages/pricing and
   every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.chat :as chat]
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

(rum/defcs pricing < {:did-mount (fn [s]
                      (when (exists? (.-OCWebSetupMarketingSiteJS js/window))
                        (js/OCWebSetupMarketingSiteJS))
                      s)}
  [s]
  [:div
    [:div.pricing-wrap
      {:id "wrap"} ; <!-- used to push footer to the bottom -->

      (site-header)
      (site-mobile-menu)
      (login-overlays-handler)

      [:div.main.pricing
        [:section.pricing-header.annual.up-to-25

          [:h1.pricing-headline
            "Pick a plan"]

          [:div.pricing-subheadline
            "Get started for free."]

          [:div.pricing-toggle-line.monthly
            [:span.pricing-toggle-pre "Pay annually (20% off)"]
            [:div.pricing-toggle
              [:span.pricing-toggle-dot]]
            [:span.pricing-toggle-post.post-monthly
              "Pay monthly"]
            [:span.pricing-toggle-post.post-yearly
              "Pay yearly"]]

          [:div.pricing-three-columns.group
            ;; Free
            [:div.pricing-column.free-column
              [:h2.tear-title
                "Free"]
              [:h3.tear-price
                "$0"]
              [:div.tear-subtitle
                [:span.bold
                  "Free for small teams"]
                " up to 10 users."]
              [:a.tear-start-bt
                {:href "/sign-up"}
                "Start for free"]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "500mb storage"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                {:data-toggle "tooltip"
                 :data-placement "top"
                 :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                 :title "Browse and search posts from the previous 3 months"}
                [:span "3 months history"]]]
            ;; Team
            [:div.pricing-column.team-column
              [:h2.tear-title
                "Standard"]
              [:div.tear-price-select-container
                [:button.mlb-reset.tear-price-select
                  "Up to 25 users"]
                [:div.tear-price-select-values
                  [:div.tear-price-select-value
                    {:data-value "25"}
                    "Up to 25 users"]
                  [:div.tear-price-select-value
                    {:data-value "100"}
                    "Up to 100 users"]
                  [:div.tear-price-select-value
                    {:data-value "250"}
                    "Up to 250 users"]]]
              [:h3.tear-price
                [:span.monthly.up-to-25
                  "$45"]
                [:span.monthly.up-to-100
                  "$85"]
                [:span.monthly.up-to-250
                  "$185"]
                [:span.annual.up-to-25
                  "$36"]
                [:span.annual.up-to-100
                  "$68"]
                [:span.annual.up-to-250
                  "$148"]]
              [:div.tear-subtitle
                [:span.billed-annually
                  "Per month, billed annually"]
                [:span.billed-monthly
                  "Per month, billed monthly"]]
              [:a.tear-start-bt
                {:href "/sign-up"}
                "Try free for 14 days"]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "Unlimited storage"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "Unlimited history"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "Priority support"]]]
            ;; Enterprise
            [:div.pricing-column.enterprise-column
              [:h2.tear-title
                "Enterprise"]
              [:div.tear-price]
              [:div.tear-subtitle
                "A team of more than 250? Let's create a custom plan."]
              [:a.tear-start-bt
                {:href "#"
                 :on-click #(chat/chat-click 43235)}
                "Contact Us"]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span
                  "Everything in the "
                  [:span.heavy "Team plan"]
                  ", plus:"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                {:data-toggle "tooltip"
                 :data-placement "top"
                 :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                 :title "Make sure everyone sees what matters most."}
                [:span "Carrot AI"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "On premise option"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "Uptime SLA"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "Premium support"]]]]]

      [:section.pricing-faq
        [:h2.faq-header
          "Frequently asked questions."]

        [:div.faq-row
          [:div.faq-row-question
            "Can I use Carrot for free?"]
          [:div.faq-row-answer
            (str
             "Absolutely! Carrot is free for teams of up to 10 people. The Free plan "
             "has a storage limit, but includes all of the features of the Standard plan. "
             "It’s fast to sign up, and no credit card is required.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the Standard plan different from the Free plan?"]
          [:div.faq-row-answer
            (str
             "The Standard plan includes unlimited storage and history. "
             "Choose the size of your team, and whether you’d like to pay "
             "monthly or annually. Annual plans paid in advance provide a 20% discount.")]]

        [:div.faq-row
          [:div.faq-row-question
            "Do I need a credit card to sign up?"]
          [:div.faq-row-answer
            "No! You can use Carrot right away without a credit card."]]

        [:div.faq-row
          [:div.faq-row-question
            "What happens if we go over our storage limit in the Free plan?"]
          [:div.faq-row-answer
            (str
             "You can still read, write, edit, and organize existing content, but you won't be able "
             "to add new attachments and videos.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the payment being processed?"]
          [:div.faq-row-answer
            (str
             "We use Stripe to process your payment. It's the same payment provider used in "
             "products such as Slack, Pinterest, and Lyft. We do not handle your credit card "
             "information directly.")]]

        [:div.faq-row
          [:div.faq-row-question
            "Are discounts available for nonprofits?"]
          [:div.faq-row-answer
            "Yes! We offer eligible nonprofit organizations a 50% discount. "
            [:a
              {:href "#"
               :on-click #(chat/chat-click 43239)}
              "Contact us"]
            " to see if your organization is eligible."]]

        [:div.faq-row
          [:div.faq-row-question
            "Still have more questions?"]
          [:div.faq-row-answer
            [:a.chat-with-us
              {:href "#"
               :on-click #(chat/chat-click 43234)}
              "Get in touch with us"]]]]

        shared-misc/testimonials-section

        shared-misc/keep-aligned-bottom
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])