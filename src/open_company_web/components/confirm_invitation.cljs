(ns open-company-web.components.confirm-invitation
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.small-loading :refer (small-loading)]))

(defcomponent confirm-invitation [data owner]

  (init-state [_]
    {:loading true
     :confirmed false})

  (did-mount [_]
    (if (contains? (:query-params @router/path) :token)
      ; start loading the auth settings,
      ; then the token will be automatically exchanged
      (dis/dispatch! [:get-auth-settings])
      ; if the token is not present show an error message
      (om/update-state! owner #(merge % {:loading false
                                         :confirmed false}))))

  (will-receive-props [_ _]
    (when (contains? data :email-confirmed)
      (om/update-state! owner #(merge % {:loading false
                                         :confirmed (:email-confirmed data)}))))

  (render-state [_ {:keys [loading confirmed]}]
    (dom/div {:class (utils/class-set {:confirm-invitation true
                                       :main-scroll true})}
      (dom/div {:class (str "group fullscreen-page " (if (jwt/jwt) "with-small-footer" "with-footer"))}
        (dom/div {:class "confirm-invitation-center center group"}
          (dom/h1 {:class "confirm-invitation-cta"}
            (if loading
              "Confirming invitation..."
              (if confirmed
                "Email confirmed!"
                "Email confirmation error, plase try again.")))
          (dom/button {:class "btn-reset btn-solid confirm-invitation-get-started"
                       :on-click #(router/nav! oc-urls/home)}
            (if loading
              (small-loading)
              (if confirmed
                "OK! LET’S GET STARTED"
                "TRY AGAIN"))))
        (dom/div {:class "mt5 center group"}
          (dom/img {:src "/img/oc-logo-gold.png"})
          (dom/div {:class "confirm-invitation-p group"}
            (dom/p {:class ""} "OpenCompany makes it easy to see the big picture. Companies are stronger when everyone knows what matters most."))))
      (om/build footer {:columns-num (responsive/columns-num)
                        :card-width (responsive/calc-card-width)}))))