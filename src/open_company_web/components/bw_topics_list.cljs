(ns open-company-web.components.bw-topics-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]))

(defn patch-company [topics-list]
  (api/patch-company (router/current-company-slug) {:sections topics-list}))

(defn ordered-topics-list
  "Return the list of active topics in the order the user moved them."
  []
  (let [topics (sel [:div.left-topics-list-items :div.left-topics-list-item])
        topics-list (for [topic topics] (.data (js/jQuery topic) "topic"))]
    (vec (remove nil? topics-list))))

(defn setup-sortable
  "Setup the jQuery UI Sortable on the create-update-topics-list div"
  [owner]
  (when-let [list-node (js/jQuery "div.left-topics-list-items")]
    (-> list-node
      (.sortable #js {:scroll false
                      :forcePlaceholderSize true
                      :items ".left-topics-list-item"
                      :stop (fn [event ui]
                              (let [topics-list (ordered-topics-list)]
                                (om/set-state! owner :topics topics-list)
                                (patch-company topics-list)))
                      :axis "y"})
      (.disableSelection))))

(defn can-dnd? []
  (not (:read-only (dis/company-data))))

(defcomponent bw-topics-list [{:keys [company-data card-width selected-topic-view] :as data} owner options]

  (init-state [_]
    {:topics (:sections company-data)})

  (did-mount [_]
    (when (can-dnd?)
      (setup-sortable owner)))

  (did-update [_ _ _]
    (om/set-state! owner :topics (:sections company-data))
    (when (can-dnd?)
      (setup-sortable owner)))

  (render [_]
    (dom/div {:class "left-topics-list group" :style {:width (str responsive/left-topics-list-width "px")}}
      (dom/div {:class "left-topics-list-top group"}
        (dom/h3 {:class "left-topics-list-top-title left"
                 :on-click #(router/nav! (oc-urls/company))} "TOPICS")
        ; Temp comment this waiting for add topic to work properly
        ; (dom/button {:class "left-topics-list-top-title btn-reset right"} "+")
        )
      (dom/div {:class "left-topics-list-items group"}
        (for [topic (:sections company-data)
              :let [sd (->> topic keyword (get company-data))]]
          (dom/div {:class (utils/class-set {:left-topics-list-item true
                                             :dnd (can-dnd?)
                                             :group true
                                             :selected (= selected-topic-view topic)})
                    :data-topic (name topic)
                    :on-click #(router/nav! (oc-urls/company-section (router/current-company-slug) (name topic)))}
            (dom/div {:class "internal"}
              (:title sd))))))))