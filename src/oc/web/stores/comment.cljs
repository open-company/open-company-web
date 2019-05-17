(ns oc.web.stores.comment
  (:require [taoensso.timbre :as timbre]
            [defun.core :refer (defun-)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.utils.comment :as comment-utils]))

(defn- is-emoji
  [body]
  (let [plain-text (.text (js/$ (str "<div>" body "</div>")))
        is-emoji? (js/RegExp "^([\ud800-\udbff])([\udc00-\udfff])" "g")
        is-text-message? (js/RegExp "[a-zA-Z0-9\\s!?@#\\$%\\^&(())_=\\-<>,\\.\\*;':\"]" "g")]
    (and ;; emojis can have up to 11 codepoints
         (<= (count plain-text) 11)
         (.match plain-text is-emoji?)
         (not (.match plain-text is-text-message?)))))

(defun- parse-comment
  ([comments :guard sequential?]
    (map parse-comment comments))
  ([comment-map :guard nil?]
    {})
  ([comment-map :guard map?]
    (let [edit-comment-link (utils/link-for (:links comment-map) "partial-update")
          delete-comment-link (utils/link-for (:links comment-map) "delete")
          can-react? (utils/link-for (:links comment-map) "react"  "POST")]
      (-> comment-map
        (assoc :is-emoji (is-emoji (:body comment-map)))
        (assoc :can-edit (boolean edit-comment-link))
        (assoc :can-delete (boolean delete-comment-link))
        (assoc :can-react can-react?)))))

(defmethod dispatcher/action :add-comment-change
  [db [_ org-slug activity-uuid comment-body]]
  (let [add-comment-activity-key (dispatcher/add-comment-activity-key org-slug activity-uuid)]
    (assoc-in db add-comment-activity-key comment-body)))

(defmethod dispatcher/action :add-comment-remove
  [db [_ org-slug activity-uuid]]
  (let [add-comment-key (dispatcher/add-comment-key org-slug)]
    (update-in db add-comment-key dissoc activity-uuid)))

(defmethod dispatcher/action :add-comment-focus
  [db [_ focus-uuid]]
  (assoc db :add-comment-focus focus-uuid))

(defmethod dispatcher/action :comment-add
  [db [_ activity-data comment-body comments-key]]
  (let [comments-data (get-in db comments-key)
        user-data (if (jwt/jwt)
                    (jwt/get-contents)
                    (jwt/get-id-token-contents))
        new-comment-data (parse-comment {:body comment-body
                                         :created-at (utils/as-of-now)
                                         :author {:name (:name user-data)
                                                  :avatar-url (:avatar-url user-data)
                                                  :user-id (:user-id user-data)}})
        new-comments-data (comment-utils/sort-comments (conj comments-data new-comment-data))]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-data]}]]
  (assoc db :comment-add-finish true))

(defmethod dispatcher/action :comments-get
  [db [_ comments-key activity-data]]
  (let [pre-comments-key (vec (butlast comments-key))]
    (assoc-in db (vec (conj pre-comments-key :loading)) true)))

(defmethod dispatcher/action :comments-get/finish
  [db [_ {:keys [success error comments-key body activity-uuid]}]]
  (let [cleaned-comments (map parse-comment (:items (:collection body)))
        sorted-comments (comment-utils/sort-comments cleaned-comments)
        pre-comments-key (vec (butlast comments-key))]
    (-> db
      (assoc-in comments-key sorted-comments)
      (assoc-in (vec (conj pre-comments-key :loading)) false))))

(defmethod dispatcher/action :comment-delete
  [db [_ activity-uuid comment-data comments-key]]
  (let [item-uuid (:uuid comment-data)
        comments-data (get-in db comments-key)
        new-comments-data (remove #(= item-uuid (:uuid %)) comments-data)]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-reaction-toggle
  [db [_ comments-key activity-data comment-data reaction-data reacting?]]
  (let [comment-uuid (:uuid comment-data)
        activity-uuid (:uuid activity-data)
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

(defn inc-time [t]
  (.getTime (js/Date. (inc (.getTime (js/Date. t))))))

(defmethod dispatcher/action :comment-save
  [db [_ comments-key activity-uuid comment-data new-body]]
  (let [item-uuid (:uuid comment-data)
        comments-data (get-in db comments-key)
        comment-idx (utils/index-of comments-data #(= item-uuid (:uuid %)))]
    (if comment-idx
      (let [comment-data (nth comments-data comment-idx)
            with-new-comment (merge comment-data {:body new-body
                                                  :updated-at (inc-time (:updated-at comment-data))})
            new-comments-data (assoc comments-data comment-idx with-new-comment)]
        (assoc-in db comments-key new-comments-data))
      db)))

(defmethod dispatcher/action :ws-interaction/comment-update
  [db [_ comments-key interaction-data]]
  (let [comment-data (:interaction interaction-data)
        item-uuid (:uuid comment-data)
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
            (assoc-in db comments-key new-comments-data))
          db))
      db)))

(defmethod dispatcher/action :ws-interaction/comment-delete
  [db [_ comments-key interaction-data]]
  (let [item-uuid (:uuid (:interaction interaction-data))
        comments-data (get-in db comments-key)
        new-comments-data (remove #(= item-uuid (:uuid %)) comments-data)]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :ws-interaction/comment-add
  [db [_ board-key entry-data interaction-data comments-key]]
  (if entry-data
    ;; If the entry is present in the local state
    (let [; get the comment data from the ws message
          activity-uuid (:resource-uuid interaction-data)
          comment-data (parse-comment (:interaction interaction-data))
          created-at (:created-at comment-data)
          all-old-comments-data (dispatcher/activity-comments-data activity-uuid)
          old-comments-data (filterv :links all-old-comments-data)
          ;; Add the new comment to the comments list, make sure it's not present already
          new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
          sorted-comments-data (comment-utils/sort-comments new-comments-data)
          ;; update the comments link of the entry
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
        (assoc-in (dispatcher/activity-key (second board-key) activity-uuid) with-authors)))
    db))