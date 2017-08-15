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
        storyboards (vec (filter #(= (:type %) "story") (:boards org-data)))]
    [:div.left-navigation-sidebar.group
      {:style {:width (str left-navigation-sidebar-width "px")}}
      ;; All activity
      (when (jwt/user-is-part-of-the-team (:team-id org-data))
        [:button
          {:class (utils/class-set {:all-activity true
                                    :group true
                                    :selected (utils/in? (:route @router/path) "all-activity")})
           :on-click #(router/nav! (oc-urls/all-activity))}
          [:div.all-activity-icon]
          [:div.all-activity-label
              "All Activity"]])
      ;; Boards list
      [:div.left-navigation-sidebar-top.group
        ;; Boards header
        [:h3.left-navigation-sidebar-top-title
          [:div.boards-icon]
          [:span "BOARDS"]]
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
          [:div
            {:class (utils/class-set {:left-navigation-sidebar-item true
                                      :selected (= (router/current-board-slug) (:slug board))})
             :data-board (name (:slug board))
             :key (str "board-list-" (name (:slug board)))
             :on-click #(dis/dispatch! [:board-nav (:slug board)])}
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
          [:span "STORIES"]]
        (when (and (not (responsive/is-tablet-or-mobile?))
                   true) ;; FIXME: replace with create storeis link check
          [:button.left-navigation-sidebar-top-title-button.btn-reset.right
            {:on-click #(dis/dispatch! [:board-edit nil "story"])
             :title "Create a new storyboard"
             :data-placement "top"
             :data-toggle "tooltip"
             :data-container "body"}])]
      [:div.left-navigation-sidebar-items.group
        (let [drafts-link (utils/link-for (:links org-data) "draft")]
          (when (pos? (:count drafts-link))
            [:div
              {:class (utils/class-set {:left-navigation-sidebar-item true
                                        :selected (utils/in? (:route @router/path) "drafts")})
               :data-drafts true
               :key "board-list-draft"
               :on-click #(router/nav! (oc-urls/drafts))}
              [:div.board-name.team-board.group
                [:div.internal
                  (str "Drafts" (:count drafts-link))]]]))
        (for [storyboard (sorted-stories storyboards)]
          [:div
            {:class (utils/class-set {:left-navigation-sidebar-item true
                                      :selected (= (router/current-board-slug) (:slug storyboard))})
             :data-board (name (:slug storyboard))
             :data-storyboard true
             :key (str "board-list-" (name (:slug storyboard)))
             :on-click #(dis/dispatch! [:storyboard-nav (:slug storyboard)])}
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
            {:on-click #(router/nav! (oc-urls/org-team-settings))}
            [:div.invite-people-icon]
            [:span "Invite People"]])
        [:button.mlb-reset.about-carrot-btn
          {:on-click #(router/nav! oc-urls/about)}
          [:div.about-carrot-icon]
          [:span "About Carrot"]]]]))