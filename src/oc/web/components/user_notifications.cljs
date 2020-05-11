(ns oc.web.components.user-notifications
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.activity :as am]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.components.stream-item :refer (stream-item)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.post-authorship :refer (post-authorship)]))

(rum/defc user-notification-timestamp
  [{:keys [timestamp is-mobile?]}]
  [:span.time-since
    {:data-toggle (when-not is-mobile? "tooltip")
     :data-placement "top"
     :data-container "body"
     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
     :data-title (utils/activity-date-tooltip timestamp)}
    [:time
      {:date-time timestamp}
      (utils/foc-date-time timestamp)]])

(rum/defc user-notification-attribution
  [{:keys [authorship-map current-user-id unread timestamp is-mobile?] :as props}]
  [:div.user-notification-header
    (post-authorship {:activity-data authorship-map
                      :user-avatar? true
                      :user-hover? true
                      :hide-last-name? true
                      :activity-board? false
                      :current-user-id current-user-id})
    (when timestamp
      [:div.separator-dot])
    (when timestamp
      (user-notification-timestamp props))
    (when unread
      [:div.separator-dot])
    (when unread
      [:div.new-tag
        "NEW"])])

(rum/defc user-notification-body
  [{:keys [parent-interaction-id interaction-id body]}]
  [:div.user-notification-body-container
    {:class (utils/class-set {:comment (seq interaction-id)
                              :reply (seq parent-interaction-id)})}
    [:div.user-notification-body.oc-mentions.oc-mentions-hover
      {:dangerouslySetInnerHTML (utils/emojify body)}]])

(rum/defcs user-notification-item < rum/static
  [s
   {current-user-id       :current-user-id
    entry-id              :entry-id
    latest-notify-at      :latest-notify-at
    activity-data         :activity-data
    notifications         :notifications
    replies               :replies
    :as n}]
  (let [is-mobile? (responsive/is-mobile-size?)]
    [:div.user-notification.group
      {:class    (utils/class-set {:unread (:unread n)})
       :ref :user-notification
       :on-click (fn [e]
                   (let [user-notification-el (rum/ref-node s :user-notification)]
                     (when (and (fn? (:click n))
                                (not (utils/button-clicked? e))
                                (not (utils/input-clicked? e))
                                (not (utils/anchor-clicked? e))
                                (not (utils/event-inside? e (.querySelector user-notification-el "div.add-comment-box-container"))))
                       ((:click n)))))}
      (when (:headline activity-data)
        [:div.user-notification-title
          (:headline activity-data)])

      [:div.user-notification-blocks.group

        (for [c notifications
              :let [notification-authorship-map {:publisher (:author c)
                                                 :board-slug (:board-slug activity-data)
                                                 :board-name (:board-name activity-data)
                                                 :is-mobile? is-mobile?}]]
          [:div.user-notification-block.group.vertical-line
            {:key (str "unic-" (:notify-at c) "-" (:interaction-id c))}
            (user-notification-attribution {:authorship-map notification-authorship-map
                                            :current-user-id current-user-id
                                            :unread (:unread c)
                                            :is-mobile? is-mobile?
                                            :timestamp (:notify-at c)})
            (user-notification-body (select-keys c [:interaction-id :parent-interaction-id :body]))

            (for [r (:replies c)
                  :let [reply-authorship-map {:publisher (:author r)
                                              :board-slug (:board-slug activity-data)
                                              :board-name (:board-name activity-data)
                                              :is-mobile? is-mobile?}]]
              [:div.user-notification-block.horizontal-line.group
                {:key (str "unicr-" (:notify-at r) "-" (:interaction-id r))}
                (user-notification-attribution {:authorship-map reply-authorship-map
                                                :current-user-id current-user-id
                                                :unread (:unread r)
                                                :is-mobile? is-mobile?
                                                :timestamp (:notify-at r)})
                (user-notification-body (select-keys r [:interaction-id :parent-interaction-id :body]))])])

        (for [r replies
              :let [reply-authorship-map {:publisher (:author r)
                                          :board-slug (:board-slug activity-data)
                                          :board-name (:board-name activity-data)
                                          :is-mobile? is-mobile?}]]
          [:div.user-notification-block.vertical-line.group
            {:key (str "unir-" (:notify-at r) "-" (:interaction-id r))}
            (user-notification-attribution {:authorship-map reply-authorship-map
                                            :current-user-id current-user-id
                                            :unread (:unread r)
                                            :is-mobile? is-mobile?
                                            :timestamp (:notify-at r)})
            (user-notification-body (select-keys r [:interaction-id :parent-interaction-id :body]))])]

      (when activity-data
        (rum/with-key (add-comment {:activity-data activity-data
                                    :parent-comment-uuid (when (-> notifications count (= 1)) (-> notifications first :interaction-id))
                                    :collapsed? true})
         (str "adc-" "-" entry-id latest-notify-at)))]))

(defn has-new-content? [notifications-data]
  (some :unread notifications-data))

(defn- fix-notification [n]
  (if (and (not (map? (:activity-data n)))
           (not (:reminder? n)))
    (assoc n :activity-data (dis/activity-data (:uuid n)))
    n))

(rum/defcs user-notifications < rum/static
                                rum/reactive
                                (drv/drv :user-notifications)
                                (drv/drv :posts-data)
                                ui-mixins/refresh-tooltips-mixin
                                (am/truncate-element-mixin "div.user-notification-body" (* 18 3))
  [s {:keys [tray-open]}]
  (let [{user-notifications-data :grouped all-notifications :sorted :as x} (drv/react s :user-notifications)
        has-new-content (has-new-content? all-notifications)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.user-notifications-tray
      {:class (utils/class-set {:hidden-tray (not tray-open)})}
      [:div.user-notifications-tray-header.group
        [:div.title "Threads"]
        (when has-new-content
          [:button.mlb-reset.all-read-bt
            {:on-click #(user-actions/read-notifications)
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :data-container "body"
             :title "Mark all as read"}])]
      [:div.user-notifications-tray-list
        (if (empty? user-notifications-data)
          [:div.user-notifications-tray-empty
            (all-caught-up)]
          (for [n user-notifications-data
                :let [entry-id (:entry-id n)
                      interaction-id (:interaction-id n)
                      parent-interaction-id (:parent-interaction-id n)
                      board-slug (:board-slug n)
                      children-key (str "uni-" (:notify-at n)
                                    (cond
                                      (seq parent-interaction-id)
                                      (str "-" parent-interaction-id)
                                      (seq interaction-id)
                                      (str "-" interaction-id)
                                      (seq entry-id)
                                      (str "-" entry-id)
                                      (:reminder? n)
                                      (str "-" (:uuid (:reminder n)))))]]
            (rum/with-key (user-notification-item n) children-key)))]]))

(defn- close-tray [s]
  (reset! (::tray-open s) false)
  (user-actions/read-notifications))

(rum/defcs user-notifications-button < rum/static
                                       rum/reactive
                                       (drv/drv :unread-notifications-count)
                                       (rum/local false ::tray-open)
                                       ; (rum/local (rum/create-ref) ::list-ref)
                                       ui-mixins/refresh-tooltips-mixin
                                       (ui-mixins/on-window-click-mixin (fn [s e]
                                        (when-let [user-notifications-node (rum/ref-node s "user-notifications-list")]
                                          (when (and @(::tray-open s)
                                                     (.-parentElement user-notifications-node)
                                                     (not (utils/event-inside? e user-notifications-node)))

                                            (close-tray s)))))
                                       {:will-mount (fn [s]
                                         (when (responsive/is-mobile-size?)
                                           (dom-utils/lock-page-scroll))
                                        s)
                                        :will-unmount (fn [s]
                                         (when (responsive/is-mobile-size?)
                                           (dom-utils/unlock-page-scroll))
                                        s)}
  [s]
  (let [unread-notifications-count (drv/react s :unread-notifications-count)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.user-notifications
      [:button.mlb-reset.notification-bell-bt
        {:class (utils/class-set {:new (pos? unread-notifications-count)
                                  :active @(::tray-open s)})
         :data-toggle (when-not is-mobile? "tooltip")
         :data-placement "bottom"
         :title "Notifications"
         :on-click #(if @(::tray-open s)
                      (close-tray s)
                      (reset! (::tray-open s) true))}
        [:span.bell-icon]]
      (rum/with-ref
       (user-notifications {:tray-open @(::tray-open s)})
       "user-notifications-list")]))
