(ns oc.web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [org.martinklepsch.derivatives :as drv]
            [taoensso.timbre :as timbre]
            [oc.web.router :as router]))

(defonce app-state (atom {:loading false
                          :mobile-menu-open false
                          :show-login-overlay false
                          :trend-bar-status :hidden}))

;; Data key paths

(def api-entry-point-key [:api-entry-point])

(defn org-data-key [org-slug]
  [(keyword org-slug) :org-data])

(defn all-activity-key [org-slug]
  [(keyword org-slug) :all-activity])

(defn calendar-key [org-slug]
  [(keyword org-slug) :calendar])

(defn board-data-key [org-slug board-slug]
  [(keyword org-slug) :boards (keyword board-slug) :board-data])

(defn board-cache-key [org-slug board-slug]
  [(keyword org-slug) (keyword board-slug) :cache])

(defn board-new-topics-key [org-slug board-slug]
  [(keyword org-slug) (keyword board-slug) :new-topics])

(defn board-new-categories-key [org-slug board-slug]
  [(keyword org-slug) (keyword board-slug) :new-categories])

(defn updates-list-key [org-slug]
  [(keyword org-slug) :updates-list])

(defn latest-update-key [org-slug]
  [(keyword org-slug) :latest-su])

(defn update-key [org-slug update-slug]
  [(keyword org-slug) :updates (keyword update-slug)])

(defn entries-key [org-slug board-slug]
  [(keyword org-slug) :boards (keyword board-slug) :entries-data])

(defn comments-key [org-slug board-slug activity-uuid]
 (vec (conj (entries-key org-slug board-slug) activity-uuid :comments-data)))

(def teams-data-key [:teams-data :teams])

(defn team-data-key [team-id]
  [:teams-data team-id :data])

(defn team-roster-key [team-id]
  [:teams-data team-id :roster])

(defn team-channels-key [team-id]
  [:teams-data team-id :channels])

;; Derived Data ================================================================

(defn drv-spec [db route-db]
  {:base                [[] db]
   :route               [[] route-db]
   :orgs                [[:base] (fn [base] (:orgs base))]
   :org-slug            [[:route] (fn [route] (:org route))]
   :board-slug          [[:route] (fn [route] (:board route))]
   :topic-slug          [[:route] (fn [route] (:topic route))]
   :activity-uuid       [[:route] (fn [route] (:activity route))]
   :su-share            [[:base] (fn [base] (:su-share base))]
   :board-filters       [[:base] (fn [base] (:board-filters base))]
   :loading             [[:base] (fn [base] (:loading base))]
   :updates-list        [[:base :org-slug]
                          (fn [base org-slug]
                            (when org-slug
                              (:items (get-in base (updates-list-key org-slug)))))]
   :team-management     [[:base :route]
                          (fn [base route]
                            {:um-invite (:um-invite base)
                             :private-board-invite (:private-board-invite base)
                             :query-params (:query-params route)
                             :teams-data (:teams-data base)
                             :um-domain-invite (:um-domain-invite base)
                             :add-email-domain-team-error (:add-email-domain-team-error base)
                             :teams-data-requested (:teams-data-requested base)})]
   :jwt                 [[:base] (fn [base] (:jwt base))]
   :current-user-data   [[:base] (fn [base] (:current-user-data base))]
   :subscription        [[:base] (fn [base] (:subscription base))]
   :show-login-overlay  [[:base] (fn [base] (:show-login-overlay base))]
   :rum-popover-data    [[:base] (fn [base] (:rum-popover-data base))]
   :foce-data           [[:base] (fn [base] (:foce-data base))]
   :org-data            [[:base :org-slug]
                          (fn [base org-slug]
                            (when org-slug
                              (get-in base (org-data-key org-slug))))]
   :all-activity        [[:base :org-slug]
                          (fn [base org-slug]
                            (when (and base org-slug)
                              (get-in base (all-activity-key org-slug))))]
   :calendar            [[:base :org-slug]
                          (fn [base org-slug]
                            (when (and base org-slug)
                              (get-in base (calendar-key org-slug))))]
   :team-data           [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-data-key (:team-id org-data)))))]
   :team-roster         [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-roster-key (:team-id org-data)))))]
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
                              (get-in base (board-data-key org-slug board-slug))))]
   :activity-data       [[:base :org-slug :board-slug :activity-uuid]
                          (fn [base org-slug board-slug activity-uuid]
                            (when (and org-slug board-slug activity-uuid)
                              (let [board-data (get-in base (board-data-key org-slug board-slug))]
                                (first (filter #(= (:uuid %) activity-uuid) (:entries board-data))))))]
   :comments-data       [[:base :org-slug :board-slug :activity-uuid]
                          (fn [base org-slug board-slug activity-uuid]
                            (when (and org-slug board-slug activity-uuid)
                              (let [comments-key (comments-key org-slug board-slug activity-uuid)
                                    comments-data (get-in base comments-key)]
                                comments-data)))]
   :trend-bar-status    [[:base]
                          (fn [base]
                            (:trend-bar-status base))]
   :edit-user-profile   [[:base]
                          (fn [base]
                            {:user-data (:edit-user-profile base)
                             :error (:edit-user-profile-failed base)})]
   :activity-modal-fade-in [[:base]
                             (fn [base]
                               (:activity-modal-fade-in base))]
   :error-banner        [[:base]
                          (fn [base]
                            {:error-banner-message (:error-banner-message base)
                             :error-banner-time (:error-banner-time base)})]
   :entry-editing    [[:base]
                          (fn [base]
                            (:entry-editing base))]
   :board-editing       [[:base]
                          (fn [base]
                            (:board-editing base))]
   :alert-modal         [[:base]
                          (fn [base]
                            (:alert-modal base))]
   :navbar-data         [[:base :org-data :board-data]
                          (fn [base org-data board-data]
                            (let [navbar-data (select-keys base [:mobile-menu-open :header-width :show-login-overlay])]
                              (-> navbar-data
                                (assoc :org-data org-data)
                                (assoc :board-data board-data))))]})

;; Action Loop =================================================================

(defmulti action (fn [db [action-type & _]]
                   (when (and (not= action-type :input)
                              (not= action-type :foce-input))
                     (timbre/info "Dispatching action:" action-type))
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

(defn all-activity-data
  "Get org all activity data."
  ([]
    (all-activity-data @app-state))
  ([data]
    (all-activity-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (all-activity-key org-slug))))

(defn calendar-data
  "Get org calendar data."
  ([]
    (calendar-data @app-state))
  ([data]
    (calendar-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (calendar-key org-slug))))

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

(defn activity-data
  "Get activity data."
  ([]
    (activity-data (router/current-org-slug) (router/current-board-slug) (router/current-activity-id) @app-state))
  ([activity-id]
    (activity-data (router/current-org-slug) (router/current-board-slug) activity-id @app-state))
  ([board-slug activity-id]
    (activity-data (router/current-org-slug) board-slug activity-id @app-state))
  ([org-slug board-slug activity-id]
    (activity-data org-slug board-slug activity-id @app-state))
  ([org-slug board-slug activity-id data]
    (let [is-all-activity (:from-all-activity @router/path)
          data-key (if is-all-activity (all-activity-key org-slug) (board-data-key org-slug board-slug))
          board-data (get-in data data-key)
          activities-data (if is-all-activity
                            (:items board-data)
                            (if (= (:type board-data) "story")
                              (:stories board-data)
                              (:entries board-data)))]
      (first (filter #(= (:uuid %) activity-id) activities-data)))))

(defn comments-data
  ([]
    (comments-data (router/current-org-slug) (router/current-board-slug) (router/current-activity-id) @app-state))
  ([activity-uuid]
    (comments-data (router/current-org-slug) (router/current-board-slug) activity-uuid @app-state))
  ([org-slug board-slug activity-uuid]
    (comments-data org-slug board-slug activity-uuid @app-state))
  ([org-slug board-slug activity-uuid data]
    (get-in data (comments-key org-slug board-slug activity-uuid))))

(defn entries-data
  ([] (entries-data @app-state (router/current-org-slug) (router/current-board-slug)))
  ([data] (entries-data data (router/current-org-slug) (router/current-board-slug)))
  ([data org-slug board-slug] (get-in data (entries-key org-slug board-slug))))

(defn teams-data
  ([] (teams-data @app-state))
  ([data] (get-in data teams-data-key)))

(defn team-data
  ([] (team-data (:team-id (org-data))))
  ([team-id] (team-data team-id @app-state))
  ([team-id data] (get-in data (team-data-key team-id))))

(defn team-roster
  ([] (team-roster (:team-id (org-data))))
  ([team-id] (team-roster team-id @app-state))
  ([team-id data] (get-in data (team-roster-key team-id))))

(defn team-channels
  ([] (team-channels (:team-id (org-data))))
  ([team-id] (team-channels team-id @app-state))
  ([team-id data] (get-in data (team-channels-key team-id))))

;; Debug functions

(defn print-app-state []
  (js/console.log @app-state))

(defn print-org-data []
  (js/console.log (get-in @app-state (org-data-key (router/current-org-slug)))))

(defn print-all-activity-data []
  (js/console.log (get-in @app-state (all-activity-key (router/current-org-slug)))))

(defn print-team-data []
  (js/console.log (get-in @app-state (team-data-key (:team-id (org-data))))))

(defn print-team-roster []
  (js/console.log (get-in @app-state (team-roster-key (:team-id (org-data))))))

(defn print-board-data []
  (js/console.log (get-in @app-state (board-data-key (router/current-org-slug) (router/current-board-slug)))))

(defn print-entries-data []
  (js/console.log (get-in @app-state (entries-key (router/current-org-slug) (router/current-board-slug)))))

(set! (.-OCWebPrintAppState js/window) print-app-state)
(set! (.-OCWebPrintOrgData js/window) print-org-data)
(set! (.-OCWebPrintAllActivityData js/window) print-all-activity-data)
(set! (.-OCWebPrintTeamData js/window) print-team-data)
(set! (.-OCWebPrintTeamRoster js/window) print-team-roster)
(set! (.-OCWebPrintBoardData js/window) print-board-data)
(set! (.-OCWebPrintEntriesData js/window) print-entries-data)