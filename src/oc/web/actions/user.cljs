(ns oc.web.actions.user
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local_settings :as ls]))

;; Logout

(defn logout []
  (cook/remove-cookie! :jwt)
  (router/redirect! "/")
  (dis/dispatch! [:logout]))

(defn logout-click [e]
  (utils/event-stop e)
  (utils/after 100 #(dis/dispatch! [:mobile-menu-toggle]))
  (logout))

;; JWT

(defn update-jwt-cookie [jwt]
  (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))

(defn dispatch-jwt []
  (when (and (cook/get-cookie :show-login-overlay)
             (not= (cook/get-cookie :show-login-overlay) "collect-name-password")
             (not= (cook/get-cookie :show-login-overlay) "collect-password"))
    (cook/remove-cookie! :show-login-overlay))
  (utils/after 1 #(dis/dispatch! [:jwt (jwt/get-contents)])))

(defn update-jwt [jbody]
  (timbre/info jbody)
  (when jbody
    (update-jwt-cookie jbody)
    (dispatch-jwt)))

(defn jwt-refresh []
  (api/jwt-refresh update-jwt logout))

;; API Entry point
(defn entry-point-get-finished
  ([success body] (entry-point-get-finished success body #()))

  ([success body callback]
  (let [collection (:collection body)]
    (if success
      (let [orgs (:items collection)]
        (when-let [whats-new-link (utils/link-for (:links collection) "whats-new")]
          (api/get-whats-new whats-new-link))
        (callback orgs collection)
        (dis/dispatch! [:entry-point orgs collection]))
      (dis/dispatch! [:error-banner-show utils/generic-network-error 0])))))

(defn entry-point-get [org-slug]
  (api/web-app-version-check
    (fn [{:keys [success body status]}]
      (when (= status 404)
        (dis/dispatch! [:error-banner-show (str "You have an older version of the Carrot web app, "
                                                "please refresh your browser window!")]))))
  (api/get-entry-point
   (fn [success body]
     (entry-point-get-finished success body
       (fn [orgs collection]
         (if org-slug
           (do
             (if-let [org-data (first (filter #(= (:slug %) org-slug) orgs))]
               (api/get-org org-data)
               (router/redirect-404!)))
           (when (and (jwt/jwt) (utils/in? (:route @router/path) "login"))
             (router/nav! (oc-urls/org (:slug (first orgs)))))))))))

(defn slack-lander-check-team-redirect []
  (utils/after 100 #(api/get-entry-point
    (fn [success body]
      (entry-point-get-finished success body
        (fn [orgs collection]
          (if (zero? (count orgs))
            (router/nav! oc-urls/sign-up-team)
            (router/nav! (oc-urls/org (:slug (utils/get-default-org orgs)))))))))))

;; Login
(defn login-with-email-finish
  [user-email success body status]
  (if success
    (do
      (if (empty? body)
        (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" user-email)))
        (do
          (update-jwt-cookie body)
          (api/get-entry-point
           (fn [success body] (entry-point-get-finished success body
             (fn [orgs collection]
               (router/nav! (oc-urls/org (:slug (utils/get-default-org orgs))))))))))
      (dis/dispatch! [:login-with-email/success body]))
    (cond
     (= status 401)
     (dis/dispatch! [:login-with-email/failed 401])
     :else
     (dis/dispatch! [:login-with-email/failed 500]))))

(defn login-with-email [email pswd]
  (api/auth-with-email email pswd (partial login-with-email-finish email))
  (dis/dispatch! [:login-with-email]))

(defn login-with-slack [auth-url]
  (let [auth-url-with-redirect (utils/slack-link-with-state
                                 (:href auth-url)
                                 nil
                                 "open-company-auth" oc-urls/slack-lander-check)]
    (router/redirect! auth-url-with-redirect)
    (dis/dispatch! [:login-with-slack])))

(defn show-login [login-type]
  (dis/dispatch! [:login-overlay-show login-type]))

;; Auth

(defn bot-auth [org-data team-data user-data]
  (let [current (router/get-token)
        auth-link (utils/link-for (:links team-data) "bot")
        fixed-auth-url (utils/slack-link-with-state (:href auth-link) (:user-id user-data) (:id team-data)
                        (router/get-token))]
    (router/redirect! fixed-auth-url)))

(defn auth-with-token-failed [error]
  (dis/dispatch! [:auth-with-token/failed error]))

(defn auth-with-token-success [token-type jwt]
  (api/get-auth-settings
   (fn [auth-body]
     (api/get-entry-point
      (fn [success body]
        (entry-point-get-finished success body)
        (let [orgs (:items (:collection body))
              to-org (utils/get-default-org orgs)]
          (router/redirect! (if to-org (oc-urls/org (:slug to-org)) oc-urls/user-profile)))))))
  (when (= token-type :password-reset)
    (cook/set-cookie! :show-login-overlay "collect-password"))
  (dis/dispatch! [:auth-with-token/success jwt]))

(defn auth-with-token-callback
  [token-type success body status]
  (if success
    (do
      (update-jwt body)
      (auth-with-token-success token-type body))
    (cond
      (= status 401)
      (auth-with-token-failed 401)
      :else
      (auth-with-token-failed 500))))

(defn auth-with-token [token-type]
  (api/auth-with-token (:token (:query-params @router/path)) (partial auth-with-token-callback token-type))
  (dis/dispatch! [:auth-with-token token-type]))

;; Signup

(defn signup-with-email-failed [status]
  (dis/dispatch! [:signup-with-email/failed status]))

(defn signup-with-email-success
  [user-email status jwt]
  (cond
    (= status 204) ;; Email wall since it's a valid signup w/ non verified email address
    (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" user-email)))
    (= status 200) ;; Valid login, not signup, redirect to home
    (if (or
          (and (empty? (:first-name jwt)) (empty? (:last-name jwt)))
          (empty? (:avatar-url jwt)))
      (do
        (utils/after 200 #(router/nav! oc-urls/sign-up-profile))
        (api/get-entry-point entry-point-get-finished))
      (do
        (api/get-entry-point
         (fn [success body]
           (entry-point-get-finished success body
             (fn [orgs collection]
               (if (zero? (count orgs))
                 (router/nav! oc-urls/sign-up-team)
                 (router/nav! (oc-urls/org (:slug (utils/get-default-org orgs)))))))))))
    :else ;; Valid signup let's collect user data
    (do
      (update-jwt-cookie jwt)
      (cook/set-cookie!
       (router/show-nux-cookie (jwt/user-id))
       (:new-user router/nux-cookie-values)
       (* 60 60 24 7))
      (utils/after 200 #(router/nav! oc-urls/sign-up-profile))
      (api/get-entry-point entry-point-get-finished)
      (dis/dispatch! [:signup-with-email/success]))))

(defn signup-with-email-callback
  [user-email success body status]
  (if success
    (signup-with-email-success user-email status body)
    (signup-with-email-failed status)))

(defn signup-with-email [signup-data]
  (api/signup-with-email
   (or (:firstname signup-data) "")
   (or (:lastname signup-data) "")
   (:email signup-data)
   (:pswd signup-data)
   (partial signup-with-email-callback (:email signup-data)))
  (dis/dispatch! [:signup-with-email]))

(defn signup-with-email-reset-errors []
  (dis/dispatch! [:input [:signup-with-email] {}]))

;;Invitation
(defn invitation-confirmed [status]
  (when (= status 201)
    (api/get-entry-point entry-point-get-finished)
    (api/get-auth-settings))
  (dis/dispatch! [:invitation-confirmed (= status 201)]))

;; Debug

(defn force-jwt-refresh []
  (when (jwt/jwt) (jwt-refresh)))

(set! (.-OCWebForceRefreshToken js/window) force-jwt-refresh)