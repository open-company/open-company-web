(ns oc.core
  (:require [oc.pages :as pages]
            [oc.pages.shared :as shared]
            [boot.util :as util]
            [hiccup.page :as hp]
            [environ.core :refer (env)]))

(def contact-email "hello@carrot.io")
(def contact-mail-to (str "mailto:" contact-email))
(def oc-github "https://github.com/open-company")

(def options {:contact-email contact-email
              :contact-mail-to contact-mail-to
              :oc-github oc-github})

(defn head []
  [:head
    pages/tag-manager-head
    ;; -------------
    [:meta {:charset "utf-8"}]
    [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
    [:meta {:content "width=device-width, height=device-height, initial-scale=1", :name "viewport"}]
    [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
    ;; The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags
    [:title "Wut! | wuts.io"]
    (pages/circular-book-font)
    (pages/circular-bold-font)
    pages/google-fonts
    pages/bootstrap-css
    ;; Local css
    [:link {:href (shared/cdn "/css/app.main.css"), :rel "stylesheet"}]
    ;; Fallback for the CDN compacted css
    [:link {:href (shared/cdn "/main.css") :rel "stylesheet"}]
    ;; HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries
    ;; WARNING: Respond.js doesn't work if you view the page via file://
    "<!--[if lt IE 9]>
      <script src=\"//oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js\"></script>
      <script src=\"//oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>
    <![endif]-->"
    pages/font-awesome
    ;; Favicon
    [:link {:rel "icon" :type "image/png" :href (shared/cdn "/img/carrot_logo.png") :sizes "64x64"}]
    ;; jQuery needed by Bootstrap JavaScript
    pages/jquery
    pages/ie-jquery-fix
    ;; Static js files
    [:script {:src (shared/cdn "/js/static-js.js")}]
    ;; Intercom (Support chat)
    [:script {:src (shared/cdn "/js/intercom.js")}]
    ;; Google Analytics
    [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js"}]
    [:script {:type "text/javascript" :src "/lib/autotrack/autotrack.js"}]
    [:script {:type "text/javascript" :src "/lib/autotrack/google-analytics.js"}]
    (pages/google-analytics-init)
    ;; TODO: enable when we want to use full story for static pages.
    ;;(when (= (env :fullstory) "true")
    ;;  [:script {:type "text/javascript" :src "/lib/fullstory.js"}])
    ;;(when (= (env :fullstory) "true") (pages/fullstory-init))
    pages/bootstrap-js])

(defn mobile-menu
  "Mobile menu used to show the collapsable menu in the marketing site."
  [active-page]
  [:div.site-mobile-menu.hidden
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
          {:href "/apps/detect"}
          "Get the mobile app"]]]
    [:div.site-mobile-menu-footer
      [:button.mlb-reset.login-btn
        {:id "site-mobile-menu-login"}
        "Login"]
      [:button.mlb-reset.get-started-button.get-started-action
        {:id "site-mobile-menu-getstarted"}
        "Start free"]]])

(defn nav
  "Static hiccup for the site header."
  [active-page]
  (let [is-slack-lander? (= active-page "slack-lander")
        is-slack-page? (= active-page "slack")
        site-navbar-container (if is-slack-lander?
                               :div.site-navbar-container.is-slack-header
                               :div.site-navbar-container)]
    [:nav.site-navbar
      [site-navbar-container
        [:a.navbar-brand-left
          {:href "/?no_redirect=1"}]
        [:div.navbar-brand-center
          [:a
            {:href "/?no_redirect=1"
             :class (when (= active-page "index") "active")}
            "Home"]
          [:div.apps-container
            [:button.mlb-reset.apps-bt
              {:class (when (= active-page "about") "active")}
              "Apps"]
            [:div.apps-dropdown-menu
              [:div.app-items-group
                "Desktop apps"]
              [:a.app-item
                {:href "/apps/mac"}
                [:span "Mac"]
                [:span.beta-app-label "BETA"]]
              [:a.app-item
                {:href "/apps/win"}
                [:span "Windows"]
                [:span.beta-app-label "BETA"]]
              [:div.app-items-group
                "Mobile apps"]
              [:a.app-item
                {:href "/apps/android"}
                [:span "Android"]
                [:span.beta-app-label "BETA"]]
              [:a.app-item
                {:href "/apps/ios"}
                [:span "iPhone"]
                [:span.beta-app-label "BETA"]]]]
          [:a
            {:href "/pricing"
             :class (when (= active-page "pricing") "active")}
            "Pricing"]]

        ;; Desktop & tablet
        (cond
          is-slack-page?
          [:div.site-navbar-right.big-web-tablet-only
            [:a.login
              {:id "site-header-login-item"
               :href (env :slack-signup-url)}
                "Add to Slack"]]
          is-slack-lander?
          [:div.site-navbar-right.big-web-tablet-only
            [:a.signup.continue-with-slack
              {:id "site-header-signup-item"
               :href (env :slack-signup-url)}
                "Continue with Slack"]]
          :else
          [:div.site-navbar-right.big-web-tablet-only
            [:a.login
              {:id "site-header-login-item"
               :href "/login"}
                "Login"]
            [:a.signup
              {:id "site-header-signup-item"
               :href "/sign-up"}
              "Start free"]])

        ;; Mobile
        (cond
          is-slack-lander?
          [:div.site-navbar-right.mobile-only
            [:a.signup.continue-with-slack
              {:id "site-header-signup-item-mobile"
               :href (env :slack-signup-url)}]]
          :else
          [:div.site-navbar-right.mobile-only
            [:a.login
              {:id "site-header-login-item-mobile"
               :data-bago false
               :href "/login"}
                "Login"]])
        [:div.mobile-ham-menu
          {:onClick "javascript:OCStaticSiteMobileMenuToggle();"}]]]))

(defn footer
  "Static hiccup for the site footer."
  [page]
  [:footer.navbar.navbar-default.navbar-bottom
    [:div.container-fluid.group
      [:div.right-column.group

        [:div.column.column-company
          [:div.column-title
            "Product"]
          [:div.column-item [:a {:href "/pricing"} "Pricing"]]
          [:div.column-item [:a {:href "https://carrot.news/" :target "_blank"} "What’s new"]]
          [:div.column-item [:a {:href oc-github :target "_blank"} "GitHub"]]
          [:div.column-item [:a {:href "/slack"} "Slack integration"]]]

        [:div.column.column-resources
          [:div.column-title
            "Company"]
          [:div.column-item [:a {:href "/about"} "About"]]
          [:div.column-item [:a {:href "https://twitter.com/carrot_hq" :target "_blank"} "Twitter"]]
          [:div.column-item [:a {:href "https://blog.carrot.io" :target "_blank"} "Blog"]]]

        [:div.column.column-support
          [:div.column-title
            "Resources"]
          [:div.column-item [:a {:href "https://help.carrot.io/" :target "_blank"} "Help center"]
          [:div.column-item
            [:a
              {:class "intercom-chat-link"
               :href "mailto:hello@carrot.io"}
              "Contact us"]]]]]
      [:div.left-column.group
        [:img.logo
          {:src (shared/cdn "/img/ML/carrot_wordmark.svg")}]
        [:div.footer-small-links.static
          [:a {:href "/login"} "Login"]
          "or"
          [:a {:href "/sign-up"} "create your team"]]
        [:div.tos-and-pp
          [:a {:href "/privacy"}
           "Privacy"]
          " & "
          [:a {:href "/terms"}
           "Terms"]]
        [:div.copyright
          "© 2020 Carrot"]]]])


(defn read-edn [entry]
  (read-string (slurp (:full-path entry))))

(def ph-banner
  [:div.ph-banner
    [:div.ph-banner-content
      [:div.ph-banner-cat]
      [:div.ph-banner-copy
        [:span.heavy "Hello Product Hunter! "]
        " We can't wait to hear what you think about Carrot 2.0."]]
    [:div.ph-banner-opac-bg]
    [:button.mlb-reset.ph-banner-close-button
      {:onclick "OCStaticHidePHBanner();"}]])

(defn covid-banner [page]
  [:div.covid-banner
    [:div.covid-banner-content
      [:div.covid-banner-carrot-logo]
      [:div.covid-banner-copy.mobile-only
        "Given COVID-19, Carrot is now free."
        (when-not (= page :pricing)
          [:br])
        (when-not (= page :pricing)
          [:a
            {:href "/pricing"}
            "Learn more"])
        (when-not (= page :pricing)
          ".")]
      [:div.covid-banner-copy.big-web-tablet-only
        "Given the COVID-19 crisis, Carrot is free for unlimited users until further notice. "
        (when-not (= page :pricing)
          [:a
            {:href "/pricing"}
            "Learn more"])
        (when-not (= page :pricing)
          ".")]]
    [:button.mlb-reset.covid-banner-close-button
      {:onclick "OCStaticHideCovidBanner();"}]])

(defn static-page
  ([content]
   (static-page content {}))
  ([content opts]
   (let [{:keys [page title]} (-> content :entry read-edn)
         is?    (fn [& args] ((set args) page))]
     (hp/html5 {:lang "en"}
               (head)
               [:body
                {:class (when (env :covid-banner) "covid-banner")}
                pages/tag-manager-body
                [:div
                 {:class "outer header"}
                 ph-banner
                 (covid-banner page)
                 (nav (name page))
                 (mobile-menu (name page))]
                (case page
                  :index   (pages/index options)
                  :about   (pages/about options)
                  :slack   (pages/slack options)
                  :slack-lander   (pages/slack-lander options)
                  :press-kit   (pages/press-kit options)
                  :pricing (pages/pricing options)
                  :404     (pages/not-found options)
                  :500     (pages/server-error options)
                  :privacy (pages/privacy options)
                  :terms   (pages/terms options))
                (footer page)]))))

(defn app-shell [_]
  (hp/html5 {:lang "en"}
            (:head pages/app-shell)
            (:body pages/app-shell)))

(defn prod-app-shell [_]
  (hp/html5 {:lang "en"}
            (:head pages/prod-app-shell)
            (:body pages/prod-app-shell)))
