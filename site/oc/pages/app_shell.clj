(ns oc.pages.app-shell
  (:require [environ.core :refer (env)]
            [oc.shared :as shared]))

(def oc-loading
  [:div.oc-loading.active
   [:div.oc-loading-inner
    [:div.oc-loading-heart]
    [:div.oc-loading-body]]])

(def app-shell
  {:head [:head
          shared/tag-manager-head
          shared/tag-manager-body
          [:meta {:charset "utf-8"}]
          [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
          [:meta
           {:content "width=device-width, height=device-height, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
            :name "viewport"}]
          [:meta {:name "apple-mobile-web-app-capable" :content "yes"}]
          [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
          [:link {:rel "icon" :type "image/png" :href (shared/cdn "/img/carrot_logo.png") :sizes "64x64"}]
          ;; The above 3 meta tags *must* come first in the head;
          ;; any other head content must come *after* these tags
          [:title "Carrot | Remote team communication"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          shared/bootstrap-css
          ;; Normalize.css //necolas.github.io/normalize.css/
          ;; TODO inline this into app.main.css
          [:link {:rel "stylesheet" :href "/css/normalize.css"}]
          shared/font-awesome
          ;; OpenCompany CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/app.main.css"}]
          ;; Emoji One Autocomplete CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emoji-autocomplete/main.css"}]
          ;; Lineto font
          (shared/circular-book-font)
          (shared/circular-bold-font)
          ;; Google fonts
          shared/google-fonts
          ;;  Medium Editor css
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/medium-editor.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/themes/default.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/extensions/mention-panel.min.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/extensions/media-picker.css"}]
          ;; Emojone Sprites CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emoji-mart/emoji-mart.css"}]
          ;; Local env
          [:script {:type "text/javascript" :src "/lib/local-env.js"}]
          [:script {:type "text/javascript" :src "/lib/print_ascii.js"}]
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.js"}]
          ;; Intercom (Support)
          (shared/intercom-js)
          ;; Stripe
          shared/stripe-js]
   :body [:body
          [:div#app
           oc-loading]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          [:div.preload-interstitial]
          ;; jQuery needed by Bootstrap JavaScript
          shared/jquery
          shared/ie-jquery-fix
          ;; Static js files
          (shared/static-js)
          (when-let [react-native-devtools-url (env :react-native-devtools-url)]
            [:script {:src react-native-devtools-url}])
          ;; Google Analytics
          ;; [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/autotrack.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/google-analytics.js"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          shared/bootstrap-js
          ;; ClojureScript generated JavaScript
          (shared/oc-js)
          ;; Utilities
          [:script {:type "text/javascript", :src "/lib/js-utils/pasteHtmlAtCaret.js"}]
          ;; Clean HTML input
          [:script {:src "/lib/cleanHTML/cleanHTML.js" :type "text/javascript"}]
          (shared/google-analytics-init)]})

(def prod-app-shell
  {:head [:head
          shared/tag-manager-head
          shared/tag-manager-body
          [:meta {:charset "utf-8"}]
          [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
          [:meta
           {:content "width=device-width, height=device-height, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
            :name "viewport"}]
          [:meta {:name "apple-mobile-web-app-capable" :content "yes"}]
          [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
          ;; Lineto font
          (shared/circular-book-font)
          (shared/circular-bold-font)
          ;; Google fonts
          shared/google-fonts
          [:link {:rel "icon" :type "image/png" :href (shared/cdn "/img/carrot_logo.png") :sizes "64x64"}]
          ;; The above 3 meta tags *must* come first in the head;
          ;; any other head content must come *after* these tags
          [:title "Carrot | Remote team communication"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          shared/bootstrap-css
          shared/font-awesome
          ;; App single CSS
          [:link {:type "text/css" :rel "stylesheet" :href (shared/cdn "/main.css")}]
          ;; jQuery needed by Bootstrap JavaScript
          shared/jquery
          shared/ie-jquery-fix
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.min.js"}]
          ;; Stripe
          shared/stripe-js]
   :body [:body {:class "covid-banner"}
          [:div.covid-banner
            [:div.covid-banner-content
              [:div.covid-banner-carrot-logo]
              [:div.covid-banner-copy
                "Unfortunately Carrot is shutting down service on April 14th, 2023."]]]
          [:div#app
           [:div.oc-loading.active
            [:div.oc-loading-inner
             [:div.oc-loading-heart]
             [:div.oc-loading-body]]]]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          [:div.preload-interstitial]
          ;; Static js files
          (shared/static-js)
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          shared/bootstrap-js
          ;; Google Analytics
          ;; [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js"}]
          [:script {:type "text/javascript" :src (shared/cdn "/lib/autotrack/autotrack.js")}]
          [:script {:type "text/javascript" :src (shared/cdn "/lib/autotrack/google-analytics.js")}]
          ;; Intercom (Support)
          (shared/intercom-js)
          ;; Compiled oc.min.js from our CDN
          (shared/oc-js)
          ;; Compiled assets
          (shared/oc-assets-js)
          ;; Fullstory
          (when (= (env :fullstory) "true")
            [:script {:type "text/javascript" :src (shared/cdn "/lib/fullstory.js")}])
          (when (= (env :fullstory) "true")
            (shared/fullstory-init))
          (shared/google-analytics-init)]})