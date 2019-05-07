(ns oc.web.lib.whats-new
  (:require [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]))

(def initialized (atom false))
(def latest-timeout (atom nil))

(defn- check-whats-new-badge
  "Once the number of new items is available turn on a flag in the app state if it's more than 0."
  [selector]
  (reset! latest-timeout nil)
  (let [sel (str selector " #HW_badge")
        $el (js/$ sel)]
    (if (zero? (.-length $el)) ;; whatsnew not yet initialized, retry
      (reset! latest-timeout (utils/after 1000 #(check-whats-new-badge selector)))
      (dis/dispatch! [:input [:show-whats-new-green-dot] (pos? (js/parseInt (.text $el) 10))]))))

(defn- initialize
  "Until it's found look for the given selector. When found wait for the headway internal
   initialization to read the number of new items."
  [selector]
  (reset! latest-timeout nil)
  (if (and (not @initialized)
             (pos? (.-length (js/$ selector))))
    (do
      (reset! initialized true)
      (let [headway-config (clj->js {
                            :selector selector
                            :account "xGYD6J"
                            :position {:y "bottom"}
                            :translations {:title "What's New"
                                           :footer "👉 Show me more new stuff"}})]
        (.init js/Headway headway-config)
        (reset! latest-timeout (utils/after 1000 #(check-whats-new-badge selector)))))
    (reset! latest-timeout (utils/after 1000 #(initialize selector)))))

(defn init
  "Reset the initializations vars and start looking for the given selector."
  [selector]
  (reset! initialized false)
  (when @latest-timeout
    (js/clearTimeout @latest-timeout)
    (reset! latest-timeout nil))
  (initialize selector))

(defn show []
  (when @initialized
    (.show js/Headway)))