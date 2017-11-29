(ns oc.web.components.ui.team-setup
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.loading :refer (rloading)]
            [oc.web.components.ui.onboard-wrapper :refer (onboard-wrapper)]))

(def default-setup-wait-seconds 5)

(rum/defcs team-setup < rum/static
                        rum/reactive
                        (drv/drv :show-setup)
                        (drv/drv :org-redirect)
                        {:did-mount (fn [s]
                          (utils/after
                           (* 1000 default-setup-wait-seconds)
                           #(dis/dispatch! [:org-created-redirect]))
                          s)}
  [s]
  [:div
    (if-not (drv/react s :show-setup)
      (onboard-wrapper :lander-team)
      [:div.setup-screen
        ;; Top left corner
        [:div.balloon.big.big-yellow]
        [:div.balloon.small.small-red]
        [:div.balloon.small.face.small-face-purple]
        ;; Top right corner
        [:div.balloon.big.big-red]
        [:div.balloon.small.small-yellow]
        [:div.balloon.small.face.small-face-yellow]
        ;; Bottom left corner
        [:div.balloon.big.big-green]
        [:div.balloon.small.small-purple]
        ;; Bottom right corner
        [:div.balloon.big.big-purple]
        (rloading {:loading true})
        [:div.setup-cta
          [:div "Hang tight. We’re just getting"]
          [:div "everything setup for you!"]]])])