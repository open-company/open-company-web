(ns oc.web.rum-utils
  (:require [org.martinklepsch.derivatives :as drv]
            [rum.core :as rum]
            [oc.web.lib.sentry :as sentry]))

(rum/defcs app < (drv/rum-derivatives* first)
                 {:did-catch (fn [s error error-info]
                   (sentry/capture-error! error error-info)
                   s)}
  [state spec component]
  (component))

(defn drv-root [{:keys [state drv-spec target component]}]
  ; unmount rum component if mounted to the same node
  (rum/unmount target)
  ; mount component
  (rum/mount (app drv-spec component) target))