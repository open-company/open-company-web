(ns oc.web.components.org-dashboard
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.entry-edit :refer (entry-edit)]
            [oc.web.components.board-edit :refer (board-edit)]
            [oc.web.components.org-settings :refer (org-settings)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.dashboard-layout :refer (dashboard-layout)]
            [oc.web.components.activity-modal :refer (activity-modal)]
            [oc.web.components.ui.onboard-overlay :refer (onboard-overlay)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.components.ui.media-chart-modal :refer (media-chart-modal)]
            [oc.web.components.ui.whats-new-modal :refer (whats-new-modal)]
            [oc.web.components.ui.activity-share :refer (activity-share)]
            [oc.web.components.ui.made-with-carrot-modal :refer (made-with-carrot-modal)]))

(defn refresh-board-data [s]
  (when-not (router/current-activity-id)
    (utils/after 100 (fn []
     (if (= (router/current-board-slug) "all-posts")
       (dis/dispatch! [:all-posts-get])
       (let [{:keys [org-data
                     board-data]} @(drv/get-ref s :org-dashboard-data)
             fixed-board-data (or
                               board-data
                               (some #(when (= (:slug %) (router/current-board-slug)) %) (:boards org-data)))]
         (dis/dispatch! [:board-get (utils/link-for (:links fixed-board-data) ["item" "self"] "GET")])))))))

(rum/defcs org-dashboard < rum/static
                           rum/reactive
                           (drv/drv :org-dashboard-data)
                           {:did-mount (fn [s]
                             (utils/after 100 #(set! (.-scrollTop (.-body js/document)) 0))
                             (refresh-board-data s)
                             s)}
  [s]
  (let [{:keys [org-data
                board-data
                all-posts-data
                activity-data
                nux
                nux-loading
                nux-end
                ap-initial-at
                org-settings-data
                whats-new-modal-data
                made-with-carrot-modal-data
                entry-editing
                board-editing
                activity-share-data
                entry-edit-dissmissing
                alert-modal-data
                media-input]} (drv/react s :org-dashboard-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        should-show-onboard-overlay? (some #{nux} [:1 :7])]
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
        (loading {:loading true :nux (or (cook/get-cookie :nux) nux-loading)})]
      [:div
        {:class (utils/class-set {:org-dashboard true
                                  :modal-activity-view (router/current-activity-id)
                                  :mobile-or-tablet is-mobile?
                                  :no-scroll (and (not is-mobile?)
                                                  (router/current-activity-id))})}
        ;; Use cond for the next components to exclud each other and avoid rendering all of them
        (cond
          (some #{nux} [:1 :7])
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
          ;; Entry editing
          entry-editing
          (entry-edit)
          ;; Board editing
          board-editing
          (board-edit)
          ;; Activity share for mobile
          (and is-mobile?
               activity-share-data)
          (activity-share)
          ;; Activity modal
          (and (router/current-activity-id)
               (not entry-edit-dissmissing))
          (let [from-ap (:from-all-posts @router/path)
                board-slug (if from-ap :all-posts (router/current-board-slug))]
            (activity-modal activity-data)))
        ;; Activity share modal for no mobile
        (when (and (not is-mobile?)
                   activity-share-data)
          (activity-share))
        ;; Alert modal
        (when alert-modal-data
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
                           entry-editing
                           activity-share-data))
          [:div.page
            (navbar)
            [:div.dashboard-container
              [:div.topic-list
                (dashboard-layout)]]])])))