(ns oc.web.actions.comment
  (:require [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]))

(defn add-comment-focus [activity-data]
  (dis/dispatch! [:add-comment-focus activity-data]))

(defn add-comment-blur []
  (dis/dispatch! [:add-comment-focus nil]))

(defn get-comments-finished [activity-data {:keys [status success body]}]
  (dis/dispatch! [:comments-get/finish {:success success
                                        :error (when-not success body)
                                        :body (when (seq body) (json->cljs body))
                                        :activity-uuid (:uuid activity-data)}]))

(defn add-comment [activity-data comment-body]
  (add-comment-blur)
  ;; Add the comment to the app-state to show it immediately
  (dis/dispatch! [:comment-add activity-data comment-body])
  (api/add-comment activity-data comment-body
    ;; Once the comment api request is finished refresh all the comments, no matter
    ;; if it worked or not
    (fn [{:keys [status success body]}]
      (api/get-comments activity-data
       #(get-comments-finished activity-data %))
      (dis/dispatch! [:comment-add/finish {:success success
                                           :error (when-not success body)
                                           :body (when (seq body) (json->cljs body))
                                           :activity-data activity-data}]))))

(defn get-comments [activity-data]
  (dis/dispatch! [:comments-get activity-data])
  (api/get-comments activity-data
   #(get-comments-finished activity-data %)))

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
  (dis/dispatch! [:comment-delete (:uuid activity-data) comment-data])
  (api/delete-comment (:uuid activity-data) comment-data
    (fn [{:keys [status success body]}]
      (api/get-comments activity-data
       #(get-comments-finished activity-data %)))))

(defn comment-reaction-toggle
  [activity-data comment-data reaction-data reacting?]
  (dis/dispatch! [:comment-reaction-toggle])
  (api/toggle-reaction reaction-data reacting?
    (fn [{:keys [status success body]}]
      (get-comments activity-data))))

(defn save-comment
  [comment-data new-body]
  (api/save-comment comment-data new-body)
  (dis/dispatch! [:comment-save comment-data new-body]))

(defn ws-comment-update
  [interaction-data]
  (dis/dispatch! [:ws-interaction/comment-update interaction-data]))