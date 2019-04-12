(ns oc.web.components.org-settings-modal
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.actions.org :as org-actions]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.qsg-breadcrumb :refer (qsg-breadcrumb)]))

(defn show-modal [& [panel]]
  (dis/dispatch! [:input [:org-settings] (or panel :main)]))

(defn real-close []
  (dis/dispatch! [:input [:org-settings] nil]))

(defn dismiss-modal [& [s]]
  (if s
    (reset! (::unmounting s) true)
    (real-close)))

(defn logo-on-load [org-avatar-editing url img]
  (org-actions/org-avatar-edit-save {:logo-url url
                                     :logo-width (.-width img)
                                     :logo-height (.-height img)})
  (gdom/removeNode img))

(defn logo-add-error
  "Show an error alert view for failed uploads."
  [img]
  (notification-actions/show-notification
   {:title "Image upload error"
    :description "An error occurred while processing your company avatar. Please retry."
    :expire 5
    :id :org-avatar-upload-failed
    :dismiss true})
  (when img
    (gdom/removeNode img)))

(defn- update-tooltip [s]
  (utils/after 100
   #(let [header-logo (rum/ref-node s "org-settings-header-logo")
          $header-logo (js/$ header-logo)
          org-avatar-editing @(drv/get-ref s :org-avatar-editing)
          title (if (empty? (:logo-url org-avatar-editing))
                  "Add a logo"
                  "Change logo")]
      (.tooltip $header-logo #js {:title title
                                  :trigger "hover focus"
                                  :position "top"
                                  :container "body"}))))

(defn logo-on-click [org-avatar-editing]
  (iu/upload! org-utils/org-avatar-filestack-config
    (fn [res]
      (qsg-actions/finish-company-logo-trail)
      (let [url (gobj/get res "url")
            img (gdom/createDom "img")]
        (set! (.-onerror img) #(logo-add-error img))
        (set! (.-onload img) #(logo-on-load org-avatar-editing url img))
        (set! (.-className img) "hidden")
        (gdom/append (.-body js/document) img)
        (set! (.-src img) url)))
    nil
    (fn [err]
      (qsg-actions/finish-company-logo-trail)
      (logo-add-error nil))))

(rum/defcs org-settings-modal < ;; Mixins
                                rum/reactive
                                (drv/drv :qsg)
                                (drv/drv :org-data)
                                (drv/drv :team-data)
                                (drv/drv :org-editing)
                                (drv/drv :org-avatar-editing)
                                (drv/drv :org-settings-team-management)
                                ;; Locals
                                (rum/local false ::unmounting)
                                (rum/local false ::unmounted)
                                (rum/local "" ::email-domain)
                                ;; Mixins
                                mixins/no-scroll-mixin
                                mixins/first-render-mixin
                                {:did-update (fn [s]
                                  (when (and @(::unmounting s)
                                             (compare-and-set! (::unmounted s) false true))
                                    (utils/after 180 real-close))
                                  s)}
  [s]
  (let [org-data (drv/react s :org-data)
        appear-class (and @(:first-render-done s)
                          (not @(::unmounting s))
                          (not @(::unmounted s)))
        org-avatar-editing (drv/react s :org-avatar-editing)
        qsg-data (drv/react s :qsg)
        org-data-for-avatar (merge org-data org-avatar-editing)
        org-editing (drv/react s :org-editing)
        {:keys [query-params
                um-domain-invite
                add-email-domain-team-error
                team-data]
         :as team-management-data}
                    (drv/react s :org-settings-team-management)]
    [:div.org-settings-modal
      {:class (utils/class-set {:appear appear-class})}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(dismiss-modal s)}]
      [:div.org-settings
        [:div.org-settings-header
          [:div.org-settings-header-title
            "Admin settings"]
          [:button.mlb-reset.save-bt
            "Save"]
          [:button.mlb-reset.cancel-bt
            {:on-click #(dismiss-modal s)}
            "Back"]]
        [:div.org-settings-body
          [:div.org-settings-header-avatar.qsg-company-logo-3.group
            {:ref "org-settings-header-logo"
             :class (utils/class-set {:missing-logo (empty? (:logo-url org-avatar-editing))
                                      utils/hide-class true})
             :on-click logo-on-click}
            (when (= (:step qsg-data) :company-logo-3)
              (qsg-breadcrumb qsg-data))
            (org-avatar org-data-for-avatar false :never)
            [:span.edit-company-logo
              "Edit company logo"]]
          [:div.org-settings-fields
            [:div.org-settings-label
              "Company name"]
            [:input.org-settings-field
              {:type "text"
              :value (:name org-editing)
              :on-change #(dis/dispatch! [:input [:org-editing] (merge org-editing {:name (.. % -target -value)
                                                                                    :has-changes true})])}]
            [:div.org-settings-desc
              (str ls/web-server-domain "/" (:slug org-data))]
            [:div.org-settings-label
              "Allowed email domains"]
            [:input.org-settings-field
              {:type "text"
              :value @(::email-domain s)
              :on-change #(reset! (::email-domain s) (.. % -target -value))}]
            [:div.org-settings-email-domains
              (for [domain (:email-domains team-data)]
                [:div.org-settings-email-domain-row
                  {:key (str "org-settings-domain-" domain)}
                  (str "@" domain)
                  [:button.mlb-reset.remove-email-bt
                    "Remove"]])]]
          [:div.org-settings-advanced
            [:button.mlb-reset.advanced-settings-bt
              "Show advanced settings"]]]]]))