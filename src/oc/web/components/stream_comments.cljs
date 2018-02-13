(ns oc.web.components.stream-comments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.comment :as comment-actions]))

(rum/defcs stream-comments < rum/static
                             (rum/local false ::bottom-gradient)
                             {:did-mount (fn [s]
                              (let [scrolling-node (rum/ref-node s "stream-comments-inner")]
                                (set! (.-scrollTop scrolling-node) (.-scrollHeight scrolling-node)))
                              s)
                              :after-render (fn [s]
                              (let [scrolling-node (rum/ref-node s "stream-comments-inner")
                                    scrolls (> (.-scrollHeight scrolling-node) (.-clientHeight scrolling-node))]
                                (compare-and-set! (::bottom-gradient s) (not scrolls) scrolls))
                              s)}
  [s activity-data comments-data]
  (js/console.log "stream-comments/render" (:uuid activity-data))
  [:div.stream-comments
    {:class (utils/class-set {:bottom-gradient @(::bottom-gradient s)})}
    [:div.stream-comments-inner
      {:ref "stream-comments-inner"}
      (if (pos? (count comments-data))
        (for [comment-data comments-data]
          [:div.stream-comment
            {:key (str "stream-comment-" (:created-at comment-data))}
            [:div.stream-comment-header.group
              [:div.stream-comment-author-avatar
                {:style {:background-image (str "url(" (:avatar-url (:author comment-data)) ")")}}]
              [:div.stream-comment-author-right
                [:div.stream-comment-author-name
                  (:name (:author comment-data))]
                [:div.stream-comment-author-timestamp
                  (utils/time-since (:created-at comment-data))]]]
            [:div.stream-comment-content
              [:div.stream-comment-body
                {:dangerouslySetInnerHTML (utils/emojify (:body comment-data))}]]
            [:div.stream-comment-footer.group
              (let [reaction-data (first (:reactions comment-data))]
                [:div.stream-comment-reaction
                  {:class (utils/class-set {:reacted (:reacted reaction-data)
                                            :can-react (utils/link-for (:links reaction-data) "react" 
                                                        ["PUT" "DELETE"])})}
                    [:div.stream-comment-reaction-icon
                      {:on-click #(comment-actions/comment-reaction-toggle activity-data comment-data reaction-data
                        (not (:reacted reaction-data)))}]
                    (when (pos? (:count reaction-data))
                      [:div.stream-comment-reaction-count
                        (:count reaction-data)])])]])
        [:div.stream-comments-empty])]])