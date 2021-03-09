(ns oc.web.components.ui.labels
  (:require [rum.core :as rum]
            [taoensso.timbre :as timbre]
            [oops.core :refer (oget)]
            [cuerdas.core :as string]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.utils.color :as color-utils]
            [oc.web.lib.react-utils :as rutils]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.label :as label-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            ["react-color" :as react-color :refer (ChromePicker)]))

(def color-picker (partial rutils/build ChromePicker))

(defn- select-label [s label]
  (timbre/infof "Label %s selected" (:slug label)))

(rum/defcs labels <
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
        [:div.oc-label
          {:data-label-slug (:slug label)
           :key (str "label-" (or (:uuid label) (rand 1000)))
           :on-click #(select-label s label)}
          ;; (carrot-checkbox {:selected false})
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
     [:button.mlb-reset.add-label-bt
      {:on-click #(label-actions/new-label)}
      [:span.add-label-plus]
      [:span.add-label-span
       "Add label"]]
    ;;  [:div.oc-labels-footer
    ;;   [:button.mlb-reset.cancel-bt
    ;;    {:on-click #(label-actions/hide-labels-manager)}
    ;;    "Cancel"]
    ;;   [:button.mlb-reset.save-bt
    ;;    "Save"]]
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
  (rum/local false ::color-value)
  (rum/local false ::show-color-picker)
  ;; Mixins
  (ui-mixins/on-click-out :color-picker-container
                          (fn [s _]
                            (when @(::show-color-picker s)
                              (reset! (::show-color-picker s) false))))
  {:will-mount (fn [s]
                 (reset! (::color-value s) (:color @(drv/get-ref s :editing-label)))
                 s)}
  [s]
  (let [editing-label (drv/react s :editing-label)]
    [:div.oc-label-edit.fields-modal
     [:div.oc-label-edit-title
      (if (:uuid editing-label)
        "Edit label"
        "New label")]
     [:div.oc-label-edit-name-header
      "Name"]
     [:input.field-value.oc-input
      {:value (:name editing-label)
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
        :value @(::color-value s)
        :pattern color-utils/colors-reg-exp
        :placeholder "Ie: red, green or #0000ff"
        :on-focus #(reset! (::show-color-picker s) true)
        :on-change (fn [e]
                      (let [v (string/lower (.. e -target -value))]
                        (reset! (::color-value s) v)
                        (when (.. e -target checkValidity)
                          (let [is-hex-color? (.match v (js/RegExp. color-utils/hex-reg-string))
                                hex-color (if is-hex-color? v (-> v keyword color-utils/default-css-color-names))]
                            (dis/dispatch! [:input [:editing-label :color] hex-color])))))}]]
      (when @(::show-color-picker s)
        [:div.color-picker-container
         {:ref :color-picker-container}
         (color-picker {:color @(::color-value s) ;; (-> current-brand-color :primary :hex)
                        :onChangeComplete (fn [color]
                                            (when color
                                              (let [hex-color (oget color "?hex")]
                                                (reset! (::color-value s) hex-color)
                                                (dis/dispatch! [:input [:editing-label :color] hex-color]))))})])
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
  (ui-mixins/on-click-out :org-labels-manager-inner (fn [_ e]
    (timbre/info "Click out labels manager dismiss")
    (when-not (dom-utils/event-cotainer-has-class e "alert-modal")
      (label-actions/hide-labels-manager))))
  [s]
  [:div.org-labels-manager
   [:div.org-labels-manager-inner
    {:ref :org-labels-manager-inner}
    (if (drv/react s :show-label-editor)
      (label-editor)
      (labels))]])