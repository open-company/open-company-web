(ns open-company-web.components.sign-up
  (:require [rum.core :as rum]))

(rum/defc sign-up []
  [:div
    [:div
      {:id "wrap"} ; <!-- used to push footer to the bottom --> 
        
      ; <!-- Nav Bar -->
      [:nav.navbar.navbar-default.navbar-static-top
        [:div.container-fluid
          [:div.navbar-header
            [:a.navbar-brand
              {:href "/"}
              [:img {:alt "OpenCompany" :src "img/oc-wordmark.svg"}]]
            [:button.navbar-toggle.collapsed
              {type="button" class="" data-toggle="collapse" data-target="#oc-navbar-collapse"}
              [:span.sr-only "Toggle navigation"]
              [:span.icon-bar]
              [:span.icon-bar]
              [:span.icon-bar]]]
          [:div.collapse.navbar-collapse {:id "oc-navbar-collapse"}
            [:ul.nav.navbar-nav.navbar-right.navbar-top
              [:li
                [:a {:href "/pricing"} "Pricing"]]
              [:li
                [:a {:href "/about"} "About"]]
              [:li.mobile-only
                [:a.contact {:href "mailto:hello@opencompany.com"} "Contact"]]]]]]

        [:div.main
          [:div.cta
            [:div.container.cta
              [:div.vertical-center-row
                [:h1.tagline {} "Transparency Made Simple"]
                [:div.control-group
                  [:form.form-inline {:id "subscribe"}
                    [:input {:id "referrer" :type "hidden" :name "refrerrer" :value ""}]
                    [:div.row
                      [:div.col-sm-6.col-xs-12.left-cta
                        [:input {:id "email" :type "email" :name "email" :placeholder "YOUR WORK EMAIL" :required "required"}]]
                      [:div.col-sm-6.col-xs-12.right-cta
                        [:button.submit {:id "submit" :name "subscribe" :type "submit"} "Get Early Access →"]]
                      [:div.col-xs-12
                        [:span {:id "thank-you"} "Thank you! We'll be in touch soon."]]
                      [:div.col-xs-12.error
                        [:span {:id "error-message"} "Sorry! Our system is temporarily down, please try again later."]]]]]]]]

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

        ] ; <!-- main -->
      ]

    ; <!-- footer -->
    [:nav.navbar.navbar-default.navbar-bottom

        [:ul.nav.navbar-nav.navbar-left.navbar-bottom-left
          [:li
            [:a.navbar-logo {:href "/"}
              [:img {:alt "OpenCompany" :src "img/oc-wordmark.svg"}]]]
          [:li.web-only
              [:a {:href "/pricing"} "Pricing"]]
          [:li.web-only
              [:a {:href "/about"} "About"]]
          [:li
            [:a.contact {:href "mailto:hello@opencompany.com"} "Contact"]]
          [:li.mobile-only {:style {:float "right" :margin-right "15px"}}
            [:a {:href "https://twitter.com/opencompanyhq"}
              [:i.fa.fa-2x.fa-twitter {:aria-hidden "true"}]]]]
        [:ul.nav.navbar-nav.navbar-right
          [:li
            [:a {:href "https://twitter.com/opencompanyhq"}
              [:i.fa.fa-2x.fa-twitter {:aria-hidden "true"}]]]
          [:li
            [:a {:href "https://github.com/open-company"}
              [:i.fa.fa-2x.fa-github {:aria-hidden "true"}]]]]]
    ])