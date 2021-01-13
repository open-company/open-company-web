(ns oc.web.components.explore-view
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.follow-button :refer (follow-button)]))

(defn- filter-item [s item]
  (not= (:slug item) utils/default-drafts-board-slug))

(defn- filter-sort-items [s items]
  (->> items
   (filterv #(filter-item s %))
   (sort-by :name)))

(rum/defcs explore-view <
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :follow-boards-list)
  (drv/drv :followers-boards-count)
  ui-mixins/refresh-tooltips-mixin
  [s]
  (let [org-data (drv/react s :org-data)
        follow-boards-list (map :uuid (drv/react s :follow-boards-list))
        followers-boards-count (drv/react s :followers-boards-count)
        with-follow (map #(assoc % :follow (utils/in? follow-boards-list (:uuid %))) (:boards org-data))
        sorted-items (filter-sort-items s with-follow)
        is-mobile? (responsive/is-mobile-size?)
        premium-lock? (not (:premium? org-data))]
    [:div.explore-view
      [:div.explore-view-blocks
        (for [item sorted-items
              :let [followers-count (get-in followers-boards-count [(:uuid item) :count])
                    premium-locked? (and premium-lock?
                                         (not= (:access item) "team"))]]
          [:a.explore-view-block.board-link
           {:key (str "explore-view-board-" (:slug item))
            :href (oc-urls/board (:slug item))
            :class (utils/class-set {:premium-lock premium-locked?})
            :data-toggle (when premium-locked? "tooltip")
            :data-placement "top"
            :data-container "body"
            :title (str (string/capital (:access item)) " boards are read-only except on premium.")
            :on-click (fn [e]
                        (utils/event-stop e)
                        (when-not (utils/button-clicked? e)
                          (nav-actions/nav-to-url! e (:slug item) (oc-urls/board (:slug item)))))}
           [:div.explore-view-block-title
            {:class (utils/class-set {:short-name (< (count (:name item)) 15)})}
            [:span.board-name (:name item)]
            (when (= (:access item) "private")
              [:span.private-board
               {:data-toggle (when-not is-mobile? "tooltip")
                :data-placement "top"
                :data-container "body"
                :title "Private"}])
            (when (= (:access item) "public")
              [:span.public-board
               {:data-toggle (when-not is-mobile? "tooltip")
                :data-placement "top"
                :data-container "body"
                :title "Public"}])
            (when (utils/link-for (:links item) "partial-update")
              [:div.board-settings
               [:button.mlb-reset.board-settings-bt
                {:data-placement "top"
                 :data-container "body"
                 :data-toggle (when-not is-mobile? "tooltip")
                 :title "Edit topic"
                 :on-click (fn [e]
                             (utils/event-stop e)
                             (nav-actions/show-section-editor (:slug item)))}]])]
           [:div.explore-view-block-description
            (:description item)]
           (when (:last-entry-at item)
             [:div.explore-view-block-latest-activity
              (str "Last update " (utils/explore-date-time (:last-entry-at item)))])
           [:div.explore-view-block-footer
            (follow-button {:following (:follow item)
                            :resource-uuid (:uuid item)
                            :resource-type :board})
            (when (pos? (:total-count item))
              [:span.posts-count
               {:data-toggle (when-not is-mobile? "tooltip")
                :data-placement "top"
                :data-container "body"
                :title "Number of updates"}
               (:total-count item)])
            (when (pos? followers-count)
              [:span.followers-count
               {:data-toggle (when-not is-mobile? "tooltip")
                :data-placement "top"
                :data-container "body"
                :title (str followers-count
                            (if (= followers-count 1)
                              " person"
                              " people")
                            " subscribed")}
               followers-count
                  ; (str followers-count " follower" (when (not= followers-count 1) "s"))
               ])
            (when (utils/link-for (:links item) "partial-update")
              [:button.mlb-reset.mobile-edit-topic-bt
               {:on-click (fn [e]
                            (utils/event-stop e)
                            (nav-actions/show-section-editor (:slug item)))}
               "Edit topic"])]])]]))