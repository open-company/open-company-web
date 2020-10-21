(ns oc.web.actions.theme
  "Add the proper class to the html elemenet when the app starts and when the system mode changes.
   NB: The list of the routes not allowed to get dark mode is in the static-js.js file."
  (:require [goog.events :as events]
            [goog.events.EventType :as EventType]
            [taoensso.timbre :as timbre]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.theme :as theme-utils]
            [oc.web.lib.cookies :as cook]))

(def ^:private theme-cookie-name-suffix :ui-theme)

(def ^:private theme-class-name-prefix :theme-mode)

(defn theme-cookie-name []
  (str (name theme-cookie-name-suffix)))

(defn save-theme-cookie [v]
  (cook/set-cookie! (theme-cookie-name) (name v) (* 60 60 24 365)))

(defn read-theme-cookie []
  (let [cookie-val (cook/get-cookie (theme-cookie-name))]
    (timbre/debug "Reading theme from cookie:" cookie-val)
    (if (seq cookie-val)
      (keyword cookie-val)
      theme-utils/theme-default-value)))

(defn set-theme-class [mode]
  (let [html-el (sel1 [:html])]
    (dommy/remove-class! (sel1 [:html]) (str (name theme-class-name-prefix) "-dark"))
    (dommy/remove-class! (sel1 [:html]) (str (name theme-class-name-prefix) "-light"))
    (dommy/add-class! (sel1 [:html]) (str (name theme-class-name-prefix) "-" (name mode)))))

(defn get-theme-setting []
  (let [current-mode (read-theme-cookie)]
    (or current-mode theme-utils/theme-default-value)))

(defn computed-theme []
  (theme-utils/computed-value {dis/theme-setting-key (get-theme-setting)}))

(defn ^:export set-theme [v]
  (let [fixed-value (or v :auto)]
    (save-theme-cookie fixed-value)
    ; (set-theme-class (theme-utils/computed-value {dis/theme-setting-key fixed-value}))
    (dis/dispatch! [:theme/set-setting fixed-value])))

(defonce visibility-change-listener (atom nil))

(defn setup-theme []
  ; (let [cur-val (get-theme-setting)
  ;       computed-val (theme-utils/computed-value cur-val)]
  ;   (timbre/info "Theme:" (name cur-val) "->" (name computed-val))
  ;   ; (set-theme-class computed-val)
  ;   ;; FIXME: use swap! instead of dis/dispatch! since the multimethod have not been intialized yet
  ;   ;; at this point.
  ;   (swap! dis/app-state #(assoc-in % dis/theme-key {dis/theme-setting-key cur-val dis/theme-computed-key computed-val})))
  (timbre/info "Setup UI theme actions")
  (when (theme-utils/electron-mac-theme-supported?)
    (set! (.-onchange (.matchMedia js/window "(prefers-color-scheme: light)"))
          #(when (= (get-theme-setting) :auto)
             (set-theme :auto)))
    (when @visibility-change-listener
      (events/unlistenByKey @visibility-change-listener))
    (reset! visibility-change-listener
            (events/listen js/document EventType/VISIBILITYCHANGE
                           #(when (and (= (.-visibilityState js/document) "visible")
                                       (= (get-theme-setting) :auto))
                              (set-theme :auto))))))

(defn expo-color-scheme-changed! [new-expo-color-theme]
  (dis/dispatch! [:theme/expo-theme new-expo-color-theme]))

(defn pre-routing! []
  (dis/dispatch! [:theme/set-setting (read-theme-cookie)]))

(defn handle-url-change []
  (dis/dispatch! [:theme/routing]))