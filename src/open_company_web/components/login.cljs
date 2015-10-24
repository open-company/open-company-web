(ns open-company-web.components.login
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent login [data owner]
  (render [_]
    (dom/div {:class "row"}
      (dom/div {:class "login-container col-md-5 col-md-offset-1"}
        (dom/h1 "OpenCompany login with Slack:")
        (case

          (:loading data)
          (dom/h2 "Loading...")

          (and (not (:loading data)) (contains? data :auth-settings))
          (dom/div {}
            (if-let [access (:access data)]
              (let [msg (if (= access "denied")
                "OpenCompany requires verification with your Slack team. Please allow access."
                "There is a temporary error validating with Slack. Please try again later.")]
                (dom/h4 {:class "login-error-message"} msg)))
            (dom/a {:href (:full-url (:auth-settings data))}
              (dom/img {
                :alt "Login with Slack"
                :height "40"
                :width "139"
                :src "https://platform.slack-edge.com/img/add_to_slack.png"
                :srcSet "https://platform.slack-edge.com/img/add_to_slack.png 1x, https://platform.slack-edge.com/img/add_to_slack@2x.png 2x"
              }))))))))