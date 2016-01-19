(ns open-company-web.components.login
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.login-button :refer (login-button)]))

(defcomponent login [data owner]
  (render [_]
    (dom/div {:class "row"}
      (dom/div {:class "login-container col-md-5 col-md-offset-1"}
        (dom/h1 "OPENcompany login with Slack:")
        (case

          (:loading data)
          (dom/h2 "Loading...")

          (and (not (:loading data)) (contains? data :auth-settings))
          (dom/div {}
            (if-let [access (:access data)]
              (let [msg (if (= access "denied")
                "OPENcompany requires verification with your Slack team. Please allow access."
                "There is a temporary error validating with Slack. Please try again later.")]
                (dom/h4 {:class "login-error-message"} msg)))
            (om/build login-button data)))))))