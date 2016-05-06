(ns open-company-web.lib.utils
  (:require [om.core :as om :include-macros true]
            [clojure.string]
            [dommy.core :refer-macros (sel1)]
            [cljs.core.async :refer (put!)]
            [cljs-time.format :as cljs-time-format]
            [cljs-time.core :as cljs-time]
            [goog.fx.dom :refer (Scroll)]
            [goog.string :as gstring]
            [goog.i18n.NumberFormat :as nf]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.lib.iso4217 :refer (iso4217)]
            [open-company-web.caches :refer (company-cache)]
            [open-company-web.local-settings :as ls])
  (:import  [goog.i18n NumberFormat]))

(defn abs [n] (when n (max n (- n))))

(def oc-animation-duration 300)

(defn sort-by-key-pred [k & invert]
  (if-not (first invert)
    (fn [a b] (compare (k a) (k b)))
    (fn [a b] (compare (k b) (k a)))))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn get-currency [currency-code]
  (let [kw (keyword currency-code)]
    (when (contains? iso4217 kw) (kw iso4217))))

(defn get-symbol-for-currency-code [currency-code]
  (let [currency (get-currency currency-code)
        sym (if (and (contains? currency :symbol)
                     (not (clojure.string/blank? (:symbol currency))))
              (:symbol currency)
              currency-code)]
    (or sym (:code currency))))

(def channel-coll (atom {}))

(defn add-channel [channel-name channel]
  (swap! channel-coll assoc channel-name channel))

(defn get-channel [channel-name]
  (@channel-coll channel-name))

(defn remove-channel [channel-name]
  (swap! channel-coll dissoc channel-name))

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
  (vec (filter #(not= elem %) coll)))

;; TODO use goog.i18n.DateTimeFormat here
(defn month-string [month & [flags]]
  (let [short-month (in? flags :short)]
    (case (.parseInt js/window month 10)
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
             "December"))))

;; TODO use goog.i18n.DateTimeFormat here
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

;; TODO use goog.i18n.DateTimeFormat here
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

(defn format-value [value]
  (if (nil? value)
    0
    (.toLocaleString value)))

(defn calc-burn-rate [revenue costs]
  (- revenue costs))

(defn calc-avg-burn-rate [periods]
  (when (pos? (count periods))
    (let [burn-rates (map #(calc-burn-rate (:revenue %) (:costs %)) periods)
          tot (count burn-rates)]
      (apply (fn [& items]
               (/ (apply + items) tot)) burn-rates))))

(defn calc-runway [cash burn-rate]
  (int (* (/ cash burn-rate) 30)))

(defn calc-burnrate-runway
  "Helper function that add burn-rate and runway to each update section"
  [finances-data]
  (if (empty? finances-data)
    finances-data
    (let [sort-pred (sort-by-key-pred :period)
          sorted-data (vec (sort sort-pred finances-data))]
      (vec (map
              (fn [data]
                (let [idx (inc (.indexOf (to-array sorted-data) data))
                      start (max 0 (- idx 3))
                      sub-data (subvec sorted-data start idx)
                      avg-burn-rate (calc-avg-burn-rate sub-data)
                      burn-rate (calc-burn-rate (:revenue data) (:costs data))
                      runway (calc-runway (:cash data) burn-rate)]
                  (merge data {:runway runway
                               :avg-burn-rate avg-burn-rate
                               :burn-rate burn-rate})))
              sorted-data)))))

(defn camel-case-str [value]
  (when value
    (let [upper-value (clojure.string/replace value #"^(\w)" #(clojure.string/upper-case (first %1)))]
      (clojure.string/replace upper-value #"-(\w)"
                              #(str " " (clojure.string/upper-case (second %1)))))))

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

;; TODO use goog.i18n.DateTimeFormat here
(defn date-string [js-date & [year]]
  (let [month (month-string (add-zero (inc (.getMonth js-date))))
        day (.getDate js-date)]
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
        hours-interval (.floor js/Math (/ seconds 3600))]
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

(defn class-set
  "Given a map of class names as keys return a string of the those classes that evaulates as true"
  [classes]
  (clojure.string/join (map #(str " " (name %)) (keys (filter second classes)))))

(defn period-exists [period data]
  (if (pos? (count (filter #(= (:period %) period) data)))
    true
    false))

(defn current-period []
  (let [date (js/Date.)
        month (inc (.getMonth date))
        month-str (str (when (< month 10) "0") month)
        cur-period (str (.getFullYear date) "-" month-str)]
    cur-period))

(defn get-section-keys [company-data]
  "Get the section names, as a vector of keywords, in category order and order in the category."
  (vec (map keyword (flatten (remove nil? (map #(get-in company-data [:sections (keyword %)]) (:categories company-data)))))))

(def finances-empty-notes {:notes {:body ""}})

(defn link-for
  ([links rel] (some #(when (= (:rel %) rel) %) links))
  ([links rel method] (some #(when (and (= (:method %) method) (= (:rel %) rel)) %) links)))

(defn readonly? [links]
  (let [update (link-for links "update" "PUT")
        partial-update (link-for links "partial-update" "PATCH")
        delete (link-for links "delete" "DELETE")]
    (or (nil? update) (nil? partial-update))))

(defn fix-finances [section-body]
  (let [finances-data (if (contains? section-body :data) (:data section-body) [])
        fixed-finances (calc-burnrate-runway finances-data)
        sort-pred (sort-by-key-pred :period true)
        sorted-finances (sort sort-pred fixed-finances)
        fixed-section (assoc section-body :data sorted-finances)
        section-with-notes (merge finances-empty-notes fixed-section)]
    section-with-notes))

(defn fix-section 
  "Add `:section` name, `:as-of` and `:read-only` keys to the section map"
  [section-body section-name & [read-only force-write]]
  (let [read-only (if force-write
                    false
                    (or read-only (readonly? (:links section-body)) false))
        with-read-only (-> section-body
                        (assoc :section (name section-name))
                        (assoc :as-of (:updated-at section-body))
                        (assoc :read-only read-only))]
    (if (= section-name :finances)
      (fix-finances with-read-only)
      with-read-only)))

(defn fix-sections [company-data]
  "Add section name in each section and a section sorter"
  (let [links (:links company-data)
        section-keys (get-section-keys company-data)
        read-only (readonly? links)
        without-sections (apply dissoc company-data section-keys)
        with-read-only (assoc without-sections :read-only read-only)
        sections (into {} (map
                           (fn [sn] [sn (fix-section (sn company-data) sn)])
                           section-keys))
        with-fixed-sections (merge with-read-only sections)]
    with-fixed-sections))

(defn sort-revisions [revisions]
  (let [sort-pred (sort-by-key-pred :updated-at)]
    (vec (sort sort-pred revisions))))

(defn revision-next
  "Return the first future revision"
  [revisions as-of]
  (when (pos? (count revisions))
    (first (remove nil? (map
                          (fn [r]
                            (when (= (:updated-at r) as-of)
                              (let [idx (.indexOf (to-array revisions) r)]
                                (get revisions (inc idx)))))
                          revisions)))))

(defn revision-prev
  "Return the first future revision"
  [revisions as-of]
  (when (pos? (count revisions))
    (first (remove nil? (map
                          (fn [r]
                            (when (= (:updated-at r) as-of)
                              (let [idx (.indexOf (to-array revisions) r)]
                                (get revisions (dec idx)))))
                          revisions)))))

(defn as-of-now []
  (let [date (js-date)]
    (.toISOString date)))

(defn px [n]
  (str n "px"))

(defn select-section-data [section-data section as-of]
  (when (or as-of (:placeholder section-data))
    (let [slug (keyword (router/current-company-slug))]
      (if (or (not (contains? (slug @caches/revisions) section))
              (= as-of (:updated-at section-data)))
        section-data
        (((keyword section) (slug @caches/revisions)) as-of)))))

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


(def quarterly-input-format (cljs-time-format/formatter "yyyy-MM"))
(def monthly-input-format (cljs-time-format/formatter "yyyy-MM"))
(def weekly-input-format (cljs-time-format/formatter "yyyy-MM-dd"))

(defn get-formatter [interval]
  "Get the date formatter from the interval type."
  (case interval
    "quarterly"
    quarterly-input-format
    "weekly"
    weekly-input-format
    ; else
    monthly-input-format))

(defn date-from-period [period & [interval]]
  (cljs-time-format/parse (get-formatter interval) period))

(defn period-from-date [date & [interval]]
  (cljs-time-format/unparse (get-formatter interval) date))

(def default-growth-interval "monthly")

(defn get-period-string [period & [interval flags]]
  "Get descriptive string for the period by interval. Use :short as a flag to get
  the short formatted string."
  (when period
    (let [fixed-interval (or interval default-growth-interval)
          parsed-date (date-from-period period fixed-interval)
          month (cljs-time/month parsed-date)
          year (cljs-time/year parsed-date)
          fixed-year (if (in? flags :short-year) (str "'" (subs (str year) 2 4)) (str year))
          plus-one-week-year (cljs-time/year (cljs-time/plus parsed-date (cljs-time/days 7)))
          minus-one-week-year (cljs-time/year (cljs-time/minus parsed-date (cljs-time/days 7)))
          needs-year (or (in? flags :force-year)
                         (case fixed-interval
                           "weekly"
                           (or (not= plus-one-week-year year) (not= minus-one-week-year year))
                           "quarterly"
                           (or (= month 1) (= month 10))
                           ;else
                           (or (= month 1) (= month 12))))]
      (case fixed-interval
        "quarterly"
        (str (get-quarter-from-month (cljs-time/month parsed-date) flags)
             (when needs-year
               (str " " fixed-year)))
        "monthly"
        (str (month-string-int (cljs-time/month parsed-date) flags)
             (when needs-year
               (str " " fixed-year)))
        "weekly"
        (str (month-string-int (cljs-time/month parsed-date) flags)
             " "
             (cljs-time/day parsed-date)
             (when needs-year
               (str ", " fixed-year)))))))

(defn update-page-title [title]
  (set! (.-title js/document) title))

(defn periods-diff [first-period last-period & [interval]]
  (let [fixed-interval (or interval default-growth-interval)
        first-date (date-from-period first-period fixed-interval)
        last-date (date-from-period last-period fixed-interval)]
    (case fixed-interval
      "quarterly" (/ (cljs-time/in-months (cljs-time/interval first-date last-date)) 3)
      "monthly" (cljs-time/in-months (cljs-time/interval first-date last-date))
      "weekly" (cljs-time/in-weeks (cljs-time/interval first-date last-date)))))

(defn get-year [period & [interval]]
  (let [fixed-interval (or interval "monthly")
        date (date-from-period period fixed-interval)]
    (cljs-time/year date)))

(defn get-weekly-period-day [period]
  (let [date (date-from-period period "weekly")]
    (cljs-time/day date)))

(defn get-month [period & [interval]]
  (let [fixed-interval (or interval default-growth-interval)
        date (date-from-period period fixed-interval)
        month (add-zero (cljs-time/month date))]
    (case interval
      "quarterly"
      (month-short-string month)
      "weekly"
      (month-short-string month)
      ;else
      (month-short-string month))))

(defn get-month-quarter [current-month]
  (case
    (and (> current-month 1)
         (< current-month 4))
    1
    (and (> current-month 4)
         (< current-month 7))
    4
    (and (> current-month 7)
         (< current-month 9))
    7
    (> current-month 9)
    10))

(defn non-zero-number?
  "Return `true` if the arg is both a number and is not zero, otherwise return `false`."
  [number]
  (and (number? number)
       (not (zero? number))))

(defn no-finances-data?
  "Return `true` if the passed finances data has no substantive data, otherwise return `false`."
  [data]
  (not-any? #(or (non-zero-number? (:cash %))
                 (non-zero-number? (:revenue %))
                 (non-zero-number? (:costs %))) data))

(defn no-growth-data?
  "Return `true` if the passed finances data has no substantive data, otherwise return `false`."
  [data]
  (not-any? #(non-zero-number? (:value %)) (vals data)))

(defn current-growth-period [interval]
  (let [fixed-interval (or interval default-growth-interval)
        now (cljs-time/now)
        year (cljs-time/year now)
        month (cljs-time/month now)]
    (case interval
      "quarterly"
      (str year "-" (add-zero (get-month-quarter month)))
      "weekly"
      (let [day-of-week (cljs-time/day-of-week now)
            to-monday (dec day-of-week)
            monday-date (cljs-time/minus now (cljs-time/days to-monday))]
        (str (cljs-time/year monday-date) "-" (add-zero (cljs-time/month monday-date)) "-" (add-zero (cljs-time/day monday-date))))
      ;; Default tp monthly
      (str year "-" (add-zero month)))))

(defn company-cache-key [k & [v]]
  (let [slug (keyword (router/current-company-slug))
        cc (slug @company-cache)]
    (when v
      (swap! company-cache assoc-in [slug k] v))
    (get cc k nil)))

(defn clean-company-caches []
  (reset! company-cache {}))

(defn thousands-separator
  ([number]
    (.format (NumberFormat. nf/Format.DECIMAL) number))
  ([number currency-code]
    (.format (NumberFormat. nf/Format.CURRENCY currency-code nf/CurrencyStyle.LOCAL) number))
  ([number currency-code decimals]
    (-> (NumberFormat. nf/Format.CURRENCY currency-code nf/CurrencyStyle.LOCAL)
        (.setMinimumFractionDigits (min 2 decimals))
        (.format number))))

(defn offset-top [elem]
  (let [bound-rect (.getBoundingClientRect elem)]
    (.-top bound-rect)))

(defn scroll-to-y [scroll-y & [duration]]
  (.play
    (new Scroll
         (.-body js/document)
         #js [0 (.-scrollTop (.-body js/document))]
         #js [0 scroll-y]
         (or duration oc-animation-duration))))

(defn scroll-to-element [elem]
  (let [elem-scroll-top (offset-top elem)]
    (scroll-to-y elem-scroll-top)))

(defn scroll-top-with-id [id]
  (offset-top (sel1 (str "#" id))))

(defn scroll-to-id [id & [duration]]
  (let [body-scroll-top (.-scrollTop (.-body js/document))
        top (- (+ (scroll-top-with-id id) body-scroll-top) 50)]
    (scroll-to-y top (or duration oc-animation-duration))))

(defn scroll-to-section [section-name]
  (scroll-to-id (str "section-" (name section-name))))

(def _mobile (atom -1))

(def big-web-min-width 768)

(defn set-browser-type! []
  (let [force-mobile-cookie (cook/get-cookie :force-browser-type)
        is-big-web (if (.-body js/document)
                      (>= (.-clientWidth (.-body js/document)) big-web-min-width)
                      true) ; to not break tests
        fixed-browser-type (if (nil? force-mobile-cookie)
                            (not is-big-web)
                            (if (= force-mobile-cookie "mobile")
                             true
                             false))]
  (reset! _mobile fixed-browser-type)))

(defn is-mobile []
 ; fake the browser type for the moment
 (when (neg? @_mobile)
  (set-browser-type!))
 @_mobile)

(defn get-topic-body [section-data section]
  (let [section-kw (keyword section)]
    (if (#{:finances :growth} section-kw)
      (get-in section-data [:notes :body])
      (:body section-data))))

(defn round-2-dec [value decimals]
  ; cut to 2 dec maximum then parse to float to use toString to remove trailing zeros
  (.toLocaleString (js/parseFloat (gstring/format (str "%." decimals "f") value))))

(defn with-metric-prefix [value]
  (when value
    (cond
      ; 100M
      (>= value 100000000)
      (str (round-2-dec (int (/ value 1000000)) 2) "M")
      ; 10.0M
      (>= value 10000000)
      (str (round-2-dec (/ value 1000000) 1) "M")
      ; 1.00M
      (>= value 1000000)
      (str (round-2-dec (/ value 1000000) 2) "M")
      ; 100K
      (>= value 100000)
      (str (round-2-dec (int (/ value 1000)) 2) "K")
      ; 10.0K
      (>= value 10000)
      (str (round-2-dec (/ value 1000) 1) "K")
      ; 1.00K
      (>= value 1000)
      (str (round-2-dec (/ value 1000) 2) "K")
      ; 100
      (>= value 100)
      (str (round-2-dec (int value) 2))
      ; 10.0
      (>= value 10)
      (str (round-2-dec value 1))
      ; 1.00
      :else
      (str (round-2-dec value 2)))))

(defn is-test-env? []
  (not (not (.-_phantom js/window))))

(defonce overlay-max-win-height 670)

(defn absolute-offset [element]
  (loop [top 0
         left 0
         el element]
    (if-not el
      {:top top
       :left left}
      (recur (+ top (or (.-offsetTop el) 0))
             (+ left (or (.-offsetLeft el) 0))
             (.-offsetParent el)))))

(defn medium-editor-options [placeholder] {
  :toolbar #js {
    :buttons #js ["bold" "italic" "underline" "strikethrough" "h2" "orderedlist" "unorderedlist" "anchor" "image"]
  }
  :anchorPreview #js {
    :hideDelay 500
    :previewValueSelector "a"
  }
  :anchor #js {
    ;; These are the default options for anchor form,
    ;; if nothing is passed this is what it used
    :customClassOption nil
    :customClassOptionText "Button"
    :linkValidation false
    :placeholderText "Paste or type a link"
    :targetCheckbox false
    :targetCheckboxText "Open in new window"
  }
  :placeholder #js {
    :text placeholder
    :hideOnClick true
  }})

(defn after [ms fn]
  (js/setTimeout fn ms))

(defn columns-num []
  (let [win-width (.-clientWidth (.-body js/document))]
    (cond
      (>= win-width 1006)
      3
      (>= win-width 684)
      2
      :else
      1)))

(defn emojify
  "Take a string containing either Unicode emoji (mobile device keyboard), short code emoji (web app),
  or ASCII emoji (old skool) and convert it to HTML string ready to be added to the DOM (dangerously)
  with emoji image tags via the Emoji One lib and resources."
  [text]
  ;; use an SVG sprite map
  (set! (.-imageType js/emojione) "svg")
  (set! (.-sprites js/emojione) true)
  (set! (.-imagePathSVGSprites js/emojione) "/img/emojione.sprites.svg")
  ;; convert textual emoji's into SVG elements
  (set! (.-ascii js/emojione) true)
  (let [text-string (or text "") ; handle nil
        unicode-string (.toImage js/emojione text-string)]
    (clj->js {"__html" unicode-string})))