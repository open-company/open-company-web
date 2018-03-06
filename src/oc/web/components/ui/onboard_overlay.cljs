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
            [oc.web.actions.activity :as activity-actions]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs onboard-overlay < ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local nil ::resize-listener)
                             (rum/local nil ::wh)
                             ;; Mixins
                             mixins/no-scroll-mixin
                             {:will-mount (fn [s]
                              (reset! (::resize-listener s)
                               (events/listen js/window EventType/RESIZE
                                #(reset! (::wh s) (.-innerHeight js/window))))
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
      (let [create-board-link (utils/link-for (:links (dis/org-data)) "create")
            is-mobile? (responsive/is-tablet-or-mobile?)
            is-safari-mobile (and is-mobile?
                                  (js/isSafari))
            wh @(::wh s)
            top-padding (when-not is-mobile?
                          {:padding-top (str (if (< wh 800) 70 (/ (- wh 536 71 64) 2)) "px")})]
        (case step
          :1
          [:div.onboard-overlay-mobile-step.step-1
            {:style top-padding}
            (when is-mobile?
              [:div.onboard-overlay-step-title
                "Your company digest keeps everyone aligned."])
            [:div.step-illustration-container-center
              (when is-mobile?
                [:div.step-illustration-container
                  {:style {:height (str (min 518 (- wh 138 103)) "px")}}])
              (when-not is-mobile?
                [:div.onboard-overlay-step-title
                  "Your company digest keeps everyone aligned."])
              (when-not is-mobile?
                [:div.onboard-overlay-step-description
                  "Reactions and comments stay together for better context."])]
            [:button.mlb-reset.continue-btn
              {:on-click #(activity-actions/nux-next-step :2)
               :style (when is-mobile? {:bottom (if is-safari-mobile "120px" "78px")})}
              "Next"]
            (when-not is-mobile?
              [:div.step-illustration-container
                {:style {:bottom (str (if (< wh 800) (- wh 800) 0) "px")}}])]
          :2
          [:div.onboard-overlay-mobile-step.step-2
            {:style top-padding}
            (when is-mobile?
              [:div.onboard-overlay-step-title
                "Information stays organized so it’s easy to find."])
            [:div.step-illustration-container-center
              (when is-mobile?
                [:div.step-illustration-container
                  {:style (when is-mobile?
                            (let [max-h 342
                                  height (- wh 38 263)
                                  next-height (min 442 height)
                                  margin-top (if (>= height max-h)
                                               (+ (/ (- height max-h) 2) 32)
                                               32)]
                              {:height (str next-height "px")
                               :margin-top (if is-mobile? (str margin-top "px") "0px")}))}])
              (when-not is-mobile?
                [:div.onboard-overlay-step-title
                  "Information stays organized so it’s easy to find."])
              (when-not is-mobile?
                [:div.onboard-overlay-step-description
                  "Get caught up anytime - great for distributed teams."])]
            [:button.mlb-reset.continue-btn
              {:on-click #(activity-actions/nux-end)
               :style (when is-mobile? {:bottom (if is-safari-mobile "120px" "78px")})}
              "Start using Carrot"]
            (when-not is-mobile?
              [:div.step-illustration-container
                {:style {:bottom (str (if (< wh 800) (- wh 800) 0) "px")}}])]))]])