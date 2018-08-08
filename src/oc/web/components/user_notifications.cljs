(ns oc.web.components.user-notifications
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(def notifications
  [{:body "Kim Walker mentioned you in a comment: <span class=\"oc-mention\" data-found=\"true\">@Ryan Le Roux</span> Ah ok, I understand now. Thank you…"
    :created-at "2018-08-08T10:21:51.899Z"
    :author {:avatar-url "https://d1wc0stj82keig.cloudfront.net/img/ML/happy_face_yellow.svg"
             :name "Kim Walker"}
    :unread true}
   {:body "Miriam Thomas commented on your post: I’m not  totally clear on how this is going to help…"
    :created-at "2018-08-08T10:21:51.899Z"
    :author {:avatar-url "https://d1wc0stj82keig.cloudfront.net/img/ML/happy_face_red.svg"
             :name "Miriam Thomas"}
    :unread false}
   {:body "Iacopo, Stuart, and 6 others commented on your post Design standup for Tuesday May…"
    :created-at "2018-08-08T10:21:51.899Z"
    :author {:avatar-url "https://avatars.slack-edge.com/2017-02-02/136114833346_3758034af26a3b4998f4_512.jpg"
             :name "Iacopo Carraro"}
    :unread true}
   {:body "Kim Walker mentioned you in a comment: <span class=\"oc-mention\" data-found=\"true\">@Ryan Le Roux</span> Ah ok, I understand now. Thank you…"
    :created-at "2018-08-08T10:21:51.899Z"
    :author {:avatar-url "https://d1wc0stj82keig.cloudfront.net/img/ML/happy_face_yellow.svg"
             :name "Kim Walker"}
    :unread true}
   {:body "Miriam Thomas commented on your post: I’m not  totally clear on how this is going to help…"
    :created-at "2018-08-08T10:21:51.899Z"
    :author {:avatar-url "https://d1wc0stj82keig.cloudfront.net/img/ML/happy_face_red.svg"
             :name "Miriam Thomas"}
    :unread false}
   {:body "Iacopo, Stuart, and 6 others commented on your post Design standup for Tuesday May…"
    :created-at "2018-08-08T10:21:51.899Z"
    :author {:avatar-url "https://avatars.slack-edge.com/2017-02-02/136114833346_3758034af26a3b4998f4_512.jpg"
             :name "Iacopo Carraro"}
    :unread false}])

(rum/defcs user-notifications < rum/reactive
                                (drv/drv :user-notifications)
                                (rum/local false ::tray-open)
                                (rum/local nil ::click-out-listener)

                                {:will-mount (fn [s]
                                  (reset! (::click-out-listener s)
                                   (events/listen js/window EventType/CLICK
                                    #(when-not (utils/event-inside? % (rum/dom-node s))
                                       (reset! (::tray-open s) false))))
                                  s)
                                 :will-unmount (fn [s]
                                  (when @(::click-out-listener s)
                                    (events/unlistenByKey @(::click-out-listener s))
                                    (reset! (::click-out-listener s) nil))
                                  s)}
  [s]
  (let [user-notifications-data (drv/react s :user-notifications)
        has-new-content true] ;(:new-content? user-notifications-data)]
    [:div.user-notifications
      [:button.mlb-reset.notification-bell-bt
        {:class (utils/class-set {:new has-new-content
                                  :active @(::tray-open s)})
         :on-click #(swap! (::tray-open s) not)}]
      [:div.user-notifications-tray
        {:class (utils/class-set {:hidden (not @(::tray-open s))})}
        [:div.user-notifications-tray-header.group
          [:div.title "Notifications"]
          [:button.mlb-reset.all-read-bt
            {:on-click #()}
            "Mark all as read"]]
        [:div.user-notifications-tray-list
          (for [n notifications]
            [:div.user-notification.group
              {:class (utils/class-set {:unread (:unread n)})}
              (user-avatar-image (:author n))
              [:div.user-notification-body.oc-mentions
                {:dangerouslySetInnerHTML (utils/emojify (:body n))}]
              [:div.user-notification-time-since
                [:time
                  {:date-time (:created-at n)
                   :data-toggle "tooltip"
                   :data-placement "top"
                   :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                   :data-title (utils/entry-date-tooltip n)}
                  (utils/time-since (:created-at n))]]])]]]))
