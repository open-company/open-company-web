(ns oc.web.actions.reaction
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.actions.activity :as activity-actions]))

(defn react-from-picker [activity-data emoji]
  (dis/dispatch! [:handle-reaction-to-entry activity-data
   {:reaction emoji :count 1 :reacted true :links [] :authors []}
   (dis/activity-key (router/current-org-slug) (:uuid activity-data))])
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
  (let [activity-key (dis/activity-key (router/current-org-slug) (:uuid activity-data))
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

(defn is-activity-reaction? [org-slug board-slug interaction-data]
  (let [activity-uuid (router/current-activity-id)
        item-uuid (:resource-uuid interaction-data)
        reaction-data (:interaction interaction-data)
        comments-key (dis/activity-comments-key org-slug activity-uuid)
        comments-data (get-in @dis/app-state comments-key)
        comment-idx (utils/index-of comments-data #(= item-uuid (:uuid %)))]
    (nil? comment-idx)))

(defn refresh-if-needed [org-slug board-slug interaction-data]
  (let [; Get the current router data
        activity-uuid (:resource-uuid interaction-data)
        ; Entry data
        entry-data (dis/activity-data org-slug activity-uuid)
        reaction-data (:interaction interaction-data)
        is-current-user (= (jwt/get-key :user-id) (:user-id (:author reaction-data)))]
    ;; Refresh entry if necessary
    (when (and entry-data
             (seq (:reactions entry-data))
             is-current-user)
      (activity-actions/get-entry entry-data))))

(defn ws-interaction-reaction-add [interaction-data]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)]
    (when (is-activity-reaction? org-slug board-slug interaction-data)
      (refresh-if-needed org-slug board-slug interaction-data)))
  (dis/dispatch! [:ws-interaction/reaction-add interaction-data]))

(defn ws-interaction-reaction-delete [interaction-data]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)]
    (when (is-activity-reaction? org-slug board-slug interaction-data)
      (refresh-if-needed org-slug board-slug interaction-data)))
  (dis/dispatch! [:ws-interaction/reaction-delete interaction-data]))

(defn subscribe []
  (ws-ic/subscribe :interaction-reaction/add
                   #(ws-interaction-reaction-add (:data %)))
  (ws-ic/subscribe :interaction-reaction/delete
                   #(ws-interaction-reaction-delete (:data %))))