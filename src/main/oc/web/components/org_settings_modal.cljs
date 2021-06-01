(ns oc.web.components.org-settings-modal
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [oops.core :refer (oget)]
            [cuerdas.core :as string]
            [oc.web.utils.color :as color-utils]
            [oc.lib.color :as lib-color]
            [oc.web.utils.rum :as rutils]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.theme :as theme-utils]
            [oc.web.actions.org :as org-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.email-domains :refer (email-domains)]
            [oc.web.components.ui.colors-presets :refer (colors-presets)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            ["react-color" :as react-color :refer (ChromePicker)]))

(def color-picker (partial rutils/build ChromePicker))

(def brand-colors-list [{:label "White (default)" :value "#FFFFFF" :rgb {:r 244 :g 244 :b 244}}
                        {:label "Deep navy" :value "#34414F" :rgb {:r 52 :g 65 :b 79}}
                        {:label "Blue" :value "#0000FF" :rgb {:r 0 :g 0 :b 244}}
                        {:label "Green" :value "#00FF00" :rgb {:r 0 :g 244 :b 0}}
                        {:label "Red" :value "#FF0000" :rgb {:r 244 :g 0 :b 0}}])

(defn close-clicked [s dismiss-action]
  (let [org-data @(drv/get-ref s :org-data)
        org-editing @(drv/get-ref s :org-editing)
        dismiss #(do
                   (org-utils/reset-org-editing! org-data)
                   (dismiss-action))]
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
                                            (org-utils/set-brand-color! org-editing)
                                            (dismiss))}]
        (alert-modal/show-alert alert-data))
      (dismiss))))

(defn form-is-clean? [s]
  (let [has-org-edit-changes (:has-changes @(drv/get-ref s :org-editing))
        {:keys [um-domain-invite]} @(drv/get-ref s :org-settings-team-management)]
    (and (not has-org-edit-changes)
         (empty? (:domain um-domain-invite)))))

(defn reset-form [s]
  (let [org-data @(drv/get-ref s :org-data)]
    (org-actions/org-edit-setup org-data)
    (dis/dispatch! [:input [:um-domain-invite :domain] ""])
    (dis/dispatch! [:input [:add-email-domain-team-error] nil])))

(defn- on-change [k v & [error?]]
  (dis/dispatch! [:update [:org-editing] #(merge % {k v
                                                    :has-changes true
                                                    :error (if error?
                                                             (conj (set (:error %)) k)
                                                             (disj (set (:error %)) k))})]))

(defn- change-content-visibility [content-visibility-data k v]
  (let [new-content-visibility (merge content-visibility-data {k v})]
    (on-change :content-visibility new-content-visibility)))

(defn logo-on-load [_ url img]
  (org-actions/org-avatar-edit-save {:logo-url url})
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

(defn logo-on-click [org-avatar-editing]
  (iu/upload! org-utils/org-avatar-filestack-config
    (fn [res]
      (let [url (oget res "url")
            img (gdom/createDom "img")]
        (set! (.-onerror img) #(logo-add-error img))
        (set! (.-onload img) #(logo-on-load org-avatar-editing url img))
        (set! (.-className img) "hidden")
        (gdom/append (.-body js/document) img)
        (set! (.-src img) url)))
    nil
    (fn [_]
      (logo-add-error nil))))

(defn- change-brand-color [primary-color secondary-color]
  (let [color-map {:primary primary-color :secondary secondary-color}
        new-brand-color {:light color-map :dark color-map}]
    (on-change :brand-color new-brand-color)
    (org-utils/set-brand-color! color-map)))

(defn- theme-preview [brand-color theme]
  (let [color-map (get brand-color theme)
        hex (-> color-map :primary :hex)
        text-color (-> color-map :secondary :hex)]
    [:div.theme-preview
     {:class (str (name theme) "-preview")}
     [:div.theme-background]
     [:div.sample-link
      {:style {:color hex}}
      "Sample text link"]
     [:button.mlb-reset.sample-button
      {:style {:background-color hex
               :color text-color}}
      "Button text"]]))

(rum/defcs org-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :theme)
  (drv/drv :org-data)
  (drv/drv :team-data)
  (drv/drv :org-editing)
  (drv/drv :org-avatar-editing)
  (drv/drv :org-settings-team-management)
  ui-mixins/strict-refresh-tooltips-mixin
  ;; Locals
  (rum/local false ::saving)
  (rum/local false ::show-advanced-settings)
  (rum/local false ::show-color-picker)
  (rum/local false ::primary-color-value)
  ;; Mixins
  (ui-mixins/on-click-out :color-picker-container
   (fn [s _]
     (when @(::show-color-picker s)
       (reset! (::show-color-picker s) false))))
  {:will-mount (fn [s]
    (let [org-data @(drv/get-ref s :org-data)
          content-visibility-data (:content-visibility org-data)
          theme @(drv/get-ref s :theme)
          current-theme (theme-utils/computed-value theme)]
      (org-actions/get-org org-data true)
      (reset-form s)
      ;; Auto-expand the content visibility settings (aka advanced settings)
      ;; only if the org tweaked them from default values:
      ;; {:disallow-public-board false
      ;;  :disallow-public-share false
      ;;  :disallow-secure-links false}
      (reset! (::show-advanced-settings s) (some content-visibility-data (keys content-visibility-data)))
      (reset! (::primary-color-value s) (-> org-data :brand-color current-theme :primary :hex)))
    s)
   :will-update (fn [s]
    (let [org-editing @(drv/get-ref s :org-editing)]
      (when (and @(::saving s)
                 (or (contains? org-editing :saved)
                     (contains? org-editing :error)))
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
        theme-data (drv/react s :theme)
        org-avatar-editing (drv/react s :org-avatar-editing)
        org-data-for-avatar (merge org-data org-avatar-editing)
        org-editing (drv/react s :org-editing)
        is-mobile? (responsive/is-mobile-size?)
        _team-management-data (drv/react s :org-settings-team-management)
        content-visibility-data (or (:content-visibility org-editing) {})
        current-theme (theme-utils/computed-value theme-data)
        current-brand-color (get (:brand-color org-editing) current-theme)
        premium-lock? (not (:premium? org-data))
        premium-tooltip* "Premium accounts can customize logo and team colors. Click for details."
        premium-tooltip (str premium-tooltip* " Click for details.")
        premium-lock-click (when premium-lock?
                             (fn [e]
                               (utils/event-stop e)
                               (nav-actions/toggle-premium-picker! premium-tooltip*)))
        save-disabled? (or @(::saving s)
                           (:saved org-editing)
                           (not (<= org-utils/org-name-min-length (count (:name org-editing)) org-utils/org-name-max-length))
                           (not (<= org-utils/new-entry-cta-min-length (count (:new-entry-cta org-editing)) org-utils/new-entry-cta-max-length))
                           (not (<= org-utils/new-entry-placeholder-min-length (count (:new-entry-placeholder org-editing)) org-utils/new-entry-placeholder-max-length)))]
    [:div.org-settings-modal.fields-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(close-clicked s nav-actions/close-all-panels)}]
      [:div.org-settings-modal-container
        [:div.org-settings-header
          [:div.org-settings-header-title
            "Admin settings"]
          [:button.mlb-reset.save-bt
            {:on-click #(when (compare-and-set! (::saving s) false true)
                         (org-actions/org-edit-save org-editing))
             :disabled save-disabled?
             :class (when (:saved org-editing) "no-disable")}
            "Save"]
          [:button.mlb-reset.cancel-bt
            {:on-click (fn [_] (close-clicked s #(nav-actions/show-org-settings nil)))}
            "Back"]]
        [:div.org-settings-body
          [:div.org-settings-header-avatar.group
            {:ref "org-settings-header-logo"
             :class (utils/class-set {:missing-logo (empty? (:logo-url org-avatar-editing))
                                      utils/hide-class true
                                      :premium-lock premium-lock?})
             :data-toggle (when (and (not is-mobile?)
                                     premium-lock?)
                            "tooltip")
             :title (when premium-lock?
                      premium-tooltip)
             :data-placement "top"
             :data-container "body"
             :on-click (if premium-lock?
                         premium-lock-click
                         logo-on-click)}
            (org-avatar org-data-for-avatar false :never)
            [:span.edit-company-logo
              "Edit company logo"]]
          [:div.org-settings-fields.field-group
            [:div.field-label
              "Company name"]
            [:input.field-value.oc-input
              {:type "text"
               :class (when (:name (:error org-editing)) "error")
               :value (or (:name org-editing) "")
               :min-length org-utils/org-name-min-length
               :max-length org-utils/org-name-max-length
               :on-change #(let [org-name (.. % -target -value)
                                 error? (not (<= org-utils/org-name-min-length (count org-name) org-utils/org-name-max-length))]
                             (on-change :name org-name error?))}]
            (when (:name (:error org-editing))
              [:div.error (str "Must be between " org-utils/org-name-min-length " and " org-utils/org-name-max-length " characters")])
            (email-domains)]
          [:div.org-settings-fields.field-group
           {:class (utils/class-set {:premium-lock premium-lock?})
            :data-toggle (when (and (not is-mobile?)
                                    premium-lock?)
                           "tooltip")
            :title (when premium-lock?
                     premium-tooltip)
            :data-placement "top"
            :data-container "body"
            :on-click premium-lock-click}
           [:div.field-label
            "Customize your team's colors"]
           [:div.field-description
            "Button/link color"]
           [:input.field-value.oc-input
            {:type "text"
             :value @(::primary-color-value s)
             :pattern lib-color/valid-color?
             :placeholder "Ie: red, green or #0000ff"
             :on-focus #(reset! (::show-color-picker s) true)
             :on-change (fn [e]
                          (let [v (string/lower (.. e -target -value))]
                            (reset! (::primary-color-value s) v)
                            (when (.. e -target checkValidity)
                              (let [is-hex-color? (lib-color/valid-hex-color? v)
                                    hex-color (if is-hex-color? v (-> v keyword lib-color/default-css-color-names))
                                    rgb-color (lib-color/hex->rgb hex-color)]
                                (change-brand-color {:hex hex-color :rgb rgb-color} (:secondary current-brand-color))))))}]
           (when @(::show-color-picker s)
             [:div.color-picker-container
              {:ref :color-picker-container}
              (color-picker {:color @(::primary-color-value s) ;; (-> current-brand-color :primary :hex)
                             :onChangeComplete (fn [color]
                                                 (when color
                                                   (let [hex-color (oget color "?hex")
                                                         rgb-colors (-> color (oget "?rgb") (js->clj :keywordize-keys true) (select-keys [:r :g :b]))]
                                                     (reset! (::primary-color-value s) hex-color)
                                                     (change-brand-color {:hex hex-color :rgb rgb-colors} (:secondary current-brand-color)))))})])
           [:div.field-description.colors-preset.group
            [:div.colors-list.group
             (colors-presets {:color-list color-utils/colors-presets-list
                              :current-selected (-> current-brand-color :primary :hex)
                              :on-change-cb (fn [c]
                                              (reset! (::primary-color-value s) (:hex c))
                                              (change-brand-color (select-keys c [:hex :rgb])
                                                                  (:secondary current-brand-color)))})]]
           [:div.field-description
            "Button text color"]
           [:select.oc-input.field-value.button-text-color
            {:value (-> current-brand-color :secondary :hex)
             :on-change (fn [e]
                          (let [v (.. e -target -value)
                                color-data (some #(when (= (:value %) v) %) brand-colors-list)]
                            (change-brand-color (:primary current-brand-color) {:hex (:value color-data) :rgb (:rgb color-data)})))}
            (for [c brand-colors-list]
              [:option
               {:key (str "button-text-color-" (:value c))
                :value (:value c)}
               (:label c)])]
           [:div.theme-previews
            {:class (if (= current-theme :light) "on-light-theme" "on-dark-theme")}
            (theme-preview (:brand-color org-editing) :light)
            (theme-preview (:brand-color org-editing) :dark)]]
          ;; Prompts copy
          [:div.org-settings-fields.field-group
           [:div.field-label
            "Change prompt and button copy"]
           [:div.field-description
            "Prompt"]
           [:input.field-value.oc-input
            {:type "text"
             :class (when (:new-entry-placeholder (:error org-editing)) "error")
             :value (or (:new-entry-placeholder org-editing) "")
             :min-length org-utils/new-entry-placeholder-min-length
             :max-length org-utils/new-entry-placeholder-max-length
             :placeholder "\"Start a new discussion...\", \"What's new...\", etc"
             :on-change #(let [entry-placeholder (.. % -target -value)
                               error? (not (<= org-utils/new-entry-placeholder-min-length (count entry-placeholder) org-utils/new-entry-placeholder-max-length))]
                           (on-change :new-entry-placeholder entry-placeholder error?))}]
            (when (:new-entry-placeholder (:error org-editing))
              [:div.error (str "Must be between " org-utils/new-entry-placeholder-min-length " and " org-utils/new-entry-placeholder-max-length " characters")])
           [:div.field-description
            "Button"]
           [:input.field-value.oc-input
            {:type "text"
             :class (when (:new-entry-cta (:error org-editing)) "error")
             :value (or (:new-entry-cta org-editing) "")
             :min-length org-utils/new-entry-cta-min-length
             :max-length org-utils/new-entry-cta-max-length
             :placeholder "\"NEW\", \"New update\", \"Post\", etc"
             :on-change #(let [entry-cta (.. % -target -value)
                               error? (not (<= org-utils/new-entry-cta-min-length (count entry-cta) org-utils/new-entry-cta-max-length))]
                           (on-change :new-entry-cta entry-cta error?))}]
           (when (:new-entry-cta (:error org-editing))
             [:div.error (str "Must be between " org-utils/new-entry-cta-min-length " and " org-utils/new-entry-cta-max-length " characters")])]
          ;; Advanced section
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
              (let [did-change-cb #(change-content-visibility content-visibility-data :disallow-secure-links (not (:disallow-secure-links content-visibility-data)))]
                [:div.org-settings-advanced-row.digest-links.group
                  (carrot-checkbox {:selected (not (:disallow-secure-links content-visibility-data))
                                    :disabled false
                                    :did-change-cb did-change-cb})
                  [:div.checkbox-label
                    {:class (when (:disallow-secure-links content-visibility-data) "unselected")
                    :on-click did-change-cb}
                    "Allow secure links to open posts from email or Slack"
                    [:i.mdi.mdi-information-outline
                      {:title (str
                              "When team members receive " ls/product-name " posts via an email or Slack morning digest, secure "
                              "links allow them to read the post without first logging in. A login is still required "
                              "to access additional posts. If you turn off secure links, your team will always need to "
                              "be logged in to view posts.")
                      :data-toggle (when-not is-mobile? "tooltip")
                      :data-placement "top"
                      :data-container "body"}]]])
              (let [did-change-cb (when-not premium-lock?
                                    #(change-content-visibility content-visibility-data :disallow-public-board (not (:disallow-public-board content-visibility-data))))]
                [:div.org-settings-advanced-row.public-sections.group
                 {:class (utils/class-set {:premium-lock premium-lock?})
                  :on-click premium-lock-click}
                 (carrot-checkbox {:selected (not (:disallow-public-board content-visibility-data))
                                   :disabled false
                                   :did-change-cb did-change-cb})
                 [:div.checkbox-label
                  {:class (when (:disallow-public-board content-visibility-data) "unselected")
                   :on-click did-change-cb}
                  "Allow public topics"]])
              (let [did-change-cb #(change-content-visibility content-visibility-data :disallow-public-share (not (:disallow-public-share content-visibility-data)))]
                [:div.org-settings-advanced-row.public-share.group
                  (carrot-checkbox {:selected (not (:disallow-public-share content-visibility-data))
                                    :disabled false
                                    :did-change-cb did-change-cb})
                  [:div.checkbox-label
                    {:class (when (:disallow-public-share content-visibility-data) "unselected")
                    :on-click did-change-cb}
                    "Allow public share links"]])
              (let [did-change-cb #(change-content-visibility content-visibility-data :disallow-wrt-download (not (:disallow-wrt-download content-visibility-data)))]
                [:div.org-settings-advanced-row.wrt-download.group
                 (carrot-checkbox {:selected (not (:disallow-wrt-download content-visibility-data))
                                   :disabled false
                                   :did-change-cb did-change-cb})
                 [:div.checkbox-label
                  {:class (when (:disallow-wrt-download content-visibility-data) "unselected")
                   :on-click did-change-cb}
                  "Allow analytics download"
                  [:i.mdi.mdi-information-outline
                   {:title (str
                            "When team members open the post analytics view relative to a post, this "
                            "link will allow them to download the info in a excel compatible format.")
                    :data-toggle (when-not is-mobile? "tooltip")
                    :data-placement "top"
                    :data-container "body"}]]])])]]]))