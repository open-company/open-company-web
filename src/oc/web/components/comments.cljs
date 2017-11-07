(ns oc.web.components.comments
  (:require-macros [if-let.core :refer (when-let*)]
                   [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.mixins :refer (first-render-mixin)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defc comment-row < rum/static
  [c]
  (let [author (:author c)]
    [:div.comment
      [:div.comment-header.group
        [:div.comment-avatar
          {:style {:background-image (str "url(" (:avatar-url author) ")")}}]
        [:div.comment-author-timestamp
          [:div.comment-author
            (:name author)]
          [:div.comment-timestamp
            (utils/time-since (:created-at c))]]]
      [:p.comment-body.group
        {:dangerouslySetInnerHTML (utils/emojify (:body c))}]
      [:div.comment-footer.group]]))

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
                         rum/static
                         first-render-mixin
                         {:did-mount (fn [s]
                                       (utils/after 2500 #(js/emojiAutocomplete))
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
      (when @(::show-successful s)
        [:div.successfully-posted
          "Comment was posted successfully!"])
      [:div.add-comment-internal
        [:div.add-comment.emoji-autocomplete.emojiable
          {:ref "add-comment"
           :content-editable true
           :on-key-up #(enable-add-comment? s)
           :on-focus #(enable-add-comment? s)
           :on-blur #(enable-add-comment? s)
           :on-paste #(do
                        (js/OnPaste_StripFormatting (rum/ref-node s "add-comment") %)
                        (enable-add-comment? s))
           :placeholder "Share your thoughts..."}]
        [:div.add-comment-footer.group
          [:div.reply-button-container
            [:button.mlb-reset.mlb-default.reply-btn
              {:on-click #(let [add-comment-div (rum/ref-node s "add-comment")]
                            (dis/dispatch! [:comment-add activity-data (add-comment-content add-comment-div)])
                            (set! (.-innerHTML add-comment-div) ""))
               :disabled @(::add-button-disabled s)}
              "Add"]]]]
      (emoji-picker {:width 32
                     :height 32
                     :add-emoji-cb #(enable-add-comment? s)})]))

(defn scroll-to-bottom [s]
  (when-let* [dom-node (utils/rum-dom-node s)
              comments-internal-scroll (sel1 dom-node :div.comments-internal-scroll)]
    ;; Make sure the dom-node exists and that it's part of the dom, ie has a parent element.
    (when comments-internal-scroll
      (set! (.-scrollTop comments-internal-scroll) (.-scrollHeight comments-internal-scroll)))))

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
                                       (let [comments-internal-scroll (js/$ ".comments-internal-scroll")
                                             next-needs-gradient (> (.prop comments-internal-scroll "scrollHeight") (.height comments-internal-scroll))]
                                         ;; Show the gradient at the top only if there are at least 5 comments
                                         ;; or the container has some scroll
                                         (when (not= @(::needs-gradient s) next-needs-gradient)
                                           (reset! (::needs-gradient s) next-needs-gradient))
                                         ;; recall scroll to bottom if needed
                                         (scroll-to-bottom s))
                                       s)}
  [s activity-data]
  (let [comments-data (drv/react s :activity-comments-data)
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
          {:class (utils/class-set {:add-comment-focus (and (pos? (count sorted-comments)) @(::add-comment-focus s))
                                    :empty (zero? (count sorted-comments))})}
          (if (pos? (count sorted-comments))
            [:div.comments-internal-scroll
              (for [c sorted-comments]
                (rum/with-key (comment-row c) (str "activity-" (:uuid activity-data) "-comment-" (:created-at c))))]
            (when-not @(::add-comment-focus s)
              [:div.comments-internal-empty
                [:div.no-comments-placeholder]
                [:div.no-comments-message "No comments yet. Jump in and let everyone know what you think!"]]))
          (add-comment activity-data)]])))