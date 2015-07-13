(ns ^:figwheel-always open-company-web.core
    (:require [om.core :as om :include-macros true]
              [om-tools.dom :as dom :include-macros true]
              [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
              [open-company-web.router]
              [open-company-web.components.page :refer [page]]
              [open-company-web.components.list-companies :refer [list-companies]]
              [open-company-web.components.page-not-found :refer [page-not-found]]))

(defonce app-state (atom {
  :open-company {
    :name "Open company"
    :id "open-company"
    :currency ["USD"]
    :headcount {
      :founders 2
      :executives 0
      :ft-employees 3
      :ft-contractors 0
      :pt-employees 0
      :pt-contractors 2
      :comment "Open company comment."
    },
    :finances {
      :cash 173228
      :revenue 2767
      :costs 22184
      :burn-rate -19417
      :runway "9 months"
      :comment "Open company comment."
    },
    :compensation {
      :percentage false
      :founders 6357
      :executives 0
      :employees 5899
      :contractors 2582
      :comment "Open company comment."
    }
  }
  :buffer {
    :name "Buffer"
    :id "buffer"
    :currency ["USD"]
    :headcount {
      :founders 1
      :executives 2
      :ft-employees 1
      :ft-contractors 2
      :pt-employees 1
      :pt-contractors 4
      :comment "Buffer comment."
    },
    :finances {
      :cash 323232
      :revenue 1234
      :costs 11321
      :burn-rate -10000
      :runway "9 months"
      :comment "Buffer comment."
    },
    :compensation {
      :percentage true
      :founders 40
      :executives 40
      :employees 10
      :contractors 10
      :comment "Buffer comment."
    }
  }
}))

;;Routes
(defroute editable-page-route "/edit/:id" {id :id}
  (om/root page ((keyword id) @app-state)
    {:target (. js/document (getElementById "app"))}))

(defroute list-page-route "/" []
  (om/root list-companies app-state
    {:target (. js/document (getElementById "app"))}))

(defroute default-route "*" []
  (om/root page-not-found app-state
    {:target (. js/document (getElementById "app"))}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
