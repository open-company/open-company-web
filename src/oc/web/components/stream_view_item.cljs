(ns oc.web.components.stream-view-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.comments :refer (comments)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.activity-move :refer (activity-move)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (reactions-summary)]
            [oc.web.components.ui.activity-attachments :refer (activity-attachments)]))

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

(defn- expand-item [activity-data]
  (dis/dispatch! [:input [:stream-view-expanded-item] (:uuid activity-data)]))

(defn- collapse-item [activity-data]
  (dis/dispatch! [:input [:stream-view-expanded-item] nil]))

(defn toggle-item [s activity-data]
  (if (= @(drv/get-ref s :stream-view-expanded-item) (:uuid activity-data))
    (collapse-item activity-data)
    (expand-item activity-data)))

(rum/defcs stream-view-item < rum/reactive
                              ;; Derivatives
                              (drv/drv :org-data)
                              (drv/drv :stream-view-expanded-item)
                              ;; Locals
                              (rum/local false ::more-dropdown)
                              (rum/local false ::move-activity)
                              (rum/local nil ::window-click)
                              {:did-mount (fn [s]
                                (let [activity-data (first (:rum/args s))
                                      activity-card-class (str "div.activity-card-" (:uuid activity-data))]
                                  (reset! (::window-click s)
                                   (events/listen
                                    js/window
                                    EventType/CLICK
                                    (fn [e]
                                      (when (and
                                             (not (utils/event-inside? e (sel1 [activity-card-class [:div.more-button]])))
                                             (not (utils/event-inside? e (sel1 [activity-card-class [:div.activity-move]]))))
                                        (reset! (::more-dropdown s) false))))))
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
        expanded? (= (drv/react s :stream-view-expanded-item) (:uuid activity-data))]
    [:div.stream-view-item
      {:class (str "stream-view-item-" (:uuid activity-data))}
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
                (activity-move
                 {:activity-data activity-data
                  :boards-list all-boards
                  :dismiss-cb #(reset! (::move-activity s) false)}))]))]
      [:div.stream-view-item-body
        [:div.stream-body-left
          [:span.posted-in
            {:dangerouslySetInnerHTML (utils/emojify (str "Posted in " (:board-name activity-data)))}]
          [:div.stream-item-headline
            {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.stream-item-body-container
            {:class (if expanded? "expanded" "collapsed")}
            [:div.stream-item-body
              {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))}]
            (when-not expanded?
              [:button.mlb-reset.expand-button
                {:on-click #(toggle-item s activity-data)}
                "Continue reading"])]
          (activity-attachments activity-data true)
          [:div.stream-item-reactions.group
            (reactions-summary activity-data)]]
        [:div.stream-body-right
          (comments activity-data)]]]))