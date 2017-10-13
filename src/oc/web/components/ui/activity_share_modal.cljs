(ns oc.web.components.ui.activity-share-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.mixins :as mixins]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.item-input :refer (item-input email-item)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

(defn dismiss []
  (dis/dispatch! [:activity-share-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss))

(rum/defcs activity-share-modal < rum/reactive
                                 ;; Derivatives
                                 (drv/drv :activity-share)
                                 (drv/drv :activity-shared-data)
                                 ;; Locals
                                 (rum/local nil ::share-data)
                                 (rum/local false ::dismiss)
                                 mixins/no-scroll-mixin
                                 mixins/first-render-mixin
                                 {:will-mount (fn [s]
                                                (dis/dispatch! [:teams-get])
                                                (let [activity-data (first (:rum/args s))]
                                                  (reset! (::share-data s) {:email false
                                                                              :slack false
                                                                              :email-data {:subject (str (:name (dis/org-data))
                                                                                                         (when (not (empty? (:board-name activity-data))) " " (:board-name activity-data))
                                                                                                         ": " (.text (.html (js/$ "<textarea />") (:headline activity-data))))
                                                                                           :note ""}
                                                                              :slack-data {:note ""}}))
                                                s)
                                  :did-mount (fn [s]
                                               (utils/after 500 #(when-let [activity-shared-url (sel1 :input#activity-share-modal-shared-url)]
                                                                   (.select activity-shared-url)))
                                               s)
                                  :did-remount (fn [o s]
                                                 (utils/after 500 #(when-let [activity-shared-url (sel1 :input#activity-share-modal-shared-url)]
                                                                     (.select activity-shared-url)))
                                                 s)}
  [s]
  (let [activity-data (drv/react s :activity-share)
        share-data @(::share-data s)
        slack-data (:slack-data share-data)
        email-data (:email-data share-data)
        shared-data (drv/react s :activity-shared-data)
        shared? (not (empty? (:secure-uuid shared-data)))
        secure-uuid (if (:secure-uuid shared-data) (:secure-uuid shared-data) (:secure-uuid activity-data))]
    [:div.activity-share-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
            {:on-click #(close-clicked s)}]
        [:div.activity-share-modal
          (when (not shared?)
            [:div.title "Share " [:span {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]])
          (when shared?
            [:div.activity-share-modal-shared
              (when shared?
                [:img {:src (utils/cdn "/img/ML/caught_up.svg") :width 42 :height 42}])
              (when shared?
                [:div.shared-headline "Your post has been shared!"])
              (let [share-url (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server (oc-urls/secure-activity (router/current-org-slug) secure-uuid))]
                [:div.shared-url-container.group
                  [:input
                    {:value share-url
                     :read-only true
                     :id "activity-share-modal-shared-url"}]
                  [:button.mlb-reset.mlb-default.copy-btn
                    {:on-click (fn [_]
                                (.select (sel1 :input#activity-share-modal-shared-url))
                                (utils/copy-to-clipboard))}
                    "Copy"]])
              [:div.shared-subheadline (str "You can provide anyone with this link to your update.")]
              (when shared?
                [:button.mlb-reset.mlb-default.done-btn
                  {:on-click #(close-clicked s)}
                  "Done"])])
          (when (not shared?)
            [:div.activity-share-share
              [:div.mediums-box
                [:div.medium
                  [:div.medium-row.group
                    [:span.labels "Share via Email"]
                    (carrot-checkbox {:selected (:email share-data)
                                      :did-change-cb #(reset! (::share-data s) (merge share-data {:email (not (:email share-data))}))})]
                  (when (:email share-data)
                    [:div.email-medium.group
                      [:div.medium-row.group
                        [:span.labels "To"]
                        [:div.fields
                          {:class (when (:to-error email-data) "error")}
                          (item-input {:item-render email-item
                                       :match-ptn #"(\S+)[,|\s]+"
                                       :split-ptn #"[,|\s]+"
                                       :container-node :div.email-field
                                       :valid-item? utils/valid-email?
                                       :on-intermediate-change (fn [_]
                                                                 (reset! (::share-data s)
                                                                  (merge share-data
                                                                   {:email-data (merge email-data
                                                                    {:to-error false})})))
                                       :on-change (fn [v] (reset! (::share-data s)
                                                            (merge share-data
                                                             {:email-data (merge email-data
                                                              {:to v
                                                               :to-error false})})))})]]
                      [:div.medium-row.subject.group
                        [:span.labels "Subject"]
                        [:div.fields
                          {:class (when (:subject-error email-data) "error")}
                          [:input
                            {:type "text"
                             :value (:subject email-data)
                             :on-change #(reset! (::share-data s)
                                          (merge share-data
                                           {:email-data (merge email-data
                                             {:subject (.. % -target -value)
                                              :subject-error false})}))}]]]
                      [:div.medium-row.note.group
                        [:span.labels "Add a note (optional)"]
                        [:div.fields
                          [:textarea
                            {:value (:note email-data)
                             :on-change #(reset! (::share-data s)
                                          (merge share-data
                                           {:email-data (merge email-data
                                            {:note (.. % -target -value)})}))}]]]])]
                [:div.medium
                  [:div.medium-row.group
                    [:span.labels "Share to Slack"]
                    (carrot-checkbox {:selected (:slack share-data)
                                      :did-change-cb #(reset! (::share-data s) (merge share-data {:slack (not (:slack share-data))}))})]
                  (when (:slack share-data)
                    [:div.slack-medium.group
                      [:div.medium-row.group
                        [:span.labels "To"]
                        [:div.fields
                          {:class (when (:channel-error slack-data) "error")}
                          (slack-channels-dropdown {:on-change (fn [team channel]
                                                                 (reset! (::share-data s)
                                                                  (merge share-data
                                                                   {:slack-data (merge slack-data
                                                                    {:channel {:channel-id (:id channel)
                                                                               :channel-name (:name channel)
                                                                               :slack-org-id (:slack-org-id team)}
                                                                     :channel-error false})})))
                                                    :on-intermediate-change (fn [_]
                                                                             (reset! (::share-data s)
                                                                              (merge share-data
                                                                               {:slack-data (merge slack-data
                                                                                {:channel-error false})})))
                                                    :initial-value ""
                                                    :disabled false})]]
                      [:div.medium-row.note.group
                        [:span.labels "Add a note (optional)"]
                        [:div.fields
                          [:textarea
                            {:value (:note slack-data)
                             :on-change (fn [e]
                                         (reset! (::share-data s)
                                          (merge share-data
                                           {:slack-data (merge slack-data
                                            {:note (.. e -target -value)})})))}]]]])]]
              [:div.share-footer.group
                [:div.buttons
                  [:button.mlb-reset.mlb-black-link
                    {:on-click #(close-clicked s)}
                    "Cancel"]
                  [:button.mlb-reset.mlb-default
                    {:on-click #(let [slack-share (when (:slack share-data)
                                                    {:medium :slack
                                                     :note (:note slack-data)
                                                     :channel (:channel slack-data)})
                                      email-share (when (:email share-data)
                                                    {:medium :email
                                                     :note (:note email-data)
                                                     :subject (:subject email-data)
                                                     :to (:to email-data)})
                                      data-to-share (vec (remove nil? [(when slack-share slack-share) (when email-share email-share)]))]
                                  (if (or (and (:email share-data) (empty? (:to email-data)))
                                          (and (:slack share-data) (empty? (:channel slack-data))))
                                    (do
                                      (when (and (:email share-data) (empty? (:to email-data)))
                                        (reset! (::share-data s) (merge share-data {:email-data (merge email-data {:to-error true})})))
                                      (when (and (:slack share-data) (empty? (:channel slack-data)))
                                        (reset! (::share-data s) (merge share-data {:slack-data (merge slack-data {:channel-error true})}))))
                                    (when (not (empty? data-to-share))
                                      ;; FIXME: change share action
                                      (dis/dispatch! [:activity-share data-to-share]))))
                     :disabled (and (not (:slack share-data)) (not (:email share-data)))}
                    "Share"]]]])]]]))