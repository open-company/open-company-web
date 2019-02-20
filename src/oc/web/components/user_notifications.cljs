(ns oc.web.components.user-notifications
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn- has-new-content? [notifications-data]
  (some :unread notifications-data))

(defn- close-tray [s]
  (reset! (::tray-open s) false)
  (user-actions/read-notifications))

(rum/defcs user-notifications < rum/reactive
                                (drv/drv :user-notifications)
                                (drv/drv :show-add-post-tooltip)
                                (rum/local false ::tray-open)
                                (on-window-click-mixin (fn [s e]
                                 (when-not (utils/event-inside? e (rum/ref-node s :read-bt))
                                   (close-tray s))))
  [s]
  (let [user-notifications-data (drv/react s :user-notifications)
        has-new-content (has-new-content? user-notifications-data)]
    [:div.user-notifications
      [:button.mlb-reset.notification-bell-bt
        {:class (utils/class-set {:new has-new-content
                                  :active @(::tray-open s)})
         :on-click #(if @(::tray-open s)
                      (close-tray s)
                      (reset! (::tray-open s) true))}]
      [:div.user-notifications-tray
        {:class (utils/class-set {:hidden-tray (not @(::tray-open s))})}
        [:div.user-notifications-tray-header.group
          [:div.title "Notifications"]
          (when has-new-content
            [:button.mlb-reset.all-read-bt
              {:on-click #(user-actions/read-notifications)}
              "Mark all as read"])]
        [:div.user-notifications-tray-list
          (if (empty? user-notifications-data)
            [:div.user-notifications-tray-empty
              (all-caught-up)]
            (for [n user-notifications-data]
              [:div.user-notification.group
                {:class (utils/class-set {:unread (:unread n)})
                 :on-click (fn [e]
                             (let [entry-uuid (:uuid n)
                                   board-slug (:board-slug n)
                                   reminder? (:reminder? n)
                                   reminder (when reminder?
                                             (:reminder n))
                                   notification-type (when reminder?
                                                       (:notification-type reminder))]
                               (cond
                                 (and reminder?
                                      (= notification-type "reminder-alert"))
                                 (ui-compose @(drv/get-ref s :show-add-post-tooltip))
                                 (and reminder?
                                      (= notification-type "reminder-notification"))
                                 (nav-actions/show-reminders)
                                 (and entry-uuid
                                      board-slug
                                      (not (utils/event-inside? e (rum/ref-node s :read-bt))))
                                 (router/nav! (oc-urls/entry board-slug entry-uuid)))
                               (user-actions/hide-mobile-user-notifications)))
                 :key (str "user-notification-" (:created-at n))}
                (user-avatar-image (:author n))
                [:div.user-notification-title
                  (:title n)]
                [:div.user-notification-body.oc-mentions.oc-mentions-hover
                  {:dangerouslySetInnerHTML (utils/emojify (:body n))}]
                [:div.user-notification-time-since
                  [:time
                    {:date-time (:created-at n)
                     :data-toggle "tooltip"
                     :data-placement "top"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-string (utils/js-date (:created-at n)))}
                    (utils/time-since (:created-at n))]]
                (when (:unread n)
                  [:button.mlb-reset.read-bt
                    {:title "Mark as read"
                     :ref :read-bt
                     :data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :on-click #(user-actions/read-notification n)}])]))]]]))