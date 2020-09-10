(defproject open-company-web "3.0.0-SNAPSHOT"
  :description "OpenCompany Web: webapp, electron app (iOS and Android) and static HTML site."
  :url "https://github.com/open-company/open-company-web"
  :license {
    :name "GNU Affero General Public License Version 3"
    :url "https://www.gnu.org/licenses/agpl-3.0.en.html"
  }

  :min-lein-version "2.9.1"

  ;; JVM memory
  :jvm-opts ^:replace ["-Xms512m" "-Xmx3072m" "-server"]
  
  ;; Source paths
  :source-paths ["site"]

  ;; All profile dependencies
  :dependencies [;; Lisp on the JVM http://clojure.org/documentation
                 [org.clojure/clojure "1.10.2-alpha1"]
                 ;; Library for OC projects https://github.com/open-company/open-company-lib
                 [open-company/lib "0.17.29-alpha32"]
                 ;; Utility functions https://github.com/weavejester/medley
                 [medley "1.3.0"]
                 ;; HTML rendering https://github.com/weavejester/hiccup
                 [hiccup "2.0.0-alpha2"]
                 ;; String manipulation library https://github.com/funcool/cuerdas
                 [funcool/cuerdas "2020.03.26-3"]]
  
  ;; All profile plugins
  :plugins [;; Get environment settings from different sources https://github.com/weavejester/environ
            [lein-environ "1.1.0"]
            ;; Automatic restart task https://github.com/weavejester/lein-auto
            [lein-auto "0.1.3"]
            ;; Call shell from within Leiningen. https://github.com/hypirion/lein-shell
            [lein-shell "0.5.0"]]
  
  :auto {:start {:file-pattern #"\. (clj|cljs|cljx|cljc|edn) $"}}

  :profiles {

    ;; QA environment and dependencies
    :qa {
      :env {
        :hot-reload "false"
      }
      :dependencies [
        ;; Example-based testing https://github.com/marick/Midje
        ;; NB: org.clojure/tools.macro is pulled in manually
        ;; NB: clj-time is pulled in by oc.lib
        ;; NB: joda-time is pulled in by oc.lib via clj-time
        ;; NB: commons-codec pulled in by oc.lib
        [midje "1.9.9" :exclusions [joda-time org.clojure/tools.macro clj-time commons-codec]] 
      ]
      :plugins [
        ;; Example-based testing https://github.com/marick/lein-midje
        [lein-midje "3.2.2"]
        ;; Linter https://github.com/jonase/eastwood
        [jonase/eastwood "0.3.11"]
        ;; Static code search for non-idiomatic code https://github.com/jonase/kibit
        [lein-kibit "0.1.8" :exclusions [org.clojure/clojure]]
        [lein-pdo "0.1.1"]
      ]
    }

    ;; Dev environment and dependencies
    :dev [:qa {
      :env ^:replace {
        :hot-reload "true" ; reload code when changed on the file system
        :log-level "debug"
      }
      :plugins [
        ;; Check for code smells https://github.com/dakrone/lein-bikeshed
        ;; NB: org.clojure/tools.cli is pulled in by lein-kibit
        [lein-bikeshed "0.5.2" :exclusions [org.clojure/tools.cli]] 
        ;; Runs bikeshed, kibit and eastwood https://github.com/itang/lein-checkall
        [lein-checkall "0.1.1"]
        ;; pretty-print the lein project map https://github.com/technomancy/leiningen/tree/master/lein-pprint
        [lein-pprint "1.3.2"]
        ;; Check for outdated dependencies https://github.com/xsc/lein-ancient
        [lein-ancient "0.6.15"]
        ;; Catch spelling mistakes in docs and docstrings https://github.com/cldwalker/lein-spell
        [lein-spell "0.1.0"]
        ;; Dead code finder https://github.com/venantius/yagni
        [venantius/yagni "0.1.7" :exclusions [org.clojure/clojure]]
      ]  
    }]
    
    :repl-config [:dev {
      :dependencies [
        ;; Network REPL https://github.com/clojure/tools.nrepl
        [org.clojure/tools.nrepl "0.2.13"]
        ;; Pretty printing in the REPL (aprint ...) https://github.com/razum2um/aprint
        [aprint "0.1.3"]
      ]
      ;; REPL injections
      :injections [
        (require '[aprint.core :refer (aprint ap)]
                 '[clojure.stacktrace :refer (print-stack-trace)])
      ]
    }]

    ;; Production environment
    :prod {
      :env {
        :env "production"
        :hot-reload "false"
      }
    }

  }

  :repl-options {
    :welcome (println (str "\n" (slurp (clojure.java.io/resource "public/ascii_art.txt")) "\n"
                           "OpenCompany Web REPL\n"
                           "\nReady to do your bidding...\n"))
    :init-ns oc.site
  }

            
  :aliases {"start" ["pdo" "run" "watch-site," "watch-ocweb"] ; Site & cljs dev
            "watch-ocweb" ["auto" "ocweb"]
            "ocweb" ["shell" "npm" "run" "dev:watch"] ; start js dev task
            "watch-site" ["auto" "watch-site"] ; Build the static website
            "build-site" ["run" "dev"] ; build the HTML pages and watch for changes

            "build" ["do" "clean," "deps," "compile"] ; clean and build code
            "autotest" ["with-profile" "qa" "do" "midje" ":autotest"] ; watch for code changes and run affected tests
            "test!" ["with-profile" "qa" "do" "build," "midje"] ; build, init the DB and run all tests
            "repl" ["with-profile" "+repl-config" "repl"]
            "spell!" ["spell" "-n"] ; check spelling in docs and docstrings
            "bikeshed!" ["bikeshed" "-v" "-m" "120"] ; code check with max line length warning of 120 characters
            "ancient" ["ancient" ":all" ":allow-qualified"] ; check for out of date dependencies
            }

  ;; ----- Code check configuration -----

  ;; :eastwood {
  ;;   ;; Disable some linters that are enabled by default
  ;;   ;; contant-test - just seems mostly ill-advised, logical constants are useful in something like a `->cond` 
  ;;   ;; wrong-arity - unfortunate, but it's failing on 3/arity of sqs/send-message
  ;;   ;; implicit-dependencies - uhh, just seems dumb
  ;;   :exclude-linters [:constant-test :wrong-arity :implicit-dependencies]
  ;;   ;; Enable some linters that are disabled by default
  ;;   :add-linters [:unused-namespaces :unused-private-vars] ; :unused-locals]

  ;;   :config-files ["third-party-macros.clj"]
    
  ;;   ;; Exclude testing namespaces
  ;;   :tests-paths ["test"]
  ;;   :exclude-namespaces [:test-paths]
  ;; }

  :zprint {:old? false}

  :main oc.site
  :nrepl {:port 8451}
)