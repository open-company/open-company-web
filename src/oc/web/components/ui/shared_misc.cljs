(ns oc.web.components.ui.shared-misc
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(def carrot-testimonials
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it"]
    [:div.testimonials-section-subtitle
      "Here’s how we’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:img.card
        {:src (utils/cdn "/img/ML/testimonial_katie.png")
         :src-set (str (utils/cdn "/img/ML/testimonial_katie@2x.png") " 2x")}]
      [:img.card
        {:src (utils/cdn "/img/ML/testimonial_riley.png")
         :src-set (str (utils/cdn "/img/ML/testimonial_riley@2x.png") " 2x")}]
      [:img.card
        {:src (utils/cdn "/img/ML/testimonial_matt.png")
         :src-set (str (utils/cdn "/img/ML/testimonial_matt@2x.png") " 2x")}]]])

(def no-credit-card
  [:div.no-credit-card
    "No credit card required&nbsp;•&nbsp;Works with Slack"])

(def keep-aligned
  [:section.keep-aligned
    [:div.keep-aligned-title
      "It’s never been easier to keep everyone on the same page"]
    [:button.mlb-reset.get-started-button
      "Get started for free"]
    no-credit-card])

(def core-values-list
  [:div.core-values-list.group
    [:div.core-value.key-announcement
      "Key announcements"]
    [:div.core-value.company-updates
      "Company & team updates"]
    [:div.core-value.strategic-plans
      "Strategic plans"]
    [:div.core-value.ideas-discussions
      "Ideas & follow-on discussions"]])

(def testimonials-section
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it"]
    [:div.testimonials-section-subtitle
      "Here’s how we’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:img.card
        {:src (utils/cdn "/img/ML/testimonial_katie.png")
         :src-set (str (utils/cdn "/img/ML/testimonial_katie@2x.png") " 2x")}]
      [:img.card
        {:src (utils/cdn "/img/ML/testimonial_riley.png")
         :src-set (str (utils/cdn "/img/ML/testimonial_riley@2x.png") " 2x")}]
      [:img.card
        {:src (utils/cdn "/img/ML/testimonial_matt.png")
         :src-set (str (utils/cdn "/img/ML/testimonial_matt@2x.png") " 2x")}]]])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "It’s never been easier to keep everyone on the same page"]
    [:button.mlb-reset.get-started-button
      "Get started for free"]
    no-credit-card])

(def keep-aligned-section
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
           "See \"must sees\" and focus on the "
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
           "back into Carrot.")]]]])

(def team-transparency-section
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
           "eliminate communication gaps.")]]]])

(defn slack-comparison-section [& [slack-version?]]
  [:section.slack-comparison
    [:div.slack-comparison-headline
      "PERFECT FOR SLACK TEAMS"]
    (when-not slack-version?
      [:div.slack-comparison-headline-1
        "Slack keeps your team connected in the moment."])
    (if slack-version? 
      [:div.slack-comparison-headline-2
        "Keep Slackers on the same page with fewer distractions."]
      [:div.slack-comparison-headline-2
        "Carrot keeps it aligned over time."])
    [:img.slack-comparison-screenshot.big-web-only
      {:src (utils/cdn "/img/ML/slack_comparison_screenshot.png")
       :src-set (str (utils/cdn "/img/ML/slack_comparison_screenshot@2x.png") " 2x")}]
    [:img.slack-comparison-screenshot.mobile-only
      {:src (utils/cdn "/img/ML/slack_comparison_screenshot_mobile.png")
       :src-set (str (utils/cdn "/img/ML/slack_comparison_screenshot_mobile@2x.png") " 2x")}]])