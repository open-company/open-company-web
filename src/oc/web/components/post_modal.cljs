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

(defn save-fixed-comment-height [s]
  (let [cur-height (.outerHeight (js/$ (rum/ref-node s :post-modal-fixed-add-comment)))]
    (when-not (= @(::comment-height s) cur-height)
      (reset! (::comment-height s) cur-height))))

(rum/defcs post-modal < (drv/drv :activity-data)
                        (drv/drv :comments-data)
                        (drv/drv :hide-left-navbar)
                        (drv/drv :add-comment-focus)
                        ;; Locals
                        (rum/local false ::scroll-outer-height)
                        (rum/local nil ::wh)
                        (rum/local nil ::comment-height)
                        ;; Mixins
                        rum/reactive
                        mixins/no-scroll-mixin
                        (mixins/on-window-resize-mixin (fn [s e]
                          (reset! (::wh s) (.height (js/$ js/window)))))
                        {:will-mount (fn [s]
                          (reset! (::wh s) (.height (js/$ js/window)))
                          s)
                         :did-update (fn [s]
                          (when-not @(::scroll-outer-height s)
                            (reset! (::scroll-outer-height s)
                             (.outerHeight (js/$ (rum/ref-node s :post-modal-inner)))))
                         s)
                         :did-mount (fn [s]
                          (when-let* [activity-data @(drv/get-ref s :activity-data)
                                      team-id (:team-id(dis/org-data))
                                      _part_of_team (jwt/user-is-part-of-the-team team-id)]
                            (activity-actions/send-item-seen (:uuid activity-data)))
                          s)
                         :after-render (fn [s]
                          (when (and @(::scroll-outer-height s)
                                     (> @(::scroll-outer-height s) @(::wh s)))
                            (save-fixed-comment-height s))
                          s)}
  [s]
  (let [activity-data (drv/react s :activity-data)
        comments-drv (drv/react s :comments-data)
        _add-comment-focus (drv/react s :add-comment-focus)
        comments-data (au/get-comments activity-data comments-drv)
        dom-element-id (str "post-modal-" (:uuid activity-data))
        dom-node-class (str "post-modal-" (:uuid activity-data))
        publisher (:publisher activity-data)
        is-mobile? (responsive/is-mobile-size?)
        fixed-add-comment (> @(::scroll-outer-height s) @(::wh s))
        show-bottom-share (> @(::scroll-outer-height s) @(::wh s))
        hide-left-navbar (drv/react s :hide-left-navbar)]
    (js/console.log "DBG hide-left-navbar" hide-left-navbar)
    [:div.post-modal-container
      {:id dom-element-id
       :class (utils/class-set {:must-see-item (:must-see activity-data)
                                :new-item (:new activity-data)})
       :on-click modal-close}
      [:button.mlb-reset.modal-close-bt
        {:on-click modal-close}]
      [:div.post-modal-wrapper
        {:on-click #(.stopPropagation %)
         :class (utils/class-set {:left-navbar-hidden hide-left-navbar})}
        [:div.post-modal
          {:class (utils/class-set {dom-node-class true})}
          [:div.activity-share-container]
          [:div.post-modal-inner
            {:ref :post-modal-inner
             :class (when fixed-add-comment "fixed-add-comment")
             :style {:padding-bottom (when @(::comment-height s)
                                       (str @(::comment-height s) "px"))}}
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
              (when show-bottom-share
                (more-menu activity-data dom-element-id
                 {:external-share (not is-mobile?)}))]
            [:div.post-modal-comments.group
              {:class (utils/class-set {:bottom-fixed fixed-add-comment})}
              (stream-comments activity-data comments-data)
              (when (and (not fixed-add-comment)
                         (:can-comment activity-data))
                [:div.post-modal-comments-add-comment
                  (rum/with-key (add-comment activity-data)
                   (str "post-modal-add-comment-" (:uuid activity-data)))])]]]
        (when (and fixed-add-comment
                   (:can-comment activity-data))
          [:div.post-modal-fixed-add-comment
            {:ref :post-modal-fixed-add-comment}
            (rum/with-key (add-comment activity-data #(save-fixed-comment-height s))
             (str "post-modal-fixed-add-comment-" (:uuid activity-data)))])]]))