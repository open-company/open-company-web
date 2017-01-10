(ns open-company-web.components.welcome-screen
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]))

(rum/defc welcome-screen < rum/static
  [choose-topic-cb]
  [:div.welcome-screen
    [:div.welcome-screen-box
      [:span "This is your dashboard, the place for key information you want everyone to know."]
      [:button.choose-first-topic-button.btn-reset.btn-solid
        {:on-click choose-topic-cb}
        [:label.pointer.mt1 "Choose Your First Topic"]]]])