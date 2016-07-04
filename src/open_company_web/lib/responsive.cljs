(ns open-company-web.lib.responsive
  (:require [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.cookies :as cook]
            [goog.userAgent :as userAgent]))

(defn columns-num []
  (let [win-width (.-clientWidth (.-body js/document))]
    (cond
      (>= win-width 1006)
      3
      (>= win-width 684)
      2
      :else
      1)))

;; 3 Columns
(def c3-min-win-width 1006)
(def c3-max-win-width 1800)
(def c3-min-card-width 302)
(def c3-max-card-width 500)
(def c3-win-card-diff (- (/ c3-max-win-width c3-max-card-width) (/ c3-min-win-width c3-min-card-width)))
(def c3-win-diff (- c3-max-win-width c3-min-win-width))
(def c3-min-card-delta (/ c3-min-win-width c3-min-card-width))

;; 2 Columns
(def c2-min-win-width 684)
(def c2-max-win-width 1005)
(def c2-min-card-width 302)
(def c2-max-card-width 462)
(def c2-win-card-diff (- (/ c2-max-win-width c2-max-card-width) (/ c2-min-win-width c2-min-card-width)))
(def c2-win-diff (- c2-max-win-width c2-min-win-width))
(def c2-min-card-delta (/ c2-min-win-width c2-min-card-width))

;; 1 Columns
(def c1-min-win-width 414)
(def c1-max-win-width 683)
(def c1-min-card-width 396)
(def c1-max-card-width 500)
(def c1-win-card-diff (- (/ c1-max-win-width c1-max-card-width) (/ c1-min-win-width c1-min-card-width)))
(def c1-win-diff (- c1-max-win-width c1-min-win-width))
(def c1-min-card-delta (/ c1-min-win-width c1-min-card-width))

(defn win-width [columns]
  (let [ww (.-clientWidth (sel1 js/document :body))]
    (case columns
      3
      (max (min ww c3-max-win-width) c3-min-win-width)
      2
      (max (min ww c2-max-win-width) c2-min-win-width)
      1
      (max (min ww c1-max-win-width) c1-min-win-width))))

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

(def big-web-min-width 684)

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

(defn can-edit? []
  (not userAgent/MOBILE))