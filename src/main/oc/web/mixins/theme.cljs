(ns oc.web.mixins.theme
  (:require [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.theme :as theme-actions]
            [oc.web.utils.theme :as theme-utils]))

(defn theme-mixin []
  (let [last-theme-computed-value (atom nil)]
    {:will-mount (fn [s]
                   (let [theme-data @(drv/get-ref s :theme)
                         theme-computed-value (theme-utils/computed-value theme-data)]
                     (when theme-computed-value
                       (reset! last-theme-computed-value theme-computed-value)
                       (theme-utils/set-theme-class theme-computed-value)))
                   s)
    :will-update (fn [s]
                  (let [theme-data @(drv/get-ref s :theme)
                        theme-computed-value (theme-utils/computed-value theme-data)]
                    (when (not= theme-computed-value @last-theme-computed-value)
                      (reset! last-theme-computed-value theme-computed-value)
                      (theme-utils/set-theme-class theme-computed-value)))
                  s)}))