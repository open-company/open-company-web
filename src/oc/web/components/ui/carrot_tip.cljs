(ns oc.web.components.ui.carrot-tip
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(defn second-step-oval [width height px py]
  (if (responsive/is-tablet-or-mobile?)
    (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
          second-line (str "M" width "," (- height 118) "\n")
          nth-line-3 (str "L" width "," height "\n")
          nth-line-4 (str "L" (- width 114) "," height "\n")
          nth-line-5 (str "C" (- width 114) "," height
                          " " (- width 155) "," (- height 121)
                          " " width "," (- height 118)
                          "Z")]
      [:g
        {:stroke "none"
         :stroke-width "1"
         :fill "none"
         :fill-rule "evenodd"
         :opacity "0.9"}
        [:g
          {:fill "#6187F8"}
          [:g
            [:path
              {:d
                (str first-line second-line nth-line-3 nth-line-4 nth-line-5)}]]]])
    (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
          offset-x px
          offset-y py
          second-line (str "M" (+ 1071.3683 offset-x) "," (+ 218.965915 offset-y) "\n")
          nth-line-3 (str "C" (+ 1117.20129 offset-x) "," (+ 246.53226 offset-y)
                          " " (+ 1175.09029 offset-x) "," (+ 232.488897 offset-y)
                          " " (+ 1210.99409 offset-x) "," (+ 195.095574 offset-y) "\n")
          nth-line-4 (str "C" (+ 1245.68139 offset-x) "," (+ 159.057381 offset-y)
                          " " (+ 1230.25612 offset-x) "," (+ 91.3314095 offset-y)
                          " " (+ 1170.63164 offset-x) "," (+ 53.7640728 offset-y) "\n")
          nth-line-5 (str "C" (+ 1109.46966 offset-x) "," (+ 18.7555776 offset-y)
                          " " (+ 1042.43005 offset-x) "," (+ 36.9302954 offset-y)
                          " " (+ 1026.89431 offset-x) "," (+ 84.4772704 offset-y) "\n")
          nth-line-6 (str "C" (+ 1010.73378 offset-x) "," (+ 133.733542 offset-y)
                          " " (+ 1025.51147 offset-x) "," (+ 191.439458 offset-y)
                          " " (+ 1071.36836 offset-x) "," (+ 218.965915 offset-y)
                          " Z ")]
      [:g
        {:stroke "none"
         :stroke-width "1"
         :fill "none"
         :fill-rule "evenodd"
         :fill-opacity "0.9"
         :opacity "0.3"}
        [:g
          {:fill "#34414F"}
          [:g
            [:path
              {:d
                (str first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6)}]]]])))

(defn third-step-oval [width height px py]
  (if (responsive/is-tablet-or-mobile?)
    (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
          second-line (str "M" (- width 136) "," 0 "\n")
          nth-line-3 (str "L" width "," 0 "\n")
          nth-line-4 (str "L" width "," 90 "\n")
          nth-line-5 (str "C" width "," 100
                          " " (- width 124) "," 71
                          " " (- width 136) "," 0
                          "Z ")]
      [:g
        {:stroke "none"
         :stroke-width "1"
         :fill "none"
         :fill-rule "evenodd"
         :opacity "0.9"}
        [:g
          {:fill "#6187F8"}
          [:g
            [:path
              {:d
                (str first-line second-line nth-line-3 nth-line-4 nth-line-5)}]]]])
    (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
          offset-x px
          offset-y py
          second-line (str "M" (+ 1071.3683 offset-x) "," (+ 218.965915 offset-y) "\n")
          nth-line-3 (str "C" (+ 1117.20129 offset-x) "," (+ 246.53226 offset-y)
                          " " (+ 1175.09029 offset-x) "," (+ 232.488897 offset-y)
                          " " (+ 1210.99409 offset-x) "," (+ 195.095574 offset-y) "\n")
          nth-line-4 (str "C" (+ 1245.68139 offset-x) "," (+ 159.057381 offset-y)
                          " " (+ 1230.25612 offset-x) "," (+ 91.3314095 offset-y)
                          " " (+ 1170.63164 offset-x) "," (+ 53.7640728 offset-y) "\n")
          nth-line-5 (str "C" (+ 1109.46966 offset-x) "," (+ 18.7555776 offset-y)
                          " " (+ 1042.43005 offset-x) "," (+ 36.9302954 offset-y)
                          " " (+ 1026.89431 offset-x) "," (+ 84.4772704 offset-y) "\n")
          nth-line-6 (str "C" (+ 1010.73378 offset-x) "," (+ 133.733542 offset-y)
                          " " (+ 1025.51147 offset-x) "," (+ 191.439458 offset-y)
                          " " (+ 1071.36836 offset-x) "," (+ 218.965915 offset-y)
                          " Z ")]
      [:g
        {:stroke "none"
         :stroke-width "1"
         :fill "none"
         :fill-rule "evenodd"
         :fill-opacity "0.9"
         :opacity "0.3"}
        [:g
          {:fill "#34414F"}
          [:g
            [:path
              {:d
                (str first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6)}]]]])))

(defn fourth-step-oval [width height px py]
  (if (responsive/is-tablet-or-mobile?)
    (let [mobile-post-offset-y 138
          mobile-post-height 300
          first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
          second-line (str "M" 0 "," mobile-post-offset-y "\n")
          nth-line-3 (str "L" width "," mobile-post-offset-y "\n")
          nth-line-4 (str "L" width "," (+ mobile-post-offset-y mobile-post-height) "\n")
          nth-line-5 (str "L" 0 "," (+ mobile-post-offset-y mobile-post-height) "\n")
          nth-line-6 (str "L" 0 "," mobile-post-offset-y "Z")]
      [:g
        {:stroke "none"
         :stroke-width "1"
         :fill "none"
         :fill-rule "evenodd"
         :opacity "0.9"}
        [:g
          {:fill "#6187F8"}
          [:g
            [:path
              {:d
                (str first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6)}]]]])
    (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
          offset-x px
          offset-y py
          second-line (str "M" (+ 485 offset-x) "," (+ 132.916372 offset-y) "\n")
          nth-line-3 (str "C" (+ 382.44772 offset-x)  "," (+ 132.869289 offset-y)
                          " " (+ 301.172233 offset-x) "," (+ 217.560286 offset-y)
                          " " (+ 279.089994 offset-x) "," (+ 319.839231 offset-y) "\n")
          nth-line-4 (str "C" (+ 257.668873 offset-x) "," (+ 418.502182 offset-y)
                          " " (+ 349.904004 offset-x) "," (+ 519.895962 offset-y)
                          " " (+ 485 offset-x)        "," (+ 522.916353 offset-y) "\n")
          nth-line-5 (str "C" (+ 620.095996 offset-x) "," (+ 519.895962 offset-y)
                          " " (+ 712.331127 offset-x) "," (+ 418.499828 offset-y)
                          " " (+ 690.910006 offset-x) "," (+ 319.839231 offset-y) "\n")
          nth-line-6 (str "C" (+ 668.827767 offset-x) "," (+ 217.560286 offset-y)
                          " " (+ 587.55228 offset-x)  "," (+ 132.869289 offset-y)
                          " " (+ 485 offset-x)        "," (+ 132.916372 offset-y)
                          " Z ")]
      [:g
        {:stroke "none"
         :stroke-width "1"
         :fill "none"
         :fill-rule "evenodd"
         :fill-opacity "0.9"
         :opacity "0.3"}
        [:g
          {:fill "#34414F"}
          [:g
            [:path
              {:d
                (str first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6)}]]]])))

(defn carrot-tip-inner
  [{:keys [x y width height ;; container data
           arrow-top arrow-left ;; arrow data
           step-label ;; label on top right corner
           title message message-2;; content data
           button-title on-next-click button-position ;; button data
           circle-type ;; used to check if it has circle or not
           step ;; step of the NUX
           ] :as data}]
  [:div.carrot-tip.group
    {:style (when (not (responsive/is-tablet-or-mobile?))
              {:left (str x "px")
               :top (str y "px")
               :width (str width "px")
               :height (when height
                         (str height "px"))})}
    [:div.arrow
      {:style {:top (str arrow-top "px")
               :left (str arrow-left "px")}}]
    [:div.balloons-background]
    (when step-label
      [:div.carrot-tip-step
        step-label])
    [:div.carrot-tip-inner
      [:div.carrot-tip-title
        title]
      [:div.carrot-tip-description
        message]
      (when message-2
        [:div.carrot-tip-description.second-line
          message-2])
      (when (and (string? button-title)
                 (fn? on-next-click))
        [:button.mlb-reset.next-button
          {:class button-position
           :on-click (fn [e]
                       (utils/event-stop e)
                       (when (fn? on-next-click)
                         (on-next-click)))}
          button-title])]])

(rum/defc carrot-tip < rum/static
  [{:keys [x y width height ;; container data
           arrow-top arrow-left ;; arrow data
           circle-type circle-offset ;; type of background circle
           step ;; Overall nux step
           ] :as data}]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.carrot-tip-container.needs-background
      {:class (str "step-" (name step))}
      [:div.carrot-tip-background
        [:svg
          {:width "100%"
           :height "100%"
           :viewBox (str "0 0 " (.-innerWidth js/window) " " (.-innerHeight js/window))
           :version "1.1"
           :xmlns "http://www.w3.org/2000/svg"
           :xmlnsXlink "http://www.w3.org/1999/xlink"}
          (cond
           (= :2 step)
           (second-step-oval
            (.-innerWidth js/window)
            (.-innerHeight js/window)
            (+ x (:left circle-offset))
            (+ y (:top circle-offset)))
           (= :3 step)
           (third-step-oval
            (.-innerWidth js/window)
            (.-innerHeight js/window)
            (+ x (:left circle-offset))
            (+ y (:top circle-offset)))
           (= :4 step)
           (fourth-step-oval
            (.-innerWidth js/window)
            (.-innerHeight js/window)
            (+ x (:left circle-offset))
            (+ y (:top circle-offset))))]]
      (carrot-tip-inner data)]))