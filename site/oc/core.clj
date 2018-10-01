(ns oc.core
  (:require [oc.pages :as pages]
            [boot.util :as util]
            [hiccup.page :as hp]
            [environ.core :refer (env)]))

(def contact-email "hello@carrot.io")
(def contact-mail-to (str "mailto:" contact-email))

(def options {:contact-email contact-email
              :contact-mail-to contact-mail-to})

(defn head [drift?]
  [:head
    [:meta {:charset "utf-8"}]
    [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
    [:meta {:content "width=device-width, initial-scale=1", :name "viewport"}]
    [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
    ;; The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags
    [:title "Carrot | Leadership communication for growing and distributed teams"]
    pages/bootstrap-css
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
    pages/font-awesome
    ;; Favicon
    [:link {:rel "icon" :type "image/png" :href (pages/cdn "/img/carrot_logo.png") :sizes "64x64"}]
    ;; jQuery needed by Bootstrap JavaScript
    [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" :type "text/javascript"}]
    ;; Static js files
    [:script {:src (pages/cdn "/js/static-js.js")}]
    (when drift?
      ;; Drift
      [:script {:src (pages/cdn "/js/drift.js")}])
    ;; Google Analytics
    [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js"}]
    [:script {:type "text/javascript" :src "/lib/autotrack/autotrack.js"}]
    [:script {:type "text/javascript" :src "/lib/autotrack/google-analytics.js"}]
    (pages/google-analytics-init)
    (when (= (env :fullstory) "true")
      [:script {:type "text/javascript" :src "/lib/fullstory.js"}])
    (when (= (env :fullstory) "true") (pages/fullstory-init))
    ;; Headway (What's New)
    [:script {:type "text/javascript" :src "//cdn.headwayapp.co/widget.js"}]
    pages/bootstrap-js])

(defn mobile-menu
  "Mobile menu used to show the collapsable menu in the marketing site."
  [active-page]
  [:div.site-mobile-menu.mobile-only.hidden
    [:div.site-mobile-menu-container
      [:div.site-mobile-menu-item
        [:a
          {:href "/?no_redirect=1"
           :class (when (= active-page "index") "active")}
          "Home"]]
      [:div.site-mobile-menu-item
        [:a
          {:href "/pricing"
           :class (when (= active-page "pricing") "active")}
          "Pricing"]]
      [:div.site-mobile-menu-item
        [:a
          {:href "/about"
           :class (when (= active-page "about") "active")}
          "About"]]
      [:div.site-mobile-menu-item
        [:a
          {:href "http://blog.carrot.io"
           :target "_blank"}
          "Blog"]]]
    [:div.site-mobile-menu-footer
      [:button.mlb-reset.login-btn
        {:id "site-mobile-menu-login"}
        "Login"]
      [:button.mlb-reset.get-started-button
        {:id "site-mobile-menu-getstarted"}
        "Get started for free"]]])

(defn nav
  "Static hiccup for the site header. This is a copy of oc.web.components.ui.site-header
   and every change here should be reflected there."
  [active-page]
  ;; NB: copy of oc.web.components.ui.site-header, every change should be reflected there and vice-versa
  [:nav.site-navbar
    [:div.site-navbar-container
      [:a.navbar-brand-left
        {:href "/?no_redirect=1"}]
      [:div.navbar-brand-center
        [:a
          {:href "/"
           :class (when (= active-page "index") "active")}
          "Home"]
        [:a
          {:href "/about"
           :class (when (= active-page "about") "active")}
          "About"]
        [:a
          {:href "/pricing"
           :class (when (= active-page "pricing") "active")}
          "Pricing"]
        [:a
          {:href "https://blog.carrot.io"
           :target "_blank"}
          "Blog"]]
      [:div.site-navbar-right.big-web-only
        [:a.login
          {:id "site-header-login-item"
           :href "/login"}
            "Login"]
        [:a.start
          {:id "site-header-signup-item"
           :href "/sign-up"
           :class (when (= active-page "slack")
                    "slack-get-started")}
          (when (= active-page "slack")
            [:span.slack-orange-icon])
          [:span.start-copy
            (if (= active-page "slack")
              "Add to Slack"
              "Get Started")]]]
      [:div.site-navbar-right.mobile-only
        [:a.start
          {:id "site-header-mobile-signup-item"
           :class (when (= active-page "slack") "slack")
           :href "/sign-up"}
            [:span.copy
              (if (= active-page "slack")
                "ADD"
                "START")]]]
      [:div.mobile-ham-menu.mobile-only
        {:onClick "javascript:OCStaticSiteMobileMenuToggle();"}]]])

(defn footer
  "Static hiccup for the site footer. This is a copy of oc.web.components.ui.site-footer
   and every change here should be reflected there."
  []
  ;; NB: copy of oc.web.components.ui.site-footer, every change should be reflected there and vice-versa
  [:footer.navbar.navbar-default.navbar-bottom
    [:div.container-fluid.group
      [:div.footer-balloon.big-green]
      [:div.footer-balloon.small-purple]
      [:div.right-column.group

        [:div.column.column-company
          [:div.column-title
            {:onClick
              (str
               "$('nav.navbar-bottom div.column:not(.column-company)').removeClass('expanded');"
               "$('nav.navbar-bottom div.column.column-company').toggleClass('expanded');")}
            "Company"]
          [:div.column-item [:a {:href "/?no_redirect=1"} "Home"]]
          [:div.column-item [:a {:href "/about"} "About"]]
          [:div.column-item [:a {:href "/pricing"} "Pricing"]]
          [:div.column-item [:a {:href "http://blog.carrot.io" :target "_blank"} "Blog"]]
          [:div.column-item [:a {:href "https://twitter.com/carrot_hq" :target "_blank"} "Twitter"]]]

        [:div.column.column-resources
          [:div.column-title
            {:onClick (str
                       "$('nav.navbar-bottom div.column:not(.column-resources)').removeClass('expanded');"
                       "$('nav.navbar-bottom div.column.column-resources').toggleClass('expanded');")}
            "Resources"]
          [:div.column-item [:a {:href "https://github.com/open-company" :target "_blank"} "GitHub"]]
          [:div.column-item [:a {:href "/privacy"} "Privacy"]]
          [:div.column-item [:a {:href "/terms"} "Terms"]]]

        [:div.column.column-support
          [:div.column-title
            {:onClick
              (str
               "$('nav.navbar-bottom div.column:not(.column-support)').removeClass('expanded');"
               "$('nav.navbar-bottom div.column.column-support').toggleClass('expanded');")}
            "Support"]
          [:div.column-item [:a {:href "https://trello.com/b/eKs2LtLu" :target "_blank"} "Roadmap"]]
          ; [:div.column-item [:a {:href "http://help.carrot.io" :target "_blank"} "Help"]]
          [:div.column-item [:a {:href contact-mail-to} "Contact"]]]

        [:div.column.column-integrations
          [:div.column-title
            {:onClick
              (str
               "$('nav.navbar-bottom div.column:not(.column-integrations)').removeClass('expanded');"
               "$('nav.navbar-bottom div.column.column-integrations').toggleClass('expanded');")}
            "Integrations"]
          [:div.column-item [:a {:href "/slack"} "Slack"]]]]
      [:div.left-column.group
        [:img.logo
          {:src (pages/cdn "/img/ML/carrot_wordmark.svg")}]
        [:div.footer-communication-copy
          "Leadership communication for growing and distributed teams."]
        [:div.footer-small-links.static
          [:a {:href "/sign-up"} "Get started for free"]
          "or"
          [:a {:href "/login"} "Login"]]
        [:div.copyright
          (str
           "Copyright © 2018 Carrot. "
           "All rights reserved.")]]]])


(defn read-edn [entry]
  (read-string (slurp (:full-path entry))))

(defn static-page
  ([content]
   (static-page content {}))
  ([content opts]
   (let [{:keys [page title drift]} (-> content :entry read-edn)
         is?    (fn [& args] ((set args) page))]
     (hp/html5 {:lang "en"}
               (head drift)
               [:body
                [:div
                 {:class "outer header"}
                 (nav (name page))
                 (mobile-menu (name page))]
                (case page
                  :index   (pages/index options)
                  :about   (pages/about options)
                  :slack   (pages/slack options)
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