(ns oc.web.images
  (:require [clojure.string :as cstr])
  (:import [goog Uri]))

(defn- is-filestack?
  [uri]
  (= (.getDomain uri) "cdn.filestackcontent.com"))

(defn optimize-filestack-image-url
  [url preferred-height]
  (let [uri (Uri. url)]
    (if-not (is-filestack? uri)
      url
      (let [cur-path    (.getPath uri)
            resize-frag (str "resize=height:" preferred-height)
            new-path    (str resize-frag cur-path)
            new-uri     (.setPath uri new-path)]
        (str new-uri)))))

(defn- is-slack?
  [uri]
  (= (.getDomain uri) "avatars.slack-edge.com"))

(defn- approximate-slack-height
  [pref-height]
  (cond
    (<= pref-height 34)  34
    (<= pref-height 44)  44
    (<= pref-height 68)  68
    (<= pref-height 88)  88
    (<= pref-height 102) 102
    (<= pref-height 132) 132
    :default             230
    ))

(defn optimize-slack-image-url
  [url preferred-height]
  (let [uri (Uri. url)]
    (if-not (is-slack? uri)
      url
      (let [cur-path      (.getPath uri)
            re            #"_(\d{2,3})\.([a-z]+)$"
            approx-height (approximate-slack-height preferred-height)
            template      (str "_" approx-height ".$2")
            new-path      (cstr/replace cur-path re template)
            new-uri       (.setPath uri new-path)]
        (str new-uri)))))

(defn optimize-image-url
  [url preferred-height]
  (let [uri (Uri. url)]
    (cond
      (is-filestack? uri) (optimize-filestack-image-url url preferred-height)
      (is-slack? uri)     (optimize-slack-image-url url preferred-height)
      :default            url)))
