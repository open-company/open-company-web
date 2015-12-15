(ns open-company-web.components.growth.growth-metric-edit
  (:require [om.core :as om]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [cljs-dynamic-resources.core :as cdr]
            [open-company-web.caches :refer (new-sections)]
            [open-company-web.router :as router]
            [shodan.console :as console]))

(defn option-template [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)]
    (js/jQuery (str "<span>" (.-text state) "</span>"))))

(defn init-select2 [owner]
  ; get needed states
  (let [req-libs-loaded (om/get-state owner :req-libs-loaded)
        did-mount (om/get-state owner :did-mount)
        select2-initialized (om/get-state owner :select2-initialized)]
    ; check if we are ready to initialize the widget and if we haven't aready done that
    (when (and req-libs-loaded did-mount (not select2-initialized))
      ; init name
      (let [us (.$ js/window "select.metric-data#mtr-name")]
        (.select2 us (clj->js {"placeholder" "Metric name"
                               "templateResult" option-template
                               "templateSelection" option-template})))
      ; init unit
      (let [us (.$ js/window "select.metric-data#mtr-unit")]
        (.select2 us (clj->js {"placeholder" "Metric unit"
                               "templateResult" option-template
                               "templateSelection" option-template})))
      ; init goal
      (let [us (.$ js/window "select.metric-data#mtr-goal")]
        (.select2 us (clj->js {"placeholder" "Metric goal"
                               "allowClear" true
                               "templateResult" option-template
                               "templateSelection" option-template})))
      ; init name
      (let [us (.$ js/window "select.metric-data#mtr-interval")]
        (.select2 us (clj->js {"placeholder" "Metric interval"
                               "templateResult" option-template
                               "templateSelection" option-template})))
      ; save flag so we don't reinitialize the widget
      (om/update-state! owner :select2-initialized (fn [_]true)))))

(defn get-presets []
  (let [slug (keyword (:slug @router/path))
        sections-map (:categories (slug @new-sections))
        all-sections (apply concat (map (fn [sec] (:sections sec)) sections-map))
        growth-defaults (first (filter #(= (:name %) "growth") all-sections))]
    {:intervals (:intervals growth-defaults)
     :units (:units growth-defaults)
     :goal (:goal growth-defaults)
     :metrics (:metrics growth-defaults)}))

(defcomponent growth-metric-edit [data owner]

  (init-state [_]
    {:req-libs-loaded false
     :did-mount false
     :select2-initialized false
     :presets (get-presets)})

  (did-mount [_]
    (om/set-state! owner :did-mount true))

  (will-mount [_]
              ; load needed resources
              (cdr/add-style! "/lib/select2/css/select2.css")
              (cdr/add-scripts! [{:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"}
                                 {:src "/lib/select2/js/select2.js"}]
                                (fn []
                                  (om/update-state! owner :req-libs-loaded (fn [] true))
                                  (init-select2 owner))))
  (render [_]
    (let [metric-info (:metric-info data)
          {:keys [metrics intervals goal units] :as presets} (om/get-state owner :presets)]
      (dom/div {:class "growth-metric-edit"}
        ; name and unit
        (dom/div {:class "growth-metric-edit-row group"}
          ; name
          (dom/div {:class "metric-data-container group"}
            (dom/select {:class "metric-data metric-name"
                         :value (:slug metric-info)
                         :id "mtr-name"
                         :placeholder "Metric name"
                         :style {"width" "250px"}}
              (dom/option {:value ""} "Name")
              (for [metric metrics]
                (dom/option {:value (:slug metric)} (:name metric)))))
          ; unit
          (dom/div {:class "metric-data-container right group"}
            (dom/label {:for "mtr-unit"} "Measured in")
            (dom/select {:class "metric-data metric-unit"
                         :value (:unit metric-info)
                         :id "mtr-unit"
                         :placeholder "Metric unit"
                         :style {"width" "150px"}}
              (dom/option {:value ""} "Unit")
              (for [unit units]
                (dom/option {:value unit} (utils/camel-case-str unit))))))
        ; textarea
        (dom/div {:class "growth-metric-edit-row group"}
          (dom/textarea {:class "metric-data metric-description"
                         :placeholder "Metric description"}))
        ; goal and interval
        (dom/div {:class "growth-metric-edit-row group"}
          ; goal
          (dom/div {:class "metric-data-container group"}
            (dom/label {:for "mtr-goal"} "Goal:")
            (dom/select {:class "metric-data metric-goal"
                         :value (:goal metric-info)
                         :id "mtr-goal"
                         :placeholder "Metric goal"
                         :style {"width" "150px"}}
              (dom/option {:value ""} "Goal")
              (for [g goal]
                (dom/option {:value g} (utils/camel-case-str g)))))
          ; interval
          (dom/div {:class "metric-data-container right group"}
            (dom/label {:for "mtr-goal"} "Interval:")
            (dom/select {:class "metric-data metric-interval"
                         :value (:interval metric-info)
                         :id "mtr-interval"
                         :placeholder "Metric interval"
                         :style {"width" "150px"}}
              (dom/option {:value ""} "Interval")
              (for [interval intervals]
                (dom/option {:value interval} (utils/camel-case-str interval))))))))))