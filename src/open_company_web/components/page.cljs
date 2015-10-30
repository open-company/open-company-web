(ns open-company-web.components.page
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.navbar :refer [navbar]]
            [open-company-web.components.link :refer [link]]
            [open-company-web.components.company-profile :refer [company-profile]]
            [open-company-web.router :as router]
            [open-company-web.components.section-selector :refer [section-selector]]
            [open-company-web.components.all-sections :refer [all-sections]]
            [clojure.string :as str]
            [open-company-web.lib.utils :as utils]))

(defcomponent company [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]
      (dom/div {:class "company-container container"}
        (om/build navbar company-data)
        (dom/div {:class "container-fluid"}
          (dom/div {:class "col-md-12 main"}
            (cond

              (:loading data)
              (dom/div
                (dom/h4 "Loading data..."))

              (contains? @router/path :section)
              (let [section (keyword (:section @router/path))
                    section-data (section company-data)]
                (om/build section-selector {:section-data section-data :section section}))

              (utils/in? (:route @router/path) "profile")
              (om/build company-profile data)

              (and (not (contains? data :loading)) (contains? data (keyword slug)))
              (om/build all-sections data)

              :else
              (dom/div
                (dom/h2 (str (:slug @router/path) " not found"))
                (om/build link {:href "/" :name "Back home"})))))))))