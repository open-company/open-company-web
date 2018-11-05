(ns oc.web.lib.logrocket
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]))

(defn init []
  (js/console.log "DBG logrocket/init")
  (when (and ls/logrocket-key
             (exists? js/LogRocket))
    (.init js/LogRocket ls/logrocket-key
     (clj->js {:release ls/deploy-key}))
    (js/console.log "DBG   initialized")

    (.getSessionURL js/LogRocket
     (fn [session-url]
      (js/console.log "DBG LogRocket.getSessionURL" session-url)
      (when (exists? js/drift)
        (js/console.log "DBG   add drift track" session-url)
        (.track js/drift "LogRocket" #js {"sessionURL" session-url}))

      (when (exists? js/Raven)
       (.setDataCallback js/Raven
        (fn [data]
         (js/console.log "DBG   add Raven extra:" (.-extra data))
         (if (aget data "extra")
           (aset (aget data "extra") "sessionURL" (.-sessionURL js/LogRocket))
           (aset data "extra" #js {:sessionURL (.-sessionURL js/LogRocket)}))
         data)))))))

(defn identify []
  (js/console.log "DBG logrocket/identify")
  (when (and ls/logrocket-key
             (exists? js/LogRocket)
             (jwt/user-id))
    (let [user-data (jwt/get-contents)
          org-data (dis/org-data)]
      (js/console.log "DBG    identifying" user-data org-data)
      (.identify js/LogRocket (:user-id user-data)
       (clj->js {:name (or (:name user-data) (str (:first-name user-data) " " (:last-name user-data)))
                 :email (:email user-data)
                 :org (if org-data (:name org-data) "")})))))