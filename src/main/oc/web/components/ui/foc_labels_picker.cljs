(ns oc.web.components.ui.foc-labels-picker
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.local-settings :as ls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.labels :refer (add-label-bt)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.actions.label :as label-actions]))

(rum/defcs foc-labels-picker <
  rum/static
  rum/reactive
  (drv/drv :user-labels)
  ui-mixins/strict-refresh-tooltips-mixin
  (ui-mixins/on-click-out :labels-picker-inner
   (fn [_ e]
     (when-not (dom-utils/event-container-has-class e "alert-modal")
       (label-actions/toggle-foc-labels-picker nil))))
  (ui-mixins/on-key-press ["Escape"]
   #(label-actions/toggle-foc-labels-picker nil))
  {:will-mount (fn [s]
                 (label-actions/get-labels)
                 s)}
  [s {selected-labels :labels entry-uuid :uuid}]
  (let [org-labels (drv/react s :user-labels)
        label-slugs (set (map :slug selected-labels))
        is-mobile? (responsive/is-mobile-size?)
        lock-add? (>= (count selected-labels) ls/max-entry-labels)]
    [:div.labels-picker.label-modal-view.foc-labels-picker
     [:div.labels-picker-inner
      {:ref :labels-picker-inner}
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
                                  (label-actions/toggle-entry-label entry-uuid (:uuid label))))]]
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
