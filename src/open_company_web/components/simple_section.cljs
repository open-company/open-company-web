(ns open-company-web.components.simple-section
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.router :as router]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.lib.utils :as utils]))

(defcomponent simple-section [data owner]
  (render [_]
    (let [company-data (:company-data data)
          section (:section data)
          section-kw (keyword section)]
      (if (:loading company-data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "simple-section section"}
          (om/build editable-title {:title (:title (section-kw company-data))})
          (dom/div {:class "challenges-body"}
            (om/build rich-editor (section-kw company-data))))))))
