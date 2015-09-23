(ns ^:figwheel-always open-company-web.core
  (:require [om.core :as om :include-macros true]
            [secretary.core :as secretary :refer-macros [defroute]]
            [open-company-web.router :as router]
            [open-company-web.components.page :refer [company]]
            [open-company-web.components.list-companies :refer [list-companies]]
            [open-company-web.components.page-not-found :refer [page-not-found]]
            [open-company-web.components.report :refer [report readonly-report]]
            [open-company-web.components.finances-component.finances-component :refer [finances finances-edit]]
            [open-company-web.components.challenges-component.challenges :refer [challenges]]
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
    (defroute list-page-route "/companies" []
      ; save route
      (router/set-route! ["companies"] {})
      ; load data from api
      (api/get-companies)
      ; render component
      (om/root list-companies app-state {:target target}))

    (defroute company-profile-route "/companies/:ticker" {{ticker :ticker} :params}
      ; save route
      (router/set-route! ["companies" ticker] {:ticker ticker})
      ; load data from api
      (api/get-company ticker)
      (swap! app-state assoc :loading true)
      ; render compoenent
      (om/root company app-state {:target target}))

    (defroute report-summary-route "/companies/:ticker/summary" {{ticker :ticker} :params}
      ; save route
      (router/set-route! ["companies" ticker "summary"] {:ticker ticker})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/get-company ticker)
      ; render component
      (om/root report app-state {:target target}))

    (defroute report-editable-route "/companies/:ticker/reports/:year/:period/edit" {{ticker :ticker year :year period :period} :params}
      ; save route
      (router/set-route! ["companies" ticker "reports" year period "edit"] {:ticker ticker :year year :period period})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/get-report ticker year period)
      ; render component
      (om/root report app-state {:target target}))

    (defroute report-route "/companies/:ticker/reports/:year/:period" {{ticker :ticker year :year period :period} :params}
      ; save route
      (router/set-route! ["companies" ticker "reports" year period] {:ticker ticker :year year :period period})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/get-report ticker year period)
      ; render component
      (om/root readonly-report app-state {:target target}))

    (defroute finances-route "/finances/:tab" {{tab :tab} :params}
      ; save route
      (router/set-route! ["finances" tab] {:tab tab})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/load-finances)
      ; render component
      (om/root finances app-state {:target target}))

    (defroute finances-edit-route "/finances/edit" {}
      ; save route
      (router/set-route! ["finances" "edit"] {})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/load-finances)
      ; render component
      (om/root finances-edit app-state {:target target}))

    (defroute challenges-route "/challenges" {}
      ; save route
      (router/set-route! ["challenges"] {})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/load-finances)
      ; render component
      (om/root challenges app-state {:target target}))

    (defroute not-found-route "*" []
      ; render component
      (om/root page-not-found app-state {:target target}))

    (def dispatch!
      (secretary/uri-dispatcher [list-page-route
                                 company-profile-route
                                 report-summary-route
                                 report-editable-route
                                 report-route
                                 finances-edit-route
                                 finances-route
                                 challenges-route
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
