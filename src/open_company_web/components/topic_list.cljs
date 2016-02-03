(ns open-company-web.components.topic-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.add-topic :refer (add-topic)]))

(defcomponent topic-list [data owner]

  (render [_]
    (let [company-data (:company-data data)
          active-category (keyword (:active-category data))
          active-sections (get-in company-data [:sections active-category])]
      (dom/div {:class "topic-list"}
        (for [section-name active-sections]
          (dom/div {:class "topic-row"
                    :key (str "topic-row-" (name section-name))}
            (om/build topic {:loading (:loading company-data)
                             :company-data company-data
                             :active-category active-category}
                             {:opts {:section-name section-name}})))
        (when-not (:read-only company-data)
          (om/build add-topic {} {:opts {:add-topic-cb #(println "add topic click!")}}))
        ))))