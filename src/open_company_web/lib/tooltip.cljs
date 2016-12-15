(ns open-company-web.lib.tooltip)

;; Cljs wrapper for Tooltip js object that is in /lib/tooltip/tooltip.js
;; Docs at https://github.com/darsain/tooltip/wiki

(defn tooltip [content el position]
  (.position
    (js/Tooltip. content #js {:baseClass "js-tooltip"
                              :effect "fade in"
                              :auto true
                              :place position})
    el))

(defn show [tip]
  (.show tip)
  (.on (js/$ (.-body js/document)) "click" #(.hide tip)))

(defn hide [tip]
  (.hide tip))