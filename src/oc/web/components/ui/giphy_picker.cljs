(ns oc.web.components.ui.giphy-picker
  (:require [rum.core :as rum]
            [oops.core :refer (oget+)]
            [dommy.core :refer-macros (sel1)]
            [oc.web.local-settings :as ls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.react-utils :as react-utils]))

(def fullscreen-giphy-picker-max-height 408)
(def default-giphy-picker-max-height 320)

(rum/defcs giphy-picker < (rum/local 0 ::offset-top)
                        {:will-mount (fn [s]

                          (when-let [picker-el (sel1 [:div.medium-editor-media-picker])]
                            (reset! (::offset-top s) (.-offsetTop picker-el)))
                         s)}
  [s {:keys [fullscreen pick-emoji-cb]}]
  (let [scrolling-element-selector (if fullscreen :div.cmail-content-outer :div.cmail-content)
        scrolling-element (sel1 [scrolling-element-selector])
        win-height (or (.-clientHeight (.-documentElement js/document))
                       (.-innerHeight js/window))
        fixed-win-height (if fullscreen win-height (- win-height 52)) ;; Remove bottom padding of Cmail view
        top-offset-limit (when-not fullscreen (.-offsetTop (sel1 [:div.rich-body-editor-outer-container])))
        top-limit (if fullscreen
                    0
                    ;; On Cmail the top limit is not 0 but it's the dark bar, so let's
                    ;; calculate the distance btw the rich body editor and the top of the bar
                    ;; and remove the bar height
                    (+ (* -1 top-offset-limit)
                       56))
        offset-top (if fullscreen
                    286
                    ;; On cmail let's get the distance of Cmail from the top of the page
                    ;; and add the distance between the rich-body-editr container
                    ;; and the first fixed element (that is the limit to calculate the absolute offset)
                    (+ (oget+ (.offset (js/$ (sel1 [:div.cmail-outer]))) :top)
                       (.-offsetTop(sel1 [:div.rich-body-editor-outer-container]))))
        ; offset-height (.-scrollHeight scrolling-element)
        scroll-top (.-scrollTop scrolling-element)
        top-position (max 0 @(::offset-top s))
        relative-position (+ top-position
                             offset-top
                             (* scroll-top -1)
                             (if fullscreen fullscreen-giphy-picker-max-height default-giphy-picker-max-height))
        adjusted-position (if (> relative-position fixed-win-height) ;; 286 is the top offset of the body
                            (max top-limit (- top-position (- relative-position fixed-win-height) 16))
                            top-position)]
    [:div.giphy-picker
      {:style {:top (str adjusted-position "px")}}
      (react-utils/build (.-Selector js/ReactGiphySelector)
       {:apiKey ls/giphy-api-key
        :queryInputPlaceholder "Search for GIF"
        :resultColumns 1
        :preloadTrending true
        :containerClassName "giphy-picker-container"
        :queryFormAutoFocus true
        :queryFormClassName "giphy-picker-form"
        :queryFormInputClassName "giphy-picker-form-input"
        :queryFormSubmitClassName "mlb-reset giphy-picker-form-submit"
        :queryFormSubmitContent "Seach"
        :searchResultsClassName (str "giphy-picker-results-container" (when fullscreen " fullscreen"))
        :searchResultClassName "giphy-picker-results-item"
        :suggestionsClassName "giphy-picker-suggestions"
        :suggestionClassName "giphy-picker-suggestions-suggestion"
        :loaderClassName "giphy-picker-loader"
        :onGifSelected pick-emoji-cb})]))