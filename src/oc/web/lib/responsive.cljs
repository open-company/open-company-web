(ns oc.web.lib.responsive
  (:require [dommy.core :refer-macros (sel1)]
            [oc.web.lib.cookies :as cook]
            [goog.object :as gobj]
            [goog.userAgent :as userAgent]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def big-web-min-width 768)
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
      ; (>= win-width c3-min-win-width)
      ; 3
      (>= win-width mobile-2-columns-breakpoint)
      2
      :else
      1)))

(defn columns-num []
  (let [win-width (ww)]
    (cond
      ; (>= win-width c3-min-win-width)
      ; 3
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
(def topic-total-x-padding 32)
(def topic-list-right-margin 36)
(def left-navigation-sidebar-minimum-right-margin 40)
(def left-navigation-sidebar-width 188)
(def board-container-width 860)

(defn is-tablet-or-mobile? []
  ;; check if it's test env, can't import utils to avoid circular dependencies
  (if (not (not (.-_phantom js/window)))
    false
    (or (= (gobj/get js/WURFL "form_factor") "Tablet")
        (= (gobj/get js/WURFL "form_factor") "Smartphone")
        (= (gobj/get js/WURFL "form_factor") "Other Mobile"))))

(def card-width 432)

(defn can-edit? []
  "Check if it's mobile based only on the UserAgent"
  (not (user-agent-mobile?)))

(defn total-layout-width-int [card-width columns-num]
  (if (is-mobile-size?)
    (let [win-width (ww)]
      (- win-width 8 8))
    (+ (* (+ card-width topic-total-x-padding) columns-num)    ; width of each column plus
       (* topic-list-x-padding 2)
       topic-list-right-margin
       (if (is-tablet-or-mobile?) 0 left-navigation-sidebar-width)))) ; the left side panel with the topics list

(when (not (.-_phantom js/window))
  (events/listen js/window EventType/RESIZE #(set-browser-type!)))