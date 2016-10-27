(ns open-company-web.components.growth.growth-edit
  (:require [clojure.string :as string]
            [cuerdas.core :as s]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.components.ui.cell :refer (cell)]
            [open-company-web.components.growth.growth-metric-edit :refer (growth-metric-edit)]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [cljs.core.async :refer (put!)]))

(def batch-size 5)

;; ===== Growth Data Row Editing Component (Single Row in the Data Table) =====

(defn- signal-tab [period k]
  (when-let [ch (utils/get-channel (str period k))]
    (put! ch {:period period :key k})))

(defcomponent growth-edit-row [{:keys [interval is-last change-cb next-period prefix suffix] :as data} owner]


  (render [_]

    (let [growth-data (:cursor data)
          is-new (and (not (:value growth-data)) (:new growth-data))
          value (:value growth-data)
          period (:period growth-data)
          flags [:short :skip-year]
          period-string (utils/get-period-string period interval flags)
          cell-state (if is-new :new :display)
          tab-cb (fn [_ k]
                   (cond
                     (= k :value)
                     (when next-period
                       (signal-tab next-period :value))))
          needs-year? (or (:needs-year data)
                          (and (not= interval "weekly")
                               (= (utils/get-month period interval) "JAN")))]

      (dom/tbody {}
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
                 :tab-cb tab-cb}))))

        (when needs-year?
          (dom/tr {}
            (dom/th {:class "no-cell year"}
              (utils/get-year period interval))
            (dom/td {:class "no-cell"})))))))

;; ===== Growth Metric Metadata Functions =====

(defn- current-metric-info [owner metric-slug]
  (get (om/get-state owner :metrics) metric-slug))

(defn- growth-change-metric
  ""
  [owner slug properties-map]
  (let [metrics (or (om/get-state owner :metrics) {})
        metric (or (get metrics slug) {})
        new-metric (merge metric properties-map)
        new-metrics (assoc metrics slug new-metric)]
    (om/set-state! owner :metrics new-metrics)))

(defn- new-metric-slug [metric-name]
  (s/slug (str metric-name " " (utils/my-uuid))))

;; ===== Growth Metric Data Functions =====

(defn- growth-get-value
  "Return a blank string, 0 or the value."
  [v]
  (if (string/blank? v)
    ""
    (if (js/isNaN v)
      0
      v)))

(defn- growth-fix-row
  "Fix the value in the row to be missing, numeric, or 0"
  [row]
  (let [fixed-value (growth-get-value (:value row))
        with-fixed-value (if (string/blank? fixed-value)
                           (dissoc row :value)
                           (assoc row :value fixed-value))]
    with-fixed-value))

(defn- growth-check-value
  "Return true if the value is a number."
  [v]
  (and (not (= v ""))
       (not (nil? v))
       (not (js/isNaN v))))

(defn- growth-change-data
  "Update the local state of growth data with change from the user."
  [owner row]
  (let [{:keys [period slug value] :as fixed-row} (growth-fix-row row) ; fix up this period's value if it needs it
        growth-data (om/get-state owner :growth-data) ; current state of the data
        ;; update the data
        fixed-data (if value
                     (assoc growth-data (str period slug) fixed-row)
                     (dissoc growth-data (str period slug)))
        data-on-change-cb (om/get-props owner :data-on-change-cb)]
    (om/set-state! owner :has-changes? true)
    (om/set-state! owner :growth-data fixed-data)
    (data-on-change-cb fixed-data)))

(defn- more-months [owner data]
  (om/update-state! owner :stop #(+ % batch-size)))

(defn- filter-growth-data [metric-slug growth-data]
  (into {} (filter (fn [[k v]] (= (:slug v) metric-slug)) growth-data)))

(defn- growth-clean-row [data]
  ; a data entry is good if we have the period value
  (when (and (not (nil? (:period data)))
             (not (nil? (:slug data))))
    (dissoc data :new)))

(defn- growth-clean-data [growth-data new-slug]
  (let [fixed-growth-data (apply merge (map (fn [[k v]]
                                              (hash-map
                                                (if (= (:slug v) growth-utils/new-metric-slug-placeholder)
                                                  (str (:period v) new-slug)
                                                  k)
                                                (if (= (:slug v) growth-utils/new-metric-slug-placeholder)
                                                  (assoc v :slug new-slug)
                                                  v))) growth-data))]
    (vec (remove nil? (map (fn [[_ v]] (growth-clean-row v)) fixed-growth-data)))))

(defn- save-data [owner data new-metric?]
  (let [metric-slug (om/get-state owner :metric-slug)
        metadata-map (current-metric-info owner metric-slug)]
    ; if it's a new metric
    (if new-metric?
      ; create a unique slug and update the metadata and the current focus
      (let [new-slug (new-metric-slug (:name metadata-map))
            metadata-on-change-cb (om/get-props owner :metadata-on-change-cb)] ; new slug for a new metric
        (growth-change-metric owner new-slug (assoc metadata-map :slug new-slug))
        (om/set-state! owner :metric-slug new-slug)
        (metadata-on-change-cb :slug new-slug)) ; now data editing the new metric
      (growth-change-metric owner metric-slug metadata-map))
    ; reset the has-changes? flag
    (om/set-state! owner :has-changes? false)
    ; save all the metric data
    (let [new-slug (om/get-state owner :metric-slug)
          data-map {:data (growth-clean-data (om/get-state owner :growth-data) new-slug)}
          metrics-map (om/get-state owner :metrics)
          new-metrics-map (if new-metric?
                            (assoc (dissoc metrics-map growth-utils/new-metric-slug-placeholder) new-slug (assoc metadata-map :slug new-slug))
                            metrics-map)
          old-metric-slugs (vec (:metric-slugs data))
          metric-slugs (if new-metric?
                          (assoc old-metric-slugs (.indexOf (to-array old-metric-slugs) growth-utils/new-metric-slug-placeholder) new-slug)
                          old-metric-slugs)
          new-metrics (vec (growth-utils/metrics-as-sequence new-metrics-map metric-slugs))
          final-map (assoc data-map :metrics new-metrics)] ; add the metadata if this is a new metric
      (dis/dispatch! [:save-topic-data "growth" (assoc final-map :placeholder false)])
      (when new-metric?
        ((:switch-focus-cb data) new-slug {}))))) ; show the new metric

(defn- replace-row-in-data [owner row k v]
  (let [new-row (update row k (fn[_]v))]
    (growth-change-data owner new-row)))

;; ===== Growth Data Editing Component =====

(defcomponent growth-edit [{:keys [editing-cb
                                   archive-metric-cb
                                   first-edit-tip-cb
                                   initial-focus
                                   new-metric?
                                   currency
                                   focus
                                   metadata-on-change-cb] :as data} owner options]

  (init-state [_]
    {:growth-data (:growth-data data) ; all the growth data for all metrics
     :metric-slug initial-focus ; the slug of the current metric
     :metrics (:metrics data)
     :has-changes? false
     :stop batch-size}) ; how many periods (reverse chronological) to show

  (will-receive-props [_ next-props]
    (when (or (not= (:growth-data data) (:growth-data next-props))
              (not= (:metrics data) (:metrics next-props)))
      (om/update-state! owner #(merge % {:growth-data (:growth-data next-props)
                                         :metrics (:metrics next-props)}))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render-state [_ {:keys [growth-data metrics metric-slug stop has-changes?] :as state}]
    (let [company-slug (router/current-company-slug)
          {slug :slug interval :interval metric-name :name unit :unit description :description :as metric-info} (current-metric-info owner metric-slug)
          prefix (if (= unit "currency") (utils/get-symbol-for-currency-code currency) "")
          suffix (when (= unit "%") "%")]
      (dom/div {:class "growth"}
        (dom/div {:class "composed-section-edit growth-body edit"}

          ;; Meta-data editing form
          (om/build growth-metric-edit {:metric-info metric-info
                                        :new-metric? new-metric?
                                        :metric-count (count (filter-growth-data metric-slug growth-data))
                                        :currency currency
                                        :metadata-on-change-cb (fn [k v]
                                                                (om/set-state! owner :has-changes? true)
                                                                (om/set-state! owner :metrics (assoc metrics metric-slug (assoc (get metrics metric-slug) k v)))
                                                                (metadata-on-change-cb k v))})

          (dom/div

            ;; Growth metric data editing table
            (when interval

              ;; Data editing table
              (dom/div {:class "table-container group"}
                (dom/table {:class "table"
                            :key (str "growth-edit-" slug)}

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
                    (dom/tfoot {}
                      (dom/tr {}
                        (dom/th {:class "earlier" :col-span 2}
                          (dom/a {:class "small-caps underline bold dimmed-gray" :on-click #(more-months owner data)} "Earlier..."))
                        (dom/td {}))))))

            ;; Save growth data and cancel edit buttons
            (when interval
              (dom/div {:class "topic-foce-footer group"}
                (dom/div {:class "topic-foce-footer-right"}
                  (dom/button {:class "btn-reset btn-solid btn-data-save"
                               :disabled (or (not has-changes?)
                                              (string/blank? metric-name))
                               :on-click  #(do
                                            (save-data owner data new-metric?)
                                            (editing-cb false))} (if new-metric? "ADD" "UPDATE"))
                  (dom/button {:class "btn-reset btn-outline"
                               :on-click #(editing-cb false)} "CANCEL")
                  (when-not new-metric?
                    (dom/button {:class "btn-reset archive-button"
                                 :title "Archive this chart"
                                 :type "button"
                                 :data-toggle "tooltip"
                                 :data-container "body"
                                 :data-placement "top"
                                 :on-click #(archive-metric-cb metric-slug)}
                        (dom/i {:class "fa fa-archive"}))))))))))))