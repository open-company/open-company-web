(ns oc.core
  (:require [oc.pages :as pages]
            [boot.util :as util]
            [hiccup.page :as hp]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
   [:meta {:content "width=device-width, initial-scale=1", :name "viewport"}]
   ;; The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags
   [:title "OPENcompany - Startup Transparency Simplified"]
   ;; Bootstrap
   ;; Latest compiled and minified CSS
   [:link {:crossorigin "anonymous",
           :integrity "sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7",
           :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css",
           :rel "stylesheet"}]
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
   ;; Font Awesome
   [:link {:href "https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css",
           :rel "stylesheet"}]])

(defn nav []
  [:nav.navbar.navbar-default
   {:role "navigation"}
   [:div.navbar-header
    [:a.navbar-brand {:href "/"} [:strong "open"] "company"]
    ;; Hamburger menu for mobile
    [:button.navbar-toggle.collapsed
     {:aria-expanded "false", :data-target "#bs-example-navbar-collapse-1",
      :data-toggle "collapse", :type "button"}
     [:span.sr-only "Toggle navigation"]
     [:span.icon-bar]
     [:span.icon-bar]
     [:span.icon-bar]]]
   [:div#bs-example-navbar-collapse-1.collapse.navbar-collapse
    [:ul.nav.navbar-nav.navbar-right
     [:li [:a {:href "/"} "Home"]]
     [:li [:a {:href "/pricing.html"} "Pricing"]]
     [:li
      [:a {:href "http://newsletter.opencompany.com/"} "Newsletter"]]
     [:li [:a {:href "/about.html"} "About"]]
     [:li [:a {:href "mailto:hello@opencompany.com"} "Contact"]]]]])

(defn tagline []
  [:div.tagline.text-center
    [:h1 "Startup Transparency Made Simple"]
    [:div.early-access
     [:form.form-inline
      [:div.form-group
       [:input#email
        {:size "40", :placeholder "email@work.com", :type "email"}]
       [:button "Get early access"]]]]])

(defn email-capture []
  [:div.solid
   [:div.container.outer.section
    [:div.container.inner
     [:div.bottom-block.text-center
      [:h2 "Operate in the Open"]
      [:div.early-access
       [:form.form-inline
        [:div.form-group
         [:input#email
          {:size "40", :placeholder "email@work.com", :type "email"}]
         [:button "Get early access"]]]]]]]])

(defn footer []
  [:div.container.outer.footer
   [:nav.navbar.navbar-default
    [:p.navbar-text "OpenCompany"]
    [:ul.nav.navbar.navbar-nav
     [:li
      [:a {:href "http://twitter.com/opencompanyhq"}
       [:i.fa.fa-twitter]]]
     [:li
      [:a {:href "https://github.com/open-company"}
       [:i.fa.fa-github-alt]]]]
    [:ul.nav.navbar-nav.navbar-right
     [:li [:a {:href "/"} "Home"]]
     [:li [:a {:href "/pricing.html"} "Pricing"]]
     [:li [:a {:href "http://newsletter.opencompany.com/"} "Newsletter"]]
     [:li [:a {:href "/about.html"} "About"]]
     [:li [:a {:href "mailto:hello@opencompany.com"} "Contact"]]]]])


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
                  {:class (if (is? :index) "container outer header hero" "container outer header")}
                  (nav)
                  (when (is? :index) (tagline))]]
                (case page
                  :index   (pages/index)
                  :about   (pages/about)
                  :pricing (pages/pricing)
                  :404     (pages/not-found))
                (if (is? :index :about)
                  (email-capture))
                (footer)]))))

(defn app-shell [_]
  (hp/html5 {:lang "en"}
            (:head pages/app-shell)
            (:body pages/app-shell)))