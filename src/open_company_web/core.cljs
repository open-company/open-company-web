(ns ^:figwheel-always open-company-web.core
    (:require [om.core :as om :include-macros true]
              [open-company-web.components.page :refer [page]]
              [open-company-web.raven :refer [raven-setup]]))

(defonce app-state (atom {
  :currency ["USD"]
  :headcount {
    :founders 2,
    :executives 0,
    :ft-employees 3,
    :ft-contractors 0,
    :pt-employees 0,
    :pt-contractors 2,
    :comment "This is a comment."
  },
  :finances {
    :cash 173228,
    :revenue 2767,
    :costs 22184,
    :burn-rate -19417,
    :runway "9 months",
    :comment "This is another comment."
  },
  :compensation {
    :percentage false,
    :founders 6357,
    :executives 0,
    :employees 5899,
    :contractors 2582,
    :comment "More comments for you."
  }
}))

(om/root page app-state
  {:target (. js/document (getElementById "app"))})

;; setup Sentry error reporting
(raven-setup)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
