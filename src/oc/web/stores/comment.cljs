(ns oc.web.stores.comment
  (:require [taoensso.timbre :as timbre]
            [cljs-flux.dispatcher :as flux]
            [defun.core :refer (defun-)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]))

(defonce comments-atom (atom {}))

(defn make-post-index [uuid]
  (keyword (str "post-" uuid)))

(defn make-comment-index [uuid]
  (keyword (str "comment-" uuid)))

;; Reducers used to watch for reaction dispatch data
(defmulti reducer (fn [db [action-type & _]]
                    (when-not (some #{action-type} [:update :input])
                      (timbre/debug "Dispatching comment reducer:" action-type))
                    action-type))

(def comments-dispatch
  (flux/register
   dispatcher/actions
   (fn [payload]
     (swap! dispatcher/app-state reducer payload))))

;; This function is used to store the comment uuid and the comments key to
;; find that comment in the app state.  The comment store can then find the
;; key by the comment uuid. It does NOT change the app state.
(defn- index-comments
  [ra org board-slug post-uuid comments]
  (reduce (fn [acc comment]
            (let [idx (make-comment-index (:uuid comment))
                  comment-key (dispatcher/activity-comments-key
                               org
                               board-slug
                               post-uuid)]
              (assoc acc idx comment-key)))
          ra comments))

;; This is used to store the relationship between an post uuid that has
;; comments with the post key in the app state. When a related comment
;; is then needed you can search for the key by the post uuid. It does not
;; change the app state.
(defn- index-posts
  [ra org posts]
  (reduce (fn [acc post]
            (let [board-slug (:board-slug post)
                  post-uuid (:uuid post)
                  idx (make-post-index post-uuid)
                  post-key (dispatcher/activity-key
                                org
                                board-slug
                                post-uuid)
                  next-acc (index-comments acc org board-slug post-uuid (:comments post))]
              (assoc next-acc idx post-key)))
          ra posts))

(defun- sort-comments
  ([comments :guard nil?]
   [])
  ([comments :guard map?]
   (sort-comments (vals comments)))
  ([comments :guard sequential?]
   (vec (reverse (sort-by :created-at comments)))))


(defn- is-emoji
  [body]
  (let [plain-text (.text (js/$ (str "<div>" body "</div>")))
        is-emoji? (js/RegExp "^([\ud800-\udbff])([\udc00-\udfff])" "g")
        is-text-message? (js/RegExp "[a-zA-Z0-9\\s!?@#\\$%\\^&(())_=\\-<>,\\.\\*;':\"]" "g")]
    (and ;; emojis can have up to 11 codepoints
         (<= (count plain-text) 11)
         (.match plain-text is-emoji?)
         (not (.match plain-text is-text-message?)))))

(defn- can-react?
  [reaction-data]
  (or
   (utils/link-for (:links reaction-data) "react"  ["PUT" "DELETE"])
   (pos? (:count reaction-data))))

(defun- parse-comment
  ([comments :guard sequential?]
    (map parse-comment comments))
  ([comment-map :guard nil?]
    {})
  ([comment-map :guard map?]
    (-> comment-map
      (assoc :is-emoji (is-emoji (:body comment-map)))
      (assoc :can-react (can-react? (first (:reactions comment-map)))))))

(defmethod dispatcher/action :add-comment-focus
  [db [_ focus-uuid]]
  (assoc db :add-comment-focus focus-uuid))

(defmethod dispatcher/action :comment-add
  [db [_ org-slug board-slug post-data comment-body]]
  (let [comments-key (dispatcher/activity-comments-key org-slug board-slug (:uuid post-data))
        comments-data (get-in db comments-key)
        new-comment-data (parse-comment {:body comment-body
                                                          :created-at (utils/as-of-now)
                                                          :author {:name (jwt/get-key :name)
                                                                   :avatar-url (jwt/get-key :avatar-url)
                                                                   :user-id (jwt/get-key :user-id)}})
        new-comments-data (sort-comments (conj comments-data new-comment-data))]
    (index-comments @comments-atom org-slug board-slug (:uuid post-data) [new-comment-data])
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-data]}]]
  (assoc db :comment-add-finish true))

(defmethod dispatcher/action :comments-get
  [db [_ org-slug board-slug post-data]]
  (let [comments-key (dispatcher/activity-comments-key org-slug board-slug (:uuid post-data))
        pre-comments-key (vec (butlast comments-key))]
    (index-posts @comments-atom org-slug [post-data])
    (assoc-in db (vec (conj pre-comments-key :loading)) true)))

(defmethod dispatcher/action :comments-get/finish
  [db [_ {:keys [success error body activity-uuid]}]]
  (let [post-key (get @comments-atom (make-post-index activity-uuid))
        org-slug (name (nth post-key 0))
        board-slug (name (nth post-key 2))
        comments-key (dispatcher/activity-comments-key
                      org-slug
                      board-slug
                      activity-uuid)
        all-posts-key (assoc comments-key 2 :all-posts)
        cleaned-comments (map parse-comment (:items (:collection body)))
        sorted-comments (sort-comments cleaned-comments)
        pre-comments-key (vec (butlast comments-key))]
    (index-comments @comments-atom org-slug board-slug activity-uuid sorted-comments)
    (-> db
      (assoc-in comments-key sorted-comments)
      (assoc-in all-posts-key sorted-comments)
      (assoc-in (vec (conj pre-comments-key :loading)) false))))

(defmethod dispatcher/action :comment-delete
  [db [_ org-slug board-slug post-uuid comment-data]]
  (let [item-uuid (:uuid comment-data)
        comments-key (dispatcher/activity-comments-key org-slug board-slug post-uuid)
        comments-data (get-in db comments-key)
        new-comments-data (remove #(= item-uuid (:uuid %)) comments-data)]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-reaction-toggle
  [db [_ org-slug board-slug post-data comment-data reaction-data reacting?]]
  (let [comment-uuid (:uuid comment-data)
        post-uuid (:uuid post-data)
        comments-key (dispatcher/activity-comments-key org-slug board-slug post-uuid)
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
  [db [_ org-slug board-slug post-uuid comment-data new-body]]
  (let [item-uuid (:uuid comment-data)
        comments-key (dispatcher/activity-comments-key org-slug board-slug post-uuid)
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
  (let [comment-data (:interaction interaction-data)
        item-uuid (:uuid comment-data)
        comments-key (get @comments-atom (make-comment-index item-uuid))
        all-posts-key (assoc comments-key 2 :all-posts)
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
                new-comments-data (assoc comments-data comment-idx (parse-comment new-comment-data))]
            (-> db
              (assoc-in all-posts-key new-comments-data)
              (assoc-in comments-key new-comments-data)))
          db))
      db)))

(defmethod dispatcher/action :ws-interaction/comment-delete
  [db [_ interaction-data]]
  (let [item-uuid (:uuid (:interaction interaction-data))
        comments-key (get @comments-atom (make-comment-index item-uuid))
        all-posts-key (assoc comments-key 2 :all-posts)
        comments-data (get-in db comments-key)
        new-comments-data (remove #(= item-uuid (:uuid %)) comments-data)]
    (-> db
      (assoc-in all-posts-key new-comments-data)
      (assoc-in comments-key new-comments-data))))

(defmethod dispatcher/action :ws-interaction/comment-add
  [db [_ interaction-data]]
  (let [post-uuid (:resource-uuid interaction-data)
        board-key (dispatcher/current-board-key)
        entry-key (@comments-atom (make-post-index post-uuid))
        entry-data (get-in db entry-key)]
    (if entry-data
      ; If the entry is present in the local state
      (let [; get the comment data from the ws message
            comment-data (parse-comment (:interaction interaction-data))
            created-at (:created-at comment-data)
            all-old-comments-data (dispatcher/activity-comments-data post-uuid)
            old-comments-data (filterv :links all-old-comments-data)
            ; Add the new comment to the comments list, make sure it's not present already
            new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
            sorted-comments-data (sort-comments new-comments-data)
            comments-key (get @comments-atom (make-comment-index (:uuid comment-data)))
            all-posts-key (assoc comments-key 2 :all-posts)
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
         (assoc-in all-posts-key sorted-comments-data)
         (assoc-in (vec (concat board-key [:fixed-items post-uuid])) with-authors)))
      db)))

;; Reaction store specific reducers
(defmethod reducer :default [db payload]
  ;; ignore state changes not specific to reactions
  db)

(defmethod reducer :all-posts-get/finish
  [db [_ {:keys [org year month from body]}]]
  (swap! comments-atom index-posts org (-> body :collection :items))
  db)

(defmethod reducer :section
  [db [_ board-data]]
  (let [org (utils/section-org-slug board-data)
        fixed-board-data (utils/fix-board board-data (dispatcher/change-data db))]
    (swap! comments-atom index-posts org (vals (:fixed-items fixed-board-data))))
  db)
