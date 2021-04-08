(ns oc.web.lib.emoji-autocomplete
  (:require [rum.core :as rum]
            [defun.core :refer (defun)]
            ["@bago2k4/emoji-autocomplete" :as emoji-autocomplete :refer (setupAutocomplete destroyAutocomplete)]
            [dommy.core :refer-macros (sel)]))


(defun init!
  ([element :guard (partial instance? js/Node)]
   (setupAutocomplete element))
  ([xs :guard coll?]
   (for [x xs]
     (init! x)))
  ([xs :guard (partial instance? js/Array)]
   (for [x (js->clj xs)]
     (init! x)))
  ([selector :guard string?]
   (for [el (sel selector)]
     (init! el)))
  ([element :guard (partial instance? js/Node) selector :guard string?]
   (for [el (sel element selector)]
     (init! el)))
  ([nil]
   (js/console.warn "Emoji autocomplete not initialized, called with undefined element or selector.")
   nil)
  ([element]
   (throw (js/Error. (str "Emoji autocomplete called on unknown object. Type" (type element))))))

(defun destroy!
  ([tcs :guard coll?]
   (for [tc tcs]
     (destroy! tc)))
  ([tcs :guard (partial instance? js/Array)]
   (for [tc (js->clj tcs)]
     (destroy! tc)))
  ([tc :guard #(.-destroy %)]
   (destroyAutocomplete tc))
  ([nil]
   (js/console.warn "Emoji autocomplete not destroyed, passed TextComplete object is null.")
   nil)
  ([tc]
   (throw (js/Error. (str "Emoji autocomplete destroy called on unknown object. Type:" (type tc))))))

(defn autocomplete-mixin [refs]
  {:did-mount (fn [s]
                (let [refs* (if (sequential? refs)
                              refs
                              [refs])
                      nodes (for [ref refs*]
                              (rum/ref-node s ref))
                      tcs (init! nodes)]
                  (if (seq tcs)
                    (assoc s ::autocomplete-elements tcs)
                    s)))
   :will-unmount (fn [s]
                   (destroy! (::autocomplete-elements s))
                   (dissoc s ::autocomplete-elements))})