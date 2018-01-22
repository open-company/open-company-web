(ns oc.web.rum-utils
  (:require [org.martinklepsch.derivatives :as drv]
            [rum.core :as rum]))

(rum/defc app < (drv/rum-derivatives* first)
  [spec component]
  (component))

(defn drv-root [{:keys [state drv-spec target component]}]
  ; unmount rum component if mounted to the same node
  (rum/unmount target)
  ; mount component
  (rum/mount (app drv-spec component) target))