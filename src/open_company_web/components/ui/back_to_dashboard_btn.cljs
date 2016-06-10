(ns open-company-web.components.ui.back-to-dashboard-btn
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]))

(defn btn-clicked [e]
  (let [company-slug (or (router/current-company-slug) (dis/last-company-slug))
        redirect-url (if company-slug
                        (oc-urls/company company-slug)
                        oc-urls/home)]
    (router/nav! redirect-url)))

(defcomponent back-to-dashboard-btn [data owner]
  (render [_]
    (dom/div {:class "back-to-dashboard-row"}
      (dom/button {:class "back-to-dashboard btn-reset btn-outline"
                   :on-click (partial btn-clicked)} "← BACK TO DASHBOARD"))))