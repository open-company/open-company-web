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

(defn get-period-string [period]
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

    "Q1" "January - March"
    "Q2" "April - June"
    "Q3" "July - September"
    "Q4" "October - December"

    ""))

(defn month-string [month]
  (case month
    "01" "January"
    "02" "February"
    "03" "March"
    "04" "April"
    "05" "May"
    "06" "June"
    "07" "July"
    "08" "August"
    "09" "September"
    "10" "October"
    "11" "November"
    "12" "December"))

(defn period-string [period]
  (let [[year month] (clojure.string/split period "-")
        month-str (month-string month)]
    (if (or (= month "01") (= month "12"))
      (str month-str " " year)
      month-str)))

(defn get-periods [prefix n]
 (let [r (range 1 (+ n 1))]
    (into [] (for [a r] (str prefix a)))))

(defn format-value [value]
  (.toLocaleString value))