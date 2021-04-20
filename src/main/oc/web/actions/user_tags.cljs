(ns oc.web.actions.user-tags
  (:require [oc.lib.hateoas :as hateoas]
            [oc.web.dispatcher :as dis]
            [oc.web.api :as api]
            [clojure.set :as clj-set]
            [defun.core :refer (defun)]
            [oc.web.lib.json :refer (json->cljs)]))

(defn- refresh-user []
  (let [user-link (hateoas/link-for (:links (dis/auth-settings)) "user" "GET")]
    (api/get-user user-link (fn [success data]
                              (let [user-map (when success (json->cljs data))]
                                (dis/dispatch! [:user-data user-map]))))))

(defun ^:export tag!
  ([tag :guard string?] (when tag (tag! (keyword tag))))
  ([tag :guard keyword?]
   (when-let [cur-user (dis/current-user-data)]
     (let [cleaned-tag (if (keyword? tag) (name tag) (str tag))
           tag-link (hateoas/link-for (:links cur-user) "partial-tag" "POST" {} {:tag cleaned-tag})]
       (dis/dispatch! [:user/tag! tag])
       (api/user-tag tag-link (fn [{success :success}]
                                (when-not success
                                  (refresh-user))))))))

(defun ^:export untag!
  ([tag :guard string?] (when tag (untag! (keyword tag))))
  ([tag :guard keyword?]
   (when-let [cur-user (dis/current-user-data)]
     (let [cleaned-tag (if (keyword? tag) (name tag) (str tag))
           tag-link (hateoas/link-for (:links cur-user) "partial-untag" "DELETE" {} {:tag cleaned-tag})]
       (dis/dispatch! [:user/untag! tag])
       (api/user-tag tag-link (fn [{success :success}]
                                (when-not success
                                  (refresh-user))))))))

(defun ^:export user-tagged?
  ([tags :guard sequential?]
   (let [cleaned-tags-set (set (map keyword tags))]
     (some-> (dis/current-user-tags)
             set
             (clj-set/intersection cleaned-tags-set)
             seq)))
  ([tag]
   (dis/user-tagged? tag)))