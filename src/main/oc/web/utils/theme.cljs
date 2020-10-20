(ns oc.web.utils.theme
  (:require [oc.lib.cljs.useragent :as ua]
            [oc.web.dispatcher :as dis]))

(def theme-default-value :auto)

(def theme-values #{:dark :light theme-default-value})

(defn dark-allowed-path? []
  (-> (.. js/window -location -pathname)
      (.match (.-OCWebUIThemeAllowedPathRegExp js/window))
      seq
      not))

(defn electron-mac-theme-supported? []
  (or ;; Electron wrapper on mac has always support for auto dark mode
   (and ua/desktop-app?
        ua/mac?)
      ;; On web we need to check if the media query is supported
   (and (exists? js/window.matchMedia)
        (or (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
            (.-matches (.matchMedia js/window "(prefers-color-scheme: light)"))))))

(defn electron-mac-dark-theme-enabled? []
  (if (and ua/mac?
           ua/desktop-app?
           (exists? js/OCCarrotDesktop.isDarkMode))
    (js/OCCarrotDesktop.isDarkMode)
    (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))))

(defn- expo-theme-supported? []
  (and ua/mobile-app?
       (or ua/ios?
           ua/android?)))

(defn computed-value
  [theme-map]
  (let [theme-setting (get theme-map dis/theme-setting-key)]
    (if (dark-allowed-path?)
      (if (= theme-setting :auto)
        (cond
          (electron-mac-theme-supported?)
          (if (electron-mac-dark-theme-enabled?)
            :dark
            :light)
          (expo-theme-supported?)
          (if (= (get theme-map dis/expo-key) :dark)
            :dark
            :light)
          :else
          theme-default-value)
        theme-setting)
      :light)))