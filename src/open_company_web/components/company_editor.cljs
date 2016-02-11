(ns open-company-web.components.company-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [cljs-flux.dispatcher :as flux]
            [open-company-web.components.ui.cell :refer (cell)]))

(defcomponent company-editor [data owner]
  (render [_]
    (dom/div {}
      (dom/input {:type "text"
                  :placeholder "Company Name"
                  :value (-> data :company-editor :name)
                  :on-change #(dis/dispatch! [:input [:company-editor :name] (.. % -target -value)])})
      (dom/input {:type "text"
                  :placeholder "Description"
                  :value (-> data :company-editor :description)
                  :on-change #(dis/dispatch! [:input [:company-editor :description] (.. % -target -value)])})
      (dom/button {:on-click #(dis/dispatch! [:company-submit])} "Submit"))))
