(ns ^:figwheel-always open-company-web.core
  (:require [om.core :as om :include-macros true]
            [secretary.core :as secretary :refer-macros [defroute]]
            [open-company-web.router :as router]
            [open-company-web.components.page :refer [company]]
            [open-company-web.components.list-companies :refer [list-companies]]
            [open-company-web.components.page-not-found :refer [page-not-found]]
            [open-company-web.lib.raven :refer [raven-setup]]
            [open-company-web.dispatcher :refer [app-state]]
            [open-company-web.api :as api]
            [goog.events :as events])
  (:import [goog.history EventType]))

(enable-console-print!)

;; setup Sentry error reporting
(defonce raven (raven-setup))

; Routes - Do not define routes when js/document#app
; is undefined because it breaks tests
(if-let [target (. js/document (getElementById "app"))]
  (do
    (defn home-handler []
      ; save route
      (router/set-route! ["companies"] {})
      ; load data from api
      (api/get-companies)
      ; render component
      (om/root list-companies app-state {:target target}))

    (defroute home-page-route "/" []
      (home-handler))

    (defroute list-page-route "/companies" []
      (home-handler))

    (defroute company-profile-route "/companies/:slug/profile" {{slug :slug} :params}
      ; save route
      (router/set-route! ["companies" slug] {:slug slug})
      ; load data from api
      (api/get-company slug)
      (swap! app-state assoc :loading true)
      ; render compoenent
      (om/root company app-state {:target target}))

    (defroute company-route "/companies/:slug" {{slug :slug} :params}
      ; save route
      (router/set-route! ["companies" slug] {:slug slug})
      ; load data from api
      (api/get-company slug)
      (swap! app-state assoc :loading true)
      ; render compoenent
      (om/root company app-state {:target target}))

    (defn finances-handler [slug section tab]
      ; save route
      (router/set-route! ["companies" slug section tab] {:slug slug :section section :tab tab})
      ; if there are no company data
      (when-not (contains? @app-state (keyword slug))
        ; load data from api
        (swap! app-state assoc :loading true)
        (api/get-company slug))
      ; render component
      (om/root company app-state {:target target}))

    (defn section-handler [slug section]
      ; if there are no company data
      (when-not (contains? @app-state (keyword slug))
        ; load data from api
        (swap! app-state assoc :loading true)
        (api/get-company slug))
      ; render component
      (om/root company app-state {:target target}))

    (defroute section-route "/companies/:slug/:section" {{slug :slug section :section} :params}
      ; save route
      (router/set-route! ["companies" slug section] {:slug slug :section section})
      (section-handler slug section))

    (defroute section-edit-route "/companies/:slug/:section/edit" {{slug :slug section :section} :params}
      ; save route
      (router/set-route! ["companies" slug section "edit"] {:slug slug :section section :tab "edit"})
      (section-handler slug section))

    (defroute not-found-route "*" []
      ; render component
      (om/root page-not-found app-state {:target target}))

    (def dispatch!
      (secretary/uri-dispatcher [home-page-route
                                 list-page-route
                                 company-route
                                 company-profile-route
                                 section-route
                                 section-edit-route
                                 not-found-route]))

    (defn handle-url-change [e]
      ;; we are checking if this event is due to user action,
      ;; such as click a link, a back button, etc.
      ;; as opposed to programmatically setting the URL with the API
      (when-not (.-isNavigation e)
        ;; in this case, we're setting it so
        ;; let's scroll to the top to simulate a navigation
        (js/window.scrollTo 0 0))
      ;; dispatch on the token
      (dispatch! (router/get-token)))))

(defonce history
  (doto (router/make-history)
    (events/listen EventType.NAVIGATE
      ;; wrap in a fn to allow live reloading
      #(handle-url-change %))
    (.setEnabled true)))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (dispatch! (router/get-token))
)
