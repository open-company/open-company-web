(ns open-company-web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [no.en.core :refer [deep-merge]]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defonce app-state (atom {}))


(def companies (flux/dispatcher))

(def company (flux/dispatcher))

(def section (flux/dispatcher))

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
        (let [updated-body (utils/add-section-names body)]
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