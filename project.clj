(defproject open-company-web "0.1.0-SNAPSHOT"
  :description "OpenCompany Web Application"
  :url "https://opencompany.io/"
  :license {
    :name "Mozilla Public License v2.0"
    :url "http://www.mozilla.org/MPL/2.0/"
  }

  :min-lein-version "2.5.1" ; highest version supported by Travis-CI as of 1/19/2016

  :dependencies [
    [org.clojure/clojure "1.8.0"] ; Lisp on the JVM http://clojure.org/documentation
    [org.clojure/clojurescript "1.7.228"] ; ClojureScript compiler https://github.com/clojure/clojurescript
    ; --- DO NOT UPDATE OM, the 1.x.x code is Om Next and requires changes on our part https://github.com/omcljs/om/wiki/Quick-Start-(om.next)
    [org.omcljs/om "0.9.0" :excludes [cljsjs/react]] ; Cljs interface to React https://github.com/omcljs/om
    [cljs-http "0.1.39"] ; HTTP for cljs https://github.com/r0man/cljs-http
    [prismatic/schema "1.0.5"] ; Dependency of om-tools https://github.com/Prismatic/schema
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
  ]

  :plugins [
    [lein-cljsbuild "1.1.2"] ; ClojureScript compiler https://github.com/emezeske/lein-cljsbuild
    [lein-figwheel "0.5.0-6"] ; Dynamic development environment https://github.com/bhauman/lein-figwheel
    [lein-ancient "0.6.8"] ; Check for outdated dependencies https://github.com/xsc/lein-ancient
    [lein-doo "0.1.6"] ; A plugin to run tests in many JS environments https://github.com/bensu/doo
    [lein-deps-tree "0.1.2"] ; Print a tree of project dependencies https://github.com/the-kenny/lein-deps-tree
    [lein-kibit "0.1.2"] ; Static code analyzer https://github.com/jonase/kibit
    [lein-sassy "1.0.7"] ; Leiningen plugin to compile haml files https://github.com/101loops/lein-sass
  ]

  :profiles {
    :dev {
      :dependencies [
        [ring "1.4.0"] ; Web framework https://github.com/ring-clojure/ring
        [compojure "1.4.0"] ; Web routing http://github.com/weavejester/compojure
        [shodan "0.4.2"] ; A ClojureScript library providing wrappers for the JavaScript console API. https://github.com/noprompt/shodan
      ]
    }
    :devcards [:dev {
      :dependencies [
        [devcards "0.2.1-6"] ; Devcards aims to provide a visual REPL experience for ClojureScript https://github.com/bhauman/devcards
      ]
    }]
    :tests [:devcards {
      :dependencies [
        [cljs-react-test "0.1.3-SNAPSHOT"] ; React test utilities https://github.com/bensu/cljs-react-test
      ]
    }]
  }

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
          :optimizations :none
          :source-map-timestamp true
          :pretty-print true}}

      :devcards {
        :id "devcards"
        :source-paths ["src"]
        :figwheel { :devcards true}
        :compiler {
          :main "open-company-web.lib.devcards"
          :asset-path "/js/compiled/out_devcards"
          :output-to "resources/public/js/compiled/devcards/oc_devcards.js"
          :output-dir "resources/public/js/compiled/out_devcards"
          :optimizations :none
          :source-map-timestamp true
          :pretty-print true}}

      :min {
        :id "min"
        :source-paths ["src"]
        :compiler {
          :output-to "resources/public/js/compiled/open_company.js"
          :main open-company-web.core
          :externs ["resources/public/js/externs.js"]
          :optimizations :advanced
          :pretty-print false}}

      :test-navigation {
        :id "test-navigation"
        :source-paths ["src" "test"]
        :compiler {
          :main "test.test-navigation"
          :output-to "target/testable-navigation.js"
          :source-map false
          :optimizations :none}}

      :test-company {
        :id "test-company"
        :source-paths ["src" "test"]
        :compiler {
          :main "test.test-company"
          :output-to "target/testable-company.js"
          :source-map false
          :optimizations :none}}

      :test-ui-components {
        :id "test-ui-components"
        :source-paths ["src" "test"]
        :compiler {
          :main "test.test-ui-components"
          :output-to "target/testable-components.js"
          :source-map false
          :optimizations :none}}
  }}

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
    :ring-handler open-company-web.lib.server/handler

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

  :sass {
    :src "scss"
    :dst "resources/public/css"

    ;; other options (provided are default values):
    ; :delete-output-dir false
    ; :source-maps true
    :style :nested
    :syntax :scss
    }

  :aliases {
    "ancient" ["ancient" ":all" ":allow-qualified"] ; check for out of date dependencies
    "build" ["cljsbuild" "once" "dev"]
    "build!" ["cljsbuild" "once" "min"]
    "scss!" ["sass" "watch"]
    "figwheel!" ["with-profile" "+devcards" "figwheel"]
    "devcards!" ["with-profile" "+devcards" "figwheel" "devcards"]
    "test-navigation!" ["with-profile" "+tests" "doo" "phantom" "test-navigation" "once"]
    "test-company!" ["with-profile" "+tests" "doo" "phantom" "test-company" "once"]
    "test-ui-components!" ["with-profile" "+tests" "doo" "phantom" "test-ui-components" "once"]
    "test-all!" ["do" "test-navigation!," "test-company!," "test-ui-components!"] ; don't use in CI
  }

)