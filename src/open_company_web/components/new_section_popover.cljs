(ns open-company-web.components.new-section-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [cljs.core.async :refer (put!)]))

(defn add-section [e category section section-body]
  (.preventDefault e)
  (let [ch (utils/get-channel "add-section")]
    (put! ch {:category category :section section :section-defaults (dissoc section-body :name :core)})))

(defn filter-sections [old-sections new-sections]
  (filter #(not (utils/in? old-sections (:name %)))
          new-sections))

(defn filter-categories [old-categories new-categories]
  (vec (for [category new-categories]
         (let [category-kw (keyword (:name category))]
           (merge category
                  {:sections (filter-sections (category-kw old-categories) (:sections category))})))))

(defcomponent new-section-popover [data owner]
  (init-state [_]
    (api/get-new-sections)
    {})
  (render [_]
    (let [slug (keyword (:slug @router/path))
          company-data (slug data)
          old-sections (:sections company-data)
          sections (filter-categories old-sections (:categories (:new-section data)))]
      (dom/div {:class "new-section-popover"}
        (dom/img {:src "/img/add_new_section_icon.png" :width "80" :height "82"})
        (dom/h4 {} "Add a new section")
        (dom/div {:class "new-section-container"}
          (for [category sections]
            (when (pos? (count (:sections category)))
              (dom/div {:class "new-section-category"}
                (dom/h3 {} (:title category))
                (dom/div {:class "new-section-category-sections"}
                  (for [section (:sections category)]
                    (dom/div {:class "new-section-category-section"}
                      (dom/a {:href "" :on-click #(add-section % (:name category) (:name section) section)}
                      (dom/h4 {:class "section-title"} (:title section))
                      (dom/p {:class "section-description"} (:description section))))))))))))))