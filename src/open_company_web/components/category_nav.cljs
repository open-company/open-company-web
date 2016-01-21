(ns open-company-web.components.category-nav
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent category-nav [data owner]
  (render [_]
    (let [company-data (:company-data data)
          categories (:categories company-data)]
      (dom/div {:class "row category-nav"}
        (for [category categories]
          (dom/div {:class "col-xs-4 category"}
            (dom/div {:class "category-label"}
              (utils/camel-case-str (name category)))))))))