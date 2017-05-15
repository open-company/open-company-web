(ns oc.web.components.comments
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]))

(def comment-author-me {
  :name "Iacopo Carraro"
  :user-id "3af0-4014-a3c6"
  :avatar-url "https://avatars.slack-edge.com/2017-02-02/136114833346_3758034af26a3b4998f4_512.jpg"
})

(def comment-author-2 {
  :name "Sean Johnson"
  :user-id "4fb0-1536-a8c0"
  :avatar-url "https://ca.slack-edge.com/T06SBMH60-U06SBTXJR-gf5b8fc1affa-1024"
})

(def comments-data {
  :entry-uuid "1234-1234-1234-1234"
  :id "1234"
  :topic-slug "product"
  :body "Body"
  :headline "Headline"
  :created-at "2017-03-23T11:21:39.000Z"
  :updated-at "2017-03-23T12:21:39.000Z"
  :author comment-author-me
  :links []
  :comments [{
    :comment-id "fedc-ba98-7654-3210"
    :body "Comment #1 body"
    :created-at "2017-04-21T12:12:12.000Z"
    :updated-at "2017-04-21T12:12:12.000Z"
    :author comment-author-me
  } {
    :comment-id "fedc-ba98-7654-1230"
    :body "Comment #2 body"
    :created-at "2017-04-30T12:12:12.000Z"
    :updated-at "2017-04-30T12:12:12.000Z"
    :author comment-author-2
  } {
    :comment-id "fedc-ba98-7654-ab21"
    :body "Comment #3 body"
    :created-at "2017-05-01T12:12:12.000Z"
    :updated-at "2017-05-01T12:12:12.000Z"
    :author comment-author-me
  }]
})

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
          (utils/date-string (utils/js-date (:created-at c)))]]
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
            [:button.btn-reset.btn-solid "Post"]]
          [:div.add-comment-counter
            (str (- 300 (count @v)))]]]]))

(rum/defcs comments < (drv/drv :entry-data)
                      rum/reactive
                      rum/static
  [s]
  (let [entry-data (drv/react s :entry-data)
        entry-comments (:comments comments-data)] ; (:comments entry-data)]
    (when (:show-comments entry-data)
      [:div.comments
        (when (pos? (count entry-comments))
          (for [c entry-comments]
           (rum/with-key (comment-row c) (str "entry-" (:created-at entry-data) "comment-" (:comment-id c)))))
        (add-comment)])))