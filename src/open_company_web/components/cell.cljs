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
    (let [parsed-value (.parseFloat js/window (:value data))
          value (if (js/isNaN parsed-value) "" parsed-value)]
      {:cell-state (initial-cell-state data)
       :inital-value (:value data)
       :value value}))
  (render [_]
    (let [value (om/get-state owner :value)
          float-value (.parseFloat js/window value)
          float-value (if (js/isNaN float-value) "" float-value)
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
                       :onBlur #(let [value (.. % -target -value)
                                      init-value (om/get-state owner :inital-value)
                                      ; if the value is the same as it was at the start
                                      ; go to the :display state, else go to :draft
                                      state (if (= value init-value) :display :draft)
                                      ; if the value is empty and it was empty got to the :new state
                                      state (if (and (= state :display) (= value "")) :new state)]
                                  (to-state owner state))
                       :onKeyDown #(when (= (.-key %) "Enter") (to-state owner :draft))})))))))

