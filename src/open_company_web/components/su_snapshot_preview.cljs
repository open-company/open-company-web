(ns open-company-web.components.su-snapshot-preview
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.menu :refer (menu)]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.onboard-tip :refer (onboard-tip)]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.su-preview-dialog :refer (su-preview-dialog)]
            [open-company-web.components.ui.multi-items-input :refer (item-input email-item)]
            [open-company-web.components.ui.emoji-picker :refer (emoji-picker)]
            [open-company-web.components.ui.small-loading :refer (small-loading)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade)]
            [goog.object :as gobj]
            [cljsjs.hammer]
            [cljsjs.react.dom]))

(defn ordered-topics-list []
  (let [topics (sel [:div.topic-row])
        topics-list (for [topic topics] (.data (js/jQuery topic) "topic"))]
    (vec (remove nil? topics-list))))

(defn post-stakeholder-update [owner]
  (om/set-state! owner :share-status :su-posting)
  (let [share-medium (om/get-state owner :share-medium)]
    (cond
      ; post for email: to, subject and note
      (= share-medium :email)
      (let [post-data {:email true
                       :to (-> @dis/app-state :su-share :email :to)
                       :subject (-> @dis/app-state :su-share :email :subject)
                       :note (-> @dis/app-state :su-share :email :note)}]
        (api/share-stakeholder-update post-data))
      ; post for :slack
      (= share-medium :slack)
      (let [post-data {:slack true
                       :note (-> @dis/app-state :su-share :slack :note)}]
        (api/share-stakeholder-update post-data))
      ; post for link
      (= share-medium :link)
      (api/share-stakeholder-update {}))))

(defn stakeholder-update-data [data]
  (:stakeholder-update (dis/company-data data)))

(defn share-clicked [owner]
  (let [patch-data {:title (or (om/get-state owner :title) "")
                    :sections (om/get-state owner :su-topics)}]
    (api/patch-stakeholder-update patch-data))
  (om/set-state! owner :share-status :su-patching))

(defn dismiss-su-preview [owner]
  (om/set-state! owner (merge (om/get-state owner) {:show-su-dialog false})))

(defn setup-sortable [owner options]
  (when-let [list-node (js/jQuery (sel1 [:div.topics-column]))]
    (.sortable list-node #js {:scroll true
                              :forcePlaceholderSize true
                              :placeholder "sortable-placeholder"
                              :items ".topic-row"
                              :axis "y"
                              :start (fn [event ui]
                                        (if-let [dragged-item (gobj/get ui "item")]
                                          (do 
                                            (om/set-state! owner :su-dragging-topic (.data dragged-item "topic"))
                                            (.addClass (js/$ dragged-item) "su-dragging-topic")
                                            (.addClass (js/$ (sel1 [:div.topics-columns])) "sortable-active"))))
                              :stop (fn [event ui]
                                      (if-let [dragged-item (gobj/get ui "item")]
                                        (do
                                          (.removeClass (js/$ dragged-item) "su-dragging-topic")
                                          (.removeClass (js/$ (sel1 [:div.topics-columns])) "sortable-active")
                                          (om/set-state! owner :su-topics (ordered-topics-list))))
                                      (om/set-state! owner :su-dragging-topic nil))
                              :opacity 1})))

(defn add-su-section [owner topic]
  (om/update-state! owner :su-topics #(conj % topic)))

(defn title-from-section-name [owner section]
  (let [company-data (dis/company-data (om/get-props owner))]
    (->> section keyword (get company-data) :title)))

(def topic-row-x-padding 40)

(defn email-note-did-change []
  (let [email-notes (utils/emoji-images-to-unicode (.-innerHTML (sel1 [:.preview-note-field])))]
    (dis/dispatch! [:input
                    [:su-share :email :note]
                    email-notes])))

(defn slack-note-did-change []
  (let [email-notes (.-value (sel1 [:.preview-note-field]))]
    (dis/dispatch! [:input
                    [:su-share :slack :note]
                    email-notes])))

(defcomponent su-snapshot-preview [data owner options]

  (init-state [_]
    (let [company-data (dis/company-data data)
          su-data (stakeholder-update-data data)
          su-sections (if (empty? (:sections su-data))
                        (utils/filter-placeholder-sections (vec (:sections company-data)) company-data)
                        (utils/filter-placeholder-sections (:sections su-data) company-data))]
      {:columns-num (responsive/columns-num)
       :su-topics su-sections
       :title-focused false
       :title (:title su-data)
       :share-medium (keyword (:medium @router/path))
       :share-status nil
       :show-su-dialog false}))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (setup-sortable owner options)
    (js/emojiAutocomplete)
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num))))

  (will-receive-props [_ next-props]
    (when-not (= (dis/company-data data) (dis/company-data next-props))
      (let [company-data (dis/company-data next-props)
            su-data      (stakeholder-update-data next-props)
            su-sections  (if (empty? (:sections su-data))
                           (vec (:sections company-data))
                           (utils/filter-placeholder-sections (:sections su-data) company-data))]
        (om/set-state! owner :su-topics su-sections)))
    (when (= (om/get-state owner :share-status) :su-patching)
      ; post with data
      (post-stakeholder-update owner))
    (when (= (om/get-state owner :share-status) :su-posting)
      ; share post sent, let's show the final dialog
      (om/set-state! owner :share-status :su-posted)
      (om/set-state! owner :show-su-dialog true)))

  (did-update [_ _ _]
    (setup-sortable owner options))

  (render-state [_ {:keys [columns-num
                           title-focused
                           title
                           show-su-dialog
                           share-status
                           su-topics
                           share-medium]}]
    (let [company-slug (router/current-company-slug)
          company-data (dis/company-data data)
          su-data      (stakeholder-update-data data)
          card-width   (responsive/calc-card-width 1)
          ww           (.-clientWidth (.-body js/document))
          title-width  (if (>= ww responsive/c1-min-win-width)
                          (str (if (< ww card-width) ww card-width) "px")
                          "auto")
          fields-width  (if (>= ww responsive/c1-min-win-width)
                           (str (if (< ww card-width) ww (+ card-width 10)) "px")
                           "auto")
          total-width  (if (>= ww responsive/c1-min-win-width)
                          (str (if (< ww card-width) ww (+ card-width (* topic-row-x-padding 2))) "px")
                          "auto")
          su-subtitle  (str "— " (utils/date-string (js/Date.) [:year]))
          possible-sections (utils/filter-placeholder-sections (vec (:sections company-data)) company-data)
          topics-to-add (sort #(compare (title-from-section-name owner %1) (title-from-section-name owner %2)) (reduce utils/vec-dissoc possible-sections su-topics))]
      (dom/div {:class (utils/class-set {:su-snapshot-preview true
                                         :main-scroll true})}
        (when (and (seq company-data)
                   (empty? (:sections su-data)))
          (onboard-tip {
            :id (str "update-preview-" company-slug)
            :once-only true
            :mobile false
            :desktop "This is a preview of your update. You can drag topics to reorder, and you can remove them by clicking the \"X\"."
            :css-class "large"
            :dismiss-tip-fn #(.focus (sel1 [:input#su-snapshot-preview-title]))}))
        (om/build menu data)
        (dom/div {:class "page snapshot-page"}
          (dom/div {:class "su-snapshot-header"}
            (back-to-dashboard-btn {})
            (dom/div {:class "snapshot-cta"} "Choose the topics to share and arrange them in any order.")
            (dom/div {:class "share-su"}
              (dom/button {:class "btn-reset btn-solid share-su-button"
                           :on-click #(share-clicked owner)
                           :disabled (or (zero? (count su-topics))
                                         (and (= share-medium :email)
                                              (or (not (seq (->> data :su-share :email :to)))
                                                  (not (every? utils/valid-email? (->> data :su-share :email :to)))
                                                  (clojure.string/blank? (->> data :su-share :email :subject)))))}
                (when (or (= share-status :su-patching)
                          (= share-status :su-posting))
                  (small-loading))
                "SHARE " (dom/i {:class "fa fa-share"}))))
          ;; SU Snapshot Preview
          (when company-data
            (dom/div {:class "su-sp-content group"
                      :key (apply str su-topics)}
              (dom/div {:class "su-sp-company-header group"}
                (when (= share-medium :slack)
                  (dom/div {:class "preview-note-container"
                            :style #js {:width fields-width}}
                    (dom/label {} "Optional Slack Note")
                    (dom/div {:class "slack-note npt group"}
                      (dom/textarea {:class "domine p1 col-12 ta-mh no-outline slack-note-field"
                                     :value (-> data :su-share :slack :note)
                                     :on-change #(slack-note-did-change)}))))
                (when (not= share-medium :email)
                  (dom/div {:class "company-logo-container group"}
                    (dom/img {:class "company-logo" :src (:logo company-data)})))
                (when (and (:title su-data) (not= share-medium :email))
                  (dom/div {:class "preview-field-container group"
                            :style #js {:width fields-width}}
                    (dom/input #js {:className "preview-title"
                                    :id "su-snapshot-preview-title"
                                    :type "text"
                                    :value title
                                    :ref "preview-title"
                                    :placeholder "Title of this Update"
                                    :onChange #(om/set-state! owner :title (.. % -target -value))})))
                (when (= share-medium :email)
                  (dom/div {:class "preview-field-container group"
                            :style #js {:width fields-width}}
                    (dom/div {:class "preview-field-container-inner"}
                      (dom/label {} "To")
                      (dom/div {:class "preview-to"}
                        (item-input {:item-render email-item
                                     :match-ptn #"(\S+)[,|\s]+"
                                     :split-ptn #"[,|\s]+"
                                     :container-node :div.npt.pt1.pr1.pl1.mh4.overflow-auto.preview-to-field
                                     :input-node :input.border-none.outline-none.mr.mb1
                                     :valid-item? utils/valid-email?
                                     :on-change (fn [val] (dis/dispatch! [:input [:su-share :email :to] val]))})))
                    (when-let [to-field (->> data :su-share :email :to)]
                      (cond
                        (not (seq to-field))
                        (dom/span {:class "left red pt1 ml1"} "Required")
                        (not (every? utils/valid-email? to-field))
                        (dom/span {:class "left red pt1 ml1"} "Not a valid email address")))
                    (dom/hr {:class "separator"})))
                (when (= share-medium :email)
                  (dom/div {:class "preview-field-container group"
                            :style #js {:width fields-width}}
                    (dom/div {:class "preview-field-container-inner"}
                      (dom/label {} "Subject")
                      (dom/div {:class "preview-subject"}
                        (dom/input #js {:className "preview-subject-field"
                                        :type "text"
                                        :value (-> data :su-share :email :subject)
                                        :onChange #(dis/dispatch! [:input [:su-share :email :subject] (.-value (sel1 [:input.preview-subject-field]))])})))
                    (dom/hr {:class "separator"})))
                (when (= share-medium :email)
                  (dom/div {:class "preview-note-container"
                              :style #js {:width fields-width}}
                    (dom/label {} "Optional Note")
                    (when (= share-medium :email)
                      (dom/div {:class "email-note group"}
                        (dom/div
                          {:class "domine p1 col-12 emoji-autocomplete ta-mh no-outline emojiable preview-note-field"
                           :content-editable true
                           :on-key-down #(email-note-did-change)
                           :on-key-up #(email-note-did-change)
                           :placeholder "Optional note to go with this update."})
                        (dom/div
                          {:class "group"
                           :style #js {:minHeight "25px"}}
                          (dom/div
                           {:class "left"
                            :style #js {:color "rgba(78, 90, 107, 0.5)"}}
                            (emoji-picker {:add-emoji-cb #(email-note-did-change)}))))))))
              (when show-su-dialog
                (om/build su-preview-dialog {:latest-su (dis/latest-stakeholder-update)
                                             :share-via share-medium}
                                            {:opts {:dismiss-su-preview #(dismiss-su-preview owner)}}))
              (om/build topics-columns {:columns-num 1
                                        :card-width card-width
                                        :total-width total-width
                                        :is-stakeholder-update true
                                        :content-loaded (not (:loading data))
                                        :topics su-topics
                                        :su-dragging-topic (om/get-state owner :su-dragging-topic)
                                        :su-medium share-medium
                                        :company-data company-data
                                        :show-share-remove true
                                        :topics-data company-data
                                        :hide-add-topic true}
                                       {:opts {:share-remove-click (fn [topic]
                                                                      (let [fade-anim (Fade. (sel1 [(str "div#topic-" topic)]) 1 0 utils/oc-animation-duration)]
                                                                        (doto fade-anim
                                                                          (events/listen AnimationEventType/FINISH
                                                                            (fn [] (om/set-state! owner :su-topics (utils/vec-dissoc (om/get-state owner :su-topics) topic))))
                                                                          (.play))))}})))
          ;; Add section container
          (when (pos? (count topics-to-add))
            (dom/div {:class "su-preview-add-section-container"}
              (dom/div {:class "su-preview-add-section"
                        :style #js {:width total-width}}
                (dom/div {:class "add-header"} "Topics You Can Add to this Update")
                (for [topic topics-to-add
                      :let [title (->> topic keyword (get company-data) :title)]]
                  (dom/div {:class "add-section"
                            :on-click #(add-su-section owner topic)}
                    (i/icon :check-square-09 {:accent-color "transparent" :size 16 :color "black"})
                    (dom/div {:class "section-name"} title)))))))))))