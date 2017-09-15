(ns oc.web.components.comments
  (:require-macros [if-let.core :refer (when-let*)]
                   [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(rum/defc comment-row < rum/static
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
                         rum/reactive
                         (drv/drv :current-user-data)
                         rum/static
  [s activity-data did-expand-cb]
  (let [v (::v s)
        show-footer (::show-footer s)
        fixed-show-footer (or @show-footer (not (empty? @v)))
        current-user-data (drv/react s :current-user-data)]
    [:div.add-comment-box
      {:class (if fixed-show-footer "expanded" "")}
      (user-avatar-image current-user-data)
      [:div.add-comment-internal
        [:textarea.add-comment
          {:value @v
           :on-focus (fn [_] (reset! show-footer true) (when (fn? did-expand-cb) (did-expand-cb true)))
           :on-blur (fn [_] (reset! show-footer false) (when (fn? did-expand-cb) (did-expand-cb false)))
           :on-change #(reset! v (.. % -target -value))
           :placeholder "Add a comment..."}]
        [:div.add-comment-footer.group
          {:style {:display (if fixed-show-footer "block" "none")}}
          [:div.reply-button-container
            [:button.btn-reset.reply-btn
              {:on-click (fn [_]
                            (dis/dispatch! [:comment-add activity-data @v])
                            (reset! v ""))}
              "Add"]]]]]))

(defn scroll-to-bottom [s]
  (when-let* [dom-node (utils/rum-dom-node s)
              comments-internal-scroll (sel1 dom-node :div.comments-internal-scroll)]
    ;; Make sure the dom-node exists and that it's part of the dom, ie has a parent element.
    (when comments-internal-scroll
      (set! (.-scrollTop comments-internal-scroll) (.-scrollHeight comments-internal-scroll)))))

(defn add-comment-expand-cb [s expanding?]
  (when expanding?
    (utils/after 200 #(scroll-to-bottom s)))
  (reset! (::add-comment-focus s) expanding?))

(defn load-comments-if-needed [s]
  (let [activity-data (first (:rum/args s))]
    (when (and (not @(::comments-requested s))
               activity-data)
      (reset! (::comments-requested s) true)
      (dis/dispatch! [:comments-get activity-data]))))

(rum/defcs comments < (drv/drv :activity-comments-data)
                      rum/reactive
                      rum/static
                      (rum/local false ::add-comment-focus)
                      (rum/local false ::needs-gradient)
                      (rum/local false ::comments-requested)
                      {:will-mount (fn [s]
                                    (load-comments-if-needed s)
                                    s)
                       :did-remount (fn [o s]
                                      (load-comments-if-needed s)
                                      s)
                       :after-render (fn [s]
                                       (let [comments (js/$ ".comments")
                                             comments-internal-scroll (js/$ ".comments-internal-scroll")
                                             comments-internal-scroll-final-height (- (.height comments) (if @(::add-comment-focus s) 130 65))
                                             next-needs-gradient (> (.prop comments-internal-scroll "scrollHeight") comments-internal-scroll-final-height)]
                                         (.css comments-internal-scroll #js {:height (str comments-internal-scroll-final-height "px")})
                                         ;; Show the gradient at the top only if there are at least 5 comments
                                         ;; or the container has some scroll
                                         (when (not= @(::needs-gradient s) next-needs-gradient)
                                           (reset! (::needs-gradient s) next-needs-gradient))
                                         ;; recall scroll to bottom if needed
                                         (scroll-to-bottom s))
                                       s)}
  [s activity-data]
  (let [comments-data (drv/react s :activity-comments-data)
        needs-gradient @(::needs-gradient s)]
    (if (:loading comments-data)
      [:div.comments
        (small-loading)]
      [:div.comments
        (when needs-gradient
          [:div.top-gradient])
        [:div.comments-internal
          {:class (utils/class-set {:add-comment-focus (and (pos? (count comments-data)) @(::add-comment-focus s))
                                    :empty (zero? (count comments-data))})}
          (if (pos? (count comments-data))
            [:div.comments-internal-scroll
              (for [c comments-data]
                (rum/with-key (comment-row c) (str "activity-" (:uuid activity-data) "-comment-" (:created-at c))))
              ]
            [:div.comments-internal-empty
              [:img {:src (utils/cdn "/img/ML/comments_empty.png")}]
              [:div "No comments yet"]
              [:div "Jump in and let everybody know"]
              [:div "what you think!"]])
          (add-comment activity-data (partial add-comment-expand-cb s))]])))