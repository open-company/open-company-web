(ns open-company-web.components.growth.growth-metric-edit
  (:require [om.core :as om]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [cljs-dynamic-resources.core :as cdr]
            [open-company-web.caches :refer (new-sections)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.iso4217 :refer (sorted-iso4217)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.lib.responsive :as responsive]
            [cuerdas.core :as s]
            [open-company-web.components.ui.popover :refer (add-popover hide-popover)]))

(def metric-defaults {
  :slug growth-utils/new-metric-slug-placeholder
  :name ""
  :description ""
  :unit "number"
  :interval "monthly"})

(defn- show-archive-confirm-popover [owner data]
  (add-popover {:container-id "archive-metric-confirm"
                :message "This chart will no longer be available for new data, but prior entries will still be visible in topic history. Are you sure you want to archive?"
                :cancel-title "KEEP"
                :cancel-cb #(hide-popover nil "delete-metric-confirm")
                :success-title "ARCHIVE"
                :success-cb #((:archive-metric-cb data) (om/get-state owner :metric-slug))}))

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
      (doto (js/$ "select#mtr-unit")
        (.select2 (clj->js {"placeholder" "Metric unit"
                            "minimumResultsForSearch" -1
                            "templateResult" unit-option-template
                            "templateSelection" unit-option-template}))
        (.on "change" (fn [e]
                        (let [unit-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)]
                          (om/set-state! owner :unit unit-value)))))

      ; init interval dropdown
      (doto (js/$ "select#mtr-interval")
        (.select2 (clj->js {"placeholder" "Metric interval"
                            "minimumResultsForSearch" -1
                            "templateResult" option-template
                            "templateSelection" option-template}))
        (.on "change" (fn [e]
                        (let [interval-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)]
                          (om/set-state! owner :interval interval-value)))))

      ; focus name
      (.focus (js/$ "input#mtr-name"))

      ; save flag so we don't reinitialize the widget
      (om/update-state! owner :select2-initialized (fn [_]true)))))

(defn- get-presets [data]
  (let [slug (keyword (router/current-company-slug))
        all-sections (:new-sections (slug @new-sections)) ; get new sections templates from cache
        growth-defaults (first (filter #(= (:section-name %) "growth") all-sections))]
    {:intervals (:intervals growth-defaults)
     :units (:units growth-defaults)}))

(defn- save-metric-info [owner save-cb new?]
  (let [slug (om/get-state owner :metric-slug)]
    (save-cb slug {
      :slug slug
      :name (om/get-state owner :metric-name)
      :description (om/get-state owner :description)
      :unit (om/get-state owner :unit)
      :interval (om/get-state owner :interval)}
      new?)))

(defcomponent growth-metric-edit [data owner options]

  (init-state [_]
    (let [new-metric? (:new-metric? data)
          metric-info (if new-metric? metric-defaults (:metric-info data))
          company-currency-code (:currency options)
          presets (get-presets data)
          units (:units presets)
          fixed-units (vec (map #(if (= (:unit %) "currency")
                                   {:unit "currency"
                                    :name (utils/get-symbol-for-currency-code company-currency-code)}
                                   %) units))]
      {:req-libs-loaded false
       :did-mount false
       :select2-initialized false
       :presets presets
       :metric-slug (:slug metric-info)
       :metric-name (:name metric-info)
       :description (:description metric-info)
       :unit (:unit metric-info)
       :units fixed-units
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
      (when (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render [_]
    (let [all-metrics (:metrics data)
          {:keys [metrics intervals prompt] :as presets} (om/get-state owner :presets)
          units (om/get-state owner :units)
          new-metric? (:new-metric? data)]

      (dom/div {:class "growth-metric-edit p3"}

        ;; name
        (dom/div {:class "small-caps bold mb1"} "Chart label")
        (dom/input {:class "npt col-8 p1 mb2"
          :type "text"
          :value (om/get-state owner :metric-name)
          :on-change (fn [e] (om/set-state! owner :metric-name (.. e -target -value)))
                       ;(change-name owner data))
          :id "mtr-name"
          :placeholder "A short name, e.g. DAU"})

        ;; description
        (dom/div {:class "small-caps bold mb1"} "Description (Shown in tooltip)")
        (dom/input {:class "npt col-12 p1 mb2"
                    :type "text"
                    :value (om/get-state owner :description)
                    :placeholder "e.g. Daily Active Users"
                    :on-change (fn [e] (om/set-state! owner :description (.. e -target -value)))})

        ;; interval
        (dom/div {:class "small-caps bold mb1"} "Interval")
        (dom/select {:class "npt col-5 p1 mb2"
                     :default-value (om/get-state owner :interval)
                     :id "mtr-interval"
                     ; if there are data the interval can't be changed
                     :disabled (and (pos? (:metric-count data))
                                    (not (:new-metric? data)))}
          (for [interval intervals]
            (dom/option {:value interval} (utils/camel-case-str interval))))

        ;; unit
        (dom/div {:class "small-caps bold mb1"} "Measured As")
        (dom/select {:class "npt col-5 p1 mb2"
                     :default-value (or (om/get-state owner :unit) "A number")
                     :id "mtr-unit"}
          (for [unit units]
            (dom/option {:value (:unit unit)} (:name unit))))

        (dom/div {:class "topic-foce-footer group"}
          (dom/div {:class "topic-foce-footer-right"}

            ;; next or save button
            (dom/button {:class "btn-reset btn-outline btn-data-save"
                         :disabled (or (s/blank? (om/get-state owner :metric-slug))
                                       (s/blank? (om/get-state owner :metric-name))
                                       (s/blank? (om/get-state owner :unit))
                                       (s/blank? (om/get-state owner :interval)))
                         :on-click #(do (utils/event-stop %)
                                        (save-metric-info owner (:save-cb data) new-metric?))}
              (if new-metric? "NEXT" "SAVE"))

            ;; cancel button
            (dom/button {:class "btn-reset btn-outline"
                         :on-click #(do (utils/event-stop %)
                                        ((:cancel-cb data)))} "CANCEL")

            (when-not new-metric?
              (dom/button {:class "btn-reset archive-button"
                           :title "Archive this chart"
                           :type "button"
                           :data-toggle "tooltip"
                           :data-placement "top"
                           :on-click #(show-archive-confirm-popover owner data)}
                  (dom/i {:class "fa fa-archive"})))


            ))))))