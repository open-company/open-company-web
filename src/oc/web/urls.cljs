(ns oc.web.urls
  (:require [oc.web.router :as router]
            [oc.web.lib.jwt :as j]
            [clojure.string :as clj-str]))

(defn params->query-string [m]
     (clojure.string/join "&" (for [[k v] m] (str (name k) "=" v))))

;; Main

(def home "/")

(def about "/about")

(def contact "/contact")

(def contact-email "hello@opencompany.com")
(def contact-mail-to (str "mailto:" contact-email))

(def login "/login")
(def sign-up "/sign-up")

(def logout "/logout")

(def pricing "/pricing")

(defn not-found [& [params]]
  (str "/404" (when params (str "?" (params->query-string params)))))

(def oc-twitter "https://twitter.com/opencompanyhq")

(def oc-github "https://github.com/open-company")

(def subscription-callback "/subscription-completed")

(def email-confirmation "/email-confirmation")

(def confirm-invitation "/invite")

;; User

(def user-profile "/profile")

;; Organizations

(def orgs "/orgs")

(def create-org "/create-org")

(defn org
  "Org url"
  ([]
    (org (router/current-org-slug)))
  ([org-slug]
    (str "/" (name org-slug))))

;; Boards

(defn boards
  ([]
    (boards (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/boards")))

(defn create-board
  ([]
    (create-board (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/create-board")))

(defn board
  "Board url"
  ([]
    (board (router/current-org-slug) (router/current-board-slug)))
  ([org-slug]
    (board org-slug (router/current-board-slug)))
  ([org-slug board-slug]
   (str (org org-slug) "/" (name board-slug))))

(defn board-settings
  "Board settings url"
  ([]
    (board-settings (router/current-org-slug) (router/current-board-slug)))
  ([org-slug board-slug]
    (str (board org-slug board-slug) "/settings")))

(defn board-logo-setup
  ([]
    (board-logo-setup (router/current-org-slug) (router/current-board-slug)))
  ([org-slug board-slug]
    (str (board-settings org-slug board-slug) "/logo")))

(defn team-settings-um
  "Team settings url"
  ([]
    (team-settings-um (j/team-id)))
  ([team-id]
    (str "/" team-id "/settings/user-management")))

;; Topics

(defn topic
  "Topic url"
  ([] (topic (router/current-org-slug) (router/current-board-slug) (router/current-topic-slug)))
  ([topic-slug] (topic (router/current-org-slug) (router/current-board-slug) topic-slug))
  ([board-slug topic-slug] (topic (router/current-org-slug) board-slug topic-slug))
  ([org-slug board-slug topic-slug] (str (board org-slug board-slug) "/" (name topic-slug))))

(defn topic-entry
  ([] (topic (router/current-org-slug) (router/current-board-slug) (router/current-topic-slug)))
  ([entry] (str (topic (router/current-org-slug) (router/current-board-slug) (router/current-topic-slug)) "?as-of=" entry))
  ([org-slug board-slug topic-slug entry] (str (topic org-slug board-slug topic-slug) "?as-of=" entry)))

;; Stakeholder update

(defn stakeholder-update-list
  ([]
    (stakeholder-update-list (router/current-board-slug)))
  ([slug]
    (str "/" (name slug) "/updates"))
  ([slug update-slug]
    (str (stakeholder-update-list slug) "/" (name update-slug))))

(defn stakeholder-update-preview
  ([]
    (stakeholder-update-preview (router/current-board-slug)))
  ([slug]
    (str "/" (name slug) "/updates/preview")))

(defn stakeholder-update
  ([]
    (stakeholder-update (router/current-board-slug) (router/current-stakeholder-update-date) (router/current-stakeholder-update-slug)))
  ([update-date update-slug]
    (stakeholder-update (router/current-board-slug) update-date update-slug))
  ([slug update-date update-slug]
    (str "/" (name slug) "/updates/" (name update-date) "/" (name update-slug))))