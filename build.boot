(set-env! :source-paths #{"src" "scss"}
          :resource-paths #{"resources"}
          :dependencies '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
                          [adzerk/boot-reload "0.4.5" :scope "test"]
                          [deraen/boot-sass "0.2.0" :scope "test"]
                          [pandeiro/boot-http "0.7.2" :scope "test"]
                          [compojure "1.4.0" :scope "test"]

                          [org.clojure/clojure "1.8.0"] ; Lisp on the JVM http://clojure.org/documentation
                          [org.clojure/clojurescript "1.7.228"] ; ClojureScript compiler https://github.com/clojure/clojurescript
                                        ; --- DO NOT UPDATE OM, the 1.x.x code is Om Next and requires changes on our part https://github.com/omcljs/om/wiki/Quick-Start-(om.next)
                          [org.omcljs/om "0.9.0" :excludes [cljsjs/react]] ; Cljs interface to React https://github.com/omcljs/om
                          [cljs-http "0.1.39"] ; HTTP for cljs https://github.com/r0man/cljs-http
                          [prismatic/schema "1.0.4"] ; Dependency of om-tools https://github.com/Prismatic/schema
                          [prismatic/plumbing "0.5.2"] ; Dependency of om-tools https://github.com/Prismatic/plumbing
                          [prismatic/om-tools "0.4.0"] ; Tools for Om https://github.com/Prismatic/om-tools
                          [secretary "2.0.0.1-260a59"] ; Client-side router https://github.com/gf3/secretary
                          [prismatic/dommy "1.1.0"] ; DOM manipulation and event library https://github.com/Prismatic/dommy
                          [cljs-flux "0.1.2"] ; Flux implementation for Om https://github.com/kgann/cljs-flux
                          [com.cognitect/transit-cljs "0.8.237"] ; ClojureScript wrapper for JavaScript JSON https://github.com/cognitect/transit-cljs
                          [racehub/om-bootstrap "0.6.1"] ; Bootstrap for Om https://github.com/racehub/om-bootstrap
                          [noencore "0.2.0"] ; Clojure & ClojureScript functions not in core https://github.com/r0man/noencore
                          [org.clojure.bago/cljs-dynamic-resources "0.0.3"] ; Dynamically load JavaScript and CSS https://github.com/bago2k4/cljs-dynamic-resources
                          [com.andrewmcveigh/cljs-time "0.4.0"] ; A clj-time inspired date library for clojurescript. https://github.com/andrewmcveigh/cljs-time
                          [funcool/cuerdas "0.7.1"] ; String manipulation library for Clojure(Script) https://github.com/funcool/cuerdas
                          [cljsjs/react "0.14.3-0"] ; A Javascript library for building user interfaces https://github.com/cljsjs/packages
                          [medley "0.7.1"] ; lightweight library of useful, mostly pure functions that are "missing" from clojure.core
                          ])


(require '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[deraen.boot-sass :refer [sass]])

(deftask dev []
  (comp (serve :handler 'open-company-web.lib.server/boot-handler
               :reload true
               :port 3449)
        (watch)
        (reload :asset-path "/public"
                :on-jsload 'open-company-web.core/on-js-reload)
        (sass)
        (cljs)))
