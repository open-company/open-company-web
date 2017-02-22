(ns oc.web.components.create-update
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.topic :refer (topic)]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.su-preview-dialog :refer (su-preview-dialog)]
            [clojure.data :as cd]))

; (defn ordered-topics-list
;   "Return the list of active topics in the order the user moved them."
;   []
;   (let [topics (sel [:div.topics-columns :div.topic-row])
;         topics-list (for [topic topics] (.data (js/jQuery topic) "topic"))]
;     (vec (remove nil? topics-list))))

; (defn patch-stakeholder-update [owner]
;   (let [title  (om/get-state owner :su-title)
;        topics (om/get-state owner :su-topics)]
;     (api/patch-stakeholder-update {:title (or title "")
;                                    :sections topics})))

; (defn setup-sortable
;   "Setup the jQuery UI Sortable on the create-update-topics-list div"
;   [owner]
;   (when (> (count (om/get-state owner :su-topics)) 1)
;     (when-let [list-node (js/jQuery "div.topics-columns")]
;       (-> list-node
;         (.sortable #js {:scroll true
;                         :forcePlaceholderSize true
;                         :items ".topic-row"
;                         :stop (fn [event ui]
;                                 ; the user stopped ordering, save the current order
;                                 (when-let [dragged-item (gobj/get ui "item")]
;                                   (om/update-state! owner #(merge % {:su-topics (ordered-topics-list)
;                                                                      :should-update-data false}))
;                                   (patch-stakeholder-update owner)))
;                         :axis "y"})
;         (.disableSelection)))))

(defn share-clicked [owner]
  ; (patch-stakeholder-update owner)
  (om/set-state! owner :show-su-dialog :prompt))

(defn- share-tooltip []
  (if (utils/slack-share?)
    "Share this update by Slack, email or link"
    "Share this update by email or link"))

(defcomponent create-update [data owner]

  (init-state [_]
    (dis/dispatch! [:start-foce nil])
    {:columns-num (responsive/columns-num)
     :card-width (responsive/calc-card-width)
     :su-title ""
     :should-update-data true
     :did-share false
     :show-su-dialog false})

  ; (will-receive-props [_ next-props]
  ;   (when (om/get-state owner :should-update-data)
  ;     (let [company-data (dis/board-data next-props)
  ;           su-data (:stakeholder-update company-data)
  ;           su-topics (if (and (not (contains? su-data :sections)) (empty? (:sections su-data)))
  ;                       (utils/filter-placeholder-sections (vec (:sections company-data)) company-data)
  ;                       (utils/filter-placeholder-sections (:sections su-data) company-data))]
  ;       (om/update-state! owner #(merge % {:su-topics (vec su-topics)
  ;                                          :su-title (:title su-data)})))))

  (did-mount [_]
    ; (setup-sortable owner)
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE (fn [] (om/update-state! owner #(merge % {:columns-num (responsive/columns-num)
                                                                                          :card-width (responsive/calc-card-width)}))))))

  ; (did-update [_ _ prev-state]
  ;   (setup-sortable owner))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (render-state [_ {:keys [columns-num card-width su-title show-su-dialog]}]
    (let [org-data (dis/org-data data)
          total-width-int (responsive/total-layout-width-int card-width columns-num)
          total-width (str total-width-int "px")
          fixed-card-width (responsive/calc-update-width columns-num)
          back-to-dashboard-fn #(do
                                  (dis/dispatch! [:dashboard-share-mode false])
                                  (router/nav! (oc-urls/org)))]
      (dom/div {:class "create-update main-scroll group"}
        (dom/div {:class "page"}
          (om/build navbar {:card-width card-width
                            :columns-num columns-num
                            :header-width total-width-int
                            :org-data org-data
                            :foce-key (:foce-key data)
                            :show-share-su-button false
                            :show-login-overlay (:show-login-overlay data)
                            :active nil
                            :mobile-menu-open (:mobile-menu-open data)
                            :auth-settings (:auth-settings data)
                            :is-update-preview true})
          (dom/div {:class "create-update-inner group navbar-offset"}
            (when show-su-dialog
              (om/build su-preview-dialog {:latest-su (dis/latest-stakeholder-update)
                                           :su-title su-title
                                           :dismiss-su-preview #(om/set-state! owner :show-su-dialog false)
                                           :back-to-dashboard-cb back-to-dashboard-fn
                                           :did-share-cb #(om/set-state! owner :did-share true)}))
            (dom/div {:class "create-update-content group"
                      :style {:width total-width}}
              (dom/div {:class "create-update-content-list group right"
                        :style {:width (str responsive/updates-content-list-width "px")}}
                (dom/div {:class "create-update-content-buttons group"}
                  (dom/button {:class "btn-reset btn-outline cancel"
                               :title "Back to dashboard"
                               :data-toggle "tooltip"
                               :data-container "body"
                               :data-placement "left"
                               :on-click back-to-dashboard-fn} "CANCEL")
                  (dom/button {:class "share btn-reset btn-solid"
                               :title (share-tooltip)
                               :data-toggle "tooltip"
                               :data-container "body"
                               :data-placement "left"
                               :on-click #(share-clicked owner)
                               :disabled (zero? (count (:dashboard-selected-topics data)))} "SHARE"))
                (when (> (count (:dashboard-selected-topics data)) 1)
                  (dom/div {:class "create-update-content-cta"}
                    "Tip: You can drag topics into any order before you share them.")))
              (dom/div {:class (str "create-update-content-cards right" (when (> (count (:dashboard-selected-topics data)) 1) " dnd"))
                        :style {:width (str fixed-card-width "px")}}
                (dom/input {:class "create-update-content-cards-title"
                            :type "text"
                            :value (or (:su-title data) "")
                            :placeholder "Title"
                            :style #js {:width (str (- fixed-card-width 60) "px")}
                            :on-change (fn [e]
                                          (dis/dispatch! [:input [:su-title] (.. e -target -value)])
                                          (om/update-state! owner #(merge % {:su-title (.. e -target -value)
                                                                             :should-update-data false})))})
                (if (zero? (count (:dashboard-selected-topics data)))
                  (dom/div {:class "create-update-content-cards-no-topics"} "No Topics Selected")
                  (dom/div {:class (str "topics-columns overflow-visible group" (when-not (:loading data) " content-loaded"))}
                    (dom/div {:class "topics-column-container columns-1 group"
                              :style (if (responsive/is-mobile-size?)
                                       #js {:margin "0px 9px"
                                            :width "auto"}
                                       #js {:width (- fixed-card-width 60)})}
                      (dom/div {:class "topics-column"}
                        (for [entry (:dashboard-selected-topics data)
                              :let [board-data (dis/board-data data (router/current-org-slug) (:board-slug entry))
                                    topic-data ((:topic-slug entry) board-data)]]
                          (dom/div {:class "topic-row"}
                            (om/build topic {:loading (:loading data)
                                             :topic (:topic-slug entry)
                                             :is-stakeholder-update true
                                             :topic-data topic-data
                                             :entries-data []
                                             :card-width card-width
                                             :columns-num columns-num
                                             :read-only-board (:read-only org-data)
                                             :currency (:currency org-data)
                                             :editing false
                                             :foce-key nil
                                             :is-dashboard false
                                             :show-editing false
                                             :column 1}
                                             {:opts {:topic-name (name (:topic-slug entry))}
                                              :key (str (:board-slug entry) "-" (:topic-slug entry))}))))))))))
          (footer total-width-int))))))