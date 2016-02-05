(ns open-company-web.components.topic-list-edit
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.manage-topic :refer (manage-topic)]))

(defn sort-end [owner]
  (let [all-active-topic-div (sel :div.topic-sortable.active)
        resorted-sections (map #(.-sectionname (.-dataset %)) all-active-topic-div)]
    (om/set-state! owner :active-topics (vec resorted-sections))))

(defn setup-sortable [owner]
  (when (.-$ js/window)
    (.sortable (.$ js/window "div.topic-list-edit")
               #js {"axis" "y"
                    "start" #(.addClass (.-item %2) "active")
                    "stop" #(sort-end owner)})))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch))
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    {:active-topics (get-in (:company-data data) [:sections (keyword (:active-category data))])})

  (did-mount [_]
    (when-not (:read-only (:company-data data))
      (let [category-sections (om/get-state owner :active-topics)]
        (setup-sortable owner)))
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (println "save ch!" change)))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (loop []
        (let [change (<! cancel-ch)]
          ((:cancel-editing-cb options)))))))

  (render [_]
    (if (empty? @caches/new-sections)
      (dom/h2 {} "Loading sections...")
      (let [active-category (:active-category options)
            all-sections (:new-sections options)
            category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
            active-sections (om/get-state owner :active-topics)]
        (dom/div {:class "topic-list-edit fix-top-margin-scrolling group"}
          (for [section category-sections]
            (let [active (utils/in? active-sections (:name section))
                  check-src (str "/img/check_" (if active "checked" "empty") ".png?" ls/deploy-key)]
              (dom/div {:class (utils/class-set {:topic-edit true
                                                 :group true
                                                 :topic-sortable true
                                                 (str "topic-" (:name section)) true
                                                 :active active})
                        :data-sectionname (:name section)
                        :on-click (fn []
                                    (if active
                                      (om/set-state! owner :active-topics (utils/vec-dissoc active-sections (:name section)))
                                      (om/set-state! owner :active-topics (concat active-sections [(:name section)]))))
                        :key (str "topic-edit-" (:name section))}
                (dom/div {:class "topic-edit-internal group"}
                  (dom/div {:class "topic-edit-labels"}
                    (dom/h3 {:class "topic-title oc-header"} (:title section))
                    (dom/label {:class "topic-description"} (:description section)))
                  (dom/img {:class "check" :src check-src}))))))))))