(ns open-company-web.components.welcome-screen
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]))

(rum/defc welcome-screen < rum/static
  [choose-topic-cb]
  [:div.welcome-screen
    [:div.welcome-screen-box
      [:span (str "Hi" (when (jwt/jwt) (str " " (jwt/get-key :name))) "! Welcome to OpenCompany!")]
      [:span "(Copy here)"]
      [:button.choose-first-topic-button.btn-reset.btn-solid
        {:on-click choose-topic-cb}
        "Choose Your First Topic"]]])