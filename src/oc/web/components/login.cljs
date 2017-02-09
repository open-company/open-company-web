(ns oc.web.components.login
  (:require [rum.core :as rum]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defc login < rum/static
  [{:keys [welcome]}]
  [:div.login.fullscreen-page
    (login-overlays-handler)
    [:div.login-internal
      (login-required {:welcome welcome})]
    (footer (responsive/total-layout-width-int (responsive/calc-card-width) (responsive/columns-num)))])