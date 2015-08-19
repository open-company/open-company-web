(ns open-company-web.components.new-report-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [om-bootstrap.random :as r]
            [om-bootstrap.button :as b]
            [open-company-web.lib.utils :refer [get-periods get-period-string]]))

(def current-year (.getFullYear (new js/Date)))

(def years (range 2013 (+ current-year 1)))

(def periods (concat (get-periods "Q" 4) [""] (get-periods "M" 12)))

(defn- period-label [period]
  (let [label (get-period-string period)]
    (if (re-find #"^Q" period) 
      (str period ": " label)
      label)))

(defn- get-value-from-ref [owner ref]
  (let [el (om/get-ref owner ref)
        dom-node (.getDOMNode el)]
    (.-value dom-node)))

(defcomponent new-report-popover [data owner]
  (render [_]
    (let [on-create (:on-create data)
          hide-cb (:hide-cb data)]
      (r/popover {
        :placement "bottom"
        :position-top (str (+ (:offsetTop data) 30) "px")
        :position-left (str (- (:offsetLeft data) 115) "px")
        :title "New report"}
        (dom/div {:class "row"}
          (dom/form {:class "form-horizontal col-sm-12"}
            ;year
            (dom/div {:class "form-group"}
              (dom/label {:class "col-sm-4 control-label"} "Year")
              (dom/div {:class "input-group col-sm-8"}
                (dom/select #js {
                  :className "form-control"
                  :ref "year"
                  :defaultValue current-year
                  :id "new-report-year"
                  }
                  (dom/option {:value ""} "Select a year:")
                  (for [year years]
                    (dom/option {:value year} year)))))
            ; period
            (dom/div {:class "form-group"}
              (dom/label {:class "col-sm-4 control-label"} "Period")
              (dom/div {:class "input-group col-sm-8"}
                (dom/select #js {
                  :className "form-control"
                  :ref "period"
                  :defaultValue ""
                  :id "new-report-month"
                  }
                  (dom/option {:value ""} "Select a period:")
                  (for [period periods]
                    (dom/option {:value period} (period-label period))))))
            ; button
            (b/button {
              :bs-style "primary"
              :on-click #(on-create (get-value-from-ref owner "year") (get-value-from-ref owner "period"))}
              "Create")
            (b/button {
              :bs-style "link"
              :on-click #(hide-cb)}
              "Cancel")))))))