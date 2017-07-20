(ns oc.web.components.ui.filters-dropdown
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]))

(defn board-filters-label [board-filters topics]
  (cond
    ;; by topic order
    (= board-filters :by-topic) "By Topic"
    ;; if the filter is a string it means we are filtering by a topic slug, get the topic name from the board data
    (string? board-filters) (or (:name (utils/get-topic topics board-filters)) (s/capital board-filters))
    ;; in any other case we are showing by latest updates
    :else "Latest Updates"))

(rum/defcs filters-dropdown < rum/reactive
                              (drv/drv :board-filters)
                              (drv/drv :board-data)
  [s]
  (let [board-data (drv/react s :board-data)
        board-filters (drv/react s :board-filters)
        topic-groups (group-by :topic-slug (:entries board-data))]
    [:div.filters-dropdown-name.group
      (when (string? board-filters)
        [:button.mlb-reset.board-remove-filter
          {:on-click #(let [org-slug (router/current-org-slug)
                            board-slug (router/current-board-slug)
                            last-filter (keyword (cook/get-cookie (router/last-board-filter-cookie org-slug board-slug)))]
                        (if (= last-filter :by-topic)
                          (router/nav! (oc-urls/board-sort-by-topic))
                          (router/nav! (oc-urls/board))))}])
      [:div.filters-dropdown-container
        [:button.mlb-reset.filters-dropdown-caret.dropdown-toggle
          {:type "button"
           :id "filters-dropdown-btn"
           :data-toggle (if (pos? (count topic-groups)) "dropdown" "")
           :aria-haspopup true
           :aria-expanded false}
          (board-filters-label board-filters (:topics board-data))
          (when (pos? (count topic-groups)) [:i.fa.fa-caret-down])]
        [:div.filters-dropdown-list.dropdown-menu
          {:aria-labelledby "filters-dropdown-btn"}
          [:div.triangle]
          [:div.filters-dropdown-list-content
            [:ul
              [:li.category
                {:on-click (fn []
                              (cook/set-cookie! (router/last-board-filter-cookie (router/current-org-slug) (router/current-board-slug)) (name :latest) (* 60 60 24 30) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
                              (router/nav! (oc-urls/board)))
                 :class (if (= board-filters :latest) "select" "")}
                "Latest Updates"]
              [:li.category
                {:on-click (fn [_]
                              (cook/set-cookie! (router/last-board-filter-cookie (router/current-org-slug) (router/current-board-slug)) (name :by-topic) (* 60 60 24 30) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
                              (router/nav! (oc-urls/board-sort-by-topic)))
                 :class (if (= board-filters :by-topic) "select" "")}
                "By Topic"]
              (when (pos? (count (:topics board-data)))
                [:li.divider])
              (for [topic-slug (keys topic-groups)
                    :let [t (some #(when (= (:slug %) topic-slug) %) (:topics board-data))]]
                [:li
                  {:on-click #(router/nav! (oc-urls/board-filter-by-topic (or (:slug t) "uncategorized")))
                   :class (if (or (= board-filters (:slug t))
                                  (and (= board-filters "uncategorized")
                                       (nil? t))) "select" "")
                   :key (str "board-filters-topic-" (or (:slug t) "uncategorized"))}
                  (if t
                    (s/capital (:name t))
                    "Uncategorized")])]]]]]))