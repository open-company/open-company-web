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

(defn get-topics [data]
  (let [board-data (:board-data data)]
    (if (:read-only board-data)
      (utils/filter-placeholder-topics (:topics board-data) board-data)
      (:topics board-data))))

(defn sorted-boards [boards]
  (into [] (sort #(compare (utils/js-date (:created-at %1)) (utils/js-date (:created-at %2))) boards)))

(defcomponent bw-topics-list [{:keys [org-data board-data card-width show-add-topic] :as data} owner options]

  (init-state [_]
    {:topics (get-topics data)})

  (did-update [_ prev-props _]
    (when (and (nil? (:create-board prev-props))
               (not (nil? (om/get-props owner :create-board))))
      (utils/after 100 #(.focus (js/$ "input.board-name"))))
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (render-state [_ {:keys [topics]}]
    (when-not (:dashboard-sharing data)
      (dom/div {:class "left-topics-list group" :style {:width (str responsive/left-topics-list-width "px")}}
        (dom/div {:class "left-topics-list-top group"}
          (dom/h3 {:class "left-topics-list-top-title"} "BOARDS")
          (when (and (not (responsive/is-tablet-or-mobile?))
                     (utils/link-for (:links org-data) "create"))
            (dom/button {:class "left-topics-list-top-title btn-reset right"
                         :on-click #(when (nil? (:foce-key data))
                                      (dis/dispatch! [:input [:create-board] ""]))
                         :title "Create a new board"
                         :data-placement "top"
                         :data-toggle "tooltip"
                         :data-container "body"}
              (dom/i {:class "fa fa-plus-circle"}))))
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
                (str "#" (or (:name board) (:slug board))))))
          (when-not (nil? (:create-board data))
            (dom/div {:class "left-topics-list-item group"}
              (dom/span {:class "left"} "#")
              (dom/input {:class "board-name left"
                          :value (:create-board data)
                          :data-toggle "tooltip"
                          :data-placement "right"
                          :data-container "body"
                          :title "Press Enter to submit and Escape to cancel."
                          :on-change #(dis/dispatch! [:input [:create-board] (.. % -target -value)])
                          :on-blur #(when (s/blank? (:create-board data))
                                      (dis/dispatch! [:input [:create-board] nil]))
                          :on-key-up (fn [e]
                                          (cond
                                            (= "Enter" (.-key e))
                                            (dis/dispatch! [:create-board])
                                            (= "Escape" (.-key e))
                                            (dis/dispatch! [:input [:create-board] nil])))}))))))))