(ns open-company-web.components.ui.footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.icon :refer (icon)]))

(defcomponent footer [{:keys [su-preview footer-width] :as data} owner options]
  (render [_]
    (dom/div {:class "footer"}
      (when-not (jwt/jwt)
        (dom/div {:class "footer-cta"}
          (when su-preview
            (dom/div {}
              (dom/a {:class "com-link"
                      :href "https://opencompany.com/"} "LEARN MORE ➞")))))
      (dom/div {:class "footer-internal"}
        (dom/div {:class "footer-bottom"
                  :style #js {:width (str footer-width "px")}}
          (dom/a {:class "oc-logo" :href oc-urls/home} (dom/img {:src "/img/oc-wordmark.svg"}))
          ; (dom/a {:class "footer-link" :href oc-urls/about} (str "ABOUT"  (when-not (responsive/is-mobile-size?) " US")))
          (dom/a {:class "footer-link" :href oc-urls/contact-mail-to} "CONTACT")
          (when (responsive/is-mobile-size?)
            (dom/a {:class "twitter" :target "_blank" :href oc-urls/oc-twitter :alt "twitter"}
              (dom/img {:src "/img/twitter.svg"})))
          (when (responsive/is-mobile-size?)
            (dom/a {:class "github" :target "_blank" :href oc-urls/oc-github :alt "github"}
              (dom/img {:src "/img/github.svg"})))
          (when-not (responsive/is-mobile-size?)
            (dom/div {:class "footer-bottom-right"}
              (dom/a {:class "twitter" :target "_blank" :href oc-urls/oc-twitter :alt "twitter"}
                (dom/img {:src "/img/twitter.svg"}))
              (dom/a {:class "github" :target "_blank" :href oc-urls/oc-github :alt "github"}
                (dom/img {:src "/img/github.svg"})))))))))