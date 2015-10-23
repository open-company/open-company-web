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

(defn sections-focuser []
  (let [scroll-top (.scrollTop (.$ js/window js/window))
        sections (.$ js/window ".section-container, .section-separator")
        window-height (.height (.$ js/window js/window))
        pivot (int (* (/ window-height 100) 30))
        tot (- window-height pivot)]
    (.each sections
           (fn [n item]
             (let [$-item (.$ js/window item)
                   item-offset-top (.-top (.offset $-item))
                   distance (- item-offset-top scroll-top)
                   el-tot (- distance pivot)]
               (if-not (> distance pivot)
                 (.css $-item #js {"opacity" "1"})
                 (let [opac-percent (/ (* el-tot 100) tot)]
                   (.css $-item #js {"opacity" (str (- 1 (/ opac-percent 100)))}))))))))

(defcomponent section-selector [data owner]
  (init-state [_]
    {:finances-edit false})
  (did-mount [_]
    (let [jq-body (.$ js/window js/window)]
      (.scroll jq-body #(sections-focuser)))
    (sections-focuser))
  (render [_]
    (let [section (:section data)
          read-only (:read-only data)
          company-data (:data data)]
      (dom/div #js {:className "section-selector" :ref "section-selector"}
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