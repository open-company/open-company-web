(ns oc.web.stores.user
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.router :as router]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.utils :as utils]))

(defonce show-login-overlay-key :show-login-overlay)
(defonce show-login-overlay? show-login-overlay-key)

;; Signup keys
(defonce signup-with-email :signup-with-email)

(defn get-show-login-overlay []
  (get-in @dispatcher/app-state [show-login-overlay-key]))

;; JWT handling

;; Store JWT in App DB so it can be easily accessed in actions etc.
(defmethod dispatcher/action :jwt
  [db [_ jwt-data]]
  (timbre/debug jwt-data)
  (let [next-db (if (cook/get-cookie :show-login-overlay)
                  (assoc db show-login-overlay-key (keyword (cook/get-cookie :show-login-overlay)))
                  db)]
    (when (and (cook/get-cookie :show-login-overlay)
               (not= (cook/get-cookie :show-login-overlay) "collect-name-password")
               (not= (cook/get-cookie :show-login-overlay) "collect-password"))
      (cook/remove-cookie! :show-login-overlay))
    (assoc next-db :jwt (j/get-contents))))


;; Login actions

;; Store in application state whether to display the login overlay
(defmethod dispatcher/action :login-overlay-show
 [db [_ show-login-overlay]]
 (cond
    (= show-login-overlay :login-with-email)
    (-> db
      (assoc show-login-overlay-key show-login-overlay)
      (assoc :login-with-email {:email "" :pswd ""})
      (dissoc :login-with-email-error))
    (= show-login-overlay :signup-with-email)
    (-> db
      (assoc show-login-overlay-key show-login-overlay)
      (assoc :signup-with-email {:firstname "" :lastname "" :email "" :pswd ""})
      (dissoc :signup-with-email-error))
    :else
    (assoc db show-login-overlay-key show-login-overlay)))

(defn- dissoc-auth
  [db]
  (dissoc db :latest-auth-settings :latest-entry-point))

(defmethod dispatcher/action :login-with-email
  [db [_]]
  (-> db
      (dissoc :login-with-email-error)
      (dissoc-auth)))

(defmethod dispatcher/action :login-with-slack
  [db [_]]
  (dissoc-auth db))

(defmethod dispatcher/action :login-with-email/failed
  [db [_ error]]
  (assoc db :login-with-email-error error))

(defmethod dispatcher/action :login-with-email/success
  [db [_]]
  (dissoc db show-login-overlay-key))

;; Auth actions

(defmethod dispatcher/action :auth-with-token
  [db [ _ token-type]]
  (-> db
    (assoc :auth-with-token-type token-type)
    (dissoc :latest-auth-settings :latest-entry-point)))

(defmethod dispatcher/action :auth-with-token/failed
  [db [_ error]]
  (if (= (:auth-with-token-type db) :password-reset)
    (assoc db :collect-pswd-error error)
    (assoc db :email-verification-error error)))

(defmethod dispatcher/action :auth-with-token/success
  [db [_ jwt]]
  (assoc db :email-verification-success true))

;; Signup actions

(defmethod dispatcher/action :signup-with-email
  [db [_]]
  (dissoc db :signup-with-email-error :latest-auth-settings :latest-entry-point))

(defmethod dispatcher/action :signup-with-email/failed
  [db [_ status]]
  (assoc-in db [:signup-with-email :error] status))

(defmethod dispatcher/action :signup-with-email/success
  [db [_]]
  (dissoc db :signup-with-email-error))

;; Logout action

(defmethod dispatcher/action :logout
  [db _]
  (dissoc db :jwt :latest-entry-point :latest-auth-settings))