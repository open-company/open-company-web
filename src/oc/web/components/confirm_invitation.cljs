(ns oc.web.components.confirm-invitation
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.small-loading :refer (small-loading)]))

(defcomponent confirm-invitation [data owner]

  (init-state [_]
    {:loading true
     :confirmed false})

  (did-mount [_]
    (when-not (contains? (:query-params @router/path) :token)
      ; if the token is not present show an error message
      (om/update-state! owner #(merge % {:loading false
                                         :confirmed false}))))

  (will-receive-props [_ next-props]
    (when (contains? next-props :email-confirmed)
      (om/update-state! owner #(merge % {:loading false
                                         :confirmed (:email-confirmed next-props)}))))

  (render-state [_ {:keys [loading confirmed] :as state}]
    (dom/div {:class (utils/class-set {:confirm-invitation true
                                       :main-scroll true})}
      (dom/div {:class "group fullscreen-page with-small-footer"}
        (dom/div {:class "confirm-invitation-center center group"}
          (dom/h1 {:class "confirm-invitation-cta"}
            (if loading
              "Confirming invitation..."
              (if confirmed
                "Invitation confirmed!"
                "Invite confirmation error. Please ask to be re-invited.")))
          (when confirmed
            (dom/button {:class "btn-reset btn-solid confirm-invitation-get-started"
                         :disabled loading
                         :on-click #(router/redirect! oc-urls/home)}
              (if loading
                (small-loading)
                "OK! LET’S GET STARTED →"))))
        (dom/div {:class "mt5 center group"}
          (dom/img {:src "/img/oc-logo-gold.png"})
          (dom/div {:class "confirm-invitation-p group"}
            (dom/p {:class ""} "OpenCompany makes it easy to see the big picture. Companies are stronger when everyone knows what matters most."))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (footer (responsive/total-layout-width-int card-width columns-num))))))