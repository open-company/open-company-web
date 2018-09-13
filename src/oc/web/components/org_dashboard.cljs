(ns oc.web.components.org-dashboard
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [taoensso.timbre :as timbre]
            [oc.web.lib.raven :as raven]
            [org.martinklepsch.derivatives :as drv]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.stores.search :as search]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.cmail :refer (cmail)]
            [oc.web.components.entry-edit :refer (entry-edit)]
            [oc.web.components.org-settings :refer (org-settings)]
            [oc.web.components.user-profile :refer (user-profile)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.search :refer (search-results-view)]
            [oc.web.components.fullscreen-post :refer (fullscreen-post)]
            [oc.web.components.ui.section-editor :refer (section-editor)]
            [oc.web.components.ui.activity-share :refer (activity-share)]
            [oc.web.components.dashboard-layout :refer (dashboard-layout)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.activity-removed :refer (activity-removed)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.ui.activity-not-found :refer (activity-not-found)]
            [oc.web.components.ui.made-with-carrot-modal :refer (made-with-carrot-modal)]))

(defn refresh-board-data [s]
  (when-not (router/current-activity-id)
    (utils/after 100 (fn []
     (let [{:keys [org-data
                   board-data
                   ap-initial-at]} @(drv/get-ref s :org-dashboard-data)]
       (cond

        (= (router/current-board-slug) "all-posts")
        (activity-actions/all-posts-get org-data ap-initial-at)

        (= (router/current-board-slug) "must-see")
        (activity-actions/must-see-get org-data)

        :default
        (let [fixed-board-data (or
                                board-data
                                (some #(when (= (:slug %) (router/current-board-slug)) %) (:boards org-data)))]
          (section-actions/section-get (utils/link-for (:links fixed-board-data) ["item" "self"] "GET")))))))))

(rum/defcs org-dashboard < ;; Mixins
                           rum/static
                           rum/reactive
                           (ui-mixins/render-on-resize nil)
                           ;; Derivatives
                           (drv/drv :org-dashboard-data)
                           (drv/drv search/search-key)
                           (drv/drv search/search-active?)

                           {:did-mount (fn [s]
                             (utils/after 100 #(set! (.-scrollTop (.-body js/document)) 0))
                             (refresh-board-data s)
                             s)
                            :did-catch (fn [s e i]
                              ;; Give 2.5s to sentry to send the error
                              (utils/after 2500 #(router/redirect-500!))
                              (assoc s ::keep-loading true))}
  [s]
  (let [{:keys [orgs
                org-data
                jwt
                board-data
                container-data
                posts-data
                ap-initial-at
                user-settings
                org-settings-data
                made-with-carrot-modal-data
                is-entry-editing
                is-sharing-activity
                entry-edit-dissmissing
                is-showing-alert
                media-input
                show-section-editor
                show-section-add
                show-sections-picker
                entry-editing-board-slug
                mobile-navigation-sidebar
                activity-share-container
                mobile-menu-open
                show-cmail]} (drv/react s :org-dashboard-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        search-active? (drv/react s search/search-active?)
        search-results? (pos?
                         (count
                          (:results (drv/react s search/search-key))))
        loading? (or ;; There was an error
                     (::keep-loading s)
                     ;; the org data are not loaded yet
                     (not org-data)
                     ;; No board specified
                     (and (not (router/current-board-slug))
                          ;; but there are some
                          (pos? (count (:boards org-data))))
                     ;; Board specified
                     (and (not= (router/current-board-slug) "all-posts")
                          (not= (router/current-board-slug) "must-see")
                          (not ap-initial-at)
                          ;; But no board data yet
                          (not board-data))
                     ;; Another container
                     (and (or (= (router/current-board-slug) "all-posts")
                              (= (router/current-board-slug) "must-see")
                              ap-initial-at)
                          ;; But no all-posts data yet
                         (not container-data)))
        org-not-found (and orgs
                           (not ((set (map :slug orgs)) (router/current-org-slug))))
        section-not-found (and (not org-not-found)
                               org-data

                               (not ((set (map :slug (:boards org-data))) (router/current-board-slug))))
        entry-not-found (and (not section-not-found)
                             (or board-data
                                 container-data)
                             posts-data
                             (not ((set (keys posts-data)) (router/current-activity-id))))
        show-activity-not-found (and (not jwt)
                                     (router/current-activity-id)
                                     (or org-not-found
                                         section-not-found
                                         entry-not-found))
        show-activity-removed (and jwt
                                   (router/current-activity-id)
                                   (or org-not-found
                                       section-not-found
                                       entry-not-found))
        is-loading (and (not show-activity-not-found)
                        (not show-activity-removed)
                        loading?)]
    ;; Show loading if
    (if is-loading
      [:div.org-dashboard
        (loading {:loading true})]
      [:div
        {:class (utils/class-set {:org-dashboard true
                                  :modal-activity-view (router/current-activity-id)
                                  :mobile-or-tablet is-mobile?
                                  :activity-not-found show-activity-not-found
                                  :activity-removed show-activity-removed
                                  :no-scroll (and (not is-mobile?)
                                                  (router/current-activity-id))})}
        ;; Use cond for the next components to exclud each other and avoid rendering all of them
        (login-overlays-handler)
        (cond
          ;; Activity removed
          show-activity-removed
          (activity-removed)
          ;; Activity not found
          show-activity-not-found
          (activity-not-found)
          ;; Org settings
          org-settings-data
          (org-settings)
          ;; User settings
          user-settings
          (user-profile)
          ;; Made with carrot modal
          made-with-carrot-modal-data
          (made-with-carrot-modal)
          ;; Mobile create a new section
          (and is-mobile?
               show-section-editor)
          (section-editor board-data
           (fn [sec-data note]
            (when sec-data
              (section-actions/section-save sec-data note #(dis/dispatch! [:input [:show-section-editor] false])))))
          ;; Mobile edit current section data
          (and is-mobile?
               show-section-add)
          (section-editor nil
           (fn [sec-data note]
            (when sec-data
              (section-actions/section-save sec-data note #(dis/dispatch! [:input [:show-section-add] false])))))
          ;; Mobile sections picker
          (and is-mobile?
               show-sections-picker)
          (sections-picker entry-editing-board-slug
            (fn [board-data note]
             (dis/dispatch! [:input [:show-sections-picker] false])
             (when board-data
              (dis/dispatch! [:update [:entry-editing]
               #(merge % {:board-slug (:slug board-data)
                          :board-name (:name board-data)
                          :invite-note note})]))))
          ;; Entry editing
          is-entry-editing
          (entry-edit)
          ;; Activity share for mobile
          (and is-mobile?
               is-sharing-activity)
          (activity-share)
          ;; Search results
          (and is-mobile? search-active? (not (router/current-activity-id)))
          (search-results-view)
          ;; Show mobile navigation
          (and is-mobile?
               mobile-navigation-sidebar)
          (navigation-sidebar)
          ;; Activity modal
          (and (router/current-activity-id)
               (not entry-edit-dissmissing))
          (fullscreen-post))
        ;; Activity share modal for no mobile
        (when (and (not is-mobile?)
                   is-sharing-activity)
          ;; If we have an element id find the share container inside that element
          ;; and portal the component to that element
          (let [portal-element (when activity-share-container
                                  (.get (.find (js/$ (str "#" activity-share-container))
                                         ".activity-share-container") 0))]
            (if portal-element
              (rum/portal (activity-share) portal-element)
              (activity-share))))
        ;; cmail editor
        (when show-cmail
          (cmail))
        ;; Media video modal for entry editing
        (when (and media-input
                   (:media-video media-input))
          (media-video-modal))
        ;; Alert modal
        (when is-showing-alert
          (alert-modal))
        (when-not (and is-mobile?
                       (or (router/current-activity-id)
                           is-entry-editing
                           is-sharing-activity
                           show-section-add
                           show-section-editor))
          [:div.page
            (navbar)
            [:div.org-dashboard-container
              [:div.org-dashboard-inner
               (when-not (and is-mobile?
                              (or (and search-active? search-results?)
                                  mobile-navigation-sidebar
                                  org-settings-data
                                  user-settings
                                  mobile-menu-open))
                 (dashboard-layout))]]])])))