(ns oc.web.components.ui.site-footer
  "Component for the site footer. This is copied into oc.core/footer
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(rum/defc bottom-footer
  [container-class]
  [:div
    {:class container-class}
    [:div.small-logos
      [:a.twitter
        {:target "_blank" :href oc-urls/oc-twitter :title "Carrot on Twitter"}
        [:img {:src (utils/cdn "/img/ML/home_page_twitter.svg")}]]
      [:a.medium
        {:target "_blank" :href oc-urls/blog :title "Carrot on Medium"}
        [:img {:src (utils/cdn "/img/ML/home_page_medium.svg")}]]]
    [:div.copyright "© Copyright 2017. All rights reserved"]])

(defn navigate-to-your-boards [your-boards-url]
  (router/redirect! your-boards-url))

(rum/defcs site-footer  < (rum/local nil ::expanded)
  [s]
  ;; <!-- footer -->
  [:footer.navbar.navbar-default.navbar-bottom
    [:div.container-fluid.group
      [:div.left-column
        [:img.logo
          {:src (utils/cdn "/img/ML/carrot_wordmark.svg")}]
        (when-not (jwt/jwt)
          [:div.footer-small-links
            [:a
              {:href oc-urls/sign-up
               :on-click #(do (utils/event-stop %) (router/nav! oc-urls/sign-up))}
              "Get Started"]
            "|"
            [:a
              {:href oc-urls/login
               :on-click #(do (utils/event-stop %) (router/nav! oc-urls/login))}
              "Log in"]])
        (when-not (responsive/is-mobile-size?)
          (bottom-footer "big-web-footer"))]

      [:div.right-column

        [:div.column.column-company
          {:class (when (= @(::expanded s) :company) "expanded")}
          [:div.column-title
            {:on-click #(when (responsive/is-mobile-size?)
                          (if (= @(::expanded s) :company)
                            (reset! (::expanded s) nil)
                            (reset! (::expanded s) :company)))}
            "COMPANY"]
          [:div.column-item [:a {:href oc-urls/home} "Home"]]
          [:div.column-item [:a {:href oc-urls/about} "About"]]
          [:div.column-item [:a {:href oc-urls/blog :target "_blank"} "Blog"]]]

        [:div.column.column-integration
          {:class (when (= @(::expanded s) :integration) "expanded")}
          [:div.column-title
            {:on-click #(when (responsive/is-mobile-size?)
                          (if (= @(::expanded s) :integration)
                            (reset! (::expanded s) nil)
                            (reset! (::expanded s) :integration)))}
            "RESOURCES"]
          [:div.column-item [:a {:href oc-urls/oc-github :target "_blank"} "GitHub"]]
          [:div.column-item [:a {:href oc-urls/privacy} "Privacy Policy"]]
          [:div.column-item [:a {:href oc-urls/terms} "Terms of Use"]]]

        [:div.column.column-support
          {:class (when (= @(::expanded s) :support) "expanded")}
          [:div.column-title
            {:on-click #(when (responsive/is-mobile-size?)
                          (if (= @(::expanded s) :support)
                            (reset! (::expanded s) nil)
                            (reset! (::expanded s) :support)))}
            "SUPPORT"]
          [:div.column-item [:a {:href oc-urls/help :target "_blank"} "Help"]]
          [:div.column-item [:a {:href oc-urls/contact-mail-to} "Contact"]]]

        ; [:div.column.column-tour
        ;   {:class (when (= @(::expanded s) :tour) "expanded")}
        ;   [:div.column-title
        ;     {:on-click #(when (responsive/is-mobile-size?)
        ;                   (if (= @(::expanded s) :tour)
        ;                     (reset! (::expanded s) nil)
        ;                     (reset! (::expanded s) :tour)))}
        ;     "TOUR"]
        ;   [:div.column-item [:a {:href oc-urls/home} "Home"]]
        ;   [:div.column-item [:a {:href oc-urls/features} "Features"]]]
          ]

        (when (responsive/is-mobile-size?)
          (bottom-footer "mobile-footer"))]])