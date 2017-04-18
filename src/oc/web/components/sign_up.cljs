(ns oc.web.components.sign-up
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs sign-up < rum/reactive [s]

  [:div
    [:div {:id "wrap"} ; <!-- used to push footer to the bottom --> 
        
      (site-header)
      ;; preload slack button as hidden
      [:img.hidden {:src "https://api.slack.com/img/sign_in_with_slack.png"}]
      (login-overlays-handler)

      [:div.main.sign-up
        [:div.cta
          [:div.container.cta
            [:div.vertical-center-row
              [:h1.tagline {} "Transparency Made Simple"]
              [:div.control-group
                [:form.form-inline {:id "subscribe"}
                  [:input {:id "referrer" :type "hidden" :name "refrerrer" :value ""}]
                  [:div.row
                    [:div.col-xs-12.center
                      [:button.submit.domine {:id "submit"
                                              :name "subscribe"
                                              :type "submit"
                                              :onClick #(do
                                                          (.preventDefault %)
                                                          (dis/dispatch! [:show-login-overlay :signup-with-slack]))}
                        "Get Started →"]]]]]]]]

        [:div.sub-tag
          [:div.container.sub-tag
            [:div.vertical-center-row
              [:h2.sub-tagline "Company updates that build trust with employees, investors and customers."]]]]

        [:div.left-to-right
          [:div.container.info.first
            [:div.row
              [:div.col-sm-6.col-xs-12.left
                [:div
                  [:h3.info-headline "Create beautiful company updates in less time."]]]
              [:div.col-sm-6.col-xs-12.right {:style {:padding-bottom "0px"}}
                [:img.info-image {:id "org-dashboard" :src (str ls/cdn-url "/img/green-labs-mac@4x.png") :alt "Company Dashboard"}]]]]]

        [:div.right-to-left.web-only
          [:div.container.info.second
            [:div.row
              [:div.col-sm-6.col-xs-12.left.no-bottom
                [:img.info-image.investor-update {:src (str ls/cdn-url "/img/green-labs-iphone@4x.png") :alt "Investor Update"}]]
              [:div.col-sm-6.col-xs-12.right
                [:div
                  [:h3.info-headline "Keep your team engaged and on the same page. Impress investors and advisors."]]]]]]

        [:div.right-to-left.mobile-only
          [:div.container.info.second
            [:div.row
              [:div.col-sm-6.col-xs-12.right
                [:div
                  [:h3.info-headline "Keep your team engaged and on the same page. Impress investors and advisors."]]]
              [:div.col-sm-6.col-xs-12.left.no-bottom
                [:img.info-image.investor-update {:src (str ls/cdn-url "/img/green-labs-iphone@4x.png") :alt "Investor Update"}]]]]]

        [:div.left-to-right
          [:div.container.info.third
            [:div.row
              [:div.col-sm-6.col-xs-12.left
                [:div
                  [:h3.info-headline "Expand your audience with optional public transparency."]
                  [:p "Ideal for crowdfunded ventures, Kickstarter campaigns, social enterprises, and others interested in being more open."]]]
              [:div.col-sm-6.col-xs-12.right
                [:img.info-image {:id "transparency" :src (str ls/cdn-url "/img/transparency.png") :alt "Transparency toggle"}]]]]]
      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])