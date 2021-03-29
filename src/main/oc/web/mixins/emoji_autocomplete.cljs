(ns oc.web.mixins.emoji-autocomplete
  (:require [rum.core :as rum]
            [defun.core :refer (defun)]
            ["@bago2k4/emoji-autocomplete" :as emoji-autocomplete :refer (default) :rename {default emojiAutocomplete}]
            [dommy.core :refer-macros (sel)]))

(def ^{:export true} counter (atom 0))

(defun autocomplete
  ([element :guard (partial instance? js/Node)]
   (swap! counter inc)
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
  ([nil]
   (js/console.warn "Emoji autocomplete not initialized, called with undefined element or selector.")
   nil)
  ([element]
   (throw (js/Error. (str "Emoji autocomplete called on unknown object. Type" (type element))))))

(defun destroy
  ([tcs :guard coll?]
   (js/console.log "DBG destroy" tcs)
   (for [tc tcs]
     (destroy tc)))
  ([tcs :guard (partial instance? js/Array)]
   (js/console.log "DBG destroy" tcs)
   (for [tc (js->clj tcs)]
     (destroy tc)))
  ([tc :guard #(.-destroy %)]
   (js/console.log "DBG destroy" tc)
   (.destroy ^js tc)
   (swap! counter dec))
  ([nil]
   (js/console.warn "Emoji autocomplete not destroyed, passed TextComplete object is null.")
   nil)
  ([tc]
   (throw (js/Error. (str "Emoji autocomplete destroy called on unknown object. Type:" (type tc))))))

(defn autocomplete-mixin [refs]
  {:did-mount (fn [s]
                (js/console.log "DBG autocomplete-mixin/did-mount creating")
                (js/console.log "DBG    refs" refs)
                (let [refs* (if (sequential? refs)
                              refs
                              [refs])
                      _ (js/console.log "DBG    refs*" refs*)
                      nodes (for [ref refs*]
                              (rum/ref-node s ref))
                      _ (js/console.log "DBG   nodes" nodes)
                      tcs (autocomplete nodes)]
                  (js/console.log "DBG     components" tcs)
                  (if (seq tcs)
                    (assoc s ::autocomplete-elements tcs)
                    s)))
   :will-unmount (fn [s]
                   (js/console.log "DBG autocomplete-mixin/will-unmount destroying" (::autocomplete-elements s))
                   (destroy (::autocomplete-elements s))
                   (dissoc s ::autocomplete-elements))})