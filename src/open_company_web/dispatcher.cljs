(ns open-company-web.dispatcher
  (:require [cljs-flux.dispatcher :as flux]
            [no.en.core :refer [deep-merge]]
            [open-company-web.router :as router]))

(defonce app-state (atom {
  ; :OPEN {
  ;   "name" "Transparency, LLC"
  ;   "symbol" "OPEN"
  ;   "currency" ["USD"]
  ;   "headcount" {
  ;     "founders" 2
  ;     "executives" 0
  ;     "ft-employees" 3
  ;     "pt-employees" 0
  ;     "contractors" 2
  ;     "comment" "Transparency headcount comment."
  ;   },
  ;   "finances" {
  ;     "cash" 173228
  ;     "revenue" 2767
  ;     "costs" 22184
  ;     "burn-rate" -19417
  ;     "runway" "9 months"
  ;     "comment" "Transparency finances comment."
  ;   },
  ;   "compensation" {
  ;     "percentage" false
  ;     "founders" 6357
  ;     "executives" 0
  ;     "employees" 5899
  ;     "contractors" 2582
  ;     "comment" "Transparency compensation comment."
  ;   }
  ; }
  ; "BUFFR" {
  ;   "name" "Buffer"
  ;   "symbol" "BUFFR"
  ;   "currency" ["USD"]
  ;   "headcount" {
  ;     "founders" 1
  ;     "executives" 2
  ;     "ft-employees" 1
  ;     "pt-employees" 1
  ;     "contractors" 4
  ;     "comment" "Buffer headcount comment."
  ;   },
  ;   "finances" {
  ;     "cash" 323232
  ;     "revenue" 1234
  ;     "costs" 11321
  ;     "burn-rate" -10000
  ;     "runway" "9 months"
  ;     "comment" "Buffer finances comment."
  ;   },
  ;   "compensation" {
  ;     "percentage" true
  ;     "founders" 40
  ;     "executives" 40
  ;     "employees" 10
  ;     "contractors" 10
  ;     "comment" "Buffer compensation comment."
  ;   }
  ; }
}))


(def companies (flux/dispatcher))

(def company (flux/dispatcher))

(def report (flux/dispatcher))

(def companies-list-dispatch
  (flux/register
    companies
    (fn [body]
      (when body
        (swap! app-state assoc :companies (:companies (:collection body)))))))

(def company-dispatch
  (flux/register
    company
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        ; add the new values to the atom
        (swap! app-state assoc (keyword (:symbol body)) body)))))

(def empty-report {
  :headcount {}
  :finances {}
  :compensation {}
})

(def report-dispatch
  (flux/register
    report
    (fn [body]
      (when body
        ; remove loading key
        (swap! app-state dissoc :loading)
        ; make sure the report contains all the keys :headcount :finances :compensation
        (let [report-data (merge empty-report body)]
          ; add the new report data
          (let [symbol (:symbol @router/path)
                year (:year @router/path)
                period (:period @router/path)
                report-key (keyword (str "report-" symbol "-" year "-" period))]
            (swap! app-state assoc-in [(keyword symbol) report-key] report-data)))))))