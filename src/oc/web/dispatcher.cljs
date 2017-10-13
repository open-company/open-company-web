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

(defn org-key [org-slug]
  [(keyword org-slug)])

(defn org-data-key [org-slug]
  (vec (conj (org-key org-slug) :org-data)))

(defn all-posts-key [org-slug]
  (vec (concat (org-key org-slug) [:boards :all-posts])))

(defn calendar-key [org-slug]
  (vec (conj (org-key org-slug) :calendar)))

(defn change-data-key [org-slug]
  (vec (conj (org-key org-slug) :change-data)))

(defn board-key [org-slug board-slug]
  (vec (concat (org-key org-slug) [:boards (keyword board-slug)])))

(defn board-data-key [org-slug board-slug]
  (conj (board-key org-slug board-slug) :board-data))

(defn secure-activity-key [org-slug secure-id]
  (vec (concat (org-key org-slug) [:secure-activities secure-id])))

(defn activity-key [org-slug board-slug activity-uuid]
  (let [board-key (if (= board-slug :all-posts)
                    (all-posts-key org-slug)
                    (board-data-key org-slug board-slug))]
    (vec (concat board-key [:fixed-items activity-uuid]))))

(defn comments-key [org-slug board-slug]
  (vec (conj (board-key org-slug board-slug) :comments-data)))

(defn activity-comments-key [org-slug board-slug activity-uuid]
  (vec (conj (comments-key org-slug board-slug) activity-uuid :sorted-comments)))

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
   :secure-id           [[:route] (fn [route] (:secure-id route))]
   :story-uuid          [[:route] (fn [route] (:activity route))]
   :su-share            [[:base] (fn [base] (:su-share base))]
   :board-filters       [[:base] (fn [base] (:board-filters base))]
   :loading             [[:base] (fn [base] (:loading base))]
   :signup-with-email   [[:base] (fn [base] (:signup-with-email base))]
   :query-params        [[:route] (fn [route] (:query-params route))]
   :teams-data          [[:base] (fn [base] (get-in base teams-data-key))]
   :auth-settings       [[:base] (fn [base] (:auth-settings base))]
   :org-settings        [[:base] (fn [base] (:org-settings base))]
   :email-verification  [[:base :auth-settings]
                          (fn [base auth-settings]
                            {:auth-settings auth-settings
                             :error (:email-verification-error base)
                             :success (:email-verification-success base)})]
   :teams-load          [[:base :auth-settings]
                          (fn [base auth-settings]
                            {:teams-data-requested (:teams-data-requested base)
                             :auth-settings auth-settings})]
   :team-management     [[:base :query-params]
                          (fn [base query-params]
                            {:um-invite (:um-invite base)
                             :private-board-invite (:private-board-invite base)
                             :query-params query-params
                             :teams-data (:teams-data base)
                             :um-domain-invite (:um-domain-invite base)
                             :add-email-domain-team-error (:add-email-domain-team-error base)
                             :teams-data-requested (:teams-data-requested base)})]
   :jwt                 [[:base] (fn [base] (:jwt base))]
   :current-user-data   [[:base] (fn [base] (:current-user-data base))]
   :subscription        [[:base] (fn [base] (:subscription base))]
   :show-login-overlay  [[:base] (fn [base] (:show-login-overlay base))]
   :rum-popover-data    [[:base] (fn [base] (:rum-popover-data base))]
   :about-carrot-modal  [[:base] (fn [base] (:about-carrot-modal base))]
   :made-with-carrot-modal [[:base] (fn [base] (:made-with-carrot-modal base))]
   :org-data            [[:base :org-slug]
                          (fn [base org-slug]
                            (when org-slug
                              (get-in base (org-data-key org-slug))))]
   :team-data           [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-data-key (:team-id org-data)))))]
   :team-roster         [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-roster-key (:team-id org-data)))))]
   :invite-users        [[:base :team-data :current-user-data :team-roster]
                          (fn [base team-data current-user-data team-roster]
                            {:team-data team-data
                             :invite-users (:invite-users base)
                             :current-user-data current-user-data
                             :team-roster team-roster})]
   :org-settings-team-management
                        [[:base :query-params :org-data :team-data :auth-settings]
                          (fn [base query-params org-data team-data]
                            {:um-domain-invite (:um-domain-invite base)
                             :add-email-domain-team-error (:add-email-domain-team-error base)
                             :team-data team-data
                             :query-params query-params})]
   :all-posts        [[:base :org-slug]
                          (fn [base org-slug]
                            (when (and base org-slug)
                              (get-in base (all-posts-key org-slug))))]
   :calendar            [[:base :org-slug]
                          (fn [base org-slug]
                            (when (and base org-slug)
                              (get-in base (calendar-key org-slug))))]
   :team-channels       [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-channels-key (:team-id org-data)))))]
   :change-data         [[:base :org-slug]
                          (fn [base org-slug]
                            (when (and base org-slug)
                              (get-in base (change-data-key org-slug))))]
   :board-data          [[:base :org-slug :board-slug]
                          (fn [base org-slug board-slug]
                            (when (and org-slug board-slug)
                              (get-in base (board-data-key org-slug board-slug))))]
   :activity-data       [[:base :org-slug :board-slug :activity-uuid :secure-id]
                          (fn [base org-slug board-slug activity-uuid secure-id]
                            (get-in base (activity-key org-slug board-slug (or activity-uuid secure-id))))]
   :comments-data       [[:base :org-slug :board-slug]
                        (fn [base org-slug board-slug]
                          (get-in base (comments-key org-slug board-slug)))]
   :activity-comments-data [[:base :org-slug :board-slug :activity-uuid :secure-id]
                           (fn [base org-slug board-slug activity-uuid secure-id]
                              (get-in base (butlast (activity-comments-key org-slug board-slug (or activity-uuid secure-id)))))]
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
   :entry-editing       [[:base]
                          (fn [base]
                            (:entry-editing base))]
   :board-editing       [[:base]
                          (fn [base]
                            (:board-editing base))]
   :org-editing         [[:base]
                          (fn [base]
                            (:org-editing base))]
   :story-editing       [[:base]
                          (fn [base]
                            (:story-editing base))]
   :alert-modal         [[:base]
                          (fn [base]
                            (:alert-modal base))]
   :entry-edit-topics   [[:base :org-slug :board-slug :board-data :entry-editing]
                          (fn [base org-slug board-slug board-data entry-editing]
                            (if (:topics board-data)
                              (:topics board-data)
                              (let [edit-board-slug (:board-slug entry-editing)]
                                (get-in base (vec (conj (board-data-key org-slug edit-board-slug) :topics))))))]
   :navbar-data         [[:base :org-data :board-data]
                          (fn [base org-data board-data]
                            (let [navbar-data (select-keys base [:mobile-menu-open :show-login-overlay])]
                              (-> navbar-data
                                (assoc :org-data org-data)
                                (assoc :board-data board-data))))]
   :story-editing-publish [[:base] (fn [base] (:story-editing-published-url base))]
   :activity-share        [[:base] (fn [base] (:activity-share base))]
   :activity-shared-data  [[:base] (fn [base] (:activity-shared-data base))]
   :confirm-invitation    [[:base :jwt]
                            (fn [base jwt]
                              {:invitation-confirmed (:email-confirmed base)
                               :collect-pswd (:collect-pswd base)
                               :collect-pswd-error (:collect-password-error base)
                               :jwt jwt})]
   :password-reset        [[:base :auth-settings]
                            (fn [base auth-settings]
                              {:auth-settings auth-settings
                               :error (:collect-pswd-error base)})]
   :media-input           [[:base]
                            (fn [base]
                              (:media-input base))]
   :comment-add-finish    [[:base] (fn [base] (:comment-add-finish base))]})

;; Action Loop =================================================================

(defmulti action (fn [db [action-type & _]]
                   (when (not= action-type :input)
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

(defn all-posts-data
  "Get org all posts data."
  ([]
    (all-posts-data @app-state))
  ([data]
    (all-posts-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (all-posts-key org-slug))))

(defn calendar-data
  "Get org calendar data."
  ([]
    (calendar-data @app-state))
  ([data]
    (calendar-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (calendar-key org-slug))))

(defn change-data
  "Get chancge data."
  ([]
    (change-data @app-state))
  ([data]
    (change-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (change-data-key org-slug))))

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
    (let [activity-key (activity-key org-slug board-slug activity-id)]
      (get-in data activity-key))))

(defn comments-data
  ([]
    (comments-data (router/current-org-slug) (router/current-board-slug) @app-state))
  ([org-slug board-slug]
    (comments-data org-slug board-slug @app-state))
  ([org-slug board-slug data]
    (get-in data (comments-key org-slug board-slug))))

(defn activity-comments-data
  ([]
    (activity-comments-data (router/current-org-slug) (router/current-board-slug) (router/current-activity-id) @app-state))
  ([activity-uuid]
    (activity-comments-data (router/current-org-slug) (router/current-board-slug) activity-uuid @app-state))
  ([org-slug board-slug activity-uuid]
    (activity-comments-data org-slug board-slug activity-uuid @app-state))
  ([org-slug board-slug activity-uuid data]
    (get-in data (activity-comments-key org-slug board-slug activity-uuid))))

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

(defn print-all-posts-data []
  (js/console.log (get-in @app-state (all-posts-key (router/current-org-slug)))))

(defn print-team-data []
  (js/console.log (get-in @app-state (team-data-key (:team-id (org-data))))))

(defn print-team-roster []
  (js/console.log (get-in @app-state (team-roster-key (:team-id (org-data))))))

(defn print-change-data []
  (js/console.log (get-in @app-state (change-data-key (router/current-org-slug)))))

(defn print-board-data []
  (js/console.log (get-in @app-state (board-data-key (router/current-org-slug) (router/current-board-slug)))))

(defn print-activities-data []
  (js/console.log (get-in @app-state (conj (board-data-key (router/current-org-slug) (router/current-board-slug)) :fixed-items))))

(defn print-activity-data []
  (js/console.log (get-in @app-state (activity-key (router/current-org-slug) (router/current-board-slug) (router/current-activity-id)))))

(defn print-secure-activity-data []
  (js/console.log (get-in @app-state (secure-activity-key (router/current-org-slug) (router/current-secure-activity-id)))))

(defn print-reactions-data []
  (js/console.log (get-in @app-state (conj (activity-key (router/current-org-slug) (router/current-board-slug) (router/current-activity-id)) :reactions))))

(defn print-comments-data []
  (js/console.log (get-in @app-state (comments-key (router/current-org-slug) (router/current-board-slug)))))

(defn print-activity-comments-data []
  (js/console.log (get-in @app-state (activity-comments-key (router/current-org-slug) (router/current-board-slug) (router/current-activity-id)))))

(defn print-entry-editing-data []
  (js/console.log (get @app-state :entry-editing)))

(defn print-story-editing-data []
  (js/console.log (get @app-state :story-editing)))

(set! (.-OCWebPrintAppState js/window) print-app-state)
(set! (.-OCWebPrintOrgData js/window) print-org-data)
(set! (.-OCWebPrintAllPostsData js/window) print-all-posts-data)
(set! (.-OCWebPrintTeamData js/window) print-team-data)
(set! (.-OCWebPrintTeamRoster js/window) print-team-roster)
(set! (.-OCWebPrintChangeData js/window) print-change-data)
(set! (.-OCWebPrintBoardData js/window) print-board-data)
(set! (.-OCWebPrintActivitiesData js/window) print-activities-data)
(set! (.-OCWebPrintActivityData js/window) print-activity-data)
(set! (.-OCWebPrintSecureActivityData js/window) print-secure-activity-data)
(set! (.-OCWebPrintReactionsData js/window) print-reactions-data)
(set! (.-OCWebPrintCommentsData js/window) print-activity-comments-data)
(set! (.-OCWebPrintActivityCommentsData js/window) print-comments-data)
(set! (.-OCWebPrintEntryEditingData js/window) print-entry-editing-data)
(set! (.-OCWebPrintStoryEditingData js/window) print-story-editing-data)