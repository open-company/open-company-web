(ns oc.web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [org.martinklepsch.derivatives :as drv]
            [taoensso.timbre :as timbre]
            [oc.web.router :as router]))

(defonce app-state (atom {:loading false
                          :mobile-menu-open false
                          :show-login-overlay false
                          :show-add-topic false
                          :dashboard-sharing false}))

;; Data key paths

(def api-entry-point-key [:api-entry-point])

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

(defn updates-list-key [org-slug]
  [(keyword org-slug) :updates-list])

(defn latest-update-key [org-slug]
  [(keyword org-slug) :latest-su])

(defn update-key [org-slug update-slug]
  [(keyword org-slug) :updates (keyword update-slug)])

(defn entries-key [org-slug board-slug]
  [(keyword org-slug) :boards (keyword board-slug) :entries-data])

(defn topic-entries-key [org-slug board-slug topic-slug]
  (vec (conj (entries-key org-slug board-slug) (keyword topic-slug))))

(defn entry-key [org-slug board-slug topic-slug as-of]
  (vec (conj (topic-entries-key org-slug board-slug topic-slug) (str as-of))))

(def teams-data-key [:enumerate-users :teams])

(defn team-data-key [team-id]
  [:enumerate-users team-id :data])

(defn team-channels-key [team-id]
  [:enumerate-users team-id :channels])

;; Derived Data ================================================================

(defn drv-spec [db route-db]
  {:base                [[] db]
   :route               [[] route-db]
   :org-slug            [[:route] (fn [route] (:org route))]
   :board-slug          [[:route] (fn [route] (:board route))]
   :su-share            [[:base] (fn [base] (:su-share base))]
   :updates-list        [[:base :org-slug]
                          (fn [base org-slug]
                            (when org-slug
                              (:items (get-in base (updates-list-key org-slug)))))]
   :team-management     [[:base :route]
                          (fn [base route]
                            {:um-invite (:um-invite base)
                             :private-board-invite (:private-board-invite base)
                             :query-params (:query-params route)
                             :enumerate-users (:enumerate-users base)
                             :um-domain-invite (:um-domain-invite base)
                             :add-email-domain-team-error (:add-email-domain-team-error base)
                             :enumerate-users-requested (:enumerate-users-requested base)})]
   :jwt                 [[:base] (fn [base] (:jwt base))]
   :current-user-data   [[:base] (fn [base] (:current-user-data base))]
   :subscription        [[:base] (fn [base] (:subscription base))]
   :show-login-overlay  [[:base] (fn [base] (:show-login-overlay base))]
   :rum-popover-data    [[:base] (fn [base] (:rum-popover-data base))]
   :org-data            [[:base :org-slug]
                          (fn [base org-slug]
                            (when org-slug
                              (get-in base (org-data-key org-slug))))]
   :team-data           [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-data-key (:team-id org-data)))))]
   :team-channels       [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-channels-key (:team-id org-data)))))]
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

(defmulti action (fn [db [action-type & _]]
                   (when (not= action-type :input)
                     (timbre/info "Dipatching action:" action-type))
                   action-type))

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

(defn api-entry-point
  "Get the API entry point."
  ([] (api-entry-point @app-state))
  ([data] (get-in data api-entry-point-key)))

(defn org-data
  "Get org data."
  ([]
    (org-data @app-state))
  ([data]
    (org-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (org-data-key org-slug))))

(defn board-data
  "Get board data."
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
    (latest-stakeholder-update data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (latest-update-key org-slug))))

(defn updates-list-data
  ([]
    (updates-list-data @app-state))
  ([data]
    (updates-list-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (updates-list-key org-slug))))

(defn update-data
  ([]
    (update-data @app-state))
  ([data]
    (update-data data (router/current-org-slug) (router/current-update-slug)))
  ([data org-slug update-slug]
    (get-in data (update-key org-slug update-slug))))

(defn force-edit-topic []
  (:force-edit-topic @app-state))

(defn entry
  ([org-slug board-slug topic-slug as-of] (entry org-slug board-slug topic-slug as-of @app-state))
  ([org-slug board-slug topic-slug as-of data] (get-in data (entry-key org-slug board-slug topic-slug as-of))))

(defn entries-data
  ([] (entries-data @app-state (router/current-org-slug) (router/current-board-slug)))
  ([data] (entries-data data (router/current-org-slug) (router/current-board-slug)))
  ([data org-slug board-slug] (get-in data (entries-key org-slug board-slug))))

(defn topic-entries-data
  ([topic-slug] (topic-entries-data (router/current-org-slug) (router/current-board-slug) topic-slug))
  ([org-slug board-slug topic-slug] (topic-entries-data org-slug board-slug topic-slug @app-state))
  ([org-slug board-slug topic-slug data] (get-in data (topic-entries-key org-slug board-slug topic-slug))))

(defn teams-data
  ([] (teams-data @app-state))
  ([data] (get-in data teams-data-key)))

(defn team-data
  ([] (team-data (:team-id (org-data))))
  ([team-id] (team-data team-id @app-state))
  ([team-id data] (get-in data (team-data-key team-id))))

(defn team-channels
  ([] (team-channels (:team-id (org-data))))
  ([team-id] (team-channels team-id @app-state))
  ([team-id data] (get-in data (team-channels-key team-id))))

(defn foce-topic-key []
  (:foce-key @app-state))

(defn foce-topic-data []
  (:foce-data @app-state))

(defn foce-topic-data-editing? []
  (:foce-data-editing? @app-state))

;; Debug functions

(defn print-app-state []
  (js/console.log @app-state))

(defn print-org-data []
  (js/console.log (get-in @app-state (org-data-key (router/current-org-slug)))))

(defn print-team-data []
  (js/console.log (get-in @app-state (team-data-key (:team-id (org-data))))))

(defn print-updates-list-data []
  (js/console.log (get-in @app-state (updates-list-key (router/current-org-slug)))))

(defn print-update-data []
  (js/console.log (get-in @app-state (update-key (router/current-org-slug) (router/current-update-slug)))))

(defn print-board-data []
  (js/console.log (get-in @app-state (board-data-key (router/current-org-slug) (router/current-board-slug)))))

(defn print-entries-data []
  (js/console.log (get-in @app-state (entries-key (router/current-org-slug) (router/current-board-slug)))))

(set! (.-OCWebPrintAppState js/window) print-app-state)
(set! (.-OCWebPrintOrgData js/window) print-org-data)
(set! (.-OCWebPrintTeamData js/window) print-team-data)
(set! (.-OCWebPrintUpdatesListData js/window) print-updates-list-data)
(set! (.-OCWebPrintUpdateData js/window) print-update-data)
(set! (.-OCWebPrintBoardData js/window) print-board-data)
(set! (.-OCWebPrintEntriesData js/window) print-entries-data)