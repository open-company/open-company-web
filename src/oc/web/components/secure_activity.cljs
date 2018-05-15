(ns oc.web.components.secure-activity
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]
            [oc.web.components.ui.made-with-carrot-modal :as made-with-carrot-modal]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn save-win-height [s]
  (reset! (::win-height s) (.-innerHeight js/window)))

(def default-activity-header-height 56)

(rum/defcs secure-activity < rum/reactive
                             ;; Derivatives
                             (drv/drv :secure-activity-data)
                             (drv/drv :made-with-carrot-modal)
                             ;; Locals
                             (rum/local 0 ::win-height)
                             (rum/local nil ::win-resize-listener)
                             (rum/local true ::show-login-header)
                             ;; Mixins
                             (ui-mixins/render-on-resize save-win-height)

                             {:will-mount (fn [s]
                               (utils/after 100 #(activity-actions/secure-activity-get))
                               (save-win-height s)
                               (reset! (::show-login-header s) (not (jwt/jwt)))
                              s)}
  [s]
  (let [activity-data (drv/react s :secure-activity-data)
        activity-author (:publisher activity-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        win-height @(::win-height s)]
    [:div.secure-activity-container
      {:style {:min-height (when is-mobile?
                             (str (- win-height default-activity-header-height) "px"))}}
      (when (drv/react s :made-with-carrot-modal)
        (made-with-carrot-modal/made-with-carrot-modal))
      (when @(::show-login-header s)
        [:div.secure-activity-login-header
          [:button.mlb-reset.remove-header-bt
            {:on-click #(reset! (::show-login-header s) false)}]
          [:div.secure-activity-login-header-center
            [:div.carrot-logo-copy]
            [:div.separator]
            [:div.signup-login-copy
              "Want to create your own company digest? "
              [:a "Sign up"]
              (str ". Are you part of the " (or (:org-name activity-data) "") " team? ")
              [:a "Sign in"]
              "."]]])
      (when activity-data
        [:div.activity-header.group
          {:class (when @(::show-login-header s) "showing-login-header")}
          [:div.activity-header-title.fs-hide
            {:dangerouslySetInnerHTML #js {:__html (:headline activity-data)}}]])
      (when activity-data
        [:div.activity-content-outer
          {:class (when @(::show-login-header s) "showing-login-header")}
          [:div.activity-content-header
            [:div.activity-content-header-author
              (user-avatar-image (:publisher activity-data))
              [:div.name.fs-hide
                (:name (:publisher activity-data))]
              [:div.time-since
                (let [t (or (:published-at activity-data) (:created-at activity-data))]
                  [:time
                    {:date-time t
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip activity-data)}
                    (utils/time-since t)])]]
            [:div.activity-content-header-right
              [:div.section-tag
                {:class (when (:new activity-data) "has-new")
                 :dangerouslySetInnerHTML (utils/emojify (:board-name activity-data))}]]]
          [:div.activity-content
            {:style {:min-height (when is-mobile?
                                  (str (- win-height default-activity-header-height) "px"))}}
            (when (:headline activity-data)
              [:div.activity-title.fs-hide
                {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}])
            (when (:body activity-data)
              [:div.activity-body.fs-hide
                {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))}])
            (stream-attachments (:attachments activity-data))]])
      (when-not activity-data
        [:div.secure-activity-container
          (loading {:loading true})])
      [:div.activity-content-footer
        {:on-click #(when-not is-mobile?
                      (made-with-carrot-modal/show-modal))}
        [:div.activity-content-footer-inner
          [:div.carrot-logo]
          [:div.you-did-it "Made with Carrot"]]]]))