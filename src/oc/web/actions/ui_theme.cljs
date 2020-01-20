(ns oc.web.actions.ui-theme
  (:require [taoensso.timbre :as timbre]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.shared.useragent :as ua]
            [oc.web.lib.cookies :as cook]))

(def ^:private ui-theme-cookie-name-suffix :ui-theme)

(def ^:private ui-theme-class-name-prefix :theme-mode)

(def ^:private ui-theme-values #{:dark :light :auto})

(def ^:private ui-theme-default-value :auto)

(defn- dark-allowed-path? []
  (-> (.. js/window -location -pathname)
      (.match #"(?i)^/(\bsign\-up\b|\blogin\b)?(/|$)")
      seq
      not))

(defn ui-theme-cookie-name []
  (str (name ui-theme-cookie-name-suffix)))

(defn save-ui-theme-cookie [v]
  (cook/set-cookie! (ui-theme-cookie-name) (name v) (* 60 60 24 365)))

(defn read-ui-theme-cookie []
  (let [cookie-val (cook/get-cookie (ui-theme-cookie-name))]
    (timbre/debug "Reading theme from cookie:" cookie-val)
    (if (seq cookie-val)
      (keyword cookie-val)
      ui-theme-default-value)))

(defn- set-ui-theme-class [mode]
  (let [html-el (sel1 [:html])]
    (dommy/remove-class! (sel1 [:html]) (str (name ui-theme-class-name-prefix) "-dark"))
    (dommy/remove-class! (sel1 [:html]) (str (name ui-theme-class-name-prefix) "-light"))
    (dommy/add-class! (sel1 [:html]) (str (name ui-theme-class-name-prefix) "-" (name mode)))))

(defn support-system-dark-mode? []
  (or ;; Electron wrapper on mac has always support for auto dark mode
      (and ua/desktop-app?
           ua/mac?)
      ;; On web we need to check if the media query is supported
      (and (exists? js/window.matchMedia)
           (or (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
               (.-matches (.matchMedia js/window "(prefers-color-scheme: light)"))))))

(defn system-ui-theme-enabled? []
  (if (and ua/mac?
           ua/desktop-app?
           (exists? js/OCCarrotDesktop.isDarkMode))
    (js/OCCarrotDesktop.isDarkMode)
    (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))))

(defn computed-value [v]
  (if (and (support-system-dark-mode?)
           (dark-allowed-path?)
           (or (and (= v :auto)
                     (system-ui-theme-enabled?))
               (= v :dark)))
    :dark
    :light))

(defn get-ui-theme-setting []
  (let [current-mode (read-ui-theme-cookie)]
    (or current-mode ui-theme-default-value)))

(defn ^:export set-ui-theme [v]
  (let [fixed-value (or v :auto)]
    (timbre/debug "Saving theme:" (name fixed-value) "(" v ")")
    (save-ui-theme-cookie fixed-value)
    (set-ui-theme-class (computed-value fixed-value))
    (dis/dispatch! [:input dis/ui-theme-key {:setting-value fixed-value :computed-value (computed-value fixed-value)}])))

(defn setup-ui-theme []
  (let [cur-val (get-ui-theme-setting)
        computed-val (computed-value cur-val)]
    (timbre/info "Theme:" (name cur-val) "->" (name computed-val))
    (set-ui-theme-class computed-val)
    ;; FIXME: use swap! instead of dis/dispatch! since the multimethod have not been intialized yet
    ;; at this point.
    (swap! dis/app-state #(assoc-in % dis/ui-theme-key {:setting-value cur-val :computed-value computed-val}))
    (when (support-system-dark-mode?)
      (set! (.-onchange (.matchMedia js/window "(prefers-color-scheme: light)"))
       #(when (= (get-ui-theme-setting) :auto)
          (set-ui-theme :auto))))))

(setup-ui-theme)

