(ns oc.web.lib.emoji-autocomplete
  (:require ["@bago2k4/emoji-autocomplete" :as emoji-autocomplete :refer (emojiAutocomplete)]))

(defn autocomplete []
  (try
    (emojiAutocomplete ".emoji-autocomplete")
    (catch :default e false)))