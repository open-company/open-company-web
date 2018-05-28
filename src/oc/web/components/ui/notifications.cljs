(ns oc.web.components.ui.notifications
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.notifications :as notification-actions]))

(defn description-wrapper [desc]
  (cond
   (string? desc)
   {:dangerouslySetInnerHTML #js {"__html" desc}}

   (sequential? desc)
   desc))

(defn setup-timeout [s]
  (when @(::timeout s)
    (reset! (::timeout s) nil)
    (js/clearTimeout @(::timeout s)))
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
  [s notification-data]
  [:div.notification
    {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                              :appear (and (not @(::dismiss s)) @(:first-render-done s))
                              :server-error (:server-error notification-data)
                              :app-update (:app-update notification-data)
                              :opac (:opac notification-data)
                              :dismiss-button (:dismiss-bt notification-data)})
     :data-notificationid (:id notification-data)}
    [:div.notification-title.group
      (when (:slack-icon notification-data)
        [:span.slack-icon])
      (:title notification-data)]
    (when (:dismiss notification-data)
      [:button.mlb-reset.notification-dismiss-bt
        {:on-click #(do
                      (reset! (::timeout s) nil)
                      (js/clearTimeout @(::timeout s))
                      (notification-actions/remove-notification notification-data))}])
    (when-not (empty? (:description notification-data))
      [:div.notification-description
        (description-wrapper (:description notification-data))])])

(rum/defcs notifications < rum/static
                           rum/reactive
                           (drv/drv :notifications-data)
  [s]
  (let [notifications-data (drv/react s :notifications-data)]
    [:div.notifications
      (for [idx (range (count notifications-data))
            :let [n (nth notifications-data idx)]]
        (rum/with-key (notification n) (str "notif-" (:created-at n))))]))