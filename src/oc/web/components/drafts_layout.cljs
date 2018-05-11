(ns oc.web.components.drafts-layout
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn delete-clicked [draft e]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message "Delete this draft?"
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                       (activity-actions/activity-delete draft)
                                       (alert-modal/hide-alert))}]
   (alert-modal/show-alert alert-data)))

(rum/defcs draft-card < am/truncate-body-mixin
                        {:after-render (fn [s]
                          (let [draft-data (first (:rum/args s))
                                body-sel (str "div.draft-card-" (:uuid draft-data) " div.draft-card-body")
                                body-a-sel (str body-sel " a")]
                            ; Prevent body links in FoC
                            (.click (js/$ body-a-sel) #(.stopPropagation %)))
                          s)}
  [s draft]
  [:div.draft-card
    {:class (utils/class-set {:empty-draft (not draft)
                              (str "draft-card-" (:uuid draft)) true})
     :key (str "draft-" (:created-at draft))
     :on-click #(when draft
                  (activity-actions/entry-edit draft))}
    (when draft
      [:div.draft-card-inner
        [:div.draft-card-head
          [:div.draft-card-head-author
            (user-avatar-image (:author draft))
            [:div.name (:name (:author draft))]
            [:div.time-since
              [:time
                {:date-time (:created-at draft)
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                 :title (:time-tooltip draft)}
                (:time-since draft)]]]]
        [:div.draft-card-content.group.fs-hide
          [:div.draft-card-title
            {:dangerouslySetInnerHTML
              (utils/emojify (utils/strip-HTML-tags (if (empty? (:headline draft))
                                                     "Untitled Draft"
                                                     (:headline draft))))}]
          (let [fixed-body (utils/body-without-preview (:body draft))
                empty-body? (empty? (utils/strip-HTML-tags fixed-body))]
            [:div.draft-card-body
              {:class (utils/class-set {:empty-body empty-body?})
               :ref "activity-body"
               :dangerouslySetInnerHTML (utils/emojify fixed-body)}])]
        [:div.draft-card-footer-last-edit
          [:span.edit "Edit"]
          (when (utils/link-for (:links draft) "delete")
            [:button.delete-draft.mlb-reset
              {:title "Delete draft"
               :data-toggle "tooltip"
               :data-placement "top"
               :on-click (partial delete-clicked draft)}
              [:i.mdi..mdi-delete]])
          [:span.last-edit (str "Last edited " (utils/time-since (:updated-at draft)))]]
        [:div.draft-card-mobile-footer
          [:button.mlb-reset.continue-editing-bt
            "Continue editing"]
          [:button.mlb-reset.delete-draft-bt
            {:on-click (partial delete-clicked draft)}
            "Delete draft"]]])])

(defn get-sorted-drafts [drafts-data]
  (vec (reverse (sort-by :created-at (vals (:fixed-items drafts-data))))))

(rum/defc drafts-layout
  [drafts-data]
  [:div.drafts-layout
    (let [sorted-drafts (get-sorted-drafts drafts-data)]
      [:div.draft-cards-container.group
        [:div.draft-card-column.group
          (for [idx (range (.ceil js/Math (/ (count sorted-drafts) 2)))
                :let [first-draft (get sorted-drafts (* idx 2))]]
            (draft-card first-draft))]
        [:div.draft-card-column.group
          (for [idx (range (.ceil js/Math (/ (count sorted-drafts) 2)))
                :let [second-draft (get sorted-drafts (inc (* idx 2)))]]
            (draft-card second-draft))]])])