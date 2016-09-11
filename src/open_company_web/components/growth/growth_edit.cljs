(ns open-company-web.components.growth.growth-edit
  (:require [clojure.string :as string]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.components.ui.cell :refer (cell)]
            [open-company-web.components.growth.growth-metric-edit :refer (growth-metric-edit)]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.components.ui.onboard-tip :refer (onboard-tip)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [cljs.core.async :refer (put!)]))

(def batch-size 6)

;; ===== Growth Data Row Editing Component (Single Row in the Data Table) =====

(defn- signal-tab [period k]
  (when-let [ch (utils/get-channel (str period k))]
    (put! ch {:period period :key k})))

(defcomponent growth-edit-row [{:keys [interval needs-year is-last change-cb next-period prefix suffix] :as data} owner]

  (render [_]
    (let [growth-data (:cursor data)
          is-new (and (not (:value growth-data)) (:new growth-data))
          value (:value growth-data)
          period (:period growth-data)
          period-month (utils/get-month period interval)
          flags [:short :skip-year]
          period-string (utils/get-period-string period interval flags)
          cell-state (if is-new :new :display)
          tab-cb (fn [_ k]
                   (cond
                     (= k :value)
                     (when next-period
                       (signal-tab next-period :value))))]
      (dom/tr {:class "growth-edit-row"}
        (dom/th {:class "no-cell"}
          period-string)
        (dom/td {}
          (when-not is-last
            (om/build
              cell
              {:value value
               :positive-only false
               :placeholder "Value"
               :cell-state cell-state
               :draft-cb #(change-cb :value %)
               :prefix prefix
               :suffix suffix
               :period period
               :key :value
               :tab-cb tab-cb})))))))

;; ===== Growth Metric Data Functions =====

(defn- growth-get-value [v]
  (if (string/blank? v)
    ""
    (if (js/isNaN v)
      0
      v)))

(defn- growth-fix-row [row]
  (let [fixed-value (growth-get-value (:value row))
        with-fixed-value (if (string/blank? fixed-value)
                           (dissoc row :value)
                           (assoc row :value fixed-value))]
    with-fixed-value))

(defn- growth-check-value [v]
  (and (not (= v ""))
       (not (nil? v))))

(defn- growth-change-data
  ""
  [owner row]
  (when (growth-check-value (:value row))
    (let [{:keys [period slug] :as fixed-row} (growth-fix-row row)
          growth-data (om/get-state owner :growth-data)
          fixed-data (if (not (:value fixed-row))
                       (dissoc growth-data (str period slug))
                       (assoc growth-data (str period slug) fixed-row))]
      ;(om/set-state! owner :has-changes true)
      (om/set-state! owner :growth-data fixed-data))))

(defn- more-months [owner data]
  (om/update-state! owner :stop #(+ % batch-size)))

(defn- filter-growth-data [metric-slug growth-data]
  (into {} (filter (fn [[k v]] (= (:slug v) metric-slug)) growth-data)))

(defn- growth-clean-row [data]
  ; a data entry is good if we have the period value
  (when (and (not (nil? (:period data)))
             (not (nil? (:slug data))))
    (dissoc data :new)))

(defn- growth-clean-data [growth-data]
  (remove nil? (vec (map (fn [[_ v]] (growth-clean-row v)) growth-data))))

(defn- save-data [owner]
  ; (om/set-state! owner :has-changes false)
  (dis/dispatch! [:save-topic-data "growth" {:data (growth-clean-data (om/get-state owner :growth-data))}]))

(defn- replace-row-in-data [owner row k v]
  (let [new-row (update row k (fn[_]v))]
    (growth-change-data owner new-row)))

;; ===== Growth Metric Metadata Functions =====

(defn- current-metric-info [metric-slug data]
  (or (get (:metrics data) metric-slug) {}))

(defn- growth-change-metric
  ""
  [owner data slug properties-map]
  (let [metrics (or (:metrics data) {})
        metric (or (get metrics slug) {})
        new-metric (merge metric properties-map)
        new-metrics (assoc metrics slug new-metric)]
    (om/set-state! owner :growth-metrics new-metrics)))

(defn- set-metadata-edit [owner data editing]
  (om/set-state! owner :metadata-edit? editing))

(defn- metrics-as-sequence
  "We have metrics here as a map, but they need to be dispatched as a sequence."
  [owner data]
  (let [metric-map (om/get-state owner :growth-metrics)
        metric-slugs (:metric-slugs data)]
    (map metric-map metric-slugs)))

(defn- save-metadata-cb [owner data slug properties-map]
  ;; no longer editing
  (set-metadata-edit owner data false)
  ;; reflect edits in metric metadata in local state
  (growth-change-metric owner data slug properties-map)
  ;; dispatch the metric matadata change (for API update)
  (dis/dispatch! [:save-topic-data "growth" {:metrics (metrics-as-sequence owner data)}]))

;; ===== Growth Data Editing Component =====

(defcomponent growth-edit [{:keys [editing-cb show-first-edit-tip first-edit-tip-cb] :as data} owner options]

  (init-state [_]
    {:metadata-edit? false ; not editing metric metadata
     :growth-data (:growth-data data) ; all the growth data for all metrics
     :metric-slug (:initial-focus data) ; the slug of the current metric
     :new-metric false ; this metric hasn't been saved before
     :stop batch-size}) ; how many periods (reverse chronological) to show

  (will-receive-props [_ next-props]
    (when (not= (:growth-data data) (:growth-data next-props))
      (om/set-state! owner :growth-data (:growth-data next-props))))

  (render-state [_ {:keys [metadata-edit? growth-data metric-slug new-metric stop] :as state}]

    (let [company-slug (router/current-company-slug)
          metric-info (current-metric-info metric-slug data)
          slug (:slug metric-info)
          interval (:interval metric-info)
          unit (:unit metric-info)
          prefix (if (= unit "currency") (utils/get-symbol-for-currency-code (:currency options)) "")
          suffix (when (= unit "%") "%")]

      (dom/div {:class "growth"}
        (dom/div {:class "composed-section-edit growth-body edit"}

          ;; Show either meta-data editing form or growth metric data editing table
          (if metadata-edit?

            ;; Meta-data editing form
            (om/build growth-metric-edit {:metric-info metric-info
                                          :new-metric new-metric
                                          :metric-count (count (filter-growth-data metric-slug growth-data))
                                          :save-cb (partial save-metadata-cb owner data)
                                          :cancel-cb (fn []
                                                       (om/set-state! owner :new-metric false)
                                                       (set-metadata-edit owner data false))}
                                          ; :archive-metric-cb (fn [metric-slug]
                                          ;                      (set-metadata-edit owner data false)
                                          ;                      ((:delete-metric-cb data) metric-slug))
                                          {:opts {:currency (:currency options)}})
            
            (dom/div
              ;; Metric label and meta-data edit icon
              (when interval
                (dom/div {:class "metric-name"}
                  (:name metric-info)
                  (dom/button {:class "btn-reset metadata-edit-button"
                               :title "Edit this metric"
                               :type "button"
                               :data-toggle "tooltip"
                               :data-placement "right"
                               :style {:font-size "15px"}
                               :on-click #(set-metadata-edit owner data true)}
                    (dom/i {:class "fa fa-cog"}))))

              ;; Growth metric data editing table
              (when interval
                ;; Data editing table
                (dom/div {:class "table-container group"}
                  (dom/table {:class "table"
                              :key (str "growth-edit-" slug)}
                    ;; Table header
                    (dom/thead {}
                      (dom/tr {}
                        (dom/th {} "")
                        (dom/th {} "Value")))
                    ;; Table body
                    (dom/tbody {}
                      ;; For each period from the current, until as far in the past as the stop value
                      (let [current-period (utils/current-growth-period interval)]
                        (for [idx (range 1 stop)]
                          (let [period (growth-utils/get-past-period current-period idx interval)
                                has-value (contains? growth-data (str period slug))
                                row-data (if has-value
                                            (get growth-data (str period slug))
                                            {:period period
                                             :slug slug
                                             :value nil
                                             :new true})
                                next-period (growth-utils/get-past-period current-period (inc idx) interval)]
                            ;; A table row for this period
                            (om/build growth-edit-row {:cursor row-data
                                                       :next-period next-period
                                                       :is-last (= idx 0)
                                                       :needs-year (= idx (dec stop))
                                                       :prefix prefix
                                                       :suffix suffix
                                                       :interval interval
                                                       :change-cb (fn [k v]
                                                            (replace-row-in-data owner row-data k v))}))))
                      ;; Ending table row to paginate to more data in the table
                      (dom/tr {}
                        (dom/th {:class "earlier" :col-span 2}
                          (dom/a {:class "small-caps underline bold dimmed-gray" :on-click #(more-months owner data)} "Earlier..."))
                        (dom/td {}))))))

              ;; Save growth data and cancel edit buttons
              (when interval
                (dom/div {:class "topic-foce-footer group"}
                  (dom/div {:class "topic-foce-footer-right"}
                    (dom/button {:class "btn-reset btn-outline btn-data-save"
                                 :on-click  #(do
                                              (utils/event-stop %)
                                              (save-data owner)
                                              (editing-cb false))} "SAVE")
                    (dom/button {:class "btn-reset btn-outline"
                                 :on-click #(do
                                              (utils/event-stop %)
                                              (editing-cb false))} "CANCEL"))))

            ;; Onboarding toolip
            (when (:show-first-edit-tip data)
              (onboard-tip
                {:id (str "growth-topic-add-" company-slug)
                 :once-only true
                 :mobile false
                 :desktop "Add metrics you'd like to share and we'll build simple charts for you."
                 :dismiss-tip-fn (:first-edit-tip-cb data)})))))))))