(ns open-company-web.components.growth.growth-metric-edit
  (:require [om.core :as om]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [cljs-dynamic-resources.core :as cdr]
            [open-company-web.caches :refer (new-sections)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.iso4217 :refer (sorted-iso4217)]))

(defn option-template [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)]
    (js/jQuery (str "<span>" (.-text state) "</span>"))))

(defn unit-option-template [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)
        currency (utils/get-currency text)
        fixed-text (if currency
                     (utils/get-symbol-for-currency-code text)
                     (utils/camel-case-str text))]
    (js/jQuery (str "<span>" fixed-text "</span>"))))

(defn change-name [owner data]
  (let [change-cb (:change-growth-metric-cb data)
        name-value (clojure.string/trim (.val (.$ js/window "input#mtr-name")))
        slug (utils/slugify name-value)]
    (when-not (clojure.string/blank? name-value)
      ; change the slug only if it's a newly created metric
      (when (:new data)
        (om/set-state! owner :metric-slug slug)
        (change-cb slug :slug slug)
        (change-cb slug :description (om/get-state owner :description))
        (change-cb slug :unit (om/get-state owner :unit))
        (change-cb slug :target (om/get-state owner :target))
        (change-cb slug :interval (om/get-state owner :interval)))
      (om/set-state! owner :metric-name name-value)
      (change-cb slug :name name-value))))

(defn init-select2 [owner data]
  ; get needed states
  (let [req-libs-loaded (om/get-state owner :req-libs-loaded)
        did-mount (om/get-state owner :did-mount)
        select2-initialized (om/get-state owner :select2-initialized)
        change-cb (:change-growth-metric-cb data)]
    ; check if we are ready to initialize the widget and if we haven't aready done that
    (when (and req-libs-loaded did-mount (not select2-initialized))
      ; init name
      (let [name-input (.$ js/window "input#mtr-name")
            metrics (:metrics (om/get-state owner :presets))
            metrics-list (vec (sort #(compare %1 %2) (map #(:name %) metrics)))
            autocomplete (.autocomplete name-input (clj->js {"source" metrics-list
                                                             "minLength" 0
                                                             "change" #(change-name owner data)}))]
        (.focus name-input (fn [_]
                            (this-as this
                              (when (clojure.string/blank? (.-value this))
                                (.trigger (.$ js/window this) "keydown.autocomplete")))))
        (.call (.autocomplete autocomplete "option" "change") autocomplete))
      ; init unit
      (doto (.$ js/window "select#mtr-unit")
        (.select2 (clj->js {"placeholder" "Metric unit"
                            "templateResult" unit-option-template
                            "templateSelection" unit-option-template}))
        (.on "change" (fn [e]
                        (let [unit-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)]
                          (om/set-state! owner :unit unit-value)
                          (when slug
                            (change-cb slug :unit unit-value))))))
      ; init goal
      (doto (.$ js/window "select.metric-data#mtr-goal")
        (.select2 (clj->js {"placeholder" "Metric goal"
                            "allowClear" true
                            "templateResult" option-template
                            "templateSelection" option-template}))
        (.on "change" (fn [e]
                        (let [target-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)]
                          (om/set-state! owner :target target-value)
                          (when slug
                            (change-cb slug :target target-value))))))
      ; init interval
      (doto (.$ js/window "select.metric-data#mtr-interval")
        (.select2 (clj->js {"placeholder" "Metric interval"
                            "templateResult" option-template
                            "templateSelection" option-template}))
        (.on "change" (fn [e]
                        (let [interval-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)]
                          (om/set-state! owner :interval interval-value)
                          (when slug
                            (change-cb slug :interval interval-value))))))
      ; save flag so we don't reinitialize the widget
      (om/update-state! owner :select2-initialized (fn [_]true)))))

(defn get-presets []
  (let [slug (keyword (:slug @router/path))
        sections-map (:categories (slug @new-sections))
        all-sections (apply concat (map (fn [sec] (:sections sec)) sections-map))
        growth-defaults (first (filter #(= (:name %) "growth") all-sections))]
    {:intervals (:intervals growth-defaults)
     :units (:units growth-defaults)
     :target (:goal growth-defaults)
     :metrics (:metrics growth-defaults)}))

(defcomponent growth-metric-edit [data owner]

  (init-state [_]
    (let [metric-info (:metric-info data)
          company-slug (keyword (:slug @router/path))
          company-data (company-slug @dispatcher/app-state)
          company-currency-code (:currency company-data)
          presets (get-presets)
          units (:units presets)
          fixed-units (vec (map #(if (= % "currency") company-currency-code %) units))]
      {:req-libs-loaded false
       :did-mount false
       :select2-initialized false
       :presets presets
       :metric-name (:name metric-info)
       :metric-slug (:slug metric-info)
       :description (:description metric-info)
       :unit (:unit metric-info)
       :units fixed-units
       :interval (:interval metric-info)
       :target (:target metric-info)
       :currency company-currency-code}))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (init-select2 owner data))

  (will-mount [_]
              ; load needed resources
              (cdr/add-style! "/lib/select2/css/select2.css")
              (cdr/add-scripts! [{:src "/lib/select2/js/select2.js"}]
                                (fn []
                                  (om/update-state! owner :req-libs-loaded (fn [] true))
                                  (init-select2 owner data))))
  (render [_]
    (let [metric-info (:metric-info data)
          {:keys [metrics intervals target] :as presets} (om/get-state owner :presets)
          units (om/get-state owner :units)]
      (dom/div {:class "growth-metric-edit"}
        ; name and unit
        (dom/div {:class "growth-metric-edit-row group"}
          ; name
          (dom/div {:class "metric-data-container group"}
            (dom/input {:class "metric-data metric-name"
                        :type "text"
                        :value (om/get-state owner :metric-name)
                        :on-change #(om/set-state! owner :metric-name (.. % -target -value))
                        :id "mtr-name"
                        :placeholder "Metric name"
                        :style {"width" "240px"}}))
          ; unit
          (dom/div {:class "metric-data-container right group"}
            (dom/label {:for "mtr-unit"} "Measured in")
            (dom/select {:class "metric-data metric-unit"
                         :value (om/get-state owner :unit)
                         :id "mtr-unit"
                         :placeholder "Metric unit"
                         :style {"width" "150px"}}
              (dom/option {:value ""} "Unit")
              (for [unit units]
                (dom/option {:value (if (= unit "currency")
                                      (om/get-state owner :currency)
                                      unit)} unit)))))
        ; textarea
        (dom/div {:class "growth-metric-edit-row group"}
          (dom/textarea {:class "metric-data metric-description"
                         :on-change (fn [e]
                                      (let [value (.. e -target -value)
                                            slug (om/get-state owner :metric-slug)
                                            change-cb (:change-growth-metric-cb data)]
                                        (when slug
                                          (change-cb slug :description value))))
                         :placeholder "Metric description"} (om/get-state owner :description)))
        ; goal and interval
        (dom/div {:class "growth-metric-edit-row group"}
          ; goal
          (dom/div {:class "metric-data-container group"}
            (dom/label {:for "mtr-goal"} "Goal:")
            (dom/select {:class "metric-data metric-goal"
                         :value (om/get-state owner :target)
                         :id "mtr-goal"
                         :placeholder "Metric goal"
                         :style {"width" "150px"}}
              (dom/option {:value ""} "Goal")
              (for [g target]
                (dom/option {:value g} (utils/camel-case-str g)))))
          ; interval
          (dom/div {:class "metric-data-container right group"}
            (dom/label {:for "mtr-goal"} "Interval:")
            (dom/select {:class "metric-data metric-interval"
                         :value (om/get-state owner :interval)
                         :id "mtr-interval"
                         ; if there are data the interval can't be changed
                         :disabled (pos? (:metric-count data))
                         :placeholder "Metric interval"
                         :style {"width" "150px"}}
              (dom/option {:value ""} "Interval")
              (for [interval intervals]
                (dom/option {:value interval} (utils/camel-case-str interval))))))
        (when (:new data)
          (dom/div {:class "growth-metric-edit-row group"}
            (dom/button {:class "oc-btn oc-success green"
                         :disabled (or (not (om/get-state owner :metric-name))
                                       (not (om/get-state owner :metric-slug))
                                       (not (om/get-state owner :unit))
                                       (not (om/get-state owner :interval)))
                         :on-click #((:next-cb data))} "NEXT")
            (dom/button {:class "oc-btn oc-cancel gray"} "CANCEL")))))))