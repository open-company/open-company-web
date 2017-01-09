(ns open-company-web.components.company-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [cuerdas.core :as s]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.popover :refer (add-popover hide-popover)]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]))

(defn- create-company-alert []
  (add-popover {:container-id "create-company-alert"
                :message "Please enter a company name."
                :height "120px"
                :success-title "GOT IT"
                :success-cb #(hide-popover nil "create-company-alert")}))

(defn create-company-clicked [owner e]
  (utils/event-stop e)
  (when-not (om/get-state owner :loading)
    (let [data         (om/get-props owner)
          company-name (-> data :company-editor :name)]
      (if (clojure.string/blank? company-name)
        (create-company-alert)
        (do
          (om/set-state! owner :loading true)
          (dis/dispatch! [:company-submit]))))))

(rum/defcs bot-access-prompt < rum/static
  [s maybe-later-cb]
  [:div.company-editor-box.group.navbar-offset
    [:div
      [:div.slack-disclaimer "OpenCompany has a " [:span.bold "Slack bot"] " to make your life easier."]
      [:div.slack-disclaimer "The bot makes it easy to " [:span.bold "share updates to Slack"] ", and " [:span.bold "invite Slack teammates"] "that haven’t signed up."]
      [:div.slack-disclaimer "The bot will " [:span.bold "never"] " do anything without your permission."]
      [:botton.btn-reset.btn-link
        {:on-click maybe-later-cb}
        "MAYBE LATER"]
      [:botton.btn-reset.btn-solid
        {:on-click #(dis/dispatch! [:auth-bot])}
        "LET’S ADD IT!"]]])

(defcomponent company-editor [data owner]

  (init-state [_]
    {:loading false
     :skip-bot (or (not (jwt/is-slack-org?))
                   (and (jwt/is-slack-org?)
                        (map? (:bot (jwt/get-contents)))))})

  (did-mount [_]
    (utils/update-page-title "OpenCompany - Setup Your Company")
    (when-not (-> data :company-editor :name)
      ;; using utils/after here because we can't dispatch inside another dispatch.
      ;; ultimately we should switch to some event-loop impl that works like a proper queue
      ;; and does not have these limitations
      (utils/after 1 #(dis/dispatch! [:input [:company-editor :name] (jwt/get-key :org-name)]))))

  (render-state [_ {:keys [loading skip-bot]}]
    (dom/div {:class "company-editor"}
      (dom/div {:class "fullscreen-page group"}
        (om/build navbar {:hide-right-menu true :show-navigation-bar true})
        (if (not skip-bot)
          (bot-access-prompt #(om/set-state! owner :skip-bot true))
          (dom/div {:class "company-editor-box group navbar-offset"}
            (dom/form {:on-submit (partial create-company-clicked owner)}
              (dom/div {:class "form-group"}
                (dom/label {:class "company-editor-message"} (str "Hi" (when (jwt/jwt) (str " " (s/capital (jwt/get-key :name)))) "!"))
                (dom/label {:class "company-editor-message"} "What’s the name of your company?")
                (dom/input {:type "text"
                            :class "company-editor-input domine h4"
                            :style #js {:width "100%"}
                            :placeholder "Simple name without the Inc., LLC, etc."
                            :value (-> data :company-editor :name)
                            :on-change #(dis/dispatch! [:input [:company-editor :name] (.. % -target -value)])})))
              (dom/div {:class "center"}
                (dom/button {:class "btn-reset btn-solid get-started-button"
                             :on-click (partial create-company-clicked owner)}
                            (when loading
                              (loading/small-loading {:class "left mt1"}))
                            (dom/label {:class (str "pointer mt1" (when loading " ml2"))} "OK, LET’S GO")))))))))