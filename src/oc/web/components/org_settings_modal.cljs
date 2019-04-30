(ns oc.web.components.org-settings-modal
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [clojure.string :as str]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.qsg-breadcrumb :refer (qsg-breadcrumb)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]))

(defn real-close []
  (nav-actions/show-org-settings nil))

(defn dismiss-modal [& [s]]
  (if s
    (reset! (::unmounting s) true)
    (real-close)))

(defn close-clicked [s]
  (let [org-editing @(drv/get-ref s :org-editing)]
    (if (:has-changes org-editing)
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "org-settings-unsaved-edits"
                        :message "Leave without saving your changes?"
                        :link-button-title "Stay"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Lose changes"
                        :solid-button-cb #(do
                                            (alert-modal/hide-alert)
                                            (dismiss-modal s))}]
        (alert-modal/show-alert alert-data))
      (dismiss-modal))))

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

(defn- change-content-visibility [content-visibility-data k v]
  (let [new-content-visibility (merge content-visibility-data {k v})]
    (dis/dispatch! [:update [:org-editing] #(merge % {:has-changes true
                                                      :content-visibility new-content-visibility})])))

(defn logo-on-load [org-avatar-editing url img]
  (org-actions/org-avatar-edit-save {:logo-url url
                                     :logo-width (.-width img)
                                     :logo-height (.-height img)})
  (gdom/removeNode img))

(defn logo-add-error
  "Show an error alert view for failed uploads."
  [img]
  (notification-actions/show-notification
   {:title "Image upload error"
    :description "An error occurred while processing your company avatar. Please retry."
    :expire 3
    :id :org-avatar-upload-failed
    :dismiss true})
  (when img
    (gdom/removeNode img)))

(defn- update-tooltip [s]
  (utils/after 100
   #(let [header-logo (rum/ref-node s "org-settings-header-logo")
          $header-logo (js/$ header-logo)
          org-avatar-editing @(drv/get-ref s :org-avatar-editing)
          title (if (empty? (:logo-url org-avatar-editing))
                  "Add a logo"
                  "Change logo")]
      (.tooltip $header-logo #js {:title title
                                  :trigger "hover focus"
                                  :position "top"
                                  :container "body"}))))

(defn logo-on-click [org-avatar-editing]
  (iu/upload! org-utils/org-avatar-filestack-config
    (fn [res]
      (qsg-actions/finish-company-logo-trail)
      (let [url (gobj/get res "url")
            img (gdom/createDom "img")]
        (set! (.-onerror img) #(logo-add-error img))
        (set! (.-onload img) #(logo-on-load org-avatar-editing url img))
        (set! (.-className img) "hidden")
        (gdom/append (.-body js/document) img)
        (set! (.-src img) url)))
    nil
    (fn [err]
      (qsg-actions/finish-company-logo-trail)
      (logo-add-error nil))))

(defn email-domain-removed [success?]
  (notification-actions/show-notification
   {:title (if success? "Email domain successfully removed" "Error")
    :description (when-not success? "An error occurred while removing the email domain, please try again.")
    :expire 3
    :id (if success? :email-domain-remove-success :email-domain-remove-error)
    :dismiss true}))

(rum/defcs org-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :qsg)
  (drv/drv :org-data)
  (drv/drv :team-data)
  (drv/drv :org-editing)
  (drv/drv :org-avatar-editing)
  (drv/drv :org-settings-team-management)
  ;; Locals
  (rum/local false ::unmounting)
  (rum/local false ::unmounted)
  (rum/local false ::saving)
  (rum/local false ::show-advanced-settings)
  ;; Mixins
  mixins/no-scroll-mixin
  mixins/first-render-mixin
  {:will-mount (fn [s]
    (let [org-data @(drv/get-ref s :org-data)]
      (org-actions/get-org org-data)
      (team-actions/force-team-refresh (:team-id org-data)))
    (reset-form s)
    (let [content-visibility-data (:content-visibility @(drv/get-ref s :org-data))]
      (reset! (::show-advanced-settings s) (some #(content-visibility-data %) (keys content-visibility-data))))
    s)
   :did-mount (fn [s]
    (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
    s)
   :did-update (fn [s]
    (when (and @(::unmounting s)
               (compare-and-set! (::unmounted s) false true))
      (utils/after 180 real-close))
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
    s)}
  [s]
  (let [org-data (drv/react s :org-data)
        appear-class (and @(:first-render-done s)
                          (not @(::unmounting s))
                          (not @(::unmounted s)))
        org-avatar-editing (drv/react s :org-avatar-editing)
        qsg-data (drv/react s :qsg)
        org-data-for-avatar (merge org-data org-avatar-editing)
        org-editing (drv/react s :org-editing)
        is-tablet-or-mobile? (responsive/is-tablet-or-mobile?)
        {:keys [query-params
                um-domain-invite
                add-email-domain-team-error
                team-data]
         :as team-management-data}
                    (drv/react s :org-settings-team-management)
        content-visibility-data (or (:content-visibility org-editing) {})]
    [:div.org-settings-modal
      {:class (utils/class-set {:appear appear-class})}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(close-clicked s)}]
      [:div.org-settings-modal-container
        [:div.org-settings-header
          [:div.org-settings-header-title
            "Admin settings"]
          [:button.mlb-reset.save-bt
            {:on-click #(when (compare-and-set! (::saving s) false true)
                         (org-actions/org-edit-save org-editing))
             :disabled (or @(::saving s)
                           (:saved org-editing)
                           (and (seq (:name org-editing))
                                (< (count (str/trim (:name org-editing))) 3)))
           :class (when (:saved org-editing) "no-disable")}
            "Save"]
          [:button.mlb-reset.cancel-bt
            {:on-click #(close-clicked s)}
            "Back"]]
        [:div.org-settings-body
          [:div.org-settings-header-avatar.qsg-company-logo-3.group
            {:ref "org-settings-header-logo"
             :class (utils/class-set {:missing-logo (empty? (:logo-url org-avatar-editing))
                                      utils/hide-class true})
             :on-click logo-on-click}
            (when (= (:step qsg-data) :company-logo-3)
              (qsg-breadcrumb qsg-data))
            (org-avatar org-data-for-avatar false :never)
            [:span.edit-company-logo
              "Edit company logo"]]
          [:div.org-settings-fields
            [:div.org-settings-label
              "Company name"]
            [:input.org-settings-field
              {:type "text"
              :value (or (:name org-editing) "")
              :on-change #(dis/dispatch! [:input [:org-editing] (merge org-editing {:name (.. % -target -value)
                                                                                    :has-changes true})])}]
            [:div.org-settings-desc
              (str ls/web-server-domain "/" (:slug org-data))]
            [:div.org-settings-label
              "Allowed email domains"
              [:i.mdi.mdi-information-outline
                {:title "Any user that signs up with an allowed email domain and verifies their email address will have contributor access to your team."
                 :data-toggle (when-not is-tablet-or-mobile? "tooltip")
                 :data-placement "top"
                 :data-container "body"
                 :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
            [:input.org-settings-field
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
                                     (team-actions/email-domain-team-add domain)))))}]
            [:div.org-settings-email-domains
              (for [domain (:email-domains team-data)]
                [:div.org-settings-email-domain-row
                  {:key (str "org-settings-domain-" domain)}
                  (str "@" (:domain domain))
                  (when (utils/link-for (:links domain) "remove")
                    [:button.mlb-reset.remove-email-bt
                      {:on-click #(team-actions/remove-team (:links domain) email-domain-removed)}
                      "Remove"])])]]
          (if-not @(::show-advanced-settings s)
            [:div.org-settings-advanced
              [:button.mlb-reset.advanced-settings-bt
                {:on-click #(reset! (::show-advanced-settings s) true)}
                "Show advanced settings"]]
            [:div.org-settings-advanced
              [:div.org-settings-advanced-title
                "Advanced settings"]
              [:div.org-settings-advanced-row.digest-links.group
                (carrot-checkbox {:selected (:disallow-secure-links content-visibility-data)
                                  :disabled false
                                  :did-change-cb #(change-content-visibility content-visibility-data :disallow-secure-links %)})
                [:div.checkbox-label
                  {:class (when-not (:disallow-secure-links content-visibility-data) "unselected")
                   :on-click #(change-content-visibility content-visibility-data :disallow-secure-links (not (:disallow-secure-links content-visibility-data)))}
                  "Do not allow secure links to open posts from email or Slack"
                  [:i.mdi.mdi-information-outline
                    {:title (str
                             "When team members receive Carrot posts via an email or Slack morning digest, secure "
                             "links allow them to read the post without first logging in. A login is still required "
                             "to access additional posts. If you turn off secure links, your team will always need to "
                             "be logged in to view posts.")
                     :data-toggle (when-not is-tablet-or-mobile? "tooltip")
                     :data-placement "top"
                     :data-container "body"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
              [:div.org-settings-advanced-row.public-sections.group
                (carrot-checkbox {:selected (:disallow-public-board content-visibility-data)
                                  :disabled false
                                  :did-change-cb #(change-content-visibility content-visibility-data :disallow-public-board %)})
                [:div.checkbox-label
                  {:class (when-not (:disallow-public-board content-visibility-data) "unselected")
                   :on-click #(change-content-visibility content-visibility-data :disallow-public-board (not (:disallow-public-board content-visibility-data)))}
                  "Do not allow public sections"]]
              [:div.org-settings-advanced-row.public-share.group
                (carrot-checkbox {:selected (:disallow-public-share content-visibility-data)
                                  :disabled false
                                  :did-change-cb #(change-content-visibility content-visibility-data :disallow-public-share %)})
                [:div.checkbox-label
                  {:class (when-not (:disallow-public-share content-visibility-data) "unselected")
                   :on-click #(change-content-visibility content-visibility-data :disallow-public-share (not (:disallow-public-share content-visibility-data)))}
                  "Do not allow public share links"]]])]]]))