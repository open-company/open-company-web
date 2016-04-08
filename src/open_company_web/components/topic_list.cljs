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
            [open-company-web.components.topic-overlay :refer (topic-overlay)]
            [open-company-web.components.ui.drawer-toggler :refer (drawer-toggler)]))

(defn get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)]
      (when (and (empty? (slug @caches/new-sections)) (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (api/get-new-sections)))))

(defn get-active-topics [company-data category]
  (if (= category "all")
    (apply concat (vals (:sections company-data)))
    (get-in company-data [:sections (keyword category)])))

(defn update-active-topics [owner options category new-active-topics]
  (let [old-active-categories (om/get-state owner :active-topics)
        new-active-categories (assoc old-active-categories category new-active-topics)]
    (api/patch-sections (dissoc new-active-categories :all))))

(defn get-state [data current-state]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        all-categories (if (utils/is-mobile) categories (concat ["all"] categories))
        active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) all-categories))]
    {:initial-active-topics active-topics
     :active-topics active-topics
     :new-sections-requested (or (:new-sections-requested current-state) false)
     :selected-topic (or (:selected-topic current-state) (:selected-topic data))
     :drawer-open (or (:drawer-open current-state) false)}))

(defn topic-click [owner topic selected-metric]
  (om/set-state! owner :selected-topic topic)
  (om/set-state! owner :selected-metric selected-metric))

(def scrolled-to-top (atom false))

(defn set-lis-height [owner]
  (when-not (utils/is-mobile)
    (when-let [topic-list (om/get-ref owner "topic-list-ul")]
      (let [li-elems (sel topic-list [:li.topic-row])
            max-height (apply max (map #(.-clientHeight %) li-elems))]
        (doseq [li li-elems]
          (setStyle li #js {:height (str max-height "px")}))))))

(defn close-overlay-cb [owner]
  (om/set-state! owner :selected-topic nil)
  (om/set-state! owner :selected-metric nil))

(defn sections-for-category [slug active-category]
  (let [category-data (first (filter #(= (:name %) (name active-category)) (:categories (slug @caches/new-sections))))
        all-category-sections (:sections category-data)]
    (apply merge
           (map #(hash-map (keyword (:section-name %)) %) all-category-sections))))

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
    ; set the cards height on big web
    (set-lis-height owner))

  (will-receive-props [_ next-props]
    (when-not (= (:company-data next-props) (:company-data data))
      (om/set-state! owner (get-state next-props (om/get-state owner))))
    (when-not (:read-only (:company-data next-props))
      (get-new-sections-if-needed owner)))

  (did-update [_ _ _]
    ; set the cards height on big web
    (set-lis-height owner))

  (render-state [_ {:keys [active-topics selected-topic selected-metric drawer-open]}]
    (let [slug (keyword (:slug @router/path))
          company-data (:company-data data)
          active-category (keyword (:active-category data))
          category-topics (get active-topics active-category)]
      (dom/div {:class "topic-list fix-top-margin-scrolling"
                :key "topic-list"}
        (when (and (not (:read-only company-data))
                   (not (utils/is-mobile))
                   (not (:loading data)))
          ;; drawer toggler
          (om/build drawer-toggler {:close (not drawer-open)} {:opts {:click-cb #(om/update-state! owner :drawer-open not)}}))
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
          (om/build topic-overlay {:section selected-topic
                                   :section-data (->> selected-topic keyword (get company-data))
                                   :selected-metric selected-metric
                                   :currency (:currency company-data)}
                                  {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                          :topic-edit-cb (:topic-edit-cb options)}}))
        (dom/ul #js {:className (utils/class-set {:topic-list-internal true
                                                  :read-only (or (utils/is-mobile) (:read-only company-data))
                                                  :group true
                                                  :content-loaded (not (:loading data))})
                     :ref "topic-list-ul"}
          (for [section-name category-topics
                :let [sd (->> section-name keyword (get company-data))]]
            (dom/li #js {:className "topic-row"
                         :ref section-name
                         :key (str "topic-row-" (name section-name))}
              (when-not (and (:read-only company-data) (:placeholder sd))
                (om/build topic {:loading (:loading company-data)
                                 :section section-name
                                 :section-data sd
                                 :currency (:currency company-data)
                                 :active-category active-category}
                                 {:opts {:section-name section-name
                                         :bw-topic-click (partial topic-click owner)}})))))))))