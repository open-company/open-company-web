(ns oc.web.actions.comment
  (:require [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions :as actions]
            [oc.web.lib.json :refer (json->cljs)]))

(defn add-comment-focus []
  (dis/dispatch! [:add-comment-focus true]))

(defn add-comment-blur []
  (dis/dispatch! [:add-comment-focus false]))

(defn add-comment [activity-data comment-body]
  ;; Add the comment to the app-state to show it immediately
  (dis/dispatch! [:comment-add activity-data comment-body])
  (api/add-comment activity-data comment-body
    ;; Once the comment api request is finished refresh all the comments, no matter
    ;; if it worked or not
    (fn [{:keys [status success body]}]
      (api/get-comments activity-data)
      (dis/dispatch! [:comment-add/finish {:success success
                                           :error (when-not success body)
                                           :body (when (seq body) (json->cljs body))
                                           :activity-data activity-data}]))))

(defn get-comments [activity-data]
  (dis/dispatch! [:comments-get activity-data])
  (api/get-comments activity-data
    (fn [{:keys [status success body]}]
      (dis/dispatch! [:comments-get/finish {:success success
                                            :error (when-not success body)
                                            :body (when (seq body) (json->cljs body))
                                            :activity-uuid (:uuid activity-data)}]))))