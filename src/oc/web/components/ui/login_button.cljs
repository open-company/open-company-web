(ns oc.web.components.ui.login-button
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.actions.user :as user]
            [oc.web.lib.utils :as utils]))

(rum/defcs login-button < rum/static
                          rum/reactive
                          {:will-mount (fn [s]
                                        (when-not (utils/is-test-env?)
                                          (user/auth-settings-get))
                                        s)}
  [s {:keys [button-classes]}]
  [:div.login-button
    [:a.signup-signin
      {:class (when button-classes button-classes)
       :href oc-urls/sign-up
       :on-click (fn [e]
                   (utils/event-stop e)
                   (router/nav! oc-urls/sign-up))
       :on-touch-start identity}
      "Get started"]
    [:span.signup-signin]
    [:a.signup-signin
      {:class (when button-classes button-classes)
       :href oc-urls/login
       :on-click (fn [e]
                   (utils/event-stop e)
                   (router/nav! oc-urls/login))
       :on-touch-start identity}
      "Login"]])