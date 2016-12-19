(ns open-company-web.lib.tooltip
  "Cljs wrapper for Tooltip js object that is in /lib/tooltip/tooltip.js
   Docs at https://github.com/darsain/tooltip/wiki"
  (:require [defun.core :refer (defun)]
            [open-company-web.lib.cookies :as cookie]
            [open-company-web.lib.responsive :as responsive]))

(def ten-years (* 60 60 24 365 10))

(defn- skip-on-device?
  "If there is no data for this device type, then return true to skip showing this tip."
  [mobile desktop]
  (if (responsive/is-mobile-size?)
    (not (string? mobile))
    (not (string? desktop))))

(defn- already-shown?
  "If this is a once-only tip and there is a cookie saying we've already shown it, return true."
  [id once-only]
  (if once-only
    (cookie/get-cookie id)
    false))

(defn get-device-content [mobile desktop]
  (if (responsive/is-mobile-size?)
    mobile
    desktop))

(defonce tooltips (atom {}))

(defn real-hide [^js/Tooltip t]
  (.hide t))

(defn- hide-tooltip
  [tt tip-id]
  (when (:tip tt)
    (real-hide (:tip tt)))
  (reset! tooltips (dissoc @tooltips tip-id))
  (when (:once-only (:setup tt))
    (cookie/set-cookie! tip-id true ten-years))
  (when (fn? (:dismiss-cb (:setup tt)))
    ((:dismiss-cb (:setup tt)))))

(defn tooltip
  "Create a tooltip, attach it to the passed element at the gived position and return it"
  [el {:keys [id mobile desktop once-only dismiss-cb config] :as setup}]
  (when el
    (when-let [tt (get @tooltips id)]
      (hide-tooltip (:tip tt) id))
    (let [tt (js/Tooltip. (get-device-content mobile desktop) (clj->js (merge {:baseClass "js-tooltip"
                                                                               :effect "fade in"
                                                                               :auto true
                                                                               :place "bottom"}
                                                                        config)))]
    (reset! tooltips (assoc @tooltips id {:tip tt
                                          :el el
                                          :setup setup}))
    (.position tt el))))

(defn hide
  "Programmatically hide the passed tooltip"
  [tip-id]
  (let [tt (get @tooltips tip-id)]
    (hide-tooltip tt tip-id)))

(defn show
  "Show the passed tooltip. Optionally disable the hide on click."
  ([tip-id]
    (let [tt (get @tooltips tip-id)
          tip (:tip tt)]
      (if (and (not (skip-on-device? (:mobile (:setup tt)) (:desktop (:setup tt))))
               (not (already-shown? tip-id (:once-only (:setup tt)))))
        (let [$body (js/$ (.-body js/document))]
          (.show tip)
          (when-not (:persistent (:setup tt))
            (.on $body "click" (fn []
                                 (hide-tooltip tt tip-id)
                                 (.off $body "click")))))
        (reset! tooltips (dissoc @tooltips tip-id))))))