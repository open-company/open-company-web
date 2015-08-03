(ns open-company-web.components.new-report-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [om-bootstrap.random :as r]
            [om-bootstrap.button :as b]))

(def current-year (.getFullYear (new js/Date)))

(def years (range 1950 (+ current-year 1)))
(def periods ["M1"
              "M2"
              "M3"
              "M4"
              "M5"
              "M6"
              "M7"
              "M8"
              "M9"
              "M10"
              "M11"
              "M12"
              ""
              "Q1"
              "Q2"
              "Q3"
              "Q4"])

(defn get-value-from-ref [owner ref]
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
        :position-left (str (- (:offsetLeft data) 63) "px")
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
                  }
                  (for [year years]
                    (dom/option {:value year} year)))))
            ; period
            (dom/div {:class "form-group"}
              (dom/label {:class "col-sm-4 control-label"} "Period")
              (dom/div {:class "input-group col-sm-8"}
                (dom/select #js {
                  :className "form-control"
                  :ref "period"
                  :defaultValue "M1"
                  }
                  (for [period periods]
                    (dom/option {:value period} period)))))
            ; button
            (b/button {
              :bs-style "primary"
              :on-click #(on-create (get-value-from-ref owner "year") (get-value-from-ref owner "period"))}
              "Create")
            (b/button {
              :bs-style "link"
              :on-click #(hide-cb)}
              "Cancel")))))))