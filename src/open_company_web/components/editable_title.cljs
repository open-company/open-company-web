(ns open-company-web.components.editable-title
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defn check-length [value elem px-pos cur-char-idx]
  (let [substr (subs value 0 cur-char-idx)]
    (set! (.-innerHTML elem) substr)
    (let [str-width (.width (.$ js/window elem))
          diff (utils/abs (- px-pos str-width))]
      (if (or (>= str-width px-pos)
              (< diff 5))
        cur-char-idx
        false))))

(defn get-char-index [value elem px-pos]
  (loop [ch 0]
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
        (recur (inc ch))))))

(defn delay-focus-input [owner position]
  (.setTimeout js/window
               #(let [el (.getDOMNode (om/get-ref owner "editable-title-input"))]
                  (.focus el)
                  ; (utils/set-caret-position! el position)
                  (let [span (.getDOMNode (om/get-ref owner "hidden-span"))
                        value (om/get-state owner :title)
                        char-pos (get-char-index value span position)]
                    (utils/set-caret-position! el char-pos)))
               100))

(defcomponent editable-title [data owner]
  (init-state [_]
    {:title (:title data)
     :editing false})
  (render [_]
    (dom/div {:class "editable-title-container"}
      (if-not (om/get-state owner :editing)
        (dom/h2 {:class "editable-title fix"
                 :on-click (fn [e]
                             (om/update-state! owner :editing (fn [_]true))
                             (delay-focus-input owner (utils/get-click-position e)))}
                (om/get-state owner :title))
        (dom/input #js {:ref "editable-title-input"
                        :className "editable-title edit"
                        :value (om/get-state owner :title)
                        :onChange #(let [value (.. % -target -value)]
                                     (om/update-state! owner :title (fn [_] value)))
                        :onBlur #(om/update-state! owner :editing (fn [_]false))
                        :onKeyDown #(when (= (.-key %) "Enter")
                                      (om/update-state! owner :editing (fn [_]false)))}))
      (dom/span #js {:ref "hidden-span" :className "hidden-span"} (:title data)))))