(ns oc.web.stores.foc-menu
  (:require [oc.web.dispatcher :as dispatcher]))

(defn- show-menu-item [db item val]
  (as-> db tdb
    ;; Hide all the menu items
    (merge tdb {:foc-show-menu nil
                :foc-menu-open nil
                :foc-labels-picker nil
                :foc-activity-move nil
                :foc-share-entry nil})
    ;; Show the current item if necessary
    (assoc tdb item val)
    ;; Hide the menu if no visible item
    (assoc tdb :foc-show-menu val)))

(defmethod dispatcher/action :foc-show-menu
  [db [_ val]]
  (show-menu-item db :foc-show-menu val))

(defmethod dispatcher/action :foc-share-entry
  [db [_ val]]
  (show-menu-item db :foc-share-entry val))

(defmethod dispatcher/action :foc-labels-picker
  [db [_ val]]
  (show-menu-item db :foc-labels-picker val))

(defmethod dispatcher/action :foc-menu-open
  [db [_ val]]
  (show-menu-item db :foc-menu-open val))

(defmethod dispatcher/action :foc-activity-move
  [db [_ val]]
  (show-menu-item db :foc-activity-move val))
