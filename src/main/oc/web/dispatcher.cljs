(ns oc.web.dispatcher
  (:require [defun.core :refer (defun)]
            [taoensso.timbre :as timbre]
            [clojure.string :as s]
            [clojure.set :as clj-set]
            [cljs-flux.dispatcher :as flux]
            [oc.web.utils.drafts :as du]))


(defn- s-or-k? [x]
  (or (keyword? x) (string? x)))

(defonce ^{:export true} app-state (atom {:loading false
                                          :show-login-overlay false}))

(def ^{:export true} recent-activity-sort :recent-activity)
(def ^{:export true} recently-posted-sort :recently-posted)

(def ^{:export true} default-foc-layout :expanded)
(def ^{:export true} other-foc-layout :collapsed)

(def ^{:export true} premium-picker-modal :show-premium-picker?)

;; Pre-declare some routing functions

(declare current-org-slug)
(declare current-board-slug)
(declare current-contributions-id)
(declare current-label-slug)
(declare current-container-slug)
(declare current-sort-type)
(declare current-activity-id)
(declare current-secure-activity-id)
(declare current-comment-id)
(declare query-params)
(declare query-param)
(declare get-label)

;; Data key paths

(def ^{:export true} router-key :router-path)
(def ^{:export true} router-opts-key :opts)
(def ^{:export true} router-dark-allowed-key :dark-allowed)

(def ^{:export true} checkout-result-key :checkout-success-result)
(def ^{:export true} checkout-update-price-key :checkout-update-price)

(def ^{:export true} expo-key [:expo])

(def ^{:export true} expo-deep-link-origin-key (vec (conj expo-key :deep-link-origin)))
(def ^{:export true} expo-app-version-key (vec (conj expo-key :app-version)))
(def ^{:export true} expo-push-token-key (vec (conj expo-key :push-token)))

(def ^{:export true} show-invite-box-key :show-invite-box)

(def ^{:export true} api-entry-point-key [:api-entry-point])

(def ^{:export true} auth-settings-key [:auth-settings])

(def ^{:export true} notifications-key [:notifications-data])
(def ^{:export true} show-login-overlay-key :show-login-overlay)

(def ^{:export true} current-user-key [:current-user-data])

(def ^{:export true} orgs-key :orgs)

(defn ^:export org-key [org-slug]
  [(keyword org-slug)])

(defn ^:export org-data-key [org-slug]
  (vec (conj (org-key org-slug) :org-data)))

(defn ^:export boards-key [org-slug]
  (vec (conj (org-key org-slug) :boards)))

;; Labels

(def ^{:export true} editing-label-key [:editing-label])

(def ^{:export true} foc-labels-picker-key [:foc-labels-picker])

(defn ^:export labels-key [org-slug]
  (vec (conj (org-key org-slug) :labels)))

(defn ^:export org-labels-key [org-slug]
  (vec (conj (labels-key org-slug) :org-labels)))

(defn ^:export user-labels-key [org-slug]
  (vec (conj (labels-key org-slug) :user-labels)))

;; Editable boards keys

(defn ^:export editable-boards-key [org-slug]
  (vec (conj (org-key org-slug) :editable-boards)))

(defn ^:export private-boards-key [org-slug]
  (vec (conj (org-key org-slug) :private-boards)))

(defn ^:export public-boards-key [org-slug]
  (vec (conj (org-key org-slug) :public-boards)))

(defn ^:export payments-key [org-slug]
  (vec (conj (org-key org-slug) :payments)))

(defn ^:export payments-notify-cache-key [org-slug]
  (vec (conj (org-key org-slug) :payments-notify-cache)))

(defn ^:export posts-data-key [org-slug]
  (vec (conj (org-key org-slug) :posts)))

(defn ^:export board-key 
  ([org-slug board-slug sort-type]
    (if sort-type
      (vec (concat (boards-key org-slug) [(keyword board-slug) (keyword sort-type)]))
      (vec (concat (boards-key org-slug) [(keyword board-slug)]))))
  ([org-slug board-slug]
   (vec (concat (boards-key org-slug) [(keyword board-slug) recently-posted-sort]))))

(defn ^:export board-data-key
  ([org-slug board-slug]
   (board-data-key org-slug board-slug recently-posted-sort))
  ([org-slug board-slug sort-type]
    (conj (board-key org-slug board-slug sort-type) :board-data)))

(defn ^:export contributions-list-key [org-slug]
  (vec (conj (org-key org-slug) :contribs)))

(defn ^:export contributions-key
  ([org-slug author-uuid]
   (contributions-key org-slug author-uuid recently-posted-sort))
  ([org-slug author-uuid sort-type]
   (if sort-type
     (vec (concat (contributions-list-key org-slug) [(keyword author-uuid) (keyword sort-type)]))
     (vec (conj (contributions-list-key org-slug) (keyword author-uuid))))))

(defn ^:export contributions-data-key
  ([org-slug slug-or-uuid sort-type]
   (conj (contributions-key org-slug slug-or-uuid sort-type) :contrib-data))
  ([org-slug slug-or-uuid]
   (conj (contributions-key org-slug slug-or-uuid) :contrib-data)))

;; Label entries

(defn ^:export label-entries-list-key [org-slug]
  (vec (conj (org-key org-slug) :label-entries)))

(defn ^:export label-entries-key
  ([org-slug label-slug]
   (label-entries-key org-slug label-slug recently-posted-sort))
  ([org-slug label-slug sort-type]
   (if sort-type
     (vec (concat (label-entries-list-key org-slug) [(keyword label-slug) (keyword sort-type)]))
     (vec (conj (label-entries-list-key org-slug) (keyword label-slug))))))

(defn ^:export label-entries-data-key
  ([org-slug slug-or-uuid sort-type]
   (conj (label-entries-key org-slug slug-or-uuid sort-type) :label-data))
  ([org-slug slug-or-uuid]
   (conj (label-entries-key org-slug slug-or-uuid) :label-data)))

;; Containers

(defn ^:export containers-key [org-slug]
  (vec (conj (org-key org-slug) :container-data)))

(defn ^:export container-key
  ([org-slug items-filter]
   (container-key org-slug items-filter recently-posted-sort))
  ([org-slug items-filter sort-type]
   (cond
     sort-type
     (vec (conj (containers-key org-slug) (keyword items-filter) (keyword sort-type)))
     :else
     (vec (conj (containers-key org-slug) (keyword items-filter))))))

(defn ^:export badges-key [org-slug]
  (vec (conj (org-key org-slug) :badges)))

(defn ^:export replies-badge-key [org-slug]
  (vec (conj (badges-key org-slug) :replies)))

(defn ^:export following-badge-key [org-slug]
  (vec (conj (badges-key org-slug) :following)))

(defn ^:export secure-activity-key [org-slug secure-id]
  (vec (concat (org-key org-slug) [:secure-activities secure-id])))

(defn ^:export activity-key [org-slug activity-uuid]
  (let [posts-key (posts-data-key org-slug)]
    (vec (concat posts-key [activity-uuid]))))
    
(defn ^:export entry-labels-key [org-slug activity-uuid]
  (let [post-key (activity-key org-slug activity-uuid)]
    (vec (concat post-key [:labels]))))

(defn ^:export pins-key [org-slug entry-uuid]
  (let [entry-key (activity-key org-slug entry-uuid)]
    (vec (concat entry-key [:pins]))))

(defn ^:export pin-key [org-slug entry-uuid pin-container-uuid]
  (vec (concat (pins-key org-slug entry-uuid) pin-container-uuid)))

(defn ^:export activity-last-read-at-key [org-slug activity-uuid]
  (vec (conj (activity-key org-slug activity-uuid) :last-read-at)))

(defn ^:export add-comment-key [org-slug]
  (vec (concat (org-key org-slug) [:add-comment-data])))

(defn ^:export add-comment-string-key
  ([activity-uuid] (add-comment-string-key activity-uuid nil nil))
  ([activity-uuid parent-comment-uuid] (add-comment-string-key activity-uuid parent-comment-uuid nil))
  ([activity-uuid parent-comment-uuid comment-uuid]
   (str activity-uuid
     (when parent-comment-uuid
       (str "-" parent-comment-uuid))
     (when comment-uuid
       (str "-" comment-uuid)))))

(def ^{:export true} add-comment-force-update-root-key :add-comment-force-update)

(defn ^:export add-comment-force-update-key [add-comment-string-key]
  (vec (concat [add-comment-force-update-root-key] [add-comment-string-key])))

(defn ^:export add-comment-activity-key [org-slug activity-uuid]
  (vec (concat (add-comment-key org-slug) [activity-uuid])))

(defn ^:export comment-reply-to-key [org-slug]
  (vec (conj (org-key org-slug) :comment-reply-to-key)))

(defn ^:export comments-key [org-slug]
  (vec (conj (org-key org-slug) :comments)))

(defn ^:export activity-comments-key [org-slug activity-uuid]
  (vec (conj (comments-key org-slug) activity-uuid)))

(def ^{:export true} sorted-comments-key :sorted-comments)

(defn ^:export activity-sorted-comments-key [org-slug activity-uuid]
  (vec (concat (comments-key org-slug) [activity-uuid sorted-comments-key])))

(def ^{:export true} teams-data-key [:teams-data :teams])

(defn ^:export team-data-key [team-id]
  [:teams-data team-id :data])

(defn ^:export team-roster-key [team-id]
  [:teams-data team-id :roster])

(defn ^:export team-channels-key [team-id]
  [:teams-data team-id :channels])

(defn ^:export active-users-key [org-slug]
  (vec (conj (org-key org-slug) :active-users)))

(defn ^:export follow-list-key [org-slug]
  (vec (conj (org-key org-slug) :follow-list)))

(defn ^:export follow-list-last-added-key [org-slug]
  (vec (conj (org-key org-slug) :follow-list-last-added)))

(defn ^:export follow-publishers-list-key [org-slug]
  (vec (conj (follow-list-key org-slug) :publisher-uuids)))

(defn ^:export follow-boards-list-key [org-slug]
  (vec (conj (follow-list-key org-slug) :follow-boards-list)))

(defn ^:export unfollow-board-uuids-key [org-slug]
  (vec (conj (follow-list-key org-slug) :unfollow-board-uuids)))

(defn ^:export followers-count-key [org-slug]
  (vec (conj (org-key org-slug) :followers-count)))

(defn ^:export followers-publishers-count-key [org-slug]
  (vec (conj (followers-count-key org-slug) :publishers)))

(defn ^:export followers-boards-count-key [org-slug]
  (vec (conj (followers-count-key org-slug) :boards)))

(defn ^:export mention-users-key [org-slug]
  (vec (conj (org-key org-slug) :mention-users)))

(defn ^:export users-info-hover-key [org-slug]
  (vec (conj (org-key org-slug) :users-info-hover)))

(defn ^:export uploading-video-key [org-slug video-id]
  (vec (concat (org-key org-slug) [:uploading-videos video-id])))

(defn ^:export current-board-key
  "Find the board key for db based on the current path."
  []
  (let [org-slug (current-org-slug)
        board-slug (current-board-slug)]
     (board-data-key org-slug board-slug)))

(def ^{:export true} can-compose-key :can-copmose?)

(defn ^:export org-can-compose-key
  "Key for a boolean value: true if the user has at least one board
   he can publish updates in."
  [org-slug]
  (vec (conj (org-data-key org-slug) can-compose-key)))

;; User notifications

(defn ^:export user-notifications-key [org-slug]
  (vec (conj (org-key org-slug) :user-notifications)))

;; Reminders

(defn ^:export reminders-key [org-slug]
  (vec (conj (org-key org-slug) :reminders)))

(defn ^:export reminders-data-key [org-slug]
  (vec (conj (reminders-key org-slug) :reminders-list)))

(defn ^:export reminders-roster-key [org-slug]
  (vec (conj (reminders-key org-slug) :reminders-roster)))

(defn ^:export reminder-edit-key [org-slug]
  (vec (conj (reminders-key org-slug) :reminder-edit)))

;; Change related keys

(defn ^:export change-data-key [org-slug]
  (vec (conj (org-key org-slug) :change-data)))

(def ^{:export true} activities-read-key
  [:activities-read])

;; Seen

(defn ^:export org-seens-key [org-slug]
  (vec (conj (org-key org-slug) :container-seen)))

; (defn ^{:export true} container-seen-key [org-slug container-id]
;   (vec (conj (org-seens-key org-slug) (keyword container-id))))

;; Cmail keys

(def ^{:export true} cmail-state-key [:cmail-state])

(def ^{:export true} cmail-data-key [:cmail-data])

;; Payments keys

(def payments-checkout-session-result :checkout-session-result)

;; Payments UI banner keys

(def ^{:export true} payments-ui-upgraded-banner-key :payments-ui-upgraded-banner)

;; Boards helpers

(defn get-posts-for-board [posts-data board-slug]
  (let [filter-fn (if (= board-slug du/default-drafts-board-slug)
                    #(not= (:status %) "published")
                    #(and (= (:board-slug %) board-slug)
                          (= (:status %) "published")))]
    (filter (comp filter-fn last) posts-data)))

;; Container helpers

(defn is-container? [container-slug]
  ;; Rest of containers
  (#{:inbox :all-posts :bookmarks :following :unfollowing :activity :replies} (keyword container-slug)))

(defn is-container-with-sort? [container-slug]
  ;; Rest of containers
  (#{"all-posts" "following" "unfollowing"} container-slug))

(defn is-recent-activity? [container-slug]
  (when-let [container-slug-kw (keyword container-slug)]
    (#{:replies} container-slug-kw)))

;; Internal getter helpes

(defn- get-container-posts [base posts-data org-slug container-slug sort-type items-key]
  (let [cnt-key (cond
                  (is-container? container-slug)
                  (container-key org-slug container-slug sort-type)
                  (seq (current-label-slug))
                  (label-entries-data-key org-slug container-slug)
                  (seq (current-contributions-id))
                  (contributions-data-key org-slug container-slug)
                  :else
                  (board-data-key org-slug container-slug))
        container-data (get-in base cnt-key)
        posts-list (get container-data items-key)
        container-posts (map (fn [entry]
                               (if (and (map? entry)
                                        (= (:resource-type entry) :entry)
                                        (map? (get posts-data (:uuid entry))))
                                 ;; Make sure the local map is merged as last value
                                 ;; since the kept value relates directly to the container
                                 (merge (get posts-data (:uuid entry)) entry)
                                 entry))
                         posts-list)
        items (if (= container-slug du/default-drafts-board-slug)
                (filter (comp not :published?) container-posts)
                container-posts)]
    (vec items)))

;; Label lookup

(def ^{:private true} label-lookup-keys [:uuid :slug])

(defun find-label
  ([labels-list label-uuid-or-slug]
   (find-label label-lookup-keys labels-list label-uuid-or-slug))

  ([lookup-keys labels-list label :guard string?]
   (find-label lookup-keys  labels-list (set [label])))

  ([lookup-keys labels-list labels :guard map?]
   (find-label lookup-keys  labels-list (-> labels
                                            (select-keys lookup-keys)
                                            vals
                                            set)))

  ([lookup-keys labels-list label-vals]
   (let [label-values-set (set label-vals)]
     (some #(when (-> %
                      (select-keys lookup-keys)
                      vals
                      set
                      (clj-set/intersection label-values-set)
                      seq)
            %)
           labels-list))))

(def ^{:export true} theme-key [:theme])
(def ^{:export true} theme-setting-key :setting-value)
(def ^{:export true} theme-mobile-key :mobile-value)
(def ^{:export true} theme-desktop-key :desktop-value)
(def ^{:export true} theme-web-key :web-value)

;; Functions needed by derivatives

;; (declare org-data)
;; (declare labels-data)
;; (declare org-labels-data)
;; (declare user-labels-data)
;; (declare editing-label)
;; (declare board-data)
;; (declare contributions-data)
;; (declare label-entries-data)
;; (declare editable-boards-data)
;; (declare private-boards-data)
;; (declare public-boards-data)
;; (declare activity-data)
;; (declare secure-activity-data)
;; (declare activity-read-data)
;; (declare activity-data-get)

;; Action Loop =================================================================

(defmulti action (fn [_db [action-type & _]]
                   (when (and (not= action-type :input)
                              (not= action-type :update)
                              (not= action-type :entry-toggle-save-on-exit)
                              (not= action-type :cmail-state/update)
                              (not= action-type :cmail-data/update))
                     (timbre/info "Dispatching action:" action-type))
                   action-type))

(def actions (flux/dispatcher))

(def actions-dispatch
  (flux/register
   actions
   (fn [payload]
     ;; (prn payload) ; debug :)
     (let [next-db (swap! app-state action payload)]
       (when (get next-db nil)
         (timbre/warn "Nil key in app-state! Content:" (get next-db nil)))
       next-db))))

(defn ^:export dispatch! [payload]
  (flux/dispatch actions payload))

;; Path components retrieve

(defn ^:export db []
  (deref app-state))

(defn ^:export route
  ([] (route @app-state))
  ([data] (get-in data [router-key])))

(defn ^:export current-org-slug
  ([] (current-org-slug @app-state))
  ([data] (get-in data [router-key :org])))

(defn ^:export current-board-slug
  ([] (current-board-slug @app-state))
  ([data] (get-in data [router-key :board])))

(defn ^:export current-contributions-id
  ([] (current-contributions-id @app-state))
  ([data] (get-in data [router-key :contributions])))

(defn ^:export current-label-slug
  ([] (current-label-slug @app-state))
  ([data] (get-in data [router-key :label])))

(defn ^:export current-container-slug
  ([] (current-container-slug @app-state))
  ([data] (when-not (current-activity-id data)
            (or (current-contributions-id data)
                (current-board-slug data)))))

(defn ^:export current-sort-type
  ([] (current-sort-type @app-state))
  ([data] (get-in data [router-key :sort-type])))

(defn ^:export current-activity-id
  ([] (current-activity-id @app-state))
  ([data] (get-in data [router-key :activity])))

(defn ^:export current-entry-board-slug
  ([] (current-entry-board-slug @app-state))
  ([data] (get-in data [router-key :entry-board])))

(defn ^:export current-secure-activity-id
  ([] (current-secure-activity-id @app-state))
  ([data] (get-in data [router-key :secure-id])))

(defn ^:export current-comment-id
  ([] (current-comment-id @app-state))
  ([data] (get-in data [router-key :comment])))

(defn ^:export query-params
  ([] (query-params @app-state))
  ([data] (get-in data [router-key :query-params])))

(defn ^:export query-param
  ([k] (query-param @app-state k))
  ([data k] (get-in data [router-key :query-params k])))

(defun ^:export route-param
  ([k] (route-param @app-state k))
  ([data k :guard s-or-k?] (route-param data [k]))
  ([data ks :guard coll?] (get-in data (concat [router-key] ks))))

(defn ^:export route-set
  ([] (route-set @app-state))
  ([data] (route-param data :route)))

(defn ^:export invite-token
  ([] (invite-token @app-state))
  ([data] (query-param data :invite-token)))

(defn ^:export in-route?
  ([route-name] (in-route? (route-set @app-state) route-name))
  ([routes route-name]
  (when route-name
    (routes (keyword route-name)))))

;; Theme

(defn ^:export theme-map
  ([] (theme-map @app-state))
  ([data] (get-in data theme-key)))

;; Payments

(defn ^:export payments-data
  ([]
    (payments-data @app-state (current-org-slug)))
  ([org-slug]
   (payments-data @app-state org-slug))
  ([data org-slug]
   (get-in data (payments-key org-slug))))

;; Payments cached data

(defn ^:export payments-notify-cache-data
  ([]
    (payments-notify-cache-data @app-state (current-org-slug)))
  ([org-slug]
   (payments-notify-cache-data @app-state org-slug))
  ([data org-slug]
   (get-in data (payments-notify-cache-key org-slug))))

;; Data

(defn ^:export bot-access
  ([] (bot-access @app-state))
  ([data]
    (:bot-access data)))

(defn ^:export notifications-data
  ([] (notifications-data @app-state))
  ([data]
    (get-in data notifications-key)))

(defn ^:export teams-data-requested
  ([] (teams-data-requested @app-state))
  ([data] (:teams-data-requested data)))

(defn ^:export auth-settings
  "Get the Auth settings data"
  ([] (auth-settings @app-state))
  ([data] (get-in data auth-settings-key)))

(defn ^:export api-entry-point
  "Get the API entry point."
  ([] (api-entry-point @app-state))
  ([data] (get-in data api-entry-point-key)))

(defn ^:export current-user-data
  "Get the current logged in user info."
  ([] (current-user-data @app-state))
  ([data] (get-in data current-user-key)))

(defn ^:export orgs-data
  ([] (orgs-data @app-state))
  ([data] (get data orgs-key)))

(defn ^:export early-org-data
  ([] (early-org-data @app-state (current-org-slug)))
  ([data] (early-org-data data (current-org-slug)))
  ([data org-slug]
   (some #(when (= (:slug %) org-slug) %) (orgs-data data))))

(defn ^:export org-data
  "Get org data."
  ([]
    (org-data @app-state (current-org-slug)))
  ([data]
    (org-data data (current-org-slug)))
  ([data org-slug]
    (get-in data (org-data-key org-slug))))

(defn ^:export labels-data
  "Get the org labels"
  ([] (labels-data @app-state (current-org-slug)))
  ([data] (labels-data data (current-org-slug)))
  ([data org-slug]
   (get-in data (labels-key org-slug))))

(defn ^:export org-labels-data
  "Get the org labels"
  ([] (org-labels-data @app-state (current-org-slug)))
  ([data] (org-labels-data data (current-org-slug)))
  ([data org-slug]
   (get-in data (org-labels-key org-slug))))

(defn ^:export user-labels-data
  "Get the org labels but sorted by user usage."
  ([] (user-labels-data @app-state (current-org-slug)))
  ([data] (user-labels-data data (current-org-slug)))
  ([data org-slug]
   (get-in data (user-labels-key org-slug))))

(defn ^:export label-data
  "Get a label by uuid"
  ([] (label-data @app-state (current-org-slug) (current-label-slug)))
  ([label-uuid-or-slug] (label-data @app-state (current-org-slug) label-uuid-or-slug))
  ([data label-uuid-or-slug] (label-data data (current-org-slug) label-uuid-or-slug))
  ([data org-slug label-uuid-or-slug]
   (let [labels (org-labels-data data org-slug)]
     (find-label labels label-uuid-or-slug))))

(defn ^:export editing-label
  "Get the current editing label"
  ([] (editing-label @app-state))
  ([data] (get-in data editing-label-key)))

(defn ^:export posts-data
  "Get org all posts data."
  ([]
    (posts-data @app-state))
  ([data]
    (posts-data data (current-org-slug data)))
  ([data org-slug]
    (get-in data (posts-data-key org-slug))))

(defun ^:export org-board-data
  "Get board data from org data map: mostly used to edit the board infos."
  ([nil] nil)
  ([nil _] nil)
  ([_ nil] nil)
  ([]
   (org-board-data (org-data) (current-board-slug)))
  ([board-slug :guard s-or-k?]
   (org-board-data (org-data) board-slug))
  ([org-slug :guard #(or (keyword? %) (string? %)) board-slug :guard #(or (keyword? %) (string? %))]
   (org-board-data (org-data @app-state org-slug) board-slug))
  ([org-data :guard #(and (map? %) (:links %) (:boards %))
    board-slug]
   (when board-slug
    (let [board-slug-kw (keyword board-slug)]
      (some #(when (-> % :slug keyword (= board-slug-kw)) %) (:boards org-data)))))
  ([data :guard map? org-slug]
   (org-board-data (org-data data org-slug) (current-board-slug data)))
  ([data :guard map? org-slug board-slug]
   (org-board-data (org-data data org-slug) board-slug)))

(defun ^:export board-data
  "Get board data."
  ([]
    (board-data @app-state))
  ([data :guard map?]
    (board-data data (current-org-slug data) (current-board-slug data)))
  ([board-slug :guard #(or (keyword? %) (string? %))]
    (board-data @app-state (current-org-slug) board-slug))
  ([org-slug :guard #(or (keyword? %) (string? %)) board-slug :guard #(or (keyword? %) (string? %))]
    (board-data @app-state org-slug board-slug))
  ([data :guard map? org-slug :guard #(or (keyword? %) (string? %))]
    (board-data @app-state org-slug (current-board-slug data)))
  ([data org-slug board-slug]
    (when (and org-slug board-slug)
      (get-in data (board-data-key org-slug board-slug)))))

(defun ^:export contributions-data
  "Get contributions data"
  ([]
    (contributions-data @app-state))
  ([data :guard map?]
    (contributions-data data (current-org-slug data) (current-contributions-id data)))
  ([contributions-id :guard #(or (keyword? %) (string? %))]
    (contributions-data @app-state (current-org-slug) contributions-id))
  ([org-slug :guard #(or (keyword? %) (string? %)) contributions-id :guard #(or (keyword? %) (string? %))]
    (contributions-data @app-state org-slug contributions-id))
  ([data :guard map? org-slug :guard #(or (keyword? %) (string? %))]
    (contributions-data @app-state org-slug (current-contributions-id data)))
  ([data org-slug contributions-id]
    (when (and org-slug contributions-id)
      (get-in data (contributions-data-key org-slug contributions-id)))))

(defun ^:export label-entries-data
  "Get label entries data"
  ([]
   (label-entries-data @app-state))
  ([data :guard map?]
   (label-entries-data data (current-org-slug data) (current-label-slug data)))
  ([label-slug :guard #(or (keyword? %) (string? %))]
   (label-entries-data @app-state (current-org-slug) label-slug))
  ([org-slug :guard #(or (keyword? %) (string? %)) label-slug :guard #(or (keyword? %) (string? %))]
   (label-entries-data @app-state org-slug label-slug))
  ([data :guard map? org-slug :guard #(or (keyword? %) (string? %))]
   (label-entries-data @app-state org-slug (current-label-slug data)))
  ([data org-slug label-slug]
   (when (and org-slug label-slug)
     (get-in data (label-entries-data-key org-slug label-slug)))))

(defn ^:export editable-boards-data
  ([] (editable-boards-data @app-state (current-org-slug)))
  ([org-slug] (editable-boards-data @app-state org-slug))
  ([data org-slug]
   (get-in data (editable-boards-key org-slug))))

(defn ^:export private-boards-data
  ([] (private-boards-data @app-state (current-org-slug)))
  ([org-slug] (private-boards-data @app-state org-slug))
  ([data org-slug]
   (get-in data (private-boards-key org-slug))))

(defn ^:export public-boards-data
  ([] (public-boards-data @app-state (current-org-slug)))
  ([org-slug] (public-boards-data @app-state org-slug))
  ([data org-slug]
   (get-in data (public-boards-key org-slug))))

(defn ^:export container-data
  "Get container data."
  ([]
    (container-data @app-state (current-org-slug) (current-board-slug) (current-sort-type)))
  ([data]
    (container-data data (current-org-slug data) (current-board-slug data) (current-sort-type data)))
  ([data org-slug]
    (container-data data org-slug (current-board-slug data) (current-sort-type data)))
  ([data org-slug board-slug]
    (container-data data org-slug board-slug (current-sort-type data)))
  ([data org-slug board-slug sort-type]
    (get-in data (container-key org-slug board-slug sort-type))))

(defn ^:export current-container-data []
  (let [board-slug (current-board-slug)
        contributions-id (current-contributions-id)
        label-slug (current-label-slug)]
    (cond
      (seq label-slug)
      (label-entries-data @app-state (current-org-slug) label-slug)
      (seq contributions-id)
      (contributions-data @app-state (current-org-slug) contributions-id)
      (is-container? board-slug)
      (container-data @app-state (current-org-slug) board-slug)
      (= (keyword board-slug) :topic)
      nil
      :else
      (board-data @app-state (current-org-slug) board-slug))))

(defn ^:export all-posts-data
  "Get all-posts container data."
  ([]
    (all-posts-data (current-org-slug) recently-posted-sort @app-state))
  ([org-slug]
    (all-posts-data org-slug recently-posted-sort @app-state))
  ([org-slug data]
    (all-posts-data org-slug recently-posted-sort data))
  ([org-slug sort-type data]
    (container-data data org-slug :all-posts sort-type)))

(defn ^:export replies-data
  "Get replies container data."
  ([]
    (replies-data (current-org-slug) @app-state))
  ([org-slug]
    (replies-data org-slug @app-state))
  ([org-slug data]
    (container-data data org-slug :replies recent-activity-sort)))

(defn ^:export following-data
  "Get following container data."
  ([]
    (following-data (current-org-slug) @app-state))
  ([org-slug]
    (following-data org-slug @app-state))
  ([org-slug data]
    (container-data data org-slug :following recently-posted-sort)))

(defn ^:export unfollowing-data
  "Get following container data."
  ([]
    (unfollowing-data (current-org-slug) @app-state))
  ([org-slug]
    (unfollowing-data org-slug @app-state))
  ([org-slug data]
    (container-data data org-slug :unfollowing recently-posted-sort)))

(defn ^:export bookmarks-data
  "Get following container data."
  ([]
    (bookmarks-data (current-org-slug) @app-state))
  ([org-slug]
    (bookmarks-data org-slug @app-state))
  ([org-slug data]
    (container-data data org-slug :bookmarks recently-posted-sort)))

(defn ^:export filtered-posts-data
  ([]
    (filtered-posts-data @app-state (current-org-slug) (current-board-slug) (current-sort-type)))
  ([data]
    (filtered-posts-data data (current-org-slug data) (current-board-slug data) (current-sort-type data)))
  ([data org-slug]
    (filtered-posts-data data org-slug (current-board-slug data) (current-sort-type data)))
  ([data org-slug board-slug]
    (filtered-posts-data data org-slug board-slug (current-sort-type data)))
  ([data org-slug board-slug sort-type]
    (let [posts-data (get-in data (posts-data-key org-slug))]
     (get-container-posts data posts-data org-slug board-slug sort-type :posts-list)))
  ; ([data org-slug board-slug activity-id]
  ;   (let [org-data (org-data data org-slug)
  ;         all-boards-slug (map :slug (:boards org-data))
  ;         is-board? ((set all-boards-slug) board-slug)
  ;         posts-data (get-in data (posts-data-key org-slug))]
  ;    (if is-board?
  ;      (get-posts-for-board activity-id posts-data board-slug)
  ;      (let [container-key (container-key org-slug board-slug)
  ;            items-list (:posts-list (get-in data container-key))]
  ;       (zipmap items-list (map #(get posts-data %) items-list))))))
  )

(defn ^:export items-to-render-data
  ([]
    (items-to-render-data @app-state))
  ([data]
    (items-to-render-data data (current-org-slug data) (current-board-slug data) (current-sort-type data)))
  ([data org-slug]
    (items-to-render-data data org-slug (current-board-slug data) (current-sort-type data)))
  ([data org-slug board-slug]
    (items-to-render-data data org-slug board-slug (current-sort-type data)))
  ([data org-slug board-slug sort-type]
    (let [posts-data (get-in data (posts-data-key org-slug))]
     (get-container-posts data posts-data org-slug board-slug sort-type :items-to-render)))
  ; ([data org-slug board-slug activity-id]
  ;   (let [org-data (org-data data org-slug)
  ;         all-boards-slug (map :slug (:boards org-data))
  ;         is-board? ((set all-boards-slug) board-slug)
  ;         posts-data (get-in data (posts-data-key org-slug))]
  ;    (if is-board?
  ;      (get-posts-for-board activity-id posts-data board-slug)
  ;      (let [container-key (container-key org-slug board-slug)
  ;            items-list (:posts-list (get-in data container-key))]
  ;       (zipmap items-list (map #(get posts-data %) items-list))))))
  )

(defn ^:export draft-posts-data
  ([]
    (draft-posts-data @app-state (current-org-slug)))
  ([org-slug]
    (draft-posts-data @app-state org-slug))
  ([data org-slug]
    (filtered-posts-data data org-slug du/default-drafts-board-slug)))

(defn ^:export activity-data
  "Get activity data."
  ([]
    (activity-data (current-org-slug) (current-activity-id) @app-state))
  ([activity-id]
    (activity-data (current-org-slug) activity-id @app-state))
  ([org-slug activity-id]
    (activity-data org-slug activity-id @app-state))
  ([org-slug activity-id data]
    (let [activity-key (activity-key org-slug activity-id)]
      (get-in data activity-key))))
(def activity-data-get activity-data)
(def entry-data activity-data)

(defn ^:export pins-data
  "Get entry pins data."
  ([]
   (pins-data (current-org-slug) (current-activity-id) @app-state))
  ([entry-uuid]
   (pins-data (current-org-slug) entry-uuid @app-state))
  ([org-slug entry-uuid]
   (pins-data org-slug entry-uuid @app-state))
  ([org-slug entry-uuid data]
   (let [entry-pins-key (pins-key org-slug entry-uuid)]
     (get-in data entry-pins-key))))

(defn ^:export pin-data
  "Get entry pin data."
  ([pin-container-uuid]
   (pin-data (current-org-slug) (current-activity-id) pin-container-uuid @app-state))
  ([entry-uuid pin-container-uuid]
   (pin-data (current-org-slug) entry-uuid pin-container-uuid @app-state))
  ([org-slug entry-uuid pin-container-uuid]
   (pin-data org-slug entry-uuid pin-container-uuid @app-state))
  ([org-slug entry-uuid pin-container-uuid data]
   (let [entry-pin-key (pin-key org-slug entry-uuid pin-container-uuid)]
     (get-in data entry-pin-key))))

(defn ^:export entry-labels-data
  ([] (entry-labels-data @app-state (current-org-slug) (current-activity-id)))
  ([activity-id] (entry-labels-data @app-state (current-org-slug) activity-id))
  ([org-slug activity-id] (entry-labels-data @app-state org-slug activity-id))
  ([data org-slug activity-id]
   (get-in data (entry-labels-key org-slug activity-id))))

(defn ^:export entry-label-data
  ([label-uuid] (entry-label-data @app-state (current-org-slug) (current-activity-id) label-uuid))
  ([activity-id label-uuid] (entry-label-data @app-state (current-org-slug) activity-id label-uuid))
  ([org-slug activity-id label-uuid] (entry-label-data @app-state org-slug activity-id label-uuid))
  ([data org-slug activity-id label-uuid]
   (let [entry-labels (entry-labels-data data org-slug activity-id)]
     (some #(when (= (:uuid %) label-uuid) %) entry-labels))))

(defn ^:export foc-labels-picker
  ([] (foc-labels-picker @app-state))
  ([data] (get-in data foc-labels-picker-key)))

(defn ^:export secure-activity-data
  "Get secure activity data."
  ([]
    (secure-activity-data (current-org-slug) (current-secure-activity-id) @app-state))
  ([secure-id]
    (secure-activity-data (current-org-slug) secure-id @app-state))
  ([org-slug secure-id]
    (secure-activity-data org-slug secure-id @app-state))
  ([org-slug secure-id data]
    (let [activity-key (secure-activity-key org-slug secure-id)]
      (get-in data activity-key))))

(defn ^:export comments-data
  ([]
    (comments-data (current-org-slug) @app-state))
  ([org-slug]
    (comments-data org-slug @app-state))
  ([org-slug data]
    (get-in data (comments-key org-slug))))

(defn ^:export comment-data
  ([comment-uuid]
    (comment-data (current-org-slug) comment-uuid @app-state))
  ([org-slug comment-uuid]
    (comment-data org-slug comment-uuid @app-state))
  ([org-slug comment-uuid data]
    (let [all-entry-comments (get-in data (comments-key org-slug))
          all-comments (flatten (map :sorted-comments (vals all-entry-comments)))]
      (some #(when (= (:uuid %) comment-uuid) %) all-comments))))

(defn ^:export activity-comments-data
  ([]
    (activity-comments-data
     (current-org-slug)
     (current-activity-id)
     @app-state))
  ([activity-uuid]
    (activity-comments-data
     (current-org-slug)
     activity-uuid @app-state))
  ([org-slug activity-uuid]
    (activity-comments-data org-slug activity-uuid @app-state))
  ([org-slug activity-uuid data]
    (get-in data (activity-comments-key org-slug activity-uuid))))

(defn ^:export activity-sorted-comments-data
  ([]
    (activity-sorted-comments-data
     (current-org-slug)
     (current-activity-id)
     @app-state))
  ([activity-uuid]
    (activity-sorted-comments-data
     (current-org-slug)
     activity-uuid @app-state))
  ([org-slug activity-uuid]
    (activity-sorted-comments-data org-slug activity-uuid @app-state))
  ([org-slug activity-uuid data]
    (get-in data (activity-sorted-comments-key org-slug activity-uuid))))

(defn ^:export teams-data
  ([] (teams-data @app-state))
  ([data] (get-in data teams-data-key))
  ([data team-id] (some #(when (= (:team-id %) team-id) %) (get-in data teams-data-key))))

(defn ^:export team-data
  ([] (team-data (:team-id (org-data))))
  ([team-id] (team-data team-id @app-state))
  ([team-id data] (get-in data (team-data-key team-id))))

(defn ^:export team-roster
  ([] (team-roster (:team-id (org-data))))
  ([team-id] (team-roster team-id @app-state))
  ([team-id data] (get-in data (team-roster-key team-id))))

(defn ^:export team-channels
  ([] (team-channels (:team-id (org-data))))
  ([team-id] (team-channels team-id @app-state))
  ([team-id data] (get-in data (team-channels-key team-id))))

(defn ^:export active-users
  ([] (active-users (:slug (org-data)) @app-state))
  ([org-slug] (active-users org-slug @app-state))
  ([org-slug data] (get-in data (active-users-key org-slug))))

(defn ^:export follow-list
  ([] (follow-list (:slug (org-data)) @app-state))
  ([org-slug] (follow-list org-slug @app-state))
  ([org-slug data] (get-in data (follow-list-key org-slug))))

(defn ^:export followers-count
  ([] (followers-count (:slug (org-data)) @app-state))
  ([org-slug] (followers-count org-slug @app-state))
  ([org-slug data] (get-in data (followers-count-key org-slug))))

(defn ^:export followers-publishers-count
  ([] (followers-publishers-count (:slug (org-data)) @app-state))
  ([org-slug] (followers-publishers-count org-slug @app-state))
  ([org-slug data] (get-in data (followers-publishers-count-key org-slug))))

(defn ^:export followers-boards-count
  ([] (followers-boards-count (:slug (org-data)) @app-state))
  ([org-slug] (followers-boards-count org-slug @app-state))
  ([org-slug data] (get-in data (followers-boards-count-key org-slug))))

(defn ^:export follow-publishers-list
  ([] (follow-publishers-list (:slug (org-data)) @app-state))
  ([org-slug] (follow-publishers-list org-slug @app-state))
  ([org-slug data] (get-in data (follow-publishers-list-key org-slug))))

(defn ^:export follow-boards-list
  ([] (follow-boards-list (:slug (org-data)) @app-state))
  ([org-slug] (follow-boards-list org-slug @app-state))
  ([org-slug data] (get-in data (follow-boards-list-key org-slug))))

(defn ^:export unfollow-board-uuids
  ([] (unfollow-board-uuids (:slug (org-data)) @app-state))
  ([org-slug] (unfollow-board-uuids org-slug @app-state))
  ([org-slug data] (get-in data (unfollow-board-uuids-key org-slug))))

(defn ^:export uploading-video-data
  ([video-id] (uploading-video-data (current-org-slug) video-id @app-state))
  ([org-slug video-id] (uploading-video-data org-slug video-id @app-state))
  ([org-slug video-id data]
    (let [uv-key (uploading-video-key org-slug video-id)]
      (get-in data uv-key))))

;; User notifications

(defn ^:export user-notifications-data
  "Get user notifications data"
  ([]
    (user-notifications-data (current-org-slug) @app-state))
  ([org-slug]
    (user-notifications-data org-slug @app-state))
  ([org-slug data]
    (get-in data (user-notifications-key org-slug))))

;; Change related

(defn ^:export change-data
  "Get change data."
  ([]
    (change-data @app-state))
  ([data]
    (change-data data (current-org-slug)))
  ([data org-slug]
    (get-in data (change-data-key org-slug))))

(defun ^:export activity-read-data
  "Get the read counts of all the items."
  ([]
    (activity-read-data @app-state))
  ([data :guard map?]
    (get-in data activities-read-key))
  ([item-ids :guard seq?]
    (activity-read-data item-ids @app-state))
  ([item-ids :guard seq? data :guard map?]
    (let [all-activities-read (get-in data activities-read-key)]
      (select-keys all-activities-read item-ids)))
  ([item-id :guard string?]
    (activity-read-data item-id @app-state))
  ([item-id :guard string? data :guard map?]
    (let [all-activities-read (get-in data activities-read-key)]
      (get all-activities-read item-id))))

;; Seen

(defn ^:export org-seens-data
  ([] (org-seens-data @app-state (current-org-slug)))
  ([org-slug] (org-seens-data @app-state org-slug))
  ([data org-slug] (get-in data (org-seens-key org-slug))))

; (defn ^:export container-seen-data
;  ([container-id] (container-seen-data @app-state (current-org-slug) container-id))
;  ([org-slug container-id] (container-seen-data @app-state org-slug container-id))
;  ([data org-slug container-id] (get-in data (container-seen-key org-slug container-id))))

;; Cmail

(defn ^:export cmail-data
  ([] (cmail-data @app-state))
  ([data] (get-in data cmail-data-key)))

(defn ^:export cmail-state
  ([] (cmail-state @app-state))
  ([data] (get-in data cmail-state-key)))

;; Reminders

(defn ^:export reminders-data
  ([] (reminders-data (current-org-slug) @app-state))
  ([org-slug] (reminders-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminders-data-key org-slug))))

(defn ^:export reminders-roster-data
  ([] (reminders-roster-data (current-org-slug) @app-state))
  ([org-slug] (reminders-roster-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminders-roster-key org-slug))))

(defn ^:export reminder-edit-data
  ([] (reminder-edit-data (current-org-slug) @app-state))
  ([org-slug] (reminder-edit-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminder-edit-key org-slug))))

;; Expo

(defn ^:export expo-deep-link-origin
  ([] (expo-deep-link-origin @app-state))
  ([data] (get-in data expo-deep-link-origin-key)))

(defn ^:export expo-app-version
  ([] (expo-app-version @app-state))
  ([data] (get-in data expo-app-version-key)))

(defn ^:export expo-push-token
  ([] (expo-push-token @app-state))
  ([data] (get-in data expo-push-token-key)))

;; Utility externs
(set! (.-OCWebUtils js/window) #js {:deref deref
                                    :keyword keyword
                                    :count count
                                    :get get
                                    :filter filter
                                    :map map
                                    :clj__GT_js clj->js
                                    :js__GT_clj js->clj})

(defn ^:export js-debug [key]
  (cond (instance? js/Array key)
        (let [kkeys (mapv keyword
                          (js->clj key))]
          (get-in @app-state kkeys))
        (sequential? key)
        (get-in @app-state key)
        :else
        (get @app-state (keyword key))))
