(ns oc.web.stores.comment
  (:require [taoensso.timbre :as timbre]
            [defun.core :refer (defun)]
            [cuerdas.core :as str]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.utils.activity :as au]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :add-comment/reply
  [db [_ focus-value parent-body]]
  (assoc-in db [:comment-reply-to focus-value] parent-body))

(defmethod dispatcher/action :add-comment/reset-reply
  [db [_ focus-value]]
  (update db :comment-reply-to dissoc focus-value))

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
        comments-data (cu/ungroup-comments (get-in db sorted-comments-key))
        new-comment-data (au/parse-comment (dispatcher/org-data db) activity-data comment-data)
        all-comments (concat comments-data [new-comment-data])
        sorted-comments (cu/sort-comments all-comments)]
    (-> db
     (assoc-in sorted-comments-key sorted-comments)
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
        comments-data (cu/ungroup-comments (get-in db sorted-comments-key))
        old-comments-data (filterv #(not= (:uuid %) new-comment-uuid) comments-data)
        new-comment-data (au/parse-comment (dispatcher/org-data db) activity-data comment-data)
        all-comments (concat old-comments-data [new-comment-data])
        sorted-all-comments (cu/sort-comments all-comments)]
    (assoc-in db sorted-comments-key sorted-all-comments)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-data body]}]]
  (assoc db :comment-add-finish true))

(defmethod dispatcher/action :comment-add/failed
  [db [_ activity-data comment-data comments-key]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        all-comments (cu/ungroup-comments (get-in db sorted-comments-key))
        filtered-comments (filterv #(not= (:uuid comment-data) (:uuid %)) all-comments)
        sorted-filtered-comments (cu/sort-comments filtered-comments)]
    (assoc-in db sorted-comments-key sorted-filtered-comments)))

(defmethod dispatcher/action :comment-save/failed
  [db [_ activity-data comment-data comments-key]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        all-comments (cu/ungroup-comments  (get-in db sorted-comments-key))
        filtered-comments (filterv #(not= (:uuid comment-data) (:uuid %)) all-comments)
        sorted-filtered-comments (cu/sort-comments (conj filtered-comments comment-data))]
    (assoc-in db sorted-comments-key sorted-filtered-comments)))

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
          cleaned-comments (map #(au/parse-comment org-data activity-data %) (:items (:collection body)))
          sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
          sorted-comments (cu/sort-comments cleaned-comments)]
      (-> db
       (assoc-in sorted-comments-key sorted-comments)
       (assoc-in (vec (conj comments-key :loading)) false)))
    (assoc-in db (vec (conj comments-key :loading)) false)))

(defmethod dispatcher/action :comment-delete
  [db [_ activity-uuid comment-data comments-key]]
  (let [item-uuid (:uuid comment-data)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (cu/ungroup-comments (get-in db sorted-comments-key))
        new-comments-data (filterv #(and (not= item-uuid (:uuid %))
                                         (not= item-uuid (:parent-uuid %)))
                           comments-data)
        sorted-comments (cu/sort-comments new-comments-data)]
    (assoc-in db sorted-comments-key sorted-comments)))

(defmethod dispatcher/action :comment-reaction-toggle
  [db [_ comments-key activity-uuid comment-uuid reaction-data reacting?]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (cu/ungroup-comments (get-in db sorted-comments-key))
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
            new-sorted-comments-data (cu/sort-comments new-comments-data)]
        (assoc-in db sorted-comments-key new-sorted-comments-data))
      db)))

(defmethod dispatcher/action :comment-react-from-picker
  [db [_ comments-key activity-uuid comment-uuid reaction]]
  (let [sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (cu/ungroup-comments (get-in db sorted-comments-key))
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
            new-sorted-comments-data (cu/sort-comments new-comments-data)]
        (assoc-in db sorted-comments-key new-sorted-comments-data))
      db)))

(defmethod dispatcher/action :comment-save
  [db [_ org-slug comments-key updated-comment-map*]]
  (let [updated-comment-map (dissoc updated-comment-map* :thread-children)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        all-comments (cu/ungroup-comments (get-in db sorted-comments-key))
        filtered-comments (filterv #(not= (:uuid %) (:uuid updated-comment-map)) all-comments)
        sorted-new-comments (cu/sort-comments (conj filtered-comments updated-comment-map))]
    (assoc-in db sorted-comments-key sorted-new-comments)))

(defmethod dispatcher/action :ws-interaction/comment-update
  [db [_ comments-key interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        org-data (dispatcher/org-data db)
        activity-data (dispatcher/activity-data (:slug org-data) activity-uuid db)
        ws-comment-data (:interaction interaction-data)
        item-uuid (:uuid ws-comment-data)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (cu/ungroup-comments (get-in db sorted-comments-key))
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
              new-sorted-comments-data (cu/sort-comments new-comments-data)]
          (assoc-in db sorted-comments-key new-sorted-comments-data))
        db)
      db)))

(defmethod dispatcher/action :ws-interaction/comment-delete
  [db [_ org-slug interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        item-uuid (:uuid (:interaction interaction-data))
        last-read-at (:last-read-at (dispatcher/activity-data org-slug activity-uuid db))
        comments-key (dispatcher/activity-comments-key org-slug activity-uuid)
        sorted-comments-key (vec (conj comments-key dispatcher/sorted-comments-key))
        comments-data (cu/ungroup-comments (get-in db sorted-comments-key))
        deleting-comment-data (some #(when (= (:uuid %) item-uuid) %) comments-data)
        current-user-id (jwt/user-id)
        deleting-new-comment? (when deleting-comment-data
                                (cu/unread? last-read-at deleting-comment-data))
        new-comments-data (vec (remove #(= item-uuid (:uuid %)) comments-data))
        new-sorted-comments-data (cu/sort-comments new-comments-data)
        last-not-own-comment (last (sort-by :created-at (filterv #(not= (:user-id %) current-user-id) new-comments-data)))]
    (-> db
     (assoc-in sorted-comments-key new-sorted-comments-data)
     (update-in (dispatcher/activity-key org-slug activity-uuid) merge {:last-activity-at (:created-at last-not-own-comment)})
     (update-in (vec (conj (dispatcher/activity-key org-slug activity-uuid) :new-comments-count)) (if deleting-new-comment? dec identity)))))

(defmethod dispatcher/action :ws-interaction/comment-add
  [db [_ org-slug entry-data interaction-data]]
  (if entry-data
    ;; If the entry is present in the local state
    (let [; get the comment data from the ws message
          activity-uuid (:resource-uuid interaction-data)
          org-data (dispatcher/org-data db)
          activity-data (dispatcher/activity-data (:slug org-data) activity-uuid db)
          comment-data (au/parse-comment org-data activity-data (:interaction interaction-data))
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
          old-comments-count (:new-comments-count activity-data)
          new-comments-count (if (and ;; comment is not from current user
                                      (not (:author? comment-data))
                                      ;; and the activity we have is old (last-activity-at is the created-at of the last comment)
                                      (> (.getTime (utils/js-date (:last-activity-at activity-data)))
                                         (.getTime (utils/js-date (:created-at comment-data)))))
                               (inc old-comments-count)
                               old-comments-count)
          with-last-activity-at (if (:author? comment-data)
                        with-authors
                        (-> with-authors
                          (assoc :last-activity-at created-at)
                          (assoc :new-comments-count new-comments-count)))
          sorted-comments-key (dispatcher/activity-sorted-comments-key org-slug activity-uuid)
          all-old-comments-data (cu/ungroup-comments (get-in db sorted-comments-key))]
      (if all-old-comments-data
        (let [;; If we have the previous comments already loaded
              old-comments-data (filterv :links all-old-comments-data)
              ;; Add the new comment to the comments list, make sure it's not present already
              new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
              new-sorted-comments-data (cu/sort-comments new-comments-data)]
          (-> db
           (assoc-in sorted-comments-key new-sorted-comments-data)
           (assoc-in (dispatcher/activity-key org-slug activity-uuid) with-last-activity-at)))
        ;; In case we don't have the comments already loaded just update the :last-activity-at value
        ;; needed to compare the last read-at of the current user and show NEW comments
        (assoc-in db (dispatcher/activity-key org-slug activity-uuid) with-last-activity-at)))
    db))
