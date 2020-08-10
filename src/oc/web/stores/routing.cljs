(ns oc.web.stores.routing
  (:require [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]))

(defn- clean-route [route-map]
  (update route-map :route #(set (map keyword (remove nil? %)))))

(defmethod dispatcher/action :routing
  [db [_ route]]
  (assoc db dispatcher/router-key (clean-route route)))

(defmethod dispatcher/action :show-login-wall
  [db [_ route]]
  (assoc db :force-login-wall true))

(defmethod dispatcher/action :route/rewrite
  [db [_ k v]]
  (assoc-in db [dispatcher/router-key k] v))

(defmethod dispatcher/action :org-nav-out
  [db [_ from-org to-org]]
  (-> db
   (assoc :orgs-dropdown-visible false)
   (assoc :mobile-navigation-sidebar false)
   (dissoc (first dispatcher/cmail-state-key))
   (dissoc (first dispatcher/cmail-data-key))))