(ns open-company-web.lib.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]
              [open-company-web.lib.iso4217 :refer [iso4217]]
              [cljs.core.async :refer [put!]]
              [open-company-web.router :as router]
              [open-company-web.caches :as caches]))

(defn abs [n] (max n (- n)))

(defn sort-by-key-pred [k & invert]
  (if-not (first invert)
    (fn [a b] (compare (k a) (k b)))
    (fn [a b] (compare (k b) (k a)))))

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
        sym (if (and
                  (contains? currency :symbol)
                  (> (count (:symbol currency)) 0))
                (:symbol currency)
                currency-code)]
    (or sym (:code currency))))

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
  [coll elm]
  (some #(= elm %) coll))

(defn vec-dissoc [coll elem]
  (vec (filter #(not (= elem %)) coll)))

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
    "12" "December"
    ""))

(defn month-short-string [month]
  (case month
    "01" "JAN"
    "02" "FEB"
    "03" "MAR"
    "04" "APR"
    "05" "MAY"
    "06" "JUN"
    "07" "JUL"
    "08" "AUG"
    "09" "SEP"
    "10" "OCT"
    "11" "NOV"
    "12" "DEC"
    ""))

(defn period-string [period & flags]
  (let [force-year (in? flags :force-year)
        short-month-string (in? flags :short-month)
        [year month] (clojure.string/split period "-")
        month-str (if short-month-string 
                    (month-short-string month)
                    (month-string month))]
    (if (or (in? flags :force-year) (= month "01") (= month "12"))
      (str month-str " " year)
      month-str)))

(defn get-periods [prefix n]
 (let [r (range 1 (+ n 1))]
    (into [] (for [a r] (str prefix a)))))

(defn redirect! [loc]
  (set! (.-location js/window) loc))

(defn format-value [value]
  (if (nil? value)
    0
    (.toLocaleString value)))

(defn burn-rate [revenue costs]
  (- revenue costs))

(defn avg-burn-rate [periods]
  (let [burn-rates (map #(burn-rate (:revenue %) (:costs %)) periods)
        tot (count burn-rates)]
    (apply (fn [& items]
             (/ (apply + items) tot)) burn-rates)))

(defn calc-runway
  "Helper function that add burn-rate and runway to each update section"
  [finances-data]
  (let [sort-pred (sort-by-key-pred :period)
        sorted-data (into [] (sort #(sort-pred %1 %2) finances-data))]
    (loop [idx 1
           datas sorted-data]
      (let [start   (max 0 (- idx 3))
            avg     (avg-burn-rate (subvec datas start idx))]
        (if (neg? avg)
          (let [period  (datas (dec idx))
                runway (abs (int (* (/ (:cash period) avg) 30)))
                fixed-period (merge period {:runway runway
                                            :avg-burn-rate avg
                                            :burn-rate (burn-rate (:revenue period) (:costs period))})
                datas   (assoc datas (dec idx) fixed-period)]
            (if (< idx (count sorted-data))
              (recur (inc idx)
                     datas)
              datas))
          (if (< idx (dec (count sorted-data)))
            (recur (inc idx)
                   datas)
            datas))))))

(defn camel-case-str [value]
  (let [upper-value (clojure.string/replace value #"^(\w)" #(clojure.string/upper-case (first %1)))]
    (clojure.string/replace upper-value #"-(\w)"
                            #(str " " (clojure.string/upper-case (second %1))))))

(defn js-date [ & [date-str]]
  (if date-str
    (new js/Date date-str)
    (new js/Date)))

(defn add-zero [v]
  (str (when (< v 10) "0") v))

(defn date-string [js-date & [year]]
  (let [month (month-string (add-zero (inc (.getMonth js-date))))
        day (add-zero (.getDate js-date))]
    (str month " " day (when year (str ", " (.getFullYear js-date))))))

(defn pluralize [string n]
  (if (pos? n)
    (str string "s")
    string))
  
(defn time-since
  "Get a string representing the elapsed time from a date in the past"
  [past-date]
  (let [past-js-date (js-date past-date)
        past (.getTime past-js-date)
        now (.getTime (js-date))
        seconds (.floor js/Math (/ (- now past) 1000))
        years-interval (.floor js/Math (/ seconds 31536000))
        months-interval (.floor js/Math (/ seconds 2592000))
        days-interval (.floor js/Math (/ seconds 86400))
        hours-interval (.floor js/Math (/ seconds 3600))
        minutes-interval (.floor js/Math (/ seconds 60))]
    (cond
      (pos? years-interval)
      (date-string past-js-date true)

      (pos? months-interval)
      (date-string past-js-date)

      (pos? days-interval)
      (str days-interval " " (pluralize "day" days-interval) " ago")

      (pos? hours-interval)
      (str hours-interval " " (pluralize "hour" hours-interval) " ago")

      :else
      "just now")))

(defn get-click-position
  "Return the x position inside the clicked element"
  [event]
  (let [client-x (.-clientX event)
        bound-rect (.getBoundingClientRect (.-target event))
        lf (.-left bound-rect)
        from-left (- client-x lf)]
    from-left))

(defn set-caret-position!
  "Move the caret to the specified position of a certain element"
  [elem caret-pos]
  (when elem
    (cond
      (.-createTextRange elem)
      (let [rg (.createTextRange elem)]
        (.move rg "character" caret-pos)
        (.select rg))

      (.-selectionStart elem)
      (do
        (.focus elem)
        (.setSelectionRange elem caret-pos caret-pos))

      :else
      (.focus elem))))

(defn class-set
  "Given a map of class names as keys return a string of the those classes that evaulates as true"
  [classes]
  (apply str (map #(str " " (name %)) (keys (filter #(second %) classes)))))

(defn period-exists [period data]
  (if (> (count (filter #(= (:period %) period) data)) 0)
    true
    false))

(defn current-period []
  (let [date (js/Date.)
        month (+ (.getMonth date) 1)
        month-str (str (when (< month 10) "0") month)
        cur-period (str (.getFullYear date) "-" month-str)]
    cur-period))

(defn get-section-keys [company-data]
  "Get the section names, as a vector of keywords, in category order and order in the category."
  (vec (map keyword (flatten (map #(get-in company-data [:sections (keyword %)]) (:categories company-data))))))

(defn get-sections [section-keys company-data]
  (loop [ks section-keys
         sections []]
    (if (> (count ks) 0)
      (do
        (let [k (first ks)
              section (k company-data)]
          (recur (subvec ks 1)
                 (conj sections section))))
      sections)))

(def finances-empty-notes {:notes {:body ""}})

(defn fix-finances [section-body]
  (let [finances-data (:data section-body)
        fixed-finances (calc-runway finances-data)
        sort-pred (sort-by-key-pred :period true)
        sorted-finances (sort #(sort-pred %1 %2) fixed-finances)
        fixed-section (assoc section-body :data sorted-finances)
        section-with-notes (merge finances-empty-notes fixed-section)]
    section-with-notes))

(defn fix-section [section-body section-name & [read-only]]
  (let [read-only (or read-only false)
        with-section-key (assoc section-body :section (name section-name))
        with-as-of (assoc with-section-key :as-of (:updated-at with-section-key))
        with-read-only (assoc with-as-of :read-only read-only)]
    (if (= section-name :finances)
      (fix-finances with-read-only)
      with-read-only)))

(defn fix-sections [company-data]
  "add section name in each section and a section sorter"
  (let [section-keys (get-section-keys company-data)]
    (loop [body company-data
           idx  0]
      (if (< idx (count section-keys))
        (let [section-name (section-keys idx)
              section-body (section-name company-data)
              fixed-section (fix-section section-body section-name)
              fixed-body (assoc body section-name fixed-section)]
          (recur fixed-body
                 (inc idx)))
        body))))

(defn sort-revisions [revisions]
  (let [sort-pred (sort-by-key-pred :updated-at)]
    (into [] (sort #(sort-pred %1 %2) revisions))))

(defn as-of [date]
  (let [year (.getFullYear date)
        month (add-zero (inc (.getMonth date)))
        day (add-zero (.getDate date))
        hours (add-zero (.getHours date))
        minutes (add-zero (.getMinutes date))
        seconds (add-zero (.getSeconds date))]
    (str year "-" month "-" day "T" hours ":" minutes ":" seconds "Z")))

(defn as-of-now []
  (let [date (js-date)]
    (as-of date)))

(defn link-for
  ([links rel] (some #(if (= (:rel %) rel) % nil) links))
  ([links rel method] (some #(if (and (= (:method %) method) (= (:rel %) rel)) % nil) links)))

(defn px [n]
  (str n "px"))

(defn select-section-data [section-data section as-of]
  (when as-of
    (let [slug (keyword (:slug @router/path))]
      (if (or (not (contains? (slug @caches/revisions) section))
              (= as-of (:updated-at section-data)))
        section-data
        (((keyword section) (slug @caches/revisions)) as-of)))))

(defn scroll-to-id [id & [duration]]
  (let [section-el (.$ js/window (str "#" id))
        section-offset (.offset section-el)
        top (- (.-top section-offset) 60)]
    (.scrollTo js/$ #js {"top" (str top "px") "left" "0px"} (or duration 500))))

(defn scroll-to-section [section-name]
  (scroll-to-id (str "section-" (name section-name))))

(defn get-quarter-string [quarter & [flags]]
  (case quarter
    "3"
    (if (in? flags :short)
      "First"
      "First quarter")
    "6"
    (if (in? flags :short)
      "Second"
      "Second quarter")
    "9"
    (if (in? flags :short)
      "Third"
      "Third quarter")
    "12"
    (if (in? flags :short)
      "Fourth"
      "Fourth quarter")))

(defn week-number
  "From https://gist.github.com/remvee/2735ee151ab6ec075255
  Week number according to the ISO-8601 standard, weeks starting on
  Monday. The first week of the year is the week that contains that
  year's first Thursday (='First 4-day week'). The highest week number
  in a year is either 52 or 53."
  [ts]
  (let [year (.getFullYear ts)
        month (.getMonth ts)
        date (.getDate ts)
        day (.getDay ts)
        thursday (js/Date. year month (- (+ date 4) (if (= 0 day) 7 day)))
        year-start (js/Date. year 0 1)]
    (Math/ceil (/ (+ (/ (- (.getTime thursday)
                           (.getTime year-start))
                        (* 1000 60 60 24))
                     1)
                  7))))

(defn add-ordinal-indicator [n]
  (case (mod n 10)
    1
    (str n "st")
    2
    (str n "nd")
    3
    (str n "rd")
    (str n "th")))

(defn get-period-string [period interval & [flags]]
  (println "asd" period interval)
  (case interval
    "quarterly"
    (let [[month year] (clojure.string/split period "-")]
      (if (in? flags :short)
        (get-quarter-string month [:short])
        (get-quarter-string month)))
    "monthly"
    (let [[year month] (clojure.string/split period "-")]
      (if (in? flags :short)
        (month-short-string month)
        (month-string month)))
    "weekly"
    (let [week-num (add-ordinal-indicator (week-number (new js/Date period)))]
      (if (in? flags :short)
        (str week-num)
        (str week-num " week")))))