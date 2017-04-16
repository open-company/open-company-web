(ns oc.web.lib.charts
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [taoensso.timbre :as timbre]
            [oc.web.lib.utils :as utils]))

(defn load-chart-url
  "
  Load a (presumably) public Google Sheets chart URL via our proxy (to avoid CORs issues) and extract
  the parts we need to display the chart. Then display it.

  Public Google Sheets chart URLs look like:
  https://docs.google.com/spreadsheets/d/1X5Ar6_JJ3IviO64-cJ0DeklFuS42BSdXxZV6x5W0qOc/pubchart?oid=1033950253&format=interactive

  Our proxy request URLs look like:
  /_/sheets-proxy/1X5Ar6_JJ3IviO64-cJ0DeklFuS42BSdXxZV6x5W0qOc/pubchart?oid=1033950253&format=interactive
  "
  [chart-url card-width]
  
  (let [url-fragment (last (clojure.string/split chart-url #"/spreadsheets/d/"))
        chart-proxy-url (str "/_/sheets-proxy/" url-fragment)]
    (go
      (let [resp (<! (http/get chart-proxy-url {:with-credentials? false})) ; get the chart via the proxy
            chart-dom (js/$ (str "<div>" (:body resp) "</div>"))
            non-src-scripts (js/$ "script[type='text/javascript']:not([src])" chart-dom)
            src-scripts (js/$ "script[src]" chart-dom)]

          (dotimes [idx (.-length src-scripts)]
            (let [src (.-src (.get src-scripts idx))
                  splitted (clojure.string/split src #"/")
                  final-src (str "https://docs.google.com/" (clojure.string/join "/" (subvec splitted 3)) "?no-cache=" (rand 4))]
              (.append (js/$ "body") (js/$ (str "<script type='text/javascript' src='" final-src "' />")))))

          (dotimes [idx (.-length non-src-scripts)]
            (let [script-js-str (.-innerHTML (.get non-src-scripts idx))
                  rp (js/RegExp "(\"width\":\\d+)" "gi")
                  fixed-string (.replace script-js-str rp (str "\"width\":" card-width))
                  r-chart (js/RegExp "safeDraw\\(document.getElementById\\('c'\\)\\)" "gi")
                  fixed-string-c (.replace fixed-string r-chart (str "safeDraw(document.getElementById('chart-" chart-url "'))"))]
              (.call js/eval js/window fixed-string-c)))))))