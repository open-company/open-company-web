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
    (put! ch {:category category :section section :section-defaults (dissoc section-body :name :core :image)})))

(defn filter-sections [old-sections new-sections]
  (filter #(not (utils/in? old-sections (:name %)))
          new-sections))

(defn filter-categories [old-categories new-categories]
  (vec (for [category new-categories]
         (let [category-kw (keyword (:name category))]
           (merge category
                  {:sections (filter-sections (category-kw old-categories) (:sections category))})))))

(def icons-path "/img/section-icons/")

(def icons-list [
  ; progress
  "progress-update"
  "progress-highlights"
  "progress-growth"
  "progress-challenges"
  "progress-team"
  "progress-product"
  "progress-customer-service"
  "progress-marketing"
  "progress-press"
  "progress-sales"
  "progress-business-development"
  "progress-help"
  "progress-kudos"
  ; company
  "company-diversity"
  "company-values"
  "company-privacy"
  "company-mission"
  ; financial
  "financial-finances"
  "financial-compensation"
  "financial-ownership"])

(defn get-section-image [category section]
  (let [nm (str category "-" section)]
    (if (utils/in? icons-list nm)
      (str icons-path nm ".svg")
      (str icons-path "placeholder.svg"))))

(def class-counter (atom -1))

(defn get-color-class []
  (swap! class-counter (fn [v] (mod (inc v) 3)))
  (case @class-counter
    0 "green"
    1 "darkblue"
    2 "lightblue"))

(defn replace-svg []
  (when (.-svgcss js/window)
    (.setTimeout js/window #(.svgcss js/window) 1)))

(defcomponent new-section-popover [data owner]
  (did-mount [_]
    (replace-svg))
  (did-update [_ _ _]
    (replace-svg))
  (render [_]
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)
          old-sections (:sections company-data)
          sections (filter-categories old-sections (:categories data))]
      (dom/div {:class "new-section-popover"}
        (dom/div {:class "new-section-popover-close"
                  :on-click #(put! (utils/get-channel "close-new-section-popover") {})})
        (dom/img {:src "/img/add_new_section_icon.png" :width "80" :height "82"})
        (dom/h4 {} "Add a new section")
        (dom/div {:class "new-section-container"}
          (dom/div {:class "gradient top-gradient"})
          (for [category sections]
            (when (pos? (count (:sections category)))
              (dom/div {:class "new-section-category"}
                (dom/h3 {} (:title category))
                (dom/div {:class "new-section-category-sections"}
                  (for [section (:sections category)]
                    (let [color-class (get-color-class)
                          section-name (:name section)
                          category-name (:name category)
                          section-uniq (str category-name "-" section-name)
                          section-img-class (str section-uniq "-icon")
                          section-inner-class (str section-uniq "-inner")]
                      (dom/div {:class "new-section-category-section"}
                        (dom/a {:href ""
                                :on-click #(add-section % category-name section-name section)}
                          (dom/div {:class "section-icon-container"}
                            (dom/div {:class (utils/class-set {:section-icon-inner true
                                                               section-inner-class true
                                                               color-class true
                                                               :icon-placeholder (not (utils/in? icons-list section-uniq))})}
                              (dom/img {:class (str "section-placeholder svg " section-img-class)
                                        :src (get-section-image category-name section-name)})))
                          (dom/div {:class "section-details"}
                            (dom/h4 {:class "section-title"} (:title section))
                            (dom/p {:class "section-description"} (:description section))))))))))))
        (dom/div {:class "gradient bottom-gradient"})))))