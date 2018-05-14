(ns oc.web.components.section-stream
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.stream-item :refer (stream-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

(rum/defcs section-stream < rum/static
                            rum/reactive
                            ;; Derivatives
                            (drv/drv :section-stream-data)
                            ;; Mixins
                            mixins/first-render-mixin
  [s]
  (let [section-data (drv/react s :section-stream-data)
        items (activity-utils/get-sorted-activities section-data)]
    [:div.section-stream.group
      [:div.section-stream-cards
        [:div.section-stream-cards-inner.group
          (for [e items]
            (rum/with-key (stream-item e) (str "section-stream-item-" (:uuid e))))]
        (when (responsive/is-mobile-size?)
          (all-caught-up))]]))