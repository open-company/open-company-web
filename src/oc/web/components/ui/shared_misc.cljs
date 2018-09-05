(ns oc.web.components.ui.shared-misc
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(def testimonials-section
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it"]
    [:div.testimonials-section-subtitle
      "Here’s how we’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.testimonial-card
        [:div.testimonial-image]
        [:div.testimonial-name
          "CHRIS CAIRNS"]
        [:div.testimonial-role
          "Managing Partner"]
        [:div.testimonial-quote
          (str
           "As a busy leader it's hard to keep everyone up to date. "
           "I use Carrot to record a quick video update and it "
           "lets me know that everyone's seen it.")]
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
          "On Carrot, my updates get noticed and get the team talking. I love that."]
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
           "Carrot helps me share things the entire team needs to know "
           "about - instead of burying it somewhere it won’t get noticed.")]
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
      "It’s never been easier to keep everyone on the same page"]
    [:button.mlb-reset.get-started-button
      "Get started for free"]
    no-credit-card])

(def keep-aligned-section
  [:section.home-keep-aligned
    [:div.scroll-down]
    [:h2.keep-aligned-title
      "Carrot keeps leaders and their teams aligned"]

      [:div.keep-aligned-section
        [:div.keep-aligned-section-row.first-row.group
          [:img.keep-aligned-section-screenshot.screenshot-1.big-web-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_update_team.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_update_team@2x.png") " 2x")}]
          [:img.keep-aligned-section-screenshot.screenshot-1.mobile-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_update_team_mobile.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_update_team_mobile@2x.png") " 2x")}]
          [:div.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Update your team in seconds"]
            [:div.keep-aligned-section-list-item.purple-checkmark
              "Create compelling updates people want to read"]
            [:div.keep-aligned-section-list-item.purple-checkmark
              "Capture video updates to add a human touch"]
            [:div.keep-aligned-section-list-item.purple-checkmark
              "Link to Google, Dropbox, wikis and others"]
            [:div.keep-aligned-section-list-item.purple-checkmark
            "Automate recurring team updates"]]]

        [:div.keep-aligned-section-row.second-row.group
          [:img.keep-aligned-section-screenshot.screenshot-2.big-web-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_keep_informed.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_keep_informed@2x.png") " 2x")}]
          [:img.keep-aligned-section-screenshot.screenshot-2.mobile-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_keep_informed_mobile.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_keep_informed_mobile@2x.png") " 2x")}]
          [:div.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Keep everyone informed"]
            [:div.keep-aligned-section-list-item.green-checkmark
              "“Must see” updates rise to the top"]
            [:div.keep-aligned-section-list-item.green-checkmark
              "See what’s trending to stay in the loop"]
            [:div.keep-aligned-section-list-item.green-checkmark
              "Organized for easy browsing and search"]
            [:div.keep-aligned-section-list-item.green-checkmark
              "A morning digest summarizes what’s new"]]]

        [:div.keep-aligned-section-row.third-row.group
          [:img.keep-aligned-section-screenshot.screenshot-3.big-web-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_better_discussions.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_better_discussions@2x.png") " 2x")}]
          [:img.keep-aligned-section-screenshot.screenshot-3.mobile-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_better_discussions_mobile.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_better_discussions_mobile@2x.png") " 2x")}]
          [:div.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Spark better follow-on discussions"]
            [:div.keep-aligned-section-list-item.red-checkmark
              "Encourage thoughtful comments and questions"]
            [:div.keep-aligned-section-list-item.red-checkmark
              "Keep interactions together for greater context"]
            [:div.keep-aligned-section-list-item.red-checkmark
              "Syncs with Slack so discussions can happen anywhere"]]]

        [:div.keep-aligned-section-row.fourth-row.group
          [:img.keep-aligned-section-screenshot.screenshot-4.big-web-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_being_heard.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_being_heard@2x.png") " 2x")}]
          [:img.keep-aligned-section-screenshot.screenshot-4.mobile-only
            {:src (utils/cdn "/img/ML/homepage_screenshots_being_heard_mobile.png")
             :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_being_heard_mobile@2x.png") " 2x")}]
          [:div.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Make sure you're being heard"]
            [:div.keep-aligned-section-list-item.blue-checkmark
              "Know who's seen your post"]
            [:div.keep-aligned-section-list-item.blue-checkmark
              "Send reminders with a single click"]
            [:div.keep-aligned-section-list-item.blue-checkmark
              "Eliminate communication gaps"]
            [:div.keep-aligned-section-list-item.blue-checkmark
              "Ensure alignment on important items"]]]]])

(def access-anywhere-section
  [:section.access-anywhere-section
    [:div.access-anywhere-section-container
      [:div.access-anywhere-copy
        [:div.access-anywhere-copy-title
          "Stay up to date on the go."]
        [:div.access-anywhere-copy-title.big-web-only
          "Access Carrot from anywhere."]
        [:div.access-anywhere-copy-title.mobile-only
          "Access Carrot anywhere."]
        [:div.access-anywhere-copy-subtitle.big-web-only
          "Fully responsive web app. No app install required."]
        [:div.access-anywhere-copy-subtitle.mobile-only
          "Fully responsive web app."]
        [:div.access-anywhere-copy-subtitle.mobile-only
          "No app install required."]]
      [:div.access-anywhere-screenshot]]])

(defn slack-comparison-section [& [slack-version?]]
  [:section.slack-comparison
    [:div.slack-comparison-headline
      "PERFECT FOR SLACK TEAMS"]
    (when-not slack-version?
      [:div.slack-comparison-headline-1
        "Slack keeps your team connected in the moment."])
    (if slack-version? 
      [:div.slack-comparison-headline-2
        "Keep your team informed without distractions."]
      [:div.slack-comparison-headline-2
        "Carrot keeps it aligned over time."])
    [:img.slack-comparison-screenshot.big-web-only
      {:src (utils/cdn "/img/ML/slack_comparison_screenshot.png")
       :src-set (str (utils/cdn "/img/ML/slack_comparison_screenshot@2x.png") " 2x")}]
    [:img.slack-comparison-screenshot.mobile-only
      {:src (utils/cdn "/img/ML/slack_comparison_screenshot_mobile.png")
       :src-set (str (utils/cdn "/img/ML/slack_comparison_screenshot_mobile@2x.png") " 2x")}]])