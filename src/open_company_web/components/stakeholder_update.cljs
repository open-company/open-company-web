(ns open-company-web.components.stakeholder-update
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.local-settings :as ls]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.topic-body :refer (topic-body)]
            [open-company-web.components.ui.link :refer (link)]
            [open-company-web.router :as router]
            [open-company-web.components.section-selector :refer (section-selector)]
            [clojure.string :as str]
            [open-company-web.lib.utils :as utils]))

(defn get-key-from-sections [sections]
  (clojure.string/join
    (map #(str (name (get % 0)) (clojure.string/join (get % 1))) sections)))

(defcomponent prior-updates [data owner]
  (render [_]
    (js/console.log (pr-str data))
    (dom/div "")))

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
    (let [stakeholder-update (:stakeholder-update data)
          section-keys (map keyword (:sections stakeholder-update))
          all-sections-key (get-key-from-sections (:sections data))]
      (dom/div {:class "update-sections"
                :key all-sections-key}
        (for [section section-keys]
          (let [section-data (section data)]
            (when-not (and (:read-only section-data) (:placeholder section-data))
              (om/build stakeholder-update-topic {
                                      :section-data section-data
                                      :section section
                                      :currency (:currency data)
                                      :loading (:loading data)}))))))))

(defcomponent stakeholder-update-intro [data owner]
  (render [_]
    (let [intro (:intro data)
          body (:body intro)
          title (:title intro)]
      (when-not (str/blank? body)
        (dom/div {:class "update-intro"}
          (dom/div {:class "intro-title"} (if (str/blank? title) "Current Update" title))
          (dom/div {:class "topic-body"}
            (dom/div {:class "topic-body-inner group"} body)))))))

(defcomponent stakeholder-update [data owner]
  (render [_]
    (let [slug (keyword (:slug @router/path))
          company-data (get data slug)]

      (utils/update-page-title (str "OpenCompany - " (:name company-data)))

      (cond

        ;; The data is still loading
        (:loading data)
        (dom/div
          (dom/h4 "Loading data..."))

        ;; Stakeholder update
        (and (not (contains? data :loading)) (contains? data slug))
        (dom/div {:class (utils/class-set {:stakeholder-update true
                                           :navbar-offset (not (utils/is-mobile))})}
          ;; Company / user header
          (when-not (utils/is-mobile)
            (om/build navbar data))
            
          ;; Company header
          (om/build company-header {
              :editing-topic true ; no category nav
              :company-data company-data
              :stakeholder-update true})
          
          (dom/div {:class "update-internal row"}
          
            (dom/div {:class "sections col-md-9 col-sm-12"}
              ;; Stakeholder update intro
              (om/build stakeholder-update-intro (:stakeholder-update company-data))
              ;; Stakeholder update topics
              (om/build selected-topics company-data)
              ;; Dashboard link
              (when (utils/is-mobile)
                (dom/div {:class "dashboard-link"}
                  (om/build link {:href (str "/" (:slug company-data)) :name "View Dashboard"}))))
            
            (dom/div {:class "col-md-3 col-sm-0"} 
              (om/build prior-updates company-data))))


        ;; Error fallback
        :else
        (dom/div
          (dom/h2 (str (name slug) " not found"))
          (om/build link {:href "/" :name "Back home"}))))))