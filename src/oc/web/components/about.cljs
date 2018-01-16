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
          [:div.balloon.big-yellow]
          [:div.balloon.big-red]
          [:div.balloon.big-purple]
          [:div.balloon.small-purple-face]
          [:div.balloon.small-red]
          [:div.balloon.small-yellow-face]
          [:div.balloon.small-yellow]
          [:div.balloon.big-purple-1]
          [:div.balloon.small-green]
          [:div.balloon.big-blue]
          [:div.balloon.small-red-2]

          [:h1.about "About"]
          [:div.about-subline
            (str
             "Growing companies struggle to keep everyone on the same page. "
             "Carrot provides the big picture that keeps them together.")]

          [:div.paragraphs-container.group
            [:p
              (str
               "Messaging apps are designed for real-time work. They’re great in the moment, but chat "
               "gets noisy and conversations disappear, making it easy to miss the important stuff.")]
            [:p
              (str
                "Carrot provides an easy to read view of the latest announcements, updates, and stories "
                "so you can always see what’s happening in context. A common, shared view of what’s "
                "important creates real transparency and alignment.")]
            [:p
              "It also brings teams closer so they can grow together."]
            [:p
              "Carrot on!"]

            [:div.principles-title
              "We designed Carrot based on three core principles:"]

            [:div.principles.group
              [:div.principle.left-principle
                [:div.principle-icon]
                [:div.principle-title
                  (str
                   "Alignment should be "
                   "simple and fun.")]
                [:div.principle-description
                  (str
                   "Alignment might be essential for success, "
                   "but achieving it has never been easy. "
                   "We’re changing that. With a simple "
                   "structure and beautiful writing experience, "
                   "it can’t be easier. Just say what’s going on, "
                   "we’ll take care of the rest.")]]

              [:div.principle.right-principle.group
                [:div.principle-icon]
                [:div.principle-title
                  (str
                   "The “big picture” should "
                   "always be visible.")]
                [:div.principle-description
                  (str
                   "No one wants to look through folders and "
                   "documents to understand what’s going on, "
                   "or search through chat messages to find "
                   "something. It should be easy to get an "
                   "instant, bird’s-eye view of what’s "
                   "happening across the company anytime.")]]]

            [:div.principle.center-principle.group
              [:div.principle-icon]
              [:div.principle-title
                (str
                 "It should be easy to keep "
                 "stakeholders in the loop, too.")]
              [:div.principle-description
                (str
                 "Sharing the latest with stakeholders "
                 "shouldn’t be a chore. Just give investors, "
                 "customers and others their own big picture "
                 "view. It’s the surest way to keep them "
                 "engaged and supportive, and an easy way "
                 "to grow your business.")]]]]

        [:section.about-team.group
          [:div.about-team-inner.group
            [:h1.team "Our team"]

            [:div.about-team-users.group
              [:div.column-left.group
                [:div.team-card.stuart-levinson
                  [:div.team-avatar
                    [:img {:src "http://www.gravatar.com/avatar/99399ee082e57d67045cb005f9c2e4ef?s=100"}]]
                  [:div.team-member
                    [:div.team-name "Stuart Levinson"]
                    [:div.team-title "CEO and founder"]
                    [:div.team-media-links
                      [:a.linkedin {:href "https://linkedin.com/in/stuartlevinson"}]]]]
                [:div.team-card.iacopo-carraro
                  [:div.team-avatar
                    [:img {:src "http://www.gravatar.com/avatar/0224b757acf053e02d8cdf807620417c?s=100"}]]
                  [:div.team-member
                    [:div.team-name "Iacopo Carraro"]
                    [:div.team-title "Software Engineer"]
                    [:div.team-media-links
                      [:a.linkedin {:href "https://www.linkedin.com/in/iacopocarraro"}]]]]]

              [:div.column-right.group
                [:div.team-card.sean-johnson
                  [:div.team-avatar
                    [:img {:src "http://www.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04?s=100"}]]
                  [:div.team-member
                    [:div.team-name "Sean Johnson"]
                    [:div.team-title "CTO and founder"]
                    [:div.team-media-links
                      [:a.linkedin {:href "https://linkedin.com/in/snootymonkey"}]]]]
                [:div.team-card.nathan-zorn
                  [:div.team-avatar
                    [:img {:src "https://s.gravatar.com/avatar/e7407a2aefa6b5a54a0af630a0a58210?s=100"}]]
                  [:div.team-member
                    [:div.team-name "Nathan Zorn"]
                    [:div.team-title "Software Engineer"]
                    [:div.team-media-links
                      [:a.linkedin {:href "https://www.linkedin.com/in/nathanzorn"}]]]]]]

            [:div.about-team-users.group
              [:div.column-center.group
                [:div.team-card.new-member
                  [:div.team-avatar]
                    [:div.team-member
                      [:div.team-name "You?"]
                      [:div.team-title "We’re always looking for talented"]
                      [:div.team-title "people to join us."]]]]]]]

        [:section.about-footer.group

          [:div.block.join-us
            [:div.block-title
              "Join Us"]
            [:div.block-description
              "Want to join us? We are always looking for amazing people no matter where they live."]
            [:a.link
              {:href oc-urls/contact-mail-to}
              "Say hello"]]

          [:div.block.open-source
            [:div.block-title
              "Open Source"]
            [:div.block-description
              "Have an idea you’d like to contribute? A new integration you’d like to see?"]
            [:a.link
              {:href "https://github.com/open-company"}
              "Build it with us on Github"]]]

        [:section.fourth-section.group
          [:div.above-noise-container
            [:div.above-noise-title
              "Rise above the noise"]
            [:div.above-noise-description
              "Give your team a clear view of what’s most important."]
            [:button.mlb-reset.get-started-button
             {:on-click #(if (utils/in? (:route @router/path) "login")
                           (user/show-login :signup-with-slack)
                           (router/nav! oc-urls/sign-up))}
              "Get started for free"]]]
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])