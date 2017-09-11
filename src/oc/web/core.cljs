(ns oc.web.core
  (:require [om.core :as om :include-macros true]
            [secretary.core :as secretary :refer-macros (defroute)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.lib.medium-editor-exts]
            [oc.web.rum-utils :as ru]
            [oc.web.actions]
            [oc.web.api :as api]
            [oc.web.urls :as urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.raven :as sentry]
            [oc.web.lib.logging :as logging]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [oc.web.components.home :refer (home)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.list-orgs :refer (list-orgs)]
            [oc.web.components.org-dashboard :refer (org-dashboard)]
            [oc.web.components.user-profile :refer (user-profile)]
            [oc.web.components.about :refer (about)]
            [oc.web.components.login :refer (login)]
            [oc.web.components.oc-wall :refer (oc-wall)]
            [oc.web.components.home-page :refer (home-page)]
            [oc.web.components.pricing :refer (pricing)]
            [oc.web.components.features :refer (features)]
            [oc.web.components.org-editor :refer (org-editor)]
            [oc.web.components.confirm-invitation :refer (confirm-invitation)]
            [oc.web.components.org-settings :refer (org-settings)]
            [oc.web.components.mobile-boards-list :refer (mobile-boards-list)]
            [oc.web.components.email-confirmation :refer (email-confirmation)]
            [oc.web.components.password-reset :refer (password-reset)]
            [oc.web.components.board-settings :refer (board-settings)]
            [oc.web.components.error-banner :refer (error-banner)]
            [oc.web.components.story :refer (story)]
            [oc.web.components.story-edit :refer (story-edit)]
            [oc.web.components.ui.onboard-wrapper :refer (onboard-wrapper)]))

(enable-console-print!)

(defn drv-root [om-component target]
  (swap! dis/app-state assoc :router-path @router/path)
  (ru/drv-root {:state dis/app-state
                :drv-spec (dis/drv-spec dis/app-state router/path)
                :component om-component
                :target target}))

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

(defn rewrite-url []
  (let [l (.-location js/window)
        rewrite-to (str (.-pathname l) (.-hash l))]
    ;; Push state only if the query string has parameters or the history will have duplicates.
    (when-not (empty? (.-search l))
      (.pushState (.-history js/window) #js {} (.-title js/document) rewrite-to))))

(defn pre-routing [query-params & [should-rewrite-url]]
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
    (cook/set-cookie! :jwt (:jwt query-params) (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
  (check-get-params query-params)
  (when should-rewrite-url
    (rewrite-url))
  (inject-loading))

(defn post-routing []
  (api/get-entry-point)
  (api/get-auth-settings))

;; home
(defn home-handler [target params]
  (pre-routing (:query-params params) true)
  ;; save route
  (router/set-route! ["home"] {:query-params (:query-params params)})
  (when (jwt/jwt)
    ;; load data from api
    (swap! dis/app-state assoc :loading true))
  (post-routing)
  ;; render component
  (drv-root home target))

; ;; Orgs list
(defn list-orgs-handler [target params]
  (pre-routing (:query-params params))
  ;; save route
  (router/set-route! ["orgs"] {:query-params (:query-params params)})
  ;; load data from api
  (swap! dis/app-state assoc :loading true)
  (post-routing)
  ;; render component
  (drv-root list-orgs target))

;; Company list
(defn org-handler [route target component params]
  (let [org (:org (:params params))
        board (:board (:params params))
        query-params (:query-params params)]
    (when org
      (cook/set-cookie! (router/last-org-cookie) org (* 60 60 24 6)))
    (when (= route "all-activity")
      (cook/set-cookie! (router/last-board-cookie org) "all-activity" (* 60 60 24 6)))
    (pre-routing query-params)
    ;; save route
    (router/set-route! [org route] {:org org :board board :query-params (:query-params params)})
    ;; load data from api
    (when-not (dis/org-data)
      (swap! dis/app-state merge {:loading true}))
    (post-routing)
    ;; render component
    (drv-root component target)))

(defn oc-wall-handler [message target params]
  (pre-routing (:query-params params))
  (post-routing)
  (drv-root #(om/component (oc-wall message :login)) target))

;; Handle successful and unsuccessful logins
(defn login-handler [target params]
  (pre-routing (:query-params params))
  (when-not (contains? (:query-params params) :jwt)
    (router/set-route! ["login"] {:query-params (:query-params params)})
    (when (contains? (:query-params params) :login-redirect)
      (cook/set-cookie! :login-redirect (:login-redirect (:query-params params)) (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    ;; render component
    (post-routing)
    (drv-root #(om/component (login)) target)))

(defn simple-handler [component route-name target params]
  (pre-routing (:query-params params))
  ;; save route
  (router/set-route! [route-name] {:query-params (:query-params params)})
  (post-routing)
  (when-not (contains? (:query-params params) :jwt)
    (when (contains? (:query-params params) :login-redirect)
      (cook/set-cookie! :login-redirect (:login-redirect (:query-params params)) (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    ; remove om component if mounted to the same node
    (om/detach-root target)
    ;; render component
    (drv-root #(om/component (component)) target)))

;; Component specific to a board
(defn board-handler [route target component params & [board-sort-or-filter]]
  (let [org (:org (:params params))
        board (:board (:params params))
        entry (:entry (:params params))
        query-params (:query-params params)]
    (when org
      (cook/set-cookie! (router/last-org-cookie) org (* 60 60 24 6)))
    (when board
      (cook/set-cookie! (router/last-board-cookie org) board (* 60 60 24 6)))
    (pre-routing query-params)
    ;; save the route
    (router/set-route! (vec (remove nil? [org board (when entry entry) route])) {:org org :board board :activity entry :query-params query-params})
    (when board-sort-or-filter
      (swap! dis/app-state assoc :board-filters board-sort-or-filter)
      (when (keyword? board-sort-or-filter)
        (cook/set-cookie! (router/last-board-filter-cookie org board) (name board-sort-or-filter) (* 60 60 24 30) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)))
    ;; do we have the company data already?
    (when (or (not (dis/board-data))              ;; if the company data are not present
              (not (:entries (dis/board-data)))) ;; or the entries key is missing that means we have only
                                                    ;; a subset of the company data loaded with a SU
      (swap! dis/app-state merge {:loading true}))
    (post-routing)
    ;; render component
    (drv-root component target)))

;; Component specific to a storyboard
(defn storyboard-handler [route target component params]
  (let [org (:org (:params params))
        storyboard (:storyboard (:params params))
        story (:story (:params params))
        query-params (:query-params params)]
    (when org
      (cook/set-cookie! (router/last-org-cookie) org (* 60 60 24 6)))
    (pre-routing query-params)
    ;; save the route
    (router/set-route! (vec (remove nil? [org storyboard (when story story) route])) {:org org :board storyboard :activity story :query-params query-params})
    ;; do we have the company data already?
    (when (or (not (dis/board-data))              ;; if the company data are not present
              (not (:stories (dis/board-data)))) ;; or the entries key is missing that means we have only
                                                    ;; a subset of the company data loaded with a SU
      (swap! dis/app-state merge {:loading true}))
    (post-routing)
    ;; render component
    (drv-root component target)))

;; Component specific to a storyboard
(defn story-handler [component route target params]
  (let [org (:org (:params params))
        storyboard (:storyboard (:params params))
        story (:story (:params params))
        secure-id (:secure-id (:params params))
        query-params (:query-params params)]
    (pre-routing query-params)
    ;; save the route
    (router/set-route! (vec (remove nil? [org storyboard route (when story story) secure-id])) {:org org :board storyboard :activity story :secure-id secure-id :query-params query-params})
    ;; do we have the company data already?
    (when (or (not (dis/board-data))              ;; if the company data are not present
              (not (:stories (dis/board-data)))) ;; or the entries key is missing that means we have only
                                                    ;; a subset of the company data loaded with a SU
      (swap! dis/app-state merge {:loading true}))
    (post-routing)
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
  (let [jwt (jwt/get-contents)
        slack-profile-cookie (router/slack-profile-filled-cookie (jwt/get-key :user-id))
        slack-profile-cookie-value (cook/get-cookie slack-profile-cookie)]
    (if slack-profile-cookie-value
      (dis/dispatch! [:entry-point-get {:slack-lander-check-team-redirect true}])
      (utils/after 100 #(router/nav! urls/slack-lander)))))

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
        (swap! dis/app-state assoc :show-login-overlay :login-with-slack))
      (simple-handler home-page "login" target params))

    (defroute signup-route urls/sign-up {:as params}
      (timbre/info "Routing signup-route" urls/sign-up)
      (simple-handler #(onboard-wrapper :email-lander) "sign-up" target params))

    (defroute signup-slash-route (str urls/sign-up "/") {:as params}
      (timbre/info "Routing signup-slash-route" (str urls/sign-up "/"))
      (simple-handler #(onboard-wrapper :email-lander) "sign-up" target params))

    (defroute signup-profile-route urls/sign-up-profile {:as params}
      (timbre/info "Routing signup-profile-route" urls/sign-up-profile)
      (simple-handler #(onboard-wrapper :email-lander-profile) "sign-up" target params))

    (defroute signup-profile-slash-route (str urls/sign-up-profile "/") {:as params}
      (timbre/info "Routing signup-profile-slash-route" (str urls/sign-up-profile "/"))
      (simple-handler #(onboard-wrapper :email-lander-profile) "sign-up" target params))

    (defroute signup-team-route urls/sign-up-team {:as params}
      (timbre/info "Routing signup-team-route" urls/sign-up-team)
      (simple-handler #(onboard-wrapper :email-lander-team) "sign-up" target params))

    (defroute signup-team-slash-route (str urls/sign-up-team "/") {:as params}
      (timbre/info "Routing signup-team-slash-route" (str urls/sign-up-team "/"))
      (simple-handler #(onboard-wrapper :email-lander-team) "sign-up" target params))

    (defroute slack-lander-route urls/slack-lander {:as params}
      (timbre/info "Routing slack-lander-route" urls/slack-lander)
      (simple-handler #(onboard-wrapper :slack-lander) "sign-up" target params))

    (defroute slack-lander-slash-route (str urls/slack-lander "/") {:as params}
      (timbre/info "Routing slack-lander-slash-route" (str urls/slack-lander "/"))
      (simple-handler #(onboard-wrapper :slack-lander) "sign-up" target params))

    (defroute slack-lander-team-route urls/slack-lander-team {:as params}
      (timbre/info "Routing slack-lander-team-route" urls/slack-lander-team)
      (simple-handler #(onboard-wrapper :slack-lander-team) "sign-up" target params))

    (defroute slack-lander-team-slash-route (str urls/slack-lander-team "/") {:as params}
      (timbre/info "Routing slack-lander-team-slash-route" (str urls/slack-lander-team "/"))
      (simple-handler #(onboard-wrapper :slack-lander-team) "sign-up" target params))

    (defroute slack-lander-check-route urls/slack-lander-check {:as params}
      (timbre/info "Routing slack-lander-check-route" urls/slack-lander-check)
      ;; Check if the user already have filled the needed data or if it needs to
      (slack-lander-check params))

    (defroute slack-lander-check-slash-route (str urls/slack-lander-check "/") {:as params}
      (timbre/info "Routing slack-lander-check-slash-route" (str urls/slack-lander-check "/"))
      ;; Check if the user already have filled the needed data or if it needs to
      (slack-lander-check params))

    (defroute about-route urls/about {:as params}
      (timbre/info "Routing about-route" urls/about)
      (simple-handler about "about" target params))

    (defroute features-route urls/features {:as params}
      (timbre/info "Routing features-route" urls/features)
      (simple-handler features "features" target params))

    (defroute pricing-route urls/pricing {:as params}
      (timbre/info "Routing pricing-route" urls/pricing)
      (simple-handler pricing "pricing" target params))

    (defroute email-confirmation-route urls/email-confirmation {:as params}
      (cook/remove-cookie! :jwt)
      (cook/remove-cookie! :login-redirect)
      (cook/remove-cookie! :show-login-overlay)
      (timbre/info "Routing email-confirmation-route" urls/email-confirmation)
      (simple-handler #(onboard-wrapper :email-verified) "email-verification" target params))

    (defroute password-reset-route urls/password-reset {:as params}
      (timbre/info "Routing password-reset-route" urls/password-reset)
      (pre-routing (:query-params params))
      (router/set-route! ["password-reset"] {:query-params (:query-params params)})
      (post-routing)
      (drv-root password-reset target))

    (defroute confirm-invitation-route urls/confirm-invitation {:keys [query-params] :as params}
      (timbre/info "Routing confirm-invitation-route" urls/confirm-invitation)
      (when (jwt/jwt)
        (router/redirect! urls/home))
      (pre-routing query-params)
      (router/set-route! ["confirm-invitation"] {:query-params query-params})
      (post-routing)
      (drv-root confirm-invitation target))

    ; (defroute subscription-callback-route urls/subscription-callback {}
    ;   (when-let [s (cook/get-cookie :subscription-callback-slug)]
    ;     (router/redirect! (urls/org-settings s))))

    (defroute email-wall-route urls/email-wall {:keys [query-params] :as params}
      (timbre/info "Routing email-wall-route" urls/email-wall)
      ;; Email wall is shown only to not logged in users
      (when (jwt/jwt)
        (router/redirect! urls/home))
      (pre-routing query-params true)
      (router/set-route! ["email-wall"] {:query-params query-params})
      (post-routing)
      (drv-root #(om/component (onboard-wrapper :email-wall)) target))

    (defroute email-wall-slash-route (str urls/email-wall "/") {:keys [query-params] :as params}
      (timbre/info "Routing email-wall-slash-route" (str urls/email-wall "/"))
      (when (jwt/jwt)
        (router/redirect! urls/home))
      (pre-routing query-params true)
      (router/set-route! ["email-wall"] {:query-params query-params})
      (post-routing)
      (drv-root #(om/component (onboard-wrapper :email-wall)) target))

    (defroute home-page-route urls/home {:as params}
      (timbre/info "Routing home-page-route" urls/home)
      (home-handler target params))

    (defroute org-list-route urls/orgs {:as params}
      (timbre/info "Routing org-list-route" urls/orgs)
      (if (jwt/jwt)
        (list-orgs-handler target params)
        (oc-wall-handler "Please sign in to access this organization." target params)))

    (defroute org-create-route urls/create-org {:as params}
      (timbre/info "Routing org-create-route" urls/create-org)
      (if (jwt/jwt)
        (do
          (pre-routing (:query-params params))
          (router/set-route! ["create-org"] {:query-params (:query-params params)})
          (api/get-entry-point)
          (api/get-auth-settings)
          (post-routing)
          (drv-root org-editor target))
        (oc-wall-handler "Please sign in." target params)))

    (defroute logout-route urls/logout {:as params}
      (timbre/info "Routing logout-route" urls/logout)
      (cook/remove-cookie! :jwt)
      (cook/remove-cookie! :login-redirect)
      (cook/remove-cookie! :show-login-overlay)
      (router/redirect! urls/home))

    (defroute org-route (urls/org ":org") {:as params}
      (timbre/info "Routing org-route" (urls/org ":org"))
      (org-handler "org" target #(om/component) params))

    (defroute org-slash-route (str (urls/org ":org") "/") {:as params}
      (timbre/info "Routing org-slash-route" (str (urls/org ":org") "/"))
      (org-handler "org" target #(om/component) params))

    (defroute all-activity-route (urls/all-activity ":org") {:as params}
      (timbre/info "Routing all-activity-route" (urls/all-activity ":org"))
      (org-handler "all-activity" target org-dashboard (assoc-in params [:params :board] "all-activity")))

    (defroute all-activity-slash-route (str (urls/all-activity ":org") "/") {:as params}
      (timbre/info "Routing all-activity-slash-route" (str (urls/all-activity ":org") "/"))
      (org-handler "all-activity" target org-dashboard (assoc-in params [:params :board] "all-activity")))

    (defroute user-profile-route urls/user-profile {:as params}
      (timbre/info "Routing user-profile-route" urls/user-profile)
      (pre-routing (:query-params params))
      (router/set-route! ["user-profile"] {:query-params (:query-params params)})
      (post-routing)
      (if (jwt/jwt)
        (drv-root #(om/component (user-profile)) target)
        (oc-wall-handler "Please sign in to access this page." target params)))

    (defroute org-settings-route (urls/org-settings ":org") {:as params}
      (timbre/info "Routing org-settings-route" (urls/org-settings ":org"))
      (org-handler "org-settings" target #(om/component (org-settings)) params))

    (defroute org-settings-slash-route (str (urls/org-settings ":org") "/") {:as params}
      (timbre/info "Routing org-settings-slash-route" (str (urls/org-settings ":org") "/"))
      (org-handler "org-settings" target #(om/component (org-settings)) params))

    (defroute org-settings-team-route (urls/org-settings-team ":org") {:as params}
      (timbre/info "Routing org-settings-team-route" (urls/org-settings-team ":org"))
      (team-handler "org-settings-team" target #(om/component (org-settings)) params))

    (defroute org-settings-team-slash-route (str (urls/org-settings-team ":org") "/") {:as params}
      (timbre/info "Routing org-settings-team-slash-route" (str (urls/org-settings-team ":org") "/"))
      (team-handler "org-settings-team" target #(om/component (org-settings)) params))

    (defroute org-settings-invite-route (urls/org-settings-invite ":org") {:as params}
      (timbre/info "Routing org-settings-invite-route" (urls/org-settings-invite ":org"))
      (team-handler "org-settings-invite" target #(om/component (org-settings)) params))

    (defroute org-settings-invite-slash-route (str (urls/org-settings-invite ":org") "/") {:as params}
      (timbre/info "Routing org-settings-invite-slash-route" (str (urls/org-settings-invite ":org") "/"))
      (team-handler "org-settings-invite" target #(om/component (org-settings)) params))

    (defroute secure-story-route (urls/secure-story ":org" ":secure-id") {:as params}
      (timbre/info "Routing secure-story-route" (urls/secure-story ":org" ":secure-id"))
      (story-handler #(om/component (story)) "secure-story" target (assoc-in params [:params :storyboard] "secure-stories")))

    (defroute secure-story-slash-route (str (urls/secure-story ":org" ":secure-id") "/") {:as params}
      (timbre/info "Routing secure-story-slash-route" (str (urls/secure-story ":org" ":secure-id") "/"))
      (story-handler #(om/component (story)) "secure-story" target (assoc-in params [:params :storyboard] "secure-stories")))

    (defroute boards-list-route (urls/boards ":org") {:as params}
      (timbre/info "Routing boards-list-route" (urls/boards ":org"))
      (swap! dis/app-state assoc :loading true)
      (if (responsive/is-mobile-size?)
        (do
          (pre-routing (:query-params params))
          (router/set-route! [(:org (:params params)) "boards-list"] {:org (:org (:params params)) :query-params (:query-params params)})
          (post-routing)
          (drv-root #(om/component (mobile-boards-list)) target))
        (org-handler "boards-list" target #(om/component) params)))

    (defroute board-route (urls/board ":org" ":board") {:as params}
      (timbre/info "Routing board-route" (urls/board ":org" ":board"))
      (board-handler "dashboard" target org-dashboard params (or (keyword (cook/get-cookie (router/last-board-filter-cookie (:org (:params params)) (:board (:params params))))) :latest)))

    (defroute board-slash-route (str (urls/board ":org" ":board") "/") {:as params}
      (timbre/info "Routing board-route-slash" (str (urls/board ":org" ":board") "/"))
      (board-handler "dashboard" target org-dashboard params (or (keyword (cook/get-cookie (router/last-board-filter-cookie (:org (:params params)) (:board (:params params))))) :latest)))

    (defroute board-settings-route (urls/board-settings ":org" ":board") {:as params}
      (timbre/info "Routing board-settings-route" (urls/board-settings ":org" ":board"))
      (if (jwt/jwt)
        (board-handler "board-settings" target board-settings params)
        (oc-wall-handler "Please sign in to access this board." target params)))

    (defroute board-sort-by-topic-route (urls/board-sort-by-topic ":org" ":board") {:as params}
      (timbre/info "Routing board-sort-by-topic-route" (urls/board-sort-by-topic ":org" ":board"))
      (when (= (keyword (cook/get-cookie (router/last-board-filter-cookie (:org (:params params)) (:board (:params params))))) :latest)
        (router/redirect! (urls/board (:org (:params params)) (:board (:params params)))))
      (board-handler "dashboard" target org-dashboard params :by-topic))

    (defroute board-sort-by-topic-slash-route (str (urls/board-sort-by-topic ":org" ":board") "/") {:as params}
      (timbre/info "Routing board-sort-by-topic-slash-route" (str (urls/board-sort-by-topic ":org" ":board") "/"))
      (when (= (keyword (cook/get-cookie (router/last-board-filter-cookie (:org (:params params)) (:board (:params params))))) :latest)
        (router/redirect! (urls/board (:org (:params params)) (:board (:params params)))))
      (board-handler "dashboard" target org-dashboard params :by-topic))

    (defroute board-filter-by-topic-route (urls/board-filter-by-topic ":org" ":board" ":topic-filter") {:as params}
      (timbre/info "Routing board-filter-by-topic-route" (urls/board-filter-by-topic ":org" ":board" ":topic-filter"))
      (board-handler "dashboard" target org-dashboard params (:topic-filter (:params params))))

    (defroute board-filter-by-topic-slash-route (str (urls/board-filter-by-topic ":org" ":board" ":topic-filter") "/") {:as params}
      (timbre/info "Routing board-filter-by-topic-slash-route" (str (urls/board-filter-by-topic ":org" ":board" ":topic-filter") "/"))
      (board-handler "dashboard" target org-dashboard params (:topic-filter (:params params))))

    (defroute entry-route (urls/entry ":org" ":board" ":entry") {:as params}
      (timbre/info "Routing entry-route" (urls/entry ":org" ":board" ":entry"))
      (board-handler "activity" target org-dashboard params))

    (defroute entry-slash-route (str (urls/entry ":org" ":board" ":entry") "/") {:as params}
      (timbre/info "Routing entry-route" (str (urls/entry ":org" ":board" ":entry") "/"))
      (board-handler "activity" target org-dashboard params))

    (defroute story-route (urls/story ":org" ":storyboard" ":story") {:as params}
      (timbre/info "Routing story-route" (urls/story ":org" ":storyboard" ":story"))
      (story-handler #(om/component (story)) "story" target params))

    (defroute story-slash-route (str (urls/story ":org" ":storyboard" ":story") "/") {:as params}
      (timbre/info "Routing story-slash-route" (str (urls/story ":org" ":storyboard" ":story") "/"))
      (story-handler #(om/component (story)) "story" target params))

    (defroute story-edit-route (urls/story-edit ":org" ":storyboard" ":story") {:as params}
      (timbre/info "Routing story-edit-route" (urls/story-edit ":org" ":storyboard" ":story"))
      (story-handler #(om/component (story-edit)) "story-edit" target params))

    (defroute story-edit-slash-route (str (urls/story-edit ":org" ":storyboard" ":story") "/") {:as params}
      (timbre/info "Routing story-edit-slash-route" (str (urls/story-edit ":org" ":storyboard" ":story") "/"))
      (story-handler #(om/component (story-edit)) "story-edit" target params))

    (defroute not-found-route "*" []
      (timbre/info "Routing not-found-route" "*")
      ;; render component
      (router/redirect-404!))

    (def route-dispatch!
      (secretary/uri-dispatcher [_loading_route
                                 login-route
                                 ;; Signup email
                                 signup-profile-route
                                 signup-profile-slash-route
                                 signup-team-route
                                 signup-team-slash-route
                                 signup-route
                                 signup-slash-route
                                 ;; Signup slack
                                 slack-lander-check-route
                                 slack-lander-check-slash-route
                                 slack-lander-team-route
                                 slack-lander-team-slash-route
                                 slack-lander-route
                                 slack-lander-slash-route
                                 ;; Email wall
                                 email-wall-route
                                 email-wall-slash-route
                                 ;; Marketing site components
                                 about-route
                                 features-route
                                 pricing-route
                                 logout-route
                                 org-create-route
                                 email-confirmation-route
                                 confirm-invitation-route
                                 password-reset-route
                                 ;  ; subscription-callback-route
                                 ;; Home page
                                 home-page-route
                                 user-profile-route
                                 ;; Org routes
                                 org-list-route
                                 org-route
                                 org-slash-route
                                 all-activity-route
                                 all-activity-slash-route
                                 org-settings-route
                                 org-settings-slash-route
                                 org-settings-team-route
                                 org-settings-team-slash-route
                                 org-settings-invite-route
                                 org-settings-invite-slash-route
                                 ; Secure story route
                                 secure-story-route
                                 secure-story-slash-route
                                 ;; Boards
                                 boards-list-route
                                 board-route
                                 board-slash-route
                                 ; Board settings
                                 board-settings-route
                                 ; ;; Board sorting
                                 board-sort-by-topic-route
                                 board-sort-by-topic-slash-route
                                 ; Entry route
                                 entry-route
                                 entry-slash-route
                                 ; Story route
                                 story-route
                                 story-slash-route
                                 ; Story edit
                                 story-edit-route
                                 story-edit-slash-route
                                 ; ;; Board filter
                                 board-filter-by-topic-route
                                 board-filter-by-topic-slash-route
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
  ;; Persist JWT in App State
  (dis/dispatch! [:jwt (jwt/get-contents)])
  ;; on any click remove all the shown tooltips to make sure they don't get stuck
  (.click (js/$ js/window) #(utils/remove-tooltips))
  ; mount the error banner
  (drv-root #(om/component (error-banner)) (sel1 [:div#oc-error-banner]))
  ;; setup the router navigation only when handle-url-change and route-disaptch!
  ;; are defined, this is used to avoid crash on tests
  (when (and handle-url-change route-dispatch!)
    (router/setup-navigation! handle-url-change route-dispatch!)))

(defn on-js-reload []
  (.clear js/console)
  (route-dispatch! (router/get-token)))