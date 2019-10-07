(ns oc.web.stores.comment
  (:require [taoensso.timbre :as timbre]
            [defun.core :refer (defun)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
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
          is-root-comment (empty? (:parent-uuid comment-map))]
      (-> comment-map
        (assoc :is-emoji (is-emoji (:body comment-map)))
        (assoc :can-edit (boolean edit-comment-link))
        (assoc :can-delete (boolean delete-comment-link))
        (assoc :can-react can-react?)
        (assoc :reply-parent reply-parent)
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
      (update-in [dispatcher/add-comment-force-update-key comment-key] #(if force-update? (utils/activity-uuid) %)))))

(defmethod dispatcher/action :add-comment-reset
  [db [_ org-slug activity-uuid parent-comment-uuid comment-uuid]]
  (let [add-comment-key (dispatcher/add-comment-key org-slug)
        comment-key (dispatcher/add-comment-string-key activity-uuid parent-comment-uuid comment-uuid)]
    (-> db
      (update-in add-comment-key dissoc comment-key)
      ;; Force refresh of the add comment field to remove the body
      (assoc-in [dispatcher/add-comment-force-update-key comment-key] (utils/activity-uuid)))))

(defmethod dispatcher/action :add-comment-focus
  [db [_ focus-uuid]]
  (assoc db :add-comment-focus focus-uuid))

(defmethod dispatcher/action :comment-add
  [db [_ activity-data comment-data parent-comment-uuid comments-key]]
  (let [comments-data (vec (get-in db comments-key))
        new-comment-data (parse-comment (dispatcher/org-data db)
                                        activity-data
                                        comment-data)
        all-comments (concat comments-data [new-comment-data])
        sorted-all-comments (comment-utils/sort-comments all-comments)]
    (assoc-in db comments-key sorted-all-comments)))

(defmethod dispatcher/action :comment-add/replace
  [db [_ activity-data comment-data comments-key new-comment-uuid]]
  (let [comments-data (vec (get-in db comments-key))
        old-comments-data (filterv #(not= (:uuid %) new-comment-uuid) comments-data)
        new-comment-data (parse-comment (dispatcher/org-data db)
                                        activity-data
                                        comment-data)
        all-comments (concat old-comments-data [new-comment-data])
        sorted-all-comments (comment-utils/sort-comments all-comments)]
    (assoc-in db comments-key sorted-all-comments)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-data body]}]]
  (-> db
    (assoc :comment-add-finish true)
    (assoc :add-comment-highlight (:uuid body))))

(defmethod dispatcher/action :comment-add/failed
  [db [_ activity-data comment-data comments-key]]
  (let [all-comments (get-in db comments-key)
        filtered-comments (filterv #(not= (:uuid comment-data) (:uuid %)) all-comments)
        sorted-filtered-comments (comment-utils/sort-comments filtered-comments)]
    (assoc-in db comments-key sorted-filtered-comments)))

(defmethod dispatcher/action :comment-save/failed
  [db [_ activity-data comment-data comments-key]]
  (let [all-comments (get-in db comments-key)
        filtered-comments (filterv #(not= (:uuid comment-data) (:uuid %)) all-comments)
        sorted-filtered-comments (comment-utils/sort-comments (conj filtered-comments comment-data))]
    (assoc-in db comments-key sorted-filtered-comments)))

(defmethod dispatcher/action :add-comment-highlight-reset
  [db [_]]
  (dissoc db :add-comment-highlight))

(defmethod dispatcher/action :comments-get
  [db [_ comments-key activity-data]]
  (let [pre-comments-key (vec (butlast comments-key))]
    (assoc-in db (vec (conj pre-comments-key :loading)) true)))

(defmethod dispatcher/action :comments-get/finish
  [db [_ {:keys [success error comments-key body secure-activity-uuid activity-uuid]}]]
  (let [pre-comments-key (vec (butlast comments-key))]
    (if success
      (let [org-data (dispatcher/org-data db)
            activity-data (if secure-activity-uuid
                            (dispatcher/secure-activity-data (:slug org-data) secure-activity-uuid db)
                            (dispatcher/activity-data (:slug org-data) activity-uuid db))
            cleaned-comments (map #(parse-comment org-data activity-data %) (:items (:collection body)))
            sorted-comments (comment-utils/sort-comments cleaned-comments)]
        (-> db
          (assoc-in comments-key sorted-comments)
          (assoc-in (vec (conj pre-comments-key :loading)) false)))
      (assoc-in db (vec (conj pre-comments-key :loading)) false))))

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

(defmethod dispatcher/action :comment-react-from-picker
  [db [_ comments-key activity-data comment-data reaction]]
  (let [comments-data (get-in db comments-key)
        comment-idx (utils/index-of comments-data #(= (:uuid comment-data) (:uuid %)))]
    ;; the comment has yet to be stored locally in app state so ignore and
    ;; wait for server side reaction
    (if comment-idx
      (let [old-comment-data (nth comments-data comment-idx)
            reactions-data (:reactions old-comment-data)
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
            new-comments-data (assoc comments-data comment-idx new-comment-data)]
        (assoc-in db comments-key new-comments-data))
      db)))

(defmethod dispatcher/action :comment-save
  [db [_ org-slug comments-key updated-comment-map]]
  (let [all-comments (get-in db comments-key)
        filtered-comments (filterv #(not= (:uuid %) (:uuid updated-comment-map)) all-comments)
        sorted-new-comments (comment-utils/sort-comments (conj filtered-comments updated-comment-map))]
    (assoc-in db comments-key sorted-new-comments)))

(defmethod dispatcher/action :ws-interaction/comment-update
  [db [_ comments-key interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        org-data (dispatcher/org-data db)
        activity-data (dispatcher/activity-data (:slug org-data) activity-uuid db)
        comment-data (:interaction interaction-data)
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
                new-comments-data (assoc comments-data comment-idx (parse-comment org-data activity-data new-comment-data))]
            (assoc-in db comments-key new-comments-data))
          db))
      db)))

(defmethod dispatcher/action :ws-interaction/comment-delete
  [db [_ org-slug interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        item-uuid (:uuid (:interaction interaction-data))
        comments-key (dispatcher/activity-comments-key org-slug activity-uuid)
        comments-data (get-in db comments-key)
        new-comments-data (vec (remove #(= item-uuid (:uuid %)) comments-data))
        last-not-own-comment (last (sort-by :created-at (filterv #(not= (:user-id %) (jwt/user-id)) new-comments-data)))]
    (-> db
      (update-in (dispatcher/activity-key org-slug activity-uuid) merge {:new-at (:created-at last-not-own-comment)})
      (assoc-in comments-key new-comments-data))))

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
                             (:links entry-data)
                             #(and (= (:rel %) "comments") (= (:method %) "GET")))
          with-increased-count (update-in entry-data [:links comments-link-idx :count] inc)
          old-authors (or (:authors (get (:links entry-data) comments-link-idx)) [])
          new-author (:author comment-data)
          new-authors (if (and old-authors (first (filter #(= (:user-id %) (:user-id new-author)) old-authors)))
                        old-authors
                        (concat [new-author] old-authors))
          with-authors (assoc-in with-increased-count [:links comments-link-idx :authors] new-authors)
          comment-from-current-user? (= (:user-id comment-data) (jwt/user-id))
          with-new-at (if comment-from-current-user?
                        with-authors
                        (assoc with-authors :new-at created-at))
          all-old-comments-data (dispatcher/activity-comments-data activity-uuid)]
      (if all-old-comments-data
        (let [;; If we have the previous comments already loaded
              old-comments-data (filterv :links all-old-comments-data)
              ;; Add the new comment to the comments list, make sure it's not present already
              new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
              sorted-comments-data (comment-utils/sort-comments new-comments-data)]
          (-> db
            (assoc-in (dispatcher/activity-comments-key org-slug activity-uuid) sorted-comments-data)
            (assoc-in (dispatcher/activity-key org-slug activity-uuid) with-new-at)
            ;; Highlight the comment being added
            (update-in [:add-comment-highlight] #(if comment-from-current-user? % (:uuid comment-data)))))
        ;; In case we don't have the comments already loaded just update the :new-at value
        ;; needed to compare the last read-at of the current user and show NEW comments
        (assoc-in db (dispatcher/activity-key org-slug activity-uuid) with-new-at)))
    db))