(ns open-company-web.components.company-header
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.category-nav :refer (category-nav)]
            [open-company-web.router :as router]))

(defcomponent company-header [data owner]
  (render [_]
    (let [company-data (:company-data data)]
      (dom/nav {:class "navbar navbar-fixed-top company-header"}
        (dom/div {:class "row"}
          
          ;; Company logo
          (dom/div {:class "col-xs-4 company-logo"}
            (when (contains? @router/path :slug)
              (om/build company-avatar {:company-data company-data :navbar-brand true})))
          
          ;; White space
          (dom/div {:class "col-xs-4"})
          
          ;; Share
          (dom/div {:class "col-xs-4 text-right company-share"}
            (dom/button {:type "button" :class "btn btn-default round"} "Share")))
        
        ;; Company name
        (dom/div {:class "row"}
          (dom/div {:class "col-xs-12 company-name"} (:name company-data)))
        
        ;; Company description
        (dom/div {:class "row"}
          (dom/div {:class "col-xs-12 company-description"} (:description company-data)))
        
        ;; Category navigation
        (om/build category-nav {:loading (:loading data)
                                :company-data company-data})))))