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
                 "When teamwork gets too noisy and overwhelming, "
                 "leaders struggle to communicate effectively with their teams. "
                 "With Carrot, leaders rise above the noise to communicate what matters.")]

              [:p
                (str
                 "It’s especially tough for leaders to be heard in the "
                 "age of Slack because key announcements, updates, and "
                 "decisions get lost in fast-moving conversations.")]

              [:p
                (str
                 "Carrot makes leadership communication stand out so "
                 "no one misses it, sparks more meaningful discussions, "
                 "and makes sure your team is listening and looped in.")]

              [:p
                (str
                 "With Carrot, leaders are sure their teams are empowered "
                 "with the information they need to stay focused and make better decisions.")]]
            [:div.press-kit-intro-footer
              [:div.press-kit-intro-footer-left
                "Questions?"]
              [:div.press-kit-intro-footer-right
                "We’re always happy to talk about Carrot."
                [:a
                  {:class "intercom-chat-link"
                   :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
                  "SAY HELLO"]]]]]

        [:div.core-ft
          [:div.core-ft-title
            "Core features"]
          [:div.core-ft-content
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Leadership visibility"]
              [:div.core-ft-content-description
                [:p
                 (str
                  "Key communications stay visible and organized outside of noisy channels, so "
                  "it’s easier for distributed teams to get caught up on their own time without "
                  "worrying they missed something important in a fast-moving conversation. "
                  "It’s also perfect for new employees getting up to speed quickly. With greater "
                  "transparency, your team stays focused and makes better decisions.")]]]
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Meaningful discussions"]
              [:div.core-ft-content-description
                [:p
                  (str
                    "Carrot gives everyone the time and space they need to add reactions and "
                    "thoughtful comments to a post, and then makes it easy for everyone to see as "
                    "part of the original post. Better discussions provide greater insight into "
                    "what your team is thinking, and give everyone more context about what’s happening.")]]]
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Make sure you're being heard"]
              [:div.core-ft-content-description
                [:p
                 (str
                  "Too often leaders want to be transparent, but they communicate in ways that aren’t. "
                  "Slack is too noisy, and email is ignored, and above all leaders have no idea if "
                  "anyone is paying attention.")]
                [:p
                 (str
                  "When you share something important in Carrot, you’ll know who’s seen it. If "
                  "people haven’t seen it, Carrot reminds them to make sure they’re looped in. "
                  "That eliminates communication gaps and keeps everyone on the same page.")]]]
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Communicate more effectively"]
              [:div.core-ft-content-description
                [:p
                  "With Carrot, leaders can rise above the noise to reach their teams more effectively."]
                [:ul
                  [:li
                   "Beautifully formatted updates, including images and video, motivate engagement"]
                  [:li
                   "Video updates add fun and a human touch ‒ great for distributed teams"]
                  [:li
                   "Space for medium- and long-form storytelling, e.g., Lessons Learned, Success Stories, and other stories that unify your team"]
                  [:li
                   "Recurring updates that build consistent communication and build trust"]]]]]]

        [:div.media-res-container
          [:div.media-res
            [:div.media-res-title
              "Media resources"]
            [:div.media-res-content
              [:button.mlb-reset.media-res-content-video
                {:onClick "OCStaticShowAnimationLightbox();"}
                [:img
                  {:src (shared/cdn "/img/ML/press_kit_homepage.png")
                   :srcSet (str (shared/cdn "/img/ML/press_kit_homepage.png") " 2x")}]
                [:div.play-button]]

              [:a.media-res-content-video-download
                {:href "https://carrot-press-kit.s3.amazonaws.com/Carrot-intro-video.mp4"}
                "Download marketing video"]

              [:div.media-res-footer
                [:div.media-res-footer-left
                  "Carrot logo pack"]
                [:div.media-res-footer-right
                  "Carrot logo assets. Light, dark, and the Carrot icon."
                  [:div.media-res-download
                    [:a
                      {:href "https://carrot-press-kit.s3.amazonaws.com/Carrot-Logo.zip"}
                      "Download"]]]]

              [:div.media-res-footer
                [:div.media-res-footer-left
                  "Product screenshots"]
                [:div.media-res-footer-right
                  "Press-friendly product screenshots."
                  [:div.media-res-download
                    [:a
                      {:href "https://carrot-press-kit.s3.amazonaws.com/Carrot-Screenshots.zip"}
                      "Download"]]]]]]]

        [:div.odds-ends
          [:div.odds-ends-title
            "Odd & Ends"]

          [:div.odds-ends-description
            (str
             "Here are a few more details about Carrot and where to find us. "
             "For a quote or to talk more about leadership communication, contact us anytime.")]

          [:div.odds-ends-content
            [:div.odds-ends-content-left
              "Other facts"]
            [:div.odds-ends-content-right
              [:ul
                [:li "Our team is distributed by design"]
                [:li "Carrot is open source. " [:a {:href "https://github.com/open-company" :target "_blank"} "Visit us on Github"]]
                [:li "Carrot has a free plan for small teams, and offers 50% off for approved nonprofits."]
                [:li "Press contact: "
                    [:a {:href "mailto:stuart.levinson@carrot.io"}
                      "Stuart Levinson"]]]]]
          [:div.odds-ends-content
            [:div.odds-ends-content-left
              "Find us online"]
            [:div.odds-ends-content-right
              [:ul
                [:li "Website: "
                     [:a {:href "https://carrot.io"} "https://carrot.io"]]
                [:li "Email: "
                     [:a {:href "mailto:hello@carrot.io"} "hello@carrot.io"]]
                [:li "Chat: "
                     [:a {:class "intercom-chat-link"
                          :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
                      "Say hello"]]
                [:li "Social: "
                     [:a {:href "https://twitter.com/carrot_hq" :target "_blank"} "Twitter"]
                     ", "
                     [:a {:href "https://github.com/open-company" :target "_blank"} "Github"]
                     ", and "
                     [:a {:href "https://blog.carrot.io" :target "_blank"} "Medium"]
                     " (our blog)"]]]]]]

      shared/testimonials-section

      shared/keep-aligned-bottom]])