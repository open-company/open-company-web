(ns oc.site
  (:require [hiccup.page :as hp]
            [environ.core :refer (env)]

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

(def options {:contact-email contact-email
              :contact-mail-to contact-mail-to
              :oc-github oc-github})

(defn- body-wrapper [body page opts]
  [:body
   {:class (when (env :covid-banner) "covid-banner")}
   shared/tag-manager-body
   [:div
    {:class "outer header"}
    shared/ph-banner
    (shared/covid-banner page)
    (shared/nav (name page))
    (shared/mobile-menu (name page))]
   (if (fn? body) (body opts) body)
   (shared/footer page)])

(def pages [{:name "404"
             :page :404
             :head shared/head
             :body (partial body-wrapper not-found/not-found)
             :title "Carrot | Page not found"}
            {:name "500"
             :page :500
             :head shared/head
             :body (partial body-wrapper server-error/server-error)
             :title "Carrot | Internal server error"}
            {:name "about"
             :page :about
             :head shared/head
             :body (partial body-wrapper about/about)
             :title "Carrot | About"}
            {:name "app-shell"
             :page :app-shell
             :head (:head app-shell/app-shell)
             :body (:body app-shell/app-shell)
             :title "Carrot"}
            {:name "app-shell"
             :page :app-shell
             :head (:head app-shell/prod-app-shell)
             :body (:body app-shell/prod-app-shell)
             :title "Carrot"}
            {:name "index"
             :page :index
             :head shared/head
             :body (partial body-wrapper index/index)
             :title "Carrot | Home"}
            {:name "press-kit"
             :page :press-kit
             :head shared/head
             :body (partial body-wrapper press-kit/press-kit)
             :title "Carrot | Press kit"}
            {:name "pricing"
             :page :pricing
             :head shared/head
             :body (partial body-wrapper pricing/pricing)
             :title "Carrot | Pricing"}
            {:name "privacy"
             :page :privacy
             :head shared/head
             :body (partial body-wrapper privacy/privacy)
             :title "Carrot | Privacy Policy"}
            {:name "slack"
             :page :slack
             :head shared/head
             :body (partial body-wrapper slack/slack)
             :title "Carrot | Slack"}
            {:name "slack-lander"
             :page :slack-lander
             :head shared/head
             :body (partial body-wrapper slack/slack-lander)
             :title "Carrot | Slack lander"}
            {:name "terms"
             :page :terms
             :head shared/head
             :body (partial body-wrapper terms/terms)
             :title "Carrot | Terms of Service"}])

;; (defn static-page [title content opts]
;;   (hp/html5 {:lang "en"}
;;             (shared/head)
;;             [:body
;;              {:class (when (env :covid-banner) "covid-banner")}
;;              shared/tag-manager-body
;;              [:div
;;               {:class "outer header"}
;;               shared/ph-banner
;;               (shared/covid-banner page)
;;               (shared/nav (name page))
;;               (shared/mobile-menu (name page))]
;;              content
;;              (shared/footer page)]))

;; (defn app-shell [_]
;;   (hp/html5 {:lang "en"}
;;             (:head pages/app-shell)
;;             (:body pages/app-shell)))

;; (defn prod-app-shell [_]
;;   (hp/html5 {:lang "en"}
;;             (:head pages/prod-app-shell)
;;             (:body pages/prod-app-shell)))


(defn build-pages []
  (for [{:keys [title head body name page] :as p} pages]
    (->> (hp/html5 {:lang "en"}
                   (if (fn? head) (head title) head)
                   (if (fn? body) (body page options) body))
         (spit (str "public/" name ".html")))))

;; (defn -main [& args]
;;   (println "DBG in -main" args)
;;   (let [idx (index)
;;         _ (println "DBG   idx:" idx)
;;         hic (h/html idx)
;;         _ (println "DBG   hic" hic)
;;         page (str doc-type hic)
;;         _ (println "DBG   page:" page)]
;;     page))