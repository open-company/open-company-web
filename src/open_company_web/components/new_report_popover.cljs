(ns open-company-web.components.new-report-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [om-bootstrap.random :as r]
            [om-bootstrap.button :as b]))

(defn get-value-from-ref [owner ref]
  (let [el (om/get-ref owner ref)
        dom-node (.getDOMNode el)]
    (.-value dom-node)))

(defcomponent new-report-popover [data owner]
  (render [_]
    (let [on-create (:on-create data)]
      (r/popover {
        :placement "bottom"
        :position-top "50"
        :position-left "105px"
        :title "New report"}
        (dom/div {:class "row"}
          (dom/form {:class "form-horizontal col-sm-12"}
            ;year
            (dom/div {:class "form-group"}
              (dom/label {:class "col-sm-4 control-label"} "Year")
              (dom/div {:class "input-group col-sm-8"}
                (dom/input #js {
                  :type "text"
                  :placeholder "2015"
                  :ref "year"
                  :className "form-control"})))
            ; period
            (dom/div {:class "form-group"}
              (dom/label {:class "col-sm-4 control-label"} "Period")
              (dom/div {:class "input-group col-sm-8"}
                (dom/input #js {
                  :type "text"
                  :ref "period"
                  :placeholder "M6"
                  :className "form-control"})))
            ; button
            (b/button {
              :bs-style "primary"
              :on-click #(on-create (get-value-from-ref owner "year") (get-value-from-ref owner "period"))}
              "Create")))))))
