(ns oc.web.components.ui.add-comment
  (:require [rum.core :as rum]
            [goog.events :as events]
            [dommy.core :refer-macros (sel1)]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.mention :as mention-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.medium-editor-media :as me-media-utils]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.giphy-picker :refer (giphy-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]))

;; Add commnet handling

(defn enable-add-comment? [s]
  (reset! (::did-change s) true)
  (when-let [add-comment-div (rum/ref-node s "editor-node")]
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
  (when-let [add-comment-node (rum/ref-node s "editor-node")]
    (enable-add-comment? s)
    (when (zero? (count (.-innerText add-comment-node)))
      (comment-actions/add-comment-blur))))

(defn- send-clicked [s parent-comment-uuid]
  (let [add-comment-div (rum/ref-node s "editor-node")
        comment-body (cu/add-comment-content add-comment-div)
        activity-data (first (:rum/args s))]
    (set! (.-innerHTML add-comment-div) "")
    (comment-actions/add-comment activity-data comment-body parent-comment-uuid)))

(def me-options
  {:media-config ["gif" "photo" "video"]
   :placeholder "Leave a new comment…"
   :use-inline-media-picker true})

(defn add-emoji-cb [s]
  (enable-add-comment? s))

(rum/defcs add-comment < rum/reactive
                         ;; Locals
                         (rum/local nil :me/editor)
                         (rum/local nil :me/media-picker-ext)
                         (rum/local false :me/media-photo)
                         (rum/local false :me/media-video)
                         (rum/local false :me/media-attachment)
                         (rum/local false :me/media-photo-did-success)
                         (rum/local false :me/media-attachment-did-success)
                         (rum/local false :me/showing-media-video-modal)
                         (rum/local false :me/showing-gif-selector)
                         ;; Image upload lock
                         (rum/local false :me/upload-lock)
                         (rum/local "" ::add-comment-id)

                         ;; Derivatives
                         (drv/drv :media-input)
                         (drv/drv :add-comment-focus)
                         (drv/drv :add-comment-data)
                         (drv/drv :team-roster)
                         (drv/drv :current-user-data)
                         ;; Locals
                         (rum/local true ::add-button-disabled)
                         (rum/local nil ::esc-key-listener)
                         (rum/local nil ::focus-listener)
                         (rum/local nil ::blur-listener)
                         (rum/local "" ::initial-add-comment)
                         (rum/local false ::did-change)
                         ;; Mixins
                         ;; Mixins
                         ui-mixins/first-render-mixin
                         (mention-mixins/oc-mentions-hover)

                         (ui-mixins/on-window-click-mixin (fn [s e]
                          (when (and @(:me/showing-media-video-modal s)
                                     (not (.contains (.-classList (.-target e)) "media-video"))
                                     (not (utils/event-inside? e (rum/ref-node s :video-container))))
                            (me-media-utils/media-video-add s @(:me/media-picker-ext s) nil)
                            (reset! (:me/showing-media-video-modal s) false))
                          (when (and @(:me/showing-gif-selector s)
                                     (not (.contains (.-classList (.-target e)) "media-gif"))
                                     (not (utils/event-inside? e (sel1 [:div.giphy-picker]))))
                            (me-media-utils/media-gif-add s @(:me/media-picker-ext s) nil)
                            (reset! (:me/showing-gif-selector s) false))))
                         {:init (fn [s]
                           (reset! (::add-comment-id s) (utils/activity-uuid))
                           s)
                          :will-mount (fn [s]
                          (let [activity-data (first (:rum/args s))
                                add-comment-data @(drv/get-ref s :add-comment-data)
                                add-comment-activity-data (get add-comment-data (:uuid activity-data))]
                            (reset! (::initial-add-comment s) (or add-comment-activity-data "")))
                          s)
                          :did-mount (fn [s]
                           (utils/after 2500 #(js/emojiAutocomplete))
                           (let [add-comment-node (rum/ref-node s "editor-node")
                                 activity-data (first (:rum/args s))
                                 add-comment-focus @(drv/get-ref s :add-comment-focus)
                                 should-focus-field? (= (:uuid activity-data) add-comment-focus)]
                             (me-media-utils/setup-editor s enable-add-comment? me-options)
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
                                    (send-clicked s (second (:rum/args s)))))))
                             (when should-focus-field?
                               (.focus add-comment-node)
                               (utils/after 0
                                #(utils/to-end-of-content-editable add-comment-node))))
                           s)
                          :did-remount (fn [_ s]
                           (me-media-utils/setup-editor s enable-add-comment? me-options)
                           s)
                          :will-update (fn [s]
                           (let [data @(drv/get-ref s :media-input)
                                 video-data (:media-video data)]
                              (when (and @(:me/media-video s)
                                         (or (= video-data :dismiss)
                                             (map? video-data)))
                                (when (or (= video-data :dismiss)
                                          (map? video-data))
                                  (reset! (:me/media-video s) false)
                                  (dis/dispatch! [:update [:media-input] #(dissoc % :media-video)]))
                                (if (map? video-data)
                                  (me-media-utils/media-video-add s @(:me/media-picker-ext s) video-data)
                                  (me-media-utils/media-video-add s @(:me/media-picker-ext s) nil))))
                           s)
                          :will-unmount (fn [s]
                           (when @(:me/editor s)
                             (.destroy @(:me/editor s))
                             (reset! (:me/editor s) nil))
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
  [s activity-data parent-comment-uuid]
  (let [_add-comment-data (drv/react s :add-comment-data)
        _media-input (drv/react s :media-input)
        _add-comment-focus (drv/react s :add-comment-focus)
        _team-roster (drv/react s :team-roster)
        current-user-data (drv/react s :current-user-data)
        add-comment-class (str "add-comment-" @(::add-comment-id s))]
    [:div.add-comment-box-container
      [:div.add-comment-box
        (user-avatar-image current-user-data)
        [:div.add-comment-internal
          [:div.add-comment.emoji-autocomplete.emojiable.oc-mentions.oc-mentions-hover
           {:ref "editor-node"
            :class (utils/class-set {add-comment-class true
                                     :medium-editor-placeholder-hidden @(::did-change s)
                                     utils/hide-class true})
            :content-editable true
            :dangerouslySetInnerHTML #js {"__html" @(::initial-add-comment s)}}]]
        (when @(:me/showing-media-video-modal s)
          [:div.video-container
            {:ref :video-container}
            (media-video-modal {:fullscreen false
                                :dismiss-cb #(do
                                              (me-media-utils/media-video-add s @(:me/media-picker-ext s) nil)
                                              (reset! (:me/showing-media-video-modal s) false))})])
        (when @(:me/showing-gif-selector s)
          (giphy-picker {:fullscreen false
                         :pick-emoji-cb (fn [gif-obj]
                                         (reset! (:me/showing-gif-selector s) false)
                                         (me-media-utils/media-gif-add s @(:me/media-picker-ext s) gif-obj))}))
        [:div.add-comment-footer
          [:button.mlb-reset.send-btn
            {:on-click #(send-clicked s parent-comment-uuid)
             :disabled @(::add-button-disabled s)}
            "Post"]
          (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                         :width 24
                         :height 24
                         :position "top"
                         :default-field-selector (str "div." add-comment-class)
                         :container-selector (str "div." add-comment-class)})]]]))