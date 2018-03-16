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

(defn save-win-height
  "Save the window height in the local component state."
  [s]
  (reset! (::wh s) (.-innerHeight js/window)))

(rum/defcs onboard-overlay < ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local nil ::wh)
                             ;; Mixins
                             mixins/no-scroll-mixin
                             (mixins/render-on-resize save-win-height)
                             {:will-mount (fn [s]
                              (save-win-height s)
                              s)}
  [s step]
  [:div.onboard-overlay-container
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
          [:div.onboard-overlay-step.step-1
            {:style top-padding}
            [:div.step-illustration-container-center
              [:div.onboard-overlay-step-title
                "Your company digest keeps everyone aligned."]
              [:div.onboard-overlay-step-description
                "Reactions and comments stay together for better context."]]
            [:button.mlb-reset.continue-btn
              {:on-click #(if is-mobile?
                            (activity-actions/nux-end)
                            (activity-actions/nux-next-step :2))
               :style (when is-mobile? {:bottom (if is-safari-mobile "120px" "78px")})}
              "Next"]
            [:div.step-illustration-container
              {:style {:bottom (str (if (< wh 800) (- wh 800) 0) "px")}}]]
          :2
          [:div.onboard-overlay-step.step-2
            {:style top-padding}
            (when is-mobile?
              [:div.onboard-overlay-step-title
                "Welcome to Carrot!"])
            [:div.step-illustration-container-center
              {:style (when is-mobile?
                        (let [height (- wh 220 (when is-safari-mobile -60))
                              padding-top (/ (- height 231 78 20) 2)]
                          {:height (str height "px")
                           :padding-top (str padding-top "px")}))}
              (when is-mobile?
                [:div.step-illustration-container])
              (if is-mobile?
                [:div.onboard-overlay-step-description
                  "Your company digest keeps everyone aligned around what matters most."]
                [:div.onboard-overlay-step-title
                  "Information stays organized so it’s easy to find."])]
            [:button.mlb-reset.continue-btn
              {:on-click #(activity-actions/nux-end)
               :style (when is-mobile? {:bottom (if is-safari-mobile "85px" "78px")})}
              "Start using Carrot"]
            (when-not is-mobile?
              [:div.step-illustration-container
                {:style {:bottom (str (if (< wh 800) (- wh 800) 0) "px")}}])]))]])