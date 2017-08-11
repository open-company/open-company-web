(ns oc.web.components.list-orgs
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros (defcomponent)]
              [om-tools.dom :as dom :include-macros true]
              [rum.core :as rum]
              [oc.web.urls :as oc-urls]
              [oc.web.router :as router]
              [oc.web.lib.utils :as utils]
              [oc.web.lib.responsive :as responsive]
              [oc.web.components.ui.navbar :refer (navbar)]
              [oc.web.components.ui.footer :refer (footer)]
              [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defcomponent list-page-item [data owner]
  (render [_]
    (dom/li
      (dom/a {:href (oc-urls/org (:slug data))
              :on-click (fn [e]
                          (.preventDefault e)
                          (router/nav! (oc-urls/org (:slug data))))}
        (when-not (clojure.string/blank? (:logo data))
          (dom/img {:class "org-logo" :src (:logo data)}))
        (or (:name data) (:slug data))))))

(defcomponent list-orgs [{:keys [mobile-menu-open] :as data} owner]

  (init-state[_]
    {:columns-num (responsive/columns-num)})

  (render-state [_ {:keys [columns-num]}]
    (utils/update-page-title "Carrot - Get everyone aligned")
    (when (:orgs data)
      (let [orgs-list (:orgs data)
            card-width responsive/card-width]
        (dom/div {:class "list-orgs"}
          ;show login overlays if needed
          (when-not (utils/is-test-env?)
            (login-overlays-handler))
          (dom/div {:class "page"}
            (navbar)
            (dom/div {:class "navbar-offset group"}
              (if (:loading data)
                (dom/h4 "Loading Organizations...")
                (if (pos? (count orgs-list))
                  (dom/ul {:class "orgs"}
                    (om/build-all list-page-item orgs-list))
                  (dom/h2 "No organizations found."))))
            (footer (responsive/total-layout-width-int card-width columns-num))))))))