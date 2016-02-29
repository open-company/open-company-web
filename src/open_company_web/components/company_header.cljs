(ns open-company-web.components.company-header
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.category-nav :refer (category-nav)]
            [open-company-web.router :as router]))

(defcomponent company-header [{:keys [company-data navbar-editing] :as data} owner]

  (render [_]
    (dom/div {:class "company-header"}

      (when-not navbar-editing
        (dom/div {}
          ;; Company name
          (dom/div {:class "container oc-header"}
            (dom/div {:class "col-xs-12 company-name"} (:name company-data)))

          ;; Company description
          (dom/div {:class "container oc-header"}
            (dom/div {:class "col-xs-12 company-description"} (:description company-data)))))

      ;; Category navigation
      (om/build category-nav (assoc data :navbar-editing navbar-editing)))))