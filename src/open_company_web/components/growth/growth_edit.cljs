(ns open-company-web.components.growth.growth-edit
  (:require [clojure.string :as string]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.cell :refer (cell)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.components.growth.growth-metric-edit :refer (growth-metric-edit)]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.components.ui.onboard-tip :refer (onboard-tip)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [cljs.core.async :refer (put!)]))

(def batch-size 6)

(defn signal-tab [period k]
  (when-let [ch (utils/get-channel (str period k))]
    (put! ch {:period period :key k})))

(defcomponent growth-edit-row [{:keys [interval needs-year is-last change-cb next-period prefix suffix] :as data} owner]

  (render [_]
    (let [growth-data (:cursor data)
          is-new (and (not (:value growth-data)) (:new growth-data))
          value (:value growth-data)
          period (:period growth-data)
          period-month (utils/get-month period interval)
          flags [:short :skip-year]
          period-string (utils/get-period-string period interval flags)
          cell-state (if is-new :new :display)
          tab-cb (fn [_ k]
                   (cond
                     (= k :value)
                     (when next-period
                       (signal-tab next-period :value))))]
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
               :tab-cb tab-cb})))))))

(defn growth-get-value [v]
  (if (string/blank? v)
    ""
    (if (js/isNaN v)
      0
      v)))

(defn growth-fix-row [row]
  (let [fixed-value (growth-get-value (:value row))
        with-fixed-value (if (string/blank? fixed-value)
                           (dissoc row :value)
                           (assoc row :value fixed-value))
        fixed-target (growth-get-value (:target with-fixed-value))
        with-fixed-target (if (string/blank? fixed-target)
                           (dissoc with-fixed-value :target)
                           (assoc with-fixed-value :target fixed-target))]
    with-fixed-target))

(defn growth-check-value [v]
  (and (not (= v ""))
       (not (nil? v))))

(defn growth-row-has-data [row]
  (or (growth-check-value (:value row))
      (growth-check-value (:target row))))

(defn growth-change-data
  [owner row]
  (when (growth-row-has-data row)
    (let [{:keys [period slug] :as fixed-row} (growth-fix-row row)
          growth-data (om/get-state owner :growth-data)
          fixed-data (if (and (not (:target fixed-row))
                              (not (:value fixed-row)))
                       (dissoc growth-data (str period slug))
                       (assoc growth-data (str period slug) fixed-row))]
      ;(om/set-state! owner :has-changes true)
      (om/set-state! owner :growth-data fixed-data))))

(defn get-current-metric-info [metric-slug data]
  (or (get (:metrics data) metric-slug) {}))

(defn more-months [owner data]
  (om/update-state! owner :stop #(+ % batch-size)))

(defn set-metadata-edit [owner data editing]
  ((:metadata-edit-cb data) editing)
  (om/set-state! owner :metadata-edit editing))

(defn save-metadata-cb [owner data]
  ((:save-metadata-cb data) (:metric-slug data))
  (set-metadata-edit owner data false))

(defn filter-growth-data [metric-slug growth-data]
  (into {} (filter (fn [[k v]] (= (:slug v) metric-slug)) growth-data)))

(defn replace-row-in-data [owner row k v]
  (let [new-row (update row k (fn[_]v))]
    (growth-change-data owner new-row)))

(defcomponent growth-edit [{:keys [editing-cb show-first-edit-tip first-edit-tip-cb] :as data} owner options]

  (init-state [_]
    {:metadata-edit false ; not editing metric metadata
     :growth-data (:growth-data data) ; all the growth data for all metrics
     :metric-slug (:initial-focus data) ; the slug of the current metric
     :new-metric false
     :stop batch-size})

  (will-receive-props [_ next-props]
    (when (not= (:growth-data data) (:growth-data next-props))
      (om/set-state! owner :growth-data (:growth-data next-props))))

  (render-state [_ {:keys [metric-slug growth-data metrics metadata-edit new-metric stop] :as state}]

    (let [company-slug (router/current-company-slug)
          {:keys [interval slug] :as metric-info} (get-current-metric-info metric-slug data)
          prefix (if (= (:unit metric-info) "currency")
                   (utils/get-symbol-for-currency-code (:currency options))
                   "")
          suffix (when (= (:unit metric-info) "%") "%")]

      (dom/div {:class "growth"}
        (dom/div {:class "composed-section-edit growth-body edit"}
      
          (if metadata-edit
            (om/build growth-metric-edit {:metric-info metric-info
                                          :new-metric new-metric
                                          :metric-count (count (filter-growth-data metric-slug growth-data))
                                          :metrics metrics
                                          :new-growth-section (:new-growth-section data)
                                          :save-cb #(save-metadata-cb owner data)
                                          :delete-metric-cb (fn [metric-slug]
                                                             (om/set-state! owner :metadata-edit false)
                                                             ((:delete-metric-cb data) metric-slug))
                                          :cancel-cb (fn []
                                                       ((:cancel-cb data))
                                                       (om/set-state! owner :new-metric false)
                                                       (set-metadata-edit owner data false))
                                          :change-growth-metric-cb (:change-growth-metric-cb data)}
                                          {:opts {:currency (:currency options)}})
            
            (dom/div
              ;; metric label and meta-data edit icon
              (when interval
                (dom/div {:class "metric-name"}
                  (:name metric-info)
                  (dom/button {:class "btn-reset metadata-edit-button"
                               :title "Edit this metric"
                               :type "button"
                               :data-toggle "tooltip"
                               :data-placement "right"
                               :style {:font-size "15px"}
                               :on-click #(set-metadata-edit owner data true)}
                    (dom/i {:class "fa fa-cog"}))))

              ;; metric editing table
              (when interval
                (dom/div {:class "table-container group"}
                  (dom/table {:class "table"
                              :key (str "growth-edit-" slug)}
                    (dom/thead {}
                      (dom/tr {}
                        (dom/th {} "")
                        (dom/th {} "Value")))
                    (dom/tbody {}
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
                            (om/build growth-edit-row {:cursor row-data
                                                       :next-period next-period
                                                       :is-last (= idx 0)
                                                       :needs-year (or (= idx 1)
                                                                       (= idx (dec stop)))
                                                       :prefix prefix
                                                       :suffix suffix
                                                       :interval interval
                                                       :change-cb (fn [k v]
                                                            (replace-row-in-data owner row-data k v))}))))
                      (dom/tr {}
                        (dom/th {:class "earlier" :col-span 2}
                          (dom/a {:class "small-caps underline bold dimmed-gray" :on-click #(more-months owner data)} "Earlier..."))
                        (dom/td {}))))))

              (when interval
                (dom/div {:class "topic-foce-footer group"}
                (dom/div {:class "topic-foce-footer-right"}
                  (dom/button {:class "btn-reset btn-outline btn-data-save"
                               :on-click  #(do
                                            (utils/event-stop %)
                                            ;(save-data owner)
                                            (editing-cb false))} "SAVE")
                  (dom/button {:class "btn-reset btn-outline"
                               :on-click #(do
                                            (utils/event-stop %)
                                            (editing-cb false))} "CANCEL"))))

            ;; Onboarding toolip
            (when (:show-first-edit-tip data)
              (onboard-tip
                {:id (str "growth-topic-add-" company-slug)
                 :once-only true
                 :mobile false
                 :desktop "Add metrics you'd like to share and we'll build simple charts for you."
                 :dismiss-tip-fn (:first-edit-tip-cb data)})))))))))