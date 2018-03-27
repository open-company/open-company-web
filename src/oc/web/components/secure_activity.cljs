(ns oc.web.components.secure-activity
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
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
            [oc.web.components.ui.made-with-carrot-modal :as made-with-carrot-modal]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn save-win-height [s]
  (reset! (::win-height s) (.-innerHeight js/window)))

(def default-activity-header-height 69)
(def default-activity-content-height 136)

(rum/defcs secure-activity < ;; Mixins
                             rum/reactive
                             (ui-mixins/render-on-resize save-win-height)
                             ;; Derivatives
                             (drv/drv :secure-activity-data)
                             (drv/drv :made-with-carrot-modal)
                             ;; Locals
                             (rum/local 0 ::win-height)
                             (rum/local nil ::win-resize-listener)

                             {:will-mount (fn [s]
                               (utils/after 100 #(activity-actions/secure-activity-get))
                               (save-win-height s)
                              s)}
  [s]
  (let [activity-data (drv/react s :secure-activity-data)
        activity-author (:publisher activity-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        win-height @(::win-height s)]
    (if activity-data
      [:div.secure-activity-container
        {:style {:min-height (when is-mobile?
                               (str (- win-height default-activity-header-height) "px"))}}
        (when (drv/react s :made-with-carrot-modal)
          (made-with-carrot-modal/made-with-carrot-modal))
        [:div.activity-header.group
          (org-avatar (clojure.set/rename-keys
                       activity-data
                       {:org-logo-height :logo-height
                        :org-logo-width :logo-width
                        :org-logo-url :logo-url
                        :org-name :name
                        :org-slug :slug}))]
        [:div.activity-content-outer
          [:div.activity-content
            {:style {:min-height (when is-mobile?
                                  (str (- win-height default-activity-header-height) "px"))}}
            (when (:headline activity-data)
              [:div.activity-title
                {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}])
            [:div.activity-author.group
              [:div.activity-author-inner.group
                (user-avatar-image activity-author)
                [:div.posted-by
                  (str
                   "Posted by "
                   (:name activity-author)
                   " "
                   (utils/activity-date (utils/js-date (:published-at activity-data)) true) ".")]]]
            (when (:body activity-data)
              [:div.activity-body
                {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))}])]]
        [:div.activity-content-footer
          {:on-click #(when-not is-mobile?
                        (made-with-carrot-modal/show-modal))}
          [:div.activity-content-footer-inner
            [:div.carrot-logo]
            [:div.you-did-it "Made with Carrot"]]]]
      [:div.secure-activity-container
        (loading {:loading true})])))