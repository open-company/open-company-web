(ns oc.web.stores.payments
  (:require [oc.web.dispatcher :as dispatcher]))

(def attempting-checkout-key :checkout-session-id)

(defmethod dispatcher/action :payments
  [db [_ org-slug payments-data]]
  (let [payments-key (dispatcher/payments-key org-slug)]
    (-> db
        (assoc-in payments-key payments-data)
        (dissoc dispatcher/payments-checkout-session-result))))

(defmethod dispatcher/action :payments-create-subscription/finished
  [db [_ org-slug payments-data new-subscription session-data]]
  (let [payments-key (dispatcher/payments-key org-slug)]
    (-> db
        (assoc payments-key payments-data)
        (assoc :checkout-session-data session-data)
        (assoc :new-subscription-data new-subscription))))

(defmethod dispatcher/action :checkout-session-return
  [db [_ success?]]
  (assoc db dispatcher/payments-checkout-session-result success?))