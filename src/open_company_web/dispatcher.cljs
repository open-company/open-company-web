(ns open-company-web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [no.en.core :refer [deep-merge]]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defonce app-state (atom {}))


(def companies (flux/dispatcher))

(def company (flux/dispatcher))

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
        ; add the new values to the atom
        (swap! app-state assoc (keyword (:slug body)) body)))))