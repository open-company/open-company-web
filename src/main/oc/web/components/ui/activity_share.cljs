(ns oc.web.components.ui.activity-share
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.foc-menu :as foc-menu-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.carrot-option-button :refer (carrot-option-button)]))

(defn close-clicked [s]
  (foc-menu-actions/toggle-foc-share-entry))

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
                            (drv/drv :current-user-data)
                            ;; Locals
                            (rum/local false ::sharing)
                            (rum/local false ::shared)
                            (rum/local :team ::url-audience)
                            ;; Mixins
                            (mixins/on-click-out #(close-clicked %1))
                            {:will-mount (fn [s]
                              (team-actions/teams-get-if-needed)
                             s)
                             :did-update (fn [s]
                              ;; When we have a sharing response
                              (when-let [shared-data @(drv/get-ref s :activity-shared-data)]
                                (when (compare-and-set! (::sharing s) true false)
                                  (reset! (::shared s) (if (:error shared-data) :error :shared))
                                  (utils/after 2000 #(reset! (::shared s) false)))
                                (foc-menu-actions/toggle-foc-share-entry))
                              s)}
  [s]
  (let [activity-data (:share-data (drv/react s :activity-share))
        org-data (drv/react s :org-data)
        secure-uuid (:secure-uuid activity-data)
        ;; Make sure it gets remounted when share request finishes
        _ (drv/react s :activity-shared-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        disallow-public-share? (get-in org-data [:content-visibility :disallow-public-share])]
    [:div.activity-share-modal-container
      [:div.activity-share-modal
        [:div.activity-share-main-cta
          (when is-mobile?
            [:button.mobile-modal-close-bt.mlb-reset
              {:on-click #(close-clicked s)}])
          "Share post"]
        [:div.activity-share-modal-shared.group
          [:form
            {:on-submit #(utils/event-stop %)}
            (when-not disallow-public-share?
              [:div.medium-row.group
                [:div.fields
                  [:button.mlb-reset.checkbox-row
                    {:on-click (fn [_] (swap! (::url-audience s) #(if (= % :team) :all :team)))}
                    (carrot-option-button {:selected (= @(::url-audience s) :team)})
                    [:div.checkbox-label "Require authentication"]]
                  [:button.mlb-reset.checkbox-row
                    {:on-click (fn [_] (swap! (::url-audience s) #(if (= % :all) :team :all)))}
                    (carrot-option-button {:selected (= @(::url-audience s) :all)})
                    [:div.checkbox-label "Public (anyone with this link)"]]]])
            [:div.medium-row.group
              (let [url-protocol (str "http" (when ls/jwt-cookie-secure "s") "://")
                    secure-url (oc-urls/secure-activity (:slug org-data) secure-uuid)
                    post-url (oc-urls/entry (:slug org-data) (:board-slug activity-data) (:uuid activity-data))
                    share-url (str url-protocol ls/web-server
                                (if (= @(::url-audience s) :team)
                                  post-url
                                  secure-url))]
                [:div.shared-url-container.group
                  [:input.oc-input
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
                              (let [copied? (utils/copy-to-clipboard url-input)]
                                (notification-actions/show-notification {:title (if copied? "Share URL copied to clipboard" "Error copying the share URL")
                                                                          :description (when-not copied? "Please try copying the URL manually")
                                                                          :primary-bt-title "OK"
                                                                          :primary-bt-dismiss true
                                                                          :primary-bt-inline copied?
                                                                          :expire 3
                                                                          :id (if copied? :share-url-copied :share-url-copy-error)}))))}
                "Copy URL"]]]]]]))