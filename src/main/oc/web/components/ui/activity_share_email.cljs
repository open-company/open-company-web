(ns oc.web.components.ui.activity-share-email
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.foc-menu :as foc-menu-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.carrot-option-button :refer (carrot-option-button)]))

(defn- highlight-url
  "Select the whole content of the share link filed."
  [s]
  (when-let [url-field (rum/ref-node s "activity-share-url-field")]
    (.select url-field)))

(defn- copy-link-clicked [s e]
  (dom-utils/event-stop! e)
  (let [url-input (rum/ref-node s "activity-share-url-field")]
    (highlight-url s)
    (let [copied? (ui-utils/copy-to-clipboard url-input)]
      (notification-actions/show-notification
       {:title (if copied? "Share URL copied to clipboard" "Error copying the share URL")
        :description (when-not copied? "Please try copying the URL manually")
        :primary-bt-title "OK"
        :primary-bt-dismiss true
        :primary-bt-inline copied?
        :expire 3
        :id (if copied? :share-url-copied :share-url-copy-error)}))))

(defn close-clicked [s]
  (foc-menu-actions/toggle-foc-share-entry))

(rum/defcs activity-share-email <
  rum/reactive
  ;; Derivatives
  (drv/drv :activity-shared-data)
  (drv/drv :org-data)
  ;; Locals
  (rum/local :team ::url-audience)
  ;; Mixins
  ;; mixins/first-render-mixin
  {:did-update (fn [s]
                ;; When we have a sharing response
                 (when @(drv/get-ref s :activity-shared-data)
                   ;; Dismiss the share view
                   (foc-menu-actions/toggle-foc-share-entry))
                 s)}

  [s {activity-data :activity-data}]
  (let [_activity-shared-data (drv/react s :activity-shared-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        secure-uuid (:secure-uuid activity-data)
        org-data (drv/react s :org-data)]
    [:div.activity-share-modal-container
     [:div.activity-share-modal
      [:div.activity-share-main-cta
       (when is-mobile?
         [:button.mobile-modal-close-bt.mlb-reset
          {:on-click #(close-clicked s)}])
       "Share post"]
      [:div.activity-share-modal-shared.group
        [:form
        {:on-submit #(dom-utils/event-stop! %)}
        (when-not (-> org-data :content-visibility :disallow-public-share)
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
          (let [secure-url (oc-urls/secure-activity (:slug org-data) secure-uuid)
                post-url (oc-urls/entry (:slug org-data) (:board-slug activity-data) (:uuid activity-data))
                share-url (str ls/web-server-domain
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
            :on-click (partial copy-link-clicked s)}
          "Copy URL"]]]]]]))