(ns oc.web.components.comments
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
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
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn delete-clicked [s]
  (let [comment-data (first (:rum/args s))]
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "delete-comment"
                      :message (str "Delete this comment?")
                      :link-button-title "No"
                      :link-button-cb #(do
                                         (dis/dispatch! [:alert-modal-hide])
                                         (reset! (::editing? s) false))
                      :solid-button-title "Yes"
                      :solid-button-cb #(let [activity-uuid (router/current-activity-id)]
                                         (dis/dispatch! [:comment-delete activity-uuid comment-data])
                                         (dis/dispatch! [:alert-modal-hide]))
                      }]
      (dis/dispatch! [:alert-modal-show alert-data]))))

(defn add-comment-content [add-comment-div]
  (let [inner-html (.-innerHTML add-comment-div)
        with-emojis-html (utils/emoji-images-to-unicode (gobj/get (utils/emojify inner-html) "__html"))
        replace-br (.replace with-emojis-html (js/RegExp. "<br[ ]{0,}/?>" "ig") "\n")
        cleaned-text (.replace replace-br (js/RegExp. "<div?[^>]+(>|$)" "ig") "\n")
        cleaned-text-1 (.replace cleaned-text (js/RegExp. "</div?[^>]+(>|$)" "ig") "")
        final-text (.text (.html (js/$ "<div/>") cleaned-text-1))]
    final-text))

(defn edit-finished
  [e s c]
  (let [new-comment (rum/ref-node s "comment-body")
        comment-text (add-comment-content new-comment)]
    (when-not (= comment-text (:body c))
      (reset! (::editing? s) false)
      (dis/dispatch! [:comment-save c comment-text]))))

(defn is-emoji
  [body]
  ; (let [ranges #js ["\\ud83c[\\udf00-\\udfff]"  ;; U+1F300 to U+1F3FF
  ;                   "\\ud83d[\\udc00-\\ude4f]"  ;; U+1F400 to U+1F64F
  ;                   "\\ud83d[\\ude80-\\udeff]"]] ;; U+1F680 to U+1F6FF
  ;   (.match body (.join ranges "|")))
  (let [r (js/RegExp "^([\ud800-\udbff])([\udc00-\udfff])" "g")]
    (and ;; emojis can have up to 11 codepoints
         (<= (count body) 11)
         ;;
         (.match body r)
         (not (.match body (js/RegExp "[a-zA-Z0-9\\s!?@#\\$%\\^&(())_=\\-<>,\\.\\*;':\"]" "g"))))))

(defn edit-clicked
  [s]
  (reset! (::editing? s) true)
  (utils/after 600 #(utils/to-end-of-content-editable (rum/ref-node s "comment-body"))))

(rum/defcs comment-row < rum/static
                         rum/reactive
                         (rum/local false ::editing?)
                         (rum/local nil ::window-click)
                         (rum/local false ::show-more-dropdown)
                         (rum/local false ::initial-body)
                         {:did-mount (fn [s]
                            (reset! (::window-click s)
                             (events/listen
                              js/window
                              EventType/CLICK
                              (fn [e]
                                (when (and @(::editing? s)
                                       (not (utils/event-inside? e (rum/ref-node s "comment-edit-delete")))
                                       (not (utils/event-inside? e (rum/ref-node s "comment-body"))))
                                  (reset! (::editing? s) false))
                                (when (and @(::show-more-dropdown s)
                                           (not (utils/event-inside? e (rum/ref-node s "comment-edit-delete"))))
                                  (reset! (::show-more-dropdown s) false)))))
                            (let [comment-data (first (:rum/args s))]
                              (reset! (::initial-body s) (:body comment-data)))
                            s)
                          :will-unmount (fn [s]
                            (events/unlistenByKey
                             @(::window-click s)))
                          }
  [s c]
  (let [author (:author c)
        is-emoji-comment? (is-emoji (:body c))
        is-owner? (= (-> c :author :user-id) (jwt/user-id))
        can-delete? (utils/link-for (:links c) "delete")
        can-edit? (and (not is-emoji-comment?)
                       (utils/link-for (:links c) "partial-update"))
        editable (or can-delete? can-edit?)
        should-show-comment-reaction? (and (not is-emoji-comment?)
                                           (or (not can-edit?)
                                               (and can-edit?
                                                    (pos? (:count (last (:reactions c)))))))]
    [:div.comment.group
      [:div.comment-avatar-container
        [:div.comment-avatar
          {:style {:background-image (str "url(" (:avatar-url author) ")")}}]]
      [:div.comment-content
        {:class (utils/class-set {:single-emoji is-emoji-comment?})}
        [:div.comment-author-timestamp.group
          [:div.comment-author
            (:name author)]
          [:div.comment-timestamp
            (utils/time-since (:created-at c))]]
        [:div.comment-body-container
          [:p.comment-body.group
           {:on-key-down (fn [e]
                           (when (and (= "Enter" (.-key e)) (not (.-shiftKey e)))
                             (.blur (rum/ref-node s "comment-body"))
                             (.preventDefault e))
                           (when (= "Escape" (.-key e))
                              (.stopPropagation e)
                              (let [comment-field (rum/ref-node s "comment-body")]
                                (set! (.-innerHTML comment-field) (utils/emojify @(::initial-body s) true))
                                (reset! (::editing? s) false))))
             :on-blur #(edit-finished % s c)
             :ref "comment-body"
             :class (utils/class-set {:editable can-edit?
                                      :is-owner is-owner?})
             :content-editable @(::editing? s)
             :dangerouslySetInnerHTML (utils/emojify (:body c))}]]
        (when (or should-show-comment-reaction?
                  editable)
          [:div.comment-footer-container.group
            (when should-show-comment-reaction?
              (comment-reactions/comment-reactions c))
            (when editable
              [:div.edit-delete-button
                {:ref "comment-edit-delete"}
                [:button.mlb-reset.more-button
                  {:on-click #(reset! (::show-more-dropdown s) (not @(::show-more-dropdown s)))
                   :title "More"
                   :class (utils/class-set {:more-dd-expanded @(::show-more-dropdown s)})}]
                (when @(::show-more-dropdown s)
                  (let [edit-item [{:value "delete" :label "Delete"}]
                        items (if can-edit?
                                (concat [{:value "edit" :label "Edit"}] edit-item)
                                edit-item)]
                    (dropdown-list {:items items
                                    :on-change (fn [item]
                                                (reset! (::show-more-dropdown s) false)
                                                (cond
                                                  (= (:value item) "edit")
                                                  (edit-clicked s)
                                                  (= (:value item) "delete")
                                                  (delete-clicked s)))})))])])]]))

(defn enable-add-comment? [s]
  (let [add-comment-div (rum/ref-node s "add-comment")
        comment-text (add-comment-content add-comment-div)
        next-add-bt-disabled (or (nil? comment-text) (zero? (count comment-text)))]
    (when (not= next-add-bt-disabled @(::add-button-disabled s))
      (reset! (::add-button-disabled s) next-add-bt-disabled))))

(rum/defcs add-comment < rum/reactive
                         rum/static
                         ;; Mixins
                         first-render-mixin
                         ;; Derivatives
                         (drv/drv :current-user-data)
                         ;; Locals
                         (rum/local true ::add-button-disabled)
                         (rum/local false ::show-buttons)
                         {:did-mount (fn [s]
                                       (utils/after 2500 #(js/emojiAutocomplete))
                                       (dis/dispatch! [:input [:add-comment-focus] false])
                                       s)}
  [s activity-data]
  (let [current-user-data (drv/react s :current-user-data)]
    [:div.add-comment-box-container
      [:div.add-comment-label "Add comment"]
      [:div.add-comment-box
        {:class (utils/class-set {:show-buttons @(::show-buttons s)})}
       [:div.add-comment-internal
         [:div.add-comment.emoji-autocomplete.emojiable
           {:ref "add-comment"
            :content-editable true
            :on-key-down #(let [add-comment-node (rum/ref-node s "add-comment")]
                            (when (and (= "Escape" (.-key %))
                                     (= (.-activeElement js/document) add-comment-node))
                              (.stopPropagation %)
                              (.blur add-comment-node)))
            :on-key-up #(enable-add-comment? s)
            :on-focus #(do
                         (enable-add-comment? s)
                         (dis/dispatch! [:input [:add-comment-focus] true])
                         (reset! (::show-buttons s) true))
            :on-blur #(do
                        (enable-add-comment? s)
                        (when (empty? (.-innerHTML (rum/ref-node s "add-comment")))
                          (reset! (::show-buttons s) false)
                          (dis/dispatch! [:input [:add-comment-focus] false])))
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
       (when (not (js/isIE))
         (emoji-picker {:width 32
                        :height 32
                        :add-emoji-cb (fn [active-element emoji already-added?]
                                        (let [add-comment (rum/ref-node s "add-comment")]
                                          (.focus add-comment)
                                          (utils/after 100
                                           #(do
                                              (when-not already-added?
                                                (js/pasteHtmlAtCaret
                                                 (.-native emoji)
                                                 (.getSelection js/window)
                                                 false))
                                              (enable-add-comment? s)))))
                        :force-enabled true
                        :container-selector "div.add-comment-box"}))]]))

(defn scroll-to-bottom [s & [force-scroll]]
  (when-let [comments-internal-scroll (rum/ref-node s "comments-internal-scroll")]
    ;; Make sure the dom-node exists and that it's part of the dom, ie has a parent element.
    (when (and comments-internal-scroll
               (or force-scroll
                   @(::scroll-bottom-after-render s)))
      (set! (.-scrollTop comments-internal-scroll) (.-scrollHeight comments-internal-scroll)))))

(defn load-comments-if-needed [s]
  (let [activity-data (first (:rum/args s))]
    (when (and (not @(::comments-requested s))
               activity-data)
      (reset! (::comments-requested s) true)
      (utils/after 10 #(dis/dispatch! [:comments-get activity-data])))))

(defn show-loading?
  [s]
  (let [comments-data @(drv/get-ref s :activity-comments-data)]
    (and (zero? (count (:sorted-comments comments-data)))
         (or (:loading comments-data)
             (not (contains? comments-data :sorted-comments))))))

;; Rum comments component
(rum/defcs comments < (drv/drv :activity-comments-data)
                      (drv/drv :comment-add-finish)
                      (drv/drv :add-comment-focus)
                      rum/reactive
                      rum/static
                      (rum/local false ::comments-requested)
                      (rum/local true  ::scroll-bottom-after-render)
                      (rum/local false ::scrolled-on-add-focus)
                      (rum/local false ::initially-scrolled)
                      {:will-mount (fn [s]
                        (load-comments-if-needed s)
                        s)
                       :did-mount (fn [s]
                        (utils/after 1000 #(scroll-to-bottom s true))
                        s)
                       :did-remount (fn [o s]
                        (load-comments-if-needed s)
                        (when @(drv/get-ref s :comment-add-finish)
                          (reset! (::scroll-bottom-after-render s) true))
                        (let [add-comment-focus @(drv/get-ref s :add-comment-focus)
                              scrolled-on-add-focus (::scrolled-on-add-focus s)]
                          (when (and (not @scrolled-on-add-focus)
                                     add-comment-focus)
                            (utils/after 500 #(scroll-to-bottom s true)))
                          (reset! scrolled-on-add-focus add-comment-focus))
                        s)
                       :after-render (fn [s]
                        (let [show-loading (show-loading? s)]
                          (when (and (not @(::initially-scrolled s))
                                     (not show-loading))
                            (reset! (::initially-scrolled s) true)
                            (utils/after 230 #(scroll-to-bottom s true))))
                        s)}
  [s activity-data]
  (let [sorted-comments (:sorted-comments (drv/react s :activity-comments-data))
        add-comment-focus (drv/react s :add-comment-focus)
        show-loading (show-loading? s)]
    (if show-loading
      [:div.comments.loading
        [:div.vertical-line]
        (small-loading {:class "small-loading"})]
      [:div.comments
        [:div.top-gradient.top-left]
        [:div.line-top-gradient]
        [:div.top-gradient.top-right]
        [:div.comments-internal
          {:class (utils/class-set {:add-comment-focus add-comment-focus
                                    :empty (zero? (count sorted-comments))})}
          [:div.vertical-line]
          (if (pos? (count sorted-comments))
            [:div.comments-internal-scroll
             {:ref "comments-internal-scroll"
              :on-scroll (fn [e]
                           (let [comments-internal-scroll (js/$ ".comments-internal-scroll")]
                             ;; when a user scrolls up,
                             ;; turn off the scroll to bottom after render
                             (when (and (> (.data comments-internal-scroll "lastScrollTop")
                                           (.scrollTop comments-internal-scroll))
                                        @(::scroll-bottom-after-render s))
                               (reset! (::scroll-bottom-after-render s) false))
                             (.data comments-internal-scroll "lastScrollTop" (.scrollTop comments-internal-scroll))))}
              (for [c sorted-comments]
                (rum/with-key (comment-row c) (str "activity-" (:uuid activity-data) "-comment-" (:created-at c))))
              [:div.bottom-gradient.bottom-left]
              [:div.line-bottom-gradient]
              [:div.bottom-gradient.bottom-right]]
            [:div.comments-internal-empty
              [:div.no-comments-placeholder]
              [:div.no-comments-message "No comments yet. Jump in and let everyone know what you think!"]])
          (add-comment activity-data)]])))