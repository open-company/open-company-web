(ns oc.web.components.ui.login-button
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defcs login-button < rum/reactive
                          {:will-mount (fn [s]
                                        (when-not (utils/is-test-env?)
                                          (dis/dispatch! [:get-auth-settings]))
                                        s)}
  [s {:keys [button-classes]}]
  [:div.login-button
    [:button
      {:class (str "btn-reset signup-signin " (when button-classes button-classes))
       :on-click #(dis/dispatch! [:show-login-overlay :login-with-slack])}
      "Sign In"]
    [:span.signup-signin " / "]
    [:button
      {:class (str "btn-reset signup-signin " (when button-classes button-classes))
       :on-click #(dis/dispatch! [:show-login-overlay :signup-with-slack])}
      "Sign Up"]])