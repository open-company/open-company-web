(ns oc.web.components.post-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn real-modal-close []
  ;; Redirect to board
  (routing-actions/dismiss-post-modal))

(defn modal-close [& [s]]
  (if s
    (reset! (::unmounting s) true)
    (real-modal-close)))

(rum/defcs post-modal < (drv/drv :activity-data)
                        (drv/drv :comments-data)
                        (drv/drv :add-comment-focus)
                        ;; Locals
                        (rum/local false ::unmounting)
                        (rum/local false ::unmounted)
                        (rum/local false ::bottom-fixed-add-comment)
                        ;; Mixins
                        rum/reactive
                        mixins/no-scroll-mixin
                        mixins/first-render-mixin
                        {:did-update (fn [s]
                          (when (and @(::unmounting s)
                                     (compare-and-set! (::unmounted s) false true))
                            (utils/after 180 real-modal-close))
                          (when-not @(::bottom-fixed-add-comment s)
                            (reset! (::bottom-fixed-add-comment s)
                             (> (.outerHeight (js/$ (rum/ref-node s :post-modal-inner)))
                                (.height (js/$ js/window)))))
                         s)}
  [s]
  (let [activity-data (drv/react s :activity-data)
        comments-drv (drv/react s :comments-data)
        comments-data (au/get-comments activity-data comments-drv)
        dom-element-id (str "post-modal-" (:uuid activity-data))
        dom-node-class (str "post-modal-" (:uuid activity-data))
        appear-class (and @(:first-render-done s)
                          (not @(::unmounting s))
                          (not @(::unmounted s)))
        publisher (:publisher activity-data)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.post-modal-container
      {:id dom-element-id
       :class (utils/class-set {:appear appear-class
                                :must-see-item (:must-see activity-data)
                                :new-item (:new activity-data)})}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(modal-close s)}]
      [:div.post-modal-wrapper
        [:div.post-modal
          {:class (utils/class-set {dom-node-class true})}
          [:div.activity-share-container]
          [:div.post-modal-inner
            {:ref :post-modal-inner
             :class (when @(::bottom-fixed-add-comment s) "fixed-add-comment")}
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
                (let [t (or (:published-at activity-data) (:created-at activity-data))]
                  [:time
                    {:date-time t
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip activity-data)}
                    (utils/time-since t)])]]
            [:div.post-modal-footer
              (comments-summary activity-data true)
              (reactions activity-data)
              (more-menu activity-data dom-element-id
               {:external-share (not is-mobile?)})]
            [:div.post-modal-comments.group
              {:class (utils/class-set {:bottom-fixed @(::bottom-fixed-add-comment s)})}
              (stream-comments activity-data comments-data)
              (when (and (not @(::bottom-fixed-add-comment s))
                         (:can-comment activity-data))
                [:div.post-modal-comments-add-comment
                  (rum/with-key (add-comment activity-data)
                   (str "post-modal-add-comment-" (:uuid activity-data)))])]]]
        (when (and @(::bottom-fixed-add-comment s)
                   (:can-comment activity-data))
          [:div.post-modal-fixed-add-comment
            (rum/with-key (add-comment activity-data)
             (str "post-modal-fixed-add-comment-" (:uuid activity-data)))])]]))