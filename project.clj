(defproject open-company-web "0.1.0-SNAPSHOT"
  :description "OpenCompany.io Web Application"
  :url "https://opencompany.io/"
  :license {
    :name "Mozilla Public License v2.0"
    :url "http://www.mozilla.org/MPL/2.0/"
  }

  :min-lein-version "2.5.1" ; highest version supported by Travis-CI as of 7/5/2015

  :dependencies [
    [org.clojure/clojure "1.7.0"] ; Lisp on the JVM http://clojure.org/documentation
    [org.clojure/clojurescript "0.0-3308"] ; ClojureScript compiler https://github.com/clojure/clojurescript
    [org.clojure/core.async "0.1.346.0-17112a-alpha"] ; Async library https://github.com/clojure/core.async
    [org.omcljs/om "0.9.0"] ; ClojureScript interface to React https://github.com/omcljs/om
    [prismatic/om-tools "0.3.11"] ; Tools for Om https://github.com/Prismatic/om-tools
    [sablono "0.3.4"] ; Hiccup templating for Om/React https://github.com/r0man/sablono
  ]

  :plugins [
    [lein-cljsbuild "1.0.6"] ; ClojureScript compiler https://github.com/emezeske/lein-cljsbuild
    [lein-figwheel "0.3.7"] ; Dynamic development environment https://github.com/bhauman/lein-figwheel
    [lein-ancient "0.6.7"] ; Check for outdated dependencies https://github.com/xsc/lein-ancient
  ]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]

              :figwheel { :on-jsload "open-company-web.core/on-js-reload" }

              :compiler {:main open-company-web.core
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/open_company.js"
                         :output-dir "resources/public/js/compiled/out"
                         :source-map-timestamp true }}
             {:id "min"
              :source-paths ["src"]
              :compiler {:output-to "resources/public/js/compiled/open_company.js"
                         :main open-company-web.core
                         :optimizations :advanced
                         :pretty-print false}}]}

  :figwheel {
   ;; :http-server-root "public" ;; default and assumes "resources"
   ;; :server-port 3449 ;; default
   ;; :server-ip "127.0.0.1"

   :css-dirs ["resources/public/css"] ;; watch and update CSS

   ;; Start an nREPL server into the running figwheel process
   ;; :nrepl-port 7888

   ;; Server Ring Handler (optional)
   ;; if you want to embed a ring handler into the figwheel http-kit
   ;; server, this is for simple ring servers, if this
   ;; doesn't work for you just run your own server :)
   ;; :ring-handler hello_world.server/handler

   ;; To be able to open files in your editor from the heads up display
   ;; you will need to put a script on your path.
   ;; that script will have to take a file path and a line number
   ;; ie. in  ~/bin/myfile-opener
   ;; #! /bin/sh
   ;; emacsclient -n +$2 $1
   ;;
   ;; :open-file-command "myfile-opener"

   ;; if you want to disable the REPL
   ;; :repl false

   ;; to configure a different figwheel logfile path
   ;; :server-logfile "tmp/logs/figwheel-logfile.log"
  }

  :aliases {
    "ancient" ["with-profile" "dev" "do" "ancient" ":allow-qualified," "ancient" ":plugins" ":allow-qualified"] ; check for out of date dependencies
  }
)