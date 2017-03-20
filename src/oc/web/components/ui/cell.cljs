(ns oc.web.components.ui.cell
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.lib.utils :as utils]
            [cuerdas.core :as s]
            [cljsjs.react.dom]
            [cljs.core.async :refer (chan <!)]))

;; Cell component
;; props: 
;; - :value an initial value for the component
;; - :placeholder an placeholder for the :new state
;; - :prefix a prefix to show before the formatted value
;; - :suffix a suffix to show after the formatted value
;; - :cell-state (optional) an initial state for the component
;; - :positive-only (optional) if true, don't allow negative values (just drops the -)
;; - :short (optional) if true, display values as max 3 digits (e.g. 2.34M)

(defn- to-state [owner data state]
  (om/set-state! owner :cell-state state)
  (when (= state :edit)
    (utils/after 10 #(when-let [input (.findDOMNode js/ReactDOM (om/get-ref owner "edit-field"))]
                       (when input
                         (.focus (.findDOMNode js/ReactDOM input))
                         (set! (.-value input) (.-value input)))))))

(defn- initial-cell-state
  "Get the initial state for the cell.
   If :value is empty (ie 0 length or nil/undefined) it returns :new
   else it returns :display"
  [data]
  (let [value (:value data)
        state (:cell-state data)]
    (or state (if (>= value 0)
      :display
      :new))))

(defn check-value [current initial]
  (or (= current initial) (and (s/blank? initial) (s/blank? current))))

(defn trim-commas [v]
  (if (string? v)
    (.replace v (new js/RegExp "," "g") "")
    v))

(defn parse-value [v]
  (let [cleaned-value (trim-commas v)
        parsed-value (if (s/blank? cleaned-value)
                       cleaned-value
                       (.parseFloat js/window cleaned-value))]
    parsed-value))

(defn exit-cell [e owner data]
  (let [raw-value (.. e -target -value)
        parsed-value (parse-value raw-value)
        abs-value (if (:positive-only data) (utils/abs parsed-value) parsed-value)
        init-value (om/get-state owner :initial-value)
        ; if the value is the same as it was at the start
        ; go to the :display state, else go to :draft
        state (if (check-value abs-value init-value)
                :display
                :draft)
        ; if the value is empty and it was empty go to the :new state
        state (if (and (= state :display)
                       (s/blank? abs-value))
                :new
                state)]
    (when (or (= state :draft)
            (= state :display)
            (= state :new))
      ((:draft-cb data) abs-value))
    (to-state owner data state)))

(defn safe-parse-float [value]
  (when-not (clojure.string/blank? value)
    (let [cleaned-value (trim-commas value)
          float-value (js/parseFloat cleaned-value)]
      (if (js/isNaN float-value)
        0
        float-value))))

(defcomponent cell [data owner]

  (init-state [_]
    (utils/add-channel (str (:period data) (:key data)) (chan))
    (let [parsed-value (safe-parse-float (:value data))]
      {:cell-state (initial-cell-state data)
       :initial-value (:value data)
       :value parsed-value}))

  (did-update [_ _ prev-state]
    (when-not (= (:value prev-state) (om/get-state owner :value))
      ((:draft-cb data) (parse-value (om/get-state owner :value)))))

  (did-mount [_]
    (go (loop []
      (let [ch (utils/get-channel (str (:period data) (:key data)))
            signal (<! ch)]
        (.setTimeout js/window #(to-state owner data :edit) 100)
        (recur)))))

  (render-state [_ {:keys [value cell-state]}]
    (let [safe-value (safe-parse-float value)
          positive-only? (:positive-only data)
          short? (:short data)
          abs-value (if positive-only? (utils/abs safe-value) safe-value)
          prefix (:prefix data)
          currency (:currency data)
          decimals (or (:decimals data) 2)
          formatted-value (when abs-value
                            (cond
                              (and short? currency)
                              (str (utils/get-symbol-for-currency-code currency) (utils/with-metric-prefix abs-value))

                              short?
                              (utils/with-metric-prefix abs-value)

                              currency
                              (utils/thousands-separator abs-value currency decimals)

                              :else
                              (utils/thousands-separator abs-value)))
          prefix-value (if (and (not (s/blank? formatted-value)) (:prefix data))
                         (str (:prefix data) formatted-value)
                         formatted-value)
          final-value (if (and (not (s/blank? formatted-value)) (:suffix data))
                        (str prefix-value (:suffix data))
                        prefix-value)]
      (dom/div {:class "comp-cell"}
        (case cell-state

          :new
          (dom/div {:class "comp-cell-int state-new"
                    :on-click #(to-state owner data :edit)})

          :display
          (dom/div {:class "comp-cell-int state-display"
                    :on-click #(to-state owner data :edit)}
            (dom/span {} final-value))

          :draft
          (dom/div {:class "comp-cell-int state-draft"
                    :on-click #(to-state owner data :edit)}
            (dom/span {} final-value))

          :edit
          (dom/div {:class "comp-cell-int state-edit"}
            (dom/input #js {
                       :ref "edit-field"
                       :placeholder (:placeholder data)
                       :type "text"
                       :value (or value "")
                       :onFocus #(let [input (om/get-ref owner "edit-field")]
                                   (set! (.-value input) (.-value input)))
                       :onChange #(om/set-state! owner :value (.. % -target -value))
                       :onBlur #(exit-cell % owner data)
                       :onKeyDown #(cond

                                     (= "Enter" (.-key %))
                                     (exit-cell % owner data)

                                     (= "Tab" (.-key %))
                                     (do
                                       (exit-cell % owner data)
                                       ((:tab-cb data) (:period data) (:key data))
                                       (.preventDefault %)))})))))))