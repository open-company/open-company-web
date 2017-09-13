(ns oc.web.components.ui.org-settings-main-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [goog.object :as gobj]
            [goog.dom :as gdom]))

(defn reset-form [s keep?]
  (let [org-data (first (:rum/args s))
        um-domain-invite (:um-domain-invite @(drv/get-ref s :org-settings-team-management))
        domain (if (and keep?
                        um-domain-invite
                        (:domain um-domain-invite))
                  (:domain um-domain-invite)
                  "")]
    (dis/dispatch! [:org-edit org-data keep?])
    (dis/dispatch! [:input [:um-domain-invite :domain] domain])
    (when-not keep?
      (dis/dispatch! [:input [:add-email-domain-team-error] nil]))))

(defn logo-on-load [org-data url img]
  (dis/dispatch! [:input [:org-editing] (merge org-data {:has-changes true
                                                         :logo-url url
                                                         :logo-width (.-width img)
                                                         :logo-height (.-height img)})])
  (gdom/removeNode img))

(defn logo-add-error
  "Show an error alert view for failed uploads."
  []
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(dis/dispatch! [:alert-modal-hide])}]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs org-settings-main-panel
  < rum/reactive
    (drv/drv :org-settings-team-management)
    (drv/drv :org-editing)
    (drv/drv :current-user-data)
    {:will-mount (fn [s]
                   (reset-form s true)
                   s)
     :after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     s)}
  [s org-data]
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
            [:label "Team Name"]
            (when false
              [:label.error "Name can't be empty"])]
          [:div.org-settings-field
            [:input
              {:type "text"
               :value (:name org-editing)
               :on-change (fn [e]
                            (dis/dispatch! [:input [:org-editing] (merge org-editing {:name (.. e -target -value)
                                                                                      :has-changes true})]))}]]]
        ;; Org logo row
        [:div.org-settings-panel-row.org-logo-row.group
          {:on-click (fn [_]
                      (iu/upload! {:accept "image/*"}
                      (fn [res]
                        (let [url (gobj/get res "url")
                              img (gdom/createDom "img")]
                          (set! (.-onload img) #(logo-on-load org-editing url img))
                          (set! (.-className img) "hidden")
                          (gdom/append (.-body js/document) img)
                          (set! (.-src img) url)))
                      nil
                      (fn [err]
                        (logo-add-error))))}
          [:div.org-logo-container
            {:title (if (empty? (:logo-url org-editing))
                      "Add a logo"
                      "Change logo")
             :data-toggle "tooltip"
             :data-container "body"
             :data-position "top"
             :class (when (empty? (:logo-url org-editing)) "no-logo")}
            [:img.org-logo
              {:src (if (empty? (:logo-url org-editing))
                      (utils/cdn "/img/ML/carrot_grey.svg")
                      (:logo-url org-editing))}]]
          [:div.org-logo-label
            [:div.cta
              (if (empty? (:logo-url org-editing))
                "Upload logo"
                "Change logo")]
            [:div.description "A transparent background PNG works best"]]]
        ;; Slack teams row
        [:div.org-settings-panel-row.slack-teams-row.group
          [:div.org-settings-label
            [:label
              "Slack Teams"
              [:i.mdi.mdi-information-outline
                {:title "Anyone who signs up with your Slack team can contribute to team boards."
                 :data-toggle "tooltip"
                 :data-placement "top"}]]
            (when (not (empty? (:access query-params)))
              [:label
                {:class (if (or (= "bot" (:access query-params))
                                (= "team" (:access query-params))) "success-message" "error")}
                (cond
                  (= (:access query-params) "team-exists")
                  "This team was already added."
                  (= (:access query-params) "team")
                  "Team successfully added."
                  (= (:access query-params) "bot")
                  "Bot successfully added."
                  :else
                  "An error occurred, please try again.")])]
          [:div.org-settings-list
            (let [slack-bots (get (jwt/get-key :slack-bots) (jwt/slack-bots-team-key (:team-id org-data)))]
              (for [team (:slack-orgs team-data)]
                [:div.org-settings-list-item.group
                  {:key (str "slack-org-" (:slack-org-id team))}
                  [:label.org-settings-list-item-name
                    [:img.slack-logo {:src (utils/cdn "/img/slack.png")}]
                    (:name team)
                    [:button.remove-team-btn.btn-reset
                      {:on-click #(api/user-action (utils/link-for (:links team) "remove" "DELETE") nil)}
                      "Remove Slack team"]]
                  (when (zero? (count (filter #(= (:slack-org-id %) (:slack-org-id team)) slack-bots)))
                    (when-let [add-bot-link (utils/link-for (:links team-data) "bot" "GET" {:auth-source "slack"})]
                      (let [fixed-add-bot-link (utils/slack-link-with-state (:href add-bot-link) (:user-id cur-user-data) (:team-id org-data) (oc-urls/org-settings (:slug org-data)))]
                        [:button.org-settings-list-item-btn.btn-reset
                          {:on-click #(router/redirect! fixed-add-bot-link)
                           :title "The Carrot Slack bot enables Slack invites, assignments and sharing."
                           :data-toggle "tooltip"
                           :data-placement "top"
                           :data-container "body"}
                          "Add Bot"])))]))]
          (when (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})
            [:button.btn-reset.add-slack-team-bt
                {:on-click #(dis/dispatch! [:slack-team-add])}
                (str "Add "
                     (if (zero? (count (:slack-orgs team-data)))
                        "A"
                        "Another")
                     " Slack Team")])]
        ;; Email domains row
        (let [valid-domain-email? (utils/valid-domain? (:domain um-domain-invite))]
          [:div.org-settings-panel-row.email-domains-row.group
            [:div.org-settings-label
              [:label
                "Email Domains"
                [:i.mdi.mdi-information-outline
                  {:title "Anyone who signs up with email can contribute to team boards."
                   :data-toggle "tooltip"
                   :data-placement "top"}]]
              (when add-email-domain-team-error
                [:label.error
                  (cond
                    (and (= add-email-domain-team-error :domain-exists)
                         (:domain um-domain-invite))
                    (str (:domain um-domain-invite) " was already added.")
                    :else
                    "An error occurred, please try again.")])]
            [:div.org-settings-list
              (for [team (:email-domains team-data)]
                [:div.org-settings-list-item.group
                  {:key (str "email-domain-team-" (:domain team))}
                  [:span.org-settings-list-item-name (str "@" (:domain team))]
                  [:button.remove-team-btn.btn-reset
                    {:on-click #(api/user-action (utils/link-for (:links team) "remove" "DELETE") nil)}
                    "Remove email domain"]])]
            [:div.org-settings-field
              {:class (when add-email-domain-team-error "error")}
              [:input.um-invite-field.email
                {:name "um-domain-invite"
                 :type "text"
                 :auto-capitalize "none"
                 :value (:domain um-domain-invite)
                 :pattern "@?[a-z0-9.-]+\\.[a-z]{2,4}$"
                 :on-change #(dis/dispatch! [:input [:um-domain-invite :domain] (.. % -target -value)])
                 :placeholder "Domain, e.g. @amc.com"}]]
            [:button.mlb-reset.mlb-default.add-email-domain-bt
              {:on-click #(let [domain (:domain um-domain-invite)]
                            (if (utils/valid-domain? domain)
                              (dis/dispatch! [:email-domain-team-add])
                              (dis/dispatch! [:input [:add-email-domain-team-error] true])))
               :disabled false} ;(not valid-domain-email?)}
              "Add"]])]

      ;; Save and cancel buttons
      [:div.org-settings-footer.group
        [:button.mlb-reset.mlb-default.save-btn
          {:disabled (not (:has-changes org-editing))
           :on-click #(dis/dispatch! [:org-edit-save])}
          "Save"]
        [:button.mlb-reset.mlb-link-black.cancel-btn
          {:on-click #(reset-form s false)}
          "Cancel"]]]))