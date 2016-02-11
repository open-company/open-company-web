(ns open-company-web.actions
  (:require [medley.core :as med]
            [open-company-web.dispatcher :refer [action dispatch!]]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.api :as api]))

(defn- delay-remove-loading []
  (.setTimeout js/window #(dispatch! [:clear-loading]) 100))

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
  (if-let [co (med/find-first #(= (:rel %) "company") links)]
    (router/nav! (str (:href co) "/dashboard"))
    (if (med/find-first #(= (:rel %) "create-company") links)
      (router/nav! "/create-company")))
  db)

(defmethod action :company-submit [db _]
  (api/post-company (:company-editor db))
  db)

(defmethod action :company-created [db [_ body]]
  (let [updated (utils/fix-sections body)
        link (:href (med/find-first #(= "self" (:rel %)) (:links body)))]
    (router/nav! (str link "/dashboard"))
    (assoc db (keyword (:slug updated)) updated)))

(defmethod action :new-section [db [_ body]]
  (when body
    (let [slug (:slug body)
          response (:response body)]
      (swap! cache/new-sections assoc-in [(keyword slug) :categories] (:categories response))
      db)))

(defmethod action :auth-settings [db [_ body]]
  (when body
    (delay-remove-loading) ; remove loading key
    ;; add auth-settings data
    (assoc db :auth-settings body)))

(defmethod action :revision [db [_ body]]
  (when body
    (let [fixed-section (utils/fix-section (:body body) (:section body) true)
          assoc-in-coll [(:slug body) (:section body) (:updated-at fixed-section)]]
      (swap! cache/revisions assoc-in assoc-in-coll fixed-section)
      ;; remove loading key
      (delay-remove-loading)))
  db)

(defmethod action :section [db [_ body]]
  (if body
    (let [fixed-section (utils/fix-section (:body body) (:section body))]
      (delay-remove-loading) ; remove loading key
      (assoc-in db [(:slug body) (:section body)] fixed-section))
    db))

(defmethod action :company [db [_ body]]
  (if body
    ;; add section name inside each section
    (let [updated-body (utils/fix-sections body)]
      (delay-remove-loading) ; remove loading key
      (assoc db (keyword (:slug updated-body)) updated-body))
    db))

(defmethod action :companies [db [_ body]]
  (if body
    (do
      (delay-remove-loading) ; remove loading key)
      (assoc db :companies (:companies (:collection body))))
    db))

(defmethod action :clear-loading [db _]
  (dissoc db :loading))
