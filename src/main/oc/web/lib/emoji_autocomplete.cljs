(ns oc.web.lib.emoji-autocomplete
  (:require ["emoji-mart" :as emoji-mart :refer (emojiIndex)]))

(defn autocomplete []
  (try
    (js/emojiAutocomplete emojiIndex)
    (catch :default e false)))