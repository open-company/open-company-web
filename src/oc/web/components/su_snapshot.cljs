(ns oc.web.components.su-snapshot
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.topic :refer (topic)]
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
          su-data      (dis/update-data data)
          mobile?      (responsive/is-tablet-or-mobile?)
          fixed-card-width (responsive/calc-update-width (responsive/columns-num))
          title        (if (clojure.string/blank? (:title su-data))
                          (str (:org-name su-data) " Update")
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
          (when (and org-data su-data)
            (dom/div {:class "su-sp-content"
                      :style {:width (if (responsive/is-mobile-size?) "auto" (str fixed-card-width "px"))}}
              (dom/div {:class "su-sp-org-header"}
                (dom/div {:class "su-snapshot-title"} title))
              (dom/div {:class (str "topics-columns overflow-visible group" (when-not (:loading data) " content-loaded"))}
                (dom/div {:class "topics-column-container columns-1 group"
                          :style (if (responsive/is-mobile-size?)
                                   #js {:margin "0px 9px"
                                        :width "auto"}
                                   #js {:width (- fixed-card-width 60)})}
                  (dom/div {:class "topics-column"}
                    (for [entry (:entries su-data)]
                      (dom/div {:class "topic-row"}
                        (om/build topic {:loading (:loading data)
                                         :topic (:topic-slug entry)
                                         :is-stakeholder-update true
                                         :topic-data entry
                                         :entries-data []
                                         :card-width card-width
                                         :columns-num columns-num
                                         :read-only-board (:read-only org-data)
                                         :currency (:currency org-data)
                                         :editing false
                                         :foce-key nil
                                         :is-dashboard false
                                         :show-editing false
                                         :column 1}
                                         {:opts {:topic-name (name (:topic-slug entry))}
                                          :key (:slug update)}))))))
              (dom/div {:class "su-sp-footer"} "Updates by "
                (dom/a {:href "https://opencompany.com"} "OpenCompany")))))))))