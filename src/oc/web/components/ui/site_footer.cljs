(ns oc.web.components.ui.site-footer
  "Component for the site footer. This is copied into oc.core/footer
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(defn navigate-to-your-digest [your-digest-url]
  (router/redirect! your-digest-url))

(rum/defcs site-footer  < (rum/local nil ::expanded)
  [s]
  [:footer.navbar.navbar-default.navbar-bottom
    {:class (when (utils/in? (:route @router/path) "slack-lander") "light-background")}
    [:div.container-fluid.group
      [:div.right-column.group

        [:div.column.column-company
          {:class (when (= @(::expanded s) :company) "expanded")}
          [:div.column-title
            {:on-click #(when (responsive/is-mobile-size?)
                          (if (= @(::expanded s) :company)
                            (reset! (::expanded s) nil)
                            (reset! (::expanded s) :company)))}
            "Company"]
          [:div.column-item [:a {:href oc-urls/home-no-redirect} "Home"]]
          [:div.column-item [:a {:href oc-urls/about} "About"]]
          [:div.column-item [:a {:href oc-urls/pricing} "Pricing"]]
          [:div.column-item [:a {:href oc-urls/blog :target "_blank"} "Blog"]]
          [:div.column-item [:a {:href oc-urls/oc-twitter :target "_blank"} "Twitter"]]]

        [:div.column.column-resources
          {:class (when (= @(::expanded s) :resources) "expanded")}
          [:div.column-title
            {:on-click #(when (responsive/is-mobile-size?)
                          (if (= @(::expanded s) :resources)
                            (reset! (::expanded s) nil)
                            (reset! (::expanded s) :resources)))}
            "Resources"]
          [:div.column-item [:a {:href oc-urls/oc-github :target "_blank"} "GitHub"]]
          [:div.column-item [:a {:href oc-urls/privacy} "Privacy"]]
          [:div.column-item [:a {:href oc-urls/terms} "Terms"]]]

        [:div.column.column-support
          {:class (when (= @(::expanded s) :support) "expanded")}
          [:div.column-title
            {:on-click #(when (responsive/is-mobile-size?)
                          (if (= @(::expanded s) :support)
                            (reset! (::expanded s) nil)
                            (reset! (::expanded s) :support)))}
            "Support"]
          [:div.column-item [:a {:href oc-urls/oc-trello-public :target "_blank"} "Roadmap"]]
          ; [:div.column-item [:a {:href oc-urls/help :target "_blank"} "Help"]]
          [:div.column-item [:a {:href "#hello" :name "hello"} "Contact"]]]

        [:div.column.column-integrations
          {:class (when (= @(::expanded s) :integrations) "expanded")}
          [:div.column-title
            {:on-click #(when (responsive/is-mobile-size?)
                          (if (= @(::expanded s) :integrations)
                            (reset! (::expanded s) nil)
                            (reset! (::expanded s) :integrations)))}
            "Integrations"]
          [:div.column-item [:a {:href oc-urls/slack} "Slack"]]]]
      [:div.left-column.group
        [:img.logo
          {:src (utils/cdn "/img/ML/carrot_wordmark.svg")}]
        [:div.footer-communication-copy
          "Leadership communication for growing and distributed teams."]
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
          "Copyright © 2018 Carrot. All rights reserved"]]]])