(ns oc.core
  (:require [oc.pages :as pages]
            [boot.util :as util]
            [hiccup.page :as hp]
            [environ.core :refer (env)]))

(def contact-email "hello@carrot.io")
(def contact-mail-to (str "mailto:" contact-email))

(def options {:contact-email contact-email
              :contact-mail-to contact-mail-to})

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
   [:meta {:content "width=device-width, initial-scale=1", :name "viewport"}]
   [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
   ;; The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags
   [:title "Carrot - Grow together"]
   ;; Bootstrap - Latest compiled and minified CSS
   [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" :crossorigin "anonymous"}]
   ;; Local css
   [:link {:href (pages/cdn "/css/app.main.css"), :rel "stylesheet"}]
   ;; Fallback for the CDN compacted css
   [:link {:href (pages/cdn "/main.css") :rel "stylesheet"}]
   ;; HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries
   ;; WARNING: Respond.js doesn't work if you view the page via file://
   "<!--[if lt IE 9]>
      <script src=\"//oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js\"></script>
      <script src=\"//oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>
    <![endif]-->"
   ;; Google fonts Open Sans / Lora
   [:link {:type "text/css", :rel "stylesheet",
           :href "https://fonts.googleapis.com/css?family=Open+Sans:400,300"}]
   [:link {:type "text/css", :rel "stylesheet",
           :href "//fonts.googleapis.com/css?family=Lora:400,400italic,700,700italic"}]
   ;; CarrotKit Font
   [:link {:type "text/css" :rel "stylesheet" :href (pages/cdn "/css/fonts/CarrotKit.css")}]
   ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
   [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}]
   ;; Favicon
   [:link {:rel "icon" :type "image/png" :href (pages/cdn "/img/carrot_logo.png") :sizes "64x64"}]
   ;; jQuery needed by Bootstrap JavaScript
   [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" :type "text/javascript"}]
   ;; Static js files
   [:script {:src (pages/cdn "/js/static-js.js")}]
   ;; Bootstrap JavaScript //getf.com/
   [:script {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" :type "text/javascript" :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" :crossorigin "anonymous"}]
   ;; jQuery textcomplete needed by Emoji One autocomplete
   [:script {:src "/lib/jwt_decode/jwt-decode.min.js" :type "text/javascript"}]])

(defn nav
  "Static hiccup for the site header. This is a copy of oc.web.components.ui.site-header and every change here should be reflected there."
  [active-page]
  ;; NB: copy of oc.web.components.ui.site-header, every change should be reflected there and vice-versa
  [:nav.navbar.navbar-default.navbar-static-top
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href "/"}]
        [:button.navbar-toggle.collapsed
          {:type "button"
           :data-toggle "collapse"
           :data-target "#oc-navbar-collapse"
           :onClick "$(document.body).toggleClass('mobile-menu-expanded');"}
            [:span.sr-only "Toggle navigation"]
            [:span.icon-bar]
            [:span.icon-bar]
            [:span.icon-bar]]]
      [:div.collapse.navbar-collapse {:id "oc-navbar-collapse"}
        [:ul.nav.navbar-nav.navbar-right.navbar-top
          [:li.mobile-only
            {:class (if (= active-page "index") "active" "")}
            [:a.navbar-item {:href "/"} "Home"]]
          ; [:li
          ;   {:class (if (= active-page "pricing") "active" "")}
          ;   [:a.navbar-item {:href "/pricing"} "Pricing"]]
          ; [:li
          ;   {:class (if (= active-page "features") "active" "")}
          ;   [:a.navbar-item {:href "/features"} "Features"]]
          [:li
            {:class (if (= active-page "about") "active" "")}
            [:a.navbar-item {:href "/about"} "About"]]
          [:li
            [:a.navbar-item {:href "http://blog.carrot.io" :target "_blank"} "Blog"]]
          [:li
            {:id "site-header-login-item"}
            [:a.navbar-item {:href "/login"} "Login"]]
          [:li.get-started-item
            [:div.get-started-button.navbar-item
              [:button.mlb-reset.mlb-get-started
                {:id "site-header-signup-item"}
                "Get Started"]
              ; [:div.mobile-already-account
              ;   [:a {:href "/login"} "Already have an account? " [:span.login "Sign in"]]]
                ]]]]]])

(defn footer
  "Static hiccup for the site footer. This is a copy of oc.web.components.ui.site-footer and every change here should be reflected there."
  []
  ;; NB: copy of oc.web.components.ui.site-footer, every change should be reflected there and vice-versa
  [:nav.navbar.navbar-default.navbar-bottom
    [:div.container-fluid.group
      [:div.left-column
        [:img.logo
          {:src (pages/cdn "/img/ML/carrot_wordmark_white.svg")}]
        [:div.footer-small-links
          [:a {:href "/sign-up"} "Get Started"]
          "|"
          [:a {:href "/login"} "Log in"]]
        [:div.small-logos
          [:a.twitter
            {:target "_blank" :href "https://twitter.com/CarrotBuzz" :title "Carrot on Twitter"}
            [:img {:src (pages/cdn "/img/ML/home_page_twitter.svg")}]]
          [:a.medium
            {:target "_blank" :href "https://blog.carrot.io" :title "Carrot on Medium"}
            [:img {:src (pages/cdn "/img/ML/home_page_medium.svg")}]]]
        [:div.copyright "© Copyright 2017. All rights reserved"]]
      [:div.right-column

        [:div.column.column-support
          [:div.column-title
            {:onClick "$('nav.navbar-bottom div.column:not(.column-support)').removeClass('expanded');$('nav.navbar-bottom div.column.column-support').toggleClass('expanded');"}
            "SUPPORT"]
          [:div.column-item [:a {:href "http://help.carrot.io" :target "_blank"} "Help"]]
          [:div.column-item [:a {:href "/terms"} "Terms"]]
          [:div.column-item [:a {:href "/privacy"} "Privacy"]]
          [:div.column-item [:a {:href contact-mail-to} "Contact"]]]

        [:div.column.column-integration
          [:div.column-title
            {:onClick "$('nav.navbar-bottom div.column:not(.column-integration)').removeClass('expanded');$('nav.navbar-bottom div.column.column-integration').toggleClass('expanded');"}
            "INTEGRATIONS"]
          [:div.column-item [:a {:href "https://github.com/open-company"} "Developers"]]]

        [:div.column.column-company
          [:div.column-title
            {:onClick "$('nav.navbar-bottom div.column:not(.column-company)').removeClass('expanded');$('nav.navbar-bottom div.column.column-company').toggleClass('expanded');"}
            "COMPANY"]
          [:div.column-item [:a {:href "/"} "Home"]]
          [:div.column-item [:a {:href "/about"} "About"]]
          [:div.column-item [:a {:href "http://blog.carrot.io" :target "_blank"} "Blog"]]]

        ; [:div.column.column-tour
        ;   [:div.column-title
        ;     {:onClick "$('nav.navbar-bottom div.column:not(.column-tour)').removeClass('expanded');$('nav.navbar-bottom div.column.column-tour').toggleClass('expanded');"}
        ;     "TOUR"]
        ;   [:div.column-item [:a {:href "/"} "Home"]]
        ;   [:div.column-item [:a {:href "/features"} "Features"]]]
          ]]])


(defn read-edn [entry]
  (read-string (slurp (:full-path entry))))

(defn static-page
  ([content]
   (static-page content {}))
  ([content opts]
   (let [{:keys [page title]} (-> content :entry read-edn)
         is?    (fn [& args] ((set args) page))]
     (hp/html5 {:lang "en"}
               (head)
               [:body
                [:div
                 {:class "gradient"}
                 [:div
                  {:class "outer header"}
                  (nav (name page))]]
                (case page
                  :index   (pages/index options)
                  :about   (pages/about options)
                  :features (pages/features options)
                  :pricing (pages/pricing options)
                  :404     (pages/not-found options)
                  :500     (pages/server-error options)
                  :privacy (pages/privacy options)
                  :terms   (pages/terms options))
                (footer)]))))

(defn app-shell [_]
  (hp/html5 {:lang "en"}
            (:head pages/app-shell)
            (:body pages/app-shell)))

(defn prod-app-shell [_]
  (hp/html5 {:lang "en"}
            (:head pages/prod-app-shell)
            (:body pages/prod-app-shell)))