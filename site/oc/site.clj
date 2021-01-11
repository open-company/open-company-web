(ns oc.site
  (:require [hiccup.page :as hp]
            [environ.core :refer (env)]
            [cuerdas.core :as string]
            [oc.shared :as shared]
            [oc.pages.not-found :as not-found]
            [oc.pages.server-error :as server-error]
            [oc.pages.about :as about]
            [oc.pages.app-shell :as app-shell]
            [oc.pages.index :as index]
            [oc.pages.press-kit :as press-kit]
            [oc.pages.pricing :as pricing]
            [oc.pages.privacy :as privacy]
            [oc.pages.slack :as slack]
            [oc.pages.terms :as terms]))

(def contact-email "hello@carrot.io")
(def contact-mail-to (str "mailto:" contact-email))
(def oc-github "https://github.com/open-company")
(def anonymous-title "Start free")
(def your-digest-title "Launch Carrot")

(def options {:contact-email contact-email
              :contact-mail-to contact-mail-to
              :oc-github oc-github
              :anonymous-title anonymous-title
              :your-digest-title your-digest-title})

(defn- body-wrapper [body page opts]
  [:body
   {:class (when (env :covid-banner) "covid-banner")}
   shared/tag-manager-body
   [:div
    {:class "outer header"}
    shared/ph-banner
    (when (env :covid-banner)
      (shared/covid-banner page))
    (shared/nav (name page) opts)
    (shared/mobile-menu (name page) opts)]
   (if (fn? body) (body opts) body)
   (shared/footer opts)
   (shared/google-analytics-init)])

(def pages [{:page-name "404"
             :page :404
             :head shared/head
             :body (partial body-wrapper not-found/not-found)
             :title "Carrot | Page not found"
             :target #{:dev :prod}}
            {:page-name "500"
             :page :500
             :head shared/head
             :body (partial body-wrapper server-error/server-error)
             :title "Carrot | Internal server error"
             :target #{:dev :prod}}
            {:page-name "about"
             :page :about
             :head shared/head
             :body (partial body-wrapper about/about)
             :title "Carrot | About"
             :target #{:dev :prod}}
            {:page-name "app-shell"
             :page :app-shell
             :head (:head app-shell/app-shell)
             :body (:body app-shell/app-shell)
             :title "Carrot | Remote team communication"
             :target #{:dev}}
            {:page-name "app-shell"
             :page :prod-app-shell
             :head (:head app-shell/prod-app-shell)
             :body (:body app-shell/prod-app-shell)
             :title "Carrot | Remote team communication"
             :target #{:prod}}
            {:page-name "index"
             :page :index
             :head shared/head
             :body (partial body-wrapper index/index)
             :title "Carrot | Home"
             :target #{:dev :prod}}
            {:page-name "press-kit"
             :page :press-kit
             :head shared/head
             :body (partial body-wrapper press-kit/press-kit)
             :title "Carrot | Press kit"
             :target #{:dev :prod}}
            {:page-name "pricing"
             :page :pricing
             :head shared/head
             :body (partial body-wrapper pricing/pricing)
             :title "Carrot | Pricing"
             :target #{:dev :prod}}
            {:page-name "privacy"
             :page :privacy
             :head shared/head
             :body (partial body-wrapper privacy/privacy)
             :title "Carrot | Privacy Policy"
             :target #{:dev :prod}}
            {:page-name "slack"
             :page :slack
             :head shared/head
             :body (partial body-wrapper slack/slack)
             :title "Carrot | Slack"
             :target #{:dev :prod}}
            {:page-name "slack-lander"
             :page :slack-lander
             :head shared/head
             :body (partial body-wrapper slack/slack-lander)
             :title "Carrot | Slack lander"
             :target #{:dev :prod}}
            {:page-name "terms"
             :page :terms
             :head shared/head
             :body (partial body-wrapper terms/terms)
             :title "Carrot | Terms of Service"
             :target #{:dev :prod}}])

(defn build-pages [env-kw]
  (println (str "Building static pages (" env-kw "):"))
  (doseq [{:keys [title head body page-name page target] :as p} pages
          :let [filename (str "public/" page-name ".html")
                opts (assoc options :env-kw env-kw)]]
    (print (str "...page " (name page) (string/join "" (vec (take (- 15 (count (name page))) (repeat " ")))) " -> " filename "... "))
    (if-not (env-kw target)
      (println "skip!")
      (do
        (->> (hp/html5 {:lang "en"}
                      (if (fn? head) (head page opts) head)
                      (if (fn? body) (body page opts) body))
            (spit filename))
        (println "built!"))))
  (println "Done!"))

(defn -main [& [env-name & args]]
  (build-pages (or (string/keyword env-name) :dev)))