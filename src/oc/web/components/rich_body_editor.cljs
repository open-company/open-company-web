(ns oc.web.components.rich-body-editor
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.utils.medium-editor-media :as me-media-utils]
            [oc.web.components.ui.giphy-picker :refer (giphy-picker)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]))


(defn body-on-change [state]
  (when-not @(::did-change state)
    (reset! (::did-change state) true))
  (let [options (first (:rum/args state))
        on-change (:on-change options)]
    (on-change)))

(rum/defcs rich-body-editor  < rum/reactive
                               ;; Locals
                               (rum/local false ::did-change)
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
                               ;; Derivatives
                               (drv/drv :media-input)
                               (drv/drv :mention-users)
                               (drv/drv :users-info-hover)
                               (drv/drv :current-user-data)
                               (drv/drv :follow-publishers-list)
                               (drv/drv :followers-publishers-count)
                               ;; Mixins
                               (mention-mixins/oc-mentions-hover)
                               (on-window-click-mixin (fn [s e]
                                (when (and @(:me/showing-media-video-modal s)
                                           (not (utils/event-inside? e (sel1 [:button.media.media-video])))
                                           (not (utils/event-inside? e (rum/ref-node s :video-container))))
                                  (me-media-utils/media-video-add s @(:me/media-picker-ext s) nil)
                                  (reset! (:me/showing-media-video-modal s) false))
                                (when (and @(:me/showing-gif-selector s)
                                           (not (utils/event-inside? e (sel1 [:button.media.media-gif])))
                                           (not (utils/event-inside? e (sel1 [:div.giphy-picker]))))
                                  (me-media-utils/media-gif-add s @(:me/media-picker-ext s) nil)
                                  (reset! (:me/showing-gif-selector s) false))))
                               {:did-mount (fn [s]
                                 (let [props (first (:rum/args s))]
                                   (utils/after 300 #(me-media-utils/setup-editor s body-on-change (first (:rum/args s)))))
                                 s)
                                :did-remount (fn [o s]
                                 (me-media-utils/setup-editor s body-on-change (first (:rum/args s)))
                                 (when (not= (:cmail-key (first (:rum/args o))) (:cmail-key (first (:rum/args s))))
                                   (when @(:me/editor s)
                                     (.destroy @(:me/editor s)))
                                   (reset! (:me/editor s) nil)
                                   (reset! (:me/media-picker-ext s) nil)
                                   (reset! (::did-change s) false)
                                   (utils/after 10 #(me-media-utils/setup-editor s body-on-change (first (:rum/args s)))))
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
                                   (.destroy @(:me/editor s)))
                                 s)}
  [s {:keys [initial-body
             on-change
             classes
             show-placeholder
             upload-progress-cb
             dispatch-input-key
             attachment-dom-selector
             fullscreen
             cmail-key
             paywall?]}]
  (let [_mention-users (drv/react s :mention-users)
        _media-input (drv/react s :media-input)
        _users-info-hover (drv/react s :users-info-hover)
        _current-user-data (drv/react s :current-user-data)
        _follow-publishers-list (drv/react s :follow-publishers-list)
        _followers-publishers-count (drv/react s :followers-publishers-count)
        hide-placeholder? (or (not show-placeholder) @(::did-change s))]
    [:div.rich-body-editor-outer-container
      {:key (str "rich-body-editor-" cmail-key)
       :class (when paywall? "block-edit")}
      [:div.rich-body-editor-container
        [:div.rich-body-editor.oc-mentions.oc-mentions-hover.editing
          {:ref "editor-node"
           :content-editable (not paywall?)
           :class (str classes
                   (utils/class-set {:medium-editor-placeholder-hidden hide-placeholder?
                                     :medium-editor-placeholder-relative (not hide-placeholder?)
                                     :medium-editor-element true
                                     :uploading @(:me/upload-lock s)}))
           :dangerouslySetInnerHTML (utils/emojify initial-body)}]]
      (when @(:me/showing-media-video-modal s)
        [:div.video-container
          {:ref :video-container}
          (media-video-modal {:fullscreen fullscreen
                              :dismiss-cb #(do
                                            (me-media-utils/media-video-add s @(:me/media-picker-ext s) nil)
                                            (reset! (:me/showing-media-video-modal s) false))
                              :offset-element-selector [:div.rich-body-editor-outer-container]
                              :outer-container-selector [:div.cmail-content-outer]})])
      (when @(:me/showing-gif-selector s)
        (giphy-picker {:fullscreen fullscreen
                       :pick-emoji-cb (fn [gif-obj]
                                       (reset! (:me/showing-gif-selector s) false)
                                       (me-media-utils/media-gif-add s @(:me/media-picker-ext s) gif-obj))
                       :offset-element-selector [:div.rich-body-editor-outer-container]
                       :outer-container-selector [:div.cmail-content-outer]}))]))
