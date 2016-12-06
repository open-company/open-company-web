(ns open-company-web.components.topics-columns
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [cuerdas.core :as s]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.topic-view :refer (topic-view)]
            [open-company-web.components.add-topic :refer (add-topic)]
            [open-company-web.components.bw-topics-list :refer (bw-topics-list)]))

;; Calc best topics layout based on heights

(def topic-default-height 70)
(def data-topic-default-zero-height 74)
(def data-topic-default-one-height 251)
(def data-topic-default-more-height 357)

(def topic-margins 20)
(def mobile-topic-margins 3)

(defn headline-body-height [headline body card-width]
  (let [$headline (js/$ (str "<div class=\"topic\">"
                                "<div>"
                                  "<div class=\"topic-internal\">"
                                    (when-not (clojure.string/blank? headline)
                                      (str "<div class=\"topic-headline-inner\" style=\"width: " (+ card-width (if (responsive/is-mobile-size?) mobile-topic-margins topic-margins)) "px;\">"
                                             (utils/emojify headline true)
                                           "</div>"))
                                    "<div class=\"topic-body\" style=\"width: " (+ card-width (if (responsive/is-mobile-size?) mobile-topic-margins topic-margins)) "px;\">"
                                      (utils/emojify body true)
                                    "</div>"
                                  "</div>"
                                "</div>"
                              "</div>"))]
    (.appendTo $headline (.-body js/document))
    (let [height (.height $headline)]
      (.detach $headline)
      height)))

(defn data-topic-height [owner topic topic-data]
  (if (= topic :finances)
    (cond
      (= (count (:data topic-data)) 0)
      data-topic-default-zero-height
      (= (count (:data topic-data)) 1)
      data-topic-default-one-height
      (> (count (:data topic-data)) 1)
      data-topic-default-more-height)
    (let [data (:data topic-data)
          selected-metric (or (om/get-props owner :selected-metric) (:slug (first (:metrics topic-data))))
          metric-data (filter #(= (:slug %) selected-metric) data)]
      (cond
        (= (count metric-data) 0)
        data-topic-default-zero-height
        (= (count metric-data) 1)
        data-topic-default-one-height
        (> (count metric-data) 1)
        data-topic-default-more-height))))

(defn calc-column-height [owner data topics clmn]
  (let [card-width (om/get-props owner :card-width)
        topics-data (:topics-data data)]
    (for [topic topics
          :let [topic-kw (keyword topic)
                topic-data (get topics-data topic-kw)
                is-data-topic (#{:finances :growth} topic-kw)
                topic-body (:body topic-data)]]
      (cond
        (#{:finances :growth} topic-kw)
        (let [headline-height (headline-body-height (:headline topic-data) topic-body card-width)
              start-height (data-topic-height owner topic topic-data)]
          (+ start-height headline-height))
        :else
        (let [topic-image-height      (if (:image-url topic-data)
                                        (utils/aspect-ration-image-height (:image-width topic-data) (:image-height topic-data) card-width)
                                        0)
              headline-body-height (headline-body-height (:headline topic-data) topic-body card-width)]
          (+ topic-default-height headline-body-height topic-image-height))))))

(defn get-shortest-column [owner data current-layout]
  (let [columns-num (:columns-num data)
        frst-clmn (apply + (calc-column-height owner data (:1 current-layout) 1))
        scnd-clmn (apply + (calc-column-height owner data (:2 current-layout) 2))
        thrd-clmn (apply + (calc-column-height owner data (:3 current-layout) 3))
        min-height (if (= columns-num 3)
                    (min frst-clmn scnd-clmn thrd-clmn)
                    (min frst-clmn scnd-clmn))]
    (cond
      (= min-height frst-clmn)
      :1
      (= min-height scnd-clmn)
      :2
      (= min-height thrd-clmn)
      :3)))

(defn get-initial-layout [columns-num]
  (cond
    ; 2 columns empty layout
    (= columns-num 2)
    {:1 [] :2 []}
    ; 3 columns empty layout
    (= columns-num 3)
    {:1 [] :2 [] :3 []}))

(defn calc-layout
  "Calculate the best layout given the list of topics and the number of columns to layout to"
  [owner data]
  (cond
    ; avoid to crash tests
    (utils/is-test-env?)
    (om/get-props owner :topics)
    ; for mobile just layout the sections in :sections order
    ; w/o caring about the height it might be
    (responsive/is-mobile-size?)
    (let [sections (:sections (:company-data data))]
      (loop [idx 0
             layout {:1 [] :2 []}]
        (if (= idx (count sections))
          layout
          (let [topic (get sections idx)]
            (recur (inc idx)
                   (if (even? idx)
                      (assoc layout :1 (conj (:1 layout) topic))
                      (assoc layout :2 (conj (:2 layout) topic))))))))
    ; on big web guess what the topic height will be and layout the topics in
    ; the best order possible
    :else
    (let [columns-num (:columns-num data)
          company-data (:company-data data)
          topics-list (om/get-state owner :filtered-topics)
          final-layout (loop [idx 0
                              layout (get-initial-layout columns-num)]
                          (let [shortest-column (get-shortest-column owner data layout)
                                new-column (conj (get layout shortest-column) (get topics-list idx))
                                new-layout (assoc layout shortest-column new-column)]
                            (if (<= (inc idx) (count topics-list))
                              (recur (inc idx)
                                     new-layout)
                              new-layout)))
          clean-layout (apply merge (for [[k v] final-layout] {k (vec (remove nil? v))}))]
      clean-layout)))

(defn render-topic [owner options section-name & [column]]
  (when section-name
    (let [props                 (om/get-props owner)
          company-data          (:company-data props)
          topics-data           (:topics-data props)
          topic-click           (or (:topic-click options) identity)
          is-dashboard          (:is-dashboard props)]
      (let [sd (->> section-name keyword (get topics-data))
            topic-row-style (if (or (utils/in? (:route @router/path) "su-snapshot-preview")
                                    (utils/in? (:route @router/path) "su-list"))
                              #js {}
                              #js {:width (if is-dashboard
                                            (if (responsive/window-exceeds-breakpoint)
                                              (str (:card-width props) "px")
                                              "auto")
                                            (if (responsive/is-mobile-size?)
                                              "auto"
                                              (str (:card-width props) "px")))})]
        (when-not (and (:read-only company-data) (:placeholder sd))
          (dom/div #js {:className "topic-row"
                        :data-topic (name section-name)
                        :style topic-row-style
                        :ref section-name
                        :key (str "topic-row-" (name section-name))}
            (om/build topic {:loading (:loading company-data)
                             :section section-name
                             :is-stakeholder-update (:is-stakeholder-update props)
                             :section-data sd
                             :card-width (:card-width props)
                             :columns-num (:columns-num props)
                             :foce-data-editing? (:foce-data-editing? props)
                             :read-only-company (:read-only company-data)
                             :currency (:currency company-data)
                             :foce-key (:foce-key props)
                             :foce-data (:foce-data props)
                             :is-dashboard is-dashboard
                             :column column}
                             {:opts {:section-name section-name
                                     :topic-click (partial topic-click section-name)}})))))))

(defn- update-active-topics [owner new-topic section-data]
  (dis/dispatch! [:show-add-topic false])
  (let [company-data (om/get-props owner :company-data)
        old-topics (:sections company-data)
        new-topics (conj old-topics new-topic)
        new-topic-kw (keyword new-topic)]
    (om/set-state! owner :start-foce {:section new-topic-kw :title (:title section-data)})
    (if (s/starts-with? new-topic "custom-")
      (api/patch-sections new-topics section-data new-topic)
      (api/patch-sections new-topics))))

(defcomponent topics-columns [{:keys [columns-num
                                      content-loaded
                                      total-width
                                      card-width
                                      topics
                                      company-data
                                      topics-data
                                      is-dashboard
                                      is-stakeholder-update
                                      show-add-topic] :as data} owner options]

  (init-state [_]
    {:best-layout nil
     :filtered-topics (if (or (:read-only company-data)
                              (responsive/is-mobile-size?))
                        (utils/filter-placeholder-sections topics topics-data)
                        topics)})

  (did-mount [_]
    (when (> columns-num 1)
      (om/set-state! owner :best-layout (calc-layout owner data))))

  (will-receive-props [_ next-props]
    (when (and (> (:columns-num next-props) 1)
               (or (not= (:topics next-props) (:topics data))
                   (not= (:columns-num next-props) (:columns-num data))))
      (om/set-state! owner :filtered-topics (if (or (:read-only (:company-data next-props))
                                                    (responsive/is-mobile-size?))
                                              (utils/filter-placeholder-sections (:topics next-props) (:topics-data next-props))
                                              (:topics next-props)))
      (om/set-state! owner :best-layout (calc-layout owner next-props)))
    (when-let* [start-foce (om/get-state owner :start-foce)
                new-section (:section start-foce)
                new-section-data (get company-data new-section)]
      ; A new section was added, switch the topic-view when the add section is finished
      ; and enable FoCE
      (let [new-title (:title start-foce)
            foce-section-data (utils/new-section-initial-data
                               new-section
                               new-title
                               new-section-data)]
        (utils/after 10 #(router/nav! (oc-urls/company-section (router/current-company-slug) new-section)))
        (utils/after 60 #(dis/dispatch! [:start-foce
                                          new-section
                                          (merge foce-section-data {:new true
                                                                    :body-placeholder (utils/new-section-body-placeholder)})])))
      (om/set-state! owner :start-foce nil)))

  (render-state [_ {:keys [best-layout filtered-topics]}]
    (let [selected-topic-view   (:selected-topic-view data)
          partial-render-topic  (partial render-topic owner options)
          columns-container-key (apply str filtered-topics)
          topics-column-conatiner-style (if is-dashboard
                                          (if (responsive/window-exceeds-breakpoint)
                                            #js {:width total-width}
                                            #js {:margin "0px 9px"
                                                 :width "auto"})
                                          (if (responsive/is-mobile-size?)
                                            #js {:margin "0px 9px"
                                                 :width "auto"}
                                            #js {:width total-width}))
          topic-view-width (responsive/topic-view-width card-width columns-num)]
      ;; Topic list
      (dom/div {:class (utils/class-set {:topics-columns true
                                         :overflow-visible true
                                         :group true
                                         :content-loaded content-loaded})}
        (cond
          ;; render 2 or 3 column layout
          (> columns-num 1)
          (dom/div {:class (utils/class-set {:topics-column-container true
                                             :group true
                                             :tot-col-3 (and is-dashboard
                                                             (= columns-num 3))
                                             :tot-col-2 (and is-dashboard
                                                             (= columns-num 2))})
                    :style topics-column-conatiner-style
                    :key columns-container-key}
            (when-not (responsive/is-tablet-or-mobile?)
              (om/build bw-topics-list data))
            (cond
              (and is-dashboard
                   (not (responsive/is-mobile-size?))
                   (not selected-topic-view)
                   show-add-topic)
              (dom/div {:class "add-topic-container"
                        :style {:margin-right (str (- topic-view-width 560) "px")}}
                (add-topic (partial update-active-topics owner)))
              (and is-dashboard
                   (not (responsive/is-mobile-size?))
                   selected-topic-view)
              (om/build topic-view {:card-width card-width
                                    :columns-num columns-num
                                    :company-data company-data
                                    :foce-key (:foce-key data)
                                    :foce-data (:foce-data data)
                                    :foce-data-editing? (:foce-data-editing? data)
                                    :selected-topic-view selected-topic-view})
              ; for each column key contained in best layout
              :else
              (dom/div {:class "right" :style {:width (str (- (int total-width) responsive/left-topics-list-width) "px")}}
                (for [kw (if (= columns-num 3) [:1 :2 :3] [:1 :2])]
                  (let [column (get best-layout kw)]
                    (dom/div {:class (str "topics-column col-" (name kw))
                              :style #js {:width (str (+ card-width (if (responsive/is-mobile-size?) mobile-topic-margins topic-margins)) "px")}}
                      ; render the topics
                      (when (pos? (count column))
                        (for [idx (range (count column))
                              :let [section-kw (get column idx)
                                    section-name (name section-kw)]]
                          (partial-render-topic section-name (name kw))))))))))
          ;; 1 column or default
          :else
          (dom/div {:class "topics-column-container columns-1 group"
                    :style topics-column-conatiner-style
                    :key columns-container-key}
            (dom/div {:class "topics-column"}
              (for [section filtered-topics]
                (partial-render-topic (name section) 1)))))))))