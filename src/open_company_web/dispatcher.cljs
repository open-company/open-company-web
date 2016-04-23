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

(defn su-list-key [company-slug]
  (keyword (str (name company-slug) "-su-list")))

(defn stakeholder-update-key [company-slug]
  (keyword (str (name company-slug) "-stakeholder-update")))

(defn current-company-data
  ([]
    (current-company-data @app-state))
  ([data]
    (get data (keyword (router/current-company-slug)))))

(defn current-stakeholder-update-list-data
  ([]
    (current-stakeholder-update-list-data @app-state))
  ([data]
    (let [su-list-k (su-list-key (router/current-company-slug))]
      (get data su-list-k))))

(defn current-stakeholder-update-data
  ([]
    (current-stakeholder-update-data @app-state))
  ([data]
    (let [slug (router/current-company-slug)
          update-slug (keyword (router/current-stakeholder-update-slug))
          su-key (stakeholder-update-key slug)]
      (get-in data [su-key update-slug]))))