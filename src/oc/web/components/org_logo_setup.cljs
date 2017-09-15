(ns oc.web.components.org-logo-setup
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.oc-colors :as occ]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.org-settings :as org-settings]))

(defn save-org-clicked [owner e]
  (utils/event-stop e)
  (when-not (om/get-state owner :loading)
    (let [logo-url (om/get-state owner :logo-url)
          logo-width (om/get-state owner :logo-width)
          logo-height (om/get-state owner :logo-height)]
      (if (empty? logo-url)
        (dis/dispatch! [:error-banner-show "Please insert a logo image" 5000])
        (do
          (om/set-state! owner :redirect-to-org true)
          (api/patch-org {:logo-width (js/parseInt logo-width)
                          :logo-height (js/parseInt logo-height)
                          :logo-url logo-url}))))))

(defcomponent org-logo-setup [data owner]

  (init-state [_]
    (let [org-data (dis/org-data data)]
      {:loading false
       :logo-url (:logo-url org-data)
       :logo-width (:logo-width org-data)
       :logo-height (:logo-height org-data)
       :redirect-to-org false}))

  (will-receive-props [_ next-props]
    (let [org-data (dis/org-data next-props)]
      (when (:read-only org-data)
        (router/redirect! (oc-urls/org)))
      (when (not= (:logo-url org-data) (om/get-state owner :logo-url))
        (om/update-state! owner #(merge % {:logo-url (:logo-url org-data)
                                           :logo-width (:logo-width org-data)
                                           :logo-height (:logo-height org-data)})))
      (when (om/get-state owner :redirect-to-org)
        (router/nav! (oc-urls/org)))))

  (did-mount [_]
    (utils/update-page-title "Carrot - Setup Your Company Logo"))

  (render-state [_ {:keys [loading logo-url]}]
    (dom/div {:class "org-editor org-logo-setup"}
      (dom/div {:class "group fullscreen-page with-small-footer"
                :style {:background-color "white"}}
        (dom/div {:class "col-md-7 col-md-offset-2 p0"}
          (dom/h2 {:class "domine mb3"} "Company Setup"))
        (dom/div {:class "col-md-7 col-md-offset-2 bg-gray p3 group"}
          (dom/form {:on-submit #(utils/event-stop %)}
            (dom/div {:class "form-group"}
              (dom/label {:class "small-caps h6 bold block"} "LOGO (180x180px)")
              (om/build org-settings/org-logo-setup {:logo-url logo-url
                                                     :logo-did-change-cb (fn [new-logo]
                                                                          (om/update-state! owner #(merge % {:logo-url new-logo
                                                                                                             :loading true})))
                                                     :logo-did-load-cb (fn [new-logo new-logo-width new-logo-height]
                                                                        (om/update-state! owner #(merge % {:logo-url new-logo
                                                                                                           :loading false
                                                                                                           :logo-width new-logo-width
                                                                                                           :logo-height new-logo-height})))})))
            (dom/button {:class "btn-reset btn-solid right"
                         :on-click (partial save-org-clicked owner)
                         :disabled loading}
                        (dom/label {:class (str "mt1" (when loading " ml2"))} "NEXT →"))
            (dom/button {:class "btn-reset btn-outline right mr1"
                         :on-click #(router/nav! (oc-urls/org))}
                        (dom/label {:class (str "mt1" (when loading " ml2"))} "SKIP"))))
      (let [columns-num (responsive/columns-num)
            card-width responsive/card-width]
        (footer (responsive/total-layout-width-int card-width columns-num))))))