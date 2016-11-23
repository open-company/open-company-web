(ns open-company-web.components.ui.company-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]))

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
            company-logo (:logo company-data)
            should-show-link (if (:su-navbar data)
                                  (utils/link-for (:links (dis/stakeholder-update-data)) "company" "GET")
                                  true)
            company-home (if should-show-link (oc-urls/company) "")]
        (dom/div {:class (utils/class-set {:company-avatar true
                                           :navbar-brand (:navbar-brand data)})}
          (dom/a {:href company-home
                  :style {:cursor (if should-show-link "pointer" "default")}
                  :on-click (fn [e]
                              (.preventDefault e)
                              (when should-show-link
                                (router/nav! company-home)))}
            (dom/div {:class "company-avatar-container group"}
              (when-not (clojure.string/blank? company-logo)
                (dom/div {:class "company-avatar-border"}
                  (dom/span {:class "helper"})
                  (dom/img {:src company-logo
                            :class "company-avatar-img"
                            :title company-name})))
              (dom/span {:class (str "company-name " (when (clojure.string/blank? company-logo) "no-logo"))} company-name))))))))
