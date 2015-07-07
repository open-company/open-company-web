(ns open-company-web.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]))

(defn abs [n] (max n (- n)))

(defn thousands-separator [number]
  (let [parts (clojure.string/split (str number) "." 1)
        int-part (first parts)
        dec-part (get parts 1)
        integer-string (clojure.string/replace int-part #"\B(?=(\d{3})+(?!\d))" ",")]
    (if-not (= dec-part nil)
      (str integer-string "." dec-part)
      integer-string)))

(defn thousands-separator-strip [number]
  (let [num-str (str number)]
    (clojure.string/replace num-str "," "")))

(defn handle-change [cursor value key]
  (om/transact! cursor key (fn [_] value)))

(defn String->Number [str]
  (let [n (js/parseFloat str)]
    (if (= js/NaN n) 0 n)))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))
