(ns oc.shared.useragent
  (:require [goog.userAgent :as ua]
            [goog.userAgent.platform :as plat]
            [goog.userAgent.product :as prod]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; OS Detection

(def mac? ua/MAC)
(def windows? ua/WINDOWS)
(def linux? ua/LINUX)
(def android? ua/ANDROID)
(def ios? ua/IOS)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Browser detection

(def edge? ua/EDGE)
(def ie-or-edge? ua/EDGE_OR_IE)
(def gecko? ua/GECKO)
(def ie? ua/IE)
(def opera? ua/OPERA)
(def chrome? prod/CHROME)
(def safari? prod/SAFARI)
(def firefox? prod/FIREFOX)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Platform detection

(def ipad? ua/IPAD)
(def iphone? ua/IPHONE)
(def ipod? ua/IPOD)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; General detection

(def mobile? ua/MOBILE)
(def webkit? ua/WEBKIT)
(def x11? ua/X11)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Version awareness

(def ^{:doc "The string that describes the version number of the user agent."}
  browser-version
  ua/VERSION)

(def ^{:doc "
Detects the version of the OS/platform the browser is running in.
Not supported for Linux, where an empty string is returned."}
  os-version
  plat/VERSION)

(defn browser-version-or-higher?
  "Returns true if the browser version is v or higher."
  [v]
  (ua/isVersionOrHigher v))

(defn os-version-or-higher?
  "Returns true if the OS/platform is v or higher."
  [v]
  (plat/isVersion v))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Carrot-specific detection

(def ^{:doc "Whether we're running in the desktop application"}
  desktop-app?
  ;; injected by the Electron shell
  (some? js/window.OCCarrotDesktop))

(def ^{:doc "Whether we're running in the mobile application"}
  mobile-app?
  ;; injected by the Expo shell
  (some? js/window.ReactNativeWebView))

(def ^{:doc "Whether we're running in a web-in-native wrapper (e.g. electron, expo, etc)"}
  pseudo-native?
  (or desktop-app? mobile-app?))
