(ns oc.pages.index
  (:require [oc.pages.shared :as shared]))

(defn index [options]
  [:div.home-wrap.group
    {:id "wrap"}
    [:div.main.home-page
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Where remote teams have real discussions"]
        [:div.subheadline.big-web-only
          (str
           "It’s hard for distributed teams to stay in-sync with chat alone. "
           "Carrot makes asynchronous communication less noisy so it's easier "
           "to stay engaged and informed without the chatter.")]
        [:div.subheadline.mobile-only
          (str
           "Carrot makes asynchronous communication less "
           "noisy so everyone can stay informed with fewer interruptions.")]

        [:div.get-started-button-container.group
          [:button.mlb-reset.get-started-button.get-started-action
            {:id "get-started-centred-bt"}
            "Try Carrot for free"]
          ; [:span.get-started-subtitle
          ;   "Free for small teams"]
            ]

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