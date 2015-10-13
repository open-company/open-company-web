(ns open-company-web.components.editable-title
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defn check-length [value elem px-pos cur-char-idx offset]
  (let [substr (subs value 0 cur-char-idx)]
    (set! (.-innerHTML elem) substr)
    (let [$e (.$ js/window elem)
          str-width (+ (.width $e) offset)
          diff (utils/abs (- px-pos str-width))]
      (if (or (>= str-width px-pos)
              (< diff 5))
        cur-char-idx
        false))))

(defn get-char-index [value elem px-pos]
  (let [$e (.$ js/window elem)
          offset (.offset $e)
          parent-offset (.offset (.parent $e))
          offset (- (.-left offset) (.-left parent-offset))]
    (loop [ch 1]
      (let [check (check-length value elem px-pos ch offset)]
        (cond
          ; found character
          check
          check
          ; not found but no more chars
          (= ch (count value))
          (count value)
          ; recur to next char
          :else
          (recur (inc ch)))))))

(defn delay-focus-input [owner position title]
  (.setTimeout js/window
               #(let [el (.getDOMNode (om/get-ref owner "editable-title-input"))]
                  (.focus el)
                  ; (utils/set-caret-position! el position)
                  (let [span (.getDOMNode (om/get-ref owner "hidden-span"))
                        char-pos (get-char-index title span position)]
                    (utils/set-caret-position! el char-pos)
                    (set! (.-innerHTML span) title)))
               100))

(defn save-section [data owner]
  ; dismiss editing
  (om/update-state! owner :editing (fn [_]false))
  (let [title (clojure.string/trim (om/get-state owner :title))]
    (utils/handle-change (:section-data data) title :title)
    (when (not= (om/get-state owner :initial-title) title)
      ; async signal to save data
      (.setTimeout js/window (fn [] (utils/save-values (:save-channel data))) 100)
      (om/update-state! owner :initial-title (fn [_]title)))))

(defcomponent editable-title [data owner]
  (init-state [_]
    {:editing false
     :initial-title (:title (:section-data data))
     :title (:title (:section-data data))})
  (will-update [_ next-props next-state]
    (when (not= next-props data)
      (om/update-state! owner :title (fn [_] (:title (:section-data next-props))))))
  (render [_]
    (let [section-data (:section-data data)
          title (om/get-state owner :title)]
      (dom/div {:class "editable-title-container"}
        (if-not (om/get-state owner :editing)
          (dom/h2 {:class (str "editable-title fix" (when (:read-only data) " read-only"))
                   :on-click (fn [e]
                               (when-not (:read-only data)
                                   (om/update-state! owner :editing (fn [_]true))
                                   (delay-focus-input owner (utils/get-click-position e) title)))}
                  title)
          (dom/input #js {:ref "editable-title-input"
                          :className "editable-title edit"
                          :value title
                          :onChange #(let [value (.. % -target -value)]
                                       (om/update-state! owner :title (fn [_]value)))
                          :onBlur #(save-section data owner)
                          :onKeyDown #(cond
                                        (= (.-key %) "Enter")
                                        (save-section data owner)
                                        (= (.-key %) "Escape")
                                        (do
                                          (om/update-state! owner :title (fn [_](om/get-state owner :initial-title)))
                                          (om/update-state! owner :editing (fn [_]false))))}))
        (dom/div {:style {:text-align "center"}}
          (dom/span #js {:ref "hidden-span" :className "hidden-span"} title))))))