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
    [org.clojure/clojurescript "0.0-3308"] ; Cljs compiler https://github.com/clojure/clojurescript
    [org.clojure/core.async "0.1.346.0-17112a-alpha"] ; Async library https://github.com/clojure/core.async
    [org.omcljs/om "0.9.0" :exclusions [cljsjs/react]] ; Cljs interface to React https://github.com/omcljs/om
    [cljs-http "0.1.36-SNAPSHOT"] ; Http for cljs https://github.com/r0man/cljs-http
    [prismatic/om-tools "0.3.11"] ; Tools for Om https://github.com/Prismatic/om-tools
    [sablono "0.3.4" :exclusions [cljsjs/react]] ; Hiccup templating for Om/React https://github.com/r0man/sablono
    [secretary "1.2.3"] ; Client-side router https://github.com/gf3/secretary
    [cljs-react-test "0.1.3-SNAPSHOT" :exclusions [cljsjs/react]] ; React test utilities https://github.com/bensu/cljs-react-test
    [prismatic/dommy "1.1.0"] ; DOM manipulation and event library https://github.com/Prismatic/dommy
    [cljs-flux "0.1.1"] ; Flux implementation for Om https://github.com/kgann/cljs-flux
    [com.cognitect/transit-cljs "0.8.220"] ; Cljs wrapper for javascript JSON https://github.com/cognitect/transit-cljs
    [racehub/om-bootstrap "0.5.3"] ; Bootstrap for Om https://github.com/racehub/om-bootstrap
    [noencore "0.1.21"] ; Clojure & ClojureScript functions not in core. https://github.com/r0man/noencore
  ]

  :plugins [
    [lein-cljsbuild "1.0.6"] ; ClojureScript compiler https://github.com/emezeske/lein-cljsbuild
    [lein-figwheel "0.3.7"] ; Dynamic development environment https://github.com/bhauman/lein-figwheel
    [lein-ancient "0.6.7"] ; Check for outdated dependencies https://github.com/xsc/lein-ancient
    [lein-doo "0.1.3-SNAPSHOT"] ; A plugin to run tests in many JS environments https://github.com/bensu/doo
  ]

  :profiles {
    :dev {
      :dependencies [
        [ring "1.4.0"]
        [compojure "1.4.0"]]}}

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {
    :builds {
      :dev {
        :id "dev"
        :source-paths ["src"]

        :figwheel { :on-jsload "open-company-web.core/on-js-reload" }

        :compiler {
          :main open-company-web.core
          :asset-path "/js/compiled/out"
          :output-to "resources/public/js/compiled/open_company.js"
          :output-dir "resources/public/js/compiled/out"
          :source-map-timestamp true
          :pretty-print true}}

      :min {
        :id "min"
        :source-paths ["src"]
        :compiler {
          :output-to "resources/public/js/compiled/open_company.js"
          :main open-company-web.core
          :optimizations :advanced
          :pretty-print false}}

      :test {
        :id "test"
        :source-paths ["src" "test"]
        :compiler {
          :main "test.test-runner"
          :output-to "target/testable.js"
          :source-map "target/testable.js.map"
          :optimizations :whitespace
          :cache-analysis false
          :pretty-print true}}}}

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
   :ring-handler open-company-web.server/handler

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
    "test!" ["doo" "phantom" "test" "once"]
  }
)
