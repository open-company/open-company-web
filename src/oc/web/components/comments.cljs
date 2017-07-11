(ns oc.web.components.comments
  (:require-macros [if-let.core :refer (when-let*)]
                   [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
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
      [:p.comment-body.group
        (:body c)]
      [:div.comment-footer.group]]))

(rum/defcs add-comment < (rum/local "" ::v)
                         (rum/local false ::show-footer)
                         rum/static
  [s entry-uuid did-expand-cb]
  (let [v (::v s)
        show-footer (::show-footer s)
        fixed-show-footer (or @show-footer (not (empty? @v)))]
    [:div.add-comment-box
      {:class (if fixed-show-footer "expanded" "")}
      [:div.add-comment-internal
        [:textarea.add-comment
          {:value @v
           :on-focus (fn [_] (reset! show-footer true) (when (fn? did-expand-cb) (did-expand-cb)))
           :on-blur #(reset! show-footer false)
           :on-change #(reset! v (.. % -target -value))
           :placeholder "Add a comment..."}]
        [:div.add-comment-footer.group
          ; {:style {:opacity (if fixed-show-footer 1 0)}}
          {:style {:display (if fixed-show-footer "block" "none")}}
          [:div.reply-button-container
            [:button.btn-reset.reply-btn
              {:on-click (fn [_]
                            (js/console.log ":comment-add" entry-uuid @v)
                            (dis/dispatch! [:comment-add entry-uuid @v])
                            (reset! v ""))}
              "Reply"]]]]]))

(defn scroll-to-bottom [s]
  (when-let* [dom-node (utils/rum-dom-node s)
              comments-internal (sel1 dom-node :div.comments-internal)]
    ;; Make sure the dom-node exists and that it's part of the dom, ie has a parent element.
    (when comments-internal
      (set! (.-scrollTop comments-internal) (.-scrollHeight comments-internal)))))

(rum/defcs comments < (drv/drv :comments-data)
                      rum/reactive
                      rum/static
                      (rum/local false ::needs-gradient)
                      {:init (fn [s p]
                              (dis/dispatch! [:comments-get (first (:rum/args s))])
                              s)
                       :after-render (fn [s]
                                       (when-let* [dom-node (utils/rum-dom-node s)
                                                   comments-internal (sel1 dom-node :div.comments-internal)]
                                         (let [next-needs-gradient (> (.-scrollHeight comments-internal) 300)]
                                           ;; Show the gradient at the top only if there are at least 5 comments
                                           ;; or the container has some scroll
                                           (when (not= @(::needs-gradient s) next-needs-gradient)
                                             (reset! (::needs-gradient s) next-needs-gradient))))
                                       ;; recall scroll to bottom if needed
                                       (scroll-to-bottom s)
                                       s)}
  [s entry-uuid]
  (let [comments-data (drv/react s :comments-data)
        entry-comments (:comments comments-data)
        needs-gradient @(::needs-gradient s)]
    (js/console.log "comments:" comments-data)
    (if (:loading comments-data)
      [:div.comments
        (small-loading)]
      [:div.comments
        (when needs-gradient
          [:div.top-gradient])
        [:div.comments-internal
          (if (pos? (count entry-comments))
            (for [c entry-comments]
              (rum/with-key (comment-row c) (str "entry-" (:entry-uuid (:show-comments comments-data)) "-comment-" (:created-at c))))
            [:div.comments-internal-empty
              [:img {:src (utils/cdn "/img/ML/comments_empty.png")}]
              [:div "No comments yet"]
              [:div (str "Jump in and let everybody know what you think!")]])
          (add-comment entry-uuid (fn [] (utils/after 200 #(scroll-to-bottom s))))]])))