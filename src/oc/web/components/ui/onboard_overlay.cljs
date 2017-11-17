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
                             (rum/local 1 ::step)
                             (rum/local false ::dismiss)
                             ;; Mixins
                             mixins/no-scroll-mixin
  [s]
  [:div.onboard-overlay-container
    {:class (utils/class-set {:will-appear @(::dismiss s)
                              :appear (not @(::dismiss s))})}
    [:div.onboard-overlay
      (case @(::step s)
        1
        [:div.onboard-overlay-step.step-1
          [:div.step-illustration-container
            ""]
          [:div.onboard-overlay-step-title
            "Welcome to Carrot"]
          ; [:div.onboard-overlay-step-description
          ;   "The big picture keeps everyone aligned."]
            ]
        2
        [:div.onboard-overlay-step.step-2
          [:div.empty-line]
          [:div.onboard-overlay-step-title
            "Keep everyone on the same page"]
          [:div.step-illustration-container
            [:div.step-illustration-left
              "CEO Update"]
            [:div.step-illustration-right
              "Sales Update"]]
          [:div.onboard-overlay-step-description
            "It’s simple to post announcements, updates and stories that bring everyone together."]]
        3
        [:div.onboard-overlay-step.step-3
          [:div.empty-line]
          [:div.onboard-overlay-step-title
            "Encourage feedback"]
          [:div.step-illustration-container
            "2H Strategy"]
          [:div.onboard-overlay-step-description
            "Comments and reactions keep everyone engaged and in sync - great for distributed teams."]]
        4
        [:div.onboard-overlay-step.step-4
          [:div.empty-line]
          [:div.onboard-overlay-step-title
            "Keep stakeholders in the loop, too"]
          [:div.step-illustration-container
            "News"]
          [:div.onboard-overlay-step-description
            "It’s easy to share your news with investors, advisors, recruits and customers."]])
      [:div.onboard-overlay-footer
        [:div.onboard-overlay-footer-left
          [:button.mlb-reset.skip-button
            {:on-click #(do (utils/event-stop %) (close-clicked s))}
            "Skip"]]
        [:div.onboard-overlay-footer-steps
          [:div.dot-step
            {:class (when (= @(::step s) 1) "active")
             :on-click #(reset! (::step s) 1)}]
          [:div.dot-step
            {:class (when (= @(::step s) 2) "active")
             :on-click #(reset! (::step s) 2)}]
          [:div.dot-step
            {:class (when (= @(::step s) 3) "active")
             :on-click #(reset! (::step s) 3)}]
          [:div.dot-step
            {:class (when (= @(::step s) 4) "active")
             :on-click #(reset! (::step s) 4)}]]
        [:div.onboard-overlay-footer-right
          [:button.mlb-reset.mlb-default.next-button
            {:on-click #(if (< @(::step s) 4)
                          (reset! (::step s) (inc @(::step s)))
                          (close-clicked s))}
            (if (= @(::step s) 4)
              "Let's go! "
              "Next ")
            [:img
              {:src (utils/cdn "/img/ML/next_arrow.png")
               :width 20
               :height 20}]]]]]])