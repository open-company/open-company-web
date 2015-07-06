(ns ^:figwheel-always open-company-web.core
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.page :refer [page]]))

(defonce app-state (atom {
  :headcount {
    :founders 2
    :executives 0
    :ft-employees 3
    :ft-contractors 0
    :pt-employees 0
    :pt-contractors 3
    :comment "This is a comment."
  }
  :finances {
    :cash 173228
    :revenue 2767
    :costs 22184
    :comment "This is another comment."
  }
  :compensation {
    :dollars true
    :founders 6357
    :executives 0
    :employee 5899
    :contractor 2582
    :comment "More comments for you."
  }
}))

(om/root page app-state
  {:target (. js/document (getElementById "app"))})


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
