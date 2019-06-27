(ns oc.web.components.user-notifications
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.activity :as am]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
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
                                ui-mixins/refresh-tooltips-mixin
                                (am/truncate-element-mixin "div.user-notification-body" (* 18 3))
                                (ui-mixins/on-window-click-mixin (fn [s e]
                                 (when-not (utils/event-inside? e (rum/ref-node s :read-bt))
                                   (close-tray s))))
  [s]
  (let [user-notifications-data (drv/react s :user-notifications)
        has-new-content (has-new-content? user-notifications-data)
        is-mobile? (responsive/is-mobile-size?)]
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
          (when-not has-new-content
            [:button.mlb-reset.all-read-bt
              {:on-click #(user-actions/read-notifications)
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :data-container "body"
               :title "Mark all as read"}])
          [:div.title "Notifications"]
          (if is-mobile?
            [:button.mlb-reset.user-notifications-tray-mobile-close
              {:on-click #(user-actions/hide-mobile-user-notifications)}]
            [:button.mlb-reset.notification-settings-bt
              {:on-click #(do
                            (close-tray s)
                            (nav-actions/show-user-settings :notifications))
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :data-container "body"
               :title "Notification settings"}])]
        [:div.user-notifications-tray-list
          (if (empty? user-notifications-data)
            [:div.user-notifications-tray-empty
              (all-caught-up)]
            (for [n user-notifications-data
                  :let [entry-uuid (:uuid n)
                        board-slug (:board-slug n)
                        reminder? (:reminder? n)
                        reminder (when reminder?
                                   (:reminder n))
                        notification-type (when reminder?
                                            (:notification-type reminder))
                        ;; Base string for the key of the React child
                        children-key-base (str "user-notification-" (:created-at n) "-")
                        ;; add a unique part to the key to make sure the children are rendered
                        children-key (str children-key-base
                                      (if (seq entry-uuid)
                                        entry-uuid
                                        (if (and reminder?
                                                 (seq (:uuid reminder)))
                                          (:uuid reminder)
                                          (rand 1000))))]]
              [:div.user-notification.group
                {:class (utils/class-set {:unread (:unread n)})
                 :on-click (fn [e]
                             (when (fn? (:click n))
                               ((:click n)))
                             (user-actions/hide-mobile-user-notifications))
                 :key children-key}
                (user-avatar-image (:author n))
                [:div.user-notification-title
                  (:title n)]
                [:div.user-notification-time-since
                  [:time
                    {:date-time (:created-at n)
                     :data-toggle "tooltip"
                     :data-placement "top"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-container "body"
                     :data-title (utils/tooltip-date (:created-at n))}
                    (utils/time-since (:created-at n) [:short])]]
                [:div.user-notification-body.oc-mentions.oc-mentions-hover
                  {:dangerouslySetInnerHTML (utils/emojify (:body n))}]]))]]]))