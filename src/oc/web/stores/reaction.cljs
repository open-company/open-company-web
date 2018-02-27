(ns oc.web.stores.reaction
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]))

(defn handle-reaction-to-entry-finish
  [db activity-data reaction reaction-data]
  (let [activity-uuid (:uuid activity-data)
        next-reactions-loading (utils/vec-dissoc (:reactions-loading activity-data) reaction)
        activity-key (concat (dispatcher/current-board-key) [:fixed-items activity-uuid])]
    (if (nil? reaction-data)
      (let [updated-activity-data (assoc activity-data :reactions-loading next-reactions-loading)]
        (assoc-in db activity-key updated-activity-data))
      (let [reaction (first (keys reaction-data))
            next-reaction-data (assoc (get reaction-data reaction) :reaction (name reaction))
            reactions-data (or (:reactions activity-data) [])
            reaction-idx (utils/index-of reactions-data #(= (:reaction %) (name reaction)))
            next-reactions-data (if (pos? (:count next-reaction-data))
                                  (if (or (neg? reaction-idx) (nil? reaction-idx))
                                    (assoc reactions-data (count reactions-data) next-reaction-data)
                                    (assoc reactions-data reaction-idx next-reaction-data))
                                  (vec (remove #(= (:reaction %) (name reaction)) reactions-data)))
            updated-activity-data (-> activity-data
                                   (assoc :reactions-loading next-reactions-loading)
                                   (assoc :reactions next-reactions-data))]
        (assoc-in db activity-key updated-activity-data)))))

(defn handle-reaction-to-entry [db activity-data reaction-data]
  (let [board-key (dispatcher/current-board-key)
        old-reactions-loading (or (:reactions-loading activity-data) [])
        next-reactions-loading (conj old-reactions-loading (:reaction reaction-data))
        updated-activity-data (assoc activity-data :reactions-loading next-reactions-loading)
        activity-key (concat board-key [:fixed-items (:uuid activity-data)])]
    (assoc-in db activity-key updated-activity-data)))

(defmethod dispatcher/action :handle-reaction-to-entry
  [db [_ activity-data reaction-data]]
  (handle-reaction-to-entry db activity-data reaction-data))

(defmethod dispatcher/action :react-from-picker/finish
  [db [_ {:keys [status activity-data reaction reaction-data]}]]
  (if (and (>= status 200)
           (< status 300))
    (let [reaction-key (first (keys reaction-data))
          reaction (name reaction-key)]
      (handle-reaction-to-entry-finish db activity-data reaction reaction-data))
    ;; Wait for the entry refresh if it didn't
    db))

(defmethod dispatcher/action :activity-reaction-toggle/finish
  [db [_ activity-data reaction reaction-data]]
  (handle-reaction-to-entry-finish db activity-data reaction reaction-data))

(defn- update-entry-reaction
    "Need to update the local state with the data we have, if the interaction is from the actual unchecked-short
     we need to refresh the entry since we don't have the links to delete/add the reaction."
    [db interaction-data add-event?]
  (let [; Get the current router data
        org-slug (router/current-org-slug)
        is-all-posts (:from-all-posts @router/path)
        board-slug (router/current-board-slug)
        activity-uuid (:resource-uuid interaction-data)
        ; Entry data
        fixed-activity-uuid (or (router/current-secure-activity-id) activity-uuid)
        is-secure-activity (router/current-secure-activity-id)
        secure-activity-data (when is-secure-activity
                              (dispatcher/activity-data org-slug board-slug fixed-activity-uuid))
        entry-key (dispatcher/activity-key org-slug board-slug fixed-activity-uuid)
        entry-data (get-in db entry-key)]
    (if (and is-secure-activity
             (not= (:uuid secure-activity-data) activity-uuid))
      db
      (if (and entry-data (seq (:reactions entry-data)))
        ; If the entry is present in the local state and it has reactions
        (let [reaction-data (:interaction interaction-data)
              old-reactions-data (or (:reactions entry-data) [])
              reaction-idx (utils/index-of old-reactions-data #(= (:reaction %) (:reaction reaction-data)))
              old-reaction-data (if reaction-idx
                                  (get old-reactions-data reaction-idx)
                                  {:reacted false :reaction (:reaction reaction-data)})
              with-reaction-data (assoc old-reaction-data :count (:count interaction-data))
              is-current-user (= (jwt/get-key :user-id) (:user-id (:author reaction-data)))
              reacted (if is-current-user add-event? (:reacted old-reaction-data))
              with-reacted (assoc with-reaction-data :reacted reacted)
              with-links (assoc with-reacted :links (:links reaction-data))
              new-reactions-data (if (pos? (:count with-links))
                                   (if (or (neg? reaction-idx) (nil? reaction-idx))
                                     (assoc old-reactions-data (count old-reactions-data) with-links)
                                     (assoc old-reactions-data reaction-idx with-links))
                                   (vec (remove #(= (:reaction %) (:reaction reaction-data)) old-reactions-data)))
              ; Update the entry with the new reaction
              updated-entry-data (assoc entry-data :reactions new-reactions-data)]
          ; Update the entry in the local state with the new reaction
          (assoc-in db entry-key updated-entry-data))
        ;; the entry is not present, refresh the full board
        db))))

(defn- update-comment-reaction
  [db interaction-data add-event?]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        activity-uuid (router/current-activity-id)
        item-uuid (:resource-uuid interaction-data)
        reaction-data (:interaction interaction-data)
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
        comments-data (get-in db comments-key)
        comment-idx (utils/index-of comments-data #(= item-uuid (:uuid %)))]
    ;; the comment has yet to be stored locally in app state so ignore and
    ;; wait for server side reaction
    (when comment-idx
      (let [reaction (:reaction reaction-data)
            comment-data (nth comments-data comment-idx)
            reactions-data (:reactions comment-data)
            reaction-idx (utils/index-of reactions-data #(= (:reaction %) reaction))
            old-reaction-data (nth reactions-data reaction-idx)
            reaction-data-with-count (assoc reaction-data :count (:count interaction-data))
            is-current-user (= (jwt/get-key :user-id) (:user-id (:author reaction-data)))
            with-reacted (if is-current-user
                           ;; If the reaction is from the current user we need to update
                           ;; the reacted, the links are the one coming with the WS message
                           (assoc reaction-data-with-count :reacted add-event?)
                           ;; If it's a reaction from another user we need to survive the
                           ;; reacted and the links from the reactions we already have
                           (merge reaction-data-with-count {:reacted (:reacted old-reaction-data)
                                                            :links (:links old-reaction-data)}))
            with-links (assoc with-reacted :links old-reaction-data)
            new-reactions-data (assoc reactions-data reaction-idx with-reacted)
            new-comment-data (assoc comment-data :reactions new-reactions-data)
            new-comments-data (assoc comments-data comment-idx new-comment-data)]
        (assoc-in db comments-key new-comments-data)))))

(defn- update-reaction
  [db interaction-data add-event?]
  (let [with-updated-comment (update-comment-reaction db interaction-data add-event?)]
    (or with-updated-comment (update-entry-reaction db interaction-data add-event?))))

(defmethod dispatcher/action :ws-interaction/reaction-add
  [db [_ interaction-data]]
  (update-reaction db interaction-data true))

(defmethod dispatcher/action :ws-interaction/reaction-delete
  [db [_ interaction-data]]
  (update-reaction db interaction-data false))