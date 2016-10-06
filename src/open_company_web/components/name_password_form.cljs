(ns open-company-web.components.name-password-form
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]))

(defcomponent name-password-form [data owner]
  (render [_]
    (dom/div {:class (utils/class-set {:name-password-form true
                                       :main-scroll true})}
      (dom/div {:class "group fullscreen-page with-small-footer"}
        (dom/div {:class "name-password-form center group"}
          (dom/h1 {:class "name-password-form-cta"} "Please add a name and select a password:")
          (dom/div {:class "name-password-form-box"})))
      (om/build footer {:columns-num (responsive/columns-num)
                        :card-width (responsive/calc-card-width)}))))