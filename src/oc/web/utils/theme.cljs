(ns oc.web.utils.theme
  (:require [oc.lib.cljs.useragent :as ua]
            [oc.web.dispatcher :as dis]))

(def dark-theme-not-allowed-routes [:sign-up
                                    :login
                                    :password-reset
                                    :email-verification
                                    :confirm-invitation
                                    :email-wall
                                    :login-wall])

(def theme-default-value :auto)

(def theme-values #{:dark :light theme-default-value})

(defn dark-allowed-path? []
  (not (some dark-theme-not-allowed-routes (dis/route-param [dis/router-opts-key dis/router-dark-allowed-key]))))

(defn electron-mac-theme-supported? []
  (or ;; Electron wrapper on mac has always support for auto dark mode
   (and ua/desktop-app?
        ua/mac?)
      ;; On web we need to check if the media query is supported
   (and (exists? js/window.matchMedia)
        (or (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
            (.-matches (.matchMedia js/window "(prefers-color-scheme: light)"))))))

(defn electron-mac-theme []
  (if (and ua/desktop-app?
           ua/mac?
           (or (and (exists? js/OCCarrotDesktop.isDarkMode)
                    (js/OCCarrotDesktop.isDarkMode))
               (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))))
    :dark
    :auto))

(defn- expo-theme-supported? []
  (and ua/mobile-app?
       (or ua/ios?
           ua/android?)))

(defn computed-value
  [theme-map]
  (let [theme-setting (get theme-map dis/theme-setting-key)]
    (if (or ua/pseudo-native?
            (dark-allowed-path?))
      (if (= theme-setting :auto)
        (cond
          (expo-theme-supported?)
          (or (get theme-map dis/theme-expo-key) :light)
          (electron-mac-theme-supported?)
          (or (electron-mac-theme) :light)
          :else
          theme-default-value)
        theme-setting)
      :light)))