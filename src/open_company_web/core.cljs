(ns ^:figwheel-always open-company-web.core
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; Some utility functions

(defn abs [n] (max n (- n)))

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

(defcomponent report-line [data owner]
  (will-mount [_]
    (om/set-state! owner {
      :number (:number data)
      :label (:label data)
      :prefix (or (:prefix data) "")
      :plural-suffix (or (:plural-suffix data) "s")
    }))
  (render-state [_ {:keys [number label prefix plural-suffix]}]
    (let [suffix (if (> number 0) plural-suffix)]
      (dom/span
        (if-not (= (count prefix) 0) (dom/span {:class "label"} (str prefix)))
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
    (om/set-state! owner data))
  (render-state [_ {:keys [founders executives ft-employees ft-contractors pt-employees pt-contractors comment]}]
    (let [total-headcount (+ founders executives ft-employees ft-contractors pt-employees pt-contractors)
          full-time-equivalent (+ founders executives ft-employees ft-contractors (quot pt-employees 2) (quot pt-contractors 2))]
      (dom/div {:class "report-list headcount"}
        (dom/h3 "Headcount:")
        (dom/div
          (om/build report-line {:number founders :label "founder"}))
        (dom/div
          (om/build report-line {:number executives :label "executive"}))
        (dom/div
          (om/build report-line {:number ft-employees :label "full time employee"}))
        (dom/div
          (om/build report-line {:number ft-contractors :label "full time contractor"}))
        (dom/div
          (om/build report-line {:number pt-employees :label "part time employee"}))
        (dom/div
          (om/build report-line {:number pt-contractors :label "part time contractor"}))
        (dom/div
          (om/build report-line {:number total-headcount :label "total headcount"})", "
          (om/build report-line {:number full-time-equivalent :label "full time equivalent"}))
        (dom/div {:class "comment"} (str "Comments: " comment))))))

(defcomponent finances
  [data :- {
    :cache js/Number
    :revenue js/Number
    :costs js/Number
    :comment js/String
  }
  owner]
  (will-mount [_]
    (om/set-state! owner data))
  (render-state [_ {:keys [cash revenue costs comment]}]
    (let [burn-rate (- revenue costs)
          burn-rate-label (if (> burn-rate 0) "Growth rate: " "Burn rate: ")
          burn-rate-classes (str "num " (if (> burn-rate 0) "green" "red"))
          profitable (if (> burn-rate 0) "Yes" "No")
          run-away (if (<= burn-rate 0) (quot cash burn-rate) "N/A")]
      (dom/div {:class "report-list finances"}
        (dom/h3 "Finances:")
        (dom/div
          (om/build report-line {:prefix "$" :number cash :label "cash on hand"}))
        (dom/div
          (om/build report-line {:prefix "$" :number revenue :label "revenue this month"}))
        (dom/div
          (om/build report-line {:prefix "$" :number costs :label "costs this month"}))
        (dom/div
          (dom/span {:class "label"} (str "Profitable this month? " profitable)))
        (dom/div
          (dom/span {:class "label"} burn-rate-label)
          (dom/span {:class burn-rate-classes} (abs burn-rate)))
        (if (<= burn-rate 0) (dom/div
          (dom/span {:class "label"} "Runaway: " (abs run-away) " months")))
        (dom/div {:class "comment"} (str "Comment: " comment))))))

(defcomponent compensation
  [data :- {
    :dollars js/Boolean
    :founders js/Number
    :executives js/Number
    :employee js/Number
    :contractor js/Number
    :comment js/String
  }
  owner]
  (will-mount [_]
    (om/set-state! owner data))
  (render-state [_ {:keys [dollars founders executives employee contractor comment]}]
    (let [prefix (if dollars "$" "%")
          total-compensation (+ founders executives employee contractor)]
      (dom/div {:class "report-list compensation"}
        (dom/h3 "Compensation:")
        (dom/div
          (dom/span {:class "label"} "Report in: "
            (dom/input {
              :type "radio"
              :name "report-type"
              :value "$"
              :id "report-type-$"
              :checked dollars
              :on-click #(om/set-state! owner :dollars true)})
            (dom/label {:for "report-type-$"} " Dollars ")
            (dom/input {
              :type "radio"
              :name "report-type"
              :value "%"
              :id "report-type-%"
              :checked (not dollars)
              :on-click #(om/set-state! owner :dollars false)})
            (dom/label {:for "report-type-%"} " Percent ")))
        (dom/div
          (om/build report-line {:prefix prefix :number founders :label "founders compensation this month"}))
        (dom/div
          (om/build report-line {:prefix prefix :number executives :label "executives compensation this month"}))
        (dom/div
          (om/build report-line {:prefix prefix :number employee :label "employees compensation this month"}))
        (dom/div
          (om/build report-line {:prefix prefix :number contractor :label "contractors compensation this month"}))
        (dom/div
          (om/build report-line {:prefix prefix :number total-compensation :label "total compensation this month"}))
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
      (om/build headcount (:headcount (:data data)))
      (om/build finances (:finances (:data data)))
      (om/build compensation (:compensation (:data data))))))

(om/root
  page
  app-state
  {:target (. js/document (getElementById "app"))})


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
