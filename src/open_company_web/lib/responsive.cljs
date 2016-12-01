(ns open-company-web.lib.responsive
  (:require [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.cookies :as cook]
            [goog.object :as gobj]
            [goog.userAgent :as userAgent]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def big-web-min-width 684)
; 2 columns 302 * 2 = 604 diff: 80 |30|col1|20|col2|30|
; 1 column 420 diff: 263: |131|420|131|

(def mobile-2-columns-breakpoint 320)
;; 3 Columns
(def c3-min-win-width 1024)
(def c1-min-win-width 414)

(defn ww []
  (when (and js/document
             (.-body js/document)
             (.-clientWidth (.-body js/document)))
    (.-clientWidth (.-body js/document))))

(defn window-exceeds-breakpoint []
  (> (ww) mobile-2-columns-breakpoint))

(defn dashboard-columns-num []
  (let [win-width (ww)]
    (cond
      (>= win-width c3-min-win-width)
      3
      (>= win-width mobile-2-columns-breakpoint)
      2
      :else
      1)))

(defn columns-num []
  (let [win-width (ww)]
    (cond
      (>= win-width c3-min-win-width)
      3
      (>= win-width big-web-min-width)
      2
      :else
      1)))

(def _mobile (atom -1))

(defn set-browser-type! []
  (let [force-mobile-cookie (cook/get-cookie :force-browser-type)
        is-big-web (if (.-body js/document)
                     (>= (ww) big-web-min-width)
                     true) ; to not break tests
        fixed-browser-type (if (nil? force-mobile-cookie)
                            (not is-big-web)
                            (if (= force-mobile-cookie "mobile")
                             true
                             false))]
  (reset! _mobile fixed-browser-type)))

(defn is-mobile-size? []
 "Check if it's mobile based only on screen size"
 ;; fake the browser type for the moment
 (when (neg? @_mobile)
 (set-browser-type!))
 @_mobile)

(defn mobile-dashboard-card-width [& [force-columns]]
  (let [columns (or force-columns (dashboard-columns-num))
        win-width (ww)]
    (cond
      (= columns 2)
      (/ (- win-width 8 8 8) 2)
      (= columns 1)
      (- win-width 8 8))))

(defn user-agent-mobile? []
  userAgent/MOBILE)

(defn is-mobile?
  "Check if it's mobile based on UserAgent or screen size."
  []
  (or (is-mobile-size?) (user-agent-mobile?)))

(def topic-list-x-padding 20)
(def topic-total-x-padding 20)
(def left-topics-list-width 185)

(defn is-tablet-or-mobile? []
  ;; check if it's test env, can't import utils to avoid circular dependencies
  (if (not (not (.-_phantom js/window)))
    false
    (or (= (gobj/get js/WURFL "form_factor") "Tablet")
        (= (gobj/get js/WURFL "form_factor") "Smartphone")
        (= (gobj/get js/WURFL "form_factor") "Other Mobile"))))

(defn calc-card-width [& [force-columns]]
  (let [win-width (ww)
        columns (or force-columns (columns-num))]
    (cond
      (= columns 3)
      (min (/ (- win-width
                 (* topic-list-x-padding 2)
                 (* topic-total-x-padding 3)
                 (if (is-tablet-or-mobile?) 0 left-topics-list-width))
              3)
           420)
      (= columns 2)
      (max (/ (- win-width
                 (* topic-list-x-padding 2)
                 (* topic-total-x-padding 2)
                 (if (is-tablet-or-mobile?) 0 left-topics-list-width))
              2)
           208))))

(defn can-edit? []
  "Check if it's mobile based only on the UserAgent"
  (not (user-agent-mobile?)))

(defn fullscreen-topic-width [card-width]
  (let [win-width (ww)]
    (if (> win-width big-web-min-width)
      big-web-min-width
      (min card-width win-width))))

(def mobile-topic-total-x-padding 4)
(def updates-content-list-width 280)
(def updates-content-cards-right-margin 40)
(def updates-content-cards-max-width 560)
(def updates-content-cards-min-width 250)
(def updates-max-total-width 1152)

(defn total-layout-width-int [card-width columns-num]
  (if (is-mobile-size?)
    (let [win-width (ww)]
      (- win-width 8 8))
    (+ (* (+ card-width topic-total-x-padding) columns-num)    ; width of each column plus
       (* topic-list-x-padding 2)                              ; the padding around all the columns
       (if (is-tablet-or-mobile?) 0 left-topics-list-width)))) ; the left side panel with the topics list

; (- (* (+ card-width topic-total-x-padding) columns-num) ; width of each column less
;      (if (is-mobile?) 20 10)                              ; the container padding
;      (if (is-mobile?) 40 0)))                             ; the distance btw the columns on big web

(defn calc-update-width [columns-num]
  (let [card-width   (calc-card-width)
        total-width-int (total-layout-width-int card-width columns-num)
        total-width  (str total-width-int "px")
        fixed-total-width-int (if (<= total-width-int (+ updates-content-cards-min-width updates-content-cards-right-margin updates-content-list-width))
                                (+ updates-content-cards-min-width updates-content-cards-right-margin updates-content-list-width)
                                total-width-int)
        total-width  (str fixed-total-width-int "px")
        fixed-card-width (if (>= (- fixed-total-width-int updates-content-list-width updates-content-cards-right-margin) updates-content-cards-max-width)
                            updates-content-cards-max-width
                            (- fixed-total-width-int updates-content-list-width updates-content-cards-right-margin))]
    fixed-card-width))

(when (not (not (.-_phantom js/window)))
  (events/listen js/window EventType/RESIZE #(set-browser-type!)))