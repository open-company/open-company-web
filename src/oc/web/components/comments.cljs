(ns oc.web.components.comments
  (:require-macros [if-let.core :refer (when-let*)]
                   [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [taoensso.timbre :as timbre]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.mixins.ui :refer (first-render-mixin)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.comment-reactions :as comment-reactions]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn delete-clicked [e comment-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-comment"
                    :message (str "Delete this comment?")
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(let [org-slug (router/current-org-slug)
                                            board-slug (router/current-board-slug)
                                            board-url (utils/get-board-url org-slug board-slug)
                                            activity-uuid (router/current-activity-id)]
                                       (router/nav! (oc-urls/entry org-slug board-slug activity-uuid))
                                       (dis/dispatch! [:comment-delete activity-uuid comment-data])
                                       (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs comment-row < rum/static
                         rum/reactive
  [s c]
  (let [author (:author c)]
    [:div.comment
      [:div.comment-header.group
        [:div.comment-avatar
          {:style {:background-image (str "url(" (:avatar-url author) ")")}}]
        [:div.comment-author-timestamp
          [:div.comment-author
            (:name author)]
          [:div.comment-timestamp
            (utils/time-since (:created-at c))]]
          (when (seq (:links c))
            [:div.delete-button
              [:button
                {:type "button"
                 :on-click (fn [e]
                             (utils/remove-tooltips)
                             (delete-clicked e c))
                 :title "Delete"
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-container "body"}]])]
      [:p.comment-body.group
        {:dangerouslySetInnerHTML (utils/emojify (:body c))}]
      [:div.comment-reactions-container.group
        (comment-reactions/comment-reactions c)]]))

(defn add-comment-content [add-comment-div]
  (let [inner-html (.-innerHTML add-comment-div)
        with-emojis-html (utils/emoji-images-to-unicode (gobj/get (utils/emojify inner-html) "__html"))
        replace-br (.replace with-emojis-html (js/RegExp. "<br[ ]{0,}/?>" "ig") "\n")
        cleaned-text (.replace replace-br (js/RegExp. "<div?[^>]+(>|$)" "ig") "\n")
        cleaned-text-1 (.replace cleaned-text (js/RegExp. "</div?[^>]+(>|$)" "ig") "")
        final-text (.text (.html (js/$ "<div/>") cleaned-text-1))]
    final-text))

(defn enable-add-comment? [s]
  (let [add-comment-div (rum/ref-node s "add-comment")
        comment-text (add-comment-content add-comment-div)
        next-add-bt-disabled (or (nil? comment-text) (zero? (count comment-text)))]
    (when (not= next-add-bt-disabled @(::add-button-disabled s))
      (reset! (::add-button-disabled s) next-add-bt-disabled))))

(rum/defcs add-comment < (rum/local false ::show-successful)
                         rum/reactive
                         (drv/drv :current-user-data)
                         (drv/drv :comment-add-finish)
                         (rum/local true ::add-button-disabled)
                         (rum/local false ::show-buttons)
                         rum/static
                         first-render-mixin
                         {:did-mount (fn [s]
                                       (utils/after 2500 #(js/emojiAutocomplete))
                                       (dis/dispatch! [:input [:add-comment-focus] false])
                                       s)
                          :after-render (fn [s]
                                          (when (and (not @(::show-successful s))
                                                     @(drv/get-ref s :comment-add-finish))
                                            (utils/after 100 (fn []
                                              (reset! (::show-successful s) true)
                                              (utils/after 2500 (fn []
                                               (dis/dispatch! [:input [:comment-add-finish] false])
                                               (reset! (::show-successful s) false))))))
                                          s)}
  [s activity-data]
  (let [current-user-data (drv/react s :current-user-data)]
    [:div.add-comment-box
      {:class (utils/class-set {:show-buttons @(::show-buttons s)})}
     (when @(::show-successful s)
       [:div.successfully-posted
         "Comment was posted successfully!"])
     [:div.add-comment-internal
       [:div.add-comment.emoji-autocomplete.emojiable
         {:ref "add-comment"
          :content-editable true
          :on-key-up #(enable-add-comment? s)
          :on-focus #(do
                       (enable-add-comment? s)
                       (dis/dispatch! [:input [:add-comment-focus] true])
                       (reset! (::show-buttons s) true))
          :on-blur #(enable-add-comment? s)
          :on-paste #(do
                       (js/OnPaste_StripFormatting (rum/ref-node s "add-comment") %)
                       (enable-add-comment? s))
          :placeholder "Share your thoughts..."
          :class (utils/class-set {:show-buttons @(::show-buttons s)})}]
      (when @(::show-buttons s)
        [:div.add-comment-footer.group
          [:div.reply-button-container
            [:button.mlb-reset.mlb-default.reply-btn
              {:on-click #(let [add-comment-div (rum/ref-node s "add-comment")]
                            (reset! (::show-buttons s) false)
                            (dis/dispatch! [:input [:add-comment-focus] false])
                            (dis/dispatch! [:comment-add activity-data (add-comment-content add-comment-div)])
                            (set! (.-innerHTML add-comment-div) ""))
               :disabled @(::add-button-disabled s)}
              "Add"]]])]
     (when (and (not (js/isIE)) @(::show-buttons s))
       (emoji-picker {:width 32
                      :height 32
                      :add-emoji-cb #(enable-add-comment? s)
                      :container-selector "div.add-comment-box"}))]))

(defn scroll-to-bottom [s]
  (when-let* [dom-node (utils/rum-dom-node s)
              comments-internal-scroll (sel1 dom-node :div.comments-internal-scroll)]
    ;; Make sure the dom-node exists and that it's part of the dom, ie has a parent element.
    (when (and comments-internal-scroll @(::scroll-bottom-after-render s))
      (set! (.-scrollTop comments-internal-scroll) (.-scrollHeight comments-internal-scroll)))))

(defn load-comments-if-needed [s]
  (let [activity-data (first (:rum/args s))]
    (when (and (not @(::comments-requested s))
               activity-data)
      (reset! (::comments-requested s) true)
      (utils/after 10 #(dis/dispatch! [:comments-get activity-data])))))

;; Rum comments component
(rum/defcs comments < (drv/drv :activity-comments-data)
                      (drv/drv :comment-add-finish)
                      (drv/drv :add-comment-focus)
                      rum/reactive
                      rum/static
                      (rum/local false ::needs-gradient)
                      (rum/local false ::comments-requested)
                      (rum/local true  ::scroll-bottom-after-render)
                      {:will-mount (fn [s]
                                    (load-comments-if-needed s)
                                    s)
                       :did-remount (fn [o s]
                                      (load-comments-if-needed s)
                                      (when @(drv/get-ref s :comment-add-finish)
                                        (reset! (::scroll-bottom-after-render s) true))
                                      s)
                       :after-render (fn [s]
                                       (let [comments-internal-scroll (js/$ ".comments-internal-scroll")
                                             next-needs-gradient (>
                                                                  (.prop comments-internal-scroll "scrollHeight")
                                                                  (.height comments-internal-scroll))]
                                         ;; Show the gradient at the top only if there are at least 5 comments
                                         ;; or the container has some scroll
                                         (when (not= @(::needs-gradient s) next-needs-gradient)
                                           (reset! (::needs-gradient s) next-needs-gradient))
                                         ;; recall scroll to bottom if needed
                                         (scroll-to-bottom s))
                                       s)}
  [s activity-data]
  (let [comments-data (drv/react s :activity-comments-data)
        add-comment-focus (drv/react s :add-comment-focus)
        sorted-comments (:sorted-comments comments-data)
        needs-gradient @(::needs-gradient s)]
    (if (and (zero? (count sorted-comments))
             (or (:loading comments-data)
                 (not (contains? comments-data :sorted-comments))))
      [:div.comments
        (small-loading)]
      [:div.comments
        (when needs-gradient
          [:div.top-gradient])
        [:div.comments-internal
         {:class (utils/class-set {:add-comment-focus add-comment-focus
                                   :empty (zero? (count sorted-comments))})}
          (if (pos? (count sorted-comments))
            [:div.comments-internal-scroll
             {:on-scroll (fn [e]
                           (let [comments-internal-scroll (js/$ ".comments-internal-scroll")]
                             ;; when a user scrolls up,
                             ;; turn off the scroll to bottom after render
                             (when (and (> (.data comments-internal-scroll "lastScrollTop")
                                           (.scrollTop comments-internal-scroll))
                                        @(::scroll-bottom-after-render s))
                               (reset! (::scroll-bottom-after-render s) false))
                             (.data comments-internal-scroll "lastScrollTop" (.scrollTop comments-internal-scroll))))}
              (for [c sorted-comments]
                (rum/with-key (comment-row c) (str "activity-" (:uuid activity-data) "-comment-" (:created-at c))))]
            [:div.comments-internal-empty
              [:div.no-comments-placeholder]
              [:div.no-comments-message "No comments yet. Jump in and let everyone know what you think!"]])
          (add-comment activity-data)]])))