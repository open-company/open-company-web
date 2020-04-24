;; boot show --updates
(def cljs-deps
  '[
    ;; Boot tasks
    [adzerk/boot-cljs "2.1.5" :scope "test"] ; Boot task for compiling Cljs apps https://github.com/boot-clj/boot-cljs
    [adzerk/boot-reload "0.6.0" :scope "test"] ; Boot task providing live-reload of browser css, images https://github.com/adzerk-oss/boot-reload
    [crisptrutski/boot-cljs-test "0.3.4" :scope "test"] ; Boot task to run ClojureScript tests https://github.com/crisptrutski/boot-cljs-test
    [tolitius/boot-check "0.1.12" :scope "test"] ; Check, analyze and inspect Clojure/Script code https://github.com/tolitius/boot-check

    ;; Clojure/ClojureScript
    ;; NB: Need to change Clojure version in boot.properties in sync with this
    [org.clojure/clojure "1.10.1"] ; Lisp on the JVM http://clojure.org/documentation
    [org.clojure/clojurescript "1.10.597"] ; ClojureScript compiler https://github.com/clojure/clojurescript
    [org.clojure/core.rrb-vector "0.1.1"] ; Indirect dependency of many libs, >= 0.0.13 required for Java 11+ https://github.com/clojure/core.rrb-vector

    ;; Rum React Frameworks
    ;; Didn't update to 15.5.4+ just yet since it requires some changes to oc.web.rum-utils to remove .-PropTypes access
    ;; and some change to omcljs/om to not use createClass anymore. See React docs for more info.
    [cljsjs/react "16.11.0-0"] ; A Javascript library for building user interfaces https://github.com/cljsjs/packages
    [cljsjs/react-dom "16.11.0-0"] ; A Javascript library for building user interfaces https://github.com/cljsjs/packages
    [rum "0.11.4" :exclusions [cljsjs/react cljsjs/react-dom]] ; https://github.com/tonsky/rum
    [org.martinklepsch/derivatives "0.3.1-alpha"] ; Chains of derived data https://github.com/martinklepsch/derivatives
    [cljs-flux "0.1.2"] ; Flux implementation for Om https://github.com/kgann/cljs-flux

    ;; ClojureScript libraries
    [cljs-http "0.1.46"] ; HTTP for cljs https://github.com/r0man/cljs-http
    [clj-commons/secretary "1.2.5-SNAPSHOT"] ; Client-side router https://github.com/clj-commons/secretary
    [prismatic/dommy "1.1.0"] ; DOM manipulation and event library https://github.com/Prismatic/dommy
    [funcool/cuerdas "2.2.1"] ; String manipulation library for Clojure(Script) https://github.com/funcool/cuerdas
    [medley "1.2.0"] ; lightweight library of useful, mostly pure functions that are "missing" from clojure.core
    [org.martinklepsch/cljsjs-medium-button "0.0.0-225390f882986a8a7aee786bde247b5b2122a40b-2"] ; https://github.com/martinklepsch/cljsjs-medium-button
    [cljs-hash "0.0.2"] ; various hash functions for cljs https://github.com/davesann/cljs-hash
    [binaryage/oops "0.7.0"]  ; ClojureScript macros for convenient native Javascript object access. https://github.com/binaryage/cljs-oops

    ;; CLJSJS packages http://cljsjs.github.io/
    ;; Update together with resources/public/lib/jwt_decode/
    [cljsjs/jwt-decode "2.2.0-0"] ; Decode JWT tokens, mostly useful for browser applications. https://github.com/cljsjs/packages/tree/master/jwt-decode
    ;; -----------------------------------------------------
    [cljsjs/sentry-browser "5.11.1-0"] ; Sentry JS https://github.com/cljsjs/packages/tree/master/sentry-browser
    [cljsjs/medium-editor "5.23.2-0"] ; Medium editor https://clojars.org/cljsjs/medium-editor
    [cljsjs/emojione "2.2.6-1"] ; Emojione http://emojione.com
    [cljsjs/clipboard "2.0.4-0"] ; Copy to clipboard https://github.com/zenorocha/clipboard.js
    [cljsjs/web-animations "2.1.4-0"] ; JavaScript implementation of the Web Animations API https://github.com/web-animations/web-animations-js
    [cljsjs/moment-timezone "0.5.11-1"] ; Timezone support for moment.js https://github.com/moment/moment-timezone/
    [cljsjs/filestack "0.9.9-0"] ; Filestack image manipulatino and storing https://github.com/filestack/filestack-js
    ;; update filestack 3.5.0-0
    [cljsjs/emoji-mart "2.2.1-0"] ; EmojiMart picker for native emoji picking https://github.com/missive/emoji-mart
    [cljsjs/localforage "1.5.3-0"] ; Offline storage, improved. Wraps IndexedDB, WebSQL, or localStorage using a simple but powerful API. https://github.com/localForage/localForage
    [cljsjs/react-giphy-selector "0.0.3-0"] ;; A very customizable react search component for picking the perfect giphy. https://github.com/tshaddix/react-giphy-selector
    [cljsjs/react-virtualized "9.21.1-0" :exclusions [cljsjs/react cljsjs/react-dom]] ;; React components for efficiently rendering large lists and tabular data
    [cljsjs/hammer "2.0.8-0"] ; Hammer: recognize (multi) touch gestures on web https://hammerjs.github.io/

    ;; Library for OC projects https://github.com/open-company/open-company-lib
    [open-company/lib "0.17.27-alpha1" :excludes [amazonica liberator http-kit ring/ring-codec com.stuartsierra/component clj-time]]
    ;; In addition to common functions, brings in the following common dependencies used by this project:
    ;; defun - Erlang-esque pattern matching for Clojure functions https://github.com/killme2008/defun
    ;; if-let - More than one binding for if/when macros https://github.com/LockedOn/if-let
    ;; Timbre - Pure Clojure/Script logging library https://github.com/ptaoussanis/timbre
    ;; environ - Get environment settings from different sources https://github.com/weavejester/environ
    ;; hickory - HTML as data https://github.com/davidsantiago/hickory
    ;; cljs-time - clj-time inspired date library for clojurescript. https://github.com/andrewmcveigh/cljs-time
    ;; com.taoensso/sente - WebSocket client https://github.com/ptaoussanis/sente

    ;; NB: This needs pulled in after oc.lib
    [clojure-humanize "0.2.2" :excludes [com.andrewmcveigh/cljs-time]] ; Produce human readable strings in clojure https://github.com/trhura/clojure-humanize

    ;; ------- Deps for project repl ------------------
    ;; The following dependencies are from: https://github.com/adzerk-oss/boot-cljs-repl
    [adzerk/boot-cljs-repl   "0.4.0"]
    [cider/piggieback        "0.4.2"]
    [weasel                  "0.7.0"]
    [nrepl                   "0.6.0"]
    ;; ------------------------------------------------

])

(def static-site-deps
  '[[hiccup "2.0.0-alpha1" :scope "test"]
    [perun "0.3.0" :scope "test"]
    [compojure "1.6.1" :scope "test"]
    [pandeiro/boot-http "0.8.3" :scope "test"]
    [deraen/boot-sass "0.5.0" :scope "test"]
    [org.slf4j/slf4j-nop "1.8.0-beta2" :scope "test"]])

(set-env!
  :source-paths   #{"src" "scss" "site"}
  :resource-paths #{"resources"}
  :dependencies   (into cljs-deps static-site-deps))

(require '[pandeiro.boot-http  :refer [serve]]
         '[adzerk.boot-cljs :refer [cljs]]
         '[crisptrutski.boot-cljs-test :refer [test-cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[tolitius.boot-check :as check]
         '[deraen.boot-sass :refer [sass]]
         '[medley.core :as med]
         '[io.perun :as p]
         '[boot.util :as util]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

(deftask from-jars
  "Import files from jars (e.g. CLJSJS) and move them to the desired location in the fileset."
  [i imports IMPORT #{[sym str str]} "Tuples describing imports: [jar-symbol path-in-jar target-path]"]
  (let [re-union     (fn [paths] (re-pattern (clojure.string/join "|" (map #(str "^" % "$") paths))))
        add-jar-args (med/map-vals #(re-union (map second %)) (group-by first imports))
        move-args    (into {} (for [[_ p t] imports] [(re-pattern (str "^" p "$")) t]))]
    (sift :add-jar add-jar-args :move move-args)))

(task-options!
 from-jars {:imports #{['cljsjs/emojione
                        "cljsjs/emojione/common/css/emojione.css"
                        "public/css/emojione.css"]
                       ['cljsjs/emojione
                        "cljsjs/emojione/common/css/emojione.min.css"
                        "public/css/emojione.min.css"]
                       ['cljsjs/emoji-mart
                        "cljsjs/emoji-mart/common/emoji-mart.css"
                        "public/css/emoji-mart.css"]
                       ;; MediumEditor default theme
                       ['cljsjs/medium-editor
                        "cljsjs/medium_editor/common/medium-editor.css"
                        "public/css/medium-editor/medium-editor.css"]
                       ['cljsjs/medium-editor
                        "cljsjs/medium_editor/common/medium-editor.min.css"
                        "public/css/medium-editor/medium-editor.min.css"]
                       ['cljsjs/medium-editor
                        "cljsjs/medium_editor/common/themes/default.css"
                        "public/css/medium-editor/default.css"]
                       ['cljsjs/medium-editor
                        "cljsjs/medium_editor/common/themes/default.min.css"
                        "public/css/medium-editor/default.min.css"]}})

;; We use a bunch of edn files in `resources/pages` to declare a "page"
;; these edn files can hold additional information about the page such
;; as it's permalink identifier (`:page` key) or the page's title etc.

(deftask test!
  "Run tests."
  []
  (set-env! :source-paths #(conj % "test")
            :dependencies #(into % '[[binaryage/devtools "0.9.8"] ; Chrome DevTools enhancements https://github.com/binaryage/cljs-devtools
                                     [doo "0.1.8" :scope "test"]
                                     [cljs-react-test "0.1.4-SNAPSHOT" :scope "test" :exclusions [cljsjs/react-with-addons]]]))
  (test-cljs :js-env :phantom
             :exit? true
             :update-fs? true
             :namespaces ['test.oc.web.components.user-profile
                          'test.oc.web.components.ui.loading
                          'test.oc.web.components.ui.login-button
                          'test.oc.web.components.ui.org-avatar
                          'test.oc.web.components.ui.user-avatar]
             :cljs-opts {:optimizations :whitespace
                         :foreign-libs [{:provides ["cljsjs.react"]
                                         :file "https://cdnjs.cloudflare.com/ajax/libs/react/16.11.0/cjs/react.development.js"
                                         :file-min "https://cdnjs.cloudflare.com/ajax/libs/react/16.11.0/cjs/react.development.js"}
                                        {:provides ["cljsjs.raven"]
                                         :file "https://cdnjs.cloudflare.com/ajax/libs/raven.js/3.17.0/raven.min.js"
                                         :file-min "https://cdnjs.cloudflare.com/ajax/libs/raven.js/3.17.0/raven.min.js"}]}))

(defn page? [f]
  (and (.startsWith (:path f) "pages/")
       (.endsWith (:path f) ".edn")))

(defn page->permalink [f]
  (-> (read-string (slurp (:full-path f)))
      :page name (str ".html")))

(deftask build-site []
  (comp (p/base)
        (p/permalink :permalink-fn page->permalink
                     :filterer page?)
        (p/render :renderer 'oc.core/static-page
                  :filterer page?)
        ;; We're not actually rendering a collection here but using the collection task
        ;; is often a handy hack to render pages which are "unique"
        (p/collection :renderer 'oc.core/app-shell
                      :page "app-shell.html"
                      :filterer identity)))

(deftask build-prod-site []
  (comp (p/base)
        (p/permalink :permalink-fn page->permalink
                     :filterer page?)
        (p/render :renderer 'oc.core/static-page
                  :filterer page?)
        ;; We're not actually rendering a collection here but using the collection task
        ;; is often a handy hack to render pages which are "unique"
        (p/collection :renderer 'oc.core/prod-app-shell
                      :page "app-shell.html"
                      :filterer identity)))

(deftask dev
  "OC Development build"
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (serve :handler 'oc.server/handler
               :port 3559)
        (from-jars)
        (watch)
        (sass)
        (build-site)
        (cljs-repl)
        (reload :asset-path "/public"
                :on-jsload 'oc.web.core/on-js-reload)
        (cljs :ids #{"public/oc"}
              :optimizations :none
              :source-map true
              :compiler-options {:source-map-timestamp true
                                 :parallel-build true
                                 :preloads '[devtools.preload]})))

(deftask dev-advanced
  "Advanced build to be used in development to find compilation/externs errors."
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (serve :handler 'oc.server/handler
               :port 3559)
        (from-jars)
        (watch)
        (sass {:data "$is_wut: true;"})
        (build-prod-site)
        (cljs :ids #{"public/oc"}
              :optimizations :advanced
              :source-map true
              :compiler-options {
                :parallel-build true
                :pretty-print true
                :pseudo-names true
                :externs ["public/js/externs.js"]
                :preloads '[devtools.preload]
                :external-config {
                  :devtools/config {
                    :print-config-overrides true
                    :disable-advanced-mode-check true}}})))

(deftask staging-build
  "OC Staging build."
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (from-jars)
        (sass :output-style :compressed)
        (build-prod-site)
        (cljs :ids #{"public/oc"}
              :optimizations :advanced
              :source-map true
              :compiler-options {:parallel-build true
                                 :externs ["public/js/externs.js"]
                                 :preloads '[devtools.preload]
                                 :external-config {
                                  :devtools/config {
                                    :print-config-overrides true
                                    :disable-advanced-mode-check true}}})))

(deftask prod-build
  "OC Production build."
  []
  (comp (from-jars)
        (sass :output-style :compressed)
        (build-prod-site)
        (cljs :ids #{"public/oc"}
              :optimizations :advanced
              :source-map true
              :compiler-options {:parallel-build true
                                 :externs ["public/js/externs.js"]})))

(deftask check-sources!
  "Check source files with yagni, eastwood, kibit and bikeshed."
  []
  (set-env! :source-paths #{"src" "test"})
  (comp
    (check/with-yagni "-t")
    (check/with-eastwood "-t")
    (check/with-kibit "-t")
    (check/with-bikeshed "-t" :options {:verbose true
                                        :max-line-length 120})
    (check/throw-on-errors)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Electron App

(deftask dev-electron
  "Carrot electron app loading localhost:3559"
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (from-jars)
        (watch)
        ;; double backslash necessary for building on Windows
        ;; https://github.com/boot-clj/boot-cljs/pull/118
        (cljs :ids #{"electron/main"}
              :optimizations :simple
              :compiler-options {:closure-defines {'oc.electron.main/dev?        true
                                                   'oc.electron.main/web-origin  "http://localhost:3559"
                                                   'oc.electron.main/auth-origin "http://localhost:3003"}})
        (target)))

(deftask staging-electron
  "Carrot electron app loading staging.carrot.io"
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (cljs :ids #{"electron/main"}
              :optimizations :simple
              :compiler-options {:closure-defines {'oc.electron.main/dev?        false
                                                   'oc.electron.main/web-origin  "https://staging.carrot.io"
                                                   'oc.electron.main/auth-origin "https://staging-auth.carrot.io"
                                                   'oc.electron.main/sentry-dsn  "https://d4318ef3fbba49668211f37c56157a19@sentry.io/1509179"
                                                   }})
        (target)))

(deftask staging-electron-windows
  "Carrot electron app loading staging.carrot.io"
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (cljs :ids #{"electron\\main"}
              :optimizations :simple
              :compiler-options {:closure-defines {'oc.electron.main/dev?        false
                                                   'oc.electron.main/web-origin  "https://staging.carrot.io"
                                                   'oc.electron.main/auth-origin "https://staging-auth.carrot.io"
                                                   'oc.electron.main/sentry-dsn  "https://d4318ef3fbba49668211f37c56157a19@sentry.io/1509179"
                                                   }})
        (target)))

(deftask prod-electron
  "Carrot electron app loading production carrot.io"
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (cljs :ids #{"electron/main"}
              :optimizations :simple
              :compiler-options {:closure-defines {'oc.electron.main/dev?        false
                                                   'oc.electron.main/web-origin  "https://carrot.io"
                                                   'oc.electron.main/auth-origin "https://beta-auth.carrot.io"
                                                   'oc.electron.main/sentry-dsn  "https://760ab937836c444895614a24d8fd7b23@sentry.io/1509184"
                                                   }})
        (target)))

(deftask prod-electron-windows
  "Carrot electron app loading production carrot.io"
  []
  (set-env! :dependencies #(into % '[[binaryage/devtools "0.9.8"]]))
  (comp (cljs :ids #{"electron\\main"}
              :optimizations :simple
              :compiler-options {:closure-defines {'oc.electron.main/dev?        false
                                                   'oc.electron.main/web-origin  "https://carrot.io"
                                                   'oc.electron.main/auth-origin "https://beta-auth.carrot.io"
                                                   'oc.electron.main/sentry-dsn  "https://760ab937836c444895614a24d8fd7b23@sentry.io/1509184"
                                                   }})
        (target)))
