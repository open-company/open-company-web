(ns open-company-web.lib.tooltip
  "Cljs wrapper for Tooltip js object that is in /lib/tooltip/tooltip.js
   Docs at https://github.com/darsain/tooltip/wiki"
  (:require [defun.core :refer (defun)]))

(defn tooltip
  "Create a tooltip, attach it to the passed element at the gived position and return it"
  [content el config]
  (when el
    (.position
      (js/Tooltip. content (clj->js (merge {:baseClass "js-tooltip"
                                            :effect "fade in"
                                            :auto true
                                            :place "bottom"}
                                           config)))
      el)))

(defn hide
  "Programmatically hide the passed tooltip"
  [tip]
  (.hide tip))

(defun show
  "Show the passed tooltip. Optionally disable the hide on click."
  ([tip] (show tip true))
  ([tip false] (.show tip))
  ([tip true]
    (let [$body (js/$ (.-body js/document))]
      (.show tip)
      (.on $body "click" (fn []
                           (js/console.log "Body click!")
                           (hide tip)
                           (.off $body "click"))))))