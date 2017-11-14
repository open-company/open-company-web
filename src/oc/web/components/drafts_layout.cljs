(ns oc.web.components.drafts-layout
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.activity-utils :as au]
            [oc.web.mixins.activity :as am]))

(rum/defcs draft-card < am/truncate-body-mixin
                        am/body-thumbnail-mixin
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
                  (dis/dispatch! [:entry-edit draft]))}
    (when draft
      [:div.draft-card-inner
        [:div.draft-card-content.group
          [:div.draft-card-title
            {:dangerouslySetInnerHTML
              (utils/emojify (utils/strip-HTML-tags (if (empty? (:headline draft))
                                                     "Untitled Draft"
                                                     (:headline draft))))}]
          (let [fixed-body (utils/body-without-preview (:body draft))
                empty-body? (empty? (utils/strip-HTML-tags fixed-body))]
            [:div.draft-card-body
              {:class (utils/class-set {:empty-body empty-body?
                                        :has-media-preview @(:body-thumbnail s)})
               :ref "activity-body"
               :dangerouslySetInnerHTML (utils/emojify fixed-body)}])
          ; Body preview
          (when @(:body-thumbnail s)
            [:div.media-preview-container
              {:class (or (:type @(:body-thumbnail s)) "image")}
              [:img
                {:src (:thumbnail @(:body-thumbnail s))}]])]
        [:div.draft-card-footer-last-edit
          [:span.edit "Edit"]
          (when (utils/link-for (:links draft) "delete")
            [:button.delete-draft.mlb-reset
              {:title "Delete draft"
               :data-toggle "tooltip"
               :data-placement "top"
               :on-click (fn [e]
                           (utils/event-stop e)
                           (let [alert-data {:icon "/img/ML/trash.svg"
                                             :action "delete-entry"
                                             :message "Delete this draft?"
                                             :link-button-title "No"
                                             :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                                             :solid-button-title "Yes"
                                             :solid-button-cb #(do
                                                                (dis/dispatch! [:activity-delete draft])
                                                                (dis/dispatch! [:alert-modal-hide]))}]
                            (dis/dispatch! [:alert-modal-show alert-data])))}
              [:i.mdi..mdi-delete]])
          [:span.last-edit (str "Last edited " (utils/time-since (:updated-at draft)))]]
        ; [:div.draft-card-footer.group
        ;   [:div.draft-card-footer-left
        ;     (let [author (:author draft)
        ;           last-edit (if (map? author) author (last author))]
        ;       (utils/draft-date (:updated-at last-edit)))]
        ;   [:div.draft-card-footer-right ""]]
          ])])

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