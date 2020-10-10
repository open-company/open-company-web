(ns oc.web.mixins.gestures
  (:require [rum.core :as rum]
            [cljsjs.hammer]))

(defn swipe-gesture-manager [{:keys [swipe-right swipe-left long-press disabled] :as options}]
  {:did-mount (fn [s]
                (if (and (fn? disabled)
                         (not (disabled s))
                         (or (fn? swipe-right)
                             (fn? swipe-left)
                             (fn? long-press)))
                  (let [el (rum/dom-node s)
                        hr (js/Hammer. el)]
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
                     (.remove hr "swipeleft")
                     (.remove hr "swiperight")
                     (.remove hr "pressup")
                     (.destroy hr))
                   s)})