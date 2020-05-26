(ns oc.web.components.org-dashboard
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.shared.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.whats-new :as whats-new]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.wrt :refer (wrt)]
            [oc.web.components.cmail :refer (cmail)]
            [oc.web.components.ui.menu :refer (menu)]
            [oc.web.actions.section :as section-actions]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.login-wall :refer (login-wall)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.expanded-post :refer (expanded-post)]
            [oc.web.components.ui.follow-picker :refer (follow-picker)]
            [oc.web.components.user-info-modal :refer (user-info-modal)]
            [oc.web.components.ui.section-editor :refer (section-editor)]
            [oc.web.components.ui.activity-share :refer (activity-share)]
            [oc.web.components.dashboard-layout :refer (dashboard-layout)]
            [oc.web.components.ui.activity-removed :refer (activity-removed)]
            [oc.web.components.user-profile-modal :refer (user-profile-modal)]
            [oc.web.components.org-settings-modal :refer (org-settings-modal)]
            [oc.web.components.invite-email-modal :refer (invite-email-modal)]
            [oc.web.components.invite-slack-modal :refer (invite-slack-modal)]
            [oc.web.components.invite-picker-modal :refer (invite-picker-modal)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.theme-settings-modal :refer (theme-settings-modal)]
            [oc.web.components.team-management-modal :refer (team-management-modal)]
            [oc.web.components.ui.trial-expired-banner :refer (trial-expired-banner)]
            [oc.web.components.payments-settings-modal :refer (payments-settings-modal)]
            [oc.web.components.recurring-updates-modal :refer (recurring-updates-modal)]
            [oc.web.components.user-notifications-modal :refer (user-notifications-modal)]
            [oc.web.components.edit-recurring-update-modal :refer (edit-recurring-update-modal)]
            [oc.web.components.integrations-settings-modal :refer (integrations-settings-modal)]
            [oc.web.components.push-notifications-permission-modal :refer (push-notifications-permission-modal)]))

(defn- init-whats-new []
  (when-not (responsive/is-tablet-or-mobile?)
    (whats-new/init)))

(rum/defcs org-dashboard < ;; Mixins
                           rum/static
                           rum/reactive
                           (ui-mixins/render-on-resize nil)
                           ;; Derivatives
                           (drv/drv :org-dashboard-data)
                           (drv/drv :user-responded-to-push-permission?)
                           (drv/drv :ui-theme)

                           {:did-mount (fn [s]
                             (utils/after 100 #(set! (.-scrollTop (.-scrollingElement js/document)) (utils/page-scroll-top)))
                             (init-whats-new)
                             s)
                            :did-remount (fn [_ s]
                             (init-whats-new)
                             s)}
  [s]
  (let [{:keys [orgs
                org-data
                jwt-data
                board-data
                contributions-data
                initial-section-editing
                container-data
                posts-data
                is-sharing-activity
                is-showing-alert
                show-section-add-cb
                activity-share-container
                cmail-state
                wrt-read-data
                force-login-wall
                panel-stack
                app-loading
                payments-data
                user-info-data
                active-users]} (drv/react s :org-dashboard-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        loading? (or ;; force loading screen
                     app-loading
                     ;; the org data are not loaded yet
                     (not org-data)
                     ;; No board or contributions specified
                     (and (not (router/current-board-slug))
                          (not (router/current-contributions-id))
                          ;; but there are some
                          (pos? (count (:boards org-data))))
                     ;; Active users have not been loaded yet:
                     ;; they are blocking since they are used to:
                     ;; - init entries body and comments body for mentions
                     ;; - show the people list in the navigation sidebar
                     ;; - show user's info on hover and in profile panels
                     ;; - on mobile it's not blocking since cmail is closed
                     (and (not is-mobile?)
                          (not (map? active-users))))
        org-not-found (and (not (nil? orgs))
                           (not ((set (map :slug orgs)) (router/current-org-slug))))
        section-not-found (and (not org-not-found)
                               org-data
                               (not (router/current-contributions-id))
                               (not (dis/is-container? (router/current-board-slug)))
                               (not ((set (map :slug (:boards org-data))) (router/current-board-slug))))
        current-activity-data (when (router/current-activity-id)
                                (get posts-data (router/current-activity-id)))
        entry-not-found (and ;; org is present
                             (not org-not-found)
                             ;; Users for mentions has not been loaded
                             (not (map? active-users))
                             ;; section is present
                             (not section-not-found)
                             ;; route is for a single post and it's been loaded
                             current-activity-data
                             ;; post wasn't found
                             (or (= current-activity-data :404)
                                 ;; route has wrong board slug/uuid for the current post
                                 (and (not= (:board-slug current-activity-data) (router/current-board-slug))
                                      (not= (:board-uuid current-activity-data) (router/current-board-slug)))))
        show-login-wall (and (not jwt-data)
                             (or force-login-wall
                                 (and (router/current-activity-id)
                                     (or org-not-found
                                         section-not-found
                                         entry-not-found))))
        show-activity-removed (and jwt-data
                                   (router/current-activity-id)
                                   (or org-not-found
                                       section-not-found
                                       entry-not-found))
        is-loading (and (not show-login-wall)
                        (not show-activity-removed)
                        loading?)
        open-panel (last panel-stack)
        show-section-editor (= open-panel :section-edit)
        show-section-add (= open-panel :section-add)
        show-reminders? (= open-panel :reminders)
        show-reminder-edit? (and open-panel
                                 (s/starts-with? (name open-panel) "reminder-"))
        show-reminders-view? (or show-reminders? show-reminder-edit?)
        show-wrt-view? (and open-panel
                            (s/starts-with? (name open-panel) "wrt-"))
        show-mobile-cmail? (and cmail-state
                                (not (:collapsed cmail-state))
                                is-mobile?)
        user-responded-to-push-permission? (drv/react s :user-responded-to-push-permission?)
        show-push-notification-permissions-modal? (and ua/mobile-app?
                                                       jwt-data
                                                       (not user-responded-to-push-permission?))
        show-trial-expired? (payments-actions/show-paywall-alert? payments-data)
        show-user-info? (and open-panel
                             (s/starts-with? (name open-panel) "user-info-"))
        show-follow-picker (= open-panel :follow-picker)]
    (if is-loading
      [:div.org-dashboard
        (loading {:loading true})]
      [:div
        {:class (utils/class-set {:org-dashboard true

                                  :mobile-or-tablet is-mobile?
                                  :login-wall show-login-wall
                                  :activity-removed show-activity-removed
                                  :expanded-activity (router/current-activity-id)
                                  :trial-expired show-trial-expired?
                                  :show-menu (= open-panel :menu)})}
        ;; Use cond for the next components to exclud each other and avoid rendering all of them
        (login-overlays-handler)
        (cond
          ;; Activity removed
          show-activity-removed
          (activity-removed)
          ;; Activity not found
          show-login-wall
          (login-wall)
          (= open-panel :payments)
          (payments-settings-modal {:org-data org-data})
          ;; Org settings
          (= open-panel :org)
          (org-settings-modal)
          ;; Integrations settings
          (= open-panel :integrations)
          (integrations-settings-modal)
          ;; Invite picker settings
          (= open-panel :invite-picker)
          (invite-picker-modal)
          ;; Invite via email
          (= open-panel :invite-email)
          (invite-email-modal)
          ;; Invite via Slack
          (= open-panel :invite-slack)
          (invite-slack-modal)
          ;; Team management
          (= open-panel :team)
          (team-management-modal)
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
          ;; Mobile create a new section
          show-section-editor
          (section-editor initial-section-editing
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
          (wrt org-data)
          ;; UI Theme settings panel
          (= open-panel :theme)
          (theme-settings-modal (drv/react s :ui-theme))
          ;; User info modal
          show-user-info?
          (user-info-modal {:user-data user-info-data :org-data org-data})
          ;; Follow user picker
          show-follow-picker
          (follow-picker))
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
        (when show-mobile-cmail?
          (cmail))
        ;; Menu always rendered if not on mobile since we need the
        ;; selector for whats-new widget to be present
        (when (or (not is-mobile?)
                  (and is-mobile?
                       (= open-panel :menu)))
          (menu))
        ;; Mobile push notifications permission
        (when show-push-notification-permissions-modal?
          (push-notifications-permission-modal))
        ;; Alert modal
        (when is-showing-alert
          (alert-modal))
        
        (when (and ;; On mobile don't show the dashboard/stream when showing another panel
                   (or (not is-mobile?)
                       (and (not is-sharing-activity)
                            (not show-mobile-cmail?)
                            (not show-push-notification-permissions-modal?))))
          [:div.page
            (when show-trial-expired?
              (trial-expired-banner))
            (navbar)
            [:div.org-dashboard-container
              [:div.org-dashboard-inner
               (dashboard-layout)]
              (when (router/current-activity-id)
                (expanded-post))]])])))
