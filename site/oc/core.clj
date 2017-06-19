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
   ;; The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags
   [:title "OpenCompany - Startup Transparency Simplified"]
   ;; Bootstrap
   ;; Latest compiled and minified CSS
   [:link {:crossorigin "anonymous",
           :integrity "sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7",
           :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css",
           :rel "stylesheet"}]
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
   ;; Font Awesome
   [:link {:href "https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css",
           :rel "stylesheet"}]
   ;; Favicon
   [:link {:rel "icon" :type "image/png" :href "/img/carrot_logo.png" :sizes "64x64"}]])

(defn nav [active-page]
  [:nav.navbar.navbar-default.navbar-static-top
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href "/"}
          [:img {:alt "Carrot" :src "/img/ML/carrot_wordmark.svg"}]]
        [:button.navbar-toggle.collapsed {:type "button" :data-toggle "collapse" :data-target "#oc-navbar-collapse"}
            [:span.sr-only "Toggle navigation"]
            [:span.icon-bar]
            [:span.icon-bar]
            [:span.icon-bar]]]
      [:div.collapse.navbar-collapse {:id "oc-navbar-collapse"}
        [:ul.nav.navbar-nav.navbar-right.navbar-top
          [:li
            {:class (when (= active-page "pricing") "active")}
            [:a.navbar-item {:href "/pricing"} "Pricing"]]
          [:li
            {:class (when (= active-page "about") "active")}
            [:a.navbar-item {:href "/about"} "About"]]
          [:li
            {:class (when (= active-page "blog") "active")}
            [:a.navbar-item {:href "/blog"} "Blog"]]
          [:li.mobile-only
            [:a.navbar-item.contact {:href "mailto:hello@carrot.io"} "Contact"]]
          [:li
            [:div.get-started-button.navbar-item
              [:button.mlb-reset.mlb-get-started
                ; {:on-click #(dis/dispatch! [:login-overlay-show :login-with-slack])}
                "Get Started"]]]]]]])

(defn footer []
  [:nav.navbar.navbar-default.navbar-bottom
    [:div.container-fluid.group
      [:div.left-column
        [:img.logo
          {:src "/img/ML/carrot_wordmark_white.svg"}]
        [:div.small-links
          [:a "Get started"] "|" [:a "Login"]]
        [:div.small-logos
          [:a.twitter
            [:img {:src "/img/ML/home_page_twitter.svg"}]]
          [:a.medium
            [:img {:src "/img/ML/home_page_medium.svg"}]]]
        [:div.copyright "© Copyright 2017. All rights reserved"]]
      [:div.right-column

        [:div.column
          [:div.column-title "SUPPORT"]
          [:div.column-item [:a "Help"]]
          [:div.column-item [:a "Contact"]]]

        [:div.column
          [:div.column-title "INTEGRATIONS"]
          [:div.column-item [:a "Slack"]]
          [:div.column-item [:a "Developers"]]]

        [:div.column
          [:div.column-title "COMPANY"]
          [:div.column-item [:a "About"]]
          [:div.column-item [:a "Blog"]]
          [:div.column-item [:a "Legal"]]]

        [:div.column
          [:div.column-title "TOUR"]
          [:div.column-item [:a "Home"]]
          [:div.column-item [:a "Features"]]
          [:div.column-item [:a "Pricing"]]]]]])


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