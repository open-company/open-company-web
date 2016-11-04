(ns open-company-web.components.finances.finances-sparklines
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.lib.oc-lib :as oc-lib]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.finance-utils :as finance-utils]
            [open-company-web.components.ui.d3-column-chart :refer (d3-column-chart)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [cuerdas.core :as s]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(def editing-actions-width 15)

(defn- format-delta
  "Create a display fragment for a delta value."

  ([delta]
  (let [pos (when (pos? delta) "+")]
    (dom/span {:class "open-sans"}
      pos
      (if (zero? delta) "no change" (str (oc-lib/with-size-label delta) "%")))))

  ([currency delta]
    (dom/span {:class "open-sans"}
      (if (zero? delta)
        "no change"
        (oc-lib/with-currency currency (oc-lib/with-size-label delta) true)))))

(defn- finance-metric-delta [periods data-key]
  (let [finance-metric (first periods)
        period (utils/date-from-period (:period finance-metric))
        date (when period (oc-lib/format-period :monthly period))
        value (get finance-metric data-key)
        ;; Check for older periods contiguous to most recent
        contiguous-periods (when (seq periods) (oc-lib/contiguous (map :period periods) :monthly))
        prior-contiguous? (>= (count contiguous-periods) 2)
        ;; Info on prior period
        prior-metric (when prior-contiguous?
                        (first (filter #(= (:period %) (second contiguous-periods)) periods)))
        prior-value (when prior-metric (get prior-metric data-key))
        metric-delta (when (and value prior-value) (- value prior-value))
        metric-delta-percent (when metric-delta (* 100 (float (/ metric-delta prior-value))))]
    ;; Format output
    (when metric-delta-percent
      (format-delta metric-delta-percent))))

(defn- get-metric-label [data-key actual-period data-set currency-symbol]
  (let [actual-set (first (filter #(= (:period %) actual-period) data-set))
        actual-val (data-key actual-set)
        metric-name (if (= data-key :costs) "Expenses" (s/capital (s/human data-key)))
        period (utils/get-period-string (:period data-set) "monthly" [:force-year])
        fixed-cur-unit currency-symbol]
    (when actual-val
      (dom/span {:class "open-sans"}
        (dom/span {:class "bold"}
          (dom/span fixed-cur-unit (oc-lib/with-size-label actual-val))
          " "
          metric-name)
        (cond
          (= data-key :cash)
          (when (neg? (:runway actual-set))
            (dom/span {} ", " (finance-utils/get-rounded-runway (:runway actual-set)) " runway"))
          :else
          (when-let [delta (finance-metric-delta data-set data-key)]
            (dom/span {} " (" delta ")")))))))

(defn- sub-label-from-set [period data-set currency-symbol]
  (let [idx (.indexOf (vec (map :period (reverse data-set))) period)
        periods (subvec (vec (reverse data-set)) idx)]
    (dom/div {}
      (dom/div {} (get-metric-label :revenue period periods currency-symbol))
      (dom/div {} (get-metric-label :costs period periods currency-symbol))
      (dom/div {} (get-metric-label :cash period periods currency-symbol)))))

(defn- label-from-set [data-set currency-symbol]
  (dom/span {:class "open-sans bold"} (utils/get-period-string (:period data-set) "monthly" [:force-year])))

(defn get-fixed-sorted-metric [finances-data currency]
  (let [currency-symbol (utils/get-symbol-for-currency-code currency)
        filled-finances-data (vals (finance-utils/fill-gap-months finances-data))
        sort-pred (utils/sort-by-key-pred :period)
        sorted-data (vec (sort sort-pred filled-finances-data))]
    (vec (map #(merge % {:label (label-from-set % currency-symbol)
                         :sub-label (sub-label-from-set (:period %) sorted-data currency-symbol)}) sorted-data))))

(defcomponent finances-sparklines [{:keys [finances-data currency archive-cb editing?] :as data} owner]

  (init-state [_]
    {:card-width (responsive/calc-card-width)
     :chart-selected-idx (min 3 (dec (count finances-data)))
     :fixed-sorted-metric (get-fixed-sorted-metric finances-data currency)})

  (will-receive-props [_ next-props]
    (when (not= finances-data (:finances-data next-props))
      (om/set-state! owner {:chart-selected-idx (min 3 (dec (count (:finances-data next-props))))
                            :card-width (responsive/calc-card-width)
                            :fixed-sorted-metric (get-fixed-sorted-metric (:finances-data next-props) currency)})))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :card-width (responsive/calc-card-width)))))

  (render-state [_ {:keys [card-width chart-selected-idx fixed-sorted-metric]}]
    (dom/div {:class (str "finances-sparklines group sparklines" (when (= (dis/foce-section-key) :finances) " editing"))}
      (dom/div {:class "finances-sparklines-inner left group"}
        (let [sum-revenues (apply + (map utils/abs (map :revenue finances-data)))
              post-revenue? (pos? sum-revenues)
              filled-metric-data (finance-utils/fill-gap-months finances-data)
              actual-set (first finances-data)
              period (utils/get-period-string (:period actual-set) "monthly" [:force-year])
              currency-symbol (utils/get-symbol-for-currency-code (:currency data))
              actual-with-label (label-from-set actual-set currency-symbol)
              ww (.-clientWidth (.-body js/document))
              total-card-width (if (>= ww responsive/c1-min-win-width) card-width ww)
              fixed-card-width (if (responsive/is-mobile-size?)
                                   total-card-width ; use all the possible space on mobile
                                   card-width)
              chart-opts {:opts {:chart-type "unbordered-chart"
                                 :chart-keys (if post-revenue? [:revenue :costs] [:costs])
                                 :interval "monthly"
                                 :x-axis-labels false
                                 :chart-colors (finance-utils/finances-key-colors false)
                                 :chart-selected-colors (finance-utils/finances-key-colors true)
                                 :chart-fill-polygons false
                                 :label-color (occ/get-color-by-kw :oc-gray-5)
                                 :sub-label-color (occ/get-color-by-kw :oc-gray-5)
                                 :sparklines-class "chart-sparklines finances-sparklines"
                                 :show-chart true
                                 :labels {:value {:position :bottom
                                                  :order 1
                                                  :value-presenter #(or (:label %2) "-")
                                                  :value (occ/get-color-by-kw :oc-blue-dark)
                                                  :label-presenter #(:sub-label %2)
                                                  :label-color (occ/get-color-by-kw :oc-gray-5)}}
                                 :hide-nav true}}]
          (dom/div {:class (utils/class-set {:section true
                                             :fake-chart (:fake-chart data)
                                             :read-only (:read-only data)})
                    :key "finances-sparklines"
                    :on-click (:start-editing-cb data)}

            (dom/div {}
              (om/build d3-column-chart {:chart-data fixed-sorted-metric
                                         :selected chart-selected-idx
                                         :selected-metric-cb #(om/set-state! owner :chart-selected-idx %)
                                         :chart-height 80
                                         :chart-width (- fixed-card-width 50 40 5)} chart-opts)))))
      (dom/div {:class "actions group right"}
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Edit chart"
           :on-click #(dis/dispatch! [:start-foce-data-editing true])}
          (dom/i {:class "fa fa-pencil"}))
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Remove this chart"
           :on-click archive-cb}
          (dom/i {:class "fa fa-times"}))))))