(ns oc.web.actions.ui-theme
  (:require [oc.web.actions.theme :as theme-actions]))

(defn ^:export set-ui-theme [v]
  (theme-actions/desktop-theme-changed))