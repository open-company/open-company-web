(ns oc.web.utils.theme
  (:require [oc.lib.cljs.useragent :as ua]
            [oc.web.dispatcher :as dis]))

(def theme-default-value :auto)

(def theme-values #{:dark :light theme-default-value})

(defn dark-allowed-path? []
  (-> [dis/router-opts-key]
      (dis/route-param)
      set
      dis/router-dark-allowed-key))

(defn prefers-color-scheme-supported? []
  (and (exists? js/window.matchMedia)
       (or (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
           (.-matches (.matchMedia js/window "(prefers-color-scheme: light)")))))

(defn- prefers-color-scheme []
  (if (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
    :dark
    :light))

(defn electron-mac-theme-supported? []
  (and ua/desktop-app?
      ua/mac?
      (exists? js/window.matchMedia)
      (or (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
          (.-matches (.matchMedia js/window "(prefers-color-scheme: light)")))))

(defn- electron-mac-theme []
  (if (and (exists? js/OCCarrotDesktop.isDarkMode)
           (js/OCCarrotDesktop.isDarkMode))
    :dark
    :light))

(defn expo-theme-supported? []
  (and ua/mobile-app?
       (or ua/ios?
           ua/android?)))

(defn- expo-theme [theme-map]
  (if (= (get theme-map dis/theme-expo-key) :dark)
    :dark
    :light))

(defn dark-allowed-device? []
  (or (electron-mac-theme-supported?)
      (expo-theme-supported?)
      (prefers-color-scheme-supported?)))

(defn computed-value
  [theme-map]
  (let [theme-setting (get theme-map dis/theme-setting-key)]
    (if (and (dark-allowed-device?)
            (dark-allowed-path?))
      (if (= theme-setting :auto)
        (cond
          (expo-theme-supported?)
          (expo-theme theme-map)
          (electron-mac-theme-supported?)
          (electron-mac-theme)
          (prefers-color-scheme-supported?)
          (prefers-color-scheme)
          :else
          :light)
        theme-setting)
      :light)))