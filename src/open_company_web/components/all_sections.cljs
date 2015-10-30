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
          section-keys (utils/get-section-keys company-data)]
      (dom/div {:class "sections-container"}
        (for [section section-keys]
          (let [section-data (section company-data)]
            (dom/div {}
              (om/build section-selector {:section-data section-data
                                          :section section
                                          :loading (:loading company-data)})
              (when (not= section (last section-keys))
                (dom/hr {:class "section-separator" :size "0"})))))))))