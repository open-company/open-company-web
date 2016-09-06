(ns open-company-web.components.finances.finances-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.components.ui.cell :refer (cell)]
            [open-company-web.components.ui.onboard-tip :refer (onboard-tip)]
            [open-company-web.lib.finance-utils :as finance-utils]
            [cljs.core.async :refer (put!)]))

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
                         (= period-month "DEC")
                         (:needs-year data))]
      [(dom/tr {}
        (dom/th {:class "no-cell"}
          (utils/get-period-string (:period finances-data) "monthly" [:short]))
        ;; revenue
        (dom/td {}
          (om/build cell {:value (:revenue finances-data)
                          :decimals 0
                          :positive-only true
                          :placeholder (if is-new "entire month" "")
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
                          :placeholder (if is-new "entire month" "")
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
                          :placeholder (if is-new "month end" "")
                          :currency currency
                          :cell-state cell-state
                          :draft-cb #(change-cb :cash %)
                          :period period
                          :key :cash
                          :tab-cb tab-cb})))
      (when needs-year
        (dom/tr {}
          (dom/th {:class "no-cell year"}
            "2016")
          (dom/td {:class "no-cell"})
          (dom/td {:class "no-cell"})
          (dom/td {:class "no-cell"})
          )
        )]
      )))

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
    (let [company-slug (router/current-company-slug)
          currency (:currency data)]

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
              (dom/tbody {}
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
                                                   :change-cb #(replace-row-in-data data row-data %1 %2)}))))
                (dom/tr {}
                  (dom/th {:col-span 2}
                    (dom/a {:class "small-caps underline bold dimmed-gray" :on-click #(more-months owner)} "Earlier..."))
                  (dom/td {})
                  (dom/td {})))))

          (dom/div {:class "topic-foce-footer group"}
            (dom/div {:class "topic-foce-footer-right"}
              (dom/button {:class "btn-reset btn-solid"
                           :on-click  #(do
                                        (utils/event-stop %)
                                        (.log js/console "save"))} "SAVE")
              (dom/button {:class "btn-reset btn-outline"
                           :on-click #(do
                                        (utils/event-stop %)
                                        (.log js/console "cancel"))} "CANCEL"))))

        ;; Onboarding toolip
        (when (:show-first-edit-tip data)
          (onboard-tip
            {:id (str "finance-topic-add-" company-slug)
             :once-only true
             :mobile false
             :desktop "Enter revenue, expenses and cash to create a simple chart."
             :dismiss-tip-fn (:first-edit-tip-cb data)}))))))