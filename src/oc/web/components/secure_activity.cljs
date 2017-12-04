(ns oc.web.components.secure-activity
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.made-with-carrot-modal :refer (made-with-carrot-modal)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs secure-activity < rum/reactive
                          (drv/drv :secure-activity-data)
                          (drv/drv :made-with-carrot-modal)
                          {:will-mount (fn [s]
                                         (utils/after 100 #(dis/dispatch! [:secure-activity-get]))
                                         s)}
  [s]
  (let [activity-data (drv/react s :secure-activity-data)
        activity-author (:publisher activity-data)
        ww (min (responsive/ww) 840)]
    (if activity-data
      [:div.secure-activity-container
        (when (drv/react s :made-with-carrot-modal)
          (made-with-carrot-modal))
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
          {:on-click #(dis/dispatch! [:made-with-carrot-modal-show])}
          [:div.activity-content-footer-inner
            [:div.carrot-logo]
            [:div.you-did-it "Made with Carrot"]]]]
      [:div.secure-activity-container
        (loading {:loading true})])))