(ns oc.web.utils.notification
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [clojure.string :as clj-str]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn notification-title [notification]
  (let [mention? (:mention? notification)
        team? (:team? notification)
        author (:author notification)
        first-name (or (:first-name author) (first (clj-str/split (:name author) #"\s")))
        entry-publisher (:entry-publisher notification)
        user-id (:user-id notification)]
    (cond
      ;; Current user was mentioned in a post or comment, for comment check (seq (:interaction-id notification))
      mention?
      (str first-name " mentioned you")
      ;; Team related notification: premium changes or add existing user to team
      team?
      (:content notification)
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

(defn- load-item-if-needed [board-slug entry-uuid interaction-uuid]
  (when (and entry-uuid
             (not= entry-uuid :404)
             board-slug)
    (let [url (if interaction-uuid
                (oc-urls/comment-url board-slug entry-uuid interaction-uuid)
                (oc-urls/entry board-slug entry-uuid))]
      #(let [activity-data (dis/activity-data entry-uuid)]
         (if (or (keyword? activity-data) (seq activity-data))
           (router/nav! url)
           (cmail-actions/get-entry-with-uuid board-slug entry-uuid
                                              (fn [success status]
                                                (if (or success
                                                        (< 399 status 500))
                                                  (router/nav! url)
                                                  (let [alert-data {:icon "/img/ML/trash.svg"
                                                                    :action "notification-click-item-load"
                                                                    :title "An error occurred"
                                                                    :message "Please try again"
                                                                    :solid-button-title "Ok"
                                                                    :solid-button-style :red
                                                                    :solid-button-cb alert-modal/hide-alert}]
                                                    (alert-modal/show-alert alert-data))))))))))

(defn fix-notification [notification & [unread]]
  (let [team? (:team? notification)
        board-id (:board-id notification)
        board-data (activity-utils/board-by-uuid board-id)
        is-interaction (seq (:interaction-id notification))
        created-at (:notify-at notification)
        title (notification-title notification)
        entry-uuid (:entry-id notification)
        interaction-uuid (:interaction-id notification)
        team-id (:team-id notification)
        premium-action (:premium-action notification)]
    (when (seq title)
      {:uuid entry-uuid
       :board-slug (:slug board-data)
       :interaction-id interaction-uuid
       :is-interaction is-interaction
       :unread unread
       :mention? (:mention? notification)
       :created-at created-at
       :body (when-not team? (:content notification))
       :title title
       :author (:author notification)
       :team-id team-id
       :premium-action premium-action
       :refresh-token-at (:refresh-token-at notification)
       :click (if team?
                #(when (not= (-> notification :org :slug) (dis/current-org-slug))
                   (routing-actions/switch-org-dashboard (:org notification)))
                (load-item-if-needed (or (:slug board-data) board-id) entry-uuid interaction-uuid))})))

(defn sorted-notifications [notifications]
  (vec (reverse (sort-by :created-at notifications))))

(defn fix-notifications [notifications]
  (sorted-notifications
   (remove nil?
           (map fix-notification
                notifications))))
