(ns ^:figwheel-always open-company-web.core
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {
  :headcount {
    :founders 2
    :executives 0
    :ft-employees 3
    :ft-contractors 0
    :pt-employees 0
    :pt-contractors 2
    :comment "This is a comment."
  }
  :finances {
    :cash 173228
    :revenue 2767
    :costs 22184
    :burn-rate -19417
    :runway "9 months"
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

(defcomponent headcount-number-data [data owner]
  (will-mount [_]
    (om/set-state! owner {
      :number (:number data)
      :label (:label data)
      :plural-suffix (or (:plural-suffix data) "s")
    }))
  (render-state [_ {:keys [number label plural-suffix]}]
    (let [suffix (if (> number 0) plural-suffix)]
      (dom/span
        (dom/span {:class "num"} (str number))
        (dom/span {:class "label"} (str " " label suffix))))))

(defcomponent headcount
  [data :- {
    :founders js/Number
    :executives js/Number
    :ft-employees js/Number
    :ft-contractors js/Number
    :pt-employees js/Number
    :pt-contractors js/Number
    :comment js/String
  }
  owner]
  (will-mount [_]
    (om/set-state! owner data)
    )
  (render-state [_ {:keys [founders executives ft-employees ft-contractors pt-employees pt-contractors comment]}]
    (let [total-headcount (+ founders executives ft-employees ft-contractors pt-employees pt-contractors)
          full-time-equivalent (+ founders executives ft-employees ft-contractors (quot pt-employees 2) (quot pt-contractors 2))]
      (dom/div {:style {:margin-left "10px"}}
        (dom/h3 "Headcount:")
        (dom/div
          (om/build headcount-number-data {:number founders :label "founder"}))
        (dom/div
          (om/build headcount-number-data {:number executives :label "executive"}))
        (dom/div
          (om/build headcount-number-data {:number ft-employees :label "full time employee"}))
        (dom/div
          (om/build headcount-number-data {:number ft-contractors :label "full time contractor"}))
        (dom/div
          (om/build headcount-number-data {:number pt-employees :label "part time employee"}))
        (dom/div
          (om/build headcount-number-data {:number pt-contractors :label "part time contractor"}))
        (dom/div
          (om/build headcount-number-data {:number total-headcount :label "total headcount"})", "
          (om/build headcount-number-data {:number full-time-equivalent :label "full time equivalent"}))
        (dom/div {:class "comment"} (str "Comment: " comment))))))

(defcomponent page
  [data
  :- {
    :headcount {}
    :finances {}
    :compensation {}
    }
  owner]
  (will-mount [_]
    (om/set-state! owner :data data))
  (render-state [owner data]
    (dom/div
      (dom/h2 "Dashboard")
      (om/build headcount (:headcount (:data data))))))

(om/root
  page
  app-state
  {:target (. js/document (getElementById "app"))})


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
