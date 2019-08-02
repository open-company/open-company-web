(ns oc.web.components.ui.add-comment
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.mixins.ui :refer (first-render-mixin)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn enable-add-comment? [s]
  (when-let [add-comment-div (rum/ref-node s "add-comment")]
    (let [activity-data (first (:rum/args s))
          comment-text (cu/add-comment-content add-comment-div)
          next-add-bt-disabled (or (nil? comment-text) (zero? (count comment-text)))]
      (comment-actions/add-comment-change activity-data comment-text)
      (when (not= next-add-bt-disabled @(::add-button-disabled s))
        (reset! (::add-button-disabled s) next-add-bt-disabled)))))

(defn focus-add-comment [s]
  (enable-add-comment? s)
  (comment-actions/add-comment-focus (:uuid (first (:rum/args s)))))

(defn disable-add-comment-if-needed [s]
  (when-let [add-comment-node (rum/ref-node s "add-comment")]
    (enable-add-comment? s)
    (when (zero? (count (.-innerText add-comment-node)))
      (comment-actions/add-comment-blur))))

(defn- send-clicked [s]
  (let [add-comment-div (rum/ref-node s "add-comment")
        comment-body (cu/add-comment-content add-comment-div)
        activity-data (first (:rum/args s))]
    (set! (.-innerHTML add-comment-div) "")
    (comment-actions/add-comment activity-data comment-body)))

(defn setup-medium-editor-when-needed [s]
  (when-not @(::medium-editor s)
    (let [add-comment-node (rum/ref-node s "add-comment")
          users-list (:mention-users @(drv/get-ref s :team-roster))]
      (when (seq users-list)
        (let [medium-editor (cu/setup-medium-editor add-comment-node users-list)]
          (reset! (::medium-editor s) medium-editor)
          (.subscribe medium-editor
            "editableInput"
            #(enable-add-comment? s))
          (utils/after 100 #(enable-add-comment? s)))))))

(rum/defcs add-comment < rum/reactive
                         rum/static
                         ;; Mixins
                         first-render-mixin
                         (mention-mixins/oc-mentions-hover)
                         ;; Derivatives
                         (drv/drv :add-comment-focus)
                         (drv/drv :add-comment-data)
                         (drv/drv :team-roster)
                         (drv/drv :current-user-data)
                         ;; Locals
                         (rum/local true ::add-button-disabled)
                         (rum/local false ::medium-editor)
                         (rum/local nil ::esc-key-listener)
                         (rum/local nil ::focus-listener)
                         (rum/local nil ::blur-listener)
                         (rum/local "" ::initial-add-comment)
                         {:will-mount (fn [s]
                          (let [activity-data (first (:rum/args s))
                                add-comment-data @(drv/get-ref s :add-comment-data)
                                add-comment-activity-data (get add-comment-data (:uuid activity-data))]
                            (reset! (::initial-add-comment s) (or add-comment-activity-data "")))
                          s)
                          :did-mount (fn [s]
                           (utils/after 2500 #(js/emojiAutocomplete))
                           (let [add-comment-node (rum/ref-node s "add-comment")
                                 activity-data (first (:rum/args s))
                                 add-comment-focus @(drv/get-ref s :add-comment-focus)
                                 should-focus-field? (= (:uuid activity-data) add-comment-focus)]

                             (setup-medium-editor-when-needed s)
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
                                (fn [e]
                                  (when (and (= (.-key e) "Escape")
                                             (= (.-activeElement js/document) add-comment-node))
                                    (.blur add-comment-node))
                                  (when (and (= (.-activeElement js/document) add-comment-node)
                                             (.-metaKey e)
                                             (= (.-key e) "Enter"))
                                    (send-clicked s)))))
                             (when should-focus-field?
                               (.focus add-comment-node)
                               (utils/after 0
                                #(utils/to-end-of-content-editable add-comment-node))))
                           s)
                          :will-update (fn [s]
                           (setup-medium-editor-when-needed s)
                           s)
                          :will-unmount (fn [s]
                           (when @(::medium-editor s)
                             (.unsubscribe
                              @(::medium-editor s)
                              "editableInput"
                              #(enable-add-comment? s))
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
  (let [_ (drv/react s :add-comment-data)
        _ (drv/react s :add-comment-focus)
        _ (drv/react s :team-roster)
        current-user-data (drv/react s :current-user-data)]
    [:div.add-comment-box-container
      [:div.add-comment-box
        (user-avatar-image current-user-data)
        [:div.add-comment-internal
          [:div.add-comment.emoji-autocomplete.emojiable.oc-mentions.oc-mentions-hover
           {:ref "add-comment"
            :content-editable true
            :class utils/hide-class
            :dangerouslySetInnerHTML #js {"__html" @(::initial-add-comment s)}}]]
        [:button.mlb-reset.send-btn
          {:on-click #(send-clicked s)
           :disabled @(::add-button-disabled s)}
          "Send"]]]))