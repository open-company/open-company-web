(ns oc.core
  (:require [oc.pages :as pages]
            [boot.util :as util]
            [hiccup.page :as hp]
            [environ.core :refer (env)]))
;; FIXME: Using hellp@opencompany.com until we have hello@carrot.io setup
;; (def contact-email "hello@carrot.io")
(def contact-email "hello@opencompany.com")
(def contact-mail-to (str "mailto:" contact-email))

(def options {:contact-email contact-email
              :contact-mail-to contact-mail-to})

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
   [:meta {:content "width=device-width, initial-scale=1", :name "viewport"}]
   ;; The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags
   [:title "OpenCompany - Startup Transparency Simplified"]
   ;; Bootstrap - Latest compiled and minified CSS
   [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" :crossorigin "anonymous"}]
   [:link {:href "/css/app.main.css", :rel "stylesheet"}]
   [:link {:href "/css/site.main.css", :rel "stylesheet"}]
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
   [:link {:type "text/css" :rel "stylesheet" :href "/css/fonts/CarrotKit.css?oc_deploy_key"}]
   ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
   [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}]
   ;; Favicon
   [:link {:rel "icon" :type "image/png" :href "/img/carrot_logo.png" :sizes "64x64"}]
   ;; jQuery needed by Bootstrap JavaScript
   [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" :type "text/javascript"}]
   ;; Bootstrap JavaScript //getf.com/
   [:script {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" :type "text/javascript" :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" :crossorigin "anonymous"}]])

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
            {:class (if (= active-page "home") "active" "")}
            [:a.navbar-item {:href "/"} "Home"]]
          ; [:li
          ;   {:class (if (= active-page "pricing") "active" "")}
          ;   [:a.navbar-item {:href "/pricing"} "Pricing"]]
          [:li
            {:class (if (= active-page "features") "active" "")}
            [:a.navbar-item {:href "/features"} "Features"]]
          [:li
            {:class (if (= active-page "about") "active" "")}
            [:a.navbar-item {:href "/about"} "About"]]
          [:li
            {:class (if (= active-page "blog") "active" "")}
            [:a.navbar-item {:href "http://blog.carrot.io"} "Blog"]]
          [:li.get-started-item
            [:div.get-started-button.navbar-item
              [:button.mlb-reset.mlb-get-started
                {:on-click #()}
                "Get Early Access"]
              [:div.mobile-already-account
                [:a {:href "/login"} "Already have an account? " [:span.login "Sign in"]]]]]]]]])

(defn footer
  "Static hiccup for the site footer. This is a copy of oc.web.components.ui.site-footer and every change here should be reflected there."
  []
  ;; NB: copy of oc.web.components.ui.site-footer, every change should be reflected there and vice-versa
  [:nav.navbar.navbar-default.navbar-bottom
    [:div.container-fluid.group
      [:div.left-column
        [:img.logo
          {:src "/img/ML/carrot_wordmark_white.svg"}]
        [:div.small-links
          [:a "Request Free Early Access"]]
        [:div.small-logos
          [:a.twitter
            [:img {:src "/img/ML/home_page_twitter.svg"}]]
          [:a.medium
            [:img {:src "/img/ML/home_page_medium.svg"}]]]
        [:div.copyright "© Copyright 2017. All rights reserved"]]
      [:div.right-column

        [:div.column.support
          [:div.column-title
            {:onClick "$('nav.navbar-bottom div.column:not(.support)').removeClass('expanded');$('nav.navbar-bottom div.column.support').toggleClass('expanded');"}
            "SUPPORT"]
          [:div.column-item [:a "Help"]]
          [:div.column-item [:a {:href contact-mail-to} "Contact"]]]

        [:div.column.integration
          [:div.column-title
            {:onClick "$('nav.navbar-bottom div.column:not(.integration)').removeClass('expanded');$('nav.navbar-bottom div.column.integration').toggleClass('expanded');"}
            "INTEGRATIONS"]
          [:div.column-item [:a "Slack"]]
          [:div.column-item [:a "Developers"]]]

        [:div.column.company
          [:div.column-title
            {:onClick "$('nav.navbar-bottom div.column:not(.company)').removeClass('expanded');$('nav.navbar-bottom div.column.company').toggleClass('expanded');"}
            "COMPANY"]
          [:div.column-item [:a {:href "/about"} "About"]]
          [:div.column-item [:a {:href "http://blog.carrot.io"} "Blog"]]
          [:div.column-item [:a "Legal"]]]

        [:div.column.tour
          [:div.column-title
            {:onClick "$('nav.navbar-bottom div.column:not(.tour)').removeClass('expanded');$('nav.navbar-bottom div.column.tour').toggleClass('expanded');"}
            "TOUR"]
          [:div.column-item [:a {:href "/"} "Home"]]
          [:div.column-item [:a {:href "/features"} "Features"]]
          ; [:div.column-item [:a "Pricing"]]
          ]]]])


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
                 {:class (if (is? :index) "hero gradient" "gradient")}
                 [:div
                  {:class (if (is? :index) "container outer header hero" "outer header")}
                  (nav (name page))]]
                (case page
                  ; :index   (pages/index options)
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