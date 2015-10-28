(ns open-company-web.components.section-selector
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.simple-section :refer (simple-section)]
            [open-company-web.lib.utils :as utils]
            [shodan.inspection :refer [inspect]]
            [shodan.console :as console]))

(defcomponent section-selector [data owner]
  (init-state [_]
    {:finances-edit false})
  (render [_]
    (let [section (:section data)
          read-only (:read-only data)
          section-data (:section-data data)]
      (dom/div #js {:className "section-selector" :ref "section-selector"}
        (cond
          ; finances edit
          (and (= section :finances) (om/get-state owner :finances-edit))
          (om/build finances-edit {:section-data section-data
                                   :section :finances
                                   :loading (:loading data)
                                   :close-edit-cb #(om/update-state! owner :finances-edit (fn [_] false))})
          ; finances
          (and (= section :finances) (not (om/get-state owner :finances-edit)))
          (om/build finances {:section :finances
                              :section-data section-data
                              :loading (:loading data)
                              :editable-click-callback #(om/update-state! owner :finances-edit (fn [_] true))})
          ; else it is a simple section
          section-data
          (om/build simple-section {:read-only read-only
                                    :section section
                                    :section-data section-data
                                    :loading (:loading data)})
          ; section not found
          :else
          (dom/h4 {} (str "Section " (name section) " not found")))))))