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
        [:div.about-header
          [:h1.about "About"]

          [:div.divider-line]

          [:div.ovals-container.group

            [:div.ovals-container-face.face-red]
            [:div.ovals-container-face.face-yellow]
            [:div.ovals-container-face.face-blue]
            [:div.ovals-container-face.face-green]
            [:div.ovals-container-face.face-purple]

            [:div.about-subline
              "Growing companies struggle to keep everyone on the same page. They grow apart."]
            [:div.paragraphs-container.group
              [:div.mobile-only.happy-face.yellow-happy-face]
              [:div.mobile-only.happy-face.red-happy-face]
              [:div.paragraphs-bg-container.group
                [:div.paragraph
                  "Messaging apps are designed for real-time work. They’re great in the moment, but chat gets noisy and conversations disappear, making it difficult to have a big picture view of what’s important across the company."]
                [:div.paragraph
                  "Carrot provides a birds-eye view of the latest announcements, updates, and stories so you can always see what’s happening in context. A common, shared view of what’s important - and the opportunity to react and ask questions about it - creates real transparency and alignment."]
                [:div.paragraph
                  "It also brings teams closer so they can grow together."]
                [:div.paragraph
                  "Carrot on!"]]
              [:div.mobile-only.happy-face.blue-happy-face]
              [:div.mobile-only.happy-face.purple-happy-face]
              [:div.mobile-only.happy-face.green-happy-face]]]]

        [:div.principles.group

          [:div.principles-headline
            "We designed Carrot based on 3 principles:"]

          [:div.principle.principle-1
            [:div.principle-oval-bg]
            [:div.principle-logo]
            [:div.principle-title "Alignment should be simple."]
            [:div.principle-description "Alignment might be essential for success, but achieving it has never been simple or fun. We’re changing that. With a simple structure, and beautiful writing experience, it can’t be easier. Just say what’s going on, we’ll take care of the rest."]]

          [:div.principle.principle-2
            [:div.principle-oval-bg]
            [:div.principle-logo]
            [:div.principle-title "The “big picture” should always be visible."]
            [:div.principle-description "No one wants to look through folders and documents to understand what’s going on, or search through chat messages to find something. It should be easy to get an instant, birds-eye view of what’s happening across the company anytime."]]

          [:div.principle.principle-3
            [:div.principle-oval-bg]
            [:div.principle-logo]
            [:div.principle-title "Alignment is valuable beyond the team, too."]
            [:div.principle-description "Make it simple to share the latest updates with investors, advisors, customers and other outside stakeholders as well. It’s the surest way to keep them engaged and supportive, and an easy way to expand your network and grow your business."]]]

      ] ;<!-- main -->

      [:div.about-alignment
        [:div.quote]
        [:div.about-alignment-description "Company alignment requires real openness and transparency. Carrot makes it simple and fun for your team to get started."]]

      [:div.about-team.group
        [:div.about-team-inner.group
          [:h1.team "Our team"]
          [:div.divider-line]

          [:div.group
            [:div.column-left.group
              [:div.team-card.stuart-levinson
                [:div.team-avatar
                  [:img {:src "http://www.gravatar.com/avatar/99399ee082e57d67045cb005f9c2e4ef?s=64"}]]
                [:div.team-member
                  [:div.team-name "Stuart Levinson"]
                  [:div.team-title "CEO and cofounder"]
                  [:div.team-description "Prior to Carrot, Stuart started two venture-backed startups - Venetica (acquired by IBM) and TalkTo (acquired by Path). Those experiences, pre- and post-acquisitions, inspired a passion for transparency and its effect on overall alignment."]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://linkedin.com/in/stuartlevinson"}]
                    [:a.twitter {:href "https://twitter.com/stuartlevinson"}]]]]
              [:div.team-card.iacopo-carraro
                [:div.team-avatar
                  [:img {:src "http://www.gravatar.com/avatar/0224b757acf053e02d8cdf807620417c?s=64"}]]
                [:div.team-member
                  [:div.team-name "Iacopo Carraro"]
                  [:div.team-description "Iacopo is a full-stack engineer with lots of remote team and startup experience."]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://www.linkedin.com/pub/iacopo-carraro/21/ba2/5ab"}]
                    [:a.twitter {:href "http://twitter.com/bago2k4"}]
                    [:a.github {:href "http://github.com/bago2k4"} [:i.fa.fa-github]]]]]]

            [:div.column-right.group
              [:div.team-card.sean-johnson
                [:div.team-avatar
                  [:img {:src "http://www.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04?s=64"}]]
                [:div.team-member
                  [:div.team-name "Sean Johnson"]
                  [:div.team-title "CTO and cofounder"]
                  [:div.team-description "As a serial startup CTO and engineer, Sean has over 20 years experience building products and startup engineering teams."]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://linkedin.com/in/snootymonkey"}]
                    [:a.twitter {:href "http://twitter.com/belucid"}]
                    [:a.github {:href "http://github.com/belucid"} [:i.fa.fa-github]]]]]
              [:div.team-card.new-member
                [:div.team-avatar]
                  [:div.team-member
                    [:div.team-name "You?"]
                    [:div.team-description "We're always looking for talented individuals. Drop us a line if you share our mission."]]]]]]]

      [:div.about-footer.group

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
    ] ; <!-- wrap -->

    (site-footer)])