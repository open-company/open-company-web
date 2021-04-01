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

(defn- foc-labels-list [labels entry-uuid label-uuids lock-add? is-mobile?]
  (for [label labels
        :let [selected? (label-uuids (:uuid label))
              click-cb (fn [e]
                         (when e
                           (dom-utils/event-stop! e))
                         (when (or (not lock-add?)
                                   selected?)
                           (label-actions/toggle-entry-label entry-uuid (:uuid label))))]]
    [:div.oc-label
     {:data-label-slug (:slug label)
      :data-label-uuid (:uuid label)
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
                     (label-actions/foc-picker-edit-label label))}])]))

(rum/defcs foc-labels-picker <
  rum/static
  rum/reactive
  (drv/drv :org-labels)
  (drv/drv :editing-label)
  ui-mixins/strict-refresh-tooltips-mixin
  (ui-mixins/on-click-out :labels-picker-inner
   (fn [s e]
     (when (and (not (dom-utils/event-container-has-class e "alert-modal"))
                (not @(drv/get-ref s :editing-label)))
       (label-actions/toggle-foc-labels-picker nil))))
  (ui-mixins/on-key-press ["Escape"]
   (fn [s _]
     (when-not @(drv/get-ref s :editing-label)
       (label-actions/toggle-foc-labels-picker nil))))
  {:will-mount (fn [s]
                 (label-actions/get-labels)
                 s)}
  [s {selected-labels :labels entry-uuid :uuid}]
  (let [org-labels (drv/react s :org-labels)
        _editing-label (drv/react s :editing-label)
        label-uuids (set (map :uuid selected-labels))
        is-mobile? (responsive/is-mobile-size?)
        lock-add? (>= (count selected-labels) ls/max-entry-labels)
        org-label-uuids (set (map :uuid org-labels))
        entry-orphan-labels (filterv #(not (org-label-uuids (:uuid %))) selected-labels)]
    [:div.foc-labels-picker
     [:div.foc-labels-picker-inner
      {:ref :labels-picker-inner}
      [:div.foc-labels
       [:div.foc-labels-title
        "Add labels"]
       (when (seq entry-orphan-labels)
         (foc-labels-list entry-orphan-labels entry-uuid label-uuids lock-add? is-mobile?))
       (when (seq org-labels)
         (foc-labels-list org-labels entry-uuid label-uuids lock-add? is-mobile?))
       (when-not (or (seq entry-orphan-labels)
                     (seq org-labels))
         [:div.foc-labels-empty
          "No labels yet"])
       (add-label-bt {:label-text "Add label"
                      :on-click #(label-actions/foc-picker-new-label)})]]]))
