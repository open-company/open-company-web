(ns oc.web.stores.payments
  (:require [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :payments
  [db [_ org-slug payments-data]]
  (let [payments-key (dispatcher/payments-key org-slug)]
    (assoc-in db payments-key payments-data)))