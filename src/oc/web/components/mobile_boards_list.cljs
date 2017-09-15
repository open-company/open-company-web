(ns oc.web.components.mobile-boards-list
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.loading :refer (rloading)]))

(rum/defcs mobile-boards-list < rum/reactive
                                       (drv/drv :org-data)
                                       (drv/drv :loading)
  [s]
  (let [org-data (drv/react s :org-data)
        boards-list (:boards org-data)
        loading (drv/react s :loading)]
    [:div.mobile-boards-list-container
      (if loading
        [:div.org-dashboard.main-scroll
          (rloading {:loading true})]
        [:div.mobile-boards-list
          (navbar)
          [:div.mobile-boards-list-inner
            [:div.mobile-boards-list-box
              (for [idx (range (count boards-list))
                    :let [board (get boards-list idx)]]
                [:div.mobile-boards-list-item
                  [:div.board
                    {:style {:order idx}
                     :on-click #(router/nav! (oc-urls/board (router/current-org-slug) (:slug board)))}
                    [:div.board-internal
                      [:div.board-title
                        (:name board)]]]])]]])]))