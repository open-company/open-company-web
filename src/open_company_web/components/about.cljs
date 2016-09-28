(ns open-company-web.components.about
  (:require [rum.core :as rum]
            [open-company-web.components.ui.site-header :refer (site-header)]
            [open-company-web.components.ui.site-footer :refer (site-footer)]
            [open-company-web.urls :as oc-urls]))

(rum/defc about []
  [:div
    [:div.about-wrap {:id "wrap"} ; <!-- used to push footer to the bottom --> 

      (site-header)

      [:div.container.main.about

        [:p "Transparency is powerful."]
        [:p "When information is shared openly, it inspires new ideas and new levels of stakeholder engagement. It creates a sense of ownership, and builds trust and clarity of purpose."]
        [:p "Despite the immense benefits, it's not easy to pull company information together into something worthy of being shared. Writing long emails or building slides is cumbersome and no one enjoys receiving them."]
        [:p "OpenCompany makes it easy to create beautiful company updates that keep everyone engaged and on the same page!"]

        [:h1.about "The Team"]

        [:p [:b "Stuart Levinson"] " (Cambridge, MA)"[:br]
        "Founder & CEO "
        [:a {:href "https://linkedin.com/in/stuartlevinson" :title "Stuart on LinkedIn"}[:i.fa.fa-linkedin-square]]
        " "
        [:a {:href "https://twitter.com/stuartlevinson" :title "Stuart on Twitter"}[:i.fa.fa-twitter {:aria-hidden "true"}]]
        " "
        [:a {:href "https://wiselike.com/stuart-levinson" :title "Ask Me Anything"} "AMA"]]
        [:p "Prior to founding OpenCompany, Stuart started and sold two venture-backed startups. Venetica (acquired by IBM) pioneered a new type of enterprise integration software, and TalkTo (acquired by Path) launched the first messaging app to local businesses powered by a human + AI backend."]
        [:blockquote.blockquote [:p "\"When I think about lessons learned as a startup founder, I see so much that could've been improved or fixed more quickly if I had been more transparent with the whole team from day one. My passion for open companies is the result.\""]]

        [:p.person [:b "Sean Johnson"] " (Chapel Hill, NC)"[:br]
        "Founder & CTO "
        [:a {:href "https://linkedin.com/in/snootymonkey" :title "Sean on LinkedIn"}[:i.fa.fa-linkedin-square {:aria-hidden "true"}]]
        " "
        [:a {:href "http://twitter.com/belucid" :title "Sean on Twitter"}[:i.fa.fa-twitter {:aria-hidden "true"}]]
        " "
        [:a {:href "http://github.com/belucid" :title "Sean on GitHub"}[:i.fa.fa-github {:aria-hidden "true"}]]
        " "
        [:a {:href "https://wiselike.com/sean-johnson" :title "Ask Me Anything"} "AMA"]]
        [:p "As a serial startup CTO and engineer, Sean has over 20 years experience building products and startup engineering teams."]
        [:blockquote.blockquote [:p "\"I’ve poured all my experience into our" [:a {:href "https://github.com/open-company/developer-onboarding"} "open engineering culture"] ". I’m confident we are building the right technology and the right team to realize our vision.\""]]
        
        [:p.person [:b "Iacopo Carraro"] " (Florence, Italy)"[:br]
        "Software Engineer "
        [:a {:href "https://www.linkedin.com/pub/iacopo-carraro/21/ba2/5ab" :title "Iacopo on LinkedIn"}[:i.fa.fa-linkedin-square {:aria-hidden "true"}]]
        " "
        [:a {:href "http://twitter.com/bago2k4" :title "Iacopo on Twitter"}[:i.fa.fa-twitter {:aria-hidden "true"}]]
        " "
        [:a {:href "http://github.com/bago2k4" :title "Iacopo on GitHub"}[:i.fa.fa-github {:aria-hidden "true"}]]]
        [:p "Iacopo is a full-stack engineer with lots of remote team and startup experience."]
        [:blockquote.blockquote [:p "\"This is my first time working on a project that’s entirely open source, and it's great. Open source software is recursive: the more OSS you put out, the more you benefit. OSS builds communities and is a great example for how humanity should work on problems.\""]]
        

        [:h1.about "Join Us"]
        [:p "As an Open Source Software project, you can follow our progress or contribute on " [:a {:href "https://github.com/open-company"} "GitHub"] "."]
        [:p "Want to join us? We are always looking for amazing people no matter where they live. " [:a {:href (str "mailto:" oc-urls/contact-email)} "Say hello!"]]
      
      ] ;<!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])