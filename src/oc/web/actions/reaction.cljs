(ns oc.web.actions.reaction
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.ws-interaction-client :as ws-ic]
            [oc.web.actions.activity :as activity-actions]))

(defn react-from-picker [activity-data emoji]
  (dis/dispatch! [:handle-reaction-to-entry activity-data
   {:reaction emoji :count 1 :reacted true :links [] :authors []}])
  ;; Some times emoji.native coming from EmojiMart is null
  ;; so we need to avoid posting empty emojis
  (when (and emoji
             (utils/link-for (:links activity-data) "react"))
    (activity-actions/send-item-read (:uuid activity-data))
    (let [react-link (utils/link-for (:links activity-data) "react")]
      (api/react-from-picker react-link emoji
        (fn [{:keys [status success body]}]
          ;; Refresh the full entry after the reaction finished
          ;; in the meantime update the local state with the result.
          (activity-actions/get-entry activity-data)
          (dis/dispatch!
           [:react-from-picker/finish
            {:status status
             :activity-data activity-data
             :activity-key (dis/activity-key (router/current-org-slug) (:uuid activity-data))
             :reaction-data (if success (json->cljs body) {})}]))))))

(defn reaction-toggle
  [activity-data reaction-data reacting?]
  (activity-actions/send-item-read (:uuid activity-data))
  (let [activity-key (dis/activity-key (router/current-org-slug) (:uuid activity-data))
        link-method (if reacting? "PUT" "DELETE")
        reaction-link (utils/link-for (:links reaction-data) "react" link-method)]
    (dis/dispatch! [:handle-reaction-to-entry activity-data reaction-data activity-key])
    (api/toggle-reaction reaction-link
      (fn [{:keys [status success body]}]
        (dis/dispatch!
         [:activity-reaction-toggle/finish
          activity-data
          (:reaction reaction-data)
          (when success (json->cljs body))
          activity-key])))))

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
    (if (and entry-data (seq (:reactions entry-data)))
      (when is-current-user
        (activity-actions/get-entry entry-data))
      (router/nav! (oc-urls/board (router/current-org-slug) board-slug)))))

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