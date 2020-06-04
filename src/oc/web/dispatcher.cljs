(ns oc.web.dispatcher
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [defun.core :refer (defun)]
            [taoensso.timbre :as timbre]
            [clojure.string :as s]
            [cljs-flux.dispatcher :as flux]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.shared.useragent :as ua]))

(defonce ^{:export true} app-state (atom {:loading false
                          :show-login-overlay false}))

(def recent-activity-sort :recent-activity)
(def recently-posted-sort :recently-posted)

(def default-foc-layout :expanded)
(def other-foc-layout :collapsed)

;; Data key paths

(def checkout-result-key :checkout-success-result)
(def checkout-update-plan-key :checkout-update-plan)

(def expo-key [:expo])

(def expo-deep-link-origin-key (vec (conj expo-key :deep-link-origin)))
(def expo-app-version-key (vec (conj expo-key :app-version)))
(def expo-push-token-key (vec (conj expo-key :push-token)))

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

(defn threads-data-key [org-slug]
  (vec (conj (org-key org-slug) :threads)))

(defn unread-threads-key [org-slug]
  (vec (conj (org-key org-slug) :unread-threads)))

(defn board-key [org-slug board-slug]
  (vec (concat (boards-key org-slug) [(keyword board-slug) recently-posted-sort])))

(defn board-data-key [org-slug board-slug]
  (conj (board-key org-slug board-slug) :board-data))

(defn contributions-list-key [org-slug]
  (vec (conj (org-key org-slug) :contribs)))

(defn contributions-key
  [org-slug author-uuid]
   (vec (conj (contributions-list-key org-slug) (keyword author-uuid))))

(defn contributions-data-key [org-slug slug-or-uuid]
  (conj (contributions-key org-slug slug-or-uuid) :contrib-data))

(defn containers-key [org-slug]
  (vec (conj (org-key org-slug) :container-data)))

(defn container-key
  ([org-slug items-filter]
   (container-key org-slug items-filter recently-posted-sort))
  ([org-slug items-filter sort-type]
   (if sort-type
    (vec (conj (containers-key org-slug) (keyword items-filter) (keyword sort-type)))
    (vec (conj (containers-key org-slug) (keyword items-filter))))))

(defn secure-activity-key [org-slug secure-id]
  (vec (concat (org-key org-slug) [:secure-activities secure-id])))

(defn activity-key [org-slug activity-uuid]
  (let [posts-key (posts-data-key org-slug)]
    (vec (concat posts-key [activity-uuid]))))

(defn activity-last-read-at-key [org-slug activity-uuid]
  (vec (conj (activity-key org-slug activity-uuid) :last-read-at)))

(defn thread-key [org-slug thread-uuid]
  (let [threads-key (threads-data-key org-slug)]
    (vec (concat threads-key [thread-uuid]))))

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

(defn comments-key [org-slug]
  (vec (conj (org-key org-slug) :comments)))

(defn activity-comments-key [org-slug activity-uuid]
  (vec (conj (comments-key org-slug) activity-uuid)))

(def sorted-comments-key :sorted-comments)

(defn activity-sorted-comments-key [org-slug activity-uuid]
  (vec (concat (comments-key org-slug) [activity-uuid sorted-comments-key])))

(def threads-map-key :threads-map)

(defn activity-threads-map-key [org-slug activity-uuid]
  (vec (concat (comments-key org-slug) [activity-uuid threads-map-key])))

(defn activity-thread-data-key [org-slug activity-uuid thread-uuid]
  (vec (concat (activity-threads-map-key org-slug activity-uuid) [thread-uuid])))

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
  (vec (conj (follow-list-key org-slug) :board-uuids)))

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
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)]
     (board-data-key org-slug board-slug)))

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

(defn get-posts-for-board [posts-data board-slug]
  (let [posts-list (vals posts-data)
        filter-fn (if (= board-slug utils/default-drafts-board-slug)
                    #(not= (:status %) "published")
                    #(and (= (:board-slug %) board-slug)
                          (= (:status %) "published")))]
    (filter (comp filter-fn last) posts-data)))

;; Threads helpers

(defn is-threads? [container-slug]
  (when (or (string? container-slug)
            (keyword? container-slug))
    (-> container-slug name s/lower-case (= "threads"))))

;; Container helpers

(defn is-container? [container-slug]
  ;; Rest of containers
  (#{"inbox" "all-posts" "bookmarks" "following" "unfollowing" "activity" "threads"} container-slug))

(defn is-container-with-sort? [container-slug]
  ;; Rest of containers
  (#{"all-posts" "following" "unfollowing"} container-slug))

(defn- get-container-posts [base route posts-data org-slug container-slug sort-type items-key]
  (let [cnt-key (cond
                  (is-container? container-slug)
                  (container-key org-slug container-slug sort-type)
                  (seq (:contributions route))
                  (contributions-data-key org-slug container-slug)
                  :else
                  (board-data-key org-slug container-slug))
        container-data (get-in base cnt-key)
        posts-list (get container-data items-key)
        container-posts (vec (remove nil?
                         (map #(if (= (:content-type %) :entry)
                                 (merge % (get posts-data (:uuid %)))
                                 %)
                          posts-list)))]
    (if (= container-slug utils/default-drafts-board-slug)
      (filterv #(= (:status %) "draft") container-posts)
      container-posts)))

(defn- get-container-threads [base route posts-data threads-data org-slug container-slug sort-type items-key]
  (let [cnt-key (container-key org-slug container-slug sort-type)
        container-data (get-in base cnt-key)
        threads-list (get container-data items-key)
        container-items (vec (remove nil?
                          (map #(if (= (:content-type %) :thread)
                                  (let [thread (get threads-data (:uuid %))
                                        activity-data (get posts-data (:resource-uuid %))]
                                    (merge % thread {:activity-data activity-data}))
                                  %)
                           threads-list)))]
      (mapv (fn [thread-data]
              (let [thread-key (activity-thread-data-key org-slug (:resource-uuid thread-data) (:uuid thread-data))
                    thread-comments (get-in base thread-key)]
                (merge thread-data thread-comments)))
        container-items)))

(defn- get-container-items [base route posts-data threads-data org-slug container-slug sort-type items-key]
  (if (is-threads? container-slug)
    (get-container-threads base route posts-data threads-data org-slug container-slug sort-type items-key)
    (get-container-posts base route posts-data org-slug container-slug sort-type items-key)))

(def ui-theme-key [:ui-theme])

(def force-list-update-key [:force-list-update])

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

(defn drv-spec [db route-db]
  {:base                [[] db]
   :route               [[] route-db]
   :orgs                [[:base] (fn [base] (get base orgs-key))]
   :org-slug            [[:route] (fn [route] (:org route))]
   :contributions-id    [[:route] (fn [route] (:contributions route))]
   :board-slug          [[:route] (fn [route] (:board route))]
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
                                     (router/current-secure-activity-id))
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
   :filtered-posts      [[:base :org-data :posts-data :threads-data :route]
                         (fn [base org-data posts-data threads-data route]
                           (let [threads? (is-threads? (:board route))
                                 container-slug (or (:contributions route) (:board route))]
                             (when (and base org-data posts-data route container-slug (not threads?))
                               (get-container-items base route posts-data threads-data (:slug org-data) container-slug (:sort-type route) :posts-list))))]
   :threads-data        [[:base :org-slug]
                         (fn [base org-slug]
                           (when (and base org-slug)
                             (get-in base (threads-data-key org-slug))))]
   :items-to-render     [[:base :org-data :posts-data :threads-data :route]
                         (fn [base org-data posts-data threads-data route]
                           (let [threads? (is-threads? (:board route))
                                 container-slug (or (:contributions route) (:board route))]
                             (when (and base org-data container-slug
                                        (or (and threads?
                                                 threads-data
                                                 posts-data)
                                            (and (not threads?)
                                                 posts-data)))
                               (get-container-items base route posts-data threads-data (:slug org-data) container-slug (:sort-type route) :items-to-render))))]
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
   :container-data      [[:base :org-slug :board-slug :contributions-id :sort-type]
                         (fn [base org-slug board-slug contributions-id sort-type]
                           (when (and org-slug
                                      (or board-slug
                                          contributions-id))
                             (let [container-key (cond
                                                   (seq contributions-id)
                                                   (contributions-data-key org-slug contributions-id)
                                                   (is-container? board-slug)
                                                   (container-key org-slug board-slug sort-type)
                                                   :else
                                                   (board-data-key org-slug board-slug))]
                               (get-in base container-key))))]
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
   :show-sections-picker [[:base]
                          (fn [base]
                            (:show-sections-picker base))]
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
   :navbar-data         [[:base :org-data :board-data :contributions-user-data]
                          (fn [base org-data board-data contributions-user-data]
                            (let [navbar-data (select-keys base [:show-login-overlay
                                                                 :orgs-dropdown-visible
                                                                 :panel-stack
                                                                 :search-active
                                                                 :show-whats-new-green-dot])]
                              (-> navbar-data
                                (assoc :org-data org-data)
                                (assoc :board-data board-data)
                                (assoc :contributions-user-data contributions-user-data))))]
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
   :unread-threads        [[:base :org-slug]
                           (fn [base org-slug]
                             (get-in base (unread-threads-key org-slug)))]
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
   :org-dashboard-data    [[:base :orgs :org-data :board-data :contributions-data :container-data :posts-data :activity-data
                            :show-sections-picker :entry-editing :jwt :wrt-show :loading :payments :search-active :user-info-data
                            :active-users :follow-publishers-list :follow-boards-list]
                            (fn [base orgs org-data board-data contributions-data container-data posts-data activity-data
                                 show-sections-picker entry-editing jwt wrt-show loading payments search-active user-info-data
                                 active-users follow-publishers-list follow-boards-list]
                              {:jwt-data jwt
                               :orgs orgs
                               :org-data org-data
                               :payments-data payments
                               :container-data container-data
                               :board-data board-data
                               :contributions-data contributions-data
                               :initial-section-editing (:initial-section-editing base)
                               :posts-data posts-data
                               :panel-stack (:panel-stack base)
                               :is-sharing-activity (boolean (:activity-share base))
                               :is-showing-alert (boolean (:alert-modal base))
                               :entry-edit-dissmissing (:entry-edit-dissmissing base)
                               :media-input (:media-input base)
                               :show-section-add-cb (:show-section-add-cb base)
                               :show-sections-picker show-sections-picker
                               :entry-editing-board-slug (:board-slug entry-editing)
                               :activity-share-container (:activity-share-container base)
                               :cmail-state (:cmail-state base)
                               :force-login-wall (:force-login-wall base)
                               :app-loading loading
                               :search-active search-active
                               :user-info-data user-info-data
                               :active-users active-users
                               :follow-publishers-list follow-publishers-list
                               :follow-boards-list follow-boards-list})]
   :show-add-post-tooltip      [[:nux] (fn [nux] (:show-add-post-tooltip nux))]
   :show-edit-tooltip          [[:nux] (fn [nux] (:show-edit-tooltip nux))]
   :show-post-added-tooltip    [[:nux] (fn [nux] (:show-post-added-tooltip nux))]
   :show-invite-people-tooltip [[:nux] (fn [nux] (:show-invite-people-tooltip nux))]
   :nux-user-type              [[:nux] (fn [nux] (:user-type nux))]
   ;; Cmail
   :cmail-state           [[:base] (fn [base] (:cmail-state base))]
   :cmail-data            [[:base] (fn [base] (:cmail-data base))]
   :reminders-data        [[:base :org-slug] (fn [base org-slug]
                                    (get-in base (reminders-data-key org-slug)))]
   :reminders-roster      [[:base :org-slug] (fn [base org-slug]
                                    (get-in base (reminders-roster-key org-slug)))]
   :reminder-edit         [[:base :org-slug] (fn [base org-slug]
                                    (get-in base (reminder-edit-key org-slug)))]
   :foc-layout            [[:base] (fn [base] (:foc-layout base))]
   :ui-theme              [[:base] (fn [base] (get-in base ui-theme-key))]
   :force-list-update     [[:base] (fn [base] (get-in base force-list-update-key))]
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

;; Payments

(defn payments-data
  ([]
    (payments-data @app-state (router/current-org-slug)))
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
    (org-data @app-state (router/current-org-slug)))
  ([data]
    (org-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (org-data-key org-slug))))

(defn ^:export posts-data
  "Get org all posts data."
  ([]
    (posts-data @app-state))
  ([data]
    (posts-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (posts-data-key org-slug))))

(defn ^:export threads-data
  "Get all the threads of the given org."
  ([]
    (threads-data @app-state))
  ([data]
    (threads-data data (router/current-org-slug)))
  ([data org-slug]
    (get-in data (threads-data-key org-slug))))

(defun board-data
  "Get board data."
  ([]
    (board-data @app-state))
  ([data :guard map?]
    (board-data data (router/current-org-slug) (router/current-board-slug)))
  ([board-slug :guard #(or (keyword? %) (string? %))]
    (board-data @app-state (router/current-org-slug) board-slug))
  ([org-slug :guard #(or (keyword? %) (string? %)) board-slug :guard #(or (keyword? %) (string? %))]
    (board-data @app-state org-slug board-slug))
  ([data :guard map? org-slug :guard #(or (keyword? %) (string? %))]
    (board-data @app-state org-slug (router/current-board-slug)))
  ([data org-slug board-slug]
    (when (and org-slug board-slug)
      (get-in data (board-data-key org-slug board-slug)))))

(defun ^:export contributions-data
  "Get contributions data"
  ([]
    (contributions-data @app-state))
  ([data :guard map?]
    (contributions-data data (router/current-org-slug) (router/current-contributions-id)))
  ([contributions-id :guard #(or (keyword? %) (string? %))]
    (contributions-data @app-state (router/current-org-slug) contributions-id))
  ([org-slug :guard #(or (keyword? %) (string? %)) contributions-id :guard #(or (keyword? %) (string? %))]
    (contributions-data @app-state org-slug contributions-id))
  ([data :guard map? org-slug :guard #(or (keyword? %) (string? %))]
    (contributions-data @app-state org-slug (router/current-contributions-id)))
  ([data org-slug contributions-id]
    (when (and org-slug contributions-id)
      (get-in data (contributions-data-key org-slug contributions-id)))))

(defn editable-boards-data
  ([] (editable-boards-data @app-state (router/current-org-slug)))
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
    (container-data @app-state))
  ([data]
    (container-data data (router/current-org-slug) (router/current-posts-filter) (router/current-sort-type)))
  ([data org-slug]
    (container-data data org-slug (router/current-posts-filter) (router/current-sort-type)))
  ([data org-slug posts-filter]
    (container-data data org-slug posts-filter (router/current-sort-type)))
  ([data org-slug posts-filter sort-type]
    (get-in data (container-key org-slug posts-filter sort-type))))

(defn ^:export filtered-posts-data
  ([]
    (filtered-posts-data @app-state @router/path (router/current-org-slug) (router/current-posts-filter) (router/current-sort-type)))
  ([data]
    (filtered-posts-data data @router/path (router/current-org-slug) (router/current-posts-filter) (router/current-sort-type)))
  ([data route]
    (filtered-posts-data data route (router/current-org-slug) (router/current-posts-filter) (router/current-sort-type)))
  ([data route org-slug]
    (filtered-posts-data data route org-slug (router/current-posts-filter) (router/current-sort-type)))
  ([data route org-slug posts-filter]
    (filtered-posts-data data route org-slug posts-filter (router/current-sort-type)))
  ([data route org-slug posts-filter sort-type]
    (let [posts-data (get-in data (posts-data-key org-slug))
          threads-data (get-in data (threads-data-key org-slug))]
     (get-container-items data route posts-data threads-data org-slug posts-filter sort-type :posts-list)))
  ; ([data org-slug posts-filter activity-id]
  ;   (let [org-data (org-data data org-slug)
  ;         all-boards-slug (map :slug (:boards org-data))
  ;         is-board? ((set all-boards-slug) posts-filter)
  ;         posts-data (get-in data (posts-data-key org-slug))]
  ;    (if is-board?
  ;      (get-posts-for-board activity-id posts-data posts-filter)
  ;      (let [container-key (container-key org-slug posts-filter)
  ;            items-list (:posts-list (get-in data container-key))]
  ;       (zipmap items-list (map #(get posts-data %) items-list))))))
  )

(defn ^:export items-to-render-data
  ([]
    (items-to-render-data @app-state))
  ([data]
    (items-to-render-data data @router/path (router/current-org-slug) (router/current-posts-filter) (router/current-sort-type)))
  ([data route]
    (items-to-render-data data route (router/current-org-slug) (router/current-posts-filter) (router/current-sort-type)))
  ([data route org-slug]
    (items-to-render-data data route org-slug (router/current-posts-filter) (router/current-sort-type)))
  ([data route org-slug posts-filter]
    (items-to-render-data data route org-slug (router/current-posts-filter) (router/current-sort-type)))
  ([data route org-slug posts-filter sort-type]
    (let [posts-data (get-in data (posts-data-key org-slug))
          threads-data (get-in data (threads-data-key org-slug))]
     (get-container-items data route posts-data threads-data org-slug posts-filter sort-type :items-to-render)))
  ; ([data org-slug posts-filter activity-id]
  ;   (let [org-data (org-data data org-slug)
  ;         all-boards-slug (map :slug (:boards org-data))
  ;         is-board? ((set all-boards-slug) posts-filter)
  ;         posts-data (get-in data (posts-data-key org-slug))]
  ;    (if is-board?
  ;      (get-posts-for-board activity-id posts-data posts-filter)
  ;      (let [container-key (container-key org-slug posts-filter)
  ;            items-list (:posts-list (get-in data container-key))]
  ;       (zipmap items-list (map #(get posts-data %) items-list))))))
  )

(defn ^:export draft-posts-data
  ([]
    (draft-posts-data @app-state (router/current-org-slug)))
  ([org-slug]
    (draft-posts-data @app-state org-slug))
  ([data org-slug]
    (filtered-posts-data data @router/path org-slug utils/default-drafts-board-slug)))

(defn ^:export activity-data
  "Get activity data."
  ([]
    (activity-data (router/current-org-slug) (router/current-activity-id) @app-state))
  ([activity-id]
    (activity-data (router/current-org-slug) activity-id @app-state))
  ([org-slug activity-id]
    (activity-data org-slug activity-id @app-state))
  ([org-slug activity-id data]
    (let [activity-key (activity-key org-slug activity-id)]
      (get-in data activity-key))))
(def activity-data-get activity-data)

(defn ^:export thread-data
  "Get thread data."
  ([thread-uuid]
    (thread-data (router/current-org-slug) thread-uuid @app-state))
  ([org-slug thread-uuid data]
    (get-in data (thread-key org-slug thread-uuid))))

(defn ^:export secure-activity-data
  "Get secure activity data."
  ([]
    (secure-activity-data (router/current-org-slug) (router/current-secure-activity-id) @app-state))
  ([secure-id]
    (secure-activity-data (router/current-org-slug) secure-id @app-state))
  ([org-slug secure-id]
    (secure-activity-data org-slug secure-id @app-state))
  ([org-slug secure-id data]
    (let [activity-key (secure-activity-key org-slug secure-id)]
      (get-in data activity-key))))

(defn ^:export comments-data
  ([]
    (comments-data (router/current-org-slug) @app-state))
  ([org-slug]
    (comments-data org-slug @app-state))
  ([org-slug data]
    (get-in data (comments-key org-slug))))

(defn ^:export activity-comments-data
  ([]
    (activity-comments-data
     (router/current-org-slug)
     (router/current-activity-id)
     @app-state))
  ([activity-uuid]
    (activity-comments-data
     (router/current-org-slug)
     activity-uuid @app-state))
  ([org-slug activity-uuid]
    (activity-comments-data org-slug activity-uuid @app-state))
  ([org-slug activity-uuid data]
    (get-in data (activity-comments-key org-slug activity-uuid))))

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

(defn uploading-video-data
  ([video-id] (uploading-video-data (router/current-org-slug) video-id @app-state))
  ([org-slug video-id] (uploading-video-data org-slug video-id @app-state))
  ([org-slug video-id data]
    (let [uv-key (uploading-video-key org-slug video-id)]
      (get-in data uv-key))))

;; User notifications

(defn user-notifications-data
  "Get user notifications data"
  ([]
    (user-notifications-data (router/current-org-slug) @app-state))
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
    (change-data data (router/current-org-slug)))
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

;; Reminders

(defn reminders-data
  ([] (reminders-data (router/current-org-slug) @app-state))
  ([org-slug] (reminders-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminders-data-key org-slug))))

(defn reminders-roster-data
  ([] (reminders-roster-data (router/current-org-slug) @app-state))
  ([org-slug] (reminders-roster-data org-slug @app-state))
  ([org-slug data]
    (get-in data (reminders-roster-key org-slug))))

(defn reminder-edit-data
  ([] (reminder-edit-data (router/current-org-slug) @app-state))
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
  (get-in @app-state (org-data-key (router/current-org-slug))))

(defn print-team-data []
  (get-in @app-state (team-data-key (:team-id (org-data)))))

(defn print-team-roster []
  (get-in @app-state (team-roster-key (:team-id (org-data)))))

(defn print-change-data []
  (get-in @app-state (change-data-key (router/current-org-slug))))

(defn print-activity-read-data []
  (get-in @app-state activities-read-key))

(defn print-board-data []
  (get-in @app-state (board-data-key (router/current-org-slug) (router/current-board-slug))))

(defn print-container-data []
  (if (is-container? (router/current-board-slug))
    (get-in @app-state (container-key (router/current-org-slug) (router/current-board-slug) (router/current-sort-type)))
    (get-in @app-state (board-data-key (router/current-org-slug) (router/current-board-slug)))))

(defn print-activity-data []
  (get-in
   @app-state
   (activity-key (router/current-org-slug) (router/current-activity-id))))

(defn print-secure-activity-data []
  (get-in
   @app-state
   (secure-activity-key (router/current-org-slug) (router/current-secure-activity-id))))

(defn print-reactions-data []
  (get-in
   @app-state
   (conj
    (activity-key (router/current-org-slug) (router/current-activity-id))
    :reactions)))

(defn print-comments-data []
  (get-in
   @app-state
   (comments-key (router/current-org-slug))))

(defn print-activity-comments-data []
  (get-in
   @app-state
   (activity-comments-key (router/current-org-slug) (router/current-activity-id))))

(defn print-entry-editing-data []
  (get @app-state :entry-editing))

(defn print-posts-data []
  (get-in @app-state (posts-data-key (router/current-org-slug))))

(defn print-filtered-posts []
  (filtered-posts-data @app-state @router/path (router/current-org-slug) (router/current-posts-filter)))

(defn print-threads-data []
  (get-in @app-state (threads-data-key (router/current-org-slug))))

(defn print-items-to-render []
  (items-to-render-data @app-state (router/current-org-slug) (router/current-posts-filter)))

(defn print-user-notifications []
  (user-notifications-data (router/current-org-slug) @app-state))

(defn print-reminders-data []
  (reminders-data (router/current-org-slug) @app-state))

(defn print-reminder-edit-data []
  (reminder-edit-data (router/current-org-slug) @app-state))

(defn print-panel-stack []
  (:panel-stack @app-state))

(defn print-payments-data []
  (payments-data @app-state (router/current-org-slug)))

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
(set! (.-OCWebPrintThreadsData js/window) print-threads-data)
(set! (.-OCWebPrintUserNotifications js/window) print-user-notifications)
(set! (.-OCWebPrintRemindersData js/window) print-reminders-data)
(set! (.-OCWebPrintReminderEditData js/window) print-reminder-edit-data)
(set! (.-OCWebPrintPanelStack js/window) print-panel-stack)
(set! (.-OCWebPrintPaymentsData js/window) print-payments-data)
;; Utility externs
(set! (.-OCWebUtils js/window) #js {:deref cljs.core.deref
                                    :keyword cljs.core.keyword
                                    :count cljs.core.count
                                    :get cljs.core.get
                                    :filter cljs.core.filter
                                    :map cljs.core.map
                                    :clj__GT_js cljs.core.clj__GT_js
                                    :js__GT_clj cljs.core.js__GT_clj})
