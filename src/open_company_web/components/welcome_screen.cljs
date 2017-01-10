(ns open-company-web.components.welcome-screen
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.dispatcher :as dis]))

(rum/defc welcome-screen < rum/static
  []
  [:div.welcome-screen
    [:div.welcome-screen-box
      [:span "This is your dashboard, the place for key information you want everyone to know."]
      [:button.choose-first-topic-button.btn-reset.btn-solid
        {:on-click #(dis/dispatch! [:hide-welcome-screen])}
        [:label.pointer.mt1 "Choose Your First Topic"]]]])