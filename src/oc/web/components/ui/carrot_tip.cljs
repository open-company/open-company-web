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
         :opacity "0.9"}
        [:g
          {:fill "#6187F8"}
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
         :opacity "0.9"}
        [:g
          {:fill "#6187F8"}
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
          angle "a4,4 0 0 1 4,4\n"
          second-line (str "M" px "," py "\n")
          nth-line-3 (str "L" (+ px (- 432 4)) "," py "\na4,4 0 0 1 4,4\n")
          nth-line-4 (str "L" (+ px 432) "," (+ py 300 -4) "\n" "a4,4 0 0 1 -4,4\n")
          nth-line-5 (str "L" (+ px 4) "," (+ py 300) "\n" "a4,4 0 0 1 -4,-4\n")
          nth-line-6 (str "L" px "," (+ py 4) "\n" "a4,4 0 0 1 4,-4\n" "Z")]
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
                (str first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6)}]]]])))

(defn step-element-data [step ui-el]
  (cond
    (= step :2)
    (let [offset (.offset ui-el)]
      {:x (- (aget offset "left") 284)
       :y (+ (aget offset "top") 60)
       :width 432})
    (= step :3)
    (let [offset (.offset ui-el)]
      {:x (- (aget offset "left") 304)
       :y (+ (aget offset "top") 40)
       :width 432})
    (= step :4)
    (let [offset (.offset ui-el)]
      {:x (aget offset "left")
       :y (aget offset "top")
       :width 432})))

(defn carrot-tip-position [step element-data]
  (cond
    (= step :2)
    {:left (str (- (:x element-data) 170) "px")
     :top (str (- (:y element-data) 60) "px")
     :width (str 432 "px")}
    (= step :3)
    {:left (str (- (:x element-data) 170) "px")
     :top (str (- (:y element-data) 160) "px")
     :width (str 432 "px")}
    (= step :4)
    {:left (str (+ (:x element-data) 442) "px")
     :top (str (:y element-data) "px")
     :width (str 432 "px")}))

(defn carrot-tip-inner
  [{:keys [step-label ;; label on top right corner
           title message message-2;; content data
           button-title on-next-click button-position ;; button data
           step ;; step of the NUX
           ] :as data}
   element-data]
  [:div.carrot-tip.group
    {:style (when element-data (carrot-tip-position step element-data))}
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
  [{:keys [step ;; Overall nux step
           ] :as data}]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        ui-element (when-not is-mobile?
                     (cond
                       (= step :2)
                       (js/$ "button.add-to-board-top-button")
                       (= step :3)
                       (js/$ "div.entry-edit-modal-container button.post-btn")
                       (= step :4)
                       (js/$ (.get (js/$ "div.activity-card") 0))))
        element-data (when-not is-mobile?
                       (step-element-data step ui-element))
        circle-offset (when-not is-mobile?
                        (cond
                          (= step :2)
                          {:top -170
                           :left -760}
                          (= step :3)
                          {:top -170
                           :left -760}
                          (= step :4)
                          {:top -170
                           :left -750}))]
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
            (+ (:x element-data) (:left circle-offset))
            (+ (:y element-data) (:top circle-offset)))
           (= :3 step)
           (third-step-oval
            (.-innerWidth js/window)
            (.-innerHeight js/window)
            (+ (:x element-data) (:left circle-offset))
            (+ (:y element-data) (:top circle-offset)))
           (= :4 step)
           (fourth-step-oval
            (.-innerWidth js/window)
            (.-innerHeight js/window)
            (:x element-data)
            (:y element-data)))]]
      (carrot-tip-inner data element-data)]))