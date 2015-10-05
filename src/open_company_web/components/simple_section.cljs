(ns open-company-web.components.simple-section
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.router :as router]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer [revisions-navigator]]))

(defcomponent simple-section [data owner]
  (render [_]
    (let [company-data (:company-data data)
          section (:section data)
          section-data (section company-data)
          read-only (:read-only data)]
      (if (:loading company-data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "simple-section section"}
          (om/build editable-title {:title (:title section-data)
                                    :section section
                                    :read-only read-only})
          (dom/div {:class "simple-section-body"}
            (om/build rich-editor {:read-only read-only
                                   :section-data section-data
                                   :section section}))
          (om/build revisions-navigator (:revisions section-data)))))))