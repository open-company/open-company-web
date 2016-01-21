(ns open-company-web.components.category-nav
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent category-nav [data owner options]
  (render [_]
    (let [company-data (:company-data data)
          categories (:categories company-data)
          active-category (:active-category options)]
      (dom/div {:class "row category-nav"}
        (for [category categories]
          (let [category-name (name category)
                category-class (str "col-xs-4 category" (if (= active-category category-name) " active" ""))]
            (dom/div {:class category-class}
              (dom/div {:class "category-label"}
                (utils/camel-case-str category-name)))))))))