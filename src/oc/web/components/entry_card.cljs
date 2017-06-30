(ns oc.web.components.entry-card
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (interactions-summary)]))

(defn cut-body [entry-body]
  (.truncate js/$ entry-body (clj->js {:length utils/topic-body-limit :words true})))

(rum/defcs entry-card < rum/static
  [s entry-data]
  [:div.entry-card
    ; Card header
    [:div.entry-card-head.group
      ; Card author
      [:div.entry-card-head-author
        (user-avatar-image)
        [:div.name (:name (first (:author entry-data)))]
        [:div.time-since
          [:time
            {:date-time (:updated-at entry-data)}
            (utils/time-since (:updated-at entry-data))]]]
      ; Card labels
      [:div.entry-card-head-right
        (when true
          [:div.new "NEW"])]]
    [:div.entry-card-content.group
      [:div.entry-card-headline
        (:headline entry-data)]
      [:div.entry-card-body
        {:dangerouslySetInnerHTML #js {:__html (cut-body (:body entry-data))}}]]
    [:div.entry-card-footer.group
      (interactions-summary entry-data)
      [:div.more-button
        [:button.mlb-reset.more-ellipsis
          {:title "More"
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"}]]]])