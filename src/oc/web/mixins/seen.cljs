(ns oc.web.mixins.seen
  (:require [oc.web.actions.activity :as aa]))

(def mark-container-seen-mixin
  {:did-mount (fn [s]
   ; (when-let [container-id (-> s :rum/args first :container-data :container-id)]
   ;   (aa/send-container-seen container-id))
   s)
   :will-unmount (fn [s]
   (let [container-data (-> s :rum/args first :container-data)]
     (aa/container-nav-away container-data))
   s)})