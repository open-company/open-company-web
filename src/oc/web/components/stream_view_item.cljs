(ns oc.web.components.stream-view-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-view-attachments :refer (stream-view-attachments)]))

(defn should-show-continue-reading? [s]
  (when-not @(::expanded s)
    (let [item-body (rum/ref-node s "item-body")
          dom-node (rum/dom-node s)
          should-hide-body (> (.-clientHeight item-body) 400)]
      (when (and (not should-hide-body)
                 (not @(::should-show-comments s)))
        (reset! (::should-show-comments s) true))
      (if should-hide-body
        (.add (.-classList dom-node) "show-continue-reading")
        (.remove (.-classList dom-node) "show-continue-reading")))))

(rum/defcs stream-view-item < rum/reactive
                              ;; Derivatives
                              (drv/drv :org-data)
                              (drv/drv :add-comment-focus)
                              (drv/drv :comments-data)
                              ;; Locals
                              (rum/local false ::more-dropdown)
                              (rum/local false ::move-activity)
                              (rum/local false ::expanded)
                              (rum/local false ::should-show-comments)
                              (rum/local false ::should-scroll-to-comments)
                              {:after-render (fn [s]
                                (should-show-continue-reading? s)
                                (comment-actions/get-comments-if-needed (first (:rum/args s))
                                 @(drv/get-ref s :comments-data))
                                (when @(::should-scroll-to-comments s)
                                  (utils/scroll-to-y (.-top (.offset (js/$ (rum/ref-node s "stream-item-reactions")))) 180)
                                  (reset! (::should-scroll-to-comments s) false))
                                s)}
  [s activity-data]
  (let [org-data (drv/react s :org-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        edit-link (utils/link-for (:links activity-data) "partial-update")
        delete-link (utils/link-for (:links activity-data) "delete")
        share-link (utils/link-for (:links activity-data) "share")
        expanded? @(::expanded s)
        ;; Fallback to the activity inline comments if we didn't load
        ;; the full comments just yet
        comments-data (or (-> (drv/react s :comments-data)
                              (get (:uuid activity-data))
                              :sorted-comments)
                          (:comments activity-data))
        activity-attachments (:attachments activity-data)]
    [:div.stream-view-item
      {:class (utils/class-set {(str "stream-view-item-" (:uuid activity-data)) true
                                :expanded expanded?})}
      [:div.stream-view-item-header
        [:div.stream-header-head-author
          (user-avatar-image (:publisher activity-data))
          [:div.name (:name (:publisher activity-data))]
          [:div.time-since
            (let [t (or (:published-at activity-data) (:created-at activity-data))]
              [:time
                {:date-time t
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "top"
                 :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                 :title (utils/activity-date-tooltip activity-data)}
                (utils/time-since t)])]]
        (more-menu activity-data)]
      [:div.stream-view-item-body.group
        [:div.stream-body-left.group
          {:style {:padding-bottom (when-not is-mobile?
                                     (let [initial-padding 104
                                           attachments-num (count activity-attachments)
                                           attachments-height (* (js/Math.ceil (/ attachments-num 2)) 65)
                                           total-padding (+ initial-padding
                                                          (when (pos? attachments-num)
                                                            (+ 32 20 attachments-height)))]
                                     (str total-padding "px")))}}
          [:span.posted-in
            {:dangerouslySetInnerHTML (utils/emojify (str "Posted in " (:board-name activity-data)))}]
          [:div.stream-item-headline
            {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.stream-item-body-container
            [:div.stream-item-body
              [:div.stream-item-body-inner
                {:ref "item-body"
                 :dangerouslySetInnerHTML (utils/emojify (:body activity-data))}]]
            [:button.mlb-reset.expand-button
              {:on-click #(do
                           (reset! (::expanded s) true)
                           (reset! (::should-show-comments s) true))}
              "Continue reading"]]
          (stream-view-attachments activity-attachments)
          [:div.stream-item-reactions.group
            {:ref "stream-item-reactions"}
            (reactions activity-data)]
          (when (and is-mobile?
                     (not @(::should-show-comments s))
                     (pos? (count comments-data)))
            [:div.stream-mobile-comments-summary
              {:on-click (fn [e]
                            (utils/event-stop e)
                            (reset! (::expanded s) true)
                            (reset! (::should-show-comments s) true)
                            (reset! (::should-scroll-to-comments s) true))}
              (when-not (zero? (count comments-data))
                (comments-summary activity-data false))])
          (when (and is-mobile?
                     @(::should-show-comments s))
            [:div.stream-mobile-comments
              {:class (when (drv/react s :add-comment-focus) "add-comment-expanded")}
              (add-comment activity-data)
              (stream-comments activity-data comments-data)])]
        (when-not is-mobile?
          [:div.stream-body-right
            {:class (when expanded? "expanded")}
            [:div.stream-body-comments
              {:class (when (drv/react s :add-comment-focus) "add-comment-expanded")}
              (add-comment activity-data)
              (stream-comments activity-data comments-data)]])]]))