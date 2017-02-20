(ns oc.web.components.su-snapshot
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.topics-columns :refer (topics-columns)]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [cljsjs.hammer]))

(defcomponent su-snapshot [data owner options]

  (init-state [_]
    {:columns-num (responsive/columns-num)
     :prior-list (:list (router/query-params))})

  (did-mount [_]
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num))))

  (render-state [_ {:keys [columns-num prior-list]}]

    (let [org-data (dis/org-data data)
          su-data      (dis/stakeholder-update-data)
          mobile?      (responsive/is-tablet-or-mobile?)
          fixed-card-width (responsive/calc-update-width (responsive/columns-num))
          title        (if (clojure.string/blank? (:title su-data))
                          (str (:name org-data) " Update")
                          (:title su-data))
          card-width   (responsive/calc-card-width)]
      (dom/div {:class (utils/class-set {:su-snapshot true
                                         :main-scroll true
                                         :group true
                                         :has-navbar (not prior-list)})}
        (dom/div {:class "page"}
          (when (and org-data
                     (not prior-list))
            ;; Navbar
            (om/build navbar {:card-width card-width
                              :header-width (responsive/total-layout-width-int card-width columns-num)
                              :columns-num columns-num
                              :org-data org-data
                              :foce-key nil
                              :show-share-su-button false
                              :mobile-menu-open false
                              :su-navbar true
                              :hide-right-menu true}))

          ; closing X
          (when (and prior-list mobile?)
            (dom/div {:class "su-mobile-navigation"
                      :on-click #(.back (.-history js/window))}
              (dom/div {:class "su-mobile-navigation-title"})
              (dom/button {:class "btn-reset close-su-btn"}
                (dom/i {:class "fa fa-angle-left"}))))

          ;; SU Snapshot
          (when org-data
            (dom/div {:class "su-sp-content"
                      :style {:width (if (responsive/is-mobile-size?) "auto" (str fixed-card-width "px"))}}
              (dom/div {:class "su-sp-org-header"}
                (dom/div {:class "su-snapshot-title"} title))
              (om/build topics-columns {:columns-num 1
                                        :card-width (- fixed-card-width 60)
                                        :total-width (- fixed-card-width 60)
                                        :content-loaded (not (:loading data))
                                        :topics (:entries su-data)
                                        :topics-data (:entries su-data)
                                        :org-data org-data
                                        :hide-add-topic true
                                        :is-stakeholder-update true})
              (dom/div {:class "su-sp-footer"} "Updates by "
                (dom/a {:href "https://opencompany.com"} "OpenCompany")))))))))