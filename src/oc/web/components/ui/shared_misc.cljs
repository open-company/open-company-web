(ns oc.web.components.ui.shared-misc
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(def testimonials-section
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it."]
    [:div.testimonials-section-subtitle
      "We’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.testimonial-card
        [:div.testimonial-image]
        [:div.testimonial-name
          "CHRIS CAIRNS"]
        [:div.testimonial-role
          "Managing Partner"]
        [:div.testimonial-quote
          (str
           "“As a busy leader it's hard to keep "
           "everyone up to date. I use Carrot to "
           "record a quick video update and it lets "
           "me know that everyone's seen it.”")]
        [:div.testimonial-footer.group
          [:a
            {:href "https://skylight.digital/"
             :target "_blank"}
            [:div.testimonial-logo]]]]
      [:div.testimonial-card
        [:div.testimonial-image]
        [:div.testimonial-name
          "Tom Hadfield"]
        [:div.testimonial-role
          "CEO"]
        [:div.testimonial-quote
          (str
           "“On Carrot, my updates get noticed and "
           "get the team talking. I love that.”")]
        [:div.testimonial-footer.group
          [:a
            {:href "https://m.io/"
             :target "_blank"}
            [:div.testimonial-logo]]]]
      [:div.testimonial-card
        [:div.testimonial-image]
        [:div.testimonial-name
          "Nick DeNardis"]
        [:div.testimonial-role
          "Director of Digital Communications"]
        [:div.testimonial-quote
          (str
           "“Carrot helps me share things the entire "
           "team needs to know about - instead of "
           "burying it somewhere it won’t get "
           "noticed.”")]
        [:div.testimonial-footer.group
          [:a
            {:href "https://wayne.edu/"
             :target "_blank"}
            [:div.testimonial-logo]]]]]])

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
    [:div.core-value-container.key-announcement
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Announcements"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "Product "
          [:span.dot "•"]
          " 45 views"]
        [:div.core-value-box-title
          "Updates to billing & subscriptions (beta edition)"]]]
    [:div.core-value-container.team-updates
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Team updates"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "General "
          [:span.dot "•"]
          " 22 views"]
        [:div.core-value-box-title
          "June 25, 2018 all hands video highlights"]]]
    [:div.core-value-container.strategic-plans
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Decisions"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "Strategy "
          [:span.dot "•"]
          " 67 views"]
        [:div.core-value-box-title
          "Product roadmap review presentation and PDF"]]]
    [:div.core-value-container.stories
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Stories"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "Design "
          [:span.dot "•"]
          " 34 views"]
        [:div.core-value-box-title
          "How we pulled off our biggest launch ever 🤩"]]]])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "Try Carrot with your team for free."]
    [:button.mlb-reset.get-started-button
      "Get started for free"]
    no-credit-card])

(def keep-aligned-section
  [:section.home-keep-aligned.group

    [:div.keep-aligned-section
      [:div.keep-aligned-section-row.first-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Lead with clarity."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Key information stays organized and visible so your team can get caught "
               "up anytime without worrying they missed something in fast-moving conversations.")]
            [:img.keep-aligned-section-copy-values-list
              {:src (utils/cdn "/img/ML/homepage_vertical_values_list.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_vertical_values_list@2x.png") " 2x")}]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-1
              {:src (utils/cdn "/img/ML/homepage_screenshots_first_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.second-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Spark meaningful discussions."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Reactions and comments stay together with "
               "the post to provide greater context for "
               "what’s happening and why.")
              [:br][:br]
              (str
               "Your team can join the discussion from Slack "
               "— Carrot keeps it all in sync.")]]
          [:div.keep-aligned-section-row-left
            [:img.keep-aligned-section-screenshot.screenshot-2
              {:src (utils/cdn "/img/ML/homepage_screenshots_second_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.third-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Make sure you're heard."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "When you post something new, you’ll always know "
               "who’s seen it. If they haven’t, Carrot reminds them "
               "for you.")
              [:br][:br]
              (str
               "Carrot AI works in the background to keep everyone "
               "on your team looped in to what matters.")]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-3
              {:src (utils/cdn "/img/ML/homepage_screenshots_third_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_third_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.fourth-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Update your team in seconds."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Starting a new post is fast - with text or video "
               "- so it’s easy to create consistent "
               "communication that builds transparency and "
               "trust. Set up recurring updates, too.")]]
          [:div.keep-aligned-section-row-left
            [:img.keep-aligned-section-screenshot.screenshot-4
              {:src (utils/cdn "/img/ML/homepage_screenshots_fourth_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_fourth_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.fifth-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Perfect for Slack teams."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "New posts automatically shared to the "
               "appropriate Slack channel. Join the "
               "discussion right from Slack — Carrot keeps "
               "it all in sync.")]
            [:a.keep-aligned-section-copy-button
              {:href oc-urls/sign-up}]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-5
              {:src (utils/cdn "/img/ML/homepage_screenshots_fifth_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_fifth_row@2x.png") " 2x")}]]]]]])

(def animation-lightbox
  [:div.animation-lightbox-container
    {:onClick "OCStaticHideAnimationLightbox();"}
    [:div.animation-lightbox
      [:div {:id "youtube-player"}]
      [:button.settings-modal-close.mlb-reset
        {;:onClick "OCStaticHideAnimationLightbox();"
         :onMouseDown "OCStaticHideAnimationLightbox();"
         :ontouchstart "OCStaticHideAnimationLightbox();"}]]])

(def show-animation-button
  [:button.mlb-reset.show-animation-bt
    {:onClick "OCStaticShowAnimationLightbox();"}])