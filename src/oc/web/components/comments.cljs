(ns oc.web.components.comments
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]
            [oc.web.components.ui.small-loading :refer (small-loading)]))

(rum/defc comment-row
  [c]
  (let [author (:author c)]
    [:div.comment
      [:div.comment-header.group
        [:div.comment-avatar.left
          {:style {:background-image (str "url(" (:avatar-url author) ")")}}]
        [:div.comment-author.left
          (:name author)]
        [:div.comment-timestamp.left
          (utils/time-since (:created-at c))]]
      [:div.comment-body.group
        (:body c)]
      [:div.comment-footer.group]]))

(rum/defcs add-comment < (rum/local "" ::v)
                         (rum/local false ::show-footer)
                         rum/static
  [s]
  (let [v (::v s)
        show-footer (::show-footer s)
        fixed-show-footer (or @show-footer (not (empty? @v)))]
    [:div.add-comment-box
      {:class (if fixed-show-footer "expanded" "")}
      [:div.add-comment-internal
        [:textarea.add-comment
          {:value @v
           :on-focus #(reset! show-footer true)
           :on-blur #(reset! show-footer false)
           :on-change #(reset! v (.. % -target -value))
           :placeholder "Add a comment..."}]
        [:div.add-comment-footer.group
          ; {:style {:opacity (if fixed-show-footer 1 0)}}
          {:style {:display (if fixed-show-footer "block" "none")}}
          [:div.right
            [:button.btn-reset.btn-solid
              {:on-click #(do
                            (dis/dispatch! [:comment-add @v])
                            (reset! v ""))}
              "Post"]]
          [:div.add-comment-counter
            (str (- 300 (count @v)))]]]]))

(rum/defcs comments < (drv/drv :comments-data)
                      rum/reactive
                      rum/static
  [s]
  (let [comments-data (drv/react s :comments-data)
        entry-comments (:comments comments-data)]
    (if (:loading comments-data)
      [:div.comments
        (small-loading)]
      (when (:show-comments comments-data)
        [:div.comments
          (when (pos? (count entry-comments))
            (for [c entry-comments]
              (rum/with-key (comment-row c) (str "entry-" (:entry-uuid (:show-comments comments-data)) "-comment-" (:created-at c)))))
          (add-comment)]))))