(ns open-company-web.components.all-sections
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.section-selector :refer [section-selector]]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defn sort-sections [a b company-data]
  (let [sec1 (a company-data)
        sec2 (b company-data)]
    (compare (:updated-at sec2) (:updated-at sec1))))

(defcomponent all-sections [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)
          sorted-section-keys (utils/sort-section-keys company-data)]
      (dom/div {:class "sections-container"}
        (let [merge-partial (partial merge {:data company-data})
              sections-data (into [] (map (fn [section]{:data company-data
                                               :section section})
                                 sorted-section-keys))]
          (for [idx (range 0 (count sections-data))]
            (let [section (sections-data idx)]
              (dom/div {}
                (om/build section-selector section)
                (when (not= idx (dec (count sections-data)))
                  (dom/hr {:class "section-separator"}))))))))))