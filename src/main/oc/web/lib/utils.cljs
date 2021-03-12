(ns oc.web.lib.utils
  (:require [clojure.string]
            [goog.format.EmailAddress :as email]
            [goog.fx.dom :refer (Scroll)]
            [goog.string :refer (format)]
            [oops.core :refer (oget)]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.urls :as oc-urls]
            [oc.web.utils.drafts :as du]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.local-settings :as ls]
            [cuerdas.core :as s]
            [oc.lib.hateoas :as hateoas]
            [cljs.reader :as reader]))

(def oc-animation-duration 300)

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

(defn camel-case-str [value]
  (when value
    (let [upper-value (clojure.string/replace value #"^(\w)" #(clojure.string/upper-case (first %1)))]
      (clojure.string/replace upper-value #"-(\w)"
                              #(str " " (clojure.string/upper-case (second %1)))))))

(defn get-week-day [day & [short-day]]
  (case day
   1 (if short-day "Mon" "Monday")
   2 (if short-day "Tue" "Tuesday")
   3 (if short-day "Wed" "Wednesday")
   4 (if short-day "Thu" "Thursday")
   5 (if short-day "Fri" "Friday")
   6 (if short-day "Sat" "Saturday")
   (if short-day "Sun" "Sunday")))

(defn js-date [ & [date-str]]
  (if date-str
    (new js/Date date-str)
    (new js/Date)))

(defn add-zero [v]
  (str (when (< v 10) "0") v))

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
        now-date (js-date)
        now (.getTime now-date)
        seconds (.floor js/Math (/ (- now past) 1000))
        years-interval (.floor js/Math (/ seconds 31536000))
        months-interval (.floor js/Math (/ seconds 2592000))
        days-interval (.floor js/Math (/ seconds 86400))
        hours-interval (.floor js/Math (/ seconds 3600))
        minutes-interval (.floor js/Math (/ seconds 60))
        short? (in? flags :short)
        date-prefix (in? flags :date-prefix)
        lower-case (in? flags :lower-case)]
    (cond
      (pos? years-interval)
      (str (when date-prefix " on ") (date-string past-js-date (concat flags [:year])))

      (pos? months-interval)
      (str (when date-prefix " on ") (date-string past-js-date flags))

      (pos? days-interval)
      (str days-interval (if short? "d" (str " " (pluralize "day" days-interval) " ago")))

      (pos? hours-interval)
      (str hours-interval (if short? "h" (str " " (pluralize "hour" hours-interval) " ago")))

      (pos? minutes-interval)
      (str minutes-interval (if short? "m" (str " " (pluralize "minute" minutes-interval) " ago")))

      :else
      (if short?
        "now"
        (if lower-case
          "just now"
          "Just now")))))

(defn time-without-leading-zeros [time-string]
  (.replace time-string (js/RegExp. "^0([0-9])*" "ig") "$1"))

(defn local-date-time [past-date]
  (let [time-string (.toLocaleTimeString past-date (.. js/window -navigator -language)
                     #js {:hour "2-digit"
                          :minute "2-digit"
                          :format "hour:minute"})
        without-leading-zeros (time-without-leading-zeros time-string)]
    (s/upper without-leading-zeros)))

(defn foc-date-time [past-date & [flags]]
  (let [past-js-date (js-date past-date)
        now-date (js-date)]
    (if (and (= (.getFullYear past-js-date) (.getFullYear now-date))
             (= (.getMonth past-js-date) (.getMonth now-date))
             (= (.getDate past-js-date) (.getDate now-date)))
      (local-date-time past-js-date)
      (time-since past-date (concat flags [:short])))))

(defn explore-date-time [past-date & [flags]]
  (let [past-js-date (js-date past-date)
        now-date (js-date)]
    (if (and (= (.getFullYear past-js-date) (.getFullYear now-date))
             (= (.getMonth past-js-date) (.getMonth now-date))
             (= (.getDate past-js-date) (.getDate now-date)))
      (str " at " (local-date-time past-js-date))
      (time-since past-date (concat flags [:date-prefix :lower-case])))))

(defn class-set
  "Given a map of class names as keys return a string of the those classes that evaulates as true"
  [classes]
  (clojure.string/join (map #(str " " (name %)) (keys (filter #(and (first %) (second %)) classes)))))

(defn link-for [& args]
  (apply hateoas/link-for args))

(defn as-of-now []
  (let [date (js-date)]
    (.toISOString date)))

(defn css-color [color]
  (let [colors (subvec (clojure.string/split color #"") 2)
        red (take 2 colors)
        green (take 2 (drop 2 colors))
        blue (take 2 (drop 4 colors))]
    (map #(-> (conj % "0x") (clojure.string/join) (reader/read-string)) [red green blue])))

(defn scroll-to-y [scroll-y & [duration]]
  (if (and duration (zero? duration))
    (if ua/edge?
      (set! (.. js/document -scrollingElement -scrollTop) scroll-y)
      (.scrollTo (.-scrollingElement js/document) 0 scroll-y))
    (.play
      (new Scroll
           (.-scrollingElement js/document)
           #js [0 (.-scrollY js/window)]
           #js [0 scroll-y]
           (if (integer? duration) duration oc-animation-duration)))))

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

(defn is-test-env? []
  (not (not (.-_phantom js/window))))

(defn after [ms fn]
  (js/setTimeout fn ms))

(defn maybe-after [ms fn]
  (if (zero? ms)
   (fn)
   (js/setTimeout fn ms)))

(defn every [ms fn]
  (js/setInterval fn ms))

(defn emojify
  "Take a string containing either Unicode emoji (mobile device keyboard), short code emoji (web app),
  or ASCII emoji (old skool) and convert it to HTML string ready to be added to the DOM (dangerously)
  with emoji image tags via the Emoji One lib and resources."
  [text]
  #js {"__html" text})

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
    (let [reg
     (js/RegExp.
      "<[a-zA-Z]{1,}[ ]{0,}(class)?[ ]{0,}([0-9a-zA-Z-]{0,}=\"[a-zA-Z\\s]{0,}\")?>[ ]{0,}</[a-zA-Z]{1,}[ ]{0,}>" "ig")]
      (.replace text reg ""))))

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

(def event-stop dom-utils/event-stop!)

(def event-inside? dom-utils/event-inside?)

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

;; Based on https://github.com/google/closure-library/blob/master/closure/goog/format/emailaddress.js#L134
;;      and https://github.com/google/closure-library/blob/master/closure/goog/format/emailaddress.js#L142
;; It is designed to match about 99.9% of the valid emails while accepting some invalid emails.
(def valid-email-pattern "[+a-zA-Z0-9_.!#$%&'*\\/=?^`{|}~-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,63}")
(def valid-email-re (re-pattern (str "^" valid-email-pattern "$")))

(defn valid-email? [addr]
  (when addr
    (email/isValidAddress addr)))

;; Based on https://github.com/google/closure-library/blob/master/closure/goog/format/emailaddress.js#L142
(def valid-domain-pattern "@?(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,63}")
(def valid-domain-re (re-pattern (str "^" valid-domain-pattern "$")))

(defn valid-domain? [domain]
  (when (string? domain)
    (re-matches valid-domain-re domain)))

(defn remove-tooltips []
  (.remove (js/$ "div.tooltip")))

(defn parse-input-email [email-address]
  (let [parsed-email (email/parse email-address)]
    {:name (.getName ^js parsed-email) :address (.getAddress ^js parsed-email)}))

(defn index-of
  "Given a collection and a function return the index that make the function truely."
  [s f]
  (loop [idx 0 items s]
    (cond
      (empty? items) nil
      (f (first items)) idx
      :else (recur (inc idx) (rest items)))))

(def network-error
 {:title "Network request"
  :description (if ua/pseudo-native?
                "Probably just a temporary issue. Please try again later if this persists."
                "Probably just a temporary issue. Please refresh if this persists.")
  :server-error true
  :id :generic-network-error
  :sentry-dialog true
  :dismiss true})

(def internal-error
  {:title "Internal error occurred"
   :description (str "An internal error occurrent, we have been informed of the "
                 "problem and we will be working on it as soon as possible. "
                 "Thanks for understanding.")
   :id :internal-error
   :server-error true
   :sentry-dialog true
   :dismiss true})

(def entry-get-error
  {:server-error true
   :id :entry-load-error
   :title "Error loading the post"
   :description (format "We've been notified of this error. Please contact %s for additional help." oc-urls/contact-email)
   :expire 5
   :sentry-dialog true
   :dismiss true})

(defn clean-google-chart-url [gchart-url]
  (if (string? gchart-url)
    (.replace gchart-url #"(?i)/u/\d+" "")
    ""))

(defn valid-google-chart-url? [url]
  (when (string? url)
    (let [cleaned-url (clean-google-chart-url url)]
      (not= (.indexOf cleaned-url "://docs.google.com/spreadsheets/d/") -1))))

(defn cdn [img-src & [no-deploy-folder]]
  (let [use-cdn? (seq ls/cdn-url)
        cdn (if use-cdn? ls/cdn-url "")
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
        ampm (if (>= hours 12) " PM" " AM")
        hours (mod hours 12)
        hours (if (zero? hours) 12 hours)]
    (str hours ":" minutes ampm)))

(defn format-time-string [js-date]
  (let [r (js/RegExp "am|pm" "i")
        h12 (or (.match (.toLocaleTimeString js-date) r) (.match (str js-date) r))]
    (if h12
      (get-ampm-time js-date)
      (get-24h-time js-date))))

(defn activity-date-string [js-date & [hide-time hide-year]]
  (let [time-string (format-time-string js-date)]
    (str
     (full-month-string (inc (.getMonth js-date)))
     " "
     (.getDate js-date)
     (when-not hide-year
      ", ")
     (when-not hide-year
      (.getFullYear js-date))
     (when-not hide-time
      (str " at " time-string)))))

(defn tooltip-date [past-date]
  (let [past-js-date (js-date past-date)
        now-date (js-date)
        hide-time (or (not= (.getFullYear past-js-date) (.getFullYear now-date))
                      (not= (.getMonth past-js-date) (.getMonth now-date))
                      (not= (.getDate past-js-date) (.getDate now-date)))]
    (activity-date-string past-js-date hide-time false)))

(defn activity-date
  "Get a string representing the elapsed time from a date in the past"
  [past-js-date & [hide-time]]
  (when past-js-date
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
        "Just now"))))

(defn activity-date-tooltip [entry-data]
  (let [created-at (or (:published-at entry-data) (:created-at entry-data))
        last-edit (last (:author entry-data))
        updated-at (when (:updated-at last-edit)
                     (:updated-at last-edit))
        same-author? (= (:user-id last-edit) (:user-id (:publisher entry-data)))
        ;; Show edit only if happened at least 24 hours after publish
        should-show-updated-at? (or (not same-author?)
                                    (> (- (.getTime (js-date updated-at)) (.getTime (js-date created-at)))
                                     (* 1000 60 60 24)))
        created-str (tooltip-date created-at)
        updated-str (tooltip-date updated-at)
        label-prefix (if (or (= (:status entry-data) "published")
                             (not= (:resource-type entry-data) :entry))
                       "Posted on "
                       "Created on ")]
    (if-not should-show-updated-at?
      (str label-prefix created-str)
      (if same-author?
        (str label-prefix created-str "\nEdited " updated-str)
        (str label-prefix created-str "\nEdited " updated-str " by " (:name last-edit))))))

(defn ios-copy-to-clipboard [el]
  (let [old-ce (.-contentEditable el)
        old-ro (.-readOnly el)
        rg (.createRange js/document)]
    (set! (.-contentEditable el) true)
    (set! (.-readOnly el) false)
    (.selectNodeContents rg el)
    (let [s (.getSelection js/window)]
      (.removeAllRanges s)
      (.addRange s rg)
      (.setSelectionRange el 0 (.. el -value -length))
      (set! (.-contentEditable el) old-ce)
      (set! (.-readOnly el) old-ro))))

(defn copy-to-clipboard [el]
  (try
    (when ua/ios?
      (ios-copy-to-clipboard el))
    (.execCommand js/document "copy")
    (catch :default _
      false)))

(defn body-without-preview [body]
  (let [body-without-tags (-> body strip-img-tags strip-br-tags strip-empty-tags)
        hidden-class (str "activity-body-" (int (rand 10000)))
        $body-content (js/$ (str "<div class=\"" hidden-class " hidden\">" body-without-tags "</div>"))
        _ (.append (js/$ (.-body js/document)) $body-content)
        _ (.each (js/$ (str "." hidden-class " .carrot-no-preview")) #(this-as this
                                                                        (let [$this (js/$ this)]
                                                                          (.remove $this))))
        $hidden-div (js/$ (str "." hidden-class))
        body-without-preview (.html ^js $hidden-div)
        _ (.remove $hidden-div)]
    body-without-preview))

(defn- remove-elements [$container el-selector re-check]
  (loop [$el (.find $container el-selector)]
    (when (and (pos? (.-length $el))
               (.match (.html ^js $el) re-check))
      (.remove $el)
      (recur (.find $container el-selector)))))

(defn clean-body-html [inner-html]
  (let [$container (.html (js/$ "<div class=\"hidden\"/>") inner-html)
        _ (.remove (js/$ ".rangySelectionBoundary" $container))
        _ (.remove (js/$ ".oc-mention-popup" $container))
        _ (.remove (js/$ ".oc-poll-container" $container))
        re-check (js/RegExp "^([\\s]*|[\\<br\\s*/?\\>]{0,1}|[\\s]*|[\\&nbsp;]*)*$" "i")
        _ (remove-elements $container "> p:last-child" re-check)
        _ (remove-elements $container "> p:first-child" re-check)]
    (.html $container)))

(defn url-org-slug [storage-url]
  (let [parts (s/split storage-url "/")]
    (if (s/starts-with? storage-url "http")
      (nth parts 4)
      (nth parts 2))))

(defn storage-url-org-slug [url] (url-org-slug url))

(defn url-board-slug [url]
  (let [parts (s/split url "/")]
    (if (s/starts-with? url "http")
      (nth parts 5)
      (nth parts 3))))

(defn section-org-slug [section-data]
  (url-org-slug (link-for (:links section-data) ["item" "self"] "GET")))

(defn post-org-slug [post-data]
  (url-org-slug (link-for (:links post-data) ["item" "self"] "GET")))

(def default-drafts-board-name du/default-drafts-board-name)

(def default-drafts-board-slug du/default-drafts-board-slug)

(def default-draft-status du/default-draft-status)

(def default-drafts-board du/default-drafts-board)

(def default-board-slug "--default-section-slug")
(def default-board-access "team")

(def default-board
  {:name ""
   :access default-board-access
   :slug default-board-slug})

(defn retina-src [url]
  {:src (cdn (str url ".png"))
   :src-set (str (cdn (str url "@2x.png")) " 2x")})

(defn trim [value]
  (if (string? value)
    (s/trim value)
    value))

(def section-name-exists-error "Team name already exists or isn't allowed")

(defn calc-video-height [width]
  (int (* width (/ 3 4))))

(def hide-class "fs-hide") ;; Use fs-hide for FullStory

(defn- find-node [e fn]
  (loop [el (.-target e)]
    (if (or (not el) (fn el))
      el
      (recur (.-parentElement el)))))

(defn element-clicked? [e element-name]
  (find-node e #(= (.-tagName %) element-name)))

(defn button-clicked? [e]
  (element-clicked? e "BUTTON"))

(defn input-clicked? [e]
  (element-clicked? e "INPUT"))

(defn anchor-clicked? [e]
  (element-clicked? e "A"))

(defn content-editable-clicked? [e]
  (find-node e #(oget % "?attributes.contenteditable")))

(defn debounced-fn
  "Debounce function: give a function and a wait time call it immediately
  and avoid calling it again for the wait time.
  NB: if you call setState in the passed fn you need to check it's still mounted
  manually since this can be calling f after it has been unmounted."
  [f w]
  (let [timeout (atom nil)]
    (fn [& args]
      (let [wait? @timeout
            later (fn []
                    (reset! timeout nil)
                    (when wait?
                      (apply f args)))]
        (when wait?
          (js/clearTimeout @timeout))
        (reset! timeout (js/setTimeout later w))
        (when-not wait?
          (apply f args))))))

(defn throttled-debounced-fn
  [f w]
  (let [last-call (atom 0)
        timeout (atom nil)]
    (fn [& args]
      (let [now (.getTime (js-date))
            later (fn []
                    (reset! timeout nil)
                    (apply f args))]
        (js/clearTimeout @timeout)
        (if (< (- now @last-call) w)
          (js/setTimeout later w)
          (do ;; execute now
            (reset! timeout nil)
            (reset! last-call now)
            (apply f args)))))))

(defn observe []
  (if (.-attachEvent js/window)
    (fn [el e handler] (.attachEvent el (str "on" e) handler))
    (fn [el e handler] (.addEventListener el e handler))))

(defn page-scroll-top []
  ;; (let [is-mobile? (responsive/is-mobile-size?)
  ;;       board-slug (dis/current-board-slug)
  ;;       activity-id (dis/current-activity-id)]
  ;;   (if (and (not activity-id)
  ;;            board-slug
  ;;            (not= (keyword board-slug) (keyword default-drafts-board-slug))
  ;;            is-mobile?)
  ;;     65
  ;;     0))
  0)