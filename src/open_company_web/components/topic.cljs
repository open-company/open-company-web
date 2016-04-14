(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.api :as api]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.icon :as i]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.topic-body :refer (topic-body)]
            [open-company-web.components.topic-growth-headline :refer (topic-growth-headline)]
            [open-company-web.components.topic-finances-headline :refer (topic-finances-headline)]
            [open-company-web.local-settings :as ls]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]
            [goog.style :refer (setStyle)]))

(defcomponent topic-headline [data owner]
  (render [_]
    (dom/div {:class "topic-headline-inner"} (:headline data))))

(defn scroll-to-topic-top [topic]
  (let [body-scroll (.-scrollTop (.-body js/document))
        topic-scroll-top (utils/offset-top topic)]
    (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90))))

(defn mobile-topic-animation [data owner options expanded]
  (when expanded
    (om/set-state! owner :as-of (om/get-state owner :actual-as-of)))
  (let [topic (om/get-ref owner "topic")
        topic-more (sel1 topic [:div.topic-more])
        topic-date (sel1 topic [:div.topic-date])
        body-node (sel1 topic [:div.topic-body])
        body-nav-node (sel1 topic [:div.body-navigation-container])]
    (setStyle body-nav-node #js {:height "auto"})
    (let [body-height (.-offsetHeight body-nav-node)
          body-width (.-offsetWidth body-nav-node)]
      (setStyle body-nav-node #js {:height (if expanded "auto" "0")
                                   :overflow "hidden"})

      ;; animate finances headtitle
      (when-let [finances-children (sel1 topic ":scope > div.topic-headline > div.topic-headline-finances")]
        (let [finances-resize (Resize.
                                finances-children
                                #js [body-width (if expanded 0 100)]
                                #js [body-width (if expanded 100 0)]
                                utils/oc-animation-duration)
              finances-fade (Fade.
                              finances-children
                              (if expanded 0 1)
                              (if expanded 1 0)
                              utils/oc-animation-duration)]
          (.play finances-resize)
          (.play finances-fade)))

      ;; animate growth headtitle
      (when-let [growth-children (sel1 topic ":scope > div.topic-headline > div.topic-growth-headline")]
        (let [growth-resize (Resize.
                              growth-children
                              #js [body-width (if expanded 0 100)]
                              #js [body-width (if expanded 100 0)]
                              utils/oc-animation-duration)
              growth-fade (Fade.
                            growth-children
                            (if expanded 0 1)
                            (if expanded 1 0)
                            utils/oc-animation-duration)]
            (.play growth-resize)
            (.play growth-fade)))

      (.play
        (Fade.
          topic-more
          (if expanded 0 1)
          (if expanded 1 0)
          utils/oc-animation-duration))

      (.play
        (Resize.
          topic-more
          #js [body-width (if expanded 0 20)]
          #js [body-width (if expanded 20 0)]
          utils/oc-animation-duration))

      (.play
        (Fade.
          topic-date
          (if expanded 1 0)
          (if expanded 0 1)
          utils/oc-animation-duration))
      ;; animate height
      (let [height-animation (Resize.
                               body-nav-node
                               #js [body-width (if expanded (+ body-height 20) 0)]
                               #js [body-width (if expanded 0 (+ body-height 20))]
                               (* 0.8 utils/oc-animation-duration))]
        (doto height-animation
          (events/listen
           EventType/FINISH
           (fn [e]
            (om/update-state! owner :expanded not)
            (.setTimeout js/window
              #(setStyle body-nav-node #js {:overflow (if expanded "hidden" "visible")
                                            :height (if expanded "0" "auto")}) 1)))
          (.play)))

      (scroll-to-topic-top topic)

      (if-not expanded
        ;; show the edit button if the topic body is empty
        (let [section (keyword (:section-name options))
              section-data (:section-data data)
              body (utils/get-topic-body section-data section)]
          (when (clojure.string/blank? body)
            ((:force-edit-cb options) true)))
        ;; hide the edit button if necessary
        ((:force-edit-cb options) false)))))

(defn headline-component [section]
  (cond

    (= section :finances)
    topic-finances-headline

    (= section :growth)
    topic-growth-headline

    :else
    topic-headline))

(defn pillbox-click-cb [owner metric-slug e]
  (.stopPropagation e)
  (.preventDefault e)
  (om/set-state! owner :selected-metric metric-slug)
  (let [topic-click-cb (om/get-props owner :topic-click)]
    (topic-click-cb metric-slug)))

(defcomponent topic-internal [{:keys [topic-data section currency expanded prev-rev next-rev] :as data} owner options]
  (render [_]
    (let [section-kw (keyword section)
          headline-options {:opts {:currency currency
                                   :pillbox-click-cb (partial pillbox-click-cb owner)}}
          headline-data (assoc topic-data :expanded expanded)]
      (dom/div #js {:className "topic-internal group"
                    :ref "topic-internal"}
        ;; Topic title
        (dom/div {:class "topic-header group"}
          (dom/div {:class "left"} (i/icon (:icon topic-data)))
          (dom/div {:class "topic-title"} (:title topic-data))
          (dom/div #js {:className "topic-date"
                        :ref "topic-date"
                        :style #js {:opacity (if expanded 1 0)
                                    :height (str (if expanded 20 0) "px")
                                    :paddingTop (str (cond
                                                       (utils/is-mobile) 8
                                                       expanded 16
                                                       :else 0)
                                                     "px")}}
            (str (:name (:author topic-data)) " on " (utils/date-string (utils/js-date (:updated-at topic-data))))))

        ;; Topic headline
        (dom/div {:class "topic-headline"
                  :style {:font-weight (if expanded 600)}}
          (om/build (headline-component section-kw) headline-data headline-options))

        (when (utils/is-mobile)
          (dom/div #js {:className "topic-more"
                        :ref "topic-more"
                        :style #js {:opacity (if expanded 0 1)}}
            (dom/i {:class "fa fa-circle"})
            (dom/i {:class "fa fa-circle"})
            (dom/i {:class "fa fa-circle"})))
        ;; topic body
        (when (utils/is-mobile)
          (dom/div #js {:className (str "body-navigation-container group " (when (not expanded) "close"))
                        :ref "body-navigation-container"}
            (om/build topic-body {:section section
                                  :section-data topic-data
                                  :expanded expanded
                                  :currency currency
                                  :selected-metric (om/get-state owner :selected-metric)}
                                 {:opts options})
            (when expanded
              (dom/div {:class "topic-navigation group"}
                (when prev-rev
                  (dom/div {:class "arrow previous group"}
                    (dom/button {:on-click (fn [e]
                                            (let [bt (.-target e)]
                                              (set! (.-disabled bt) "disabled")
                                              (.stopPropagation e)
                                              ((:rev-click options) e prev-rev)
                                              (.setTimeout js/window
                                                #(set! (.-disabled bt) false) 1000)))} "< Previous")))
                (when next-rev
                  (dom/div {:class "arrow next group"}
                    (dom/button {:on-click (fn [e]
                                            (let [bt (.-target e)]
                                              (set! (.-disabled bt) "disabled")
                                              (.stopPropagation e)
                                              ((:rev-click options) e next-rev)
                                              (.setTimeout js/window
                                                #(set! (.-disabled bt) false) 1000)))} "Next >")))))))))))

(defn topic-click [data owner options expanded selected-metric]
  (if (utils/is-mobile)
    (mobile-topic-animation data owner options expanded)
    ((:bw-topic-click options) (:section data) selected-metric)))

(defn animate-transition [owner]
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

(defcomponent topic [{:keys [section-data section currency] :as data} owner options]

  (init-state [_]
    {:expanded false
     :as-of (:updated-at section-data)
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

  (did-update [_ _ _]
    (when (om/get-state owner :transition-as-of)
      (animate-transition owner)))

  (render-state [_ {:keys [expanded as-of actual-as-of transition-as-of] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (:slug @router/path))
          revisions-list (section-kw (slug @cache/revisions))
          topic-data (utils/select-section-data section-data section-kw as-of)]
      ; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div #js {:className "topic group"
                    :ref "topic"
                    :onClick #(topic-click data owner options expanded nil)}
        (dom/div #js {:className "topic-anim group"
                      :ref "topic-anim"}
          (dom/div #js {:className "topic-as-of group"
                        :ref "cur-topic"
                        :key (str "cur-" as-of (when expanded "-expanded"))
                        :style #js {:opacity 1 :width "100%" :height "auto"}}
            (om/build topic-internal {:section section
                                      :topic-data topic-data
                                      :currency currency
                                      :expanded expanded
                                      :topic-click (partial topic-click data owner options expanded)
                                      :prev-rev prev-rev
                                      :next-rev next-rev}
                                     {:opts (merge options {:rev-click (fn [e rev]
                                                                          (scroll-to-topic-top (om/get-ref owner "topic"))
                                                                          (om/set-state! owner :transition-as-of (:updated-at rev))
                                                                          (.stopPropagation e))})}))
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
                                            :currency currency
                                            :expanded expanded
                                            :topic-click #()
                                            :prev-rev tr-prev-rev
                                            :next-rev tr-next-rev}
                                           {:opts (merge options {:rev-click #()})})))))))))