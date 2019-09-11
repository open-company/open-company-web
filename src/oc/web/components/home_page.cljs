(ns oc.web.components.home-page
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.shared-misc :as shared-misc]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defc home-page
  []
  [:div
    (site-header)
    (site-mobile-menu)
    [:div.home-wrap
      {:id "wrap"}
      (login-overlays-handler)
      [:div.main.home-page
        (shared-misc/video-lightbox)
        ; Hope page header
        [:section.cta.group

          [:h1.headline
            "Where teams find clarity"]
          [:div.subheadline
            (str
              "Carrot is the platform for team communication that matters. "
              "Post important updates, news, decisions and stories "
              "that nobody should miss — perfect for remote teams.")]

          [:div.get-started-button-container.group
            [:button.mlb-reset.get-started-button
              {:id "get-started-centred-bt"}
              "Create your team. It’s free!"]]

          [:div.main-animation-container
            [:img.main-animation
              {:src (utils/cdn "/img/ML/homepage_screenshot.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshot@2x.png") " 2x")}]]

          shared-misc/testimonials-logos-line]

        (shared-misc/keep-aligned-section false)

        shared-misc/carrot-in-action

        shared-misc/testimonials-section

        shared-misc/keep-aligned-bottom]]

    (site-footer)])
