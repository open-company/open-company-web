(ns oc.web.components.topics-columns
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.carrot-tip :refer (carrot-tip)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [oc.web.components.ui.filters-dropdown :refer (filters-dropdown)]
            [oc.web.components.ui.empty-board :refer (empty-board)]
            [oc.web.components.entries-layout :refer (entries-layout)]
            [oc.web.components.drafts-layout :refer (drafts-layout)]
            [oc.web.components.stories-layout :refer (stories-layout)]
            [oc.web.components.all-posts :refer (all-posts)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn did-select-storyboard-cb [storyboard]
  (dis/dispatch! [:story-create (clojure.set/rename-keys storyboard {:value :slug :label :name :links :links})]))

(def min-scroll 50)
(def max-scroll 92)

(defn document-scroll-top []
  (max (.-pageYOffset js/window)
       (.-scrollTop (.-documentElement js/document))
       (.-scrollTop (.-body js/document))))

(defn calc-opacity [scroll-top]
  (let [fixed-scroll-top (* (- (min scroll-top max-scroll) 50) 100 (/ 1 (- max-scroll min-scroll)))]
    (max 0 (min (/ fixed-scroll-top 100) 1))))

(defn did-scroll [e owner]
  (let [entry-floating (js/$ "#new-entry-floating-btn")]
    (when (pos? (.-length entry-floating))
      (let [scroll-top (document-scroll-top)]
        (.css entry-floating #js {:opacity (calc-opacity scroll-top)}))))
  (let [story-floating (js/$ "#new-story-floating-btn")]
    (when (pos? (.-length story-floating))
      (let [scroll-top (document-scroll-top)]
        (.css story-floating #js {:opacity (calc-opacity scroll-top)})))))

(defcomponent topics-columns [{:keys [content-loaded
                                      board-data
                                      all-posts-data
                                      is-dashboard
                                      is-all-posts
                                      is-stakeholder-update
                                      board-filters] :as data} owner options]

  (init-state [_]
    {:show-boards-tooltip (and (not (:show-login-overlay data))
                               (jwt/jwt)
                               (cook/get-cookie (router/should-show-dashboard-tooltips (jwt/get-key :user-id))))
     :ww (responsive/ww)
     :resize-listener (events/listen js/window EventType/RESIZE #(om/set-state! owner :ww (responsive/ww)))})

  ; (will-mount [_]
  ;   (when (and (not (utils/is-test-env?))
  ;              is-all-posts)
  ;     (utils/after 100 #(dis/dispatch! [:calendar-get]))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
      (om/set-state! owner :scroll-listener
       (events/listen js/window EventType/SCROLL #(did-scroll % owner))))
    (when (om/get-state owner :show-boards-tooltip)
      (utils/after 1000 #(om/set-state! owner :update true))))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      (events/unlistenByKey (om/get-state owner :resize-listener))
      (events/unlistenByKey (om/get-state owner :scroll-listener))))

  (render-state [_ {:keys [show-storyboards-floating-dropdown show-storyboards-top-dropdown show-boards-tooltip ww]}]
    (let [current-activity-id (router/current-activity-id)
          is-mobile-size? (responsive/is-mobile-size?)
          columns-container-key (if current-activity-id
                                  (str "topics-columns-selected-topic-" current-activity-id)
                                  (s/join "-" (map :slug (:topics board-data))))
          empty-board? (zero? (count (:fixed-items board-data)))
          org-data (dis/org-data)
          sidebar-width (+ responsive/left-navigation-sidebar-width
                           responsive/left-navigation-sidebar-minimum-right-margin)
          board-container-style {:margin-left (str (max sidebar-width (+ (/ (- ww responsive/board-container-width sidebar-width) 2) sidebar-width)) "px")
                                 :left "50%"}]
      ;; Topic list
      (dom/div {:class (utils/class-set {:topics-columns true
                                         :group true
                                         :content-loaded content-loaded})}
        (when show-boards-tooltip
          (when-let* [nav-boards (js/$ "h3#navigation-sidebar-boards")
                      offset (.offset nav-boards)
                      boards-left (aget offset "left")]
            (carrot-tip {:x (+ boards-left 105 30)
                         :y (- (aget offset "top") 160)
                         :title "Welcome to Carrot"
                         :message "Boards make it easy to find the latest news and key updates from across the company. You can create boards for different areas of your company, like Sales, Marketing and Products."
                         :footer ""
                         :on-next-click (fn []
                                          (om/update-state! owner #(merge % {:show-boards-tooltip false}))
                                          (cook/remove-cookie! (router/should-show-dashboard-tooltips (jwt/get-key :user-id))))})))
        (dom/div {:class "topics-column-container group"
                  :key columns-container-key}
          (when-not (responsive/is-mobile-size?)
            (navigation-sidebar))
          (dom/div {:class "board-container group"
                    :style board-container-style}
            ;; Board name row: board name, settings button and say something button
            (dom/div {:class "group"}
              ;; Board name and settings button
              (dom/div {:class "board-name"}
                (if is-all-posts
                  (dom/div {:class "all-posts-icon"})
                  (if (= (:type board-data) "story")
                    (dom/div {:class "stories-icon"})
                    (dom/div {:class "boards-icon"})))
                (if is-all-posts
                  "All Posts"
                  (:name board-data))
                ;; Settings button
                (when (and (router/current-board-slug)
                           (not is-all-posts)
                           (not (:read-only board-data)))
                  (dom/button {:class "mlb-reset board-settings-bt"
                               :data-toggle "tooltip"
                               :data-placement "top"
                               :data-container "body"
                               :title (str (:name board-data) " settings")
                               :on-click #(dis/dispatch! [:board-edit board-data])})))
              ;; Add entry button
              (when (and (not is-all-posts)
                         (not (:read-only org-data))
                         (not (responsive/is-tablet-or-mobile?))
                         (= (:type board-data) "entry")
                         (utils/link-for (:links board-data) "create"))
                (dom/button {:class "mlb-reset mlb-default add-to-board-btn top-button group"
                             :on-click (fn [_]
                                        (let [entry-data {:board-slug (:slug board-data)
                                                          :board-name (:name board-data)}
                                              topic-data (when (string? board-filters)
                                                           (first (filter #(= (:slug %) board-filters) (:topics board-data))))
                                              with-topic (if (string? board-filters)
                                                          (merge entry-data {:topic-slug (:slug topic-data) :topic-name (:name topic-data)})
                                                          entry-data)]
                                          (dis/dispatch! [:entry-edit with-topic])))}
                  (dom/div {:class "add-to-board-pencil"})
                  (dom/label {:class "add-to-board-label"}) "New Post"))
              ;; Add entry floating button
              (when (and (not is-all-posts)
                         (not (:read-only org-data))
                         (not (responsive/is-tablet-or-mobile?))
                         (= (:type board-data) "entry")
                         (utils/link-for (:links board-data) "create"))
                (dom/button {:class "mlb-reset mlb-default add-to-board-btn floating-button"
                             :id "new-entry-floating-btn"
                             :style {:opacity (calc-opacity (document-scroll-top))}
                             :data-placement "left"
                             :data-toggle "tooltip"
                             :title (str "Create a new update")
                             :on-click (fn [_]
                                        (let [entry-data {:board-slug (:slug board-data)
                                                          :board-name (:name board-data)}
                                              topic-data (when (string? board-filters)
                                                           (first (filter #(= (:slug %) board-filters) (:topics board-data))))
                                              with-topic (if (string? board-filters)
                                                          (merge entry-data {:topic-slug (:slug topic-data) :topic-name (:name topic-data)})
                                                          entry-data)]
                                          (dis/dispatch! [:entry-edit with-topic])))}
                  (dom/div {:class "add-to-board-pencil"})))
              ;; Board filters dropdown
              (when (and (not is-mobile-size?)
                         (not empty-board?)
                         (not is-all-posts)
                         (= (:type board-data) "entry"))
                (filters-dropdown)))
            ;; Board content: empty board, add topic, topic view or topic cards
            (cond
              (and is-dashboard
                   is-all-posts)
              (rum/with-key (all-posts all-posts-data) (str "all-posts-" (apply str (keys (:fixed-items all-posts-data)))))
              (and is-dashboard
                   (not is-mobile-size?)
                   (not current-activity-id)
                   empty-board?)
              (empty-board)
              ; for each column key contained in best layout
              :else
              (cond
                ;; Drafts
                (and (= (:type board-data) "story")
                     (= (:slug board-data) "drafts"))
                (drafts-layout board-data)
                ;; Stories
                (= (:type board-data) "story")
                (stories-layout board-data)
                ;; Entries
                :else
                (entries-layout board-data board-filters)))))))))