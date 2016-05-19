(ns open-company-web.components.finances.topic-finances
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.finances.cash :refer (cash)]
            [open-company-web.components.finances.cash-flow :refer (cash-flow)]
            [open-company-web.components.finances.costs :refer (costs)]
            [open-company-web.components.finances.runway :refer (runway)]
            [open-company-web.lib.utils :as utils]))

(defn get-state [owner data & [initial]]
  (let [section-data (:section-data data)]
    {:focus (if initial
              (or (:selected-metric data) "cash")
              (om/get-state owner :focus))
     :finances-data (finances-utils/map-placeholder-data (:data section-data))}))

(defn pillbox-click [owner options e]
  (.preventDefault e)
  (let [tab  (.. e -target -dataset -tab)]
    (om/update-state! owner :focus (fn [] tab)))
  (.stopPropagation e))

(defn has-revenues-or-costs [finances-data]
  (some #(or (not (zero? (:revenue %))) (not (zero? (:costs %)))) finances-data))

(defn render-pillboxes [owner options]
  (let [data (om/get-props owner)
        section-data (:section-data data)
        focus (om/get-state owner :focus)
        classes "pillbox"
        cash-classes (str classes (when (= focus "cash") " active"))
        cash-flow-classes (str classes (when (= focus "cash-flow") " active"))
        runway-classes (str classes (when (= focus "runway") " active"))
        finances-row-data (:data section-data)
        sum-revenues (apply + (map :revenue finances-row-data))
        needs-runway (some #(neg? (:runway %)) finances-row-data)
        first-title (if (pos? sum-revenues) "Cash flow" "Burn rate")
        needs-cash-flow (has-revenues-or-costs finances-row-data)]
    (dom/div {:class "pillbox-container finances"}
      (dom/label {:class cash-classes
                  :title "Cash"
                  :data-tab "cash"
                  :on-click (partial pillbox-click owner options)} "Cash")
      (when needs-cash-flow
        (dom/label {:class cash-flow-classes
                    :title first-title
                    :data-tab "cash-flow"
                    :on-click (partial pillbox-click owner options)} first-title))
      (when needs-runway
        (dom/label {:class runway-classes
                    :title "Runway"
                    :data-tab "runway"
                    :on-click (partial pillbox-click owner options)} "Runway")))))

(defcomponent topic-finances [{:keys [section section-data currency] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-update [_ next-props _]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props data)
      (om/set-state! owner (get-state owner next-props true))))

  (render-state [_ {:keys [focus] :as state}]
    (let [finances-row-data (:data section-data)
          no-data (or (empty? finances-row-data) (utils/no-finances-data? finances-row-data))
          sum-revenues (apply + (map :revenue finances-row-data))
          subsection-data {:section-data section-data
                           :read-only true
                           :currency currency}
          subsection-options {:opts options}]
  
      (if no-data
  
        (dom/div {:class "topic-overlay-body"}
          (dom/div {:class "topic-body-inner group"}
            (dom/p "Information on finances is not yet available.")))
  
        (dom/div {:class "section-container" :id "section-finances"}
          (dom/div {:class "composed-section finances group"}
            (when (:pillboxes-first options)
              (render-pillboxes owner options))
            (dom/div {:class (utils/class-set {:composed-section-body true})}
              (case focus

                "cash"
                (om/build cash subsection-data subsection-options)

                "cash-flow"
                (if (pos? sum-revenues)
                  (om/build cash-flow subsection-data subsection-options)
                  (om/build costs subsection-data subsection-options))

                "runway"
                (om/build runway subsection-data subsection-options)))
            (when-not (:pillboxes-first options)
              (render-pillboxes owner options))))))))