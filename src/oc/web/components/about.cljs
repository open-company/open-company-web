(ns oc.web.components.about
  "About page component, this is copied into oc.pages/about and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs about < rum/reactive [s]
  [:div
    [:div.about-wrap {:id "wrap"} ; <!-- used to push footer to the bottom --> 

      (site-header)
      (login-overlays-handler)

      [:div.container.main.about

        [:h1.about "About"]

        [:div.divider-line]

        [:div.ovarls-container.group

          [:div.about-subline
            "Transparency is a powerful motivator for teams. It creates a sense of ownership, and builds trust and clarity of purpose."]
          [:div.paragraphs-container.group
            (when (responsive/is-mobile-size?)
              [:div.mobile-only.happy-face.yellow-happy-face])
            (when (responsive/is-mobile-size?)
              [:div.mobile-only.happy-face.red-happy-face])
            [:div.mobile-paragraphs-container.group
              [:div.paragraph
                "Serial entrepreneurs ourselves, we’ve learned most of all that transparency is essential for company alignment."]
              [:div.paragraph
                "Still, we could never find easy solutions to help us keep growing and distributed teams on the same page."]
              [:div.paragraph
                "Now we’ve built the solution we wanted ourselves, based on three principles:"]]
            (when (responsive/is-mobile-size?)
              [:div.mobile-only.happy-face.blue-happy-face])
            (when (responsive/is-mobile-size?)
              [:div.mobile-only.happy-face.purple-happy-face])
            (when (responsive/is-mobile-size?)
              [:div.mobile-only.happy-face.green-happy-face])]]

        [:div.principles.group
          [:div.principle.principle-1
            [:div.principle-oval-bg]
            [:div.principle-logo]
            [:div.principle-title "It has to be easy or no one will play."]
            [:div.principle-description "Alignment might be essential for success, but achieving it has never been easy or fun. We’re changing that. With a simple structure and beautiful writing experience, it can’t be easier. Just say what’s going on, we’ll take care of the rest."]]

          [:div.principle.principle-2
            [:div.principle-oval-bg]
            [:div.principle-logo]
            [:div.principle-title "The “big picture” should always be visible. "]
            [:div.principle-description "No one wants to drill into folders or documents to understand what’s going on, or search through corporate chat archives to find something. It should be easier to get an instant view of what’s happening across the company anytime."]]

          [:div.principle.principle-3
            [:div.principle-oval-bg]
            [:div.principle-logo]
            [:div.principle-title "Alignment is valuable beyond the team, too."]
            [:div.principle-description "Once you’ve created the big picture, you should be able to share it with anyone, inside and outside the company. Sharing beautiful updates with recruits, investors and customers is the surest way to keep them engaged and supportive."]]]

      ] ;<!-- main -->

      [:div.about-alignment
        [:div.quote]
        [:div.about-alignment-description "Company alignment requires real openness and transparency."]]

      [:div.about-team.group
        [:div.about-team-inner.group
          [:h1.team "Our team"]
          [:div.divider-line]

          [:div.group
            [:div.column-left.group
              [:div.team-card
                [:div.team-avatar]
                [:div.team-member
                  [:div.team-name "Stuart Levinson"]
                  [:div.team-description "Prior to founding OpenCompany, Stuart started and sold two venture-backed startups. Venetica (acquired by IBM) pioneered a new type of enterprise integration software, and TalkTo (acquired by Path) launched the first messaging app to local businesses powered by a human + AI backend."]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://linkedin.com/in/stuartlevinson"}]
                    [:a.twitter {:href "https://twitter.com/stuartlevinson"}]]]]
              [:div.team-card.new-member
                [:div.team-avatar]
                  [:div.team-member
                    [:div.team-name "You?"]
                    [:div.team-description "We're always looking for talented individuals. Drop us a line if you share our mission."]]]]

            [:div.column-right.group
              [:div.team-card
                [:div.team-avatar]
                [:div.team-member
                  [:div.team-name "Sean Johnson"]
                  [:div.team-description "As a serial startup CTO and engineer, Sean has over 20 years experience building products and startup engineering teams."]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://linkedin.com/in/snootymonkey"}]
                    [:a.twitter {:href "http://twitter.com/belucid"}]
                    [:a.github {:href "http://github.com/belucid"} [:i.fa.fa-github]]]]]
              [:div.team-card
                [:div.team-avatar]
                [:div.team-member
                  [:div.team-name "Iacopo Carraro"]
                  [:div.team-description "Iacopo is a full-stack engineer with lots of remote team and startup experience."]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://www.linkedin.com/pub/iacopo-carraro/21/ba2/5ab"}]
                    [:a.twitter {:href "http://twitter.com/bago2k4"}]
                    [:a.github {:href "http://github.com/bago2k4"} [:i.fa.fa-github]]]]]]]]]

      [:div.about-footer.group

        [:div.block.join-us
          [:div.block-title
            "Join Us"]
          [:div.block-description
            "Want to join us? We are always looking for amazing people no matter where they live."]
          [:a.link
            {:a "mailto:hello@carrot.io"}
            "Say hello"]]

        [:div.block.open-source
          [:div.block-title
            "Open Source"]
          [:div.block-description
            "Have an idea you’d like to contribute? A new integration you’d like to see?"]
          [:a.link
            {:href "https://github.com/open-company"}
            "Build it with us on Github"]]]
    ] ; <!-- wrap -->

    (site-footer)])