(ns open-company-web.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]))

(defn abs [n] (max n (- n)))

(defn thousands-separator [number]
  (let [integer (int number)
        decimal (int (* (- number integer) 100))
        integer-string (clojure.string/replace (str integer) #"\B(?=(\d{3})+(?!\d))" ",")]
    (if (> decimal 0)
      (str integer-string "." decimal)
      integer-string)))

(defn thousands-separator-strip [number]
  (let [num-str (str number)]
    (clojure.string/replace num-str "," "")))

(defn handle-change [cursor value key]
  (om/transact! cursor key (fn [_] value)))

(defn String->Number [str]
  (let [n (js/parseFloat str)]
    (if (= js/NaN n) 0 n)))
