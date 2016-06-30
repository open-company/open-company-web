(ns open-company-web.urls
  (:require [open-company-web.router :as router]
            [clojure.string :as clj-str]))

(defn params->query-string [m]
     (clojure.string/join "&" (for [[k v] m] (str (name k) "=" v))))

;; Main

(def home "/")

(def about "/about")

(def contact "/contact")

(def contact-mail-to "mailto:hello@opencompany.com")

(def login "/login")

(def logout "/logout")

(defn not-found [& [params]]
  (str "/404" (when params (str "?" (params->query-string params)))))

(def oc-twitter "https://twitter.com/opencompanyhq")

(def oc-github "https://github.com/open-company")

;; User

(def user-profile "/profile")

;; Companies

(def companies "/companies")

;; Company

(def create-company "/create-company")

(defn company
  "Company url"
  ([]
    (company (router/current-company-slug)))
  ([slug]
   (str "/" (name slug))))

(defn company-settings
  "Company profile url"
  ([]
    (company-settings (router/current-company-slug)))
  ([slug]
    (str "/" (name slug) "/settings")))

(defn company-category
  ([category]
    (company-category (router/current-company-slug) category))
  ([slug category]
    (str "/" (name slug) "#" (name category))))

(defn company-section
  "Section url"
  ([] (company-section (router/current-company-slug) (router/current-section)))
  ([company-slug section-name] (str "/" (name company-slug) "/" (name section-name))))

(defn company-section-edit
  "section url edit mode"
  ([] (company-section-edit (router/current-company-slug) (router/current-section)))
  ([company-slug section-name] (str "/" (name company-slug) "/" (name section-name) "/edit")))

(defn company-section-revision
  ([] (company-section (router/current-company-slug) (router/current-section)))
  ([revision] (str (company-section (router/current-company-slug) (router/current-section)) "?as-of=" revision)))

;; Stakeholder update

(defn stakeholder-update-list
  ([]
    (stakeholder-update-list (router/current-company-slug)))
  ([slug]
    (str "/" (name slug) "/updates")))

(defn stakeholder-update-preview
  ([]
    (stakeholder-update-preview (router/current-company-slug)))
  ([slug]
    (str "/" (name slug) "/updates/preview")))

(defn stakeholder-update-edit
  ([]
    (stakeholder-update-edit (router/current-company-slug)))
  ([slug]
    (str "/" (name slug) "/updates/edit")))

(defn stakeholder-update
  ([]
    (stakeholder-update (router/current-company-slug) (router/current-stakeholder-update-slug)))
  ([update-slug]
    (stakeholder-update (router/current-company-slug) update-slug))
  ([slug update-slug]
    (str "/" (name slug) "/updates/" (name update-slug))))

(defn stakeholder-update-section
  ([]
    (stakeholder-update-section (router/current-company-slug) (router/current-stakeholder-update-slug) (router/current-section)))
  ([update-slug section]
    (stakeholder-update-section (router/current-company-slug) update-slug section))
  ([slug update-slug section]
    (str "/" (name slug) "/updates/" (name update-slug) "/" section)))
