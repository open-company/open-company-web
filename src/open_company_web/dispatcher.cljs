(ns open-company-web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [no.en.core :refer [deep-merge]]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defonce app-state (atom {}))


(def companies (flux/dispatcher))

(def company (flux/dispatcher))

(def section (flux/dispatcher))

(def revision (flux/dispatcher))

(def auth-settings (flux/dispatcher))

(def companies-list-dispatch
  (flux/register
    companies
    (fn [body]
      (when body
        (swap! app-state assoc :companies (:companies (:collection body)))))))

(def company-dispatch
  (flux/register
    company
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        ; add section name inside each section
        (let [updated-body (utils/fix-sections body)]
          (swap! app-state assoc (keyword (:slug updated-body)) updated-body))))))

(def section-dispatch
  (flux/register
    section
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        (let [fixed-section (utils/fix-section (:body body) (:section body))]
          (swap! app-state assoc-in [(:slug body) (:section body)] fixed-section))))))

(def revision-dispatch
  (flux/register
    revision
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        (let [assoc-in-coll [(:slug body) (:section body)]
              read-only (:read-only body)
              fixed-section (utils/fix-section (:body body) (:section body) read-only)]
          (swap! app-state assoc-in assoc-in-coll fixed-section))))))

(def auth-settings-dispatch
  (flux/register
    auth-settings
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        ; add auth-settings data
        (swap! app-state assoc :auth-settings body)))))