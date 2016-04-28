(ns open-company-web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [open-company-web.router :as router]))

(defonce app-state (atom {:loading false}))

(defmulti action (fn [db [action-type & _]] action-type))

(def actions (flux/dispatcher))

(def actions-dispatch
  (flux/register
   actions
   (fn [payload]
     ;; (prn payload) ; debug :)
     (swap! app-state action payload))))

(defn dispatch! [payload]
  (flux/dispatch actions payload))

(def companies-key [:companies])

(defn company-data-key [company-slug]
  [(keyword company-slug) :company-data])

(defn company-section-key [company-slug section]
  [(keyword company-slug) :company-data (keyword section)])

(defn su-list-key [company-slug]
  [(keyword company-slug) :su-list])

(defn stakeholder-update-key [company-slug update-slug]
  [(keyword company-slug) (keyword update-slug)])

(defn company-data
  ([]
    (company-data @app-state))
  ([data]
    (company-data data (router/current-company-slug)))
  ([data company-slug]
    (get-in data (company-data-key company-slug))))

(defn stakeholder-update-list-data
  ([]
    (stakeholder-update-list-data @app-state))
  ([data]
    (stakeholder-update-list-data data (router/current-company-slug)))
  ([data company-slug]
    (get-in data (su-list-key company-slug))))

(defn stakeholder-update-data
  ([]
    (stakeholder-update-data @app-state))
  ([data]
    (stakeholder-update-data data (router/current-company-slug) (router/current-stakeholder-update-slug)))
  ([data company-slug update-slug]
    (get-in data (stakeholder-update-key company-slug update-slug))))