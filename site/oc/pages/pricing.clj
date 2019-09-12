(ns oc.pages.pricing
  (:require [oc.pages.shared :as shared]))

(defn pricing
  "Pricing page. This is a copy of oc.web.components.pricing and every change here should be reflected there and vice versa."
  [options]
  [:div.pricing-wrap
    {:id "wrap"}
    [:div.main.pricing

      shared/pricing-table-section

      shared/dashed-string

      (shared/testimonial-block :ifttt)

      shared/dashed-string

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

      shared/dashed-string

      (shared/testimonial-block :blend-labs)

      shared/dashed-string

    ]])