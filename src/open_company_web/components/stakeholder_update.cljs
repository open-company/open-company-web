(ns open-company-web.components.stakeholder-update
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.ui.link :refer (link)]
            [open-company-web.router :as router]
            [open-company-web.components.section-selector :refer (section-selector)]
            [clojure.string :as str]
            [open-company-web.lib.utils :as utils]))

(defn get-key-from-sections [sections]
  (clojure.string/join
    (map #(str (name (get % 0)) (clojure.string/join (get % 1))) sections)))

(defcomponent selected-topics [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          stakeholder-update (:stakeholder-update data)
          section-keys (map keyword (:sections stakeholder-update))
          all-sections-key (get-key-from-sections (:sections data))]
      (dom/div {:class "sections-container"
                :key all-sections-key}
        (for [section section-keys]
          (let [section-data (section data)]
            (when-not (and (:read-only section-data) (:placeholder section-data))
              (dom/div {}
                (om/build section-selector {:section-data section-data
                                            :section section
                                            :currency (:currency data)
                                            :loading (:loading data)})
                (when (not= section (last section-keys))
                    (dom/hr {:class "section-separator" :size "0"}))))))))))

(defcomponent stakeholder-update [data owner]
  (render [_]
    (let [slug (keyword (:slug @router/path))
          company-data (get data slug)]

      (utils/update-page-title (str "OpenCompany - " (:name company-data)))

      (dom/div {:class "company-container container"}

        ;; Company / user header
        (when-not (utils/is-mobile)
          (om/build navbar data))

        (dom/div {:class "navbar-offset container-fluid"}

          ;; White space
          (dom/div {:class "col-md-1"})

          (dom/div {:class "col-md-7 main"}
            
            (cond

              ;; The data is still loading
              (:loading data)
              (dom/div
                (dom/h4 "Loading data..."))

              ;; Stakeholder update
              (and (not (contains? data :loading)) (contains? data slug))
              (dom/div {:class "update-container"}
                ;; company header
                (om/build company-header {
                    :loading (:loading company-data)
                    :editing-topic true ; no categor nav
                    :company-data company-data})
                (om/build selected-topics company-data))

              ;; Error fallback
              :else
              (dom/div
                (dom/h2 (str (name slug) " not found"))
                (om/build link {:href "/" :name "Back home"})))))))))