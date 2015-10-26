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
          ; calc burn-rate and runway
          ; and add the new values to the atom
          (if (utils/in? (:sections updated-body) "finances")
            (let [finances (:finances updated-body)
                  finances-data (:data finances)
                  fixed-finances (utils/calc-runway finances-data)
                  sort-pred (utils/sort-by-key-pred :period true)
                  sorted-finances (sort #(sort-pred %1 %2) fixed-finances)
                  fixed-body (assoc-in updated-body [:finances :data] sorted-finances)
                  body-with-notes (if-not (contains? (:finances fixed-body) :notes)
                                    (assoc fixed-body :finances (merge (:finances fixed-body) {:notes {:body ""}}))
                                    fixed-body)]
              (swap! app-state assoc (keyword (:slug updated-body)) body-with-notes))
            (swap! app-state assoc (keyword (:slug updated-body)) updated-body)))))))

(defn add-section-name [section-body section-name]
  (assoc section-body :section (keyword section-name)))

(defn add-section-sorter [section-body]
  (assoc section-body :sorter (:updated-at section-body)))

(defn add-section-as-of [section-body]
  (assoc section-body :as-of (:updated-at section-body)))

(defn fix-finances-data [section-body section-name]
  (if (= (keyword section-name) :finances)
    (let [fixed-finances (utils/calc-runway (:data section-body))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-finances (sort #(sort-pred %1 %2) fixed-finances)]
      (assoc section-body :data sorted-finances))
    section-body))

(defn fix-section [section-body section-name]
  (let [fixed-section (add-section-as-of (add-section-sorter (add-section-name section-body section-name)))
        fixed-section (fix-finances-data fixed-section section-name)]
    fixed-section))

(def section-dispatch
  (flux/register
    section
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        (let [fixed-section (fix-section (:body body) (:section body))]
          (swap! app-state assoc-in [(:slug body) (:section body)] fixed-section))))))

(def revision-dispatch
  (flux/register
    revision
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        (let [notes? (contains? body :notes)
              assoc-in-coll [(:slug body) (:section body)]
              assoc-in-coll (if notes? (conj assoc-in-coll :notes) assoc-in-coll)
              sec-body (:body body)
              is-finances (= (:section body) :finances)
              sec-body (if (and is-finances (not notes?))
                         (assoc sec-body :data (utils/calc-runway (:data sec-body)))
                         sec-body)
              sec-body (if (:read-only body) (assoc sec-body :read-only true) sec-body)
              section ((:section body) ((:slug body) @app-state))
              section (if notes? (:notes section) section)
              section-revision (merge section sec-body)
              section-revision (assoc section-revision :as-of (:updated-at section-revision))
              section-revision (dissoc section-revision :loading)]
          (swap! app-state assoc-in assoc-in-coll section-revision))))))

(def auth-settings-dispatch
  (flux/register
    auth-settings
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        ; add auth-settings data
        (swap! app-state assoc :auth-settings body)))))