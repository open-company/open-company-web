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

(defn on-click-out [s e]
  (when-not (utils/event-inside? e (sel1 [:div.emoji-picker]))
    (reset! (::visible s) false)))

(defn save-caret-position [s]
  (let [caret-pos (::caret-pos s)]
    (if (>= (.indexOf (.-className (.-activeElement js/document)) emojiable-class) 0)
      (do (reset! (::last-active-element s) (.-activeElement js/document))
        (if (.-getSelection js/window)
          (reset! caret-pos (js/window.getSelection))
          (reset! caret-pos (js/document.selection))))
      (reset! caret-pos nil))))

(defn replace-with-emoji [caret-pos emoji]
  (when @caret-pos
    (let [unicode-str (googobj/get emoji "unicode")
          unicodes  (clojure.string/split unicode-str #"-")
          unicode-c (apply str (map utils/unicode-char unicodes))
          shortname (subs (googobj/get emoji "shortname") 1 (dec (count (googobj/get emoji "shortname"))))
          new-html  (str "<img class=\"emojione\" alt=\"" unicode-c "\" src=\"//cdn.jsdelivr.net/emojione/assets/png/" unicode-str ".png?" (googobj/get js/emojione "cacheBustParam") "\"/>")]
      (js/pasteHtmlAtCaret new-html @caret-pos false))))

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
  
  {:did-mount (fn [s] (when-not (utils/is-test-env?)
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
  
  [s {:keys [add-emoji-cb position]
      :or {:position "bottom"}}]
  (let [visible (::visible s)
        caret-pos (::caret-pos s)
        last-active-element (::last-active-element s)
        disabled (::disabled s)]
    [:div.emoji-picker.relative
      {:style {:width "15px"
               :z-index 1020
               :height "15px"}}
      [:button
        {:class (str "emoji-button btn-reset" (when @disabled " disabled"))
         :style {:font-size "15px"}
         :type "button"
         :title "Add emoji"
         :data-placement "top"
         :data-container "body"
         :data-toggle "tooltip"
         :on-mouse-down #(save-caret-position s)
         :on-click #(do
                      (.preventDefault %)
                      (if (and @caret-pos (not @visible))
                        (reset! visible true)
                        (reset! visible false)))}
         [:i.fa.fa-smile-o]]
      [:div.picker-container.absolute
        {:style {:display (if @visible "block" "none")
                 :top (if (= position "bottom") "25px" "-220px")
                 :left "0"}}
        (when-not (utils/is-test-env?)
          (react-utils/build js/EmojionePicker {:search "" :onChange (fn [emoji]
                                                                         (replace-with-emoji caret-pos emoji)
                                                                         (reset! visible false)
                                                                         (.focus @last-active-element)
                                                                         (when add-emoji-cb
                                                                            (add-emoji-cb @last-active-element emoji)))}))]]))