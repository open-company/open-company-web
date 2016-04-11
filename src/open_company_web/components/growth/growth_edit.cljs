(ns open-company-web.components.growth.growth-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.cell :refer (cell)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.growth.growth-metric-edit :refer (growth-metric-edit)]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [cljs.core.async :refer (put!)]))

(defn signal-tab [period k]
  (when-let [ch (utils/get-channel (str period k))]
    (put! ch {:period period :key k})))

(defcomponent growth-edit-row [data _]

  (render [_]
    (let [growth-data (:cursor data)
          is-new (and (not (:value growth-data)) (not (:target growth-data)) (:new growth-data))
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
          (when-not (:is-last data)
            (om/build
              cell
              {:value value
               :placeholder "Value"
               :cell-state cell-state
               :draft-cb #(change-cb :value %)
               :prefix (:prefix data)
               :suffix (:suffix data)
               :period period
               :key :value
               :tab-cb tab-cb})))))))

(defn replace-row-in-data [owner data metric-data row k v]
  "Find and replace the edited row"
  (let [array-data (vec (js->clj metric-data))
        new-row (update row k (fn[_]v))]
    ((:change-growth-cb data) new-row)
    (doseq [cur-row array-data]
      (when (= (:period cur-row) (:period new-row))
        (let [idx (.indexOf (to-array array-data) cur-row)
              new-rows (assoc array-data idx new-row)
              sort-pred (utils/sort-by-key-pred :period true)
              sorted-rows (sort sort-pred new-rows)]
          (om/update-state! owner :metric-data (fn [_] sorted-rows)))))))

(defn get-current-metric-info [data]
  (let [metric-slug (:metric-slug data)
        metrics (:metrics data)]
    (or (get metrics metric-slug) {})))

(defn sort-growth-data [data]
  (let [metric-data (:growth-data data)
        sorter (utils/sort-by-key-pred :period true)
        sorted-metric-data (sort sorter metric-data)]
    sorted-metric-data))

(def batch-size 6)

(defn more-months [owner data]
  (let [metric-info (get-current-metric-info data)]
    (om/update-state! owner :stop #(+ % batch-size))))

(defn set-metadata-edit [owner data editing]
  ((:metadata-edit-cb data) editing)
  (om/set-state! owner :metadata-edit editing))

(defcomponent growth-edit [data owner]

  (init-state [_]
    (let [metric-info (get-current-metric-info data)]
      {:metadata-edit (:new-metric data)
       :growth-data (:growth-data data)
       :stop batch-size}))

  (will-receive-props [_ next-props]
    (when (not= (:growth-data data) (:growth-data next-props))
      (om/set-state! owner :metadata-edit (:new-metric next-props))
      (om/set-state! owner :growth-data (:growth-data next-props))))

  (render-state [_ {:keys [growth-data metric-data metadata-edit stop]}]
    (let [{:keys [interval slug] :as metric-info} (get-current-metric-info data)
          company-slug (keyword (:slug @router/path))
          company-data (company-slug @dispatcher/app-state)
          prefix (if (= (:unit metric-info) "currency")
                   (utils/get-symbol-for-currency-code (:currency company-data))
                   "")
          suffix (when (= (:unit metric-info) "%") "%")]
      (dom/div {:class "composed-section-edit growth-body edit"}
        (if metadata-edit
          (om/build growth-metric-edit {:metric-info metric-info
                                        :metric-count (:metric-count data)
                                        :metrics (:metrics data)
                                        :new-metric (:new-metric data)
                                        :new-growth-section (:new-growth-section data)
                                        :next-cb #(set-metadata-edit owner data false)
                                        :delete-metric-cb (fn [metric-slug]
                                                           (om/set-state! owner :metadata-edit false)
                                                           ((:delete-metric-cb data) metric-slug))
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
                (let [current-period (utils/current-growth-period interval)]
                  (for [idx (range stop)]
                    (let [period (growth-utils/get-past-period current-period idx interval)
                          has-value (contains? growth-data (str period slug))
                          row-data (if has-value
                                      (get growth-data (str period slug))
                                      {:period period
                                       :slug slug
                                       :value nil
                                       :target nil
                                       :new true})
                          next-period (growth-utils/get-past-period current-period (inc idx) interval)]
                      (om/build growth-edit-row {:cursor row-data
                                                 :next-period next-period
                                                 :is-last (= idx 0)
                                                 :needs-year (or (= idx 0)
                                                                 (= idx (dec stop)))
                                                 :prefix prefix
                                                 :suffix suffix
                                                 :interval interval
                                                 :change-cb (fn [k v]
                                                      (replace-row-in-data owner data metric-data row-data k v))}))))
                  (dom/tr {}
                    (dom/td {}
                      (dom/a {:on-click #(more-months owner data)} "More..."))
                    (dom/td {})
                    (dom/td {}))))))))))