(ns oc.web.mixins.theme
  (:require [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.theme :as theme-actions]))

(defn theme-mixin []
  (let [last-theme-computed-value (atom nil)
        last-route-dark-allowed-value (atom false)]
    {:will-mount (fn [s]
                   (let [theme-computed-value (-> s (drv/get-ref :theme) deref dis/theme-computed-key)
                         route-dark-allowed-value @(drv/get-ref s :route/dark-allowed)]
                     (when theme-computed-value
                      (reset! last-theme-computed-value theme-computed-value)
                      (theme-actions/set-theme-class theme-computed-value)
                      (reset! last-route-dark-allowed-value route-dark-allowed-value)))
                   s)
    :will-update (fn [s]
                  (let [theme-computed-value (-> s (drv/get-ref :theme) deref dis/theme-computed-key)
                        route-dark-allowed-value @(drv/get-ref s :route/dark-allowed)]
                    (when (or (not= theme-computed-value @last-theme-computed-value)
                              (not= route-dark-allowed-value @last-route-dark-allowed-value))
                      (reset! last-theme-computed-value theme-computed-value)
                      (reset! last-route-dark-allowed-value route-dark-allowed-value)
                      (theme-actions/set-theme-class theme-computed-value)))
                  s)}))