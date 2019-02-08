(ns oc.web.components.ui.qsg-breadcrumb
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.mixins.ui :refer (first-render-mixin)]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]))

(rum/defcs qsg-breadcrumb < rum/reactive
                            (drv/drv :qsg)
                            first-render-mixin
                            (on-window-click-mixin (fn [s e]
                              (let [qsg-data @(drv/get-ref s :qsg)
                                    step (:step qsg-data)]
                                (cond
                                  ;; Profile photo
                                  (and (= step :profile-photo-1)
                                       (not (utils/event-inside? e (sel1 :.qsg-profile-photo-1))))
                                  (qsg-actions/reset-qsg)
                                  (and (= step :profile-photo-2)
                                       (not (utils/event-inside? e (sel1 :.qsg-profile-photo-2))))
                                  (qsg-actions/reset-qsg)
                                  (= step :profile-photo-3)
                                  (qsg-actions/reset-qsg)
                                  ;; Company logo
                                  (and (= step :company-logo-1)
                                       (not (utils/event-inside? e (sel1 :.qsg-company-logo-1))))
                                  (qsg-actions/reset-qsg)
                                  (and (= step :company-logo-2)
                                       (not (utils/event-inside? e (sel1 :.qsg-company-logo-2))))
                                  (qsg-actions/reset-qsg)
                                  (= step :company-logo-3)
                                  (qsg-actions/reset-qsg)
                                  ;; Invite team
                                  (= step :invite-team-1)
                                  (qsg-actions/reset-qsg)
                                  ;; Create post
                                  (= step :create-post-1)
                                  (qsg-actions/reset-qsg)
                                  ;; Create reminder
                                  (= step :create-reminder-1)
                                  (qsg-actions/reset-qsg)
                                  ;; Section settings
                                  (and (= step :configure-section-1)
                                       (not (utils/event-inside? e (sel1 :.qsg-configure-section-1))))
                                  (qsg-actions/reset-qsg)
                                  (= step :configure-section-2)
                                  (qsg-actions/reset-qsg)
                                  (= step :add-section-1)
                                  (qsg-actions/reset-qsg)))))
  [s qsg-data]
  [:div.qsg-breadcrumb
    {:class (utils/class-set {(:step qsg-data) true
                              :appear @(:first-render-done s)})}])