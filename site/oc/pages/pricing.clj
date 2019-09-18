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
          "Organized communication keeps remote teams in sync"]

        shared/pricing-table
        
        shared/pricing-table-footer]

      (shared/dashed-string 1)      

      [:section.pricing-apps.big-web-tablet-only
        [:div.pricing-apps-block.group
          [:div.pricing-apps-app-block
            [:div.pricing-apps-app-header
              "Web app"]
            [:img.pricing-apps-app-screenshot
              {:src (shared/cdn "/img/ML/pricing_apps_signup.png")
               :srcSet (str (shared/cdn "/img/ML/pricing_apps_signup@2x.png") " 2x")}]
            [:div.pricing-apps-app-links
              [:a
                {:href "/sign-up"}
                "Start your free trial"]]]
          [:div.pricing-apps-separator]
          [:div.pricing-apps-app-block
            [:div.pricing-apps-app-header
              "Desktop app"]
            [:img.pricing-apps-app-screenshot
              {:src (shared/cdn "/img/ML/pricing_apps_desktop.png")
               :srcSet (str (shared/cdn "/img/ML/pricing_apps_desktop@2x.png") " 2x")}]
            [:div.pricing-apps-app-links
              [:a.mac-icon
                {:href "/apps/mac"}
                "Mac"]
              [:a.win-icon
                {:href "/apps/windows"}
                "Windows"]]]
          [:div.pricing-apps-separator]
          [:div.pricing-apps-app-block
            [:div.pricing-apps-app-header
              "Mobile app"]
            [:img.pricing-apps-app-screenshot
              {:src (shared/cdn "/img/ML/pricing_apps_mobile.png")
               :srcSet (str (shared/cdn "/img/ML/pricing_apps_mobile@2x.png") " 2x")}]
            [:div.pricing-apps-app-links
              [:a
                {:href "/apps/ios"}
                "iOS"]
              " & "
              [:a
                {:href "/apps/android"}
                "Android"]]]]]

      [:section.pricing-apps.mobile-only
        [:div.pricing-apps-block.group
          [:div.pricing-apps-app-block
            [:div.pricing-apps-app-header
              "Get the mobile app"]
            [:div.pricing-apps-app-links
              [:a
                {:href "/apps/detect"}
                "Download from the App store"]]
            [:img.pricing-apps-mobile-screenshot
              {:src (shared/cdn "/img/ML/pricing_apps_mobile_mobile.png")
               :srcSet (str
                        (shared/cdn "/img/ML/pricing_apps_mobile_mobile@2x.png") " 2x, "
                        (shared/cdn "/img/ML/pricing_apps_mobile_mobile@3x.png") " 3x, "
                        (shared/cdn "/img/ML/pricing_apps_mobile_mobile@4x.png") " 4x")}]]]]

      (shared/dashed-string 2)

      (shared/testimonial-block :bank-novo)

      (shared/dashed-string 3)

      [:section.pricing-faq
        [:div.pricing-faq-block
          [:h1.pricing-faq-header
            "Frequently asked questions."]
          [:div.pricing-faq-question-block
            [:div.pricing-faq-question
              "Do I need a credit card to sign up?"]
            [:div.pricing-faq-answer
              "No! You can use Carrot right away without a credit card."]]
          [:div.pricing-faq-question-block
            [:div.pricing-faq-question
              "How is the payment being processed?"]
            [:div.pricing-faq-answer
              (str
               "We use Stripe to process your payment. It's the same payment "
               "provider used in products such as Slack, Pinterest, and Lyft. "
               "We do not handle your credit card information directly.")]]
          [:div.pricing-faq-question-block
            [:div.pricing-faq-question
              "Are discounts available for nonprofits?"]
            [:div.pricing-faq-answer
              (str
               "Yes! We offer eligible nonprofit organizations a 50% discount. "
               "Contact us to see if your organization is eligible.")]]
          [:div.pricing-faq-question-block
            [:div.pricing-faq-question
              "Still have questions?"]
            [:div.pricing-faq-answer
              [:a.chat-with-us
                {:class "intercom-chat-link"
                 :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
                "Chat with us about Carrot"]]]]]

      (shared/dashed-string 4)

      shared/testimonials-section-old

      (shared/dashed-string 5 "big-web-tablet-only")

      shared/pricing-footer

    ]])