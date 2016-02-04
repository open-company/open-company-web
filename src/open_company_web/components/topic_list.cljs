(ns open-company-web.components.topic-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.caches :as caches]
            [open-company-web.api :as api]
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

(defcomponent topic-list [data owner]

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
                                               :active-category (:active-category data)}})
        (let [company-data (:company-data data)
              active-category (keyword (:active-category data))
              active-sections (get-in company-data [:sections active-category])]
          (dom/div {:class "topic-list fix-top-margin-scrolling"}
            (for [section-name active-sections]
              (dom/div {:class "topic-row"
                        :key (str "topic-row-" (name section-name))}
                (om/build topic {:loading (:loading company-data)
                                 :company-data company-data
                                 :active-category active-category}
                                 {:opts {:section-name section-name}})))
            (when-not (:read-only company-data)
              (om/build manage-topic {} {:opts {:manage-topic-cb #(do
                                                                    (om/set-state! owner :editing true)
                                                                    (.animate (.$ js/window js/window) #js {"scrollTop" "0px"}))}}))
            ))))))