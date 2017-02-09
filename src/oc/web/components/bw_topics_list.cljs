(ns oc.web.components.bw-topics-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [cuerdas.core :as s]))

(defn patch-board [topics-list]
  (js.console.log "patch-board" topics-list)
  (api/patch-company (router/current-board-slug) {:topics topics-list}))

; (defn ordered-topics-list
;   "Return the list of active topics in the order the user moved them."
;   []
;   (let [topics (sel [:div.left-topics-list-items :div.left-topics-list-item])
;         topics-list (for [topic topics] (.data (js/jQuery topic) "topic"))]
;     (vec (remove nil? topics-list))))

; (defn setup-sortable
;   "Setup the jQuery UI Sortable on the create-update-topics-list div"
;   [owner]
;   (when-let [list-node (js/jQuery "div.left-topics-list-items")]
;     (-> list-node
;       (.sortable #js {:scroll false
;                       :forcePlaceholderSize true
;                       :items ".left-topics-list-item"
;                       :stop (fn [event ui]
;                               (let [topics-list (ordered-topics-list)]
;                                 (om/set-state! owner :topics topics-list)
;                                 (patch-board topics-list)))
;                       :axis "y"})
;       (.disableSelection))))

; (defn destroy-sortable
;   "Destry the sortable to make sure the user doesn't DnD while looking a topic or adding a topic"
;   []
;   (.sortable (js/jQuery "div.left-topics-list-items") "destroy"))

; (defn can-dnd? [data]
;   (and (not (:read-only (:board-data data)))
;        (nil? (:show-add-topic data))
;        (nil? (:selected-topic-view data))))

(defn get-topics [data]
  (let [board-data (:board-data data)]
    (if (:read-only board-data)
      (utils/filter-placeholder-topics (:topics board-data) board-data)
      (:topics board-data))))

(defn sorted-boards [boards]
  (into [] (sort #(compare (utils/js-date (:created-at %1)) (utils/js-date (:created-at %2))) boards)))

(defcomponent bw-topics-list [{:keys [board-data card-width selected-topic-view show-add-topic] :as data} owner options]

  (init-state [_]
    {:topics (get-topics data)})

  ; (did-mount [_]
  ;   (when (can-dnd? data)
  ;     (setup-sortable owner)))

  ; (will-update [_ next-props next-state]
  ;   (om/set-state! owner :topics (get-topics next-props))
  ;   (when (and (can-dnd? data) (not (can-dnd? next-props)))
  ;     (destroy-sortable))
  ;   (when (and (not (can-dnd? data)) (can-dnd? next-props))
  ;     (setup-sortable owner)))

  (render-state [_ {:keys [topics]}]
    (dom/div {:class "left-topics-list group" :style {:width (str responsive/left-topics-list-width "px")}}
      (dom/div {:class "left-topics-list-top group"}
        (dom/h3 {:class "left-topics-list-top-title"} "BOARDS"))
      (let [org-data (dis/org-data)]
        (dom/div {:class (str "left-topics-list-items group")}
          (for [board (sorted-boards (:boards org-data))]
            (dom/div {:class (utils/class-set {:left-topics-list-item true
                                               :highlight-on-hover (nil? (:foce-key data))
                                               :group true
                                               :selected (= (router/current-board-slug) (:slug board))})
                      :style {:width (str (- responsive/left-topics-list-width 5) "px")}
                      :data-board (name (:slug board))
                      :key (str "bw-board-list-" (name (:slug board)))
                      :on-click #(when (nil? (:foce-key data))
                                   (router/nav! (oc-urls/board (router/current-org-slug) (:slug board))))}
              (dom/div {:class "internal"
                        :key (str "bw-board-list-" (name (:slug board)) "-internal")}
                (str "#" (or (:name board) (:slug board))))))))
      (dom/div {:class "left-topics-list-top mt3 group"}
        (when (not= (count (:topics board-data)) 0)
          (dom/h3 {:class "left-topics-list-top-title"
                   :on-click #(when (nil? (:foce-key data))
                                (dis/dispatch! [:show-add-topic false])
                                (router/nav! (oc-urls/board)))} "TOPICS"))
        (when (and (not (responsive/is-tablet-or-mobile?))
                   (not show-add-topic)
                   (not (:read-only board-data)))
          (dom/button {:class "left-topics-list-top-title btn-reset right"
                       :on-click #(when (nil? (:foce-key data))
                                    (dis/dispatch! [:show-add-topic true]))
                       :title "Add a topic"
                       :data-placement "top"
                       :data-toggle "tooltip"
                       :data-container "body"}
            (dom/i {:class "fa fa-plus-circle"}))))
      (dom/div {:class (str "left-topics-list-items group" (when (:read-only board-data) " read-only"))}
        (for [topic topics
              :let [sd (->> topic keyword (get board-data))]
              :when (contains? board-data (keyword topic))]
          (dom/div {:class (utils/class-set {:left-topics-list-item true
                                             :dnd false ;(can-dnd? data)
                                             :highlight-on-hover (nil? (:foce-key data))
                                             :group true
                                             :selected (= selected-topic-view topic)})
                    :style {:width (str (- responsive/left-topics-list-width 5) "px")}
                    :data-topic (name topic)
                    :key (str "bw-topic-list-" (name topic))
                    :on-click #(when (nil? (:foce-key data))
                                 (router/nav! (oc-urls/topic (router/current-org-slug) (router/current-board-slug) (name topic))))}
            (dom/div {:class "internal"
                      :key (str "bw-topic-list-" (name topic) "-internal")}
              (or (:title sd) (s/join " " (map s/capital (s/split (name topic) "-")))))))))))