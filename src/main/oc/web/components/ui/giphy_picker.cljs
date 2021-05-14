(ns oc.web.components.ui.giphy-picker
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.local-settings :as ls]
            [oc.web.utils.rum :as rutils]
            [oc.web.utils.dom :as dom-utils]
            ["@bago2k4/react-giphy-selector" :refer (Selector)]))

(def giphy-selector (partial rutils/build Selector))

(def giphy-picker-max-height 408)

(defn- maybe-save-position [s]
  (when (nil? @(::pos s))
    (let [trigger-selector (-> s :rum/args first :trigger-selector)
          el (sel1 trigger-selector)]
      (when-let [rect (dom-utils/bounding-rect el)]
        (let [viewport-height (dom-utils/viewport-height)
              below-middle-screen? (> (int (:top rect)) (/ (int viewport-height) 2))
              pos (cond-> {:left (str (int (:left rect)) "px")
                           :top (str (int (+ (:top rect) (:height rect))) "px")}
                    below-middle-screen? (assoc :transform (str "translateY(calc(-100% - " (int (:height rect)) "px))")))]
          (reset! (::pos s) pos))))))

(rum/defcs giphy-picker < rum/static
  (rum/local nil ::pos)
  {:will-mount (fn [s]
                 (when-not (responsive/is-mobile-size?)
                   (maybe-save-position s))
                 s)
   :did-mount (fn [s]
                (when-not (responsive/is-mobile-size?)
                  (maybe-save-position s))
                s)
   :will-remount (fn [_ s]
                (when-not (responsive/is-mobile-size?)
                  (maybe-save-position s))
                s)}
  [s {:keys [fullscreen pick-emoji-cb trigger-selector]}]
  (let [style @(::pos s)]
    [:div.giphy-picker
      {:style style
       :class (if (map? style) "fixed-anchor" "absolute-anchor")}
      (giphy-selector {:apiKey ls/giphy-api-key
                       :queryInputPlaceholder "Search for GIF"
                       :resultColumns 1
                       :preloadTrending true
                       :containerClassName "giphy-picker-container"
                       :queryFormAutoFocus true
                       :queryFormClassName "giphy-picker-form"
                       :queryFormInputClassName "giphy-picker-form-input"
                       :queryFormSubmitClassName "mlb-reset giphy-picker-form-submit"
                       :queryFormSubmitContent "Search"
                       :searchResultsClassName (str "giphy-picker-results-container" (when fullscreen " fullscreen"))
                       :searchResultClassName "giphy-picker-results-item"
                       :suggestionsClassName "giphy-picker-suggestions"
                       :suggestionClassName "giphy-picker-suggestions-suggestion"
                       :loaderClassName "giphy-picker-loader"
                       :onGifSelected pick-emoji-cb})]))