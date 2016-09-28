(ns open-company-web.components.sign-up
  (:require [rum.core :as rum]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.site-header :refer (site-header)]
            [open-company-web.components.ui.site-footer :refer (site-footer)]
            [open-company-web.components.ui.login-overlay :as login-overlays]))

(rum/defcs sign-up < rum/reactive [s]

  [:div
    [:div {:id "wrap"} ; <!-- used to push footer to the bottom --> 
        
      (site-header)

      ; show login overlays when needed
      (login-overlays/login-overlays-handler (rum/react dis/app-state))

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
                                              :on-click #(do
                                                          (utils/event-stop %)
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
                [:img.info-image {:id "company-dashboard" :src "./img/green-labs-mac@4x.png" :alt "Company Dashboard"}]]]]]

        [:div.right-to-left.web-only
          [:div.container.info.second
            [:div.row
              [:div.col-sm-6.col-xs-12.left.no-bottom
                [:img.info-image.investor-update {:src "./img/green-labs-iphone@4x.png" :alt "Investor Update"}]]
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
                [:img.info-image.investor-update {:src "./img/green-labs-iphone@4x.png" :alt "Investor Update"}]]]]]

        [:div.left-to-right
          [:div.container.info.third
            [:div.row
              [:div.col-sm-6.col-xs-12.left
                [:div
                  [:h3.info-headline "Expand your audience with optional public transparency."]
                  [:p "Ideal for crowdfunded ventures, Kickstarter campaigns, social enterprises, and others interested in being more open."]]]
              [:div.col-sm-6.col-xs-12.right
                [:img.info-image {:id "transparency" :src "./img/transparency.png" :alt "Transparency toggle"}]]]]]
      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])