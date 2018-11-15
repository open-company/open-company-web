(ns oc.web.mixins.section
  (:require [oc.web.ws.change-client :as ws-cc]))

(def container-nav-in
  {:did-mount (fn [s]
   (ws-cc/container-watch)
   s)})