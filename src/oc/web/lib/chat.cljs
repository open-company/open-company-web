(ns oc.web.lib.chat)

(defn chat-click [interaction]
  (.startInteraction js/drift.api (clj->js {:interactionId interaction})))
