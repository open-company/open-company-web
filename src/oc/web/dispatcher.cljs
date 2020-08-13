(ns oc.web.dispatcher
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [defun.core :refer (defun)]
            [taoensso.timbre :as timbre]
            [clojure.string :as s]
            [cljs-flux.dispatcher :as flux]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.utils.drafts :as du]
            [oc.shared.useragent :as ua]))

(defonce ^{:export true} app-state (atom {:loading false
                                          :show-login-overlay false}))

(def recent-activity-sort :recent-activity)
(def recently-posted-sort :recently-posted)

(def default-foc-layout :expanded)
(def other-foc-layout :collapsed)

;; Pre-declare some routing functions

(declare current-org-slug)
(declare current-board-slug)
(declare current-contributions-id)
(declare current-sort-type)
(declare current-activity-id)
(declare current-secure-activity-id)
(declare current-comment-id)
(declare query-params)
(declare query-param)

;; Data key paths

(def router-key :router-path)

(def checkout-result-key :checkout-success-result)
(def checkout-update-plan-key :checkout-update-plan)

(def expo-key [:expo])

(def expo-deep-link-origin-key (vec (conj expo-key :deep-link-origin)))
(def expo-app-version-key (vec (conj expo-key :app-version)))
(def expo-push-token-key (vec (conj expo-key :push-token)))

(def show-invite-box-key :show-invite-box)

(def api-entry-point-key [:api-entry-point])

(def auth-settings-key [:auth-settings])

(def notifications-key [:notifications-data])
(def show-login-overlay-key :show-login-overlay)

(def orgs-key :orgs)

(defn org-key [org-slug]
  [(keyword org-slug)])

(defn org-data-key [org-slug]
  (vec (conj (org-key org-slug) :org-data)))

(defn boards-key [org-slug]
  (vec (conj (org-key org-slug) :boards)))

(defn payments-key [org-slug]
  (vec (conj (org-key org-slug) :payments)))

(defn posts-data-key [org-slug]
  (vec (conj (org-key org-slug) :posts)))

(defn board-key 
  ([org-slug board-slug sort-type]
    (if sort-type
      (vec (concat (boards-key org-slug) [(keyword board-slug) (keyword sort-type)]))
      (vec (concat (boards-key org-slug) [(keyword board-slug)]))))
  ([org-slug board-slug]
   (vec (concat (boards-key org-slug) [(keyword board-slug) recently-posted-sort]))))

(defn board-data-key
  ([org-slug board-slug]
   (board-data-key org-slug board-slug recently-posted-sort))
  ([org-slug board-slug sort-type]
    (conj (board-key org-slug board-slug sort-type) :board-data)))

(defn contributions-list-key [org-slug]
  (vec (conj (org-key org-slug) :contribs)))

(defn contributions-key
  ([org-slug author-uuid]
   (contributions-key org-slug author-uuid recently-posted-sort))
  ([org-slug author-uuid sort-type]
   (if sort-type
     (vec (concat (contributions-list-key org-slug) [(keyword author-uuid) (keyword sort-type)]))
     (vec (conj (contributions-list-key org-slug) (keyword author-uuid))))))

(defn contributions-data-key
  ([org-slug slug-or-uuid sort-type]
   (conj (contributions-key org-slug slug-or-uuid sort-type) :contrib-data))
  ([org-slug slug-or-uuid]
   (conj (contributions-key org-slug slug-or-uuid) :contrib-data)))

(defn containers-key [org-slug]
  (vec (conj (org-key org-slug) :container-data)))

(defn container-key
  ([org-slug items-filter]
   (container-key org-slug items-filter recently-posted-sort))
  ([org-slug items-filter sort-type]
   (cond
     sort-type
     (vec (conj (containers-key org-slug) (keyword items-filter) (keyword sort-type)))
     :else
     (vec (conj (containers-key org-slug) (keyword items-filter))))))

(defn badges-key [org-slug]
  (vec (conj (org-key org-slug) :badges)))

(defn replies-badge-key [org-slug]
  (vec (conj (badges-key org-slug) :replies)))

(defn following-badge-key [org-slug]
  (vec (conj (badges-key org-slug) :following)))

(defn secure-activity-key [org-slug secure-id]
  (vec (concat (org-key org-slug) [:secure-activities secure-id])))

(defn activity-key [org-slug activity-uuid]
  (let [posts-key (posts-data-key org-slug)]
    (vec (concat posts-key [activity-uuid]))))

(defn activity-last-read-at-key [org-slug activity-uuid]
  (vec (conj (activity-key org-slug activity-uuid) :last-read-at)))

(defn add-comment-key [org-slug]
  (vec (concat (org-key org-slug) [:add-comment-data])))

(defn add-comment-string-key
  ([activity-uuid] (add-comment-string-key activity-uuid nil nil))
  ([activity-uuid parent-comment-uuid] (add-comment-string-key activity-uuid parent-comment-uuid nil))
  ([activity-uuid parent-comment-uuid comment-uuid]
   (str activity-uuid
     (when parent-comment-uuid
       (str "-" parent-comment-uuid))
     (when comment-uuid
       (str "-" comment-uuid)))))

(def add-comment-force-update-root-key :add-comment-force-update)

(defn add-comment-force-update-key [add-comment-string-key]
  (vec (concat [add-comment-force-update-root-key] [add-comment-string-key])))

(defn add-comment-activity-key [org-slug activity-uuid]
  (vec (concat (add-comment-key org-slug) [activity-uuid])))

(defn comment-reply-to-key [org-slug]
  (vec (conj (org-key org-slug) :comment-reply-to-key)))

(defn comments-key [org-slug]
  (vec (conj (org-key org-slug) :comments)))

(defn activity-comments-key [org-slug activity-uuid]
  (vec (conj (comments-key org-slug) activity-uuid)))

(def sorted-comments-key :sorted-comments)

(defn activity-sorted-comments-key [org-slug activity-uuid]
  (vec (concat (comments-key org-slug) [activity-uuid sorted-comments-key])))

(def teams-data-key [:teams-data :teams])

(defn team-data-key [team-id]
  [:teams-data team-id :data])

(defn team-roster-key [team-id]
  [:teams-data team-id :roster])

(defn team-channels-key [team-id]
  [:teams-data team-id :channels])

(defn active-users-key [org-slug]
  (vec (conj (org-key org-slug) :active-users)))

(defn follow-list-key [org-slug]
  (vec (conj (org-key org-slug) :follow-list)))

(defn follow-list-last-added-key [org-slug]
  (vec (conj (org-key org-slug) :follow-list-last-added)))

(defn follow-publishers-list-key [org-slug]
  (vec (conj (follow-list-key org-slug) :publisher-uuids)))

(defn follow-boards-list-key [org-slug]
  (vec (conj (follow-list-key org-slug) :follow-boards-list)))

(defn unfollow-board-uuids-key [org-slug]
  (vec (conj (follow-list-key org-slug) :unfollow-board-uuids)))

(defn followers-count-key [org-slug]
  (vec (conj (org-key org-slug) :followers-count)))

(defn followers-publishers-count-key [org-slug]
  (vec (conj (followers-count-key org-slug) :publishers)))

(defn followers-boards-count-key [org-slug]
  (vec (conj (followers-count-key org-slug) :boards)))

(defn mention-users-key [org-slug]
  (vec (conj (org-key org-slug) :mention-users)))

(defn users-info-hover-key [org-slug]
  (vec (conj (org-key org-slug) :users-info-hover)))

(defn uploading-video-key [org-slug video-id]
  (vec (concat (org-key org-slug) [:uploading-videos video-id])))

(defn current-board-key
  "Find the board key for db based on the current path."
  []
  (let [org-slug (current-org-slug)
        board-slug (current-board-slug)]
     (board-data-key org-slug board-slug)))

(def can-compose-key :can-copmose?)

(defn org-can-compose-key
  "Key for a boolean value: true if the user has at least one board
   he can publish updates in."
  [org-slug]
  (vec (conj (org-data-key org-slug) can-compose-key)))

;; User notifications

(defn user-notifications-key [org-slug]
  (vec (conj (org-key org-slug) :user-notifications)))

(defn grouped-user-notifications-key [org-slug]
  (vec (concat (org-key org-slug) [:user-notifications :grouped])))

(defn sorted-user-notifications-key [org-slug]
  (vec (concat (org-key org-slug) [:user-notifications :sorted])))

;; Reminders

(defn reminders-key [org-slug]
  (vec (conj (org-key org-slug) :reminders)))

(defn reminders-data-key [org-slug]
  (vec (conj (reminders-key org-slug) :reminders-list)))

(defn reminders-roster-key [org-slug]
  (vec (conj (reminders-key org-slug) :reminders-roster)))

(defn reminder-edit-key [org-slug]
  (vec (conj (reminders-key org-slug) :reminder-edit)))

;; Change related keys

(defn change-data-key [org-slug]
  (vec (conj (org-key org-slug) :change-data)))

(def activities-read-key
  [:activities-read])

;; Seen

(defn org-seens-key [org-slug]
  (vec (conj (org-key org-slug) :container-seen)))

; (defn container-seen-key [org-slug container-id]
;   (vec (conj (org-seens-key org-slug) (keyword container-id))))

;; Cmail keys

(def cmail-state-key [:cmail-state])

(def cmail-data-key [:cmail-data])

;; Boards helpers

(defn get-posts-for-board [posts-data board-slug]
  (let [posts-list (vals posts-data)
        filter-fn (if (= board-slug du/default-drafts-board-slug)
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

(defn- get-container-posts [base posts-data org-slug container-slug sort-type items-key]
  (let [cnt-key (cond
                  (is-container? container-slug)
                  (container-key org-slug container-slug sort-type)
                  (seq (current-contributions-id))
                  (contributions-data-key org-slug container-slug)
                  :else
                  (board-data-key org-slug container-slug))
        container-data (get-in base cnt-key)
        posts-list (get container-data items-key)
        container-posts (map (fn [entry]
                               (if (and (map? entry) (= (:resource-type entry) :entry))
                                 ;; Make sure the local map is merged as last value
                                 ;; since the kept value relates directly to the container
                                 (merge (get posts-data (:uuid entry)) entry)
                                 entry))
                         posts-list)
        items (if (= container-slug du/default-drafts-board-slug)
                (filter (comp not :published?) container-posts)
                container-posts)]
    (vec items)))

(def ui-theme-key [:ui-theme])

;; Functions needed by derivatives

(declare org-data)
(declare board-data)
(declare contributions-data)
(declare editable-boards-data)
(declare activity-data)
(declare secure-activity-data)
(declare activity-read-data)
(declare activity-data-get)

;; Derived Data ================================================================

(defn drv-spec [db]
  {:base                [[] db]
   :route               [[:base] (fn [base] (get base router-key))]
   :orgs                [[:base] (fn [base] (get base orgs-key))]
   :org-slug            [[:route] (fn [route] (:org route))]
   :contributions-id    [[:route] (fn [route] (:contributions route))]
   :board-slug          [[:route] (fn [route] (:board route))]
   :entry-board-slug    [[:route] (fn [route] (:entry-board route))]
   :sort-type           [[:route] (fn [route] (:sort-type route))]
   :activity-uuid       [[:route] (fn [route] (:activity route))]
   :secure-id           [[:route] (fn [route] (:secure-id route))]
   :loading             [[:base] (fn [base] (:loading base))]
   :signup-with-email   [[:base] (fn [base] (:signup-with-email base))]
   :query-params        [[:route] (fn [route] (:query-params route))]
   :teams-data          [[:base] (fn [base] (get-in base teams-data-key))]
   :auth-settings       [[:base] (fn [base] (get-in base auth-settings-key))]
   :entry-save-on-exit  [[:base] (fn [base] (:entry-save-on-exit base))]
   :orgs-dropdown-visible [[:base] (fn [base] (:orgs-dropdown-visible base))]
   :add-comment-focus   [[:base] (fn [base] (:add-comment-focus base))]
   :nux                 [[:base] (fn [base] (:nux base))]
   :notifications-data  [[:base] (fn [base] (get-in base notifications-key))]
   :login-with-email    [[:base] (fn [base] (:login-with-email base))]
   :login-with-email-error [[:base] (fn [base] (:login-with-email-error base))]
   :panel-stack         [[:base] (fn [base] (:panel-stack base))]
   :current-panel       [[:panel-stack] (fn [panel-stack] (last panel-stack))]
   :mobile-navigation-sidebar [[:base] (fn [base] (:mobile-navigation-sidebar base))]
   :expand-image-src    [[:base] (fn [base] (:expand-image-src base))]
   :attachment-uploading [[:base] (fn [base] (:attachment-uploading base))]
   :add-comment-force-update [[:base] (fn [base] (get base add-comment-force-update-root-key))]
   :mobile-swipe-menu  [[:base] (fn [base] (:mobile-swipe-menu base))]
   checkout-result-key [[:base] (fn [base] (get base checkout-result-key))]
   checkout-update-plan-key [[:base] (fn [base] (get base checkout-update-plan-key))]
   :expo                [[:base] (fn [base] (get-in base expo-key))]
   :expo-deep-link-origin [[:base] (fn [base] (get-in base expo-deep-link-origin-key))]
   :expo-app-version    [[:base] (fn [base] (get-in base expo-app-version-key))]
   :invite-add-slack-checked [[:base] (fn [base] (:invite-add-slack-checked base))]
   :add-comment-data    [[:base :org-slug] (fn [base org-slug]
                          (get-in base (add-comment-key org-slug)))]
   :email-verification  [[:base :auth-settings]
                          (fn [base auth-settings]
                            {:auth-settings auth-settings
                             :error (:email-verification-error base)
                             :success (:email-verification-success base)})]
   :jwt                 [[:base] (fn [base] (:jwt base))]
   :id-token            [[:base] (fn [base] (:id-token base))]
   :current-user-data   [[:base]
                          (fn [base]
                            (if (and (not (:jwt base))
                                     (:id-token base)
                                     (current-secure-activity-id base))
                              (select-keys (:id-token base) [:user-id :avatar-url :first-name :last-name :name])
                              (:current-user-data base)))]
   :payments        [[:base :org-slug] (fn [base org-slug] (get-in base (payments-key org-slug)))]
   :show-login-overlay  [[:base] (fn [base] (:show-login-overlay base))]
   :site-menu-open      [[:base] (fn [base] (:site-menu-open base))]
   :ap-loading          [[:base] (fn [base] (:ap-loading base))]
   :edit-reminder       [[:base] (fn [base] (:edit-reminder base))]
   :drafts-data         [[:base :org-slug]
                          (fn [base org-slug]
                            (get-in base (board-data-key org-slug :drafts)))]
   :bookmarks-data     [[:base :org-slug]
                          (fn [base org-slug]
                            (get-in base (container-key org-slug :bookmarks)))]
   :following-data     [[:base :org-slug]
                          (fn [base org-slug]
                            (get-in base (container-key org-slug :following)))]
   :unfollowing-data   [[:base :org-slug]
                          (fn [base org-slug]
                            (get-in base (container-key org-slug :unfollowing)))]
   :org-data            [[:base :org-slug]
                          (fn [base org-slug]
                            (when org-slug
                              (org-data base org-slug)))]
   :team-data           [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-data-key (:team-id org-data)))))]
   :team-roster         [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-roster-key (:team-id org-data)))))]
   :invite-users        [[:base] (fn [base] (:invite-users base))]
   :invite-data         [[:base :team-data :current-user-data :team-roster :invite-users]
                          (fn [base team-data current-user-data team-roster invite-users]
                            {:team-data team-data
                             :invite-users invite-users
                             :current-user-data current-user-data
                             :team-roster team-roster})]
   :org-settings-team-management
                        [[:base :query-params :org-data :team-data :auth-settings]
                          (fn [base query-params org-data team-data]
                            {:um-domain-invite (:um-domain-invite base)
                             :add-email-domain-team-error (:add-email-domain-team-error base)
                             :team-data team-data
                             :query-params query-params})]
   :posts-data          [[:base :org-slug]
                         (fn [base org-slug]
                           (when (and base org-slug)
                             (get-in base (posts-data-key org-slug))))]
   :filtered-posts      [[:base :org-data :posts-data :route]
                         (fn [base org-data posts-data route]
                           (let [container-slug (or (:contributions route) (:board route))]
                             (when (and base org-data posts-data route container-slug)
                               (get-container-posts base posts-data (:slug org-data) container-slug (:sort-type route) :posts-list))))]
   :items-to-render     [[:base :org-data :posts-data :route]
                         (fn [base org-data posts-data route]
                           (let [container-slug (or (:contributions route) (:board route))]
                             (when (and base org-data container-slug posts-data)
                               (get-container-posts base posts-data (:slug org-data) container-slug (:sort-type route) :items-to-render))))]
   :team-channels       [[:base :org-data]
                          (fn [base org-data]
                            (when org-data
                              (get-in base (team-channels-key (:team-id org-data)))))]
   :change-data         [[:base :org-slug]
                          (fn [base org-slug]
                            (when (and base org-slug)
                              (get-in base (change-data-key org-slug))))]
   :editable-boards     [[:base :org-slug]
                          (fn [base org-slug]
                           (editable-boards-data base org-slug))]
   :container-data      [[:base :org-slug :board-slug :contributions-id :activity-uuid :sort-type]
                         (fn [base org-slug board-slug contributions-id activity-uuid sort-type]
                           (when (and org-slug
                                      (or board-slug
                                          contributions-id))
                             (let [is-contributions? (seq contributions-id)
                                   cnt-key (cond is-contributions?
                                                 (contributions-data-key org-slug contributions-id)
                                                 (is-container? board-slug)
                                                 (container-key org-slug board-slug sort-type)
                                                 :else
                                                 (board-data-key org-slug board-slug))]
                               (get-in base cnt-key))))]
   :contributions-data    [[:base :org-slug :contributions-id]
                         (fn [base org-slug contributions-id]
                           (when (and org-slug contributions-id)
                             (contributions-data org-slug contributions-id)))]
   :board-data          [[:base :org-slug :board-slug]
                          (fn [base org-slug board-slug]
                            (board-data base org-slug board-slug))]
   :contributions-user-data [[:base :active-users :contributions-id]
                             (fn [base active-users contributions-id]
                              (when (and active-users contributions-id)
                                (get active-users contributions-id)))]
   :activity-data       [[:base :org-slug :activity-uuid]
                          (fn [base org-slug activity-uuid]
                            (activity-data org-slug activity-uuid base))]
   :secure-activity-data [[:base :org-slug :secure-id]
                          (fn [base org-slug secure-id]
                            {:activity-data (secure-activity-data org-slug secure-id base)
                             :is-showing-alert (boolean (:alert-modal base))})]
   :comments-data       [[:base :org-slug]
                         (fn [base org-slug]
                           (get-in base (comments-key org-slug)))]
   :edit-user-profile   [[:base]
                          (fn [base]
                            {:user-data (:edit-user-profile base)
                             :error (:edit-user-profile-failed base)})]
   :edit-user-profile-avatar [[:base] (fn [base] (:edit-user-profile-avatar base))]
   :entry-editing       [[:base]
                          (fn [base]
                            (:entry-editing base))]
   :section-editing     [[:base]
                          (fn [base]
                            (:section-editing base))]
   :org-editing         [[:base]
                          (fn [base]
                            (:org-editing base))]
   :org-avatar-editing  [[:base]
                          (fn [base] (:org-avatar-editing base))]
   :alert-modal         [[:base]
                          (fn [base]
                            (:alert-modal base))]
   :activity-share        [[:base] (fn [base] (:activity-share base))]
   :activity-share-medium [[:base] (fn [base] (:activity-share-medium base))]
   :activity-share-container  [[:base] (fn [base] (:activity-share-container base))]
   :activity-shared-data  [[:base] (fn [base] (:activity-shared-data base))]
   :activities-read       [[:base] (fn [base] (get-in base activities-read-key))]
   :navbar-data         [[:base :org-data :board-data :contributions-user-data :org-slug :board-slug :contributions-id :activity-uuid :current-user-data]
                          (fn [base org-data board-data contributions-user-data org-slug board-slug contributions-id activity-uuid current-user-data]
                            (let [navbar-data (select-keys base [:show-login-overlay
                                                                 :orgs-dropdown-visible
                                                                 :panel-stack
                                                                 :search-active
                                                                 :show-whats-new-green-dot])]
                              (-> navbar-data
                                (assoc :org-data org-data)
                                (assoc :board-data board-data)
                                (assoc :contributions-user-data contributions-user-data)
                                (assoc :current-org-slug org-slug)
                                (assoc :current-board-slug board-slug)
                                (assoc :current-contributions-id contributions-id)
                                (assoc :current-activity-id activity-uuid)
                                (assoc :current-user-data current-user-data))))]
   :confirm-invitation    [[:base :route :auth-settings :jwt]
                            (fn [base route auth-settings jwt]
                              {:invitation-confirmed (:email-confirmed base)
                               :invitation-error (and (contains? base :email-confirmed)
                                                      (not (:email-confirmed base)))
                               :auth-settings auth-settings
                               :token (:token (:query-params route))
                               :jwt jwt})]
   :team-invite           [[:base :route :auth-settings :jwt]
                            (fn [base route auth-settings jwt]
                              {:auth-settings auth-settings
                               :invite-token (:invite-token (:query-params route))
                               :jwt jwt})]
   :collect-password      [[:base :jwt]
                            (fn [base jwt]
                              {:collect-pswd (:collect-pswd base)
                               :collect-pswd-error (:collect-password-error base)
                               :jwt jwt})]
   :password-reset        [[:base :auth-settings]
                            (fn [base auth-settings]
                              {:auth-settings auth-settings
                               :error (:collect-pswd-error base)})]
   :media-input           [[:base]
                            (fn [base]
                              (:media-input base))]
   :search-active         [[:base] (fn [base] (:search-active base))]
   :search-results        [[:base] (fn [base] (:search-results base))]
   :user-notifications    [[:base :org-slug]
                            (fn [base org-slug]
                              (when (and base org-slug)
                                (get-in base (user-notifications-key org-slug))))]
   :sorted-user-notifications [[:base :org-slug]
                               (fn [base org-slug]
                                 (when (and base org-slug)
                                   (get-in base (sorted-user-notifications-key org-slug))))]
   :grouped-user-notifications [[:base :org-slug]
                                (fn [base org-slug]
                                  (when (and base org-slug)
                                    (get-in base (grouped-user-notifications-key org-slug))))]
   :replies-badge        [[:base :org-slug]
                           (fn [base org-slug]
                             (get-in base (replies-badge-key org-slug)))]
   :following-badge           [[:base :org-slug]
                               (fn [base org-slug]
                                 (get-in base (following-badge-key org-slug)))]
   :unread-notifications  [[:user-notifications]
                           (fn [notifications]
                             (filter :unread notifications))]
   :unread-notifications-count [[:unread-notifications]
                                (fn [notifications]
                                  (let [ncount (count notifications)]
                                    (timbre/info "Unread notification count updated: " ncount)
                                    (when ua/desktop-app?
                                      (js/window.OCCarrotDesktop.setBadgeCount ncount))
                                    ncount))]
   :user-responded-to-push-permission? [[:base] (fn [base]
                                                  (boolean (get-in base expo-push-token-key)))]
   :wrt-show              [[:base] (fn [base] (:wrt-show base))]
   :wrt-read-data         [[:base :panel-stack]
                            (fn [base panel-stack]
                              (when (and panel-stack
                                         (seq (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                                (when-let* [wrt-panel (name (first (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                                            wrt-uuid (subs wrt-panel 4 (count wrt-panel))]
                                  (activity-read-data wrt-uuid base))))]
   :wrt-activity-data     [[:base :org-slug :panel-stack]
                            (fn [base org-slug panel-stack]
                              (when (and panel-stack
                                         (seq (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                                (when-let* [wrt-panel (name (first (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                                            wrt-uuid (subs wrt-panel 4 (count wrt-panel))]

                                  (activity-data-get org-slug wrt-uuid base))))]
   :user-info-data        [[:base :active-users :panel-stack]
                            (fn [base active-users panel-stack]
                              (when (and panel-stack
                                         (seq (filter #(s/starts-with? (name %) "user-info-") panel-stack)))
                                (when-let* [user-info-panel (name (first (filter #(s/starts-with? (name %) "user-info-") panel-stack)))
                                            user-id (subs user-info-panel (count "user-info-") (count user-info-panel))]
                                  (get active-users user-id))))]
   :org-dashboard-data    [[:base :orgs :org-data :contributions-data :container-data :posts-data :activity-data
                            :entry-editing :jwt :wrt-show :loading :payments :search-active :user-info-data :current-user-data
                            :active-users :follow-publishers-list :follow-boards-list :org-slug :board-slug :contributions-id :activity-uuid]
                            (fn [base orgs org-data contributions-data container-data posts-data activity-data
                                 entry-editing jwt wrt-show loading payments search-active user-info-data current-user-data
                                 active-users follow-publishers-list follow-boards-list org-slug board-slug contributions-id activity-uuid]
                              {:jwt-data jwt
                               :orgs orgs
                               :org-data org-data
                               :payments-data payments
                               :container-data container-data
                               :current-org-slug org-slug
                               :current-board-slug board-slug
                               :current-contributions-id contributions-id
                               :current-activity-id activity-uuid
                               :contributions-data contributions-data
                               :initial-section-editing (:initial-section-editing base)
                               :posts-data posts-data
                               :panel-stack (:panel-stack base)
                               :is-sharing-activity (boolean (:activity-share base))
                               :is-showing-alert (boolean (:alert-modal base))
                               :entry-edit-dissmissing (:entry-edit-dissmissing base)
                               :media-input (:media-input base)
                               :show-section-add-cb (:show-section-add-cb base)
                               :entry-editing-board-slug (:board-slug entry-editing)
                               :activity-share-container (:activity-share-container base)
                               :cmail-state (get-in base cmail-state-key)
                               :force-login-wall (:force-login-wall base)
                               :app-loading loading
                               :search-active search-active
                               :user-info-data user-info-data
                               :current-user-data current-user-data
                               :active-users active-users
                               :follow-publishers-list follow-publishers-list
                               :follow-boards-list follow-boards-list})]
   :show-add-post-tooltip      [[:nux] (fn [nux] (:show-add-post-tooltip nux))]
   :show-edit-tooltip          [[:nux] (fn [nux] (:show-edit-tooltip nux))]
   :show-post-added-tooltip    [[:nux] (fn [nux] (:show-post-added-tooltip nux))]
   :show-invite-people-tooltip [[:nux] (fn [nux] (:show-invite-people-tooltip nux))]
   :nux-user-type              [[:nux] (fn [nux] (:user-type nux))]
   ;; Cmail
   :cmail-state           [[:base] (fn [base] (get-in base cmail-state-key))]
   :cmail-data            [[:base] (fn [base] (get-in base cmail-data-key))]
   :reminders-data        [[:base :org-slug] (fn [base org-slug]
                                    (get-in base (reminders-data-key org-slug)))]
   :reminders-roster      [[:base :org-slug] (fn [base org-slug]
                                    (get-in base (reminders-roster-key org-slug)))]
   :reminder-edit         [[:base :org-slug] (fn [base org-slug]
                                    (get-in base (reminder-edit-key org-slug)))]
   :foc-layout            [[:base] (fn [base] (:foc-layout base))]
   :ui-theme              [[:base] (fn [base] (get-in base ui-theme-key))]
   :users-info-hover      [[:base :org-slug] (fn [base org-slug] (get-in base (users-info-hover-key org-slug)))]
   :active-users          [[:base :org-slug] (fn [base org-slug] (get-in base (active-users-key org-slug)))]
   :mention-users         [[:base :org-slug] (fn [base org-slug] (get-in base (mention-users-key org-slug)))]
   :follow-list           [[:base :org-slug] (fn [base org-slug] (get-in base (follow-list-key org-slug)))]
   :follow-list-last-added [[:base :org-slug] (fn [base org-slug] (get-in base (follow-list-last-added-key org-slug)))]
   :followers-count       [[:base :org-slug] (fn [base org-slug] (get-in base (followers-count-key org-slug)))]
   :followers-publishers-count [[:base :org-slug] (fn [base org-slug] (get-in base (followers-publishers-count-key org-slug)))]
   :followers-boards-count [[:base :org-slug] (fn [base org-slug] (get-in base (followers-boards-count-key org-slug)))]
   :follow-publishers-list [[:base :org-slug] (fn [base org-slug] (get-in base (follow-publishers-list-key org-slug)))]
   :follow-boards-list    [[:base :org-slug] (fn [base org-slug] (get-in base (follow-boards-list-key org-slug)))]
   :comment-reply-to      [[:base :org-slug] (fn [base org-slug] (get-in base (comment-reply-to-key org-slug)))]
   :show-invite-box      [[:base] (fn [base] (get base show-invite-box-key))]
   :can-compose         [[:org-data] (fn [org-data] (get org-data can-compose-key))]
   })

;; Action Loop =================================================================

(defmulti action (fn [db [action-type & _]]
                   (when (and (not= action-type :input)
                              (not= action-type :update)
                              (not= action-type :entry-toggle-save-on-exit))
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

;; Path components retrieve

(defn route
  ([] (route @app-state))
  ([data] (get-in data [router-key])))

(defn current-org-slug
  ([] (current-org-slug @app-state))
  ([data] (get-in data [router-key :org])))

(defn current-board-slug
  ([] (current-board-slug @app-state))
  ([data] (get-in data [router-key :board])))

(defn current-contributions-id
  ([] (current-contributions-id @app-state))
  ([data] (get-in data [router-key :contributions])))

(defn current-sort-type
  ([] (current-sort-type @app-state))
  ([data] (get-in data [router-key :sort-type])))

(defn current-activity-id
  ([] (current-activity-id @app-state))
  ([data] (get-in data [router-key :activity])))

(defn current-entry-board-slug
  ([] (current-entry-board-slug @app-state))
  ([data] (get-in data [router-key :entry-board])))

(defn current-secure-activity-id
  ([] (current-secure-activity-id @app-state))
  ([data] (get-in data [router-key :secure-id])))

(defn current-comment-id
  ([] (current-comment-id @app-state))
  ([data] (get-in data [router-key :comment])))

(defn query-params
  ([] (query-params @app-state))
  ([data] (get-in data [router-key :query-params])))

(defn query-param
  ([k] (query-param @app-state k))
  ([data k] (get-in data [router-key :query-params k])))

(defn route-param
  ([k] (route-param @app-state k))
  ([data k] (get-in data [router-key k])))

(defn route-set
  ([] (route-set @app-state))
  ([data] (route-param data :route)))

(defn invite-token
  ([] (invite-token @app-state))
  ([data] (query-param data :invite-token)))

(defn in-route?
  ([route-name] (in-route? (route-set @app-state) route-name))
  ([routes route-name]
  (when route-name
    (routes (keyword route-name)))))

;; Payments

(defn payments-data
  ([]
    (payments-data @app-state (current-org-slug)))
  ([org-slug]
   (payments-data @app-state org-slug))
  ([data org-slug]
   (get-in data (payments-key org-slug))))

;; Data

(defn bot-access
  ""
  ([] (bot-access @app-state))
  ([data]
    (:bot-access data)))

(defn notifications-data
  ""
  ([] (notifications-data @app-state))
  ([data]
    (get-in data notifications-key)))

(defn teams-data-requested
  ""
  ([] (teams-data-requested @app-state))
  ([data] (:teams-data-requested data)))

(defn auth-settings
  "Get the Auth settings data"
  ([] (auth-settings @app-state))
  ([data] (get-in data auth-settings-key)))

(defn api-entry-point
  "Get the API entry point."
  ([] (api-entry-point @app-state))
  ([data] (get-in data api-entry-point-key)))

(defn current-user-data
  "Get the current logged in user info."
  ([] (current-user-data @app-state))
  ([data] (get-in data [:current-user-data])))

(defn ^:export orgs-data
  ([] (orgs-data @app-state))
  ([data] (get data orgs-key)))

(defn ^:export org-data
  "Get org data."
  ([]
    (org-data @app-state (current-org-slug)))
  ([data]
    (org-data data (current-org-slug)))
  ([data org-slug]
    (get-in data (org-data-key org-slug))))

(defn ^:export posts-data
  "Get org all posts data."
  ([]
    (posts-data @app-state))
  ([data]
    (posts-data data (current-org-slug data)))
  ([data org-slug]
    (get-in data (posts-data-key org-slug))))

(defun board-data
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

(defn editable-boards-data
  ([] (editable-boards-data @app-state (current-org-slug)))
  ([org-slug] (editable-boards-data @app-state org-slug))
  ([data org-slug]
  (let [org-data (org-data data org-slug)
        filtered-boards (filterv
                         (fn [board]
                            (some #(when (= (:rel %) "create") %) (:links board)))
                         (:boards org-data))]
    (zipmap
     (map :slug filtered-boards)
     filtered-boards))))

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
    (items-to-render-data data org-slug (current-board-slug data) (current-sort-type data)))
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

(defn uploading-video-data
  ([video-id] (uploading-video-data (current-org-slug) video-id @app-state))
  ([org-slug video-id] (uploading-video-data org-slug video-id @app-state))
  ([org-slug video-id data]
    (let [uv-key (uploading-video-key org-slug video-id)]
      (get-in data uv-key))))

;; User notifications

(defn user-notifications-data
  "Get user notifications data"
  ([]
    (user-notifications-data (current-org-slug) @app-state))
  ([org-slug]
    (user-notifications-data org-slug @app-state))
  ([org-slug data]
    (get-in data (user-notifications-key org-slug))))

;; Change related

(defn change-data
  "Get change data."
  ([]
    (change-data @app-state))
  ([data]
    (change-data data (current-org-slug)))
  ([data org-slug]
    (get-in data (change-data-key org-slug))))

(defun activity-read-data
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

(defn org-seens-data
  ([] (org-seens-data @app-state (current-org-slug)))
  ([org-slug] (org-seens-data @app-state (current-org-slug)))
  ([data org-slug] (get-in data (org-seens-key org-slug))))

; (defn container-seen-data
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

(defn reminders-data
  ([] (reminders-data (current-org-slug) @app-state))
  ([org-slug] (reminders-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminders-data-key org-slug))))

(defn reminders-roster-data
  ([] (reminders-roster-data (current-org-slug) @app-state))
  ([org-slug] (reminders-roster-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminders-roster-key org-slug))))

(defn reminder-edit-data
  ([] (reminder-edit-data (current-org-slug) @app-state))
  ([org-slug] (reminder-edit-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminder-edit-key org-slug))))

;; Expo

(defn expo-deep-link-origin
  ([] (expo-deep-link-origin @app-state))
  ([data] (get-in data expo-deep-link-origin-key)))

(defn expo-app-version
  ([] (expo-app-version @app-state))
  ([data] (get-in data expo-app-version-key)))

(defn expo-push-token
  ([] (expo-push-token @app-state))
  ([data] (get-in data expo-push-token-key)))

;; Debug functions

(defn print-app-state []
  @app-state)

(defn print-org-data []
  (get-in @app-state (org-data-key (current-org-slug))))

(defn print-team-data []
  (get-in @app-state (team-data-key (:team-id (org-data)))))

(defn print-team-roster []
  (get-in @app-state (team-roster-key (:team-id (org-data)))))

(defn print-change-data []
  (get-in @app-state (change-data-key (current-org-slug))))

(defn print-activity-read-data []
  (get-in @app-state activities-read-key))

(defn print-board-data []
  (get-in @app-state (board-data-key (current-org-slug) (current-board-slug))))

(defn print-container-data []
  (if (is-container? (current-board-slug))
    (get-in @app-state (container-key (current-org-slug) (current-board-slug) (current-sort-type)))
    (get-in @app-state (board-data-key (current-org-slug) (current-board-slug)))))

(defn print-activity-data []
  (get-in
   @app-state
   (activity-key (current-org-slug) (current-activity-id))))

(defn print-secure-activity-data []
  (get-in
   @app-state
   (secure-activity-key (current-org-slug) (current-secure-activity-id))))

(defn print-reactions-data []
  (get-in
   @app-state
   (conj
    (activity-key (current-org-slug) (current-activity-id))
    :reactions)))

(defn print-comments-data []
  (get-in
   @app-state
   (comments-key (current-org-slug))))

(defn print-activity-comments-data []
  (get-in
   @app-state
   (activity-comments-key (current-org-slug) (current-activity-id))))

(defn print-entry-editing-data []
  (get @app-state :entry-editing))

(defn print-posts-data []
  (get-in @app-state (posts-data-key (current-org-slug))))

(defn print-filtered-posts []
  (filtered-posts-data @app-state (current-org-slug) (current-board-slug)))

(defn print-items-to-render []
  (items-to-render-data @app-state (current-org-slug) (current-board-slug)))

(defn print-user-notifications []
  (user-notifications-data (current-org-slug) @app-state))

(defn print-reminders-data []
  (reminders-data (current-org-slug) @app-state))

(defn print-reminder-edit-data []
  (reminder-edit-data (current-org-slug) @app-state))

(defn print-panel-stack []
  (:panel-stack @app-state))

(defn print-payments-data []
  (payments-data @app-state (current-org-slug)))

(defn print-router-path []
  (route @app-state))

(set! (.-OCWebPrintAppState js/window) print-app-state)
(set! (.-OCWebPrintOrgData js/window) print-org-data)
(set! (.-OCWebPrintTeamData js/window) print-team-data)
(set! (.-OCWebPrintTeamRoster js/window) print-team-roster)
(set! (.-OCWebPrintActiveUsers js/window) active-users)
(set! (.-OCWebPrintChangeData js/window) print-change-data)
(set! (.-OCWebPrintActivityReadData js/window) print-activity-read-data)
(set! (.-OCWebPrintBoardData js/window) print-board-data)
(set! (.-OCWebPrintContainerData js/window) print-container-data)
(set! (.-OCWebPrintActivityData js/window) print-activity-data)
(set! (.-OCWebPrintSecureActivityData js/window) print-secure-activity-data)
(set! (.-OCWebPrintReactionsData js/window) print-reactions-data)
(set! (.-OCWebPrintCommentsData js/window) print-comments-data)
(set! (.-OCWebPrintActivityCommentsData js/window) print-activity-comments-data)
(set! (.-OCWebPrintEntryEditingData js/window) print-entry-editing-data)
(set! (.-OCWebPrintFilteredPostsData js/window) print-filtered-posts)
(set! (.-OCWebPrintItemsToRender js/window) print-items-to-render)
(set! (.-OCWebPrintPostsData js/window) print-posts-data)
(set! (.-OCWebPrintUserNotifications js/window) print-user-notifications)
(set! (.-OCWebPrintRemindersData js/window) print-reminders-data)
(set! (.-OCWebPrintReminderEditData js/window) print-reminder-edit-data)
(set! (.-OCWebPrintPanelStack js/window) print-panel-stack)
(set! (.-OCWebPrintPaymentsData js/window) print-payments-data)
(set! (.-OCWebPrintRouterPath js/window) print-router-path)
;; Utility externs
(set! (.-OCWebUtils js/window) #js {:deref cljs.core.deref
                                    :keyword cljs.core.keyword
                                    :count cljs.core.count
                                    :get cljs.core.get
                                    :filter cljs.core.filter
                                    :map cljs.core.map
                                    :clj__GT_js cljs.core.clj__GT_js
                                    :js__GT_clj cljs.core.js__GT_clj})
