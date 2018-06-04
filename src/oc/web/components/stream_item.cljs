(ns oc.web.components.stream-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.tile-menu :refer (tile-menu)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn expand [s expand? & [scroll-to-comments?]]
    (reset! (::expanded s) expand?)
    (when (and expand?
               scroll-to-comments?)
      (reset! (::should-scroll-to-comments s) true))
    (when-not expand?
      (utils/after 150 #(utils/scroll-to-y (- (.-top (.offset (js/$ (rum/dom-node s)))) 70)))))

(defn get-comments [activity-data comments-data]
  (or (-> comments-data
          (get (:uuid activity-data))
          :sorted-comments)
      (:comments activity-data)))

(defn should-show-continue-reading? [s]
  (let [activity-data (first (:rum/args s))
        $item-body (js/$ (rum/ref-node s "activity-body"))
        comments-data (get-comments activity-data @(drv/get-ref s :comments-data))]
    (when (or (.hasClass $item-body "ddd-truncated")
              (> (count (:attachments activity-data)) 3)
              (pos? (count comments-data))
              (:body-has-images activity-data))
      (reset! (::truncated s) true))))

(rum/defcs stream-item < rum/reactive
                         ;; Derivatives
                         (drv/drv :org-data)
                         (drv/drv :add-comment-focus)
                         (drv/drv :comments-data)
                         ;; Locals
                         (rum/local false ::expanded)
                         (rum/local false ::truncated)
                         (rum/local false ::should-scroll-to-comments)
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
                                #(let [actual-comments-count (count (get-comments activity-data comments-data))
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
        comments-data (get-comments activity-data comments-drv)
        activity-attachments (:attachments activity-data)
        is-all-posts (or (:from-all-posts @router/path)
                         (= (router/current-board-slug) "all-posts"))
        dom-element-id (str "stream-item-" (:uuid activity-data))]
    [:div.stream-item
      {:class (utils/class-set {(str "stream-item-" (:uuid activity-data)) true
                                :show-continue-reading truncated?})
       :id dom-element-id}
      [:div.activity-share-container]
      [:div.stream-item-header.group
        [:div.stream-header-head-author
          (user-avatar-image (:publisher activity-data))
          [:div.name.fs-hide
            (str (:name (:publisher activity-data))
              (when is-all-posts
                " in ")
              (when is-all-posts
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
        (tile-menu activity-data dom-element-id)
        (when (:new activity-data)
          [:div.new-tag
            "New"])]
      [:div.stream-item-body.group
        [:div.stream-body-left.group.fs-hide
          [:div.stream-item-headline
            {:ref "activity-headline"
             :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.stream-item-body-container
            [:div.stream-item-body
              {:class (utils/class-set {:expanded expanded?})}
              [:div.stream-item-body-inner.to-truncate
                {:ref "activity-body"
                 :class (utils/class-set {:hide-images (and truncated? (not expanded?))})
                 :dangerouslySetInnerHTML (utils/emojify (:stream-view-body activity-data))}]
              [:div.stream-item-body-inner.no-truncate
                {:ref "full-activity-body"
                 :dangerouslySetInnerHTML (utils/emojify (:body activity-data))}]]]
          (stream-attachments activity-attachments
           (when (and truncated? (not expanded?))
             #(expand s true)))
          [:div.stream-item-footer.group
            {:ref "stream-item-reactions"}
            [:button.mlb-reset.expand-button
              {:class (when expanded? "expanded")
               :on-click #(expand s (not expanded?))}
              (if expanded?
                "Show less"
                "Show more")]
            (reactions activity-data)
            (when-not is-mobile?
              [:div.stream-item-comments-summary
                {:on-click #(expand s true true)}
                (comments-summary activity-data)])]
          (when (and is-mobile?
                     (:has-comments activity-data))
            [:div.stream-item-separator])
          (when (and is-mobile?
                     (:has-comments activity-data))
            [:div.stream-mobile-comments
              {:class (when (drv/react s :add-comment-focus) "add-comment-expanded")}
              (when (pos? (count comments-data))
                [:div.stream-comments-title
                  (str (count comments-data) " Comment" (when (not= (count comments-data) 1) "s"))])
              (when (:can-comment activity-data)
                (rum/with-key (add-comment activity-data) (str "add-comment-mobile-" (:uuid activity-data))))
              (stream-comments activity-data comments-data true)])]
        (when (and expanded?
                   (not is-mobile?)
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