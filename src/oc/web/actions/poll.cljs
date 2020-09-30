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
        poll-data (pu/poll-data cur-user poll-id)
        poll-key (if (coll? dispatch-key)
                   (vec (concat dispatch-key [:polls (keyword poll-id)]))
                   [dispatch-key :polls (keyword poll-id)])]
    (dis/dispatch! [:input poll-key poll-data])))

(defn remove-poll [dispatch-key poll-data]
  (timbre/info "Remove poll" dispatch-key (:poll-uuid poll-data))
  (let [poll-key (keyword (:poll-uuid poll-data))
        poll-dispatch-key (conj dispatch-key :polls)]
    (dis/dispatch! [:update poll-dispatch-key #(dissoc % poll-key)])
    (when-let [poll-element (pu/get-poll-portal-element (:poll-uuid poll-data))]
      (.removeChild (.-parentElement poll-element) poll-element))))

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
  (let [new-reply-data (pu/poll-reply (dis/current-user-data) reply-body)]
    (dis/dispatch! [:update poll-key #(merge % {:replies (assoc (:replies %) (keyword (:reply-id new-reply-data)) new-reply-data)
                                                :updated-at (utils/as-of-now)})])))

(defn update-reply [poll-key reply-id body]
  (dis/dispatch! [:input (vec (concat poll-key [:replies (keyword reply-id) :body])) body]))

(defn delete-reply [poll-key poll-reply-id]
  (dis/dispatch! [:update poll-key #(merge % {:replies (dissoc (:replies %) (keyword poll-reply-id))
                                              :updated-at (utils/as-of-now)})]))

(defn add-new-reply [poll-data poll-key reply-body]
  (timbre/info "Adding new reply to" poll-key "body:" reply-body)
  (let [add-reply-link (utils/link-for (:links poll-data) "reply" "POST")]
    (add-reply poll-key reply-body)
    (api/poll-add-reply add-reply-link reply-body (fn [{:keys [status body success]}]
     (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil)))))

(defn delete-existing-reply [poll-data poll-key poll-reply-id]
  (timbre/info "Deleting existing reply from" poll-key "reply:" poll-reply-id)
  (let [reply-data (-> poll-data :replies (keyword poll-reply-id))
        delete-reply-link (utils/link-for (:links reply-data) "delete" "DELETE")]
    (delete-reply poll-key poll-reply-id)
    (api/poll-delete-reply delete-reply-link (fn [{:keys [status body success]}]
     (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil)))))

;; Vote/unvote

(defn- update-reply-vote [user-id reply add?]
  (if add?
    (vec (conj (set (:votes reply)) user-id))
    (filterv #(not= % user-id) (:votes reply))))

(defn- update-vote-reply [user-id replies reply-id]
  (let [reps-coll (->> replies
                   vals
                   (mapv #(assoc % :votes
                           (update-reply-vote user-id % (= (:reply-id %) reply-id)))))]
    (zipmap (mapv :reply-id reps-coll) reps-coll)))

(defn vote-reply [poll-data poll-key reply-id]
  (timbre/info "Voting reply" reply-id)
  (let [reply (get-in poll-data [:replies (keyword reply-id)])]
    (when-let [vote-link (utils/link-for (:links reply) "vote")]
      (let [user-id (:user-id (dis/current-user-data))]
        (dis/dispatch! [:update (vec (conj poll-key :replies)) #(update-vote-reply user-id % reply-id)]))
      (api/poll-vote vote-link (fn [{:keys [status body success]}]
       (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil))))))

(defn unvote-reply [poll-data poll-key reply-id]
  (timbre/info "Unvoting reply" reply-id)
  (let [reply (get-in poll-data [:replies (keyword reply-id)])]
    (when-let [unvote-link (utils/link-for (:links reply) "unvote")]
      (let [user-id (:user-id (dis/current-user-data))]
        (dis/dispatch! [:update (concat poll-key [:replies (keyword reply-id) :votes]) #(update-reply-vote user-id % false)]))
      (api/poll-vote unvote-link (fn [{:keys [status body success]}]
       (activity-actions/activity-get-finish status (if success (json->cljs body) {}) nil))))))
