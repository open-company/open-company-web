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
                      (when (exists? (.-OCWebSetupStaticPagesJS js/window))
                        (js/OCWebSetupStaticPagesJS))
                      s)}
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
            "It’s free to get started"]

          [:div.pricing-subheadline
            "Carrot keeps fast-growing and remote teams focused on what matters."]

          [:div.pricing-three-columns.group
            ;; Free
            [:div.pricing-column.free-column
              [:h2.tear-title
                "Free"]
              [:h3.tear-price
                "$0"]
              [:div.tear-subtitle
                "Free for small teams up to 10 users"]
              [:a.tear-start-bt
                {:href "/sign-up"}
                "Start for free"]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "1gb storage"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "Access 3 months recent history"]]]
            ;; Team
            [:div.pricing-column.team-column.annual
              [:h2.tear-title
                "Standard"]
              [:div.pricing-toggle-line
                [:span.pricing-toggle-annual
                  "Annual (20% off)"]
                [:div.pricing-toggle
                  [:span.pricing-toggle-dot]]
                [:span.pricing-toggle-monthly
                  "Monthly"]]
              [:h3.tear-price
                [:span.annual
                  "$3.20"]
                [:span.monthly
                  "$4"]]
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
                {:class "intercom-chat-link"
                 :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
                "Contact Us"]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span
                  "Everything in the "
                  [:span.heavy "Team plan"]
                  ", plus:"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
                [:span "Custom plans"]]
              [:div.tear-feature-separator]
              [:div.tear-feature
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
              {:class "intercom-chat-link"
               :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
              "Contact us"]
            " to see if your organization is eligible."]]

        [:div.faq-row
          [:div.faq-row-question
            "Are custom plans available?"]
          [:div.faq-row-answer
            (str
             "Absolutely! Our Enterprise plan is 100% flexible and priced based on your "
             "unique needs. Please contact us to discuss further details.")]]

        [:div.faq-row
          [:div.faq-row-question
            "Still have more questions?"]
          [:div.faq-row-answer
            [:a.chat-with-us
              {:class "intercom-chat-link"
               :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
              "Get in touch with us"]]]]

        shared-misc/testimonials-section

        shared-misc/keep-aligned-bottom
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])