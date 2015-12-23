(ns open-company-web.components.growth.growth-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.cell :refer (cell)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.growth.growth-metric-edit :refer (growth-metric-edit)]
            [open-company-web.components.utility-components :refer (editable-pen)]
            [cljs.core.async :refer (put!)]))

(defn signal-tab [period k]
  (let [ch (utils/get-channel (str period k))]
    (put! ch {:period period :key k})))

(defcomponent growth-edit-row [data _]

  (render [_]
    (let [growth-data (:cursor data)
          is-new (:new growth-data)
          value (:value growth-data)
          target (:target growth-data)
          interval (:interval data)
          period (:period growth-data)
          period-month (utils/get-month period interval)
          needs-year (:needs-year data)
          flags [:short (when needs-year :force-year)]
          period-string (utils/get-period-string period interval flags)
          cell-state (if is-new :new :display)
          change-cb (:change-cb data)
          next-period (:next-period data)
          tab-cb (fn [_ k]
                   (cond
                     (and (= k :target) (:is-last data))
                     (signal-tab next-period :target)
                     (= k :target)
                     (signal-tab (:period growth-data) :value)
                     (= k :value)
                     (when next-period
                       (signal-tab next-period :target))))]
      (dom/tr {:class "growth-edit-row"}
        (dom/td {:class "no-cell"}
          period-string)
        (dom/td {}
          (om/build cell {:value target
                          :placeholder "Target (optional)"
                          :cell-state cell-state
                          :draft-cb #(change-cb :target %)
                          :prefix (:prefix data)
                          :suffix (:suffix data)
                          :period period
                          :key :target
                          :tab-cb tab-cb}))
        (dom/td {}
          (when (not (:is-last data))
            (om/build cell {:value value
                            :placeholder "Value"
                            :cell-state cell-state
                            :draft-cb #(change-cb :value %)
                            :prefix (:prefix data)
                            :suffix (:suffix data)
                            :period period
                            :key :value
                            :tab-cb tab-cb})))))))

(defn next-period [data idx]
  (let [data (to-array data)]
    (if (< idx (dec (count data)))
      (let [next-row (get data (inc idx))]
        (:period next-row))
      nil)))

(defn replace-row-in-data [owner data metric-data row k v]
  "Find and replace the edited row"
  (let [array-data (js->clj (to-array metric-data))
        new-row (update row k (fn[_]v))]
    ((:change-growth-cb data) new-row)
    (loop [idx 0]
      (let [cur-row (get array-data idx)]
        (if (= (:period cur-row) (:period new-row))
          (let [new-rows (assoc array-data idx new-row)
                sort-pred (utils/sort-by-key-pred :period true)
                sorted-rows (sort #(sort-pred %1 %2) new-rows)]
            (om/update-state! owner :sorted-data (fn [_] sorted-rows)))
          (recur (inc idx)))))))

(defn get-current-metric-info [data]
  (let [metric-slug (:metric-slug data)
        metrics (:metrics data)]
    (or (get metrics metric-slug) {})))

(defn sort-growth-data [data]
  (let [metric-data (:growth-data data)
        metric-info (get-current-metric-info data)
        focus (:metric-slug data)]
    (if (or (= focus growth-utils/new-metric-slug-placeholder)
             (not (:interval metric-info)))
      (vec [])
      (let [placeholder-data (growth-utils/edit-placeholder-data metric-data focus (:interval metric-info))
            sorter (utils/sort-by-key-pred :period true)]
        (sort #(sorter %1 %2) placeholder-data)))))

(defn get-interval-batch-size [interval]
  (case interval
    "quarterly" 8
    "monthly" 8
    "weekly" 12))

(defn get-more [last-period interval]
  (vec
    (for [idx (range 1 (inc (get-interval-batch-size interval)))]
      (let [prev-period (growth-utils/get-past-period last-period idx interval)]
        {:period prev-period
         :value nil
         :target nil
         :new true}))))

(defn more-months [owner data]
  (let [sorted-data (om/get-state owner :sorted-data)
        last-data (last sorted-data)
        last-period (:period last-data)
        interval (:interval (get-current-metric-info data))
        more (get-more last-period interval)]
    (om/set-state! owner :sorted-data (concat sorted-data more))))

(defn set-metadata-edit [owner data editing]
  ((:metadata-edit-cb data) editing)
  (om/set-state! owner :metadata-edit editing))

(defcomponent growth-edit [data owner]

  (init-state [_]
    {:metadata-edit (:new-metric data)
     :sorted-data (sort-growth-data data)})

  (will-receive-props [_ next-props]
    (when (not= (:metrics data) (:metrcs next-props))
      (om/set-state! owner :sorted-data (sort-growth-data next-props))))

  (render [_]
    (let [metric-info (get-current-metric-info data)
          metric-data (om/get-state owner :sorted-data)
          prefix (utils/get-symbol-for-currency-code (:unit metric-info))
          suffix (when (= (:unit metric-info) "%") "%")
          rows-data (vec (map (fn [row]
                                (let [v {:prefix prefix
                                         :suffix suffix
                                         :interval (:interval metric-info)
                                         :change-cb (fn [k v]
                                                      (replace-row-in-data owner data metric-data row k v))
                                         :cursor row}]
                                  v))
                              metric-data))]
      (dom/div {:class "composed-section-edit growth-body edit"}
        (if (om/get-state owner :metadata-edit)
          (om/build growth-metric-edit {:metric-info metric-info
                                        :metric-count (:metric-count data)
                                        :metrics (:metrics data)
                                        :new-metric (:new-metric data)
                                        :new-growth-section (:new-growth-section data)
                                        :next-cb (fn []
                                                   (set-metadata-edit owner data false)
                                                   (when (:new-metric data)
                                                     ; delay focus on the first target
                                                     (.setTimeout js/window
                                                                  #(signal-tab (:period (:cursor (get rows-data 0))) :target)
                                                                  400)))
                                        :delete-metric-cb (:delete-metric-cb data)
                                        :cancel-cb (fn []
                                                     ; 3 cases
                                                     (if (or (:new-growth-section data) (:new-metric data))
                                                       ; newly added growth section
                                                       ;    - remove the seciton with (:delete-new-section-cb data)
                                                       ; new metric:
                                                       ;    - remove the new metric
                                                       ;    - switch focus
                                                       ((:cancel-cb data))
                                                       ; existing metric
                                                       (do
                                                         ; - cancel the edited metadata only
                                                         ((:reset-metrics-cb data))
                                                         ; - exit the metadata edit state
                                                         (set-metadata-edit owner data false))))
                                        :change-growth-metric-cb (:change-growth-metric-cb data)})
          (dom/div {}
            (dom/div {:class "chart-header-container"}
              (dom/div {:class "target-actual-container"}
                (dom/div {:class "actual-container"}
                  (dom/h3 {:class "actual blue"
                           :on-click #(set-metadata-edit owner data true)}
                    (str (:name metric-info) " ")
                    (om/build editable-pen {:click-callback #(set-metadata-edit owner data true)}))
                  (dom/h3 {:class "actual-label gray"} (str (utils/camel-case-str (:interval metric-info)) " " (utils/camel-case-str (:unit metric-info)))))))
            (dom/table {:class "table"}
              (dom/thead {}
                (dom/tr {}
                  (dom/th {} "")
                  (dom/th {} "Target")
                  (dom/th {} "Value")))
              (dom/tbody {}
                (for [idx (range (dec (count rows-data)))]
                  (let [row-data (get rows-data idx)
                        next-period (next-period metric-data idx)
                        row (merge row-data {:next-period next-period
                                             :is-last (= idx 0)
                                             :needs-year (or (= idx 0)
                                                             (= idx (- (count rows-data) 2)))})]
                    (om/build growth-edit-row row)))
                (dom/tr {}
                  (dom/td {}
                    (dom/a {:on-click #(more-months owner data)} "More..."))
                  (dom/td {})
                  (dom/td {}))))))))))