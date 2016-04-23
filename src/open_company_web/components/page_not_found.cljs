(ns open-company-web.components.page-not-found
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.components.ui.link :refer (link)]))

(defcomponent page-not-found [data owner]
  (render [_]
    (dom/div
      (dom/h1 "Page not found")
      (om/build link {:href oc-urls/home :name "Home"}))))