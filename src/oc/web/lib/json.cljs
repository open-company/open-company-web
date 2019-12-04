(ns oc.web.lib.json
  (:require [clojure.walk :refer (stringify-keys)]))

(defn json->cljs [json-str]
  (let [parsed-json (.parse js/JSON json-str :keywordize-keys true)]
    (js->clj parsed-json :keywordize-keys true)))

(defn cljs->json [coll]
  (let [stringified-coll (stringify-keys coll)]
    (clj->js stringified-coll)))