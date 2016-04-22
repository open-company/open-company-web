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

(defn su-list-key [slug]
  (keyword (str (name slug) "-su-list")))

(defn stakeholder-update-key [slug]
  (keyword (str (name slug) "-stakeholder-update")))

(defn current-company-data [data]
  (get data (keyword (router/current-company-slug))))