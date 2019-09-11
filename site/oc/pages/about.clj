(ns oc.pages.about
  (:require [oc.pages.shared :as shared]))

(defn about
  "About page. This is a copy of oc.web.components.about and
   every change here should be reflected there and vice versa."
  [options]
  [:div.about-wrap
    {:id "wrap"}
    [:div.main.about
      [:section.about-header

        ; [:h1.about
        ;   "Meet the team"]

        ; [:div.team-container
        ;   [:div.team-row.group.three-cards
        ;     [:div.team-card.iacopo-carraro
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Iacopo Carraro"]
        ;       [:div.user-position
        ;         "Software Engineer"
        ;         [:br]
        ;         "in Livorno, Italy"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/bago2k4"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/iacopocarraro/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.sean-johnson
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Sean Johnson"]
        ;       [:div.user-position
        ;         "CTO & Founder"
        ;         [:br]
        ;         "in Chapel Hill, USA"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/belucid"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/snootymonkey/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.georgiana-laudi
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Georgiana Laudi"]
        ;       [:div.user-position
        ;         "Marketing & CX Advisor"
        ;         [:br]
        ;         "in Montreal, Canada"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/ggiiaa"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/georgianalaudi/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.stuart-levinson
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Stuart Levinson"]
        ;       [:div.user-position
        ;         "CEO & Founder"
        ;         [:br]
        ;         "in Cambridge, USA"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/stuartlevinson"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/stuartlevinson/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.ryan-le-roux
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Ryan Le Roux"]
        ;       [:div.user-position
        ;         "Head of Design"
        ;         [:br]
        ;         "in Vancouver, Canada"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/ryanleroux"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/ryanleroux/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.nathan-zorn
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Nathan Zorn"]
        ;       [:div.user-position
        ;         "Software Engineer"
        ;         [:br]
        ;         "in Charleston, USA"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/thepug"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/nathanzorn/"
        ;            :target "_blank"}]]]]]

        [:h3.about-copy-header
          "Why we built Carrot"]

        [:div.about-copy
          [:div.about-copy-inner
            [:p
              (str
               "In the age of Slack and fast-moving conversations, the lack of "
               "focus and clarity have become a huge problem in the workplace.")]
            [:p
              (str
               "Chat apps keep everyone connected throughout the day, and yet "
               "it's become even more difficult for teams to stay "
               "aligned around what matters most. ")
              [:span.oblique "How can that be?"]]
            [:p
              "This is the question that led us to build Carrot. What we "
              "found is that "
              [:span.heavy "leadership is getting lost in the noise."]]
            [:p
              (str
               "Sharing important team updates, news, and decisions alongside random chats "
               "just increases the likelihood it will scroll by without being noticed. "
               "It’s difficult for the team to know what matters, and leaders have no idea "
               "if anyone even heard what they said.")]
            [:p
              (str
               "Heavy Slack users ourselves, we wanted to design a Slack-friendly approach "
               "to handle must-see communication. We wanted this “non-chat” communication "
               "to be as fun, delightful and interactive as chat; but we also wanted it "
               "to be asynchronous so people could get caught up on their own time.")]
            [:p
              (str
               "The result is Carrot - a platform for must-see communication that keeps "
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
                  {:class "intercom-chat-link"
                   :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
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
                  "Build with us on git"]
                [:span.link-or "or"]
                [:a
                  {:href "https://trello.com/b/eKs2LtLu"
                   :target "_blank"}
                  "VISIT OUR ROADMAP"]]]]]]

      shared/keep-aligned-bottom
    ] ;<!-- main -->
  ])