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
          "Pricing"]

        [:div.pricing-subheadline
          "Simple plans to stay aligned."
          [:br]
          "Get started for free."]

        [:div.pricing-three-columns.group
          ;; Free
          [:div.pricing-column.free-column
            [:h2.tear-title
              "Free"]
            [:h3.tear-price
              "$0"]
            [:h5.tear-period
              "/month"]
            [:div.tear-subtitle
              "Free for small teams up to 10 users."]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Start Free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Attachments up to 20MB"}
              [:span "20MB upload max"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "1TB storage"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Browse and search posts from the previous 6 months"}
              [:span "6 months history"]]]
          ;; Team
          [:div.pricing-column.team-column
            [:h2.tear-title
              "Team"]
            [:h3.tear-price
              "$65"]
            [:h5.tear-period
              "/month"]
            [:div.tear-subtitle
              "Includes 15 users, additional users are $4 /mo."]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Start Free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Attachments up to 100MB"}
              [:span "100MB file upload"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Unlimited storage"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "No limits on history maintained in Carrot"}
              [:span "Unlimited history"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Support via chat and email"}
              [:span "Priority support"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Don’t feel like writing? Record a quick video"}
              [:span "Video updates"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Schedule and assign weekly and monthly updates to build consistency."}
              [:span "Recurring updates"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Add private sections for invited team members only"}
              [:span "Advanced permissions"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Make sure you’re being heard, and know who’s seen your post"}
              [:span "Who saw that"]]]
          ;; Enterprise
          [:div.pricing-column.enterprise-column
            [:h2.tear-title
              "Enterprise"]
            [:div.tear-price]
            [:div.tear-subtitle
              "A team of more than 100? Let's create a custom plan."]
            [:a.tear-start-bt
              {:href "#"
               :onclick "drift.api.startInteraction({ interactionId: 43235 }); return false;"}
              "Contact Us"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span
                "Includes everything in the "
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
             "Absolutely! You can use Carrot for free with teams of up to 10 people. "
             "The storage limit is 1TB and there’s a maximum upload of 20mbs. When you "
             "sign up for the free plan you’ll get to try a fully-featured Team plan "
             "(incl in-app video recording, recurring updates and advanced permissions) for "
             "30 days. It’s fast to sign up, and no credit card is required.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the Team plan different from the Free plan?"]
          [:div.faq-row-answer
            "With the Team plan you get unlimited  upload file size and storage, and premium features, like:"
            [:ul
              [:li [:span.heavy "In-app video recording:"] " Don’t feel like writing? Record a quick video instead."]
              [:li [:span.heavy "Recurring updates:"] " Schedule weekly and monthly updates to build consistency."]
              [:li [:span.heavy "Advanced permissions:"] " Add private sections for invited members only."]
              [:li [:span.heavy "Who saw that:"] " Make sure you’re being heard, and know who’s seen your post."]]]]

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
             "You can still read, write, edit, and organize existing content, "
             "but you won't be able to add new attachments and videos.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is pricing calculated for the Team plan?"]
          [:div.faq-row-answer
            (str
             "When you upgrade to the Team plan, you will be charged a base fee of $65 per "
             "month that includes up to 15 team members. Beyond the initial 15 members, you "
             "will be charged a fee of $4 per additional member per month.")
            [:br]
            [:br]
            (str
             "For example, if you have 25 team members, you would pay $65 for the first 15 "
             "members, and $4 for each of the additional 10 members, for a total of $105 per month.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the payment being processed?"]
          [:div.faq-row-answer
            (str
             "We use Stripe to process your payment. It's the same payment provider used in products "
             "such as Slack, Pinterest, and Lyft. We do not handle your credit card information directly.")]]

        [:div.faq-row
          [:div.faq-row-question
            "Are discounts available for nonprofits?"]
          [:div.faq-row-answer
            "Yes! We offer eligible nonprofit organizations a 50% discount. "
            [:a
              {:href "#"
               :onclick "drift.api.startInteraction({ interactionId: 43239 }); return false;"}
              "Contact us"]
            " to see if your organization is eligible."]]

        [:div.faq-row
          [:div.faq-row-question
            "Still have more questions?"]
          [:div.faq-row-answer
            [:a.chat-with-us
              {:href "#"
               :onclick "drift.api.startInteraction({ interactionId: 43234 }); return false;"}
              "Get in touch with us"]]]]

        shared-misc/testimonials-section
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])