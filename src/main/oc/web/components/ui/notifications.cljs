(ns oc.web.components.ui.notifications
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.notifications :as notification-actions]))

(defn button-wrapper [s bt-ref bt-cb bt-title bt-style bt-dismiss]
  (let [has-html (string? bt-title)
        button-base-map {:on-click (fn [e]
                                     (when (fn? bt-cb)
                                       (bt-cb e))
                                     (when bt-dismiss
                                       (notification-actions/remove-notification (first (:rum/args s)))))
                         :ref bt-ref
                         :class (utils/class-set {:solid-green (= bt-style :solid-green)
                                                  :default-link (= bt-style :default-link)})}
        button-map (if has-html
                     (assoc button-base-map :dangerouslySetInnerHTML #js {"__html" bt-title})
                     button-base-map)]
    [:button.mlb-reset.notification-button.group
      button-map
      (when-not has-html
        bt-title)]))

(defn clear-timeout [s]
  (when @(::timeout s)
    (js/clearTimeout @(::timeout s))
    (reset! (::timeout s) nil)))

(defn setup-timeout [s]
  (clear-timeout s)
  (let [n-data (first (:rum/args s))]
    (reset! (::old-expire s) (:expire n-data))
    (when (pos? (:expire n-data))
      (let [expire (* (:expire n-data) 1000)]
        (reset! (::timeout s)
         (utils/after expire
          #(reset! (::dismiss s) true)))))))

(rum/defcs notification < rum/static
                          ui-mixins/first-render-mixin
                          (rum/local false ::dismiss)
                          (rum/local false ::notification-removed)
                          (rum/local false ::timeout)

                          (rum/local 0 ::old-expire)
                          {:did-mount (fn [s]
                           (setup-timeout s)
                           s)
                           :did-remount (fn [o s]
                            (setup-timeout s)
                            s)
                           :after-render (fn [s]
                            (when @(::dismiss s)
                              (when (compare-and-set! (::notification-removed s) false true)
                                ;; remove notification from list
                                (notification-actions/remove-notification (first (:rum/args s)))))
                            s)}
  [s {:keys [id title description slack-icon opac server-error dismiss
             primary-bt-cb primary-bt-title primary-bt-style primary-bt-dismiss
             primary-bt-inline secondary-bt-cb secondary-bt-title secondary-bt-style
             secondary-bt-dismiss web-app-update slack-bot mention mention-author
             click dismiss-x] :as notification-data}
      light-theme]
  [:div.notification.group
    {:class (utils/class-set {:server-error server-error
                              :app-update web-app-update
                              :slack-bot slack-bot
                              :opac opac
                              :light-theme light-theme
                              :mention-notification (and mention mention-author)
                              :bottom-notch (js/isiPhoneWithoutPhysicalHomeBt)
                              :dismiss dismiss
                              :clickable (fn? click)
                              :inline-bt (or primary-bt-inline
                                             (and id
                                                  ((keyword id) #{:slack-team-added :slack-bot-added
                                                                  :org-settings-saved :invitation-resent
                                                                  :cancel-invitation :member-removed-from-team
                                                                  :reminder-created :reminder-updated
                                                                  :reminder-deleted :resend-verification-ok})))
                              :dismiss-button dismiss})
     :on-mouse-enter #(clear-timeout s)
     :on-mouse-leave #(setup-timeout s)
     :on-click #(when (and (fn? click)
                           (not (utils/event-inside? % (rum/ref-node s :dismiss-bt)))
                           (not (utils/event-inside? % (rum/ref-node s :first-bt)))
                           (not (utils/event-inside? % (rum/ref-node s :second-bt))))
                  (click %)
                  (clear-timeout s)
                  (notification-actions/remove-notification notification-data))
     :data-notificationid id}
    (when dismiss
      [:button.mlb-reset.notification-dismiss-bt
        {:on-click #(do
                      (when (fn? dismiss)
                        (dismiss %))
                      (clear-timeout s)
                      (notification-actions/remove-notification notification-data))
         :class (when dismiss-x "dismiss-x")
         :ref :dismiss-bt}
        (when-not dismiss-x
          "OK")])
    [:div.notification-title.group
      {:class (when-not (seq description) "no-description")}
      (when slack-icon
        [:span.slack-icon])
      title]
    (when (seq description)
      [:div.notification-description
        {:dangerouslySetInnerHTML #js {"__html" description}
         :class (when mention "oc-mentions")}])
    (when (seq secondary-bt-title)
      (button-wrapper s :second-bt secondary-bt-cb secondary-bt-title secondary-bt-style secondary-bt-dismiss))
    (when (seq primary-bt-title)
      (button-wrapper s :first-bt primary-bt-cb primary-bt-title primary-bt-style primary-bt-dismiss))])

(rum/defcs notifications < rum/static
                           rum/reactive
                           (drv/drv :notifications-data)
                           (drv/drv :panel-stack)
                           (drv/drv :ui-theme)
  [s]
  (let [notifications-data (drv/react s :notifications-data)
        panel-stack (drv/react s :panel-stack)
        {:keys [computed-value]} (drv/react s :ui-theme)
        light-theme? (or (pos? (count panel-stack))
                         (= computed-value :dark))]
    [:div.notifications
      (for [idx (range (count notifications-data))
            :let [n (nth notifications-data idx)]]
        (rum/with-key (notification n light-theme?) (str "notif-" (:id n))))]))