(ns oc.web.images
  (:require [clojure.string :as cstr])
  (:import [goog Uri]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FileStack

(defn- is-filestack?
  [uri]
  (= (.getDomain uri) "cdn.filestackcontent.com"))

(defn- optimize-filestack-image-url
  [url preferred-height]
  (let [uri (Uri. url)]
    (if-not (is-filestack? uri)
      url
      (let [cur-path    (.getPath uri)
            resize-frag (str "resize=height:" preferred-height)
            new-path    (str resize-frag cur-path)
            new-uri     (.setPath uri new-path)]
        (str new-uri)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Slack

(defn- is-slack?
  [uri]
  (= (.getDomain uri) "avatars.slack-edge.com"))

(defn- approximate-slack-org-height
  [pref-height]
  (cond
    (<= pref-height 34)  34
    (<= pref-height 44)  44
    (<= pref-height 68)  68
    (<= pref-height 88)  88
    (<= pref-height 102) 102
    (<= pref-height 132) 132
    :default             nil
    ))

(defn- approximate-slack-user-height
  [pref-height]
  (cond
    (<= pref-height 24)  24
    (<= pref-height 32)  32
    (<= pref-height 48)  48
    (<= pref-height 72)  72
    (<= pref-height 192) 192
    (<= pref-height 512) 512
    :default             nil
    ))

(defn- optimize-slack-avatar
  [url approx-height]
  (let [uri (Uri. url)]
    (if-not (is-slack? uri)
      url
      (let [cur-path      (.getPath uri)
            re            #"_(\d{2,3})\.([a-z]+)$"
            template      (str "_" approx-height ".$2")
            new-path      (cstr/replace cur-path re template)
            new-uri       (.setPath uri new-path)]
        (str new-uri)))))

(defn- optimize-slack-org-avatar
  [url preferred-height]
  (if-let [approx-height (approximate-slack-org-height preferred-height)]
    (optimize-slack-avatar url approx-height)
    url))

(defn- optimize-slack-user-avatar
  [url preferred-height]
  (if-let [approx-height (approximate-slack-user-height preferred-height)]
    (optimize-slack-avatar url approx-height)
    url))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Image optimization API

(defn optimize-image-url
  [url preferred-height]
  (optimize-filestack-image-url url preferred-height))

(defn optimize-user-avatar-url
  [url preferred-height]
  (-> url
      (optimize-slack-user-avatar preferred-height)
      (optimize-filestack-image-url preferred-height)))

(defn optimize-org-avatar-url
  [url preferred-height]
  (-> url
      (optimize-slack-org-avatar preferred-height)
      (optimize-filestack-image-url preferred-height)))
