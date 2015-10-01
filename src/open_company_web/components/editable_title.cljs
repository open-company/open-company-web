(ns open-company-web.components.editable-title
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

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
     :editing false
     :read-only (:read-only data)})
  (render [_]
    (dom/div {:class "editable-title-container"}
      (if-not (om/get-state owner :editing)
        (dom/h2 {:class (str "editable-title fix" (when (:read-only data) " read-only"))
                 :on-click (fn [e]
                             (if (om/get-state owner :read-only)
                               ; not editable
                               (do
                                 (.preventDefault e)
                                 (router/nav! (str "/companies/" (:slug @router/path) "/" (name (:section data)))))
                               ; editable:
                               (do
                                 (om/update-state! owner :editing (fn [_]true))
                                 (delay-focus-input owner (utils/get-click-position e)))))}
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