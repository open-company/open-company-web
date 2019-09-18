(ns oc.pages.slack
  (:require [oc.pages.shared :as shared]
            [environ.core :refer (env)]))

(defn slack
  "Slack page. This is a copy of oc.web.components.slack and
   every change here should be reflected there and vice versa."
  [options]
  [:div.slack-wrap
    {:id "wrap"}
    [:div.main.slack
      ; Hope page header
      [:section.carrot-plus-slack.group

        [:h1.slack-headline
          "Slack & Carrot"]
        
        [:div.slack-subline
          "Slack teams use Carrot to share important news, updates, and decisions nobody should miss. Ideal for remote teams."]

        [:div.slack-button-container.group
            [:a.add-to-slack-button
              {:id "get-started-centred-bt"
               :href (env :slack-signup-url)}]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (shared/cdn "/img/ML/slack_screenshot.png")
             :srcSet (str
                      (shared/cdn "/img/ML/slack_screenshot@2x.png") " 2x, "
                      (shared/cdn "/img/ML/slack_screenshot@3x.png") " 3x, "
                      (shared/cdn "/img/ML/slack_screenshot@4x.png") " 4x")}]]

        shared/testimonials-logos-line]

      (shared/dashed-string 1)

      (shared/testimonials-screenshot-block :thoughtful-communication)

      (shared/dashed-string 2)

      (shared/testimonials-screenshot-block :start-in-sync)

      (shared/dashed-string 3)

      (shared/testimonials-screenshot-block :conversation)

      (shared/dashed-string 4)

      (shared/testimonials-screenshot-block :analytics)

      (shared/dashed-string 4)

      (shared/testimonials-screenshot-block :follow-ups)

      (shared/dashed-string 5)

      (shared/testimonials-screenshot-block :share-to-slack)

      (shared/dashed-string 6)

      shared/pricing-table-section
      ]])