(ns oc.web.components.ui.carrot-tip
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(defn get-onboard-image [width height px py]
  (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
        offset-x (- px 61.2729802)
        offset-y (- py 167.894197)
        second-line (str "M" (+ 163.5 offset-x) "," (+ 167.916362 offset-y) "\n")
        nth-line-3 (str "C" (+ 114.677264 offset-x) "," (+ 167.894197 offset-y)
                        " " (+ 75.9839099 offset-x) "," (+ 207.763401 offset-y)
                        " " (+ 65.4710739 offset-x) "," (+ 255.912323 offset-y) "\n")
        nth-line-4 (str "C" (+ 55.2729802 offset-x) "," (+ 302.358977 offset-y)
                        " " (+ 99.1839635 offset-x) "," (+ 350.091197 offset-y)
                        " " (+ 163.5 offset-x) "," (+ 351.513079 offset-y) "\n")
        nth-line-5 (str "C" (+ 227.816037 offset-x) "," (+ 350.091197 offset-y)
                        " " (+ 271.72702 offset-x) "," (+ 302.357868 offset-y)
                        " " (+ 261.528926 offset-x) "," (+ 255.912323 offset-y) "\n")
        nth-line-6 (str "C" (+ 251.01609 offset-x) "," (+ 207.763401 offset-y)
                        " " (+ 212.322736 offset-x) "," (+ 167.894197 offset-y)
                        " " (+ 163.5 offset-x) "," (+ 167.916362 offset-y)
                        " Z ")]
    (str first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6)))

(rum/defc carrot-tip < rum/static
  [{:keys [x y title message footer on-next-click]}]
  [:div.carrot-tip-container
    [:div.carrot-tip-background
      [:svg
        {:width "100%"
         :height "100%"
         :viewBox "0 0 100% 100"
         :version "1.1"
         :xmlns "http://www.w3.org/2000/svg"
         :xmlnsXlink "http://www.w3.org/1999/xlink"}
        [:g
          {:stroke "none"
           :stroke-width "1"
           :fill "none"
           :fill-rule "evenodd"
           :fill-opacity "0.9"
           :opacity "0.303668478"}
          [:g
            {:fill "#34414F"}
            [:g
              [:path
                {:d (get-onboard-image (.-innerWidth js/window) (.-innerHeight js/window) (- x 170) (+ y 60))}]]]]]]
    [:div.carrot-tip
      {:style {:left (str (+ x 50) "px") :top (str y "px")}}
      [:div.triangle]
      [:div.carrot-tip-inner
        [:div.carrot-tip-title
          title]
        [:div.carrot-tip-description
          message]
        [:div.carrot-tip-footer.group
          [:div.carrot-tip-num
            footer]
          [:button.mlb-reset.mlb-default.next-button
            {:on-click (fn [e]
                         (utils/event-stop e)
                         (when (fn? on-next-click)
                           (on-next-click)))}
            "Got It!"]]]]])