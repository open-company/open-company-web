(ns oc.web.components.ui.site-footer
  "Component for the site footer. This is copied into oc.core/footer
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.chat :as chat]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(defn navigate-to-your-digest [your-digest-url]
  (router/redirect! your-digest-url))

(rum/defcs site-footer 
  [s]
  [:footer.navbar.navbar-default.navbar-bottom
    {:class (utils/class-set {:no-border (utils/in? (:route @router/path) "slack-lander")})}
    [:div.container-fluid.group
      [:div.right-column.group

        [:div.column.column-company
          [:div.column-title
            "Product"]
          [:div.column-item [:a {:href oc-urls/pricing} "Pricing"]]
          [:div.column-item [:a {:href oc-urls/oc-trello-public :target "_blank"} "Roadmap"]]
          [:div.column-item [:a {:href oc-urls/what-s-new :target "_blank"} "What’s new"]]
          [:div.column-item [:a {:href oc-urls/oc-github :target "_blank"} "GitHub"]]]

        [:div.column.column-resources
          [:div.column-title
            "Company"]
          [:div.column-item [:a {:href oc-urls/about} "About Carrot"]]
          [:div.column-item [:a {:href oc-urls/blog :target "_blank"} "Blog"]]
          [:div.column-item [:a {:href oc-urls/oc-twitter :target "_blank"} "Twitter"]]
          [:div.column-item [:a {:href oc-urls/press-kit} "Press Kit"]]
          [:div.column-item [:a {:href "#"
                                 :on-click #(chat/chat-click)}
                              "Contact"]]]

        [:div.column.column-support
          [:div.column-title
            "Resources"]
          [:div.column-item [:a {:href oc-urls/help :target "_blank"} "Help center"]]]]
      [:div.left-column.group
        [:img.logo
          {:src (utils/cdn "/img/ML/carrot_wordmark.svg")}]
        [:div.footer-communication-copy
          "Leadership communication for fast-growing and remote teams."]
        (when-not (jwt/jwt)
          [:div.footer-small-links
            [:a
              {:href oc-urls/sign-up
               :on-click #(do (utils/event-stop %) (router/nav! oc-urls/sign-up))}
              "Get started for free"]
            "or"
            [:a
              {:href oc-urls/login
               :on-click #(do (utils/event-stop %) (router/nav! oc-urls/login))}
              "Login"]])
        [:div.copyright
          "© 2015-2019 Carrot"]
        [:div.tos-and-pp
          [:a {:href oc-urls/privacy}
           "Privacy"]
          " • "
          [:a {:href oc-urls/terms}
           "Terms"]]]]])