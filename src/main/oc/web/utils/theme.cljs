(ns oc.web.utils.theme
  (:require [oc.lib.cljs.useragent :as ua]
            [oc.web.dispatcher :as dis]
            [oops.core :refer (oget ocall)]))

(def ^{:private true} theme-class-name-prefix :theme-mode)

(def theme-default-value :auto)

(def theme-values #{:dark :light theme-default-value})

(defn dark-allowed-path? []
  (-> [dis/router-opts-key]
      (dis/route-param)
      set
      dis/router-dark-allowed-key))

(defn web-theme-supported? []
  (and (exists? js/window.matchMedia)
       (or (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
           (.-matches (.matchMedia js/window "(prefers-color-scheme: light)")))))

(defn web-theme []
  (if (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
    :dark
    :light))

(defn desktop-theme-supported? []
  (and ua/desktop-app?
       ua/mac?))

(defn desktop-theme []
  (if (and (exists? js/OCCarrotDesktop.isDarkMode)
           (js/OCCarrotDesktop.isDarkMode))
    :dark
    :light))

(defn mobile-theme-supported? []
  (and ua/mobile-app?
       (or ua/ios?
           ua/android?)))

(defn mobile-theme []
  (if (= (get-in @dis/app-state (conj dis/theme-key dis/theme-mobile-key)) :dark)
    :dark
    :light))

(defn set-theme-class [mode]
  (when-let [html-el (oget js/window "document.documentElement")]
    (ocall html-el "classList.remove" (str (name theme-class-name-prefix) "-dark"))
    (ocall html-el "classList.remove" (str (name theme-class-name-prefix) "-light"))
    (when mode
      (ocall html-el "classList.add" (str (name theme-class-name-prefix) "-" (name mode))))))

(defn computed-value
  [theme-map]
  (let [theme-setting (get theme-map dis/theme-setting-key)]
    ;; If path supports dark mode
    (if (dark-allowed-path?)
      ;; If user has never changed theme or has set it to auto
      (if (= theme-setting :auto)
        ;; Get first available btw: desktop/mobile/web themes
        (or (some theme-map [dis/theme-mobile-key dis/theme-desktop-key dis/theme-web-key]) :light)
        ;; Fallback to the user preference
        theme-setting)
      ;; Fallback to light
      :light)))