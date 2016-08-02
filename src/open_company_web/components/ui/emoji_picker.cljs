(ns open-company-web.components.ui.emoji-picker
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.react-utils :as react-utils]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as googobj]
            [cljsjs.emojione-picker]
            [cljsjs.react]
            [cljsjs.react.dom]))

(defn on-click-out [s e]
  (when-not (utils/event-inside? e (sel1 [:div.emoji-picker]))
    (reset! (::visible s) false)))

(def default-emojiable-class "emojiable")

(defn is-emojiable-focused [editor-class]
  (>= (.indexOf (.-className (.-activeElement js/document)) editor-class) 0))

(defn save-caret-position [s editor-class]
  (let [caret-pos (::caret-pos s)]
    (if (is-emojiable-focused editor-class)
      (do (reset! (::last-active-element s) (.-activeElement js/document))
        (if (.-getSelection js/window)
          (reset! caret-pos (js/window.getSelection))
          (reset! caret-pos (js/document.selection))))
      (reset! caret-pos nil))))

(defn replace-with-emoji [caret-pos editor-class emoji]
  (when @caret-pos
    (let [unicodes  (googobj/get emoji "unicode")
          unicode   (first (clojure.string/split unicodes #"-"))
          unicode-c (utils/unicode-char unicode)
          shortname (subs (googobj/get emoji "shortname") 1 (dec (count (googobj/get emoji "shortname"))))
          new-html  (str "<img class=\"emojione\" alt=\"" unicode-c "\" src=\"//cdn.jsdelivr.net/emojione/assets/png/" unicode ".png?" (googobj/get js/emojione "cacheBustParam") "\"/>")]
      (js/pasteHtmlAtCaret new-html @caret-pos false))))

(rum/defcs emoji-picker <
  (rum/local false ::visible)
  (rum/local false ::caret-pos)
  (rum/local false ::last-active-element)
  {:did-mount (fn [s] (when-not (utils/is-test-env?)
                        (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                        (let [click-listener (events/listen (.-body js/document) EventType/CLICK (partial on-click-out s))]
                          (assoc s ::click-listener click-listener))))
   :will-unmount (fn [s] (events/unlistenByKey (::click-listener s))
                         (dissoc s ::click-listener))}
  "Render an emoji button that reveal a picker for emoji.
   It will add the selected emoji in place of the current selection if
   the current activeElement has the class `emojiable` or the custom class
   passed via component props in :editor-class."
  [s {:keys [emojiable-class]}]
  (let [fix-emojiable-class (or emojiable-class default-emojiable-class)
        visible (::visible s)
        caret-pos (::caret-pos s)
        last-active-element (::last-active-element s)]
    [:div.emoji-picker.relative
      {:style {:width "15px"
               :z-index 1020
               :height "15px"}}
      [:button.emoji-button.btn-reset
        {:style {:font-size "15px"}
         :title "Insert emoji"
         :type "button"
         :data-toggle "tooltip"
         :data-placement "top"
         :on-mouse-down #(save-caret-position s fix-emojiable-class)
         :on-click #(do
                      (utils/event-stop %)
                      (when @caret-pos
                        (reset! visible true)))}
         [:i.fa.fa-smile-o]]
      [:div.picker-container.absolute
        {:style {:display (if @visible "block" "none")
                 :top "25px"
                 :left "0"}}
        (when-not (utils/is-test-env?)
          (react-utils/build js/EmojionePicker {:search true :onChange (fn [emoji]
                                                                         (replace-with-emoji caret-pos fix-emojiable-class emoji)
                                                                         (reset! visible false)
                                                                         (.focus @last-active-element))}))]]))