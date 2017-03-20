(ns oc.web.components.growth.growth-metric-edit
  (:require [om.core :as om]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [cljs-dynamic-resources.core :as cdr]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.iso4217 :refer (sorted-iso4217)]
            [oc.web.lib.growth-utils :as growth-utils]
            [oc.web.lib.responsive :as responsive]))

(def metric-defaults {
  :slug growth-utils/new-metric-slug-placeholder
  :name ""
  :description ""
  :unit "number"
  :interval "monthly"})

(defn- option-template [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)]
    (js/jQuery (str "<span>" (.-text state) "</span>"))))

(defn- unit-option-template [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)
        currency (utils/get-currency text)
        fixed-text (if currency
                     (utils/get-symbol-for-currency-code text)
                     (utils/camel-case-str text))]
    (js/jQuery (str "<span>" fixed-text "</span>"))))

(defn- init-select2 [owner data]
  ; get needed states
  (let [req-libs-loaded (om/get-state owner :req-libs-loaded)
        did-mount (om/get-state owner :did-mount)
        select2-initialized (om/get-state owner :select2-initialized)]
    ; check if we are ready to initialize the widget and if we haven't aready done that
    (when (and req-libs-loaded did-mount (not select2-initialized))
      ; init unit dropdown
      (doto (js/$ "select#mtr-unit.unit")
        (.select2 (clj->js {"placeholder" "Metric unit"
                            "minimumResultsForSearch" -1
                            "templateResult" unit-option-template
                            "templateSelection" unit-option-template}))
        (.on "change" (fn [e]
                        (let [unit-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)
                              on-change-cb (om/get-props owner :metadata-on-change-cb)]
                          (om/set-state! owner :unit unit-value)
                          (on-change-cb :unit unit-value)))))

      ; init interval dropdown
      (doto (js/$ "select#mtr-interval.interval")
        (.select2 (clj->js {"placeholder" "Metric interval"
                            "minimumResultsForSearch" -1
                            "templateResult" option-template
                            "templateSelection" option-template}))
        (.on "change" (fn [e]
                        (let [interval-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)
                              on-change-cb (om/get-props owner :metadata-on-change-cb)]
                          (om/set-state! owner :interval interval-value)
                          (on-change-cb :interval interval-value)))))

      ; focus name
      (.focus (js/$ "input#mtr-name"))

      ; save flag so we don't reinitialize the widget
      (om/update-state! owner :select2-initialized (fn [_]true)))))

(defn- get-presets [data]
  (let [org-slug   (keyword (router/current-org-slug))
        board-slug (keyword (router/current-board-slug))
        all-topics (get-in @dis/app-state (dis/board-new-topics-key org-slug board-slug)) ; get new topics templates from cache
        growth-defaults (:growth all-topics)]
    {:intervals (:intervals growth-defaults)
     :units (:units growth-defaults)}))

(defcomponent growth-metric-edit [data owner options]

  (init-state [_]
    (let [new-metric? (:new-metric? data)
          metric-info (if new-metric? metric-defaults (:metric-info data))
          company-currency-code (:currency data)
          presets (get-presets data)
          units (:units presets)]
      {:req-libs-loaded false
       :did-mount false
       :select2-initialized false
       :presets presets
       :metric-slug (:slug metric-info)
       :metric-name (:name metric-info)
       :description (:description metric-info)
       :unit (:unit metric-info)
       :units units
       :interval (if new-metric? (:interval metric-defaults) (:interval metric-info))
       :currency company-currency-code}))

  (will-mount [_]
    ; load needed resources
    (cdr/add-style! "/lib/select2/css/select2.css")
    (cdr/add-scripts! [{:src "/lib/select2/js/select2.js"}]
                      (fn []
                        (om/update-state! owner :req-libs-loaded (fn [] true))
                        (init-select2 owner data))))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (init-select2 owner data)
    (when-not (utils/is-test-env?)
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render-state [_ {:keys [metric-slug
                           metric-name
                           description
                           interval
                           unit
                           presets
                           units
                           currency]}]
    (let [all-metrics (:metrics data)
          {:keys [metrics intervals prompt] :as presets} presets
          new-metric? (:new-metric? data)]
      (dom/div {:class "growth-metric-edit p3 group"}

        ;; name
        (dom/div {:class "group" :style {:white-space "nowrap" :text-align "left"}}
          (dom/label {:class "small-caps col-3 mt1 left"
                      :style {:padding-top "3px"}} "CHART LABEL")
          (dom/input {:class "npt col-9 p1 mb2"
                      :type "text"
                      :value (or metric-name "")
                      :max-length 10
                      :on-change (fn [e]
                                  (let [v (.. e -target -value)
                                        on-change-cb (om/get-props owner :metadata-on-change-cb)]
                                    (om/set-state! owner :metric-name v)
                                    (on-change-cb :name v)))
                      :id "mtr-name"
                      :placeholder "Short label e.g. DAU"}))

        ;; description
        (dom/div {:class "group" :style {:white-space "nowrap" :text-align "left"}}
          (dom/label {:class "small-caps col-3 mt1 left"
                      :style {:padding-top "3px"}} "DESCRIPTION")
          (dom/input {:class "npt col-9 p1 mb2"
                      :type "text"
                      :value (or description "")
                      :placeholder "Description - e.g. Daily Active Users"
                      :on-change (fn [e]
                                    (let [v (.. e -target -value)
                                          on-change-cb (om/get-props owner :metadata-on-change-cb)]
                                      (om/set-state! owner :description v)
                                      (on-change-cb :description v)))}))

        ;; interval
        (dom/div {:class "group" :style {:white-space "nowrap" :text-align "left"}}
          (dom/label {:class "small-caps col-3 left"} " ")
          (dom/div {:class "col-4 left"}
            (dom/select {:class "npt col-12 p1 mb2 interval"
                         :default-value interval
                         :id "mtr-interval"
                         ; if there are data the interval can't be changed
                         :disabled (and (pos? (:metric-count data))
                                        (not (:new-metric? data)))}
              (for [interval intervals]
                (dom/option {:value interval} (utils/camel-case-str interval)))))
          (dom/label {:class "col-1 left"} " ")
          (dom/div {:class "col-4 left"}
            (dom/select {:class "npt col-12 p1 mb2 unit"
                         :default-value (or unit "Number")
                         :id "mtr-unit"}
              (for [unit units]
                (dom/option {:value (:unit unit)} (:name unit))))))))))