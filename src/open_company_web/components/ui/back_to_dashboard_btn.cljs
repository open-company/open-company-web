(ns open-company-web.components.ui.back-to-dashboard-btn
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]))

(defn btn-clicked [owner e]
  (utils/event-stop e)
  (let [click-cb (om/get-props owner :click-cb)]
    (if (fn? click-cb)
      (click-cb)
      (let [company-slug (or (router/current-company-slug) (dis/last-company-slug))
            redirect-url (if company-slug
                            (oc-urls/company company-slug)
                            oc-urls/home)]
        (router/nav! redirect-url)))))

(defcomponent back-to-dashboard-btn [data owner]
  (render [_]
    (let [button-cta (or (:button-cta data) "BACK TO DASHBOARD")]
      (dom/div {:class "back-to-dashboard-row"}
        (dom/button {:class "back-to-dashboard btn-reset btn-outline"
                     :on-click (partial btn-clicked owner)} (str "← " button-cta))))))