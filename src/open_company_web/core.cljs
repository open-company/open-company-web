(ns ^:figwheel-always open-company-web.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [open-company-web.router :refer [make-history handle-url-change]]
            [open-company-web.components.page :refer [company company-not-found]]
            [open-company-web.components.list-companies :refer [list-companies]]
            [open-company-web.components.page-not-found :refer [page-not-found]]
            [open-company-web.raven :refer [raven-setup]]
            [goog.events :as events])
  (:import [goog.history EventType]))

;; setup Sentry error reporting
(raven-setup)

(defonce app-state (atom {
  :OPEN {
    :name "Transparency, LLC"
    :symbol "OPEN"
    :currency ["USD"]
    :headcount {
      :founders 2
      :executives 0
      :ft-employees 3
      :ft-contractors 0
      :pt-employees 0
      :pt-contractors 2
      :comment "Transparency headcount comment."
    },
    :finances {
      :cash 173228
      :revenue 2767
      :costs 22184
      :burn-rate -19417
      :runway "9 months"
      :comment "Transparency finances comment."
    },
    :compensation {
      :percentage false
      :founders 6357
      :executives 0
      :employees 5899
      :contractors 2582
      :comment "Transparency compensation comment."
    }
  }
  :BUFFR {
    :name "Buffer"
    :symbol "BUFFR"
    :currency ["USD"]
    :headcount {
      :founders 1
      :executives 2
      :ft-employees 1
      :ft-contractors 2
      :pt-employees 1
      :pt-contractors 4
      :comment "Buffer headcount comment."
    },
    :finances {
      :cash 323232
      :revenue 1234
      :costs 11321
      :burn-rate -10000
      :runway "9 months"
      :comment "Buffer finances comment."
    },
    :compensation {
      :percentage true
      :founders 40
      :executives 40
      :employees 10
      :contractors 10
      :comment "Buffer compensation comment."
    }
  }
}))

;;Routes
(defroute list-page-route "/" []
  (om/root list-companies app-state
    {:target (. js/document (getElementById "app"))}))

(defroute editable-page-route "/companies/:symbol" {ticker :symbol}
  (if-let [company-state ((keyword ticker) @app-state)]
    (om/root company company-state
      {:target (. js/document (getElementById "app"))})
    (om/root company-not-found {:symbol ticker}
      {:target (. js/document (getElementById "app"))})))

(defroute not-found-route "*" []
  (om/root page-not-found app-state
    {:target (. js/document (getElementById "app"))}))

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
