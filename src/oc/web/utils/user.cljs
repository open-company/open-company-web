(ns oc.web.utils.user
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.utils.activity :as activity-utils]))

(def user-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]
   :transformations {
     :crop {
       :aspectRatio 1}}})

(defn notification-title [notification]
  (let [reminder? (:reminder? notification)
        author (:author notification)
        first-name (or (:first-name author) (first (clojure.string/split (:name author) #"\s")))
        reminder (when reminder?
                    (:reminder notification))
        notification-type (when reminder?
                            (:notification-type reminder))
        reminder-assignee (when reminder?
                            (:assignee reminder))]
    (cond
      (and reminder
           (= notification-type "reminder-notification"))
      (str first-name " created a new reminder for you")
      (and reminder
           (= notification-type "reminder-alert"))
      (str "Hi " (first (clojure.string/split (:name reminder-assignee) #"\s")) ", it's time to update your team")
      (and (:mention? notification) (:interaction-id notification))
      (str first-name " mentioned you in a comment")
      (:mention? notification)
      (str first-name " mentioned you")
      (:interaction-id notification)
      (str first-name " commented on your post")
      :else
      nil)))

(defn notification-content [notification]
  (let [reminder? (:reminder? notification)
        reminder (when reminder?
                   (:reminder notification))
        notification-type (when reminder?
                            (:notification-type reminder))]
    (cond
      (and reminder
           (= notification-type "reminder-notification"))
      (str
       (:headline reminder) ": "
       (:frequency reminder) " starting "
       (activity-utils/post-date (:next-send reminder)))
      (and reminder
           (= notification-type "reminder-alert"))
      (:headline reminder)
      :else
      (:content notification))))

(defn fix-notification [notification & [unread]]
  (let [board-data (activity-utils/board-by-uuid (:board-id notification))
        is-interaction (seq (:interaction-id notification))
        created-at (:notify-at notification)
        title (notification-title notification)
        reminder-data (:reminder notification)
        reminder? (:reminder? notification)
        entry-uuid (:entry-id notification)]
    (when (seq title)
      {:uuid entry-uuid
       :board-slug (:slug board-data)
       :interaction-id (:interaction-id notification)
       :is-interaction is-interaction
       :unread unread
       :mention? (:mention? notification)
       :reminder? reminder?
       :reminder reminder-data
       :created-at (:notify-at notification)
       :body (notification-content notification)
       :title title
       :author (:author notification)
       :click (fn []
               (if reminder?
                 (if (and reminder-data
                          (= (:notification-type reminder-data) "reminder-notification"))
                   (oc.web.actions.nav-sidebar/show-reminders)
                   (ui-compose))
                 (when (and (:slug board-data)
                            entry-uuid)
                   (router/nav! (oc-urls/entry (:slug board-data) entry-uuid)))))})))

(defn sorted-notifications [notifications]
  (vec (reverse (sort-by :created-at notifications))))

(defn fix-notifications [notifications]
  (sorted-notifications
   (remove nil?
    (map fix-notification
     notifications))))

(def user-name-max-lenth 64)

;; Associated Slack user check

(defn user-has-slack-with-bot?
  "Check if the current user has an associated Slack user under a team that has the bot."
  [current-user-data bots-data team-roster]
  (let [slack-orgs-with-bot (map :slack-org-id bots-data)
        slack-users (:slack-users (first (filter #(= (:user-id %) (:user-id current-user-data)) (:users team-roster))))]
    (some #(contains? slack-users (keyword %)) slack-orgs-with-bot)))