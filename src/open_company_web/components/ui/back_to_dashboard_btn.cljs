(ns open-company-web.components.ui.back-to-dashboard-btn
  (:require [rum.core :as rum]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]))

(defn btn-clicked [click-cb e]
  (utils/event-stop e)
  (if (fn? click-cb)
    (click-cb)
    (let [company-slug (or (router/current-company-slug) (dis/last-company-slug))
          redirect-url (if company-slug
                         (oc-urls/company company-slug)
                         oc-urls/home)]
      (router/nav! redirect-url))))

(rum/defc back-to-dashboard-btn [{:keys [button-cta click-cb]}]
  (let [button-cta (or (:button-cta data) "BACK TO DASHBOARD")]
    [:div.back-to-dashboard-row
     [:button.back-to-dashboard.btn-reset.btn-outline
      {:on-click (partial btn-clicked click-cb)}
      (str "← " button-cta)]]))