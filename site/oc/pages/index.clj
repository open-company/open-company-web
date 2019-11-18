(ns oc.pages.index
  (:require [oc.pages.shared :as shared]))

(defn index [options]
  [:div.home-wrap
    {:id "wrap"}
    [:div.main.home-page
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Better team discussions"]
        [:div.subheadline
          (str
           "Carrot makes asynchronous communication less noisy, so your "
           "team can stay focused and informed with fewer interruptions.")]

        [:div.get-started-button-container.group
          [:button.mlb-reset.get-started-button.get-started-action
            {:id "get-started-centred-bt"}
            "Try Carrot"]
          [:span.get-started-subtitle
            "Free 14 day trial"]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (shared/cdn "/img/ML/homepage_screenshot.png")
             :alt "Carrot"
             :srcSet (str
                      (shared/cdn "/img/ML/homepage_screenshot@2x.png") " 2x, "
                      (shared/cdn "/img/ML/homepage_screenshot@3x.png") " 3x, "
                      (shared/cdn "/img/ML/homepage_screenshot@4x.png") " 4x")}]]

        shared/testimonials-logos-line]

      (shared/testimonials-section :index)

      shared/pricing-footer
      ]])