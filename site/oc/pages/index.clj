(ns oc.pages.index
  (:require [oc.pages.shared :as shared]))

(defn index [options]
  [:div.home-wrap.group
    {:id "wrap"}
    [:div.main.home-page
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Remote teams communicate differently"]
        [:div.subheadline.big-web-tablet-only
          "Teams struggle to stay in-sync with chat alone." [:br]
          "With Carrot, distributed teams have productive,"[:br]
          "asynchronous discussions without the chatter."]
        [:div.subheadline.mobile-only
          "It’s hard to stay in-sync with chat alone. Stay informed without the chatter."]

        [:div.get-started-button-container.group
          [:button.mlb-reset.get-started-button.get-started-action
            {:id "get-started-centred-bt"}
            "Try Carrot for free"]
          [:span.get-started-subtitle
            "Carrot is open source. Join us."]]

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