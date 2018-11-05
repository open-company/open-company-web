(ns oc.web.lib.logrocket
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]))

(defn init []
  (when (and ls/logrocket-key
             (exists? js/LogRocket))
    (.init js/LogRocket ls/logrocket-key)

    (.getSessionURL js/LogRocket
     (fn [session-url]

      (when (exists? js/drift)
       (.track js/drift "LogRocket" (clj->js {:sessionURL session-url})))

      (when (exists? js/Raven)
       (.setDataCallback js/Raven
        (fn [data]
         (set! (.-sessionURL (.-extra data)) (.-sessionURL js/LogRocket))
         data)))))))

(defn identify []
  (when (and ls/logrocket-key
             (exists? js/LogRocket))
    (when (jwt/user-id)
      (let [user-data (jwt/get-contents)
            org-data (dis/org-data)]
        (.identify js/LogRocket (:user-id user-data)
         (clj->js {:name (or (:name user-data) (str (:first-name user-data) " " (:last-name user-data)))
                   :email (:email user-data)
                   :org (if org-data (:name org-data) "")}))))))