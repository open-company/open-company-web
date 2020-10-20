(ns oc.web.mixins.theme
  (:require [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.theme :as theme-actions]))

(defn theme-mixin []
  (let [last-theme-computed-value (atom nil)]
    {:will-mount (fn [s]
                   (let [theme-computed-value (-> s (drv/get-ref :theme) deref dis/theme-computed-key)]
                     (reset! last-theme-computed-value theme-computed-value)
                     (theme-actions/set-theme-class theme-computed-value))
                   s)
    :did-update (fn [s]
                  (let [theme-computed-value (-> s (drv/get-ref :theme) deref dis/theme-computed-key)]
                    (when (not= theme-computed-value @last-theme-computed-value)
                      (reset! last-theme-computed-value theme-computed-value)
                      (theme-actions/set-theme-class theme-computed-value)))
                  s)}))