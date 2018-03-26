(ns oc.web.actions.error-banner
  (:require [oc.web.dispatcher :as dis]))

(defn show-banner [message dismiss-time]
  (dis/dispatch! [:error-banner-show message dismiss-time]))