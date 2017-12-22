(ns oc.web.urls
  (:require [oc.web.router :as router]
            [oc.web.lib.jwt :as j]
            [clojure.string :as clj-str]))

(defn params->query-string [m]
     (clojure.string/join "&" (for [[k v] m] (str (name k) "=" v))))

;; Main

(def home "/")

(def about "/about")

(def slack "/slack")

(def blog "https://blog.carrot.io")

(def contact "/contact")

(def help "http://help.carrot.io")

(def home-try-it-focus (str home "?tif"))

(def contact-email "hello@carrot.io")
(def contact-mail-to (str "mailto:" contact-email))

(def login "/login")
(def sign-up "/sign-up")
(def sign-up-profile "/sign-up/profile")
(def sign-up-team "/sign-up/team")

(def slack-lander-check "/slack-lander/check")

(def logout "/logout")

(def pricing "/pricing")

(def terms "/terms")

(def privacy "/privacy")

(defn not-found [& [params]]
  (str "/404" (when params (str "?" (params->query-string params)))))

(def oc-twitter "https://twitter.com/CarrotBuzz")

(def oc-facebook "https://www.facebook.com/Carrot-111981319388047/")

(def oc-github "https://github.com/open-company")

(def subscription-callback "/subscription-completed")

(def email-confirmation "/verify")

(def confirm-invitation "/invite")
(def confirm-invitation-profile "/invite/profile")

(def password-reset "/reset")

(def email-wall "/email-required")

;; User

(def user-profile "/profile")

;; Organizations

(def create-org "/create-org")

(defn org
  "Org url"
  ([]
    (org (router/current-org-slug)))
  ([org-slug]
    (str "/" (name org-slug))))

(defn all-posts
  "Org all posts url"
  ([]
    (all-posts (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/all-posts")))

(defn org-settings
  "Org settings url"
  ([]
    (org-settings (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/settings")))

(defn org-settings-team
  "Team settings url"
  ([]
    (org-settings-team (router/current-org-slug)))
  ([org-slug]
    (str (org-settings org-slug) "/team")))

(defn org-settings-invite
  "Invite people to team url"
  ([]
    (org-settings-invite (router/current-org-slug)))
  ([org-slug]
    (str (org-settings org-slug) "/invite")))

;; Boards

(defn boards
  ([]
    (boards (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/boards")))

(defn board
  "Board url"
  ([]
    (board (router/current-org-slug) (router/current-board-slug)))
  ([board-slug]
    (board (router/current-org-slug) board-slug))
  ([org-slug board-slug]
   (str (org org-slug) "/" (name board-slug))))

(defn board-sort-by-topic
  "Board sorted by latest topic"
  ([]
    (board-sort-by-topic (router/current-org-slug) (router/current-board-slug)))
  ([board-slug]
   (board-sort-by-topic (router/current-org-slug) board-slug))
  ([org-slug board-slug]
   (str (board org-slug board-slug) "/by-topic")))

(defn board-filter-by-topic
  "Board filtered by topic"
  ([topic-slug]
    (board-filter-by-topic (router/current-org-slug) (router/current-board-slug) topic-slug))
  ([org-slug board-slug topic-slug]
    (str (board org-slug board-slug) "/topic/" (name topic-slug))))

;; Drafts

(defn drafts
  ([]
    (drafts (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/drafts")))

;; Entries

(defn entry
  "Entry url"
  ([] (entry (router/current-org-slug) (router/current-board-slug) (router/current-activity-id)))
  ([entry-uuid] ( (router/current-org-slug) (router/current-board-slug) entry-uuid))
  ([board-slug entry-uuid] (entry (router/current-org-slug) board-slug entry-uuid))
  ([org-slug board-slug entry-uuid] (str (board org-slug board-slug) "/post/" (name entry-uuid))))

;; Secure activities

(defn secure-activity
  "Secure url for activity to show readonly view."
  ([] (secure-activity (router/current-org-slug) (router/current-activity-id)))
  ([secure-id] (secure-activity (router/current-org-slug) secure-id))
  ([org-slug secure-id] (str (org org-slug) "/post/" secure-id)))