(ns oc.web.components.explore-view
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.follow-button :refer (follow-button)]))

(defn- filter-item [s item]
  (not= (:slug item) utils/default-drafts-board-slug))

(defn- filter-sort-items [s items]
  (->> items
   (filterv #(filter-item s %))
   (sort-by :short-name)))

(rum/defcs explore-view <
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :follow-boards-list)
  (drv/drv :followers-boards-count)
  [s]
  (let [org-data (drv/react s :org-data)
        follow-boards-list (map :uuid (drv/react s :follow-boards-list))
        followers-boards-count (drv/react s :followers-boards-count)
        with-follow (map #(assoc % :follow (utils/in? follow-boards-list (:uuid %))) (:boards org-data))
        sorted-items (filter-sort-items s with-follow)
        is-mobile? (responsive/is-mobile-size?)
        can-create-topic? (utils/link-for (:links org-data) "create" "POST")]
    [:div.explore-view
      [:div.explore-view-header
        "Explore"]
      [:div.explore-view-blocks
        (when can-create-topic?
          [:button.mlb-reset.explore-view-block.create-topic-bt
            {:on-click #(nav-actions/show-section-add)}
            [:span.plus]
            [:span.new-topic "New topic"]])
        (for [item sorted-items
              :let [followers-count-data (get followers-boards-count (:uuid item))
                    followers-count (:count followers-count-data)]]
          [:a.explore-view-block.board-link
            {:href (oc-urls/board (:slug item))
             :on-click (fn [e]
                         (utils/event-stop e)
                         (when-not (utils/button-clicked? e)
                           (nav-actions/nav-to-url! e (:slug item) (oc-urls/board (:slug item)))))}
            [:div.explore-view-block-title
              (:name item)]
            [:div.explore-view-block-footer
              (follow-button {:following (:follow item)
                              :resource-uuid (:uuid item)
                            :resource-type :board})
              (when (pos? (:total-count item))
                [:span.posts-count
                  (:total-count item)])
              (when (pos? followers-count)
                [:span.followers-count
                  followers-count
                  ; (str followers-count " follower" (when (not= followers-count 1) "s"))
                  ])]])]]))