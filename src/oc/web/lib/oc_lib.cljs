(ns oc.web.lib.oc-lib
  "Port of some function included in oc.lib.data.utils of https://github.com/open-company/open-company-lib"
  (:require [defun.core :refer (defun)]
            [cuerdas.core :as s]
            [goog.string :as gstring]
            [cljs-time.extend]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.iso4217 :refer (iso4217)]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

;; Porting:
; Math/ -> js/Math. floor round abs
; format -> goog.string.format
; biginteger -> nothing

(def intervals #{:weekly :monthly :quarterly})

(def yearly-date (f/formatter "YYYY"))
(def quarterly-date (f/formatter "MMM YYYY"))
(def monthly-date (f/formatter "MMM YYYY"))
(def weekly-date (f/formatter "d MMM YYYY"))

(defn- get-quarter-from-month [month & [flags]]
  (let [short-str (utils/in? flags :short)]
    (cond
      (and (>= month 1) (<= month 3))
      (if short-str
        "Q1"
        "January - March")
      (and (>= month 4) (<= month 6))
      (if short-str
        "Q2"
        "April - June")
      (and (>= month 7) (<= month 9))
      (if short-str
        "Q3"
        "July - September")
      (and (>= month 10) (<= month 12))
      (if short-str
        "Q4"
        "October - December"))))

(defn format-period [interval period]
  (s/upper
    (case interval
      "quarterly" (str (get-quarter-from-month (t/month period) [:short]) " " (f/unparse yearly-date period))
      "weekly" (f/unparse weekly-date period)
      (f/unparse monthly-date period))))

(defun contiguous
  "Starting with the most recent period in the sequence, return the longest list of contiguous periods that exists.

  E.g.

  (contiguous ['2016-10' 2016-08' '2016-09' '2016-06'] :monthly) => ['2016-10' '2016-09' '2016-08']
  "

  ;; Defaults to monthly
  ([periods :guard sequential?] (contiguous periods :monthly))

  ;; Use keyword for interval
  ([periods :guard sequential? interval :guard string?] (contiguous periods (keyword interval)))

  ;; Empty case
  ([_periods :guard #(and (sequential? %) (empty? %)) _interval :guard intervals] [])

  ;; Only one period case
  ([periods :guard #(and (sequential? %) (= (count %) 1)) _interval :guard intervals] (vec periods))

  ;; Gone through all the periods with continuity, return them all as contiguous
  ([_periods :guard empty? contiguous-periods _interval] contiguous-periods)

  ;; All intervals - initial state
  ([periods :guard sequential? interval :guard intervals]
  (let [sorted-periods (reverse (sort periods))]
    (contiguous (rest sorted-periods) [(first sorted-periods)] interval))) ; start w/ the oldest period as contiguous list

  ;; Weekly - initial state
  ([periods :guard sequential? contiguous-periods :guard vector? interval :guard #{:weekly}]
    (contiguous periods contiguous-periods interval utils/weekly-input-format (t/weeks 1)))
  ;; Monthly - initial state
  ([periods :guard sequential? contiguous-periods :guard vector? interval :guard #{:monthly}]
    (contiguous periods contiguous-periods interval utils/monthly-input-format (t/months 1)))
  ;; Quarterly- initial state
  ([periods :guard sequential? contiguous-periods :guard vector? interval :guard #{:quarterly}]
    (contiguous periods contiguous-periods interval utils/quarterly-input-format (t/months 3)))

  ;; All intervals - progressing
  ([periods :guard sequential? contiguous-periods :guard vector? interval :guard intervals formatter one-interval]
    (let [current-period (f/parse formatter (last contiguous-periods))
          prior-period (f/parse formatter (first periods))
          prior-contiguous-period (t/minus current-period one-interval)]
      (if (= prior-period prior-contiguous-period)
        (contiguous (rest periods) (conj contiguous-periods (first periods)) interval) ; we found another!
        contiguous-periods)))) ; That's all there is that are contiguous

(defn remove-trailing
  ""
  [value]
  {:pre [(string? value)]
   :post [(string? %)]}
  (if-not (or (and (s/ends-with? value "0") (s/includes? value "."))
              (s/ends-with? value "."))
    value
    (remove-trailing (subs value 0 (dec (count value))))))

(defn truncate-decimals
  "Round and truncate to a float value to at most the specified number of decimal places,
  leaving no trailing 0's to the right of the decimal."
  [value decimals]
  {:pre [(number? value) (pos? decimals) (integer? decimals)]
   :post [(string? %)]}
  (let [exp (js/Math.pow 10 decimals)]
    (remove-trailing (gstring/format (str "%." decimals "f") (float (/ (js/Math.round (* exp value)) exp))))))

(defn with-size-label [orig-value]
  (when orig-value
    (let [neg (neg? orig-value)
          value (js/Math.abs orig-value)
          short-value (cond
                        ; 100M
                        (>= value 100000000)
                        (str (truncate-decimals (int (/ value 1000000)) 2) "M")
                        ; 10.0M
                        (>= value 10000000)
                        (str (truncate-decimals (/ value 1000000) 1) "M")
                        ; 1.00M
                        (>= value 1000000)
                        (str (truncate-decimals (/ value 1000000) 2) "M")
                        ; 100K
                        (>= value 100000)
                        (str (truncate-decimals (int (/ value 1000)) 2) "K")
                        ; 10.0K
                        (>= value 10000)
                        (str (truncate-decimals (/ value 1000) 1) "K")
                        ; 1.00K
                        (>= value 1000)
                        (str (truncate-decimals (/ value 1000) 2) "K")
                        ; 100
                        (>= value 100)
                        (str (truncate-decimals (int value) 2))
                        ; 10.0
                        (>= value 10)
                        (str (truncate-decimals value 1))
                        ; 1.00
                        :else
                        (str (truncate-decimals value 2)))]
      (if neg
        (str "-" short-value)
        short-value))))

(defn with-currency
  "Combine the value with the currency indicator, if available."

  ([currency value] (with-currency currency value false))

  ([currency value explicit-positive?]
  (let [value-string (str value)
        negative? (s/starts-with? value "-")
        positive? (and (not negative?) explicit-positive?)
        neg (when negative? "-")
        pos (when positive? "+")
        clean-value (if negative? (subs value-string 1) value-string)
        currency-key (keyword currency)
        currency-entry (iso4217 currency-key)
        currency-symbol (if currency-entry (:symbol currency-entry) false)
        currency-text (if currency-entry (:text currency-entry) false)]
    (if currency-symbol
      (str neg pos currency-symbol clean-value)
      (if currency-text
        (str pos value-string " " currency-text)
        (str pos value-string))))))