(ns open-company-web.components.page
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.navbar :refer [navbar]]
            [open-company-web.components.link :refer [link]]
            [open-company-web.components.profile :refer [profile]]
            [open-company-web.router :as router]
            [open-company-web.components.section-selector :refer [section-selector]]
            [open-company-web.components.all-sections :refer [all-sections]]
            [clojure.string :as str]
            [open-company-web.lib.utils :as utils]))

(enable-console-print!)

(defcomponent company [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]
      (dom/div {:class "company-container row"}
        (om/build navbar company-data)
        (dom/div {:class "container-fluid"}
          (dom/div {:class "col-md-12 main"}
            (cond

              (:loading data)
              (dom/div
                (dom/h4 "Loading data..."))

              (contains? @router/path :section)
              (om/build section-selector {:data company-data :section (keyword (:section @router/path))})

              (utils/in? (:route @router/path) "profile")
              (om/build profile data)

              (and (not (contains? data :loading)) (contains? data (keyword slug)))
              (om/build all-sections data)

              :else
              (dom/div
                (dom/h2 (str (:slug @router/path) " not found"))
                (om/build link {:href "/" :name "Back home"})))))))))