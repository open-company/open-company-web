(ns open-company-web.components.cell
  (:require [om.core :as om]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [cljs.core.async :refer [put!]]))

;; Cell component
;; props: 
;; - :value an initial value for the component
;; - :placeholder an placeholder for the :new state
;; - :prefix a prefix to show before the formatte value
;; - :suffic a suffix to show after the formatte value
;; - :cell-state (optional) an initial state for the component


(defn- format-value [value]
  (.toLocaleString value))

(defn- to-state [owner state]
  (om/update-state! owner :cell-state (fn [_] state))
  (when (= state :edit)
    (.setTimeout js/window #(let [input (om/get-ref owner "edit-field")]
                              (when input
                                (.focus (.getDOMNode input)))) 10)))

(defn- initial-cell-state
  "Get the initial state for the cell.
   If :value is empty (ie 0 length or nil/undefined) it returns :new
   else it returns :display"
  [data]
  (let [value (:value data)
        state (:cell-state data)]
    (if state
      state
      (if (> (count value) 0)
        :display
        :new))))

(defcomponent cell [data owner]
  (init-state [_]
    {:cell-state (initial-cell-state data)
     :inital-value (:value data)
     :value (.parseFloat js/window (:value data))})
  (render [_]
    (let [value (om/get-state owner :value)
          float-value (.parseFloat js/window value)
          formatted-value (format-value float-value)
          prefix-value (if (:prefix data) (str (:prefix data) formatted-value) formatted-value)
          final-value (if (:suffix data) (str (:suffix data) prefix-value) prefix-value)]
      (dom/div {:class "cell"}
        (case (om/get-state owner :cell-state)
          
          :new
          (dom/div {:class "cell-int state-new"
                    :on-click #(to-state owner :edit)}
            (dom/span {} (:placeholder data)))
          
          :display
          (dom/div {:class "cell-int state-display"
                    :on-click #(to-state owner :edit)}
            (dom/span {} final-value))
          
          :draft
          (dom/div {:class "cell-int state-draft"
                    :on-click #(to-state owner :edit)}
            (dom/span {} final-value))
          
          :edit
          (dom/div {:class "cell-int state-edit"}
            (dom/input #js {
                       :ref "edit-field"
                       :value value
                       :onFocus #(let [input (.getDOMNode (om/get-ref owner "edit-field"))]
                                   (set! (.-value input) (.-value input)))
                       :onChange #(om/update-state! owner :value (fn [_] (.. % -target -value)))
                       :onBlur #(to-state owner :draft)
                       :onKeyDown #(when (= (.-key %) "Enter") (to-state owner :draft))})))))))

