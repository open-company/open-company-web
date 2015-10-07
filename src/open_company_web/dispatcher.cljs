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
          ; calc burn-rate and runway
          ; and add the new values to the atom
          (if (utils/in? (:sections updated-body) "finances")
            (let [finances (:data (:finances updated-body))
                  fixed-finances (into [] (map utils/calc-burnrate-runway finances))
                  fixed-body (assoc-in updated-body [:finances :data] fixed-finances)]
              (swap! app-state assoc (keyword (:slug updated-body)) fixed-body))
            (swap! app-state assoc (keyword (:slug updated-body)) updated-body)))))))

(def section-dispatch
  (flux/register
    section
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        (swap! app-state assoc-in [(:slug body) (:section body)] (:body body))))))

(def revision-dispatch
  (flux/register
    revision
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        (let [commentary? (contains? body :commentary)
              assoc-in-coll [(:slug body) (:section body)]
              assoc-in-coll (if commentary? (conj assoc-in-coll :commentary) assoc-in-coll)
              sec-body (:body body)
              sec-body (if (:read-only body) (assoc sec-body :read-only true) sec-body)
              section ((:section body) ((:slug body) @app-state))
              section (if commentary? (:commentary section) section)
              section-revision (merge section sec-body)
              section-revision (dissoc section-revision :loading)]
          (swap! app-state assoc-in assoc-in-coll section-revision))))))