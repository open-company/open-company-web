(ns oc.web.components.ui.add-comment
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.actions.comment :as ca]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.ui :refer (first-render-mixin)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]))

(defn enable-add-comment? [s]
  (let [add-comment-div (rum/ref-node s "add-comment")
        comment-text (cu/add-comment-content add-comment-div)
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
                           (ca/add-comment-blur)
                           (let [add-comment-node (rum/ref-node s "add-comment")
                                 medium-editor (cu/setup-medium-editor add-comment-node)]
                             (reset! (::medium-editor s) medium-editor)
                             (.subscribe medium-editor
                              "editableInput"
                              (partial editable-input-change s))
                             (reset! (::focus-listener s)
                              (events/listen add-comment-node EventType/FOCUS
                               (fn [e]
                                 (enable-add-comment? s)
                                 (ca/add-comment-focus)
                                 (reset! (::show-buttons s) true))))
                             (reset! (::blur-listener s)
                              (events/listen add-comment-node EventType/BLUR
                               (fn [e]
                                 (enable-add-comment? s)
                                 (when (zero? (count (.-innerText add-comment-node)))
                                   (ca/add-comment-blur)
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
      ; [:div.add-comment-label "Add comment"]
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
                              (ca/add-comment-blur)
                              (dis/dispatch! [:comment-add activity-data (cu/add-comment-content add-comment-div)])
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