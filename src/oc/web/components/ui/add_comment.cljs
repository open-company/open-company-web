(ns oc.web.components.ui.add-comment
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.mixins.ui :refer (first-render-mixin)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn enable-add-comment? [s]
  (when-let [add-comment-div (rum/ref-node s "add-comment")]
    (let [comment-text (cu/add-comment-content add-comment-div)
          next-add-bt-disabled (or (nil? comment-text) (zero? (count comment-text)))]
      (when (not= next-add-bt-disabled @(::add-button-disabled s))
        (reset! (::add-button-disabled s) next-add-bt-disabled)))))

(defn editable-input-change [s editable event]
  (enable-add-comment? s))

(defn focus-add-comment [s]
  (enable-add-comment? s)
  (comment-actions/add-comment-focus (:uuid (first (:rum/args s)))))

(defn disable-add-comment-if-needed [s]
  (when-let [add-comment-node (rum/ref-node s "add-comment")]
    (enable-add-comment? s)
    (when (and (zero? (count (.-innerText add-comment-node)))
               (not @(::emoji-picker-open s)))
      (comment-actions/add-comment-blur))))

(rum/defcs add-comment < rum/reactive
                         rum/static
                         ;; Mixins
                         first-render-mixin
                         (mention-mixins/oc-mentions-hover)
                         ;; Derivatives
                         (drv/drv :add-comment-focus)
                         (drv/drv :team-roster)
                         ;; Locals
                         (rum/local true ::add-button-disabled)
                         (rum/local false ::medium-editor)
                         (rum/local nil ::esc-key-listener)
                         (rum/local nil ::focus-listener)
                         (rum/local nil ::blur-listener)
                         (rum/local false ::emoji-picker-open)
                         {:did-mount (fn [s]
                           (utils/after 2500 #(js/emojiAutocomplete))
                           (let [add-comment-node (rum/ref-node s "add-comment")
                                 users-list (:mention-users @(drv/get-ref s :team-roster))
                                 medium-editor (cu/setup-medium-editor add-comment-node users-list)]
                             (reset! (::medium-editor s) medium-editor)
                             (.subscribe medium-editor
                              "editableInput"
                              (partial editable-input-change s))
                             (reset! (::focus-listener s)
                              (events/listen add-comment-node EventType/FOCUS
                               #(focus-add-comment s)))
                             (reset! (::blur-listener s)
                              (events/listen add-comment-node EventType/BLUR
                               #(disable-add-comment-if-needed s)))
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
  (let [add-comment-focus (= (drv/react s :add-comment-focus) (:uuid activity-data))]
    [:div.add-comment-box-container
      [:div.add-comment-box
        {:class (utils/class-set {:show-buttons add-comment-focus})}
        [:div.add-comment-internal
          [:div.add-comment.emoji-autocomplete.emojiable.oc-mentions.oc-mentions-hover
           {:ref "add-comment"
            :content-editable true
            :class utils/hide-class}]
          (when (and (not (js/isIE))
                     (not (responsive/is-tablet-or-mobile?)))
            (emoji-picker {:width 32
                           :height 32
                           :position "bottom"
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
                           :will-open-picker #(reset! (::emoji-picker-open s) true)
                           :will-close-picker #(do
                                                 (reset! (::emoji-picker-open s) false)
                                                 (when-not add-comment-focus
                                                   (disable-add-comment-if-needed s)))
                           :container-selector "div.add-comment-box"}))]
        (when add-comment-focus
          [:div.add-comment-footer.group
            [:button.mlb-reset.reply-btn
              {:on-click #(let [add-comment-div (rum/ref-node s "add-comment")
                                comment-body (cu/add-comment-content add-comment-div)]
                            (set! (.-innerHTML add-comment-div) "")
                            (comment-actions/add-comment activity-data comment-body))
               :disabled @(::add-button-disabled s)}
              "Comment"]
            [:button.mlb-reset.cancel-btn
              {:on-click #(let [add-comment-div (rum/ref-node s "add-comment")
                                comment-body (cu/add-comment-content add-comment-div)]
                            (set! (.-innerHTML add-comment-div) "")
                            (comment-actions/add-comment-blur))}
              "Cancel"]])]]))