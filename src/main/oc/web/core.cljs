(ns oc.web.core
  (:require [secretary.core :as secretary :refer-macros (defroute)]
            [dommy.core :as dommy :refer (listen!) :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.rum-utils :as ru]
            [oops.core :refer (oget ocall oset!)]
            ;; Pull in functions for interfacing with Expo mobile app
            [oc.web.expo :as expo]
            [oc.lib.cljs.useragent :as ua]
            ;; Pull in all the stores to register the events
            [oc.web.actions]
            [oc.web.stores.routing]
            [oc.web.stores.theme]
            [oc.web.stores.jwt]
            [oc.web.stores.org]
            [oc.web.stores.team]
            [oc.web.stores.user]
            [oc.web.stores.search]
            [oc.web.stores.activity]
            [oc.web.stores.comment]
            [oc.web.stores.reply]
            [oc.web.stores.reaction]
            [oc.web.stores.payments]
            [oc.web.stores.section]
            [oc.web.stores.notifications]
            [oc.web.stores.reminder]
            [oc.web.stores.contributions]
            [oc.web.stores.pin]
            [oc.web.stores.label]
            ;; Pull in the needed file for the ws interaction events
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.ws.notify-client :as ws-nc]
            [oc.web.actions.team]
            [oc.web.actions.pin]
            [oc.web.actions.activity :as aa]
            [oc.web.actions.org :as oa]
            [oc.web.actions.comment :as ca]
            [oc.web.actions.reaction :as ra]
            [oc.web.actions.section :as sa]
            [oc.web.actions.contributions :as contrib-actions]
            [oc.web.actions.nux :as na]
            [oc.web.actions.jwt :as ja]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.web-app-update :as web-app-update-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.theme :as theme-actions]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.api :as api]
            [oc.web.urls :as urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.sentry :as sentry]
            [oc.web.lib.logging :as logging]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.org-dashboard :refer (org-dashboard)]
            [oc.web.components.secure-activity :refer (secure-activity)]
            [oc.web.components.ui.onboard-wrapper :refer (onboard-wrapper)]
            [oc.web.components.ui.notifications :refer (notifications)]
            [oc.web.components.ui.login-wall :refer (login-wall)]))

(enable-console-print!)

(defn drv-root [component target]
  (ru/drv-root {:state dis/app-state
                :drv-spec (dis/drv-spec dis/app-state)
                :component component
                :target target})
  (when-let [notifications-mount-point (sel1 [:div#oc-notifications-container])]
    (ru/drv-root {:state dis/app-state
                  :drv-spec (dis/drv-spec dis/app-state)
                  :component notifications
                  :target notifications-mount-point})))

;; setup Sentry error reporting
(defonce sentry (sentry/sentry-setup))

;; Avoid warnings

(defn check-get-params [query-params]
  (when (contains? query-params :browser-type)
    ; if :browser-type is "mobile" the mobile site is forced
    ; any other value will be set as big web
    ; remove the cookie to let it calculate the type of site
    ; Rules set via css won't be affected by this
    (cook/set-cookie! :force-browser-type (:browser-type query-params) (* 60 60 24 6))))

(defn inject-loading []
  (let [target (sel1 [:div#oc-loading])]
    (drv-root loading target)))

(defn rewrite-url [& [{:keys [query-params keep-params]}]]
  (let [window-location (oget js/window "location")
        window-history (oget js/window "history")
        rewrite-to (str (oget window-location "pathname") (oget window-location "hash"))
        search-values (when (seq keep-params)
                        (remove nil?
                         (map #(when-let [v (get query-params %)]
                                 (str (if (keyword? %) (name %) %) "=" v))
                              keep-params)))
        with-search (if (pos? (count search-values))
                      (str rewrite-to "?"
                        (clojure.string/join "&" search-values))
                      rewrite-to)
        doc-title (oget js/document "title")]
    ;; Push state only if the query string has parameters or the history will have duplicates.
    (when (seq (oget window-location "search"))
      (ocall window-history "pushState" #js {} doc-title with-search))))

(defn pre-routing [params & [should-rewrite-url rewrite-params]]
  (routing-actions/pre-routing)
  ;; Add Electron classes if needed
  (let [body (sel1 [:body])]
    (when ua/desktop-app?
      (dommy/add-class! body "electron")
      (when ua/mac?
        (dommy/add-class! body "mac-electron"))
      (when ua/windows?
        (dommy/add-class! body "win-electron"))))
  ;; Setup timbre log level
  (when (-> params :query-params :log-level)
    (logging/config-log-level! (-> params :query-params :log-level)))
  ; make sure the menu is closed
  (let [window-location (oget js/window "location")
        location-pathname (oget window-location "pathname")]
    (when (not= location-pathname (s/lower location-pathname))
      (let [lower-location (str (s/lower location-pathname) (oget window-location "search") (oget window-location "hash"))]
        (oset! js/window "location" lower-location))))
  (routing-actions/routing! {})
  (when (and (contains? (:query-params params) :jwt)
             (map? (js->clj (jwt/decode (-> params :query-params :jwt)))))
    ; contains :jwt, so saving it
    (ja/update-jwt (-> params :query-params :jwt)))
  (when (and (not (jwt/jwt))
             (contains? (:query-params params) :id)
             (map? (js->clj (jwt/decode (-> params :query-params :id)))))
    ; contains :id, so saving it
    (ja/update-id-token (-> params :query-params :id)))
  (check-get-params (:query-params params))
  (when should-rewrite-url
    (rewrite-url rewrite-params))
  (when (= (-> params :query-params :new) "true")
    (swap! dis/app-state assoc :new-slack-user true))
  (when (contains? params :org)
    (swap! dis/app-state assoc :foc-layout dis/default-foc-layout)) ;(aa/saved-foc-layout (:org params))))
  (inject-loading))

(defn post-routing []
  (routing-actions/post-routing)
  (user-actions/initial-loading true)
  (payments-actions/initial-loading))

(defn check-nux [query-params]
  (let [user-settings (when (and (contains? query-params :user-settings)
                                 (#{:profile :notifications} (keyword (:user-settings query-params))))
                        (keyword (:user-settings query-params)))
        org-settings (when (and (not user-settings)
                                (contains? query-params :org-settings)
                                (#{:org :team :invite-picker :invite-email :invite-slack :integrations :payments} (keyword (:org-settings query-params))))
                       (keyword (:org-settings query-params)))
        reminders (when (and ls/reminders-enabled?
                             (not org-settings)
                             (contains? query-params :reminders))
                    :reminders)
        panel-stack (cond
                      org-settings [org-settings]
                      user-settings [user-settings]
                      reminders [reminders]
                      :else [])
        bot-access (when (contains? query-params :access)
                      (:access query-params))
        billing-checkout-map (when (and (= org-settings :payments)
                                        (contains? query-params :result))
                               {dis/checkout-result-key (= (:result query-params) "true")
                                dis/checkout-update-price-key (:update-price query-params)})
        next-app-state (merge {:panel-stack panel-stack
                               :bot-access bot-access}
                        billing-checkout-map)]
    (when (seq panel-stack)
      (dom-utils/lock-page-scroll))
    (swap! dis/app-state merge next-app-state)))

(defn- read-sort-type-from-cookie
  "Read the sort order from the cookie, fallback to the default,
   if it's on drafts board force the recently posted sort since that has only that"
  [params]
  ;; NB: Bypass saved cookie for now, always use recently posted sort
  ; (let [last-sort-type (aa/saved-sort-type (:org params) (:board params))]
  ;   (if (and (#{"following" "all-posts"} (:board params))
  ;            (= last-sort-type dis/recently-posted-sort))
  ;     dis/recent-activity-sort
  ;     dis/recently-posted-sort))
  (if (= (:board params) "replies")
    dis/recent-activity-sort
    dis/recently-posted-sort))

;; Company list
(defn org-handler [route target component params]
  (let [org (:org params)
        board (:board params)
        sort-type (read-sort-type-from-cookie params)
        query-params (:query-params params)
        ;; First ever landing cookie name
        first-ever-cookie-name (when (= route urls/default-board-slug)
                                 (router/first-ever-landing-cookie (jwt/user-id)))
        ;; First ever landing cookie value
        first-ever-cookie (when (= route urls/default-board-slug)
                            (cook/get-cookie first-ever-cookie-name))]
    (if first-ever-cookie
      ;; If first ever land cookie is set redirect user to the hello url
      (do
        ;; Remove the cookie
        (cook/remove-cookie! first-ever-cookie-name)
        ;; Redirect to the first ever landing page
        (router/redirect! (urls/first-ever-landing org)))
      (do
        (pre-routing params true {:query-params query-params :keep-params [:at]})
        ;; save route
        (routing-actions/routing! {:org org
                                   :board board
                                   :sort-type sort-type
                                   :query-params (:query-params params)
                                   :route [org route]
                                   dis/router-opts-key [dis/router-dark-allowed-key]})
        ;; load data from api
        (when-not (dis/org-data)
          (swap! dis/app-state merge {:loading true}))
        (check-nux query-params)
        (post-routing)
        ;; render component
        (drv-root component target)))))

(defn simple-handler [component route-name target params & [rewrite-url]]
  (pre-routing params rewrite-url)
  ;; save route
  (let [org (:org params)
        route (vec (remove nil? [route-name org]))]
    (routing-actions/routing! {:org org
                               :query-params (:query-params params)
                               :route route}))
  (post-routing)
  (when-not (contains? (:query-params params) :jwt)
    ; remove rum component if mounted to the same node
    (rum/unmount target)
    ;; render component
    (drv-root component target)))

;; Component specific to a board
(defn board-handler [route target component params]
  (let [org (:org params)
        board (:board params)
        entry-board (:entry-board params)
        sort-type (read-sort-type-from-cookie params)
        entry (:entry params)
        comment (:comment params)
        query-params (:query-params params)
        has-at-param (contains? query-params :at)]
    (pre-routing params true {:query-params query-params :keep-params [:at]})
    ;; save the route
    (routing-actions/routing! {:org org
                               :board board
                               :sort-type sort-type
                               :entry-board entry-board
                               :activity entry
                               :comment comment
                               :query-params query-params
                               :route (vec (remove nil?
                                                   [org board (when entry entry) (when comment comment) route]))
                               dis/router-opts-key [dis/router-dark-allowed-key]})
    (check-nux query-params)
    (post-routing)
    ;; render component
    (drv-root component target)))

;; Component specific to a contributions
(defn contributions-handler [route target component params]
  (let [org (:org params)
        sort-type (read-sort-type-from-cookie params)
        contributions (:contributions params)
        query-params (:query-params params)]
    (pre-routing params true {:query-params query-params})
    ;; save the route
    (routing-actions/routing! {:org org
                               :contributions contributions
                               :sort-type sort-type
                               :query-params query-params
                               :route [org contributions route]
                               dis/router-opts-key [dis/router-dark-allowed-key]})
    (check-nux query-params)
    (post-routing)
    ;; render component
    (drv-root component target)))

;; Component specific to a secure activity
(defn secure-activity-handler [component route target params pre-routing?]
  (let [org (:org params)
        secure-id (:secure-id params)
        query-params (:query-params params)]
    (when pre-routing?
      (pre-routing params true))
    ;; save the route
    (routing-actions/routing! {:org org
                               :activity (:entry params)
                               :secure-id (or secure-id (:secure-uuid (jwt/get-id-token-contents)))
                               :comment (:comment params)
                               :query-params query-params
                               :route (vec (remove nil?
                                                   [org route secure-id]))
                               dis/router-opts-key [dis/router-dark-allowed-key]})
     ;; do we have the company data already?
    (when (or ;; if the company data are not present
              (not (dis/board-data))
              ;; or the entries key is missing that means we have only
              (not (:posts-list (dis/board-data)))
              ;; a subset of the company data loaded with a SU
              (not (dis/secure-activity-data)))
      (swap! dis/app-state merge {:loading true}))
    (post-routing)
    ;; render component
    (drv-root component target)))

(defn entry-handler [target params]
  (let [with-default-board (update params :board #(or % "following"))]
    (pre-routing with-default-board true)
    (if (and (not (jwt/jwt))
             (:secure-uuid (jwt/get-id-token-contents)))
      (secure-activity-handler secure-activity "secure-activity" target with-default-board false)
      (board-handler "activity" target org-dashboard with-default-board))))

(defn slack-lander-check [params]
  (pre-routing params true)
  (let [new-user (= (:new (:query-params params)) "true")]
    (when new-user
      (na/new-user-registered "slack"))
    (user-actions/lander-check-team-redirect)))

(defn google-lander-check [params]
  (pre-routing params true)
  (let [new-user (= (:new (:query-params params)) "true")]
    (when new-user
      (na/new-user-registered "google"))
    (user-actions/lander-check-team-redirect)))

(defn- append-url-parts [url]
  (str url (oget js/window "location.search") (oget js/window "location.hash")))

;; Routes - Do not define routes when js/document#app
;; is undefined because it breaks tests
(if-let [target (sel1 :div#app)]
  (do
    (defroute _loading_route "/__loading" {:as params}
      (timbre/info "Routing _loading_route __loading")
      (pre-routing params))

    (defroute login-route urls/login {:as params}
      (timbre/info "Routing login-route" urls/login)
      ;; In case user is logged in and has a last org cookie

      (let [last-org-cookie (cook/get-cookie (router/last-org-cookie))]
        (if (and (jwt/jwt)
                   (seq last-org-cookie))
          (do
            ;; remove the last used org cookie
            ;; to avoid infinite loop redirects.
            (cook/remove-cookie! (router/last-org-cookie))
            ;; and redirect him there,
            (router/redirect! (urls/default-landing last-org-cookie)))
          ;; if no cookie or logged out do the login dance
          (simple-handler #(login-wall {:title "Welcome back!" :desc ""}) "login" target params true))))

    (defroute signup-route urls/sign-up {:as params}
      (timbre/info "Routing signup-route" urls/sign-up)
      (when (jwt/jwt)
        (if (seq (cook/get-cookie (router/last-org-cookie)))
          (router/redirect! (urls/default-landing (cook/get-cookie (router/last-org-cookie))))
          (router/redirect! urls/sign-up-profile)))
      (simple-handler #(onboard-wrapper :lander) "sign-up" target params))

    (defroute signup-slash-route (str urls/sign-up "/") {:as params}
      (timbre/info "Routing signup-slash-route" (str urls/sign-up "/"))
      (when (and (jwt/jwt)
                 (seq (cook/get-cookie (router/last-org-cookie))))
        (router/redirect! (urls/default-landing (cook/get-cookie (router/last-org-cookie)))))
      (simple-handler #(onboard-wrapper :lander) "sign-up" target params))

    (defroute signup-profile-route urls/sign-up-profile {:as params}
      (timbre/info "Routing signup-profile-route" urls/sign-up-profile)
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-profile) "sign-up" target params))

    (defroute signup-profile-slash-route (str urls/sign-up-profile "/") {:as params}
      (timbre/info "Routing signup-profile-slash-route" (str urls/sign-up-profile "/"))
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-profile) "sign-up" target params))

    (defroute signup-team-route urls/sign-up-team {:as params}
      (timbre/info "Routing signup-team-route" urls/sign-up-team)
      (if (jwt/jwt)
        (when (seq (cook/get-cookie (router/last-org-cookie)))
          (router/redirect! (urls/default-landing (cook/get-cookie (router/last-org-cookie)))))
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-team) "sign-up" target params))

    (defroute signup-team-slash-route (str urls/sign-up-team "/") {:as params}
      (timbre/info "Routing signup-team-slash-route" (str urls/sign-up-team "/"))
      (if (jwt/jwt)
        (when (seq (cook/get-cookie (router/last-org-cookie)))
          (router/redirect! (urls/default-landing (cook/get-cookie (router/last-org-cookie)))))
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-team) "sign-up" target params))

    (defroute signup-update-team-route (urls/sign-up-update-team ":org") {:as params}
      (timbre/info "Routing signup-update-team-route" (urls/sign-up-update-team ":org"))
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-profile) "sign-up" target params))

    (defroute signup-update-team-slash-route (str (urls/sign-up-update-team ":org") "/") {:as params}
      (timbre/info "Routing signup-update-team-slash-route" (str (urls/sign-up-update-team ":org") "/"))
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-profile) "sign-up" target params))

    (defroute signup-invite-route (urls/sign-up-invite ":org") {:as params}
      (timbre/info "Routing signup-invite-route" (urls/sign-up-invite ":org"))
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-invite) "sign-up" target params))

    (defroute signup-invite-slash-route (str (urls/sign-up-invite ":org") "/") {:as params}
      (timbre/info "Routing signup-invite-slash-route" (str (urls/sign-up-invite ":org") "/"))
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-invite) "sign-up" target params))

    (defroute slack-lander-check-route urls/slack-lander-check {:as params}
      (timbre/info "Routing slack-lander-check-route" urls/slack-lander-check)
      ;; Check if the user already have filled the needed data or if it needs to
      (slack-lander-check params))

    (defroute slack-lander-check-slash-route (str urls/slack-lander-check "/") {:as params}
      (timbre/info "Routing slack-lander-check-slash-route" (str urls/slack-lander-check "/"))
      ;; Check if the user already have filled the needed data or if it needs to
      (slack-lander-check params))

    (defroute google-lander-check-route urls/google-lander-check {:as params}
      (timbre/info "Routing google-lander-check-route" urls/google-lander-check)
      ;; Check if the user already have filled the needed data or if it needs to
      (google-lander-check params))

    (defroute google-lander-check-slash-route (str urls/google-lander-check "/") {:as params}
      (timbre/info "Routing google-lander-check-slash-route" (str urls/google-lander-check "/"))
      ;; Check if the user already have filled the needed data or if it needs to
      (google-lander-check params))

    (defroute email-confirmation-route urls/email-confirmation {:as params}
      (timbre/info "Routing email-confirmation-route" urls/email-confirmation)
      (when-not (seq (:token (:query-params params)))
        (router/redirect! (if (jwt/jwt) (urls/your-digest-url) urls/home)))
      (jwt/remove-jwt!)
      (cook/remove-cookie! :show-login-overlay)
      (simple-handler #(onboard-wrapper :email-verified) "email-verification" target params))

    (defroute password-reset-route urls/password-reset {:as params}
      (timbre/info "Routing password-reset-route" urls/password-reset)
      (when (jwt/jwt)
        (router/redirect! urls/home))
      (simple-handler #(onboard-wrapper :password-reset-lander) "password-reset" target params))

    (defroute confirm-invitation-route urls/confirm-invitation {:keys [query-params] :as params}
      (timbre/info "Routing confirm-invitation-route" urls/confirm-invitation)
      (when (and (empty? (:token query-params))
                 (empty? (:invite-token query-params)))
        (router/redirect! urls/home))
      (when (jwt/jwt)
        (jwt/remove-jwt!)
        (cook/remove-cookie! :show-login-overlay))
      (let [invitee-type (if (contains? query-params :invite-token)
                          :invitee-team-lander
                          :invitee-lander)]
        (simple-handler #(onboard-wrapper invitee-type) "confirm-invitation" target params)))

    (defroute confirm-invitation-password-route urls/confirm-invitation-password {:as params}
      (timbre/info "Routing confirm-invitation-password-route" urls/confirm-invitation-password)
      (when-not (jwt/jwt)
        (router/redirect! urls/home))
      (simple-handler #(onboard-wrapper :invitee-lander-password) "confirm-invitation" target params))

    (defroute confirm-invitation-profile-route urls/confirm-invitation-profile {:as params}
      (timbre/info "Routing confirm-invitation-profile-route" urls/confirm-invitation-profile)
      (when-not (jwt/jwt)
        (router/redirect! urls/home))
      (simple-handler #(onboard-wrapper :invitee-lander-profile) "confirm-invitation" target params))

    (defroute email-wall-route urls/email-wall {:keys [query-params] :as params}
      (timbre/info "Routing email-wall-route" urls/email-wall)
      ; Email wall is shown only to not logged in users
      (when (jwt/jwt)
        (router/redirect! urls/home))
      (simple-handler #(onboard-wrapper :email-wall) "email-wall" target params true))

    (defroute email-wall-slash-route (str urls/email-wall "/") {:keys [query-params] :as params}
      (timbre/info "Routing email-wall-slash-route" (str urls/email-wall "/"))
      (when (jwt/jwt)
        (router/redirect! (urls/default-landing (cook/get-cookie (router/last-org-cookie)))))
      (simple-handler #(onboard-wrapper :email-wall) "email-wall" target params true))

    (defroute login-wall-route urls/login-wall {:keys [query-params] :as params}
      (timbre/info "Routing login-wall-route" urls/login-wall)
      ; Email wall is shown only to not logged in users
      (when (jwt/jwt)
        (router/redirect-404!))
      (simple-handler login-wall "login-wall" target params true))

    (defroute login-wall-slash-route (str urls/login-wall "/") {:keys [query-params] :as params}
      (timbre/info "Routing login-wall-slash-route" (str urls/login-wall "/"))
      (when (jwt/jwt)
        (router/redirect-404!))
      (simple-handler login-wall "login-wall" target params true))

    (defroute native-login-route urls/native-login {:keys [query-params] :as params}
      (timbre/info "Routing native-login-route" urls/native-login)
      (if (jwt/jwt)
        (router/redirect!
         (if (seq (cook/get-cookie (router/last-org-cookie)))
           (urls/default-landing (cook/get-cookie (router/last-org-cookie)))
           urls/login))
        (simple-handler #(login-wall {:title "Welcome back!" :desc ""}) "login-wall" target params true)))

    (defroute native-login-slash-route (str urls/native-login "/") {:keys [query-params] :as params}
      (timbre/info "Routing native-login-slash-route" (str urls/native-login "/"))
      (if (jwt/jwt)
        (router/redirect!
         (if (seq (cook/get-cookie (router/last-org-cookie)))
           (urls/default-landing (cook/get-cookie (router/last-org-cookie)))
           urls/login))
        (simple-handler #(login-wall {:title "Welcome back!" :desc ""}) "login-wall" target params true)))

    (defroute logout-route urls/logout {:as params}
      (timbre/info "Routing logout-route" urls/logout)
      (jwt/remove-jwt!)
      (cook/remove-cookie! :show-login-overlay)
      (router/redirect! (if ua/pseudo-native?
                          urls/native-login
                          urls/home)))

    (defroute apps-detect-route urls/apps-detect {:as params}
      (timbre/info "Routing apps-detect-route" urls/apps-detect)
      (router/redirect!
       (cond
        ua/mac? ls/mac-app-url
        ua/windows? ls/win-app-url
        ua/ios? ls/ios-app-url
        ua/android? ls/android-app-url
        :else urls/home)))

    (defroute org-route (urls/org ":org") {:as params}
      (timbre/info "Routing org-route" (urls/org ":org"))
      (org-handler "org" target org-dashboard params))

    (defroute org-slash-route (str (urls/org ":org") "/") {:as params}
      (timbre/info "Routing org-slash-route" (str (urls/org ":org") "/"))
      (org-handler "org" target org-dashboard params))

    (defroute inbox-route (urls/inbox ":org") {:as params}
      (timbre/info "Routing inbox-route" (urls/inbox ":org"))
      (router/redirect! (urls/following (:org params))))

    (defroute inbox-slash-route (str (urls/inbox ":org") "/") {:as params}
      (timbre/info "Routing inbox-slash-route" (str (urls/inbox ":org") "/"))
      (router/redirect! (urls/following (:org params))))

    (defroute all-posts-route (urls/all-posts ":org") {:as params}
      (timbre/info "Routing all-posts-route" (urls/all-posts ":org"))
      (if (seq (:old (:query-params params)))
        (org-handler "dashboard" target org-dashboard (assoc params :board "all-posts"))
        (router/redirect! (urls/following (:org params)))))

    (defroute all-posts-slash-route (str (urls/all-posts ":org") "/") {:as params}
      (timbre/info "Routing all-posts-slash-route" (str (urls/all-posts ":org") "/"))
      (if (seq (:old (:query-params params)))
        (org-handler "dashboard" target org-dashboard (assoc params :board "all-posts"))
        (router/redirect! (urls/following (:org params)))))

    (defroute following-route (urls/following ":org") {:as params}
      (timbre/info "Routing following-route" (urls/following ":org"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "following")))

    (defroute following-slash-route (str (urls/following ":org") "/") {:as params}
      (timbre/info "Routing following-slash-route" (str (urls/following ":org") "/"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "following")))

    (defroute unfollowing-route (urls/unfollowing ":org") {:as params}
      (timbre/info "Routing unfollowing-route" (urls/unfollowing ":org"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "unfollowing")))

    (defroute unfollowing-slash-route (str (urls/unfollowing ":org") "/") {:as params}
      (timbre/info "Routing unfollowing-slash-route" (str (urls/unfollowing ":org") "/"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "unfollowing")))

    (defroute first-ever-landing-route (urls/first-ever-landing ":org") {:as params}
      (timbre/info "Routing first-ever-landing-route" (urls/first-ever-landing ":org"))
      (org-handler "dashboard" target org-dashboard (assoc params :board urls/default-board-slug)))

    (defroute first-ever-landing-slash-route (str (urls/first-ever-landing ":org") "/") {:as params}
      (timbre/info "Routing first-ever-landing-slash-route" (str (urls/first-ever-landing ":org") "/"))
      (org-handler "dashboard" target org-dashboard (assoc params :board urls/default-board-slug)))

    (defroute follow-ups-route (urls/follow-ups ":org") {:as params}
      (timbre/info "Routing follow-ups-route" (urls/follow-ups ":org"))
      (router/redirect! (urls/bookmarks (:org params))))

    (defroute follow-ups-slash-route (str (urls/follow-ups ":org") "/") {:as params}
      (timbre/info "Routing follow-ups-slash-route" (str (urls/follow-ups ":org") "/"))
      (router/redirect! (urls/bookmarks (:org params))))

    (defroute bookmarks-route (urls/bookmarks ":org") {:as params}
      (timbre/info "Routing bookmarks-route" (urls/bookmarks ":org"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "bookmarks")))

    (defroute bookmarks-slash-route (str (urls/bookmarks ":org") "/") {:as params}
      (timbre/info "Routing bookmarks-slash-route" (str (urls/bookmarks ":org") "/"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "bookmarks")))

    (defroute drafts-route (urls/drafts ":org") {:as params}
      (timbre/info "Routing board-route" (urls/drafts ":org"))
      (board-handler "dashboard" target org-dashboard (assoc params :board "drafts")))

    (defroute drafts-slash-route (str (urls/drafts ":org") "/") {:as params}
      (timbre/info "Routing board-slash-route" (str (urls/drafts ":org") "/"))
      (board-handler "dashboard" target org-dashboard (assoc params :board "drafts")))

    (defroute replies-route (urls/replies ":org") {:as params}
      (timbre/info "Routing replies-route" (urls/replies ":org"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "replies")))

    (defroute replies-slash-route (str (urls/replies ":org") "/") {:as params}
      (timbre/info "Routing replies-slash-route" (str (urls/replies ":org") "/"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "replies")))

    (defroute topics-route (urls/topics ":org") {:as params}
      (timbre/info "Routing topics-route" (urls/topics ":org"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "topics")))

    (defroute topics-slash-route (str (urls/topics ":org") "/") {:as params}
      (timbre/info "Routing topics-slash-route" (str (urls/topics ":org") "/"))
      (org-handler "dashboard" target org-dashboard (assoc params :board "topics")))

    (defroute secure-activity-route (urls/secure-activity ":org" ":secure-id") {:as params}
      (timbre/info "Routing secure-activity-route" (urls/secure-activity ":org" ":secure-id"))
      (secure-activity-handler secure-activity "secure-activity" target params true))

    (defroute secure-activity-slash-route (str (urls/secure-activity ":org" ":secure-id") "/") {:as params}
      (timbre/info "Routing secure-activity-slash-route" (str (urls/secure-activity ":org" ":secure-id") "/"))
      (secure-activity-handler secure-activity "secure-activity" target params true))

    (defroute board-route (urls/board ":org" ":board") {:as params}
      (timbre/info "Routing board-route" (urls/board ":org" ":board"))
      (board-handler "dashboard" target org-dashboard params))

    (defroute board-slash-route (str (urls/board ":org" ":board") "/") {:as params}
      (timbre/info "Routing board-route-slash" (str (urls/board ":org" ":board") "/"))
      (board-handler "dashboard" target org-dashboard params))

    (defroute contributions-route (urls/contributions ":org" ":contributions") {:as params}
      (timbre/info "Routing contributions-route" (urls/board ":org" ":contributions"))
      (contributions-handler "dashboard" target org-dashboard params))

    (defroute contributions-slash-route (str (urls/contributions ":org" ":contributions") "/") {:as params}
      (timbre/info "Routing contributions-slash-route" (str (urls/board ":org" ":contributions") "/"))
      (contributions-handler "dashboard" target org-dashboard params))

    (defroute entry-route (urls/entry ":org" ":entry-board" ":entry") {:as params}
      (timbre/info "Routing entry-route" (urls/entry ":org" ":entry-board" ":entry"))
      (if-let [redirect-map (router/needs-uuid-fixes? params [:entry])]
        (router/redirect! (append-url-parts (urls/entry (:org redirect-map) (:entry-board redirect-map) (:entry redirect-map))))
        (entry-handler target params)))

    (defroute entry-slash-route (str (urls/entry ":org" ":entry-board" ":entry") "/") {:as params}
      (timbre/info "Routing entry-route-slash" (str (urls/entry ":org" ":entry-board" ":entry") "/"))
      (if-let [redirect-map (router/needs-uuid-fixes? params [:entry])]
        (router/redirect! (append-url-parts (urls/entry (:org redirect-map) (:entry-board redirect-map) (:entry redirect-map))))
        (entry-handler target params)))

    (defroute comment-route (urls/comment-url ":org" ":entry-board" ":entry" ":comment") {:as params}
      (timbre/info "Routing comment-route" (urls/comment-url ":org" ":entry-board" ":entry" ":comment"))
      (if-let [redirect-map (router/needs-uuid-fixes? params [:entry :comment])]
        (router/redirect! (append-url-parts (urls/comment-url (:org redirect-map) (:entry-board redirect-map) (:entry redirect-map) (:comment redirect-map))))
        (entry-handler target params)))

    (defroute comment-slash-route (str (urls/comment-url ":org" ":entry-board" ":entry" ":comment") "/") {:as params}
      (timbre/info "Routing comment-slash-route" (str (urls/comment-url ":org" ":entry-board" ":entry" ":comment") "/"))
      (if-let [redirect-map (router/needs-uuid-fixes? params [:entry :comment])]
        (router/redirect! (append-url-parts (urls/comment-url (:org redirect-map) (:entry-board redirect-map) (:entry redirect-map) (:comment redirect-map))))
        (entry-handler target params)))

    (defroute not-found-route "*" []
      (timbre/info "Routing not-found-route" "*")
      ;; render component
      (if (jwt/jwt)
        (router/redirect-404!)
        (router/redirect! (str urls/login-wall "?login-redirect=" (js/encodeURIComponent (router/get-token))))))

    (defn handle-url-change [e]
      ;; Get the mobile app deep link origin if we're on mobile
      (when ua/mobile-app?
        (expo/bridge-get-color-scheme))

      ;; we are checking if this event is due to user action,
      ;; such as initial page load, click a link, a back button, etc.
      ;; as opposed to programmatically setting the URL with the API
      (when-not (oget e "isNavigation")
        ;; in this case, we're setting it so
        ;; let's scroll to the top to simulate a navigation
        (if ua/edge?
          (oset! js/document "scrollingElement.scrollTop" (utils/page-scroll-top))
          (js/window.scrollTo (utils/page-scroll-top) 0)))
      ;; dispatch on the token
      (secretary/dispatch! (router/get-token))
      ; remove all the tooltips
      (utils/after 100 #(utils/remove-tooltips))))
  (do
    (timbre/error "Error: div#app is not defined!")
    (sentry/capture-message! "Error: div#app is not defined!")))

(defn ^:export init []
  (jwt/init)
  ;; Setup timbre log level
  (logging/config-log-level! (or (dis/query-param :log-level) ls/log-level))
  ;; Setup API requests
  (api/config-request
   #(ja/update-jwt %) ;; success jwt refresh after expire
   #(ja/logout) ;; failed to refresh jwt
   ;; network error
   #(when %
      (notification-actions/show-notification (assoc utils/network-error :expire 5))))
  ;; Persist JWT in App State
  (ja/dispatch-jwt)
  (ja/dispatch-id-token)
  (theme-actions/setup-theme)
  ;; Get the mobile app deep link origin if we're on mobile
  (when ua/mobile-app?
    (expo/bridge-init))
  ;; Recall Expo push token into app state (push notification permission)
  (user-actions/recall-expo-push-token)
  ;; Subscribe to websocket client events
  (aa/ws-change-subscribe)
  (sa/ws-change-subscribe)
  (contrib-actions/subscribe)
  (oa/subscribe)
  (ra/subscribe)
  (ca/subscribe)
  (user-actions/subscribe)

  ;; Start the app update check cicle
  (web-app-update-actions/start-web-app-update-check!)

  ;; on any click remove all the shown tooltips to make sure they don't get stuck
  (listen! js/window :click #(utils/remove-tooltips))
  ;; setup the router navigation only when handle-url-change and route-disaptch!
  ;; are defined, this is used to avoid crash on tests
  (when handle-url-change
    (router/setup-navigation! handle-url-change)))

(defn on-js-reload []
  (ocall js/console "clear")
  (api/config-request
   #(ja/update-jwt %) ;; success jwt refresh after expire
   #(ja/logout) ;; failed to refresh jwt
   ;; network error
   #(when %
      (notification-actions/show-notification (assoc utils/network-error :expire 5))))
  (dom-utils/force-unlock-page-scroll)
  (ws-ic/reset-connection!)
  (ws-nc/reset-connection!)
  (ws-cc/reset-connection!)
  (secretary/dispatch! (router/get-token))
  (when ua/mobile-app?
    (expo/bridge-get-color-scheme))
  (theme-actions/setup-theme))
