;; boot show --updates
(def cljs-deps
  '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
    [adzerk/boot-reload "0.4.10" :scope "test"]
    [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT" :scope "test"]
    [tolitius/boot-check "0.1.2" :scope "test"]

    ;; --- DO NOT UPDATE CLJS, the 1.9.93 fails for us
    [org.clojure/clojurescript "1.9.76"] ; ClojureScript compiler https://github.com/clojure/clojurescript]

    ;; --- DO NOT UPDATE OM, the 1.x.x code requires changes on our part
    [org.omcljs/om "0.9.0" :excludes [cljsjs/react]] ; Cljs interface to React https://github.com/omcljs/om

    [cljs-http "0.1.41"] ; HTTP for cljs https://github.com/r0man/cljs-http
    [prismatic/schema "1.1.2"] ; Dependency of om-tools https://github.com/Prismatic/schema
    [prismatic/plumbing "0.5.3"] ; Dependency of om-tools https://github.com/Prismatic/plumbing
    [prismatic/om-tools "0.4.0"] ; Tools for Om https://github.com/Prismatic/om-tools
    [secretary "2.0.0.1-41b949"] ; Client-side router https://github.com/gf3/secretary
    [prismatic/dommy "1.1.0"] ; DOM manipulation and event library https://github.com/Prismatic/dommy
    [cljs-flux "0.1.2"] ; Flux implementation for Om https://github.com/kgann/cljs-flux
    [com.cognitect/transit-cljs "0.8.239"] ; ClojureScript wrapper for JavaScript JSON https://github.com/cognitect/transit-cljs
    [racehub/om-bootstrap "0.6.1"] ; Bootstrap for Om https://github.com/racehub/om-bootstrap
    [org.clojure.bago/cljs-dynamic-resources "0.0.3"] ; Dynamically load JavaScript and CSS https://github.com/bago2k4/cljs-dynamic-resources
    [com.andrewmcveigh/cljs-time "0.5.0-alpha1"] ; A clj-time inspired date library for clojurescript. https://github.com/andrewmcveigh/cljs-time
    [funcool/cuerdas "0.8.0"] ; String manipulation library for Clojure(Script) https://github.com/funcool/cuerdas
    [medley "0.8.2"] ; lightweight library of useful, mostly pure functions that are "missing" from clojure.core

    ;; --- DO NOT UPDATE REACT, the 15.x.x code requires changes on our part
    [cljsjs/react "0.14.7-0"] ; A Javascript library for building user interfaces https://github.com/cljsjs/packages
    [cljsjs/react-dom "0.14.7-0"] ; A Javascript library for building user interfaces https://github.com/cljsjs/packages
    
    [cljsjs/raven "2.1.0-0"] ; Sentry JS https://github.com/cljsjs/packages/tree/master/raven
    [cljsjs/d3 "3.5.16-0"] ; d3 externs https://clojars.org/cljsjs/d3
    [cljsjs/medium-editor "5.15.0-1"] ; Medium editor https://clojars.org/cljsjs/medium-editor
    [cljsjs/filestack "2.4.10-0"] ; Filestack https://clojars.org/cljsjs/filestack
    [cljsjs/hammer "2.0.4-5"] ; Touch handler http://hammerjs.github.io/
    [cljsjs/emojione "2.1.4-1"] ; Emojione http://emojione.com
    [cljsjs/clipboard "1.5.9-0"] ; Copy to clipboard https://github.com/zenorocha/clipboard.js
    [org.martinklepsch/cljsjs-medium-button "0.0.0-225390f882986a8a7aee786bde247b5b2122a40b-2"]
    [lockedon/if-let "0.1.0"]]) ; More than one binding for if/when macros https://github.com/LockedOn/if-let

(def static-site-deps
  '[[hiccup "1.0.5" :scope "test"]
    [perun "0.3.0" :scope "test"]
    [compojure "1.5.1" :scope "test"]
    [pandeiro/boot-http "0.7.3" :scope "test"]
    [deraen/boot-sass "0.2.1" :scope "test"]
    [org.slf4j/slf4j-nop "1.7.21" :scope "test"]])

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
         '[io.perun :as p]
         '[boot.util :as util])

(deftask from-jars
  "Import files from jars (e.g. CLJSJS) and move them to the desired location in the fileset."
  [i imports IMPORT #{[sym str str]} "Tuples describing imports: [jar-symbol path-in-jar target-path]"]
  (let [add-jar-args (into {} (for [[j p]   imports] [j (re-pattern (str "^" p "$"))]))
        move-args    (into {} (for [[_ p t] imports] [(re-pattern (str "^" p "$")) t]))]
    (sift :add-jar add-jar-args :move move-args)))

(task-options!
 from-jars {:imports #{['cljsjs/emojione
                        "cljsjs/emojione/common/sprites/emojione.sprites.svg"
                        "public/img/emojione.sprites.svg"]}})

;; We use a bunch of edn files in `resources/pages` to declare a "page"
;; these edn files can hold additional information about the page such
;; as it's permalink identifier (`:page` key) or the page's title etc.

(deftask test! []
  (set-env! :source-paths #(conj % "test")
            :dependencies #(conj % '[cljs-react-test "0.1.3-SNAPSHOT" :scope "test"]))
  (test-cljs :js-env :phantom
             :exit? true
             :update-fs? true
             :namespaces #{"test.open-company-web.*"}))

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

(deftask dev []
  (comp (serve :handler 'oc.server/handler
               :port 3559)
        (from-jars)
        (watch)
        (sass)
        (build-site)
        (reload :asset-path "/public"
                :on-jsload 'open-company-web.core/on-js-reload)
        (cljs :optimizations :none
              :compiler-options {:source-map-timestamp true})))

(deftask dev-advanced []
  (comp (serve :handler 'oc.server/handler
               :port 3559)
        (from-jars)
        (watch)
        (sass)
        (build-site)
        (reload :asset-path "/public"
                :on-jsload 'open-company-web.core/on-js-reload)
        (cljs :optimizations :advanced
              :source-map true
              :compiler-options {:externs ["public/js/externs.js"]})))

(deftask prod-build []
  (comp (from-jars)
        (sass :output-style :compressed)
        (build-site)
        (cljs :optimizations :advanced
              :source-map true
              :compiler-options {:externs ["public/js/externs.js"]})))