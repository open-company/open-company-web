(ns oc.web.components.navigation-sidebar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.popover :refer (add-popover hide-popover)]
            [goog.events :as events]
            [taoensso.timbre :as timbre]
            [goog.events.EventType :as EventType]))

(defn sort-boards [boards]
  (into [] (sort-by :name boards)))

(defn sort-storyboards [boards]
  (into [] (sort-by :name (vec (filter #(not= (:slug %) "drafts") boards)))))

(defn anchor-nav! [e url]
  (utils/event-stop e)
  (router/nav! url))

(defn board-nav! [e board]
  (utils/event-stop e)
  (let [action-kw (if (= (:type board) "story") :storyboard-nav :board-nav)
        board-slug (:slug board)]
    (dis/dispatch! [action-kw board-slug])))

(defn new?
  "
  A board/journal is new if:
  
  change-at is newer than seen at
   -or-
  we have a change-at and no seen at
  "
  [board]
  (timbre/debug "New test for:" (:slug board) "id:" (:uuid board) "ca:" (:change-at board) "sa:" (:seen-at board))
  (let [change-at (:change-at board)
        seen-at (:seen-at board)]

    (or (and change-at seen-at (> change-at seen-at))
        (and change-at (not seen-at)))))

(def sidebar-top-margin 122)
(def footer-button-height 31)

(rum/defcs navigation-sidebar < rum/reactive
                                (drv/drv :org-data)
                                (rum/local false ::first-render-done)
                                (rum/local false ::content-height)
                                (rum/local nil ::resize-listener)
                                (rum/local nil ::window-height)
                                {:did-mount (fn [s]
                                             (reset! (::first-render-done s) true)
                                              (when-not (utils/is-test-env?)
                                                (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                              (reset! (::window-height s) (.-innerHeight js/window))
                                              (reset! (::resize-listener s)
                                               (events/listen js/window EventType/RESIZE #(reset! (::window-height s) (.-innerHeight js/window))))
                                              s)
                                 :will-update (fn [s]
                                                (when @(::first-render-done s)
                                                  (let [height (.height (js/$ (rum/ref-node s "left-navigation-sidebar-content")))]
                                                    (when (not= height @(::content-height s))
                                                      (reset! (::content-height s) height))))
                                                s)
                                 :did-update (fn [s]
                                               (when-not (utils/is-test-env?)
                                                 (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                              s)
                                 :will-unmount (fn [s]
                                                 (when @(::resize-listener s)
                                                   (events/unlistenByKey @(::resize-listener s)))
                                                 s)}
  [s]
  (let [org-data (drv/react s :org-data)
        left-navigation-sidebar-width (- responsive/left-navigation-sidebar-width 20)
        all-boards (:boards org-data)
        boards (vec (filter #(= (:type %) "entry") all-boards))
        storyboards (vec (filter #(= (:type %) "story") all-boards))
        is-all-activity (or (= (router/current-board-slug) "all-activity") (:from-all-activity @router/path))
        create-link (utils/link-for (:links org-data) "create")
        show-boards (or create-link (pos? (count boards)))
        show-storyboards (or create-link (pos? (count storyboards)))
        show-all-activity (jwt/user-is-part-of-the-team (:team-id org-data))
        show-create-new-board (and (not (responsive/is-tablet-or-mobile?))
                                   create-link)
        show-create-new-journal (and (not (responsive/is-tablet-or-mobile?))
                                     create-link)
        drafts-board (first (filter #(= (:slug %) "drafts") all-boards))
        drafts-link (utils/link-for (:links drafts-board) "self")
        show-drafts (pos? (:count drafts-link))
        show-invite-people (and (router/current-org-slug)
                                (jwt/is-admin? (:team-id org-data)))
        is-enough-tall (< @(::content-height s) (- @(::window-height s) sidebar-top-margin footer-button-height 20 (when show-invite-people footer-button-height)))]
    
    [:div.left-navigation-sidebar.group
      [:div.left-navigation-sidebar-content
        {:ref "left-navigation-sidebar-content"}
        ;; All activity
        (when show-all-activity
          [:a.all-activity.hover-item.group
            {:class (when is-all-activity "item-selected")
             :href (oc-urls/all-activity)
             :on-click #(anchor-nav! % (oc-urls/all-activity))}
            [:div.all-activity-icon]
            [:div.all-activity-label
                "All Activity"]])
        ;; Boards list
        (when show-boards
          [:div.left-navigation-sidebar-top.group
            ;; Boards header
            [:h3.left-navigation-sidebar-top-title.group
              {:id "navigation-sidebar-boards"}
              [:div.boards-icon]
              [:span
                "BOARDS"]
              (when show-create-new-board
                [:button.left-navigation-sidebar-top-title-button.btn-reset.right
                  {:on-click #(dis/dispatch! [:board-edit nil "entry"])
                   :title "Create a new board"
                   :data-placement "top"
                   :data-toggle "tooltip"
                   :data-container "body"}])]])
        (when show-boards
          [:div.left-navigation-sidebar-items.group
            (for [board (sort-boards boards)]
            [:a.left-navigation-sidebar-item.hover-item
                {:class (when (and (not is-all-activity) (= (router/current-board-slug) (:slug board))) "item-selected")
                 :data-board (name (:slug board))
                 :key (str "board-list-" (name (:slug board)))
                 :href (oc-urls/board (router/current-org-slug) (:slug board))
                 :on-click #(board-nav! % board)}
                (when (or (= (:access board) "public")
                          (= (:access board) "private"))
                  [:img
                    {:src (if (= (:access board) "public") (utils/cdn "/img/ML/board_public.svg") (utils/cdn "/img/ML/board_private.svg"))
                     :class (if (= (:access board) "public") "public" "private")}])
                [:div.board-name.group
                  {:class (utils/class-set {:public-board (= (:access board) "public")
                                            :private-board (= (:access board) "private")
                                            :team-board (= (:access board) "team")})}
                  [:div.internal
                    {:class (utils/class-set {:new (new? board)})
                     :key (str "board-list-" (name (:slug board)) "-internal")
                     :dangerouslySetInnerHTML (utils/emojify (or (:name board) (:slug board)))}]]])])
        (when show-storyboards
          [:div.left-navigation-sidebar-top.group
            ;; Boards header
            [:h3.left-navigation-sidebar-top-title.group
              {:id "navigation-sidebar-journals"}
              [:div.stories-icon]
              [:span
                "JOURNALS"]
              (when show-create-new-journal
                [:button.left-navigation-sidebar-top-title-button.btn-reset.right
                  {:on-click #(dis/dispatch! [:board-edit nil "story"])
                   :title "Create a new journal"
                   :data-placement "top"
                   :data-toggle "tooltip"
                   :data-container "body"}])]])
        (when show-storyboards
          [:div.left-navigation-sidebar-items.group
            (for [storyboard (sort-storyboards storyboards)]
              [:a.left-navigation-sidebar-item.hover-item
                {:class (when (and (not is-all-activity) (= (router/current-board-slug) (:slug storyboard))) "item-selected")
                 :data-board (name (:slug storyboard))
                 :data-storyboard true
                 :key (str "board-list-" (name (:slug storyboard)))
                 :href (oc-urls/board (router/current-org-slug) (:slug storyboard))
                 :on-click #(board-nav! % storyboard)}
                (when (or (= (:access storyboard) "public")
                          (= (:access storyboard) "private"))
                  [:img
                    {:src (if (= (:access storyboard) "public") (utils/cdn "/img/ML/board_public.svg") (utils/cdn "/img/ML/board_private.svg"))
                     :class (if (= (:access storyboard) "public") "public" "private")}])
                [:div.board-name.group
                  {:class (utils/class-set {:public-board (= (:access storyboard) "public")
                                            :private-board (= (:access storyboard) "private")
                                            :team-board (= (:access storyboard) "team")})}
                  [:div.internal
                    {:class (utils/class-set {:new (new? storyboard)})
                     :key (str "board-list-" (name (:slug storyboard)) "-internal")
                     :dangerouslySetInnerHTML (utils/emojify (or (:name storyboard) (:slug storyboard)))}]]])
            (when show-drafts
              [:div.left-navigation-sidebar-draft
                [:a.left-navigation-sidebar-item.hover-item
                  {:class (when (= (router/current-board-slug) "drafts") "item-selected")
                   :data-drafts true
                   :key "board-list-draft"
                   :href (oc-urls/drafts)
                   :on-click #(anchor-nav! % (oc-urls/drafts))}
                  [:div.board-name.team-board.group
                    [:div.internal
                      (str "Drafts (" (:count drafts-link) ")")]]]])])]
      [:div.left-navigation-sidebar-footer
        {:style {:position (if is-enough-tall "absolute" "relative")}}
        (when show-invite-people
          [:button.mlb-reset.invite-people-btn
            {:on-click #(dis/dispatch! [:org-settings-show :invite])}
            [:div.invite-people-icon]
            [:span "Invite People"]])
        [:button.mlb-reset.about-carrot-btn
          {:on-click #(dis/dispatch! [:about-carrot-modal-show])}
          [:div.about-carrot-icon]
          [:span "About Carrot"]]]]))