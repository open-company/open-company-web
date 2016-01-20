(ns open-company-web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.caches :refer (revisions new-sections)]))

(defonce app-state (atom {}))


(def companies (flux/dispatcher))

(def company (flux/dispatcher))

(def section (flux/dispatcher))

(def revision (flux/dispatcher))

(def auth-settings (flux/dispatcher))

(def new-section (flux/dispatcher))

(def companies-list-dispatch
  (flux/register
    companies
    (fn [body]
      (when body
        (swap! app-state assoc :companies (:companies (:collection body)))
        ; remove loading key
        (swap! app-state dissoc :loading)))))

(def company-dispatch
  (flux/register
    company
    (fn [body]
      (when body
        ; add section name inside each section
        (let [updated-body (utils/fix-sections body)]
          (swap! app-state assoc (keyword (:slug updated-body)) updated-body)
          ; remove loading key
          (swap! app-state dissoc :loading))))))

(def section-dispatch
  (flux/register
    section
    (fn [body]
      (when body
        (let [fixed-section (utils/fix-section (:body body) (:section body))]
          (swap! app-state assoc-in [(:slug body) (:section body)] fixed-section)
          ; remove loading key
          (swap! app-state dissoc :loading))))))

(def revision-dispatch
  (flux/register
    revision
    (fn [body]
      (when body
        (let [fixed-section (utils/fix-section (:body body) (:section body) true)
              assoc-in-coll [(:slug body) (:section body) (:updated-at fixed-section)]]
          (swap! revisions assoc-in assoc-in-coll fixed-section)
          ; remove loading key
          (swap! app-state dissoc :loading))))))

(def auth-settings-dispatch
  (flux/register
    auth-settings
    (fn [body]
      (when body
        ; add auth-settings data
        (swap! app-state assoc :auth-settings body)
        ; remove loading key
        (swap! app-state dissoc :loading)))))

(def new-section-dispatch
  (flux/register
    new-section
    (fn [body]
      (when body
        (let [slug (:slug body)
              response (:response body)]
          (swap! new-sections assoc-in [(keyword slug) :categories] (:categories response)))))))