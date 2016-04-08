(ns open-company-web.actions
  (:require [medley.core :as med]
            [clojure.string :as string]
            [open-company-web.dispatcher :refer [action dispatch!]]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.api :as api]))

;; ---- Generic Actions Dispatch
;; This is a small generic abstraction to handle "actions".
;; An `action` is a transformation on the app state.
;; The return value of an action will be used as the new app-state.

;; The extended multimethod `action` is defined in the dispatcher
;; namespace to avoid cyclical dependencies between namespaces

(defmethod action :default [db payload]
  (js/console.warn "No handler defined for" (str (first payload)))
  (js/console.log "Full event: " (pr-str payload))
  db)

(defmethod action :input [db [_ path value]]
  (assoc-in db path value))

(defmethod action :entry [db [_ {:keys [links]}]]
  (let [create-link    (med/find-first #(= (:rel %) "company-create") links)
        slug           (fn [co] (last (string/split (:href co) #"/")))
        [first second] (filter #(= (:rel %) "company") links)]
    (cond
      (and first (not second)) (router/nav! (str "/" (slug first)))
      (and first second)       (router/nav! "/companies")
      create-link              (router/nav! "/create-company")))
  db)

(defmethod action :company-submit [db _]
  (api/post-company (:company-editor db))
  db)

(defmethod action :company-created [db [_ body]]
  (if (:links body)
    (let [updated (utils/fix-sections body)]
      (router/nav! (str "/" (:slug updated)))
      (assoc db (keyword (:slug updated)) updated))
    db))

(defmethod action :new-section [db [_ body]]
  (when body
    (let [slug (:slug body)
          response (:response body)]
      (swap! cache/new-sections assoc-in [(keyword slug) :categories] (:categories response))
      ;; signal to the app-state that the new-sections have been loaded
      (assoc-in db [(keyword slug) :new-sections] (rand 4)))))

(defmethod action :auth-settings [db [_ body]]
  (when body
    (-> db
        (assoc :auth-settings body)
        (dissoc :loading))))

(defmethod action :revision [db [_ body]]
  (when body
    (let [fixed-section (utils/fix-section (:body body) (:section body) true)
          assoc-in-coll [(:slug body) (:section body) (:updated-at fixed-section)]]
      (swap! cache/revisions assoc-in assoc-in-coll fixed-section)
      (dissoc db :loading))) ; remove loading key
  db)

(defmethod action :section [db [_ body]]
  (if body
    (let [fixed-section (utils/fix-section (:body body) (:section body))]
      (-> db
          (assoc-in [(:slug body) (:section body)] fixed-section)
          (dissoc :loading)))
    db))

(defmethod action :company [db [_ {:keys [slug success status body]}]]
  (cond
    success
    ;; add section name inside each section
    (let [updated-body (utils/fix-sections body)]
      (-> db
          (assoc (keyword (:slug updated-body)) updated-body)
          (dissoc :loading)))
    (= 404 status)
    (do (utils/redirect! (str "/404?path=/" slug)) db)
    ;; probably some default failure handling should be added here
    :else db))

(defmethod action :companies [db [_ body]]
  (if body
    (-> db
     (assoc :companies (:companies (:collection body)))
     (dissoc :loading))
    db))