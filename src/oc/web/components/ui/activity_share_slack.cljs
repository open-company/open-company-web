(ns oc.web.components.ui.activity-share-slack
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.mixins :as mixins]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

(defn dismiss []
  (dis/dispatch! [:activity-share-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss))

(rum/defcs activity-share-slack < rum/reactive
                                  ;; Derivatives
                                  (drv/drv :activity-share)
                                  (drv/drv :activity-shared-data)
                                  ;; Locals
                                  (rum/local {:note ""} ::slack-data)
                                  (rum/local false ::dismiss)
                                  mixins/no-scroll-mixin
                                  mixins/first-render-mixin
                                  {:will-mount (fn [s]
                                                 (dis/dispatch! [:teams-get])
                                                 s)}
  [s]
  (let [activity-data (:share-data (drv/react s :activity-share))
        slack-data @(::slack-data s)
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
              (when shared?
                [:button.mlb-reset.mlb-default.done-btn
                  {:on-click #(close-clicked s)}
                  "Done"])])
          (when (not shared?)
            [:div.activity-share-share
              [:div.mediums-box
                [:div.medium
                  [:div.slack-medium.group
                    [:div.medium-row.group
                      [:span.labels "To"]
                      [:div.fields
                        {:class (when (:channel-error slack-data) "error")}
                        (slack-channels-dropdown {:on-change (fn [team channel]
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
                      [:span.labels "Add a note"]
                      [:div.fields
                        [:textarea
                          {:value (:note slack-data)
                           :on-change (fn [e]
                                       (reset! (::slack-data s)
                                        (merge slack-data {:note (.. e -target -value)})))}]]]
                    [:div.medium-row.group
                      [:div.shared-subheadline "Anyone outside your Carrot team won't see comments."]]]]]
              [:div.share-footer.group
                [:div.buttons
                  [:button.mlb-reset.mlb-black-link
                    {:on-click #(close-clicked s)}
                    "Cancel"]
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