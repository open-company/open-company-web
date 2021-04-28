(ns oc.web.stores.routing
  (:require [oc.web.dispatcher :as dispatcher]))

(defn- clean-route [route-map]
  (update route-map :route #(set (map keyword (remove nil? %)))))

(defmethod dispatcher/action :routing
  [db [_ route loading?]]
  (as-> db tdb
    (assoc tdb dispatcher/router-key (clean-route route))
    (cond (true? loading?)
          (assoc tdb :loading true)
          (false? loading?)
          (assoc tdb :loading false)
          :else
          tdb)))

(defmethod dispatcher/action :show-login-wall
  [db [_]]
  (assoc db :force-login-wall true))

(defmethod dispatcher/action :route/rewrite
  [db [_ k v]]
  (assoc-in db [dispatcher/router-key k] v))

(defmethod dispatcher/action :org-nav-out
  [db [_ _from-org _to-org]]
  (-> db
      (assoc :orgs-dropdown-visible false)
      (assoc :mobile-navigation-sidebar false)
      (dissoc (first dispatcher/cmail-state-key))
      (dissoc (first dispatcher/cmail-data-key))
      (dissoc dispatcher/foc-menu-key)
      (dissoc dispatcher/payments-ui-upgraded-banner-key)))