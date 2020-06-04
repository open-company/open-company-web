(ns oc.web.urls
  (:require [oc.web.router :as router]
            [clojure.string :as clj-str]))

(defn params->query-string [m]
     (clojure.string/join "&" (for [[k v] m] (str (name k) "=" v))))

;; Main

(def home "/")

(def home-no-redirect (str home "?no_redirect=1"))

(def about "/about")

(def slack "/slack")

(def domain "wuts.io")

(def blog (str "https://blog." domain))

(def contact "/contact")

(def press-kit "/press-kit")

(def help (str "http://help." domain "/"))

(def what-s-new "https://wuts.news/")

(def home-try-it-focus (str home "?tif"))

(def contact-email (str "hello@" domain))
(def contact-mail-to (str "mailto:" contact-email))

(def login "/login")
(def native-login "/login/desktop")
(def sign-up "/sign-up")
(def sign-up-slack "/sign-up/slack")
(def sign-up-profile "/sign-up/profile")
(def sign-up-team "/sign-up/team")

(defn sign-up-update-team
  ([]
    (sign-up-update-team (router/current-org-slug)))
  ([org-slug]
    (str sign-up "/" (name org-slug) "/team")))

(defn sign-up-invite
  ([]
    (sign-up-invite (router/current-org-slug)))
  ([org-slug]
    (str sign-up "/" (name org-slug) "/invite")))

(def slack-lander-check "/slack-lander/check")

(def google-lander-check "/google/lander")

(def logout "/logout")

(def pricing "/pricing")

(def terms "/terms")

(def privacy "/privacy")

(defn not-found [& [params]]
  (str "/404" (when params (str "?" (params->query-string params)))))

(def email-confirmation "/verify")

(def confirm-invitation "/invite")
(def confirm-invitation-password "/invite/password")
(def confirm-invitation-profile "/invite/profile")

(def password-reset "/reset")

(def email-wall "/email-required")

(def login-wall "/login-wall")

(def apps-detect "/apps/detect")

;; Organizations

(defn org
  "Org url"
  ([]
    (org (router/current-org-slug)))
  ([org-slug]
    (str "/" (name org-slug))))

(defn inbox
  "Org inbox url"
  ([]
    (inbox (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/inbox")))

(defn all-posts
  "Org all posts url"
  ([]
    (all-posts (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/all-posts")))

(defn following
  "Org following url"
  ([]
    (following (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/home")))

(defn unfollowing
  "Org unfollowing url"
  ([]
    (unfollowing (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/unfollowing")))

(defn follow-ups
  "Org follow-ups url"
  ([]
    (follow-ups (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/follow-ups")))

(defn bookmarks
  "Org bookmarks url"
  ([]
    (bookmarks (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/bookmarks")))

(defn must-see
  "Org must see url"
  ([]
    (must-see (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/must-see")))

(defn threads
  "Org threads url"
  ([]
    (threads (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/threads")))

(defn explore
  "Org explore url"
  ([]
    (explore (router/current-org-slug)))
  ([org-slug]
    (str (org org-slug) "/explore")))

;; Boards

(defn board
  "Board url"
  ([]
    (board (router/current-org-slug) (router/current-board-slug)))
  ([board-slug]
    (board (router/current-org-slug) board-slug))
  ([org-slug board-slug]
   (str (org org-slug) "/" (name board-slug))))

;; Default url

(def default-url-fn following)
(def default-board-slug "following")

(defn default-landing
  ([] (default-landing (router/current-org-slug)))
  ([org-slug]
   (if (fn? default-url-fn)
    (default-url-fn org-slug)
    (all-posts org-slug))))

;; First ever landing

(defn first-ever-landing
  "Org all posts url for the first ever land"
  ([]
    (first-ever-landing (router/current-org-slug)))
  ([org-slug]
    (str (default-landing org-slug) "/hello")))

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
  ([entry-uuid] (entry (router/current-org-slug) (router/current-board-slug) entry-uuid))
  ([board-slug entry-uuid] (entry (router/current-org-slug) board-slug entry-uuid))
  ([org-slug board-slug entry-uuid] (str (board org-slug board-slug) "/post/" (name entry-uuid))))

;; Commennts

(defn comment-url
  "Comment url"
  ([comment-uuid] (comment-url (router/current-org-slug) (router/current-board-slug) (router/current-activity-id)))
  ([entry-uuid comment-uuid] (comment-url (router/current-org-slug) (router/current-board-slug) entry-uuid comment-uuid))
  ([board-slug entry-uuid comment-uuid] (comment-url (router/current-org-slug) board-slug entry-uuid comment-uuid))
  ([org-slug board-slug entry-uuid comment-uuid] (str (entry org-slug board-slug entry-uuid) "/comment/" comment-uuid)))

;; Secure activities

(defn secure-activity
  "Secure url for activity to show read only view."
  ([] (secure-activity (router/current-org-slug) (router/current-secure-activity-id)))
  ([secure-id] (secure-activity (router/current-org-slug) secure-id))
  ([org-slug secure-id] (str (org org-slug) "/post/" secure-id)))

;; contributions

(defn contributions
  "contributions url"
  ([] (contributions (router/current-org-slug) (router/current-contributions-id)))
  ([author-uuid] (contributions (router/current-org-slug) author-uuid))
  ([org-slug author-uuid] (str (org org-slug) "/u/" author-uuid)))