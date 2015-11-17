(ns open-company-web.components.new-section-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]))

(defn add-section [e category section]
  (.preventDefault e)
  (utils/get-channel "add-section"))

(defcomponent new-section-popover [data owner]
  (init-state [_]
    (api/get-new-sections)
    {})
  (render [_]
    (let [sections (:new-section data)]
      (dom/div {:class "new-section-popover"}
        (dom/img {:src "/img/add_new_section_icon.png" :width "80" :height "82"})
        (dom/h4 {} "Add a new section")
        (dom/div {:class "new-section-container"}
          (for [category (:categories sections)]
            (when (pos? (count (:sections category)))
              (dom/div {:class "new-section-category"}
                (dom/h3 {} (:title category))
                (dom/div {:class "new-section-category-sections"}
                  (for [section (:sections category)]
                    (dom/div {:class "new-section-category-section"}
                      (dom/a {:href "" :on-click #(add-section % (:name category) (:name section))}
                      (dom/h4 {:class "section-title"} (:title section))
                      (dom/p {:class "section-description"} (:description section))))))))))))))