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
            [open-company-web.components.su-preview-dialog :refer (su-preview-dialog)]))

(def topic-total-x-padding 20)
(def updates-content-list-width 260)
(def updates-content-cards-right-margin 20)
(def updates-content-cards-max-width 560)
(def updates-content-cards-min-width 250)

(defn ordered-topics-list []
  (let [topics (sel [:div.create-update-topics-list :div.oc-active])
        topics-list (for [topic topics] (.data (js/jQuery topic) "topic"))]
    (vec (remove nil? topics-list))))

(defn remove-pinned [data]
  (loop [topics data
         all-keys (vec (keys data))
         idx 0]
    (if (= idx (count all-keys))
      topics
      (let [k (get all-keys idx)
            v (get topics k)
            new-v (if (map? v)
                    (if (contains? v :pin)
                      (assoc v :pin false)
                      v)
                    v)]
         (recur (assoc topics k new-v)
                all-keys
                (inc idx))))))

(defn patch-stakeholder-update [owner]
  (let [title  (om/get-state owner :su-title)
       topics (om/get-state owner :su-topics)]
    (api/patch-stakeholder-update {:title (or title "")
                                   :sections topics})))

(defn setup-sortable [owner]
  (when-let [list-node (js/jQuery "div.create-update-topics-list")]
    (-> list-node
      (.sortable #js {:scroll true
                      :forcePlaceholderSize true
                      :items ".oc-active"
                      :stop (fn [event ui]
                              (when-let [dragged-item (gobj/get ui "item")]
                                (om/set-state! owner :su-topics (ordered-topics-list))
                                (patch-stakeholder-update owner)))
                      :axis "y"})
      (.disableSelection))))

(defn share-clicked [owner]
  (patch-stakeholder-update owner)
  (om/set-state! owner :show-su-dialog :prompt))

(defcomponent create-update [data owner]

  (init-state [_]
    (let [company-data (dis/company-data data)
          su-data (:stakeholder-update company-data)]
      {:columns-num (responsive/columns-num)
       :su-topics (vec (:sections su-data))
       :su-title (:title su-data)
       :no-pinned-topics (remove-pinned (dis/company-data data))
       :show-su-dialog false}))

  (will-receive-props [_ next-props]
    (om/set-state! owner :no-pinned-topics (remove-pinned (dis/company-data next-props)))
    (when (zero? (count (om/get-state owner :su-topics)))
      (let [company-data (dis/company-data next-props)
            su-data (:stakeholder-update company-data)]
        (om/update-state! owner #(merge % {:su-topics (vec (:sections su-data))
                                           :su-title (:title su-data)})))))

  (did-mount [_]
    (setup-sortable owner)
    (om/set-state! owner :resize-listener (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num)))))

  (did-update [_ _ _]
    (setup-sortable owner))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (render-state [_ {:keys [columns-num su-title su-topics no-pinned-topics show-su-dialog]}]
    (let [company-data (dis/company-data data)
          card-width   (responsive/calc-card-width)
          ww           (.-clientWidth (.-body js/document))
          total-width-int (- (* (+ card-width topic-total-x-padding) columns-num) ; width of each column less
                             20                                                   ; the container padding
                             40)                                                  ; the distance btw the columns
          fixed-total-width-int (if (<= total-width-int (+ updates-content-cards-min-width updates-content-cards-right-margin updates-content-list-width))
                                  (+ updates-content-cards-min-width updates-content-cards-right-margin updates-content-list-width)
                                  total-width-int)
          total-width  (str fixed-total-width-int "px")
          fixed-card-width (if (>= (- fixed-total-width-int updates-content-list-width updates-content-cards-right-margin) updates-content-cards-max-width)
                              updates-content-cards-max-width
                              (- fixed-total-width-int updates-content-list-width updates-content-cards-right-margin))]
      (dom/div {:class "create-update main-scroll group"}
        (dom/div {:class "page"}
          (om/build navbar {:card-width card-width
                            :columns-num columns-num
                            :company-data company-data
                            :foce-key (:foce-key data)
                            :show-share-su-button false
                            :active nil
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
                        :style {:width (str updates-content-list-width "px")}}
                (dom/div {:class "create-update-content-cta"}
                  "Choose topics you’d like to include and arrange them in any order.")
                (dom/div {:class "create-update-topics-list"
                          :key (clojure.string/join "-" su-topics)}
                  (for [topic su-topics]
                    (let [sd ((keyword topic) company-data)]
                      (dom/div {:class "oc-active"
                               :data-topic topic
                               :key topic
                               :ref topic
                               :on-click #(do
                                            (om/set-state! owner :su-topics (utils/vec-dissoc su-topics topic))
                                            (patch-stakeholder-update owner))}
                        (:title sd))))
                  (let [all-topics (:sections company-data)
                        remaining-topics (vec (first (clojure.data/diff (set all-topics) (set su-topics))))]
                    (for [topic remaining-topics]
                      (let [sd ((keyword topic) company-data)]
                        (dom/div {:data-topic topic
                                  :key topic
                                  :ref topic
                                  :on-click #(do
                                              (om/set-state! owner :su-topics (vec (conj su-topics topic)))
                                              (patch-stakeholder-update owner))}
                          (:title sd))))))
                (dom/div {:class "create-update-content-buttons mt3 center group"}
                  (dom/button {:class "btn-reset btn-solid share"
                               :on-click #(share-clicked owner)
                               :style {:width "180px" :height "40px"}
                               :disabled (zero? (count su-topics))} "SHARE")))
              (dom/div {:class "create-update-content-cards right"
                        :style {:width (str fixed-card-width "px")}}
                (dom/input {:class "create-update-content-cards-title"
                            :type "text"
                            :value su-title
                            :placeholder "Update Title"
                            :on-change #(do
                                          (om/set-state! owner :su-title (.. % -target -value))
                                          (patch-stakeholder-update owner))})
                (om/build topics-columns {:columns-num 1
                                          :card-width (- fixed-card-width 10) ; remove 10 padding on the right
                                          :total-width (- fixed-card-width 10)
                                          :is-stakeholder-update true
                                          :content-loaded (not (:loading data))
                                          :topics su-topics
                                          :topics-data no-pinned-topics
                                          :company-data no-pinned-topics
                                          :hide-add-topic true
                                          :show-share-remove false}))))
          (om/build footer {:card-width card-width
                            :columns-num columns-num
                            :company-data company-data}))))))