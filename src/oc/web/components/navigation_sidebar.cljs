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

(defn- delete-board-alert [e board-slug]
  (utils/event-stop e)
  (add-popover {:container-id "delete-board-alert"
                :message "Are you sure you want to delete this board?"
                :height "130px"
                :success-title "DELETE"
                :success-cb #(do
                                (dis/dispatch! [:board-delete board-slug])
                                (hide-popover nil "delete-board-alert"))
                :cancel-title "KEEP IT"
                :cancel-cb #(hide-popover nil "delete-board-alert")}))

(defn sorted-boards [boards]
  (into [] (sort-by :name boards)))

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
        left-navigation-sidebar-width (- responsive/left-navigation-sidebar-width 20)]
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
            {:on-click #(dis/dispatch! [:board-edit nil])
             :title "Create a new board"
             :data-placement "top"
             :data-toggle "tooltip"
             :data-container "body"}])]
      [:div.left-navigation-sidebar-items.group
        (for [board (sorted-boards (:boards org-data))]
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
      (comment ;; FIXME: Temporarily comment out stories since we don't have backend support
        [:div.left-navigation-sidebar-top.group
          ;; Boards header
          [:h3.left-navigation-sidebar-top-title
            [:div.stories-icon]
            [:span "STORIES"]]
          (when (and (not (responsive/is-tablet-or-mobile?))
                     true) ;; FIXME: replace with create storeis link check
            [:button.left-navigation-sidebar-top-title-button.btn-reset.right
              {:on-click #(identity %) ;; FIXME: Replace with story creation action
               :title "Create a new story"
               :data-placement "top"
               :data-toggle "tooltip"
               :data-container "body"}])])
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