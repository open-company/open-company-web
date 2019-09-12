(ns oc.pages.index
  (:require [oc.pages.shared :as shared]))

(defn index [options]
  [:div.home-wrap
    {:id "wrap"}
    [:div.main.home-page
      shared/animation-lightbox
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Where teams find clarity"]
        [:div.subheadline
          (str
           "Carrot organizes your team's communication so it's easier to stay "
           "engaged and informed. Ideal for keeping remote teams in sync.")]

        [:div.get-started-button-container.group
          [:button.mlb-reset.get-started-button.get-started-action
            {:id "get-started-centred-bt"}
            "Create your team. It’s free!"]
          [:span.get-started-subtitle
            "Carrot is open source and free for all teams."]]

        [:div.main-animation-container
          [:picture.main-animation
            [:source {:srcSet (shared/cdn "/img/ML/homepage_screenshot.png")}]
            [:img {:src (shared/cdn "/img/ML/homepage_screenshot.png")
                   :alt "Carrot"
                   :srcSet (str (shared/cdn "/img/ML/homepage_screenshot@2x.png") " 2x, "
                            (shared/cdn "/img/ML/homepage_screenshot@3x.png") " 3x, "
                            (shared/cdn "/img/ML/homepage_screenshot@4x.png") " 4x")}]]]

        shared/testimonials-logos-line]

      shared/testimonials-section

      ; (shared/keep-aligned-section false)

      shared/pricing-table-section

      shared/carrot-in-action

      shared/keep-aligned-bottom
      ]])