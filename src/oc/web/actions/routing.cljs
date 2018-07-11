(ns oc.web.actions.routing
  (:require [oc.web.dispatcher :as dis]))

(defn routing [route-path]
  (dis/dispatch! [:routing route-path])
  (dis/dispatch! [:container/status (dis/change-cache-data) true]))