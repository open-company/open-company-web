(ns open-company-web.components.topic-list
  (:require [om.core :as om :include-macros true]
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

(defcomponent topic-list [data owner {:keys [navbar-editing-cb] :as options}]

  (init-state [_]
    {:editing false
     :new-sections-requested false})

  (did-mount [_]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner)))

  (did-update [_ _ _]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner)))

  (render [_]
    (let [slug (keyword (:slug @router/path))
          editing (om/get-state owner :editing)]
      (if editing
        (om/build topic-list-edit data {:opts {:new-sections (slug @caches/new-sections)
                                               :active-category (:active-category data)
                                               :save-sections-cb (partial save-sections-cb owner data options)
                                               :cancel-editing-cb (fn []
                                                                    (om/set-state! owner :editing false)
                                                                    (navbar-editing-cb false))}})
        (let [company-data (:company-data data)
              active-category (keyword (:active-category data))
              active-sections (get-in company-data [:sections active-category])]
          (dom/div {:class "topic-list fix-top-margin-scrolling"}
            (dom/div {:class "topic-list-internal"}
              (for [section-name active-sections]
                (dom/div {:class "topic-row"
                          :key (str "topic-row-" (name section-name))}
                  (om/build topic {:loading (:loading company-data)
                                   :company-data company-data
                                   :active-category active-category}
                                   {:opts {:section-name section-name
                                           :navbar-editing-cb navbar-editing-cb
                                           :topic-edit-cb (:topic-edit-cb options)}}))))
            (when-not (:read-only company-data)
              (om/build manage-topic {} {:opts {:manage-topic-cb #(manage-topic-cb owner options)}}))))))))