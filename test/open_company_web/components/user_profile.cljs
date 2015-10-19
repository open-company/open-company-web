(ns open-company-web.components.user-profile
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.navbar :refer [navbar]]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.router :as router]))

(defcomponent user-profile [data owner]
  (render [_]
    (let [user-data (jwt/decode jwt/jwt)]
      (dom/div {:class "user-profile row"}
        (om/build navbar {})
        (dom/form {:class "form-horizontal"}
          (dom/h2 {} (:name user-data)))))))