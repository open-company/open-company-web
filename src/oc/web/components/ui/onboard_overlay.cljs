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

(rum/defcs onboard-overlay < ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local nil ::resize-listener)
                             (rum/local nil ::wh)
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
      (let [create-board-link (utils/link-for (:links (dis/org-data)) "create")
            is-mobile? (responsive/is-tablet-or-mobile?)
            is-safari-mobile (and is-mobile?
                                  (js/isSafari))
            wh @(::wh s)]
        (case step
          :1
          [:div.onboard-overlay-mobile-step.step-1
            (when is-mobile?
              [:div.onboard-overlay-step-title
                "Above the noise"])
            [:div.step-illustration-container-center
              (when is-mobile?
                [:div.step-illustration-container
                  {:style {:height (str (min 418 (- wh 138 203)) "px")}}])
              (when-not is-mobile?
                [:div.onboard-overlay-step-title
                  "Above the noise"])
              [:div.onboard-overlay-step-description
                (str
                 "Share key updates and stories "
                 "that won’t be missed.")]]
            [:button.mlb-reset.continue-btn
              {:on-click #(dis/dispatch! [:input [:nux] :2])
               :style (when is-mobile? {:bottom (if is-safari-mobile "120px" "78px")})}
              "Next"]
            (when-not is-mobile?
              [:div.step-illustration-container])]
          :2
          [:div.onboard-overlay-mobile-step.step-2
            (when is-mobile?
              [:div.onboard-overlay-step-title
                "Focused conversations"])
            [:div.step-illustration-container-center
              (when is-mobile?
                [:div.step-illustration-container
                  {:style (when is-mobile?
                            (let [max-h 342
                                  height (- wh 138 263)
                                  next-height (min 342 height)
                                  margin-top (if (>= height max-h)
                                               (+ (/ (- height max-h) 2) 32)
                                               32)]
                              {:height (str next-height "px")
                               :margin-top (if is-mobile? (str margin-top "px") "0px")}))}])
              (when-not is-mobile?
                [:div.onboard-overlay-step-title
                  "Focused conversations"])
              [:div.onboard-overlay-step-description
                (str
                 "Team reactions and comments "
                 "stay together for context.")]]
            [:button.mlb-reset.continue-btn
              {:on-click #(dis/dispatch! [:nux-end])
               :style (when is-mobile? {:bottom (if is-safari-mobile "120px" "78px")})}
              "Next"]
            (when-not is-mobile?
              [:div.step-illustration-container])]))]])