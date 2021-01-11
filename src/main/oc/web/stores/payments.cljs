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

(defmethod dispatcher/action :notify-cache/store
  [db [_ org-slug cache-data]]
  (assoc-in db (dispatcher/payments-notify-cache-key org-slug) cache-data))

(defmethod dispatcher/action :notify-cache/reset
  [db [_ org-slug]]
  (let [cache-key (dispatcher/payments-notify-cache-key org-slug)]
    (update-in db (butlast cache-key) dissoc (last cache-key))))

(defmethod dispatcher/action :toggle-premium-picker
  [db [_]]
  (update db dispatcher/premium-picker-modal not))

(defmethod dispatcher/action :hide-premium-picker
  [db [_]]
  (assoc db dispatcher/premium-picker-modal false))

(defmethod dispatcher/action :show-premium-picker
  [db [_]]
  (assoc db dispatcher/premium-picker-modal true))