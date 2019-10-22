(ns oc.web.utils.stripe
  (:require [taoensso.timbre :as timbre]
            [oc.web.local-settings :as ls]))

(def stripe-obj (atom nil))

(defn- init []
  (reset! stripe-obj (js/Stripe. ls/stripe-api-key)))

(defn stripe []
  (when (nil? @stripe-obj)
    (init))
  @stripe-obj)

(defn redirect-to-checkout [session-data & [callback]]
  (let [s (stripe)
        checkout-promise (.redirectToCheckout s
                          (clj->js session-data))]
    (.then checkout-promise
     (fn [res]
      (when (.-error res)
       (timbre/warn "Error during Stripe checkout:" (.. res -error -message))
       (js/alert "Error during checkout:" (.. res -error -message)))
      (when (fn? callback)
        (callback res))))))