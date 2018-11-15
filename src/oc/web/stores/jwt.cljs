(ns oc.web.stores.jwt
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.cookies :as cook]
            [oc.web.dispatcher :as dispatcher]))

;; JWT handling

;; Store JWT in App DB so it can be easily accessed in actions etc.
(defmethod dispatcher/action :jwt
  [db [_]]
  (let [jwt-data (j/get-contents)
        next-db (if (cook/get-cookie :show-login-overlay)
                  (assoc db dispatcher/show-login-overlay-key (keyword (cook/get-cookie :show-login-overlay)))
                  db)]
    (timbre/debug jwt-data)
    (assoc next-db :jwt jwt-data)))