(ns oc.web.components.org-settings-modal
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [oops.core :refer (oget oset!)]
            [cuerdas.core :as string]
            [oc.web.lib.react-utils :as rutils]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.org :as org-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.ui-theme :as ui-theme]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.email-domains :refer (email-domains)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            ["react-color" :as react-color]))

(def color-picker (partial rutils/build react-color/ChromePicker))

(def color-presets [{:rgb {:r 251 :g 94 :b 72}
                     :hex "#FB5E48"}
                    {:rgb {:r 248 :g 154 :b 68}
                     :hex "#F89A44"}
                    {:rgb {:r 63 :g 189 :b 124}
                     :hex "#3FBD7C"}
                    {:rgb {:r 105 :g 184 :b 171}
                     :hex "#69B8AB"}
                    {:rgb {:r 97 :g 135 :b 248}
                     :hex "#6187F8"}
                    {:rgb {:r 104 :g 51 :b 241}
                     :hex "#6833F1"}])

(def brand-colors-list [{:label "White (default)" :value "#FFFFFF" :rgb {:r 244 :g 244 :b 244}}
                        {:label "Deep navy" :value "#34414F" :rgb {:r 52 :g 65 :b 79}}
                        {:label "Blue" :value "#0000FF" :rgb {:r 0 :g 0 :b 244}}
                        {:label "Green" :value "#00FF00" :rgb {:r 0 :g 244 :b 0}}
                        {:label "Red" :value "#FF0000" :rgb {:r 244 :g 0 :b 0}}])

(defn close-clicked [s dismiss-action]
  (let [org-data @(drv/get-ref s :org-data)
        org-editing @(drv/get-ref s :org-editing)
        dismiss #(do
                   (org-utils/set-brand-color! org-data)
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
      (let [url (oget res "url")
            img (gdom/createDom "img")]
        (set! (.-onerror img) #(logo-add-error img))
        (set! (.-onload img) #(logo-on-load org-avatar-editing url img))
        (set! (.-className img) "hidden")
        (gdom/append (.-body js/document) img)
        (set! (.-src img) url)))
    nil
    (fn [err]
      (logo-add-error nil))))

(defn- change-brand-color [primary-color secondary-color]
  (let [color-map {:primary primary-color :secondary secondary-color}
        new-brand-color {:light color-map :dark color-map}]
    (dis/dispatch! [:update [:org-editing] #(merge % {:brand-color new-brand-color :has-changes true})])
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
  (drv/drv :org-data)
  (drv/drv :team-data)
  (drv/drv :org-editing)
  (drv/drv :org-avatar-editing)
  (drv/drv :org-settings-team-management)
  ui-mixins/refresh-tooltips-mixin
  ;; Locals
  (rum/local false ::saving)
  (rum/local false ::show-advanced-settings)
  (rum/local false ::show-color-picker)
  ;; Mixins
  (ui-mixins/on-click-out :color-picker-container
   (fn [s e]
     (when @(::show-color-picker s)
       (reset! (::show-color-picker s) false))))
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
        content-visibility-data (or (:content-visibility org-editing) {})
        current-theme (ui-theme/computed-theme)
        current-brand-color (get (:brand-color org-editing) current-theme)]
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
             :disabled (or @(::saving s)
                           (:saved org-editing)
                           (not (seq (:name org-editing)))
                           (< (count (string/trim (:name org-editing))) 3))
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
          [:div.org-settings-fields.field-group
            [:div.field-label
              "Company name"]
            [:input.field-value.oc-input
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
          [:div.org-settings-fields.field-group
            [:div.field-label
              "Customize colors"]
            [:div.field-description
              "Choose the color of buttons and links."]
            [:div.field-label
              "Button/link color"]
            [:input.field-value.oc-input
             {:type "text"
              :class (when (:error org-editing) "error")
              :value (-> current-brand-color :primary :hex)
              :pattern #"(?i)^[0-9A-Z]{6}$"
              :read-only true
              :on-click #(reset! (::show-color-picker s) true)}]
            (when @(::show-color-picker s)
              [:div.color-picker-container
                {:ref :color-picker-container}
                (color-picker {:color (-> current-brand-color :primary :hex)
                               :onChangeComplete (fn [color]
                                                   (when color
                                                     (let [hex-color (oget color "?hex")
                                                           rgb-colors (-> color (oget "?rgb") (js->clj :keywordize-keys true) (select-keys [:r :g :b]))]
                                                       (change-brand-color {:hex hex-color :rgb rgb-colors} (:secondary current-brand-color)))))})])
            [:div.field-description.colors-preset.group
             [:span.color-preset-label "Presets:"]
             [:div.colors-list.group
              (for [c color-presets
                    :let [active? (= (string/lower (-> current-brand-color :primary :hex)) (string/lower (:hex c)))]]
                [:button.mlb-reset.color-preset-bt
                  {:key (str "color-preset-" (:hex c))
                   :on-click #(change-brand-color c (:secondary current-brand-color))
                   :class (when active? "active")}
                 [:span.dot
                  {:data-color-hex (:hex c)
                   :data-color-rgb (str (-> c :rgb :r) " " (-> c :rgb :g) " " (-> c :rgb :b))
                   :style {:background-color (:hex c)}
                   }]])]]
            [:div.field-label
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
                             "When team members receive Wut posts via an email or Slack morning digest, secure "
                             "links allow them to read the post without first logging in. A login is still required "
                             "to access additional posts. If you turn off secure links, your team will always need to "
                             "be logged in to view posts.")
                     :data-toggle (when-not is-tablet-or-mobile? "tooltip")
                     :data-placement "top"
                     :data-container "body"}]]]
              ; [:div.org-settings-advanced-row.public-sections.group
              ;   (carrot-checkbox {:selected (:disallow-public-board content-visibility-data)
              ;                     :disabled false
              ;                     :did-change-cb #(change-content-visibility content-visibility-data :disallow-public-board %)})
              ;   [:div.checkbox-label
              ;     {:class (when-not (:disallow-public-board content-visibility-data) "unselected")
              ;      :on-click #(change-content-visibility content-visibility-data :disallow-public-board (not (:disallow-public-board content-visibility-data)))}
              ;     "Do not allow public teams"]
              ;   ]
              [:div.org-settings-advanced-row.public-share.group
                (carrot-checkbox {:selected (:disallow-public-share content-visibility-data)
                                  :disabled false
                                  :did-change-cb #(change-content-visibility content-visibility-data :disallow-public-share %)})
                [:div.checkbox-label
                  {:class (when-not (:disallow-public-share content-visibility-data) "unselected")
                   :on-click #(change-content-visibility content-visibility-data :disallow-public-share (not (:disallow-public-share content-visibility-data)))}
                  "Do not allow public share links"]]])]]]))