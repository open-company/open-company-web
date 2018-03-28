(ns oc.web.components.org-dashboard
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.stores.search :as search]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.entry-edit :refer (entry-edit)]
            [oc.web.components.org-settings :refer (org-settings)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.search :refer (search-results-view)]
            [oc.web.components.fullscreen-post :refer (fullscreen-post)]
            [oc.web.components.ui.section-editor :refer (section-editor)]
            [oc.web.components.ui.activity-share :refer (activity-share)]
            [oc.web.components.dashboard-layout :refer (dashboard-layout)]
            [oc.web.components.ui.onboard-overlay :refer (onboard-overlay)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.whats-new-modal :refer (whats-new-modal)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.components.ui.media-chart-modal :refer (media-chart-modal)]
            [oc.web.components.ui.made-with-carrot-modal :refer (made-with-carrot-modal)]))

(defn refresh-board-data [s]
  (when-not (router/current-activity-id)
    (utils/after 100 (fn []
     (let [{:keys [org-data
                   board-data
                   ap-initial-at]} @(drv/get-ref s :org-dashboard-data)]
       (if (= (router/current-board-slug) "all-posts")
         (activity-actions/all-posts-get org-data ap-initial-at)
         (let [fixed-board-data (or
                                 board-data
                                 (some #(when (= (:slug %) (router/current-board-slug)) %) (:boards org-data)))]
           (dis/dispatch! [:board-get (utils/link-for (:links fixed-board-data) ["item" "self"] "GET")]))))))))

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
                             s)}
  [s]
  (let [{:keys [org-data
                board-data
                all-posts-data
                nux
                nux-loading
                nux-end
                ap-initial-at
                org-settings-data
                whats-new-modal-data
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
                mobile-navigation-sidebar]} (drv/react s :org-dashboard-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        should-show-onboard-overlay? (some #{nux} [:1 :2])
        search-active? (drv/react s search/search-active?)
        search-results? (pos?
                         (count
                          (:results (drv/react s search/search-key))))]
    ;; Show loading if
    (if (or ;; the org data are not loaded yet
            (not org-data)
            ;; No board specified
            (and (not (router/current-board-slug))
                 ;; but there are some
                 (pos? (count (:boards org-data))))
            ;; Board specified
            (and (not= (router/current-board-slug) "all-posts")
                 (not ap-initial-at)
                 ;; But no board data yet
                 (not board-data))
            ;; All posts
            (and (or (= (router/current-board-slug) "all-posts")
                     ap-initial-at)
                 ;; But no all-posts data yet
                 (not all-posts-data))
            ;; First ever user nux, not enough time
            (and nux-loading
                 (not nux-end)))
      [:div.org-dashboard
        (when should-show-onboard-overlay?
          [:div.screenshots-preload
            [:div.screenshot-preload.screenshot-1]
            [:div.screenshot-preload.screenshot-2]])
        (loading {:loading true})]
      [:div
        {:class (utils/class-set {:org-dashboard true
                                  :modal-activity-view (router/current-activity-id)
                                  :mobile-or-tablet is-mobile?
                                  :no-scroll (and (not is-mobile?)
                                                  (router/current-activity-id))})}
        ;; Use cond for the next components to exclud each other and avoid rendering all of them
        (cond
          should-show-onboard-overlay?
          (onboard-overlay nux)
          ;; Org settings
          org-settings-data
          (org-settings)
          ;; About carrot
          whats-new-modal-data
          (whats-new-modal)
          ;; Made with carrot modal
          made-with-carrot-modal-data
          (made-with-carrot-modal)
          ;; Mobile create a new section
          (and is-mobile?
               show-section-editor)
          (section-editor board-data #(dis/dispatch! [:section-edit-save]))
          ;; Mobile edit current section data
          (and is-mobile?
               show-section-add)
          (section-editor nil #(dis/dispatch! [:input [:show-section-add] false]))
          ;; Mobile sections picker
          (and is-mobile?
               show-sections-picker)
          (sections-picker entry-editing-board-slug
            (fn [board-data]
             (dis/dispatch! [:input [:show-sections-picker] false])
             (when board-data
              (dis/dispatch! [:update [:entry-editing]
               #(merge % {:board-slug (:slug board-data)
                          :board-name (:name board-data)})]))))
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
          (let [from-ap (:from-all-posts @router/path)
                board-slug (if from-ap :all-posts (router/current-board-slug))]
            (fullscreen-post)))
        ;; Activity share modal for no mobile
        (when (and (not is-mobile?)
                   is-sharing-activity)
          (activity-share))
        ;; Alert modal
        (when is-showing-alert
          (alert-modal))
        ;; Media video modal for entry editing
        (when (and media-input
                   (:media-video media-input))
          (media-video-modal))
        ;; Media chart modal for entry editing
        (when (and media-input
                   (:media-chart media-input))
          (media-chart-modal))
        (when-not (and is-mobile?
                       (or (router/current-activity-id)
                           is-entry-editing
                           should-show-onboard-overlay?
                           is-sharing-activity
                           show-section-add
                           show-section-editor
                           mobile-navigation-sidebar))
          [:div.page
            (navbar)
            [:div.org-dashboard-container
              [:div.org-dashboard-inner
               (when-not (and is-mobile?
                              (and search-active? search-results?))
                 (dashboard-layout))]]])])))