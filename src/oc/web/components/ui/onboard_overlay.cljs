(ns oc.web.components.ui.onboard-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn dismiss-modal []
  (dis/dispatch! [:nux-end]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs onboard-overlay < ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local nil ::resize-listener)
                             (rum/local nil ::wh)
                             ;; Derivatives
                             rum/reactive
                             (drv/drv :board-data)
                             ;; Mixins
                             mixins/no-scroll-mixin
                             {:will-mount (fn [s]
                              (when (responsive/is-tablet-or-mobile?)
                                (reset! (::resize-listener s)
                                 (events/listen js/window EventType/RESIZE
                                  #(reset! (::wh s) (.-innerHeight js/window)))))
                              (reset! (::wh s) (.-innerHeight js/window))
                              s)
                              :will-unmount (fn [s]
                               (when @(::resize-listener s)
                                 (events/unlistenByKey @(::resize-listener s))
                                 (reset! (::resize-listener s) nil))
                               s)}
  [s step]
  [:div.onboard-overlay-container
    {:class (utils/class-set {:will-appear @(::dismiss s)
                              :appear (not @(::dismiss s))})}
    [:div.onboard-overlay
      (let [is-safari-mobile (and (responsive/is-tablet-or-mobile?)
                                  (js/isSafari))
            wh @(::wh s)]
        (case step
          :1-mobile
          [:div.onboard-overlay-mobile-step.step-1
            [:div.onboard-overlay-step-title
              "Rise above the noise"]
            [:div.step-illustration-container-center
              [:div.step-illustration-container
                {:style {:height (str (min 418 (- wh 138 163)) "px")}}]
              [:div.onboard-overlay-step-description
                (str
                 "Share key updates and stories "
                 "on what matters most.")]]
            [:button.mlb-reset.continue-btn
              {:on-click #(dis/dispatch! [:input [:nux] :2-mobile])
               :style {:bottom (if is-safari-mobile "120px" "78px")}}
              "Next"]
            [:div.steps-dot-container
              {:style {:bottom (if is-safari-mobile "90px" "48px")}}
              [:div.step-dot.active]
              [:div.step-dot]
              [:div.step-dot]]]
          :2-mobile
          [:div.onboard-overlay-mobile-step.step-2
            [:div.onboard-overlay-step-title
              "Engage your team with focused discussions"]
            [:div.step-illustration-container-center
              (let [max-h 342
                    height (- wh 138 183)
                    next-height (min 342 height)
                    margin-top (if (>= height max-h)
                                 (+ (/ (- height max-h) 2) 32)
                                 32)]
                [:div.step-illustration-container
                  {:style {:height (str next-height "px")
                           :margin-top (str margin-top "px")}}])
              [:div.onboard-overlay-step-description
                (str
                 "Reactions, comments and questions "
                 "stay together.")]]
            [:button.mlb-reset.continue-btn
              {:on-click #(dis/dispatch! [:input [:nux] :3-mobile])
               :style {:bottom (if is-safari-mobile "120px" "78px")}}
              "Next"]
            [:div.steps-dot-container
              {:style {:bottom (if is-safari-mobile "90px" "48px")}}
              [:div.step-dot]
              [:div.step-dot.active]
              [:div.step-dot]]]
          :3-mobile
          [:div.onboard-overlay-mobile-step.step-3
            [:div.onboard-overlay-step-title
              "Keep everyone aligned"]
            [:div.step-illustration-container-center
              (let [margin-top (/ (- wh 115 203 201) 2)]
                [:div.step-illustration-container
                  {:style {:margin-top (str margin-top "px")}}])
              [:div.onboard-overlay-step-description
                (str
                 "Teams stay in sync when the "
                 "full picture is in one place.")]]
            [:button.mlb-reset.continue-btn
              {:on-click #(dis/dispatch! [:nux-end])
               :style {:bottom (if is-safari-mobile "120px" "78px")}}
              "Start using Carrot"]
            [:div.steps-dot-container
              {:style {:bottom (if is-safari-mobile "90px" "48px")}}
              [:div.step-dot]
              [:div.step-dot]
              [:div.step-dot.active]]]
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
              "Start using Carrot"]]))]])