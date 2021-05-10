(ns oc.web.utils.rum
  (:require [org.martinklepsch.derivatives :as drv]
            [rum.core :as rum]
            ["react" :as react :refer (createElement)]
            [oc.web.utils.sentry :as sentry]))

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

(defn build [component props & children]
  (apply createElement component (clj->js props) children))

(def create-element build)