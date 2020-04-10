(ns oc.web.components.ui.login-button
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.user :as user]
            [oc.web.lib.responsive :as responsive]))

(rum/defc login-button <

  rum/static
  rum/reactive

  {:will-mount (fn [s]
    (js/console.log "DBG will-mount" s)
    (when-not (utils/is-test-env?)
      (user/auth-settings-get))
    s)}

  []
  (let [is-mobile? (responsive/is-mobile-size?)]
    [:div.login-button-container.group
      [:a.login-bt
        {:href oc-urls/login
         :on-click #(do
                     (utils/event-stop %)
                     (user/show-login :login-with-email))}
        "Log in"]
      [:span.or " or "]
      [:a.signup-bt
        {:href oc-urls/sign-up
         :on-click #(do
                     (utils/event-stop %)
                     (router/nav! oc-urls/sign-up))}
        "Sign up"]]))