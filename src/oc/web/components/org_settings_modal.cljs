(ns oc.web.components.org-settings-modal
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [clojure.string :as str]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.email-domains :refer (email-domains)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]))

(defn close-clicked [s dismiss-action]
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
                                            (dismiss-action))}]
        (alert-modal/show-alert alert-data))
      (dismiss-action))))

(defn form-is-clean? [s]
  (let [has-org-edit-changes (:has-changes @(drv/get-ref s :org-editing))
        {:keys [um-domain-invite]} @(drv/get-ref s :org-settings-team-management)]
    (and (not has-org-edit-changes)
         (empty? (:domain um-domain-invite)))))

(defn reset-form [s]
  (let [org-data @(drv/get-ref s :org-data)
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
      (let [url (gobj/get res "url")
            img (gdom/createDom "img")]
        (set! (.-onerror img) #(logo-add-error img))
        (set! (.-onload img) #(logo-on-load org-avatar-editing url img))
        (set! (.-className img) "hidden")
        (gdom/append (.-body js/document) img)
        (set! (.-src img) url)))
    nil
    (fn [err]
      (logo-add-error nil))))

(rum/defcs org-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :team-data)
  (drv/drv :org-editing)
  (drv/drv :org-avatar-editing)
  (drv/drv :org-settings-team-management)
  ui-mixins/refresh-tooltips-mixin
  ;; Locals
  (rum/local false ::saving)
  (rum/local false ::show-advanced-settings)
  {:will-mount (fn [s]
    (let [org-data @(drv/get-ref s :org-data)]
      (org-actions/get-org org-data true))
    (reset-form s)
    (let [content-visibility-data (:content-visibility @(drv/get-ref s :org-data))]
      (reset! (::show-advanced-settings s) (some #(content-visibility-data %) (keys content-visibility-data))))
    s)
   :will-update (fn [s]
    (let [org-editing @(drv/get-ref s :org-editing)]
      (when (and @(::saving s)
                 (contains? org-editing :saved))
        (reset! (::saving s) false)
        (utils/after 2500 (fn [_] (dis/dispatch! [:update [:org-editing] #(dissoc % :saved)])))
        (notification-actions/show-notification {:title (if (:saved org-editing) "Settings saved" "Error saving, please retry")
                                                 :primary-bt-title "OK"
                                                 :primary-bt-dismiss true
                                                 :expire 3
                                                 :id :org-settings-saved})))
    s)}
  [s]
  (let [org-data (drv/react s :org-data)
        org-avatar-editing (drv/react s :org-avatar-editing)
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
      [:button.mlb-reset.modal-close-bt
        {:on-click #(close-clicked s nav-actions/close-all-panels)}]
      [:div.org-settings-modal-container
        [:div.org-settings-header
          [:div.org-settings-header-title
            "Admin settings"]
          [:button.mlb-reset.save-bt
            {:on-click #(when (compare-and-set! (::saving s) false true)
                         (org-actions/org-edit-save org-editing))
             :disabled (or @(::saving s)
                           (:saved org-editing)
                           (not (seq (:name org-editing)))
                           (< (count (str/trim (:name org-editing))) 3))
           :class (when (:saved org-editing) "no-disable")}
            "Save"]
          [:button.mlb-reset.cancel-bt
            {:on-click (fn [_] (close-clicked s #(nav-actions/show-org-settings nil)))}
            "Back"]]
        [:div.org-settings-body
          [:div.org-settings-header-avatar.group
            {:ref "org-settings-header-logo"
             :class (utils/class-set {:missing-logo (empty? (:logo-url org-avatar-editing))
                                      utils/hide-class true})
             :on-click logo-on-click}
            (org-avatar org-data-for-avatar false :never)
            [:span.edit-company-logo
              "Edit company logo"]]
          [:div.org-settings-fields
            [:div.org-settings-label
              "Company name"]
            [:input.org-settings-field.oc-input
              {:type "text"
               :class (when (:error org-editing) "error")
               :value (or (:name org-editing) "")
               :max-length org-utils/org-name-max-length
               :on-change #(let [org-name (.. % -target -value)
                                 clean-org-name (subs org-name 0 (min (count org-name) org-utils/org-name-max-length))]
                            (dis/dispatch! [:input [:org-editing] (merge org-editing {:name clean-org-name
                                                                                      :has-changes true
                                                                                      :error false
                                                                                      :rand (rand 1000)})]))}]
            (when (:error org-editing)
              [:div.error "Must be between 3 and 50 characters"])
            (email-domains)]
          (if-not @(::show-advanced-settings s)
            [:div.org-settings-advanced
              [:button.mlb-reset.advanced-settings-bt
                {:on-click (fn [_]
                              (reset! (::show-advanced-settings s) true)
                              (utils/after 1000 #(.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))}
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
                     :data-container "body"}]]]
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