(ns open-company-web.components.dashboard
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.topic-list :refer [topic-list]]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defn get-key-from-sections [sections]
  (apply str (map #(str (name (get % 0)) (apply str (get % 1))) sections)))

(defcomponent dashboard [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)
          section-keys (utils/get-section-keys company-data)
          all-sections-key (get-key-from-sections (:sections company-data))]
      (dom/div {:class "company-dashboard"}
        (om/build company-header {:loading (:loading company-data)})
        (om/build topic-list {:loading (:loading company-data)})))))