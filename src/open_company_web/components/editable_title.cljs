(ns open-company-web.components.editable-title
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defn check-length [value elem px-pos cur-char-idx]
  (let [substr (subs value 0 cur-char-idx)]
    (set! (.-innerHTML elem) substr)
    (let [$e (.$ js/window elem)
          str-width (.width $e)
          diff (utils/abs (- px-pos str-width))]
      (if (or (>= str-width px-pos)
              (< diff 5))
        cur-char-idx
        false))))

(defn get-char-index [value elem px-pos]
  (let [$e (.$ js/window elem)]
    (loop [ch 1]
      (let [check (check-length value elem px-pos ch)]
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
                  (let [span (.getDOMNode (om/get-ref owner "hidden-span"))
                        char-pos (get-char-index title span position)]
                    (utils/set-caret-position! el char-pos)
                    (set! (.-innerHTML span) title)))
               100))

(defcomponent editable-title [data owner]

  (render [_]
    (let [title (:title data)]
      (dom/div {:class "editable-title-container"}
        (dom/div {:class "hidden-span-container"}
          (dom/span #js {:ref "hidden-span" :className "hidden-span"} title))
        (if-not (:editing data)
          (dom/h2 {:class (str "editable-title fix" (when (:read-only data) " read-only"))
                   :on-click (fn [e]
                               (when-not (:read-only data)
                                 ((:start-editing-cb data))
                                 (delay-focus-input owner (utils/get-click-position e) title)))}
                  title)
          (dom/input #js {:ref "editable-title-input"
                          :className "editable-title edit"
                          :value title
                          :onChange #(let [value (.. % -target -value)]
                                       ((:change-cb data) value))
                          :onBlur (fn [e]
                                    ((:cancel-if-needed-cb data)))
                          :onKeyDown #(cond
                                        (= (.-key %) "Enter")
                                        ((:save-cb data))
                                        (= (.-key %) "Escape")
                                        (do
                                          ((:cancel-if-needed-cb data))))}))))))