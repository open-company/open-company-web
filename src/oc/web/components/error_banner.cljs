(ns oc.web.components.error-banner
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [goog.events :as events]
            [goog.fx.dom :refer (Fade)]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]))

(rum/defcs error-banner < rum/reactive
                          (rum/local false ::showing)
                          (drv/drv :error-banner)
                          {:before-render
                            (fn [s]
                             (let [error-banner @(drv/get-ref s :error-banner)
                                   message (:error-banner-message error-banner)
                                   showing-time (:error-banner-time error-banner)
                                   showing @(::showing s)
                                   banner-el (sel1 [:div.error-banner])]
                               (when (and (not showing)
                                          (seq message))
                                  (reset! (::showing s) true)
                                  ;; dismiss the message only if required
                                  (when (pos? showing-time)
                                    (utils/after showing-time
                                     (fn []
                                      (dis/dispatch! [:error-banner-show nil 0])
                                      (reset! (::showing s) false))))))
                             s)}
  [s]
  (let [banner-message (:error-banner-message (drv/react s :error-banner))]
    [:div.error-banner
      {:class (if @(::showing s) "showing" "hidden")}
      [:h3.error-message banner-message]]))
