(ns open-company-web.components.section-selector
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.simple-section :refer (simple-section)]))

(defcomponent section-selector [data owner]
  (init-state [_]
    {:finances-edit false})
  (render [_]
    (let [section (:section data)
          read-only (:read-only data)
          company-data (:data data)]
      (dom/div {:class "section-selector"}
        (cond
          ; finances edit
          (and (= section :finances) (om/get-state owner :finances-edit))
          (om/build finances-edit {:company-data company-data
                                   :section :finances
                                   :close-edit-cb #(om/update-state! owner :finances-edit (fn [_] false))})
          ; finances
          (and (= section :finances) (not (om/get-state owner :finances-edit)))
          (om/build finances {:section :finances
                              :company-data company-data
                              :editable-click-callback #(om/update-state! owner :finances-edit (fn [_] true))})
          ; else it is a simple section
          (contains? company-data section)
          (om/build simple-section {:read-only read-only
                                    :section section
                                    :company-data company-data})
          ; section not found
          :else
          (dom/h4 {} (str "Section " (name section) " not found")))))))