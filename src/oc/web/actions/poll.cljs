(ns oc.web.actions.poll
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.utils.poll :as pu]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.actions.activity :as activity-actions]))

;; Polls

(defn add-poll [dispatch-key poll-id]
  (timbre/info "Adding poll with id" poll-id)
  (let [cur-user (dis/current-user-data)
        polls-key (if (coll? dispatch-key)
                    (vec (conj dispatch-key :polls))
                    [dispatch-key :polls])
        poll-data (pu/poll-data cur-user poll-id)]
    (dis/dispatch! [:update polls-key #(vec (conj % poll-data))])))

(defn remove-poll [dispatch-key poll-data]
  (timbre/info "Remove poll" dispatch-key (:poll-uuid poll-data))
  (dis/dispatch! [:update (vec (conj dispatch-key :polls)) (fn [polls] (filterv #(not= (:poll-uuid %) (:poll-uuid poll-data)) polls))])
  (when-let [poll-element (pu/get-poll-portal-element (:poll-uuid poll-data))]
    (.removeChild (.-parentElement poll-element) poll-element)))

(defn remove-poll-for-max-retry [dispatch-key poll-data]
  (timbre/info "Remove poll for max retry" dispatch-key (:poll-uuid poll-data))
  (remove-poll dispatch-key poll-data))

(defn show-preview [poll-key]
  (dis/dispatch! [:update poll-key #(assoc % :preview true)]))

(defn hide-preview [poll-key]
  (dis/dispatch! [:update poll-key #(dissoc % :preview)]))

(defn update-question [poll-key poll-data question]
  (dis/dispatch! [:input (vec (conj poll-key :question)) question])
  (utils/after 0 #(pu/set-poll-element-question! (:poll-uuid poll-data) question)))

;; Replies

(defn add-reply [poll-key & [reply-body]]
  (dis/dispatch! [:update poll-key #(merge % {:replies (vec (conj (:replies %) (pu/poll-reply (dis/current-user-data) reply-body)))
                                              :updated-at (utils/as-of-now)})]))

(defn update-reply [poll-key idx body]
  (dis/dispatch! [:input (vec (concat poll-key [:replies idx :body])) body]))

(defn delete-reply [poll-key poll-reply-id]
  (dis/dispatch! [:update poll-key #(merge % {:replies (filterv (fn[r] (not= (:reply-id r) poll-reply-id)) (:replies %))
                                              :updated-at (utils/as-of-now)})]))

(defn add-new-reply [poll-data poll-key reply-body]
  (timbre/info "Adding new reply to" poll-key "body:" reply-body)
  (let [add-reply-link (utils/link-for (:links poll-data) "reply" "POST")]
    (add-reply poll-key reply-body)
    (api/poll-add-reply add-reply-link reply-body (fn [{:keys [status body success]}]
     (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil)))))

(defn delete-existing-reply [poll-data poll-key poll-reply-id]
  (timbre/info "Deleting existing reply from" poll-key "reply:" poll-reply-id)
  (let [reply-data (some #(when (= (:reply-id %) poll-reply-id) %) (:replies poll-data))
        delete-reply-link (utils/link-for (:links reply-data) "delete" "DELETE")]
    (delete-reply poll-key poll-reply-id)
    (api/poll-delete-reply delete-reply-link (fn [{:keys [status body success]}]
     (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil)))))

;; Vote/unvote

(defn- update-reply-vote [user-id voting-reply-id add? reply]
  (let [updated-votes (fn [r add?]
                        (if add?
                          (vec (conj (set (:votes r)) user-id))
                          (filterv #(not= % user-id) (:votes r))))]
    (cond
      (= (:reply-id reply) voting-reply-id)
      (let [next-votes (updated-votes reply add?)]
        (merge reply
          {:votes next-votes
           :votes-count (count next-votes)}))
      (some #(when (= % user-id) %) (:votes reply))
      (let [next-votes (updated-votes reply false)]
        (merge reply
          {:votes next-votes
           :votes-count (count next-votes)}))
      :else
      reply)))

(defn vote-reply [poll-data poll-key reply-id]
  (timbre/info "Voting reply" reply-id)
  (let [reply (some #(when (= (:reply-id %) reply-id) %) (:replies poll-data))]
    (when-let [vote-link (utils/link-for (:links reply) "vote")]
      (let [user-id (:user-id (dis/current-user-data))
            updated-replies (mapv (partial update-reply-vote user-id reply-id true) (:replies poll-data))
            total-count (reduce + (map :votes-count updated-replies))]
        (dis/dispatch! [:update poll-key #(merge % {:total-votes-count total-count
                                                    :replies updated-replies})]))
      (api/poll-vote vote-link (fn [{:keys [status body success]}]
       (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil))))))

(defn unvote-reply [poll-data poll-key reply-id]
  (timbre/info "Unvoting reply" reply-id)
  (let [reply (some #(when (= (:reply-id %) reply-id) %) (:replies poll-data))]
    (when-let [unvote-link (utils/link-for (:links reply) "unvote")]
      (let [user-id (:user-id (dis/current-user-data))
            updated-replies (mapv (partial update-reply-vote user-id reply-id false) (:replies poll-data))
            total-count (reduce + (map :votes-count updated-replies))]
        (dis/dispatch! [:update poll-key #(merge % {:total-votes-count total-count
                                                    :replies updated-replies})]))
      (api/poll-vote unvote-link (fn [{:keys [status body success]}]
       (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil))))))