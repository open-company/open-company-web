(ns oc.web.lib.google-analytics
  (:require [oc.web.local-settings :as ls]
            [taoensso.timbre :as timbre]
            [oc.web.lib.json :refer (json->cljs cljs->json)]
            [cljsjs.google-analytics]))


(def tracking-version ls/ga-version)
(def null-value "(not set)")

(def dimensions {:TRACKING_VERSION "dimension1"
                 :CLIENT_ID "dimension2"
                 :WINDOW_ID "dimension3"
                 :HIT_ID "dimension4"
                 :HIT_TIME "dimension5"
                 :HIT_TYPE "dimension6"
                 :HIT_SOURCE "dimension7"
                 :VISIBILITY_STATE "dimension8"
                 :URL_QUERY_PARAMS "dimension9"})

(def metrics {:RESPONSE_END_TIME "metric1"
              :DOM_LOAD_TIME "metric2"
              :WINDOW_LOAD_TIME "metric3"
              :PAGE_VISIBLE "metric4"
              :MAX_SCROLL_PERCENTAGE "metric5"
              :PAGE_LOADS "metric6"})

(defn create-tracker []
  (js/ga "create" ls/ga-tracking-id "auto")
  (js/ga "set" "transport" "beacon"))

(defn track-custom-dimensions []
  (doseq [key (keys dimensions)]
    (js/ga "set" (key dimensions) null-value))
  (js/ga (fn [tracker]
           (.set tracker
             (cljs->json {(:TRACKING_VERSION dimensions) tracking-version
                          (:CLIENT_ID dimensions) (.get tracker "clientId")
                          (:WINDOW_ID dimensions) (random-uuid)
                          }))))
  (js/ga (fn [tracker]
           (let [og-task (.get tracker "buildHitTask")]
             (.set tracker "buildHitTask" (fn [model]
               (let [qt (or (.get model 'queueTime') 0)]
                 (.set model (:HIT_TIME dimensions)
                       (str (- (js/Date.) qt)) true)
                 (.set model (:HIT_ID dimensions) (random-uuid) true)
                 (.set model (:HIT_TYPE dimensions) (.get model "hitType") true)
                 (.set model (:VISIBILITY_STATE dimensions)
                       (.-visibilityState js/document) true)
                 (og-task model))))))))

(defn send-navigation-timing-metrics []
  (when (and (.-performance js/window) (.-timing (.-performance js/window)))
    (let [nt (.-timing (.-performance js/window))
          nav-start (.-navigationStart nt)
          respond-end (.round js/Math (- (.-respondEnd nt) nav-start))
          dom-loaded (.round js/Math (- (.-domContentLoadedEventStart nt)
                                          nav-start))
          window-loaded (.round js/Math (- (.-loadEventStart nt) nav-start))
          respond-end-clean (if (and (pos? respond-end) (< respond-end 6e6))
                              respond-end
                              0)
          dom-loaded-clean (if (and (pos? dom-loaded) (< dom-loaded 6e6))
                             dom-loaded
                             0)
          window-loaded-clean (if (and (pos? window-loaded)
                                       (< window-loaded 6e6))
                                window-loaded
                                0)]
      (js/ga "send" "event" (cljs->json
                             {:eventCategory "Navigation Timing"
                              :eventAction "track"
                              :eventLabel null-value
                              :nonInteraction true
                              (:RESPONSE_END_TIME metrics) respond-end-clean
                              (:DOM_LOAD_TIME metrics) dom-loaded-clean
                              (:WINDOW_LOAD_TIME metrics) window-loaded-clean})))))

(defn require-autotrack-plugins []
  (js/ga "require" "cleanUrlTracker"
         (cljs->json
          {:stripQuery true
           :queryDimensionIndex (re-find #"\d+$" (:URL_QUERY_PARAMS dimensions))
           :trailingSlash "remove"}))
  (js/ga "require" "maxScrollTracker"
         (cljs->json
          {:sessionTimeout 30
           :timeZone "America/New_York"
           :maxScrollMetric_index
           (re-find #"\d+$" (:MAX_SCROLL_PERCENTAGE metrics))}))
  (js/ga "require" "outboundLinkTracker"
         (cljs->json {:events ["click" "contextmenu"]}))
  (js/ga "require" "urlChangeTracker"
         (cljs->json {:fieldsObj {(keyword (:HIT_SOURCE dimensions)) "urlChangeTracker"}}))
  (js/ga "require" "pageVisibilityTracker"
         (cljs->json
          {:sendInitialPageview true
           :pageLoadsMetricIndex
           (re-find #"\d+$" (:PAGE_LOADS metrics))
           :visibleMetricIndex (re-find #"\d+$" (:PAGE_VISIBLE metrics))
           :timeZone "America/New_York"
           :fieldObjs {(keyword (:HIT_SOURCE dimensions)) "pageVisibilityTracker"}})))

(defn init []
  (create-tracker)
  (track-custom-dimensions)
  (require-autotrack-plugins)
  (send-navigation-timing-metrics))

  
