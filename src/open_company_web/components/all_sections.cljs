(ns open-company-web.components.all-sections
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.section-selector :refer [section-selector]]
            [open-company-web.router :as router]))

(defn sort-sections [a b company-data]
  (let [sec1 (a company-data)
        sec2 (b company-data)]
    (compare (:updated-at sec2) (:updated-at sec1))))

(defcomponent all-sections [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)
          section-names (:sections company-data)
          kw-section-names (map keyword section-names)
          sorted-sections (sort #(sort-sections %1 %2 company-data) kw-section-names)]
      (dom/div {:class "sections-container"}
        (let [merge-partial (partial merge {:data company-data})
              sections-data (map #(merge {:data company-data :section %}) sorted-sections)]
          (om/build-all section-selector sections-data))))))