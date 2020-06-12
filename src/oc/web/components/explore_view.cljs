(ns oc.web.components.explore-view
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
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
  (rum/local nil ::dropdown-menu)
  (ui-mixins/on-window-click-mixin (fn [s e]
    (when (and (seq @(::dropdown-menu s))
               (not (utils/event-inside? e (rum/ref-node s (str "dropdown-menu-" @(::dropdown-menu s))))))
      (reset! (::dropdown-menu s) nil))))
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
        "Browse topics"]
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
            {:key (str "explore-view-board-" (:slug item))
             :href (oc-urls/board (:slug item))
             :on-click (fn [e]
                         (utils/event-stop e)
                         (when-not (utils/button-clicked? e)
                           (nav-actions/nav-to-url! e (:slug item) (oc-urls/board (:slug item)))))}
            [:div.explore-view-block-title
              {:class (when (< (count (:name item)) 15) "short-name")}
              [:span.board-name (:name item)]
              [:div.board-dropdown-container
                {:ref (str "dropdown-menu-" (:uuid item))}
                [:button.mlb-reset.board-dropdown-bt
                 {:on-click (fn [e]
                              (utils/event-stop e)
                              (reset! (::dropdown-menu s) (:uuid item)))}]
                (when (= @(::dropdown-menu s) (:uuid item))
                  (dropdown-list {:items [{:label "Edit topic"
                                           :value :edit
                                           :disabled (:read-only item)}
                                          {:label "Preview" :value :preview}]
                                  :on-change (fn [i e]
                                               (when e
                                                 (utils/event-stop e))
                                               (reset! (::dropdown-menu s) nil)
                                               (cond
                                                 (= (:value i) :edit)
                                                 (nav-actions/show-section-editor (:slug item))
                                                 (= (:value i) :preview)
                                                 (nav-actions/nav-to-url! nil (:slug item) (oc-urls/board (:slug item)))
                                                 :else
                                                 nil))}))]]
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
                  ])]])]]))