(ns open-company-web.components.login
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent login [data owner]
  (render [_]
    (dom/div {:class "login-container"}
      (dom/h1 "Login with slack:")
      (case

        (:loading data)
        (dom/h2 "Loading...")

        (and (not (:loading data)) (contains? data :auth-settings))
        (dom/a {:href (:full-url (:auth-settings data))}
          (dom/img {:alt "Login with Slack"
                    :height "40"
                    :width "139"
                    :src "https://platform.slack-edge.com/img/add_to_slack.png"
                    :srcset "https://platform.slack-edge.com/img/add_to_slack.png 1x, https://platform.slack-edge.com/img/add_to_slack@2x.png 2x"}))))))