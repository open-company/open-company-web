(ns open-company-web.components.table-of-contents-item
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]))

(defcomponent table-of-contents-item [data owner]
  
  (display-name [_] (str "table-of-contents-item" (:seciton data)))
  
  (render [_]
    (let [category (:category data)
          section (:section data)]
      (dom/div {:class (str "category-sortable category-" category)
                :id (str "section-sort--" (name section))}
        (dom/div {:class "category-section"}
          (dom/div {:class "category-section-close"
                    :on-click #(api/remove-section (name section))})
          (dom/a {:href "#"
                  :on-click (fn [e]
                              (.preventDefault e)
                              (utils/scroll-to-section (name section)))}
            (dom/p {:class "section-title"} (:title data))
            (dom/p {:class "section-date"} (utils/time-since (:updated-at data))))
          (dom/div {:class "category-section-sortable"}))))))