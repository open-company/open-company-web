(ns oc.web.components.expanded-post
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn close-expanded-post []
  (routing-actions/dismiss-post-modal))

(defn save-fixed-comment-height [s]
  (let [cur-height (.outerHeight (js/$ (rum/ref-node s :expanded-post-fixed-add-comment)))]
    (when-not (= @(::comment-height s) cur-height)
      (reset! (::comment-height s) cur-height))))

(rum/defcs expanded-post <
  rum/reactive
  (drv/drv :activity-data)
  (drv/drv :comments-data)
  (drv/drv :hide-left-navbar)
  (drv/drv :add-comment-focus)
  ;; Locals
  (rum/local nil ::wh)
  (rum/local nil ::comment-height)
  ;; Mixins
  (mention-mixins/oc-mentions-hover)
  {:did-mount (fn [s]
    (save-fixed-comment-height s)
    (activity-actions/send-item-read (:uuid @(drv/get-ref s :activity-data)))
    s)}
  [s]
  (let [activity-data (drv/react s :activity-data)
        comments-drv (drv/react s :comments-data)
        _add-comment-focus (drv/react s :add-comment-focus)
        comments-data (au/get-comments activity-data comments-drv)
        dom-element-id (str "expanded-post-" (:uuid activity-data))
        dom-node-class (str "expanded-post-" (:uuid activity-data))
        publisher (:publisher activity-data)
        is-mobile? (responsive/is-mobile-size?)
        is-all-posts? (= (router/current-board-slug) "all-posts")
        back-to-label (str "Back to "
                       (if is-all-posts?
                         "All Posts"
                         (:board-name activity-data)))]
    [:div.expanded-post
      {:class dom-node-class
       :id dom-element-id
       :style {:padding-bottom (str @(::comment-height s) "px")}}
      [:div.activity-share-container]
      [:div.expanded-post-header.group
        [:button.mlb-reset.back-to-board
          {:on-click close-expanded-post}
          [:div.back-arrow]
          [:div.back-to-board-inner
            back-to-label]]
        (more-menu activity-data dom-element-id
         {:external-share (not is-mobile?)
          :tooltip-position "bottom"})]
      [:div.expanded-post-headline
        (:headline activity-data)]
      [:div.expanded-post-author
        (user-avatar-image publisher)
        [:div.expanded-post-author-inner
          (str (:name publisher) " in "
               (:board-name activity-data) " on "
               (utils/date-string (utils/js-date (:published-at activity-data)) [:year]))]]
      [:div.expanded-post-body.oc-mentions.oc-mentions-hover
        {:dangerouslySetInnerHTML {:__html (:body activity-data)}}]
      (stream-attachments (:attachments activity-data))
      [:div.expanded-post-footer
        (comments-summary activity-data true)
        (reactions activity-data)]
      [:div.expanded-post-comments.group
        (stream-comments activity-data comments-data)]
      [:div.expanded-post-fixed-add-comment
        {:ref :expanded-post-fixed-add-comment}
        [:div.expanded-post-fixed-add-comment-inner
          (rum/with-key (add-comment activity-data (utils/debounced-fn #(save-fixed-comment-height s) 300))
           (str "expanded-post-fixed-add-comment-" (:uuid activity-data)))]]]))