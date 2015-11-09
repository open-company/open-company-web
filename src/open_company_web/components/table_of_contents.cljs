(ns open-company-web.components.table-of-contents
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defcomponent table-of-contents [data owner]
  (render [_]
    (let [sections (:sections data)
          categories (:categories data)]
      (dom/div #js {:className "table-of-contents" :ref "table-of-contents"}
        (dom/div {:class "table-of-contents-inner"}
          (for [category categories]
            (dom/div {:class "category-container"}
              (dom/div {:class (utils/class-set {:category true :empty (zero? (count ((keyword category) sections)))})} (dom/h3 (utils/camel-case-str (name category))))
              (for [section (into [] (get sections (keyword category)))]
                (let [section-data ((keyword section) data)]
                  (dom/div {:class "category-section"}
                    (dom/a {:href "#"
                            :on-click (fn [e]
                                        (.preventDefault e)
                                        (utils/scroll-to-section (name section)))}
                      (dom/p {:class "section-title"} (:title section-data))
                      (dom/p {:class "section-date"} (utils/time-since (:updated-at section-data))))))))))))))