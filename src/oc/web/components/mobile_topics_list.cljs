(ns oc.web.components.mobile-topics-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.topic :refer (topic)]
            [oc.web.components.topic-view :refer (topic-view)]))

(defcomponent mobile-topics-list [{:keys [columns-num
                                          content-loaded
                                          total-width
                                          card-width
                                          topics
                                          org-data
                                          board-data
                                          topics-data
                                          is-dashboard
                                          is-stakeholder-update
                                          prevent-topic-not-found-navigation] :as data} owner options]
  (render [_]
    (dom/div {:class (str "mobile-topics-list" (when (router/current-topic-slug) " showing-topic-view"))}
      (if (router/current-topic-slug)
        (om/build topic-view {:card-width card-width
                              :columns-num columns-num
                              :loading (:loading data)
                              :org-data org-data
                              :board-data board-data
                              :topic-data (get board-data (keyword (router/current-topic-slug)))
                              :read-only-board (:read-only board-data)
                              :prevent-topic-not-found-navigation prevent-topic-not-found-navigation})
        (for [idx (range (count topics))
              :let [topic-kw (get topics idx)
                    topic-name (name topic-kw)
                    sd (->> topic-name keyword (get board-data))
                    topic-data {:loading (:loading board-data)
                                :topic topic-kw
                                :org-data org-data
                                :board-data board-data
                                :is-stakeholder-update is-stakeholder-update
                                :topic-data sd
                                :card-width card-width
                                :read-only-board (:read-only board-data)
                                :is-dashboard is-dashboard
                                :topic-flex-num idx}]]
          (when-not (:placeholder sd)
            (dom/div {:class "topics-mobile-item" :style {:width (str card-width "px")}}
              (om/build topic topic-data))))))))