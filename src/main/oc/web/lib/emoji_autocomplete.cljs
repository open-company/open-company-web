(ns oc.web.lib.emoji-autocomplete
  (:require ["@bago2k4/emoji-autocomplete" :as emoji-autocomplete :refer (default) :rename {default emojiAutocomplete}]
            [dommy.core :refer-macros (sel)]))

(defn autocomplete
  ([element-or-selector]
   (emojiAutocomplete element-or-selector))
  ([element selector]
   (doseq [el (sel element selector)]
     (emojiAutocomplete el))))