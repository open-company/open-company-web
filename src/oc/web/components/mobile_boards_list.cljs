(ns oc.web.components.mobile-boards-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.loading :refer (loading)]))

(defcomponent mobile-boards-list [data owner options]
  (render [_]
    (let [org-data (dis/org-data data)
          card-width (responsive/mobile-dashboard-card-width)
          columns-num (responsive/dashboard-columns-num)
          total-width-int (responsive/total-layout-width-int card-width columns-num)
          boards-list (:boards org-data)]
      (dom/div {:class "mobile-boards-container"}
        (if (:loading data)
          (dom/div {:class (utils/class-set {:org-dashboard true
                                             :main-scroll true})}
            (om/build loading {:loading true}))
          (dom/div {:class "mobile-boards"}
            (om/build navbar {:org-data org-data
                              :card-width card-width
                              :header-width total-width-int
                              :columns-num columns-num
                              :show-share-su-button false
                              :show-login-overlay (:show-login-overlay data)
                              :mobile-menu-open (:mobile-menu-open data)
                              :auth-settings (:auth-settings data)
                              :active :dashboard
                              :is-dashboard (not (router/current-topic-slug))})
            (dom/div {:class "mobile-boards-inner"}
              (dom/div {:class "mobile-boards-list"}
                (for [idx (range (count boards-list))
                      :let [board (get boards-list idx)]]
                  (dom/div {:class "mobile-boards-item"
                            :style {:width (str card-width "px")}}
                    (dom/div {:class "board"
                              :style {:order idx}
                              :on-click #(router/nav! (oc-urls/board (router/current-org-slug) (:slug board)))}
                      (dom/div {:class "board-internal"}
                        (dom/div {:class "board-title"}
                          (:name board))))))))
            (footer total-width-int)))))))