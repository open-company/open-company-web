(ns open-company-web.components.page
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :refer [handle-change]]
            [open-company-web.components.currency-picker :refer [currency-picker]]
            [open-company-web.components.navbar :refer [navbar]]
            [open-company-web.components.link :refer [link]]
            [open-company-web.components.sidebar :refer [sidebar]]
            [open-company-web.components.profile :refer [profile]]
            [open-company-web.router :as router]
            [open-company-web.components.section-selector :refer [section-selector]]
            [clojure.string :as str]))

(enable-console-print!)

(defcomponent company [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)
          reports (filterv #(= (:rel %) "report") (:links company-data))]
      (dom/div {:class "company-container row"}
        (om/build navbar company-data)
        (dom/div {:class "container-fluid"}
          (om/build sidebar data)
          (dom/div {:class "col-md-11 col-md-offset-1 main"}
            (cond

              (:loading data)
              (dom/div
                (dom/h4 "Loading data..."))

              (contains? @router/path :section)
              (om/build section-selector data)

              (contains? company-data :slug)
              (om/build profile data)

              :else
              (dom/div
                (dom/h2 (str (:slug @router/path) " not found"))
                (om/build link {:href "/" :name "Back home"})))))))))
