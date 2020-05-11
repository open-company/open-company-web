(ns oc.web.utils.notification
  (:require [defun.core :refer (defun defun-)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn- notification-title [notification & [no-first-name?]]
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
      (str (when-not no-first-name? (str first-name " ")) "created a new reminder for you")
      ;; A reminder has been triggered for the current user
      (and reminder
           (= notification-type "reminder-alert"))
      (str "Hi " (first (clojure.string/split (:name reminder-assignee) #"\s")) ", it's time to update your team")
      ;; Current user was mentioned in a post or comment, for comment check (seq (:interaction-id notification))
      mention?
      (str (when-not no-first-name? (str first-name " ")) "mentioned you")
      (:interaction-id notification)
      (str (when-not no-first-name? (str first-name " ")) "added a comment")
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

(defn- load-item-if-needed [db board-slug entry-uuid interaction-uuid]
  (when (and board-slug
             entry-uuid)
    #(let [url (if interaction-uuid
                (oc-urls/comment-url board-slug entry-uuid interaction-uuid)
                (oc-urls/entry board-slug entry-uuid))
           activity-data (dis/activity-data (router/current-org-slug) entry-uuid db)]
      (cond
        (and (= (router/current-board-slug) "activity")
             (seq activity-data))
        (oc.web.actions.nav-sidebar/open-post-modal activity-data false)
        (seq activity-data)
        (router/nav! url)
        :else
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

(defn fix-notification [db notification & [unread]]
  (let [board-id (:board-id notification)
        board-data (activity-utils/board-by-uuid board-id)
        title (notification-title notification)
        body (notification-content notification)
        reminder-data (:reminder notification)
        entry-uuid (:entry-id notification)
        interaction-uuid (:interaction-id notification)
        activity-data (dis/activity-data entry-uuid)]
    (merge notification
     {:activity-data activity-data
      :title title
      :body body
      :unread unread
      :current-user-id (or (get-in db [:current-user-data :user-id]) (get-in db [:jwt :user-id]))
      :click (if (:reminder? notification)
               (when-not (responsive/is-mobile-size?)
                 (if (and reminder-data
                          (= (:notification-type reminder-data) "reminder-notification"))
                   #(oc.web.actions.nav-sidebar/show-reminders)
                   #(ui-compose)))
               (load-item-if-needed db (or (:slug board-data) board-id) entry-uuid
                (:interaction-id notification)))})))

(defn- reply-notifications [comment-uuid ns]
  (->> ns
   (filter #(and (-> % :interaction-id empty? not)
                 (-> % :parent-interaction-id (= comment-uuid))))
   (sort-by :notify-at)))

(defn- comment-notifications [ns]
  (->> ns
   (filter #(and (-> % :interaction-id empty? not)
                 (-> % :parent-interaction-id empty?)))
   (map #(assoc % :replies (reply-notifications (:interaction-id %) ns)))))

(defun- latest-notify-at

  ([ns :guard sequential?]
  (apply max (map :notify-at ns)))

  ([n :guard map?]
  (if (contains? n :replies)
    (max (:notify-at n) (latest-notify-at (:replies n)))
    (:notify-at n))))

(defn- entry-notifications [ns]
  (let [all-roots (filter #(and (-> % :interaction-id empty?)
                                (-> % :parent-interaction-id empty?)) ns)
        all-comments (comment-notifications ns)
        included-notify-at (set (concat (map :notify-at all-roots)
                                        (map :notify-at all-comments)
                                        (mapcat #(map :notify-at (:replies %)) all-comments)))
        excluded-ns (filter #(-> % :notify-at included-notify-at not) ns)
        all-ns (concat all-roots all-comments excluded-ns)
        with-notify-at (map #(assoc % :latest-notify-at (latest-notify-at %)) all-ns)]
    (sort-by :latest-notify-at with-notify-at)))

(defn sorted-notifications [notifications]
  (vec (reverse (sort-by :notify-at notifications))))

(defun fix-notifications
  ([db notifications :guard map?]
   (fix-notifications (:sorted notifications)))

  ([db notifications :guard sequential?]
   (let [fixed-notifications (map (partial fix-notification db) notifications)]
     {:sorted (sorted-notifications (remove nil? fixed-notifications))
      :grouped (entry-notifications fixed-notifications)}))

  ([_db _notifications :guard nil?]
   []))