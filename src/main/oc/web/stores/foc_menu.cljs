(ns oc.web.stores.foc-menu
  (:require [oc.web.dispatcher :as dispatcher]))

(defn- show-menu-item [db item val]
  (assoc db dispatcher/foc-menu-key {item val
                                     dispatcher/foc-show-menu-key val}))

(defmethod dispatcher/action :foc-show-menu
  [db [_ val]]
  (show-menu-item db dispatcher/foc-show-menu-key val))

(defmethod dispatcher/action :foc-share-entry
  [db [_ val]]
  (show-menu-item db dispatcher/foc-share-entry-key val))

(defmethod dispatcher/action :foc-labels-picker
  [db [_ val]]
  (show-menu-item db dispatcher/foc-labels-picker-key val))

(defmethod dispatcher/action :foc-menu-open
  [db [_ val]]
  (show-menu-item db dispatcher/foc-menu-open-key val))

(defmethod dispatcher/action :foc-activity-move
  [db [_ val]]
  (show-menu-item db dispatcher/foc-activity-move-key val))
