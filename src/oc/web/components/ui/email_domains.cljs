(ns oc.web.components.ui.email-domains
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.team :as team-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]))

(defn email-domain-removed [success?]
  (notification-actions/show-notification
   {:title (if success? "Email domain successfully removed" "Error")
    :description (when-not success? "An error occurred while removing the email domain, please try again.")
    :expire 3
    :id (if success? :email-domain-remove-success :email-domain-remove-error)
    :dismiss true}))

(defn email-domain-added [success?]
  (notification-actions/show-notification
   {:title (if success? "Email domain successfully added" "Error")
    :description (when-not success? "An error occurred while adding the email domain, please try again.")
    :expire 3
    :id (if success? :email-domain-add-success :email-domain-add-error)
    :dismiss true}))

(defn remove-email-domain-prompt [domain]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "org-email-domain-remove"
                    :message "Are you sure you want to remove this email domain?"
                    :link-button-title "Keep"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                        (alert-modal/hide-alert)
                                        (team-actions/remove-team (:links domain) email-domain-removed))}]
    (alert-modal/show-alert alert-data)))

(rum/defcs email-domains < rum/reactive
                           (drv/drv :org-settings-team-management)
                           ui-mixins/refresh-tooltips-mixin
  [s]
  (let [{:keys [um-domain-invite team-data]} (drv/react s :org-settings-team-management)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.email-domain-container
      [:div.email-domain-title
        "Allowed email domains"
        [:i.mdi.mdi-information-outline
          {:title "Any user that signs up with an allowed email domain and verifies their email address will have contributor access to your team."
           :data-toggle (when-not is-mobile? "tooltip")
           :data-placement "top"
           :data-container "body"}]]
      [:div.email-domain-field-container.oc-input.group
        [:input.email-domain-field
          {:type "text"
           :placeholder "@domain.com"
           :auto-capitalize "none"
           :value (:domain um-domain-invite)
           :pattern "@?[a-z0-9.-]+\\.[a-z]{2,4}$"
           :on-change #(dis/dispatch! [:input [:um-domain-invite :domain] (.. % -target -value)])
           :on-key-press (fn [e]
                           (when (= (.-key e) "Enter")
                             (let [domain (:domain um-domain-invite)]
                               (when (utils/valid-domain? domain)
                                 (team-actions/email-domain-team-add domain email-domain-added)))))}]
        [:button.mlb-reset.add-email-domain-bt
          {:disabled (not (utils/valid-domain? (:domain um-domain-invite)))
           :on-click (fn [e]
                       (let [domain (:domain um-domain-invite)]
                         (when (utils/valid-domain? domain)
                           (team-actions/email-domain-team-add domain email-domain-added))))}
          "Add"]]
      [:div.email-domain-list
        (for [domain (:email-domains team-data)]
          [:div.email-domain-row
            {:key (str "org-settings-domain-" domain)}
            (str "@" (:domain domain))
            (when (utils/link-for (:links domain) "remove")
              [:button.mlb-reset.remove-email-bt
                {:on-click #(remove-email-domain-prompt domain)}
                "Remove"])])]]))