(ns open-company-web.core
  (:require [om.core :as om :include-macros true]
            [secretary.core :as secretary :refer-macros (defroute)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.rum-utils :as ru]
            [open-company-web.actions]
            [open-company-web.api :as api]
            [open-company-web.urls :as urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.lib.raven :as sentry]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [open-company-web.components.company-editor :refer (company-editor)]
            [open-company-web.components.company-logo-setup :refer (company-logo-setup)]
            [open-company-web.components.company-dashboard :refer (company-dashboard)]
            [open-company-web.components.company-settings :refer (company-settings)]
            [open-company-web.components.su-edit :refer (su-edit)]
            [open-company-web.components.su-list :refer (su-list)]
            [open-company-web.components.su-snapshot-preview :refer (su-snapshot-preview)]
            [open-company-web.components.su-snapshot :refer (su-snapshot)]
            [open-company-web.components.home :refer (home)]
            [open-company-web.components.list-companies :refer (list-companies)]
            [open-company-web.components.page-not-found :refer (page-not-found)]
            [open-company-web.components.user-profile :refer (user-profile)]
            [open-company-web.components.login :refer (login)]
            [open-company-web.components.ui.loading :refer (loading)]
            [open-company-web.components.sign-up :refer (sign-up)]
            [open-company-web.components.about :refer (about)]
            [open-company-web.components.pricing :refer (pricing)]
            [open-company-web.components.email-confirmation :refer (email-confirmation)]))

(enable-console-print!)

(defn drv-root [om-component target]
  (ru/drv-root {:state dis/app-state
                :drv-spec (dis/drv-spec dis/app-state)
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
  (utils/after 100 #(dis/toggle-menu false))
  (if (jwt/jwt)
    (dommy/add-class! (sel1 [:body]) :small-footer)
    (dommy/remove-class! (sel1 [:body]) :small-footer))
  (check-get-params query-params)
  (inject-loading))

;; home
(defn home-handler [target params]
  (pre-routing (:query-params params))
  ;; clean the caches
  (utils/clean-company-caches)
  ;; save route
  (router/set-route! [] {})
  (when (jwt/jwt)
    (swap! dis/app-state assoc :loading true))
  ;; load data from api
  (api/get-entry-point)
  ;; render component
  (drv-root home target))

;; Company list
(defn list-companies-handler [target params]
  (pre-routing (:query-params params))
  ;; clean the caches
  (utils/clean-company-caches)
  ;; save route
  (router/set-route! ["companies"] {})
  ;; load data from api
  (swap! dis/app-state assoc :loading true)
  (api/get-entry-point)
  (api/get-companies)
  ;; render component
  (drv-root list-companies target))

;; Handle successful and unsuccessful logins
(defn login-handler [target params]
  (pre-routing (:query-params params))
  (utils/clean-company-caches)
  (if (contains? (:query-params params) :jwt)
    (do ; contains :jwt so auth went well
      (cook/set-cookie! :jwt (:jwt (:query-params params)) (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point))
    (do
      (when (contains? (:query-params params) :login-redirect)
        (cook/set-cookie! :login-redirect (:login-redirect (:query-params params)) (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
      ;; save route
      (router/set-route! ["login"] {})
      (when (contains? (:query-params params) :access)
        ;login went bad, add the error message to the app-state
        (swap! dis/app-state assoc :slack-access (:access (:query-params params))))
      ;; render component
      (drv-root login target))))

(defn simple-handle [component route-name target params]
  (pre-routing (:query-params params))
  (utils/clean-company-caches)
  (if (contains? (:query-params params) :jwt)
    (do ; contains :jwt so auth went well
      (cook/set-cookie! :jwt (:jwt (:query-params params)) (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point))
    (do
      (when (contains? (:query-params params) :login-redirect)
        (cook/set-cookie! :login-redirect (:login-redirect (:query-params params)) (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
      ;; save route
      (router/set-route! [route-name] {})
      (when (contains? (:query-params params) :access)
        ;login went bad, add the error message to the app-state
        (swap! dis/app-state assoc :slack-access (:access (:query-params params))))
      ;; render component
      (rum/mount (component) target))))

;; Component specific to a company
(defn company-handler [route target component params]
  (let [slug (:slug (:params params))
        section (:section (:params params))
        edit?   (= route "section-edit")
        query-params (:query-params params)]
    (pre-routing query-params)
    (utils/clean-company-caches)
    ;; save the route
    (router/set-route! [slug section route (when edit? "edit")] {:slug slug :section section :edit edit? :query-params query-params})
    ;; load revision if needed
    (when (:as-of query-params)
      (api/load-revision {:updated-at (:as-of query-params)
                          :href (str "/companies" (urls/company-section-revision (:as-of query-params)))
                          :type (api/content-type "section")}
                         slug
                         section))
    (when (contains? (:query-params params) :access)
        ;login went bad, add the error message to the app-state
        (swap! dis/app-state assoc :slack-access (:access (:query-params params))))
    ;; do we have the company data already?
    (when-not (dis/company-data)
      ;; load the company data from the API
      (api/get-company slug)
      (swap! dis/app-state assoc :loading true))
    ;; render component
    (drv-root component target)))

;; Component specific to a stakeholder update
(defn stakeholder-update-handler [target component params]
  (let [slug (:slug (:params params))
        update-slug (:update-slug (:params params))
        update-date (:update-date (:params params))
        update-section (:section (:params params))
        query-params (:query-params params)
        su-key (dis/stakeholder-update-key slug update-slug)]
    (pre-routing query-params)
    (utils/clean-company-caches)
    ;; save the route
    (router/set-route! [slug "su-snapshot" "updates" update-date update-slug update-section] {:slug slug :update-slug update-slug :update-date update-date :query-params query-params :section update-section})
    ;; do we have the company data already?
    (when (not (get-in @dis/app-state su-key))
      ;; load the Stakeholder Update data from the API
      (api/get-stakeholder-update slug update-slug)
      (let [su-loading-key (conj su-key :loading)]
        (swap! dis/app-state assoc-in su-loading-key true)))
    ;; render component
    (drv-root component target)))

;; Routes - Do not define routes when js/document#app
;; is undefined because it breaks tests
(if-let [target (sel1 :div#app)]
  (do
    (defroute login-route urls/login {:as params}
      (simple-handle sign-up "login" target params))

    (defroute signup-route urls/sign-up {:as params}
      (simple-handle sign-up "sign-up" target params))

    (defroute about-route urls/about {:as params}
      (simple-handle about "about" target params))

    (defroute pricing-route urls/pricing {:as params}
      (simple-handle pricing "pricing" target params))

    (defroute email-confirmation-route urls/email-confirmation {:as params}
      (utils/clean-company-caches)
      (pre-routing (:query-params params))
      (drv-root email-confirmation target))

    (defroute subscription-callback-route urls/subscription-callback {}
      (when-let [s (cook/get-cookie :subscription-callback-slug)]
        (router/redirect! (urls/company-settings s))))

    (defroute home-page-route urls/home {:as params}
      (home-handler target params))

    (defroute company-create-route urls/create-company {:as params}
      (if (jwt/jwt)
        (do
          (pre-routing (:query-params params))
          (drv-root company-editor target))
        (login-handler target params)))

    (defroute company-logo-setup-route (urls/company-logo-setup ":slug") {:as params}
      (let [slug (:slug (:params params))
            query-params (:query-params params)]
        (pre-routing query-params)
        (utils/clean-company-caches)
        ;; save the route
        (router/set-route! [slug "settings" "logo"] {:slug slug :query-params query-params})
        ;; do we have the company data already?
        (when-not (dis/company-data)
          ;; load the company data from the API
          (api/get-company slug)
          (swap! dis/app-state assoc :loading true))
      (drv-root company-logo-setup target)))

    (defroute list-page-route urls/companies {:as params}
      (list-companies-handler target params))

    (defroute list-page-route-slash (str urls/companies "/") {:as params}
      (list-companies-handler target params))

    (defroute user-profile-route urls/user-profile {:as params}
      (utils/clean-company-caches)
      (pre-routing (:query-params params))
      (drv-root user-profile target))

    (defroute company-settings-route (urls/company-settings ":slug") {:as params}
      ; add force-remove-loading to avoid inifinte spinner if the company
      ; has no sections and the user is looking at company profile
      (swap! dis/app-state assoc :force-remove-loading true)
      (company-handler "profile" target company-settings params))

    (defroute company-section-route (urls/company-section ":slug" ":section") {:as params}
      (company-handler "section" target company-dashboard params))

    (defroute company-route (urls/company ":slug") {:as params}
      (company-handler "dashboard" target company-dashboard params))

    (defroute company-route-slash (str (urls/company ":slug") "/") {:as params}
      (company-handler "dashboard" target company-dashboard params))

    (defroute su-snapshot-preview-route (urls/stakeholder-update-preview ":slug") {:as params}
      (company-handler "su-snapshot-preview" target su-snapshot-preview params))

    (defroute su-list-route (urls/stakeholder-update-list ":slug") {:as params}
      (company-handler "su-list" target su-list params))

    (defroute su-edit-route (urls/stakeholder-update-edit ":slug") {:as params}
      (company-handler "su-edit" target su-edit params))

    (defroute stakeholder-update-route (urls/stakeholder-update ":slug" ":update-date" ":update-slug") {:as params}
      (stakeholder-update-handler target su-snapshot params))

    (defroute stakeholder-update-section-route (urls/stakeholder-update-section ":slug" ":update-date" ":update-slug" ":section") {:as params}
      (stakeholder-update-handler target su-snapshot params))

    (defroute not-found-route "*" []
      ;; render component
      (router/redirect-404!))

    (def route-dispatch!
      (secretary/uri-dispatcher [login-route
                                 signup-route
                                 about-route
                                 pricing-route
                                 email-confirmation-route
                                 subscription-callback-route
                                 home-page-route
                                 list-page-route-slash
                                 list-page-route
                                 company-create-route
                                 company-logo-setup-route
                                 user-profile-route
                                 company-settings-route
                                 company-route
                                 company-route-slash
                                 company-section-route
                                 su-snapshot-preview-route
                                 su-edit-route
                                 su-list-route
                                 stakeholder-update-route
                                 stakeholder-update-section-route
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
        (route-dispatch! (router/get-token)))))
  (sentry/capture-message "Error: div#app is not defined!"))

(defn init []
  ;; Persist JWT in App State
  (dis/dispatch! [:jwt (jwt/get-contents)])
  ;; setup the router navigation only when handle-url-change and route-disaptch!
  ;; are defined, this is used to avoid crash on tests
  (when (and handle-url-change route-dispatch!)
    (router/setup-navigation! handle-url-change route-dispatch!)))

(defn on-js-reload []
  (.clear js/console)
  (route-dispatch! (router/get-token)))