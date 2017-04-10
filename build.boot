;; boot show --updates
(def cljs-deps
  '[[adzerk/boot-cljs "1.7.228-2" :scope "test"]
    [adzerk/boot-reload "0.4.13" :scope "test"]
    [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]
    [tolitius/boot-check "0.1.4" :scope "test"]

    [org.clojure/clojure "1.9.0-alpha14"] ; Lisp on the JVM http://clojure.org/documentation
    ;; NB: do not update to 1.9.493+ since it's broken w/ advanced compilation
    [org.clojure/clojurescript "1.9.473"] ; ClojureScript compiler https://github.com/clojure/clojurescript

    [org.omcljs/om "1.0.0-alpha48" :excludes [cljsjs/react]] ; Cljs interface to React https://github.com/omcljs/om

    [rum "0.10.8" :exclusions [cljsjs/react]] ; https://github.com/tonsky/rum

    [org.martinklepsch/derivatives "0.2.0"] ; Chains of derived data https://github.com/martinklepsch/derivatives

    [cljs-http "0.1.42"] ; HTTP for cljs https://github.com/r0man/cljs-http
    [prismatic/schema "1.1.3"] ; Dependency of om-tools https://github.com/Prismatic/schema
    [prismatic/plumbing "0.5.3"] ; Dependency of om-tools https://github.com/Prismatic/plumbing
    [org.clojars.martinklepsch/om-tools "0.4.0-w-select"] ; Tools for Om https://github.com/plumatic/om-tools/pull/91
    [secretary "2.0.0.1-41b949"] ; Client-side router https://github.com/gf3/secretary
    [prismatic/dommy "1.1.0"] ; DOM manipulation and event library https://github.com/Prismatic/dommy
    [cljs-flux "0.1.2"] ; Flux implementation for Om https://github.com/kgann/cljs-flux
    [com.cognitect/transit-cljs "0.8.239"] ; ClojureScript wrapper for JavaScript JSON https://github.com/cognitect/transit-cljs
    [racehub/om-bootstrap "0.6.1"] ; Bootstrap for Om https://github.com/racehub/om-bootstrap
    [org.clojure.bago/cljs-dynamic-resources "0.0.3"] ; Dynamically load JavaScript and CSS https://github.com/bago2k4/cljs-dynamic-resources
    [com.andrewmcveigh/cljs-time "0.5.0-alpha2"] ; A clj-time inspired date library for clojurescript. https://github.com/andrewmcveigh/cljs-time
    [funcool/cuerdas "2.0.3"] ; String manipulation library for Clojure(Script) https://github.com/funcool/cuerdas
    [medley "0.8.4"] ; lightweight library of useful, mostly pure functions that are "missing" from clojure.core
    [defun "0.3.0-RC1"] ; defun used mostly to port some fn from oc-lib https://github.com/killme2008/defun
    [environ "1.1.0"] ; Get environment settings from different sources https://github.com/weavejester/environ

    [cljsjs/react "15.4.2-2"] ; A Javascript library for building user interfaces https://github.com/cljsjs/packages
    [cljsjs/react-dom "15.4.2-2"] ; A Javascript library for building user interfaces https://github.com/cljsjs/packages

    [cljsjs/jwt-decode "2.1.0-0"] ; Decode JWT tokens, mostly useful for browser applications. https://github.com/cljsjs/packages/tree/master/jwt-decode
    [cljsjs/rangy-selectionsaverestore "1.3.0-1"]
    [cljsjs/raven "3.9.1-0"] ; Sentry JS https://github.com/cljsjs/packages/tree/master/raven
    [cljsjs/d3 "4.3.0-3"] ; d3 externs https://clojars.org/cljsjs/d3
    [cljsjs/medium-editor "5.22.1-2"] ; Medium editor https://clojars.org/cljsjs/medium-editor
    [cljsjs/hammer "2.0.4-5"] ; Touch handler http://hammerjs.github.io/
    [cljsjs/emojione "2.2.6-1"] ; Emojione http://emojione.com
    [cljsjs/clipboard "1.5.13-1"] ; Copy to clipboard https://github.com/zenorocha/clipboard.js
    [cljsjs/emojione-picker "0.3.6-2"] ; EmojionePicker cljsjs package https://github.com/tommoor/emojione-picker
    [org.martinklepsch/cljsjs-medium-button "0.0.0-225390f882986a8a7aee786bde247b5b2122a40b-2"]
    [lockedon/if-let "0.1.0"] ; More than one binding for if/when macros https://github.com/LockedOn/if-let
    [com.taoensso/timbre "4.9.0-alpha1"] ; Pure Clojure/Script logging library https://www.taoensso.com

    [binaryage/devtools "0.9.2"] ; Chrome DevTools enhancements https://github.com/binaryage/cljs-devtools

    ;; ------- Deps for project repl ------------------
    [adzerk/boot-cljs-repl   "0.3.3"] ;; latest release
    [com.cemerick/piggieback "0.2.1"  :scope "test"]
    [weasel                  "0.7.0"  :scope "test"]
    [org.clojure/tools.nrepl "0.2.12" :scope "test"]
    ;; ------------------------------------------------
])

(def static-site-deps
  '[[hiccup "2.0.0-alpha1" :scope "test"]
    [perun "0.3.0" :scope "test"]
    [compojure "1.6.0-beta3" :scope "test"]
    [pandeiro/boot-http "0.7.6" :scope "test"]
    [deraen/boot-sass "0.3.0" :scope "test"]
    [org.slf4j/slf4j-nop "1.7.24" :scope "test"]])

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
                       ;; We have a local copy of the sprites css
                       ;; to make sure it's loaded from our CDN
                       ; ['cljsjs/emojione
                       ;   "cljsjs/emojione/common/sprites/emojione.sprites.css"
                       ;   "public/css/emojione.sprites.css"]
                       ['cljsjs/emojione
                        "cljsjs/emojione/common/sprites/emojione.sprites.png"
                        "public/img/emojione.sprites.png"]
                       ['cljsjs/emojione
                        "cljsjs/emojione/common/sprites/emojione.sprites.svg"
                        "public/img/emojione.sprites.svg"]
                       ['cljsjs/emojione-picker
                        "cljsjs/emojione-picker/common/emojione-picker.css"
                        "public/css/emojione-picker.css"]}})

;; We use a bunch of edn files in `resources/pages` to declare a "page"
;; these edn files can hold additional information about the page such
;; as it's permalink identifier (`:page` key) or the page's title etc.

(deftask test!
  "Run tests."
  []
  (set-env! :source-paths #(conj % "test")
            :dependencies #(into % '[[cljs-react-test "0.1.4-SNAPSHOT" :scope "test" :exclusions [cljsjs/react-with-addons]]]))
  (test-cljs :js-env :phantom
             :exit? true
             :update-fs? true
             :namespaces #{"test.oc.web.*"}
             :cljs-opts {:optimizations :whitespace
                         :foreign-libs [{:provides ["cljsjs.react"]
                                         :file "https://cdnjs.cloudflare.com/ajax/libs/react/15.4.2/react-with-addons.js"
                                         :file-min "https://cdnjs.cloudflare.com/ajax/libs/react/15.4.2/react-with-addons.min.js"}]}))

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
  (comp (serve :handler 'oc.server/handler
               :port 3559)
        (from-jars)
        (watch)
        (sass)
        (build-site)
        (cljs-repl)
        (reload :asset-path "/public"
                :on-jsload 'oc.web.core/on-js-reload)
        (cljs :optimizations :none
              :compiler-options {:source-map-timestamp true
                                 :preloads '[devtools.preload]})))

(deftask dev-advanced 
  "Advanced build to be used in development to find compilation/externs errors."
  []
  (comp (serve :handler 'oc.server/handler
               :port 3559)
        (from-jars)
        (watch)
        (sass)
        (build-prod-site)
        (cljs :optimizations :advanced
              :source-map true
              :compiler-options {
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
  (comp (from-jars)
        (sass :output-style :compressed)
        (build-prod-site)
        (cljs :optimizations :advanced
              :source-map true
              :compiler-options {:externs ["public/js/externs.js"]
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
        (cljs :optimizations :advanced
              :source-map true
              :compiler-options {:externs ["public/js/externs.js"]})))
