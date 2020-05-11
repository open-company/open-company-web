(ns oc.web.stores.reaction
  (:require [taoensso.timbre :as timbre]
            [cljs-flux.dispatcher :as flux]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.dispatcher :as dispatcher]))

;; Store reaction and related data
(defonce reactions-atom (atom {}))

(defn make-activity-index [uuid]
  (keyword (str "activity-" uuid)))

(defn make-comment-index [uuid]
  (keyword (str "comment-" uuid)))

;; Reducers used to watch for reaction dispatch data
(defmulti reducer (fn [db [action-type & _]]
                    (when-not (some #{action-type} [:update :input])
                      (timbre/debug "Dispatching reaction reducer:" action-type))
                    action-type))

(def reactions-dispatch
  (flux/register
   dispatcher/actions
   (fn [payload]
     (swap! dispatcher/app-state reducer payload))))

;; Handle dispatch events
(defn handle-reaction-to-entry-finish
  [db activity-data reaction reaction-data activity-key]
  (let [activity-uuid (:uuid activity-data)
        next-reactions-loading (utils/vec-dissoc (:reactions-loading activity-data) reaction)]
    (if (nil? reaction-data)
      (let [updated-activity-data (assoc activity-data :reactions-loading next-reactions-loading)]
        (assoc-in db activity-key updated-activity-data))
      (let [reaction (first (keys reaction-data))
            next-reaction-data (assoc (get reaction-data reaction) :reaction (name reaction))
            reactions-data (or (:reactions activity-data) [])
            reaction-idx (utils/index-of reactions-data #(= (:reaction %) (name reaction)))
            next-reactions-data (if (or (neg? reaction-idx) (nil? reaction-idx))
                                  (assoc reactions-data (count reactions-data) next-reaction-data)
                                  (assoc reactions-data reaction-idx next-reaction-data))
            updated-activity-data (-> activity-data
                                   (assoc :reactions-loading next-reactions-loading)
                                   (assoc :reactions next-reactions-data))]
        (assoc-in db activity-key updated-activity-data)))))

(defn handle-reaction-to-entry [db activity-data reaction-data activity-key]
  (let [old-reactions-loading (or (:reactions-loading activity-data) [])
        next-reactions-loading (conj old-reactions-loading (:reaction reaction-data))
        updated-activity-data (assoc activity-data :reactions-loading next-reactions-loading)]
    (assoc-in db activity-key updated-activity-data)))

(defmethod dispatcher/action :handle-reaction-to-entry
  [db [_ activity-data reaction-data activity-key]]
  (handle-reaction-to-entry-finish db activity-data (:reaction reaction-data) {(keyword (:reaction reaction-data)) reaction-data} activity-key))

(defmethod dispatcher/action :react-from-picker/finish
  [db [_ {:keys [status activity-data reaction reaction-data activity-key]}]]
  (if (and (>= status 200)
           (< status 300))
    (let [reaction-key (first (keys reaction-data))
          reaction (name reaction-key)]
      (handle-reaction-to-entry-finish db activity-data reaction reaction-data activity-key))
    ;; Wait for the entry refresh if it didn't
    db))

(defmethod dispatcher/action :activity-reaction-toggle/finish
  [db [_ activity-data reaction reaction-data activity-key]]
  (handle-reaction-to-entry-finish db activity-data reaction reaction-data activity-key))

(defn- update-entry-reaction-data
  [add-event? interaction-data entry-data]
  (when entry-data
    (let [reaction-data (assoc (:interaction interaction-data) :count (:count interaction-data))
          reaction (:reaction reaction-data)
          reactions-data (:reactions entry-data)
          reaction-idx (utils/index-of reactions-data #(= (:reaction %) reaction))
          reaction-found (and (number? reaction-idx) (>= reaction-idx 0))
          old-reaction-data (when reaction-found
                             (get reactions-data reaction-idx))
          is-current-user (= (jwt/get-key :user-id) (:user-id (:author reaction-data)))
          authors-fn (if add-event? conj utils/vec-dissoc)
          with-authors (-> reaction-data
                        (assoc :authors (authors-fn (:authors old-reaction-data) (:name (:author reaction-data))))
                        (assoc :author-ids (authors-fn (:author-ids old-reaction-data) (:user-id (:author reaction-data)))))
          with-reacted (if is-current-user
                         ;; If the reaction is from the current user we need to
                         ;; update the reacted, the links are the one coming with
                         ;; the WS message
                         (assoc with-authors :reacted add-event?)
                         ;; If it's a reaction from another user we need to
                         ;; survive the reacted and the links from the reactions
                         ;; we already have
                         (assoc with-authors :reacted (if old-reaction-data (:reacted old-reaction-data) false)))
          new-reactions-data (if reactions-data
                               (assoc reactions-data (if reaction-found reaction-idx
                                                      (count reactions-data)) with-reacted)
                               [with-reacted])
          new-entry-data (assoc entry-data :reactions new-reactions-data)]
      new-entry-data)))

(defn- update-entry-reaction
  [db interaction-data add-event?]
  (let [item-uuid (:resource-uuid interaction-data)
        entries-key (get @reactions-atom (make-activity-index item-uuid))
        ;; look for data in the board
        keys (remove nil? [entries-key])
        new-data (->> (mapcat #(vector %
                                (update-entry-reaction-data add-event? interaction-data
                                 (get-in db %)))
                       keys)
                  (partition 2)
                  (filter second) ;; remove nil data
                  (map vec))]
    (when (seq new-data)
      (reduce #(assoc-in %1 (first %2) (second %2)) db new-data))))

(defn- get-comments-data
  [db data-key item-uuid]
  (let [comments-data (get-in db data-key)
        comment-idx (utils/index-of comments-data #(= item-uuid (:uuid %)))]
    (when comment-idx
      {:index comment-idx :data comments-data})))

(defn- update-comments-data
  [add-event? interaction-data data]
  (when data
    (let [reaction-data (:interaction interaction-data)
          reaction (:reaction reaction-data)
          comment-data (nth (:data data) (:index data))
          reactions-data (:reactions comment-data)
          reaction-idx (utils/index-of reactions-data #(= (:reaction %) reaction))]
      (let [
          is-current-user (= (jwt/get-key :user-id) (:user-id (:author reaction-data)))
          old-reaction-data (if reaction-idx
                              (nth reactions-data reaction-idx)
                              ;; In case we don't have an old reaction
                              ;; grab the links from the WS message and set reacted
                              ;; true only if the reaction is coming from the current user
                              ;; and is an add interaction
                              {:reacted (when is-current-user
                                          add-event?)
                               :authors []
                               :author-ids []
                               :links (:links reaction-data)})
          authors-fn (if add-event? conj utils/vec-dissoc)
          with-reacted (merge reaction-data {:count (:count interaction-data)
                                             :reacted (:reacted old-reaction-data)
                                             :authors (authors-fn (:authors old-reaction-data)
                                                       (:name (:author reaction-data)))
                                             :author-ids (authors-fn (:author-ids old-reaction-data)
                                                          (:user-id (:author reaction-data)))
                                             :links (:links old-reaction-data)})
          new-reactions-data (if reaction-idx
                               (assoc reactions-data reaction-idx with-reacted)
                               (conj (or reactions-data []) with-reacted))
          new-comment-data (assoc comment-data :reactions new-reactions-data)
          new-comments-data (assoc (:data data) (:index data) new-comment-data)]
      new-comments-data))))

(defn- update-comment-reaction
  [db interaction-data add-event?]
  (let [item-uuid (:resource-uuid interaction-data)
        comments-key (get @reactions-atom (make-comment-index item-uuid))
        ;; look for data in the board
        keys (remove nil? [comments-key])
        new-data (->> (mapcat #(vector %
                                       (update-comments-data
                                        add-event?
                                        interaction-data
                                        (get-comments-data db % item-uuid)))
                              keys)
                      (partition 2)
                      (filter second) ;; remove nil data
                      (map vec))]
    (when (seq new-data)
      (reduce #(assoc-in %1 (first %2) (second %2)) db new-data))))

(defn- update-reaction
  [db interaction-data add-event?]
  (let [with-updated-comment (update-comment-reaction db interaction-data add-event?)
        with-updated-activity (update-entry-reaction db interaction-data add-event?)]
    (or with-updated-comment with-updated-activity db)))

(defmethod dispatcher/action :ws-interaction/reaction-add
  [db [_ interaction-data]]
  (update-reaction db interaction-data true))

(defmethod dispatcher/action :ws-interaction/reaction-delete
  [db [_ interaction-data]]
  (update-reaction db interaction-data false))

;; Reaction store specific reducers
(defmethod reducer :default [db payload]
  ;; ignore state changes not specific to reactions
  db)

;; This function is used to store the comment uuid and the comments key to
;; find that comment in the app state.  The reaction store can then find the
;; key by the comment uuid. It does NOT change the app state.
(defn- index-comments
  [ra org board-slug activity-uuid comments]
  (reduce (fn [acc comment]
            (let [idx (make-comment-index (:uuid comment))
                  comment-key (dispatcher/activity-comments-key
                               org
                               activity-uuid)]
              (assoc acc idx comment-key)))
          ra comments))

;; This is used to store the relationship between an activity uuid that has
;; reactions with the activity key in the app state. When a related reaction
;; is then needed you can search for the key by the activity uuid. It does not
;; change the app state.
(defn- index-posts
  [ra org posts]
  (reduce (fn [acc post]
            (let [board-slug (:board-slug post)
                  activity-uuid (:uuid post)
                  idx (make-activity-index activity-uuid)
                  activity-key (dispatcher/activity-key
                                org
                                activity-uuid)
                  next-acc (index-comments acc org board-slug activity-uuid (:comments post))]
              (assoc next-acc idx activity-key)))
          ra posts))

(defmethod reducer :all-posts-get/finish
  [db [_ org fixed-body]]
  (swap! reactions-atom index-posts org (-> fixed-body :fixed-items vals))
  db)

(defmethod reducer :all-posts-more/finish
  [db [_ org direction fixed-body]]
  (swap! reactions-atom index-posts org (-> fixed-body :fixed-items vals))
  db)

(defmethod reducer :inbox-get/finish
  [db [_ org fixed-body]]
  (swap! reactions-atom index-posts org (-> fixed-body :fixed-items vals))
  db)

(defmethod reducer :bookmarks-get/finish
  [db [_ org fixed-body]]
  (swap! reactions-atom index-posts org (-> fixed-body :fixed-items vals))
  db)

(defmethod reducer :following-get/finish
  [db [_ org fixed-body]]
  (swap! reactions-atom index-posts org (-> fixed-body :fixed-items vals))
  db)

(defmethod reducer :following-get/finish
  [db [_ org fixed-body]]
  (swap! reactions-atom index-posts org (-> fixed-body :fixed-items vals))
  db)

(defmethod reducer :section
  [db [_ board-data]]
  (let [org (utils/section-org-slug board-data)
        fixed-board-data (au/fix-board board-data (dispatcher/change-data db))]
    (swap! reactions-atom index-posts org (-> fixed-board-data :fixed-items vals)))
  db)

(defmethod reducer :ws-interaction/comment-add
  [db [_ org-slug entry-data interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        activity-key (@reactions-atom (make-activity-index activity-uuid))
        board-slug (nth activity-key 2)]
    (when activity-key
      (swap! reactions-atom index-comments
             org-slug
             board-slug
             activity-uuid
             [(:interaction interaction-data)])))
  db)

(defmethod reducer :ws-interaction/comment-delete
  [db [_ org-slug interaction-data]]
  (let [activity-uuid (:resource-uuid interaction-data)
        activity-key (@reactions-atom (make-activity-index activity-uuid))
        board-slug (nth activity-key 2)]
    (when activity-key
      (swap! reactions-atom index-comments
             org-slug
             board-slug
             activity-uuid
             [(:interaction interaction-data)])))
  db)

(defmethod reducer :comments-get/finish
  [db [_ {:keys [success error body activity-uuid]}]]
  (let [activity-key (@reactions-atom (make-activity-index activity-uuid))
        org (first activity-key)
        board-slug (nth activity-key 2)]
    (when activity-key
      (swap! reactions-atom index-comments
             org
             board-slug
             activity-uuid
             (:items (:collection body)))))
  db)
