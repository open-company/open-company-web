(ns open-company-web.components.pricing
  (:require [rum.core :as rum]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.ui.site-header :refer (site-header)]
            [open-company-web.components.ui.site-footer :refer (site-footer)]
            [open-company-web.components.ui.login-overlay :as login-overlays]
            [open-company-web.urls :as oc-urls]))

(rum/defcs pricing < rum/reactive [s]
  [:div
    [:div.pricing-wrap {:id "wrap"} ; <!-- used to push footer to the bottom --> 

      (site-header)

      ; show login overlays when needed
      (login-overlays/login-overlays-handler (rum/react dis/app-state))

      [:div.container.main.pricing

        [:div.row.equal
          [:div.col-sm-2.col-xs-0]
          [:div.col-sm-4.col-xs-12.left
            [:div.price-box
              [:h1 "Public"]
              [:hr]
              [:p
                [:span.cost "$0"][:string " / month"]]
              [:p "OpenCompany is free to use for dashboards that are open to the public."]]]
          [:div.col-sm-4.col-xs-12.right
            [:div.price-box
              [:h1 "Private"]
              [:hr]
              [:p
                [:span.cost "$25"][:strong " / month"]]
              [:p [:strong "Includes 5 active contributors. Additional are $5 / month."]]
              [:p "Active contributors can add and edit information. All other users are free."]]]
          [:div.col-sm-2.col-xs-0]]

        [:div.row
          [:div.col-sm-3.col-xs-0]
          [:div.col-sm-6.col-xs-12
            [:div.concierge-logo
              [:img {:id "oc-logo-gold" :src "./img/oc-logo-gold.png" :alt "OpenCompany Logo"}]]
            [:h1 "Concierge"]
            [:p "Want us to build and maintain your company dashboard for you? Our team will make it
            beautiful, and we can prepare team and investor updates for you to approve and send."]
            [:p "The concierge service is "[:strong "$199 / month"]"."]]
          [:div.col-sm-3.col-xs-0]]

      ] ; <!-- main -->
    ] ; <!-- wrap -->

    (site-footer)])