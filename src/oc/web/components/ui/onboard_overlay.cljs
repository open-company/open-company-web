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
            [oc.web.actions.nux :as nux-actions]
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
  [s]
  [:div.onboard-overlay-container
    [:div.onboard-overlay
      (let [create-board-link (utils/link-for (:links (dis/org-data)) "create")
            is-mobile? (responsive/is-tablet-or-mobile?)
            is-safari-mobile (and is-mobile?
                                  (js/isSafari))
            wh @(::wh s)]
        [:div.onboard-overlay-step
          (when-not is-mobile?
            [:div.onboard-overlay-carrot-icon])
          [:div.onboard-overlay-step-title
            (if is-mobile?
              "Welcome to Carrot!"
              "You're ready!")]
          (when is-mobile?
            [:div.step-illustration-container])
          [:div.step-illustration-container-center
            {:style (when is-mobile?
                      (let [height (- wh 220 (when is-safari-mobile -60))
                            padding-top (/ (- height 231 78 30) 2)]
                        {:height (str height "px")
                         :padding-top (str padding-top "px")}))}
            "Your company digest keeps everyone aligned around what matters most."]
          [:button.mlb-reset.continue-btn
            {:on-click #(nux-actions/dismiss-onboard-overlay)
             :style (when is-mobile? {:bottom (if is-safari-mobile "85px" "78px")})}
            "Start using Carrot"]])]])