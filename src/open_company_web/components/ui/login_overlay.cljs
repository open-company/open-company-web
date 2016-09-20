(ns open-company-web.components.ui.login-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.login-button :as login]))

(defn close-overlay [e]
  (utils/event-stop e)
  (dis/dispatch! [:show-login-overlay false]))

(rum/defcs login-overlay < rum/reactive 
                           {:will-mount (fn [s] (dommy/add-class! (sel1 [:body]) :no-scroll) s)
                            :will-unmount (fn [s] (dommy/remove-class! (sel1 [:body]) :no-scroll) s)}
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.center
      {:on-click #(utils/event-stop %)}
      [:button.close {:on-click (partial close-overlay)} [:i.fa.fa-times]]
      [:button.btn-reset.mt2.login-button
        {:on-click #(login/login! (:extended-scopes-url (:auth-settings @dis/app-state)) %)}
        [:img {:src "https://api.slack.com/img/sign_in_with_slack.png"}]]
      [:div.login-overlay-footer.p2.mt1.group
        [:a.left {} "DON’T HAVE AN ACCOUNT? SIGN UP NOW"]]]])