(ns oc.web.components.email-confirmation
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.small-loading :refer (small-loading)]))

(defn exchange-token-when-ready [owner]
  (when-let [auth-settings (om/get-props owner :auth-settings)]
    (when (and (not (om/get-state owner :exchange-started))
               (utils/link-for (:links auth-settings) "authenticate" "GET" {:auth-source "email"}))
      (om/set-state! owner :exchange-started true)
      (dis/dispatch! [:auth-with-token :email-verification]))))

(defcomponent email-confirmation [data owner]

  (did-mount [_]
    (exchange-token-when-ready owner))

  (did-update [_ _ _]
    (exchange-token-when-ready owner))

  (render [_]
    (dom/div {:class (utils/class-set {:email-confirmation true
                                       :main-scroll true})}
      (dom/div {:class "group fullscreen-page with-small-footer"}
        (dom/div {:class "email-confirmation-center center group"}
          (cond
            (= (:email-verification-error data) 401)
            (dom/h1 {:class "email-confirmation-cta"} "This link is not valid, please try again.")
            (:email-verification-error data)
            (dom/h1 {:class "email-confirmation-cta"} "An error occurred, please try again.")
            :else
            (small-loading)))
        (dom/div {:class "mt5 center group"}
          (dom/img {:src (str ls/cdn-url "/img/oc-logo-gold.png")})
          (dom/div {:class "email-confirmation-p group"}
            (dom/p {:class ""} "Carrot makes it easy to see the big picture. Companies are stronger when everyone knows what matters most."))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (footer (responsive/total-layout-width-int card-width columns-num))))))