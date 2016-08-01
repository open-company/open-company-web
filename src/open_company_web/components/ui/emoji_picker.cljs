(ns open-company-web.components.ui.emoji-picker
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljsjs.emojione-picker]
            [cljsjs.react]
            [cljsjs.react.dom]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.react-utils :as react-utils]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as googobj]))



(defn on-click-out [owner e]
  (om/set-state! owner :visible false))

(def caret-pos (atom nil))

(defn is-emojiable-focused []
  (>= (.indexOf (.-className (.-activeElement js/document)) "emojiable") 0))

(defn save-caret-position [e]
  (if (is-emojiable-focused)
    (if (.-getSelection js/window)
      (reset! caret-pos (js/window.getSelection))
      (reset! caret-pos (js/document.selection)))
    (reset! caret-pos nil)))

(defn replace-with-emoji [emoji]
  (when caret-pos
    (let [unicode      (googobj/get emoji "unicode")
          unicode-c    (utils/unicode-char unicode)
          shortname    (subs (googobj/get emoji "shortname") 1 (dec (count (googobj/get emoji "shortname"))))
          new-html     (str "<img class=\"emojione\" alt=\"" unicode-c "\" src=\"//cdn.jsdelivr.net/emojione/assets/png/" unicode ".png?" (.-cacheBustParam js/emojione) "\"/>")]
      (js/pasteHtmlAtCaret new-html @caret-pos false))))

(defcomponent emoji-picker
  "Render an emoji button that reveal a picker for emoji.
   It will add the selected emoji in place of the current selection if
   the current activeElement has the class `emojiable`."
  [data owner]

  (init-state [_]
    {:visible false
     :click-listener nil})

  (did-mount [_]
    (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
    (let [click-listener (events/listen (.-body js/document) EventType/CLICK (partial on-click-out owner))]
      (om/set-state! owner :click-listener click-listener)))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :click-listener)))

  (render-state [_ {:keys [visible]}]
    (dom/div {:class "emoji-picker relative"
              :style {:width "15px"
                      :z-index 1044
                      :height "15px"}}
      (dom/button {:class "emoji-button btn-reset"
                   :style {:font-size "15px"}
                   :title "Insert emoji"
                   :type "button"
                   :data-toggle "tooltip"
                   :data-placement "top"
                   :on-mouse-down #(save-caret-position %)
                   :on-click #(when @caret-pos
                                (om/update-state! owner :visible not))}
        (dom/i {:class "fa fa-smile-o"}))
      (dom/div {:clas "picker-container absolute"
                :style {:display (if visible "block" "none")
                        :top "0"
                        :left "0"}}
        (react-utils/build js/EmojionePicker {:search true :onChange #(replace-with-emoji %)})))))