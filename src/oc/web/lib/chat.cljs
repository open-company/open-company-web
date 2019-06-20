(ns oc.web.lib.chat
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [taoensso.timbre :as timbre]))

;; Need equivalent for Intercom
; (defn check-drift-dialog []
;   (let [$drift-el (js/$ "#drift-widget-container iframe")]
;     (when (or (not (exists? js/drift))
;               (zero? (.-length $drift-el))
;               (zero? (.width $drift-el)))
;       (let [alert-data {:action "drift-not-working"
;                         :title "Support chat not available"
;                         :message [:span
;                                    "Oh no! It seems our support chat has been blocked. "
;                                    "You can whitelist Carrot in your ad-blocker settings, "
;                                    "or just send us an email instead at "
;                                    [:a {:href "mailto:hello@carrot.io"
;                                         :target "_blank"}
;                                     "hello@carrot.io"]
;                                   "."]
;                         :solid-button-style :red
;                         :solid-button-title "OK, got it!"
;                         :solid-button-cb #(alert-modal/hide-alert)}]
;         (alert-modal/show-alert alert-data)))))

(defn chat-click []
  (timbre/info "New Intercom chat")
  (.Intercom js/window "showNewMessage")
  ;(utils/after 500 check-intercom-dialog)
  )

(defn identify []
  (when (jwt/get-key :email)
    (timbre/info "Identify user to Intercom")
    (utils/after 1 #(.Intercom js/window "update"
                        (clj->js {:user_id (jwt/user-id)
                                  :name (jwt/get-key :name)
                                  :email (jwt/get-key :email)})))))