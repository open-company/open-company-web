(ns open-company-web.components.company-dashboard
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.router :as router]
            [open-company-web.components.topic-list :refer (topic-list)]
            [open-company-web.caches :as caches]
            [open-company-web.components.ui.login-required :refer (login-required)]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.loading :refer (loading)]
            [open-company-web.components.ui.login-overlay :refer (login-overlays-handler)]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn- get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (router/current-company-slug))
          company-data (dis/company-data)]
      (when (and (empty? (slug @caches/new-sections))
                 (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (utils/after 1000 #(api/get-new-sections))))))

(defcomponent company-dashboard [data owner]

  (init-state [_]
    {:navbar-editing false
     :editing-topic false
     :save-bt-active false
     :new-sections-requested false
     :card-width (if (responsive/is-mobile-size?)
                   (responsive/mobile-dashboard-card-width)
                   (responsive/calc-card-width))
     :columns-num (responsive/dashboard-columns-num)})

  (did-mount [_]
    (utils/after 100 #(set! (.-scrollTop (.-body js/document)) 0))
    (when-not (:read-only (dis/company-data data))
      (get-new-sections-if-needed owner))
    (events/listen js/window EventType/RESIZE (fn [_] (om/update-state! owner #(merge % {:columns-num (responsive/dashboard-columns-num)
                                                                                         :card-width (if (responsive/is-mobile-size?)
                                                                                                       (responsive/mobile-dashboard-card-width)
                                                                                                       (responsive/calc-card-width))})))))

  (will-receive-props [_ next-props]
    (when-not (:read-only (dis/company-data next-props))
      (get-new-sections-if-needed owner)))

  (render-state [_ {:keys [editing-topic navbar-editing save-bt-active columns-num card-width] :as state}]
    (let [slug (keyword (router/current-company-slug))
          company-data (dis/company-data data)
          total-width-int (responsive/total-layout-width-int card-width columns-num)]
      (if (:loading data)
        (dom/div {:class (utils/class-set {:company-dashboard true
                                           :main-scroll true})}
          (om/build loading {:loading true}))
        (dom/div {:class (utils/class-set {:company-dashboard true
                                           :mobile-company-dashboard (responsive/is-mobile-size?)
                                           :selected-topic-view (:selected-topic-view data)
                                           :mobile-or-tablet (responsive/is-tablet-or-mobile?)
                                           :small-navbar (not (utils/company-has-topics? company-data))
                                           :editing-topic (not (nil? (:foce-key data)))
                                           :main-scroll true})}
          (when (and (not (utils/is-test-env?))
                     (get-in data [(keyword (router/current-company-slug)) :error]))
            ;show login overlays if needed
            (login-overlays-handler))
          (if (get-in data [(keyword (router/current-company-slug)) :error])
            (dom/div {:class (str "fullscreen-page " (if (jwt/jwt) "with-small-footer" "with-footer"))}
              (login-required data)
              ;;Footer
               (om/build footer {:footer-width total-width-int}))
            (dom/div {:class "page"}
              ;; Navbar
              (when-not (and (responsive/is-tablet-or-mobile?)
                             (:selected-topic-view data))
                (om/build navbar {:save-bt-active save-bt-active
                                  :company-data company-data
                                  :card-width card-width
                                  :header-width total-width-int
                                  :columns-num columns-num
                                  :foce-key (:foce-key data)
                                  :show-share-su-button (utils/can-edit-sections? company-data)
                                  :show-login-overlay (:show-login-overlay data)
                                  :mobile-menu-open (:mobile-menu-open data)
                                  :auth-settings (:auth-settings data)
                                  :active :dashboard
                                  :dashboard-selected-topics (:dashboard-selected-topics data)
                                  :dashboard-sharing (:dashboard-sharing data)
                                  :show-navigation-bar (utils/company-has-topics? company-data)
                                  :is-dashboard (nil? (:selected-topic-view data))}))
              (when (:dashboard-sharing data)
                (dom/button {:class "btn-reset btn-link dashboard-sharing-select-all"
                             :on-click #(dis/dispatch! [:dashboard-select-all])} "Select all"))
              (if (and (empty? (:sections company-data)) (responsive/is-mobile-size?))
                (dom/div {:class "empty-dashboard"}
                  (dom/h3 {:class "empty-dashboard-title"}
                    "No topics have been created.")
                  (when-not (:read-only company-data)
                    (dom/p {:class "empty-dashboard-msg"}
                      (str "Hi" (when (jwt/jwt) (str " " (jwt/get-key :name))) ", your dashboard can be viewed after it's been created on a desktop browser."))))
                (om/build topic-list
                            {:loading (:loading data)
                             :content-loaded (or (:loading company-data) (:loading data))
                             :company-data company-data
                             :new-sections (:new-sections (slug data))
                             :latest-su (dis/latest-stakeholder-update)
                             :force-edit-topic (:force-edit-topic data)
                             :foce-data-editing? (:foce-data-editing? data)
                             :revision-updates (dis/revisions (router/current-company-slug))
                             :card-width card-width
                             :columns-num columns-num
                             :show-login-overlay (:show-login-overlay data)
                             :foce-key (:foce-key data)
                             :foce-data (:foce-data data)
                             :show-add-topic (:show-add-topic data)
                             :selected-topic-view (:selected-topic-view data)
                             :dashboard-selected-topics (:dashboard-selected-topics data)
                             :dashboard-sharing (:dashboard-sharing data)
                             :is-dashboard true}))
              ;;Footer
              (om/build footer {:footer-width total-width-int}))))))))