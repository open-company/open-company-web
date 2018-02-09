(ns oc.web.components.comments
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.ui :refer (first-render-mixin)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.comment-reactions :as comment-reactions]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn setup-medium-editor [comment-node]
  (let [config {:toolbar false
                :anchorPreview false
                :extensions #js []
                :autoLink true
                :anchor false
                :paste #js {:forcePlainText true}
                :placeholder #js {:text "Share your thoughts..."
                                  :hideOnClick true}
               :keyboardCommands #js {:commands #js [
                                  #js {
                                    :command false
                                    :key "B"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "I"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "U"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }]}}]
    (new js/MediumEditor comment-node (clj->js config))))

(defn stop-editing [s]
  (let [medium-editor @(::medium-editor s)]
    (.destroy medium-editor)
    (when @(::esc-key-listener s)
      (events/unlistenByKey @(::esc-key-listener s))
      (reset! (::esc-key-listener s) nil))
    (reset! (::medium-editor s) nil)
    (reset! (::editing? s) false)
    (dis/dispatch! [:input [:comment-edit] nil])))

(defn cancel-edit
  [e s c]
  (.stopPropagation e)
  (let [comment-field (rum/ref-node s "comment-body")
        comment-data (first (:rum/args s))]
    (set! (.-innerHTML comment-field) (utils/emojify (:body comment-data) true))
    (stop-editing s)))

(defn add-comment-content [add-comment-div]
  (let [inner-html (.-innerHTML add-comment-div)
        with-emojis-html (utils/emoji-images-to-unicode (gobj/get (utils/emojify inner-html) "__html"))
        replace-br (.replace with-emojis-html (js/RegExp. "<br[ ]{0,}/?>" "ig") "\n")
        cleaned-text (.replace replace-br (js/RegExp. "<div?[^>]+(>|$)" "ig") "")
        cleaned-text-1 (.replace cleaned-text (js/RegExp. "</div?[^>]+(>|$)" "ig") "\n")
        final-node (.html (js/$ "<div/>") cleaned-text-1)
        final-text (.trim (.text final-node))]
    (when (pos? (count final-text))
      (string/trim (.html final-node)))))

(defn edit-finished
  [e s c]
  (let [new-comment (rum/ref-node s "comment-body")
        comment-text (add-comment-content new-comment)]
    (if (pos? (count comment-text))
      (do
        (stop-editing s)
        (set! (.-innerHTML new-comment) comment-text)
        (dis/dispatch! [:comment-save c comment-text]))
      (cancel-edit e s c))))

(defn start-editing [s]
  (dis/dispatch! [:input [:comment-edit] (:uuid (first (:rum/args s)))])
  (let [comment-node (rum/ref-node s "comment-body")
        medium-editor (setup-medium-editor comment-node)]
    (reset! (::esc-key-listener s)
     (events/listen
      js/window
      EventType/KEYDOWN
      (fn [e]
        (when (and (= "Enter" (.-key e))
                   (not (.-shiftKey e)))
          (edit-finished e s (first (:rum/args s)))
          (.preventDefault e))
        (when (= "Escape" (.-key e))
          (cancel-edit e s (first (:rum/args s)))))))
    (reset! (::medium-editor s) medium-editor)
    (reset! (::editing? s) true)
    (utils/after 600 #(utils/to-end-of-content-editable (rum/ref-node s "comment-body")))))

(defn delete-clicked [s]
  (let [comment-data (first (:rum/args s))]
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "delete-comment"
                      :message (str "Delete this comment?")
                      :link-button-title "No"
                      :link-button-cb #(do
                                         (dis/dispatch! [:alert-modal-hide])
                                         (stop-editing s))
                      :solid-button-title "Yes"
                      :solid-button-cb #(let [activity-uuid (router/current-activity-id)]
                                         (dis/dispatch! [:comment-delete activity-uuid comment-data])
                                         (dis/dispatch! [:alert-modal-hide]))
                      }]
      (dis/dispatch! [:alert-modal-show alert-data]))))

(defn is-emoji
  [body]
  (let [r (js/RegExp "^([\ud800-\udbff])([\udc00-\udfff])" "g")]
    (and ;; emojis can have up to 11 codepoints
         (<= (count body) 11)
         (.match body r)
         (not (.match body (js/RegExp "[a-zA-Z0-9\\s!?@#\\$%\\^&(())_=\\-<>,\\.\\*;':\"]" "g"))))))

(rum/defcs comment-row < rum/static
                         rum/reactive
                         (rum/local false ::editing?)
                         (rum/local false ::medium-editor)
                         (rum/local nil ::window-click)
                         (rum/local false ::show-more-dropdown)
                         (rum/local nil ::esc-key-listener)
                         {:did-mount (fn [s]
                            (reset! (::window-click s)
                             (events/listen
                              js/window
                              EventType/CLICK
                              (fn [e]
                                (when (and @(::show-more-dropdown s)
                                           (not (utils/event-inside? e (rum/ref-node s "comment-edit-delete"))))
                                  (reset! (::show-more-dropdown s) false)))))
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
          [:div.comment-body.group
           {:ref "comment-body"
            :class (utils/class-set {:editable can-edit?
                                     :is-owner is-owner?
                                     :editing @(::editing? s)})
            :content-editable @(::editing? s)
            :dangerouslySetInnerHTML (utils/emojify (:body c))}]]
        (when (or should-show-comment-reaction?
                  editable)
          [:div.comment-footer-container.group
            (when should-show-comment-reaction?
              (comment-reactions/comment-reactions c))
            (when @(::editing? s)
              [:div.save-cancel-edit-buttons
                [:button.mlb-reset.mlb-link-green
                  {:on-click #(edit-finished % s c)}
                  "Save"]
                [:button.mlb-reset.mlb-link-black
                  {:on-click #(cancel-edit % s c)}
                  "Cancel"]])
            (when (and editable
                       (not @(::editing? s)))
              [:div.edit-delete-button
                {:ref "comment-edit-delete"
                 :class (when @(::editing? s) "editing")}
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
                                                  (start-editing s)
                                                  (= (:value item) "delete")
                                                  (delete-clicked s)))})))])])]]))

(defn enable-add-comment? [s]
  (let [add-comment-div (rum/ref-node s "add-comment")
        comment-text (add-comment-content add-comment-div)
        next-add-bt-disabled (or (nil? comment-text) (zero? (count comment-text)))]
    (when (not= next-add-bt-disabled @(::add-button-disabled s))
      (reset! (::add-button-disabled s) next-add-bt-disabled))))

(defn editable-input-change [s editable event]
  (enable-add-comment? s))

(rum/defcs add-comment < rum/reactive
                         rum/static
                         ;; Mixins
                         first-render-mixin
                         ;; Derivatives
                         (drv/drv :current-user-data)
                         ;; Locals
                         (rum/local true ::add-button-disabled)
                         (rum/local false ::show-buttons)
                         (rum/local false ::medium-editor)
                         (rum/local nil ::esc-key-listener)
                         (rum/local nil ::focus-listener)
                         (rum/local nil ::blur-listener)
                         {:did-mount (fn [s]
                           (utils/after 2500 #(js/emojiAutocomplete))
                           (dis/dispatch! [:input [:add-comment-focus] false])
                           (let [add-comment-node (rum/ref-node s "add-comment")
                                 medium-editor (setup-medium-editor add-comment-node)]
                             (reset! (::medium-editor s) medium-editor)
                             (.subscribe medium-editor
                              "editableInput"
                              (partial editable-input-change s))
                             (reset! (::focus-listener s)
                              (events/listen add-comment-node EventType/FOCUS
                               (fn [e]
                                 (enable-add-comment? s)
                                 (dis/dispatch! [:input [:add-comment-focus] true])
                                 (reset! (::show-buttons s) true))))
                             (reset! (::blur-listener s)
                              (events/listen add-comment-node EventType/BLUR
                               (fn [e]
                                 (enable-add-comment? s)
                                 (when (zero? (count (.-innerText add-comment-node)))
                                   (dis/dispatch! [:input [:add-comment-focus] false])
                                   (reset! (::show-buttons s) false)))))
                             (reset! (::esc-key-listener s)
                               (events/listen
                                js/window
                                EventType/KEYDOWN
                                #(when (and (= (.-key %) "Escape")
                                            (= (.-activeElement js/document) add-comment-node))
                                   (.blur add-comment-node)))))
                           s)
                          :will-unmount (fn [s]
                           (when @(::medium-editor s)
                             (.unsubscribe
                              @(::medium-editor s)
                              "editableInput"
                              (partial editable-input-change s))
                             (.destroy @(::medium-editor s))
                             (reset! (::medium-editor s) nil))
                           (when @(::esc-key-listener s)
                             (events/unlistenByKey @(::esc-key-listener s))
                             (reset! (::esc-key-listener s) nil))
                           (when @(::focus-listener s)
                             (events/unlistenByKey @(::focus-listener s))
                             (reset! (::focus-listener s) nil))
                           (when @(::blur-listener s)
                             (events/unlistenByKey @(::blur-listener s))
                             (reset! (::blur-listener s) nil))
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
            :class (utils/class-set {:show-buttons @(::show-buttons s)})}]
        (when @(::show-buttons s)
          [:div.add-comment-footer.group
            [:div.reply-button-container
              [:button.mlb-reset.mlb-default.reply-btn
                {:on-click #(let [add-comment-div (rum/ref-node s "add-comment")]
                              (reset! (::show-buttons s) false)
                              (dis/dispatch! [:input [:add-comment-focus] false])
                              (dis/dispatch! [:comment-add activity-data (add-comment-content add-comment-div)])
                              (set! (.-innerHTML add-comment-div) "<p><br/></p>"))
                 :disabled @(::add-button-disabled s)}
                "Add"]]])]
       (when (and (not (js/isIE))
                  (not (responsive/is-tablet-or-mobile?)))
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
      (if (responsive/is-mobile-size?)
        (set! (.-scrollTop (.-body js/document)) (.-scrollHeight (.-body js/document)))
        (set! (.-scrollTop comments-internal-scroll) (.-scrollHeight comments-internal-scroll))))))

(defn load-comments-if-needed [s]
  (let [activity-data (first (:rum/args s))
        all-comments-data @(drv/get-ref s :comments-data)
        comments-data (get all-comments-data (:uuid activity-data))]
    (when (and (not (:loading comments-data))
               (not (contains? comments-data :sorted-comments)))
      (utils/after 10 #(dis/dispatch! [:comments-get activity-data])))))

(defn show-loading?
  [s]
  (let [activity-data (first (:rum/args s))
        comments-data (get @(drv/get-ref s :comments-data) (:uuid activity-data))]
    (and (zero? (count (:sorted-comments comments-data)))
         (or (:loading comments-data)
             (not (contains? comments-data :sorted-comments))))))

;; Rum comments component
(rum/defcs comments < (drv/drv :comments-data)
                      (drv/drv :comment-add-finish)
                      (drv/drv :add-comment-focus)
                      rum/reactive
                      rum/static
                      (rum/local true  ::scroll-bottom-after-render)
                      (rum/local false ::scrolled-on-add-focus)
                      (rum/local false ::initially-scrolled)
                      {:will-mount (fn [s]
                        (load-comments-if-needed s)
                        s)
                       :did-mount (fn [s]
                        (when-not (responsive/is-tablet-or-mobile?)
                          (utils/after 1000 #(scroll-to-bottom s true)))
                        s)
                       :did-remount (fn [o s]
                        (load-comments-if-needed s)
                        (when @(drv/get-ref s :comment-add-finish)
                          (reset! (::scroll-bottom-after-render s) true)
                          (dis/dispatch! [:input [:comment-add-finish] false])
                          (utils/after 500 #(scroll-to-bottom s true)))
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
                            (when-not (responsive/is-tablet-or-mobile?)
                              (utils/after 230 #(scroll-to-bottom s true)))))
                        s)}
  [s activity-data]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        sorted-comments (:sorted-comments (get (drv/react s :comments-data) (:uuid activity-data)))
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