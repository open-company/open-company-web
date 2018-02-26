(ns oc.web.components.ui.shared-misc
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(def video
  [:div.main-animation-container
    [:video.main-animation
      {:controls false
       :auto-play true
       :poster (utils/cdn "/img/ML/new_homepage_screenshot.png")
       :loop true}
      [:source
        {:src (utils/cdn "/img/ML/animation.webm")
         :type "video/webm"}]
      [:source
        {:src (utils/cdn "/img/ML/animation.mp4")
         :type "video/mp4"}]
      [:div.fallback
        "Your browser doesn’t support this video format."]]])

(def horizontal-carousell
  [:div.horizontal-carousell
    [:div.horizontal-carousell-inner
      [:img.horizontal-carousell-1
        (utils/retina-src "/img/ML/homepage_mobile_screenshot_1")]
      [:img.horizontal-carousell-2
        (utils/retina-src "/img/ML/homepage_mobile_screenshot_2")]
      [:img.horizontal-carousell-3
        (utils/retina-src "/img/ML/homepage_mobile_screenshot_3")]]])


(def carrot-cards
  [:div.cards-container
    [:div.cards-row.group
      [:div.card.card-1
        [:div.card-icon]
        [:div.card-title
          "Say what’s important "]
        [:div.card-content
          (str
            "Space to write more "
            "than a quick chat - like "
            "key updates, announcements, "
            "plans, and stories.")]]
      [:div.card.card-2
        [:div.card-icon]
        [:div.card-title
          "Read without interruptions"]
        [:div.card-content
          (str
            "Stay in sync without worrying "
            "you missed something important "
            "in a fast-moving conversation.")]]
      [:div.card.card-3
        [:div.card-icon]
        [:div.card-title
          "Focused, topic-based discussion"]
        [:div.card-content
          (str
            "Keep team reactions, comments "
            "and questions together in one "
            "place for better context.")]]]
    [:div.cards-row.group
      [:div.card.card-4
        [:div.card-icon]
        [:div.card-title
          "Stay informed and aligned"]
        [:div.card-content
          (str
            "Get up to speed quickly with the "
            "full picture in one place. Perfect "
            "for distributed teams, too.")]]
      [:div.card.card-5
        [:div.card-icon]
        [:div.card-title
          "See who’s engaged"]
        [:div.card-content
          (str
            "Carrot measures team "
            "engagement so leaders "
            "can see if their teams "
            "are aligned.")]]
      [:div.card.card-6
        [:div.card-icon]
        [:div.card-title
          (str
            "In sync "
            "with Slack")]
        [:div.card-content
          (str
            "Discussions are automatically "
            "shared to the right channels. ")
          [:a
            {:href oc-urls/slack
             :on-click #(router/nav! oc-urls/slack)}
            "Learn more"]]]]])

(def carrot-testimonials
  [:section.testimonials-section
    [:div.balloon.big-yellow-1]
    [:div.balloon.small-blue-1]
    [:div.testimonials-section-title
      "Don’t take our word for it"]
    [:div.testimonials-section-subtitle
      "Here’s how we’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.card
        [:div.card-content
          (str
           "We love Slack for spontaneous "
           "stuff, but when it’s time to "
           "post key updates that can’t "
           "be missed, Carrot is awesome.")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (utils/cdn "/img/ML/happy_face_blue.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]
      [:div.card
        [:div.card-content
          (str
           "Carrot makes sure our crucial "
           "updates aren’t drowned out "
           "by \"taco Tuesday?\" and "
           "silly memes!")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (utils/cdn "/img/ML/happy_face_purple.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]
      [:div.card
        [:div.card-content
          (str
           "RIP team email! 👻  With "
           "Carrot, email is finally "
           "obsolete for team updates "
           "and the rest.")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (utils/cdn "/img/ML/happy_face_red.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]
      [:div.card
        [:div.card-content
          (str
           "Before Carrot, I never knew "
           "if anyone saw my updates! "
           "Now I know who's engaged "
           "and aligned.  🙏")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (utils/cdn "/img/ML/happy_face_yellow.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]]])