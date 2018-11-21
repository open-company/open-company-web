(ns oc.web.lib.fullstory
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.stores.user :as user-store]))

(defn identify []
  (when (and (exists? js/FS)
             (jwt/user-id))
    (let [user-data (jwt/get-contents)]
      (.identify js/FS (:user-id user-data)
       (clj->js {:displayName (or (:name user-data) (str (:first-name user-data) " " (:last-name user-data)))
                 :email (:email user-data)})))))

(defn track-org [org-data]
  (when (and (exists? js/FS)
             (map? org-data))
    (.setUserVars js/FS
     (clj->js {:org (:name org-data)
               :org_slug (:slug org-data)
               :role (user-store/user-role org-data (dis/current-user-data))}))))

(defn session-url []
  (when (and (exists? js/FS)
             (.-getCurrentSessionURL js/FS))
    (.getCurrentSessionURL js/FS)))