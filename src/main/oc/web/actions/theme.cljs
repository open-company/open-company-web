(ns oc.web.actions.theme
  "Add the proper class to the html elemenet when the app starts and when the system mode changes.
   NB: The list of the routes not allowed to get dark mode is in the static-js.js file."
  (:require [goog.events :as events]
            [goog.events.EventType :as EventType]
            [taoensso.timbre :as timbre]
            [cuerdas.core :as cstr]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.theme :as theme-utils]
            [oc.web.lib.cookies :as cook]))

(def ^:private theme-cookie-name-suffix :ui-theme)

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

(defn get-theme-setting []
  (let [current-mode (read-theme-cookie)]
    (or current-mode theme-utils/theme-default-value)))

(defn ^:export set-theme-setting [v]
  (let [fixed-value (or v :auto)]
    (save-theme-cookie fixed-value)
    (dis/dispatch! [:theme/set-setting fixed-value])))

(defonce visibility-change-listener (atom nil))

(defn expo-color-scheme-changed! [new-mobile-color-theme]
  (dis/dispatch! [:theme/mobile-theme-changed new-mobile-color-theme (read-theme-cookie)]))

(defn pre-routing! []
  (dis/dispatch! [:theme/set-setting (read-theme-cookie)]))

(defn setup-change-listeners []
  (when (theme-utils/web-theme-supported?)
    (set! (.-onchange (.matchMedia js/window "(prefers-color-scheme: light)"))
          #(dis/dispatch! [:theme/web-theme (theme-utils/web-theme)])))
  (when @visibility-change-listener
    (events/unlistenByKey @visibility-change-listener))
  (reset! visibility-change-listener
          (events/listen js/document EventType/VISIBILITYCHANGE
                         #(when (= (.-visibilityState js/document) "visible")
                            (let [web-value (when (theme-utils/web-theme-supported?) (theme-utils/web-theme))
                                  mobile-value (when (theme-utils/mobile-theme-supported?) (theme-utils/mobile-theme))
                                  desktop-value (when (theme-utils/desktop-theme-supported?) (theme-utils/desktop-theme))]
                              (dis/dispatch! [:theme/visibility-changed web-value desktop-value mobile-value (read-theme-cookie)]))))))

(def dark-not-allowed-routes ["sign-up"
                              "login"
                              "password-reset"
                              "email-verification"
                              "confirm-invitation"
                              "email-wall"])

(defn- exclude-signup [computed-value]
  (if (and (= computed-value :dark)
           (re-matches (re-pattern (str "^\\/(" (cstr/join "|" dark-not-allowed-routes) ")(\\/|\\?|#|$)")) (router/get-token)))
    :light
    computed-value))

(defn ^:export setup-theme
  "Insert the computed theme in the app-state directly without using dispatch! since it might not be initialized yet."
  []
  (timbre/info "Setup UI theme data and listeners")
  (let [setting-value (read-theme-cookie)
        theme-map (assoc (get @dis/app-state dis/theme-key)
                         dis/theme-setting-key setting-value
                         dis/theme-web-key     (theme-utils/web-theme))
        computed-value (theme-utils/computed-value theme-map)]
    (timbre/info "Theme:" (name setting-value) "->" (name computed-value))
    ;; FIXME: use swap! instead of dis/dispatch! since the multimethod have not been intialized yet
    ;; at this point.
    (swap! dis/app-state update-in dis/theme-key #(merge % theme-map))
    (theme-utils/set-theme-class (exclude-signup computed-value))
    (setup-change-listeners)))

(defn ^:export desktop-theme-changed []
  (dis/dispatch! [:theme/desktop-theme-changed (theme-utils/desktop-theme) (read-theme-cookie)]))