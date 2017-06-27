(ns oc.web.components.ui.footer
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]))

(rum/defc footer < rum/static
  [footer-width]
  [:div.footer
    [:div.footer-internal
      [:div.footer-bottom
        {:style {:width (if footer-width (str footer-width "px") "100%")}}
        [:a.oc-logo {:href oc-urls/home}
          [:img {:src (str ls/cdn-url "/img/oc-wordmark.svg")}]]
        [:div.footer-bottom-right
          [:a.contact {:href oc-urls/contact-mail-to
                       :title "Contact Carrot"
                       :alt "Contact Carrot"} "CONTACT"]
          [:a.twitter {:target "_blank"
                       :href oc-urls/oc-twitter
                       :title "Carrot on Twitter"
                       :alt "twitter"}
            [:img {:src (str ls/cdn-url "/img/twitter.svg")}]]
            [:a.github {:target "_blank"
                        :href oc-urls/oc-github
                        :title "Carrot on GitHub"
                        :alt "github"}
              [:img {:src (str ls/cdn-url "/img/github.svg")}]]]]]])