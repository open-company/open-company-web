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

(defn get-topics [owner]
  (let [company-data (om/get-props owner :company-data)]
    (if (:read-only company-data)
      (utils/filter-placeholder-sections (:sections company-data) company-data)
      (:sections company-data))))

(defcomponent bw-topics-list [{:keys [company-data card-width selected-topic-view show-add-topic] :as data} owner options]

  (init-state [_]
    {:topics (get-topics owner)})

  (did-mount [_]
    (when (can-dnd?)
      (setup-sortable owner)))

  (did-update [_ _ _]
    (om/set-state! owner :topics (get-topics owner))
    (when (can-dnd?)
      (setup-sortable owner)))

  (render-state [_ {:keys [topics]}]
    (dom/div {:class "left-topics-list group" :style {:width (str responsive/left-topics-list-width "px")}}
      (dom/div {:class "left-topics-list-top group"}
        (when-not (= (count (:sections company-data)) 0)
          (dom/h3 {:class "left-topics-list-top-title left"
                   :on-click #(do
                                (dis/dispatch! [:show-add-topic false])
                                (router/nav! (oc-urls/company)))} "TOPICS"))
        (when (and (not show-add-topic)
                   (not (:read-only company-data)))
          (dom/button {:class "left-topics-list-top-title btn-reset right"
                       :on-click #(dis/dispatch! [:show-add-topic true])
                       :title "Add a topic"
                       :data-placement "top"
                       :data-toggle "tooltip"
                       :data-container "body"}
            (dom/i {:class "fa fa-plus-circle"}))))
      (dom/div {:class (str "left-topics-list-items group" (when (:read-only company-data) " read-only"))}
        (for [topic topics
              :let [sd (->> topic keyword (get company-data))]]
          (dom/div {:class (utils/class-set {:left-topics-list-item true
                                             :dnd (can-dnd?)
                                             :group true
                                             :selected (= selected-topic-view topic)})
                    :style {:width (str (- responsive/left-topics-list-width 5) "px")}
                    :data-topic (name topic)
                    :key (str "bw-topic-list-" (name topic))
                    :on-click #(router/nav! (oc-urls/company-section (router/current-company-slug) (name topic)))}
            (dom/div {:class "internal"
                      :key (str "bw-topic-list-" (name topic) "-internal")}
              (:title sd))))))))