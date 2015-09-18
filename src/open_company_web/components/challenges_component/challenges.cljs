(ns open-company-web.components.challenges-component.challenges
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.router :as router]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.lib.utils :as utils]))

(defcomponent challenges [data owner]
  (render [_]
    (let []
      (if (:loading data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "challenges"}
          (om/build editable-title {:title (:title (:oc:challenges (:finances data)))})
          (dom/div {:class "challenges-body"}
            (om/build rich-editor (:oc:challenges (:finances data)))))))))
