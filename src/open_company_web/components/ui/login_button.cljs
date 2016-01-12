(ns open-company-web.components.ui.login-button
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.router :as router]))

(defcomponent login-button [data owner]
  (render [_]
    (let [full-url (:full-url (:auth-settings data))
          current-token (router/get-token)]
      (dom/button {:class "btn btn-default login-button"
                   :on-click (fn [e]
                    (.preventDefault e)
                    (when-not (.startsWith current-token "/login")
                      (cook/set-cookie! :login-redirect current-token))
                    (set! (.-location js/window) full-url))}
        "Sign in / Sign up"))))