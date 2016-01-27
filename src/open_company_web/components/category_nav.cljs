(ns open-company-web.components.category-nav
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (put!)]
            [open-company-web.lib.utils :as utils]))

(defn change-category [category-name]
  (let [ch (utils/get-channel "change-category")]
    (put! ch category-name)))

(defcomponent category-nav [data owner]
  (render [_]
    (let [company-data (:company-data data)
          categories (:categories company-data)
          active-category (:active-category data)]
      (dom/div {:class "row category-nav"}
        (for [category categories]
          (let [category-name (name category)
                category-class (str "col-xs-4 category" (if (= active-category category-name) " active" ""))]
            (dom/a {:on-click #(change-category category-name)}
              (dom/div {:class category-class}
                (dom/div {:class "category-label"}
                  (utils/camel-case-str category-name))))))))))