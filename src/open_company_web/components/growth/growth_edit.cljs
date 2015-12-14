(ns open-company-web.components.growth.growth-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.cell :refer (cell)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.growth.utils :as growth-utils]))

(defcomponent growth-edit-row [data owner]
  (render [_]
    (let [prefix (:prefix data)
          cursor (:cursor data)
          is-new (:new cursor)
          value (:value cursor)
          target (:target cursor)
          interval (:interval data)
          period (:period cursor)
          period-month (utils/get-month period interval)
          needs-year (or (= period-month "JAN")
                         (= period-month "DEC")
                         (:needs-year data))
          period-string (utils/period-string period (when needs-year :force-year))
          cell-state (if is-new :new :display)
          change-cb (:change-cb data)]
      (dom/tr {:class "growth-edit-row"}
        (dom/td {:class "no-cell"}
          period-string)
        (dom/td {}
          (om/build cell {:value target
                          :placeholder "Target for the interval"
                          :cell-state cell-state
                          :draft-cb #(change-cb :target %)
                          :period period
                          :key :target}))
        (dom/td {}
          (om/build cell {:value value
                          :placeholder "Value for the interval"
                          :cell-state cell-state
                          :draft-cb #(change-cb :target %)
                          :period period
                          :key :value}))))))

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
  (let [metric-slug (:metric-slug data)
        all-data (:data (:section-data data))
        metric-data (filter #(= (:slug %) metric-slug) all-data)
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
          metrics (:metrics (:section-data data))]
      {:sorted-data (sort-growth-data data)
       :metric-slug metric-slug
       :metric-info (first (filter #(= (:slug %) metric-slug) metrics))}))

  (render [_]
    (let [{:keys [section-data metric-slug]} data
          {metrics :metrics growth-data :data} section-data
          metric-info (first (filter #(= (:slug %) metric-slug) metrics))
          metric-data (om/get-state owner :sorted-data)
          rows-data (vec (map (fn [row]
                                (let [v {:prefix (:unit metric-info)
                                         :interval (:interval metric-info)
                                         :change-cb (fn [k v]
                                                      (replace-row-in-data owner data metric-data row k v))
                                         :cursor row}]
                                  v))
                              metric-data))]
      (dom/div {:class "growth-body finances-body edit"}
        (dom/table {:class "table table-striped"}
          (dom/thead {}
            (dom/tr {}
              (dom/th {} "")
              (dom/th {} "Target")
              (dom/th {} "Value")))
          (dom/tbody {}
            (for [idx (range (count rows-data))]
              (let [row-data (get rows-data idx)
                    next-period (next-period metric-data idx)
                    row (merge row-data {:next-period next-period
                                         :needs-year (or (= idx 0)
                                                          (= idx (dec (count rows-data))))})]
                (om/build growth-edit-row row)))
            (dom/tr {}
              (dom/td {}
                (dom/a {:on-click #(more-months owner data)} "More..."))
              (dom/td {})
              (dom/td {}))))))))