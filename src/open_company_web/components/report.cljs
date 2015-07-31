(ns open-company-web.components.report
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]
              [open-company-web.components.headcount :refer [headcount readonly-headcount]]
              [open-company-web.components.finances :refer [finances readonly-finances]]
              [open-company-web.components.compensation :refer [compensation readonly-compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [open-company-web.components.navbar :refer [navbar]]
              [open-company-web.components.sidebar :refer [sidebar]]
              [open-company-web.components.link :refer [link]]
              [open-company-web.components.new-report-popover :refer [new-report-popover]]
              [open-company-web.dispatcher :refer [app-state]]
              [om-bootstrap.nav :as n]
              [open-company-web.api :refer [save-or-create-report]]
              [cljs.core.async :refer [put! chan <!]]
              [open-company-web.router :as router]))

(defn create-new-report [owner company-data new-year new-period]
  (let [symbol (:symbol company-data)]
    ; hide popover
    (om/set-state! owner :show-new-report-popover false)
    ; when the data are correct: FIXME check year and period
    (when (and new-year new-period)
      (let [new-report-key (str "report-" symbol "-" new-year "-" new-period)
            links (:links company-data)]
        ; add an empty report
        (om/transact! company-data assoc (keyword new-report-key) {
          :finances {}
          :headcount {}
          :compensation {}
          })
        ; add the report to links
        (om/transact! company-data :links #(conj % {
          :href (str "/v1/companies/" symbol "/" new-year "/" new-period)
          :rel "report"
          }))
        ; navigate to the new report
        (router/nav! (str "/" symbol "/" new-year "/" new-period "/edit"))))))

(defcomponent report [data owner]
  (init-state [_]
    (let [chan (chan)]
      (utils/add-channel "save-report" chan))
    {:show-new-report-popover false})
  (will-mount [_]
    (om/set-state! owner :selected-tab 1)
    (let [save-change (utils/get-channel "save-report")]
        (go (loop []
          (let [change (<! save-change)
                symbol (:symbol @router/path)
                year (:year @router/path)
                period (:period @router/path)
                company-data ((keyword symbol) @app-state)
                report-key (keyword (str "report-" symbol "-" year "-" period))
                report-data (report-key company-data)]
            (save-or-create-report symbol year period report-data)
            (recur))))))
  (render [_]
    (let [symbol (:symbol @router/path)
          year (:year @router/path)
          period (:period @router/path)
          company-data ((keyword symbol) data)
          report-key (keyword (str "report-" symbol "-" year "-" period))
          report-data (report-key company-data)
          reports (filterv #(= (:rel %) "report") (:links company-data))
          is-summary (utils/in? (:route @router/path) "summary")]
      (dom/div {:class "report-container row"}
        (om/build navbar company-data)
        (dom/div {:class "container-fluid"}
          (om/build sidebar {:active "reports"})
          (dom/div {:class "col-md-11 col-md-offset-1 main"}
            (n/nav {
              :class "profile-tab-navigation"
              :bs-style "tabs"}

              (let [url (str "/" symbol "/summary")]
                (n/nav-item {
                  :key "summary"
                  :href url
                  :on-click (fn [e] (.preventDefault e) (router/nav! url))
                  :class (if is-summary "active" "")
                  } "Summary"))

              (for [report reports]
                (let [href (:href report)
                      parts (clojure.string/split href "/")
                      rep-year (nth parts 4)
                      rep-period (nth parts 5)
                      rep-key (str "report-" symbol "-" rep-year "-" rep-period)
                      link (str "/" symbol "/" rep-year "/" rep-period "/edit")]
                  (n/nav-item {
                    :key rep-key
                    :href link
                    :on-click (fn [e] (.preventDefault e) (router/nav! link))
                    :class (if (= (name report-key) rep-key) "active" "")
                    } (str rep-period " " rep-year))))

              (let [url (str "/" symbol "/new-report")]
                (n/nav-item {
                  :key "new-report"
                  :href url
                  :on-click (fn [e]
                              ; toggle popover
                              (let [toggle (not (om/get-state owner :show-new-report-popover))]
                                (om/set-state! owner :show-new-report-popover toggle))
                              (.preventDefault e))}
                  (dom/i {:class "fa fa-plus"}))))

            ; New report popover
            (when (om/get-state owner :show-new-report-popover)
              (om/build new-report-popover {
                :on-create (fn [new-year new-period]
                            (create-new-report owner company-data new-year new-period))}))

            (cond

              (:loading data)
              (dom/div nil "Loading")

              (and (contains? data (keyword symbol)) (contains? company-data report-key))
              (dom/div
                (om/build finances {
                    :finances (:finances report-data)
                    :currency (:currency company-data)})
                (om/build headcount (:headcount report-data))
                (om/build compensation {
                    :compensation (:compensation report-data)
                    :headcount (:headcount report-data)
                    :currency (:currency company-data)}))

              is-summary
              (dom/div nil (dom/h3 "Reports summary"))

              :else
              (dom/div nil "Report not found"))))))))

(defcomponent readonly-report [data owner]
  (will-mount [_]
    (om/set-state! owner :selected-tab 1))
  (render [_]
    (let [symbol (:symbol @router/path)
          year (:year @router/path)
          period (:period @router/path)
          company-data ((keyword symbol) data)
          report-key (keyword (str "report-" symbol "-" year "-" period))
          report-data (report-key company-data)
          headcount (:headcount report-data)]
      (dom/div
        (dom/h2 (:name company-data) " Report for " year " " period)
        (cond
          (:loading data)
          (dom/div nil "Loading")

          (and (contains? data (keyword symbol)) (contains? company-data report-key))
          (dom/div nil
            (n/nav {
              :class "tab-navigation"
              :bs-style "tabs"
              :active-key (om/get-state owner :selected-tab)
              :on-select #(om/set-state! owner :selected-tab %) }
              (n/nav-item {:key 1 :href ""} "Headcount")
              (n/nav-item {:key 2 :href ""} "Finances")
              (n/nav-item {:key 3 :href ""} "Compensation"))
            (case (om/get-state owner :selected-tab)
              1 (om/build readonly-headcount (:headcount report-data))
              2 (om/build readonly-finances {
                  :finances (:finances report-data)
                  :currency (:currency report-data)})
              3 (om/build readonly-compensation {
                  :compensation (:compensation report-data)
                  :headcount (:headcount report-data)
                  :currency (:currency report-data)})))

          :else
          (dom/div nil "Report not found"))))))
