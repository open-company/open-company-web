(ns oc.web.stores.theme
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.utils.theme :as theme-utils]))

(defn- with-computed-theme [cur-theme]
  (let [fixed-cur-theme (if (map? cur-theme) cur-theme {})
        computed-value (theme-utils/computed-value fixed-cur-theme)]
    (merge fixed-cur-theme {dispatcher/theme-computed-key computed-value})))

(defmethod dispatcher/action :theme/set-setting
  [db [_ setting-theme]]
  (let [cur-value (get-in db dispatcher/theme-key)
        with-setting-theme (assoc cur-value dispatcher/theme-setting-key setting-theme)
        next-theme (with-computed-theme with-setting-theme)]
    (assoc-in db dispatcher/theme-key next-theme)))

(defmethod dispatcher/action :theme/routing
  [db [_]]
  (update-in db dispatcher/theme-key with-computed-theme))

(defmethod dispatcher/action :theme/expo-theme
  [db [_ expo-theme setting-theme]]
  (let [cur-value (get-in db dispatcher/theme-key)
        with-expo-theme (assoc cur-value dispatcher/theme-expo-key expo-theme
                                         dispatcher/theme-setting-key setting-theme)
        next-theme (with-computed-theme with-expo-theme)]
    (assoc-in db dispatcher/theme-key next-theme)))