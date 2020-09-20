(ns oc.web.lib.emoji-autocomplete
  (:require ["@bago2k4/emoji-autocomplete" :as emoji-autocomplete :refer (default) :rename {default emojiAutocomplete}]))

(defn autocomplete [el]
  (emojiAutocomplete el))