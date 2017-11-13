(ns oc.web.components.ui.activity-share
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.mixins :as mixins]
            [oc.web.components.ui.item-input :refer (item-input email-item)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

(defn dismiss []
  (dis/dispatch! [:activity-share-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss))

(defn- has-bot?
  "Check if the current team has a bot associated."
  [org-data]
  (jwt/team-has-bot? (:team-id org-data)))

(defn- show-slack-tooltip?
  "Show the Slack tooltip only if the user signed in with Slack
  and there our bot was added to the team."
  [org-data]
  (and (jwt/is-slack-org?)
       (not (has-bot? org-data))))

(rum/defcs activity-share < rum/reactive
                            ;; Derivatives
                            (drv/drv :org-data)
                            (drv/drv :activity-share)
                            (drv/drv :activity-shared-data)
                            ;; Locals
                            (rum/local nil ::email-data)
                            (rum/local {:note ""} ::slack-data)
                            (rum/local :url ::medium)
                            (rum/local false ::dismiss)
                            mixins/no-scroll-mixin
                            mixins/first-render-mixin
                            {:will-mount (fn [s]
                              (dis/dispatch! [:teams-get])
                              (let [activity-data (:share-data @(drv/get-ref s :activity-share))
                                    org-data @(drv/get-ref s :org-data)
                                    subject (str
                                             (:name org-data)
                                             (when (seq (:board-name activity-data))
                                              (str " " (:board-name activity-data)))
                                             ": "
                                             (.text (.html (js/$ "<div />") (:headline activity-data))))]
                               (reset! (::email-data s) {:subject subject
                                                         :note ""}))
                             s)
                             :did-mount (fn [s]
                              (.select (sel1 :input#activity-share-modal-shared-url))
                              (let [slack-button (rum/ref-node s "slack-button")
                                    org-data @(drv/get-ref s :org-data)]
                                (when (show-slack-tooltip? org-data)
                                  (.tooltip
                                   (js/$ slack-button)
                                   #js {:trigger "manual"})))
                              s)
                             :did-remount (fn [o s]
                              (let [shared-data @(drv/get-ref s :activity-shared-data)]
                                (when shared-data
                                  (close-clicked s)))
                              s)}
  [s]
  (let [activity-data (:share-data (drv/react s :activity-share))
        org-data (drv/react s :org-data)
        email-data @(::email-data s)
        slack-data @(::slack-data s)
        shared-data (drv/react s :activity-shared-data)
        secure-uuid (or (:secure-uuid shared-data) (:secure-uuid activity-data))]
    [:div.activity-share-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
            {:on-click #(close-clicked s)}]
        [:div.activity-share-modal
          [:div.activity-share-title
            "Share "
            [:span
              {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]]
          [:div.activity-share-divider-line]
          [:div.activity-share-subheadline
            "People outside your Carrot team cannot see comments."]
          [:div.activity-share-medium-selector-container
            [:div.activity-share-medium-selector
              {:class (when (= @(::medium s) :url) "selected")
               :on-click (fn [_]
                          (reset! (::medium s) :url)
                          (utils/after
                           500
                           #(when-let [activity-shared-url (sel1 :input#activity-share-modal-shared-url)]
                             (.select activity-shared-url))))}
              "URL"]
            [:div.activity-share-medium-selector
              {:class (when (= @(::medium s) :email) "selected")
               :on-click #(reset! (::medium s) :email)}
              "Email"]
            (let [slack-disabled (has-bot? org-data)
                  show-slack-tooltip? (show-slack-tooltip? org-data)]
              [:div.activity-share-medium-selector
                {:class (utils/class-set {:selected (= @(::medium s) :slack)
                                          :medium-selector-disabled slack-disabled})
                 :data-placement "top"
                 :data-container "body"
                 :title "Enable the Slack bot in Settings"
                 :ref "slack-button"
                 :on-click (fn [e]
                             (utils/event-stop e)
                             (if slack-disabled
                               (when show-slack-tooltip?
                                 (let [$this (js/$ (rum/ref-node s "slack-button"))]
                                   (.tooltip $this "show")
                                   (utils/after
                                    2000
                                    #(.tooltip $this "hide"))))
                               (reset! (::medium s) :slack)))}
                "Slack"])]
          [:div.activity-share-divider-line]
          (when (= @(::medium s) :email)
            [:div.activity-share-share
              [:div.mediums-box
                [:div.medium
                  [:div.email-medium.group
                    [:div.medium-row.group
                      [:div.labels
                        "TO"]
                      [:div.fields
                        {:class (when (:to-error email-data) "error")}
                        (item-input {:item-render email-item
                                     :match-ptn #"(\S+)[,|\s]+"
                                     :split-ptn #"[,|\s]+"
                                     :container-node :div.email-field
                                     :valid-item? utils/valid-email?
                                     :on-intermediate-change #(reset!
                                                               (::email-data s)
                                                               (merge email-data {:to-error false}))
                                     :on-change (fn [v]
                                                 (reset!
                                                  (::email-data s)
                                                  (merge email-data {:to v
                                                                     :to-error false})))})]]
                    [:div.medium-row.subject.group
                      [:div.labels
                        "SUBJECT"]
                      [:div.fields
                        {:class (when (:subject-error email-data) "error")}
                        [:input
                          {:type "text"
                           :value (:subject email-data)
                           :on-change #(reset! (::email-data s) (merge email-data {:subject (.. % -target -value)
                                                                                   :subject-error false}))}]]]
                    [:div.medium-row.note.group
                      [:div.labels
                        "ADD A NOTE"
                        [:span.optional " (optional)"]]
                      [:div.fields
                        [:textarea
                          {:value (:note email-data)
                           :on-change #(reset! (::email-data s) (merge email-data {:note (.. % -target -value)}))}]]]]]]
              [:div.share-footer.group
                [:div.left-buttons
                  [:button.mlb-reset.mlb-black-link
                    {:on-click #(close-clicked s)}
                    "Cancel"]]
                [:div.right-buttons
                  [:button.mlb-reset.mlb-default
                    {:on-click #(let [email-share {:medium :email
                                                   :note (:note email-data)
                                                   :subject (:subject email-data)
                                                   :to (:to email-data)}]
                                  (if (empty? (:to email-data))
                                    (reset! (::email-data s) (merge email-data {:to-error true}))
                                    (dis/dispatch! [:activity-share [email-share]])))
                     :class (when (empty? (:to email-data)) "disabled")}
                    "Share"]]]])
          (when (= @(::medium s) :url)
            [:div.activity-share-modal-shared.group
              (let [share-url (str
                               "http"
                               (when ls/jwt-cookie-secure
                                "s")
                               "://"
                               ls/web-server
                               (oc-urls/secure-activity (router/current-org-slug) secure-uuid))]
                [:div.shared-url-container.group
                  [:input
                    {:value share-url
                     :read-only true
                     :on-click #(.select (sel1 :input#activity-share-modal-shared-url))
                     :id "activity-share-modal-shared-url"}]])
              [:button.mlb-reset.mlb-default.copy-btn
                {:on-click (fn [_]
                            (.select (sel1 :input#activity-share-modal-shared-url))
                            (utils/copy-to-clipboard))}
                "Copy URL"]])
          (when (= @(::medium s) :slack)
            [:div.activity-share-share
              [:div.mediums-box
                [:div.medium
                  [:div.slack-medium.group
                    [:div.medium-row.group
                      [:div.labels "TO"]
                      [:div.fields
                        {:class (when (:channel-error slack-data) "error")}
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
                        "ADD A NOTE"
                        [:span.optional " (optional)"]]
                      [:div.fields
                        [:textarea
                          {:value (:note slack-data)
                           :on-change (fn [e]
                                       (reset! (::slack-data s)
                                        (merge slack-data {:note (.. e -target -value)})))}]]]]]]
              [:div.share-footer.group
                [:div.left-buttons
                  [:button.mlb-reset.mlb-black-link
                    {:on-click #(close-clicked s)}
                    "Cancel"]]
                [:div.right-buttons
                  [:button.mlb-reset.mlb-default
                    {:on-click #(let [slack-share {:medium :slack
                                                   :note (:note slack-data)
                                                   :channel (:channel slack-data)}]
                                  (if (empty? (:channel slack-data))
                                    (when (empty? (:channel slack-data))
                                      (reset! (::slack-data s) (merge slack-data {:channel-error true})))
                                    (dis/dispatch! [:activity-share [slack-share]])))
                     :class (when (empty? (:channel slack-data)) "disabled")}
                    "Share"]]]])]]]))