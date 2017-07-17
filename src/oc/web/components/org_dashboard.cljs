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
            [oc.web.components.topics-list :refer (topics-list)]
            [oc.web.components.welcome-screen :refer (welcome-screen)]
            [oc.web.components.entry-modal :refer (entry-modal)]
            [oc.web.components.entry-create :refer (entry-create)]
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

(defn- get-new-topics-if-needed [owner]
  (when-not (om/get-state owner :new-topics-requested)
    (let [org-slug (keyword (router/current-org-slug))
          board-slug (keyword (router/current-board-slug))
          board-data (dis/board-data)]
      (when (and (not (get-in (om/get-props owner) (dis/board-new-topics-key org-slug board-slug)))
                 (seq board-data))
        (om/update-state! owner :new-topics-requested not)
        (utils/after 1000 #(api/get-new-topics))))))

(def add-second-topic-tt-prefix "add-second-topic-")

(defn refresh-board-data []
  (when (not (router/current-entry-uuid))
    ; FIXME: re-enable the auto board refresh once the add entry has server support
    ; (api/get-board (dis/board-data))
    ))

(defcomponent org-dashboard [data owner]

  (init-state [_]
    {:navbar-editing false
     :editing-topic false
     :save-bt-active false
     :add-second-topic-tt-shown false
     :new-topics-requested false
     :card-width (if (responsive/is-mobile-size?)
                   (responsive/mobile-dashboard-card-width)
                   (responsive/calc-card-width))
     :columns-num (responsive/dashboard-columns-num)})

  (did-mount [_]
    (utils/after 100 #(set! (.-scrollTop (.-body js/document)) 0))
    (when-not (:read-only (dis/board-data data))
      (get-new-topics-if-needed owner))
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE (fn [_] (om/update-state! owner #(merge % {:columns-num (responsive/dashboard-columns-num)
                                                                                           :card-width (if (responsive/is-mobile-size?)
                                                                                                         (responsive/mobile-dashboard-card-width)
                                                                                                         (responsive/calc-card-width))})))))
    (om/set-state! owner :window-click-listener
      (events/listen js/window EventType/CLICK (fn[e]
                                                 (when (and (:show-top-menu @dis/app-state)
                                                            (not (utils/event-inside? e (sel1 [(str "div.topic[data-topic=" (name (:show-top-menu @dis/app-state)) "]")]))))
                                                   (utils/event-stop e)
                                                   (dis/dispatch! [:top-menu-show nil])))))
    (refresh-board-data)
    (om/set-state! owner :board-refresh-interval
      (js/setInterval #(refresh-board-data) (* 60 1000))))

  (will-unmount [_]
    (when (om/get-state owner :board-refresh-interval)
      (js/clearInterval (om/get-state owner :board-refresh-interval)))
    (when (om/get-state owner :window-click-listener)
      (events/unlistenByKey (om/get-state owner :window-click-listener)))
    (when (om/get-state owner :resize-listener)
      (events/unlistenByKey (om/get-state owner :resize-listener))))

  (will-receive-props [_ next-props]
    (when-not (:read-only (dis/board-data next-props))
      (get-new-topics-if-needed owner)))

  (render-state [_ {:keys [editing-topic navbar-editing save-bt-active columns-num card-width] :as state}]
    (let [org-slug (keyword (router/current-org-slug))
          org-data (dis/org-data data)
          board-slug (keyword (router/current-board-slug))
          board-data (dis/board-data data)
          entries-data (dis/entries-data data)
          total-width-int (responsive/total-layout-width-int card-width columns-num)
          board-error (get-in data (dis/board-access-error-key (router/current-org-slug) (router/current-board-slug)))]
      (if (or (not org-data)
              (not board-data))
        (dom/div {:class (utils/class-set {:org-dashboard true
                                           :main-scroll true})}
          (om/build loading {:loading true}))
        (dom/div {:class (utils/class-set {:org-dashboard true
                                           :mobile-dashboard (responsive/is-mobile-size?)
                                           :dashboard-sharing (:dashboard-sharing data)
                                           :selected-topic-view (router/current-entry-uuid)
                                           :mobile-or-tablet (responsive/is-tablet-or-mobile?)
                                           :editing-topic (or (not (nil? (:foce-key data)))
                                                              (not (nil? (:show-top-menu data))))
                                           :main-scroll true
                                           :no-scroll (router/current-entry-uuid)})}
          (when (and (not (utils/is-test-env?))
                     board-error)
            ;show login overlays if needed
            (login-overlays-handler))
          (when (router/current-entry-uuid)
            (entry-modal (dis/entry-data)))
          (when (:new-entry data)
            (entry-create))
          (if board-error
            (dom/div {:class "fullscreen-page with-small-footer"}
              (login-required data))
            (dom/div {:class "page"}
              ;; Navbar
              (when-not (and (responsive/is-tablet-or-mobile?)
                             (router/current-entry-uuid))
                (om/build navbar {:save-bt-active save-bt-active
                                  :org-data org-data
                                  :board-data board-data
                                  :card-width card-width
                                  :header-width total-width-int
                                  :columns-num columns-num
                                  :foce-key (:foce-key data)
                                  :show-share-su-button (utils/can-edit-topics? board-data)
                                  :show-login-overlay (:show-login-overlay data)
                                  :mobile-menu-open (:mobile-menu-open data)
                                  :auth-settings (:auth-settings data)
                                  :active :dashboard
                                  :dashboard-selected-topics (:dashboard-selected-topics data)
                                  :dashboard-sharing (:dashboard-sharing data)
                                  :show-navigation-bar (utils/company-has-topics? board-data)
                                  :is-dashboard (not (router/current-entry-uuid))}))
              (if (:show-welcome-screen data)
                (welcome-screen)
                (dom/div {:class "dashboard-container"}
                  (if (and (empty? (:topics board-data)) (responsive/is-tablet-or-mobile?))
                    (dom/div {:class "empty-dashboard"}
                      (dom/h3 {:class "empty-dashboard-title"}
                        "No topics have been created.")
                      (when-not (:read-only board-data)
                        (dom/p {:class "empty-dashboard-msg"}
                          (str "Hi" (when (jwt/jwt) (str " " (jwt/get-key :name))) ", your dashboard can be viewed after it's been created on a desktop browser."))))
                    (if (:dashboard-sharing data)
                      (dom/div {:class "sharing-boards"}
                        (for [board (:boards org-data)
                              :let [board-data (dis/board-data data (router/current-org-slug) (:slug board))]
                              :when (pos? (count (:topics board-data)))]
                          (dom/div {:class "sharing-board-container"}
                            (dom/h3 {:class "board-title"} (:name board-data))
                            (om/build topics-list
                                        {:loading (:loading data)
                                         :content-loaded (or (:loading board-data) (:loading data))
                                         :org-data org-data
                                         :board-data board-data
                                         :entries-data []
                                         :create-board (:create-board data)
                                         :new-topics nil
                                         :force-edit-topic (:force-edit-topic data)
                                         :foce-data-editing? (:foce-data-editing? data)
                                         :card-width card-width
                                         :columns-num columns-num
                                         :show-login-overlay (:show-login-overlay data)
                                         :foce-key (:foce-key data)
                                         :foce-data (:foce-data data)
                                         :new-entry-edit (:new-entry-edit data)
                                         :dashboard-selected-topics (:dashboard-selected-topics data)
                                         :dashboard-sharing (:dashboard-sharing data)
                                         :prevent-topic-not-found-navigation (:prevent-topic-not-found-navigation data)
                                         :is-dashboard true
                                         :show-top-menu (:show-top-menu data)
                                         :board-filters (:board-filters data)}))))
                      (om/build topics-list
                                  {:loading (:loading data)
                                   :content-loaded (or (:loading board-data) (:loading data))
                                   :org-data org-data
                                   :board-data board-data
                                   :entries-data entries-data
                                   :create-board (:create-board data)
                                   :new-topics (get-in data (dis/board-new-topics-key (router/current-org-slug) (router/current-board-slug)))
                                   :force-edit-topic (:force-edit-topic data)
                                   :foce-data-editing? (:foce-data-editing? data)
                                   :card-width card-width
                                   :columns-num columns-num
                                   :show-login-overlay (:show-login-overlay data)
                                   :foce-key (:foce-key data)
                                   :foce-data (:foce-data data)
                                   :new-entry-edit (:new-entry-edit data)
                                   :dashboard-selected-topics (:dashboard-selected-topics data)
                                   :dashboard-sharing (:dashboard-sharing data)
                                   :prevent-topic-not-found-navigation (:prevent-topic-not-found-navigation data)
                                   :is-dashboard true
                                   :show-top-menu (:show-top-menu data)
                                   :board-filters (:board-filters data)}))))))))))))