(ns open-company-web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.router :as router]))

(defonce app-state (atom {:loading false :menu-open false}))

;; Derived Data ================================================================

(def drv-spec
  {:base         [[] app-state]
   :su-share     [[:base] (fn [base] (:su-share base))]
   :jwt          [[:base] (fn [base] (:jwt base))]
   :subscription [[:base :jwt] (fn [base jwt] (prn 'passed-jwt jwt) (get-in base [:subscriptions (:org-id jwt)]))]})

(def drv
  ;; A variant of `org.martinklepsch.derivatives/drv` that works by
  ;; encapsulating global state instead of passing it down the component
  ;; tree using React's childContext. We're using this instad of the
  ;; bundled `drv` because our Root components are Om and setting
  ;; childContext on them is something I didn't want to bother with
  (let [{:keys [get! release!]} (drv/derivatives-manager drv-spec)]
    (fn drv [drv-k]
      (let [token (random-uuid)]
        {:will-mount   (fn [s]
                         (assoc-in s [::drv/derivatives drv-k] (get! drv-k token)))
         :will-unmount (fn [s]
                         (release! drv-k token)
                         (update s ::drv/derivatives dissoc drv-k))}))))

;; Action Loop =================================================================

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

(defn latest-stakeholder-update-key [company-slug]
  [(keyword company-slug) :latest-su])

(defn stakeholder-update-key [company-slug update-slug]
  [(keyword company-slug) (keyword update-slug)])

(defn revisions-key [slug]
  [:revisions (keyword slug)])

(defn section-revisions-key [slug section]
  (vec (conj (revisions-key slug) (keyword section))))

(defn revision-key [slug section as-of]
  (vec (conj (section-revisions-key slug section) (str as-of))))

(defn company-data
  ([]
    (company-data @app-state))
  ([data]
    (company-data data (router/current-company-slug)))
  ([data company-slug]
    (get-in data (company-data-key company-slug))))

(defn latest-stakeholder-update
  ([]
    (latest-stakeholder-update @app-state))
  ([data]
    (latest-stakeholder-update data (router/current-company-slug)))
  ([data company-slug]
    (get-in data [(keyword company-slug) :latest-su])))

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

(defn force-edit-topic []
  (:force-edit-topic @app-state))

(defn toggle-menu
  ([] (swap! app-state update :menu-open not))
  ([force?] (swap! app-state assoc :menu-open force?)))

(defn save-last-company-slug []
  (swap! app-state assoc :last-slug (router/current-company-slug)))

(defn last-company-slug []
  (:last-slug @app-state))

(defn revision
  ([slug section as-of] (revision slug section as-of @app-state))
  ([slug section as-of data] (get-in data (revision-key slug section as-of))))

(defn section-revisions
  ([slug section] (section-revisions slug section @app-state))
  ([slug section data] (get-in data (section-revisions-key slug section))))

(defn revisions
  ([slug] (revisions slug @app-state))
  ([slug data] (get-in data (revisions-key slug))))

(defn foce-section-key []
  (:foce-key @app-state))

(defn foce-section-data []
  (:foce-data @app-state))