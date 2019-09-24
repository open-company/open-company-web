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
               "Chat apps keep everyone connected throughout the day, and yet "
               "it's become even more difficult for teams to stay "
               "aligned around what matters most. How can that be?")]
            [:p
              "This is the question that led us to build Carrot. What we "
              "found is that leadership is getting lost in the noise."]
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
               "everyone focused on what matters to build transparency, trust, and stronger teams.")]]]]

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
                   "Build with us on GitHub"]
                [:a
                  {:href "https://trello.com/b/eKs2LtLu/carrot-roadmap-https-carrotio"
                   :target "_blank"}
                   "View our roadmap"]]]]]]

    (shared/dashed-string 1)

    shared/pricing-footer

    ] ;<!-- main -->
  ])