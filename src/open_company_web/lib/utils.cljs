(ns open-company-web.lib.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]
              [open-company-web.lib.iso4217 :refer [iso4217]]
              [cljs.core.async :refer [put!]]
              [open-company-web.router :as router]
              [open-company-web.caches :as caches]
              [cljs-time.format :as cljs-time-format]
              [cljs-time.core :as cljs-time]))

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
        sym (if (and (contains? currency :symbol)
                     (> (count (:symbol currency)) 0))
              (:symbol currency)
              "$")]
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

(defn month-string [month & [flags]]
  (let [short-month (in? flags :short)]
    (case month
      "01" (if short-month
             "JAN"
             "January")
      "02" (if short-month
             "FEB"
             "February")
      "03" (if short-month
             "MAR"
             "March")
      "04" (if short-month
             "APR"
             "April")
      "05" (if short-month
             "MAY"
             "May")
      "06" (if short-month
             "JUN"
             "June")
      "07" (if short-month
             "JUL"
             "July")
      "08" (if short-month
             "AUG"
             "August")
      "09" (if short-month
             "SEP"
             "September")
      "10" (if short-month
             "OCT"
             "October")
      "11" (if short-month
             "NOV"
             "November")
      "12" (if short-month
             "DEC"
             "December")
      "")))

(defn month-string-int [month & [flags]]
  (let [short-month (in? flags :short)]
    (case month
      1 (if short-month
          "JAN"
          "January")
      2 (if short-month
          "FEB"
          "February")
      3 (if short-month
          "MAR"
          "March")
      4 (if short-month
          "APR"
          "April")
      5 (if short-month
          "MAY"
          "May")
      6 (if short-month
          "JUN"
          "June")
      7 (if short-month
          "JUL"
          "July")
      8 (if short-month
          "AUG"
          "August")
      9 (if short-month
          "SEP"
          "September")
      10 (if short-month
          "OCT"
          "October")
      11 (if short-month
          "NOV"
          "November")
      12 (if short-month
          "DEC"
          "December")
      "")))

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

(defn get-month [period]
  (let [[year month] (clojure.string/split period "-")]
    (month-short-string month)))

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

(defn calc-burn-rate [revenue costs]
  (- revenue costs))

(defn calc-avg-burn-rate [periods]
  (let [burn-rates (map #(calc-burn-rate (:revenue %) (:costs %)) periods)
        tot (count burn-rates)]
    (apply (fn [& items]
             (/ (apply + items) tot)) burn-rates)))

(defn calc-runway [cash burn-rate]
  (int (* (/ cash burn-rate) 30)))

(defn calc-burnrate-runway
  "Helper function that add burn-rate and runway to each update section"
  [finances-data]
  (if (empty? finances-data)
    finances-data
    (let [sort-pred (sort-by-key-pred :period)
          sorted-data (into [] (sort #(sort-pred %1 %2) finances-data))]
      (loop [idx 1
             datas sorted-data]
        (let [start (max 0 (- idx 3))
              avg-burn-rate (calc-avg-burn-rate (subvec datas start idx))]
          (let [period  (datas (dec idx))
                runway (calc-runway (:cash period) avg-burn-rate) 
                fixed-period (merge period {:runway runway
                                            :avg-burn-rate avg-burn-rate
                                            :burn-rate (calc-burn-rate (:revenue period) (:costs period))})
                datas   (assoc datas (dec idx) fixed-period)]
            (if (< idx (count sorted-data))
              (recur (inc idx)
                     datas)
              datas)))))))

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

(defn add-zeros [v]
  (str (cond
        (< v 10) "00"
        (< v 100) "0")
    v))

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
  (vec (map keyword (flatten (remove nil? (map #(get-in company-data [:sections (keyword %)]) (:categories company-data)))))))

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
  (let [finances-data (if (contains? section-body :data) (:data section-body) [])
        fixed-finances (calc-burnrate-runway finances-data)
        sort-pred (sort-by-key-pred :period true)
        sorted-finances (sort #(sort-pred %1 %2) fixed-finances)
        fixed-section (assoc section-body :data sorted-finances)
        section-with-notes (merge finances-empty-notes fixed-section)]
    section-with-notes))

(defn fix-section 
  "Add `:section` name, `:as-of` and `:read-only` keys to the section map"
  [section-body section-name & [read-only]]
  (let [read-only (or read-only false)
        with-read-only (-> section-body
                        (assoc :section (name section-name))
                        (assoc :as-of (:updated-at section-body))
                        (assoc :read-only read-only))]
    (if (= section-name :finances)
      (fix-finances with-read-only)
      with-read-only)))

(defn fix-sections [company-data]
  "Add section name in each section and a section sorter"
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
        seconds (add-zero (.getSeconds date))
        millis (add-zeros (.getMilliseconds date))]
    (str year "-" month "-" day "T" hours ":" minutes ":" seconds "." millis "Z")))

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

(defn scroll-top [selector]
  (let [section-el (.$ js/window selector)
        section-offset (.offset section-el)]
    (.-top section-offset)))

(defn scroll-top-with-id [id]
  (scroll-top (str "#" id)))

(defn scroll-to-id [id & [duration]]
  (let [top (- (scroll-top-with-id id) 60)]
    (.scrollTo js/$ #js {"top" (str top "px") "left" "0px"} (or duration 500))))

(defn scroll-to-section [section-name]
  (scroll-to-id (str "section-" (name section-name))))

(defn scroll-toc-to-id [id]
  (let [top (scroll-top-with-id id)]
    (.scrollTo (.$ js/window ".table-of-contents-inner")
               #js {"top" (str top "px") "left" "0px"} 500)))

(defn get-quarter-from-month [month & [flags]]
  (let [short-str (in? flags :short)]
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


(def quarterly-input-format (cljs-time-format/formatter "MM-yyyy"))
(def monthly-input-format (cljs-time-format/formatter "yyyy-MM"))
(def weekly-input-format (cljs-time-format/formatter "yyyy-MM-dd"))

(defn get-formatter [interval]
  "Get the date formatter from the interval type."
  (case interval
    "quarterly"
    quarterly-input-format
    "monthly"
    monthly-input-format
    "weekly"
    weekly-input-format
    :else
    weekly-input-format))

(defn get-period-string [period interval & [flags]]
  "Get descriptive string for the period by interval. Use :short as a flag to get
  the short formatted string."
  (let [formatter (get-formatter interval)
        date (cljs-time-format/parse formatter period)]
    (case interval
      "quarterly"
      (str (get-quarter-from-month (cljs-time/month date) flags) " " (cljs-time/year date))
      "monthly"
      (str (month-string-int (cljs-time/month date) flags))
      "weekly"
      (str (month-string-int (cljs-time/month date) flags) " " (cljs-time/day date)))))

(defn update-page-title [title]
  (set! (.-title js/document) title))

(defn periods-diff-in-months [first-period last-period]
  (let [[first-year first-month] (clojure.string/split first-period "-")
        [last-year last-month] (clojure.string/split last-period "-")
        first-date (cljs-time/date-time (int first-year) (int first-month))
        last-date (cljs-time/date-time (int last-year) (int last-month))]
    (cljs-time/in-months (cljs-time/interval first-date last-date))))