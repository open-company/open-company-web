(ns open-company-web.components.login-button
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
      (dom/a {:href full-url
              :on-click (fn [e]
                          (.preventDefault e)
                          (when-not (.startsWith current-token "/login")
                            (cook/set-cookie! :login-redirect current-token))
                          (set! (.-location js/window) full-url))}
             (dom/img {
                       :alt "Login with Slack"
                       :height "40"
                       :width "139"
                       :src "https://platform.slack-edge.com/img/add_to_slack.png"
                       :srcSet "https://platform.slack-edge.com/img/add_to_slack.png 1x, https://platform.slack-edge.com/img/add_to_slack@2x.png 2x"
                      })))))