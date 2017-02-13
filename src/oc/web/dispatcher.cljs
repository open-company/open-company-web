(ns oc.web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]))

(defonce app-state (atom {:loading false
                          :mobile-menu-open false
                          :show-login-overlay false
                          :dashboard-sharing false}))

(defn print-app-state []
  (js/console.log @app-state))

;; Data key paths

(def companies-key [:companies])

(defn org-data-key [org-slug]
  [(keyword org-slug) :org-data])

(defn board-data-key [org-slug board-slug]
  [(keyword org-slug) :boards (keyword board-slug) :board-data])

(defn board-cache-key [org-slug board-slug]
  [(keyword org-slug) (keyword board-slug) :cache])

(defn board-access-error-key [org-slug board-slug]
  [(keyword org-slug) (keyword board-slug) :error])

(defn board-new-topics-key [org-slug board-slug]
  [(keyword org-slug) (keyword board-slug) :new-topics])

(defn board-new-categories-key [org-slug board-slug]
  [(keyword org-slug) (keyword board-slug) :new-categories])

(defn board-topic-key [org-slug board-slug topic-slug]
  (conj (board-data-key org-slug board-slug) (keyword topic-slug)))

(defn su-list-key [company-slug]
  [(keyword company-slug) :su-list])

(defn latest-stakeholder-update-key [company-slug]
  [(keyword company-slug) :latest-su])

(defn stakeholder-update-key [company-slug update-slug]
  [(keyword company-slug) (keyword update-slug)])

(defn entries-key [org-slug board-slug]
  [(keyword org-slug) :boards (keyword board-slug) :entries-data])

(defn topic-entries-key [org-slug board-slug topic-slug]
  (vec (conj (entries-key org-slug board-slug) (keyword topic-slug))))

(defn entry-key [org-slug board-slug topic-slug as-of]
  (vec (conj (topic-entries-key org-slug board-slug topic-slug) (str as-of))))

;; Derived Data ================================================================

(defn drv-spec [db route-db]
  {:base                [[] db]
   :route               [[] route-db]
   :org-slug            [[:route] (fn [route] (:org route))]
   :board-slug          [[:route] (fn [route] (:board route))]
   :su-share            [[:base] (fn [base] (:su-share base))]
   :su-list             [[:base :company-slug]
                          (fn [base company-slug]
                            (when company-slug
                              (-> company-slug keyword base :su-list :collection :stakeholder-updates)))]
   :user-management     [[:base]
                          (fn [base]
                            {:um-invite (:um-invite base)
                             :enumerate-users (:enumerate-users base)
                             :um-domain-invite (:um-domain-invite base)
                             :invite-by-email-error (:invite-by-email-error base)
                             :add-email-domain-team-error (:add-email-domain-team-error base)})]
   :jwt                 [[:base] (fn [base] (:jwt base))]
   :current-user-data   [[:base] (fn [base] (:current-user-data base))]
   :subscription        [[:base] (fn [base] (:subscription base))]
   :show-login-overlay  [[:base] (fn [base] (:show-login-overlay base))]
   :rum-popover-data    [[:base] (fn [base] (:rum-popover-data base))]
   :org-data            [[:base :org-slug]
                          (fn [base org-slug]
                            (when org-slug
                              (get-in base (org-data-key org-slug))))]
   :board-new-topics    [[:base :org-slug :board-slug]
                          (fn [base org-slug board-slug]
                            (when (and org-slug board-slug)
                              (get-in base (board-new-topics-key org-slug board-slug))))]
   :board-new-categories [[:base :org-slug :board-slug]
                          (fn [base org-slug board-slug]
                            (when (and org-slug board-slug)
                              (get-in base (board-new-categories-key org-slug board-slug))))]
   :board-data          [[:base :org-slug :board-slug]
                          (fn [base org-slug board-slug]
                            (when (and org-slug board-slug)
                              (get-in base (board-data-key org-slug board-slug))))]})

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

;; Data

(defn org-data
  ([]
    (org-data @app-state))
  ([data]
    (org-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (org-data-key org-slug))))

(defn board-data
  ([]
    (board-data @app-state))
  ([data]
    (board-data data (router/current-org-slug) (router/current-board-slug)))
  ([data org-slug]
    (board-data data org-slug (router/current-board-slug)))
  ([data org-slug board-slug]
    (get-in data (board-data-key org-slug board-slug))))

(defn board-cache
  ([]
    (board-cache @app-state))
  ([data]
    (board-cache data (router/current-org-slug) (router/current-board-slug)))
  ([data org-slug]
    (board-cache data org-slug (router/current-board-slug)))
  ([data org-slug board-slug]
    (get-in data (board-cache-key org-slug board-slug)))
  ([data org-slug board-slug k]
    (get-in data (conj (board-cache-key org-slug board-slug) k))))

(defn latest-stakeholder-update
  ([]
    (latest-stakeholder-update @app-state))
  ([data]
    (latest-stakeholder-update data (router/current-board-slug)))
  ([data company-slug]
    (get-in data (latest-stakeholder-update-key company-slug))))

(defn stakeholder-update-list-data
  ([]
    (stakeholder-update-list-data @app-state))
  ([data]
    (stakeholder-update-list-data data (router/current-board-slug)))
  ([data company-slug]
    (get-in data (su-list-key company-slug))))

(defn stakeholder-update-data
  ([]
    (stakeholder-update-data @app-state))
  ([data]
    (stakeholder-update-data data (router/current-board-slug) (router/current-stakeholder-update-slug)))
  ([data company-slug update-slug]
    (get-in data (stakeholder-update-key company-slug update-slug))))

(defn force-edit-topic []
  (:force-edit-topic @app-state))

(defn entry
  ([org-slug board-slug topic-slug as-of] (entry org-slug board-slug topic-slug as-of @app-state))
  ([org-slug board-slug topic-slug as-of data] (get-in data (entry-key org-slug board-slug topic-slug as-of))))

(defn entries
  ([] (entries (router/current-org-slug) (router/current-board-slug)))
  ([org-slug board-slug] (entries org-slug board-slug @app-state))
  ([org-slug board-slug data] (get-in data (entries-key org-slug board-slug))))

(defn topic-entries-data
  ([topic-slug] (topic-entries-data (router/current-org-slug) (router/current-board-slug) topic-slug))
  ([org-slug board-slug topic-slug] (topic-entries-data org-slug board-slug topic-slug @app-state))
  ([org-slug board-slug topic-slug data] (get-in data (topic-entries-key org-slug board-slug topic-slug))))

(defn foce-topic-key []
  (:foce-key @app-state))

(defn foce-topic-data []
  (:foce-data @app-state))

(defn foce-topic-data-editing? []
  (:foce-data-editing? @app-state))

(set! (.-OCDispatcherPrintAppState js/window) print-app-state)