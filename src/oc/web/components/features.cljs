(ns oc.web.components.features
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defc features < rum/reactive []

  [:div
    [:div {:id "wrap"} ; <!-- used to push footer to the bottom --> 
        
      (site-header)
      (login-overlays-handler)

      [:div.container.main.features
        ; Hope page header
        [:h1.features "Features"]

        [:div.divider-line]


        [:div.illustrations.group

          [:div.illustration.illustration-1.group
            [:img {:src (utils/cdn "/img/ML/features_il_1_608_544.svg")}]
            [:div.description.group
              [:div.title
                "Simplicity"]
              [:div.subtitle
                "Whether you’re adding a quick team update about one topic, or writing a regular stakeholder update that covers many, getting started is fast and simple."]]]

          [:div.illustration.illustration-2.group
            [:img {:src (utils/cdn "/img/ML/features_il_2_465_408.svg")}]
            [:div.description.group
              [:div.title
                "Company timeline"]
              [:div.subtitle
                "It’s easy to catch up if you missed something or want more context. Great for getting new employees up to speed, too."]]]

          [:div.illustration.illustration-3.group
            [:img {:src (utils/cdn "/img/ML/features_il_3_443_269.svg")}]
            [:div.description.group
              [:div.title
                "Feedback loops"]
              [:div.subtitle
                "Company updates are best when they trigger conversation. Comments and reactions keep everyone engaged and in sync - great for distributed teams."]]]

          [:div.illustration.illustration-4.group
            [:img {:src (utils/cdn "/img/ML/features_il_4_346_321.svg")}]
            [:div.description.group
              [:div.title
                "Works with Slack"]
              [:div.subtitle
                "With Slack single sign-on and our Slack bot, updates are automatically shared to the right channels. Discussions about updates can happen within Slack or Carrot - everything is kept in sync."]]]

          [:div.illustration.illustration-5.group
            [:img {:src (utils/cdn "/img/ML/features_il_5_333_274.svg")}]
            [:div.description.group
              [:div.title
                "Go public"]
              [:div.subtitle
                "Updates can also be made public - ideal for crowdfunded ventures, social enterprises, and startups interested in full transparency."]]]]

      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
