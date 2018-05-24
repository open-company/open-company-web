(ns oc.web.components.home-page
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.shared-misc :as shared-misc]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.try-it-form :refer (try-it-form)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.carrot-box-thanks :refer (carrot-box-thanks)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs home-page < (rum/local false ::thanks-box-top)
                       (rum/local false ::thanks-box-bottom)
                       (rum/local false ::confirm)
                       {:did-mount (fn [s]
                                    (when (:tif (:query-params @router/path))
                                      (utils/after 1500 #(.focus (sel1 [:input.try-it-form-central-input]))))
                                    s)
                       :will-mount (fn [s]
                                     (when (:confirm (:query-params @router/path))
                                       (reset! (::confirm s) true))
                                     s)}
  [s]
  [:div
    (site-header)
    (site-mobile-menu)
    [:div
      {:id "wrap"}
      (login-overlays-handler)
      [:div.main.home-page
        ; Hope page header
        [:section.cta.group
          [:div.balloon.big-blue]
          [:div.balloon.small-green]
          [:div.balloon.big-green]
          [:div.balloon.small-purple-face]
          [:div.balloon.big-yellow]
          [:div.balloon.small-purple]

          [:h1.headline
            "Company digest for growing and distributed teams."]
          [:div.subheadline
            (str
              "Key updates and announcements get lost in fast-moving chat and stuffed inboxes. "
              "Carrot makes it simple to stay aligned around what matters most.")]
          ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
          [:div.get-started-button-container
            [:button.mlb-reset.get-started-button
              {:id "get-started-centred-bt"}
              "Get started for free"]]
          [:div.subheadline-2
            "No credit card required  •  Works with Slack"]
          (carrot-box-thanks "carrot-box-thanks-top")
          [:div.carrot-box-container.confirm-thanks.group
            {:style {:display "none"}}
            [:div.carrot-box-thanks
              [:div.thanks-headline "You are Confirmed!"]
              [:div.thanks-subheadline "Thank you for subscribing."]]]

          shared-misc/video

          [:div.core-values-list.group
            [:div.core-value.key-announcement
              "Key announcements"]
            [:div.core-value.company-updates
              "Company & team updates"]
            [:div.core-value.strategic-plans
              "Strategic plans"]
            [:div.core-value.ideas-discussions
              "Ideas & follow-on discussions"]]]

        [:section.home-keep-aligned
          [:h2.home-keep-aligned-title
            "Carrot keeps leaders and their teams aligned"]

          [:div.home-keep-aligned-carouselle.carouselle
            [:div.carouselle-screenshots
              [:div.carouselle-screenshot.screenshot-1]
              [:div.carouselle-screenshot.screenshot-2.disappear]
              [:div.carouselle-screenshot.screenshot-3.disappear]]
            [:button.mlb-reset.carouselle-left]
            [:button.mlb-reset.carouselle-right]]

          [:div.green-values.group
            [:div.green-value.lamp
              [:div.green-value-icon]
              [:div.green-value-title
                "KNOW WHAT MATTERS MOST"]
              [:div.green-value-description
                (str
                 "See \"must reads\" and focus on the "
                 "people and topics you can't miss.")]]
            [:div.green-value.people
              [:div.green-value-icon]
              [:div.green-value-title
                "SPARK FOLLOW-ON DISCUSSIONS"]
              [:div.green-value-description
                (str
                 "React, comment and ask questions, with "
                 "time and space to be more thoughtful.")]]
            [:div.green-value.slack
              [:div.green-value-icon]
              [:div.green-value-title
                "SYNC TO SLACK"]
              [:div.green-value-description
                (str
                 "Share posts to the relevant Slack "
                 "channel, and sync comments from Slack "
                 "back into Carrot.")]]]]

        [:section.team-transparency
          [:h2.team-transparency-title
            "Carrot keeps leaders and their teams aligned"]

          [:div.team-transparency-carouselle.carouselle
            [:div.carouselle-screenshots
              [:div.carouselle-screenshot.screenshot-1]
              [:div.carouselle-screenshot.screenshot-2.disappear]
              [:div.carouselle-screenshot.screenshot-3.disappear]]
            [:button.mlb-reset.carouselle-left]
            [:button.mlb-reset.carouselle-right]]

          [:div.blue-values.group
            [:div.blue-value.mega
              [:div.blue-value-icon]
              [:div.blue-value-title
                "POST QUICKLY"]
              [:div.blue-value-description
                (str
                 "It's simple and fast to share something "
                 "new with your team.")]]
            [:div.blue-value.paperclip
              [:div.blue-value-icon]
              [:div.blue-value-title
                "ADD CONTENT FROM ANYWHERE"]
              [:div.blue-value-description
                (str
                 "Link to external content, or add images, "
                 "video and attachments from Google "
                 "Drive, Dropbox, and others.")]]
            [:div.blue-value.thumbup
              [:div.blue-value-icon]
              [:div.blue-value-title
                "SEE WHO’S ENGAGED"]
              [:div.blue-value-description
                (str
                 "See who’s viewed your post to "
                 "eliminate communication gaps.")]]]]

        [:section.slack-comparison
          [:div.slack-comparison-headline
            "PERFECT FOR SLACK TEAMS"]
          [:div.slack-comparison-headline-1
            "Slack keeps your team connected in the moment."]
          [:div.slack-comparison-headline-2
            "Carrot keeps it aligned over time."]
          [:img.slack-comparison-screenshot.big-web-only
            {:src (utils/cdn "/img/ML/slack_comparison_screenshot.png")
             :src-set (str (utils/cdn "/img/ML/slack_comparison_screenshot@2x.png") " 2x")}]
          [:img.slack-comparison-screenshot.mobile-only
            {:src (utils/cdn "/img/ML/slack_comparison_screenshot_mobile.png")
             :src-set (str (utils/cdn "/img/ML/slack_comparison_screenshot_mobile@2x.png") " 2x")}]]

        shared-misc/carrot-testimonials

        shared-misc/keep-aligned]]

    (site-footer)])
