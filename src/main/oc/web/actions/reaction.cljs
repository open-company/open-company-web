(ns oc.web.actions.reaction
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.actions.activity :as activity-actions]))

(defn react-from-picker [activity-data emoji]
  (dis/dispatch! [:handle-reaction-to-entry activity-data
   {:reaction emoji :count 1 :reacted true :links [] :authors []}
   (dis/activity-key (dis/current-org-slug) (:uuid activity-data))])
  ;; Some times emoji.native coming from EmojiMart is null
  ;; so we need to avoid posting empty emojis
  (when (and emoji
             (utils/link-for (:links activity-data) "react"))
    (let [react-link (utils/link-for (:links activity-data) "react")]
      (api/react-from-picker react-link emoji
        (fn [{:keys [status success body]}]
          ;; Refresh the full entry to make sure it's up to date
          (activity-actions/get-entry activity-data))))))

(defn reaction-toggle
  [activity-data reaction-data reacting?]
  (let [activity-key (dis/activity-key (dis/current-org-slug) (:uuid activity-data))
        link-method (if reacting? "PUT" "DELETE")
        reaction-link (utils/link-for (:links reaction-data) "react" link-method)
        fixed-count (if reacting?
                      (inc (or (:count reaction-data) 0))
                      (dec (or (:count reaction-data) 0)))
        fixed-reaction-data (-> reaction-data
                             (assoc :count fixed-count)
                             (assoc :reacted reacting?))]
    (dis/dispatch! [:handle-reaction-to-entry activity-data fixed-reaction-data activity-key])
    (api/toggle-reaction reaction-link
      (fn [{:keys [status success body]}]
        ;; Refresh the full entry to make sure it's up to date
        (activity-actions/get-entry activity-data)))))

(defn reaction-resource [org-slug item-uuid]
  (let [entry-data (dis/activity-data org-slug item-uuid)
        comment-data (dis/comment-data org-slug item-uuid)]
    (or entry-data comment-data)))

(defn refresh-entry-if-needed [org-slug board-slug resource-uuid entry-data]
  ;; Refresh entry if necessary
  (if entry-data
    (activity-actions/get-entry entry-data)
    (cmail-actions/get-entry-with-uuid board-slug resource-uuid)))

(defn refresh-comments-if-needed [org-slug board-slug comment-data]
  (let [entry-uuid (:resource-uuid comment-data)
        ; Entry data
        entry-data (dis/activity-data org-slug entry-uuid)]
    ;; Refresh entry if necessary
    (if entry-data
      (activity-utils/get-comments entry-data)
      (cmail-actions/get-entry-with-uuid board-slug entry-uuid
       (fn [success status]
         (when success
           (activity-utils/get-comments (dis/activity-data org-slug entry-uuid))))))))

(defn refresh-resource [org-slug board-slug item-uuid]
  (let [resource-data (reaction-resource org-slug item-uuid)]
    (if (activity-utils/comment? resource-data)
      (refresh-comments-if-needed org-slug board-slug resource-data)
      (refresh-entry-if-needed org-slug board-slug item-uuid resource-data))))

(defn ws-interaction-reaction-add [interaction-data]
  (let [org-slug (dis/current-org-slug)
        board-uuid (:container-id interaction-data)]
    (dis/dispatch! [:ws-interaction/reaction-add interaction-data])
    (refresh-resource org-slug board-uuid (:resource-uuid interaction-data))))

(defn ws-interaction-reaction-delete [interaction-data]
  (let [org-slug (dis/current-org-slug)
        board-uuid (:container-id interaction-data)]
    (dis/dispatch! [:ws-interaction/reaction-delete interaction-data])
    (refresh-resource org-slug board-uuid (:resource-uuid interaction-data))))

(defn subscribe []
  (ws-ic/subscribe :interaction-reaction/add
                   #(ws-interaction-reaction-add (:data %)))
  (ws-ic/subscribe :interaction-reaction/delete
                   #(ws-interaction-reaction-delete (:data %))))