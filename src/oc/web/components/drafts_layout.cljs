(ns oc.web.components.drafts-layout
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defc draft-card < rum/static
  [draft]
  [:div.draft-card
    {:class (when-not draft "empty-draft")
     :on-click #(when draft
                  (router/nav! (oc-urls/story-edit (router/current-org-slug) (:board-slug draft) (:uuid draft))))}
    (when draft
      [:div.draft-card-inner
        [:div.draft-card-tag
          [:div.activity-tag
            (:storyboard-name draft)]]
        [:div.draft-card-title
          {:dangerouslySetInnerHTML (utils/emojify (utils/strip-HTML-tags (if (empty? (:title draft)) "Untitled Draft" (:title draft))))}]
        (let [empty-body? (empty? (:body draft))
              fixed-body (if empty-body? "Say something..." (:body draft))
              final-body (-> fixed-body utils/strip-img-tags utils/strip-br-tags utils/strip-empty-tags)]
          [:div
            [:div.draft-card-body
              {:class (utils/class-set {:empty-body empty-body?})
               :dangerouslySetInnerHTML (utils/emojify final-body)}]
            (when (> (count fixed-body) 50)
              [:div.bottom-gradient])])
        [:div.draft-card-footer.group
          [:div.draft-card-footer-left
            (utils/draft-date (or (:updated-at draft) (:created-at draft)))]
          [:div.draft-card-footer-right ""]]])])

(rum/defc drafts-layout
  [drafts-data]
  [:div.drafts-layout
    (let [sorted-drafts (vec (reverse (sort-by :created-at (:stories drafts-data))))]
      [:div.draft-cards-container.group
        (for [idx (range (.ceil js/Math (/ (count sorted-drafts) 2)))
              :let [first-draft (get sorted-drafts (* idx 2))
                    second-draft (get sorted-drafts (inc (* idx 2)))]]
          [:div.draft-card-row.group
            {:key (str "draft-row-" idx)}
            (draft-card first-draft)
            (draft-card second-draft)])])])