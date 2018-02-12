(ns oc.web.components.stream-comments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defcs stream-comments < rum/reactive
  [s comments-data]
  [:div.stream-comments
    (for [c comments-data]
      [:div.stream-comment
        {:key (str "stream-comment-" (:created-at c))}
        [:div.stream-comment-header.group
          [:div.stream-comment-author-avatar
            {:style {:background-image (str "url(" (:avatar-url (:author c)) ")")}}]
          [:div.stream-comment-author-right
            [:div.stream-comment-author-name
              (:name (:author c))]
            [:div.stream-comment-author-timestamp
              (utils/time-since (:created-at c))]]]
        [:div.stream-comment-content
          [:div.stream-comment-body
            {:dangerouslySetInnerHTML (utils/emojify (:body c))}]]])])