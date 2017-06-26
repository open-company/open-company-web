(ns oc.web.components.home-page
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs home-page < rum/reactive [s]

  [:div
    [:div {:id "wrap"} ; <!-- used to push footer to the bottom --> 
        
      (site-header)
      ;; preload slack button as hidden
      [:img.hidden {:src "https://api.slack.com/img/sign_in_with_slack.png"}]
      (login-overlays-handler)

      [:div.main.home-page
        ; Hope page header
        [:div.cta
          [:h1.headline "Company updates that build transparency and alignment"]
          [:div.subheadline "It's never been easier to get everyone aligned - inside and outside the company."]
          [:button.get-started-centred-bt.mlb-reset
            {:on-click #(dis/dispatch! [:login-overlay-show :signup-with-slack])}
            "Get Early Access"]
          [:div.small-teams
            "Easy set-up • Free for small teams"]
          ;; FIXME: Remove the carrot screenshot for the initial onboarding period
          (comment
            [:img
              {:src "/img/ML/home_page_screenshot.png"
               :width 756
               :height 511}])]


        [:div.illustrations.group

          [:div.illustration.illustration-1.group
            [:img {:src "/img/ML/home_page_il_1_412_385.svg"}]
            [:div.description.group
              [:div.title
                "Get aligned fast"]
              [:div.subtitle
                "Check out what’s new this week, or get new employees up to speed in a flash. Updates are in one place and easy to find."]]]

          [:div.illustration.illustration-2.group
            [:img {:src "/img/ML/home_page_il_2_444_414.svg"}]
            [:div.description.group
              [:div.title
                "Keep investors up to date"]
              [:div.subtitle
                "Investors and advisors are happier - and more helpful - when they’re in the loop!"]]]

          [:div.illustration.illustration-3.group
            [:img {:src "/img/ML/home_page_il_3_355_350.svg"}]
            [:div.description.group
              [:div.title
                "Grow your business"]
              [:div.subtitle
                "Share the latest news with recruits, potential investors, and customers. Build trust with a bigger audience and they’ll reward you for it."]]]]

        (comment
          [:div.customers
            [:div.customers-title
              [:img {:src "/img/ML/user_avatar_yellow.svg"}]
              "Our happy clients"]
            [:div.customers-cards.group
              [:div.left-arrow
                [:button.mlb-reset.left-arrow-bt
                  {:disabled true}]]
              [:div.customers-cards-scroll
                [:div.customers-card]
                [:div.customers-card]
                [:div.customers-card]]
              [:div.right-arrow
                [:button.mlb-reset.right-arrow-bt
                  {:disabled true}]]]])

        [:div.try-it
          {:id "mc_embed_signup"}
          [:div.try-it-title
            "Try it today"]
          [:div.try-it-subtitle
            "Easy set-up • Free for small teams"]
          [:form.validate
            {:action "//opencompany.us11.list-manage.com/subscribe/post?u=16bbc69a5b39531f20233bd5f&amp;id=2ee535bf29"
             :method "post"
             :id "mc-embedded-subscribe-form"
             :name "mc-embedded-subscribe-form"
             :target "_blank"
             :no-validate true}
            [:div.try-it-combo-field
              {:id "mc_embed_signup_scroll"}
              [:div.hidden "real people should not fill this in and expect good things - do not remove this or risk form bot signups"]
              [:div.clear {:id "mce-responses"}
                [:div.response.hidden {:id "mce-error-response"}]
                [:div.response.hidden {:id "mce-success-response"}]]
              [:div.mc-field-group
                [:input.mail.required
                  {:type "text"
                   :id "mce-EMAIL"
                   :name "EMAIL"
                   :placeholder "Email address"}]]
              [:div {:style #js {:position "absolute" :left "-5000px"}
                     :aria-hidden true}
                [:input {:type "text" :name "b_16bbc69a5b39531f20233bd5f_2ee535bf29" :tab-index "-1" :value ""}]]
              [:button.mlb-reset.try-it-get-started
                {:type "submit"
                 :id "mc-embedded-subscribe"}
                "Get Started"]]]]

      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
