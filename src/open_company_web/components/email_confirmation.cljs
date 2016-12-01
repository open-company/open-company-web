(ns open-company-web.components.email-confirmation
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]))

(defcomponent email-confirmation [data owner]
  (render [_]
    (dom/div {:class (utils/class-set {:email-confirmation true
                                       :main-scroll true})}
      (dom/div {:class (str "group fullscreen-page " (if (jwt/jwt) "with-small-footer" "with-footer"))}
        (dom/div {:class "email-confirmation-center center group"}
          (dom/h1 {:class "email-confirmation-cta"} "Email Confirmed!")
          (dom/button {:class "btn-reset btn-solid email-confirmation-get-started"
                       :on-click #(router/nav! oc-urls/create-company)}
            "OK! LET'S GET STARTED →"))
        (dom/div {:class "mt5 center group"}
          (dom/img {:src "/img/oc-logo-gold.png"})
          (dom/div {:class "email-confirmation-p group"}
            (dom/p {:class ""} "OpenCompany makes it easy to see the big picture. Companies are stronger when everyone knows what matters most."))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (om/build footer {:footer-width (responsive/total-layout-width-int card-width columns-num)})))))