(ns oc.pages.app-shell
  (:require [environ.core :refer (env)]
            [oc.shared :as shared]))

(def app-shell
  {:head [:head
          shared/tag-manager-head
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
          ;; jQuery UI CSS
          [:link
           {:rel "stylesheet"
            :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; Emoji One Autocomplete CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione/autocomplete.css"}]
          ;; Lineto font
          (shared/circular-book-font)
          (shared/circular-bold-font)
          ;; Google fonts
          shared/google-fonts
          ;;  Medium Editor css
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/medium-editor.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/themes/default.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/MediumEditorTCMention/mention-panel.min.css"}]
          ;; Emojione CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione/emojione.css"}]
          ;; Emojone Sprites CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emoji-mart/emoji-mart.css"}]
          ;; MediumEditorMediaPicker
          [:link
           {:type "text/css"
            :rel "stylesheet"
            :href "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.css"}]
          ;; Local env
          [:script {:type "text/javascript" :src "/lib/local-env.js"}]
          [:script {:type "text/javascript" :src "/lib/print_ascii.js"}]
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.js"}]
          ;; Ziggeo
          shared/ziggeo-css
          shared/ziggeo-js
          ;; Intercom (Support)
          [:script {:src (shared/cdn "/lib/intercom.js")}]
          ;; Headway (What's New)
          [:script {:type "text/javascript" :src "//cdn.headwayapp.co/widget.js"}]
          ;; Stripe
          shared/stripe-js]
   :body [:body
          shared/tag-manager-body
          [:div#app
           [:div.oc-loading.active
            [:div.oc-loading-inner
             [:div.oc-loading-heart]
             [:div.oc-loading-body]]]]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          [:div.preload-interstitial]
          ;; jQuery needed by Bootstrap JavaScript
          shared/jquery
          shared/ie-jquery-fix
          ;; Static js files
          [:script {:type "text/javascript" :src (shared/cdn "/lib/static-js.js")}]
          ;; Google Analytics
          [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/autotrack.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/google-analytics.js"}]
          (shared/google-analytics-init)
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
          shared/bootstrap-js
          ;; Emoji One Autocomplete
          [:script {:src "/lib/emojione/autocomplete.js" :type "text/javascript"}]
          ;; ClojureScript generated JavaScript
          shared/oc-js
          ;; Utilities
          [:script {:type "text/javascript", :src "/lib/js-utils/pasteHtmlAtCaret.js"}]
          ;; Clean HTML input
          [:script {:src "/lib/cleanHTML/cleanHTML.js" :type "text/javascript"}]
          ;; MediumEditor
          [:script {:src "/lib/MediumEditor/index.js" :type "text/javascript"}]
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
          ;; [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorTCMention/index.min.js"}]
          ;; ;; MediumEditorTCMention Panel
          ;; [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorTCMention/CustomizedTagComponent.js"}]
          ]})

(def prod-app-shell
  {:head [:head
          shared/tag-manager-head
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
          ;; jQuery UI CSS
          [:link
           {:rel "stylesheet"
            :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; App single CSS
          [:link {:type "text/css" :rel "stylesheet" :href (shared/cdn "/main.css")}]
          ;; jQuery needed by Bootstrap JavaScript
          shared/jquery
          shared/ie-jquery-fix
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.min.js"}]
          ;; Ziggeo
          shared/ziggeo-css
          shared/ziggeo-js
          ;; Stripe
          shared/stripe-js]
   :body [:body
          shared/tag-manager-body
          [:div#app
           [:div.oc-loading.active
            [:div.oc-loading-inner
             [:div.oc-loading-heart]
             [:div.oc-loading-body]]]]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          [:div.preload-interstitial]
          ;; Static js files
          [:script {:src (shared/cdn "/lib/static-js.js")}]
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
          shared/bootstrap-js
          ;; Google Analytics
          [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js" :async true}]
          ;; Intercom (Support)
          [:script {:src (shared/cdn "/lib/intercom.js")}]
          ;; Headway (What's New)
          [:script {:type "text/javascript" :src "//cdn.headwayapp.co/widget.js"}]
          ;; Compiled oc.min.js from our CDN
          shared/oc-js
          ;; Compiled assets
          shared/oc-assets-js
          (when (= (env :fullstory) "true")
            (shared/fullstory-init))
          (shared/google-analytics-init)]})