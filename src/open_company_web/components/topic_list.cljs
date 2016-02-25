(ns open-company-web.components.topic-list
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.caches :as caches]
            [open-company-web.api :as api]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.topic-list-edit :refer (topic-list-edit)]
            [open-company-web.components.manage-topic :refer (manage-topic)]))

(defn get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)]
      (when (and (empty? (slug @caches/new-sections)) (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (api/get-new-sections)))))

(defn save-sections-cb [owner data options new-sections]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        active-category (keyword (:active-category data))
        old-active-sections (get-in company-data [:sections active-category])
        remaining-categories (utils/vec-dissoc categories (name active-category))
        remaining-sections (apply merge
                                  (map #(hash-map (keyword %) ((keyword %) (:sections company-data)))
                                       remaining-categories))
        all-sections (assoc remaining-sections active-category new-sections)]
    (api/patch-sections all-sections)
    ((:navbar-editing-cb options) false)
    (om/set-state! owner :editing false)))

(defn manage-topic-cb [owner options]
  (om/set-state! owner :editing true)
  ((:navbar-editing-cb options) true)
  (utils/scroll-to-y 0))

(defcomponent topic-list [data owner options]

  (init-state [_]
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch))
    {:editing false
     :new-sections-requested false
     :save-bt-active false})

  (did-mount [_]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner))
    ; save all the changes....
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (let [active-topics (om/get-state owner :active-topics)]
            ((:save-sections-cb options) active-topics))))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (loop []
        (let [change (<! cancel-ch)]
          ((:cancel-editing-cb options)))))))

  (did-update [_ _ _]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner)))

  (render [_]
    (let [slug (keyword (:slug @router/path))
          editing (om/get-state owner :editing)]
      (if editing
        (let [categories (vec (map :name (:categories (slug @caches/new-sections))))]
          (dom/div {:class "topic-list-edit-container"
                    :key "topic-list-edit-container"}
            (for [cat categories]
              (om/build topic-list-edit (assoc data :active (= cat (:active-category data)))
                        {:key cat
                         :opts {:new-sections (slug @caches/new-sections)
                                :active-category cat
                                :save-sections-cb (partial save-sections-cb owner data options)
                                :save-bt-active-cb (fn [active]
                                                     (om/set-state! owner :save-bt-active (or (om/get-state owner :save-bt-active) active))
                                                     ((:save-bt-active-cb options) (om/get-state owner :save-bt-active)))
                                :cancel-editing-cb (fn []
                                                     (om/set-state! owner :editing false)
                                                     ((:navbar-editing-cb options) false))}}))))
        (let [company-data (:company-data data)
              active-category (keyword (:active-category data))
              active-sections (get-in company-data [:sections active-category])]
          (dom/div {:class "topic-list fix-top-margin-scrolling"
                    :key "topic-list"}
            (dom/div {:class "topic-list-internal"}
              (for [section-name active-sections
                    :let [sd (->> section-name keyword (get company-data))]]
                (dom/div {:class "topic-row"
                          :key (str "topic-row-" (name section-name))}
                  (when-not (and (:read-only company-data) (:placeholder sd))
                    (om/build topic {:loading (:loading company-data)
                                     :section section-name
                                     :section-data (get company-data (keyword section-name))
                                     :currency (:currency company-data)
                                     :active-category active-category}
                                     {:opts {:section-name section-name}})))))
            (when (and (not (:read-only company-data)) (seq company-data))
              (om/build manage-topic {} {:opts {:manage-topic-cb #(manage-topic-cb owner options)}}))))))))