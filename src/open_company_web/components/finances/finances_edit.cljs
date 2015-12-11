(ns open-company-web.components.finances.finances-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.cell :refer [cell]]
            [open-company-web.components.finances.utils :as finances-utils]
            [cljs.core.async :refer [put!]]))

(defn signal-tab [period k]
  (let [ch (utils/get-channel (str period k))]
    (put! ch {:period period :key k})))

(defcomponent finances-edit-row [data owner]
  
  (render [_]
    (let [prefix (:prefix data)
          finances-data (:cursor data)
          period (:period finances-data)
          is-new (:new finances-data)
          cell-state (if is-new :new :display)
          change-cb (:change-cb data)
          next-period (:next-period data)
          tab-cb (fn [period k]
                   (cond
                     (= k :cash)
                     (signal-tab (:period finances-data) :revenue)
                     (= k :revenue)
                     (signal-tab (:period finances-data) :costs)
                     (= k :costs)
                     (when next-period
                       (signal-tab next-period :cash))))
          burn (- (:revenue finances-data) (:costs finances-data))
          burn-prefix (if (neg? burn) (str "-" prefix) prefix)
          burn-rate (if (js/isNaN burn)
                      "calculated"
                      (str burn-prefix (.toLocaleString (utils/abs burn))))
          runway-days (:runway finances-data)
          runway (cond
                   (nil? runway-days) "calculated"
                   (zero? runway-days) "break-even"
                   (pos? runway-days) "profitable"
                   :else (str (.toLocaleString (utils/abs runway-days)) " days"))
          ref-prefix (str (:period finances-data) "-")
          period-month (utils/get-month period)
          needs-year (or (= period-month "JAN")
                         (= period-month "DEC")
                         (:needs-month data))]
      (dom/tr {}
        (dom/td {:class "no-cell"} (utils/period-string (:period finances-data) (when needs-year :force-year)))
        ;; cash
        (dom/td {}
          (om/build cell {:value (:cash finances-data)
                          :placeholder (if is-new "at month end" "")
                          :prefix prefix
                          :cell-state cell-state
                          :draft-cb #(change-cb :cash %)
                          :period period
                          :key :cash
                          :tab-cb tab-cb}))
        ;; revenue
        (dom/td {}
          (om/build cell {:value (:revenue finances-data)
                          :placeholder (if is-new "entire month" "")
                          :prefix prefix
                          :cell-state cell-state
                          :draft-cb #(change-cb :revenue %)
                          :period period
                          :key :revenue
                          :tab-cb tab-cb}))
        ;; costs
        (dom/td {}
          (om/build cell {:value (:costs finances-data)
                          :placeholder (if is-new "entire month" "")
                          :prefix prefix
                          :cell-state cell-state
                          :draft-cb #(change-cb :costs %)
                          :period period
                          :key :costs
                          :tab-cb tab-cb}))
        ;; Burn
        (when (:show-burn data)
          (dom/td {:class (utils/class-set {:no-cell true :new-row-placeholder is-new})}
            burn-rate))
        ;; Runway
        (dom/td {:class (utils/class-set {:no-cell true :new-row-placeholder is-new})}
                runway)))))

(defn replace-row-in-data [owner data finances-data row k v]
  "Find and replace the edited row"
  ((:change-finances-cb data) (update row k (fn[_]v)))
  (let [array-data (js->clj (to-array finances-data))
        new-row (update row k (fn[_]v))]
    (loop [idx 0]
      (let [cur-row (get array-data idx)]
        (if (= (:period cur-row) (:period new-row))
          (let [new-rows (assoc array-data idx new-row)
                runway-rows (utils/calc-burnrate-runway new-rows)
                sort-pred (utils/sort-by-key-pred :period true)
                sorted-rows (sort #(sort-pred %1 %2) runway-rows)]
            (om/update-state! owner :sorted-data (fn [_] sorted-rows)))
          (recur (inc idx)))))))

(defn next-period [data idx]
  (let [data (to-array data)]
    (if (< idx (dec (count data)))
      (let [next-row (get data (inc idx))]
        (:period next-row))
      nil)))

(defn sort-finances-data [data]
  (let [data-vec (vec (vals data))
        runway-rows (utils/calc-burnrate-runway data-vec)
        sorter (utils/sort-by-key-pred :period true)]
    (sort #(sorter %1 %2) runway-rows)))

(defn get-12-months [last-period]
  (vec
    (for [idx (range 1 13)]
      (let [prev-period (finances-utils/get-past-period last-period idx)]
        {:period prev-period
         :cash nil
         :revenue nil
         :costs nil
         :burn-rate nil
         :avg-burn-rate nil
         :runway nil
         :value nil
         :new true}))))


(defn more-12-month [owner]
  (let [sorted-data (om/get-state owner :sorted-data)
        last-data (last sorted-data)
        last-period (:period last-data)
        more-12 (get-12-months last-period)]
    (om/set-state! owner :sorted-data (concat sorted-data more-12))))

(defcomponent finances-edit [data owner]

  (init-state [_]
    {:sorted-data (sort-finances-data (:finances-data data))})

  (render [_]
    (let [finances-data (om/get-state owner :sorted-data)
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          show-burn (some #(pos? (:revenue %)) finances-data)
          rows-data (vec (map (fn [row]
                                (let [v {:prefix cur-symbol
                                         :show-burn show-burn
                                         :change-cb (fn [k v]
                                                      (replace-row-in-data owner data finances-data row k v))
                                         :cursor row}]
                                  v))
                              finances-data))]
      ; real component
      (dom/div {:class "finances"}
        (dom/div {:class "finances-body edit"}
          (dom/table {:class "table table-striped"}
            (dom/thead {}
              (dom/tr {}
                (dom/th {} "")
                (dom/th {} "Cash")
                (dom/th {} "Revenue")
                (dom/th {} "Costs")
                (when show-burn
                  (dom/th {} "Burn"))
                (dom/th {} "Runway")))
            (dom/tbody {}
              (for [idx (range (count rows-data))]
                (let [row-data (get rows-data idx)
                      next-period (next-period finances-data idx)
                      row (merge row-data {:next-period next-period
                                           :needs-month (or (= idx 0)
                                                            (= idx (dec (count rows-data))))})]
                  (om/build finances-edit-row row)))
              (dom/tr {}
                (dom/td {}
                  (dom/a {:on-click #(more-12-month owner)} "More..."))
                (dom/td {})
                (dom/td {})
                (dom/td {})
                (dom/td {})
                (when show-burn
                  (dom/th {} ""))
                (dom/td {})))))))))