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
            [:div.onboard-overlay-step-title
              "Getting started with posts"]
            [:div.step-illustration-container]
            [:div.onboard-overlay-step-description
              (if first-ever-user?
                (str
                 "Updates and interactions are based around posts. Your "
                 "posts keep information tidy, accessible, and open for "
                 "feedback.")
                (str
                 "Updates and interactions are based around posts. "
                 "Posts keep information tidy, accessible, and open for "
                 "feedback."))]
            [:button.mlb-reset.continue-btn
              {:class (when-not first-ever-user? "center-button")
               :on-click #(if first-ever-user?
                           (dis/dispatch! [:first-forced-post-start])
                           (dis/dispatch! [:input [:nux] (if read-only-user :7 :4)]))}
              (if first-ever-user?
                "Create your first post"
                "OK, got it")]])
        :7
        [:div.onboard-overlay-step.step-last
          [:div.onboard-overlay-step-title
            "You’re on your way!"]
          [:div.step-illustration-container]
          [:div.onboard-overlay-step-description
            (str
             "We hope you enjoy Carrot as much as we do. Feel free "
             "to reach out if you have any questions!")]
          [:button.mlb-reset.continue-btn
            {:on-click #(close-clicked s)}
            "Start using Carrot"]])]])