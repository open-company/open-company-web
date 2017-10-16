(ns oc.web.components.ui.story-publish-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.item-input :refer (item-input email-item)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (let [close-cb (nth (:rum/args s) 1)]
    (utils/after 180 close-cb)))

(rum/defcs story-publish-modal < rum/reactive
                                 (drv/drv :story-editing-publish)
                                 (rum/local nil ::publish-data)
                                 (rum/local false ::dismiss)
                                 (rum/local false ::first-render-done)
                                 {:will-mount (fn [s]
                                                (dis/dispatch! [:teams-get])
                                                (let [story-data (first (:rum/args s))]
                                                  (reset! (::publish-data s) {:email false
                                                                              :slack false
                                                                              :email-data {:subject (str (:org-name story-data) " " (:storyboard-name story-data) ": " (.text (.html (js/$ "<textarea />") (:title story-data))))
                                                                                           :note ""}
                                                                              :slack-data {:note ""}}))
                                                s)
                                  :did-mount (fn [s]
                                               ;; Add no-scroll to the body
                                               (dommy/add-class! (sel1 [:body]) :no-scroll)
                                               (utils/after 500 #(when-let [story-published-url (sel1 :input#story-publish-modal-published-url)]
                                                                   (.select story-published-url)))
                                               s)
                                  :after-render (fn [s]
                                                  (when (not @(::first-render-done s))
                                                    (reset! (::first-render-done s) true))
                                                  s)
                                  :did-remount (fn [o s]
                                                 (utils/after 500 #(when-let [story-published-url (sel1 :input#story-publish-modal-published-url)]
                                                                     (.select story-published-url)))
                                                 s)
                                  :will-unmount (fn [s]
                                                  ;; Remove no-scroll class from the body tag
                                                  (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                                  (utils/after 100 #(dis/dispatch! [:input [:story-editing-published-url] nil]))
                                                  s)}
  [s story-data close-cb]
  (let [publish-data @(::publish-data s)
        slack-data (:slack-data publish-data)
        email-data (:email-data publish-data)
        published-data (drv/react s :story-editing-publish)
        published? (not= (:status story-data) "draft")
        shared? (not (empty? (:secure-uuid published-data)))
        secure-uuid (if (not (router/current-secure-story-id))
                      (if (:secure-uuid published-data) (:secure-uuid published-data) (:secure-uuid story-data))
                      (router/current-secure-story-id))]
    [:div.story-publish-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
            {:on-click #(close-clicked s)}]
        [:div.story-publish-modal
          (when (not shared?)
            [:div.title (if published? "Share " "Post ") (when-not published? [:span {:dangerouslySetInnerHTML (utils/emojify (:title story-data))}])])
          (when (or published?
                    shared?)
            [:div.story-publish-modal-published
              (when shared?
                [:img {:src (utils/cdn "/img/ML/caught_up.svg") :width 42 :height 42}])
              (when shared?
                (let [show-posted? (not published?)
                      show-shared? (or (:slack publish-data) (:email publish-data))
                      headline (str "Your journal has been " (when show-posted? "posted") (when (and show-posted? show-shared?) " and ") (when show-shared? "shared") "!")]
                  [:div.published-headline headline]))
              (let [publish-url (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server (oc-urls/secure-story (router/current-org-slug) secure-uuid))]
                [:div.published-url-container.group
                  [:input
                    {:value publish-url
                     :read-only true
                     :id "story-publish-modal-published-url"}]
                  [:button.mlb-reset.mlb-default.copy-btn
                    {:on-click (fn [_]
                                (.select (sel1 :input#story-publish-modal-published-url))
                                (utils/copy-to-clipboard))}
                    "Copy"]])
              [:div.published-subheadline (str "You can" (when (and (not published?) shared?) " also") " provide anyone with this link to your update.")]
              (when shared?
                [:button.mlb-reset.mlb-default.done-btn
                  {:on-click #(if published?
                               (close-clicked s)
                               (router/nav! (oc-urls/board (router/current-org-slug) (:storyboard-slug published-data))))}
                  "Done"])])
          (when (not shared?)
            [:div.story-publish-share
              (when (not published?)
                [:div.access
                  "Journals posted in "
                  [:span.storyboard-name (:storyboard-name story-data)]
                  (cond
                    (= (:access story-data) "private") " are private and can be viewed by people you invite."
                    (= (:access story-data) "public") " are public and can be viewed by anyone that has the link."
                    :else " can be viewed by anyone on the team.")])
              [:div.mediums-box
                [:div.medium
                  [:div.medium-row.group
                    [:span.labels "Share via Email"]
                    (carrot-checkbox {:selected (:email publish-data)
                                      :did-change-cb #(reset! (::publish-data s) (merge publish-data {:email (not (:email publish-data))}))})]
                  (when (:email publish-data)
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
                                                                 (reset! (::publish-data s)
                                                                  (merge publish-data
                                                                   {:email-data (merge email-data
                                                                    {:to-error false})})))
                                       :on-change (fn [v] (reset! (::publish-data s)
                                                            (merge publish-data
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
                             :on-change #(reset! (::publish-data s)
                                          (merge publish-data
                                           {:email-data (merge email-data
                                             {:subject (.. % -target -value)
                                              :subject-error false})}))}]]]
                      [:div.medium-row.note.group
                        [:span.labels "Add a note (optional)"]
                        [:div.fields
                          [:textarea
                            {:value (:note email-data)
                             :on-change #(reset! (::publish-data s)
                                          (merge publish-data
                                           {:email-data (merge email-data
                                            {:note (.. % -target -value)})}))}]]]])]
                [:div.medium
                  [:div.medium-row.group
                    [:span.labels "Share to Slack"]
                    (carrot-checkbox {:selected (:slack publish-data)
                                      :did-change-cb #(reset! (::publish-data s) (merge publish-data {:slack (not (:slack publish-data))}))})]
                  (when (:slack publish-data)
                    [:div.slack-medium.group
                      [:div.medium-row.group
                        [:span.labels "To"]
                        [:div.fields
                          {:class (when (:channel-error slack-data) "error")}
                          (slack-channels-dropdown {:on-change (fn [team channel]
                                                                 (reset! (::publish-data s)
                                                                  (merge publish-data
                                                                   {:slack-data (merge slack-data
                                                                    {:channel {:channel-id (:id channel)
                                                                               :channel-name (:name channel)
                                                                               :slack-org-id (:slack-org-id team)}
                                                                     :channel-error false})})))
                                                    :on-intermediate-change (fn [_]
                                                                             (reset! (::publish-data s)
                                                                              (merge publish-data
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
                                         (reset! (::publish-data s)
                                          (merge publish-data
                                           {:slack-data (merge slack-data
                                            {:note (.. e -target -value)})})))}]]]])]]
              [:div.publish-footer.group
                [:div.buttons
                  [:button.mlb-reset.mlb-black-link
                    {:on-click #(close-clicked s)}
                    "Cancel"]
                  [:button.mlb-reset.mlb-default
                    {:on-click #(let [slack-share (when (:slack publish-data)
                                                    {:medium :slack
                                                     :note (:note slack-data)
                                                     :channel (:channel slack-data)})
                                      email-share (when (:email publish-data)
                                                    {:medium :email
                                                     :note (:note email-data)
                                                     :subject (:subject email-data)
                                                     :to (:to email-data)})
                                      share-data (vec (remove nil? [(when slack-share slack-share) (when email-share email-share)]))]
                                  (if (or (and (:email publish-data) (empty? (:to email-data)))
                                          (and (:slack publish-data) (empty? (:channel slack-data))))
                                    (do
                                      (when (and (:email publish-data) (empty? (:to email-data)))
                                        (reset! (::publish-data s) (merge publish-data {:email-data (merge email-data {:to-error true})})))
                                      (when (and (:slack publish-data) (empty? (:channel slack-data)))
                                        (reset! (::publish-data s) (merge publish-data {:slack-data (merge slack-data {:channel-error true})}))))
                                    (when (or (not published?)
                                              (not (empty? share-data)))
                                      (dis/dispatch! [(if published? :story-reshare :story-share) share-data]))))
                     :disabled (and published? (not (:slack publish-data)) (not (:email publish-data)))}
                    (if published? "Share" "Post")]]]])]]]))