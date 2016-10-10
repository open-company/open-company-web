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
            [open-company-web.components.ui.menu :refer (menu)]
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

(defcomponent company-dashboard [{:keys [menu-open] :as data} owner]

  (init-state [_]
    {:navbar-editing false
     :editing-topic false
     :save-bt-active false
     :new-sections-requested false
     :columns-num (responsive/columns-num)})

  (did-mount [_]
    (when-not (:read-only (dis/company-data data))
      (get-new-sections-if-needed owner))
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num))))

  (will-receive-props [_ next-props]
    (when-not (:read-only (dis/company-data next-props))
      (get-new-sections-if-needed owner)))

  (render-state [_ {:keys [editing-topic navbar-editing save-bt-active columns-num] :as state}]
    (let [slug (keyword (router/current-company-slug))
          company-data (dis/company-data data)
          card-width (responsive/calc-card-width)]
      (if (:loading data)
        (dom/div {:class (utils/class-set {:company-dashboard true
                                           :main-scroll true})}
          (om/build loading {:loading true}))
        (dom/div {:class (utils/class-set {:company-dashboard true
                                           :main-scroll true})}
          ;show login overlays if needed
          (login-overlays-handler data)
          (om/build menu data)
          (if (get-in data [(keyword (router/current-company-slug)) :error])
            (dom/div {:class (str "fullscreen-page " (if (jwt/jwt) "with-small-footer" "with-footer"))}
              (login-required data)
              ;;Footer
               (om/build footer {:columns-num columns-num
                                 :card-width card-width}))
            (dom/div {:class "page"}
              ;; Navbar
              (om/build navbar {:save-bt-active save-bt-active
                                :company-data company-data
                                :card-width card-width
                                :columns-num columns-num
                                :foce-key (:foce-key data)
                                :show-share-su-button (utils/can-edit-sections? company-data)
                                :menu-open menu-open
                                :auth-settings (:auth-settings data)})
              (om/build topic-list
                          {:loading (:loading data)
                           :content-loaded (or (:loading company-data) (:loading data))
                           :company-data company-data
                           :new-sections (:new-sections (slug data))
                           :latest-su (dis/latest-stakeholder-update)
                           :force-edit-topic (:force-edit-topic data)
                           :revision-updates (dis/revisions (router/current-company-slug))
                           :card-width card-width
                           :columns-num columns-num
                           :foce-key (:foce-key data)
                           :foce-data (:foce-data data)})
              ;;Footer
              (om/build footer {:columns-num columns-num
                                :card-width card-width}))))))))