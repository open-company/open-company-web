(ns oc.web.utils.notification
  (:require [defun.core :refer (defun defun-)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
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

(defn- notification-click [activity-data interaction-uuid status]
  (let [url (when activity-data
              (if (seq interaction-uuid)
                (oc-urls/comment-url (:board-slug activity-data) (:uuid activity-data) interaction-uuid)
                (oc-urls/entry (:board-slug activity-data) (:uuid activity-data))))]
    (cond
      (and (= (router/current-board-slug) "threads")
           (seq activity-data))
      #(oc.web.actions.nav-sidebar/open-post-modal activity-data false interaction-uuid)
      (seq activity-data)
      #(router/nav! url)
      :else
      #(let [alert-data {:icon "/img/ML/trash.svg"
                         :action "notification-click-item-load"
                         :title (if (= status 404) "Post not found" "An error occurred")
                         :message (if (= status 404)
                                    "The post you're trying to access may have been moved or deleted."
                                    "Please try again")
                         :solid-button-title "Ok"
                         :solid-button-style :red
                         :solid-button-cb alert-modal/hide-alert}]
         (alert-modal/show-alert alert-data)))))

(def loading-items #{})

(defn- load-item [db org-slug board-slug entry-uuid interaction-uuid]
  (let [item-key (str org-slug "-" board-slug "-" entry-uuid)]
    (when-not (loading-items item-key)
      (swap! loading-items conj item-key)
      (cmail-actions/get-entry-with-uuid board-slug entry-uuid
       (fn [success status]
        (when (= 404 status)
          (dis/dispatch! [:user-notification-remove-by-entry org-slug board-slug entry-uuid]))))))
  nil)

(defn- load-item-if-needed [db board-slug entry-uuid interaction-uuid]
  (when (and board-slug
             entry-uuid)
    (let [activity-data (dis/activity-data (router/current-org-slug) entry-uuid db)]
      (if (map? activity-data)
        (notification-click activity-data interaction-uuid nil)
        (load-item db (router/current-org-slug) board-slug entry-uuid interaction-uuid)))))

(defn fix-notification [db notification]
  (let [board-id (:board-id notification)
        board-data (activity-utils/board-by-uuid board-id)
        title (notification-title notification)
        body (notification-content notification)
        reminder-data (:reminder notification)
        entry-uuid (:entry-id notification)
        interaction-uuid (:interaction-id notification)
        activity-data (dis/activity-data entry-uuid)
        current-user-data (:current-user-data db)]
    (merge notification
     {:activity-data activity-data
      :title title
      :body body
      :unread (and (not= (:user-id current-user-data) (-> notification :author :user-id))
                   (or (:unread notification)
                       (> (:notify-at notification) (:last-read-at activity-data))))
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
   (filter #(and (-> % :interaction-id seq)
                 (-> % :parent-interaction-id (= comment-uuid))))
   (sort-by :notify-at)))

(defn- comment-notifications [ns]
  (->> ns
   (filter #(and (-> % :interaction-id seq)
                 (-> % :parent-interaction-id empty?)))
   (map #(assoc % :replies (reply-notifications (:interaction-id %) ns)))))

(defun- latest-notify-at

  ([ns :guard sequential?]
  (apply max (map :notify-at ns)))

  ([n :guard #(contains? % :notify-at)]
   (:notify-at n))

  ([n :guard map?]
  (if (contains? n :replies)
    (max (:notify-at n) (latest-notify-at (:replies n)))
    (:notify-at n))))

(defn- entry-notifications [db entry-id ns]
  (let [all-roots (filter #(and (-> % :interaction-id empty?)
                                (-> % :parent-interaction-id empty?)) ns)
        all-comments (comment-notifications ns)
        included-notify-at (set (concat (map :notify-at all-roots)
                                        (map :notify-at all-comments)
                                        (mapcat #(map :notify-at (:replies %)) all-comments)))
        excluded-ns (filter #(-> % :notify-at included-notify-at not) ns)
        all-ns (remove nil? (concat all-roots all-comments excluded-ns))
        with-notify-at (map #(assoc % :latest-notify-at (latest-notify-at %)) all-ns)
        activity-data (dis/activity-data (router/current-org-slug) entry-id db)
        board-id (or (:board-slug activity-data) (:board-id (first ns)))]
    {:entry-id  entry-id
     :notifications (sort-by :latest-notify-at with-notify-at)
     :activity-data (dis/activity-data (router/current-org-slug) entry-id db)
     :current-user-id (or (get-in db [:current-user-data :user-id]) (get-in db [:jwt :user-id]))
     :latest-notify-at (latest-notify-at with-notify-at)
     :click (load-item-if-needed db board-id entry-id nil)}))

(defn- caught-up-map
  ([] (caught-up-map nil))
  ([n]
   (let [t (if (:latest-notify-at n)
             (-> n :latest-notify-at utils/js-date .getTime inc utils/js-date .toISOString)
             (utils/as-of-now))]
     {:resource-type :caught-up :latest-notify-at t})))

(defn- insert-open-close-items [ns]
  (when (seq ns)
    (-> ns
     vec
     (update 0 #(assoc % :open-item true))
     (update (dec (count ns)) #(assoc % :close-item true)))))

(defn- insert-caught-up [ns]
  (when (seq ns)
    (let [first-read-index (utils/index-of ns (comp not :unread))
          insert? (and first-read-index
                       (> first-read-index -1))
          [ns-before ns-after] (when insert?
                                 (split-at first-read-index ns))]
      (if insert?
        (vec (concat (insert-open-close-items ns-before) [(caught-up-map (last ns-before))] (insert-open-close-items ns-after)))
        (vec (cons (caught-up-map) (insert-open-close-items ns)))))))

(defn- group-notifications [db ns]
  (let [grouped-ns (group-by :entry-id ns)
        three-ns (map (fn [[k v]] (entry-notifications db k v)) grouped-ns)
        sorted-ns (reverse (sort-by :latest-notify-at three-ns))]
    (or (insert-caught-up sorted-ns) [])))

(defn sorted-notifications [notifications]
  (vec (reverse (sort-by :notify-at notifications))))

(defun fix-notifications
  ([db notifications :guard map?]
   (fix-notifications db (:sorted notifications)))

  ([db notifications :guard sequential?]
   (let [fixed-notifications (map (partial fix-notification db) notifications)]
     {:sorted (sorted-notifications (remove nil? fixed-notifications))
      :grouped (group-notifications db fixed-notifications)}))

  ([_db _notifications :guard nil?]
   []))