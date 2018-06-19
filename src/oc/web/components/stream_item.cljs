(ns oc.web.components.stream-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog :as g]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.utils.draft :as draft-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.events.expand-event :as expand-event]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.tile-menu :refer (tile-menu)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn expand [s expand? & [scroll-to-comments?]]
  (reset! (::expanded s) expand?)
  (when (and expand?
             scroll-to-comments?)
    (reset! (::should-scroll-to-comments s) true))
  (when-not expand?
    (utils/after 150 #(utils/scroll-to-y (- (.-top (.offset (js/$ (rum/dom-node s)))) 70) 0)))
  (when expand?
    ;; When expanding send an expand event with a bit of delay to let the component re-render first
    (utils/after 100
     #(let [e (expand-event/ExpandEvent. expand?)]
        (.dispatchEvent expand-event/expand-event-target e)))))

(defn should-show-continue-reading? [s]
  (let [activity-data (first (:rum/args s))
        $item-body (js/$ (rum/ref-node s "activity-body"))
        comments-data (au/get-comments activity-data @(drv/get-ref s :comments-data))]
    (when (or (.hasClass $item-body "ddd-truncated")
              (> (count (:attachments activity-data)) 3)
              (pos? (count comments-data))
              (:body-has-images activity-data))
      (reset! (::truncated s) true))
    (reset! (::item-ready s) true)))

(rum/defcs stream-item < rum/reactive
                         ;; Derivatives
                         (drv/drv :org-data)
                         (drv/drv :add-comment-focus)
                         (drv/drv :comments-data)
                         ;; Locals
                         (rum/local false ::expanded)
                         (rum/local false ::truncated)
                         (rum/local false ::item-ready)
                         (rum/local false ::should-scroll-to-comments)
                         (rum/local false ::more-menu-open)
                         ;; Mixins
                         (am/truncate-element-mixin "activity-body" (* 30 3))
                         am/truncate-comments-mixin
                         {:did-mount (fn [s]
                           (should-show-continue-reading? s)
                           s)
                          :after-render (fn [s]
                           (let [activity-data (first (:rum/args s))
                                 comments-data @(drv/get-ref s :comments-data)]
                             (comment-actions/get-comments-if-needed activity-data comments-data)
                             (when @(::should-scroll-to-comments s)
                               (utils/after 180
                                #(let [actual-comments-count (count (au/get-comments activity-data comments-data))
                                       dom-node (rum/dom-node s)]
                                  ;; Commet out the scroll to comments for the moment
                                  ; (utils/scroll-to-y
                                  ;  (- (.-top (.offset (js/$ (rum/ref-node s "stream-item-reactions")))) 30 (when (zero? actual-comments-count) 40)))
                                  (when (zero? actual-comments-count)
                                    (.focus (.find (js/$ dom-node) "div.add-comment")))))
                               (reset! (::should-scroll-to-comments s) false)))
                           s)}
  [s activity-data]
  (let [org-data (drv/react s :org-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        edit-link (utils/link-for (:links activity-data) "partial-update")
        delete-link (utils/link-for (:links activity-data) "delete")
        share-link (utils/link-for (:links activity-data) "share")
        truncated? @(::truncated s)
        expanded? @(::expanded s)
        ;; Fallback to the activity inline comments if we didn't load
        ;; the full comments just yet
        comments-drv (drv/react s :comments-data)
        comments-data (au/get-comments activity-data comments-drv)
        activity-attachments (:attachments activity-data)
        is-drafts-board (= (router/current-board-slug) utils/default-drafts-board-slug)
        is-all-posts (or (:from-all-posts @router/path)
                         (= (router/current-board-slug) "all-posts"))
        dom-element-id (str "stream-item-" (:uuid activity-data))
        publisher (if is-drafts-board
                    (first (:author activity-data))
                    (:publisher activity-data))
        dom-node-class (str "stream-item-" (:uuid activity-data))]
    [:div.stream-item
      {:class (utils/class-set {dom-node-class true
                                :show-continue-reading truncated?
                                :draft is-drafts-board})
       :on-click (fn [e]
                   (let [ev-in? (partial utils/event-inside? e)
                         dom-node-selector (str "div." dom-node-class)]
                     (when (and is-mobile?
                                (not @(::more-menu-open s))
                                (not is-drafts-board)
                                (not (ev-in? (sel1 [dom-node-selector :div.more-menu])))
                                (not (ev-in? (rum/ref-node s :expand-button)))
                                (not (ev-in? (sel1 [dom-node-selector :div.reactions])))
                                (not (ev-in? (sel1 [dom-node-selector :div.stream-body-comments])))
                                (not (ev-in? (sel1 [dom-node-selector :div.mobile-summary]))))
                       (activity-actions/activity-modal-fade-in activity-data))))
       :id dom-element-id}
      [:div.activity-share-container]
      [:div.stream-item-header.group
        [:div.stream-header-head-author
          (user-avatar-image publisher)
          [:div.name.fs-hide
            (str (:name publisher)
              (when (or is-all-posts is-drafts-board)
                " in ")
              (when (or is-all-posts is-drafts-board)
                (:board-name activity-data)))]
          [:div.time-since
            (let [t (or (:published-at activity-data) (:created-at activity-data))]
              [:time
                {:date-time t
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "top"
                 :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                 :data-title (utils/activity-date-tooltip activity-data)}
                (utils/time-since t)])]]
        (when (and (not is-mobile?)
                   (not is-drafts-board))
          (tile-menu activity-data dom-element-id))
        (when is-mobile?
          (more-menu activity-data dom-element-id
           {:will-open #(reset! (::more-menu-open s) true)
            :will-close #(reset! (::more-menu-open s) false)}))
        (when (:new activity-data)
          [:div.new-tag
            "New"])]
      [:div.stream-item-body.group
        [:div.stream-body-left.group.fs-hide
          [:div.stream-item-headline
            {:ref "activity-headline"
             :data-itemuuid (:uuid activity-data)
             :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.stream-item-body-container
            [:div.stream-item-body
              {:class (utils/class-set {:expanded expanded?
                                        :wrt-item-ready @(::item-ready s)})}
              [:div.stream-item-body-inner.to-truncate
                {:ref "activity-body"
                 :data-itemuuid (:uuid activity-data)
                 :class (utils/class-set {:hide-images (and truncated? (not expanded?))
                                          :wrt-truncated truncated?
                                          :wrt-expanded expanded?})
                 :dangerouslySetInnerHTML (utils/emojify (:stream-view-body activity-data))}]
              [:div.stream-item-body-inner.no-truncate
                {:ref "full-activity-body"
                 :data-itemuuid (:uuid activity-data)
                 :class (utils/class-set {:wrt-truncated truncated?
                                          :wrt-expanded expanded?})
                 :dangerouslySetInnerHTML (utils/emojify (:body activity-data))}]]]
          (when (or (not is-mobile?) expanded?)
            (stream-attachments activity-attachments
             (when (and truncated? (not expanded?))
               #(expand s true))))
          (if is-drafts-board
            [:div.stream-item-footer.group
              [:div.stream-body-draft-edit
                [:button.mlb-reset.edit-draft-bt
                  {:on-click #(activity-actions/activity-edit activity-data)}
                  "Continue writing"]]
              [:div.stream-body-draft-delete
                [:button.mlb-reset.delete-draft-bt
                  {:on-click #(draft-utils/delete-draft-clicked activity-data %)}
                  "Delete draft"]]]
            [:div.stream-item-footer.group
              {:ref "stream-item-reactions"}
              [:button.mlb-reset.expand-button
                {:class (when expanded? "expanded")
                 :ref :expand-button
                 :on-click #(expand s (not expanded?))}
                (if expanded?
                  "Show less"
                  "Show more")]
              (when-not is-mobile?
                (reactions activity-data))
              (if is-mobile?
                (if expanded?
                  (reactions activity-data)
                  [:div.group
                    {:on-click #(expand s true)}
                    [:div.mobile-summary
                      [:div.mobile-comments-summary
                        [:div.mobile-comments-summary-icon]
                        [:span
                          (if (zero? (count comments-data))
                            "Add a comment"
                            (count comments-data))]]
                      (let [max-reaction (first (sort-by :count (:reactions activity-data)))]
                        (when (pos? (:count max-reaction))
                          [:div.mobile-summary-reaction
                            [:span.reaction
                              (:reaction max-reaction)]
                            [:span.count
                              (:count max-reaction)]]))]
                    (when (pos? (count (:attachments activity-data)))
                      [:div.mobile-summary-attachments
                        [:span.attachments-icon]
                        [:span.attachments-count (count (:attachments activity-data))]])])
                [:div.stream-item-comments-summary
                  {:on-click #(expand s true true)}
                  (comments-summary activity-data true)])])]
        (when (and expanded?
                   (:has-comments activity-data))
          [:div.stream-body-right
            [:div.stream-body-comments
              {:class (when (drv/react s :add-comment-focus) "add-comment-expanded")}
              (when (pos? (count comments-data))
                [:div.stream-comments-title
                  (str (count comments-data) " Comment" (when (not= (count comments-data) 1) "s"))])
              (when (:can-comment activity-data)
                (rum/with-key (add-comment activity-data) (str "add-comment-" (:uuid activity-data))))
              (stream-comments activity-data comments-data true)]])]]))