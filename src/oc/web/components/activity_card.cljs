(ns oc.web.components.activity-card
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as string]
            [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.utils.draft :as draft-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(rum/defcs activity-card < rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :comments-data)
                        ;; Mixins
                        (am/truncate-element-mixin "activity-headline" (* 18 2))
                        {:after-render (fn [s]
                          (let [activity-data (first (:rum/args s))
                                body-sel (str "div.activity-card-" (:uuid activity-data) " div.activity-card-body")
                                body-a-sel (str body-sel " a")]
                            ; Prevent body links in FoC
                            (.click (js/$ body-a-sel) #(.stopPropagation %)))
                          (when-not (responsive/is-tablet-or-mobile?)
                            (doto (js/$ "[data-toggle=\"tooltip\"]")
                              (.tooltip "fixTitle")
                              (.tooltip "hide")))
                          s)}
  [s activity-data has-headline has-body is-new has-attachments]
  (let [share-link (utils/link-for (:links activity-data) "share")
        edit-link (utils/link-for (:links activity-data) "partial-update")
        is-mobile? (responsive/is-tablet-or-mobile?)
        is-all-posts (or (:from-all-posts @router/path)
                         (= (router/current-board-slug) "all-posts"))
        is-must-see (= (router/current-board-slug) "must-see")
        dom-element-id (str "activity-card-" (:uuid activity-data))
        comments-data (au/get-comments activity-data (drv/react s :comments-data))
        is-drafts-board (= (router/current-board-slug) utils/default-drafts-board-slug)
        publisher (if is-drafts-board
                    (first (:author activity-data))
                    (:publisher activity-data))]
    [:div.activity-card
      {:class (utils/class-set {(str "activity-card-" (:uuid activity-data)) true
                                :draft is-drafts-board})
       :id dom-element-id
       :on-click (fn [e]
                   (let [ev-in? (partial utils/event-inside? e)]
                     (when-not (or is-drafts-board
                                   (ev-in? (sel1 [(str "div.activity-card-" (:uuid activity-data)) :div.more-menu])))
                       (activity-actions/activity-modal-fade-in activity-data))))}
      [:div.activity-share-container]
      [:div.activity-card-preview-container
        [:div.activity-card-preview-header.group
          (user-avatar-image publisher)
          [:div.activity-card-preview-author
            (:name publisher)]
          [:div.time-since
            (let [t (or (:published-at activity-data) (:created-at activity-data))]
              [:time
                {:date-time t}
                (utils/time-since t)])]]
        [:div.activity-card-preview-title
          {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
        [:div.activity-card-preview-body
          {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))}]]
      [:div.activity-card-bottom-container.group
        [:div.activity-card-header
          (str (:name publisher)
            (when (or is-all-posts is-drafts-board is-must-see)
              " in ")
            (when (or is-all-posts is-drafts-board is-must-see)
              (:board-name activity-data)))]
        [:div.activity-card-headline
          {:ref "activity-headline"
           :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
        [:div.activity-card-footer-placeholder]
        (if is-drafts-board
          [:div.activity-card-footer.group
            [:button.mlb-reset.edit-draft-bt
              {:on-click #(activity-actions/activity-edit activity-data)}
              "Edit"]
            [:button.mlb-reset.delete-draft-bt
              {:on-click #(draft-utils/delete-draft-clicked activity-data %)}
              "Delete"]]
          [:div.activity-card-footer
            (when-not is-drafts-board
              [:div.activity-card-menu-container
                (more-menu activity-data dom-element-id)])
            [:div.comments-count
              [:span.comments-icon]
              [:span.comments-count
                (count comments-data)]]
            [:div.tile-reactions
              (let [max-reaction (first (sort-by :count (:reactions activity-data)))]
                (when (pos? (:count max-reaction))
                  [:div.tile-reaction
                    [:span.reaction
                      (:reaction max-reaction)]
                    [:span.count
                      (:count max-reaction)]]))]
            [:div.activity-card-footer-right
              (when (pos? (count (:attachments activity-data)))
                [:div.tile-attachments
                  [:span.attachments-count
                    (count (:attachments activity-data))]
                  [:span.attachments-icon]])
              (when (:must-see activity-data)
                [:div.activity-card-must-see
                 {:class (utils/class-set {:must-see-on
                                           (:must-see activity-data)})}])]])]]))