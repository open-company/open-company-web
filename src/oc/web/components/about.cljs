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
            [oc.web.components.ui.shared-misc :as shared-misc]
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
        [:section.about-header

          [:h1.about
            "Meet the team"]

          [:div.team-container
            [:div.team-row.group.three-cards
              [:div.team-card.iacopo-carraro
                [:div.user-avatar]
                [:div.user-name
                  "Iacopo Carraro"]
                [:div.user-position
                  "Software Engineer"
                  [:br]
                  "in Livorno, Italy"]
                [:div.user-links
                  [:a.twitter-link
                    {:href "https://twitter.com/bago2k4"
                     :target "_blank"}]
                  [:a.linkedin-link
                    {:href "https://linkedin.com/in/iacopocarraro/"
                     :target "_blank"}]]]
              [:div.team-card.sean-johnson
                [:div.user-avatar]
                [:div.user-name
                  "Sean Johnson"]
                [:div.user-position
                  "CTO & Founder"
                  [:br]
                  "in Chapel Hill, USA"]
                [:div.user-links
                  [:a.twitter-link
                    {:href "https://twitter.com/belucid"
                     :target "_blank"}]
                  [:a.linkedin-link
                    {:href "https://linkedin.com/in/snootymonkey/"
                     :target "_blank"}]]]
              [:div.team-card.georgiana-laudi
                [:div.user-avatar]
                [:div.user-name
                  "Georgiana Laudi"]
                [:div.user-position
                  "Marketing & CX Advisor"
                  [:br]
                  "in Montreal, Canada"]
                [:div.user-links
                  [:a.twitter-link
                    {:href "https://twitter.com/ggiiaa"
                     :target "_blank"}]
                  [:a.linkedin-link
                    {:href "https://linkedin.com/in/georgianalaudi/"
                     :target "_blank"}]]]
              [:div.team-card.stuart-levinson
                [:div.user-avatar]
                [:div.user-name
                  "Stuart Levinson"]
                [:div.user-position
                  "CEO & Founder"
                  [:br]
                  "in Cambridge, USA"]
                [:div.user-links
                  [:a.twitter-link
                    {:href "https://twitter.com/stuartlevinson"
                     :target "_blank"}]
                  [:a.linkedin-link
                    {:href "https://linkedin.com/in/stuartlevinson/"
                     :target "_blank"}]]]
              [:div.team-card.ryan-le-roux
                [:div.user-avatar]
                [:div.user-name
                  "Ryan Le Roux"]
                [:div.user-position
                  "Head of Design"
                  [:br]
                  "in Vancouver, Canada"]
                [:div.user-links
                  [:a.twitter-link
                    {:href "https://twitter.com/ryanleroux"
                     :target "_blank"}]
                  [:a.linkedin-link
                    {:href "https://linkedin.com/in/ryanleroux/"
                     :target "_blank"}]]]
              [:div.team-card.nathan-zorn
                [:div.user-avatar]
                [:div.user-name
                  "Nathan Zorn"]
                [:div.user-position
                  "Software Engineer"
                  [:br]
                  "in Charleston, USA"]
                [:div.user-links
                  [:a.twitter-link
                    {:href "https://twitter.com/thepug"
                     :target "_blank"}]
                  [:a.linkedin-link
                    {:href "https://linkedin.com/in/nathanzorn/"
                     :target "_blank"}]]]]]

          [:div.about-copy
            [:div.about-copy-inner
              [:h3.about-copy-header
                "Why we built Carrot"]
              [:div.about-copy-title
                (str
                 "In the age of Slack, the lack of clear leadership "
                 "is the single, greatest problem in the workplace.")]
              [:p
                (str
                 "Sure, teams are more connected than ever with chat apps like Slack - and yet, "
                 "it’s become even more difficult for those same teams to stay focused and aligned "
                 "around what matters most. ")
                [:span.oblique "How can that be?"]]
              [:p
                "This is the question that led us to build Carrot. What we "
                "found is that "
                [:span.heavy "leadership is getting lost in the noise."]]
              [:p
                (str
                 "Sharing key information everyone needs alongside random chats just increases "
                 "the likelihood it will scroll by without being noticed. "
                 "And leaders have no idea if anyone even heard what they said.")]
              [:p
                (str
                 "Heavy Slack users ourselves, we wanted to design a Slack-friendly approach "
                 "to handle leadership communication. We wanted this “non-chat” communication "
                 "to be as fun, delightful and interactive as chat; but we also wanted it to be "
                 "asynchronous so people could get caught up on their own time.")]
              [:p
                (str
                 "The result is Carrot - a platform for leadership communication that keeps "
                 "everyone focused on what matters to build transparency, trust, and stronger teams.")]]

            [:div.about-bottom-copy
              [:div.about-bottom-copy-row.group
                [:div.about-bottom-copy-left
                  "Distributed by design."]
                [:div.about-bottom-copy-right
                  [:div.about-bottom-copy-description
                    (str
                     "Want to join us? We are always looking for "
                     "amazing people regardless of where they "
                     "call home.")]
                  [:a
                    {:href "#hello"
                     :target "_blank"}
                    "Say hello"]]]
              [:div.about-bottom-copy-row.group
                [:div.about-bottom-copy-left
                  "Crazy for open source."]
                [:div.about-bottom-copy-right
                  [:div.about-bottom-copy-description
                    (str
                     "Have an idea you’d like to contribute? A "
                     "new integration you’d like to see?")]
                  [:a
                    {:href "https://github.com/open-company"
                     :target "_blank"}
                    "Build with us on git"]]]]]]

        shared-misc/keep-aligned-bottom] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])