(ns oc.web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.oc-colors :as oc-colors]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.chart :refer (chart)]
            [oc.web.components.topic-edit :refer (topic-edit)]
            [oc.web.components.topic-attachments :refer (topic-attachments)]
            [oc.web.components.ui.popover :refer (add-popover hide-popover)]
            [oc.web.components.growth.topic-growth :refer (topic-growth)]
            [oc.web.components.finances.topic-finances :refer (topic-finances)]
            [goog.events.EventType :as EventType]
            [goog.events :as events]
            [cljsjs.react.dom]))

(defcomponent topic-image-header [{:keys [image-header image-size]} owner options]
  (render [_]
    (dom/img {:src image-header
              :class "topic-header-img"})))

(defcomponent topic-headline [data owner]
  (render [_]
    (dom/div #js {:className (str "topic-headline-inner group" (when (:placeholder data) " italic"))
                  :dangerouslySetInnerHTML (utils/emojify (:headline data))})))

(defn html-text-exceeds-limit [html limit]
  (> (count (.text (.html (js/$ "<div/>") html))) limit))

(defn start-foce-click [owner]
  (let [topic-kw (keyword (om/get-props owner :topic))
        topic-data (om/get-props owner :topic-data)]
    (dis/dispatch! [:foce-start topic-kw (assoc topic-data :topic (name topic-kw))])))

(defn get-author-string
  "Return the a formatted string that shows the topic creator and the last editor."
  [topic-data]
  (cond
    ; if there is only one author it means this was never changed
    (= (count (:author topic-data)) 1)
    (let [first-author (first (:author topic-data))]
      (str "by " (:name first-author) " on " (utils/date-string (utils/js-date (:updated-at first-author)) [:year])))
    ; if there are more than one record in author it means it was edited
    (> (count (:author topic-data) 1))
    (let [first-author (first (:author topic-data))
          last-author  (last (:author topic-data))]
      (if (= (:user-id first-author) (:user-id last-author))
        ; if the last editor is the same of the first don't repeat the name
        (str "by " (:name first-author) " on " (utils/date-string (utils/js-date (:updated-at first-author)) [:year]) "\n"
             "edited " (utils/date-string (utils/js-date (:updated-at last-author)) [:year]))
        ; if the last editor is different than the creator shows the name and the date of the ditor too
        (str "by " (:name first-author) " on " (utils/date-string (utils/js-date (:updated-at first-author)) [:year]) "\n"
             "edited by " (:name last-author) " on " (utils/date-string (utils/js-date (:updated-at last-author)) [:year]))))))

(defn- assign-topic-click []
  (add-popover {:container-id "assign-topic-wip"
                :message "Coming soon. Assign someone to keep this topic up to date."
                :height "160px"
                :success-title "Ok, got it"
                :success-cb #(do
                              (dis/dispatch! [:top-menu-show nil])
                              (hide-popover nil "assign-topic-wip"))
                :cancel-title nil
                :cancel-cb nil}))

(defcomponent topic-internal [{:keys [topic-data
                                      entries-data
                                      topic
                                      currency
                                      card-width
                                      columns-num
                                      read-only-company
                                      is-stakeholder-update
                                      is-mobile?
                                      is-dashboard
                                      dashboard-sharing
                                      is-topic-view
                                      foce-active
                                      show-editing
                                      column
                                      show-top-menu] :as data} owner options]

  (did-mount [_]
    (when (and is-topic-view
               (not is-mobile?))
      (dis/dispatch! [:comments-get (:uuid topic-data)])))

  (render [_]
    (let [topic-kw          (keyword topic)
          chart-opts          {:chart-size {:width 230}
                               :hide-nav true}
          is-growth-finances? (#{:growth :finances} topic-kw)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          image-header        (:image-url topic-data)
          image-header-size   {:width (:image-width topic-data)
                               :height (:image-height topic-data)}
          topic-body          (if (:placeholder topic-data) (:body-placeholder topic-data) (:body topic-data))
          fixed-column        (js/parseInt column)
          should-truncate-text (and (not (utils/is-test-env?))
                                    is-dashboard
                                    (html-text-exceeds-limit topic-body utils/topic-body-limit))
          truncated-body      (if should-truncate-text
                                 (.truncate js/$ topic-body (clj->js {:length utils/topic-body-limit :words true}))
                                 topic-body)
          showing-menu        (= show-top-menu topic)
          attachments         (:attachments topic-data)
          comments-link       (utils/link-for (:links topic-data) "comments")
          should-show-comments-button (and comments-link
                                           show-editing
                                           (not is-stakeholder-update)
                                           (not is-dashboard)
                                           (not is-mobile?)
                                           is-topic-view
                                           (not foce-active))]
      (dom/div #js {:className "topic-internal group"
                    :key (str "topic-internal-" (name topic))
                    :ref "topic-internal"}

        ;; Topic image for dashboard
        (when (and image-header
                   (or (not is-mobile?)
                       (not is-dashboard))
                   (not is-mobile?)
                   is-dashboard)
          (dom/div {:class "card-header card-image"}
            (om/build topic-image-header {:image-header image-header :image-size image-header-size} {:opts options})))

        ;; Topic title
        (dom/div {:class "topic-title-container group"}
          (when (and is-dashboard
                     (not (responsive/is-tablet-or-mobile?)))
            (dom/div {:class (utils/class-set {:topic-top-menu true
                                               :active showing-menu
                                               :left-column (not= fixed-column columns-num)
                                               :right-column (= fixed-column columns-num)})}
              (dom/button {:class "topic-top-menu-btn btn-reset"
                           :on-click #(dis/dispatch! [:foce-start topic-kw {:placeholder true
                                                                            :topic (name topic-kw)
                                                                            :title (:title topic-data)
                                                                            :links (:links topic-data)}])}
                (dom/i {:class "fa fa-plus"})" New entry")
              (dom/button {:class "topic-top-menu-btn btn-reset"
                           :on-click #(dis/dispatch! [:foce-start topic-kw topic-data])}
                (dom/i {:class "fa fa-pencil"}) " Edit")
              ; Assign action, disabled for now
              (dom/button {:class "topic-top-menu-btn btn-reset"
                           :on-click #(assign-topic-click)}
                (dom/i {:class "fa fa-user"}) " Assign")))
          (chart topic-data (- card-width (* 16 2)))
          (dom/div {:class (utils/class-set {:topic-title true
                                             :has-comments should-show-comments-button})}

            (when-not (and is-topic-view
                       is-mobile?)
              (:title topic-data))
            (when (or is-topic-view
                      (and (not is-mobile?)
                           is-dashboard))
              (dom/span {:data-toggle "tooltip"
                         :data-placement (if (= fixed-column 1)
                                            "right"
                                            (if (or (and (= columns-num 2)
                                                         (= fixed-column 2))
                                                    (= fixed-column 3))
                                              "left"
                                              "top"))
                         :data-container "body"
                         :key (str "tt-attrib-" (get-author-string topic-data))
                         :title (get-author-string topic-data)}
                (when-not (and is-topic-view
                               is-mobile?)
                  " · ")
                (utils/time-since (if is-topic-view (:created-at topic-data) (:updated-at topic-data)) [:short-month])))
            (when (and is-dashboard
                       (not is-mobile?)
                       (or (> (count entries-data) 1)
                           (> (:count (utils/link-for (:links topic-data) "collection")) 1)))
              (dom/button {:class "topic-history-button btn-reset"
                           :data-placement "top"
                           :data-container "body"
                           :data-toggle "tooltip"
                           :title "This topic has prior history"}
                (dom/i {:class "fa fa-history"}))))
          (when (and show-editing
                     (not is-stakeholder-update)
                     (not is-dashboard)
                     (not is-mobile?)
                     is-topic-view
                     (responsive/can-edit?)
                     (not (:read-only topic-data))
                     (not read-only-company)
                     (not foce-active))
            (dom/button {:class "top-right-button topic-pencil-button btn-reset"
                         :on-click #(if is-dashboard
                                      (router/nav! (oc-urls/topic (router/current-org-slug) (router/current-board-slug) topic-kw))
                                      (start-foce-click owner))}
              (dom/i {:class "fa fa-pencil"
                      :title "Edit"
                      :data-toggle "tooltip"
                      :data-container "body"
                      :data-placement "top"})))
          (when should-show-comments-button
            (dom/button {:class "top-right-button topic-comments-button btn-reset"
                         :on-click #(dis/dispatch! [:comments-show topic-kw (:uuid topic-data)])
                         :title "Comments"
                         :data-toggle "tooltip"
                         :data-container "body"
                         :data-placement "top"}
              (dom/i {:class "fa fa-comments-o"})
              (str "(" (:count comments-link) ")")))
          (when (and show-editing
                     (not is-stakeholder-update)
                     is-dashboard
                     (not dashboard-sharing)
                     (not is-mobile?)
                     (not is-topic-view)
                     (responsive/can-edit?)
                     (not (:read-only topic-data))
                     (not read-only-company)
                     (not foce-active)
                     (not (responsive/is-tablet-or-mobile?)))
            (dom/button {:class "top-right-button topic-top-menu-button btn-reset"
                         :on-click #(do
                                      (utils/event-stop %)
                                      (dis/dispatch! [:top-menu-show (if showing-menu nil topic)]))}
              (dom/i {:class "fa fa-ellipsis-v"}))))

        ;; Topic image for dashboard
        (when (and image-header
                   (or is-stakeholder-update
                       is-topic-view))
          (dom/div {:class "card-header card-image"}
            (om/build topic-image-header {:image-header image-header :image-size image-header-size} {:opts options})))

        ;; Topic headline
        (when (and is-topic-view
                   (not (clojure.string/blank? (:headline topic-data))))
          (om/build topic-headline topic-data))

        ;; Topic data
        (when (and (or (not is-dashboard)
                       (not is-mobile?))
                   is-growth-finances?
                   (utils/data-topic-has-data topic topic-data))
          (dom/div {:class ""}
            (cond
              (= topic-kw :growth)
              (om/build topic-growth {:topic-data topic-data
                                      :topic topic
                                      :card-width card-width
                                      :columns-num columns-num
                                      :currency currency} {:opts chart-opts})
              (= topic-kw :finances)
              (om/build topic-finances {:topic-data (utils/fix-finances topic-data)
                                        :topic topic
                                        :card-width card-width
                                        :columns-num columns-num
                                        :currency currency} {:opts chart-opts}))))

        ;; Topic headline
        (when (and (or (not is-mobile?) (not is-dashboard))
                   (not is-topic-view)
                   (not (clojure.string/blank? (:headline topic-data))))
          (om/build topic-headline topic-data))

        ;; Attribution for topic
        (when (and is-mobile? is-dashboard)
          (dom/div {:class "mobile-date"}
            (utils/time-since (:created-at topic-data))))
        
        ;; Topic body
        (when (and (or (not is-dashboard)
                       (not is-mobile?))
                   (not (clojure.string/blank? truncated-body)))
          (dom/div {:class "group" :style #js {:position "relative"}}
            (when (and is-dashboard (>= (count (.text (.html (js/$ "<div/>") (:body topic-data)))) utils/topic-body-limit))
              (dom/div {:class "topic-body-fade"}))
            (dom/div #js {:className (utils/class-set {:topic-body true
                                                       :italic (:placeholder topic-data)})
                          :ref "topic-body"
                          :dangerouslySetInnerHTML (utils/emojify truncated-body)})))

        ;; Attachments
        (topic-attachments attachments nil)))))

(defn anchor-clicked? [e]
  (loop [element (.-target e)]
    (if element
      (if (= (.-tagName element) "A")
        (if (empty? (.-href element))
          false
          true)
        (recur (.-parentElement element)))
      false)))

(defn topic-click [e data board-slug]
  (when (and (nil? (:foce-key data))
             (or (responsive/is-mobile-size?)
                 (not (:read-only-company data))
                 (> (count (:entries-data data)) 1)
                 (html-text-exceeds-limit (:body (:topic-data data)) utils/topic-body-limit))
             (not (anchor-clicked? e)))
    (router/nav! (oc-urls/topic (keyword (router/current-org-slug)) board-slug (keyword (:topic data))))))

(defcomponent topic [{:keys [active-topics
                             topic-data
                             entries-data
                             topic
                             currency
                             column
                             card-width
                             columns-num
                             archived-topics
                             is-stakeholder-update
                             is-dashboard
                             is-topic-view
                             foce-key
                             foce-data
                             foce-data-editing?
                             show-editing
                             dashboard-selected-topics
                             topic-flex-num
                             read-only-company] :as data} owner options]

  (init-state [_]
    {:window-width (responsive/ww)})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      ; initialize bootstrap tooltips
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :window-width (responsive/ww)))))

  (did-update [_ _ _]
    (when-not (utils/is-test-env?)
      ; initialize bootstrap tooltips
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render-state [_ {:keys [editing as-of window-width] :as state}]
    (let [topic-kw (keyword topic)
          org-slug (keyword (router/current-org-slug))
          board-data (:board-data data)
          board-slug (:slug board-data)
          foce-active (not (nil? foce-key))
          is-current-foce (or (and (= foce-key topic-kw) (= (:created-at foce-data) (:created-at topic-data)))
                              (and is-dashboard (= foce-key topic-kw) (= (:created-at foce-data) nil)))
          is-mobile? (responsive/is-mobile-size?)
          with-order (if (contains? data :topic-flex-num) {:order topic-flex-num} {})
          topic-style (clj->js (if (or (utils/in? (:route @router/path) "su-snapshot-preview")
                                       (utils/in? (:route @router/path) "su-snapshot")
                                       (utils/in? (:route @router/path) "updates-list")
                                       (responsive/is-mobile-size?))
                                 with-order
                                 (merge with-order {:width (if (responsive/is-tablet-or-mobile?) "auto" (str card-width "px"))})))]
      (dom/div #js {:className (utils/class-set {:topic true
                                                 :group true
                                                 :mobile-dashboard-topic (and is-mobile? is-dashboard)
                                                 :no-tablet (not (responsive/is-tablet-or-mobile?))
                                                 :topic-edit is-current-foce
                                                 :sticky-borders (or (= (:show-top-menu data) topic) (and is-dashboard
                                                                                                            is-current-foce))
                                                 :dashboard-topic is-dashboard
                                                 :dashboard-selected (pos? (count (filter #(and (= (:slug board-data) (:board-slug %)) (= topic-kw (:topic-slug %))) dashboard-selected-topics)))
                                                 :dashboard-share-mode (:dashboard-sharing data)
                                                 :selectable-topic (and (not is-current-foce)
                                                                        (nil? (:show-top-menu data))
                                                                        (or (not read-only-company)
                                                                            (html-text-exceeds-limit (:body topic-data) utils/topic-body-limit)
                                                                            (and read-only-company
                                                                                 (> (count entries-data) 1))))
                                                 :no-foce (or (and (not (nil? (:show-top-menu data)))
                                                                   (not= (:show-top-menu data) topic))
                                                              (and foce-active (not is-current-foce)))})
                    :onClick #(when (and is-dashboard
                                         (not is-current-foce)
                                         (nil? (:show-top-menu data)))
                                (if (:dashboard-sharing data)
                                  (dis/dispatch! [:dashboard-select-topic board-slug topic-kw])
                                  (topic-click % data board-slug)))
                    :style topic-style
                    :ref "topic"
                    :data-topic (name topic)
                    :key (str "topic-" (when is-current-foce "foce-") (name topic) "-" (:created-at topic-data))
                    :id (str "topic-" (name topic) "-" (:created-at topic-data))}
        (if is-current-foce
          (om/build topic-edit {:topic topic
                                :topic-data topic-data
                                :is-stakeholder-update is-stakeholder-update
                                :currency currency
                                :card-width card-width
                                :foce-data-editing? foce-data-editing?
                                :read-only-company read-only-company
                                :foce-key foce-key
                                :foce-data foce-data
                                :show-delete-entry-button (:show-delete-entry-button data)
                                :prevent-topic-not-found-navigation (:prevent-topic-not-found-navigation data)
                                :columns-num columns-num}
                               {:opts options
                                :key (str "topic-foce-" topic "-" (:created-at topic-data))})
          (om/build topic-internal {:topic topic
                                    :topic-data topic-data
                                    :entries-data entries-data
                                    :is-stakeholder-update (:is-stakeholder-update data)
                                    :currency currency
                                    :card-width card-width
                                    :columns-num columns-num
                                    :read-only-company (:read-only-company data)
                                    :foce-active foce-active
                                    :is-mobile? is-mobile?
                                    :is-dashboard is-dashboard
                                    :dashboard-sharing (:dashboard-sharing data)
                                    :is-topic-view is-topic-view
                                    :show-editing show-editing
                                    :column column
                                    :show-top-menu (:show-top-menu data)
                                    :comments-open (:comments-open data)}
                                   {:opts options
                                    :key (str "topic-" topic)}))))))