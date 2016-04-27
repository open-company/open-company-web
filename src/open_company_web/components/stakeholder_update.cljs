(ns open-company-web.components.stakeholder-update
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.topic-body :refer (topic-body)]
            [open-company-web.components.company-header :refer [company-header]]))


(defn get-key-from-sections [sections]
  (clojure.string/join
    (map #(str (name (get % 0)) (clojure.string/join (get % 1))) sections)))

(defcomponent stakeholder-update-topic [data owner]
  (render [_]
    (let [section (:section data)
          section-data (:section-data data)
          headline (:headline section-data)]
      (dom/div {:class "update-topic"}

        ;; topic title
        (dom/div {:class "topic-title"} (:title section-data))

        ;; topic headline
        (when headline
          (dom/div {:class "topic-headline"} headline))
        
        ;; topic body
        (om/build topic-body {:section section
                              :section-data section-data
                              :currency (:currency data)
                              :expanded true})))))

(defcomponent selected-topics [data owner]

  (render [_]
    (let [su-data (:su-data data)
          section-keys (map keyword (:sections su-data))
          all-sections-key (get-key-from-sections (:sections su-data))]
      (dom/div {:class "update-sections"
                :key all-sections-key}

        (dom/div {:class "update-sections-internal"}
          (dom/div {:class "update-sections-internal-width"}
            (dom/div {:class "overlay"})
            (for [section section-keys]
              (let [section-data (section su-data)]
                (when-not (and (:read-only section-data) (:placeholder section-data))
                  (om/build stakeholder-update-topic {
                                          :section-data section-data
                                          :section section
                                          :currency (:currency su-data)
                                          :loading (:loading data)}))))))))))

(defcomponent stakeholder-update [data owner]
  (render [_]
    (let [slug (keyword (:slug @router/path))
          su-slug (keyword (:update-slug @router/path))
          su-key (dispatcher/stakeholder-update-key slug)
          su-data (get-in data [su-key su-slug])]
      (if (:loading su-data)
        (dom/h2 {} "Loading...")
        (dom/div {:class (utils/class-set {:stakeholder-update true
                                           :navbar-offset (not (utils/is-mobile))})}
          ;; Company / user header
          (when-not (utils/is-mobile)
            (om/build navbar data))

          ;; Company header
          (om/build company-header {
              :editing-topic true ; no category nav
              :company-data su-data
              :stakeholder-update true})
          
          (dom/div {:class "stakeholder-update-internal"}
            (dom/div {:class "sections group"}; col-md-9 col-sm-12"}
              ;; Stakeholder update header
              (dom/div {:class "stakeholder-update-header"}
                (dom/div {:class "title"} (:title su-data))
                (dom/div {:class "intro"
                          :dangerouslySetInnerHTML (clj->js {"__html" (:body (:intro su-data))})}))
              ;; Stakeholder update topics
              (om/build selected-topics {:su-data su-data})
              ;; Stakeholder update footer
              (dom/div {:class "stakeholder-update-footer"}
                (dom/div {:class "outro"
                          :dangerouslySetInnerHTML (clj->js {"__html" (:body (:outro su-data))})})))))))))