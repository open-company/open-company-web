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
            [open-company-web.components.table-of-contents :refer [table-of-contents]]
            [clojure.string :as str]
            [open-company-web.lib.utils :as utils]))

(defcomponent company [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]

      (utils/update-page-title (str "OPENcompany - " (:name company-data)))
      
      (dom/div {:class "company-container container"}

        ; Company / user header
        (om/build navbar company-data)

        (dom/div {:class "container-fluid"}
        
          ; ToC
          (dom/div {:class "col-md-3 toc"}
            (om/build table-of-contents company-data))
        
          ; White space
          (dom/div {:class "col-md-1"})

          ; Body
          (dom/div {:class "col-md-7 main"}
            (cond

              ; data is loading
              (:loading data)
                (dom/div
                  (dom/h4 "Loading data..."))

              ; company profile
              (utils/in? (:route @router/path) "profile")
                (om/build company-profile data)

              ; all sections
              (and (not (contains? data :loading)) (contains? data (keyword slug)))
                (om/build all-sections data)

              ; error fallback
              :else
              (dom/div
                (dom/h2 (str (:slug @router/path) " not found"))
                (om/build link {:href "/" :name "Back home"})))))))))