(ns oc.web.components.ui.shared-misc
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(def testimonials-logos-line
  [:div.homepage-testimonials-container.group
    [:div.homepage-testimonials-logo.logo-novo]
    [:div.homepage-testimonials-logo.logo-ph]
    [:div.homepage-testimonials-logo.logo-wsu]
    [:div.homepage-testimonials-logo.logo-om]
    [:div.homepage-testimonials-logo.logo-mio]
    [:div.homepage-testimonials-logo.logo-sd]])

(def testimonials-section
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it."]
    [:div.testimonials-section-subtitle
      "We’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.testimonials-cards-inner.group
        [:div.testimonials-cards-row.group
          [:div.testimonial-card.oval-money
            [:div.testimonial-quote
              (str
               "“The morning digest keeps "
               "everyone informed without "
               "a constant barrage "
               "throughout the day.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Edoardo Benedetto"]
              [:div.testimonial-role
                [:a
                  {:href "https://ovalmoney.com/"
                   :target "_blank"}
                  "Oval Money"]
                ", UX Architect"]]]
          [:div.testimonial-card.skylight-digital
            [:div.testimonial-quote
              (str
               "“Carrot is our favorite Slack "
               "app. It's perfect for longer- "
               "form updates no one "
               "should miss.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Chris Cairns"]
              [:div.testimonial-role
                [:a
                  {:href "https://skylight.digital/"
                   :target "_blank"}
                  "Skylight Digital"]
                ", Managing Director"]]]
          [:div.testimonial-card.wayne-state-univerity
            [:div.testimonial-quote
              (str
               "“Carrot helps me share "
               "things the entire team "
               "needs to know - instead of "
               "burying it somewhere it "
               "won’t get noticed.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Nick DeNardis"]
              [:div.testimonial-role
                [:a
                  {:href "https://wayne.edu/"
                   :targe "_blank"}
                  "Wayne State University"]
                ", Director of Communications"]]]
          [:div.testimonial-card.partner-hero
            [:div.testimonial-quote
              (str
               "“Carrot is where we "
               "communicate important "
               "information when we need "
               "everyone to see it - "
               "regardless of their time zone.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Andrew Love"]
              [:div.testimonial-role
                [:a
                  {:href "https://partnerhero.com/"
                   :targe "_blank"}
                  "PartnerHero"]
                ", Director of R&D"]]]]
        [:div.testimonials-cards-row.group
          [:div.testimonial-card.blend-labs
            [:div.testimonial-quote
              (str
               "“I used to waste time "
               "begging team leads to get "
               "their updates in, but now "
               "Carrot reminds them "
               "automatically for me.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Sara Vienna"]
              [:div.testimonial-role
                [:a
                  {:href "https://bl3ndlabs.com/"
                   :target "_blank"}
                  "BL3NDlabs"]
                ", Head of Design"]]]
          [:div.testimonial-card.m-io
            [:div.testimonial-quote
              (str
               "“On Carrot, my updates "
               "get noticed and get the "
               "team talking. I love that.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Tom Hadfield"]
              [:div.testimonial-role
                [:a
                  {:href "https://m.io/"
                   :target "_blank"}
                  "M.io"]
                ", CEO"]]]
          [:div.testimonial-card.peak-support
            [:div.testimonial-quote
              (str
               "“Carrot fixed our issue of "
               "email overload. We use it "
               "for important company "
               "communications, shout- "
               "outs, and announcements "
               "so they won’t get buried in "
               "everyone’s inbox. It’s been "
               "a big win for us.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Jon Steiman"]
              [:div.testimonial-role
                [:a
                  {:href "https://www.peaksupport.io/"
                   :target "_blank"}
                  "Peak Support"]
                ", CEO"]]]
          [:div.testimonial-card.novo
            [:div.testimonial-quote
              (str
               "“Before Carrot I had no "
               "idea if anyone even noticed "
               "what I was saying.” ")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Tyler McIntyre"]
              [:div.testimonial-role
                [:a
                  {:href "https://banknovo.com/"
                   :target "_blank"}
                  "Novo"]
                ", CEO"]]]]]]])

(def keep-aligned
  [:section.keep-aligned
    [:div.keep-aligned-title
      "It’s never been easier to keep everyone on the same page"]
    [:button.mlb-reset.get-started-button
      "Get started - It's free"]])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "Company updates that rise above the noise"]
    [:div.keep-aligned-values-line.big-web-tablet-only
      [:img.keep-aligned-value.value-announcements
        {:src (utils/cdn "/img/ML/homepage_bottom_section_announcements.png")
         :srcSet (str (utils/cdn "/img/ML/homepage_bottom_section_announcements@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-decisions
        {:src (utils/cdn "/img/ML/homepage_bottom_section_decisions.png")
         :srcSet (str (utils/cdn "/img/ML/homepage_bottom_section_decisions@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-team-updates
        {:src (utils/cdn "/img/ML/homepage_bottom_section_team_updates.png")
         :srcSet (str (utils/cdn "/img/ML/homepage_bottom_section_team_updates@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-news
        {:src (utils/cdn "/img/ML/homepage_bottom_section_news.png")
         :srcSet (str (utils/cdn "/img/ML/homepage_bottom_section_news@2x.png") " 2x")}]]
    [:button.mlb-reset.get-started-button
      "Get started - It's free"]])

(defn keep-aligned-section [slack?]
  [:section.home-keep-aligned.group

    [:div.keep-aligned-section
      [:div.keep-aligned-section-row.first-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Stay in sync"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Carrot reminds you when it’s time to update "
               "your team. Consistent communication builds "
               "trust and transparency.")]]
          [:div.keep-aligned-section-row-right
            (let [img-name (if slack? "homepage_screenshots_first_row_slack" "homepage_screenshots_first_row")]
             [:img.keep-aligned-section-screenshot.screenshot-1.big-web-tablet-only
              {:src (utils/cdn (str "/img/ML/" img-name ".png"))
               :srcSet (str (utils/cdn (str "/img/ML/"img-name "@2x.png")) " 2x")}])
            (let [img-name (if slack? "homepage_screenshots_first_row_mobile_slack" "homepage_screenshots_first_row_mobile")]
              [:img.keep-aligned-section-screenshot.screenshot-1.mobile-only
                {:src (utils/cdn (str "/img/ML/" img-name ".png"))
                 :srcSet (str (utils/cdn (str "/img/ML/" img-name "@2x.png")) " 2x")}])]]]

      [:div.keep-aligned-section-row.second-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Lower the noise level"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Protect your team’s focus (and sanity). "
               "Carrot condenses noisy updates "
               "into a morning digest.")]
            [:div.slack-email-container.group.big-web-tablet-only
              [:div.slack-email-switch-container
                (when slack?
                  [:button.mlb-reset.slack-email-switch-bt.slack-bt
                    {:class (when slack? "active")}
                    [:div.slack-logo]
                    "Slack"])
                [:button.mlb-reset.slack-email-switch-bt.email-bt
                  {:class (when-not slack? "active")}
                  [:div.email-logo]
                  "Email"]
                (when-not slack?
                  [:button.mlb-reset.slack-email-switch-bt.slack-bt
                    [:div.slack-logo]
                    "Slack"])]]]
          [:div.keep-aligned-section-row-left
            [:span.sent-daily-slack-email.mobile-only
              "Sent daily on Slack or email"]
            [:div.keep-aligned-section-carion-container
              [:div.keep-aligned-section-carion-inner
                (when slack?
                  [:div.keep-aligned-section-header.mobile-only.first-header
                    [:span.slack-logo]
                    [:span.inner-label "Slack"]])
                (when slack?
                  [:img.keep-aligned-section-screenshot.screenshot-2.carion-1
                    {:class "first-image"
                     :src (utils/cdn "/img/ML/homepage_screenshots_second_row_slack.png")
                     :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_second_row_slack@2x.png") " 2x")}])
                [:div.keep-aligned-section-header.mobile-only
                  {:class (if slack? "second-header" "first-header")}
                  [:span.email-logo]
                  [:span.inner-label "Email"]]
                [:img.keep-aligned-section-screenshot.screenshot-2
                  {:class (if slack? "carion-1-alt" "carion-1")
                   :src (utils/cdn "/img/ML/homepage_screenshots_second_row.png")
                   :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]
                (when-not slack?
                  [:div.keep-aligned-section-header.mobile-only.second-header
                    [:span.slack-logo]
                    [:span.inner-label "Slack"]])
                (when-not slack?
                  [:img.keep-aligned-section-screenshot.screenshot-2.carion-1-alt
                    {:class "second-image"
                     :src (utils/cdn "/img/ML/homepage_screenshots_second_row_slack.png")
                     :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_second_row_slack@2x.png") " 2x")}])]
              [:button.keep-aligned-section-next-bt.mlb-reset]]]]]

      [:div.keep-aligned-section-row.third-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Know who’s engaged"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "With Carrot, you know who saw your "
               "update. If it’s missed, Carrot works in the "
               "background to remind them for you.")
              [:br]
              [:br]
              (str
               "See which updates create energy and "
               "engagement, and which ones aren’t getting "
               "through.")]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-3.big-web-tablet-only
              {:src (utils/cdn "/img/ML/homepage_screenshots_third_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_third_row@2x.png") " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-3.mobile-only
              {:src (utils/cdn "/img/ML/homepage_screenshots_third_row_mobile.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_third_row_mobile@2x.png") " 2x")}]]]]]])

(def animation-lightbox
  [:div.animation-lightbox-container
    {:on-click #(js/OCStaticHideAnimationLightbox %)}
    [:div.animation-lightbox
      [:div {:id "youtube-player"}]
      [:button.settings-modal-close.mlb-reset
        {:on-mouse-down #(js/OCStaticHideAnimationLightbox %)
         :on-touch-start #(js/OCStaticHideAnimationLightbox %)}]]])

(def show-animation-button
  [:button.mlb-reset.show-animation-bt
    {:on-click #(js/OCStaticShowAnimationLightbox)}
    [:div.green-play]
    "Carrot in 60 seconds"])