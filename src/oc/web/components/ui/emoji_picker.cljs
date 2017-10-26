(ns oc.web.components.ui.emoji-picker
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.react-utils :as react-utils]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as googobj]
            [cljsjs.emojione-picker]
            [cljsjs.react]
            [cljsjs.react.dom]))

(def emojiable-class "emojiable")

(defn remove-markers [s]
  (when @(::caret-pos s)
    (.removeMarkers js/rangy @(::caret-pos s))))

(defn on-click-out [s e]
  (when-not (utils/event-inside? e (sel1 [:div.emoji-picker]))
    (remove-markers s)
    (let [prior-value @(::visible s)]
      (reset! (::visible s) false)
      (let [will-hide-picker (:will-hide-picker (first (:rum/args s)))]
        (when (and prior-value (fn? will-hide-picker))
          (will-hide-picker))))))

(defn save-caret-position [s]
  (remove-markers s)
  (let [caret-pos (::caret-pos s)]
    (if (>= (.indexOf (.-className (.-activeElement js/document)) emojiable-class) 0)
      (do
        (reset! (::last-active-element s) (.-activeElement js/document))
        (reset! caret-pos (.saveSelection js/rangy js/window)))
      (reset! caret-pos nil))))

(defn replace-with-emoji [caret-pos emoji]
  (when @caret-pos
    (.restoreSelection js/rangy @caret-pos)
    (let [unicode-str (googobj/get emoji "unicode")
          unicodes  (clojure.string/split unicode-str #"-")
          unicode-c (apply str (map utils/unicode-char unicodes))]
        (js/pasteHtmlAtCaret unicode-c (.getSelection js/rangy js/window) false))))

(defn check-focus [s _]
  (let [active-element (googobj/get js/document "activeElement")]
    (reset! (::disabled s) (< (.indexOf (.-className active-element) emojiable-class) 0))))

;; ===== D3 Chart Component =====

;; Render an emoji button that reveal a picker for emoji.
;; It will add the selected emoji in place of the current selection if
;; the current activeElement has the class `emojiable`.

(rum/defcs emoji-picker <
  
  (rum/local false ::visible)
  (rum/local false ::caret-pos)
  (rum/local false ::last-active-element)
  (rum/local false ::disabled)
  
  {:init (fn [s p] (js/rangy.init) s)
   :will-mount (fn [s]
                 (check-focus s nil)
                 s)
   :did-mount (fn [s] (when-not (utils/is-test-env?)
                        (let [click-listener (events/listen (.-body js/document) EventType/CLICK (partial on-click-out s))
                              focusin (events/listen js/document EventType/FOCUSIN (partial check-focus s))
                              focusout (events/listen js/document EventType/FOCUSOUT (partial check-focus s))]
                          (merge s {::click-listener click-listener
                                    ::focusin-listener focusin
                                    ::focusout-listener focusout}))))
  
   :will-unmount (fn [s] (events/unlistenByKey (::click-listener s))
                         (events/unlistenByKey (::focusin-listener s))
                         (events/unlistenByKey (::focusout-listener s))
                         (dissoc s ::click-listener ::focusin-listener ::focusout-listener))}
  
  [s {:keys [add-emoji-cb position width height will-show-picker will-hide-picker]
      :or {:position "bottom"
           :width 25
           :height 25}}]
  (let [visible (::visible s)
        caret-pos (::caret-pos s)
        last-active-element (::last-active-element s)
        disabled (::disabled s)]
    [:div.emoji-picker.relative
      {:style {:width (str width "px")
               :z-index 1132
               :height (str height "px")}}
      [:button
        {:class (str "emoji-button btn-reset" (when @disabled " disabled"))
         :type "button"
         :title "Insert emoji"
         :data-placement "top"
         :data-container "body"
         :data-toggle "tooltip"
         :on-mouse-down #(save-caret-position s)
         :on-click #(do
                      (.preventDefault %)
                      (let [vis (and @caret-pos (not @visible))]
                        (when (and vis (fn? will-show-picker))
                          (will-show-picker))
                        (when (and vis (fn? will-hide-picker))
                          (will-hide-picker))
                        (reset! visible vis)))}]
      [:div.picker-container.absolute
        {:style {:display (if @visible "block" "none")
                 :top (if (= position "bottom") (str height "px") "-220px")
                 :right "-10px"}}
        (when-not (utils/is-test-env?)
          (react-utils/build js/EmojionePicker {:search ""
                                                :emojione #js {:sprites true
                                                               :imageType "png"
                                                               :spritePath "https://d1wc0stj82keig.cloudfront.net/emojione.sprites.png"}
                                                :onChange (fn [emoji]
                                                           (replace-with-emoji caret-pos emoji)
                                                           (remove-markers s)
                                                           (reset! visible false)
                                                           (.focus @last-active-element)
                                                           (when (fn? add-emoji-cb)
                                                              (add-emoji-cb @last-active-element emoji)))}))]]))