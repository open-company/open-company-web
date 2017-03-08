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
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.ui.floating-add-topic :refer (floating-add-topic)]
            [oc.web.components.bot-access-prompt :refer (bot-access-prompt)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            ; [oc.web.lib.tooltip :as t]
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

; (defn share-tooltip-id [slug]
;   (str "second-topic-share-" slug))

; (defn show-share-tooltip-if-needed [owner]
;   (let [board-data (dis/board-data (om/get-props owner))
;         tt-id (share-tooltip-id (:slug board-data))]
;     (when (and (not (om/get-state owner :share-tooltip-shown))
;                (not (om/get-state owner :share-tooltip-dismissed))
;                (not= (om/get-props owner :show-login-overlay) :collect-name-password)
;                board-data
;                (zero? (:count (utils/link-for (:links board-data) "stakeholder-updates")))
;                (= (+ (count (utils/filter-placeholder-topics (:topics board-data) board-data))
;                    (count (:archived board-data))) 2)
;                (.querySelector js/document "button.sharing-button"))
;       (om/set-state! owner :share-tooltip-shown true)
;       (utils/after 500
;         (fn []
;           (let [tip (t/tooltip (.querySelector js/document "button.sharing-button") {:config {:place "bottom-left"}
;                                                                                      :id tt-id
;                                                                                      :once-only true
;                                                                                      :dismiss-cb #(om/set-state! owner :share-tooltip-dismissed true)
;                                                                                      :desktop "When you’re ready, you can share a beautiful board update with these topics."})]
;             (t/show tt-id)))))))

(def add-second-topic-tt-prefix "add-second-topic-")

(defn refresh-board-data []
  (when (not (router/current-topic-slug))
    (api/get-board (dis/board-data))))

(defn bot-access-requested-cookie [org-slug]
  (str org-slug "-bot-access-requested"))

(defcomponent org-dashboard [data owner]

  (init-state [_]
    {:navbar-editing false
     :editing-topic false
     :save-bt-active false
     :share-tooltip-shown false
     :share-tooltip-dismissed false
     :add-second-topic-tt-shown false
     :new-topics-requested false
     :hide-prompt-add-bot (cook/get-cookie (bot-access-requested-cookie (:slug (dis/org-data data))))
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
                                                   (dis/dispatch! [:show-top-menu nil])))))
    ; (when (pos? (:count (utils/link-for (:links (dis/board-data data)) "stakeholder-updates")))
    ;   (om/set-state! owner :share-tooltip-dismissed (t/tooltip-already-shown? (share-tooltip-id (:slug (dis/board-data data))))))
    (refresh-board-data)
    (om/set-state! owner :board-refresh-interval
      (js/setInterval #(refresh-board-data) (* 60 1000))))

  (will-unmount [_]
    (when (om/get-state owner :board-refresh-interval)
      (js/clearInterval (om/get-state owner :board-refresh-interval)))
    (when (om/get-state owner :window-click-listener)
      (events/unlistenByKey (om/get-state owner :window-click-listener)))
    (when (om/get-state owner :resize-listener)
      (events/unlistenByKey (om/get-state owner :resize-listener)))
    ; (let [board-data (dis/board-data (om/get-props owner))]
    ;   (t/hide (str add-second-topic-tt-prefix (:slug board-data)))
    ;   (t/hide (share-tooltip-id (:slug board-data))))
    )

  (will-update [_ next-props _]
    (let [org-data (dis/org-data next-props)
          next-hide-prompt-add-bot (cook/get-cookie (bot-access-requested-cookie (:slug org-data)))]
      (when (not= (om/get-state owner :hide-prompt-add-bot) next-hide-prompt-add-bot)
        (om/set-state! owner :hide-prompt-add-bot next-hide-prompt-add-bot))))

  ; (did-update [_ prev-props prev-state]
  ;   (let [board-data (dis/board-data data)]
  ;     (when (and (:dashboard-sharing data)
  ;                (not (:dashboard-sharing prev-props)))
  ;       (t/hide (share-tooltip-id (:slug board-data))))
  ;     (when (and (not (:show-add-topic prev-props))
  ;                (:show-add-topic data))
  ;       (let [add-second-topic-tt (str add-second-topic-tt-prefix (:slug board-data))]
  ;         (t/hide add-second-topic-tt)))
  ;     (when (and (not (om/get-state owner :add-second-topic-tt-shown))
  ;                (not (router/current-topic-slug))
  ;                (= (count (utils/filter-placeholder-topics (:topics board-data) board-data)) 1)
  ;                (not (:show-login-overlay data)))
  ;       (om/set-state! owner :add-second-topic-tt-shown true)
  ;       (let [add-second-topic-tt (str add-second-topic-tt-prefix (:slug board-data))]
  ;         (t/tooltip (.querySelector js/document "button.left-topics-list-top-title")
  ;                     {:desktop "Click on the + to add more topics and put together a complete board update."
  ;                      :once-only true
  ;                      :id add-second-topic-tt
  ;                      :config {:typeClass "add-more-tooltip" :place "bottom-left"}})
  ;         (t/show add-second-topic-tt)))
  ;     (show-share-tooltip-if-needed owner)))

  (will-receive-props [_ next-props]
    (when-not (:read-only (dis/board-data next-props))
      (get-new-topics-if-needed owner)))

  (render-state [_ {:keys [editing-topic navbar-editing save-bt-active columns-num card-width hide-prompt-add-bot] :as state}]
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
                                           :selected-topic-view (router/current-topic-slug)
                                           :mobile-or-tablet (responsive/is-tablet-or-mobile?)
                                           :small-navbar (not (utils/company-has-topics? board-data))
                                           :editing-topic (or (not (nil? (:foce-key data)))
                                                              (not (nil? (:show-top-menu data))))
                                           :main-scroll true})}
          (when (and (not (utils/is-test-env?))
                     board-error)
            ;show login overlays if needed
            (login-overlays-handler))
          (if board-error
            (dom/div {:class (str "fullscreen-page " (if (jwt/jwt) "with-small-footer" "with-footer"))}
              (login-required data)
              ;;Footer
               (footer total-width-int))
            (dom/div {:class "page"}
              ;; Navbar
              (when-not (and (responsive/is-tablet-or-mobile?)
                             (router/current-topic-slug))
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
                                  :is-topic-view (router/current-topic-slug)
                                  :is-dashboard (not (router/current-topic-slug))}))
              (if (and (not (cook/get-cookie (bot-access-requested-cookie (:slug org-data))))
                       (jwt/is-slack-org?)
                       (not (jwt/team-has-bot? (:team-id org-data)))
                       (not hide-prompt-add-bot))
                (bot-access-prompt #(do
                                      (cook/set-cookie! (bot-access-requested-cookie (:slug org-data)) true (* 60 60 24 30))
                                      (om/set-state! owner :hide-prompt-add-bot true)))
                (if (:show-welcome-screen data)
                  (welcome-screen)
                  (dom/div {}
                    (if (and (empty? (:topics board-data)) (responsive/is-tablet-or-mobile?))
                      (dom/div {:class "empty-dashboard"}
                        (dom/h3 {:class "empty-dashboard-title"}
                          "No topics have been created.")
                        (when-not (:read-only board-data)
                          (dom/p {:class "empty-dashboard-msg"}
                            (str "Hi" (when (jwt/jwt) (str " " (jwt/get-key :name))) ", your dashboard can be viewed after it's been created on a desktop browser."))))
                      (if (:dashboard-sharing data)
                        (for [board (:boards org-data)
                              :let [board-data (dis/board-data data (router/current-org-slug) (:slug board))]
                              :when (pos? (count (:topics board-data)))]
                          (om/build topics-list
                                      {:loading (:loading data)
                                       :content-loaded (or (:loading board-data) (:loading data))
                                       :org-data org-data
                                       :board-data board-data
                                       :entries-data []
                                       :create-board (:create-board data)
                                       :new-topics nil
                                       :latest-su (dis/latest-stakeholder-update)
                                       :force-edit-topic (:force-edit-topic data)
                                       :foce-data-editing? (:foce-data-editing? data)
                                       :card-width card-width
                                       :columns-num columns-num
                                       :show-login-overlay (:show-login-overlay data)
                                       :foce-key (:foce-key data)
                                       :foce-data (:foce-data data)
                                       :show-add-topic (:show-add-topic data)
                                       :dashboard-selected-topics (:dashboard-selected-topics data)
                                       :dashboard-sharing (:dashboard-sharing data)
                                       :prevent-topic-not-found-navigation (:prevent-topic-not-found-navigation data)
                                       :is-dashboard true
                                       :show-top-menu (:show-top-menu data)}))
                        (om/build topics-list
                                    {:loading (:loading data)
                                     :content-loaded (or (:loading board-data) (:loading data))
                                     :org-data org-data
                                     :board-data board-data
                                     :entries-data entries-data
                                     :create-board (:create-board data)
                                     :new-topics (get-in data (dis/board-new-topics-key (router/current-org-slug) (router/current-board-slug)))
                                     :latest-su (dis/latest-stakeholder-update)
                                     :force-edit-topic (:force-edit-topic data)
                                     :foce-data-editing? (:foce-data-editing? data)
                                     :card-width card-width
                                     :columns-num columns-num
                                     :show-login-overlay (:show-login-overlay data)
                                     :foce-key (:foce-key data)
                                     :foce-data (:foce-data data)
                                     :show-add-topic (:show-add-topic data)
                                     :dashboard-selected-topics (:dashboard-selected-topics data)
                                     :dashboard-sharing (:dashboard-sharing data)
                                     :prevent-topic-not-found-navigation (:prevent-topic-not-found-navigation data)
                                     :is-dashboard true
                                     :show-top-menu (:show-top-menu data)})))
                    (when (and (not (:read-only org-data))
                               (not (:show-add-topic data))
                               (not (router/current-topic-slug))
                               (not (:foce-key data))
                               (not (responsive/is-tablet-or-mobile?))
                               (not (:dashboard-sharing data)))
                      (floating-add-topic))
                    ;;Footer
                    (footer total-width-int)))))))))))