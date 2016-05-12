(ns open-company-web.components.topic-list
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.fullscreen-topic :refer (fullscreen-topic)]))

(defn get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (router/current-company-slug))
          company-data (dispatcher/company-data)]
      (when (and (empty? (slug @caches/new-sections))
                 (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (api/get-new-sections)))))

(defn get-active-topics [company-data category]
  (get-in company-data [:sections (keyword category)]))

(defn update-active-topics [owner category new-active-topics]
  (let [old-active-categories (om/get-state owner :active-topics)
        new-active-categories (assoc old-active-categories category new-active-topics)]
    (api/patch-sections new-active-categories)))

(defn get-state [data current-state]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) categories))]
    {:initial-active-topics active-topics
     :active-topics active-topics
     :new-sections-requested (or (:new-sections-requested current-state) false)
     :selected-topic (or (:selected-topic current-state) (:selected-topic data))}))

(defn topic-click [owner topic selected-metric]
  (om/set-state! owner :selected-topic topic)
  (om/set-state! owner :selected-metric selected-metric))

(def scrolled-to-top (atom false))

(defn close-overlay-cb [owner]
  (om/set-state! owner :selected-topic nil)
  (om/set-state! owner :selected-metric nil))

(defn sections-for-category [slug active-category]
  (let [category-data (first (filter #(= (:name %) (name active-category)) (:categories (slug @caches/new-sections))))
        all-category-sections (:sections category-data)]
    (apply merge
           (map #(hash-map (keyword (:section-name %)) %) all-category-sections))))

(defn render-topic [owner section-name company-data active-category]
  (when section-name
    (if (= section-name "add-topic")
      (om/build topic {:loading false
                       :section "add-topic"
                       :add-topic true
                       :section-data {:title "+ ADD A TOPIC"
                                      :body ""
                                      :updated-at 0
                                      :headline ""}
                        :currency (:currency company-data)
                        :active-topics (om/get-state owner :active-topics)
                        :active-category active-category}
                       {:opts {:section-name section-name
                               :topic-click (partial topic-click owner section-name)
                               :update-active-topics (partial update-active-topics owner active-category)}})
      (let [sd (->> section-name keyword (get company-data))]
        (when-not (and (:read-only company-data) (:placeholder sd))
          (dom/div #js {:className "topic-row"
                       :ref section-name
                       :key (str "topic-row-" (name section-name))}
            (om/build topic {:loading (:loading company-data)
                             :section section-name
                             :section-data sd
                             :currency (:currency company-data)
                             :active-category active-category}
                             {:opts {:section-name section-name
                                     :topic-click (partial topic-click owner section-name)}})))))))

(defcomponent topic-list [data owner options]

  (init-state [_]
    (get-state data nil))

  (did-mount [_]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner))
    ; scroll to top when the component is initially mounted to
    ; make sure the calculation for the fixed navbar are correct
    (when-not @scrolled-to-top
      (set! (.-scrollTop (.-body js/document)) 0)
      (reset! scrolled-to-top true)))

  (will-receive-props [_ next-props]
    (when-not (= (:company-data next-props) (:company-data data))
      (om/set-state! owner (get-state next-props (om/get-state owner))))
    (when-not (:read-only (:company-data next-props))
      (get-new-sections-if-needed owner)))

  (render-state [_ {:keys [active-topics selected-topic selected-metric]}]
    (let [slug            (keyword (router/current-company-slug))
          company-data    (:company-data data)
          active-category (keyword (:active-category data))
          category-topics (flatten (vals active-topics))
          win-width       (.-clientWidth (sel1 js/document :body))
          card-width      (:card-width data)
          columns-num     (:columns-num data)
          ww              (.-clientWidth (sel1 js/document :body))
          add-first-column? (= (count category-topics) 0)
          add-second-column? (= (count category-topics) 1)
          add-third-column? (>= (count category-topics) 2)
          add-topic?      (and (not (:read-only company-data))
                               (not (responsive/is-mobile)))]
      (dom/div {:class "topic-list group"
                :key "topic-list"}
        (when selected-topic
          (om/build fullscreen-topic {:section selected-topic
                                      :section-data (->> selected-topic keyword (get company-data))
                                      :selected-metric selected-metric
                                      :card-width card-width
                                      :currency (:currency company-data)}
                                     {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                             :topic-edit-cb (:topic-edit-cb options)}}))
        ;; Topic list
        (dom/div {:class (utils/class-set {:topic-list-internal true
                                           :group true
                                           :content-loaded (not (:loading data))})}
          (case columns-num
            3
            (dom/div {:class "topics-column-container group"
                      :style #js {:width (str (+ (* card-width 3) 40 60) "px")}}
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (inc (quot (count category-topics) 3)))
                  :while (< idx (inc (quot (count category-topics) 2)))
                  :let [real-idx (* idx 3)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))
                (when (and add-first-column? add-topic?)
                  (render-topic owner "add-topic" company-data active-category)))
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (inc (quot (count category-topics) 3)))
                  :while (< idx (inc (quot (count category-topics) 2)))
                  :let [real-idx (inc (* idx 3))
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))
                (when (and add-second-column? add-topic?)
                  (render-topic owner "add-topic" company-data active-category)))
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (inc (quot (count category-topics) 3)))
                  :while (< idx (inc (quot (count category-topics) 2)))
                  :let [real-idx (+ (* idx 3) 2)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))
                (when (and add-third-column? add-topic?)
                  (render-topic owner "add-topic" company-data active-category))))
            2
            (dom/div {:class "topics-column-container columns-2 group"
                      :style #js {:width (str (+ (* card-width 2) 20 60) "px")}}
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (inc (quot (count category-topics) 2)))
                  :while (< idx (inc (quot (count category-topics) 2)))
                  :let [real-idx (* idx 2)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))
                (when (and add-first-column? add-topic?)
                  (render-topic owner "add-topic" company-data active-category)))
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (inc (quot (count category-topics) 2)))
                  :while (< idx (inc (quot (count category-topics) 2)))
                  :let [real-idx (inc (* idx 2))
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))
                (when (and (not add-first-column?)
                           add-topic?)
                  (render-topic owner "add-topic" company-data active-category))))
            ; 1 column or default
            (dom/div {:class "topics-column-container columns-1 group"
                      :style #js {:width (if (> ww 413) (str card-width "px") "auto")}}
              (dom/div {:class "topics-column"}
                (for [section-name category-topics]
                  (render-topic owner section-name company-data active-category))
                (when add-topic?
                  (render-topic owner "add-topic" company-data active-category))))))))))