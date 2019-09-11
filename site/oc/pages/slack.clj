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
      shared/animation-lightbox
      ; Hope page header
      [:section.carrot-plus-slack.group

        [:h1.slack-headline
          "Where teams find clarity"]
        
        [:div.slack-subline
          (str
            "Carrot is the platform for team communication that matters. "
            "Post important updates, news, decisions and stories "
            "that nobody should miss — perfect for remote teams.")]

        [:div.slack-button-container.group
            [:a.add-to-slack-button
              {:id "get-started-centred-bt"
               :href (env :slack-signup-url)}]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (shared/cdn "/img/ML/slack_screenshot.png")
             :srcSet (str (shared/cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]

        shared/testimonials-logos-line]

      (shared/keep-aligned-section true)

      shared/carrot-in-action

      shared/testimonials-section

      shared/keep-aligned-bottom
      ]])