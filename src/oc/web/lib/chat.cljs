(ns oc.web.lib.chat
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [taoensso.timbre :as timbre]))

(defn identify []
  (when (jwt/get-key :email)
    (timbre/info "Identify user to Intercom")
    (utils/after 100 #(let [user (dispatcher/current-user-data)
                            user-id (:user-id user)
                            org (dispatcher/org-data)]
                       (.Intercom js/window "update"
                         (clj->js {; Intercom standard
                                   :user_id user-id
                                   :created_at (:created-at user)
                                   :name (jwt/get-key :name)
                                   :email (:email user)
                                   :org_author (= (-> org :author :user-id) user-id)
                                   :avatar {
                                     :type :avatar
                                     :image_url (:avatar-url (:avatar-url user))
                                   }
                                   :company {
                                     :id (:slug org)
                                     :name (:name org)
                                     :created_at (:created-at org)
                                    }
                                   ; custom
                                   :timezone (:timezone user)}))))))