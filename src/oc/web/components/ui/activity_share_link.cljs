(ns oc.web.components.ui.activity-share-link
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

(rum/defcs activity-share-link < rum/reactive
                                 ;; Derivatives
                                 (drv/drv :activity-share)
                                 (drv/drv :activity-shared-data)
                                 ;; Locals
                                 (rum/local nil ::share-data)
                                 (rum/local false ::dismiss)
                                 mixins/no-scroll-mixin
                                 mixins/first-render-mixin
                                 {:did-mount (fn [s]
                                   (utils/after
                                    500
                                    #(when-let [activity-shared-url (sel1 :input#activity-share-modal-shared-url)]
                                      (.select activity-shared-url)))
                                   s)
                                  :did-remount (fn [o s]
                                   (utils/after
                                    500
                                    #(when-let [activity-shared-url (sel1 :input#activity-share-modal-shared-url)]
                                      (.select activity-shared-url)))
                                   s)}
  [s]
  (let [activity-data (:share-data (drv/react s :activity-share))
        secure-uuid (:secure-uuid activity-data)]
    [:div.activity-share-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
            {:on-click #(close-clicked s)}]
        [:div.activity-share-modal
          [:div.title "Share " [:span {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]]
          [:div.activity-share-modal-shared
            (let [share-url (str
                             "http"
                             (when ls/jwt-cookie-secure
                              "s")
                             "://"
                             ls/web-server
                             (oc-urls/secure-activity
                              (router/current-org-slug) secure-uuid))]
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
            [:button.mlb-reset.mlb-default.done-btn
              {:on-click #(close-clicked s)}
              "Done"]]]]]))