(ns oc.web.lib.whats-new
  (:require [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.dom :as dom-utils]
            [oops.core :refer (oget ocall)]))

(def initialized (atom false))
(def latest-timeout (atom nil))

(def whats-new-selector ".whats-new")

(defn check-whats-new-badge
  "Once the number of new items is available turn on a flag in the app state if it's more than 0."
  []
  (reset! latest-timeout nil)
  (let [selector (str whats-new-selector " #HW_badge")
        el (sel1 selector)
        parsed-val (when (dom-utils/dom-node? el)
                     (int (oget el "?innerText")))]
    (if (nil? parsed-val) ;; whatsnew not yet initialized, retry
      (reset! latest-timeout (utils/after 1000 check-whats-new-badge))
      (dis/dispatch! [:input [:show-whats-new-green-dot] (pos? parsed-val)]))))

(defn- initialize
  "Until it's found look for the selector. When found wait for the headway internal
   initialization to read the number of new items."
  ([] (initialize whats-new-selector))
  ([dom-selector]
   (when (exists? js/Headway)
     (reset! latest-timeout nil)
     (if (and (not @initialized)
              (dom-utils/dom-node? (sel1 dom-selector)))
       (do
         (reset! initialized true)
         (let [headway-config (clj->js {:selector dom-selector
                                        :account "xGYD6J"
                                        :position {:y "bottom"}
                                        :translations {:title "What's New"
                                                       :footer "👉 Show me more new stuff"}})]
           (.init js/Headway headway-config)
           (reset! latest-timeout (utils/after 1000 check-whats-new-badge))))
       (reset! latest-timeout (utils/after 1000 #(initialize dom-selector)))))))

(defn init
  "Reset the initializations vars and start looking for the selector."
  []
  (reset! initialized false)
  (when @latest-timeout
    (js/clearTimeout @latest-timeout)
    (reset! latest-timeout nil))
  (initialize))

(defn show []
  (when @initialized
    (.show js/Headway)
    (utils/after 1000 check-whats-new-badge)))
