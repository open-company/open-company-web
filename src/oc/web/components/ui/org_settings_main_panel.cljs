(ns oc.web.components.ui.org-settings-main-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [goog.object :as gobj]
            [goog.dom :as gdom]))

(defn reset-form [s]
  (let [org-data (first (:rum/args s))]
    (dis/dispatch! [:org-edit org-data false])
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
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(dis/dispatch! [:alert-modal-hide])}]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs org-settings-main-panel
  < rum/reactive
    (drv/drv :email-domains)
    (drv/drv :org-editing)
    (rum/local nil ::updated-at)
    {:will-mount (fn [s]
                   (reset-form s)
                   s)
     :before-render (fn [s]
                     (let [email-domains-data @(drv/get-ref s :email-domains)
                           auth-settings (:auth-settings email-domains-data)
                           teams-data-requested (:teams-data-requested email-domains-data)]
                       (when (and auth-settings
                                  (not teams-data-requested))
                         (dis/dispatch! [:teams-get])))
                     s)
     :after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     s)}
  [s org-data]
  (let [org-editing (drv/react s :org-editing)]
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
                      (iu/upload! {:accept "image/*"
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
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
                "Add a logo"
                "Change logo")]
            [:div.description "A 160x160 transparent PNG works best"]]]
        ;; Email domains row
        (let [{:keys [um-domain-invite add-email-domain-team-error team-data] :as email-domains-data} (drv/react s :email-domains)
              valid-domain-email? (utils/valid-domain? (:domain um-domain-invite))
              team-data {:email-domains [{:domain "bago.me" :links [{:rel "remove" :href "/hello" :method "DELETE"}]}
                                         {:domain "bago.space" :links [{:rel "remove" :href "/hello" :method "DELETE"}]}]}]
          [:div.org-settings-panel-row.email-domains-row.group
            [:div.org-settings-label
              [:label
                {:title "Anyone who signs up with email can contribute to team boards."
                 :data-toggle "tooltip"
                 :data-placement "top"}
                "Email Domains"]
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
                  [:span.org-settings-list-item-name (str "@" (:domain team))]
                  [:button.org-settings-list-item-remove-btn.btn-reset
                    {:on-click #(api/user-action (utils/link-for (:links team) "remove" "DELETE") nil)}
                    "Remove"]])]
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
          {:on-click #(reset-form s)}
          "Cancel"]]]))