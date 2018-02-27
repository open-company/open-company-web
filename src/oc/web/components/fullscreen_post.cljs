(ns oc.web.components.fullscreen-post
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.activity-move :refer (activity-move)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.stream-view-attachments :refer (stream-view-attachments)]))

;; Modal dismiss handling

(defn dismiss-modal [s]
  (let [modal-data @(drv/get-ref s :fullscreen-post-data)
        activity-data (:activity-data modal-data)]
    (activity-actions/activity-modal-fade-out (:board-slug activity-data))))

(defn close-clicked [s]
  (let [ap-initial-at (:ap-initial-at @(drv/get-ref s :fullscreen-post-data))]
    (when-not (:from-all-posts @router/path)
      ;; Make sure the seen-at is not reset when navigating back to the board so NEW is still visible
      (dis/dispatch! [:input [:no-reset-seen-at] true])))
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal s)))

;; Delete handling

(defn delete-clicked [e activity-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message (str "Delete this update?")
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(let [org-slug (router/current-org-slug)
                                            board-slug (router/current-board-slug)
                                            board-url (oc-urls/board org-slug board-slug)]
                                       (router/nav! board-url)
                                       (dis/dispatch! [:activity-delete activity-data])
                                       (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs fullscreen-post < rum/reactive
                             ;; Derivatives
                             (drv/drv :fullscreen-post-data)
                             ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local false ::animate)
                             (rum/local false ::showing-dropdown)
                             (rum/local false ::move-activity)
                             (rum/local nil ::window-click)
                             (rum/local false ::resize-listener)
                             ;; Mixins
                             (when-not (responsive/is-mobile-size?)
                               mixins/no-scroll-mixin)
                             mixins/first-render-mixin

                             {:before-render (fn [s]
                               (let [modal-data @(drv/get-ref s :fullscreen-post-data)]
                                 (when (and (not @(::animate s))
                                          (= (:activity-modal-fade-in modal-data) (:uuid (:activity-data modal-data))))
                                   (reset! (::animate s) true)))
                               s)
                              :will-mount (fn [s]
                               (reset! (::resize-listener s)
                                (events/listen js/window EventType/RESIZE
                                 #(reset! (::resize-listener s) true)))
                               s)
                              :did-mount (fn [s]
                               (reset! (::window-click s)
                                (events/listen
                                 js/window
                                 EventType/CLICK
                                 (fn [e]
                                   (when (and (not
                                               (utils/event-inside? e (rum/ref-node s "more-dropdown")))
                                              (not
                                               (utils/event-inside? e (sel1 [:div.fullscreen-post :div.activity-move]))))
                                     (reset! (::showing-dropdown s) false)))))
                               s)
                              :will-unmount (fn [s]
                               (when @(::window-click s)
                                 (events/unlistenByKey @(::window-click s))
                                 (reset! (::window-click s) nil))
                               (when @(::resize-listener s)
                                 (events/unlistenByKey @(::resize-listener s))
                                 (reset! (::resize-listener s) false))
                               s)}
  [s]
  (let [modal-data (drv/react s :fullscreen-post-data)
        activity-data (:activity-data modal-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        delete-link (utils/link-for (:links activity-data) "delete")
        edit-link (utils/link-for (:links activity-data) "partial-update")
        share-link (utils/link-for (:links activity-data) "share")
        activity-attachments (au/get-attachments-from-body (:body activity-data))]
    [:div.fullscreen-post-container.group
      {:class (utils/class-set {:will-appear (or @(::dismiss s)
                                                 (and @(::animate s)
                                                      (not @(:first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.fullscreen-post-header
        [:button.mlb-reset.mobile-modal-close-bt
          {:on-click #(close-clicked s)}]
        [:div.header-title-container.group
          (user-avatar-image (:publisher activity-data))
          [:div.header-title
            {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.header-timing
            [:time
              {:date-time (:published-at activity-data)
               :data-toggle "tooltip"
               :data-placement "top"
               :title (utils/activity-date-tooltip activity-data)}
              (utils/time-since (:published-at activity-data))]]]
        [:div.fullscreen-post-header-right
          (when (or edit-link
                    share-link
                    delete-link)
            (let [all-boards (filter
                              #(not= (:slug %) utils/default-drafts-board-slug)
                              (:boards (:org-data modal-data)))]
              [:div.more-dropdown
                {:ref "more-dropdown"}
                [:button.mlb-reset.fullscreen-post-more.dropdown-toggle
                  {:type "button"
                   :on-click (fn [e]
                               (utils/remove-tooltips)
                               (reset! (::showing-dropdown s) (not @(::showing-dropdown s)))
                               (reset! (::move-activity s) false))
                   :data-toggle "tooltip"
                   :data-placement "left"
                   :data-container "body"
                   :title "More"}]
                (when @(::showing-dropdown s)
                  [:div.fullscreen-post-dropdown-menu
                    [:div.triangle]
                    [:ul.fullscreen-post-more-menu
                      (when edit-link
                       [:li.no-editing
                         {:on-click #(do
                                      (reset! (::showing-dropdown s) false)
                                      (activity-actions/activity-edit activity-data))}
                         "Edit"])
                      (when share-link
                       [:li.no-editing
                         {:on-click #(do
                                      (reset! (::showing-dropdown s) false)
                                      (dis/dispatch! [:activity-share-show activity-data]))}
                         "Share"])
                      (when edit-link
                        [:li.no-editing
                          {:on-click #(do
                                       (reset! (::showing-dropdown s) false)
                                       (reset! (::move-activity s) true))}
                          "Move"])
                      (when delete-link
                        [:li
                          {:on-click #(do
                                        (reset! (::showing-dropdown s) false)
                                        (delete-clicked % activity-data))}
                          "Delete"])]])
                (when @(::move-activity s)
                  (activity-move {:activity-data activity-data
                                  :boards-list all-boards
                                  :dismiss-cb #(reset! (::move-activity s) false)
                                  :on-change #(close-clicked s)}))]))]]
      [:div.fullscreen-post.group
        {:ref "fullscreen-post"}
        [:div.fullscreen-post-box.group
          ;; Left column
          [:div.fullscreen-post-left-column
            [:div.fullscreen-post-left-column-content.group
              [:div.fullscreen-post-box-content-board
                {:dangerouslySetInnerHTML (utils/emojify (str "Posted in " (:board-name activity-data)))}]
              [:div.fullscreen-post-box-content-headline
                {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
              [:div.fullscreen-post-box-content-body
                {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))
                 :class (when (empty? (:headline activity-data)) "no-headline")}]
              (stream-view-attachments activity-attachments)
              [:div.fullscreen-post-box-content-reactions.group
                (reactions activity-data)]]]
          ;; Right column
          (let [activity-comments (-> modal-data
                                      :comments-data
                                      (get (:uuid activity-data))
                                      :sorted-comments)
                comments-data (or activity-comments (:comments activity-data))]
            [:div.fullscreen-post-right-column.group
              {:class (utils/class-set {:add-comment-focused (:add-comment-focus modal-data)
                                        :no-comments (zero? (count comments-data))})
               :style {:right (str (/ (- (.-clientWidth (.-body js/document)) 1060) 2) "px")}}
              (add-comment activity-data)
              (stream-comments activity-data comments-data)])]]]))