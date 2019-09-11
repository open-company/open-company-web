(ns oc.pages.slack-lander
  (:require [oc.pages.shared :as shared]
            [environ.core :refer (env)]))

(defn slack-lander
  "Slack lander page. This is a copy of oc.web.components.slack-lander and
   every change here should be reflected there and vice versa."
  [options]
  [:div.slack-lander-wrap
    {:id "wrap"}
    [:div.main.slack-lander
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Join your team on Carrot"]

        [:div.subheadline
          "Carrot makes it simple for Slack teams to stay aligned around what matters most."]

        [:a.continue-with-slack-bt
          {:href (env :slack-signup-url)}]

        [:div.main-animation-container
          [:img.main-animation
            {:src (shared/cdn "/img/ML/slack_screenshot.png")
             :src-set (str (shared/cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]

        shared/testimonials-logos-line]]])