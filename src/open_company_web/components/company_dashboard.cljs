(ns open-company-web.components.company-dashboard
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer (sel1)]
            [rum.core :as rum]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.router :as router]
            [open-company-web.components.topic-list :refer (topic-list)]
            [open-company-web.caches :as caches]
            [open-company-web.components.welcome-screen :refer (welcome-screen)]
            [open-company-web.components.ui.login-required :refer (login-required)]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.loading :refer (loading)]
            [open-company-web.components.ui.login-overlay :refer (login-overlays-handler)]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.tooltip :as t]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

(defn- get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (router/current-company-slug))
          company-data (dis/company-data)]
      (when (and (empty? (slug @caches/new-sections))
                 (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (utils/after 1000 #(api/get-new-sections))))))

(defn share-tooltip-id [slug]
  (str "second-topic-share-" slug))

(defn show-share-tooltip-if-needed [owner]
  (let [company-data (dis/company-data (om/get-props owner))
        tt-id (share-tooltip-id (:slug company-data))]
    (when (and (not (om/get-state owner :share-tooltip-shown))
               (not (om/get-state owner :share-tooltip-dismissed))
               (not= (om/get-props owner :show-login-overlay) :collect-name-password)
               company-data
               (zero? (:count (utils/link-for (:links company-data) "stakeholder-updates")))
               (= (+ (count (utils/filter-placeholder-sections (:sections company-data) company-data))
                   (count (:archived company-data))) 2)
               (.querySelector js/document "button.sharing-button"))
      (om/set-state! owner :share-tooltip-shown true)
      (utils/after 500
        (fn []
          (let [tip (t/tooltip (.querySelector js/document "button.sharing-button") {:config {:place "bottom-left"}
                                                                                     :id tt-id
                                                                                     :once-only true
                                                                                     :dismiss-cb #(om/set-state! owner :share-tooltip-dismissed true)
                                                                                     :desktop "When you’re ready, you can share a beautiful company update with these topics."})]
            (t/show tt-id)))))))

(defn refresh-company-data [owner]
  (when (nil? (om/get-props owner :selected-topic-view))
    (api/get-company (router/current-company-slug))))

(defcomponent company-dashboard [data owner]

  (init-state [_]
    {:navbar-editing false
     :editing-topic false
     :save-bt-active false
     :share-tooltip-shown false
     :share-tooltip-dismissed false
     :add-second-topic-tt-shown false
     :new-sections-requested false
     :card-width (if (responsive/is-mobile-size?)
                   (responsive/mobile-dashboard-card-width)
                   (responsive/calc-card-width))
     :columns-num (responsive/dashboard-columns-num)})

  (did-mount [_]
    (utils/after 100 #(set! (.-scrollTop (.-body js/document)) 0))
    (when-not (:read-only (dis/company-data data))
      (get-new-sections-if-needed owner))
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE (fn [_] (om/update-state! owner #(merge % {:columns-num (responsive/dashboard-columns-num)
                                                                                           :card-width (if (responsive/is-mobile-size?)
                                                                                                         (responsive/mobile-dashboard-card-width)
                                                                                                         (responsive/calc-card-width))})))))
    (om/set-state! owner :window-click-listener
      (events/listen js/window EventType/CLICK (fn[e]
                                                 (when (and (:show-top-menu @dis/app-state)
                                                            (not (utils/event-inside? e (sel1 [(str "div.topic[data-section=" (name (:show-top-menu @dis/app-state)) "]")]))))
                                                   (utils/event-stop e)
                                                   (dis/dispatch! [:show-top-menu nil])))))
    (when (pos? (:count (utils/link-for (:links (dis/company-data data)) "stakeholder-updates")))
      (om/set-state! owner :share-tooltip-dismissed (t/tooltip-already-shown? (share-tooltip-id (:slug (dis/company-data data))))))
    (refresh-company-data owner)
    (om/set-state! owner :company-refresh-interval
      (js/setInterval #(refresh-company-data owner) (* 60 1000))))

  (will-unmount [_]
    (when (om/get-state owner :company-refresh-interval)
      (js/clearInterval (om/get-state owner :company-refresh-interval)))
    (when (om/get-state owner :window-click-listener)
      (events/unlistenByKey (om/get-state owner :window-click-listener)))
    (when (om/get-state owner :resize-listener)
      (events/unlistenByKey (om/get-state owner :resize-listener))))

  (did-update [_ prev-props prev-state]
    (let [company-data (dis/company-data data)]
      (when (and (:dashboard-sharing data)
                 (not (:dashboard-sharing prev-props)))
        (t/hide (str "second-topic-share-" (:slug company-data))))
      (when (and (not (:show-add-topic prev-props))
                 (:show-add-topic data))
        (let [add-second-topic-tt (str "add-second-topic-" (:slug company-data))]
          (t/hide add-second-topic-tt)))
      (when (and (not (om/get-state owner :add-second-topic-tt-shown))
                 (not (:selected-topic-view data))
                 (= (count (utils/filter-placeholder-sections (:sections company-data) company-data)) 1)
                 (not (:show-login-overlay data)))
        (om/set-state! owner :add-second-topic-tt-shown true)
        (let [add-second-topic-tt (str "add-second-topic-" (:slug company-data))]
          (t/tooltip (.querySelector js/document "button.left-topics-list-top-title")
                      {:desktop "Click on the + to add more topics and put together a complete company update."
                       :once-only true
                       :id add-second-topic-tt
                       :config {:typeClass "add-more-tooltip" :place "bottom-left"}})
          (t/show add-second-topic-tt)))
      (show-share-tooltip-if-needed owner)))

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
                                           :editing-topic (or (not (nil? (:foce-key data)))
                                                              (not (nil? (:show-top-menu data))))
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
                                  :is-topic-view (not (nil? (:selected-topic-view data)))
                                  :is-dashboard (nil? (:selected-topic-view data))}))
              (if (:show-welcome-screen data)
                (welcome-screen)
                (dom/div {}
                  (if (and (empty? (:sections company-data)) (responsive/is-tablet-or-mobile?))
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
                                 :is-dashboard true
                                 :show-top-menu (:show-top-menu data)}))
                  ;;Footer
                  (om/build footer {:footer-width total-width-int}))))))))))