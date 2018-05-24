(ns oc.web.components.ui.shared-misc
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(def video
  [:div.main-animation-container
    [:img.main-animation
      {:src (utils/cdn "/img/ML/homepage_screenshot.webp")
       :src-set (str (utils/cdn "/img/ML/homepage_screenshot@2x.webp") " 2x")}]])

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
          "Find anything fast"]
        [:div.card-content
          (str
            "Get up to speed quickly with the "
            "full picture in one place. Great "
            "for distributed teams, too.")]]
      [:div.card.card-5
        [:div.card-icon]
        [:div.card-title
          "See who’s engaged"]
        [:div.card-content
          (str
            "Carrot shows you who's reading "
            "what so leaders can see if "
            "their teams are aligned.")]]
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

(def keep-aligned
  [:section.keep-aligned
    [:div.keep-aligned-title
      "It’s never been easier to keep everyone on the same page"]
    [:button.mlb-reset.get-started-button
      "Get started for free"]
    [:div.keep-aligned-subtitle
      "No credit card required  •  Works with Slack"]])