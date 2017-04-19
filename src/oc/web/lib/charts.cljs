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
  [chart-url chart-id card-width]
  
  (let [url-fragment (last (clojure.string/split chart-url #"/spreadsheets/d/"))
        chart-proxy-url (str "/_/sheets-proxy/" url-fragment)]
    (go
      (let [resp (<! (http/get chart-proxy-url {:with-credentials? false})) ; get the chart via the proxy
            chart-dom (js/$ (str "<div>" (:body resp) "</div>"))
            non-src-scripts (js/$ "script[type='text/javascript']:not([src])" chart-dom)
            src-scripts (js/$ "script[src]" chart-dom)]

        (dotimes [idx (.-length non-src-scripts)]
          (let [script-js-str (.-innerHTML (.get non-src-scripts idx))
                rp (js/RegExp "(\"width\":\\d+)" "gi")
                fixed-string (.replace script-js-str rp (str "\"width\":" card-width))
                ;; Regexp to match charts exported as simple charts
                r1 (js/RegExp "safeDraw\\(document.getElementById\\('c'\\)\\)" "gi")
                ;; Regexp to match charts exported as HTML page
                r2 (js/RegExp "activeSheetId = '\\d+'; switchToSheet\\('\\d+'\\);" "gi")
                r3 (js/RegExp "\"containerId\":\"embed_\\d+\"" "gi")
                r4 (js/RegExp "posObj\\('\\d+', 'embed_\\d+', 0, 0, 0, 0\\);};" "gi")
                ;; Replace all regexp
                fixed-string-1 (.replace fixed-string r1 (str "safeDraw(document.getElementById('" chart-id "'))"))
                fixed-string-2 (.replace fixed-string-1 r2 (str "activeSheetId = '" chart-id "'; switchToSheet('" chart-id "');"))
                fixed-string-3 (.replace fixed-string-2 r3 (str "\"containerId\":\"" chart-id "\""))
                fixed-string-4 (.replace fixed-string-3 r4 (str "posObj('" chart-id "', '" chart-id "', 0, 0, 0, 0);};"))]
            (.call js/eval js/window fixed-string-4)))

        (dotimes [idx (.-length src-scripts)]
          (let [src (.-src (.get src-scripts idx))
                splitted (clojure.string/split src #"/")
                final-src (str "https://docs.google.com/" (clojure.string/join "/" (subvec splitted 3)))]
            (.append (js/$ "body") (js/$ (str "<script type='text/javascript' src='" final-src "' />")))))))))