(ns open-company-web.components.finances.finances-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.components.ui.cell :refer (cell)]
            [open-company-web.lib.finance-utils :as finance-utils]
            [cljs.core.async :refer (put!)]))

(def batch-size 5)

(defn signal-tab [period k]
  (let [ch (utils/get-channel (str period k))]
    (put! ch {:period period :key k})))

(defcomponent finances-edit-row [data owner]
  
  (render [_]
    (let [currency (:currency data)
          prefix (:prefix data)
          finances-data (:cursor data)
          period (:period finances-data)
          is-new (:new finances-data)
          cell-state (if is-new :new :display)
          change-cb (:change-cb data)
          next-period (:next-period data)
          tab-cb (fn [_ k]
                   (cond
                     (= k :revenue)
                     (signal-tab (:period finances-data) :costs)
                     (= k :costs)
                     (signal-tab (:period finances-data) :cash)
                     (= k :cash)
                     (when next-period
                        (signal-tab next-period :revenue))))
          burn (- (:revenue finances-data) (:costs finances-data))
          burn-prefix (if (or (zero? burn) (pos? burn)) prefix (str "-" prefix))
          burn-rate (if (js/isNaN burn)
                      "-"
                      (if (zero? burn)
                        (str burn-prefix "-")
                        (str burn-prefix (utils/thousands-separator (utils/abs burn) currency 0))))
          ref-prefix (str (:period finances-data) "-")
          period-month (utils/get-month period)
          needs-year (or (= period-month "JAN")
                         (:needs-year data))]
      (dom/tbody {}
        (dom/tr {}
          (dom/th {:class "no-cell"}
            (utils/get-period-string (:period finances-data) "monthly" [:short :skip-year]))
          ;; revenue
          (dom/td {}
            (om/build cell {:value (:revenue finances-data)
                            :decimals 0
                            :positive-only true
                            :short true
                            :currency currency
                            :cell-state cell-state
                            :draft-cb #(change-cb :revenue %)
                            :period period
                            :key :revenue
                            :tab-cb tab-cb}))
          ;; costs
          (dom/td {}
            (om/build cell {:value (:costs finances-data)
                            :decimals 0
                            :positive-only true
                            :short true
                            :currency currency
                            :cell-state cell-state
                            :draft-cb #(change-cb :costs %)
                            :period period
                            :key :costs
                            :tab-cb tab-cb}))
          ;; cash
          (dom/td {}
            (om/build cell {:value (:cash finances-data)
                            :decimals 0
                            :positive-only false
                            :short true
                            :placeholder "" ; (if is-new "on SEP 30" "")
                            :currency currency
                            :cell-state cell-state
                            :draft-cb #(change-cb :cash %)
                            :period period
                            :key :cash
                            :tab-cb tab-cb})))
        (when needs-year
          (dom/tr {}
            (dom/th {:class "no-cell year"}
              (utils/get-year period))
            (dom/td {:class "no-cell"})
            (dom/td {:class "no-cell"})
            (dom/td {:class "no-cell"})))))))

(defn finances-get-value [v]
  (if (js/isNaN v)
    0
    v))

(defn- finances-check-value
  "Return true if the value is a number."
  [v]
  (and (not (= v ""))
       (not (nil? v))
       (not (js/isNaN v))))


(defn finances-fix-row [row]
  (let [fixed-cash (update-in row [:cash] finances-get-value)
        fixed-revenue (assoc fixed-cash :revenue (finances-get-value (:revenue row)))
        fixed-costs (assoc fixed-revenue :costs (finances-get-value (:costs row)))
        fixed-burnrate (assoc fixed-costs :burn-rate (utils/calc-burn-rate (:revenue fixed-costs) (:costs fixed-costs)))
        fixed-runway (assoc fixed-burnrate :runway (utils/calc-runway (:cash fixed-burnrate) (:burn-rate fixed-burnrate)))]
    fixed-runway))

(defn finances-row-has-data [row]
  (or (finances-check-value (:cash row))
      (finances-check-value (:costs row))
      (finances-check-value (:revenue row))))

(defn change-finances-data
  "Update the local state of growth data with change from the user."
  [owner row]
  (let [has-data? (finances-row-has-data row)
        fixed-row (when has-data? (finances-fix-row row))
        period (:period row)
        finances-data (om/get-state owner :finances-data)
        fixed-data (if has-data? 
                      (assoc finances-data period fixed-row)
                      (dissoc finances-data period))]
    ;(om/set-state! owner :has-changes (or (om/get-state owner :has-changes) (not= finances-data fixed-data)))
    (om/set-state! owner :finances-data fixed-data)))

(defn finances-clean-row [data]
  ; a data entry is good if we have the period and one other value: cash, costs or revenue
  (when (and (not (nil? (:period data)))
             (or (not (nil? (:cash data)))
                 (not (nil? (:costs data)))
                 (not (nil? (:revenue data)))))
    (dissoc data :burn-rate :runway :new :value)))

(defn finances-clean-data [finances-data]
  (remove nil? (vec (map (fn [[_ v]] (finances-clean-row v)) finances-data))))

(defn- save-data [owner]
  ; (om/set-state! owner :has-changes false)
  (dis/dispatch! [:save-topic-data "finances" {:data (finances-clean-data (om/get-state owner :finances-data))
                                               :placeholder false}]))

(defn replace-row-in-data [owner row-data k v]
  "Find and replace the edited row"
  (let [new-row (update row-data k (fn[_]v))]
    (change-finances-data owner new-row)))

(defn more-months [owner]
  (om/update-state! owner :stop #(+ % batch-size)))

(defcomponent finances-edit [{:keys [currency editing-cb show-first-edit-tip first-edit-tip-cb] :as data} owner]

  (init-state [_]
    {:finances-data (:finances-data data)
     :stop batch-size})

  (will-receive-props [_ next-props]
    (om/set-state! owner :finances-data (:finances-data next-props)))

  (render-state [_ {:keys [finances-data stop]}]

    (let [company-slug (router/current-company-slug)]

      (dom/div {:class "finances"}
        (dom/div {:class "composed-section-edit finances-body edit"}
          (dom/div {:class "table-container group"}
            (dom/table {:class "table"}
              (dom/thead {}
                (dom/tr {}
                  (dom/th {} "")
                  (dom/th {} "Revenue")
                  (dom/th {} "Expenses")
                  (dom/th {} "Cash")))
              (let [current-period (utils/current-period)]
                (for [idx (range stop)]
                  (let [period (finance-utils/get-past-period current-period idx)
                        has-value (contains? finances-data period)
                        row-data (if has-value
                                    (get finances-data period)
                                    (finance-utils/placeholder-data period {:new true}))
                        next-period (finance-utils/get-past-period current-period (inc idx))]
                    (om/build finances-edit-row {:cursor row-data
                                                 :next-period next-period
                                                 :is-last (= idx 0)
                                                 :needs-year (= idx (dec stop))
                                                 :currency currency
                                                 :change-cb #(replace-row-in-data owner row-data %1 %2)}))))
              (dom/tfoot {}
                (dom/tr {}
                  (dom/th {:class "earlier" :col-span 2}
                    (dom/a {:class "small-caps underline bold dimmed-gray" :on-click #(more-months owner)} "Earlier..."))
                  (dom/td {})
                  (dom/td {})))))

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
                                        (editing-cb false))} "CANCEL"))))))))