(ns oc.web.components.navigation-sidebar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.popover :refer (add-popover hide-popover)]))

(defn sorted-boards [boards]
  (into [] (sort-by :name boards)))

(defn sorted-stories [boards]
  (into [] (sort-by :created-at boards)))

(defn sorted-storyboards [boards]
  (into [] (sort-by :name (vec (filter #(not= (:slug %) "drafts") boards)))))

(defn anchor-nav! [e url]
  (utils/event-stop e)
  (router/nav! url))

(defn board-nav! [e board]
  (utils/event-stop e)
  (let [action-kw (if (= (:type board) "story") :storyboard-nav :board-nav)
        board-slug (:slug board)]
    (dis/dispatch! [action-kw board-slug])))

(rum/defcs navigation-sidebar < rum/reactive
                                (drv/drv :org-data)
                                {:did-mount (fn [s]
                                              (when-not (utils/is-test-env?)
                                                (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                              s)
                                 :did-update (fn [s]
                                               (when-not (utils/is-test-env?)
                                                 (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                              s)}
  [s]
  (let [org-data (drv/react s :org-data)
        left-navigation-sidebar-width (- responsive/left-navigation-sidebar-width 20)
        boards (vec (filter #(= (:type %) "entry") (:boards org-data)))
        storyboards (vec (filter #(= (:type %) "story") (:boards org-data)))
        is-all-activity (utils/in? (:route @router/path) "all-activity")]
    [:div.left-navigation-sidebar.group
      {:style {:width (str left-navigation-sidebar-width "px")}}
      ;; All activity
      (when (jwt/user-is-part-of-the-team (:team-id org-data))
        [:a.all-activity.group
          {:class (when is-all-activity "selected")
           :href (oc-urls/all-activity)
           :on-click #(anchor-nav! % (oc-urls/all-activity))}
          [:div.all-activity-icon]
          [:div.all-activity-label
              "All Activity"]])
      ;; Boards list
      [:div.left-navigation-sidebar-top.group
        ;; Boards header
        [:h3.left-navigation-sidebar-top-title
          [:div.boards-icon]
          [:span
            "BOARDS"]]
        (when (and (not (responsive/is-tablet-or-mobile?))
                   (utils/link-for (:links org-data) "create"))
          [:button.left-navigation-sidebar-top-title-button.btn-reset.right
            {:on-click #(dis/dispatch! [:board-edit nil "entry"])
             :title "Create a new board"
             :data-placement "top"
             :data-toggle "tooltip"
             :data-container "body"}])]
      [:div.left-navigation-sidebar-items.group
        (for [board (sorted-boards boards)]
          [:a.left-navigation-sidebar-item
            {:class (when (and (not is-all-activity) (= (router/current-board-slug) (:slug board))) "selected")
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
                {:key (str "board-list-" (name (:slug board)) "-internal")}
                (or (:name board) (:slug board))]]])]
      [:div.left-navigation-sidebar-top.group
        ;; Boards header
        [:h3.left-navigation-sidebar-top-title
          [:div.stories-icon]
          [:span
            "JOURNALS"]]
        (when (and (not (responsive/is-tablet-or-mobile?))
                   (utils/link-for (:links org-data) "create"))
          [:button.left-navigation-sidebar-top-title-button.btn-reset.right
            {:on-click #(dis/dispatch! [:board-edit nil "story"])
             :title "Create a new journal"
             :data-placement "top"
             :data-toggle "tooltip"
             :data-container "body"}])]
      [:div.left-navigation-sidebar-items.group
        (let [drafts-board (first (filter #(= (:slug %) "drafts") (:boards org-data)))
              drafts-link (utils/link-for (:links drafts-board) "self")]
          (when (pos? (:count drafts-link))
            [:div.left-navigation-sidebar-draft
              [:a.left-navigation-sidebar-item
                {:class (when (= (router/current-board-slug) "drafts") "selected")
                 :data-drafts true
                 :key "board-list-draft"
                 :href (oc-urls/drafts)
                 :on-click #(anchor-nav! % (oc-urls/drafts))}
                [:div.board-name.team-board.group
                  [:div.internal
                    (str "Drafts (" (:count drafts-link) ")")]]]
              [:div.left-navigation-sidebar-draft-divider]]))
        (for [storyboard (sorted-storyboards storyboards)]
          [:a.left-navigation-sidebar-item
            {:class (when (and (not is-all-activity) (= (router/current-board-slug) (:slug storyboard))) "selected")
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
                {:key (str "board-list-" (name (:slug storyboard)) "-internal")}
                (or (:name storyboard) (:slug storyboard))]]])]
      [:div.left-navigation-sidebar-footer
        (when (and (router/current-org-slug)
                   (jwt/is-admin? (:team-id org-data)))
          [:button.mlb-reset.invite-people-btn
            {:on-click #(router/nav! (oc-urls/org-settings-invite))}
            [:div.invite-people-icon]
            [:span "Invite People"]])
        [:button.mlb-reset.about-carrot-btn
          {:on-click #(router/nav! oc-urls/about)}
          [:div.about-carrot-icon]
          [:span "About Carrot"]]]]))