(ns oc.pages
  (:require [oc.terms :as terms]
            [oc.privacy :as privacy]
            [environ.core :refer (env)]
            [oc.pages.shared :as shared]
            [oc.pages.index :as index]
            [oc.pages.pricing :as pricing]
            [oc.pages.slack :as slack]
            [oc.pages.slack-lander :as slack-lander]
            [oc.pages.about :as about]
            [oc.pages.press-kit :as press-kit]))

(defn oc-loading []
  [:div.oc-loading.active
    [:div.oc-loading-inner
      "Wut!"]])

(def tag-manager-head
  [:script"
    (function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
    new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
    j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
    'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
    })(window,document,'script','dataLayer','GTM-5D4DTSB');"])

(def tag-manager-body
  [:noscript
    [:iframe
      {:src "https://www.googletagmanager.com/ns.html?id=GTM-5D4DTSB"
       :height "0"
       :width "0"
       :style {:display "none"
               :visibility "hidden"}}]])

(def bootstrap-css
  ;; Bootstrap CSS //getbootstrap.com/
  [:link
    {:rel "stylesheet"
     :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
     :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
     :crossorigin "anonymous"}])

(def bootstrap-js
  ;; Bootstrap JavaScript //getf.com/
  [:script
    {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
     :type "text/javascript"
     :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
     :crossorigin "anonymous"}])

(def font-awesome
  ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
  [:link
    {:rel "stylesheet"
     :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}])

(def ie-jquery-fix
  ;; From https://stackoverflow.com/questions/5087549/access-denied-to-jquery-script-on-ie
  ;; Github: https://github.com/MoonScript/jQuery-ajaxTransport-XDomainRequest
  [:script
    {:src "//cdnjs.cloudflare.com/ajax/libs/jquery-ajaxtransport-xdomainrequest/1.0.4/jquery.xdomainrequest.min.js"
     :crossorigin "anonymous"}])

(def jquery
  [:script
    {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
     :crossorigin "anonymous"}])

(def ziggeo-css
  [:link {:rel "stylesheet" :href "/lib/ziggeo/ziggeo.css"}])
  ; [:link {:rel "stylesheet" :href "https://assets-cdn.ziggeo.com/v2-stable/ziggeo.css"}])

(def ziggeo-js
  [:script {:src "/lib/ziggeo/ziggeo.js"}])
  ; [:script {:src "https://assets-cdn.ziggeo.com/v2-stable/ziggeo.js"}])

(def google-fonts
  ;; Google fonts Muli
  [:link {:href "https://fonts.googleapis.com/css2?family=IBM+Plex+Mono&family=Muli&family=PT+Serif:wght@700&display=swap" :rel "stylesheet"}])

(def stripe-js
  [:script {:src "https://js.stripe.com/v3/"}])

(defn circular-font-folder [font-file]
  (str (when (env :oc-web-cdn-url)
        (env :oc-web-cdn-url))
   font-file))

(defn circular-book-font []
  [:link {:rel "stylesheet"
          :type "text/css"
          :href (circular-font-folder "/CircularWebFont/LLCircular-BookWeb/css/stylesheet.css")}])

(defn circular-bold-font []
  [:link {:rel "stylesheet"
          :type "text/css"
          :href (circular-font-folder "/CircularWebFont/LLCircular-BoldWeb/css/stylesheet.css")}])

(defn google-analytics-init []
  [:script (let [ga-version (if (env :ga-version)
                              (str "'" (env :ga-version) "'")
                              false)
                 ga-tracking-id (if (env :ga-tracking-id)
                                  (str "'" (env :ga-tracking-id) "'")
                                  false)]
             (str "CarrotGA.init(" ga-version "," ga-tracking-id ");"))])

(defn fullstory-init []
  [:script (str "init_fullstory();")])

(defn terms [options]
  (terms/terms options))

(defn privacy [options]
  (privacy/privacy options))

(defn index [options]
  (index/index options))

(defn pricing [options]
  (pricing/pricing options))

(defn slack [options]
  (slack/slack options))

(defn slack-lander [options]
  (slack-lander/slack-lander options))

(defn about [options]
  (about/about options))

(defn press-kit [options]
  (press-kit/press-kit options))

(defn not-found [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.not-found
    [:div
      [:div.error-page.not-found-page
        [:img {:src (shared/cdn "/img/ML/carrot_404.svg") :width 338 :height 189}]
        [:h2 "Page Not Found"]
        [:p "The page may have been moved or removed,"]
        [:p.not-logged-in.last "or you may need to " [:a.login {:href "/login"} "login"] "."]
        [:p.logged-in.last "or you may not have access with this account."]
        [:script {:src "/js/set-path.js"}]]]])

(defn server-error [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.server-error
    [:div
      [:div.error-page
        [:h1 "500"]
        [:h2 "Internal Server Error"]
        [:p "We are sorry for the inconvenience."]
        [:p.last "Please try again later."]
        [:script {:src "/js/set-path.js"}]]]])

(def app-shell
  {:head [:head
          tag-manager-head
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
          bootstrap-css
          ;; Normalize.css //necolas.github.io/normalize.css/
          ;; TODO inline this into app.main.css
          [:link {:rel "stylesheet" :href "/css/normalize.css"}]
          font-awesome
          ;; OpenCompany CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/app.main.css"}]
          ;; jQuery UI CSS
          [:link
            {:rel "stylesheet"
             :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; Emoji One Autocomplete CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione/autocomplete.css"}]
          ;; Lineto font
          (circular-book-font)
          (circular-bold-font)
          ;; Google fonts
          google-fonts
          ;;  Medium Editor css
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/medium-editor.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/default.css"}]
          ;; Emojione CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione.css"}]
          ;; Emojone Sprites CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emoji-mart.css"}]
          ;; MediumEditorTCMention css
          [:link {:type "text/css" :rel "stylesheet" :href "/lib/MediumEditorExtensions/MediumEditorTCMention/mention-panel.min.css"}]
          ;; MediumEditorMediaPicker
          [:link
            {:type "text/css"
             :rel "stylesheet"
             :href "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.css"}]
          ;; Local env
          [:script {:type "text/javascript" :src "/js/local-env.js"}]
          [:script {:type "text/javascript" :src "/lib/print_ascii.js"}]
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.js"}]
          ;; Ziggeo
          ziggeo-css
          ziggeo-js
          ;; Intercom (Support)
          [:script {:src (shared/cdn "/js/intercom.js")}]
          ;; Headway (What's New)
          [:script {:type "text/javascript" :src "//cdn.headwayapp.co/widget.js"}]
          ;; Stripe
          stripe-js]
   :body [:body
          tag-manager-body
          [:div#app
            (oc-loading)]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          [:div.preload-interstitial]
          ;; jQuery needed by Bootstrap JavaScript
          jquery
          ie-jquery-fix
          ;; Static js files
          [:script {:type "text/javascript" :src (shared/cdn "/js/static-js.js")}]
          ;; Google Analytics
          [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/autotrack.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/google-analytics.js"}]
          (google-analytics-init)
          ;; Truncate html string
          [:script {:type "text/javascript" :src "/lib/truncate/jquery.dotdotdot.js"}]
          ;; Rangy
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-core.js"}]
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-classapplier.js"}]
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-selectionsaverestore.js"}]
          ;; jQuery textcomplete needed by Emoji One autocomplete
          [:script
            {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.8.4/jquery.textcomplete.min.js"
             :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery scrollTo plugin
          [:script {:src "/lib/scrollTo/scrollTo.min.js" :type "text/javascript"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          bootstrap-js
          ;; Emoji One Autocomplete
          [:script {:src "/js/emojione/autocomplete.js" :type "text/javascript"}]
          ;; ClojureScript generated JavaScript
          [:script {:src "/oc.js" :type "text/javascript"}]
          ;; Utilities
          [:script {:type "text/javascript", :src "/lib/js-utils/pasteHtmlAtCaret.js"}]
          ;; Clean HTML input
          [:script {:src "/lib/cleanHTML/cleanHTML.js" :type "text/javascript"}]
          ;; MediumEditorToolbar
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorToolbar/toolbar.js"}]
          ;; MediumEditorPaste
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorPaste/paste.js"}]
          ;; MediumEditorAutolist
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorAutolist/autolist.js"}]
          ;; MediumEditorAutoquote
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorAutoquote/autoquote.js"}]
          ;; MediumEditorAutocode
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorAutocode/autocode.js"}]
          ;; MediumEditorAutocode
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorAutoInlinecode/autoinlinecode.js"}]
          ;; MediumEditorAutocode
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorInlineCodeButton/inlinecodebutton.js"}]
          ;; MediumEditorMediaPicker
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.js"}]
          ;; MediumEditorFileDragging
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorFileDragging/filedragging.js"}]
          ;; MediumEditorHighlighterButton
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorHighlighterButton/highlighterbutton.js"}]
          ;; MediumEditorTCMention
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorTCMention/index.min.js"}]
          ;; MediumEditorTCMention Panel
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorTCMention/CustomizedTagComponent.js"}]]})

(def prod-app-shell
  {:head [:head
          tag-manager-head
          [:meta {:charset "utf-8"}]
          [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
          [:meta
            {:content "width=device-width, height=device-height, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
             :name "viewport"}]
          [:meta {:name "apple-mobile-web-app-capable" :content "yes"}]
          [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
          ;; Lineto font
          (circular-book-font)
          (circular-bold-font)
          ;; Google fonts
          google-fonts
          [:link {:rel "icon" :type "image/png" :href (shared/cdn "/img/carrot_logo.png") :sizes "64x64"}]
          ;; The above 3 meta tags *must* come first in the head;
          ;; any other head content must come *after* these tags
          [:title "Carrot | Remote team communication"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          bootstrap-css
          font-awesome
          ;; jQuery UI CSS
          [:link
            {:rel "stylesheet"
             :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; App single CSS
          [:link {:type "text/css" :rel "stylesheet" :href (shared/cdn "/main.css")}]
          ;; jQuery needed by Bootstrap JavaScript
          jquery
          ie-jquery-fix
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.min.js"}]
          ;; Ziggeo
          ziggeo-css
          ziggeo-js
          ;; Stripe
          stripe-js]
   :body [:body
          tag-manager-body
          [:div#app
            (oc-loading)]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          [:div.preload-interstitial]
          ;; Static js files
          [:script {:src (shared/cdn "/js/static-js.js")}]
          ;; jQuery textcomplete needed by Emoji One autocomplete
          [:script
            {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.8.4/jquery.textcomplete.min.js"
             :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          bootstrap-js
          ;; Google Analytics
          [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js" :async true}]
          ;; Intercom (Support)
          [:script {:src (shared/cdn "/js/intercom.js")}]
          ;; Headway (What's New)
          [:script {:type "text/javascript" :src "//cdn.headwayapp.co/widget.js"}]
          ;; Compiled oc.min.js from our CDN
          [:script {:src (shared/cdn "/oc.js")}]
          ;; Compiled assets
          [:script {:src (shared/cdn "/oc_assets.js")}]
          (when (= (env :fullstory) "true")
            (fullstory-init))
          (google-analytics-init)]})
