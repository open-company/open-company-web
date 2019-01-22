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
        ;; First column
        [:div.testimonals-cards-column.group
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
                  "Peak Support, CEO"]]]]
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
                    "Wayne State University, Director of Communications"]]]]]
        ;; Second column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.oval-money
            [:div.testimonial-quote
              (str
               "“Carrot keeps our "
               "distributed team informed "
               "about what matters most.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Edoardo Benedetto"]
              [:div.testimonial-role
                [:a
                  {:href "https://ovalmoney.com/"
                   :target "_blank"}
                  "Oval Money, Head of Design"]]]]
          [:div.testimonial-card.helloticket
            [:div.testimonial-quote
              (str
               "“Staying aligned across two "
               "offices is hard. Slack feels "
               "too ‘light’ and crazy for "
               "important information; and "
               "no one wants to read "
               "weekly emails. With Carrot, "
               "we have a knowledge base "
               "of what's going on with the "
               "company that’s made it "
               "easy to stay aligned.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Alberto Martinez"]
              [:div.testimonial-role
                [:a
                  {:href "https://ovalmoney.com/"
                   :target "_blank"}
                  "Hellotickets, CEO"]]]]]
        ;; Third column
        [:div.testimonals-cards-column.group
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
                  "M.io, CEO"]]]]
          [:div.testimonial-card.partner-hero
            [:div.testimonial-quote
              (str
               "“Carrot is where we "
               "communicate important "
               "information when we need "
               "everyone to see it - "
               "regardless of their time "
               "zone.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Andrew Love"]
              [:div.testimonial-role
                [:a
                  {:href "https://partnerhero.com/"
                   :targe "_blank"}
                  "PartnerHero, Director of R&D"]]]]
          [:div.testimonial-card.blend-labs
            [:div.testimonial-quote
              (str
               "“Carrot reminds us when "
               "it’s time to share a weekly "
               "update - so it’s easier for "
               "everyone to stay in sync.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Sara Vienna"]
              [:div.testimonial-role
                [:a
                  {:href "https://bl3ndlabs.com/"
                   :target "_blank"}
                  "BL3NDlabs, Head of Design"]]]]]
        ;; Fourth column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.novo
            [:div.testimonial-quote
              (str
               "“We use Carrot when we "
               "need to make sure "
               "everyone is on the same "
               "page across all our offices "
               "here and abroad. It helps us "
               "share BIG wins, and in a "
               "fast-growing startup "
               "keeping that family vibe, "
               "its awesome.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Tyler McIntyre"]
              [:div.testimonial-role
                [:a
                  {:href "https://banknovo.com/"
                   :target "_blank"}
                  "Novo, CEO"]]]]
          [:div.testimonial-card.skylight-digital
            [:div.testimonial-quote
              (str
               "“Carrot is a perfect "
               "compliment for Slack. We "
               "use it for longer-form "
               "weekly updates no one "
               "should miss.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Chris Cairns"]
              [:div.testimonial-role
                [:a
                  {:href "https://skylight.digital/"
                   :target "_blank"}
                  "Skylight Digital, Managing Director"]]]]]]]])

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

(defn keep-aligned-section []
  [:section.home-keep-aligned.group

    [:div.keep-aligned-section
      [:div.keep-aligned-section-row.first-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Start each day in sync"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Carrot condenses noisy updates into a "
               "morning digest for Slack or email. Your team "
               "stays aligned with fewer interruptions.")]
            [:div.slack-email-container.group.big-web-only
              [:div.slack-email-switch-container.group
                {:class "show-slack"}
                [:button.mlb-reset.slack-email-switch-bt.slack-bt
                  [:div.slack-logo]]
                [:button.mlb-reset.slack-email-switch-bt.email-bt
                  [:div.email-logo]]
                [:div.slack-email-switch-bottom-panel.slack-panel
                  [:div.slack-email-switch-bottom-panel-copy
                    (str
                     "“Our remote teams are ecstatic about not having to read "
                     "hundreds of Slack messages each morning to make sense of "
                     "the previous day.”")]
                  [:div.slack-email-switch-bottom-panel-img]
                  [:div.slack-email-switch-bottom-panel-name
                    "Andrew Love"]
                  [:div.slack-email-switch-bottom-panel-role
                    "PartnerHero, Director of R&D"]]
                [:div.slack-email-switch-bottom-panel.email-panel
                  [:div.slack-email-switch-bottom-panel-copy
                    (str
                     "“The morning digest is how we know everyone in our "
                     "offices is up to date on what matters.”")]
                  [:div.slack-email-switch-bottom-panel-img]
                  [:div.slack-email-switch-bottom-panel-name
                    "Bev Pellow"]
                  [:div.slack-email-switch-bottom-panel-role
                    "QPL Rural, Owner"]]]]]
          [:div.keep-aligned-section-row-right
            [:div.keep-aligned-section-carion-container
              [:div.keep-aligned-section-carion-inner
                [:div.keep-aligned-section-header..tablet-mobile-only.first-header
                  [:span.slack-logo]]
                [:img.keep-aligned-section-screenshot.screenshot-1.carion-1.first-image
                  {:src (utils/cdn "/img/ML/homepage_screenshots_first_row_slack.png")
                   :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_first_row_slack@2x.png") " 2x")}]
                [:div.keep-aligned-section-header..tablet-mobile-only.second-header
                  [:span.email-logo]]
                [:img.keep-aligned-section-screenshot.screenshot-1.second-image.carion-1-alt
                  {:src (utils/cdn "/img/ML/homepage_screenshots_first_row.png")
                   :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]]
              [:button.keep-aligned-section-next-bt.mlb-reset]]]]]

      [:div.keep-aligned-section-row.second-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
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
          [:div.keep-aligned-section-row-left
            [:img.keep-aligned-section-screenshot.screenshot-2.big-web-tablet-only
              {:src (utils/cdn "/img/ML/homepage_screenshots_second_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-2.mobile-only
              {:src (utils/cdn "/img/ML/homepage_screenshots_second_row_mobile.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_second_row_mobile@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.third-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Stay current"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Carrot reminds you when it’s time to update "
               "your team. Consistent communication builds "
               "trust and transparency.")]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-3.big-web-tablet-only
              {:src (utils/cdn "/img/ML/homepage_screenshots_third_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_third_row@2x.png") " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-3.mobile-only
              {:src (utils/cdn "/img/ML/homepage_screenshots_third_row_mobile.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_third_row_mobile@2x.png") " 2x")}]]]]]])