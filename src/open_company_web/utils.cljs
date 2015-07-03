(ns open-company-web.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]))

(defn abs [n] (max n (- n)))

(defn thousands-separator [number]
  (let [num-str (str number)]
    (clojure.string/replace num-str #"\B(?=(\d{3})+(?!\d))" ",")))

(defn thousands-separator-strip [number]
  (let [num-str (str number)]
    (clojure.string/replace num-str "," "")))

(defn handle-change [cursor value key]
  (om/transact! cursor key (fn [_] value)))

(defn get-event-int-value [e]
  (int (thousands-separator-strip (.. e -target -value))))
