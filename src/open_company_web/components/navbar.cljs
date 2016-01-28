(ns open-company-web.components.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.user-avatar :refer (user-avatar)]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [om-bootstrap.nav :as n]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]))

(defn company-title [data]
  (str (:name data)))

(defcomponent navbar [data owner]
  (render [_]
    (n/navbar {:inverse? true :fixed-top? true :fluid true :collapse? true}
      (dom/div {:class "navbar-header"}
        (when (contains? @router/path :slug)
          (let [slug (keyword (:slug @router/path))]
            (om/build company-avatar {:company-data (slug data) :navbar-brand true})))
        (dom/ul {:class "nav navbar-nav navbar-right"}
          (dom/li {}
            (if (:show-share data)
              (dom/div {}
                (dom/button {:type "button" :class "btn btn-link digest-button"}
                            (dom/img {:src "/img/digest.svg"}))
                (dom/button {:type "button" :class "btn btn-link share-button"}
                            (dom/img {:src "/img/share.svg"})))
              (if (jwt/jwt)
                (om/build user-avatar {})
                (om/build login-button data))))))
      (dom/div {:id "navbar" :class "navbar-collapse collapse container-fluid"}))))