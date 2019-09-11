(ns oc.pages.pricing
  (:require [oc.pages.shared :as shared]))

(defn pricing
  "Pricing page. This is a copy of oc.web.components.pricing and every change here should be reflected there and vice versa."
  [options]
  [:div.pricing-wrap
    {:id "wrap"}
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
              "Perfect for any size team"]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Start for free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Unlimited users"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Unlimited posts"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "2 GB storage"]]]
          ;; Premium
          [:div.pricing-column.team-column.annual
            [:h2.tear-title
              "Premium"]
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
                "Per user, per month, billed annually"]
              [:span.billed-monthly
                "Per user, per month"]]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Try free for 14 days"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span
                "Everything in "
                [:span.heavy "Free"]
                ", plus:"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Advanced permissions"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Follow-ups"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Analytics"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Post scheduling (soon)"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Guest users (soon)"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Premium support"]]]
          ;; Enterprise
          [:div.pricing-column.enterprise-column
            [:h2.tear-title
              "Enterprise"]
            [:div.tear-price]
            [:div.tear-subtitle
              "Tailored for larger organizations"]
            [:a.tear-start-bt
              {:class "intercom-chat-link"
               :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
              "Chat with Us"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span
                "Everything in "
                [:span.heavy "Premium"]
                ", plus:"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Ideal for teams of 250+"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Custom pricing plans"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Enterprise support"]]]]]

      [:section.pricing-faq
        [:h2.faq-header
          "Frequently asked questions."]

        [:div.faq-row
          [:div.faq-row-question
            "Can I use Carrot for free?"]
          [:div.faq-row-answer
            (str
             "Absolutely! Carrot can be used for free. The Free plan "
             "has a storage limit, and doesn't include all of the premium features. "
             "It’s fast to sign up, and no credit card is required.")]]

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

      shared/testimonials-section

      shared/keep-aligned-bottom

    ]])