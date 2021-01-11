(ns oc.pages.slack
  (:require [oc.shared :as shared]
            [environ.core :refer (env)]))

(defn slack
  "Slack page. This is a copy of oc.web.components.slack and
   every change here should be reflected there and vice versa."
  [options]
  [:div.slack-wrap.group
   {:id "wrap"}
   [:div.main.slack
      ; Hope page header
    [:section.carrot-plus-slack.group

     [:h1.slack-headline
      "Slack & Carrot"]

     [:div.slack-subline
      "Slack teams use Carrot to share important news, updates, and decisions nobody should miss."]

     [:div.slack-subline
      "Clear, organized communication. Ideal for remote teams."]

     [:div.slack-button-container.group
      [:a.add-to-slack-button
       {:id "get-started-centred-bt"
        :href (env :slack-signup-url)}]]

     [:div.main-animation-container
      [:img.main-animation.big-web-tablet-only
       {:src (shared/cdn "/img/ML/homepage_screenshot.png")
        :alt "Carrot"
        :srcSet (str
                 (shared/cdn "/img/ML/homepage_screenshot@2x.png") " 2x, "
                 (shared/cdn "/img/ML/homepage_screenshot@3x.png") " 3x")}]
      [:img.main-animation.mobile-only
       {:src (shared/cdn "/img/ML/homepage_mobile_screenshot.png")
        :alt "Carrot"
        :srcSet (str
                 (shared/cdn "/img/ML/homepage_mobile_screenshot@2x.png") " 2x, "
                 (shared/cdn "/img/ML/homepage_mobile_screenshot@3x.png") " 3x")}]]

        ;shared/testimonials-logos-line
     ]

    (shared/testimonials-section :slack)

    shared/pricing-footer]])

(defn slack-lander
  "Slack lander page. This is a copy of oc.web.components.slack-lander and
   every change here should be reflected there and vice versa."
  [options]
  [:div.slack-lander-wrap.group
   {:id "wrap"}
   [:div.main.slack-lander
      ; Hope page header
    [:section.cta.group

     [:h1.headline
      "Join your team on Carrot"]

     [:div.subheadline
      "Carrot makes it simple for Slack teams to stay aligned around what matters most."]

     [:a.continue-with-slack-bt
      {:id "get-started-centred-bt"
       :href (env :slack-signup-url)}]

     [:div.main-animation-container
      [:img.main-animation.big-web-tablet-only
       {:src (shared/cdn "/img/ML/homepage_screenshot.png")
        :alt "Carrot"
        :srcSet (str
                 (shared/cdn "/img/ML/homepage_screenshot@2x.png") " 2x, "
                 (shared/cdn "/img/ML/homepage_screenshot@3x.png") " 3x")}]
      [:img.main-animation.mobile-only
       {:src (shared/cdn "/img/ML/homepage_mobile_screenshot.png")
        :alt "Carrot"
        :srcSet (str
                 (shared/cdn "/img/ML/homepage_mobile_screenshot@2x.png") " 2x, "
                 (shared/cdn "/img/ML/homepage_mobile_screenshot@3x.png") " 3x")}]]

        ;shared/testimonials-logos-line
     ]]])