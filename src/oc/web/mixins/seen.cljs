(ns oc.web.mixins.seen
  (:require [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as aa]))

(defn container-nav-mixin []
  (let [container-slug (atom nil)]
    {:did-mount (fn [s]
      (reset! container-slug (router/current-board-slug))
      (aa/container-nav-in @container-slug)
     s)
     :did-remount (fn [_ s]
      (aa/container-nav-in @container-slug)
     s)
     :will-unmount (fn [s]
      (aa/container-nav-out @container-slug)
     s)}))