(ns open-company-web.components.list-companies
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros (defcomponent)]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.urls :as oc-urls]
              [open-company-web.router :as router]
              [open-company-web.lib.utils :as utils]
              [open-company-web.lib.responsive :as responsive]
              [open-company-web.components.menu :refer (menu)]
              [open-company-web.components.navbar :refer (navbar)]
              [open-company-web.components.footer :refer (footer)]))

(defcomponent list-page-item [data owner]
  (render [_]
    (dom/li
      (dom/a {:href (oc-urls/company (:slug data))
              :on-click (fn [e] (utils/event-stop e) (router/nav! (oc-urls/company (:slug data))))}
        (dom/img {:class "company-logo" :src (:logo data)})
        (:name data)))))

(defcomponent list-companies [{:keys [menu-open] :as data} owner]

  (init-state[_]
    {:columns-num (responsive/columns-num)})

  (render-state [_ {:keys [columns-num]}]
    (utils/update-page-title "OpenCompany - Startup Transparency Made Simple")
    (when (:companies data)
      (let [company-list (:companies data)
            card-width (responsive/calc-card-width)]
        (dom/div {:class "list-companies"}
          (om/build menu data)
          (dom/div {:class "page"}
            (om/build navbar {:card-width card-width
                              :sharing-mode false
                              :columns-num columns-num
                              :menu-open menu-open
                              :auth-settings (:auth-settings data)})
            (dom/div {:class "navbar-offset group"}
              (if (:loading data)
                (dom/h4 "Loading companies...")
                (if (pos? (count company-list))
                  (dom/ul {:class "companies"}
                    (om/build-all list-page-item company-list))
                  (dom/h2 "No companies found."))))
            (om/build footer {:card-width card-width
                              :columns-num columns-num})))))))