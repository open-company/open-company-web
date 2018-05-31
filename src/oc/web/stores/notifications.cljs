(ns oc.web.stores.notifications
  "Notifications store is a list of notification having the following form:
  {:title \"A string representing the title to show.\"
   :description \"A string representing the description to show.\"
   :server-error true // Only if representing a server error, mostly it changes color theme.
   :app-update true // Only if representing an app update error, mostly it changes color theme.
   :dismiss true // Show an X to remove the notification
   :opac true // Apply opacity to all the contained elements
   :expire 100 // Number of seconds after which the notification is removed, 0 is infinite.
   :id :unique-id // Used to avoid showing 2 times the same notification.
  }"
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [taoensso.timbre :as timbre]
            [cljs-flux.dispatcher :as flux]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.slack-bot-modal :as slack-bot-modal]))

(defn show-add-bot-notification? [db]
  ;; Do we have the needed data loaded
  (when-let* [org-data (dispatcher/org-data db)
              current-user-data (dispatcher/current-user-data db)
              team-data (dispatcher/team-data (:team-id org-data) db)]
    ;; Show the notification
    (notification-actions/show-notification {:title "Enable Carrot for Slack?"
                                             :description "Share post to Slack, sync comments, invite & manage your team."
                                             :id :slack-second-step-banner
                                             :dismiss true
                                             :expire 0
                                             :slack-bot true
                                             :primary-bt-cb #(user-actions/bot-auth team-data current-user-data)
                                             :primary-bt-title [:span [:span.slack-icon] "Add to Slack"]
                                             :primary-bt-style :solid-green
                                             :primary-bt-dismiss true
                                             :secondary-bt-cb #(slack-bot-modal/show-modal)
                                             :secondary-bt-title "Learn More"
                                             :secondary-bt-style :default-link
                                             :secondary-bt-dismiss true})))

(defn maybe-show-add-bot-notification? [db]
  ;; Do we need to show the add bot banner?
  (if (and (= (jwt/get-key :auth-source) "slack")
           (empty? (jwt/get-key :slack-bots)))
    (utils/after 100 #(show-add-bot-notification? db))
    (let [bot-access (dispatcher/bot-access db)]
      (when (= bot-access :slack-bot-success-notification)
        (notification-actions/show-notification {:title "Slack integration successful"
                                                 :slack-icon true
                                                 :id "slack-bot-integration-succesful"}))
      (dispatcher/dispatch! [:bot-access nil]))))

;; Reducers used to watch for reaction dispatch data
(defmulti reducer (fn [db [action-type & _]]
                    (when-not (some #{action-type} [:update :input])
                      (timbre/debug "Dispatching reaction reducer:" action-type))
                    action-type))

(def notifications-dispatch
  (flux/register
   dispatcher/actions
   (fn [payload]
     (swap! dispatcher/app-state reducer payload))))

(defn find-duplicate [n-data notifs]
  (when (:id n-data)
    (first (filter #(= (:id %) (:id n-data)) notifs))))

(defmethod dispatcher/action :notification-add
  [db [_ notification-data]]
  (let [current-notifications (get-in db dispatcher/notifications-key)
        dup (find-duplicate notification-data current-notifications)
        old-notifications (remove #(= (:id %) (:id notification-data)) current-notifications)
        fixed-notification-data (if dup
                                  (if (or (zero? (:expire notification-data)) (zero? (:expire dup)))
                                    (assoc notification-data :expire 0)
                                    (assoc notification-data :expire (max (:expire notification-data) (:expire dup))))
                                  notification-data)
        next-notifications (conj old-notifications fixed-notification-data)]
    (assoc-in db dispatcher/notifications-key next-notifications)))

(defmethod dispatcher/action :notification-remove
  [db [_ notification-data]]
  (let [current-notifications (get-in db dispatcher/notifications-key)
        next-notifications (filter #(not= (:created-at %) (:created-at notification-data)) current-notifications)]
    (assoc-in db dispatcher/notifications-key next-notifications)))

;; Reaction store specific reducers
(defmethod reducer :default [db payload]
  ;; ignore state changes not specific to reactions
  db)

(defmethod reducer :team-loaded
  [db [_ team-data]]
  (let [org-data (dispatcher/org-data db)]
    (when (= (:team-id team-data) (:team-id org-data)))
      (maybe-show-add-bot-notification? db))
  db)

(defmethod reducer :user-data
  [db [_ user-data]]
  (maybe-show-add-bot-notification? db)
  db)

(defmethod reducer :bot-access
  [db [_ user-data]]
  (maybe-show-add-bot-notification? db)
  db)