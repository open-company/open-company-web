(ns oc.web.components.ui.giphy-picker
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [oc.web.local-settings :as ls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.react-utils :as react-utils]))

(def giphy-picker-max-height 408)

(rum/defcs giphy-picker < (rum/local 0 ::offset-top)
                          {:will-mount (fn [s]
                            (let [outer-container-selector (:outer-container-selector (first (:rum/args s)))]
                              (when-let [picker-el (sel1 (concat outer-container-selector [:div.medium-editor-media-picker]))]
                                (reset! (::offset-top s) (.-offsetTop picker-el))))
                           s)}
  [s {:keys [fullscreen pick-emoji-cb outer-container-selector offset-element-selector]}]
  (let [scrolling-element (if fullscreen (sel1 outer-container-selector) (.-scrollingElement js/document))
        win-height (or (.-clientHeight (.-documentElement js/document))
                       (.-innerHeight js/window))
        top-offset-limit (.-offsetTop (sel1 offset-element-selector))
        scroll-top (.-scrollTop scrolling-element)
        top-position (max 0 @(::offset-top s))
        relative-position (+ top-position
                             top-offset-limit
                             (* scroll-top -1)
                             giphy-picker-max-height)
        adjusted-position (if (> relative-position win-height)
                            (max 0 (- top-position (- relative-position win-height) 16))
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