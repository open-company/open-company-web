(ns oc.web.components.qsg-digest-sample
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.qsg :as qsg-actions]))

(rum/defcs qsg-digest-sample < rum/reactive
                               (drv/drv :current-user-data)
                               (rum/local false ::show-second-image)
                               {:will-mount (fn [s]
                                (let [current-user-data @(drv/get-ref s :current-user-data)]
                                  (reset! (::show-second-image s) (= (:digest-medium current-user-data) "email")))
                                s)}
  [s]
  (let [cur-user-data (drv/react s :current-user-data)]
    [:div.qsg-digest-sample
      [:div.qsg-digest-sample-modal.group
        [:button.settings-modal-close.mlb-reset
          {:on-click #(qsg-actions/dismiss-digest-sample)}]
        [:div.qsg-digest-sample-left
          [:div.qsg-digest-sample-title
            "Starting each morning in sync"]
          [:div.qsg-digest-sample-desc
            (str
             "Carrot reduces noisy interruptions so your team can stay focused on what matters. "
             "Posts are shared in a digest each morning "
             "in email or Slack.")]
          [:button.mlb-reset.qsg-digest-sample-bt
            {:on-click #(qsg-actions/dismiss-digest-sample)}
            "Ok, got it"]]
        [:div.qsg-digest-sample-right
          [:div.qsg-digest-sample-carion-items
            {:class (utils/class-set {:second-image @(::show-second-image s)})}
            [:div.qsg-digest-sample-carion-item.first-item
              [:div.qsg-digest-sample-header
                [:span.slack-logo]]
              [:img.qsg-digest-sample-image
                {:src (utils/cdn "/img/ML/homepage_screenshots_first_row_slack.png")
                 :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_first_row_slack@2x.png") " 2x")}]]
            [:div.qsg-digest-sample-carion-item.second-item
              [:div.qsg-digest-sample-header
                [:span.email-logo]]
              [:img.qsg-digest-sample-image
                {:src (utils/cdn "/img/ML/homepage_screenshots_first_row.png")
                 :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]]
            [:button.mlb-reset.qsg-digest-sample-carion-prev-bt
              {:on-click #(reset! (::show-second-image s) false)}]
            [:button.mlb-reset.qsg-digest-sample-carion-next-bt
              {:on-click #(reset! (::show-second-image s) true)}]]]]]))