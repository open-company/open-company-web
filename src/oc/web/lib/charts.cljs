(ns oc.web.lib.charts
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [taoensso.timbre :as timbre]
            [oc.web.lib.utils :as utils]))

(defn load-chart-url [chart-url card-width]
  (let [cors-io-url (str "https://crossorigin.me/" chart-url)]
    (go
      (let [resp (<! (http/get cors-io-url {:with-credentials? false}))
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