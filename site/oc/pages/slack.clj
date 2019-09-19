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
          "Slack teams use Carrot to share important news, updates, and decisions nobody should miss."]

        [:div.slack-subline
          "Clear, organized communication. Ideal for remote teams."]

        [:div.slack-button-container.group
            [:a.add-to-slack-button
              {:id "get-started-centred-bt"
               :href (env :slack-signup-url)}]]

        shared/slack-hero-screenshot

        shared/testimonials-logos-line]

      (shared/testimonials-section :slack)

      shared/pricing-footer
      ]])