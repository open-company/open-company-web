(ns oc.web.components.ui.org-settings-main-panel
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.org :as org-utils]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [goog.object :as gobj]
            [goog.dom :as gdom]))

(defn form-is-clean? [s]
  (let [has-org-edit-changes (:has-changes @(drv/get-ref s :org-editing))
        {:keys [um-domain-invite]} @(drv/get-ref s :org-settings-team-management)]
    (and (not has-org-edit-changes)
         (empty? (:domain um-domain-invite)))))

(defn reset-form [s]
  (let [org-data (first (:rum/args s))
        um-domain-invite (:um-domain-invite @(drv/get-ref s :org-settings-team-management))]
    (org-actions/org-edit-setup org-data)
    (dis/dispatch! [:input [:um-domain-invite :domain] ""])
    (dis/dispatch! [:input [:add-email-domain-team-error] nil])))

(rum/defcs org-settings-main-panel
  < rum/reactive
    (rum/local false ::saving)
    (rum/local false ::showing-menu)
    (rum/local false ::show-advanced-settings)
    (rum/local true ::advanced-settings-1)
    (rum/local true ::advanced-settings-2)
    (rum/local true ::advanced-settings-3)
    (drv/drv :org-data)
    (drv/drv :org-settings-team-management)
    (drv/drv :org-editing)
    (drv/drv :current-user-data)
    (on-window-click-mixin (fn [s _] (reset! (::showing-menu s) false)))
    {:will-mount (fn [s]
                   (reset-form s)
                   s)
     :will-update (fn [s]
                    (let [org-editing @(drv/get-ref s :org-editing)]
                      (when (and @(::saving s)
                                 (:saved org-editing))
                        (reset! (::saving s) false)
                        (utils/after 2500 #(dis/dispatch! [:input [:org-editing :saved] false]))
                        (notification-actions/show-notification {:title "Settings saved"
                                                                 :primary-bt-title "OK"
                                                                 :primary-bt-dismiss true
                                                                 :expire 10
                                                                 :id :org-settings-saved})))
                    s)
     :after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     s)}
  [s org-data dismiss-settings-cb]
  (let [org-editing (drv/react s :org-editing)
        {:keys [query-params
                um-domain-invite
                add-email-domain-team-error
                team-data]
         :as team-management-data}
                    (drv/react s :org-settings-team-management)
        cur-user-data (drv/react s :current-user-data)]
    [:div.org-settings-panel
      ;; Panel rows
      [:div.org-settings-main
        ;; Org name row
        [:div.org-settings-panel-row.group
          [:div.org-settings-label
            [:label "Team name"]
            (when false
              [:label.error "Name can't be empty"])]
          [:div.org-settings-field
            [:input
              {:type "text"
               :value (:name org-editing)
               :max-length org-utils/org-name-max-length
               :on-change (fn [e]
                            (dis/dispatch! [:input [:org-editing] (merge org-editing {:name (.. e -target -value)
                                                                                      :has-changes true})]))}]]]
        ;; Email domains row
        (let [valid-domain-email? (utils/valid-domain? (:domain um-domain-invite))]
          [:div.org-settings-panel-row.email-domains-row.group
            [:div.org-settings-label
              [:label
                "Accepted email domains for registration"
                [:i.mdi.mdi-information-outline
                  {:title "Anyone who signs up with this email domain can contribute to team boards."
                   :data-toggle "tooltip"
                   :data-placement "top"}]]
              (when add-email-domain-team-error
                [:label.error
                  "Only company email domains are allowed."])]
            [:div.org-settings-field.org-settings-email-domain-field
              {:class (when add-email-domain-team-error "error")}
              [:input.um-invite-field.email
                {:name "um-domain-invite"
                 :type "text"
                 :auto-capitalize "none"
                 :value (:domain um-domain-invite)
                 :pattern "@?[a-z0-9.-]+\\.[a-z]{2,4}$"
                 :on-change #(dis/dispatch! [:input [:um-domain-invite :domain] (.. % -target -value)])
                 :placeholder "Domain, e.g. @acme.com"}]
              [:button.mlb-reset.add-email-domain-bt
                {:on-click #(let [domain (:domain um-domain-invite)]
                              (if (utils/valid-domain? domain)
                                (team-actions/email-domain-team-add
                                 (-> @(drv/get-ref s :org-settings-team-management) :um-domain-invite :domain))
                                (dis/dispatch! [:input [:add-email-domain-team-error] true])))
                 :disabled (not valid-domain-email?)}
                "Add domain"]]
            (when-not (zero? (count (:email-domains team-data)))
              [:div.org-settings-list.org-settings-email-domains-list
                (for [team (:email-domains team-data)]
                  [:div.org-settings-list-item.group
                    {:key (str "email-domain-team-" (:domain team))}
                    [:span.org-settings-list-item-name
                      (str "@" (:domain team))]
                    [:button.remove-team-btn.btn-reset
                      {:on-click #(team-actions/remove-team (:links team))
                       :title "Remove"
                       :data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"}]])])])
        ;; Slack teams row
        [:div.org-settings-panel-row.slack-teams-row.group
          [:div.org-settings-label
            [:label
              "Integrations"
              [:i.mdi.mdi-information-outline
                {:title "Anyone who signs up with your Slack team can contribute to team boards."
                 :data-toggle "tooltip"
                 :data-placement "top"}]]
            (when (seq (:access query-params))
              [:label
                {:class (if (or (= "bot" (:access query-params))
                                (= "team" (:access query-params))) "success-message" "error")}
                (cond
                  (= (:access query-params) "team-exists")
                  "This Slack team was already added."
                  (= (:access query-params) "team")
                  "Slack team was successfully added."
                  (= (:access query-params) "bot")
                  "Carrot Bot was successfully added."
                  :else
                  "An error occurred, please try again.")])]
          (when (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})
            [:button.btn-reset.add-slack-team-bt
                {:on-click #(team-actions/slack-team-add @(drv/get-ref s :current-user-data) (str (router/get-token) "?org-settings=main"))}
                [:img {:alt "Add to Slack"
                       :height "40"
                       :width "262"
                       :src (utils/cdn "/img/ML/add_a_slack_team_bt.png")
                       :src-set (str (utils/cdn "/img/ML/add_a_slack_team_bt.png") " 1x, "
                                 (utils/cdn "/img/ML/add_a_slack_team_bt@2x.png") " 2x")}]])
          (when-not (zero? (count (:slack-orgs team-data)))
            [:div.org-settings-list
              (let [slack-bots (get (jwt/get-key :slack-bots) (jwt/slack-bots-team-key (:team-id org-data)))]
                (for [team (:slack-orgs team-data)
                      :let [has-logo (seq (:logo-url team))
                            logo-url (if has-logo
                                       (:logo-url team)
                                       (utils/cdn "/img/slack.png"))
                            slack-domain (:slack-domain team)
                            has-bot? (pos? (count (filter #(= (:slack-org-id %) (:slack-org-id team)) slack-bots)))]]
                  [:div.org-settings-list-item.group
                    {:key (str "slack-org-" (:slack-org-id team))}
                    [:div.logo-container
                      [:img.slack-logo
                        {:class (when-not has-logo "no-logo")
                         :src logo-url}]]
                    [:div.org-settings-list-item-slack-org
                      [:div.org-settings-list-item-name
                        {:ref (str "slack-team-" (:slack-org-id team))}
                        (:name team)
                        [:div.integrations-more-menu
                          {:ref (str "integrations-more-menu-" (:slack-org-id team))}
                          [:button.mlb-reset.integrations-more-menu-bt
                            {:type "button"
                             :class (utils/class-set {:active (= @(::showing-menu s) (:slack-org-id team))})
                             :ref (str "integrations-more-menu-bt-" (:slack-org-id team))
                             :on-click #(reset! (::showing-menu s) (:slack-org-id team))
                             :data-toggle "tooltip"
                             :data-placement "top"
                             :data-container "body"
                             :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                             :title "More"}]
                          (when (= @(::showing-menu s) (:slack-org-id team))
                            [:ul.integrations-more-menu-list
                              [:li.delete
                                {:on-click (fn []
                                            (alert-modal/show-alert {:icon "/img/ML/trash.svg"
                                                                     :action "remove-slack-team"
                                                                     :message "Are you sure you want to remove this Slack team?"
                                                                     :link-button-title "Keep"
                                                                     :link-button-cb #(alert-modal/hide-alert)
                                                                     :solid-button-style :red
                                                                     :solid-button-title "Yes"
                                                                     :solid-button-cb (fn []
                                                                                       (team-actions/remove-team (:links team))
                                                                                       (alert-modal/hide-alert))}))}
                                "Remove Slack team"]])]]
                      (when (seq slack-domain)
                        [:div.org-settings-list-item-desc.group
                          "This team is linked to the "
                          slack-domain ".slack.com"
                          " team."])
                      [:div.slack-org-self-join
                        "Slack members can self-join this team."]
                      [:div.slack-org-bot-switch
                        "Carrot Bot for Slack is currently "
                        [:span.bold
                          (if has-bot? "on" "off")]
                        "."
                        (when-not has-bot?
                          [:i.mdi.mdi-information-outline
                            {:title "The Carrot Bot for Slack enables Slack unfurls, invites, notifications and sharing."
                             :data-toggle "tooltip"
                             :data-placement "top"
                             :data-container "body"}])
                        (when-not has-bot?
                          [:button.mlb-reset.enable-carrot-bot-bt
                            {:on-click #(org-actions/bot-auth team-data cur-user-data (str (router/get-token) "?org-settings=main"))}
                            "Enable Carrot Bot"])]]]))])]
      (when @(::show-advanced-settings s)
        [:div.org-settings-panel-row.advanced-settings-row.group
          [:div.org-settings-label
            [:label "Content visibility"]]
          [:div.advanced-settings-rows
            [:div.advanced-settings-row-item.group
              (carrot-checkbox {:selected @(::advanced-settings-1 s)
                                :disabled false
                                :did-change-cb (fn [flag]
                                                 (swap! (::advanced-settings-1 s) not)
                                                 (dis/dispatch! [:update [:org-editing] #(merge % {:has-changes true
                                                                                                   :anonymous-digest-links flag})]))})
              [:div.checkbox-label
                {:class (when-not @(::advanced-settings-1 s) "unselected")}
                "Hassle-free links in Slack and email digests that don't require a logged-in Carrot session"]]
            [:div.advanced-settings-row-item.group
              (carrot-checkbox {:selected @(::advanced-settings-2 s)
                                :disabled false
                                :did-change-cb (fn [flag]
                                                 (swap! (::advanced-settings-2 s) not)
                                                 (dis/dispatch! [:update [:org-editing] #(merge % {:has-changes true
                                                                                                   :deny-contrib-public-section-creation flag})]))})
              [:div.checkbox-label
                {:class (when-not @(::advanced-settings-2 s) "unselected")}
                "Allow contributors to create public sections"]]
            [:div.advanced-settings-row-item.group
              (carrot-checkbox {:selected @(::advanced-settings-3 s)
                                :disabled false
                                :did-change-cb (fn [flag]
                                                 (swap! (::advanced-settings-3 s) not)
                                                 (dis/dispatch! [:update [:org-editing] #(merge % {:has-changes true
                                                                                                   :deny-contrib-public-share-creation flag})]))})
              [:div.checkbox-label
                {:class (when-not @(::advanced-settings-3 s) "unselected")}
                "Allow contributors to create public share links"]]]])]

      ;; Save and cancel buttons
      [:div.org-settings-footer.group
        (when-not @(::show-advanced-settings s)
          [:button.mlb-reset.hidden-settings-bt
            {:on-click #(reset! (::show-advanced-settings s) true)}
            "Show advanced security settings"])
        [:button.mlb-reset.save-btn
          {:disabled (or @(::saving s)
                         (:saved org-editing)
                         (not (:has-changes org-editing))
                         (< (count (s/trim (:name org-editing))) 3))
           :class (when (:saved org-editing) "no-disable")
           :on-click #(when (compare-and-set! (::saving s) false true)
                        (org-actions/org-edit-save @(drv/get-ref s :org-editing)))}
          (if (:saved org-editing)
            "Saved!"
            (if @(::saving s)
              "Saving..."
              "Save"))]
        [:button.mlb-reset.cancel-btn
          {:on-click #(if (form-is-clean? s)
                        (dismiss-settings-cb)
                        (reset-form s))}
          "Cancel"]]]))