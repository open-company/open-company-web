(ns open-company-web.lib.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]
              [open-company-web.lib.iso4217 :refer [iso4217]]
              [cljs.core.async :refer [put!]]
              [open-company-web.router :as router]))

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
        symbol (if
                (and
                  (contains? currency :symbol)
                  (> (count (:symbol currency)) 0))
                (:symbol currency)
                currency-code)]
    (or symbol (:code currency))))

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
    "12" "December"
    ""))

(defn period-string [period & flags]
  (let [[year month] (clojure.string/split period "-")
        month-str (month-string month)]
    (if (or (in? flags :force-year) (= month "01") (= month "12"))
      (str month-str " " year)
      month-str)))

(defn get-periods [prefix n]
 (let [r (range 1 (+ n 1))]
    (into [] (for [a r] (str prefix a)))))

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
                fixed-period (assoc period :runway runway)
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
  
(defn time-since
  "Get a string representing the elapsed time from a date in the past"
  [past-date]
  (let [past (.getTime (new js/Date past-date))
        now (.getTime (new js/Date))
        seconds (.floor js/Math (/ (- now past) 1000))
        years-interval (.floor js/Math (/ seconds 31536000))]
    (if (> years-interval 1)
      (str years-interval " years ago")
      (let [months-interval (.floor js/Math (/ seconds 2592000))]
        (if (> months-interval 1)
          (str months-interval " months ago")
          (let [days-interval (.floor js/Math (/ seconds 86400))]
            (if (> days-interval 1)
              (str days-interval " days ago")
              (let [hours-interval (.floor js/Math (/ seconds 3600))]
                (if (> hours-interval 1)
                  (str hours-interval " hours ago")
                  (let [minutes-interval (.floor js/Math (/ seconds 60))]
                    (if (> minutes-interval 1)
                      (str minutes-interval " minutes ago")
                      (str "just now"))))))))))))

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
  (let [section-names (:sections company-data)]
    (into [] (map keyword section-names))))

(defn sort-section-keys-func [a b company-data]
  (let [sec1 (a company-data)
        sec2 (b company-data)]
    (compare (:sorter sec2) (:sorter sec1))))

(defn sort-section-keys [company-data]
  (let [section-keys (get-section-keys company-data)]
    (sort #(sort-section-keys-func %1 %2 company-data) section-keys)))

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

(defn sort-sections [company-data]
  (let [section-keys (get-section-keys company-data)
        sections (get-sections section-keys company-data)
        sort-pred (sort-by-key-pred :updated-at true)]
    (sort #(sort-pred %1 %2) sections)))

(defn fix-sections [company-data]
  "add section name in each section and a section sorter"
  (let [section-keys (get-section-keys company-data)]
    (loop [sections section-keys
           body company-data]
      (if (pos? (count sections))
        (let [cur-key (first sections)
              section (cur-key body)
              updated-section (assoc section :section (name cur-key))
              updated-section (assoc updated-section :sorter (:updated-at section))
              updated-body (assoc body cur-key updated-section)]
          (recur (subvec sections 1)
                 updated-body))
        body))))

(defn sort-revisions [revisions]
  (let [sort-pred (sort-by-key-pred :updated-at)]
    (into [] (sort #(sort-pred %1 %2) revisions))))

(defn add-zero [v]
  (str (when (< v 10) "0") v))

(defn as-of [date]
  (let [year (.getFullYear date)
        month (add-zero (inc (.getMonth date)))
        day (add-zero (.getDate date))
        hours (add-zero (.getHours date))
        minutes (add-zero (.getMinutes date))
        seconds (add-zero (.getSeconds date))]
    (str year "-" month "-" day "T" hours ":" minutes ":" seconds "Z")))

(defn as-of-now []
  (let [date (new js/Date)]
    (as-of date)))

(defn link-for
  ([links rel] (some #(if (= (:rel %) rel) % nil) links))
  ([links rel method] (some #(if (and (= (:method %) method) (= (:rel %) rel)) % nil) links)))