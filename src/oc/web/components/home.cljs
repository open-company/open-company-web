(ns oc.web.components.home
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.home-page :refer (home-page)]))

(rum/defcs home
           < (drv/drv :loading)
  [s]
  (if (jwt/jwt)
    [:div.home.fullscreen-page
      (when-not (drv/react s :loading)
        [:div.home-internal]
        (footer))]
    (home-page)))