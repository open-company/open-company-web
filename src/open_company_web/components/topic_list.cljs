(ns open-company-web.components.topic-list
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel)]
            [goog.style :refer (setStyle)]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.ui.side-drawer :refer (side-drawer)]
            [open-company-web.components.fullscreen-topic :refer (fullscreen-topic)]
            [open-company-web.components.ui.drawer-toggler :refer (drawer-toggler)]))

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

(defn columns-num []
  (let [win-width (.-clientWidth (.-body js/document))]
    (cond
      (>= win-width 1132)
      3
      (>= win-width 780)
      2
      :else
      1)))

(defn get-state [data current-state]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) categories))]
    {:initial-active-topics active-topics
     :active-topics active-topics
     :new-sections-requested (or (:new-sections-requested current-state) false)
     :selected-topic (or (:selected-topic current-state) (:selected-topic data))
     :drawer-open (or (:drawer-open current-state) false)
     :columns (columns-num)}))

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
                                 :topic-click (partial topic-click owner)}})))))

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

  (render-state [_ {:keys [active-topics selected-topic selected-metric drawer-open columns]}]
    (let [slug            (keyword (router/current-company-slug))
          company-data    (:company-data data)
          active-category (keyword (:active-category data))
          category-topics (flatten (vals active-topics))]
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
                                       :active-topics-list category-topics})
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
                                      :currency (:currency company-data)}
                                     {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                             :topic-edit-cb (:topic-edit-cb options)}}))
        ;; Topic list
        (dom/div {:class (utils/class-set {:topic-list-internal true
                                           :group true
                                           :content-loaded (not (:loading data))})}
          (case columns
            1
            (dom/div {:class "topics-column-container columns-1 group"}
              (dom/div {:class "topics-column"}
                (for [section-name category-topics]
                  (render-topic owner section-name company-data active-category))))
            2
            (dom/div {:class "topics-column-container columns-2 group"}
              (dom/div {:class "topics-column"}
                (for [idx (range (quot (count category-topics) 2))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (* idx 2)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category)))
              (dom/div {:class "topics-column"}
                (for [idx (range (quot (count category-topics) 2))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (inc (* idx 2))
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))))
            3
            (dom/div {:class "topics-column-container columns-3 group"}
              (dom/div {:class "topics-column"}
                (for [idx (range (quot (count category-topics) 3))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (* idx 3)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category)))
              (dom/div {:class "topics-column"}
                (for [idx (range (quot (count category-topics) 3))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (inc (* idx 3))
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category)))
              (dom/div {:class "topics-column"}
                (for [idx (range (quot (count category-topics) 3))
                  :while (< idx (quot (count category-topics) 2))
                  :let [real-idx (+ (* idx 3) 2)
                        section-name (get (vec category-topics) real-idx)]]
                  (render-topic owner section-name company-data active-category))))))))))