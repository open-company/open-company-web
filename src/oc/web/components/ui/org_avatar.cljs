(ns oc.web.components.ui.org-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(defcomponent org-avatar [data owner]
  (render-state [_ {:keys [img-load-failed]}]
    (when (:org-data data)
      (let [org-data (:org-data data)
            org-slug (router/current-org-slug)
            has-name (not (empty? (:name org-data)))
            org-name (if has-name
                        (:name org-data)
                        (utils/camel-case-str org-slug))
            first-letter (first (clojure.string/upper-case org-name))
            org-logo (:logo-url org-data)
            su-navbar (:su-navbar data)
            should-show-link (if su-navbar
                                  (utils/link-for (:links (dis/update-data)) "company" "GET")
                                  true)
            show-org-avatar? (and (not img-load-failed)
                                  (not (clojure.string/blank? org-logo)))
            avatar-link (if should-show-link
                          (if (router/current-board-slug)
                            (oc-urls/board)
                            (oc-urls/org))
                          "")]
        (dom/div {:class (utils/class-set {:org-avatar true
                                           :navbar-brand (:navbar-brand data)})}
          (dom/a {:href avatar-link
                  :style {:cursor (if should-show-link "pointer" "default")}
                  :on-click (fn [e]
                              (.preventDefault e)
                              (dis/dispatch! [:dashboard-share-mode false])
                              (when should-show-link
                                (router/nav! avatar-link)))}
            (dom/div {:class "org-avatar-container group"}
              (when show-org-avatar?
                (dom/div {:class "org-avatar-border"}
                  (dom/span {:class "helper"})
                  (dom/img {:src org-logo
                            :class "org-avatar-img"
                            :on-error #(om/set-state! owner :img-load-failed true)
                            :title org-name})))
              (dom/span {:class (str "org-name " (when-not show-org-avatar? "no-logo"))} org-name))))))))