(ns oc.web.components.ui.onboard-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]))

(defn dismiss-modal []
  (dis/dispatch! [:nux-end]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs onboard-overlay < rum/reactive
                             ;; Locals
                             (rum/local false ::dismiss)
                             (drv/drv :board-data)
                             ;; Mixins
                             mixins/no-scroll-mixin
  [s step]
  [:div.onboard-overlay-container
    {:class (utils/class-set {:will-appear @(::dismiss s)
                              :appear (not @(::dismiss s))})}
    [:div.onboard-overlay
      (case step
        :1
        (let [nux-cookie (cook/get-cookie (router/show-nux-cookie (jwt/user-id)))
              first-ever-user? (= nux-cookie (:first-ever-user router/nux-cookie-values))
              board-data (drv/react s :board-data)
              read-only-user (not (utils/link-for (:links board-data) "create"))]
          [:div.onboard-overlay-step.step-1
            [:div.onboard-balloon.red-balloon]
            [:div.onboard-balloon.green-balloon]
            [:div.onboard-balloon.yellow-balloon]
            [:div.onboard-overlay-step-title
              "Let’s get started!"]
            [:div.step-illustration-container-center
              [:div.step-illustration-container]
              [:div.onboard-overlay-step-description
                (str
                 "Keep everyone aligned around "
                 "what matters most.")]]
            [:button.mlb-reset.continue-btn
              {:on-click #(dis/dispatch! [:input [:nux] :2])}
              (if first-ever-user?
                "Create your first post"
                "OK, got it")]])
        :5
        [:div.onboard-overlay-step.step-last
          [:div.onboard-balloon.red-balloon-1]
          [:div.onboard-balloon.green-balloon-1]
          [:div.onboard-balloon.yellow-balloon-1]
          [:div.onboard-balloon.purple-balloon]
          [:div.onboard-overlay-step-title
            "You’re on your way!"]
          [:div.step-illustration-container-center
            [:div.step-illustration-container]
            [:div.onboard-overlay-step-description
              (str
               "Now your team will have a clear view "
               "of what's most important so everyone "
               "stays on the same page!")]]
          [:button.mlb-reset.continue-btn
            {:on-click #(close-clicked s)}
            "Start using Carrot"]])]])