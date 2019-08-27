(ns oc.web.components.ui.emoji-picker
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.react-utils :as react-utils]
            [oc.web.mixins.ui :refer (on-window-click-mixin no-scroll-mixin)]
            [oc.shared.useragent :as ua]
            [goog.events :as events]
            [goog.object :as gobj]
            [goog.events.EventType :as EventType]
            [oc.shared.useragent :as ua]
            [cljsjs.react]
            [cljsjs.react.dom]
            [cljsjs.emoji-mart]))

(def emojiable-class "emojiable")

(defn emojiable-active?
  []
  (>= (.indexOf (.-className (.-activeElement js/document)) emojiable-class) 0))

(defn remove-markers [s]
  (when-let  [caret-pos @(::caret-pos s)]
    (when (= (:type caret-pos) "rangy")
      (.removeMarkers js/rangy (:selection caret-pos)))))

(defn on-click-out [s e]
  (when-not (utils/event-inside? e (rum/ref-node s "emoji-picker"))
    (remove-markers s)
    (when @(::visible s)
      (let [will-close-picker (:will-close-picker (first (:rum/args s)))]
        (when (fn? will-close-picker)
          (will-close-picker))))
    (reset! (::visible s) false)))

(defn save-caret-position [s]
  (remove-markers s)
  (let [caret-pos (::caret-pos s)
        emojiable-active (emojiable-active?)
        active-element (.-activeElement js/document)]
    (if emojiable-active
      (do
        (reset! (::last-active-element s) active-element)
        (reset! caret-pos
         (if (#{"TEXTAREA" "INPUT"} (.-tagName active-element))
           {:type "default" :selection (js/OCStaticTextareaSaveSelection)}
           {:type "rangy" :selection (.saveSelection js/rangy js/window)})))
      (reset! caret-pos nil))))

(defn replace-with-emoji [s emoji]
  (when-let [caret-pos @(::caret-pos s)]
    (if (= (:type caret-pos) "rangy")
      (do (.restoreSelection js/rangy (:selection caret-pos))
          (js/pasteHtmlAtCaret (gobj/get emoji "native") (.getSelection js/rangy js/window) false))
      (do (js/OCStaticTextareaRestoreSelection (:selection caret-pos))
          (js/pasteTextAtSelection @(::last-active-element s) (gobj/get emoji "native"))))))

(defn check-focus [s _]
  (let [container-selector (or (:container-selector (first (:rum/args s))) "document.body")
        container-node (.querySelector js/document container-selector)
        active-element (.-activeElement js/document)]
    ;; Enabled when:
    ;; active element is emojiable and active element is descendant of container
    (reset! (::disabled s)
     (or (not (emojiable-active?))
         (not container-node)
         (not (.contains container-node active-element))))))

;; ===== D3 Chart Component =====

;; Render an emoji button that reveal a picker for emoji.
;; It will add the selected emoji in place of the current selection if
;; the current activeElement has the class `emojiable`.

(rum/defcs emoji-picker <
  (rum/local false ::visible)
  (rum/local false ::caret-pos)
  (rum/local false ::last-active-element)
  (rum/local false ::disabled)
  (on-window-click-mixin on-click-out)
  (when ua/mobile?
    no-scroll-mixin)
  {:init (fn [s p] (js/rangy.init) s)
   :will-mount (fn [s]
                 (check-focus s nil)
                 (let [focusin (events/listen
                                js/document
                                EventType/FOCUSIN
                                (partial check-focus s))
                       focusout (events/listen
                                 js/document
                                 EventType/FOCUSOUT
                                 (partial check-focus s))
                       ff-click (when ua/firefox?
                                  (events/listen
                                   js/window
                                   EventType/CLICK
                                   (partial check-focus s)))
                       ff-keypress (when ua/firefox?
                                     (events/listen
                                      js/window
                                      EventType/KEYPRESS
                                      (partial check-focus s)))]
                   (merge s {::focusin-listener focusin
                             ::focusout-listener focusout
                             ::ff-window-click ff-click
                             ::ff-keypress ff-keypress})))
   :did-mount (fn [s]
                (utils/after 100 #(check-focus s nil))
                s)
   :will-unmount (fn [s] (events/unlistenByKey (::focusin-listener s))
                         (events/unlistenByKey (::focusout-listener s))
                         (when (::ff-window-click s)
                           (events/unlistenByKey (::ff-window-click s)))
                         (when (::ff-keypress s)
                           (events/unlistenByKey (::ff-keypress s)))
                         (dissoc s
                          ::focusin-listener
                          ::focusout-listener
                          ::ff-window-click
                          ::ff-keypress))}
  [s {:keys [add-emoji-cb position width height container-selector force-enabled default-field-selector
             will-open-picker will-close-picker tooltip-position]
      :as arg
      :or {position "top"
           width 25
           height 25}}]
  (let [visible (::visible s)
        caret-pos (::caret-pos s)
        last-active-element (::last-active-element s)
        disabled (::disabled s)]
    [:div.emoji-picker
      {:ref "emoji-picker"
       :style {:width (str width "px")
               :height (str height "px")}}
      [:button.emoji-button.btn-reset
        {:type "button"
         :title "Insert emoji"
         :data-placement (or tooltip-position "top")
         :data-container "body"
         :data-toggle "tooltip"
         :disabled (and (not default-field-selector) (not force-enabled) @(::disabled s))
         :on-mouse-down #(when (or default-field-selector force-enabled (not @(::disabled s)))
                           (save-caret-position s)
                           (let [vis (and (or default-field-selector
                                              force-enabled
                                              @caret-pos)
                                          (not @visible))]
                             (if vis
                               (when (fn? will-open-picker)
                                 (will-open-picker vis))
                               (when (fn? will-close-picker)
                                 (will-close-picker vis)))
                             (reset! visible vis)))}]
     (when @visible
       [:div.picker-container
         {:class (utils/class-set {position true})}
         [:button.mlb-reset.mobile-cancel-bt
          {:on-click #(do
                        (remove-markers s)
                        (when (fn? will-close-picker)
                          (will-close-picker))
                        (reset! visible false))}
          "Cancel"]
         (when-not (utils/is-test-env?)
           (react-utils/build (.-Picker js/EmojiMart)
             {:native true
              :onClick (fn [emoji event]
                         (when (and default-field-selector
                                    (not @(::caret-pos s)))
                           (utils/to-end-of-content-editable (.querySelector js/document default-field-selector))
                           (save-caret-position s))
                         (let [add-emoji? (boolean @(::caret-pos s))]
                           (when add-emoji?
                             (replace-with-emoji s emoji)
                             (remove-markers s)
                             (.focus @last-active-element))
                           (when (fn? will-close-picker)
                             (will-close-picker))
                           (reset! visible false)
                           (when (fn? add-emoji-cb)
                             (add-emoji-cb @last-active-element emoji add-emoji?))))}))])]))
