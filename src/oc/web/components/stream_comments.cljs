(ns oc.web.components.stream-comments
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.comment :as comment-actions]))

(defn scroll-to-bottom [s]
  (let [scrolling-node (rum/ref-node s "stream-comments-inner")]
    (set! (.-scrollTop scrolling-node) (.-scrollHeight scrolling-node))))

(rum/defcs stream-comments < rum/reactive
                             (drv/drv :add-comment-focus)
                             (rum/local false ::bottom-gradient)
                             (rum/local false ::last-focused-state)
                             {:after-render (fn [s]
                               (let [scrolling-node (rum/ref-node s "stream-comments-inner")
                                     scrolls (> (.-scrollHeight scrolling-node) (.-clientHeight scrolling-node))]
                                 (compare-and-set! (::bottom-gradient s) (not scrolls) scrolls))
                               (let [activity-uuid (:uuid (first (:rum/args s)))
                                     focused-uuid @(drv/get-ref s :add-comment-focus)
                                     current-local-state @(::last-focused-state s)
                                     is-self-focused? (= focused-uuid activity-uuid)]
                                  (when (not= current-local-state is-self-focused?)
                                    (reset! (::last-focused-state s) is-self-focused?)
                                    (when is-self-focused?
                                      (scroll-to-bottom s))))
                               s)}
  [s activity-data comments-data]
  [:div.stream-comments
    {:class (utils/class-set {:bottom-gradient @(::bottom-gradient s)})}
    [:div.stream-comments-inner
      {:ref "stream-comments-inner"}
      (when (pos? (count comments-data))
        [:div.stream-comments-title
          (str (count comments-data) " Comment" (when (> (count comments-data) 1) "s"))])
      (if (pos? (count comments-data))
        (for [comment-data comments-data]
          [:div.stream-comment
            {:key (str "stream-comment-" (:created-at comment-data))}
            [:div.stream-comment-header.group.fs-hide
              [:div.stream-comment-author-avatar
                {:style {:background-image (str "url(" (:avatar-url (:author comment-data)) ")")}}]
              [:div.stream-comment-author-right
                [:div.stream-comment-author-name
                  (:name (:author comment-data))]
                [:div.stream-comment-author-timestamp
                  (utils/time-since (:created-at comment-data))]]]
            [:div.stream-comment-content
              [:div.stream-comment-body
                {:dangerouslySetInnerHTML (utils/emojify (:body comment-data))
                 :class (utils/class-set {:emoji-comment (:is-emoji comment-data)})}]]
            (when-not (:is-emoji comment-data)
              [:div.stream-comment-footer.group
                (let [reaction-data (first (:reactions comment-data))
                      can-react? (utils/link-for (:links reaction-data) "react"  ["PUT" "DELETE"])]
                  (when (or can-react?
                            (pos? (:count reaction-data)))
                    [:div.stream-comment-reaction
                      {:class (utils/class-set {:reacted (:reacted reaction-data)
                                                :can-react can-react?})}
                        (when (or (pos? (:count reaction-data))
                                  can-react?)
                          [:div.stream-comment-reaction-icon
                            {:on-click #(comment-actions/comment-reaction-toggle activity-data comment-data
                              reaction-data (not (:reacted reaction-data)))}])
                        (when (pos? (:count reaction-data))
                          [:div.stream-comment-reaction-count
                            (:count reaction-data)])]))])])
        [:div.stream-comments-empty])]])