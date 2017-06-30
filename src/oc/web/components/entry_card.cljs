(ns oc.web.components.entry-card
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (interactions-summary)]))

(defn cut-body [entry-body]
  (.truncate js/$ entry-body (clj->js {:length utils/topic-body-limit :words true})))

(rum/defc entry-card-empty
  []
  [:div.entry-card.empty-state.group
    [:div.entry-card-title
      "This topic’s a little sparse. "
      [:button.mlb-reset
        {:on-click #(js/alert "Coming soon")}
        "Add an update?"]]])

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
            (utils/time-since (:updated-at entry-data))]
          (when (or (:topic-name entry-data) (:topic-slug entry-data))
            (str " · " (or (:topic-name entry-data) (s/capital (:topic-slug entry-data)))))]]
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