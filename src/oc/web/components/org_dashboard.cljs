(ns oc.web.components.org-dashboard
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [clojure.string :as s]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.stores.search :as search]
            [oc.web.components.qsg :refer (qsg)]
            [oc.web.lib.whats-new :as whats-new]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.wrt :refer (wrt)]
            [oc.web.components.cmail :refer (cmail)]
            [oc.web.components.ui.menu :refer (menu)]
            [oc.web.actions.section :as section-actions]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.search :refer (search-box)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.org-settings :refer (org-settings)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.shared-misc :refer (video-lightbox)]
            [oc.web.components.ui.section-editor :refer (section-editor)]
            [oc.web.components.ui.activity-share :refer (activity-share)]
            [oc.web.components.dashboard-layout :refer (dashboard-layout)]
            [oc.web.components.qsg-digest-sample :refer (qsg-digest-sample)]
            [oc.web.components.ui.activity-removed :refer (activity-removed)]
            [oc.web.components.user-profile-modal :refer (user-profile-modal)]
            [oc.web.components.org-settings-modal :refer (org-settings-modal)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [oc.web.components.user-notifications :refer (user-notifications)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.ui.activity-not-found :refer (activity-not-found)]
            [oc.web.components.invite-settings-modal :refer (invite-settings-modal)]
            [oc.web.components.team-management-modal :refer (team-management-modal)]
            [oc.web.components.recurring-updates-modal :refer (recurring-updates-modal)]
            [oc.web.components.ui.made-with-carrot-modal :refer (made-with-carrot-modal)]
            [oc.web.components.user-notifications-modal :refer (user-notifications-modal)]
            [oc.web.components.edit-recurring-update-modal :refer (edit-recurring-update-modal)]
            [oc.web.components.integrations-settings-modal :refer (integrations-settings-modal)]))

(defn refresh-board-data [s]
  (when (and (not (router/current-activity-id))
             (router/current-board-slug))
    (utils/after 100 (fn []
     (let [{:keys [org-data
                   board-data
                   ap-initial-at]} @(drv/get-ref s :org-dashboard-data)]
       (cond

        (= (router/current-board-slug) "all-posts")
        (activity-actions/all-posts-get org-data ap-initial-at)

        (= (router/current-board-slug) "must-see")
        (activity-actions/must-see-get org-data)

        :default
        (when-let* [fixed-board-data (or board-data
                     (some #(when (= (:slug %) (router/current-board-slug)) %) (:boards org-data)))
                    board-link (utils/link-for (:links fixed-board-data) ["item" "self"] "GET")]
          (section-actions/section-get board-link))))))))

(rum/defcs org-dashboard < ;; Mixins
                           rum/static
                           rum/reactive
                           (ui-mixins/render-on-resize nil)
                           ;; Derivatives
                           (drv/drv :org-dashboard-data)
                           (drv/drv search/search-key)
                           (drv/drv search/search-active?)
                           (drv/drv :qsg)

                           {:did-mount (fn [s]
                             (utils/after 100 #(set! (.-scrollTop (.-body js/document)) 0))
                             (refresh-board-data s)
                             (whats-new/init)
                             s)
                            :did-remount (fn [s]
                             (whats-new/init)
                             s)}
  [s]
  (let [{:keys [orgs
                org-data
                jwt
                board-data
                container-data
                posts-data
                ap-initial-at
                made-with-carrot-modal-data
                is-sharing-activity
                is-showing-alert
                show-section-add-cb
                activity-share-container
                show-cmail
                showing-mobile-user-notifications
                wrt-read-data
                force-login-wall
                panel-stack]} (drv/react s :org-dashboard-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        search-active? (drv/react s search/search-active?)
        search-results? (pos?
                         (count
                          (:results (drv/react s search/search-key))))
        loading? (or ;; the org data are not loaded yet
                     (not org-data)
                     ;; No board specified
                     (and (not (router/current-board-slug))
                          ;; but there are some
                          (pos? (count (:boards org-data))))
                     ;; Board specified
                     (and (not= (router/current-board-slug) "all-posts")
                          (not= (router/current-board-slug) "must-see")
                          (not ap-initial-at)
                          ;; But no board data yet
                          (not board-data))
                     ;; Another container
                     (and (or (= (router/current-board-slug) "all-posts")
                              (= (router/current-board-slug) "must-see")
                              ap-initial-at)
                          ;; But no all-posts data yet
                         (not container-data)))
        org-not-found (and (not (nil? orgs))
                           (not ((set (map :slug orgs)) (router/current-org-slug))))
        section-not-found (and (not org-not-found)
                               org-data
                               (not= (router/current-board-slug) "all-posts")
                               (not= (router/current-board-slug) "must-see")
                               (not ((set (map :slug (:boards org-data))) (router/current-board-slug))))
        entry-not-found (and (not section-not-found)
                             (or (and (router/current-activity-id)
                                      board-data)
                                 (and ap-initial-at
                                      (not (jwt/user-is-part-of-the-team (:team-id org-data))))
                                 (and ap-initial-at
                                      container-data))
                             (not (nil? posts-data))
                             (or (and (router/current-activity-id)
                                      (not ((set (keys posts-data)) (router/current-activity-id))))
                                 (and ap-initial-at
                                      (not ((set (map :published-at (vals posts-data))) ap-initial-at)))))
        show-activity-not-found (and (not jwt)
                                     (or force-login-wall
                                         (and (router/current-activity-id)
                                              (or org-not-found
                                                  section-not-found
                                                  entry-not-found))))
        show-activity-removed (and jwt
                                   (or (router/current-activity-id)
                                       ap-initial-at)
                                   (or org-not-found
                                       section-not-found
                                       entry-not-found))
        is-loading (and (not show-activity-not-found)
                        (not show-activity-removed)
                        loading?)
        is-showing-mobile-search (and is-mobile? search-active?)
        qsg-data (drv/react s :qsg)
        open-panel (last panel-stack)
        show-section-editor (= open-panel :section-edit)
        show-section-add (= open-panel :section-add)
        show-reminders? (= open-panel :reminders)
        show-reminder-edit? (and open-panel
                                 (s/starts-with? (name open-panel) "reminder-"))
        show-reminders-view? (or show-reminders? show-reminder-edit?)
        show-wrt-view? (and open-panel
                            (s/starts-with? (name open-panel) "wrt-"))]
    (if is-loading
      [:div.org-dashboard
        (loading {:loading true})]
      [:div
        {:class (utils/class-set {:org-dashboard true
                                  :mobile-or-tablet is-mobile?
                                  :activity-not-found show-activity-not-found
                                  :activity-removed show-activity-removed
                                  :expanded-activity (router/current-activity-id)
                                  :showing-qsg (:visible qsg-data)
                                  :showing-digest-sample (:qsg-show-sample-digest-view qsg-data)
                                  :show-menu (= open-panel :menu)})}
        ;; Use cond for the next components to exclud each other and avoid rendering all of them
        (login-overlays-handler)
        (cond
          ;; User menu
          (and is-mobile?
               (= open-panel :menu))
          (menu)
          ;; Activity removed
          show-activity-removed
          (activity-removed)
          ;; Activity not found
          show-activity-not-found
          (activity-not-found)
          ;; Org settings
          (= open-panel :org)
          (org-settings-modal)
          ;; Integrations settings
          (= open-panel :integrations)
          (integrations-settings-modal)
          ;; Invite settings
          (= open-panel :invite)
          (invite-settings-modal)
          ;; Team management
          (= open-panel :team)
          (team-management-modal)
          ;; Billing
          (= open-panel :billing)
          (org-settings)
          ;; User settings
          (= open-panel :profile)
          (user-profile-modal)
          ;; User notifications
          (= open-panel :notifications)
          (user-notifications-modal)
          ;; Reminders list
          show-reminders?
          (recurring-updates-modal)
          ;; Edit a reminder
          show-reminder-edit?
          (edit-recurring-update-modal)
          ;; Made with carrot modal
          made-with-carrot-modal-data
          (made-with-carrot-modal)
          ;; Mobile create a new section
          show-section-editor
          (section-editor board-data
           (fn [sec-data note dismiss-action]
            (if sec-data
              (section-actions/section-save sec-data note dismiss-action)
              (dismiss-action))))
          ;; Mobile edit current section data
          show-section-add
          (section-editor nil show-section-add-cb)
          ;; Activity share for mobile
          (and is-mobile?
               is-sharing-activity)
          (activity-share)
          ;; WRT
          show-wrt-view?
          (wrt)
          ;; Search results
          is-showing-mobile-search
          (search-box)
          ;; Mobile notifications
          (and is-mobile?
               showing-mobile-user-notifications)
          (user-notifications))
        ;; Activity share modal for no mobile
        (when (and (not is-mobile?)
                   is-sharing-activity)
          ;; If we have an element id find the share container inside that element
          ;; and portal the component to that element
          (let [portal-element (when activity-share-container
                                  (.get (.find (js/$ (str "#" activity-share-container))
                                         ".activity-share-container") 0))]
            (if portal-element
              (rum/portal (activity-share) portal-element)
              (activity-share))))
        ;; cmail editor
        (when show-cmail
          (cmail))
        ;; Menu always rendered if not on mobile since we need the
        ;; selector for whats-new widget to be present
        (when-not is-mobile?
          (menu))
        ;; Alert modal
        (when is-showing-alert
          (alert-modal))
        ;; On mobile don't show the dashboard/stream when showing another panel
        (when (or (not is-mobile?)
                  (and (not is-sharing-activity)
                       (not show-cmail)
                       (not open-panel)))
          [:div.page
            (navbar)
            [:div.org-dashboard-container
              [:div.org-dashboard-inner
               (when (or (not is-mobile?)
                         (and (or (not search-active?) (not search-results?))
                              (not open-panel)
                              (not is-showing-mobile-search)
                              (not showing-mobile-user-notifications)))
                 (dashboard-layout))]]
            (when (:qsg-show-sample-digest-view qsg-data)
              (qsg-digest-sample))])
        (when (:visible qsg-data)
          [:div.qsg-main-container
            (qsg)
            (video-lightbox)])])))