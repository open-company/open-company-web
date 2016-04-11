(ns open-company-web.components.finances.finances-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.components.ui.cell :refer (cell)]
            [open-company-web.components.finances.utils :as finances-utils]
            [cljs.core.async :refer (put!)]))

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
          tab-cb (fn [_ k]
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
                      (str burn-prefix (utils/thousands-separator (utils/abs burn))))
          runway-days (:runway finances-data)
          runway (cond
                   (nil? runway-days) "calculated"
                   (or (not (:cash finances-data))
                       (not (:costs finances-data))) ""
                   (zero? runway-days) "break-even"
                   (pos? runway-days) "profitable"
                   :else (finances-utils/get-rounded-runway runway-days)) 
          ref-prefix (str (:period finances-data) "-")
          period-month (utils/get-month period)
          needs-year (or (= period-month "JAN")
                         (= period-month "DEC")
                         (:needs-year data))]
      (dom/tr {}
        (dom/td {:class "no-cell"}
          (utils/get-period-string (:period finances-data) "monthly" (when needs-year [:force-year])))
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

(defn replace-row-in-data [data row k v]
  "Find and replace the edited row"
  (let [new-row (update row k (fn[_]v))]
    ((:change-finances-cb data) new-row)))

(def batch-size 6)

(defn more-months [owner]
  (om/update-state! owner :stop #(+ % batch-size)))

(defcomponent finances-edit [data owner]

  (init-state [_]
    {:finances-data (:finances-data data)
     :stop batch-size})

  (will-receive-props [_ next-props]
    (when-not (= (:finances-data data) (:finances-data next-props))
      (om/set-state! owner :finances-data (:finances-data next-props))))

  (render-state [_ {:keys [finances-data stop]}]
    (let [currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          show-burn (some #(pos? (:revenue %)) finances-data)]
      ; real component
      (dom/div {:class "finances"}
        (dom/div {:class "composed-section-edit finances-body edit"}
          (dom/table {:class "table"}
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
              (let [current-period (utils/current-period)]
                (for [idx (range stop)]
                  (let [period (finances-utils/get-past-period current-period idx)
                        has-value (contains? finances-data period)
                        row-data (if has-value
                                    (get finances-data period)
                                    (finances-utils/placeholder-data period))
                        next-period (finances-utils/get-past-period current-period (inc idx))]
                    (om/build finances-edit-row {:cursor row-data
                                                 :next-period next-period
                                                 :is-last (= idx 0)
                                                 :needs-year (or (= idx 0)
                                                                 (= idx (dec stop)))
                                                 :prefix cur-symbol
                                                 :show-burn show-burn
                                                 :change-cb #(replace-row-in-data data row-data %1 %2)}))))
              (dom/tr {}
                (dom/td {}
                  (dom/a {:on-click #(more-months owner)} "More..."))
                (dom/td {})
                (dom/td {})
                (dom/td {})
                (dom/td {})
                (when show-burn
                  (dom/th {} ""))
                (dom/td {})))))))))