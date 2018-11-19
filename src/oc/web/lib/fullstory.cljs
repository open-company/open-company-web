(ns oc.web.lib.fullstory
  (:require [oc.web.lib.jwt :as jwt]))

(defn identify []
  (when (and (exists? js/FS)
             (jwt/user-id))
    (let [user-data (jwt/get-contents)]
      (.identify js/FS (:user-id user-data)
       (clj->js {:displayName (or (:name user-data) (str (:first-name user-data) " " (:last-name user-data)))
                 :email (:email user-data)})))))

(defn track-org [org]
  (when (and (exists? js/FS)
             (map? org))
    (.setUserVars js/FS
     (clj->js {:org (:name org)
               :org-slug (:slug org)}))))