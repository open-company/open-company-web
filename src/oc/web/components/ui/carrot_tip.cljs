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
    [:g
      {:stroke "none"
       :fill-opacity "0.9"
       :opacity "0.3"
       :fill "#34413F"}
      [:rect
        {:x "0"
         :y "0"
         :width (str (.-innerWidth js/window))
         :height "138"}]
      [:rect
        {:x "0"
         :y "438"
         :width (str (.-innerWidth js/window))
         :height (str (- (.-height (.getBoundingClientRect (.-body js/document))) 438))}]]
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

(defn fifth-step-oval [width height px py]
  (if (responsive/is-tablet-or-mobile?)
    (let [navbar-height 64]
      [:g
        {:stroke "none"
         :fill-opacity "0.9"
         :opacity "0.3"
         :fill "#34413F"}
        [:rect
          {:x "0"
           :y (str navbar-height)
           :width (str (.-innerWidth js/window))
           :height (str (- (.-height (.getBoundingClientRect (.-body js/document))) navbar-height))}]])
    (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
          offset-x px
          offset-y py
          second-line (str "M" (+ 102.465877 offset-x) "," (+ 29.4005311 offset-y) "\n")
          nth-line-3 (str "C" (+ 47.9448333 offset-x) "," (+ 50.3042759 offset-y)
                          " " (+ 19.7406369 offset-x) "," (+ 105.961005 offset-y)
                          " " (+ 26.1160043 offset-x) "," (+ 157.653849 offset-y) "\n")
          nth-line-4 (str "C" (+ 32.2024097 offset-x) "," (+ 207.543534 offset-y)
                          " " (+ 99.1875736 offset-x) "," (+ 235.501906 offset-y)
                          " " (+ 171.534127 offset-x) "," (+ 209.329474 offset-y) "\n")
          nth-line-5 (str "C" (+ 242.810869 offset-x) "," (+ 180.370089 offset-y)
                          " " (+ 273.882327 offset-x) "," (+ 114.770059 offset-y)
                          " " (+ 245.02311 offset-x) "," (+ 73.6232845 offset-y) "\n")
          nth-line-6 (str "C" (+ 215.171667 offset-x) "," (+ 30.9420608 offset-y)
                          " " (+ 156.970244 offset-x) "," (+ 8.45334202 offset-y)
                          " " (+ 102.465877 offset-x) "," (+ 29.4005311 offset-y)
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

(defn sixth-step-oval [width height px py]
  (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
        offset-x px
        offset-y py
        second-line (str "M" (+ 98.4658768 offset-x) "," (+ 587.400531 offset-y) "\n")
        nth-line-3 (str "C" (+ 43.9448333 offset-x) "," (+ 608.304276 offset-y)
                        " " (+ 15.7406369 offset-x) "," (+ 663.961005 offset-y)
                        " " (+ 22.1160043 offset-x) "," (+ 715.653849 offset-y) "\n")
        nth-line-4 (str "C" (+ 28.2024097 offset-x) "," (+ 765.543534 offset-y)
                        " " (+ 95.1875736 offset-x) "," (+ 793.501906 offset-y)
                        " " (+ 167.534127 offset-x) "," (+ 767.329474 offset-y) "\n")
        nth-line-5 (str "C" (+ 238.810869 offset-x) "," (+ 738.370089 offset-y)
                        " " (+ 269.882327 offset-x) "," (+ 672.770059 offset-y)
                        " " (+ 241.02311 offset-x) "," (+ 631.623284 offset-y) "\n")
        nth-line-6 (str "C" (+ 211.171667, offset-x) "," (+ 588.942061 offset-y)
                        " " (+ 152.970244 offset-x) "," (+ 566.453342 offset-y)
                        " " (+ 98.4658768 offset-x) "," (+ 587.400531 offset-y)
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
              (str first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6)}]]]]))

(defn is-step-with-oval
  [step]
  (cond
    (some #{step} [:2 :3 :4 :5])
    true
    :else
    false))

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
    {:style (when (and (not (responsive/is-tablet-or-mobile?))
                       (is-step-with-oval step))
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
        [:button.mlb-reset.mlb-default.next-button
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
    (if (is-step-with-oval step)
      [:div.carrot-tip-container.needs-background
        {:class (str "step-" (name step))}
        [:div.carrot-tip-background
          [:svg
            {:width "100%"
             :height "100%"
             :viewBox (str "0px 0px " (.-innerWidth js/window) "px " (.-height (.getBoundingClientRect (.-body js/document))) "px")
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
              (+ y (:top circle-offset)))
             (= :5 step)
             (fifth-step-oval
              (.-innerWidth js/window)
              (.-innerHeight js/window)
              (+ x (:left circle-offset))
              (+ y (:top circle-offset)))
             (= :6 step)
             (sixth-step-oval
              (.-innerWidth js/window)
              (.-innerHeight js/window)
              (+ x (:left circle-offset))
              (+ y (:top circle-offset))))]]
        (carrot-tip-inner data)]
      [:div.carrot-tip-container
        {:style (when-not (responsive/is-tablet-or-mobile?)
                  {:left (str x "px")
                   :top (str y "px")
                   :width (str width "px")
                   :height (str height "px")})
         :class (str "step-" (name step))}
        (carrot-tip-inner data)])))