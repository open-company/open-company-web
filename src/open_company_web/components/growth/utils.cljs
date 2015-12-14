(ns open-company-web.components.growth.utils
  (:require [open-company-web.lib.utils :as utils]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn columns-num [interval]
  (case interval
    "quarterly" 8
    "montyly" 12
    "weekly" 8))

(defn get-minus [diff interval]
  (case interval
    "quarterly" (t/months (* diff 3))
    "monthly" (t/months diff)
    "weekly" (t/weeks diff)))

(defn- get-past-period [period diff interval]
  (let [[year month] (clojure.string/split period "-")
        period-date (t/date-time (int year) (int month))
        past-date (t/minus period-date (get-minus diff interval))
        formatter (utils/get-formatter interval)]
    (f/unparse formatter past-date)))