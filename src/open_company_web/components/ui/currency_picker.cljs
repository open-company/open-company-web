(ns open-company-web.components.ui.currency-picker
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :refer [handle-change]]
              [open-company-web.lib.iso4217 :refer [iso4217 sorted-iso4217]]
              [om-bootstrap.button :as b]
              [dommy.core :as dommy :refer-macros [sel1 sel]]))

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

(defn get-currency-text [currency]
  (let [symbol (:symbol currency)
        display-symbol (or symbol (:code currency))
        label (str (:text currency) " " display-symbol)]
    label))

(defcomponent currency-picker
  "Show a select with all the possible currencies,
  the one in (:currency data) is selected."
  [data owner]
  (render [_]
    (let [current-curr ((keyword (:currency data)) iso4217)]
      (dom/div
        (dom/label "Currency:")
        (b/dropdown {
          :title (get-currency-text current-curr)
          :id "currency-dropdown"}
          (for [option (sorted-iso4217)]
              (b/menu-item {
                :key (:code option)
                :on-click (fn[e]
                            (.preventDefault e)
                            (when (> (count (:code option)) 0)
                              (.removeClass (.parent (sel1 :#currency-dropdown)) "open")
                              (handle-change data (:code option) :currency)))
              } (get-currency-text option))))))))