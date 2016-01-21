(ns open-company-web.components.company-dashboard
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.topic-list :refer [topic-list]]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defcomponent company-dashboard [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]
      (dom/div {:class "company-dashboard"}
        (om/build company-header {:loading (:loading company-data)
                                  :company-data company-data})
        (om/build topic-list {:loading (:loading company-data)
                              :company-data company-data})))))