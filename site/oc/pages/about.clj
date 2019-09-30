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
               "Teams are hyper-connected throughout the day, and yet "
               "it's become even more difficult to know what matters. "
               "How can that be?")]
            [:p
              (str
               "This is the question that led us to build Carrot. What we found is "
               "that important and thoughtful communication gets lost in the noise.")]
            [:p
              (str
               "That problem is even worse for distributed teams working in different "
               "places or time zones. Sharing important team updates, news, and decisions "
               "alongside real-time chats just increases the likelihood it will scroll "
               "by without being noticed. Plus, it’s difficult to know if anyone even "
               "hears what you’ve said.")]
            [:p
              (str
               "Heavy Slack users ourselves, we wanted to design a Slack-friendly approach "
               "to transform asynchronous communication into something more useful. "
               "We wanted this “non-chat” communication to be as fun, delightful and "
               "interactive as chat; but organized in a way that made it easier for "
               "everyone to get caught up on their own time.")]

            [:p
              (str
               "The result is Carrot - a platform for remote team communication that "
               "keeps everyone focused on what matters to build transparency, "
               "trust, and stronger teams.")]]]]

      [:section.team-section
        [:div.team-section-block
          [:div.team-section-header
            "Those that helped make Carrot possible"]
          [:div.team-section-icons.group
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_1.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_1@2x.png") " 2x")}]
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_2.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_2@2x.png") " 2x")}]
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_3.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_3@2x.png") " 2x")}]
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_4.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_4@2x.png") " 2x")}]
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_5.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_5@2x.png") " 2x")}]
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_6.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_6@2x.png") " 2x")}]
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_7.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_7@2x.png") " 2x")}]
            [:img.team-section-icon
              {:src (shared/cdn "/img/ML/team_section_8.png")
               :srcSet (str (shared/cdn "/img/ML/team_section_8@2x.png") " 2x")}]]

          [:div.team-section-bottom-copy
            [:div.team-section-bottom-copy-row.group
              [:div.team-section-bottom-copy-left
                "Distributed by design"]
              [:div.team-section-bottom-copy-right
                [:div.team-section-bottom-copy-description
                  (str
                   "Want to join us? We are always looking for "
                   "amazing people regardless of where they "
                   "call home.")]
                [:a
                  {:href "https://github.com/open-company"
                   :target "_blank"}
                   "Say hello"]]]]

          [:div.team-section-bottom-copy
            [:div.team-section-bottom-copy-row.group
              [:div.team-section-bottom-copy-left
                "Carrot is open source"]
              [:div.team-section-bottom-copy-right
                [:div.team-section-bottom-copy-description
                  (str
                   "Have an idea you'd like to contribute? A new integration you'd like to see?")]
                [:a
                  {:href "https://github.com/open-company"
                   :target "_blank"}
                   "Build with us on GitHub"]]]]]]

    (shared/dashed-string 1)

    shared/pricing-footer

    ] ;<!-- main -->
  ])