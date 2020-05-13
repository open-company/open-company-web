(ns oc.web.components.explore-view
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.follow-button :refer (follow-button)]))

(defn- is-user? [item]
  (= (:resource-type item) :user))

(defn- is-board? [item]
  (= (:resource-type item) :board))

(defn- sort-items [items]
  (sort-by #(if (is-board? %)
              (:name %)
              (:short-name %))
   items))

(defn- filter-item [s current-user-id item]
  (or (and (is-user? item)
           (not= current-user-id (:user-id item)))
      (and (is-board? item)
           (not= (:slug item) utils/default-drafts-board-slug)
           (not (:publisher-board item)))))

(defn- filter-sort-items [s current-user-id items]
  (sort-items (filterv #(filter-item s current-user-id %) items)))

(rum/defcs explore-view <
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :active-users)
  (drv/drv :current-user-data)
  (drv/drv :follow-boards-list)
  (drv/drv :follow-publishers-list)
  (drv/drv :followers-boards-count)
  (drv/drv :followers-publishers-count)
  [s]
  (let [org-data (drv/react s :org-data)
        current-user-data (drv/react s :current-user-data)
        all-active-users (drv/react s :active-users)
        follow-boards-list (map :uuid (drv/react s :follow-boards-list))
        followers-boards-count (drv/react s :followers-boards-count)
        follow-publishers-list (map :user-id (drv/react s :follow-publishers-list))
        followers-publishers-count (drv/react s :followers-publishers-count)
        all-boards (map #(assoc % :resource-type :board) (:boards org-data))
        authors-uuids (->> org-data :authors (map :user-id) set)
        all-authors (->> all-active-users
                     vals
                     (filter #(and (authors-uuids (:user-id %))
                                   (not= (:user-id current-user-data) (:user-id %))))
                     (map #(assoc % :resource-type :user)))
        all-items (concat all-boards all-authors)
        with-follow (map #(assoc % :follow (or (and (is-user? %)
                                                    (utils/in? follow-publishers-list (:user-id %)))
                                               (and (is-board? %)
                                                    (utils/in? follow-boards-list (:uuid %)))))
                      all-items)
        sorted-items (filter-sort-items s (:user-id current-user-data) with-follow)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.explore-view
      [:div.explore-view-header
        "Explore"]
      [:div.explore-view-blocks
        (for [item sorted-items
              :let [followers-count-data (if (is-user? item)
                                           (get followers-publishers-count (:user-id item))
                                           (get followers-boards-count (:uuid item)))
                    followers-count (:count followers-count-data)]]
          [:div.explore-view-block
            [:div.explore-view-block-title
              (:name item)]
            (when (is-user? item)
              [:div.explore-view-block-description
                (:role item)])
            [:div.explore-view-block-footer
              (follow-button {:following (:follow item)
                              :resource-uuid (if (is-user? item) (:user-id item) (:uuid item))
                            :resource-type (:resource-type item)})
              [:span.followers-count
                (if (pos? followers-count)
                  (str followers-count " follower" (when (not= followers-count 1) "s"))
                  "No followers")]]])]]))