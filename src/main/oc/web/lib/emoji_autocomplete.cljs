(ns oc.web.lib.emoji-autocomplete
  (:require ["emoji-mart" :as emoji-mart]))

(defn autocomplete []
  (try
    (js/emojiAutocomplete emoji-mart/emojiIndex)
    (catch :default e false)))