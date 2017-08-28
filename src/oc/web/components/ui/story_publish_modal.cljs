(ns oc.web.components.ui.story-publish-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (let [close-cb (nth (:rum/args s) 0)]
    (utils/after 180 close-cb)))

(rum/defcs story-publish-modal < rum/reactive
                                 (drv/drv :story-editing)
                                 (drv/drv :story-editing-publish)
                                 (rum/local nil ::publish-data)
                                 (rum/local false ::dismiss)
                                 (rum/local false ::first-render-done)
                                 {:will-mount (fn [s]
                                                (dis/dispatch! [:teams-get])
                                                (let [story-data @(drv/get-ref s :story-editing)]
                                                  (reset! (::publish-data s) {:email false :slack false :email-data {:subject (:title story-data)} :slack-data {}}))
                                                s)
                                  :did-mount (fn [s]
                                               ;; Add no-scroll to the body
                                               (dommy/add-class! (sel1 [:body]) :no-scroll)
                                               (let [published-data @(drv/get-ref s :story-editing)]
                                                   (when (:secure-uuid published-data)
                                                      (utils/after 500 #(.select (sel1 :input#story-publish-modal-published-url)))))
                                               s)
                                  :after-render (fn [s]
                                                  (when (not @(::first-render-done s))
                                                    (reset! (::first-render-done s) true))
                                                  s)
                                  :did-remount (fn [o s]
                                                 (let [published-data @(drv/get-ref s :story-editing-publish)]
                                                   (when (:secure-uuid published-data)
                                                      (utils/after 500 #(.select (sel1 :input#story-publish-modal-published-url)))))
                                                 s)
                                  :will-unmount (fn [s]
                                                  ;; Remove no-scroll class from the body tag
                                                  (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                                  s)}
  [s close-cb]
  (let [story-data (drv/react s :story-editing)
        publish-data @(::publish-data s)
        slack-data (:slack-data publish-data)
        email-data (:email-data publish-data)
        published-data (drv/react s :story-editing-publish)
        secure-uuid (or (:secure-uuid published-data) (:secure-uuid story-data))]
    [:div.story-publish-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      [:div.story-publish-modal
        (if (not (empty? secure-uuid))
          [:div.story-publish-modal-published
            [:img {:src (utils/cdn "/img/ML/caught_up.svg") :width 42 :height 42}]
            [:div.published-headline "Your update has been posted and shared!"]
            (let [publish-url (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server "/" (router/current-org-slug) "/story/" secure-uuid)]
              [:div.published-url-container.group
                [:input
                  {:value publish-url
                   :readOnly true
                   :id "story-publish-modal-published-url"}]
                [:button.mlb-reset.mlb-default.copy-btn
                  {:on-click (fn [_]
                              (.select (sel1 :input#story-publish-modal-published-url))
                              (utils/copy-to-clipboard))}
                  "Copy"]])
            [:div.published-subheadline "You can also provide anyone with this link to your update."]
            [:button.mlb-reset.mlb-default.done-btn
              {:on-click #(router/nav! (oc-urls/board (router/current-org-slug) (:board-slug story-data)))}
              "Done"]]
          [:div.story-publish-share
            [:div.title "Post " [:span (:title story-data)]]
            [:div.access (str "Updates posted in " (:storyboard-name story-data) " "
              (cond
                (= (:access story-data) "private") "are private and can be viewed by people you invite."
                (= (:access story-data) "public") "are public and can be viewed by anyone that has the link."
                :else "can be viewed by anyone on the team."))]
            [:div.mediums-box
              [:div.medium
                [:div.medium-row.group
                  [:span.labels "Share via Email"]
                  (carrot-checkbox {:selected (:email publish-data)
                                    :did-change-cb #(reset! (::publish-data s) (merge publish-data {:email (not (:email publish-data))}))})]
                (when (:email publish-data)
                  [:div.email-medium
                    [:div.medium-row.group
                      [:span.labels "To"]
                      [:div.fields
                        [:input
                          {:type "text"
                           :value ""}]]]
                    [:div.medium-row.subject.group
                      [:span.labels "Subject"]
                      [:div.fields
                        [:input
                          {:type "text"
                           :value (:subject email-data)
                           :on-change #(reset! (::publish-data s)
                                        (merge publish-data
                                         {:email-data (merge email-data
                                           {:subject (.. % -target -value)})}))}]]]
                    [:div.medium-row.note.group
                      [:span.labels "Add a note (optional)"]
                      [:div.fields
                        [:textarea
                          {:on-change #(reset! (::publish-data s)
                                        (merge publish-data
                                         {:email-data (merge email-data
                                                       {:note (.. % -target -innerText)})}))}]]]])]
              [:div.medium
                [:div.medium-row.group
                  [:span.labels "Share to Slack"]
                  (carrot-checkbox {:selected (:slack publish-data)
                                    :did-change-cb #(reset! (::publish-data s) (merge publish-data {:slack (not (:slack publish-data))}))})]
                (when (:slack publish-data)
                  [:div.slack-medium
                    [:div.medium-row.group
                      [:span.labels "To"]
                      [:div.fields
                        (slack-channels-dropdown {:did-change-cb #(merge publish-data {:slack-data (merge slack-data {:slack-channel %2 :slack-team %1})}) :initial-value "" :disabled false})]]
                    [:div.medium-row.note.group
                      [:span.labels "Add a note (optional)"]
                      [:div.fields
                        [:textarea
                          {:on-change #(reset! (::publish-data s)
                                        (merge publish-data
                                         {:slack-data (merge slack-data
                                                       {:note (.. % -target -innerText)})}))}]]]])]]
            [:div.publish-footer.group
              [:div.buttons
                [:button.mlb-reset.mlb-black-link
                  {:on-click #(close-clicked s)}
                  "Cancel"]
                [:button.mlb-reset.mlb-default
                  {:on-click #(dis/dispatch! [:story-share])}
                  "Post"]]]])]]))