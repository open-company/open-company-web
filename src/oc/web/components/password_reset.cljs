(ns oc.web.components.password-reset
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

(defn exchange-token-when-ready [owner]
  (when-let [auth-settings (om/get-props owner :auth-settings)]
    (when (and (not (om/get-state owner :exchange-started))
               (utils/link-for (:links auth-settings) "authenticate" "GET" {:auth-source "email"}))
      (om/set-state! owner :exchange-started true)
      (dis/dispatch! [:auth-with-token :password-reset]))))

(defcomponent password-reset [data owner]

  (did-mount [_]
    (exchange-token-when-ready owner))

  (did-update [_ _ _]
    (exchange-token-when-ready owner))

  (render [_]
    (dom/div {:class (utils/class-set {:password-reset true
                                       :main-scroll true})}
      (dom/div {:class "group fullscreen-page with-small-footer"}
        (dom/div {:class "password-reset-center center group"}
          (cond
            (= (:collect-pswd-error data) 401)
            (dom/h1 {:class "password-reset-cta"} "This reset password link is not valid, please try again.")
            (:collect-pswd-error data)
            (dom/h1 {:class "password-reset-cta"} "An error occurred, please try again.")
            :else
            (small-loading)))
        (dom/div {:class "mt5 center group"}
          (dom/img {:src "https://d1wc0stj82keig.cloudfront.net/img/oc-logo-gold.png"})
          (dom/div {:class "password-reset-p group"}
            (dom/p {:class ""} "OpenCompany makes it easy to see the big picture. Companies are stronger when everyone knows what matters most."))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (footer (responsive/total-layout-width-int card-width columns-num))))))