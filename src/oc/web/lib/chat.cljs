(ns oc.web.lib.chat
  (:require [oc.web.lib.utils :as utils]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn check-drift-dialog []
  (let [$drift-el (js/$ "#drift-widget-container iframe")]
    (when (or (zero? (.-length $drift-el))
              (zero? (.width $drift-el)))
      (let [alert-data {:action "drift-not-working"
                        :title "Chat not available"
                        :message [:span
                                   "If you want to chat with us please disable"
                                   " your ad blocker and reload this page."
                                   "Or send us an email: "
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