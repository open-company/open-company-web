(ns oc.web.components.user-profile
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [goog.object :as googobj]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.user :as user-utils]
            [oc.web.stores.user :as user-stores]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]
            [oc.web.components.user-profile-personal-tab :refer (user-profile-personal-tab)]
            [oc.web.components.user-profile-notifications-tab :refer (user-profile-notifications-tab)]))

(defn show-modal [& [panel]]
  (dis/dispatch! [:input [:user-settings] (or panel :profile)]))

(defn dismiss-modal []
  (dis/dispatch! [:input [:user-settings] nil]))

(defn real-close-cb [editing-user-data]
  (when (:has-changes editing-user-data)
    (user-actions/user-profile-reset))
  (dismiss-modal))

(def default-user-profile (oc.web.stores.user/random-user-image))

(defn- update-tooltip [s]
  (utils/after 100
   #(let [header-avatar (rum/ref-node s "user-profile-header-avatar")
          $header-avatar (js/$ header-avatar)
          edit-user-profile-avatar @(drv/get-ref s :edit-user-profile-avatar)
          title (if (empty? edit-user-profile-avatar)
                  "Add a photo"
                  "Change photo")
          profile-tab? (.hasClass $header-avatar "profile-tab")]
      (if profile-tab?
        (.tooltip $header-avatar #js {:title title
                                      :trigger "hover focus"
                                      :position "top"
                                      :container "body"})
        (.tooltip $header-avatar "destroy")))))

(defn- img-on-load [url img]
  (dis/dispatch! [:input [:edit-user-profile-avatar] url])
  (gdom/removeNode img)
  (user-actions/user-avatar-save url))

(defn close-cb [current-user-data]
  (dis/dispatch! [:input [:latest-entry-point] 0])
  (if (:has-changes current-user-data)
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "user-profile-unsaved-edits"
                      :message "Leave without saving your changes?"
                      :link-button-title "Stay"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Lose changes"
                      :solid-button-cb #(do
                                          (alert-modal/hide-alert)
                                          (real-close-cb current-user-data))}]
      (alert-modal/show-alert alert-data))
    (real-close-cb current-user-data)))

(defn error-cb [res error]
  (notification-actions/show-notification
    {:title "Image upload error"
     :description "An error occurred while processing your image. Please retry."
     :expire 5
     :dismiss true}))

(defn success-cb
  [res]
  (let [url    (googobj/get res "url")
        node   (gdom/createDom "img")]
    (if-not url
      (notification-actions/show-notification
        {:title "Image upload error"
         :description "An error occurred while processing the image URL. Please try again."
         :expire 5})
      (do
        (set! (.-onerror node) #(error-cb nil nil))
        (set! (.-onload node) #(img-on-load url node))
        (set! (.-className node) "hidden")
        (gdom/append (.-body js/document) node)
        (set! (.-src node) url)))))

(defn progress-cb [res progress])

(defn upload-user-profile-pictuer-clicked []
  (iu/upload! user-utils/user-avatar-filestack-config success-cb progress-cb error-cb))

(rum/defcs user-profile < rum/reactive
                          ;; Derivatives
                          (drv/drv :org-data)
                          (drv/drv :alert-modal)
                          (drv/drv :edit-user-profile)
                          (drv/drv :user-settings)
                          (drv/drv :edit-user-profile-avatar)
                          ;; Mixins
                          no-scroll-mixin
                          {:will-mount (fn [s]
                            (user-actions/user-profile-reset)
                            s)
                           :did-remount (fn [_ s]
                            (update-tooltip s)
                            s)
                           :did-mount (fn [s]
                            (update-tooltip s)
                            s)
                           :did-update (fn [s]
                            (update-tooltip s)
                            s)}
  [s]
  (let [user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        tab (drv/react s :user-settings)
        org-data (drv/react s :org-data)
        edit-user-profile-avatar (drv/react s :edit-user-profile-avatar)
        user-for-avatar (merge current-user-data {:avatar-url edit-user-profile-avatar})]
    [:div.user-profile.fullscreen-page
      [:div.user-profile-inner
        [:button.mlb-reset.settings-modal-close
          {:on-click #(close-cb current-user-data)}]
        [:div.user-profile-header.group
          [:div.user-profile-header-avatar
            {:ref "user-profile-header-avatar"
             :class (utils/class-set {:profile-tab (= tab :profile)})
             :on-click #(upload-user-profile-pictuer-clicked)}
            (user-avatar-image user-for-avatar)]
          [:div.user-profile-header-name
            (clojure.string/trim
             (str (:first-name current-user-data) " " (:last-name current-user-data)))]
          [:div.user-profile-header-role
            (case (user-stores/user-role org-data current-user-data)
             :admin "Admin"
             :author "Contributor"
             :viewer "Viewer")]]
        [:div.user-profile-tabs-header.group
          [:div.user-profile-bottom-line]
          [:div.user-profile-tab-header
            {:class (when (= tab :profile) "active")
             :on-click #(user-actions/change-user-profile-panel :profile)}
            [:a.user-profile-tab-link
              "PERSONAL PROFILE"]]
          [:div.user-profile-tab-header
            {:class (when (= tab :notifications) "active")
             :on-click #(user-actions/change-user-profile-panel :notifications)}
            [:a.user-profile-tab-link
              "NOTIFICATIONS"]]]
        (if (= tab :notifications)
          (user-profile-notifications-tab org-data real-close-cb)
          (user-profile-personal-tab real-close-cb))]]))