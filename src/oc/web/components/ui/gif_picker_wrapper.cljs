(ns oc.web.components.ui.gif-picker-wrapper
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [oc.web.local-settings :as ls]
            [oc.web.lib.react-utils :as react-utils]))

(rum/defcs gif-picker-wrapper < (rum/local 0 ::offset-top)
                                {:will-mount (fn [s]

                                  (when-let [picker-el (sel1 [:div.medium-editor-media-picker])]
                                    ; (set! (.. picker-el -style -display) "block")
                                    (js/console.log "DBG el" picker-el "offsetTop" (.-offsetTop picker-el))
                                    (reset! (::offset-top s) (.-offsetTop picker-el))
                                    ; (set! (.. picker-el -style -display) "none")
                                    )
                                 s)}
  [s pick-emoji-cb]
  (let [scrolling-element (sel1 [:div.cmail-content-outer])
        win-height (or (.-clientHeight (.-documentElement js/document))
                       (.-innerHeight js/window))
        ; offset-height (.-scrollHeight scrolling-element)
        scroll-top (.-scrollTop scrolling-element)
        top-position (max 0 @(::offset-top s))
        relative-position (+ top-position 286 (* scroll-top -1) 408)
        adjusted-position (if (> relative-position win-height) ;; 286 is the top offset of the body
                            (max 0 (- top-position (- relative-position win-height) 16))
                            top-position)]
    [:div.giphy-picker
      {:ref :giphy-picker
       :style {:top (str adjusted-position "px")}}
      (react-utils/build (.-Selector js/ReactGiphySelector)
       {:apiKey ls/giphy-api-key
        :queryInputPlaceholder "Search for GIF"
        :resultColumns 1
        :preloadTrending true
        :containerClassName "giphy-picker-container"
        :queryFormClassName "giphy-picker-form"
        :queryFormInputClassName "giphy-picker-form-input"
        :queryFormSubmitClassName "mlb-reset giphy-picker-form-submit"
        :queryFormSubmitContent "Seach"
        :searchResultsClassName "giphy-picker-results-container"
        :searchResultClassName "giphy-picker-results-item"
        :suggestionsClassName "giphy-picker-suggestions"
        :suggestionClassName "giphy-picker-suggestions-suggestion"
        :loaderClassName "giphy-picker-loader"
        :onGifSelected pick-emoji-cb})]))