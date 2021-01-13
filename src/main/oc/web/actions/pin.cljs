(ns oc.web.actions.pin
  (:require [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.activity :as activity-actions]))
;; Pins

(defn toggle-pin [entry-data pin-link refresh-feed-cb]
  (api/toggle-pin pin-link
                  (fn [resp]
                    (cmail-actions/get-entry-finished (dis/current-org-slug) (:uuid entry-data) resp)
                    (refresh-feed-cb))))

(defn toggle-home-pin! [entry-data pin-link]
  (dis/dispatch! [:toggle-home-pin! (dis/current-org-slug) entry-data ls/seen-home-container-id])
  (toggle-pin entry-data pin-link activity-actions/following-get))

(defn toggle-board-pin! [entry-data pin-link]
  (dis/dispatch! [:toggle-board-pin! (dis/current-org-slug) entry-data (:board-uuid entry-data)])
  (toggle-pin entry-data pin-link #(section-actions/section-get
                                    (:board-slug entry-data)
                                    (utils/link-for (:links entry-data) "up"))))