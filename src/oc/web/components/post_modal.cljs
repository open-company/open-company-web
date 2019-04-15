(ns oc.web.components.post-modal
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn modal-close []
  (routing-actions/dismiss-post-modal))

(rum/defcs post-modal < (drv/drv :activity-data)
                        (drv/drv :comments-data)
                        (drv/drv :add-comment-focus)
                        ;; Locals
                        (rum/local false ::post-exceeds-window-height)
                        ;; Mixins
                        rum/reactive
                        mixins/no-scroll-mixin
                        {:did-update (fn [s]
                          (when-not @(::post-exceeds-window-height s)
                            (reset! (::post-exceeds-window-height s)
                             (> (.outerHeight (js/$ (rum/ref-node s :post-modal-inner)))
                                (.height (js/$ js/window)))))
                         s)
                         :did-mount (fn [s]
                          (when-let* [activity-data @(drv/get-ref s :activity-data)
                                      team-id (:team-id(dis/org-data))
                                      _part_of_team (jwt/user-is-part-of-the-team team-id)]
                            (activity-actions/send-item-seen (:uuid activity-data)))
                          s)}
  [s]
  (let [activity-data (drv/react s :activity-data)
        comments-drv (drv/react s :comments-data)
        comments-data (au/get-comments activity-data comments-drv)
        dom-element-id (str "post-modal-" (:uuid activity-data))
        dom-node-class (str "post-modal-" (:uuid activity-data))
        publisher (:publisher activity-data)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.post-modal-container
      {:id dom-element-id
       :class (utils/class-set {:must-see-item (:must-see activity-data)
                                :new-item (:new activity-data)})
       :on-click modal-close}
      [:button.mlb-reset.modal-close-bt
        {:on-click modal-close}]
      [:div.post-modal-wrapper
        {:on-click #(.stopPropagation %)}
        [:div.post-modal
          {:class (utils/class-set {dom-node-class true})}
          [:div.activity-share-container]
          [:div.post-modal-inner
            {:ref :post-modal-inner
             :class (when @(::post-exceeds-window-height s) "fixed-add-comment")}
            [:div.post-modal-header.group
              (user-avatar-image publisher)
              [:div.name
                [:div.name-inner
                  {:class utils/hide-class}
                  (str
                   (:name publisher)
                   " in "
                   (:board-name activity-data))]
                [:div.must-see-tag.big-web-tablet-only "Must see"]
                [:div.new-tag.big-web-tablet-only "NEW"]]
              (more-menu activity-data dom-element-id
               {:external-share (not is-mobile?)})]
            [:div.post-modal-body
              [:div.post-headline
                (:headline activity-data)]
              [:div.post-body
                {:dangerouslySetInnerHTML {:__html (:body activity-data)}}]
              (stream-attachments (:attachments activity-data))
              [:div.time-since
                (let [author (:author activity-data)
                      t (if (pos? (count author))
                          (:updated-at (last author))
                          (or (:updated-at activity-data)
                              (:published-at activity-data)))]
                  [:time
                    {:date-time t
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip activity-data)}
                    (str "Last edited " (clojure.string/lower-case (utils/time-since t)))])]]
            [:div.post-modal-footer
              (comments-summary activity-data true)
              (reactions activity-data)
              (when @(::post-exceeds-window-height s)
                (more-menu activity-data dom-element-id
                 {:external-share (not is-mobile?)}))]
            [:div.post-modal-comments.group
              {:class (utils/class-set {:bottom-fixed @(::post-exceeds-window-height s)})}
              (stream-comments activity-data comments-data)
              (when (and (not @(::post-exceeds-window-height s))
                         (:can-comment activity-data))
                [:div.post-modal-comments-add-comment
                  (rum/with-key (add-comment activity-data)
                   (str "post-modal-add-comment-" (:uuid activity-data)))])]]]
        (when (and @(::post-exceeds-window-height s)
                   (:can-comment activity-data))
          [:div.post-modal-fixed-add-comment
            (rum/with-key (add-comment activity-data)
             (str "post-modal-fixed-add-comment-" (:uuid activity-data)))])]]))