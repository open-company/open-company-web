(ns oc.web.lib.chat
  (:require [oc.web.lib.utils :as utils]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn check-drift-dialog []
  (let [$drift-el (js/$ "#drift-widget-container iframe")]
    (js/console.log "DBG check-drift-dialog" $drift-el (.width $drift-el))
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
  (utils/after 500 #(check-drift-dialog)))