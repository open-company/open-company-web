(ns oc.web.components.ui.footer
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.responsive :as responsive]))

(rum/defc footer < rum/static
  [footer-width]
  [:div.footer
    [:div.footer-internal
      [:div.footer-bottom
        {:style {:width (if footer-width (str footer-width "px") "100%")}}
        [:a.oc-logo {:href oc-urls/home}
          [:img {:src "https://d1wc0stj82keig.cloudfront.net/img/oc-wordmark.svg"}]]
        [:div.footer-bottom-right
          [:a.contact {:href oc-urls/contact-mail-to
                       :title "Contact OpenCompany"
                       :alt "Contact OpenCompany"} "CONTACT"]
          [:a.twitter {:target "_blank"
                       :href oc-urls/oc-twitter
                       :title "OpenCompany on Twitter"
                       :alt "twitter"}
            [:img {:src "https://d1wc0stj82keig.cloudfront.net/img/twitter.svg"}]]
            [:a.github {:target "_blank"
                        :href oc-urls/oc-github
                        :title "OpenCompany on GitHub"
                        :alt "github"}
              [:img {:src "https://d1wc0stj82keig.cloudfront.net/img/github.svg"}]]]]]])