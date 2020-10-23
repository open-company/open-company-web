(ns oc.web.stores.theme
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.utils.theme :as theme-utils]))

(defmethod dispatcher/action :theme/set-setting
  [db [_ setting-theme]]
    (update-in db dispatcher/theme-key merge {dispatcher/theme-setting-key setting-theme}))

(defmethod dispatcher/action :theme/mobile-theme-changed
  [db [_ mobile-theme setting-theme]]
  (update-in db dispatcher/theme-key merge {dispatcher/theme-mobile-key  mobile-theme
                                            dispatcher/theme-setting-key setting-theme}))

(defmethod dispatcher/action :theme/desktop-theme-changed
  [db [_ desktop-theme setting-theme]]
  (update-in db dispatcher/theme-key merge {dispatcher/theme-desktop-key desktop-theme
                                            dispatcher/theme-setting-key setting-theme}))

(defmethod dispatcher/action :theme/web-theme-changed
  [db [_ web-theme setting-theme]]
  (update-in db dispatcher/theme-key merge {dispatcher/theme-web-key     web-theme
                                            dispatcher/theme-setting-key setting-theme}))

(defmethod dispatcher/action :theme/visibility-changed
  [db [_ web-theme desktop-theme mobile-theme setting-theme]]
  (let [theme-map (cond-> {dispatcher/theme-setting-key setting-theme}
                    web-theme (assoc dispatcher/theme-web-key web-theme)
                    mobile-theme (assoc dispatcher/theme-mobile-key mobile-theme)
                    desktop-theme (assoc dispatcher/theme-desktop-key desktop-theme))]
    (update-in db dispatcher/theme-key merge theme-map)))