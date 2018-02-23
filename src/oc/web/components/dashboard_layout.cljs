(ns oc.web.components.dashboard-layout
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.all-posts :refer (all-posts)]
            [oc.web.components.ui.empty-org :refer (empty-org)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [oc.web.components.ui.empty-board :refer (empty-board)]
            [oc.web.components.drafts-layout :refer (drafts-layout)]
            [oc.web.components.entries-layout :refer (entries-layout)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def min-scroll 50)
(def max-scroll 92)

(defn document-scroll-top []
  (if (.-body js/document)
    (max (.-pageYOffset js/window)
         (.-scrollTop (.-documentElement js/document))
         (.-scrollTop (.-body js/document)))
    0))

(defn calc-opacity [scroll-top]
  (let [fixed-scroll-top (/
                          (*
                           (- (min scroll-top max-scroll) 50)
                           100)
                          (- max-scroll min-scroll))]
    (max 0 (min (/ fixed-scroll-top 100) 1))))

(defn did-scroll [e s]
  (let [entry-floating (js/$ "#new-entry-floating-btn-container")]
    (when (pos? (.-length entry-floating))
      (let [scroll-top (document-scroll-top)
            opacity (if (or @(::show-floating-boards-dropdown s)
                            (responsive/is-tablet-or-mobile?))
                      1
                      (calc-opacity scroll-top))]
        (.css entry-floating #js {:opacity opacity
                                 :display (if (pos? opacity) "block" "none")})))))

(rum/defcs dashboard-layout < rum/reactive
                              ;; Derivative
                              (drv/drv :route)
                              (drv/drv :org-data)
                              (drv/drv :board-data)
                              (drv/drv :all-posts)
                              (drv/drv :nux)
                              (drv/drv :editable-boards)
                              ;; Locals
                              (rum/local nil ::force-update)
                              (rum/local nil ::ww)
                              (rum/local nil ::resize-listener)
                              (rum/local nil ::scroll-listener)
                              (rum/local nil ::show-top-boards-dropdown)
                              (rum/local nil ::show-floating-boards-dropdown)
                              {:will-mount (fn [s]
                                ;; Get current window width
                                (reset! (::ww s) (responsive/ww))
                                ;; Update window width on window resize
                                (reset! (::resize-listener s)
                                 (events/listen js/window EventType/RESIZE #(reset! (::ww s) (responsive/ww))))
                                s)
                               :did-mount (fn [s]
                                (when-not (utils/is-test-env?)
                                  (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                                  (reset! (::scroll-listener s)
                                   (events/listen js/window EventType/SCROLL #(did-scroll % s))))
                                s)
                               :will-unmount (fn [s]
                                (when-not (utils/is-test-env?)
                                  (when @(::resize-listener s)
                                    (events/unlistenByKey @(::resize-listener s)))
                                  (when @(::scroll-listener s)
                                    (events/unlistenByKey @(::resize-listener s))))
                                s)}
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        all-posts-data (drv/react s :all-posts)
        route (drv/react s :route)
        is-all-posts (or (utils/in? (:route route) "all-posts")
                         (:from-all-posts route))
        nux (drv/react s :nux)
        current-activity-id (router/current-activity-id)
        is-mobile-size? (responsive/is-mobile-size?)
        empty-board? (and (not nux)
                          (zero? (count (:fixed-items board-data))))
        sidebar-width (+ responsive/left-navigation-sidebar-width
                         responsive/left-navigation-sidebar-minimum-right-margin)
        board-container-style {:marginLeft (if is-mobile-size?
                                             "0px"
                                             (str (max
                                                   sidebar-width
                                                   (+
                                                    (/
                                                     (- @(::ww s) responsive/board-container-width sidebar-width)
                                                     2)
                                                   sidebar-width))
                                             "px"))}
        is-drafts-board (= (:slug board-data) utils/default-drafts-board-slug)
        all-boards (drv/react s :editable-boards)]
      ;; Entries list
      [:div.dashboard-layout.group
        [:div.dashboard-layout-container.group
          (navigation-sidebar)
          [:div.board-container.group
            {:style board-container-style}
            ;; Board name row: board name, settings button and say something button
            [:div.board-name-container.group
              ;; Board name and settings button
              [:div.board-name
                (when (router/current-board-slug)
                  [:span.board-name-span
                    {:dangerouslySetInnerHTML (if is-all-posts
                                                #js {"__html" "All Posts"}
                                                (utils/emojify (:name board-data)))}])
                ;; Settings button
                (when (and (router/current-board-slug)
                           (not is-all-posts)
                           (not (:read-only board-data)))
                  [:button.mlb-reset.board-settings-bt
                    {:data-toggle (when-not is-mobile-size? "tooltip")
                     :data-placement "top"
                     :data-container "body"
                     :title (str (:name board-data) " settings")
                     :on-click #(dis/dispatch! [:board-edit board-data])}])
                (when (= (:access board-data) "private")
                  [:div.private-board
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title (if (= (router/current-board-slug) utils/default-drafts-board-slug)
                             "Only visible to you"
                             "Only visible to invited team members")}
                    "Private"])
                (when (= (:access board-data) "public")
                  [:div.public-board
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title "Visible to the world, including search engines"}
                    "Public"])]
              ;; Add entry button
              (when (and (not (:read-only org-data))
                         (or (utils/link-for (:links board-data) "create")
                             is-drafts-board
                             is-all-posts))
                [:div.new-post-top-dropdown-container.group
                  [:button.mlb-reset.mlb-default.add-to-board-top-button.group
                    {:class (when @(::show-top-boards-dropdown s) "active")
                     :on-click (fn [_]
                                (if (or is-drafts-board is-all-posts)
                                  (reset! (::show-top-boards-dropdown s) (not @(::show-top-boards-dropdown s)))
                                  (let [entry-data {:board-slug (:slug board-data)
                                                    :board-name (:name board-data)}]
                                    (activity-actions/entry-edit entry-data))))}
                    [:div.add-to-board-pencil]
                    [:label.add-to-board-label
                      "Compose"]]
                  (when @(::show-top-boards-dropdown s)
                    (dropdown-list
                     {:items (map
                              #(-> %
                                (select-keys [:name :slug])
                                (clojure.set/rename-keys {:name :label :slug :value}))
                              (vals all-boards))
                      :value ""
                      :on-blur #(reset! (::show-top-boards-dropdown s) false)
                      :on-change (fn [item]
                                   (reset! (::show-top-boards-dropdown s) false)
                                   (activity-actions/entry-edit {:board-slug (:value item)
                                                                 :board-name (:label item)}))}))])]
            ;; Board content: empty org, all posts, empty board, drafts view, entries view
            (cond
              ;; No boards
              (zero? (count (:boards org-data)))
              (empty-org)
              ;; All Posts
              is-all-posts
              (all-posts)
              ;; Empty board
              empty-board?
              (empty-board)
              ;; Layout boards activities
              :else
              (cond
                ;; Drafts
                is-drafts-board
                (drafts-layout board-data)
                ;; Entries
                :else
                (entries-layout)))
            ;; Add entry floating button
            (when (and (not (:read-only org-data))
                       (or (utils/link-for (:links board-data) "create")
                           is-drafts-board
                           is-all-posts))
              (let [opacity (if (or @(::show-floating-boards-dropdown s)
                                    (responsive/is-tablet-or-mobile?))
                              1
                              (calc-opacity (document-scroll-top)))]
                [:div.new-post-floating-dropdown-container.group
                  {:id "new-entry-floating-btn-container"
                   :style {:opacity opacity
                           :display (if (pos? opacity) "block" "none")}}
                  [:button.mlb-reset.mlb-default.add-to-board-floating-button
                    {:class (when @(::show-floating-boards-dropdown s) "active")
                     :data-placement "left"
                     :data-container "body"
                     :data-toggle (when-not is-mobile-size? "tooltip")
                     :title "Start a new post"
                     :on-click (fn [_]
                                (utils/remove-tooltips)
                                (if (or is-drafts-board
                                        is-all-posts)
                                  (reset!
                                   (::show-floating-boards-dropdown s)
                                   (not @(::show-floating-boards-dropdown s)))
                                  (let [entry-data {:board-slug (:slug board-data)
                                                    :board-name (:name board-data)}]
                                    (activity-actions/entry-edit entry-data))))}
                    [:div.add-to-board-pencil]]
                  (when @(::show-floating-boards-dropdown s)
                    (dropdown-list
                     {:items (map
                              #(-> %
                                (select-keys [:name :slug])
                                (clojure.set/rename-keys {:name :label :slug :value}))
                              (vals all-boards))
                      :value ""
                      :on-blur #(reset! (::show-floating-boards-dropdown s) false)
                      :on-change (fn [item]
                                   (reset! (::show-floating-boards-dropdown s) false)
                                   (activity-actions/entry-edit {:board-slug (:value item)
                                                                 :board-name (:label item)}))}))]))]]]))