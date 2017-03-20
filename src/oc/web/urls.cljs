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

(def email-confirmation "/verify")

(def confirm-invitation "/invite")

(def password-reset "/reset")

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

(defn org-settings
  "Org settings url"
  ([]
    (org-settings (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/settings")))

(defn org-logo-setup
  ([]
    (org-logo-setup (router/current-org-slug)))
  ([org-slug]
    (str (org-settings org-slug) "/logo")))

(defn org-team-settings
  "Team settings url"
  ([]
    (org-team-settings (router/current-org-slug)))
  ([org-slug]
    (str (org-settings org-slug) "/team-management")))

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

(defn updates-list
  ([]
    (updates-list (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/updates"))
  ([org-slug update-slug]
    (str (updates-list org-slug) "/" (name update-slug))))

(defn update-preview
  ([]
    (update-preview (router/current-org-slug)))
  ([org-slug]
    (str (updates-list org-slug) "/preview")))

(defn update-link
  ([]
    (update (router/current-org-slug) (router/current-update-date) (router/current-update-slug)))
  ([update-date update-slug]
    (update (router/current-org-slug) update-date update-slug))
  ([org-slug update-date update-slug]
    (str (updates-list org-slug) "/" (name update-date) "/" (name update-slug))))