(ns open-company-web.components.create-update
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.su-preview-dialog :refer (su-preview-dialog)]
            [clojure.data :as cd]))

(defn ordered-topics-list
  "Return the list of active topics in the order the user moved them."
  []
  (let [topics (sel [:div.create-update-topics-list :div.oc-active])
        topics-list (for [topic topics] (.data (js/jQuery topic) "topic"))]
    (vec (remove nil? topics-list))))

(defn patch-stakeholder-update [owner]
  (let [title  (om/get-state owner :su-title)
       topics (om/get-state owner :su-topics)]
    (api/patch-stakeholder-update {:title (or title "")
                                   :sections topics})))

(defn setup-sortable
  "Setup the jQuery UI Sortable on the create-update-topics-list div"
  [owner]
  (when-let [list-node (js/jQuery "div.create-update-topics-list")]
    (-> list-node
      (.sortable #js {:scroll true
                      :forcePlaceholderSize true
                      :items ".oc-active"
                      :stop (fn [event ui]
                              ; the user stopped ordering, save the current order
                              (when-let [dragged-item (gobj/get ui "item")]
                                (om/update-state! owner #(merge % {:su-topics (ordered-topics-list)
                                                                   :should-update-data false}))
                                (patch-stakeholder-update owner)))
                      :axis "y"})
      (.disableSelection))))

(defn share-clicked [owner]
  (patch-stakeholder-update owner)
  (om/set-state! owner :show-su-dialog :prompt))

(defcomponent create-update [data owner]

  (init-state [_]
    (dis/dispatch! [:start-foce nil])
    (let [company-data (dis/company-data data)
          su-data   (:stakeholder-update company-data)
          su-topics (if (empty? (:sections su-data))
                        (utils/filter-placeholder-sections (vec (:sections company-data)) company-data)
                        (utils/filter-placeholder-sections (:sections su-data) company-data))]
      {:columns-num (responsive/columns-num)
       :card-width (responsive/calc-card-width)
       :su-topics (vec su-topics)
       :su-title (:title su-data)
       :should-update-data true
       :show-su-dialog false}))

  (will-receive-props [_ next-props]
    (when (om/get-state owner :should-update-data)
      (let [company-data (dis/company-data next-props)
            su-data (:stakeholder-update company-data)
            su-topics (if (and (not (contains? su-data :sections)) (empty? (:sections su-data)))
                        (utils/filter-placeholder-sections (vec (:sections company-data)) company-data)
                        (utils/filter-placeholder-sections (:sections su-data) company-data))]
        (om/update-state! owner #(merge % {:su-topics (vec su-topics)
                                           :su-title (:title su-data)})))))

  (did-mount [_]
    (setup-sortable owner)
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE (fn [] (om/update-state! owner #(merge % {:columns-num (responsive/columns-num)
                                                                                          :card-width (responsive/calc-card-width)}))))))

  (did-update [_ _ _]
    (setup-sortable owner))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (render-state [_ {:keys [columns-num card-width su-title su-topics show-su-dialog]}]
    (let [company-data (dis/company-data data)
          total-width-int (responsive/total-layout-width-int card-width columns-num)
          total-width (str total-width-int "px")
          fixed-card-width (responsive/calc-update-width columns-num)]
      (dom/div {:class "create-update main-scroll group"}
        (dom/div {:class "page"}
          (om/build navbar {:card-width card-width
                            :columns-num columns-num
                            :header-width total-width-int
                            :company-data company-data
                            :foce-key (:foce-key data)
                            :show-share-su-button false
                            :create-update-share-button-cb #(share-clicked owner)
                            :create-update-share-button-disabled (zero? (count su-topics))
                            :active nil
                            :show-navigation-bar (utils/can-edit-sections? company-data)
                            :mobile-menu-open (:mobile-menu-open data)
                            :auth-settings (:auth-settings data)})
          (dom/div {:class "create-update-inner group navbar-offset"}
            (when show-su-dialog
              (om/build su-preview-dialog {:selected-topics su-topics
                                           :company-data company-data
                                           :latest-su (dis/latest-stakeholder-update)
                                           :su-title su-title}
                                          {:opts {:dismiss-su-preview #(om/set-state! owner :show-su-dialog false)}}))
            (dom/div {:class "create-update-content group"
                      :style {:width total-width}}
              (dom/div {:class "create-update-content-list group right"
                        :style {:width (str responsive/updates-content-list-width "px")}}
                (dom/div {:class "create-update-content-cta"}
                  "Click the topics you'd like to share and arrange them in any order.")
                (dom/div {:class "create-update-topics-list"
                          :key (clojure.string/join "-" su-topics)}
                  (for [topic su-topics]
                    (let [sd ((keyword topic) company-data)]
                      (dom/div {:class "create-update-topics-list-item oc-active"
                                :data-topic topic
                                :key topic
                                :ref topic
                                :on-click (fn [_]
                                            (om/update-state! owner #(merge % {:su-topics (utils/vec-dissoc su-topics topic)
                                                                               :should-update-data false}))
                                            (patch-stakeholder-update owner))}
                        (:title sd))))
                  (let [all-topics (:sections company-data)
                        remaining-topics (vec (first (cd/diff (set all-topics) (set su-topics))))
                        filtered-topics (utils/filter-placeholder-sections remaining-topics company-data)
                        sorted-topics (sort #(let [sd1 ((keyword %1) company-data)
                                                   sd2 ((keyword %2) company-data)]
                                               (compare (:title sd1) (:title sd2))) filtered-topics)]
                    (for [topic sorted-topics]
                      (let [sd ((keyword topic) company-data)]
                        (dom/div {:class "create-update-topics-list-item"
                                  :data-topic topic
                                  :key topic
                                  :ref topic
                                  :on-click (fn [_]
                                              (om/update-state! owner #(merge % {:su-topics (vec (conj su-topics topic))
                                                                                 :should-update-data false}))
                                              (patch-stakeholder-update owner))}
                          (:title sd)))))))
              (dom/div {:class "create-update-content-cards right"
                        :style {:width (str fixed-card-width "px")}}
                (dom/input {:class "create-update-content-cards-title"
                            :type "text"
                            :value su-title
                            :placeholder "Update Title"
                            :on-change (fn [e]
                                          (om/update-state! owner #(merge % {:su-title (.. e -target -value)
                                                                             :should-update-data false}))
                                          (patch-stakeholder-update owner))})
                (om/build topics-columns {:columns-num 1
                                          :card-width (- fixed-card-width 60) ; remove 60 padding around it
                                          :total-width (- fixed-card-width 60)
                                          :is-stakeholder-update true
                                          :content-loaded (not (:loading data))
                                          :topics su-topics
                                          :topics-data company-data
                                          :company-data company-data
                                          :hide-add-topic true}))))
          (om/build footer {:footer-width total-width-int}))))))