(ns oc.web.components.activity-card
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (comments-summary)]
            [oc.web.components.ui.activity-attachments :refer (activity-attachments)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

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

(rum/defcs activity-card < rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :nux)
                        ;; Mixins
                        am/truncate-body-mixin
                        {:after-render (fn [s]
                          (let [activity-data (first (:rum/args s))
                                body-sel (str "div.activity-card-" (:uuid activity-data) " div.activity-card-body")
                                body-a-sel (str body-sel " a")
                                is-all-posts (nth (:rum/args s) 4 false)]
                            ; Prevent body links in FoC
                            (.click (js/$ body-a-sel) #(.stopPropagation %)))
                          (when-not (responsive/is-tablet-or-mobile?)
                            (doto (js/$ "[data-toggle=\"tooltip\"]")
                              (.tooltip "fixTitle")
                              (.tooltip "hide")))
                          s)}
  [s activity-data has-headline has-body is-new is-all-posts share-thoughts]
  (let [attachments (au/get-attachments-from-body (:body activity-data))
        share-link (utils/link-for (:links activity-data) "share")
        edit-link (utils/link-for (:links activity-data) "partial-update")
        is-mobile? (responsive/is-tablet-or-mobile?)
        nux (drv/react s :nux)]
    [:div.activity-card
      {:class (utils/class-set {(str "activity-card-" (:uuid activity-data)) true
                                :all-posts-card is-all-posts})
       :on-click (fn [e]
                   (let [ev-in? (partial utils/event-inside? e)]
                    (when-not
                     (or
                      nux
                      (ev-in? (sel1 [(str "div.activity-card-" (:uuid activity-data)) :div.activity-attachments]))
                      (ev-in? (sel1 [(str "div.activity-card-" (:uuid activity-data)) :div.more-menu]))
                      (ev-in? (sel1 [(str "div.activity-card-" (:uuid activity-data)) :div.activity-tag]))
                      (ev-in? (sel1 [(str "div.activity-card-" (:uuid activity-data)) :button.post-edit]))
                      (ev-in? (sel1 [(str "div.activity-card-" (:uuid activity-data)) :div.activity-share]))
                      (ev-in? (sel1 [(str "div.activity-card-" (:uuid activity-data)) :div.reactions])))

                      (activity-actions/activity-modal-fade-in activity-data))))}
      ; Card header
      [:div.activity-card-head.group
        {:class "entry-card"}
        ; Card author
        [:div.activity-card-head-author
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
        ; Card labels
        [:div.activity-card-head-right
          (when (not nux)
            (more-menu activity-data))
          (activity-attachments activity-data true)
          (when is-all-posts
            [:div.activity-tag.board-tag.on-gray
              {:on-click #(router/nav! (oc-urls/board (router/current-org-slug) (:board-slug activity-data)))}
              (:board-name activity-data)])
                ;; TODO This will be replaced w/ new Ryan new design, be sure to clean up CSS too when this changes
                ;;(when is-new [:div.new-tag "New"])
                ]]
      [:div.activity-card-shadow-container.group
        [:div.activity-card-content.group
          [:span.posted-in
            {:dangerouslySetInnerHTML (utils/emojify (str "Posted in " (:board-name activity-data)))}]
          ; Headline
          [:div.activity-card-headline
            {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))
             :class (when has-headline "has-headline")}]
          ; Body
          (let [body-without-preview (utils/body-without-preview (:body activity-data))
                activity-url (oc-urls/entry (:board-slug activity-data) (:uuid activity-data))
                emojied-body (utils/emojify body-without-preview)]
            [:div.activity-card-body
              {:dangerouslySetInnerHTML emojied-body
               :ref "activity-body"
               :class (utils/class-set {:has-body has-body
                                        :has-headline has-headline})}])]
        [:div.activity-card-footer.group
          (reactions activity-data)
          (comments-summary activity-data)
          (when share-thoughts
            [:div.activity-share-thoughts
              "Share your thoughts"])
          (when (and (not nux)
                     (utils/link-for (:links activity-data) "partial-update"))
            [:button.mlb-reset.post-edit
              {:title "Edit"
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :data-container "body"
               :on-click (fn [e]
                           (utils/remove-tooltips)
                           (reset! (::more-dropdown s) false)
                           (activity-actions/activity-edit activity-data))}])]]]))