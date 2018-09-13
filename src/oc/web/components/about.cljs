(ns oc.web.components.about
  "About page component, this is copied into oc.pages/about and
   every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.user :as user]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
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
          [:h1.about "About us"]
          [:div.about-subline
            "We believe real transparency and alignment requires focused communication."]
          [:div.about-copy
            [:p
              (str
               "Workplace chat is everywhere, and "
               "yet teams are still struggling to "
               "stay on the same page - especially "
               "growing or distributed teams.")]
            [:p
              (str
               "Chat might be ideal for fast and "
               "spontaneous conversations in the moment, "
               "but it gets noisy and drowns out "
               "important information and follow-on "
               "discussions that teams need to stay "
               "aligned over time.")]
            [:p
              (str
               "Carrot is a company digest that gives "
               "everyone time to read and react to "
               "important information without worrying "
               "they missed it.")]
            [:p
              (str
               "When it’s this easy to see what matters "
               "most, busy teams stay informed and "
               "aligned with fewer distractions.")]]

          [:div.team-container
            [:div.team-row.group.three-cards
              [:div.team-card.iacopo-carraro
                [:div.user-avatar]
                [:div.user-name
                  "Iacopo Carraro"]
                [:div.user-position
                  "Software Engineer"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/iacopocarraro/"
                   :target "_blank"}]]
              [:div.team-card.sean-johnson
                [:div.user-avatar]
                [:div.user-name
                  "Sean Johnson"]
                [:div.user-position
                  "CTO and co-founder"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/snootymonkey/"
                   :target "_blank"}]]
              [:div.team-card.georgiana-laudi
                [:div.user-avatar]
                [:div.user-name
                  "Georgiana Laudi"]
                [:div.user-position
                  "Marketing and CX Advisor"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/georgianalaudi/"
                   :target "_blank"}]]]
            [:div.team-row.group.two-cards
              [:div.team-card.stuart-levinson
                [:div.user-avatar]
                [:div.user-name
                  "Stuart Levinson"]
                [:div.user-position
                  "CEO and co-founder"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/stuartlevinson/"
                   :target "_blank"}]]
              [:div.team-card.ryan-le-roux
                [:div.user-avatar]
                [:div.user-name
                  "Ryan Le Roux"]
                [:div.user-position
                  "Chief Design Officer"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/ryanleroux/"
                   :target "_blank"}]]
              [:div.team-card.nathan-zorn
                [:div.user-avatar]
                [:div.user-name
                  "Nathan Zorn"]
                [:div.user-position
                  "Software Engineer"]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/nathanzorn/"
                   :target "_blank"}]]]]

          [:div.other-cards.group
            [:div.other-card.heart-card
              [:div.card-icon]
              [:div.card-title
                "Careers at Carrot"]
              [:div.card-content
                (str
                 "Want to join us? We are always looking for "
                 "amazing people no matter where they live. ")]
              [:a.card-button
                {:href oc-urls/contact-mail-to
                 :on-touch-start identity}
                "Say hello!"]]
            [:div.other-card.oss-card
              [:div.card-icon]
              [:div.card-title
                "We’re Crazy for Open Source"]
              [:div.card-content
                (str
                 "Have an idea you’d like to contribute? A new "
                 "integration you’d like to see?")]
              [:a.card-button
                {:href "https://github.com/open-company"
                 :on-touch-start identity
                 :target "_blank"}
                "Build with us on GitHub"]]]

          [:div.about-bottom-get-started
            [:div.about-alignment
              "Keep everyone aligned around what matters most."]
            [:div.get-started-button-container
              (when-not (jwt/jwt)
                [:button.mlb-reset.get-started-button
                  {:on-click #(if (utils/in? (:route @router/path) "login")
                                (user/show-login :signup-with-slack)
                                (router/nav! oc-urls/sign-up))}
                  "Get started for free"])]]]
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])