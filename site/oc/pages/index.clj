(ns oc.pages.index
  (:require [oc.shared :as shared]))

(defn index [options]
  [:div.home-wrap.group
   {:id "wrap"}
   [:div.main.home-page
      ; Hope page header
    [:section.cta.group
        [:h1.headline
         "Team updates and newsletters that inspire alignment"]
        [:div.subheadline
         "Keep your team focused on what matters with more effective internal communication."]

        [:div.get-started-button-container.group
          [:a.get-started-button.get-started-action
            {:href "/sign-up"}
            "Try Carrot for free"]
          [:span.get-started-subtitle
            "Carrot is open source."]]

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

        ;; shared/testimonials-logos-line
        ]

      (shared/testimonials-section :index)

      ;; [:section.video
      ;;   [:div.main-animation-container
      ;;     [:img.main-animation
      ;;       {:src (shared/cdn "/img/ML/homepage_screenshot.png")
      ;;        :alt "Carrot"
      ;;        :srcSet (str
      ;;                 (shared/cdn "/img/ML/homepage_screenshot@2x.png") " 2x, "
      ;;                 (shared/cdn "/img/ML/homepage_screenshot@3x.png") " 3x")}]]]

    shared/pricing-footer]])