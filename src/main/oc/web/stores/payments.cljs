(ns oc.web.stores.payments
  (:require [oc.web.dispatcher :as dispatcher]))

(def attempting-checkout-key :checkout-session-id)

(defmethod dispatcher/action :payments
  [db [_ org-slug payments-data]]
  (let [payments-key (dispatcher/payments-key org-slug)]
    (assoc-in db payments-key payments-data)))

(defmethod dispatcher/action :payments-checkout-session-id
  [db [_ session-data]]
  (assoc db :checkout-session-data session-data))