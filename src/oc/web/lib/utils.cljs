(ns oc.web.lib.utils
  (:require [om.core :as om :include-macros true]
            [clojure.string]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cljs.core.async :refer (put!)]
            [cljs-time.format :as cljs-time-format]
            [cljs-time.core :as cljs-time]
            [goog.style :refer (setStyle)]
            [goog.format.EmailAddress :as email]
            [goog.fx.dom :refer (Scroll)]
            [goog.string :as gstring]
            [goog.i18n.NumberFormat :as nf]
            [goog.object :as gobj]
            [oc.web.dispatcher :as dis]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.oc-colors :refer (get-color-by-kw)]
            [cuerdas.core :as s]
            [cljsjs.emojione] ; pulled in for cljsjs externs
            [defun.core :refer (defun)]
            [cljsjs.web-animations]
            [cljs.reader :as reader])
  (:import  [goog.i18n NumberFormat]))

(defn abs
  "(abs n) is the absolute value of n"
  [n]
  (cond
   (not (number? n)) n ; non-sensical, so leave it alone
   (neg? n) (- n)
   :else n))

(def oc-animation-duration 300)

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))

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
  (filterv #(not= elem %) coll))

(defn full-month-string
  [month]
  (case month
    1 "January"
    2 "February"
    3 "March"
    4 "April"
    5 "May"
    6 "June"
    7 "July"
    8 "August"
    9 "September"
    10 "October"
    11 "November"
    12 "December"
    ""))

;; TODO use goog.i18n.DateTimeFormat here
(defn month-string-int
 "Return the name of the month given the integer month number. Accept flags to transform the string:
  - :short Return only the first 3 letters of the month name: JAN
  - :capitalize Return the capitalized name: January
  - :uppercase Return the uppercase: JANUARY"
  [month & [flags]]
  (let [short-month (in? flags :short)
        capitalize (in? flags :capitalize)
        uppercase (in? flags :uppercase)
        month-string (full-month-string month)
        short-string (if short-month (subs month-string 0 3) month-string)
        capitalized-string (if capitalize (s/capital short-string) short-string)
        uppercase-string (if uppercase (s/upper capitalized-string) capitalized-string)]
    uppercase-string))

(defn month-string [month & [flags]]
  (month-string-int (.parseInt js/window month 10) flags))

;; TODO use goog.i18n.DateTimeFormat here
(defn month-short-string [month]
  (month-string-int (.parseInt js/window month 10) [:short :uppercase]))

(defn format-value [value]
  (if (nil? value)
    0
    (.toLocaleString value)))

(defn calc-burn-rate [revenue costs]
  (- revenue costs))

(defn calc-runway [cash burn-rate]
  (int (* (/ cash burn-rate) 30)))

(defn calc-burnrate-runway
  "Helper function that add burn-rate and runway to each update topic"
  [finances-data]
  (if (empty? finances-data)
    finances-data
    (let [sort-pred (fn [a b] (compare (:period a) (:period b)))
          sorted-data (vec (sort sort-pred finances-data))]
      (vec (map
              (fn [data]
                (let [idx (inc (.indexOf (to-array sorted-data) data))
                      start (max 0 (- idx 3))
                      sub-data (subvec sorted-data start idx)
                      burn-rate (calc-burn-rate (:revenue data) (:costs data))
                      runway (calc-runway (:cash data) burn-rate)]
                  (merge data {:runway runway
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
(defn date-string [js-date & [flags]]
  (let [month (month-string
               (add-zero (inc (.getMonth js-date)))
               (when (or (in? flags :short-month) (in? flags :short))
                [:short]))
        day (.getDate js-date)]
    (str month " " day (when (in? flags :year) (str ", " (.getFullYear js-date))))))

(defn pluralize [string n]
  (if (> n 1)
    (str string "s")
    string))

(defn time-since
  "Get a string representing the elapsed time from a date in the past"
  [past-date & [flags]]
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
      (date-string past-js-date (concat flags [:year]))

      (or (pos? months-interval)
          (> days-interval 7))
      (date-string past-js-date flags)

      (pos? days-interval)
      (str days-interval " " (pluralize "day" days-interval) " ago")

      (pos? hours-interval)
      (str hours-interval " " (pluralize "hour" hours-interval) " ago")

      (pos? minutes-interval)
      (str minutes-interval " " (pluralize "min" minutes-interval) " ago")

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

(defn current-finance-period []
  (let [date (js/Date.)
        fixed-date (js/Date. (.setMonth date (dec (.getMonth date))))
        month (inc (.getMonth fixed-date))
        month-str (str (when (< month 10) "0") month)
        cur-period (str (.getFullYear fixed-date) "-" month-str)]
    cur-period))

(defn get-topic-keys [company-data]
  "Get the topic names, as a vector of keywords."
  (vec (map keyword (:topics company-data))))

(defun link-for

  ([links rel]
   (some #(when (= (:rel %) rel) %) links))

  ([links rel :guard string? method :guard string?]
   (some #(when (and (= (:method %) method) (= (:rel %) rel)) %) links))

  ([links rels :guard sequential? method :guard string?]
   (some #(when (and (= (:method %) method) (in? rels (:rel %))) %) links))

  ([links rel :guard string? methods :guard sequential?]
   (some #(when (and (in? methods (:method %)) (= (:rel %) rel)) %) links))

  ([links rels :guard sequential? methods :guard sequential?]
   (some #(when (and (in? methods (:method %)) (in? rels (:rel %))) %) links))

  ([links rel method params]
   (some (fn [link]
          (when (and (= (:method link) method)
                        (= (:rel link) rel)
                        (every? #(= (% params) (% link)) (keys params)))
            link)) links)))

(defn readonly-org? [links]
  (let [update-link (link-for links "partial-update")]
    (nil? update-link)))

(defn readonly-board? [links]
  (let [new-link (link-for links "new")
        update-link (link-for links "partial-update")
        delete-link (link-for links "delete")]
    (and (nil? new-link)
         (nil? update-link)
         (nil? delete-link))))

(defn readonly-entry? [links]
  (let [partial-update (link-for links "partial-update")
        delete (link-for links "delete")]
    (and (nil? partial-update) (nil? delete))))

(defn as-of-now []
  (let [date (js-date)]
    (.toISOString date)))

(defn fix-finances [topic-body]
  (let [finances-data (if (contains? topic-body :data) (:data topic-body) [])
        fixed-finances (calc-burnrate-runway finances-data)
        sort-pred (fn [a b] (compare (:period b) (:period a)))
        sorted-finances (sort sort-pred fixed-finances)
        fixed-topic (assoc topic-body :data sorted-finances)]
    fixed-topic))

(defn css-color [color]
  (let [colors (subvec (clojure.string/split color #"") 2)
        red (take 2 colors)
        green (take 2 (drop 2 colors))
        blue (take 2 (drop 4 colors))]
    (map #(-> (conj % "0x") (clojure.string/join) (reader/read-string)) [red green blue])))

(defn get-topic [topics-data k v]
  (some #(when (= (get % k) v) %) topics-data))

(defn fix-entry
  "Add `:read-only` and `:topic-name` keys to the entry map"
  [entry-body board-data topics-data]
  (let [topic (if (seq (:topic-slug entry-body))
                (get-topic topics-data :slug (:topic-slug entry-body))
                (when (seq (:topic-name entry-body))
                  (get-topic topics-data :name (:topic-name entry-body))))]
    (-> entry-body
      (assoc :content-type "entry")
      (assoc :read-only (readonly-entry? (:links entry-body)))
      (assoc :board-slug (or (:board-slug entry-body) (:slug board-data)))
      (assoc :board-name (or (:board-name entry-body) (:name board-data)))
      (assoc :topic-slug (or (:topic-slug entry-body) (:slug topic)))
      (assoc :topic-name (or (:topic-name entry-body) (:name topic))))))

(defn fix-board
  "Add topic name in each topic and a topic sorter"
  [board-data]
  (let [links (:links board-data)
        read-only (readonly-board? links)
        with-read-only (assoc board-data :read-only read-only)
        fixed-entries (zipmap
                       (map :uuid (:entries board-data))
                       (map #(fix-entry % board-data (:topics board-data)) (:entries board-data)))
        without-entries (dissoc with-read-only :entries)
        with-fixed-entries (assoc with-read-only :fixed-items fixed-entries)]
    with-fixed-entries))

(defn fix-activity [activity collection-data]
  (fix-entry activity collection-data nil))

(defn fix-all-posts
  "Fix org data coming from the API."
  [all-posts-data]
  (let [fixed-activities-list (map
                               #(fix-activity % {:slug (:board-slug %) :name (:board-name %)})
                               (:items all-posts-data))
        without-items (dissoc all-posts-data :items)
        fixed-activities (zipmap (map :uuid fixed-activities-list) fixed-activities-list)
        with-fixed-activities (assoc without-items :fixed-items fixed-activities)]
    with-fixed-activities))

(defn fix-org
  "Fix org data coming from the API."
  [org-data]
  (let [links (:links org-data)
        read-only (readonly-org? links)]
    (assoc org-data :read-only read-only)))

(defn px [n]
  (str n "px"))

(def quarterly-input-format (cljs-time-format/formatter "YYYY-MM"))
(def monthly-input-format (cljs-time-format/formatter "YYYY-MM"))
(def weekly-input-format (cljs-time-format/formatter "YYYY-MM-dd"))

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
  (if period
    (cljs-time-format/parse (get-formatter interval) period)
    (cljs-time/now)))

(defn period-from-date [date & [interval]]
  (cljs-time-format/unparse (get-formatter interval) date))

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

(defn get-quarter-from-period [period & [flags]]
  (let [date (date-from-period period "quarterly")
        month (cljs-time/month date)]
    (get-quarter-from-month month flags)))

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
          needs-year (and (not (in? flags :skip-year))
                          (or (in? flags :force-year)
                            (case fixed-interval
                              "weekly"
                              (or (not= plus-one-week-year year) (not= minus-one-week-year year))
                              "quarterly"
                              (or (= month 1) (= month 10))
                              ;else
                              (or (= month 1) (= month 12)))))]
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
        (str
         (cljs-time/year monday-date)
         "-"
         (add-zero (cljs-time/month monday-date))
         "-"
         (add-zero (cljs-time/day monday-date))))
      ;; Default tp monthly
      (str year "-" (add-zero month)))))

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
         (if (integer? duration) duration oc-animation-duration))))

(defn scroll-to-bottom [elem & [animated]]
  (let [elem-scroll-top (.-scrollHeight elem)]
    (.play
    (new Scroll
         elem
         #js [0 (.-scrollTop elem)]
         #js [0 elem-scroll-top]
         (if animated 320 oc-animation-duration)))))

(defn scroll-to-element [elem & [offset duration]]
  (let [elem-scroll-top (+ (.-offsetTop elem) (or offset 0))]
    (scroll-to-y elem-scroll-top duration)))

(defn scroll-top-with-id [id]
  (offset-top (sel1 (str "#" id))))

(defn scroll-to-id [id & [duration]]
  (let [body-scroll-top (.-scrollTop (.-body js/document))
        top (- (+ (scroll-top-with-id id) body-scroll-top) 50)]
    (scroll-to-y top (or duration oc-animation-duration))))

(defn scroll-to-topic [topic-name]
  (scroll-to-id (str "topic-" (name topic-name))))

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
  (loop [left 0
         top 0
         el element]
    (if-not (and el
                 (gobj/get el "offsetTop")
                 (gobj/get el "offsetLeft"))
      {:top top
       :left left}
      (recur (+ left (gobj/get el "offsetLeft" 0))
             (+ top (gobj/get el "offsetTop" 0))
             (.-offsetParent el)))))

(defn medium-editor-options [placeholder hide-on-click & [show-subtitle]]
  {:toolbar #js {:buttons (clj->js (remove nil? ["bold" "italic" (when show-subtitle "h2") "unorderedlist" "anchor"]))}
   :buttonLabels "fontawesome"
   :anchorPreview #js {:hideDelay 500, :previewValueSelector "a"}
   :extensions {:autolist (js/AutoList.)}
   :autoLink true
   :anchor #js {:customClassOption nil
                :customClassOptionText "Button"
                :linkValidation true
                :placeholderText "Paste or type a link"
                :targetCheckbox false
                :targetCheckboxText "Open in new window"}
   :paste #js {:forcePlainText false
               :cleanPastedHTML false}
   :placeholder #js {:text (or placeholder ""), :hideOnClick hide-on-click}})

(defn after [ms fn]
  (js/setTimeout fn ms))

(defn unicode-emojis [txt]
  (js/emojione.shortnameToUnicode txt))

(defn unicode-char [unicode]
  (js/encodeURI (str "&#x" unicode ";")))

(defn emojify
  "Take a string containing either Unicode emoji (mobile device keyboard), short code emoji (web app),
  or ASCII emoji (old skool) and convert it to HTML string ready to be added to the DOM (dangerously)
  with emoji image tags via the Emoji One lib and resources."
  [text & [plain-text]]
  ; ;; use an SVG sprite map
  ; (set! (.-imageType js/emojione) "png")
  ; (set! (.-sprites js/emojione) true)
  ; (set! (.-spritePath js/emojione) "https://d1wc0stj82keig.cloudfront.net/emojione.sprites.png")
  ; ;; convert ascii emoji's (like  :) and :D) into emojis
  ; (set! (.-ascii js/emojione) true)
  ; (let [text-string (or text "") ; handle nil
  ;       unicode-string (.toImage js/emojione text-string)
  ;       r (js/RegExp "<span " "ig")
  ;       with-img (.replace unicode-string r "<img ")
  ;       without-span (.replace with-img (js/RegExp ">(.{1,2})</span>" "ig") (str "alt=\"$1\" />"))]

  ;   (if plain-text
  ;     without-span
  ;     #js {"__html" without-span}))
  (if plain-text
    text
    #js {"__html" text}))

(defn strip-HTML-tags [text]
  (when text
    (let [reg (js/RegExp. "</?[^>]+(>|$)" "ig")]
      (.replace text reg ""))))

(defn strip-img-tags [text]
  (when text
    (let [reg (js/RegExp. "<img/?[^>]+(>|$)" "ig")]
      (.replace text reg ""))))

(defn strip-br-tags [text]
  (when text
    (let [reg (js/RegExp. "<br[ ]{0,}/?>" "ig")]
      (.replace text reg ""))))

(defn strip-empty-tags [text]
  (when text
    (let [reg (js/RegExp. "<[a-zA-Z]{1,}[ ]{0,}>[ ]{0,}</[a-zA-Z]{1,}[ ]{0,}>" "ig")]
      (.replace text reg ""))))

(defn su-default-title []
  (let [js-date (js-date)
        month (month-string (add-zero (.getMonth js-date)))
        year (.getFullYear js-date)]
    (str month " " year " Update")))

(defn my-uuid
  "Generate a 4 char UUID"
  []
  (.substring
    (.toString
      (.floor js/Math (* (inc (.random js/Math)) 0x10000)) 16) 1))

(defn guid
  "Generate v4 GUID based on this http://stackoverflow.com/a/2117523"
  []
  (str (my-uuid) (my-uuid) "-" (my-uuid) "-" (my-uuid) "-" (my-uuid) "-" (my-uuid) (my-uuid) (my-uuid)))

(defn activity-uuid
  "Generate a UUID for an entry"
 []
 (str (my-uuid) "-" (my-uuid) "-" (my-uuid)))

(defn event-stop [e]
  (.preventDefault e)
  (.stopPropagation e))

(defn event-inside? [e el]
  (loop [element (.-target e)]
    (if element
      (if (= element el)
        true
        (recur (.-parentElement element)))
      false)))

(defn is-parent? [el child]
  (loop [element child]
    (if element
      (if (= element el)
        true
        (recur (.-parentElement element)))
      false)))

(defn to-end-of-content-editable [content-editable-element]
  (if (.-createRange js/document)
    (let [rg (.createRange js/document)]
      (.selectNodeContents rg content-editable-element)
      (.collapse rg false)
      (let [selection (.getSelection js/window)]
        (.removeAllRanges selection)
        (.addRange selection rg)))
    (let [rg (.createTextRange (.-body js/document))]
      (.moveToElementText rg content-editable-element)
      (.collapse rg false)
      (.select rg))))

(defn emoji-images-to-unicode [html]
  (let [div (.createElement js/document "div")]
    (set! (.-id div) "temp-emojing")
    (set! (.-innerHTML div) html)
    (.appendChild (.-body js/document) div)
    (.replaceWith (js/$ "#temp-emojing img.emojione") (fn [_ _] (this-as this (.-alt this))))
    (let [$div       (js/$ "#temp-emojing")
          inner-html (.html $div)]
      (.remove $div)
      inner-html)))

(defn medium-editor-hide-placeholder [editor editor-el]
  (.each (js/$ (gobj/get editor "extensions"))
    (fn [_ _]
      (this-as this
        (when (gobj/containsKey this "hideOnClick")
          (let [hidePlaceholder (gobj/get this "hidePlaceholder")]
            (hidePlaceholder editor-el)))))))

(defn filter-placeholder-topics [topics company-data]
  (filterv #(let [sd (->> % keyword (get company-data))] (and sd (not (:placeholder sd)))) topics))

(defn su-date-from-created-at [created-at]
  (let [from-js-date (cljs-time/date-time (js-date created-at))]
    (cljs-time-format/unparse (cljs-time-format/formatter "yyyy-MM-dd") from-js-date)))

(def topic-body-limit 500)

(defn exceeds-topic-body-limit [body]
  (> (count (strip-HTML-tags body)) topic-body-limit))

(def min-no-placeholder-topic-enable-share 1)

(defn can-edit-topics? [company-data]
  (let [company-topics (vec (map keyword (:topics company-data)))]
    (and (not (responsive/is-mobile-size?))
         (responsive/can-edit?)
         (not (:read-only company-data))
         (>= (count (filter-placeholder-topics company-topics company-data)) min-no-placeholder-topic-enable-share))))

(defn company-has-topics? [company-data]
  (let [company-topics (vec (map keyword (:topics company-data)))]
    (pos? (count (filter-placeholder-topics company-topics company-data)))))

(defn remove-ending-empty-paragraph
  "Remove the last p tag if it's empty."
  [body-el]
  (when-not (is-test-env?)
    (while (and (pos? (.-length (.find (js/$ body-el) ">p:last-child")))
                (zero? (count (clojure.string/trim (.text (.find (js/$ body-el) ">p:last-child")))))
                (zero? (.-length (.find (js/$ body-el) ">p:last-child img"))))
      (.remove (js/$ ">p:last-child" (js/$ body-el))))))

(defn data-topic-has-data [topic topic-data]
  (cond
    ;; growth check count of metrics and count of data
    (= (keyword topic) :growth)
    (and (pos? (count (:metrics topic-data)))
         (pos? (count (:data topic-data))))
    ;; finances check count of data
    (= (keyword topic) :finances)
    (pos? (count (:data topic-data)))
    ;; else false
    :else
    false))

(defn valid-email? [addr]
  (when addr
    (email/isValidAddress addr)))

(defn valid-domain? [domain]
  (when (string? domain)
    (let [re (js/RegExp "^@?[a-z0-9.-]+\\.[a-z]{2,4}$" "i")]
      (pos? (count (.match domain re))))))

(defn remove-tooltips []
  (.remove (js/$ "div.tooltip")))

(defn complete-su-url [relative-su-url]
  (if (empty? relative-su-url)
    ""
    (let [protocol (.. js/document -location -protocol)
          host     (.. js/document -location -host)]
      (str protocol "//" host relative-su-url))))

(def new-topic-body-placeholder "What would you like to say...")

(defn new-topic-initial-data [topic title old-topic-data]
  (let [topic-name (name topic)
        initial-data {:topic topic-name
                      :title title
                      :body-placeholder new-topic-body-placeholder
                      :headline ""
                      :placeholder true
                      :links (:links old-topic-data)}
        with-data (if (and old-topic-data (contains? old-topic-data :data))
                    (assoc initial-data :data (:data old-topic-data))
                    initial-data)
        with-metrics (if (and old-topic-data (contains? old-topic-data :metrics))
                       (assoc with-data :metrics (:metrics old-topic-data))
                       with-data)]
    with-metrics))

(defn sum-revenues [finances-data]
  (let [cleaned-revenues (map #(-> % :revenue abs) finances-data)
        filtered-revenues (filter number? cleaned-revenues)]
    (apply + filtered-revenues)))

(defn parse-input-email [email-address]
  (let [parsed-email (email/parse email-address)]
    {:name (.getName parsed-email) :address (.getAddress parsed-email)}))

(defn get-author
  "Get the author data from the org list of authors"
  [user-id authors]
  (some #(when (= (:user-id %) user-id) %) authors))

(defn get-user-type
  "Calculate the user type, return admin if it's an admin,
  check if it's in the authors list if not admin
  return viewer else."
  [user-data org-data & [board-data]]
  (cond
    ;; if :admin is present and it's true
    (true? (:admin? user-data))
    :admin
    ;; if :admin is present and it's a list of teams
    ;; and it contains the actual org team
    (in? (:admin user-data) (:team-id org-data))
    :admin
    ;; if the user is in the list of authors of the org data
    ;; or if a board is passed and the authors list contains the user-id
    (or (get-author (:user-id user-data) (:authors org-data))
        (and board-data
             (get-author (:user-id user-data) (:authors board-data))))
    :author
    ;; viewer in all other cases
    :else
    :viewer))

(defn index-of
  "Given a collection and a function return the index that make the function truely."
  [s f]
  (loop [idx 0 items s]
    (cond
      (empty? items) nil
      (f (first items)) idx
      :else (recur (inc idx) (rest items)))))

(defn name-or-email [user]
  (let [user-name (:name user)
        first-name (:first-name user)
        last-name (:last-name user)]
    (cond
      (and (seq first-name)
           (seq last-name))
      (str first-name " " last-name)
      (seq first-name)
      first-name
      (seq last-name)
      last-name
      (seq user-name)
      user-name
      :else
      (:email user))))

(defn slack-link-with-state [original-url user-id team-id redirect]
  (clojure.string/replace
   original-url
   team-id
   (str
    (when (seq team-id) (str team-id ":"))
    (when (seq user-id) (str user-id ":"))
    redirect)))

(defn icon-for-mimetype
  "Thanks to https://gist.github.com/colemanw/9c9a12aae16a4bfe2678de86b661d922"
  [mimetype]
  (case (s/lower mimetype)
    ;; Media
    "image" "fa-file-image-o"
    "image/png" "fa-file-image-o"
    "image/bmp" "fa-file-image-o"
    "image/jpg" "fa-file-image-o"
    "image/jpeg" "fa-file-image-o"
    "image/gif" "fa-file-image-o"
    ".jpg" "fa-file-image-o"
    "audio" "fa-file-audio-o"
    "video" "fa-file-video-o"
    ;; Documents
    "application/pdf" "fa-file-pdf-o"
    "application/msword" "fa-file-word-o",
    "application/vnd.ms-word" "fa-file-word-o",
    "application/vnd.oasis.opendocument.text" "fa-file-word-o",
    "application/vnd.openxmlformats-officedocument.wordprocessingml" "fa-file-word-o",
    "application/vnd.ms-excel" "fa-file-excel-o",
    "application/vnd.openxmlformats-officedocument.spreadsheetml" "fa-file-excel-o",
    "application/vnd.oasis.opendocument.spreadsheet" "fa-file-excel-o",
    "application/vnd.ms-powerpoint" "fa-file-powerpoint-o",
    "application/vnd.openxmlformats-officedocument.presentationml" "fa-file-powerpoint-o",
    "application/vnd.oasis.opendocument.presentation" "fa-file-powerpoint-o",
    "text/plain" "fa-file-text-o",
    "text/html" "fa-file-code-o",
    "application/json" "fa-file-code-o",
    ;; Archives
    "application/gzip" "fa-file-archive-o",
    "application/zip" "fa-file-archive-o",
    ;; Code
    "text/css" "fa-file-code-o"
    "text/php" "fa-file-code-o"
    ;; Generic case
    "fa-file"))

(def generic-network-error
 "There may be a problem with your network, or with our servers. Please try again later.")

(defn clean-google-chart-url [gchart-url]
  (if (string? gchart-url)
    (.replace gchart-url #"(?i)/u/\d+" "")
    ""))

(defn valid-google-chart-url? [url]
  (when (string? url)
    (let [cleaned-url (clean-google-chart-url url)]
      (not= (.indexOf cleaned-url "://docs.google.com/spreadsheets/d/") -1))))

(defn rum-dom-node [s]
  (when-not (.-_calledComponentWillUnmount (:rum/react-component s))
    (let [component (:rum/react-component s)]
      (js/ReactDOM.findDOMNode component))))

(defn pulse-animation [el]
  (.animate el
    #js {:transform #js ["scale(1)" "scale(1.5)" "scale(2)" "scale(1.5)" "scale(1)"]}
    #js {:fill "forwards"
         :duration 500
         :iterations 3}))

(defn pulse-reaction-count
  [activity-uuid reaction]
  (let [selector (str "div.reaction-" activity-uuid "-" reaction)]
    (when-let [el (sel1 [(keyword selector)])]
      (pulse-animation el))))

(defn pulse-comments-count
  [activity-uuid]
  (let [selector (str "div.comments-count-" activity-uuid )]
    (when-let [el (sel1 [(keyword selector)])]
      (pulse-animation el))))

(defn cdn [img-src & [no-deploy-folder]]
  (let [use-cdn? (empty? ls/cdn-url)
        cdn (if use-cdn? "" (str "/" ls/cdn-url))
        deploy-key (if (empty? ls/deploy-key) "" (str "/" ls/deploy-key))
        with-deploy-folder (if no-deploy-folder
                             cdn
                             (str cdn deploy-key))]
    (str (when use-cdn? with-deploy-folder) img-src)))

(defn rgb-with-opacity [rgb opacity]
  (str "rgba(" (clojure.string/join "," (conj (vec (css-color rgb)) opacity)) ")"))

(defn get-24h-time

  [js-date]
  (str (.getHours js-date) ":" (add-zero (.getMinutes js-date))))

(defn get-ampm-time
  [js-date]
  (let [hours (.getHours js-date)
        minutes (add-zero (.getMinutes js-date))
        ampm (if (>= hours 12) " p.m." " a.m.")
        hours (mod hours 12)
        hours (if (zero? hours) 12 hours)]
    (str hours ":" minutes ampm)))

(defn format-time-string [js-date]
  (let [r (js/RegExp "am|pm" "i")
        h12 (or (.match (.toLocaleTimeString js-date) r) (.match (str js-date) r))]
    (if h12
      (get-ampm-time js-date)
      (get-24h-time js-date))))

(defn activity-date-string [js-date hide-time]
  (let [time-string (format-time-string js-date)]
    (str
     (full-month-string (inc (.getMonth js-date)))
     " "
     (.getDate js-date)
     ", "
     (.getFullYear js-date)
     (when-not hide-time
      (str " at " time-string)))))

(defn activity-date
  "Get a string representing the elapsed time from a date in the past"
  [past-js-date & [hide-time]]
  (let [past (.getTime past-js-date)
        now (.getTime (js-date))
        seconds (.floor js/Math (/ (- now past) 1000))
        years-interval (.floor js/Math (/ seconds 31536000))
        months-interval (.floor js/Math (/ seconds 2592000))
        days-interval (.floor js/Math (/ seconds 86400))
        hours-interval (.floor js/Math (/ seconds 3600))
        minutes-interval (.floor js/Math (/ seconds 60))]
    (cond
      (pos? years-interval)
      (str "on " (activity-date-string past-js-date hide-time))

      (or (pos? months-interval)
          (> days-interval 7))
      (str "on " (activity-date-string past-js-date hide-time))

      (pos? days-interval)
      (str days-interval " " (pluralize "day" days-interval) " ago")

      (pos? hours-interval)
      (str hours-interval " " (pluralize "hour" hours-interval) " ago")

      (pos? minutes-interval)
      (str minutes-interval " " (pluralize "min" minutes-interval) " ago")

      :else
      "just now")))

(defn entry-date-tooltip [entry-data]
  (let [created-at (js-date (or (:published-at entry-data) (:created-at entry-data)))
        updated-at (js-date (:updated-at entry-data))
        created-str (activity-date created-at)
        updated-str (activity-date updated-at)]
    (if (= (:created-at entry-data) (:updated-at entry-data))
      (str "Posted " created-str)
      (str "Posted " created-str "\nEdited " updated-str " by " (:name (last (:author entry-data)))))))

(defn activity-date-tooltip [activity-data]
  (entry-date-tooltip activity-data))

(defn copy-to-clipboard []
  (try
    (.execCommand js/document "copy")
    (catch :default e
      false)))

(defn ordinal-suffix [i]
  (let [j (mod i 10)
        k (mod i 100)]
    (cond
      (and (= j 1) (not= k 11)) "st"
      (and (= j 2) (not= k 12)) "nd"
      (and (= j 3) (not= k 13)) "rd"
      :else "th")))

(defn draft-complete-date [jd]
  (let [month (full-month-string (inc (.getMonth jd)))
        day (.getDate jd)]
   (str month " " day (ordinal-suffix day))))

(defn draft-date [date]
  (let [jd (js-date date)
        now (js-date)
        yesterday (js-date (- (.getTime now) (* 24 60 60 1000)))
        is-today? (= (.getDate jd) (.getDate now))
        is-yesterday? (= (.getDate jd) (.getDate yesterday))
        same-year? (= (.getYear jd) (.getYear now))
        partial-d (draft-complete-date jd)
        t (format-time-string jd)]
    (str "Edited "
      (cond
        is-today? (str "today, " t)
        is-yesterday? (str "yesterday, " t)
        same-year? partial-d
        :else (str partial-d ", " (.getYear jd))))))

(defn body-without-preview [body]
  (let [body-without-tags (-> body strip-img-tags strip-br-tags strip-empty-tags)
        hidden-class (str "activity-body-" (int (rand 10000)))
        $body-content (js/$ (str "<div class=\"" hidden-class " hidden\">" body-without-tags "</div>"))
        appened-body (.append (js/$ (.-body js/document)) $body-content)
        _ (.each (js/$ (str "." hidden-class " .carrot-no-preview")) #(this-as this
                                                                        (let [$this (js/$ this)]
                                                                          (.remove $this))))
        $hidden-div (js/$ (str "." hidden-class))
        body-without-preview (.html $hidden-div)
        _ (.remove $hidden-div)]
    body-without-preview))

(def default-body "<p><br/></p>")

(defn newest-org [orgs]
  (first (sort-by :created-at orgs)))

(defn get-default-org [orgs]
  (if-let [last-org-slug (cook/get-cookie (router/last-org-cookie))]
    (let [last-org (first (filter #(= (:slug %) last-org-slug) orgs))]
      (or
        ; Get the last accessed board from the saved cookie
        last-org
        ; Fallback to the newest board if the saved board was not found
        (newest-org orgs)))
    (newest-org orgs)))

;; Get the board to show counting the last accessed and the last created

(def default-board "welcome")

(defn get-default-board [org-data]
  (let [last-board-slug (or (cook/get-cookie (router/last-board-cookie (:slug org-data))) default-board)]
    (if (= last-board-slug "all-posts")
      {:slug "all-posts"}
      (let [boards (:boards org-data)
            board (first (filter #(= (:slug %) last-board-slug) boards))]
        (or
          ; Get the last accessed board from the saved cookie
          board
          (let [sorted-boards (vec (sort-by :name boards))]
            (first sorted-boards)))))))

(defn get-board-url [org-slug board-slug]
  (if (= (keyword (cook/get-cookie (router/last-board-filter-cookie org-slug board-slug))) :by-topic)
    (oc-urls/board-sort-by-topic org-slug board-slug)
    (oc-urls/board org-slug board-slug)))

(defn clean-body-html [inner-html]
  (let [$container (.html (js/$ "<div class=\"hidden\"/>") inner-html)
        _ (.append (js/$ (.-body js/document)) $container)
        _ (.remove (js/$ ".rangySelectionBoundary" $container))
        cleaned-html (.html $container)
        _ (.detach $container)]
    (emoji-images-to-unicode (gobj/get (emojify cleaned-html) "__html"))))

(defn get-attachments-from-body [body]
  (let [$body (.html (js/$ "<div/>") body)
        attachments (js/$ "a.media-attachment" $body)
        atch-map (atom [])]
    (.each attachments (fn [idx item]
      (let [$item (js/$ item)]
        (reset! atch-map (vec (conj @atch-map {:name (.data $item "name")
                                               :size (.data $item "size")
                                               :mimetype (.data $item "mimetype")
                                               :author (.data $item "author")
                                               :createdat (.data $item "createdat")
                                               :url (.attr $item "href")}))))))
    @atch-map))

(defn your-boards-url []
  (if-let [org-slug (cook/get-cookie (router/last-org-cookie))]
    (if-let [board-slug (cook/get-cookie (router/last-board-cookie org-slug))]
      (get-board-url org-slug board-slug)
      (oc-urls/org org-slug))
    oc-urls/login))

(defn storage-url-org-slug [storage-url]
  (let [parts (s/split storage-url "/")]
    (if (s/starts-with? storage-url "http")
      (nth parts 4)
      (nth parts 2))))

(def default-drafts-board-name "Drafts")

(def default-drafts-board-slug "drafts")

(def default-draft-status "draft")

(def default-drafts-board
  {:uuid "0000-0000-0000"
   :created-at "2000-01-01T00:00:00.000Z"
   :updated-at "2000-01-01T00:00:00.000Z"
   :slug default-drafts-board-slug
   :name default-drafts-board-name
   :entries []
   :fixed-items []
   :access "private"
   :read-only true})