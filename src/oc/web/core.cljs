(ns oc.web.core
  (:require [secretary.core :as secretary :refer-macros (defroute)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.rum-utils :as ru]
            ;; Pull in all the stores to register the events
            [oc.web.actions]
            [oc.web.stores.routing]
            [oc.web.stores.jwt]
            [oc.web.stores.org]
            [oc.web.stores.team]
            [oc.web.stores.user]
            [oc.web.stores.search]
            [oc.web.stores.activity]
            [oc.web.stores.comment]
            [oc.web.stores.reaction]
            [oc.web.stores.subscription]
            [oc.web.stores.section]
            [oc.web.stores.notifications]
            [oc.web.stores.reminder]
            ;; Pull in the needed file for the ws interaction events
            [oc.web.ws.interaction-client]
            [oc.web.actions.team]
            [oc.web.actions.activity :as aa]
            [oc.web.actions.org :as oa]
            [oc.web.actions.comment :as ca]
            [oc.web.actions.reaction :as ra]
            [oc.web.actions.section :as sa]
            [oc.web.actions.nux :as na]
            [oc.web.actions.jwt :as ja]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.api :as api]
            [oc.web.urls :as urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.ziggeo :as ziggeo]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.raven :as sentry]
            [oc.web.lib.logging :as logging]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.org-dashboard :refer (org-dashboard)]
            [oc.web.components.user-profile :refer (user-profile)]
            [oc.web.components.about :refer (about)]
            [oc.web.components.home-page :refer (home-page)]
            [oc.web.components.pricing :refer (pricing)]
            [oc.web.components.slack :refer (slack)]
            [oc.web.components.press-kit :refer (press-kit)]
            [oc.web.components.slack-lander :refer (slack-lander)]
            [oc.web.components.secure-activity :refer (secure-activity)]
            [oc.web.components.ui.onboard-wrapper :refer (onboard-wrapper)]
            [oc.web.components.ui.notifications :refer (notifications)]))

(enable-console-print!)

(defn drv-root [component target]
  (ru/drv-root {:state dis/app-state
                :drv-spec (dis/drv-spec dis/app-state router/path)
                :component component
                :target target})
  (when-let [notifications-mount-point (sel1 [:div#oc-notifications-container])]
    (ru/drv-root {:state dis/app-state
                  :drv-spec (dis/drv-spec dis/app-state router/path)
                  :component notifications
                  :target notifications-mount-point})))

;; setup Sentry error reporting
(defonce raven (sentry/raven-setup))

;; Avoid warnings
(declare route-dispatch!)

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
  (let [l (.-location js/window)
        rewrite-to (str (.-pathname l) (.-hash l))
        search-values (when (seq keep-params)
                        (remove nil?
                         (map #(when (get query-params %)
                                 (str (name %) "=" (get query-params %))) keep-params)))
        with-search (if (pos? (count search-values))
                      (str rewrite-to "?"
                        (clojure.string/join "&" search-values))
                      rewrite-to)]
    ;; Push state only if the query string has parameters or the history will have duplicates.
    (when (seq (.-search l))
      (.pushState (.-history js/window) #js {} (.-title js/document) with-search))))

(defn pre-routing [query-params & [should-rewrite-url rewrite-params]]
  ;; Setup timbre log level
  (when (:log-level query-params)
    (logging/config-log-level! (:log-level query-params)))
  ; make sure the menu is closed
  (let [pathname (.. js/window -location -pathname)]
    (when (not= pathname (s/lower pathname))
      (let [lower-location (str (s/lower pathname) (.. js/window -location -search) (.. js/window -location -hash))]
        (set! (.-location js/window) lower-location))))
  (swap! router/path {})
  (when (and (contains? query-params :jwt)
             (map? (js->clj (jwt/decode (:jwt query-params)))))
    ; contains :jwt, so saving it
    (ja/update-jwt (:jwt query-params)))
  (when (and (not (jwt/jwt))
             (contains? query-params :id)
             (map? (js->clj (jwt/decode (:id query-params)))))
    ; contains :id, so saving it
    (ja/update-id-token (:id query-params)))
  (check-get-params query-params)
  (when should-rewrite-url
    (rewrite-url rewrite-params))
  (when (= (:new query-params) "true")
    (swap! dis/app-state assoc :new-slack-user true))
  (inject-loading))

(defn post-routing []
  (routing-actions/routing @router/path)
  (user-actions/initial-loading))

;; home
(defn home-handler [target params]
  (pre-routing (:query-params params) true)
  ;; save route
  (router/set-route! ["home"] {:query-params (:query-params params)})
  (post-routing)
  ;; render component
  (drv-root home-page target))

(defn check-nux [query-params]
  (let [has-at-param (contains? query-params :at)
        loading (or (and ;; if is board page
                         (not (contains? query-params :ap))
                         ;; if the board data are not present
                         (not (dis/posts-data)))
                         ;; if the all-posts data are not preset
                    (and (contains? query-params :ap)
                         ;; this latter is used when displaying modal over AP
                         (not (dis/posts-data))))
        user-settings (when (and (contains? query-params :user-settings)
                                 (#{:profile :notifications} (keyword (:user-settings query-params))))
                        (keyword (:user-settings query-params)))
        org-settings (when (and (not user-settings)
                              (contains? query-params :org-settings)
                              (#{:main :team :invite} (keyword (:org-settings query-params))))
                       (keyword (:org-settings query-params)))
        reminders (when (and (not org-settings)
                             (contains? query-params :reminders))
                    :reminders)
        bot-access (when (contains? query-params :access)
                      (:access query-params))
        next-app-state {:loading loading
                        :ap-initial-at (when has-at-param (:at query-params))
                        :org-settings org-settings
                        :user-settings user-settings
                        :show-reminders reminders
                        :bot-access bot-access}]
    (swap! dis/app-state merge next-app-state)))

;; Company list
(defn org-handler [route target component params]
  (let [org (:org (:params params))
        board (:board (:params params))
        query-params (:query-params params)
        ;; First ever landing cookie name
        first-ever-cookie-name (when (= route "all-posts")
                                 (router/first-ever-ap-land-cookie (jwt/user-id)))
        ;; First ever landing cookie value
        first-ever-cookie (when (= route "all-posts")
                            (cook/get-cookie first-ever-cookie-name))]
    (if first-ever-cookie
      ;; If first ever land cookie is set redirect user to the hello url
      (do
        ;; Remove the cookie
        (cook/remove-cookie! first-ever-cookie-name)
        ;; Redirect to the first ever landing page
        (router/redirect! (urls/first-ever-all-posts org)))
      (do
        (pre-routing query-params true {:query-params query-params :keep-params [:at]})
        ;; save route
        (router/set-route! [org route] {:org org :board board :query-params (:query-params params)})
        ;; load data from api
        (when-not (dis/org-data)
          (swap! dis/app-state merge {:loading true}))
        (check-nux query-params)
        (post-routing)
        ;; render component
        (drv-root component target)))))

(defn simple-handler [component route-name target params & [rewrite-url]]
  (pre-routing (:query-params params) rewrite-url)
  ;; save route
  (let [org (:org (:params params))]
    (router/set-route! (vec (remove nil? [route-name org])) {:org org :query-params (:query-params params)}))
  (post-routing)
  (when-not (contains? (:query-params params) :jwt)
    ; remove rum component if mounted to the same node
    (rum/unmount target)
    ;; render component
    (drv-root component target)))

;; Component specific to a board
(defn board-handler [route target component params]
  (let [org (:org (:params params))
        board (:board (:params params))
        entry (:entry (:params params))
        query-params (:query-params params)
        has-at-param (contains? query-params :at)]
    (pre-routing query-params true {:query-params query-params :keep-params [:at]})
    ;; save the route
    (router/set-route!
     (vec
      (remove
       nil?
       [org board (when entry entry) route]))
     {:org org
      :board board
      :activity entry
      :query-params query-params})
    (check-nux query-params)
    (post-routing)
    ;; render component
    (drv-root component target)))

;; Component specific to a secure activity
(defn secure-activity-handler [component route target params]
  (let [org (:org (:params params))
        secure-id (:secure-id (:params params))
        query-params (:query-params params)]
    (pre-routing query-params true)
    ;; save the route
    (router/set-route!
     (vec
      (remove
       nil?
       [org route secure-id]))
     {:org org
      :secure-id secure-id
      :query-params query-params})
     ;; do we have the company data already?
    (when (or ;; if the company data are not present
              (not (dis/board-data))
              ;; or the entries key is missing that means we have only
              (not (:posts-list (dis/board-data)))
              ;; a subset of the company data loaded with a SU
              (not (dis/secure-activity-data)))
      (swap! dis/app-state merge {:loading true}))
    (aa/secure-activity-chain)
    ;; render component
    (drv-root component target)))

;; Component specific to a team settings
(defn team-handler [route target component params]
  (let [org (:org (:params params))
        query-params (:query-params params)]
    (pre-routing query-params true)
    ;; save the route
    (router/set-route! [org route] {:org org :query-params query-params})
    (post-routing)
    ;; render component
    (drv-root component target)))

(defn slack-lander-check [params]
  (pre-routing (:query-params params) true)
  (let [new-user (= (:new (:query-params params)) "true")]
    (when new-user
      (na/new-user-registered "slack"))
    (user-actions/lander-check-team-redirect)))

(defn google-lander-check [params]
  (pre-routing (:query-params params) true)
  (let [new-user (= (:new (:query-params params)) "true")]
    (when new-user
      (na/new-user-registered "google"))
    (user-actions/lander-check-team-redirect)))

;; Routes - Do not define routes when js/document#app
;; is undefined because it breaks tests
(if-let [target (sel1 :div#app)]
  (do
    (defroute _loading_route "/__loading" {:as params}
      (timbre/info "Routing _loading_route __loading")
      (pre-routing (:query-params params)))

    (defroute login-route urls/login {:as params}
      (timbre/info "Routing login-route" urls/login)
      (when (and (not (contains? (:query-params params) :jwt))
                 (not (jwt/jwt)))
        (if (contains? (:query-params params) :slack)
          (swap! dis/app-state assoc :show-login-overlay :signup-with-slack)
          (swap! dis/app-state assoc :show-login-overlay :login-with-slack)))
      (simple-handler home-page "login" target params))

    (defroute signup-route urls/sign-up {:as params}
      (timbre/info "Routing signup-route" urls/sign-up)
      (when (jwt/jwt)
        (if (seq (cook/get-cookie (router/last-org-cookie)))
          (router/redirect! (urls/all-posts (cook/get-cookie (router/last-org-cookie))))
          (router/redirect! urls/sign-up-profile)))
      (simple-handler #(onboard-wrapper :lander) "sign-up" target params))

    (defroute signup-slash-route (str urls/sign-up "/") {:as params}
      (timbre/info "Routing signup-slash-route" (str urls/sign-up "/"))
      (when (and (jwt/jwt)
                 (seq (cook/get-cookie (router/last-org-cookie))))
        (router/redirect! (urls/all-posts (cook/get-cookie (router/last-org-cookie)))))
      (simple-handler #(onboard-wrapper :lander) "sign-up" target params))

    (defroute sign-up-slack-route urls/sign-up-slack {:as params}
      (timbre/info "Routing sign-up-slack-route" urls/sign-up-slack)
      (when (jwt/jwt)
        (if (seq (cook/get-cookie (router/last-org-cookie)))
          (router/redirect! (urls/all-posts (cook/get-cookie (router/last-org-cookie))))
          (router/redirect! urls/sign-up-profile)))
      (simple-handler slack-lander "slack-lander" target params))

    (defroute sign-up-slack-slash-route (str urls/sign-up-slack "/") {:as params}
      (timbre/info "Routing sign-up-slack-slash-route" (str urls/sign-up-slack "/"))
      (when (jwt/jwt)
        (if (seq (cook/get-cookie (router/last-org-cookie)))
          (router/redirect! (urls/all-posts (cook/get-cookie (router/last-org-cookie))))
          (router/redirect! urls/sign-up-profile)))
      (simple-handler slack-lander "slack-lander" target params))

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
          (router/redirect! (urls/all-posts (cook/get-cookie (router/last-org-cookie)))))
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-team) "sign-up" target params))

    (defroute signup-team-slash-route (str urls/sign-up-team "/") {:as params}
      (timbre/info "Routing signup-team-slash-route" (str urls/sign-up-team "/"))
      (if (jwt/jwt)
        (when (seq (cook/get-cookie (router/last-org-cookie)))
          (router/redirect! (urls/all-posts (cook/get-cookie (router/last-org-cookie)))))
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

    (defroute signup-setup-sections-route (urls/sign-up-setup-sections ":org") {:as params}
      (timbre/info "Routing signup-setup-sections-route" (urls/sign-up-setup-sections ":org"))
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-sections) "sign-up" target params))

    (defroute signup-setup-sections-slash-route (str (urls/sign-up-setup-sections ":org") "/") {:as params}
      (timbre/info "Routing signup-setup-sections-slash-route" (str (urls/sign-up-setup-sections ":org") "/"))
      (when-not (jwt/jwt)
        (router/redirect! urls/sign-up))
      (simple-handler #(onboard-wrapper :lander-sections) "sign-up" target params))

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

    (defroute about-route urls/about {:as params}
      (timbre/info "Routing about-route" urls/about)
      (simple-handler about "about" target params))

    (defroute slack-route urls/slack {:as params}
      (timbre/info "Routing slack-route" urls/slack)
      (simple-handler slack "slack" target params))

    (defroute pricing-route urls/pricing {:as params}
      (timbre/info "Routing pricing-route" urls/pricing)
      (simple-handler pricing "pricing" target params))

    (defroute press-kit-route urls/press-kit {:as params}
      (timbre/info "Routing press-kit-route" urls/press-kit)
      (simple-handler press-kit "press-kit" target params))

    (defroute email-confirmation-route urls/email-confirmation {:as params}
      (cook/remove-cookie! :jwt)
      (cook/remove-cookie! :show-login-overlay)
      (timbre/info "Routing email-confirmation-route" urls/email-confirmation)
      (simple-handler #(onboard-wrapper :email-verified) "email-verification" target params))

    (defroute password-reset-route urls/password-reset {:as params}
      (timbre/info "Routing password-reset-route" urls/password-reset)
      (when (jwt/jwt)
        (router/redirect! urls/home))
      (simple-handler #(onboard-wrapper :password-reset-lander) "password-reset" target params))

    (defroute confirm-invitation-route urls/confirm-invitation {:keys [query-params] :as params}
      (timbre/info "Routing confirm-invitation-route" urls/confirm-invitation)
      (when (empty? (:token query-params))
        (router/redirect! urls/home))
      (when (jwt/jwt)
        (cook/remove-cookie! :jwt)
        (cook/remove-cookie! :show-login-overlay))
      (simple-handler #(onboard-wrapper :invitee-lander) "confirm-invitation" target params))

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
        (router/redirect! urls/home))
      (simple-handler #(onboard-wrapper :email-wall) "email-wall" target params true))

    (defroute home-page-route urls/home {:as params}
      (timbre/info "Routing home-page-route" urls/home)
      (home-handler target params))

    (defroute logout-route urls/logout {:as params}
      (timbre/info "Routing logout-route" urls/logout)
      (cook/remove-cookie! :jwt)
      (cook/remove-cookie! :show-login-overlay)
      (router/redirect! urls/home))

    (defroute org-route (urls/org ":org") {:as params}
      (timbre/info "Routing org-route" (urls/org ":org"))
      (org-handler "org" target org-dashboard params))

    (defroute org-slash-route (str (urls/org ":org") "/") {:as params}
      (timbre/info "Routing org-slash-route" (str (urls/org ":org") "/"))
      (org-handler "org" target org-dashboard params))

    (defroute all-posts-route (urls/all-posts ":org") {:as params}
      (timbre/info "Routing all-posts-route" (urls/all-posts ":org"))
      (org-handler "all-posts" target org-dashboard (assoc-in params [:params :board] "all-posts")))

    (defroute all-posts-slash-route (str (urls/all-posts ":org") "/") {:as params}
      (timbre/info "Routing all-posts-slash-route" (str (urls/all-posts ":org") "/"))
      (org-handler "all-posts" target org-dashboard (assoc-in params [:params :board] "all-posts")))

    (defroute first-ever-all-posts-route (urls/first-ever-all-posts ":org") {:as params}
      (timbre/info "Routing first-ever-all-posts-route" (urls/first-ever-all-posts ":org"))
      (org-handler "all-posts" target org-dashboard (assoc-in params [:params :board] "all-posts")))

    (defroute first-ever-all-posts-slash-route (str (urls/first-ever-all-posts ":org") "/") {:as params}
      (timbre/info "Routing first-ever-all-posts-slash-route" (str (urls/first-ever-all-posts ":org") "/"))
      (org-handler "all-posts" target org-dashboard (assoc-in params [:params :board] "all-posts")))

    (defroute drafts-route (urls/drafts ":org") {:as params}
      (timbre/info "Routing board-route" (urls/drafts ":org"))
      (board-handler "dashboard" target org-dashboard (assoc-in params [:params :board] "drafts")))

    (defroute drafts-slash-route (str (urls/drafts ":org") "/") {:as params}
      (timbre/info "Routing board-slash-route" (str (urls/drafts ":org") "/"))
      (board-handler "dashboard" target org-dashboard (assoc-in params [:params :board] "drafts")))

    (defroute must-see-route (urls/must-see ":org") {:as params}
      (timbre/info "Routing must-see-route" (urls/must-see ":org"))
      (org-handler "must-see" target org-dashboard (assoc-in params [:params :board] "must-see")))

    (defroute must-see-slash-route (str (urls/must-see ":org") "/") {:as params}
      (timbre/info "Routing must-see-slash-route" (str (urls/must-see ":org") "/"))
      (org-handler "must-see" target org-dashboard (assoc-in params [:params :board] "must-see")))

    (defroute user-notifications-route urls/user-notifications {:as params}
      (timbre/info "Routing user-notifications-route" urls/user-notifications)
      (pre-routing (:query-params params))
      (router/set-route! ["user-profile"] {:query-params (:query-params params)})
      (post-routing)
      (if (jwt/jwt)
        (router/redirect! (str (utils/your-digest-url) "?user-settings=notifications"))
        (do
          (user-actions/save-login-redirect)
          (router/redirect! urls/login))))

    (defroute user-profile-route urls/user-profile {:as params}
      (timbre/info "Routing user-profile-route" urls/user-profile)
      (pre-routing (:query-params params))
      (router/set-route! ["user-profile"] {:query-params (:query-params params)})
      (post-routing)
      (if (jwt/jwt)
        (router/redirect! (str (utils/your-digest-url) "?user-settings=profile"))
        (do
          (user-actions/save-login-redirect)
          (router/redirect! urls/login))))

    (defroute secure-activity-route (urls/secure-activity ":org" ":secure-id") {:as params}
      (timbre/info "Routing secure-activity-route" (urls/secure-activity ":org" ":secure-id"))
      (secure-activity-handler secure-activity "secure-activity" target params))

    (defroute secure-activity-slash-route (str (urls/secure-activity ":org" ":secure-id") "/") {:as params}
      (timbre/info "Routing secure-activity-slash-route" (str (urls/secure-activity ":org" ":secure-id") "/"))
      (secure-activity-handler secure-activity "secure-activity" target params))

    (defroute board-route (urls/board ":org" ":board") {:as params}
      (timbre/info "Routing board-route" (urls/board ":org" ":board"))
      (board-handler "dashboard" target org-dashboard params))

    (defroute board-slash-route (str (urls/board ":org" ":board") "/") {:as params}
      (timbre/info "Routing board-route-slash" (str (urls/board ":org" ":board") "/"))
      (board-handler "dashboard" target org-dashboard params))

    (defroute entry-route (urls/entry ":org" ":board" ":entry") {:as params}
      (timbre/info "Routing entry-route" (urls/entry ":org" ":board" ":entry"))
      (board-handler "activity" target org-dashboard params))

    (defroute entry-slash-route (str (urls/entry ":org" ":board" ":entry") "/") {:as params}
      (timbre/info "Routing entry-route" (str (urls/entry ":org" ":board" ":entry") "/"))
      (board-handler "activity" target org-dashboard params))

    (defroute not-found-route "*" []
      (timbre/info "Routing not-found-route" "*")
      ;; render component
      (router/redirect-404!))

    (def route-dispatch!
      (secretary/uri-dispatcher [_loading_route
                                 login-route
                                 ;; Signup email
                                 sign-up-slack-route
                                 sign-up-slack-slash-route
                                 signup-profile-route
                                 signup-profile-slash-route
                                 signup-team-route
                                 signup-team-slash-route
                                 signup-update-team-route
                                 signup-update-team-slash-route
                                 signup-setup-sections-route
                                 signup-setup-sections-slash-route
                                 signup-invite-route
                                 signup-invite-slash-route
                                 signup-route
                                 signup-slash-route
                                 ;; Signup slack
                                 slack-lander-check-route
                                 slack-lander-check-slash-route
                                 ;; Signup google
                                 google-lander-check-route
                                 google-lander-check-slash-route
                                 ;; Email wall
                                 email-wall-route
                                 email-wall-slash-route
                                 ;; Marketing site components
                                 about-route
                                 slack-route
                                 pricing-route
                                 press-kit-route
                                 logout-route
                                 email-confirmation-route
                                 confirm-invitation-route
                                 confirm-invitation-password-route
                                 confirm-invitation-profile-route
                                 password-reset-route
                                 ;  ; subscription-callback-route
                                 ;; Home page
                                 home-page-route
                                 user-profile-route
                                 user-notifications-route
                                 ;; Org routes
                                 org-route
                                 org-slash-route
                                 first-ever-all-posts-route
                                 first-ever-all-posts-slash-route
                                 all-posts-route
                                 all-posts-slash-route
                                 ; Drafts board
                                 drafts-route
                                 drafts-slash-route
                                 ; Secure activity route
                                 secure-activity-route
                                 secure-activity-slash-route
                                 ;; Boards
                                 board-route
                                 board-slash-route
                                 ; Entry route
                                 entry-route
                                 entry-slash-route
                                 ;; Not found
                                 not-found-route]))

    (defn handle-url-change [e]
      (when-not @prevent-route-dispatch
        ;; we are checking if this event is due to user action,
        ;; such as click a link, a back button, etc.
        ;; as opposed to programmatically setting the URL with the API
        (when-not (.-isNavigation e)
          ;; in this case, we're setting it so
          ;; let's scroll to the top to simulate a navigation
          (js/window.scrollTo 0 0))
        ;; dispatch on the token
        (route-dispatch! (router/get-token))
        ; remove all the tooltips
        (utils/after 100 #(utils/remove-tooltips)))))
  (do
    (timbre/error "Error: div#app is not defined!")
    (sentry/capture-message "Error: div#app is not defined!")))

(defn init []
  ;; Setup timbre log level
  (logging/config-log-level! (or (:log-level (:query-params @router/path)) ls/log-level))
  ;; Setup API requests
  (api/config-request
   #(ja/update-jwt %) ;; success jwt refresh after expire
   #(ja/logout) ;; failed to refresh jwt
   ;; network error
   #(notification-actions/show-notification (assoc utils/network-error :expire 10)))

  ;; Persist JWT in App State
  (ja/dispatch-jwt)
  (ja/dispatch-id-token)

  ;; Subscribe to websocket client events
  (aa/ws-change-subscribe)
  (sa/ws-change-subscribe)
  (sa/ws-interaction-subscribe)
  (oa/subscribe)
  (ra/subscribe)
  (ca/subscribe)
  (user-actions/subscribe)

  ;; on any click remove all the shown tooltips to make sure they don't get stuck
  (.click (js/$ js/window) #(utils/remove-tooltips))
  ;; setup the router navigation only when handle-url-change and route-disaptch!
  ;; are defined, this is used to avoid crash on tests
  (when (and handle-url-change route-dispatch!)
    (router/setup-navigation! handle-url-change route-dispatch!))
  (ziggeo/init-ziggeo true))

(defn on-js-reload []
  (.clear js/console)
  (route-dispatch! (router/get-token)))