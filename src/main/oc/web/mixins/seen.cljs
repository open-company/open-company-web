(ns oc.web.mixins.seen
  (:require [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as aa]))

(defn container-nav-mixin []
  (let [container-slug (atom nil)
        sort-type (atom nil)]
    {:did-mount (fn [s]
      (reset! container-slug (if (dis/current-board-slug) (keyword (dis/current-board-slug)) (dis/current-contributions-id)))
      (reset! sort-type (dis/current-sort-type))
      (js/console.log "DBG container-nav-mixin/did-mount" @container-slug @sort-type)
      (aa/container-nav-in @container-slug @sort-type)
     s)
     :did-remount (fn [_ s]
      (js/console.log "DBG container-nav-mixin/did-remount" @container-slug @sort-type)
      (aa/container-nav-in @container-slug @sort-type)
     s)
     :will-unmount (fn [s]
      (js/console.log "DBG container-nav-mixin/will-unmount" @container-slug @sort-type)
      (aa/container-nav-out @container-slug @sort-type)
     s)}))