(ns oc.web.actions.dark-mode
  (:require [taoensso.timbre :as timbre]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.shared.useragent :as ua]
            [oc.web.lib.cookies :as cook]))

(def ^:private dark-mode-cookie-name-suffix :dark-mode)

(def ^:private dark-mode-class-name-prefix :theme-mode)

(def ^:private dark-mode-values #{:dark :light :auto})

(def ^:private dark-mode-default-value :auto)

(defn dark-mode-cookie-name []
  (str (name dark-mode-cookie-name-suffix)))

(defn save-dark-mode-cookie [v]
  (cook/set-cookie! (dark-mode-cookie-name) (name v) (* 60 60 24 365)))

(defn read-dark-mode-cookie []
  (let [cookie-val (cook/get-cookie (dark-mode-cookie-name))]
    (timbre/debug "Reading theme from cookie:" cookie-val)
    (if (seq cookie-val)
      (keyword cookie-val)
      dark-mode-default-value)))

(defn- set-dark-mode-class [mode]
  (let [html-el (sel1 [:html])]
    (dommy/remove-class! (sel1 [:html]) (str (name dark-mode-class-name-prefix) "-dark"))
    (dommy/remove-class! (sel1 [:html]) (str (name dark-mode-class-name-prefix) "-light"))
    (dommy/add-class! (sel1 [:html]) (str (name dark-mode-class-name-prefix) "-" (name mode)))))

(defn support-system-dark-mode? []
  (or ;; Electron wrapper on mac has always support for auto dark mode
      (and ua/desktop-app?
           ua/mac?)
      ;; On web we need to check if the media query is supported
      (and (exists? js/window.matchMedia)
           (or (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
               (.-matches (.matchMedia js/window "(prefers-color-scheme: light)"))))))

(defn system-dark-mode-enabled? []
  (if (and ua/mac?
           ua/desktop-app?)
    (js/OCCarrotDesktop.isDarkMode)
    (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))))

(defn computed-value [v]
  (if (= v :auto)
    (if (support-system-dark-mode?)
      (if (system-dark-mode-enabled?)
        :dark
        :light)
      dark-mode-default-value)
    v))

(defn get-dark-mode-setting []
  (let [current-mode (read-dark-mode-cookie)]
    (or current-mode dark-mode-default-value)))

(defn ^:export set-dark-mode [v]
  (let [fixed-value (or v :auto)]
    (timbre/debug "Saving theme:" (name fixed-value) "(" v ")")
    (save-dark-mode-cookie fixed-value)
    (set-dark-mode-class (computed-value fixed-value))
    (dis/dispatch! [:input dis/dark-mode-key (computed-value fixed-value)])))

(defn setup-dark-mode []
  (let [cur-val (get-dark-mode-setting)
        computed-val (computed-value cur-val)]
    (timbre/info "Theme:" (name cur-val) "->" (name computed-val))
    (set-dark-mode-class computed-val)
    ;; FIXME: use swap! instead of dis/dispatch! since the multimethod have not been intialized yet
    ;; at this point.
    (swap! dis/app-state #(assoc-in % dis/dark-mode-key computed-val))
    (when (support-system-dark-mode?)
      (set! (.-onchange (.matchMedia js/window "(prefers-color-scheme: light)"))
       #(when (= (get-dark-mode-setting) :auto)
          (set-dark-mode :auto))))))

(setup-dark-mode)

