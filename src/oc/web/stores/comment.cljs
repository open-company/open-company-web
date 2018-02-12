(ns oc.web.stores.comment
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :add-comment-focus
  [db [_ focus?]]
  (assoc db :add-comment-focus focus?))

(defmethod dispatcher/action :comment-add
  [db [_ activity-data comment-body]]
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        comments-key (dispatcher/activity-comments-key org-slug board-slug (:uuid activity-data))
        comments-data (get-in db comments-key)
        new-comments-data (conj comments-data {:body comment-body
                                               :created-at (utils/as-of-now)
                                               :author {:name (jwt/get-key :name)
                                                        :avatar-url (jwt/get-key :avatar-url)
                                                        :user-id (jwt/get-key :user-id)}})]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-data]}]]
  (assoc db :comment-add-finish true))