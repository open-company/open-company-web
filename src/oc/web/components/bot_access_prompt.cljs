(ns oc.web.components.bot-access-prompt
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]))

(rum/defcs bot-access-prompt < rum/static
                               {:before-render (fn [s]
                                                 (when (and (:auth-settings @dis/app-state)
                                                            (not (:enumerate-users-requested @dis/app-state)))
                                                   (dis/dispatch! [:enumerate-users]))
                                                 s)}
  [s maybe-later-cb]
  [:div.bot-access-prompt.fullscreen-page.group
    [:div.org-editor-box.group.navbar-offset
      [:div
        [:div.slack-disclaimer "OpenCompany has a " [:span.bold "Slack bot"] " to make your life easier."]
        [:div.slack-disclaimer "The bot makes it easy to " [:span.bold "share updates to Slack"] ", and " [:span.bold "invite Slack teammates"] "that haven’t signed up."]
        [:div.slack-disclaimer "The bot will " [:span.bold "never"] " do anything without your permission."]
        [:botton.btn-reset.btn-link
          {:on-click maybe-later-cb}
          "MAYBE LATER"]
        [:botton.btn-reset.btn-solid
          {:on-click #(dis/dispatch! [:auth-bot])}
          "LET’S ADD IT!"]]]])