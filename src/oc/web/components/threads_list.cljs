(ns oc.web.components.threads-list
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
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.post-authorship :refer (post-authorship)]
            [oc.web.components.ui.info-hover-views :refer (board-info-hover)]))

(rum/defc thread-timestamp
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

(rum/defc thread-header
  [{last-activity-at :last-activity-at {:keys [board-name published-at] :as activity-data} :activity-data}]
  [:div.thread-item-header
    [:div.thread-item-board-name-container
      (board-info-hover {:activity-data activity-data})
      [:span.board-name
        board-name]]
    [:div.separator-dot]
    [:span.time-since
      [:time
        {:date-time published-at}
        (utils/tooltip-date published-at)]]])

(rum/defc thread-attribution
  [{:keys [authorship-map current-user-id unread timestamp is-mobile?] :as props}]
  [:div.thread-item-attribution
    (post-authorship {:activity-data authorship-map
                      :user-avatar? true
                      :user-hover? true
                      :hide-last-name? true
                      :activity-board? false
                      :current-user-id current-user-id})
    (when timestamp
      [:div.separator-dot])
    (when timestamp
      (thread-timestamp props))
    (when unread
      [:div.separator-dot])
    (when unread
      [:div.new-tag
        "NEW"])])

(rum/defc thread-body
  [{:keys [parent-uuid uuid body]}]
  [:div.thread-body-container.comment
    {:class (utils/class-set {:reply (seq parent-uuid)})}
    [:div.thread-body.oc-mentions.oc-mentions-hover
      {:dangerouslySetInnerHTML (utils/emojify body)}]])

(rum/defcs thread-item < rum/static
  [s
   {current-user-id  :current-user-id
    resource-uuid    :resource-uuid
    comment-uuid     :uuid
    last-activity-at :last-activity-at
    activity-data    :activity-data
    replies          :replies
    author           :author
    unread           :unread
    created-at       :created-at
    open-item        :open-item
    close-item       :close-item
    :as n}]
  (let [is-mobile? (responsive/is-mobile-size?)]
    [:div.thread-item.group
      {:class    (utils/class-set {:unread unread
                                   :close-item close-item
                                   :open-item open-item})
       :ref :thread
       :on-click (fn [e]
                   (let [thread-el (rum/ref-node s :thread)]
                     (when (and (fn? (:click n))
                                (not (utils/button-clicked? e))
                                (not (utils/input-clicked? e))
                                (not (utils/anchor-clicked? e))
                                (not (utils/event-inside? e (.querySelector thread-el "div.add-comment-box-container"))))
                       ((:click n)))))}
      (when open-item
        (thread-header n))
      (when open-item
        [:div.thread-item-title
          (:headline activity-data)])
      [:div.thread-item-blocks.group
        [:div.thread-item-block.vertical-line.group
          (thread-attribution {:authorship-map {:author author
                                                :board-slug (:board-slug activity-data)
                                                :board-name (:board-name activity-data)
                                                :is-mobile? is-mobile?}
                               :current-user-id current-user-id
                               :unread unread
                               :is-mobile? is-mobile?
                               :timestamp created-at})
          (thread-body n)
          (for [r replies
                :let [reply-authorship-map {:author (:author r)
                                            :board-slug (:board-slug activity-data)
                                            :board-name (:board-name activity-data)
                                            :is-mobile? is-mobile?}]]
            [:div.thread-item-block.horizontal-line.group
              {:key (str "unir-" (:created-at r) "-" (:uuid r))}
              (thread-attribution {:authorship-map reply-authorship-map
                                   :current-user-id current-user-id
                                   :unread (:unread r)
                                   :is-mobile? is-mobile?
                                   :timestamp (:created-at r)})
              (thread-body r)])]]

      (rum/with-key (add-comment {:activity-data activity-data
                                  :parent-comment-uuid resource-uuid
                                  :collapsed? true
                                  :add-comment-placeholder "Reply..."
                                  :add-comment-cb (partial user-actions/activity-reply-inline n)
                                  :add-comment-focus-prefix "thread-comment"})
       (str "adc-" "-" resource-uuid last-activity-at))]))

(rum/defcs threads-list-inner <
  ui-mixins/refresh-tooltips-mixin
  [s {:keys [items-to-render current-user-data]}]
  (let [is-mobile? (responsive/is-mobile-size?)]
    [:div.threads-list
      (if (empty? items-to-render)
        [:div.threads-list-empty
          (all-caught-up)]
        (for [item* items-to-render
              :let [caught-up? (= (:content-type item*) :caught-up)
                    item (assoc item* :current-user-data current-user-data)]]
          (if caught-up?
            [:div.threads-list-caught-up
              {:key (str "threads-caught-up-" (:last-activity-at item))}
              (all-caught-up "You’re all caught up")]
            (rum/with-key
             (thread-item item)
             (str "thread-" (:resource-uuid item) "-" (:uuid item))))))]))

(rum/defcs threads-list <
  rum/reactive
  (drv/drv :items-to-render)
  (drv/drv :current-user-data)
  [s]
  (let [items-to-render (drv/react s :items-to-render)]
    (threads-list-inner {:items-to-render items-to-render
                         :current-user-data (drv/react s :current-user-data)})))