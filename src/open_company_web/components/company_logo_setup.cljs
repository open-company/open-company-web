(ns open-company-web.components.company-logo-setup
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.company-settings :as company-settings]))

(defn save-company-clicked [owner e]
  (utils/event-stop e)
  (when-not (om/get-state owner :loading)
    (let [logo (om/get-state owner :logo)
          logo-width (om/get-state owner :logo-width)
          logo-height (om/get-state owner :logo-height)]
      (if (clojure.string/blank? logo)
        (js/alert "Please insert a logo image")
        (do
          (om/set-state! owner :redirect-to-company true)
          (api/patch-company (router/current-company-slug) {:logo-width (js/parseInt logo-width)
                                                            :logo-height (js/parseInt logo-height)
                                                          :logo logo}))))))

(defcomponent company-logo-setup [data owner]

  (init-state [_]
    (let [company-data (dis/company-data data)]
      {:loading false
       :logo (:logo company-data)
       :logo-width (:logo-width company-data)
       :logo-height (:logo-height company-data)
       :redirect-to-company false}))

  (will-receive-props [_ next-props]
    (let [company-data (dis/company-data next-props)]
      (when (not= (:logo company-data) (om/get-state owner :logo))
        (om/update-state! owner #(merge % {:logo (:logo company-data)
                                           :logo-width (:logo-width company-data)
                                           :logo-height (:logo-height company-data)})))
      (when (om/get-state owner :redirect-to-company)
        (router/nav! (oc-urls/company)))))

  (did-mount [_]
    (utils/update-page-title "OpenCompany - Setup Your Company Logo"))

  (render-state [_ {:keys [loading logo]}]
    (dom/div {:class "company-editor company-logo-setup"}
      (dom/div {:class (str "group fullscreen-page "  (if (jwt/jwt) "with-small-footer" "with-footer"))}
        (dom/div {:class "col-md-7 col-md-offset-2 p0"}
          (dom/h2 {:class "domine mb3"} "Company Setup"))
        (dom/div {:class "col-md-7 col-md-offset-2 bg-gray p3 group"}
          (dom/form {:on-submit #(utils/event-stop %)}
            (dom/div {:class "form-group"}
              (dom/label {:class "small-caps h6 bold block"} "LOGO (180x180px)")
              (om/build company-settings/company-logo-setup {:logo logo
                                                             :logo-did-change-cb (fn [new-logo]
                                                                                  (om/update-state! owner #(merge % {:logo new-logo
                                                                                                                     :loading true})))
                                                             :logo-did-load-cb (fn [new-logo new-logo-width new-logo-height]
                                                                                (om/update-state! owner #(merge % {:logo new-logo
                                                                                                                   :loading false
                                                                                                                   :logo-width new-logo-width
                                                                                                                   :logo-height new-logo-height})))})))
            (dom/button {:class "btn-reset btn-solid right"
                         :on-click (partial save-company-clicked owner)
                         :disabled loading}
                        (dom/label {:class (str "mt1" (when loading " ml2"))} "NEXT →"))
            (dom/button {:class "btn-reset btn-outline right mr1"
                         :on-click #(router/nav! oc-urls/home)}
                        (dom/label {:class (str "mt1" (when loading " ml2"))} "SKIP"))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (om/build footer {:footer-width (responsive/total-layout-width-int card-width columns-num)})))))