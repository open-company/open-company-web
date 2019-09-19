(ns oc.pages.press-kit
  (:require [oc.pages.shared :as shared]))

(defn press-kit
  "Press kit page."
  [options]
  [:div.press-kit-wrap
    {:id "wrap"}
    [:div.main.press-kit
      shared/animation-lightbox
      [:section.cta.group

        [:h1.headline
          "Press kit"]

        [:div.press-kit-intro-container
          [:div.press-kit-intro
            [:div.press-kit-intro-title
              "An introduction to Carrot"]
            [:div.press-kit-intro-description
              [:p
                (str
                 "When teamwork gets too noisy and overwhelming, everyone "
                 "struggles to know what matters. Teamwork suffers.")]
              [:p
                (str
                 "With Carrot, team communication stays organized and "
                 "clear so it's easier to stay engaged and informed.")]

              [:p
                (str
                 "Carrot is ideal for keeping remote teams in sync. "
                 "Threaded comments make it easier to stay engaged asynchronously "
                 "across time zones. And each day, teams wake up to a "
                 "personalized summary of what’s important.")]

              [:p
                (str
                 "With Carrot, teams are empowered with the information "
                 "they need to stay focused and make better decisions.")]

              [:p.core-feature
                "Core feature"]
              [:ul.core-feature-list
                [:li "Space to write longer updates that convey more information to your team"]
                [:li "Threaded comments make it easy for your team to stay engaged asynchronously. Ideal for remote teams."]
                [:li "Request a follow-up when you need a reply or feedback. Carrot works in the background to make sure they’ll follow up."]
                [:li "Know who saw your update with analytics"]
                [:li "Recurring updates that build consistent communication and build trust"]
                [:li "Each day, everyone receives a personalized summary of what’s important."]
                [:li "Slack integration, including an auto-share to the right Slack #channels."]]]
            [:div.press-kit-intro-footer
              [:div.press-kit-intro-footer-left
                "Questions?"]
              [:div.press-kit-intro-footer-right
                "We’re always happy to talk about Carrot."
                [:a
                  {:class "intercom-chat-link"
                   :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
                  "Say hello"]]]
            [:div.press-kit-intro-footer
              [:div.press-kit-intro-footer-left
                "Carrot logo pack"]
              [:div.press-kit-intro-footer-right
                "Carrot logo assets. Light, dark, and the Carrot icon."
                [:a
                  {:href "https://carrot-press-kit.s3.amazonaws.com/Carrot-Logo.zip"}
                  "Download"]]]
            [:div.press-kit-intro-footer
              [:div.press-kit-intro-footer-left
                "Product screenshots"]
              [:div.press-kit-intro-footer-right
                "Press-friendly product screenshots."
                [:a
                  {:href "https://carrot-press-kit.s3.amazonaws.com/screenshots.zip"}
                  "Download"]]]]]

        (shared/dashed-string 1)

        [:div.odds-ends
          [:div.odds-ends-title
            "Odds and ends"]

          [:div.odds-ends-description
            (str
             "Here are a few more details about Carrot and where to find us. For a quote "
             "or to talk more about communication for remote teams, contact us anytime.")]

          [:div.odds-ends-content
            [:div.odds-ends-content-left
              "Other facts"]
            [:div.odds-ends-content-right
              [:ul
                [:li
                  "Our team is distributed by design"]
                [:li
                  "Carrot is open source. "
                   [:a
                    {:href "https://github.com/open-company"
                     :target "_blank"}
                    "Visit us on Github"]]
                [:li
                  "Carrot has a 14-day free trial. After that, it’s $5 per user per month. 50% off for approved nonprofits."]
                [:li
                  "Press contact: "
                  [:a
                    {:href "mailto:stuart.levinson@carrot.io"}
                    "Stuart Levinson"]]]]]
          [:div.odds-ends-content
            [:div.odds-ends-content-left
              "Find us online"]
            [:div.odds-ends-content-right
              [:ul
                [:li
                  "Website: "
                  [:a
                    {:href "https://carrot.io"}
                    "https://carrot.io"]]
                [:li
                  "Email: "
                  [:a
                    {:href "mailto:hello@carrot.io"}
                    "hello@carrot.io"]]
                [:li
                  "Chat: "
                  [:a {:class "intercom-chat-link"
                       :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
                    "Say hello"]]
                [:li
                  "Social: "
                  [:a
                    {:href "https://twitter.com/carrot_hq"
                     :target "_blank"}
                    "Twitter"]
                  ", "
                  [:a
                    {:href "https://github.com/open-company"
                     :target "_blank"}
                    "Github"]]]

              ]]]]

        (shared/dashed-string 2)

      shared/pricing-footer]])