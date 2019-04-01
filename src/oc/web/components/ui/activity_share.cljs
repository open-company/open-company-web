(ns oc.web.components.ui.activity-share
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.org-settings :as org-settings]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

(defn dismiss []
  (activity-actions/activity-share-hide))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss))

(defn- has-slack-bot?
  "Check if the current team has a bot associated."
  [org-data]
  (jwt/team-has-bot? (:team-id org-data)))

(defn- highlight-url
  "Select the whole content of the share link filed."
  [s]
  (when-let [url-field (rum/ref-node s "activity-share-url-field")]
    (.select url-field)))

(rum/defcs activity-share < rum/reactive
                            ;; Derivatives
                            (drv/drv :org-data)
                            (drv/drv :activity-share)
                            (drv/drv :activity-shared-data)
                            (drv/drv :activity-share-medium)
                            (drv/drv :current-user-data)
                            ;; Locals
                            (rum/local {:note ""} ::slack-data)
                            (rum/local false ::dismiss)
                            (rum/local false ::copied)
                            (rum/local false ::sharing)
                            (rum/local false ::shared)
                            (rum/local (rand 1000) ::slack-channels-dropdown-key)
                            (rum/local :team ::url-audience)
                            ;; Mixins
                            mixins/first-render-mixin
                            (on-window-click-mixin (fn [s e]
                              (when-not (utils/event-inside? e (rum/dom-node s))
                                (close-clicked s))))
                            {:will-mount (fn [s]
                              (team-actions/teams-get-if-needed)
                              (let [activity-data (:share-data @(drv/get-ref s :activity-share))
                                    org-data @(drv/get-ref s :org-data)
                                    subject (.text (.html (js/$ "<div />") (:headline activity-data)))]
                               (when (and (not @(drv/get-ref s :activity-share-medium))
                                          (has-slack-bot? org-data))
                                 (dis/dispatch! [:input [:activity-share-medium] :slack])))
                             s)
                             :did-update (fn [s]
                              ;; When we have a sharing response
                              (when-let [shared-data @(drv/get-ref s :activity-shared-data)]
                                (when (compare-and-set! (::sharing s) true false)
                                  (reset!
                                   (::shared s)
                                   (if (:error shared-data) :error :shared))
                                  ;; If share succeeded reset share fields
                                  (when-not (:error shared-data)
                                    (let [medium @(drv/get-ref s :activity-share-medium)]
                                      (cond
                                        (= medium :slack)
                                        (do
                                          (reset! (::slack-channels-dropdown-key s) (rand 1000))
                                          (reset! (::slack-data s) {:note ""})))))
                                  (utils/after 2000 #(reset! (::shared s) false)))
                                (activity-actions/activity-share-reset))
                              s)}
  [s]
  (let [activity-data (:share-data (drv/react s :activity-share))
        org-data (drv/react s :org-data)
        slack-data @(::slack-data s)
        secure-uuid (:secure-uuid activity-data)
        ;; Make sure it gets remounted when share request finishes
        _ (drv/react s :activity-shared-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        medium (drv/react s :activity-share-medium)
        has-bot? (has-slack-bot? org-data)
        can-share-to-slack? (and (not is-mobile?)
                                 has-bot?)
        cur-user-data (drv/react s :current-user-data)
        user-type (utils/get-user-type cur-user-data org-data)
        disallow-public-links? (and (:content-visibility org-data)
                                    (:disallow-public-share (:content-visibility org-data))
                                    (= user-type :author))]
    [:div.activity-share-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.activity-share-modal
        [:div.activity-share-main-cta
          (when is-mobile?
            [:button.mobile-modal-close-bt.mlb-reset
              {:on-click #(close-clicked s)}])
          "Share this post"]
        (when can-share-to-slack?
          [:div.activity-share-medium-selector-container
            [:div.activity-share-medium-selector
              {:class (when (= medium :url) "selected")
               :on-click (fn [_]
                          (when-not @(::sharing s)
                            (dis/dispatch! [:input [:activity-share-medium] :url])
                            (utils/after
                             500
                             #(highlight-url s))))}
              "URL"]
            [:div.activity-share-medium-selector
              {:class (utils/class-set {:selected (= medium :slack)})
               :on-click (fn [e]
                           (utils/event-stop e)
                           (when-not @(::sharing s)
                             (if has-bot?
                               (dis/dispatch! [:input [:activity-share-medium] :slack])
                               (when (jwt/is-admin? (:team-id org-data))
                                 (org-settings/show-modal :main)))))}
              "Slack"]])
        [:div.activity-share-divider-line
          {:class (when-not has-bot? "no-tabs")}]
        (when (= medium :url)
          [:div.activity-share-modal-shared.group
            [:form
              {:on-submit #(utils/event-stop %)}
              [:div.medium-row.group
                [:div.fields
                  [:div.checkbox-row.group
                    {:on-click #(reset! (::url-audience s) :team)}
                    [:div.checkbox
                      {:class (when (= @(::url-audience s) :team)
                                "selected")}]
                    [:div.checkbox-label "Link that requires a Carrot login"]]
                  [:div.checkbox-row.group
                    {:on-click #(reset! (::url-audience s) :all)}
                    [:div.checkbox
                      {:class (when (= @(::url-audience s) :all)
                                "selected")}]
                    [:div.checkbox-label "Link that anyone can use"]]]]
              [:div.medium-row.url-field-row.group
                (let [url-protocol (str "http" (when ls/jwt-cookie-secure "s") "://")
                      secure-url (oc-urls/secure-activity (router/current-org-slug) secure-uuid)
                      post-url (oc-urls/entry (router/current-org-slug) (:board-slug activity-data) (:uuid activity-data))
                      share-url (str url-protocol ls/web-server
                                  (if (= @(::url-audience s) :team)
                                    post-url
                                    secure-url))]
                  [:div.shared-url-container.group
                    [:input
                      {:value share-url
                       :key share-url
                       :read-only true
                       :content-editable false
                       :on-click #(highlight-url s)
                       :ref "activity-share-url-field"}]])
                [:button.mlb-reset.copy-btn
                  {:ref "activity-share-url-copy-btn"
                   :on-click (fn [e]
                              (utils/event-stop e)
                              (let [url-input (rum/ref-node s "activity-share-url-field")]
                                (highlight-url s)
                                (when (utils/copy-to-clipboard url-input)
                                  (reset! (::copied s) true)
                                  (utils/after 2000 #(reset! (::copied s) false)))))}
                  (if @(::copied s)
                    "Copied!"
                    "Copy URL")]]]])
        (when (= medium :slack)
          [:div.activity-share-share
            {:class utils/hide-class}
            [:div.mediums-box
              [:div.medium
                [:div.slack-medium.group
                  [:div.medium-row.group
                    [:div.labels
                      "Send post to"]
                    [:div.fields
                      {:class (when (:channel-error slack-data) "error")
                       :key (str "slack-share-channels-dropdown-" @(::slack-channels-dropdown-key s))}
                      (slack-channels-dropdown
                       {:on-change (fn [team channel]
                                     (reset! (::slack-data s)
                                      (merge slack-data (merge slack-data
                                                         {:channel {:channel-id (:id channel)
                                                                    :channel-name (:name channel)
                                                                    :slack-org-id (:slack-org-id team)}
                                                          :channel-error false}))))
                        :on-intermediate-change (fn [_]
                                                 (reset! (::slack-data s)
                                                  (merge slack-data (merge slack-data {:channel-error false}))))
                        :initial-value ""
                        :disabled false})]]
                  [:div.medium-row.note.group
                    [:div.labels
                      "Personal note"]
                    [:div.fields
                      [:textarea
                        {:value (:note slack-data)
                         :on-change (fn [e]
                                     (reset! (::slack-data s)
                                      (merge slack-data {:note (.. e -target -value)})))}]]]]]]
            [:div.share-footer.group
              [:div.right-buttons
                [:button.mlb-reset.mlb-black-link
                  {:on-click #(close-clicked s)}
                  "Cancel"]
                (let [send-bt-disabled? (or @(::sharing s)
                                           (empty? (:channel slack-data)))]
                  [:button.mlb-reset.share-button
                    {:on-click (fn [_]
                                (reset! (::sharing s) true)
                                (let [slack-share {:medium :slack
                                                   :note (:note slack-data)
                                                   :channel (:channel slack-data)}]
                                  (activity-actions/activity-share activity-data [slack-share])))
                     :disabled send-bt-disabled?}
                    (if @(::shared s)
                      (if (= @(::shared s) :shared)
                        "Sent!"
                        "Ops...")
                      [(when @(::sharing s)
                        (small-loading))
                       "Send"])])]]])]]))