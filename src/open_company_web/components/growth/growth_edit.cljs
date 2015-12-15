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
          needs-year (or (= period-month "JAN")
                         (= period-month "DEC")
                         (:needs-year data))
          period-string (utils/period-string period (when needs-year :force-year))
          cell-state (if is-new :new :display)
          change-cb (:change-cb data)
          next-period (:next-period data)
          tab-cb (fn [_ k]
                   (cond
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
                          :placeholder "target"
                          :cell-state cell-state
                          :draft-cb #(change-cb :target %)
                          :period period
                          :key :target
                          :tab-cb tab-cb}))
        (dom/td {}
          (when (not (:is-last data))
            (om/build cell {:value value
                            :placeholder "value"
                            :cell-state cell-state
                            :draft-cb #(change-cb :value %)
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

(defn sort-growth-data [data]
  (let [metric-data (vec (vals (:growth-data data)))
        sorter (utils/sort-by-key-pred :period true)]
    (sort #(sorter %1 %2) metric-data)))

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
        interval (:interval (om/get-state owner :metric-info))
        more (get-more last-period interval)]
    (om/set-state! owner :sorted-data (concat sorted-data more))))

(defcomponent growth-edit [data owner]

  (init-state [_]
    (let [metric-slug (:metric-slug data)
          metrics (:metrics data)
          sorted-data (sort-growth-data data)]
      {:sorted-data sorted-data
       :metric-slug metric-slug
       :metric-info (first (filter #(= (:slug %) metric-slug) metrics))
       :metric-edit false}))

  (render [_]
    (let [metric-info (om/get-state owner :metric-info)
          metric-data (om/get-state owner :sorted-data)
          rows-data (vec (map (fn [row]
                                (let [v {:prefix (:unit metric-info)
                                         :interval (:interval metric-info)
                                         :change-cb (fn [k v]
                                                      (replace-row-in-data owner data metric-data row k v))
                                         :cursor row}]
                                  v))
                              metric-data))]
      (dom/div {:class "composed-section-edit growth-body edit"}
        (if (om/get-state owner :metric-edit)
          (om/build growth-metric-edit {:metric-info metric-info})
          (dom/div {}
            (dom/div {:class "chart-header-container"}
              (dom/div {:class "target-actual-container"}
                (dom/div {:class "actual-container"}
                  (dom/h3 {:class "actual blue"} (str (:name metric-info) " ")
                    (om/build editable-pen {:click-callback #(om/set-state! owner :metric-edit true)}))
                  (dom/h3 {:class "actual-label gray"} (str (utils/camel-case-str (:interval metric-info)) " " (utils/camel-case-str (:unit metric-info)))))))
            (dom/table {:class "table table-striped"}
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
                                                              (= idx (dec (count rows-data))))})]
                    (om/build growth-edit-row row)))
                (dom/tr {}
                  (dom/td {}
                    (dom/a {:on-click #(more-months owner data)} "More..."))
                  (dom/td {})
                  (dom/td {}))))))))))