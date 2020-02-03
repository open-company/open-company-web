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
                  (search/result-clicked result activity-url)
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
    (if (empty? search-results)
      [:div.search-history
        (let [history (search/search-history)]
          (for [idx (range (count history))
                :let [q (get (vec (reverse history)) idx)]]
            [:div.search-history-row
              {:key (str "search-history-" idx)
               :on-click #(do
                            (.val (js/$ "input.search.oc-input") q)
                            (search/query q))}
              q]))]
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
  (search/reset))

(defn search-inactive [s]
  (search/inactive)
  (set! (.-value (rum/ref-node s "search-input")) "")
  (reset! (::search-clicked? s) false))

(rum/defcs search-box < (drv/drv store/search-key)
                        (drv/drv store/search-active?)
                        rum/reactive
                        rum/static
                        (rum/local false ::search-clicked?)
                        (rum/local nil ::search-timeout)
                        (rum/local false ::last-search-active)
                        (rum/local "" ::query)
                        (on-window-click-mixin (fn [s e]
                          (when (and (not (responsive/is-mobile-size?))
                                     @(::search-clicked? s)
                                     (sel1 [:div.search-box])
                                     (not (utils/event-inside? e (sel1 [:div.search-box]))))
                            (search-inactive s))))
                        {:after-render (fn [s]
                          (let [search-input (rum/ref-node s "search-input")
                                saved-search @store/savedsearch
                                search-active? @(drv/get-ref s store/search-active?)
                                last-search-active @(::last-search-active s)]
                            (when (and (pos? (count saved-search))
                                       search-active?
                                       (not= last-search-active search-active?))
                              (set! (.-value search-input) (store/saved-search))
                              (.focus search-input)))
                            s)
                         :did-update (fn [s]
                          (let [current-search-active @(drv/get-ref s store/search-active?)
                                search-active? @(drv/get-ref s store/search-active?)]
                            (when (compare-and-set! (::last-search-active s) (not current-search-active) (boolean current-search-active))
                              (when-not current-search-active
                                (reset! (::query s) "")
                                (reset! (::search-clicked? s) false))))
                          s)
                         :will-mount (fn [s]
                          (when-not (responsive/is-mobile-size?)
                            (search/inactive))
                          s)}
  [s]
  (when (store/should-display)
    (let [search-active? (drv/react s store/search-active?)
          is-mobile? (responsive/is-mobile-size?)]
      [:div.search-box
        {:class (when @(::search-clicked? s) "active")
         :on-click (fn [e]
                    (when (and (not @(::search-clicked? s))
                               (not (utils/event-inside? e (rum/ref-node s :search-close))))
                      (.focus (rum/ref-node s "search-input"))))}
        [:button.mlb-reset.search-close
          {:ref :search-close
           :on-click #(search-reset s)}]
        [:div.spyglass-icon
          {:on-click #(reset! (::search-clicked? s) true)}]
        [:input.search.oc-input
          {:class (when-not @(::search-clicked? s) "inactive")
           :ref "search-input"
           :type "search"
           :value @(::query s)
           :placeholder (if is-mobile? "Search posts..." "Search")
           :on-blur #(when-not is-mobile?
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
                          (reset! (::query s) v)
                          (when @(::search-timeout s)
                            (.clearTimeout js/window @(::search-timeout s)))
                          (reset! (::search-timeout s)
                           (utils/after 500
                            #(search/query v)))))}]
       (search-results-view)])))