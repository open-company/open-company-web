(ns oc.web.stores.comment
  (:require [taoensso.timbre :as timbre]
            [defun.core :refer (defun)]
            [cuerdas.core :as str]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.utils.comment :as comment-utils]
            [oc.web.utils.activity :as activity-utils]))

(defn- is-emoji
  [body]
  (let [plain-text (.text (js/$ (str "<div>" body "</div>")))
        is-emoji? (js/RegExp "^([\ud800-\udbff])([\udc00-\udfff])" "g")
        is-text-message? (js/RegExp "[a-zA-Z0-9\\s!?@#\\$%\\^&(())_=\\-<>,\\.\\*;':\"]" "g")]
    (and ;; emojis can have up to 11 codepoints
         (<= (count plain-text) 11)
         (.match plain-text is-emoji?)
         (not (.match plain-text is-text-message?)))))

(defun parse-comment
  ([org-data activity-data comments :guard sequential?]
    (map #(parse-comment org-data activity-data %) comments))
  ([org-data activity-data comment-map :guard nil?]
    {})
  ([org-data :guard map? activity-data :guard map? comment-map :guard map?]
    (let [edit-comment-link (utils/link-for (:links comment-map) "partial-update")
          delete-comment-link (utils/link-for (:links comment-map) "delete")
          can-react? (utils/link-for (:links comment-map) "react"  "POST")
          reply-parent (or (:parent-uuid comment-map) (:uuid comment-map))
          is-root-comment (empty? (:parent-uuid comment-map))
          author? (activity-utils/is-author? comment-map)
          unread? (and (not author?)
                       (activity-utils/comment-unread? comment-map (:last-read-at activity-data)))]
      (-> comment-map
        (assoc :content-type :comment)
        (assoc :author? author?)
        (assoc :unread unread?)
        (assoc :is-emoji (is-emoji (:body comment-map)))
        (assoc :can-edit (boolean edit-comment-link))
        (assoc :can-delete (boolean delete-comment-link))
        (assoc :can-react can-react?)
        (assoc :reply-parent reply-parent)
        (assoc :resource-uuid (:uuid activity-data))
        (assoc :url (str ls/web-server-domain (oc-urls/comment-url (:slug org-data) (:board-slug activity-data)
                                               (:uuid activity-data) (:uuid comment-map))))))))

(defmethod dispatcher/action :add-comment-change
  [db [_ org-slug activity-uuid parent-comment-uuid comment-uuid comment-body force-update?]]
  (let [comment-key (dispatcher/add-comment-string-key activity-uuid parent-comment-uuid comment-uuid)
        add-comment-activity-key (dispatcher/add-comment-activity-key org-slug comment-key)]
    (-> db
      (assoc-in add-comment-activity-key comment-body)
      ;; Force refresh of the add comment field, needed in case the post comment fails and we need
      ;; to move the body back in the field to let the user retry
      (update-in (dispatcher/add-comment-force-update-key comment-key) #(if force-update? (utils/activity-uuid) %)))))

(defmethod dispatcher/action :add-comment-reset
  [db [_ org-slug activity-uuid parent-comment-uuid comment-uuid]]
  (let [add-comment-key (dispatcher/add-comment-key org-slug)
        comment-key (dispatcher/add-comment-string-key activity-uuid parent-comment-uuid comment-uuid)
        add-comment-activity-key (dispatcher/add-comment-activity-key org-slug comment-key)]
    (-> db
      ;; Lose the cached body
      (update-in add-comment-key dissoc comment-key)
      ;; Force refresh of the add comment field to remove the body
      (assoc-in (dispatcher/add-comment-force-update-key comment-key) (utils/activity-uuid)))))

(defmethod dispatcher/action :add-comment-focus
  [db [_ focus-uuid]]
  (assoc db :add-comment-focus focus-uuid))

(defmethod dispatcher/action :add-comment-blur
  [db [_ focus-uuid]]
  (update db :add-comment-focus #(if (= focus-uuid %) nil %)))

(defmethod dispatcher/action :comment-add
  [db [_ org-slug activity-data comment-data parent-comment-uuid comments-key]]
  (let [activity-key (dispatcher/activity-key org-slug (:uuid activity-data))
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        new-comment-data (parse-comment (dispatcher/org-data db)
                                        activity-data
                                        comment-data)
        all-comments (concat comments-data [new-comment-data])
        sorted-comments (comment-utils/sort-comments all-comments)
        threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
        threads-map (comment-utils/threads-map sorted-comments)]
    (-> db
     (assoc-in sorted-comments-key sorted-comments)
     (assoc-in threads-map-key threads-map)
     ;; Reset new comments count
     (assoc-in (conj activity-key :new-comments-count) 0)
     (update-in (conj activity-key :links) (fn [links]
                                             (mapv (fn [link]
                                              (if (= (:rel link) "follow")
                                                (merge link {:href (str/replace (:href link) #"/follow/?$" "/unfollow/")
                                                             :rel "unfollow"})
                                                link))
                                               links))))))

(defmethod dispatcher/action :comment-add/replace
  [db [_ activity-data comment-data comments-key new-comment-uuid]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        old-comments-data (filterv #(not= (:uuid %) new-comment-uuid) comments-data)
        new-comment-data (parse-comment (dispatcher/org-data db)
                                        activity-data
                                        comment-data)
        all-comments (concat old-comments-data [new-comment-data])
        sorted-all-comments (comment-utils/sort-comments all-comments)
        threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
        threads-map (comment-utils/threads-map sorted-all-comments)]
    (-> db
     (assoc-in sorted-comments-key sorted-all-comments)
     (assoc-in threads-map-key threads-map))))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-data body]}]]
  (assoc db :comment-add-finish true))

(defmethod dispatcher/action :comment-add/failed
  [db [_ activity-data comment-data comments-key]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        all-comments (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        filtered-comments (filterv #(not= (:uuid comment-data) (:uuid %)) all-comments)
        sorted-filtered-comments (comment-utils/sort-comments filtered-comments)
        threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
        threads-map (comment-utils/threads-map sorted-filtered-comments)]
    (-> db
     (assoc-in sorted-comments-key sorted-filtered-comments)
     (assoc-in threads-map-key threads-map))))

(defmethod dispatcher/action :comment-save/failed
  [db [_ activity-data comment-data comments-key]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        all-comments (comment-utils/ungroup-comments  (get-in db sorted-comments-key))
        filtered-comments (filterv #(not= (:uuid comment-data) (:uuid %)) all-comments)
        sorted-filtered-comments (comment-utils/sort-comments (conj filtered-comments comment-data))
        threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
        threads-map (comment-utils/threads-map sorted-filtered-comments)]
    (-> db
     (assoc-in sorted-comments-key sorted-filtered-comments)
     (assoc-in threads-map-key threads-map))))

(defmethod dispatcher/action :comments-get
  [db [_ comments-key activity-data]]
  (assoc-in db (vec (conj comments-key :loading)) true))

(defmethod dispatcher/action :comments-get/finish
  [db [_ {:keys [success error comments-key body secure-activity-uuid activity-uuid]}]]
  (if success
    (let [org-data (dispatcher/org-data db)
          activity-data (if secure-activity-uuid
                          (dispatcher/secure-activity-data (:slug org-data) secure-activity-uuid db)
                          (dispatcher/activity-data (:slug org-data) activity-uuid db))
          cleaned-comments (map #(parse-comment org-data activity-data %) (:items (:collection body)))
          sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
          sorted-comments (comment-utils/sort-comments cleaned-comments)
          threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
          threads-map (comment-utils/threads-map sorted-comments)]
      (-> db
       (assoc-in sorted-comments-key sorted-comments)
       (assoc-in threads-map-key threads-map)
       (assoc-in (vec (conj comments-key :loading)) false)))
    (assoc-in db (vec (conj comments-key :loading)) false)))

(defmethod dispatcher/action :comment-delete
  [db [_ activity-uuid comment-data comments-key]]
  (let [item-uuid (:uuid comment-data)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        new-comments-data (filterv #(and (not= item-uuid (:uuid %))
                                         (not= item-uuid (:parent-uuid %)))
                           comments-data)
        sorted-comments (comment-utils/sort-comments new-comments-data)
        threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
        threads-map (comment-utils/threads-map sorted-comments)]
    (-> db
     (assoc-in sorted-comments-key sorted-comments)
     (assoc-in threads-map-key threads-map))))

(defmethod dispatcher/action :comment-reaction-toggle
  [db [_ comments-key activity-uuid comment-uuid reaction-data reacting?]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        comment-data (some #(when (= comment-uuid (:uuid %)) %) comments-data)]
    ;; the comment has yet to be stored locally in app state so ignore and
    ;; wait for server side reaction
    (if comment-data
      (let [reactions-data (:reactions comment-data)
            reaction (:reaction reaction-data)
            reaction-idx (utils/index-of reactions-data #(= (:reaction %) reaction))
            reacted? (not (:reacted reaction-data))
            old-link (first (:links reaction-data))
            new-link (assoc old-link :method (if reacted? "DELETE" "PUT"))
            with-new-link (assoc reaction-data :links [new-link])
            with-new-reacted (assoc with-new-link :reacted reacted?)
            new-count (if reacted?
                        (inc (:count reaction-data))
                        (dec (:count reaction-data)))
            new-reaction-data (assoc with-new-reacted :count new-count)
            new-reactions-data (assoc reactions-data reaction-idx new-reaction-data)
            new-comment-data (assoc comment-data :reactions new-reactions-data)
            new-comments-data (->> comments-data
                               (filter #(not= (:uuid %) comment-uuid))
                               (cons new-comment-data))
            new-sorted-comments-data (comment-utils/sort-comments new-comments-data)
            threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
            threads-map (comment-utils/threads-map new-sorted-comments-data)]
        (-> db
         (assoc-in sorted-comments-key new-sorted-comments-data)
         (assoc-in threads-map-key threads-map)))
      db)))

(defmethod dispatcher/action :comment-react-from-picker
  [db [_ comments-key activity-uuid comment-uuid reaction]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        comment-data (some #(when (= (:uuid %) comment-uuid) %) comments-data)]
    ;; the comment has yet to be stored locally in app state so ignore and
    ;; wait for server side reaction
    (if comment-data
      (let [reactions-data (:reactions comment-data)
            reaction-idx (utils/index-of reactions-data #(= (:reaction %) reaction))
            reaction-data (when reaction-idx
                            (get reactions-data reaction-idx))
            reacted? (if reaction-data
                       (not (:reacted reaction-data))
                       true)
            old-link (when reaction-data
                       (first (:links reaction-data)))
            new-link (when old-link
                       (assoc old-link :method (if reacted? "DELETE" "PUT")))
            new-count (if reacted?
                        (inc (:count reaction-data))
                        (dec (:count reaction-data)))
            new-reaction-data {:links [new-link]
                               :reacted reacted?
                               :reaction reaction
                               :count new-count}
            new-reactions-data (if reaction-idx
                                 (assoc reactions-data reaction-idx new-reaction-data)
                                 (conj reactions-data new-reaction-data))
            new-comment-data (assoc comment-data :reactions new-reactions-data)
            new-comments-data (->> comments-data
                               (filter #(not= (:uuid %) comment-uuid))
                               (cons new-comment-data))
            new-sorted-comments-data (comment-utils/sort-comments new-comments-data)
            threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
            threads-map (comment-utils/threads-map new-sorted-comments-data)]
        (-> db
         (assoc-in sorted-comments-key new-sorted-comments-data)
         (assoc-in threads-map-key threads-map)))
      db)))

(defmethod dispatcher/action :comment-save
  [db [_ org-slug comments-key updated-comment-map*]]
  (let [updated-comment-map (dissoc updated-comment-map* :thread-children)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        all-comments (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        filtered-comments (filterv #(not= (:uuid %) (:uuid updated-comment-map)) all-comments)
        sorted-new-comments (comment-utils/sort-comments (conj filtered-comments updated-comment-map))
        threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
        threads-map (comment-utils/threads-map sorted-new-comments)]
    (-> db
     (assoc-in sorted-comments-key sorted-new-comments)
     (assoc-in threads-map-key threads-map))))

(defmethod dispatcher/action :ws-interaction/comment-update
  [db [_ comments-key interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        org-data (dispatcher/org-data db)
        activity-data (dispatcher/activity-data (:slug org-data) activity-uuid db)
        ws-comment-data (:interaction interaction-data)
        item-uuid (:uuid ws-comment-data)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        comment-data (some #(when (= item-uuid (:uuid %)) %) comments-data)]
    (if comment-data
      (if (<= (utils/js-date
               (:updated-at comment-data))
              (utils/js-date (:updated-at comment-data)))
        (let [body-comment-data (assoc comment-data
                                  :body (:body ws-comment-data))
              update-comment-data (assoc body-comment-data
                                    :updated-at (:updated-at ws-comment-data))
              new-comment-data (if (contains? update-comment-data :reactions)
                                 update-comment-data
                                 (assoc update-comment-data :reactions (:reactions ws-comment-data)))
              new-comments-data (->> comments-data
                                 (filter #(not= (:uuid %) item-uuid))
                                 (cons new-comment-data))
              new-sorted-comments-data (comment-utils/sort-comments new-comments-data)
              threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
              threads-map (comment-utils/threads-map new-sorted-comments-data)]
          (-> db
           (assoc-in sorted-comments-key new-sorted-comments-data)
           (assoc-in threads-map-key threads-map)))
        db)
      db)))

(defmethod dispatcher/action :ws-interaction/comment-delete
  [db [_ org-slug interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        item-uuid (:uuid (:interaction interaction-data))
        last-read-at (:last-read-at (dispatcher/activity-data org-slug activity-uuid db))
        comments-key (dispatcher/activity-comments-key org-slug activity-uuid)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))
        deleting-comment-data (some #(when (= (:uuid %) item-uuid) %) comments-data)
        current-user-id (jwt/user-id)
        deleting-new-comment? (when deleting-comment-data
                                (comment-utils/unread? last-read-at deleting-comment-data))
        new-comments-data (vec (remove #(= item-uuid (:uuid %)) comments-data))
        new-sorted-comments-data (comment-utils/sort-comments new-comments-data)
        threads-map-key (vec (conj comments-key dispatcher/threads-map-key))
        threads-map (comment-utils/threads-map new-sorted-comments-data)
        last-not-own-comment (last (sort-by :created-at (filterv #(not= (:user-id %) current-user-id) new-comments-data)))]
    (-> db
     (assoc-in sorted-comments-key new-sorted-comments-data)
     (assoc-in threads-map-key threads-map)
     (update-in (dispatcher/activity-key org-slug activity-uuid) merge {:new-at (:created-at last-not-own-comment)})
     (update-in (vec (conj (dispatcher/activity-key org-slug activity-uuid) :new-comments-count)) (if deleting-new-comment? dec identity)))))

(defmethod dispatcher/action :ws-interaction/comment-add
  [db [_ org-slug entry-data interaction-data]]
  (if entry-data
    ;; If the entry is present in the local state
    (let [; get the comment data from the ws message
          activity-uuid (:resource-uuid interaction-data)
          org-data (dispatcher/org-data db)
          activity-data (dispatcher/activity-data (:slug org-data) activity-uuid db)
          comment-data (parse-comment org-data activity-data (:interaction interaction-data))
          created-at (:created-at comment-data)
          ;; update the comments link of the entry
          comments-link-idx (utils/index-of
                             (:links activity-data)
                             #(and (= (:rel %) "comments") (= (:method %) "GET")))
          with-increased-count (update-in activity-data [:links comments-link-idx :count] inc)
          old-authors (or (:authors (get (:links activity-data) comments-link-idx)) [])
          new-author (:author comment-data)
          new-authors (if (and old-authors (first (filter #(= (:user-id %) (:user-id new-author)) old-authors)))
                        old-authors
                        (concat [new-author] old-authors))
          with-authors (assoc-in with-increased-count [:links comments-link-idx :authors] new-authors)
          comment-from-current-user? (= (:user-id comment-data) (jwt/user-id))
          old-comments-count (:new-comments-count activity-data)
          new-comments-count (if (and ;; comment is not from current user
                                      (not comment-from-current-user?)
                                      ;; and the activity we have is old (new-at is the created-at of the last comment)
                                      (> (.getTime (utils/js-date (:new-at activity-data)))
                                         (.getTime (utils/js-date (:created-at comment-data)))))
                               (inc old-comments-count)
                               old-comments-count)
          with-new-at (if comment-from-current-user?
                        with-authors
                        (-> with-authors
                          (assoc :new-at created-at)
                          (assoc :new-comments-count new-comments-count)))
          sorted-comments-key (dispatcher/activity-sorted-comments-key org-slug activity-uuid)
          all-old-comments-data (comment-utils/ungroup-comments (get-in db sorted-comments-key))]
      (if all-old-comments-data
        (let [;; If we have the previous comments already loaded
              old-comments-data (filterv :links all-old-comments-data)
              ;; Add the new comment to the comments list, make sure it's not present already
              new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
              new-sorted-comments-data (comment-utils/sort-comments new-comments-data)
              threads-map-key (dispatcher/activity-threads-map-key org-slug activity-uuid)
              threads-map (comment-utils/threads-map new-sorted-comments-data)]
          (-> db
           (assoc-in sorted-comments-key new-sorted-comments-data)
           (assoc-in threads-map-key threads-map)
           (assoc-in (dispatcher/activity-key org-slug activity-uuid) with-new-at)))
        ;; In case we don't have the comments already loaded just update the :new-at value
        ;; needed to compare the last read-at of the current user and show NEW comments
        (assoc-in db (dispatcher/activity-key org-slug activity-uuid) with-new-at)))
    db))
