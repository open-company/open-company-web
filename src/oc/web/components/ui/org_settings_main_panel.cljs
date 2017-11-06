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
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
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
    (dis/dispatch! [:org-edit org-data])
    (dis/dispatch! [:input [:um-domain-invite :domain] ""])
    (dis/dispatch! [:input [:add-email-domain-team-error] nil])))

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
                    :action "org-settings-main-logo-upload-error"
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(dis/dispatch! [:alert-modal-hide])}]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs org-settings-main-panel
  < rum/reactive
    (rum/local false ::saving)
    (drv/drv :org-settings-team-management)
    (drv/drv :org-editing)
    (drv/drv :current-user-data)
    {:will-mount (fn [s]
                   (reset-form s)
                   s)
     :will-update (fn [s]
                    (let [org-editing @(drv/get-ref s :org-editing)]
                      (when (and @(::saving s)
                                 (:saved org-editing))
                        (reset! (::saving s) false)
                        (utils/after 2500 #(dis/dispatch! [:input [:org-editing :saved] false]))))
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
                      (dis/dispatch!
                       [:input
                        [:org-editing]
                        (merge org-editing {:logo-url nil :logo-width 0 :logo-height 0})])
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
             :data-position "top"}
            (org-avatar org-editing false false true)]
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
            (when (seq (:access query-params))
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
                      [:button.org-settings-list-item-btn.btn-reset
                        {:on-click #(dis/dispatch! [:bot-auth])
                         :title "The Carrot Slack bot enables Slack invites, assignments and sharing."
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :data-container "body"}
                        "Add Bot"]))]))]
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
                  {:title "Anyone who signs up with this email domain can contribute to team boards."
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
          {:disabled (or @(::saving s)
                         (:saved org-editing)
                         (not (:has-changes org-editing)))
           :class (when (:saved org-editing) "no-disable")
           :on-click #(do
                        (reset! (::saving s) true)
                        (dis/dispatch! [:org-edit-save]))}
          (if (:saved org-editing)
            "Saved!"
            (if @(::saving s)
              "Saving..."
              "Save"))]
        [:button.mlb-reset.mlb-link-black.cancel-btn
          {:on-click #(if (form-is-clean? s)
                        (dismiss-settings-cb)
                        (reset-form s))}
          "Cancel"]]]))