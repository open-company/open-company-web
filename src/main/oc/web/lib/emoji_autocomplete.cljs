(ns oc.web.lib.emoji-autocomplete
  (:require [rum.core :as rum]
            [defun.core :refer (defun)]
            ["@bago2k4/emoji-autocomplete" :as emoji-autocomplete :refer (default) :rename {default emojiAutocomplete}]
            [dommy.core :refer-macros (sel)]))

(defun autocomplete
  ([element :guard (partial instance? js/Node)]
   (emojiAutocomplete element))
  ([xs :guard coll?]
   (for [x xs]
     (autocomplete x)))
  ([xs :guard (partial instance? js/Array)]
   (for [x (js->clj xs)]
     (autocomplete x)))
  ([selector :guard string?]
   (for [el (sel selector)]
     (autocomplete el)))
  ([element :guard (partial instance? js/Node) selector :guard string?]
   (for [el (sel element selector)]
     (autocomplete el)))
  ([element]
   (throw (js/Error. "Emoji autocomplete called on unknown object" (clj->js {:object element :type (type element)})))))

(defun destroy
  ([tcs :guard coll?]
   (for [tc tcs]
     (destroy tc)))
  ([tcs :guard (partial instance? js/Array)]
   (for [tc (js->clj tcs)]
     (destroy tc)))
  ([tc :guard #(.-destroy %)]
   (.-destroy ^js tc))
  ([tc]
   (throw (js/Error. "Emoji autocomplete destroy called on unknown object" (clj->js {:object tc :type (type tc)})))))

(defn autocomplete-mixin [refs]
  {:did-mount (fn [s]
                (let [nodes (for [ref refs]
                              (rum/ref-node s ref))
                      tcs (autocomplete nodes)]
                  (if (seq tcs)
                    (assoc s ::autocomplete-elements tcs)
                    s)))
   :will-unmount (fn [s]
                   (destroy (::autocomplete-elements s))
                   (dissoc s ::autocomplete-elements))})