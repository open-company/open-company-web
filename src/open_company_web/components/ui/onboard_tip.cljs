(ns open-company-web.components.ui.onboard-tip
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [open-company-web.lib.responsive :as responsive]))

;; ===== Events =====

(defn- stop-listening
  "Unbind the click listener."
  [s]
  (events/unlistenByKey (::click-listener s)))

(defn- on-click-out 
  "
  Handle a click to dismiss the tooltip by: 
  * updating state
  * removing the listener
  * setting the cookie (optional)
  * calling back the optional callback function (optional)
  "
  [s _]
  (reset! (::dismissed s) true)
  (stop-listening s)
  (let [args (first (:rum/args s))
        id (:id args)
        once-only (:once-only args)
        callback (:dismiss-tip-fn args)]
    (when once-only
      ;; TODO set the cookie
      )
    (when callback
      ((callback)))))

;; ===== Utility functions =====

(defn- skip-on-device?
  "If there is no data for this device type, then return true to skip showing this tip."
  [mobile desktop]
  (if (responsive/is-mobile?)
    (not (string? mobile))
    (not (string? desktop))))

(defn- already-shown? 
  "If this is a once-only tip and there is a cookie saying we've already shown it, return true."
  [id once-only]
  false)

(defn- shown?
  "Return true if the tip should be shown, based on local dismissal state, device and already shown cookie."
  [s]
  (let [dismissed (::dismissed s)
        args (first (:rum/args s))
        id (:id args)
        once-only (:once-only args)
        mobile (:mobile args)
        desktop (:desktop args)]
    
    (if (or (not dismissed) @dismissed)
      false ; don't show it, the user dismissed it
      (if (or (skip-on-device? mobile desktop) (already-shown? id once-only))
        false ; don't show it, there is no data for this device type, or this tip ID has already been shown to this user
        true)))) ; show it

;; ===== Onboard Tip Component =====

(rum/defcs onboard-tip 
  "
  A simple modal dialog to display a message to the user that dismisses with a click anywhere.

  Options -
  :id unique string identifier for this tooltip, often includes the company slug the tip is being show with
  :once-only boolean value which indicates the user should only see the tooltip identified by the id once,
              implemented with a cookie
  :mobile the string message to display to mobile users, or can be false to not show anything to mobile users
  :desktop the string message to display to desktop users, or can be false to not show anything to desktop users
  :css-class CSS class name(s) as a string to customize the tip's style
  :dismiss-tip-fn callback function when onboard tip is dismissed
  "

  < (rum/local false ::dismissed)

  {
   ;; Listen for a click to dismiss the onboard tip
   :did-mount (fn [s] (when (shown? s)
                        (let [click-listener (events/listen (sel1 [:body]) EventType/CLICK (partial on-click-out s))]
                          (merge s {::click-listener click-listener}))))
  
   ;; Stop listening for clicks to dismiss the onboard tip
   :will-unmount (fn [s] (stop-listening s)
                         (dissoc s ::click-listener))}

  [s {:keys [id once-only mobile desktop css-class]}]

  ;; assertions of proper usage
  (assert (string? id) "onboard-tip id not a string")
  (assert (boolean? once-only) "onboard-tip once-only not a boolean")
  (assert (or (string? mobile) (string? desktop)) "onboard-tip at least one of mobile or desktop not a string")
  (assert (or (not css-class) (string? css-class)) "onboard-tip css-class not a string")

  (when (shown? s)
    [:div {:class (str "tooltip-container " css-class)}
      [:div {:class (str "tooltip-box")}
        [:div {:class "triangle"}]
        [:div {:class "tooltip-cta"} (if (responsive/is-mobile?) mobile desktop)]]]))