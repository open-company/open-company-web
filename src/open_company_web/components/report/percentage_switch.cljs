(ns open-company-web.components.report.percentage-switch
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent percentage-switch [data owner]
  (render [_]
    (let [comp-data (:compensation data)
          percentage (:percentage comp-data)
          currency (:currency data)
          currency-symbol (utils/get-symbol-for-currency-code currency)
          prefix (str currency-symbol " ")]
      (dom/div {:class "form-group"}
        (dom/label {:for "show-as" :class "col-md-4 control-label"} "Show as")
        (dom/div {:class "btn-group btn-toggle col-md-3"}
          (dom/button {
            :type "button"
            :class (str "btn btn-success" (when (not percentage) " active"))
            :on-click #(utils/handle-change comp-data false :percentage)}
            currency-symbol)
          (dom/button {
            :type "button"
            :class (str "btn btn-default" (when percentage " active"))
            :on-click #(utils/handle-change comp-data true :percentage)}
            "%"))
        (dom/p {:class "help-block"} (str "Viewers will see as " (if percentage "%" currency-symbol)))))))