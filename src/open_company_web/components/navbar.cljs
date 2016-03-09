(ns open-company-web.components.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.user-avatar :refer (user-avatar)]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [om-bootstrap.nav :as n]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.jwt :as jwt]
            [cljs.core.async :refer (put!)]))

(defn company-title [data]
  (str (:name data)))

(defcomponent navbar [data owner]

  (render [_]
    (let [edit-mode (:edit-mode data)
          save-bt-active (:save-bt-active data)]
      (if edit-mode
        (n/navbar {:inverse? true :fixed-top? true :fluid true :collapse? true}
          (dom/div {:class "navbar-header editing-mode"}
            (dom/div {:class "left-button"}
              (dom/button {:class "cancel-bt oc-btn oc-link"
                           :on-click (fn [e]
                                       (when-let [ch (utils/get-channel "cancel-bt-navbar")]
                                         (put! ch {:click true :event e})))} "Cancel"))
            (dom/div {:class "right-button"}
              (dom/button {:class "save-bt oc-btn oc-link"
                           :disabled (not save-bt-active)
                           :on-click (fn [e]
                                       (when-let [ch (utils/get-channel "save-bt-navbar")]
                                         (put! ch {:click true :event e})))} "Save"))
            (dom/div {:class "editing-title oc-title"} (:edit-title data))))
        (n/navbar {:inverse? true :fixed-top? true :fluid true :collapse? true}
          (dom/div {:class "navbar-header"}
            (dom/label {:class "opencompany-logo-open"} "open")
            (dom/label {:class "opencompany-logo-company"} "company")
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
          (dom/div {:id "navbar" :class "navbar-collapse collapse container-fluid"}))
        ))))