(ns oc.web.lib.chat
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn check-drift-dialog []
  (let [$drift-el (js/$ "#drift-widget-container iframe")]
    (when (or (not (exists? js/drift))
              (zero? (.-length $drift-el))
              (zero? (.width $drift-el)))
      (let [alert-data {:action "drift-not-working"
                        :title "Support chat not available"
                        :message [:span
                                   "Oh no! It seems our support chat has been blocked. "
                                   "You can whitelist Carrot in your ad-blocker settings, "
                                   "or just send us an email instead at "
                                   [:a {:href "mailto:hello@carrot.io"
                                        :target "_blank"}
                                    "hello@carrot.io"]
                                  "."]
                        :solid-button-style :red
                        :solid-button-title "Ok, got it!"
                        :solid-button-cb #(alert-modal/hide-alert)}]
        (alert-modal/show-alert alert-data)))))

(defn chat-click [interaction]
  (.startInteraction js/drift.api (clj->js {:interactionId interaction}))
  (utils/after 500 check-drift-dialog))

(defn identify []
  (when (and (exists? js/drift)
             (jwt/get-key :email))
    (utils/after 1 #(.identify js/drift (jwt/user-id)
                      (clj->js {:nickname (jwt/get-key :name)
                                :email (jwt/get-key :email)})))))