(ns open-company-web.components.currency-picker
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.iso4217.iso4217 :refer [sorted-iso4217]]))

(defcomponent currency-option
  [data owner]
  (render [_]
    (let [symbol (:symbol data)
          display-symbol (or symbol (:code data))
          label (str (:text data) " " display-symbol)]
      (dom/option {
        :disabled (= (count (:code data)) 0)
        :value (:code data)
        } label))))

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
      (om/build-all currency-option (sorted-iso4217))))))
