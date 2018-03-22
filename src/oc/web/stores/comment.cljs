(ns oc.web.stores.comment
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :add-comment-focus
  [db [_ focus?]]
  (assoc db :add-comment-focus focus?))

(defmethod dispatcher/action :comment-add
  [db [_ activity-data comment-body]]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        comments-key (dispatcher/activity-comments-key org-slug board-slug (:uuid activity-data))
        comments-data (get-in db comments-key)
        new-comments-data (conj comments-data {:body comment-body
                                               :created-at (utils/as-of-now)
                                               :author {:name (jwt/get-key :name)
                                                        :avatar-url (jwt/get-key :avatar-url)
                                                        :user-id (jwt/get-key :user-id)}})]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-data]}]]
  (assoc db :comment-add-finish true))

(defmethod dispatcher/action :comments-get
  [db [_ activity-data]]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        comments-key (dispatcher/activity-comments-key org-slug board-slug (:uuid activity-data))
        pre-comments-key (vec (butlast comments-key))]
    (assoc-in db (vec (conj pre-comments-key :loading)) true)))

(defmethod dispatcher/action :comments-get/finish
  [db [_ {:keys [success error body activity-uuid]}]]
  (let [comments-key (dispatcher/activity-comments-key
                      (router/current-org-slug)
                      (router/current-board-slug) activity-uuid)
        sorted-comments (vec (sort-by :created-at (:items (:collection body))))
        pre-comments-key (vec (butlast comments-key))]
    (-> db
      (assoc-in comments-key sorted-comments)
      (assoc-in (vec (conj pre-comments-key :loading)) false))))

(defmethod dispatcher/action :comment-delete
  [db [_ activity-uuid comment-data]]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        item-uuid (:uuid comment-data)
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
        comments-data (get-in db comments-key)
        new-comments-data (remove #(= item-uuid (:uuid %)) comments-data)]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-reaction-toggle
  [db [_ activity-data comment-data reaction-data reacting?]]
  (let [comment-uuid (:uuid comment-data)
        activity-uuid (:uuid activity-data)
        org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
        comments-data (get-in db comments-key)
        comment-idx (utils/index-of comments-data #(= comment-uuid (:uuid %)))]
    ;; the comment has yet to be stored locally in app state so ignore and
    ;; wait for server side reaction
    (if comment-idx
      (let [old-comment-data (nth comments-data comment-idx)
            reactions-data (:reactions old-comment-data)
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
            new-comments-data (assoc comments-data comment-idx new-comment-data)]
        (assoc-in db comments-key new-comments-data))
      db)))

(defmethod dispatcher/action :comment-save
  [db [_ comment-data new-body]]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        activity-uuid (router/current-activity-id)
        item-uuid (:uuid comment-data)
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
        comments-data (get-in db comments-key)
        comment-idx (utils/index-of comments-data #(= item-uuid (:uuid %)))]
    (if comment-idx
      (let [comment-data (nth comments-data comment-idx)
            with-new-comment (assoc comment-data :body new-body)
            new-comments-data (assoc comments-data comment-idx with-new-comment)]
        (assoc-in db comments-key new-comments-data))
      db)))

(defmethod dispatcher/action :ws-interaction/comment-update
  [db [_ interaction-data]]
  (let [; Get the current router data
        org-slug   (router/current-org-slug)
        board-slug (router/current-board-slug)
        comment-data (:interaction interaction-data)
        item-uuid (:uuid comment-data)
        activity-uuid (router/current-activity-id)
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
        comments-data (get-in db comments-key)
        comment-idx (utils/index-of comments-data #(= item-uuid (:uuid %)))]
    (if comment-idx
      (let [old-comment-data (get comments-data comment-idx)]
        (if (<= (utils/js-date
                 (:updated-at old-comment-data))
                (utils/js-date (:updated-at comment-data)))
          (let [body-comment-data (assoc old-comment-data
                                    :body (:body comment-data))
                update-comment-data (assoc body-comment-data
                                      :updated-at (:updated-at comment-data))
                new-comment-data (if (contains? update-comment-data :reactions)
                                   update-comment-data
                                   (assoc update-comment-data :reactions (:reactions old-comment-data)))
                new-comments-data (assoc comments-data comment-idx new-comment-data)]
            (assoc-in db comments-key new-comments-data))
          db))
      db)))

(defmethod dispatcher/action :ws-interaction/comment-delete
  [db [_ comment-data]]
  (let [; Get the current router data
        org-slug   (router/current-org-slug)
        board-slug (router/current-board-slug)
        item-uuid (:uuid (:interaction comment-data))
        activity-uuid (router/current-activity-id)
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
        comments-data (get-in db comments-key)
        new-comments-data (remove #(= item-uuid (:uuid %)) comments-data)]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :ws-interaction/comment-add
  [db [_ interaction-data]]
  (let [; Get the current router data
        org-slug   (router/current-org-slug)
        board-slug (router/current-board-slug)
        is-all-posts (:from-all-posts @router/path)
        activity-uuid (:resource-uuid interaction-data)
        board-key (if is-all-posts
                    (dispatcher/all-posts-key org-slug)
                    (dispatcher/board-data-key org-slug board-slug))
        ; Entry data
        entry-key (dispatcher/activity-key org-slug board-slug activity-uuid)
        entry-data (get-in db entry-key)]
    (if entry-data
      ; If the entry is present in the local state
      (let [; get the comment data from the ws message
            comment-data (:interaction interaction-data)
            created-at (:created-at comment-data)
            all-old-comments-data (dispatcher/activity-comments-data activity-uuid)
            old-comments-data (filterv :links all-old-comments-data)
            ; Add the new comment to the comments list, make sure it's not present already
            new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
            sorted-comments-data (vec (sort-by :created-at new-comments-data))
            comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
            ; update the comments link of the entry
            comments-link-idx (utils/index-of
                               (:links entry-data)
                               #(and (= (:rel %) "comments") (= (:method %) "GET")))
            with-increased-count (update-in entry-data [:links comments-link-idx :count] inc)
            old-authors (or (:authors (get (:links entry-data) comments-link-idx)) [])
            new-author (:author comment-data)
            new-authors (if (and old-authors (first (filter #(= (:user-id %) (:user-id new-author)) old-authors)))
                          old-authors
                          (concat [new-author] old-authors))
            with-authors (assoc-in with-increased-count [:links comments-link-idx :authors] new-authors)]
        (-> db
         (assoc-in comments-key sorted-comments-data)
         (assoc-in (vec (concat board-key [:fixed-items activity-uuid])) with-authors)))
      db)))