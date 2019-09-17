(ns oc.web.lib.chat
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [taoensso.timbre :as timbre]))

(defn identify []
  (when (jwt/get-key :email)
    (timbre/info "Identify user to Intercom")
    (utils/after 1 #(.Intercom js/window "update"
                        (clj->js {:user_id (jwt/user-id)
                                  :name (jwt/get-key :name)
                                  :email (jwt/get-key :email)})))))