(ns oc.web.mixins.gestures
  (:require [rum.core :as rum]
            ["hammerjs" :as Hammer]))

(defn swipe-gesture-manager [{:keys [swipe-right swipe-left long-press disabled] :as options}]
  {:did-mount (fn [s]
                (if (and (fn? disabled)
                         (not (disabled s))
                         (or (fn? swipe-right)
                             (fn? swipe-left)
                             (fn? long-press)))
                  (let [el (rum/dom-node s)
                        hr (Hammer. el)]
                    (when (fn? swipe-left)
                      (.on hr "swipeleft" (partial swipe-left s)))
                    (when (fn? swipe-right)
                      (.on hr "swiperight" (partial swipe-right s)))
                    (when (fn? long-press)
                      (.on hr "press" (partial long-press s)))
                    ; (reset! (::hammer-recognizer s) hr)
                    (assoc s ::hammer-recognizer hr))
                  s))
   :will-unmount (fn [s]
                   (when-let [hr (::hammer-recognizer s)]
                     (.remove ^js hr "swipeleft")
                     (.remove ^js hr "swiperight")
                     (.remove ^js hr "pressup")
                     (.destroy ^js hr))
                   s)})