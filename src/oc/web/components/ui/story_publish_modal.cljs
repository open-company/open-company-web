(ns oc.web.components.ui.story-publish-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

(rum/defcs story-publish-modal < rum/reactive
                                 (drv/drv :story-editing)
                                 (rum/local nil ::publish-data)
                                 (rum/local false ::first-render-done)
                                 ()
                                 {:will-mount (fn [s]
                                                (dis/dispatch! [:teams-get])
                                                (let [story-data @(drv/get-ref s :story-editing)]
                                                  (reset! (::publish-data s) {:email false :slack false :email-data {:subject (:title story-data)} :slack-data {}}))
                                                s)
                                  :did-mount (fn [s]
                                               ;; Add no-scroll to the body
                                               (dommy/add-class! (sel1 [:body]) :no-scroll)
                                               s)
                                  :after-render (fn [s]
                                                  (when (not @(::first-render-done s))
                                                    (reset! (::first-render-done s) true))
                                                  s)
                                  :will-unmount (fn [s]
                                                  ;; Remove no-scroll class from the body tag
                                                  (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                                  s)}
  [s close-cb]
  (let [story-data (drv/react s :story-editing)
        publish-data @(::publish-data s)
        slack-data (:slack-data publish-data)
        email-data (:email-data publish-data)]
    [:div.story-publish-modal-container
      {:class (utils/class-set {:will-appear (not @(::first-render-done s))
                                :appear @(::first-render-done s)})}
      [:div.story-publish-modal
        [:div.title "Post " [:span (:title story-data)]]
        [:div.access (str "Stories posted in " (:storyboard-name story-data) " "
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
              {:on-click #(close-cb)}
              "Cancel"]
            [:button.mlb-reset.mlb-default
              {:on-click #()}
              "Post story"]]]]]))