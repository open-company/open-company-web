(ns oc.web.lib.chat
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [taoensso.timbre :as timbre]))

(defn identify []
  (when (jwt/get-key :email)
    (timbre/info "Identify user to Intercom")
    (let [user (dispatcher/current-user-data)
          org (dispatcher/org-data)]
      (utils/after 1 #(.Intercom js/window "update"
                          (clj->js {; Intercom standard
                                    :user_id (:user-id user)
                                    :created_at (:created-at user)
                                    :name (jwt/get-key :name)
                                    :email (:email user)
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