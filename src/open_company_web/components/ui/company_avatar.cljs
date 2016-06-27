(ns open-company-web.components.ui.company-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.urls :as oc-urls]))

(defcomponent company-avatar [data owner]
  (render [_]
    (when (:company-data data)
      (let [company-data (:company-data data)
            slug (router/current-company-slug)
            company-name (if (contains? company-data :name)
                           (:name company-data)
                           (utils/camel-case-str slug))
            first-letter (first (clojure.string/upper-case company-name))
            is-profile-page (utils/in? (:route @router/path) "profile")
            read-only-company (:read-only company-data)
            company-home (if (or read-only-company is-profile-page)
                            (oc-urls/company-settings)
                            (oc-urls/company))
            company-logo (:logo company-data)]
        (dom/div {:class (utils/class-set {:company-avatar true
                                           :navbar-brand (:navbar-brand data)})}
          (dom/a {:href company-home
                  :on-click (fn [e]
                              (.preventDefault e)
                              (router/nav! company-home))}
            (dom/div {:class "company-avatar-container"}
              (dom/div {:class "company-avatar-border"}
                (if-not (clojure.string/blank? company-logo)
                  (dom/img {:src company-logo
                            :class "company-avatar-img"
                            :title company-name})
                  (dom/span {:class "company-avatar-initial"} first-letter)))
              (dom/span {:class "company-name"} company-name))))))))
