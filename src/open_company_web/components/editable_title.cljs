(ns open-company-web.components.editable-title
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))


(defn delay-focus-input [owner]
  (.setTimeout js/window #(let [el (.getDOMNode (om/get-ref owner "editable-title-input"))]
                            (.focus el)
                            (set! (.-value el) (.-value el))) 100))

(defcomponent editable-title [data owner]
  (init-state [_]
    {:title (:title data)
     :editing false})
  (render [_]
    (dom/div {:class "editable-title-container"}
      (if-not (om/get-state owner :editing)
        (dom/h2 {:class "editable-title fix"
                 :on-click (fn [] (om/update-state! owner :editing (fn [_]true)) (delay-focus-input owner))} (:title data))
        (dom/input #js {:ref "editable-title-input"
                        :className "editable-title edit"
                        :value (om/get-state owner :title)
                        :onChange #(let [value (.. % -target -value)]
                                     (om/update-state! owner :title (fn [_] value)))
                        :onBlur #(om/update-state! owner :editing (fn [_]false))
                        :onKeyDown #(when (= (.-key %) "Enter")
                                      (om/update-state! owner :editing (fn [_]false)))
                    })))))