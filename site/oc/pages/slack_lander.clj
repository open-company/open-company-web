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
          {:id "get-started-centred-bt"
           :href (env :slack-signup-url)}]

        shared/slack-hero-screenshot

        shared/testimonials-logos-line]]])