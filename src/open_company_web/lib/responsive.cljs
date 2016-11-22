(ns open-company-web.lib.responsive
  (:require [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.cookies :as cook]
            [goog.object :as gobj]
            [goog.userAgent :as userAgent]))

(def big-web-min-width 684)
; 2 columns 302 * 2 = 604 diff: 80 |30|col1|20|col2|30|
; 1 column 420 diff: 263: |131|420|131|

(def mobile-2-columns-breakpoint 320)

(defn window-exceeds-breakpoint []
  (> (.-clientWidth (.-body js/document)) mobile-2-columns-breakpoint))

(defn columns-num []
  (let [win-width (.-clientWidth (.-body js/document))]
    (cond
      (>= win-width 1012)
      3
      (>= win-width mobile-2-columns-breakpoint)
      2
      :else
      1)))

;; 3 Columns
(def c3-max-win-width 1800) (def c3-max-card-width 420)
(def c3-min-win-width 1012) (def c3-min-card-width 302)
(def c3-win-card-diff (- (/ c3-max-win-width c3-max-card-width) (/ c3-min-win-width c3-min-card-width)))
(def c3-win-diff (- c3-max-win-width c3-min-win-width))
(def c3-min-card-delta (/ c3-min-win-width c3-min-card-width))

;; 2 Columns
(def c2-max-win-width 1011) (def c2-max-card-width 420)
(def c2-padding (if (> (.-clientWidth (.-body js/document)) big-web-min-width) 80 60))
(def c2-min-win-width mobile-2-columns-breakpoint) (def c2-min-card-width (/ (- mobile-2-columns-breakpoint c2-padding) 2))
(def c2-win-card-diff (- (/ c2-max-win-width c2-max-card-width) (/ c2-min-win-width c2-min-card-width)))
(def c2-win-diff (- c2-max-win-width c2-min-win-width))
(def c2-min-card-delta (/ c2-min-win-width c2-min-card-width))

;; 1 Columns
(def c1-max-win-width (dec mobile-2-columns-breakpoint)) (def c1-max-card-width (- (dec mobile-2-columns-breakpoint) 263))
(def c1-min-win-width 414) (def c1-min-card-width 396)
(def c1-win-card-diff (- (/ c1-max-win-width c1-max-card-width) (/ c1-min-win-width c1-min-card-width)))
(def c1-win-diff (- c1-max-win-width c1-min-win-width))
(def c1-min-card-delta (/ c1-min-win-width c1-min-card-width))

(defn win-width [columns]
  (let [ww (.-clientWidth (sel1 js/document :body))]
    (case columns
      3 (max (min ww c3-max-win-width) c3-min-win-width)
      2 (max (min ww c2-max-win-width) c2-min-win-width)
      1 (max (min ww c1-max-win-width) c1-min-win-width))))

(defn get-min-win-width [columns]
  (case columns
    3 c3-min-win-width
    2 c2-min-win-width
    1 c1-min-win-width))

(defn get-win-diff [columns]
  (case columns
    3 c3-win-diff
    2 c2-win-diff
    1 c1-win-diff))

(defn get-win-card-diff [columns]
  (case columns
    3 c3-win-card-diff
    2 c2-win-card-diff
    1 c1-win-card-diff))

(defn get-min-card-delta [columns]
  (case columns
    3 c3-min-card-delta
    2 c2-min-card-delta
    1 c1-min-card-delta))

(defn calc-card-width [& [force-columns]]
  (let [columns (or force-columns (columns-num))
        ww (win-width columns)
        ;; get params based on columns number
        min-win-width (get-min-win-width columns)
        win-diff (get-win-diff columns)
        win-card-diff (get-win-card-diff columns)
        min-card-delta (get-min-card-delta columns)
        ;; calculations
        win-delta-width (- ww min-win-width)
        perc-win-delta  (/ (* win-delta-width 100) win-diff)
        diff-delta      (* (/ win-card-diff 100) perc-win-delta)
        delta           (+ min-card-delta diff-delta)]
      (/ ww delta)))

(def _mobile (atom -1))

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

(defn is-mobile-size? []
 "Check if it's mobile based only on screen size"
 ;; fake the browser type for the moment
 (when (neg? @_mobile)
 (set-browser-type!))
 @_mobile)

(defn user-agent-mobile? []
  userAgent/MOBILE)

(defn is-mobile?
  "Check if it's mobile based on UserAgent or screen size."
  []
  (or (is-mobile-size?) (user-agent-mobile?)))

(defn can-edit? []
  "Check if it's mobile based only on the UserAgent"
  (not (user-agent-mobile?)))

(defn fullscreen-topic-width [card-width]
  (let [ww (.-clientWidth (sel1 js/document :body))]
    (if (> ww big-web-min-width)
      big-web-min-width
      (min card-width ww))))

(defn is-tablet-or-mobile? []
  ;; check if it's test env, can't import utils to avoid circular dependencies
  (if (not (not (.-_phantom js/window)))
    false
    (or (= (gobj/get js/WURFL "form_factor") "Tablet")
        (= (gobj/get js/WURFL "form_factor") "Smartphone")
        (= (gobj/get js/WURFL "form_factor") "Other Mobile"))))

(def topic-total-x-padding 20)
(def updates-content-list-width 280)
(def updates-content-cards-right-margin 40)
(def updates-content-cards-max-width 560)
(def updates-content-cards-min-width 250)

(defn total-layout-width-int [card-width columns-num]
  (- (* (+ card-width topic-total-x-padding) columns-num) ; width of each column less
     (if (is-mobile?) 20 10)                              ; the container padding
     (if (is-mobile?) 40 0)))                             ; the distance btw the columns on big web

(defn calc-update-width [columns-num]
  (let [card-width   (calc-card-width)
        ww           (.-clientWidth (.-body js/document))
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