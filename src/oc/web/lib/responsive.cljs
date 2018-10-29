(ns oc.web.lib.responsive
  (:require [oc.web.lib.cookies :as cook]
            [goog.object :as gobj]
            [goog.userAgent :as userAgent]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def big-web-min-width 768)
(def tablet-max-width 980)
(def navbar-height 56)

(defn ww []
  (when (and js/document
             (.-body js/document)
             (.-clientWidth (.-body js/document)))
    (.-clientWidth (.-body js/document))))

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

(def left-navigation-sidebar-minimum-right-margin 16)
(def left-navigation-sidebar-width 200)
(def dashboard-container-width 732)

(defn is-tablet-or-mobile? []
  ;; check if it's test env, can't import utils to avoid circular dependencies
  (if (.-_phantom js/window)
    false
    (if-not (.-WURFL js/window)
      (<= (ww) tablet-max-width)
      (or (= (gobj/get js/WURFL "form_factor") "Tablet")
          (= (gobj/get js/WURFL "form_factor") "Smartphone")
          (= (gobj/get js/WURFL "form_factor") "Other Mobile")))))

(when-not (.-_phantom js/window)
  (events/listen js/window EventType/RESIZE set-browser-type!))