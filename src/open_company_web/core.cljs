(ns ^:figwheel-always open-company-web.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [cljs-flux.dispatcher :as flux]
            [open-company-web.router :as router :refer [make-history handle-url-change]]
            [open-company-web.components.page :refer [company]]
            [open-company-web.components.list-companies :refer [list-companies]]
            [open-company-web.components.page-not-found :refer [page-not-found]]
            [open-company-web.components.report :refer [report readonly-report]]
            [open-company-web.lib.raven :refer [raven-setup]]
            [open-company-web.dispatcher :as dispatcher :refer [app-state]]
            [open-company-web.api :as api]
            [goog.events :as events])
  (:import [goog.history EventType])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

;; setup Sentry error reporting
(defonce raven (raven-setup))

(defn render-company [symbol loading target]
  (swap! app-state assoc :symbol symbol)
  (when loading
    (swap! app-state assoc :loading true))
  (om/root company app-state target))

; Routes - Do not define routes if js/document#app
; is not defined because we are in the tests
(if-let [target (. js/document (getElementById "app"))]
  (do
    (defroute list-page-route "/companies" []
      (router/save-route ["companies"] {})
      (api/get-companies)
      (om/root list-companies app-state {:target target}))

    (defroute editable-page-route "/:symbol" {symbol :symbol}
      ; save route
      (router/save-route [symbol] {:symbol symbol})
      (if-not (contains? app-state (keyword symbol))
        (do
          (api/get-company symbol)
          (render-company symbol true {:target target}))
        (render-company symbol false {:target target})))

    (defroute report-editable-route "/:symbol/:year/:period/edit" {symbol :symbol year :year period :period}
      ; save route
      (router/save-route [symbol year period "edit"] {:symbol symbol :year year :period period})

      (swap! app-state assoc :loading true)
      (api/get-report symbol year period)
      (swap! app-state assoc :symbol symbol)
      (swap! app-state assoc :year year)
      (swap! app-state assoc :period period)
      (om/root report app-state {:target target}))

    (defroute report-route "/:symbol/:year/:period" {symbol :symbol year :year period :period}
      ; save route
      (router/save-route [symbol year period] {:symbol symbol :year year :period period})

      (swap! app-state assoc :loading true)
      (api/get-report symbol year period)
      (swap! app-state assoc :symbol symbol)
      (swap! app-state assoc :year year)
      (swap! app-state assoc :period period)
      (om/root readonly-report app-state {:target target}))

    (defroute not-found-route "*" []
      (om/root page-not-found app-state {:target target}))))

(defonce history
  (doto (make-history)
    (goog.events/listen EventType.NAVIGATE
      ;; wrap in a fn to allow live reloading
      #(handle-url-change %))
    (.setEnabled true)))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
