(ns oc.web.lib.json
  (:require [cognitect.transit :as t]
            [clojure.walk :refer (keywordize-keys stringify-keys)]))

(defn json->cljs [json]
  (let [reader (t/reader :json)]
    (keywordize-keys (t/read reader json))))

(defn cljs->json [coll]
  (let [stringified-coll (stringify-keys coll)]
    (clj->js stringified-coll)))