(ns open-company-web.lib.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]
              [open-company-web.lib.iso4217 :refer [iso4217]]
              [cljs.core.async :refer [put!]]))

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

(defn String->Number [str]
  (let [n (js/parseFloat str)]
    (if (js/isNaN n) 0 n)))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn get-currency [currency-code]
  (let [kw (keyword currency-code)]
    (get iso4217 kw)))

(defn get-symbol-for-currency-code [currency-code]
  (let [currency (get-currency currency-code)
        symbol (if
                (and
                  (contains? currency :symbol)
                  (> (count (:symbol currency)) 0))
                (:symbol currency)
                currency-code)
        ret (or symbol (:code currency))]
  ret))

(def channel-coll (atom {}))

(defn add-channel [channel-name channel]
  (swap! channel-coll assoc channel-name channel))

(defn get-channel [channel-name]
  (@channel-coll channel-name))

(defn handle-change [cursor value key]
  (if (array? key)
    (om/transact! cursor assoc-in key (fn [_] value))
    (om/transact! cursor key (fn [_] value))))

(defn change-value [cursor e key]
  (handle-change cursor (.. e -target -value) key))

(defn save-values [channel-name]
  (let [save-channel (get-channel channel-name)]
    (put! save-channel 1)))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn get-long-period [period]
  (case period
    "M1" "January"
    "M2" "February"
    "M3" "March"
    "M4" "April"
    "M5" "May"
    "M6" "June"
    "M7" "July"
    "M8" "August"
    "M9" "September"
    "M10" "October"
    "M11" "November"
    "M12" "December"

    "B1" "January - February"
    "B2" "March - April"
    "B3" "May - June"
    "B4" "July - August"
    "B5" "September - October"
    "B6" "November - December"

    "T1" "January - April"
    "T2" "May - August"
    "T3" "September - December"

    "Q1" "January - March"
    "Q2" "April - June"
    "Q3" "July - September"
    "Q4" "October - December"
    ""))
