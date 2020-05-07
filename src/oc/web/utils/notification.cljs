(ns oc.web.utils.notification
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn- notification-title [notification]
  (let [mention? (:mention? notification)
        reminder? (:reminder? notification)
        author (:author notification)
        first-name (or (:first-name author) (first (clojure.string/split (:name author) #"\s")))
        reminder (when reminder?
                    (:reminder notification))
        notification-type (when reminder?
                            (:notification-type reminder))
        reminder-assignee (when reminder?
                            (:assignee reminder))
        entry-publisher (:entry-publisher notification)
        user-id (:user-id notification)]
    (cond
      ;; A reminder was created for current user
      (and reminder
           (= notification-type "reminder-notification"))
      (str first-name " created a new reminder for you")
      ;; A reminder has been triggered for the current user
      (and reminder
           (= notification-type "reminder-alert"))
      (str "Hi " (first (clojure.string/split (:name reminder-assignee) #"\s")) ", it's time to update your team")
      ;; Current user was mentioned in a post or comment, for comment check (seq (:interaction-id notification))
      mention?
      (str first-name " mentioned you")
      ;; A comment was added to a post the current is involved in
      (and ;; if is a commnet
           (:interaction-id notification)
           ;; And the recipient of the notification is different than the
           ;; author fo the post it means we are just following this post
           ;; because we commented too
           (not= (:user-id entry-publisher) user-id))
      (str first-name " replied to a thread")
      ;; A comment was added to a post the current user published
      (:interaction-id notification)
      (str first-name " commented on your post")
      :else
      nil)))

(defn- notification-content [notification]
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

(defn- load-item-if-needed [board-slug entry-uuid interaction-uuid]
  (when (and board-slug
             entry-uuid)
    (let [url (if interaction-uuid
                (oc-urls/comment-url board-slug entry-uuid interaction-uuid)
                (oc-urls/entry board-slug entry-uuid))]
      #(if (seq (dis/activity-data entry-uuid))
        (router/nav! url)
        (cmail-actions/get-entry-with-uuid board-slug entry-uuid
         (fn [success status]
          (if success
            (router/nav! url)
            (let [alert-data {:icon "/img/ML/trash.svg"
                              :action "notification-click-item-load"
                              :title (if (= status 404) "Post not found" "An error occurred")
                              :message (if (= status 404)
                                         "The post you're trying to access may have been moved or deleted."
                                         "Please try again")
                              :solid-button-title "Ok"
                              :solid-button-style :red
                              :solid-button-cb alert-modal/hide-alert}]
              (alert-modal/show-alert alert-data)))))))))

(defn fix-notification [notification & [unread]]
  (let [board-id (:board-id notification)
        board-data (activity-utils/board-by-uuid board-id)
        is-interaction (seq (:interaction-id notification))
        created-at (:notify-at notification)
        title (notification-title notification)
        reminder-data (:reminder notification)
        reminder? (:reminder? notification)
        entry-uuid (:entry-id notification)
        interaction-uuid (:interaction-id notification)
        activity-data (dis/activity-data entry-uuid)]
    (when (and (seq title)
               board-data)
      {:uuid entry-uuid
       :board-slug (:slug board-data)
       :board-name (:name board-data)
       :publisher-board (:publisher-board board-data)
       :interaction-id interaction-uuid
       :is-interaction is-interaction
       :unread unread
       :mention? (:mention? notification)
       :reminder? reminder?
       :reminder reminder-data
       :created-at (:notify-at notification)
       :body (notification-content notification)
       :title title
       :author (:author notification)
       :activity-data activity-data
       :click (if reminder?
                (when-not (responsive/is-mobile-size?)
                  (if (and reminder-data
                           (= (:notification-type reminder-data) "reminder-notification"))
                    #(oc.web.actions.nav-sidebar/show-reminders)
                    #(ui-compose)))
                (load-item-if-needed (or (:slug board-data) board-id) entry-uuid interaction-uuid))})))

(defn sorted-notifications [notifications]
  (vec (reverse (sort-by :created-at notifications))))

(defn fix-notifications [notifications]
  (sorted-notifications
   (remove nil?
    (map fix-notification
     notifications))))