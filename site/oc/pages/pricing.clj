(ns oc.pages.pricing
  (:require [oc.pages.shared :as shared]
            [environ.core :refer (env)]))

(defn pricing
  "Pricing page."
  [options]
  [:div.pricing-wrap.group
    {:id "wrap"}
    [:div.main.pricing

      (when (= (env :covid-banner) "true")
        [:section.pricing-covid-letter
          [:div.covid-letter-box
            [:h4 "Open source and free"]

            [:p
              "Across the world, more and more companies are being forced to explore remote team communication because of the "
              [:b "COVID-19"]
              " virus outbreak."]
            [:p
              "Of course, we admire all of the companies and people who are in a position to help, especially "
              [:b "health care workers"]
              " on the front lines. But, as a software company, what can "
              [:i "we"]
              " do to help? Admittedly, "
              "not much, and that makes us a little crazy."]
            [:p
              "No matter, we’re all in this together and we’ll do what we can."]
            [:p
              "Beginning immediately, new companies that sign up to use Carrot for remote team communication can use it "
              [:b "free for unlimited users"]
              " throughout the coronavirus crisis."]
            [:p
              "We do not know how long it will last, so it will be "
              [:b "free indefinitely"]
              " until things settle down. After that, we will return to our normal pricing, detailed below."]
            [:p
              [:b "Stay healthy,"]
              [:br]
              [:i "The Carrot team"]]]])

      [:section.pricing-header

        [:h1.pricing-headline
          shared/pricing-headline]

        shared/pricing-table
        
        shared/pricing-table-footer

        shared/pricing-chat
        ]

      ;; [:section.video
      ;;   [:h4 "Like what you see?"]
      ;;   [:div.main-animation-container
      ;;     [:img.main-animation
      ;;       {:src (shared/cdn "/img/ML/homepage_screenshot.png")
      ;;        :alt "Carrot"
      ;;        :srcSet (str
      ;;                 (shared/cdn "/img/ML/homepage_screenshot@2x.png") " 2x, "
      ;;                 (shared/cdn "/img/ML/homepage_screenshot@3x.png") " 3x")}]]]
      
      ;; [:section.video
      ;;  shared/close-communication-gaps]

      (shared/dashed-string 1 "darker-bg")

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
                "Try Carrot for free"]]]
          [:div.pricing-apps-separator]
          [:div.pricing-apps-app-block
            [:div.pricing-apps-app-header
              "Desktop app"]
            [:img.pricing-apps-app-screenshot
              {:src (shared/cdn "/img/ML/pricing_apps_desktop.png")
               :srcSet (str (shared/cdn "/img/ML/pricing_apps_desktop@2x.png") " 2x")}]
            [:div.pricing-apps-app-links.mac-and-windows
              [:a.mac-icon
                {:href "/apps/mac"}
                "Mac"
                [:span.beta-app-label "BETA"]]
              [:a.win-icon
                {:href "/apps/windows"}
                "Windows"
                [:span.beta-app-label "BETA"]]]]
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
                [:span.app-label "iOS"]]
              " & "
              [:a
                {:href "/apps/android"}
                [:span.app-label "Android"]]
              [:span.beta-app-label "BETA"]]]]]

      [:section.pricing-apps.mobile-only
        [:div.pricing-apps-block.group
          [:div.pricing-apps-app-block
            [:div.pricing-apps-app-header
              "Get the mobile app"]
            [:div.pricing-apps-app-links
              [:a
                {:href "/apps/detect"}
                [:span.app-label "Download the app"]
                [:span.beta-app-label "BETA"]]]
            [:img.pricing-apps-mobile-screenshot
              {:src (shared/cdn "/img/ML/pricing_apps_mobile_mobile.png")
               :srcSet (str
                        (shared/cdn "/img/ML/pricing_apps_mobile_mobile@2x.png") " 2x, "
                        (shared/cdn "/img/ML/pricing_apps_mobile_mobile@3x.png") " 3x, "
                        (shared/cdn "/img/ML/pricing_apps_mobile_mobile@4x.png") " 4x")}]]]]

      (shared/dashed-string 2 "darker-bg")

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
                 :href "mailto:hello@carrot.io"}
                "Chat with us about Carrot"]]]]]

      (shared/dashed-string 4)

      shared/testimonials-section-old

      (shared/dashed-string 5 "big-web-tablet-only")

      shared/pricing-footer

    ]])