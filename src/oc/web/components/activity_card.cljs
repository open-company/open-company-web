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
            [oc.web.components.ui.wrt :refer (wrt)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(rum/defcs activity-card < rum/reactive
                        ;; Locals
                        (rum/local false ::hovering-card)
                        (rum/local false ::showing-menu)
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
  [s activity-data read-data has-headline has-body is-new has-attachments]
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
                    (:publisher activity-data))
        activity-time (or (:published-at activity-data) (:created-at activity-data))]
    [:div.activity-card
      {:class (utils/class-set {(str "activity-card-" (:uuid activity-data)) true
                                :draft is-drafts-board
                                :new-item (:new activity-data)})
       :id dom-element-id
       :on-mouse-enter #(reset! (::hovering-card s) true)
       :on-mouse-leave #(reset! (::hovering-card s) false)
       :on-click (fn [e]
                   (when (and (not is-drafts-board)
                              (not @(::showing-menu s))
                              (not (utils/event-inside? e (sel1 [(str "div.activity-card-" (:uuid activity-data)) :div.wrt-container]))))
                     (activity-actions/activity-modal-fade-in activity-data)))}
      [:div.new-tag "NEW"]
      [:div.activity-card-preview-container
        [:div.activity-card-preview-header.group
          (user-avatar-image publisher)
          [:div.activity-card-preview-author
            (:name publisher)]
          [:div.time-since
            [:time
              {:date-time activity-time}
              (utils/time-since activity-time)]]]
        [:div.activity-card-preview-title
          {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
        [:div.activity-card-preview-body
          {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))}]]
      [:div.activity-card-bottom-container.group
        [:div.activity-card-header
          {:class (utils/class-set {:hidden (or @(::showing-menu s)
                                                @(::hovering-card s))})}
          [:span (:name publisher)]
          [:div.separator]
          [:span (utils/time-since activity-time)]]
        [:div.activity-card-header
          {:class (utils/class-set {:hidden (and (not @(::showing-menu s))
                                                 (not @(::hovering-card s)))
                                    :wrt-container true})}
          [:span.board-name (:board-name activity-data)]
          [:div.separator]
          [:div.activity-card-wrt
            (wrt activity-data read-data)]]
        [:div.activity-card-headline.ap-seen-item-headline
          {:ref "activity-headline"
           :data-itemuuid (:uuid activity-data)
           :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
        [:div.activity-card-footer-placeholder]
        [:div.activity-share-container]
        (if is-drafts-board
          [:div.activity-card-footer.group
            [:button.mlb-reset.edit-draft-bt
              {:on-click #(activity-actions/activity-edit activity-data)}
              "Edit"]
            [:button.mlb-reset.delete-draft-bt
              {:on-click #(draft-utils/delete-draft-clicked activity-data %)}
              "Delete"]]
          [:div.activity-card-footer-container
            [:div.activity-card-footer
              {:class (when (and (not @(::hovering-card s))
                                 (not @(::showing-menu s)))
                        "hidden")}
              (when-not is-drafts-board
                [:div.activity-card-menu-container
                  (more-menu activity-data dom-element-id {:will-open #(reset! (::showing-menu s) true)
                                                           :will-close #(reset! (::showing-menu s) false)})])]
            [:div.activity-card-footer
              {:class (when (or @(::hovering-card s)
                                @(::showing-menu s))
                        "hidden")}
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
                                             (:must-see activity-data)})}])]]])]]))