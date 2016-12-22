(ns open-company-web.components.welcome-screen
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]))

(rum/defc welcome-screen < rum/static
  [choose-topic-cb]
  [:div.welcome-screen
    [:div.welcome-screen-box
      [:span (str "Welcome to your dashboard!")]
      [:span "Your dashboard is the place for key information you want everyone to know."]
      [:span "At any time you can share selected topics into company updates for employees, investors or customers."]
      [:button.choose-first-topic-button.btn-reset.btn-solid
        {:on-click choose-topic-cb}
        "Choose Your First Topic"]]])