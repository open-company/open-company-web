(ns oc.web.components.about
  "About page component, this is copied into oc.pages/about and
   every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs about
  [s]
  [:div
    [:div.about-wrap
      {:id "wrap"} ; <!-- used to push footer to the bottom -->

      (site-header)
      (site-mobile-menu)
      (login-overlays-handler)

      [:div.main.about
        [:section.about-header.group
          [:div.balloon.big-red]
          [:div.balloon.big-green]
          [:div.balloon.small-green-face]
          [:div.balloon.small-blue]
          [:div.balloon.small-purple]

          [:h1.about "About us"]
          [:div.about-subline
            (str
             "We empower leaders to create alignment by keeping teams "
             "focused on what matters most.")]

          [:div.team-container
            [:div.team-row.group.three-cards
              [:div.team-card.iacopo-carraro
                [:div.user-avatar]
                [:div.user-name
                  "Iacopo Carraro"]
                [:div.user-position
                  "Software Engineer"]
                [:a.linkedin-link
                  {:href "https://www.linkedin.com/in/iacopocarraro/"
                   :target "_blank"}]]
              [:div.team-card.sean-johnson
                [:div.user-avatar]
                [:div.user-name
                  "Sean Johnson"]
                [:div.user-position
                  "CTO & Founder"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/snootymonkey/"
                   :target "_blank"}]]
              [:div.team-card.stuart-levinson
                [:div.user-avatar]
                [:div.user-name
                  "Stuart Levinson"]
                [:div.user-position
                  "CEO & Founder"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/stuartlevinson/"
                   :target "_blank"}]]]
            [:div.team-row.group.two-cards
              [:div.team-card.ryan-le-roux
                [:div.user-avatar]
                [:div.user-name
                  "Ryan Le Roux"]
                [:div.user-position
                  "CDO"]
                [:a.linkedin-link
                  {:href "https://www.linkedin.com/in/ryanleroux/"
                   :target "_blank"}]]
              [:div.team-card.nathan-zorn
                [:div.user-avatar]
                [:div.user-name
                  "Nathan Zorn"]
                [:div.user-position
                  "Software Engineer"]
                [:a.linkedin-link
                  {:href "https://www.linkedin.com/in/nathanzorn/"
                   :target "_blank"}]]]]]
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])