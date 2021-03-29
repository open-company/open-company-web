(ns oc.web.derivatives
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [oc.web.dispatcher :as dis]
            [taoensso.timbre :as timbre]
            [oc.lib.cljs.useragent :as ua]
            [clojure.string :as s]))

;; Derived Data ================================================================

(defn drv-spec [db]
  {:base                [[] db]
   :route               [[:base] (fn [base] (get base dis/router-key))]
   :route/dark-allowed  [[:route] (fn [route] (-> (get-in route [dis/router-opts-key])
                                                  set
                                                  dis/router-dark-allowed-key
                                                  boolean))]
   :orgs                [[:base] (fn [base] (get base dis/orgs-key))]
   :org-slug            [[:route] (fn [route] (:org route))]
   :contributions-id    [[:route] (fn [route] (:contributions route))]
   :board-slug          [[:route] (fn [route] (:board route))]
   :container-slug      [[:board-slug :contributions-id :activity-uuid]
                         (fn [board-slug contributions-id activity-uuid]
                           (when activity-uuid
                             (or board-slug
                                 contributions-id)))]
   :entry-board-slug    [[:route] (fn [route] (:entry-board route))]
   :sort-type           [[:route] (fn [route] (:sort-type route))]
   :activity-uuid       [[:route] (fn [route] (:activity route))]
   :secure-id           [[:route] (fn [route] (:secure-id route))]
   :loading             [[:base] (fn [base] (:loading base))]
   :signup-with-email   [[:base] (fn [base] (:signup-with-email base))]
   :query-params        [[:route] (fn [route] (:query-params route))]
   :teams-data          [[:base] (fn [base] (get-in base dis/teams-data-key))]
   :auth-settings       [[:base] (fn [base] (get-in base dis/auth-settings-key))]
   :entry-save-on-exit  [[:base] (fn [base] (:entry-save-on-exit base))]
   :orgs-dropdown-visible [[:base] (fn [base] (:orgs-dropdown-visible base))]
   :add-comment-focus   [[:base] (fn [base] (:add-comment-focus base))]
   :nux                 [[:base] (fn [base] (:nux base))]
   :notifications-data  [[:base] (fn [base] (get-in base dis/notifications-key))]
   :login-with-email    [[:base] (fn [base] (:login-with-email base))]
   :login-with-email-error [[:base] (fn [base] (:login-with-email-error base))]
   :panel-stack         [[:base] (fn [base] (:panel-stack base))]
   :current-panel       [[:panel-stack] (fn [panel-stack] (last panel-stack))]
   :mobile-navigation-sidebar [[:base] (fn [base] (:mobile-navigation-sidebar base))]
   :mobile-user-notifications [[:base] (fn [base] (:mobile-user-notifications base))]
   :expand-image-src    [[:base] (fn [base] (:expand-image-src base))]
   :attachment-uploading [[:base] (fn [base] (:attachment-uploading base))]
   :add-comment-force-update [[:base] (fn [base] (get base dis/add-comment-force-update-root-key))]
   :mobile-swipe-menu  [[:base] (fn [base] (:mobile-swipe-menu base))]
   dis/checkout-result-key [[:base] (fn [base] (get base dis/checkout-result-key))]
   dis/checkout-update-price-key [[:base] (fn [base] (get base dis/checkout-update-price-key))]
   :expo                [[:base] (fn [base] (get-in base dis/expo-key))]
   :expo-deep-link-origin [[:base] (fn [base] (get-in base dis/expo-deep-link-origin-key))]
   :expo-app-version    [[:base] (fn [base] (get-in base dis/expo-app-version-key))]
   :invite-add-slack-checked [[:base] (fn [base] (:invite-add-slack-checked base))]
   :add-comment-data    [[:base :org-slug] (fn [base org-slug]
                                             (get-in base (dis/add-comment-key org-slug)))]
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
                                    (dis/current-secure-activity-id base))
                             (select-keys (:id-token base) [:user-id :avatar-url :first-name :last-name :name])
                             (:current-user-data base)))]
   :payments        [[:base :org-slug] (fn [base org-slug] (get-in base (dis/payments-key org-slug)))]
   :show-login-overlay  [[:base] (fn [base] (:show-login-overlay base))]
   :site-menu-open      [[:base] (fn [base] (:site-menu-open base))]
   :ap-loading          [[:base] (fn [base] (:ap-loading base))]
   :edit-reminder       [[:base] (fn [base] (:edit-reminder base))]
   :drafts-data         [[:base :org-slug]
                         (fn [base org-slug]
                           (get-in base (dis/board-data-key org-slug :drafts)))]
   :bookmarks-data     [[:base :org-slug]
                        (fn [base org-slug]
                          (get-in base (dis/container-key org-slug :bookmarks)))]
   :following-data     [[:base :org-slug]
                        (fn [base org-slug]
                          (get-in base (dis/container-key org-slug :following)))]
   :unfollowing-data   [[:base :org-slug]
                        (fn [base org-slug]
                          (get-in base (dis/container-key org-slug :unfollowing)))]
   :org-data            [[:base :org-slug]
                         (fn [base org-slug]
                           (when org-slug
                             (dis/org-data base org-slug)))]
   :team-data           [[:base :org-data]
                         (fn [base org-data]
                           (when org-data
                             (get-in base (dis/team-data-key (:team-id org-data)))))]
   :team-roster         [[:base :org-data]
                         (fn [base org-data]
                           (when org-data
                             (get-in base (dis/team-roster-key (:team-id org-data)))))]
   :invite-users        [[:base] (fn [base] (:invite-users base))]
   :invite-data         [[:base :team-data :current-user-data :team-roster :invite-users]
                         (fn [_ team-data current-user-data team-roster invite-users]
                           {:team-data team-data
                            :invite-users invite-users
                            :current-user-data current-user-data
                            :team-roster team-roster})]
   :org-settings-team-management
   [[:base :query-params :team-data :auth-settings]
    (fn [base query-params team-data]
      {:um-domain-invite (:um-domain-invite base)
       :add-email-domain-team-error (:add-email-domain-team-error base)
       :team-data team-data
       :query-params query-params})]
   :posts-data          [[:base :org-slug]
                         (fn [base org-slug]
                           (when (and base org-slug)
                             (get-in base (dis/posts-data-key org-slug))))]
   :filtered-posts      [[:base :org-data :posts-data :route]
                         (fn [base org-data posts-data route]
                           (let [container-slug (or (:contributions route) (:board route))]
                             (when (and base org-data posts-data route container-slug)
                               (dis/get-container-posts base posts-data (:slug org-data) container-slug (:sort-type route) :posts-list))))]
   :items-to-render     [[:base :org-data :posts-data :route]
                         (fn [base org-data posts-data route]
                           (let [container-slug (or (:contributions route) (:board route))]
                             (when (and base org-data container-slug posts-data)
                               (dis/get-container-posts base posts-data (:slug org-data) container-slug (:sort-type route) :items-to-render))))]
   :team-channels       [[:base :org-data]
                         (fn [base org-data]
                           (when org-data
                             (get-in base (dis/team-channels-key (:team-id org-data)))))]
   :change-data         [[:base :org-slug]
                         (fn [base org-slug]
                           (when (and base org-slug)
                             (get-in base (dis/change-data-key org-slug))))]
   :editable-boards     [[:base :org-slug]
                         (fn [base org-slug]
                           (dis/editable-boards-data base org-slug))]
   :private-boards      [[:base :org-slug]
                         (fn [base org-slug]
                           (dis/private-boards-data base org-slug))]
   :public-boards       [[:base :org-slug]
                         (fn [base org-slug]
                           (dis/public-boards-data base org-slug))]
   :container-data      [[:base :org-slug :board-slug :entry-board-slug :contributions-id :activity-uuid :sort-type]
                         (fn [base org-slug board-slug entry-board-slug contributions-id activity-uuid sort-type]
                           (when (and org-slug
                                      (or entry-board-slug
                                          board-slug
                                          contributions-id))
                             (let [container-key (cond (and (not activity-uuid)
                                                            (dis/is-container? board-slug))
                                                       (dis/container-key org-slug board-slug sort-type)
                                                       (seq contributions-id)
                                                       (dis/contributions-data-key org-slug contributions-id)
                                                      ;;  (and (seq activity-uuid)
                                                      ;;       (seq entry-board-slug))
                                                      ;;  (dis/container-key org-slug entry-board-slug)
                                                       :else
                                                       (dis/board-data-key org-slug board-slug))]
                               (get-in base container-key))))]
   :contributions-data    [[:org-slug :contributions-id]
                           (fn [org-slug contributions-id]
                             (when (and org-slug contributions-id)
                               (dis/contributions-data org-slug contributions-id)))]
   :board-data          [[:base :org-slug :board-slug]
                         (fn [base org-slug board-slug]
                           (dis/board-data base org-slug board-slug))]
   :entry-board-data    [[:base :org-slug :entry-board-slug]
                         (fn [base org-slug entry-board-slug]
                           (dis/board-data base org-slug entry-board-slug))]
   :contributions-user-data [[:active-users :contributions-id]
                             (fn [active-users contributions-id]
                               (when (and active-users contributions-id)
                                 (get active-users contributions-id)))]
   :activity-data       [[:base :org-slug :activity-uuid]
                         (fn [base org-slug activity-uuid]
                           (dis/activity-data org-slug activity-uuid base))]
   :secure-activity-data [[:base :org-slug :secure-id]
                          (fn [base org-slug secure-id]
                            {:activity-data (dis/secure-activity-data org-slug secure-id base)
                             :is-showing-alert (boolean (:alert-modal base))})]
   :comments-data       [[:base :org-slug]
                         (fn [base org-slug]
                           (get-in base (dis/comments-key org-slug)))]
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
   :alert-modal         [[:base] (fn [base] (:alert-modal base))]
   :activity-share        [[:base] (fn [base] (:activity-share base))]
   :activity-share-medium [[:base] (fn [base] (:activity-share-medium base))]
   :activity-share-container  [[:base] (fn [base] (:activity-share-container base))]
   :activity-shared-data  [[:base] (fn [base] (:activity-shared-data base))]
   :activities-read       [[:base] (fn [base] (get-in base dis/activities-read-key))]
   :navbar-data         [[:base :org-data :board-data :contributions-user-data :org-slug :board-slug :contributions-id :activity-uuid :current-user-data]
                         (fn [base org-data board-data contributions-user-data org-slug board-slug contributions-id activity-uuid current-user-data]
                           (let [navbar-data (select-keys base [:show-login-overlay
                                                                :orgs-dropdown-visible
                                                                :panel-stack
                                                                :search-active
                                                                :show-whats-new-green-dot
                                                                :mobile-user-notifications])]
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
   :team-invite           [[:route :auth-settings :jwt]
                           (fn [route auth-settings jwt]
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
                               (get-in base (dis/user-notifications-key org-slug))))]
   :replies-badge        [[:base :org-slug]
                          (fn [base org-slug]
                            (get-in base (dis/replies-badge-key org-slug)))]
   :following-badge           [[:base :org-slug]
                               (fn [base org-slug]
                                 (get-in base (dis/following-badge-key org-slug)))]
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
   :user-responded-to-push-permission? [[:base]
                                        (fn [base]
                                          (boolean
                                           (get-in base dis/expo-push-token-key)))]
   :wrt-show              [[:base] (fn [base] (:wrt-show base))]
   :wrt-panel-uuid        [[:panel-stack]
                           (fn [panel-stack]
                             (when (seq panel-stack)
                               (some #(when (s/starts-with? (:name %) "wrt-") %) panel-stack))
                             (when (and panel-stack
                                        (seq (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                               (first (filter #(s/starts-with? (name %) "wrt-") panel-stack))))]
   :wrt-read-data         [[:base :panel-stack]
                           (fn [base panel-stack]
                             (when (and panel-stack
                                        (seq (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                               (when-let* [wrt-panel (name (first (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                                           wrt-uuid (subs wrt-panel 4 (count wrt-panel))]
                                          (dis/activity-read-data wrt-uuid base))))]
   :wrt-activity-data     [[:base :org-slug :panel-stack]
                           (fn [base org-slug panel-stack]
                             (when (and panel-stack
                                        (seq (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                               (when-let* [wrt-panel (name (first (filter #(s/starts-with? (name %) "wrt-") panel-stack)))
                                           wrt-uuid (subs wrt-panel 4 (count wrt-panel))]

                                 (dis/activity-data-get org-slug wrt-uuid base))))]
   :user-info-data        [[:active-users :panel-stack]
                           (fn [active-users panel-stack]
                             (when (and panel-stack
                                        (seq (filter #(s/starts-with? (name %) "user-info-") panel-stack)))
                               (when-let* [user-info-panel (name (first (filter #(s/starts-with? (name %) "user-info-") panel-stack)))
                                           user-id (subs user-info-panel (count "user-info-") (count user-info-panel))]
                                          (get active-users user-id))))]
   :app-loading?          [[:loading :board-slug :contributions-id :entry-board-slug :org-data :active-users]
                           (fn [loading board-slug contributions-id entry-board-slug org-data active-users]
                             (boolean
                              (or loading ;; force loading screen
                                   ;; the org data are not loaded yet
                                  (not org-data)
                                   ;; No board nor contributions specified
                                  (and (not board-slug)
                                       (not contributions-id)
                                       (not entry-board-slug)
                                        ;; but there are some
                                       (pos? (count (:boards org-data))))
                                   ;; Active users have not been loaded yet:
                                   ;; they are blocking since they are used to:
                                   ;; - init entries body and comments body for mentions
                                   ;; - show user's info on hover and in profile panels
                                   ;; - on mobile it's not blocking since cmail is closed
                                  (not (map? active-users)))))]
   :org-not-found?        [[:org-slug :orgs]
                           (fn [current-org-slug orgs]
                             (boolean
                              (and (not (nil? orgs))
                                   (not ((set (map :slug orgs)) current-org-slug)))))]
   :board-not-found?      [[:org-not-found? :board-slug :activity-uuid :entry-board-slug :org-data]
                           (fn [org-not-found? board-slug activity-uuid entry-board-slug org-data]
                             (let [board-exists? (set (map :slug (:boards org-data)))]
                               (boolean
                                (and (not org-not-found?)
                                     org-data
                                     (or (and activity-uuid
                                              ;; (not (dis/is-container? entry-board-slug))
                                              (not (board-exists? entry-board-slug)))
                                         (and board-slug
                                              ;; (not contributions-id)
                                              (not (dis/is-container? board-slug))
                                              (not (board-exists? board-slug))))))))]
   :contributions-not-found? [[:org-not-found? :org-data :contributions-id :active-users]
                              (fn [org-not-found? org-data current-contributions-id active-users]
                                (boolean
                                 (and (not org-not-found?)
                                      org-data
                                      current-contributions-id
                                      (map? active-users)
                                      (get active-users (keyword current-contributions-id)))))]
   :entry-not-found?      [[:org-not-found? :board-not-found? :activity-data :entry-board-slug :active-users]
                           (fn [org-not-found? board-not-found? current-activity-data current-entry-board-slug active-users]
                             (boolean
                              (and (not org-not-found?)
                                   ;; Users for mentions has not been loaded
                                   (map? active-users)
                                   ;; section is present
                                   (not board-not-found?)
                                   ;; route is for a single post and it's been loaded
                                   current-activity-data
                                   ;; post wasn't found
                                   (or (= current-activity-data :404)
                                       ;; route has wrong board slug/uuid for the current post
                                       (and (map? current-activity-data)
                                            (not (:loading current-activity-data))
                                            (not= (:board-slug current-activity-data) current-entry-board-slug)
                                            (not= (:board-uuid current-activity-data) current-entry-board-slug))))))]
   :force-login-wall      [[:base] (fn [base] (get base :force-login-wall))]
   :show-login-wall?      [[:jwt :force-login-wall :activity-uuid :org-not-found? :board-not-found? :contributions-not-found? :entry-not-found?]
                           (fn [jwt-data force-login-wall current-activity-id org-not-found? board-not-found? contributions-not-found? entry-not-found?]
                             (boolean
                              (and (not jwt-data)
                                   (or force-login-wall
                                       (and current-activity-id
                                            (or org-not-found?
                                                board-not-found?
                                                contributions-not-found?
                                                entry-not-found?))))))]
   :show-wrt-view?         [[:current-panel]
                            (fn [current-panel]
                              (boolean
                               (and current-panel
                                    (s/starts-with? (name current-panel) "wrt-"))))]
   :show-user-info?        [[:current-panel]
                            (fn [current-panel]
                              (boolean
                               (and current-panel
                                    (s/starts-with? (name current-panel) "user-info-"))))]
   :show-push-notification-permissions-modal?
   [[:jwt :user-responded-to-push-permission?]
    (fn [jwt-data user-responded-to-push-permission?]
      (boolean
       (and jwt-data
            (not user-responded-to-push-permission?))))]
   :show-activity-removed? [[:jwt :entry-not-found?]
                            (fn [jwt-data entry-not-found?]
                              (boolean
                               (and jwt-data
                                    entry-not-found?)))]
   :show-expanded-post?    [[:entry-not-found? :activity-data]
                            (fn [entry-not-found? current-activity-data]
                              (boolean
                               (and (not entry-not-found?)
                                    (map? current-activity-data)
                                    (:published? current-activity-data))))]
   :show-alert-modal?      [[:alert-modal] (fn [alert-modal] (boolean alert-modal))]
   :ui-tooltip             [[:base] (fn [base] (:ui-tooltip base))]
   :org-dashboard-data     [[:org-data :show-alert-modal? :show-section-add-cb :activity-share-container :show-activity-share?
                             :collapsed-cmail? :user-info-data :search-active :show-premium-picker? :payments-ui-upgraded-banner
                             :ui-tooltip :initial-section-editing :show-wrt-view? :show-expanded-post?
                             :show-push-notification-permissions-modal? :show-user-info?]
                            (fn [org-data show-alert-modal? show-section-add-cb activity-share-container show-activity-share?
                                 collapsed-cmail? user-info-data search-active show-premium-picker? payments-ui-upgraded-banner
                                 ui-tooltip initial-section-editing show-wrt-view? show-expanded-post?
                                 show-push-notification-permissions-modal? show-user-info?]
                              {:org-data org-data
                               :show-activity-share? show-activity-share?
                               :show-alert-modal? show-alert-modal?
                               :show-section-add-cb show-section-add-cb
                               :activity-share-container activity-share-container
                               :collapsed-cmail? collapsed-cmail?
                               :user-info-data user-info-data
                               :show-search? search-active
                               :show-premium-picker? show-premium-picker?
                               :payments-ui-upgraded-banner payments-ui-upgraded-banner
                               :ui-tooltip ui-tooltip
                               :initial-section-editing initial-section-editing
                               :show-wrt-view? show-wrt-view?
                               :show-expanded-post? show-expanded-post?
                               :show-user-info? show-user-info?
                               :show-push-notification-permissions-modal? show-push-notification-permissions-modal?})]
   :initial-section-editing [[:base] (fn [base] (:initial-section-editing base))]
   :payments-ui-upgraded-banner [[:base] (fn [base] (get base dis/payments-ui-upgraded-banner-key))]
   :show-activity-share?  [[:activity-share] (fn [activity-share] (boolean activity-share))]
   :collapsed-cmail?      [[:cmail-state] (fn [cmail-state] (:collapsed cmail-state))]
   :show-premium-picker?  [[:base] (fn [base] (:show-premium-picker? base))]
   :show-section-add-cb   [[:base] (fn [base] (:show-section-add-cb base))]
   :show-add-post-tooltip      [[:nux] (fn [nux] (:show-add-post-tooltip nux))]
   :show-edit-tooltip          [[:nux] (fn [nux] (:show-edit-tooltip nux))]
   :show-post-added-tooltip    [[:nux] (fn [nux] (:show-post-added-tooltip nux))]
   :show-invite-people-tooltip [[:nux] (fn [nux] (:show-invite-people-tooltip nux))]
   :nux-user-type              [[:nux] (fn [nux] (:user-type nux))]
   ;; Cmail
   :cmail-state           [[:base] (fn [base] (get-in base dis/cmail-state-key))]
   :cmail-data            [[:base] (fn [base] (get-in base dis/cmail-data-key))]
   :reminders-data        [[:base :org-slug] (fn [base org-slug]
                                               (get-in base (dis/reminders-data-key org-slug)))]
   :reminders-roster      [[:base :org-slug] (fn [base org-slug]
                                               (get-in base (dis/reminders-roster-key org-slug)))]
   :reminder-edit         [[:base :org-slug] (fn [base org-slug]
                                               (get-in base (dis/reminder-edit-key org-slug)))]
   :foc-layout            [[:base] (fn [base] (:foc-layout base))]
   :theme                 [[:base] (fn [base] (get-in base dis/theme-key))]
   :users-info-hover      [[:base :org-slug] (fn [base org-slug] (get-in base (dis/users-info-hover-key org-slug)))]
   :active-users          [[:base :org-slug] (fn [base org-slug] (get-in base (dis/active-users-key org-slug)))]
   :mention-users         [[:base :org-slug] (fn [base org-slug] (get-in base (dis/mention-users-key org-slug)))]
   :follow-list           [[:base :org-slug] (fn [base org-slug] (get-in base (dis/follow-list-key org-slug)))]
   :follow-list-last-added [[:base :org-slug] (fn [base org-slug] (get-in base (dis/follow-list-last-added-key org-slug)))]
   :followers-count       [[:base :org-slug] (fn [base org-slug] (get-in base (dis/followers-count-key org-slug)))]
   :followers-publishers-count [[:base :org-slug] (fn [base org-slug] (get-in base (dis/followers-publishers-count-key org-slug)))]
   :followers-boards-count [[:base :org-slug] (fn [base org-slug] (get-in base (dis/followers-boards-count-key org-slug)))]
   :follow-publishers-list [[:base :org-slug] (fn [base org-slug] (get-in base (dis/follow-publishers-list-key org-slug)))]
   :follow-boards-list    [[:base :org-slug] (fn [base org-slug] (get-in base (dis/follow-boards-list-key org-slug)))]
   :comment-reply-to      [[:base :org-slug] (fn [base org-slug] (get-in base (dis/comment-reply-to-key org-slug)))]
   :show-invite-box       [[:base] (fn [base] (get base dis/show-invite-box-key))]
   :can-compose           [[:org-data] (fn [org-data] (get org-data dis/can-compose-key))]
   :foc-menu-open         [[:base] (fn [base] (get base :foc-menu-open))]})

