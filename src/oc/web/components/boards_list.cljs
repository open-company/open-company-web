(ns oc.web.components.boards-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
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

(defcomponent boards-list
  [{:keys [org-data show-add-topic] :as data} owner options]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (did-update [_ prev-props _]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (render [_]
    (let [left-boards-list-width (- responsive/left-boards-list-width 20)]
      (dom/div {:class "left-boards-list group"
                :style {:width (str left-boards-list-width "px")}}
        ;; All activity
        (when (jwt/user-is-part-of-the-team (:team-id org-data))
          (dom/button
            {:class (utils/class-set {:all-activity true
                                      :group true
                                      :selected (utils/in? (:route @router/path) "all-activity")})
             :on-click #(router/nav! (oc-urls/all-activity))}
            (dom/div {:class "all-activity-icon"})
            (dom/div
              {:class "all-activity-label"}
              "All Activity")))
        ;; Boards list
        (dom/div {:class "left-boards-list-top group"}
          ;; Boards header
          (dom/h3 {:class "left-boards-list-top-title"}
            (dom/div {:class "boards-icon"})
            (dom/span "BOARDS"))
          (when (and (not (responsive/is-tablet-or-mobile?))
                     (utils/link-for (:links org-data) "create"))
            (dom/button {:class "left-boards-list-top-title-button btn-reset right"
                         :on-click #(dis/dispatch! [:board-edit nil])
                         :title "Create a new board"
                         :data-placement "top"
                         :data-toggle "tooltip"
                         :data-container "body"})))
        (dom/div {:class (str "left-boards-list-items group")}
          (for [board (sorted-boards (:boards org-data))]
            (dom/div {:class (utils/class-set {:left-boards-list-item true
                                               :selected (= (router/current-board-slug) (:slug board))})
                      :data-board (name (:slug board))
                      :key (str "board-list-" (name (:slug board)))
                      :on-click #(dis/dispatch! [:board-nav (:slug board)])}
              (when (or (= (:access board) "public")
                        (= (:access board) "private"))
                (dom/img {:src (if (= (:access board) "public") (utils/cdn "/img/ML/board_public.svg") (utils/cdn "/img/ML/board_private.svg"))
                          :class (if (= (:access board) "public") "public" "private")}))
              (dom/div {:class (utils/class-set {:group true
                                                 :board-name true
                                                 :public-board (= (:access board) "public")
                                                 :private-board (= (:access board) "private")
                                                 :team-board (= (:access board) "team")})}
                (dom/div {:class "internal"
                          :key (str "board-list-" (name (:slug board)) "-internal")}
                  (or (:name board) (:slug board)))))))
        (comment ;; FIXME: Temporarily comment out stories since we don't have backend support
          (dom/div {:class "left-boards-list-top group"}
                    ;; Boards header
                    (dom/h3 {:class "left-boards-list-top-title"}
                      (dom/div {:class "stories-icon"})
                      (dom/span "STORIES"))
                    (when (and (not (responsive/is-tablet-or-mobile?))
                               true) ;; FIXME: replace with create storeis link check
                      (dom/button {:class "left-boards-list-top-title-button btn-reset right"
                                   :on-click #(identity %) ;; FIXME: Replace with story creation action
                                   :title "Create a new story"
                                   :data-placement "top"
                                   :data-toggle "tooltip"
                                   :data-container "body"}))))
        (dom/div {:class "left-boards-list-footer"}
          (when (and (router/current-org-slug)
                     (jwt/is-admin? (:team-id org-data)))
            (dom/button {:class "mlb-reset invite-people-btn"
                         :on-click #(router/nav! (oc-urls/org-team-settings))}
              (dom/div {:class "invite-people-icon"}) (dom/span "Invite People")))
          (dom/button {:class "mlb-reset about-carrot-btn"
                       :on-click #(router/nav! oc-urls/about)}
            (dom/div {:class "about-carrot-icon"}) (dom/span "About Carrot")))))))