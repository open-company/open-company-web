(ns oc.web.utils.stripe
  (:require [oc.web.local-settings :as ls]))

(def stripe-obj (atom nil))

(defn- init []
  (reset! stripe-obj (js/Stripe. ls/stripe-api-key)))

(defn stripe []
  (when (nil? @stripe-obj)
    (init))
  @stripe-obj)

(defn redirect-to-checkout [session-data & [callback]]
  (let [checkout-promise (.redirectToCheckout ^js (stripe) (clj->js session-data))]
    (.then checkout-promise
     (fn [res]
      (when (fn? callback)
        (callback res))))))