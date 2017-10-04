(ns oc.web.components.org-dashboard
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer (sel1)]
            [rum.core :as rum]
            [cljs.core.async :refer (chan <!)]
            [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.router :as router]
            [oc.web.lib.cookies :as cook]
            [oc.web.components.topics-columns :refer (topics-columns)]
            [oc.web.components.activity-modal :refer (activity-modal)]
            [oc.web.components.entry-edit :refer (entry-edit)]
            [oc.web.components.board-edit :refer (board-edit)]
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.org-settings :refer (org-settings)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.components.ui.media-chart-modal :refer (media-chart-modal)]
            [oc.web.components.ui.about-carrot-modal :refer (about-carrot-modal)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

(defn refresh-board-data []
  (when (not (router/current-activity-id))
    (let [board-data (dis/board-data)
          fixed-board-data (if board-data
                              board-data
                              (some #(when (= (:slug %) (router/current-board-slug)) %) (:boards (dis/org-data))))]
      (utils/after 100
        #(dis/dispatch! [:board-get (utils/link-for (:links fixed-board-data) ["item" "self"] "GET")])))))

(defcomponent org-dashboard [data owner]

  (did-mount [_]
    (utils/after 100 #(set! (.-scrollTop (.-body js/document)) 0))
    (refresh-board-data))

  (will-unmount [_]
    (when (om/get-state owner :resize-listener)
      (events/unlistenByKey (om/get-state owner :resize-listener))))

  (render-state [_ {:keys [columns-num card-width] :as state}]
    (let [org-slug (keyword (router/current-org-slug))
          org-data (dis/org-data data)
          board-slug (keyword (router/current-board-slug))
          board-data (dis/board-data data)
          all-posts-data (dis/all-posts-data data)]
      (if (or (not org-data)
              (and (not board-data)
                   (not all-posts-data)))
        (dom/div {:class (utils/class-set {:org-dashboard true
                                           :main-scroll true})}
          (om/build loading {:loading true}))
        (dom/div {:class (utils/class-set {:org-dashboard true
                                           :mobile-dashboard (responsive/is-mobile-size?)
                                           :modal-activity-view (router/current-activity-id)
                                           :mobile-or-tablet (responsive/is-tablet-or-mobile?)
                                           :main-scroll true
                                           :no-scroll (router/current-activity-id)})}
          ;; Use cond for the next components to exclud each other and avoid rendering all of them
          (cond
            ;; Org settings
            (:org-settings data)
            (org-settings)
            ;; About carrot
            (:about-carrot-modal data)
            (about-carrot-modal)
            ;; Entry editing
            (:entry-editing data)
            (entry-edit)
            ;; Board editing
            (:board-editing data)
            (board-edit)
            ;; Activity modal
            (and (router/current-activity-id)
                 (not (:entry-edit-dissmissing data)))
            (let [from-ap (:from-all-posts @router/path)
                  board-slug (if from-ap :all-posts (router/current-board-slug))]
              (activity-modal (dis/activity-data (router/current-org-slug) board-slug (router/current-activity-id) data))))
          ;; Alert modal
          (when (:alert-modal data)
            (alert-modal))
          ;; Media video modal for entry editing
          (when (and (:media-input data)
                     (:media-video (:media-input data)))
            (media-video-modal))
          ;; Media chart modal for entry editing
          (when (and (:media-input data)
                     (:media-chart (:media-input data)))
            (media-chart-modal))
          (dom/div {:class "page"}
            ;; Navbar
            (when-not (and (responsive/is-tablet-or-mobile?)
                           (router/current-activity-id))
              (navbar))
            (dom/div {:class "dashboard-container"}
              (dom/div {:class "topic-list"}
                (om/build topics-columns
                  {:loading (:loading data)
                   :content-loaded (or (:loading board-data) (:loading data))
                   :org-data org-data
                   :board-data board-data
                   :all-posts-data all-posts-data
                   :force-edit-topic (:force-edit-topic data)
                   :card-width card-width
                   :columns-num columns-num
                   :show-login-overlay (:show-login-overlay data)
                   :prevent-topic-not-found-navigation (:prevent-topic-not-found-navigation data)
                   :is-dashboard true
                   :board-filters (:board-filters data)
                   :is-all-posts (utils/in? (:route @router/path) "all-posts")})))))))))