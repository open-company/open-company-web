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

(defcomponent growth-edit-row [{:keys [interval needs-year is-last change-cb next-period prefix suffix] :as data} owner]

  (render [_]
    (let [growth-data (:cursor data)
          is-new (and (not (:value growth-data)) (:new growth-data))
          value (:value growth-data)
          period (:period growth-data)
          period-month (utils/get-month period interval)
          flags [:short (when needs-year :force-year)]
          period-string (utils/get-period-string period interval flags)
          cell-state (if is-new :new :display)
          tab-cb (fn [_ k]
                   (cond
                     (= k :value)
                     (when next-period
                       (signal-tab next-period :value))))]
      (dom/tr {:class "growth-edit-row"}
        (dom/td {:class "no-cell"}
          period-string)
        (dom/td {}
          (when-not is-last
            (om/build
              cell
              {:value value
               :placeholder "Value"
               :cell-state cell-state
               :draft-cb #(change-cb :value %)
               :prefix prefix
               :suffix suffix
               :period period
               :key :value
               :tab-cb tab-cb})))))))

(defn replace-row-in-data [row k v change-cb]
  "Find and replace the edited row"
  (let [new-row (update row k (fn[_]v))]
    (change-cb new-row)))

(defn get-current-metric-info [metric-slug data]
  (or (get (:metrics data) metric-slug) {}))

(def batch-size 6)

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

(defcomponent growth-edit [data owner options]

  (init-state [_]
    {:metadata-edit false ; not editing metric metadata
     :growth-data (:growth-data data) ; all the growth data for all metrics
     :metric-slug (:initial-focus data) ; the slug of the current metric
     :new-metric false
     :stop batch-size})

  (will-receive-props [_ next-props]
    (when (not= (:growth-data data) (:growth-data next-props))
      (om/set-state! owner :growth-data (:growth-data next-props))))

  (render-state [_ {:keys [metric-slug growth-data metrics growth-metric-slugs metadata-edit new-metric stop]}]
    (let [{:keys [interval slug] :as metric-info} (get-current-metric-info metric-slug data)
          prefix (if (= (:unit metric-info) "currency")
                   (utils/get-symbol-for-currency-code (:currency options))
                   "")
          suffix (when (= (:unit metric-info) "%") "%")]
      (dom/div {:class "composed-section-edit growth edit"}
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
              (dom/div {:class "chart-header-container"}
                (dom/div {:class "target-actual-container"}
                  (dom/div {:class "actual-container"}
                    (dom/h3 {:class "actual blue"
                             :on-click #(set-metadata-edit owner data true)}
                      (:name metric-info)
                      (dom/button {:class "btn-reset metadata-edit-button"
                                   :title "Edit this metric"
                                   :type "button"
                                   :data-toggle "tooltip"
                                   :data-placement "right"
                                   :style {:font-size "15px"}
                                   :on-click #(set-metadata-edit owner data true)}
                        (dom/i {:class "fa fa-cog"})))))))

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
                                                          (replace-row-in-data row-data k v (:change-growth-cb data)))}))))
                    (dom/tr {}
                      (dom/td {}
                        (dom/a {:class "more" :on-click #(more-months owner data)} "Earlier..."))
                      (dom/td {}))))))

            ;; metric selection pillboxes
            (when-not metadata-edit
              ;; existing metrics
              (dom/div {:class "pillbox-container growth"}
                (for [metric-slug (:growth-metric-slugs data)]
                  (let [metric (get-in data [:metrics metric-slug])
                        mname (:name metric)
                        metric-classes (utils/class-set {:pillbox true
                                                         metric-slug true
                                                         :active (= slug metric-slug)})]
                    (dom/label {:class metric-classes
                                :title (:description metric)
                                :data-tab metric-slug
                                :on-click (fn [e]
                                            (.stopPropagation e)
                                            (om/set-state! owner :metric-slug metric-slug))} mname)))
                ;; new metric
                (dom/label {:class (utils/class-set {:pillbox true
                                                     growth-utils/new-metric-slug-placeholder true
                                                     :active (= slug growth-utils/new-metric-slug-placeholder)})
                            :title "Add a new metric"
                            :data-tab growth-utils/new-metric-slug-placeholder
                            :on-click (fn [e]
                                        (.stopPropagation e)
                                        (set-metadata-edit owner data true)
                                        (om/set-state! owner :new-metric true)
                                        (om/set-state! owner :metric-slug growth-utils/new-metric-slug-placeholder))} "+ New metric")))))))))