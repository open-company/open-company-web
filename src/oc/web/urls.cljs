(ns oc.web.urls
  (:require [oc.web.router :as router]
            [oc.web.lib.jwt :as j]
            [clojure.string :as clj-str]))

(defn params->query-string [m]
     (clojure.string/join "&" (for [[k v] m] (str (name k) "=" v))))

;; Main

(def home "/")

(def about "/about")

(def features "/features")

(def blog "https://blog.carrot.io")

(def contact "/contact")

(def home-try-it-focus (str home "?tif"))

(def contact-email "hello@carrot.io")
(def contact-mail-to (str "mailto:" contact-email))

(def login "/login")
(def sign-up "/sign-up")

(def logout "/logout")

(def pricing "/pricing")

(defn not-found [& [params]]
  (str "/404" (when params (str "?" (params->query-string params)))))

(def oc-twitter "https://twitter.com/CarrotBuzz")

(def oc-facebook "https://www.facebook.com/Carrot-111981319388047/")

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

(defn all-activity
  "Org all activity url"
  ([]
    (all-activity (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/all-activity")))

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
  ([org-slug board-slug]
   (str (board org-slug board-slug) "/by-topic")))

(defn board-filter-by-topic
  "Board filtered by topic"
  ([topic-slug]
    (board-filter-by-topic (router/current-org-slug) (router/current-board-slug) topic-slug))
  ([org-slug board-slug topic-slug]
    (str (board org-slug board-slug) "/topic/" (name topic-slug))))

(defn board-settings
  "Board settings url"
  ([]
    (board-settings (router/current-org-slug) (router/current-board-slug)))
  ([org-slug board-slug]
    (str (board org-slug board-slug) "/settings")))

;; Storyboards

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
  ([org-slug board-slug entry-uuid] (str (board org-slug board-slug) "/update/" (name entry-uuid))))

;; Stories

(defn story
  "Story url"
  ([] (story (router/current-org-slug) (router/current-board-slug) (router/current-activity-id)))
  ([story-uuid] (story (router/current-org-slug) (router/current-board-slug) story-uuid))
  ([board-slug story-uuid] (story (router/current-org-slug) board-slug story-uuid))
  ([org-slug board-slug story-uuid] (str (board org-slug board-slug) "/story/" (name story-uuid))))

(defn story-edit
  "Edit an already created story."
  ([] (story-edit (router/current-org-slug) (router/current-board-slug) (router/current-activity-id)))
  ([story-uuid] (story-edit (router/current-org-slug) (router/current-board-slug) story-uuid))
  ([board-slug story-uuid] (story-edit (router/current-org-slug) (router/current-board-slug) story-uuid))
  ([org-slug board-slug story-uuid] (str (story org-slug board-slug story-uuid) "/edit")))

(defn secure-story
  "Secure url for story to show readonly view."
  ([] (secure-story (router/current-org-slug) (router/current-activity-id)))
  ([secure-id] (secure-story (router/current-org-slug) secure-id))
  ([org-slug secure-id] (str (org org-slug) "/story/" secure-id)))