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
            [oc.web.utils.activity :as au]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.activity-move :refer (activity-move)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-view-attachments :refer (stream-view-attachments)]))

(defn- delete-clicked [e activity-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message "Delete this post?"
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                        (dis/dispatch! [:activity-delete activity-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(defn should-show-continue-reading? [s]
  (when-not @(::expanded s)
    (let [item-body (rum/ref-node s "item-body")
          dom-node (rum/dom-node s)]
      (if (> (.-clientHeight item-body) 400)
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
                              (rum/local nil ::window-click)
                              (rum/local false ::expanded)
                              (rum/local false ::mobile-show-comments)
                              {:did-mount (fn [s]
                                (reset! (::window-click s)
                                 (events/listen
                                  js/window
                                  EventType/CLICK
                                  (fn [e]
                                   (when (and (or @(::more-dropdown s)
                                                  @(::move-activity s))
                                              (not (utils/event-inside? e (rum/ref-node s "more-button")))
                                              (not (utils/event-inside? e (rum/ref-node s "activity-move-container"))))
                                     (reset! (::more-dropdown s) false)))))
                                s)
                               :after-render (fn [s]
                                (should-show-continue-reading? s)
                                (comment-actions/get-comments-if-needed (first (:rum/args s))
                                 @(drv/get-ref s :comments-data))
                                s)
                               :will-unmount (fn [s]
                                (events/unlistenByKey @(::window-click s))
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
        activity-attachments (au/get-attachments-from-body (:body activity-data))]
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
        (when (or edit-link
                  delete-link)
          (let [all-boards (filter
                            #(not= (:slug %) utils/default-drafts-board-slug)
                            (:boards (drv/react s :org-data)))]
            [:div.more-button
              {:ref "more-button"}
              [:button.mlb-reset.more-ellipsis
                {:type "button"
                 :on-click (fn [e]
                             (utils/remove-tooltips)
                             (reset! (::more-dropdown s) (not @(::more-dropdown s)))
                             (reset! (::move-activity s) false))
                 :title "More"
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "top"
                 :data-container "body"}]
              (when @(::more-dropdown s)
                [:div.activity-more-dropdown-menu
                  [:div.triangle]
                  [:ul.activity-card-more-menu
                    (when edit-link
                      [:li
                        {:on-click #(do
                                      (utils/remove-tooltips)
                                      (reset! (::more-dropdown s) false)
                                      (activity-actions/activity-edit activity-data))}
                        "Edit"])
                    (when share-link
                      [:li
                        {:on-click #(do
                                      (reset! (::more-dropdown s) false)
                                      (dis/dispatch! [:activity-share-show activity-data]))}
                        "Share"])
                    (when (and edit-link
                               (> (count all-boards) 1))
                      [:li
                        {:on-click #(do
                                      (reset! (::more-dropdown s) false)
                                      (reset! (::move-activity s) true))}
                        "Move"])
                    (when (utils/link-for (:links activity-data) "delete")
                      [:li
                        {:on-click #(do
                                      (reset! (::more-dropdown s) false)
                                      (delete-clicked % activity-data))}
                        "Delete"])]])
              (when @(::move-activity s)
                [:div.activity-move-container
                  {:ref "activity-move-container"}
                  (activity-move
                   {:activity-data activity-data
                    :boards-list all-boards
                    :dismiss-cb #(reset! (::move-activity s) false)})])]))]
      [:div.stream-view-item-body.group
        [:div.stream-body-left.group
          {:style {:padding-bottom (when-not is-mobile?
                                     (let [initial-padding 64
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
              {:on-click #(reset! (::expanded s) true)}
              "Continue reading"]]
          (stream-view-attachments activity-attachments)
          (when (and is-mobile?
                     @(::mobile-show-comments s))
            [:div.stream-mobile-comments
              {:class (when (drv/react s :add-comment-focus) "add-comment-expanded")}
              (add-comment activity-data)
              (stream-comments activity-data comments-data)])
          [:div.stream-item-reactions.group
            (when (and is-mobile?
                       (not @(::mobile-show-comments s)))
              [:div.stream-mobile-comments-summary
                {:on-click #(do
                              (utils/event-stop %)
                              (reset! (::mobile-show-comments s) true))}
                (if (zero? (count comments-data))
                  (when is-mobile?
                    [:div.zero-comments "Reply"])
                  (comments-summary activity-data false))])
            (reactions activity-data)]]
        (when-not is-mobile?
          [:div.stream-body-right
            {:class (when expanded? "expanded")}
            [:div.stream-body-comments
              {:class (when (drv/react s :add-comment-focus) "add-comment-expanded")}
              (add-comment activity-data)
              (stream-comments activity-data comments-data)]])]]))