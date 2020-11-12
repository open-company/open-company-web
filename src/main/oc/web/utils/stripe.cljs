(ns oc.web.utils.stripe
  (:require [oc.web.local-settings :as ls]
            ["stripe" :as Stripe]))

(def stripe-obj (atom nil))

(defn- init []
  (reset! stripe-obj (Stripe. ls/stripe-api-key)))

(defn stripe []
  (when (nil? @stripe-obj)
    (init))
  @stripe-obj)

(defn redirect-to-checkout [session-data & [callback]]
  (let [s (stripe)
        checkout-promise (.redirectToCheckout ^js s
                          (clj->js session-data))]
    (.then checkout-promise
     (fn [res]
      (when (fn? callback)
        (callback res))))))