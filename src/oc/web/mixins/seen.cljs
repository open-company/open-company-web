(ns oc.web.mixins.seen
  (:require [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as aa]))

(defn container-nav-mixin []
  (let [container-slug (atom nil)
        sort-type (atom nil)]
    {:did-mount (fn [s]
      (reset! container-slug (if (router/current-board-slug) (keyword (router/current-board-slug)) (router/current-contributions-id)))
      (reset! sort-type (router/current-sort-type))
      (aa/container-nav-in @container-slug @sort-type)
     s)
     :did-remount (fn [_ s]
      (aa/container-nav-in @container-slug @sort-type)
     s)
     :will-unmount (fn [s]
      (aa/container-nav-out @container-slug @sort-type)
     s)}))