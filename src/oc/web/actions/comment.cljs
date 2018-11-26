(ns oc.web.actions.comment
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.actions.activity :as activity-actions]))

(defn add-comment-focus [activity-uuid]
  (dis/dispatch! [:add-comment-focus activity-uuid]))

(defn add-comment-blur []
  (dis/dispatch! [:add-comment-focus nil]))

(defn get-comments-finished
  [comments-key activity-data {:keys [status success body]}]
  (dis/dispatch! [:comments-get/finish {:success success
                                        :error (when-not success body)
                                        :comments-key comments-key
                                        :body (when (seq body) (json->cljs body))
                                        :activity-uuid (:uuid activity-data)}]))

(defn add-comment [activity-data comment-body]
  (add-comment-blur)
  ;; Send WRT read on comment add
  (activity-actions/send-item-read (:uuid activity-data))
  (let [org-slug (router/current-org-slug)
        comments-key (dis/activity-comments-key org-slug (:uuid activity-data))
        add-comment-link (utils/link-for (:links activity-data) "create" "POST")]
    ;; Add the comment to the app-state to show it immediately
    (dis/dispatch! [:comment-add
                    activity-data
                    comment-body
                    comments-key])
    (api/add-comment add-comment-link comment-body
      ;; Once the comment api request is finished refresh all the comments, no matter
      ;; if it worked or not
      (fn [{:keys [status success body]}]
        (let [comments-link (utils/link-for (:links activity-data) "comments")]
          (api/get-comments comments-link #(get-comments-finished comments-key activity-data %)))
        (dis/dispatch! [:comment-add/finish {:success success
                                             :error (when-not success body)
                                             :body (when (seq body) (json->cljs body))
                                             :activity-data activity-data}])))))

(defn get-comments [activity-data]
  (let [comments-key (dis/activity-comments-key (router/current-org-slug) (:uuid activity-data))
        comments-link (utils/link-for (:links activity-data) "comments")]
    (dis/dispatch! [:comments-get
                    comments-key
                    activity-data])
    (api/get-comments comments-link #(get-comments-finished comments-key activity-data %))))

(defn get-comments-if-needed [activity-data all-comments-data]
  (let [comments-link (utils/link-for (:links activity-data) "comments")
        activity-uuid (:uuid activity-data)
        comments-data (get all-comments-data activity-uuid)
        should-load-comments? (and ;; there are comments to load,
                                   (pos? (:count comments-link))
                                   ;; they are not already loading,
                                   (not (:loading comments-data))
                                   ;; and they are not loaded already
                                   (not (contains? comments-data :sorted-comments)))]
    ;; Load the whole list of comments if..
    (when should-load-comments?
      (get-comments activity-data))))

(defn delete-comment [activity-data comment-data]
  ;; Send WRT read on comment delete
  (activity-actions/send-item-read (:uuid activity-data))
  (let [comments-key (dis/activity-comments-key
                      (router/current-org-slug)
                      (:uuid activity-data))
        delte-comment-link (utils/link-for (:links comment-data) "delete")]
    (dis/dispatch! [:comment-delete
                    (:uuid activity-data)
                    comment-data
                    comments-key])
    (api/delete-comment delte-comment-link
      (fn [{:keys [status success body]}]
        (let [comments-link (utils/link-for (:links activity-data) "comments")]
          (api/get-comments comments-link
           #(get-comments-finished comments-key activity-data %)))))))

(defn comment-reaction-toggle [activity-data comment-data reaction-data reacting?]
  (activity-actions/send-item-read (:uuid activity-data))
  (let [comments-key (dis/activity-comments-key (router/current-org-slug)
                      (:uuid activity-data))
        link-method (if reacting? "PUT" "DELETE")
        reaction-link (utils/link-for (:links reaction-data) "react" link-method)]
    (dis/dispatch! [:comment-reaction-toggle
                    comments-key
                    activity-data
                    comment-data
                    reaction-data
                    reacting?])
    (api/toggle-reaction reaction-link
      (fn [{:keys [status success body]}]
        (get-comments activity-data)))))

(defn save-comment [activity-uuid comment-data new-body]
  ;; Send WRT on comment update
  (activity-actions/send-item-read activity-uuid)
  (let [comments-key (dis/activity-comments-key
                      (router/current-org-slug)
                      activity-uuid)
        patch-comment-link (utils/link-for (:links comment-data) "partial-update")]
    (api/patch-comment patch-comment-link new-body nil)
    (dis/dispatch! [:comment-save
                    comments-key
                    activity-uuid
                    comment-data
                    new-body])))

(defn ws-comment-update
  [interaction-data]
  (let [comments-key (dis/activity-comments-key
                      (router/current-org-slug)
                      (:resource-uuid interaction-data))]
    (dis/dispatch! [:ws-interaction/comment-update
                    comments-key
                    interaction-data])))

(defn ws-comment-delete [comment-data]
  (let [comments-key (dis/activity-comments-key
                      (router/current-org-slug)
                      (:resource-uuid comment-data))]
    (dis/dispatch! [:ws-interaction/comment-delete comments-key comment-data])))

(defn ws-comment-add [interaction-data]
  (let [org-slug   (router/current-org-slug)
        board-slug (router/current-board-slug)
        activity-uuid (:resource-uuid interaction-data)
        entry-data (dis/activity-data org-slug activity-uuid)
        comments-key (dis/activity-comments-key org-slug activity-uuid)]
    (when entry-data
      ;; Refresh the entry data to get the new links to interact with
      (activity-actions/get-entry entry-data))
    (dis/dispatch! [:ws-interaction/comment-add
                    (dis/current-board-key)
                    entry-data
                    interaction-data
                    comments-key])))

(defn subscribe []
  (ws-ic/subscribe :interaction-comment/add
                   #(ws-comment-add (:data %)))
  (ws-ic/subscribe :interaction-comment/update
                   #(ws-comment-update (:data %)))
  (ws-ic/subscribe :interaction-comment/delete
                   #(ws-comment-delete (:data %))))