(ns open-company-web.components.currency-picker
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.iso4217.iso4217 :refer [iso4217]]))

(defcomponent currency-option
  [data owner]
  (render [_]
    (dom/option {
      :value (:code data)
    } (str (:code data)))))

(defcomponent currency-picker
  "Show a select with all the possible currencies,
  the one in (:currency data) is selected"
  [data owner]
  (render [_]
    (dom/div
      (dom/label "Currency:")
      (dom/select {
        :value (first (:currency data))
        :on-change #(handle-change data [(.. % -target -value)] :currency)
      }
      (om/build-all currency-option (vals iso4217))))))
