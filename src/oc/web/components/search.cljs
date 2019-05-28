(ns oc.web.components.search
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.actions.search :as search]
            [oc.web.stores.search :as store]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]))

(rum/defcs entry-display < rum/static

  [s data]
  (let [result (:_source data)
        title (utils/emojify (:headline result))
        author (first (:author-name result))
        activity-url (oc-urls/entry (:board-slug result) (:uuid result))]
    [:div.search-result
     {:on-click (fn [s]
                  (search/result-clicked activity-url)
                  s)}
     [:div.search-result-box
      (user-avatar-image {:user-id (first (:author-id result))
                          :name author
                          :avatar-url (first (:author-url result))} false)
      [:div.title {:dangerouslySetInnerHTML title}]
      [:div.time-since
       (let [t (or (:published-at result) (:created-at result))]
         [:time
          {:date-time t
           :data-toggle "tooltip"
           :data-placement "top"
           :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
           :data-title (utils/activity-date-tooltip result)}
          (utils/time-since t)])]
      ]]))

(rum/defcs board-display < rum/static
  [s data]
  (let [board (:_source data)]
    [:div.search-result
     [:div.content
      [:span (:name board)]
      ]
     ]))

(rum/defcs results-header < rum/static
  [s result-count]
  [:div.header.group
    [:span "SEARCH RESULTS"]
      (when (pos? result-count)
        [:span.count (str "(" result-count ")")])])

(def default-page-size
  (if (responsive/is-mobile-size?) 300 5))

(rum/defcs search-results-view < (drv/drv store/search-key)
                                 (drv/drv store/search-active?)
                                 rum/reactive
                                 rum/static
                                 (rum/local default-page-size ::page-size)
                                 {:before-render (fn [s]
                                  (when (and (not @(drv/get-ref s store/search-active?))
                                             (not= @(::page-size s) default-page-size))
                                    (reset! (::page-size s) default-page-size))
                                  s)}
  [s]
  (let [search-results (drv/react s store/search-key)
        search-active? (drv/react s store/search-active?)
        result-count (if (< store/search-limit (:count search-results))
                       store/search-limit
                       (:count search-results))
        is-mobile? (responsive/is-mobile-size?)]
    (when-not (nil? search-results)
      [:div.search-results {:ref "results"
                            :class (when-not search-active? "inactive")}
        (when-not is-mobile?
          (results-header result-count))
        [:div.search-results-container
          (when is-mobile?
            (results-header result-count))
          (if (pos? result-count)
            (let [results (reverse (:results search-results))]
              (for [sr (take @(::page-size s) results)]
                (let [key (str "result-" (:uuid (:_source sr)))]
                  (case (:type (:_source sr))
                    "entry" (rum/with-key (entry-display sr) key)
                    "board" (rum/with-key (board-display sr) key)))))
            [:div.empty-result
              [:div.message "No matching results..."]])]
        (when (< @(::page-size s) result-count)
          [:div.show-more
            {:on-click (fn [e] (reset! (::page-size s)
                                       (+ @(::page-size s) 15)))}
            [:button.mlb-reset "Show More"]])])))

(defn search-reset [s]
  (set! (.-value (rum/ref-node s "search-input")) "")
  (reset! (::search-clicked? s) false)
  (search/reset))

(defn search-inactive [s]
  (set! (.-value (rum/ref-node s "search-input")) "")
  (reset! (::search-clicked? s) false)
  (search/inactive))

(rum/defcs search-box < (drv/drv store/search-key)
                        (drv/drv store/search-active?)
                        rum/reactive
                        rum/static
                        (rum/local false ::search-clicked?)
                        (rum/local nil ::search-timeout)
                        (on-window-click-mixin (fn [s e]
                          (when (and (not (responsive/is-tablet-or-mobile?))
                                     @(::search-clicked? s)
                                     (not
                                      (utils/event-inside? e
                                        (sel1 [:div.search-box]))))
                            (search-inactive s))))
                        {:after-render (fn [s]
                          (let [search-input (rum/ref-node s "search-input")]
                            (when (and
                                   (pos?
                                    (count @store/savedsearch))
                                   (not
                                    @(::search-clicked? s)))
                              (set! (.-value search-input) (store/saved-search))
                              (.focus search-input)))
                            s)
                         :will-mount (fn [s]
                          (when-not (responsive/is-tablet-or-mobile?)
                            (search/inactive))
                          s)}
  [s]
  (when (store/should-display)
    (let [search-active? (drv/react s store/search-active?)]
      [:div.search-box
        {:class (when @(::search-clicked? s) "active")
         :on-click (fn [e]
                    (when (and (not @(::search-clicked? s))
                               (not (utils/event-inside? e (rum/ref-node s :search-close))))
                      (.focus (rum/ref-node s "search-input"))))}
        [:div.mobile-header
          [:button.mlb-reset.search-close-bt
            {:on-click #(do
                         (utils/event-stop %)
                         (search-inactive s))}]
          [:div.mobile-header-title
            "Search"]]
        [:button.mlb-reset.search-close
          {:ref :search-close
           :on-click #(search-reset s)}]
        [:div.spyglass-icon
          {:on-click #(reset! (::search-clicked? s) true)}]
        [:input.search
          {:class (when-not @(::search-clicked? s) "inactive")
           :ref "search-input"
           :placeholder "Search"
           :on-blur #(do
                       (when (responsive/is-mobile-size?)
                         (set! (.-placehoder (.-target %)) ""))
                       (let [search-input (.-target %)
                             search-query (.-value search-input)]
                         (when-not (seq (utils/trim search-query))
                           (search-inactive s))))
           :on-focus #(let [search-input (.-target %)
                            search-query (.-value search-input)]
                        (reset! (::search-clicked? s) true)
                        (search/active)
                        (search/focus)
                        (search/query search-query))
           :on-change (fn [e]
                        (let [v (.-value (.-target e))]
                          (when @(::search-timeout s)
                            (.clearTimeout js/window @(::search-timeout s)))
                          (reset! (::search-timeout s)
                           (utils/after 500
                            #(search/query v)))))}]

       (search-results-view)])))