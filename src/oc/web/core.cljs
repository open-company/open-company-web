(ns oc.web.core
  (:require [om.core :as om :include-macros true]
            [secretary.core :as secretary :refer-macros (defroute)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
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
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [oc.web.components.home :refer (home)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.list-orgs :refer (list-orgs)]
            [oc.web.components.user-management :refer (user-management-wrapper)]
            [oc.web.components.org-dashboard :refer (org-dashboard)]
            [oc.web.components.user-profile :refer (user-profile)]
            [oc.web.components.edit-user-profile :refer (edit-user-profile)]
            [oc.web.components.about :refer (about)]
            [oc.web.components.login :refer (login)]
            [oc.web.components.sign-up :refer (sign-up)]
            [oc.web.components.pricing :refer (pricing)]
            [oc.web.components.org-editor :refer (org-editor)]
            [oc.web.components.board-editor :refer (board-editor)]
            [oc.web.components.confirm-invitation :refer (confirm-invitation)]
            [oc.web.components.org-settings :refer (org-settings)]
            ; [oc.web.components.board-logo-setup :refer (board-logo-setup)]
            ; [oc.web.components.create-update :refer (create-update)]
            ; [oc.web.components.su-snapshot :refer (su-snapshot)]
            ; [oc.web.components.updates :refer (updates-responsive-switcher)]
            ; [oc.web.components.email-confirmation :refer (email-confirmation)]
            ))

(enable-console-print!)

(defn drv-root [om-component target]
  (ru/drv-root {:state dis/app-state
                :drv-spec (dis/drv-spec dis/app-state router/path)
                :component om-component
                :target target}))

;; setup Sentry error reporting
(defonce raven (sentry/raven-setup))

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

(defn pre-routing [query-params]
  ; make sure the menu is closed
  (swap! router/path {})
  (if (jwt/jwt)
    (dommy/add-class! (sel1 [:body]) :small-footer)
    (dommy/remove-class! (sel1 [:body]) :small-footer))
  (check-get-params query-params)
  (inject-loading))

;; home
(defn home-handler [target params]
  (pre-routing (:query-params params))
  ;; save route
  (router/set-route! ["home"] {})
  (when (jwt/jwt)
    ;; load data from api
    (api/get-entry-point)
    (api/get-auth-settings)
    (swap! dis/app-state assoc :loading true))
  ;; render component
  (drv-root home target))

; ;; Orgs list
(defn list-orgs-handler [target params]
  (pre-routing (:query-params params))
  ;; save route
  (router/set-route! ["orgs"] {})
  ;; load data from api
  (swap! dis/app-state assoc :loading true)
  (api/get-entry-point)
  (api/get-auth-settings)
  ;; render component
  (drv-root list-orgs target))

;; Company list
(defn org-handler [target params]
  (let [org (:org (:params params))
        query-params (:query-params params)]
    (pre-routing query-params)
    ;; save route
    (router/set-route! [org "boards"] {:org org})
    ;; load data from api
    (swap! dis/app-state merge {:loading true})
    (api/get-entry-point)
    (api/get-auth-settings)
    ;; render component
    (drv-root #(om/component) target)))

;; Handle successful and unsuccessful logins
(defn login-handler [target params]
  (pre-routing (:query-params params))
  (if (contains? (:query-params params) :jwt)
    (do ; contains :jwt so auth went well
      (cook/set-cookie! :jwt (:jwt (:query-params params)) (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point)
      (api/get-auth-settings))
    (do
      (router/set-route! ["login"] {:query-params (:query-params params)})
      (when (contains? (:query-params params) :login-redirect)
        (cook/set-cookie! :login-redirect (:login-redirect (:query-params params)) (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
      (when (contains? (:query-params params) :access)
        ;login went bad, add the error message to the app-state
        (swap! dis/app-state assoc :slack-access (:access (:query-params params))))
      ;; render component
      (drv-root #(om/component (login)) target))))

(defn simple-handler [component route-name target params]
  (pre-routing (:query-params params))
  (if (contains? (:query-params params) :jwt)
    (do ; contains :jwt so auth went well
      (cook/set-cookie! :jwt (:jwt (:query-params params)) (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point)
      (api/get-auth-settings))
    (do
      (when (contains? (:query-params params) :login-redirect)
        (cook/set-cookie! :login-redirect (:login-redirect (:query-params params)) (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
      ;; save route
      (router/set-route! [route-name] {})
      (when (contains? (:query-params params) :access)
        ;login went bad, add the error message to the app-state
        (swap! dis/app-state assoc :slack-access (:access (:query-params params))))
      ; remove om component if mounted to the same node
      (om/detach-root target)
      ;; render component
      (drv-root #(om/component (component)) target))))

;; Component specific to a company
(defn board-handler [route target component params]
  (let [org (:org (:params params))
        board (:board (:params params))
        topic (:topic (:params params))
        query-params (:query-params params)]
    (cook/set-cookie! (router/last-board-cookie org) board (* 60 60 24 6))
    (pre-routing query-params)
    ;; save the route
    (router/set-route! (vec (remove nil? [org board (when topic topic) route])) {:org org :board board :topic topic :query-params query-params})
    (when (contains? (:query-params params) :access)
        ;login went bad, add the error message to the app-state
        (swap! dis/app-state assoc :slack-access (:access (:query-params params))))
    ;; do we have the company data already?
    (when (or (not (dis/board-data))              ;; if the company data are not present
              (not (:topics (dis/board-data)))) ;; or the topic key is missing that means we have only
                                                    ;; a subset of the company data loaded with a SU
      (api/get-entry-point)
      (api/get-auth-settings)
      (reset! dis/app-state (-> @dis/app-state
                               (assoc :loading true)
                               (dissoc (keyword org)))))
    (if topic
      (reset! dis/app-state (assoc @dis/app-state :selected-topic-view topic))
      (reset! dis/app-state (dissoc @dis/app-state :selected-topic-view)))
    ;; render component
    (drv-root component target)))

;; Component specific to a team settings
(defn team-handler [route target component params]
  (let [team-id (:team-id (:params params))
        query-params (:query-params params)]
    (pre-routing query-params)
    ; (api/get-entry-point)
    (api/get-auth-settings)
    ;; save the route
    (router/set-route! [team-id route] {:team-id team-id :query-params query-params})
    (when (contains? (:query-params params) :access)
        ;login went bad, add the error message to the app-state
        (swap! dis/app-state assoc :slack-access (:access (:query-params params))))
    ;; render component
    (drv-root component target)))

; ;; Component specific to a stakeholder update
; (defn stakeholder-update-handler [target component params]
;   (let [slug (:slug (:params params))
;         update-slug (:update-slug (:params params))
;         update-date (:update-date (:params params))
;         update-topic (:topic (:params params))
;         query-params (:query-params params)
;         su-key (dis/stakeholder-update-key slug update-slug)]
;     (pre-routing query-params)
;     ;; save the route
;     (router/set-route! [slug "su-snapshot" "updates" update-date update-slug update-topic] {:slug slug :update-slug update-slug :update-date update-date :query-params query-params :topic update-topic})
;     ;; do we have the company data already?
;     (when (not (get-in @dis/app-state su-key))
;       ;; load the Stakeholder Update data from the API
;       (api/get-stakeholder-update slug update-slug true)
;       (let [su-loading-key (conj su-key :loading)]
;         (swap! dis/app-state assoc-in su-loading-key true)))
;     ;; render component
;     (drv-root component target)))

;; Routes - Do not define routes when js/document#app
;; is undefined because it breaks tests
(if-let [target (sel1 :div#app)]
  (do
    (defroute login-route urls/login {:as params}
      (timbre/info "Rounting login-route" urls/login)
      (when-not (contains? (:query-params params) :jwt)
        (swap! dis/app-state assoc :show-login-overlay :login-with-slack))
      (simple-handler sign-up "login" target params))

    (defroute signup-route urls/sign-up {:as params}
      (timbre/info "Rounting signup-route" urls/sign-up)
      (swap! dis/app-state assoc :show-login-overlay :signup-with-slack)
      (simple-handler sign-up "sign-up" target params))

    (defroute about-route urls/about {:as params}
      (timbre/info "Rounting about-route" urls/about)
      (simple-handler about "about" target params))

    (defroute pricing-route urls/pricing {:as params}
      (timbre/info "Rounting pricing-route" urls/pricing)
      (simple-handler pricing "pricing" target params))

    ; (defroute email-confirmation-route urls/email-confirmation {:as params}
    ;   (pre-routing (:query-params params))
    ;   (drv-root email-confirmation target))

    (defroute confirm-invitation-route urls/confirm-invitation {:keys [query-params] :as params}
      (timbre/info "Rounting confirm-invitation-route" urls/confirm-invitation)
      (when (jwt/jwt)
        (router/redirect! urls/home))
      (pre-routing query-params)
      (router/set-route! ["confirm-invitation"] {:query-params query-params})
      (drv-root confirm-invitation target))

    ; (defroute subscription-callback-route urls/subscription-callback {}
    ;   (when-let [s (cook/get-cookie :subscription-callback-slug)]
    ;     (router/redirect! (urls/board-settings s))))

    (defroute home-page-route urls/home {:as params}
      (timbre/info "Rounting home-page-route" urls/home)
      (home-handler target params))

    (defroute org-list-route urls/orgs {:as params}
      (timbre/info "Rounting org-list-route" urls/orgs)
      (if (jwt/jwt)
        (list-orgs-handler target params)
        (login-handler target params)))

    (defroute org-create-route urls/create-org {:as params}
      (timbre/info "Rounting org-create-route" urls/create-org)
      (if (jwt/jwt)
        (do
          (pre-routing (:query-params params))
          (router/set-route! ["create-org"] {:query-params (:query-params params)})
          (api/get-entry-point)
          (api/get-auth-settings)
          (drv-root org-editor target))
        (login-handler target params)))

    (defroute board-create-route (urls/create-board ":org") {:as params}
      (timbre/info "Rounting board-create-route" (urls/create-board ":org"))
      (if (jwt/jwt)
        (let [org (:org (:params params))]
          (pre-routing (:query-params params))
          (router/set-route! [org "create-board"] {:org org :query-params (:query-params params)})
          (api/get-entry-point)
          (api/get-auth-settings)
          (drv-root board-editor target))
        (login-handler target params)))

    ; (defroute board-logo-setup-route (urls/board-logo-setup ":org" ":board") {:as params}
    ;   (let [org (:org (:params params))
    ;         board (:board (:params params))
    ;         query-params (:query-params params)]
    ;     (pre-routing query-params)
    ;     ;; save the route
    ;     (router/set-route! [org company "settings" "logo"] {:org org :board board :query-params query-params})
    ;     ;; do we have the company data already?
    ;     (when-not (dis/board-data)
    ;       ;; load the company data from the API
    ;       (api/get-company company)
    ;       (swap! dis/app-state assoc :loading true))
    ;   (drv-root board-logo-setup target)))

    (defroute logout-route urls/logout {:as params}
      (timbre/info "Rounting logout-route" urls/logout)
      (cook/remove-cookie! :jwt)
      (cook/remove-cookie! :login-redirect)
      (router/redirect! urls/home))

    (defroute org-page-route (urls/org ":org") {:as params}
      (timbre/info "Rounting org-page-route" (urls/org ":org"))
      (org-handler target params))

    (defroute user-profile-route urls/user-profile {:as params}
      (timbre/info "Routing user-profile-route" urls/user-profile)
      (when-not (jwt/jwt)
        (router/redirect! urls/home))
      (pre-routing (:query-params params))
      (if (jwt/is-slack-org?)
        (drv-root #(om/component (user-profile)) target)
        (drv-root edit-user-profile target)))

    (defroute org-settings-route (urls/org-settings ":org") {:as params}
      (timbre/info "Routing org-settings-route" (urls/org-settings ":org"))
      ; add force-remove-loading to avoid inifinte spinner if the company
      ; has no topics and the user is looking at company profile
      (swap! dis/app-state assoc :force-remove-loading true)
      (board-handler "org-settings" target org-settings params))

    (defroute team-settings-route (urls/team-settings-um ":team-id") {:as params}
      (timbre/info "Routing team-settings-route" (urls/team-settings-um ":team-id"))
      ; add force-remove-loading to avoid inifinte spinner if the company
      ; has no topics and the user is looking at company profile
      (swap! dis/app-state assoc :force-remove-loading true)
      (team-handler "user-management" target user-management-wrapper params))

    (defroute boards-list-route (urls/boards ":org") {:as params}
      (timbre/info "Routing boards-list-route" (urls/boards ":org"))
      (swap! dis/app-state assoc :loading true)
      (api/get-entry-point)
      (api/get-auth-settings)
      (router/set-route! [(:org (:params params)) "boards-list"] {:org (:org (:params params))})
      (drv-root #(om/component) target))

    (defroute board-route (urls/board ":org" ":board") {:as params}
      (timbre/info "Routing board-route" (urls/board ":org" ":board"))
      (board-handler "dashboard" target org-dashboard params))

    (defroute board-route-slash (str (urls/board ":org" ":board") "/") {:as params}
      (timbre/info "Routing board-route-slash" (str (urls/board ":org" ":board") "/"))
      (board-handler "dashboard" target org-dashboard params))

    ; (defroute create-update-route (urls/stakeholder-update-preview ":slug") {:as params}
    ;   (board-handler "su-snapshot-preview" target create-update params))

    ; (defroute su-list-route (urls/stakeholder-update-list ":slug") {:as params}
    ;   (board-handler "su-list" target updates-responsive-switcher params))

    ; (defroute su-list-update-route (urls/stakeholder-update-list ":slug" ":update-slug") {:as params}
    ;   (if (responsive/is-mobile-size?)
    ;     (stakeholder-update-handler target su-snapshot params)
    ;     (board-handler "su-list" target updates-responsive-switcher params)))

    ; (defroute stakeholder-update-route (urls/stakeholder-update ":slug" ":update-date" ":update-slug") {:as params}
    ;   (stakeholder-update-handler target su-snapshot params))

    (defroute topic-route (urls/topic ":org" ":board" ":topic") {:as params}
      (timbre/info "Routing topic-route" (urls/topic ":org" ":board" ":topic"))
      (board-handler "topic" target org-dashboard params))

    (defroute not-found-route "*" []
      (timbre/info "Routing not-found-route" "*")
      ;; render component
      (router/redirect-404!))

    (def route-dispatch!
      (secretary/uri-dispatcher [
                                 login-route
                                 signup-route
                                 about-route
                                 pricing-route
                                 logout-route
                                 org-create-route
                                 ;  email-confirmation-route
                                 confirm-invitation-route
                                 ;  ; subscription-callback-route
                                 home-page-route
                                 user-profile-route
                                 org-list-route
                                 org-page-route
                                 board-create-route
                                 ; board-logo-setup-route
                                 org-settings-route
                                 team-settings-route
                                 boards-list-route
                                 board-route
                                 board-route-slash
                                 ; create-update-route
                                 ; su-list-route
                                 ; su-list-update-route
                                 topic-route
                                 ; stakeholder-update-route
                                 not-found-route
                                 ]))

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

(defn config-log-level! [level]
  (timbre/merge-config! {:level (keyword level)}))

(set! (.-OCWebConfigLogLevel js/window) config-log-level!)

(defn init []
  ;; Setup timbre log level
  (config-log-level! ls/log-level)
  ;; Persist JWT in App State
  (dis/dispatch! [:jwt (jwt/get-contents)])
  ;; on any click remove all the shown tooltips to make sure they don't get stuck
  (.click (js/$ js/window) #(utils/remove-tooltips))
  ;; setup the router navigation only when handle-url-change and route-disaptch!
  ;; are defined, this is used to avoid crash on tests
  (when (and handle-url-change route-dispatch!)
    (router/setup-navigation! handle-url-change route-dispatch!)))

(defn on-js-reload []
  ; (.clear js/console)
  (route-dispatch! (router/get-token)))