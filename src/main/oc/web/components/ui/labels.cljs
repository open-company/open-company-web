(ns oc.web.components.ui.labels
  (:require [rum.core :as rum]
            [oops.core :refer (oget)]
            [cuerdas.core :as string]
            [oc.web.urls :as oc-urls]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.utils.color :as color-utils]
            [oc.lib.color :as lib-color]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.components.ui.colors-presets :refer (colors-presets)]
            [oc.web.utils.rum :as rutils]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.label :as label-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.label-autocomplete :refer (label-autocomplete)]
            ["react-color" :as react-color :refer (ChromePicker)]))

(def refresh-labels-mixin
  {:will-mount (fn [s]
    (label-actions/get-labels)
    s)})

(def color-picker (partial rutils/build ChromePicker))

(defn- add-label-bt [{label-text :label-text on-click :on-click}]
  [:button.mlb-reset.add-label-bt
   {:on-click (when (fn? on-click)
                on-click)}
   [:span.add-label-plus]
   [:span.add-label-span
    label-text]])

(rum/defcs org-labels-list <
  rum/static
  rum/reactive
  (drv/drv :org-labels)
  [s]
  (let [org-labels (drv/react s :org-labels)]
    [:div.oc-labels
     [:div.oc-labels-title
      "Add labels"]
     (if (seq org-labels)
       (for [label org-labels]
        [:button.mlb-reset.oc-label
          {:data-label-slug (:slug label)
           :class (when (:can-edit? label) "editable")
           :key (str "label-" (or (:uuid label) (rand 1000)))
           :on-click (when (:can-edit? label)
                       (fn [e]
                         (dom-utils/stop-propagation! e)
                         (label-actions/edit-label label)))}
          ;; (carrot-checkbox {:selected false})
          [:span.oc-label-dot
           {:style {:background-color (:color label)}}]
          [:span.oc-label-name
           (:name label)]
          [:span.oc-label-edit-pen]])
       [:div.oc-labels-empty
        "No labels yet"])
     (add-label-bt {:label-text "Add label"
                    :on-click #(label-actions/new-label)})
     [:div.oc-labels-footer
      [:button.mlb-reset.cancel-bt
       {:on-click #(label-actions/hide-labels-manager)}
       "Close"]]]))

(defn- delete-confirm [e label]
  (dom-utils/stop-propagation! e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-label-confirm"
                    :message "Are you sure you want to delete this label label? This can’t be undone."
                    :link-button-title "Keep"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb (fn []
                                       (label-actions/delete-label label)
                                       (alert-modal/hide-alert))}]
    (alert-modal/show-alert alert-data)))

(rum/defcs label-editor <
  rum/static
  rum/reactive
  (drv/drv :editing-label)
  (rum/local false ::show-color-picker)
  ;; Mixins
  (ui-mixins/on-click-out :color-picker-container
                          (fn [s e]
                            (when (and @(::show-color-picker s)
                                       (not (dom-utils/event-cotainer-has-class e :oc-input)))
                              (reset! (::show-color-picker s) false))))
  (ui-mixins/on-key-press ["Esc" "Escape"]
                          (fn [s _]
                            (reset! (::show-color-picker s) false)))
  {:did-mount (fn [s]
                (.focus (rum/ref-node s :label-name-field))
                s)}
  [s]
  (let [editing-label (drv/react s :editing-label)]
    [:div.oc-label-edit.fields-modal.label-modal-view
     [:div.oc-label-edit-title
      (if (:uuid editing-label)
        "Edit label"
        "New label")]
     [:div.oc-label-edit-name-header
      "Name"]
     [:input.field-value.oc-input
      {:value (:name editing-label)
       :ref :label-name-field
       :max-length label-actions/max-label-name-length
       :class (when (:error editing-label)
                "error")
       :on-change #(dis/dispatch! [:input [:editing-label :name] (oget % "target.value")])
       :placeholder "Name your label"}]
     [:div.oc-label-edit-color-header
      "Button/link color"]
     [:div.oc-label-edit-picker-wrapper
      [:span.oc-label-picker-dot
       {:style {:background-color (:color editing-label)}}]
      [:input.field-value.oc-input
        {:type "text"
        :value (:color editing-label)
        :pattern lib-color/color-reg-ex
        :placeholder "Ie: red, green or #0000ff"
        :on-focus #(reset! (::show-color-picker s) true)
        :on-change (fn [e]
                      (let [v (string/lower (.. e -target -value))]
                        (when (.. e -target checkValidity)
                          (let [is-hex-color? (lib-color/valid-hex-color? v)
                                hex-color (if is-hex-color? v (-> v keyword lib-color/default-css-color-names))]
                            (label-actions/label-editor-update {:color hex-color})))))}]]
      (when @(::show-color-picker s)
        [:div.color-picker-container
         {:ref :color-picker-container}
         (color-picker {:color (:color editing-label) ;; (-> current-brand-color :primary :hex)
                        :onChangeComplete (fn [color]
                                            (when-let [hex-color (oget color "?hex")]
                                              ;; (reset! (::color-value s) hex-color)
                                              (label-actions/label-editor-update {:color hex-color})))})])
      (colors-presets {:color-list color-utils/colors-presets-list
                       :on-change-cb #(label-actions/label-editor-update {:color (:hex %)})
                       :current-selected (:color editing-label)})
      [:div.oc-label-footer
       (when (:can-delete? editing-label)
         [:button.mlb-reset.delete-bt
          {:on-click #(delete-confirm % editing-label)}
          "Delete"])
       [:button.mlb-reset.cancel-bt
        {:on-click #(label-actions/dismiss-label-editor)}
        "Cancel"]
       [:button.mlb-reset.save-bt
        {:on-click #(label-actions/save-label)}
        "Save"]]]))

(rum/defcs org-labels-manager <
  rum/static
  rum/reactive
  (drv/drv :show-label-editor)
  (ui-mixins/on-click-out :org-labels-manager-inner (fn [s e]
    (when (and (not (dom-utils/event-cotainer-has-class e "alert-modal"))
               (not @(drv/get-ref s :show-label-editor)))
      (label-actions/hide-labels-manager))))
  refresh-labels-mixin
  [s]
  [:div.org-labels-manager.label-modal-view
   [:div.org-labels-manager-inner
    {:ref :org-labels-manager-inner}
    [:button.mlb-reset.labels-modal-close-bt
     {:on-click #(label-actions/hide-labels-manager)}]
    (if (drv/react s :show-label-editor)
      (label-editor)
      (org-labels-list))]])

(rum/defcs labels-picker <
  rum/static
  rum/reactive
  refresh-labels-mixin
  (drv/drv :user-labels)
  (drv/drv :cmail-data)
  ui-mixins/strict-refresh-tooltips-mixin
  (ui-mixins/on-click-out :labels-picker-inner (fn [_ e]
    (when-not (dom-utils/event-cotainer-has-class e "alert-modal")
      (cmail-actions/toggle-cmail-labels-views false))))
  [s]
  (let [org-labels (drv/react s :user-labels)
        cmail-data (drv/react s :cmail-data)
        label-slugs (->> cmail-data
                          :labels
                          (map :slug)
                          set)
        is-mobile? (responsive/is-mobile-size?)
        lock-add? (>= (count (:labels cmail-data)) ls/max-entry-labels)]
    [:div.labels-picker.label-modal-view
     [:div.labels-picker-inner
      {:ref :labels-picker-inner}
      [:button.mlb-reset.labels-modal-close-bt
       {:on-click #(cmail-actions/toggle-cmail-labels-views false)}]
      [:div.oc-labels
       [:div.oc-labels-title
        "Add labels"]
       (if (seq org-labels)
         (for [label org-labels
               :let [selected? (label-slugs (:slug label))
                     click-cb (fn [e]
                                (when e
                                  (dom-utils/event-stop! e))
                                (when (or (not lock-add?)
                                          selected?)
                                  (cmail-actions/toggle-cmail-label label)))]]
           [:div.oc-label
            {:data-label-slug (:slug label)
             :key (str "labels-picker-" (or (:uuid label) (rand 1000)))
             :class (when (:can-edit? label)
                      "editable")
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :data-container "body"
             :data-original-title (if (and lock-add?
                                           (not selected?))
                                    "Max labels limit reached, remove another label before adding one."
                                    "")
             :on-click click-cb}
            (carrot-checkbox {:selected selected?})
            [:span.oc-label-dot
             {:style {:background-color (:color label)}}]
            [:span.oc-label-name
             (:name label)]
            (when (:can-edit? label)
              [:button.mlb-reset.edit-bt
               {:on-click (fn [e]
                            (dom-utils/stop-propagation! e)
                            (label-actions/edit-label label))}])])
         [:div.oc-labels-empty
          "No labels yet"])
       (add-label-bt {:label-text "Add label"
                      :on-click #(label-actions/new-label)})]]]))

(rum/defc cmail-label-item <
  rum/static
  [{on-click-cb :on-click-cb
    {label-color :color label-name :name :as label} :label
    class-name :class-name
    {tooltip-title :tooltip-title tooltip-placement :tooltip-placement :or {tooltip-placement "top"}} :tooltip}]
  [:button.mlb-reset.cmail-label
   {:data-uuid (:uuid label)
    :data-slug (:slug label)
    :class class-name
    :data-toggle (when tooltip-title
                   "tooltip")
    :data-placement tooltip-placement
    :title tooltip-title
    :on-click on-click-cb}
   (when label-color
     [:div.oc-label-bg
      {:style {:background-color label-color}}])
   [:span.oc-label-text
    {:style {:color label-color}}
    label-name]])

(rum/defcs cmail-labels-list
  "Options:
   {:add-label-bt true/false/nil ;> show/hide the + Add a label button with the dropdown menu
    :label-autocompleter true/false
   }"
  <
  rum/static
  rum/reactive
  (drv/drv :cmail-state)
  (drv/drv :cmail-data)
  (drv/drv :user-labels)
  ui-mixins/strict-refresh-tooltips-mixin
  (ui-mixins/on-click-out (fn [s _]
                            (when (:labels-inline-view @(drv/get-ref s :cmail-state))
                              (cmail-actions/toggle-cmail-inline-labels-view false))))
  [s {add-label-bt? :add-label-bt label-autocompleter :label-autocompleter}]
  (let [cmail-state (drv/react s :cmail-state)
        cmail-data (drv/react s :cmail-data)
        user-labels (drv/react s :user-labels)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.cmail-labels-list
     {:class (when (seq (:labels cmail-data))
               "has-labels")}
     (when (:labels-inline-view cmail-state)
       (labels-picker))
     (for [label (:labels cmail-data)]
       [:div.cmail-labels-item
        {:key (str "cmail-label-item" (or (:uuid label) (:slug label)))}
        (cmail-label-item {:label label
                           :class-name "cmail-label-item active"
                           :tooltip (when-not is-mobile? {:title "Remove label"})
                           :on-click-cb #(cmail-actions/toggle-cmail-label label)})])
     (if add-label-bt?
       [:div.cmail-add-label-container
        (add-label-bt {:label-text (if (seq user-labels)
                                     "Add a label"
                                     "Create a new label")
                       :on-click #(if (seq user-labels)
                                    (cmail-actions/toggle-cmail-inline-labels-view)
                                    (label-actions/new-label))})]
       (when (and label-autocompleter
                  (> (count user-labels) (count (:labels cmail-data))))
         [:div.cmail-labels-item
          (label-autocomplete {:cmail-data cmail-data
                               :user-labels user-labels})]))]))

(rum/defc label-item <
  rum/static
  [label]
  [:div.oc-label
   {:data-uuid (:uuid label)
    :data-slug (:slug label)}
   [:a
    {:href (oc-urls/label (:slug label))
     :on-click (fn [e]
                 (dom-utils/stop-propagation! e)
                 (nav-actions/nav-to-label! e (:slug label) (oc-urls/label (:slug label))))}
    [:div.oc-label-bg
     {:style {:background-color (:color label)}}]
    [:span.oc-label-text
     {:style {:color (:color label)}}
     (:name label)]]])

(rum/defc labels-list <
  rum/static
  [labels]
  [:div.oc-labels-list
   (for [label labels]
     [:div.oc-labels-item
      {:key (str "oc-labels-item-" (or (:uuid label) (:slug label)))}
      (label-item label)])])