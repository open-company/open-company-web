(ns ^:figwheel-always open-company-web.core
  (:require [om.core :as om :include-macros true]
            [secretary.core :refer-macros [defroute]]
            [open-company-web.router :as router]
            [open-company-web.components.page :refer [company]]
            [open-company-web.components.list-companies :refer [list-companies]]
            [open-company-web.components.page-not-found :refer [page-not-found]]
            [open-company-web.components.report :refer [report readonly-report]]
            [open-company-web.lib.raven :refer [raven-setup]]
            [open-company-web.dispatcher :refer [app-state]]
            [open-company-web.api :as api]))

(enable-console-print!)

;; setup Sentry error reporting
(defonce raven (raven-setup))

; Routes - Do not define routes when js/document#app
; is undefined because it crashed the tests
(if-let [target (. js/document (getElementById "app"))]
  (do
    (defroute list-page-route "/companies" []
      ; save route
      (router/set-route! ["companies"] {})
      ; load data from api
      (api/get-companies)
      ; render component
      (om/root list-companies app-state {:target target}))

    (defroute company-profile-route "/:symbol" {symbol :symbol}
      ; save route
      (router/set-route! [symbol] {:symbol symbol})
      ; load data from api
      (api/get-company symbol)
      (swap! app-state assoc :loading true)
      ; render compoenent
      (om/root company app-state {:target target}))

    (defroute report-summary-route "/:symbol/summary" {symbol :symbol}
      ; save route
      (router/set-route! [symbol "summary"] {:symbol symbol})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/get-company symbol)
      ; render component
      (om/root report app-state {:target target}))

    (defroute report-editable-route "/:symbol/:year/:period/edit" {symbol :symbol year :year period :period}
      ; save route
      (router/set-route! [symbol year period "edit"] {:symbol symbol :year year :period period})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/get-report symbol year period)
      ; render component
      (om/root report app-state {:target target}))

    (defroute report-route "/:symbol/:year/:period" {symbol :symbol year :year period :period}
      ; save route
      (router/set-route! [symbol year period] {:symbol symbol :year year :period period})
      ; load data from api
      (swap! app-state assoc :loading true)
      (api/get-report symbol year period)
      ; render component
      (om/root readonly-report app-state {:target target}))

    (defroute not-found-route "*" []
      ; render component
      (om/root page-not-found app-state {:target target}))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)