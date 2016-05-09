(ns open-company-web.components.topic-list
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.ui.side-drawer :refer (side-drawer)]
            [open-company-web.components.fullscreen-topic :refer (fullscreen-topic)]
            [open-company-web.components.ui.drawer-toggler :refer (drawer-toggler)]))

;; 3 Columns
(def c3-min-win-width 1006)
(def c3-max-win-width 1800)
(def c3-min-card-width 302)
(def c3-max-card-width 500)
(def c3-win-card-diff (- (/ c3-max-win-width c3-max-card-width) (/ c3-min-win-width c3-min-card-width)))
(def c3-win-diff (- c3-max-win-width c3-min-win-width))
(def c3-min-card-delta (/ c3-min-win-width c3-min-card-width))

;; 2 Columns
(def c2-min-win-width 684)
(def c2-max-win-width 1005)
(def c2-min-card-width 302)
(def c2-max-card-width 462)
(def c2-win-card-diff (- (/ c2-max-win-width c2-max-card-width) (/ c2-min-win-width c2-min-card-width)))
(def c2-win-diff (- c2-max-win-width c2-min-win-width))
(def c2-min-card-delta (/ c2-min-win-width c2-min-card-width))

;; 1 Columns
(def c1-min-win-width 320)
(def c1-max-win-width 683)
(def c1-min-card-width 302)
(def c1-max-card-width 500)
(def c1-win-card-diff (- (/ c1-max-win-width c1-max-card-width) (/ c1-min-win-width c1-min-card-width)))
(def c1-win-diff (- c1-max-win-width c1-min-win-width))
(def c1-min-card-delta (/ c1-min-win-width c1-min-card-width))

(defn win-width [columns]
  (let [ww (.-clientWidth (sel1 js/document :body))]
    (case columns
      3
      (max (min ww c3-max-win-width) c3-min-win-width)
      2
      (max (min ww c2-max-win-width) c2-min-win-width)
      1
      (max (min ww c1-max-win-width) c1-min-win-width))))

(defn get-min-win-width [columns]
  (case columns
    3 c3-min-win-width
    2 c2-min-win-width
    1 c1-min-win-width))

(defn get-win-diff [columns]
  (case columns
    3 c3-win-diff
    2 c2-win-diff
    1 c1-win-diff))

(defn get-win-card-diff [columns]
  (case columns
    3 c3-win-card-diff
    2 c2-win-card-diff
    1 c1-win-card-diff))

(defn get-min-card-delta [columns]
  (case columns
    3 c3-min-card-delta
    2 c2-min-card-delta
    1 c1-min-card-delta))

(defn calc-card-width [columns]
  (let [ww (win-width columns)
        ;; get params based on columns number
        min-win-width (get-min-win-width columns)
        win-diff (get-win-diff columns)
        win-card-diff (get-win-card-diff columns)
        min-card-delta (get-min-card-delta columns)
        ;; calculations
        win-delta-width (- ww min-win-width)
        perc-win-delta  (/ (* win-delta-width 100) win-diff)
        diff-delta      (* (/ win-card-diff 100) perc-win-delta)
        delta           (+ min-card-delta diff-delta)]
      (/ ww delta)))

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

(defn update-active-topics [owner options category new-active-topics]
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
     :selected-topic (or (:selected-topic current-state) (:selected-topic data))
     :drawer-open (or (:drawer-open current-state) false)
     :columns (utils/columns-num)}))

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
                                 :topic-click (partial topic-click owner section-name)}})))))

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
      (reset! scrolled-to-top true))
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns (utils/columns-num))))

  (will-receive-props [_ next-props]
    (when-not (= (:company-data next-props) (:company-data data))
      (om/set-state! owner (get-state next-props (om/get-state owner))))
    (when-not (:read-only (:company-data next-props))
      (get-new-sections-if-needed owner)))

  (render-state [_ {:keys [active-topics selected-topic selected-metric drawer-open columns]}]
    (let [slug            (keyword (router/current-company-slug))
          company-data    (:company-data data)
          active-category (keyword (:active-category data))
          category-topics (flatten (vals active-topics))
          win-width       (.-clientWidth (sel1 js/document :body))
          card-width      (calc-card-width columns)]
      (dom/div {:class "topic-list group"
                :key "topic-list"}
        (when (and (not (:read-only company-data))
                   (not (utils/is-mobile))
                   (not (:loading data)))
          ;; drawer toggler
          (om/build drawer-toggler {:close (not drawer-open)
                                    :click-cb #(om/update-state! owner :drawer-open not)}))
        (when-not (or (:read-only company-data)
                      (utils/is-mobile)
                      (:loading data))
          ;; side drawer
          (let [all-category-sections (sections-for-category slug active-category)
                list-data (merge data {:active true
                                       :all-topics all-category-sections
                                       :active-topics-list (get active-topics active-category)})
                list-opts {:did-change-active-topics #(update-active-topics owner options active-category %)}]
            (om/build side-drawer {:open drawer-open
                                   :list-key active-category
                                   :list-data list-data}
                                  {:opts {:list-opts list-opts
                                          :bg-click-cb #(om/set-state! owner :drawer-open false)}})))
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
          (case columns
            1
            (dom/div {:class "topics-column-container columns-1 group"
                      :style #js {:margin-left (str (/ (- win-width card-width) 2) "px")}}
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [section-name category-topics]
                  (render-topic owner section-name company-data active-category))))
            2
            (dom/div {:class "topics-column-container columns-2 group"
                      :style #js {:width (str (+ (* card-width 2) 20 60) "px")}}
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (quot (count category-topics) 2))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (* idx 2)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category)))
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (quot (count category-topics) 2))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (inc (* idx 2))
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))))
            3
            (dom/div {:class "topics-column-container group"
                      :style #js {:width (str (+ (* card-width 3) 40 60) "px")}}
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (quot (count category-topics) 3))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (* idx 3)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category)))
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (quot (count category-topics) 3))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (inc (* idx 3))
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category)))
              (dom/div {:class "topics-column"
                        :style #js {:width (str card-width "px")}}
                (for [idx (range (quot (count category-topics) 3))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (+ (* idx 3) 2)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))))))))))