(ns oc.web.actions.user
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [defun.core :refer (defun defun-)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.user :as user-utils]
            [oc.web.ws.notify-client :as ws-nc]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.jwt :as jwt-actions]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.user-tags :as user-tags]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.utils.notification :as notif-utils]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notification-actions]
            ["moment-timezone" :as moment-timezone]))

(defn- orgs? []
  (contains? @dis/app-state dis/orgs-key))

(defn- auth-settings? []
  (contains? @dis/app-state (first dis/auth-settings-key)))

(defn- auth-settings-status? []
  (and (auth-settings?)
       (contains? (dis/auth-settings) :status)))

;; Invite box

(defn dismiss-invite-box

  ([] (dismiss-invite-box (jwt/user-id) false))

  ([user-id] (dismiss-invite-box user-id false))

  ([user-id remove-now?]
  (cook/remove-cookie! (router/show-invite-box-cookie user-id))
  (when remove-now?
    (dis/dispatch! [:input [dis/show-invite-box-key] false]))))

;;User walls

(defn- check-user-walls
  "Check if one of the following is present and redirect to the proper wall if needed:
  :password-required redirect to password collect
  :name-required redirect to first and last name collect

  Use the orgs value to determine if the user has already at least one org set"
  ([]
    ; Delay to let the last api request set the app-state data
    (when (jwt/jwt)
      (utils/after 100
        #(when (and (orgs?)
                    (auth-settings?)
                    (auth-settings-status?))
            (check-user-walls (dis/auth-settings) (dis/orgs-data))))))
  ([auth-settings orgs]
    (let [status-response (set (map keyword (:status auth-settings)))
          has-orgs (pos? (count orgs))]
      (cond
        (status-response :password-required)
        (router/nav! oc-urls/confirm-invitation-password)

        (status-response :name-required)
        (if has-orgs
          (router/nav! oc-urls/confirm-invitation-profile)
          (router/nav! oc-urls/sign-up-profile))

        :else
        (when-not has-orgs
          (router/nav! oc-urls/sign-up-profile))))))

;; API Entry point
(defn entry-point-get-finished
  ([success body] (entry-point-get-finished success body nil))

  ([success body callback]
  (let [collection (:collection body)]
    (if success
      (let [orgs (sort-by :name (:items collection))]
        (dis/dispatch! [:entry-point orgs collection])
        (check-user-walls)
        (when (fn? callback)
          (callback orgs collection)))
      (notification-actions/show-notification (assoc utils/network-error :expire 0))))))

(defn entry-point-get [org-slug & [force-refresh?]]
  (api/get-entry-point (dis/current-org-slug)
   (fn [success body]
     (entry-point-get-finished success body
       (fn [orgs collection]
         (if org-slug
           (if-let [org-data (first (filter #(or (= (:slug %) org-slug)
                                                 (= (:uuid %) org-slug)) orgs))]
             ;; We got the org we were looking for. Possibly redirect if the client
             ;; used org uuid in token.
             (if (= (:uuid org-data) org-slug)
               (router/rewrite-org-uuid-as-slug org-slug (:slug org-data))
               (org-actions/get-org org-data (not force-refresh?)))
             (if (dis/current-secure-activity-id)
               (activity-actions/secure-activity-get
                #(activity-utils/get-comments-if-needed (dis/secure-activity-data) (dis/comments-data)))
               (do
                 ;; avoid infinite loop of the Go to digest button
                 ;; by changing the value of the last visited slug
                 (if (pos? (count orgs))
                   ;; we got at least one org, redirect to it next time
                   (cook/set-cookie! (router/last-org-cookie) (:slug (first orgs)) cook/default-cookie-expire)
                   ;; no orgs present, remove the last org cookie to avoid infinite loops
                   (cook/remove-cookie! (router/last-org-cookie)))
                 (when-not (dis/current-secure-activity-id)
                   ;; 404: secure entry can't 404 here since the org response is included in the
                   ;; secure entry response and not in the entry point response
                   (routing-actions/maybe-404)))))
           ;; If user is on login page and he's logged in redirect to the org page
           (when (and (jwt/jwt)
                      (dis/in-route? :login)
                      (pos? (count orgs)))
             (router/nav! (oc-urls/org (:slug (first orgs)))))))))))

(defn save-login-redirect [& [url]]
  (let [url (or url (.. js/window -location -href))]
    (when url
      (cook/set-cookie! router/login-redirect-cookie url))))

(defn maybe-save-login-redirect []
  (let [url-pathname (.. js/window -location -pathname)
        is-login-route? (or (= url-pathname oc-urls/login-wall)
                            (= url-pathname oc-urls/login)
                            (= url-pathname oc-urls/native-login))]
    (cond
      (and is-login-route?
           (:login-redirect (:query-params @dis/app-state)))
      (save-login-redirect (:login-redirect (:query-params @dis/app-state)))
      (not is-login-route?)
      (save-login-redirect))))

(defn- newest-org [orgs]
  (first (sort-by :created-at orgs)))

(defn- get-default-org [orgs]
  (if-let [last-org-slug (cook/get-cookie (router/last-org-cookie))]
    (let [last-org (first (filter #(= (:slug %) last-org-slug) orgs))]
      (or
        ; Get the last accessed board from the saved cookie
        last-org
        ; Fallback to the newest board if the saved board was not found
        (newest-org orgs)))
    (newest-org orgs)))

(defn login-redirect []
  (let [redirect-url (cook/get-cookie router/login-redirect-cookie)
        orgs (dis/orgs-data)]
    (cook/remove-cookie! router/login-redirect-cookie)
    (if redirect-url
      (router/redirect! redirect-url)
      (router/nav!
       (if (zero? (count orgs))
         oc-urls/sign-up-profile
         (oc-urls/default-landing (:slug (get-default-org orgs))))))))

(defn lander-check-team-redirect []
  (utils/after 100 #(api/get-entry-point (dis/current-org-slug)
    (fn [success body]
      (entry-point-get-finished success body login-redirect)))))

;; Login
(defn login-with-email-finish
  [user-email success body status]
  (if success
    (do
      (if (empty? body)
        (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" user-email)))
        (do
          (jwt-actions/update-jwt body)
          (lander-check-team-redirect)))
      (dis/dispatch! [:login-with-email/success body]))
    (cond
     (= status 401)
     (dis/dispatch! [:login-with-email/failed 401])
     :else
     (dis/dispatch! [:login-with-email/failed 500]))))

(defn login-with-email [email pswd]
  (let [email-links (:links (dis/auth-settings))
        auth-link (utils/link-for email-links "authenticate" "GET" {:auth-source "email"})]
    (api/auth-with-email auth-link email pswd (partial login-with-email-finish email))
    (dis/dispatch! [:login-with-email])))

(defn login-with-slack [auth-url & [state-map]]
  (let [auth-url-with-redirect (user-utils/auth-link-with-state
                                (:href auth-url)
                                (merge {:team-id "open-company-auth"
                                        :redirect oc-urls/slack-lander-check}
                                       state-map))]
    (router/redirect! auth-url-with-redirect)
    (dis/dispatch! [:login-with-slack])))

(defn login-with-google [auth-url & [state-map]]
  (let [auth-url-with-redirect (user-utils/auth-link-with-state
                                (:href auth-url)
                                (or state-map {}))]
    (router/redirect! auth-url-with-redirect)
    (dis/dispatch! [:login-with-google])))

(defn refresh-slack-user []
  (let [refresh-link (utils/link-for (:links (dis/auth-settings)) "refresh")]
    (api/refresh-slack-user refresh-link
     (fn [status body success]
      (if success
        (jwt-actions/update-jwt body)
        (router/redirect! oc-urls/logout))))))

(defn show-login [login-type]
  (dis/dispatch! [:login-overlay-show login-type]))

;; User Timezone preset

(defn- patch-timezone-if-needed [user-map]
  (when-let* [_notz (clojure.string/blank? (:timezone user-map))
              user-profile-link (utils/link-for (:links user-map) "partial-update" "PATCH")
              guessed-timezone (.. moment-timezone -tz guess)]
    (api/patch-user user-profile-link {:timezone guessed-timezone}
     (fn [status body success]
       (when success
        (dis/dispatch! [:user-data (json->cljs body)]))))))

;; Get user

(defun get-user
  ([nil] nil)
  ([] (get-user (utils/link-for (:links (dis/auth-settings)) "user" "GET")))
  ([user-link :guard :href]
   (api/get-user user-link (fn [success data]
                             (let [user-map (when success (json->cljs data))]
                               (dis/dispatch! [:user-data user-map])
                               (utils/after 100 #(nux-actions/check-nux))
                               (patch-timezone-if-needed user-map))))))

;; Auth

(defn auth-settings-get
  "Entry point call for auth service."
  []
  (api/get-auth-settings (fn [body status]
    (if body
      (do
        ;; auth settings loaded
        (when-let [user-link (utils/link-for (:links body) "user" "GET")]
          (get-user user-link))
        (dis/dispatch! [:auth-settings body])
        (check-user-walls)
        ;; Start teams retrieve if we have a link
        (team-actions/teams-get))
      (when (= status 401)
        (dis/dispatch! [:auth-settings status]))))))

(defn auth-with-token-failed [error]
  (dis/dispatch! [:auth-with-token/failed error]))

;;Invitation
(defn invitation-confirmed [status body success]
 (when success
    (jwt-actions/update-jwt body)
    (when (<= 200 status 299)
      (nux-actions/new-user-registered "email")
      (api/get-entry-point (dis/current-org-slug) entry-point-get-finished)
      (auth-settings-get))
    ;; Go to password setup
    (utils/after 200 #(router/nav! oc-urls/confirm-invitation-password)))
  (dis/dispatch! [:invitation-confirmed success]))

(defn confirm-invitation [token]
  (let [auth-link (utils/link-for (:links (dis/auth-settings)) "authenticate" "GET"
                   {:auth-source "email"})]
    (api/confirm-invitation auth-link token invitation-confirmed)))

;; Token authentication
(defn auth-with-token-success [token-type jwt]
  (api/get-auth-settings
   (fn [auth-body]
     (api/get-entry-point (dis/current-org-slug)
      (fn [success body]
        (entry-point-get-finished success body)
        (let [orgs (:items (:collection body))
              to-org (get-default-org orgs)
              to-url (if to-org
                       (oc-urls/default-landing (:slug to-org))
                       oc-urls/sign-up-profile)]
          (utils/after 200 #(router/redirect! to-url)))))))
  (when (= token-type :password-reset)
    (cook/set-cookie! :show-login-overlay "collect-password"))
  (dis/dispatch! [:auth-with-token/success jwt]))

(defn auth-with-token-callback
  [token-type success body status]
  (if success
    (do
      (jwt-actions/update-jwt body)
      (if (and (not= token-type :password-reset)
                 (and (empty? (jwt/get-key :first-name))
                      (empty? (jwt/get-key :last-name))))
        (nux-actions/new-user-registered "email" #(auth-with-token-success token-type body))
        (auth-with-token-success token-type body)))
    (cond
      (= status 401)
      (auth-with-token-failed 401)
      :else
      (auth-with-token-failed 500))))

(defn auth-with-token [token-type]
  (let [token-links (:links (dis/auth-settings))
        auth-url (utils/link-for token-links "authenticate" "GET" {:auth-source "email"})
        token (dis/query-param :token)]
    (api/auth-with-token auth-url token (partial auth-with-token-callback token-type))
    (dis/dispatch! [:auth-with-token token-type])))

;; Signup

(defn signup-with-email-failed
  ([status] (signup-with-email-failed status nil))
  ([status error-message]
   (dis/dispatch! [:signup-with-email/failed {:status status :error-message error-message}])))

(defn signup-with-email-success
  [user-email team-token-signup? status jwt]
  (let [signup-redirect (if team-token-signup?
                         oc-urls/confirm-invitation-profile
                         oc-urls/sign-up-profile)
        current-org-slug (dis/current-org-slug)]
    (cond
      (= status 204) ;; Email wall since it's a valid signup w/ non verified email address
      (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" user-email)))
      (= status 200) ;; Valid login, not signup, redirect to home
      (if (or
            (and (empty? (:first-name jwt)) (empty? (:last-name jwt)))
            (empty? (:avatar-url jwt)))
        (do
          (utils/after 200 #(router/nav! signup-redirect))
          (api/get-entry-point current-org-slug entry-point-get-finished))
        (api/get-entry-point current-org-slug
         (fn [success body]
           (entry-point-get-finished success body
             (fn [orgs collection]
               (when (pos? (count orgs))
                 (router/nav! (oc-urls/default-landing (:slug (get-default-org orgs))))))))))
      (= status 422)
      (dis/dispatch! [:signup-with-email/failed "Error signing up!"])
      :else ;; Valid signup let's collect user data
      (do
        (jwt-actions/update-jwt jwt)
        (nux-actions/new-user-registered "email")
        (utils/after 200 #(router/nav! signup-redirect))
        (api/get-entry-point (dis/current-org-slug) entry-point-get-finished)
        (dis/dispatch! [:signup-with-email/success])))))

(defn signup-with-email-callback
  [user-email team-token-signup? success body status]
  (if success
    (signup-with-email-success user-email team-token-signup? status body)
    (signup-with-email-failed status body)))

(defn signup-with-email [signup-data & [team-token-signup?]]
  (let [email-links (:links (dis/auth-settings))
        auth-link (utils/link-for email-links "create" "POST" {:auth-source "email"})]
    (api/signup-with-email auth-link
     (or (:firstname signup-data) "")
     (or (:lastname signup-data) "")
     (:email signup-data)
     (:pswd signup-data)
     (.. moment-timezone -tz guess)
     (partial signup-with-email-callback (:email signup-data) team-token-signup?))
    (dis/dispatch! [:signup-with-email])))

(defn signup-with-email-reset-errors []
  (dis/dispatch! [:input [:signup-with-email] {}]))

(defn pswd-collect [form-data password-reset?]
  (let [update-link (utils/link-for (:links (:current-user-data @dis/app-state)) "partial-update" "PATCH")]
    (api/collect-password update-link (:pswd form-data)
      (fn [status body success]
        (when success
          (dis/dispatch! [:user-data (json->cljs body)]))
        (when (<= 200 status 299)
          (if password-reset?
            (do
              (cook/remove-cookie! :show-login-overlay)
              (utils/after 600 #(router/nav! oc-urls/login)))
            (do
              (nux-actions/new-user-registered "email")
              (utils/after 600 #(router/nav! oc-urls/confirm-invitation-profile)))))
        (dis/dispatch! [:pswd-collect/finish status]))))
  (dis/dispatch! [:pswd-collect password-reset?]))

(defn password-reset [email]
  (let [reset-link (utils/link-for (:links (dis/auth-settings)) "reset")]
    (api/password-reset reset-link email
     #(dis/dispatch! [:password-reset/finish %]))
    (dis/dispatch! [:password-reset])))

;; User Profile

(defn- clean-user-data [current-user-data edit-user-profile]
  (let [new-password (:password edit-user-profile)
        password-did-change (pos? (count new-password))
        with-pswd (if (and password-did-change
                           (>= (count new-password) 8))
                    edit-user-profile
                    (dissoc edit-user-profile :password))
        new-email (:email edit-user-profile)
        email-did-change (not= new-email (:email current-user-data))
        with-email (if (and email-did-change
                            (utils/valid-email? new-email))
                     (assoc with-pswd :email new-email)
                     (assoc with-pswd :email (:email current-user-data)))
        timezone (or (:timezone edit-user-profile) (:timezone current-user-data) (.. moment-timezone -tz guess))]
    (assoc with-email :timezone timezone)))

(defn- user-profile-patch [user-data user-profile-link patch-cb]
  (dis/dispatch! [:user-profile-save])
  (api/patch-user user-profile-link user-data
                  (fn [status body success]
                    (when (= status 422)
                      (dis/dispatch! [:user-profile-update/failed]))
                    (let [resp (when success (json->cljs body))]
                      (when (fn? patch-cb)
                        (patch-cb success resp status))
                      (when success
                        (dis/dispatch! [:user-data resp]))))))

(defn user-profile-save
  ([current-user-data edit-data]
   (user-profile-save current-user-data edit-data nil))
  ([current-user-data edit-data save-cb]
   (let [user-data (clean-user-data current-user-data (or (:user-data edit-data) edit-data))
         user-profile-link (utils/link-for (:links current-user-data) "partial-update" "PATCH")]
     (user-profile-patch user-data user-profile-link
      (fn [success resp status]
        (when success
          (utils/after 100 #(jwt-actions/jwt-refresh)))
        (when (fn? save-cb)
          (utils/after 280 #(save-cb success resp))))))))

(defn onboard-profile-save
  ([current-user-data edit-data]
   (onboard-profile-save current-user-data edit-data nil))
  ([current-user-data edit-data org-editing-kw]
   (let [org-editing (when org-editing-kw
                        (get @dis/app-state org-editing-kw))
         user-data (clean-user-data current-user-data (or (:user-data edit-data) edit-data))
         user-profile-link (utils/link-for (:links current-user-data) "partial-update" "PATCH")]
     (dis/dispatch! [:user-profile-save])
     (user-profile-patch user-data user-profile-link
      (fn [success resp status]
        (if-not success
          (notification-actions/show-notification {:title "An error occurred"
                                                   :description "Please try again"
                                                   :expire 3
                                                   :primary-bt-title "OK"
                                                   :primary-bt-dismiss true
                                                   :id :user-profile-save-failed})
          (do
            (when-not org-editing
              (dis/dispatch! [:input [:ap-loading] true]))
            (utils/after 100 (fn []
                               (jwt-actions/jwt-refresh (fn []
                                                          (if org-editing
                                                            (org-actions/create-or-update-org org-editing)
                                                            (api/get-entry-point nil (fn [_ entry-point-body]
                                                                                       (router/nav! (oc-urls/default-landing (-> entry-point-body :collection :items first :slug)))))))))))))))))

(defn user-avatar-save [avatar-url]
  (let [user-avatar-data {:avatar-url avatar-url}
        current-user-data (dis/current-user-data)
        user-profile-link (utils/link-for (:links current-user-data) "partial-update" "PATCH")]
    (api/patch-user user-profile-link user-avatar-data
     (fn [status body success]
       (if-not success
         (do
           (dis/dispatch! [:user-profile-avatar-update/failed])
           (notification-actions/show-notification
            {:title "Image upload error"
             :description "An error occurred while processing your image. Please retry."
             :expire 3
             :id :user-avatar-upload-failed
             :dismiss true}))
         (do
           (utils/after 1000 jwt-actions/jwt-refresh)
           ; (dis/dispatch! [:user-data (json->cljs body)])
           (dis/dispatch! [:user-profile-avatar-update/success (json->cljs body)])
           (notification-actions/show-notification
            {:title "Image update succeeded"
             :description "Your image was succesfully updated."
             :expire 3
             :dismiss true})))))))

(defn user-profile-reset []
  (dis/dispatch! [:user-profile-reset]))

(defn resend-verification-link []
  (let [user-data (dis/current-user-data)]
    (utils/link-for (:links user-data) "resend-verification" "POST")))

(def -resend-verificaiton-cookie-suffix "-resent-verification")

(defn mark-resend-verification-email []
  (let [user-data (dis/current-user-data)]
    (cook/set-cookie! (str (:user-id user-data) -resend-verificaiton-cookie-suffix) "resent" (* 60 60 3))))

(defn can-resend-verification-email? []
  (let [user-data (dis/current-user-data)]
    (not (cook/get-cookie (str (:user-id user-data) -resend-verificaiton-cookie-suffix)))))

(defn resend-verification-email []
  (let [resend-link (resend-verification-link)]
    (when (and (can-resend-verification-email?)
               resend-link)
      (api/resend-verification-email resend-link
       (fn [success]
         (mark-resend-verification-email)
         (notification-actions/show-notification
          {:title (if success "Verification email re-sent!" "An error occurred")
           :description (when-not success "Please try again.")
           :expire 3
           :primary-bt-title "OK"
           :primary-bt-dismiss true
           :id (keyword (str "resend-verification-" (if success "ok" "failed")))}))))))

(defn verify-continue [orgs]
  (let [org (get-default-org orgs)]
    (router/nav! (if org
                   (if (and (empty? (jwt/get-key :first-name))
                            (empty? (jwt/get-key :last-name)))
                     oc-urls/confirm-invitation-profile
                     (oc-urls/org (:slug org)))
                  oc-urls/sign-up-profile))))

;; Mobile push notifications

(def ^{:private true} expo-push-token-expiry (* 60 60 24 352 10)) ;; 10 years (infinite)

(defn dispatch-expo-push-token
  "Save the expo push token in a cookie (or re-save to extend the cookie expire time)
   and dispatch the value into the app-state."
  [push-token]
  (when push-token
    ;; A blank push-token indicates that the user was prompted, but
    ;; denied the push notification permission.
    (cook/set-cookie! router/expo-push-token-cookie push-token expo-push-token-expiry)
    (dis/dispatch! [:expo-push-token push-token])))

(defn recall-expo-push-token
  []
  (dispatch-expo-push-token (cook/get-cookie router/expo-push-token-cookie)))

(defn add-expo-push-token [push-token]
  (let [user-data            (dis/current-user-data)
        add-token-link       (utils/link-for (:links user-data) "add-expo-push-token" "POST")
        need-to-add?         (not (user-utils/user-has-push-token? user-data push-token))]
    (if-not need-to-add?
      ;; Push token already known, dispatch it to app-state immediately
      (dispatch-expo-push-token push-token)
      ;; Novel push token, add it to the Auth service for storage
      (when (and add-token-link push-token)
        ;; Immediately dispatch placeholder token so we don't wait on network request
        (dispatch-expo-push-token "PENDING_PUSH_TOKEN")
        (api/add-expo-push-token
         add-token-link
         push-token
         (fn [success]
           (dispatch-expo-push-token push-token)
           (timbre/info "Successfully saved Expo push notification token")))))))

(defn deny-push-notification-permission
  "Push notification permission was denied."
  []
  (dispatch-expo-push-token ""))

;; Initial loading

(defn initial-loading [& [force-refresh?]]
  (let [force-refresh (or force-refresh?
                          (dis/in-route? :org)
                          (dis/in-route? :login))
        latest-entry-point (if (or force-refresh?
                                   (nil? (:latest-entry-point @dis/app-state)))
                             0
                             (:latest-entry-point @dis/app-state))
        latest-auth-settings (if (or force-refresh?
                                     (nil? (:latest-auth-settings @dis/app-state)))
                               0
                               (:latest-auth-settings @dis/app-state))
        now (.getTime (js/Date.))
        reload-time (* 1000 60 20)] ; every 20m
    (when (or (> (- now latest-entry-point) reload-time)
              (and (dis/current-org-slug)
                   (nil? (dis/org-data))))
      (entry-point-get (dis/current-org-slug) force-refresh?))
    (when (> (- now latest-auth-settings) reload-time)
      (auth-settings-get))))

;; User notifications

(defn read-notifications []
  (dis/dispatch! [:user-notifications/read (dis/current-org-slug)]))

(defn read-notification [notification]
  (dis/dispatch! [:user-notification/read (dis/current-org-slug) notification]))

(defn refresh-follow-containers []
  (let [org-data (dis/org-data)
        current-board-slug (dis/current-board-slug)
        ; is-inbox? (= (dis/current-org-slug) "inbox")
        is-following? (= current-board-slug "following")
        is-replies? (= current-board-slug "replies")
        ; is-unfollowing? (= (dis/current-org-slug) "unfollowing")
        ; inbox-delay (if is-inbox? 1 500)
        following-delay (if is-following? 1 500)
        replies-delay (if is-replies? 1 500)
        ; unfollowing-delay (if is-unfollowing? 1 500)
        ]
    ; (utils/maybe-after inbox-delay #(activity-actions/inbox-get org-data))
    (utils/maybe-after following-delay #(activity-actions/following-get org-data is-following? nil))
    (utils/maybe-after replies-delay #(activity-actions/replies-get org-data is-replies? nil))
    ; (utils/maybe-after unfollowing-delay #(activity-actions/unfollowing-get org-data))
    ))

(defn toggle-publisher [publisher-uuid]
  (let [org-slug (dis/current-org-slug)
        current-publishers (map :user-id (dis/follow-publishers-list org-slug))
        follow? (not (utils/in? current-publishers publisher-uuid))
        next-publishers (if follow?
                          (vec (conj (set current-publishers) publisher-uuid))
                          (vec (disj (set current-publishers) publisher-uuid)))]
    (dis/dispatch! [:publisher/follow (dis/current-org-slug)
                                      {:org-slug org-slug
                                       :publisher-uuids next-publishers
                                       :follow? follow?
                                       :publisher-uuid publisher-uuid}])
    (when follow?
      (dis/dispatch! [:follow-list-last-added org-slug {:last-added-uuid publisher-uuid
                                                        :resource-type :user}]))
    (if follow?
      (ws-cc/publisher-follow publisher-uuid)
      (ws-cc/publisher-unfollow publisher-uuid))))

(defn toggle-board [board-uuid]
  (let [org-slug (dis/current-org-slug)
        current-boards (map :uuid (dis/follow-boards-list org-slug))
        follow? (not (utils/in? current-boards board-uuid))
        next-boards (if follow?
                      (vec (conj (set current-boards) board-uuid))
                      (vec (disj (set current-boards) board-uuid)))]
    (dis/dispatch! [:board/follow (dis/current-org-slug)
                                  {:org-slug org-slug
                                   :board-uuids next-boards
                                   :follow? follow?
                                   :board-uuid board-uuid}])
    (when follow?
      (dis/dispatch! [:follow-list-last-added org-slug {:last-added-uuid board-uuid
                                                        :resource-type :board}]))
    (if follow?
      (ws-cc/board-follow board-uuid)
      (ws-cc/board-unfollow board-uuid))
    (refresh-follow-containers)))

;; Check if there is a premium notification and trigger a JWT refresh if needed

(defun- maybe-refresh-token
  "Check if notification include a refresh-token-at property and
   force refresh the JWT in case that date is after the token creation date"
  ([notifications :guard sequential?]
   (doseq [n notifications]
     (maybe-refresh-token n)))

  ([notification :guard map?]
   (when (jwt/before? (:refresh-token-at notification))
     (timbre/info "Force refresh token for notification" (:refresh-token-at notification))
     (let [redirect? (and (= (:team-action notification) :team-remove)
                          (:live notification)
                          (= (-> notification :org :team-id) (:team-id (dis/org-data))))
           cb (when redirect? #(utils/after 100 (fn []
                                                  (cook/remove-cookie! (router/last-org-cookie))
                                                  (router/redirect! oc-urls/login))))]
     (utils/after 10 #(jwt-actions/jwt-refresh cb))))))

;; subscribe to websocket events

(defn subscribe []
  (ws-nc/subscribe :user/notifications
    (fn [{:keys [data]}]
      (maybe-refresh-token (:notifications data))
      (let [fixed-notifications (notif-utils/fix-notifications (:notifications data))]
        (dis/dispatch! [:user-notifications (dis/current-org-slug) fixed-notifications]))))
  (ws-nc/subscribe :user/notification
    (fn [{:keys [data]}]
      (maybe-refresh-token (assoc data :live true))
      (when-let [fixed-notification (notif-utils/fix-notification data true)]
        (dis/dispatch! [:user-notification (dis/current-org-slug) fixed-notification])
        (notification-actions/show-notification
         {:title (:title fixed-notification)
          :mention true
          :dismiss true
          :click (:click fixed-notification)
          :mention-author (:author fixed-notification)
          :description (:body fixed-notification)
          :id (str "notif-" (:created-at fixed-notification))
          :expire 5}))))
  (ws-cc/subscribe :follow/list
    (fn [{:keys [data]}]
      (dis/dispatch! [:follow/loaded (dis/current-org-slug) data])))
  (ws-cc/subscribe :followers/count
    (fn [{:keys [data]}]
      (dis/dispatch! [:followers-count/finish (dis/current-org-slug) data]))))

;; Tags

(def tag! user-tags/tag!)

(def untag! user-tags/untag!)

;; Debug

(defn ^:export force-jwt-refresh []
  (when (jwt/jwt) (jwt-actions/jwt-refresh)))