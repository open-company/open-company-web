(ns oc.web.components.ui.onboard-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]))

(defn dismiss-modal []
  (dis/dispatch! [:onboard-overlay-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs onboard-overlay < ;; Locals
                             (rum/local false ::dismiss)
                             ;; Mixins
                             mixins/no-scroll-mixin
  [s step]
  [:div.onboard-overlay-container
    {:class (utils/class-set {:will-appear @(::dismiss s)
                              :appear (not @(::dismiss s))})}
    [:div.onboard-overlay
      (case step
        :1
        [:div.onboard-overlay-step.step-1
          [:div.onboard-overlay-step-title
            "Getting started with posts"]
          [:div.step-illustration-container]
          [:div.onboard-overlay-step-description
            (str
             "All updates and interactions are based around posts. "
             "Your posts keep information tidy, accessible, and open "
             "for feedback.")]
          [:button.mlb-reset.continue-btn
            {:on-click #(dis/dispatch! [:first-forced-post-start])}
            "Create your first post"]]
        :last
        [:div.onboard-overlay-step.step-last
          [:div.empty-line]
          [:div.onboard-overlay-step-title
            "Keep everyone on the same page"]
          [:div.step-illustration-container
            [:div.step-illustration-left
              "CEO Update"]
            [:div.step-illustration-right
              "Sales Update"]]
          [:div.onboard-overlay-step-description
            "It’s simple to post announcements, updates and stories that bring everyone together."]])]])