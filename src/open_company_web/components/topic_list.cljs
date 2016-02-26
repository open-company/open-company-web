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

(defn save-sections-cb [owner options]
  (api/patch-sections (om/get-state owner :active-topics))
  ((:navbar-editing-cb options) false)
  (om/set-state! owner :editing false))

(defn manage-topic-cb [owner options]
  (om/set-state! owner :editing true)
  ((:navbar-editing-cb options) true)
  (utils/scroll-to-y 0))

(defn get-active-topics [company-data category]
  (get-in company-data [:sections (keyword category)]))

(defn update-active-topics [owner options category new-active-topics]
  (let [old-active-categories (om/get-state owner :active-topics)
        new-active-categories (assoc old-active-categories category new-active-topics)]
    (om/set-state! owner :active-topics new-active-categories)
    ; enable/disable save button
    ((:save-bt-active-cb options) (not= new-active-topics (om/get-state owner :initial-active-topics)))))

(defcomponent topic-list [data owner options]

  (init-state [_]
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch))
    (let [company-data (:company-data data)
          categories (:categories company-data)
          active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) categories))]
      {:editing false
       :initial-active-topics active-topics
       :active-topics active-topics
       :new-sections-requested false
       :save-bt-active false}))

  (did-mount [_]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner))
    ; save all the changes....
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (save-sections-cb owner options)))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (loop []
        (let [change (<! cancel-ch)]
          ((:navbar-editing-cb options) false)
          (om/set-state! owner :editing false))))))

  (did-update [_ _ _]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner)))

  (render-state [_ {:keys [active-topics editing]}]
    (let [slug (keyword (:slug @router/path))]
      (if editing
        (let [categories (map name (keys active-topics))]
          (dom/div {:class "topic-list-edit-container"
                    :key "topic-list-edit-container"}
            (for [cat categories]
              (om/build topic-list-edit
                        (merge data {:active (= cat (:active-category data))
                                     :category cat
                                     :active-topics (get active-topics (keyword cat))})
                        {:key cat
                         :opts {:active-category (:active-category data)
                                :new-sections (slug @caches/new-sections)
                                :did-change-sort (partial update-active-topics owner options (keyword cat))}}))))
        (let [company-data (:company-data data)
              active-category (keyword (:active-category data))
              category-topics (get active-topics active-category)]
          (dom/div {:class "topic-list fix-top-margin-scrolling"
                    :key "topic-list"}
            (dom/div {:class "topic-list-internal"}
              (for [section-name category-topics
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