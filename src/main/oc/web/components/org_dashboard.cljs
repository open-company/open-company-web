(ns oc.web.components.org-dashboard
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.mixins.theme :as theme-mixins]
            [oc.web.lib.whats-new :as whats-new]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.wrt :refer (wrt)]
            [oc.web.components.cmail :refer (cmail)]
            [oc.web.components.ui.menu :refer (menu)]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.search :refer (search-box)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.login-wall :refer (login-wall)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.expanded-post :refer (expanded-post)]
            ;; [oc.web.components.ui.follow-picker :refer (follow-picker)]
            [oc.web.components.ui.nux-tooltip :refer (nux-tooltips-manager nux-tooltip)]
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
            [oc.web.components.premium-picker-modal :refer (premium-picker-modal upgraded-banner)]
            [oc.web.components.theme-settings-modal :refer (theme-settings-modal)]
            [oc.web.components.team-management-modal :refer (team-management-modal)]
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
                           (drv/drv :app-loading?)
                           (drv/drv :theme)
                           (drv/drv :nux)
                           (drv/drv :route/dark-allowed)
                           (drv/drv :show-login-wall?)
                           (drv/drv :show-activity-removed?)
                           (drv/drv :current-panel)
                           (drv/drv :org-dashboard-data)
                           (theme-mixins/theme-mixin)

                           {:did-mount (fn [s]
                             (utils/after 100 #(set! (.-scrollTop (.-scrollingElement js/document)) (utils/page-scroll-top)))
                             (init-whats-new)
                             s)
                            :did-remount (fn [_ s]
                             (init-whats-new)
                             s)}
  [s]
  (let [loading? (drv/react s :app-loading?)
        theme-data (drv/react s :theme)
        {:keys [org-data
                show-alert-modal?
                activity-share-container
                collapsed-cmail
                user-info-data
                current-panel
                show-premium-picker?
                payments-ui-upgraded-banner
                ui-tooltip
                show-activity-share?
                initial-section-editing
                show-search?
                show-expanded-post?
                show-wrt-view?
                show-push-notification-permissions-modal?
                show-user-info?]} (drv/react s :org-dashboard-data)
        _route-dark-allowed (drv/react s :route/dark-allowed)
        nux (drv/react s :nux)
        is-mobile? (responsive/is-mobile-size?)
        show-login-wall (drv/react s :show-login-wall?)
        show-activity-removed (drv/react s :show-activity-removed?)
        is-loading (and (not show-login-wall)
                        (not show-activity-removed)
                        loading?)
        show-section-editor (= current-panel :section-edit)
        show-section-add (= current-panel :section-add)
        show-reminders? (= current-panel :reminders)
        show-reminder-edit? (and current-panel
                                 (s/starts-with? (name current-panel) "reminder-"))
        show-mobile-cmail? (and (not collapsed-cmail)
                                is-mobile?)
        show-push-notification-permissions-modal?(and ua/mobile-app?
                                                      show-push-notification-permissions-modal?)
        ;; show-follow-picker (= current-panel :follow-picker)
        mobile-search? (and is-mobile?
                            show-search?)]
    (if is-loading
      [:div.org-dashboard
        (loading {:loading true})]
      [:div
        {:class (utils/class-set {:org-dashboard true
                                  :login-wall show-login-wall
                                  :activity-removed show-activity-removed
                                  :expanded-activity show-expanded-post?
                                  :top-banner payments-ui-upgraded-banner
                                  :showing-upgraded-banner payments-ui-upgraded-banner
                                  :show-menu (= current-panel :menu)})}
        ;; Use cond for the next components to exclud each other and avoid rendering all of them
        (login-overlays-handler)
        (if nux
          (nux-tooltips-manager)
          (when ui-tooltip
            (nux-tooltip {:data ui-tooltip
                          :next-cb (:next-cb ui-tooltip)
                          :dismiss-cb (:next-cb ui-tooltip)})))
        
        (cond
          ;; Activity removed
          show-activity-removed
          (activity-removed)
          ;; Activity not found
          show-login-wall
          (login-wall)
          ;; Org settings
          (= current-panel :org)
          (org-settings-modal)
          ;; Integrations settings
          (= current-panel :integrations)
          (integrations-settings-modal)
          ;; Invite picker settings
          (= current-panel :invite-picker)
          (invite-picker-modal)
          ;; Invite via email
          (= current-panel :invite-email)
          (invite-email-modal)
          ;; Invite via Slack
          (= current-panel :invite-slack)
          (invite-slack-modal)
          ;; Team management
          (= current-panel :team)
          (team-management-modal)
          ;; User settings
          (= current-panel :profile)
          (user-profile-modal)
          ;; User notifications
          (= current-panel :notifications)
          (user-notifications-modal)
          ;; Reminders list
          show-reminders?
          (recurring-updates-modal)
          ;; Edit a reminder
          show-reminder-edit?
          (edit-recurring-update-modal)
          ;; Mobile create a new section
          show-section-editor
          (section-editor initial-section-editing)
          ;; Mobile edit current section data
          show-section-add
          (section-editor nil)
          ;; Activity share for mobile
          (and is-mobile?
               show-activity-share?)
          (activity-share)
          ;; WRT
          show-wrt-view?
          (wrt org-data)
          ;; UI Theme settings panel
          (= current-panel :theme)
          (theme-settings-modal theme-data)
          ;; User info modal
          show-user-info?
          (user-info-modal {:user-data user-info-data :org-data org-data})
          ;; Follow user picker
          ;; show-follow-picker
          ;; (follow-picker)
          ;; Mobile fullscreen search
          mobile-search?
          (search-box))
        (when payments-ui-upgraded-banner
          (upgraded-banner))
        ;; Activity share modal for no mobile
        (when (and (not is-mobile?)
                   show-activity-share?)
          ;; If we have an element id find the share container inside that element
          ;; and portal the component to that element
          (let [portal-element (when activity-share-container
                                  (.get (.find (js/$ (str "#" activity-share-container))
                                         ".activity-share-container") 0))]
            (if portal-element
              (rum/portal (activity-share) portal-element)
              (activity-share))))
        ;; Cmail mobile editor
        (when show-mobile-cmail?
          (cmail))
        ;; Menu always rendered if not on mobile since we need the
        ;; selector for whats-new widget to be present
        (when (or (not is-mobile?)
                  (and is-mobile?
                       (= current-panel :menu)))
          (menu))
        ;; Mobile push notifications permission
        (when show-push-notification-permissions-modal?
          (push-notifications-permission-modal))
        (when show-premium-picker?
          (premium-picker-modal))
        ;; Alert modal
        (when show-alert-modal?
          (alert-modal))
        ;; Page container
        (when (or ;; On mobile don't show the dashboard/stream when showing another panel
                  (not is-mobile?)
                  (and (not show-activity-share?)
                       (not show-mobile-cmail?)
                       (not show-push-notification-permissions-modal?)))
          [:div.page
            (navbar)
            [:div.org-dashboard-container
              [:div.org-dashboard-inner
               (dashboard-layout)]
              (when show-expanded-post?
                (expanded-post))]])])))
