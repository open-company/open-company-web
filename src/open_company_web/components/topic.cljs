(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.topic-edit :refer (topic-edit)]
            [open-company-web.components.ui.icon :as i]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]
            [goog.style :as gstyle]
            [cljsjs.react.dom]))

(defn time-ago
  [past-date]
  (let [past-js-date (utils/js-date past-date)
        past (.getTime past-js-date)
        now (.getTime (utils/js-date))
        seconds (.floor js/Math (/ (- now past) 1000))
        minutes-interval (.floor js/Math (/ seconds 60))
        hours-interval (.floor js/Math (/ seconds 3600))
        days-interval (.floor js/Math (/ seconds 86400))
        weeks-interval (.floor js/Math (/ seconds 604800))
        months-interval (.floor js/Math (/ seconds 2592000))
        years-interval (.floor js/Math (/ seconds 31536000))]
    (cond
      (> months-interval 24)
      (str years-interval " " (utils/pluralize "year" years-interval) " ago")

      (> weeks-interval 8)
      (str months-interval " " (utils/pluralize "month" months-interval) " ago")

      (> days-interval 14)
      (str weeks-interval " " (utils/pluralize "week" weeks-interval) " ago")

      (> hours-interval 24)
      (str days-interval " " (utils/pluralize "day" days-interval) " ago")

      (> minutes-interval 60)
      (str hours-interval " " (utils/pluralize "hour" hours-interval) " ago")

      (> minutes-interval 1)
      (str minutes-interval " " (utils/pluralize "minute" minutes-interval) " ago")

      :else
      "just now")))

(defcomponent topic-image-header [{:keys [image-header image-size]} owner options]
  (render [_]
    (dom/img {:src image-header
              :class "topic-header-img"})))

(defcomponent topic-headline [data owner]
  (render [_]
    (dom/div {:class "topic-headline-inner"
              :dangerouslySetInnerHTML (utils/emojify (:headline data))})))

(defn scroll-to-topic-top [topic]
  (let [body-scroll (.-scrollTop (.-body js/document))
        topic-scroll-top (utils/offset-top topic)]
    (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90))))

(defn fullscreen-topic [data selected-metric force-editing & [e]]
  (when e
    (utils/event-stop e))
  ((:topic-click data) selected-metric force-editing))

(defn start-foce-click [owner]
  (let [section-kw (keyword (om/get-props owner :section))
        company-data (dis/company-data)
        section-data (get company-data section-kw)]
    (dis/dispatch! [:start-foce section-kw section-data])))

(defn pencil-click [owner e]
  (utils/event-stop e)
  (let [section (om/get-props owner :section)]
    (if (#{:growth :finances} (keyword section))
      (fullscreen-topic (om/get-props owner) nil true)
      (start-foce-click owner))))

(defcomponent topic-internal [{:keys [topic-data
                                      section
                                      currency
                                      card-width
                                      prev-rev
                                      next-rev
                                      sharing-mode
                                      show-fast-editing] :as data} owner options]

  (init-state [_]
    {:force-update 0
     :self-update nil})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
      ; force a rerender every minute
      (om/set-state! owner :self-update (js/setInterval #(om/update-state! owner :force-update inc) (* 60 1000)))))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; clear self update
      (js/clearTimeout (om/get-state owner :self-update))))

  (render-state [_ {:keys [force-update]}]
    (let [section-kw          (keyword section)
          chart-opts          {:chart-size {:width  260
                                            :height 196}
                               :hide-nav true
                               :pillboxes-first false
                               :topic-click (partial fullscreen-topic data nil false)}
          is-growth-finances? (#{:growth :finances} section-kw)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          finances-row-data   (:data topic-data)
          growth-data         (growth-utils/growth-data-map (:data topic-data))
          no-data             (or (and (= section-kw :finances)
                                       (or (empty? finances-row-data)
                                        (utils/no-finances-data? finances-row-data)))
                                  (and (= section-kw :growth)
                                       (utils/no-growth-data? growth-data)))
          image-header        (:image-url topic-data)
          image-header-size   {:width (:image-width topic-data)
                               :height (:image-height topic-data)}
          topic-body          (utils/get-topic-body topic-data section-kw)
          truncated-body      (if (utils/is-test-env?) topic-body (.truncate js/$ topic-body (clj->js {:length 500 :words true})))]
      (dom/div #js {:className "topic-internal group"
                    :onClick (partial fullscreen-topic data nil false)
                    :ref "topic-internal"}
        (when (or is-growth-finances?
                  image-header)
          (dom/div {:class (utils/class-set {:card-header true
                                             :card-image (not is-growth-finances?)})}
            (cond
              (= section "finances")
              (om/build topic-finances {:section-data topic-data :section section :currency currency} {:opts chart-opts})
              (= section "growth")
              (om/build topic-growth {:section-data topic-data :section section :currency currency} {:opts chart-opts})
              :else
              (om/build topic-image-header {:image-header image-header :image-size image-header-size} {:opts options}))))
        ;; Topic title
        (dom/div {:class "group"}
          (dom/div {:class "topic-title"} (:title topic-data))
          (when (and show-fast-editing
                   (responsive/can-edit?)
                   (not (responsive/is-mobile))
                   (not (:read-only topic-data))
                   (not sharing-mode))
            (dom/button {:class (str "topic-pencil-button btn-reset")
                         :on-click #(pencil-click owner %)}
              (dom/i {:class "fa fa-pencil"}))))
        ;; Topic headline
        (when-not (clojure.string/blank? (:headline topic-data))
          (om/build topic-headline topic-data))
        (dom/div #js {:className "topic-body topic-body"
                      :ref "topic-body"
                      :dangerouslySetInnerHTML (utils/emojify truncated-body)})
        (dom/div {:class "topic-attribution-container group"}
          (dom/div {:class "topic-navigation"}
            (when (:prev-rev data)
              (dom/button {:class "topic-navigation-button"
                           :title "View earlier update"
                           :type "button"
                           :data-toggle (if (:prev-rev data) "tooltip" nil)
                           :data-placement "top"
                           :on-click #(when (:prev-rev data) ((:rev-click options) % (:prev-rev data)))}
                (dom/i {:class "fa fa-caret-left"})))
            (dom/div {:class "topic-attribution"
                    :data-toggle "tooltip"
                    :data-placement "right"
                    :title (:name (:author topic-data))}
            (time-ago (:updated-at topic-data)))
            (when (:next-rev data)
              (dom/button {:class "topic-navigation-button"
                           :title "View later update"
                           :type "button"
                           :data-toggle (if (:next-rev data) "tooltip" nil)
                           :data-placement "top"
                           :on-click #(when (:next-rev data) ((:rev-click options) % (:next-rev data)))}
                (dom/i {:class "fa fa-caret-right"}))))
          (when (> (count (utils/strip-HTML-tags topic-body)) 500)
            (dom/button {:class "btn-reset topic-read-more"
                         :onClick (partial fullscreen-topic data nil false)} "READ MORE")))))))

(defn animate-revision-navigation [owner]
  (let [cur-topic (om/get-ref owner "cur-topic")
        tr-topic (om/get-ref owner "tr-topic")
        current-state (om/get-state owner)
        appear-animation (Fade. tr-topic 0 1 utils/oc-animation-duration)
        cur-size (js/getComputedStyle cur-topic)
        tr-size (js/getComputedStyle tr-topic)
        topic (om/get-ref owner "topic-anim")
        topic-size (js/getComputedStyle topic)
        scroll-top (.-scrollTop topic)]
    ; resize the light box
    (.play (Resize. topic
                    #js [(js/parseFloat (.-width topic-size)) (js/parseFloat (.-height cur-size))]
                    #js [(js/parseFloat (.-width topic-size)) (js/parseFloat (.-height tr-size))]
                    utils/oc-animation-duration))
    ; disappear current topic
    (.play (Fade. cur-topic 1 0 utils/oc-animation-duration))
    ; appear the new topic
    (doto appear-animation
      (events/listen
        EventType/FINISH
        #(om/set-state! owner (merge current-state
                                    {:as-of (:transition-as-of current-state)
                                     :transition-as-of nil})))
      (.play))))

(defn get-all-sections [slug]
  (let [categories-data (:categories (slug @caches/new-sections))
        all-category-sections (apply concat
                                     (for [category categories-data]
                                       (let [cat-name (:name category)
                                             sections (:sections category)]
                                         (map #(assoc % :category cat-name) sections))))]
    (apply merge
           (map #(hash-map (keyword (:section-name %)) %) all-category-sections))))

(defcomponent topic [{:keys [active-topics
                             section-data
                             section
                             currency
                             column
                             card-width
                             sharing-mode
                             share-selected
                             archived-topics
                             show-share-remove] :as data} owner options]

  (init-state [_]
    {:as-of (:updated-at section-data)
     :actual-as-of (:updated-at section-data)
     :transition-as-of nil})

  (will-update [_ next-props _]
    (let [new-as-of (:updated-at (:section-data next-props))
          current-as-of (om/get-state owner :as-of)
          old-as-of (:updated-at section-data)]
      (when (and (not= old-as-of new-as-of)
                 (not= current-as-of new-as-of))
        (om/set-state! owner :as-of new-as-of)
        (om/set-state! owner :actual-as-of new-as-of))))

  (did-update [_ prev-props _]
    (when (om/get-state owner :transition-as-of)
      (animate-revision-navigation owner)))

  (render-state [_ {:keys [editing as-of actual-as-of transition-as-of] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (router/current-company-slug))
          all-revisions (slug @caches/revisions)
          revisions-list (section-kw all-revisions)
          topic-data (utils/select-section-data section-data section-kw as-of)
          is-foce (= (dis/foce-section-key) section-kw)]
      ;; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ;; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div #js {:className (utils/class-set {:topic true
                                                 :group true
                                                 :sharing-selected (and sharing-mode share-selected)})
                    :ref "topic"
                    :id (str "topic-" (name section))
                    :onClick #(when (and (:topic-click options) (not is-foce))
                                ((:topic-click options) nil false))}
        (when show-share-remove
          (dom/div {:class "share-remove-container"
                    :id (str "share-remove-" (name section))}
            (dom/button {:class "btn-reset share-remove"
                         :on-click #(when (contains? options :share-remove-click) ((:share-remove-click options) (name section)))}
              (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.5)" :size 12 :stroke 4 :accent-color "rgba(78, 90, 107, 0.5)"}))))
        (dom/div #js {:className "topic-anim group"
                      :key (str "topic-anim-" as-of "-" transition-as-of)
                      :ref "topic-anim"}
          (dom/div #js {:className "topic-as-of group"
                        :ref "cur-topic"
                        :key (str "cur-" as-of)
                        :style #js {:opacity 1 :width "100%" :height "auto"}}
            (if is-foce
              (om/build topic-edit {:section section
                                    :topic-data topic-data
                                    :sharing-mode sharing-mode
                                    :show-fast-editing (:show-fast-editing data)
                                    :currency currency
                                    :card-width card-width
                                    :read-only-company (:read-only-company data)
                                    :foce-key (:foce-key data)
                                    :foce-data (:foce-data data)
                                    :prev-rev prev-rev
                                    :next-rev next-rev}
                                   {:opts (merge options {:rev-click (fn [e rev]
                                                                        (scroll-to-topic-top (om/get-ref owner "topic"))
                                                                        (om/set-state! owner :transition-as-of (:updated-at rev))
                                                                        (.stopPropagation e))})
                                    :key (str "topic-foce-" section)})
              (om/build topic-internal {:section section
                                        :topic-data topic-data
                                        :sharing-mode sharing-mode
                                        :show-fast-editing (:show-fast-editing data)
                                        :currency currency
                                        :card-width card-width
                                        :read-only-company (:read-only-company data)
                                        :topic-click (:topic-click options)
                                        :prev-rev prev-rev
                                        :next-rev next-rev}
                                       {:opts (merge options {:rev-click (fn [e rev]
                                                                            (scroll-to-topic-top (om/get-ref owner "topic"))
                                                                            (om/set-state! owner :transition-as-of (:updated-at rev))
                                                                            (.stopPropagation e))})
                                        :key (str "topic-" section)}))
            (when transition-as-of
              (dom/div #js {:className "topic-tr-as-of group"
                            :ref "tr-topic"
                            :key (str "tr-" transition-as-of "-expanded")
                            :style #js {:opacity 1}}
                (let [tr-topic-data (utils/select-section-data section-data section-kw transition-as-of)
                      tr-prev-rev (utils/revision-prev revisions transition-as-of)
                      tr-next-rev (utils/revision-next revisions transition-as-of)]
                  (om/build topic-internal {:section section
                                            :topic-data tr-topic-data
                                            :sharing-mode sharing-mode
                                            :currency currency
                                            :read-only-company (:read-only-company data)
                                            :prev-rev tr-prev-rev
                                            :next-rev tr-next-rev}
                                           {:opts (merge options {:rev-click #()})}))))))))))