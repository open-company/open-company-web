(ns oc.web.components.section-stream
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.stream-item :refer (stream-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

(defn- wrt-stream-item-mixin-cb [_ item-uuid]
  (activity-actions/wrt-events-gate item-uuid))

(rum/defcs section-stream < rum/static
                            rum/reactive
                            ;; Derivatives
                            (drv/drv :filtered-posts)
                            (drv/drv :activities-read)
                            (drv/drv :board-data)
                            (drv/drv :org-data)
                            ;; Mixins
                            mixins/first-render-mixin
                            (mixins/wrt-stream-item-mixin "div.wrt-item-ready > div.stream-item-body-inner"
                             wrt-stream-item-mixin-cb)
                            section-mixins/container-nav-in

  [s]
  (let [section-data (drv/react s :filtered-posts)
        items (activity-utils/get-sorted-activities section-data)
        activities-read (drv/react s :activities-read)
        org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)]
    [:div.section-stream.group
      [:div.section-stream-cards
        [:div.section-stream-cards-inner.group
          (for [e items
                :let [reads-data (get activities-read (:uuid e))]]
            (rum/with-key (stream-item e reads-data)
             (str "section-stream-item-" (:uuid e) "-" (:updated-at e))))]
        (when (and (responsive/is-mobile-size?)
                   (not (router/current-activity-id)))
          (all-caught-up))
        (when (and (router/current-activity-id)
                   (jwt/user-is-part-of-the-team (:team-id org-data))
                   (map? board-data)
                   (:entry-count board-data)
                   (> (:entry-count board-data) 1))
          [:div.back-to-board-container
            [:button.mlb-reset.back-to-board
              {:on-click #(router/nav! (oc-urls/board))
               :dangerouslySetInnerHTML #js {:__html
                (str "Show all " (:entry-count board-data) " posts in " (:name board-data))}}]])]]))