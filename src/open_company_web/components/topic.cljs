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
            [open-company-web.components.ui.add-topic-popover :refer (add-topic-popover)]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]
            [goog.style :as gstyle]
            [cljsjs.react.dom]))

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

(defn pencil-click [owner options e]
  (utils/event-stop e)
  (when-not (om/get-props owner :add-topic)
    (let [section (om/get-props owner :section)
          topic-click-cb (:topic-click options)]
      (topic-click-cb nil true))))

(defcomponent topic-internal [{:keys [topic-data
                                      section
                                      currency
                                      prev-rev
                                      next-rev
                                      add-topic
                                      sharing-mode
                                      show-fast-editing]} owner options]

  (render [_]
    (let [section-kw          (keyword section)
          chart-opts          {:chart-size {:width  260
                                            :height 196}
                               :hide-nav true
                               :pillboxes-first false
                               :topic-click (:topic-click options)}
          is-growth-finances? (#{:growth :finances} section-kw)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          finances-row-data   (:data topic-data)
          growth-data         (growth-utils/growth-data-map (:data topic-data))
          no-data             (or (and (= section-kw :finances)
                                       (or (empty? finances-row-data)
                                        (utils/no-finances-data? finances-row-data)))
                                  (and (= section-kw :growth)
                                       (utils/no-growth-data? growth-data)))
          snippet             (:snippet topic-data)
          image-header        (:image-url topic-data)
          image-header-size   {:width (:image-width topic-data)
                               :height (:image-height topic-data)}
          topic-body          (utils/get-topic-body topic-data section)]
      (dom/div #js {:className "topic-internal group"
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
        (dom/div {:class "topic-title"} (:title topic-data))
        (when (and show-fast-editing
                   (not add-topic)
                   (responsive/can-edit?)
                   (not (responsive/is-mobile))
                   (not (:read-only topic-data))
                   (not sharing-mode))
          (dom/button {:class "topic-pencil-button btn-reset"
                       :on-click (partial pencil-click owner options)}
            (i/icon :pencil {:size 16
                             :color gray-color
                             :accent-color gray-color})))
        ;; Topic headline
        (when-not (clojure.string/blank? (:headline topic-data))
          (om/build topic-headline topic-data))
        (dom/div #js {:className "topic-body topic-snippet"
                      :ref "topic-snippet"
                      :dangerouslySetInnerHTML (utils/emojify snippet)})
        (when-not (clojure.string/blank? topic-body)
          (dom/div #js {:className "topic-read-more"} "READ MORE"))))))

(defn topic-click [section-kw options selected-metric]
  ; ((:topic-click options) selected-metric)
  (dis/dispatch! [:start-foce section-kw]))

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

(defn add-topic [owner]
  (when-not (om/get-state owner :show-add-topic-popover)
    (om/set-state! owner :show-add-topic-popover true)))

(defn get-all-sections [slug]
  (let [categories-data (:categories (slug @caches/new-sections))
        all-category-sections (apply concat
                                     (for [category categories-data]
                                       (let [cat-name (:name category)
                                             sections (:sections category)]
                                         (map #(assoc % :category cat-name) sections))))]
    (apply merge
           (map #(hash-map (keyword (:section-name %)) %) all-category-sections))))

(def popover-max-height 312)

(defn show-popover-above? [owner]
  (let [scroll             (.-scrollTop (.-body js/document))
        win-height        (- (.-clientHeight (.-documentElement js/document)) (.-clientHeight (sel1 [:nav.oc-navbar])) 4)
        popover-offsettop (.-offsetTop (om/get-ref owner "topic"))
        add-topic-pos     (- popover-offsettop scroll)]
    (> add-topic-pos (/ win-height 2))))

(defcomponent topic [{:keys [active-topics section-data section currency column sharing-mode share-selected archived-topics] :as data} owner options]

  (init-state [_]
    {:as-of (:updated-at section-data)
     :actual-as-of (:updated-at section-data)
     :transition-as-of nil
     :show-add-topic-popover false})

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

  (render-state [_ {:keys [editing as-of actual-as-of transition-as-of show-add-topic-popover] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (router/current-company-slug))
          revisions-list (section-kw (slug @caches/revisions))
          topic-data (utils/select-section-data section-data section-kw as-of)
          add-topic? (:add-topic data)]
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
                                                 :add-topic add-topic?
                                                 :sharing-selected (and sharing-mode share-selected)
                                                 :active (and add-topic? show-add-topic-popover)})
                    :ref "topic"
                    :onClick #(if add-topic?
                                (add-topic owner)
                                (topic-click section-kw options nil))}
        (when show-add-topic-popover
          (let [all-sections (get-all-sections slug)
                update-active-topics (:update-active-topics options)
                list-data {:all-topics all-sections
                           :active-topics-list active-topics
                           :archived-topics archived-topics
                           :show-above (show-popover-above? owner)
                           :column column}
                list-opts {:did-change-active-topics update-active-topics
                           :dismiss-popover #(om/set-state! owner :show-add-topic-popover false)}]
            (om/build add-topic-popover list-data {:opts list-opts})))
        (dom/div #js {:className "topic-anim group"
                      :key (str "topic-anim-" as-of "-" transition-as-of)
                      :ref "topic-anim"}
          (dom/div #js {:className "topic-as-of group"
                        :ref "cur-topic"
                        :key (str "cur-" as-of)
                        :style #js {:opacity 1 :width "100%" :height "auto"}}
            (if (= (dis/foce-section-key) section-kw)
              (om/build topic-edit {:section section
                                    :topic-data topic-data
                                    :add-topic add-topic?
                                    :sharing-mode sharing-mode
                                    :show-fast-editing (:show-fast-editing data)
                                    :currency currency
                                    :read-only-company (:read-only-company data)
                                    :topic-click (partial topic-click options)
                                    :foce-edit (:foce-edit data)
                                    :prev-rev prev-rev
                                    :next-rev next-rev}
                                   {:opts (merge options {:rev-click (fn [e rev]
                                                                        (scroll-to-topic-top (om/get-ref owner "topic"))
                                                                        (om/set-state! owner :transition-as-of (:updated-at rev))
                                                                        (.stopPropagation e))})
                                    :key (str "topic-foce-" section)})
              (om/build topic-internal {:section section
                                        :topic-data topic-data
                                        :add-topic add-topic?
                                        :sharing-mode sharing-mode
                                        :show-fast-editing (:show-fast-editing data)
                                        :currency currency
                                        :read-only-company (:read-only-company data)
                                        :topic-click (partial topic-click options)
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
                                            :add-topic add-topic?
                                            :sharing-mode sharing-mode
                                            :currency currency
                                            :read-only-company (:read-only-company data)
                                            :prev-rev tr-prev-rev
                                            :next-rev tr-next-rev}
                                           {:opts (merge options {:rev-click #()})}))))))))))