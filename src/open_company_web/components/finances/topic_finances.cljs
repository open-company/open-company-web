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
              "cash"
              (om/get-state owner :focus))
     :finances-data (finances-utils/map-placeholder-data (:data section-data))}))

(defn subsection-click [e owner]
  (.preventDefault e)
  (let [tab  (.. e -target -dataset -tab)]
    (om/update-state! owner :focus (fn [] tab)))
  (.stopPropagation e))

(defn has-revenues-or-costs [finances-data]
  (some #(or (not (zero? (:revenue %))) (not (zero? (:costs %)))) finances-data))

(defcomponent topic-finances [{:keys [section section-data currency] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-receive-props [_ next-props]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner (get-state owner next-props))))

  (render-state [_ {:keys [focus] :as state}]
    (let [classes "composed-section-link oc-header"
          cash-classes (str classes (when (= focus "cash") " active"))
          cash-flow-classes (str classes (when (= focus "cash-flow") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))
          finances-row-data (:data section-data)
          sum-revenues (apply + (map :revenue finances-row-data))
          first-title (if (pos? sum-revenues) "Cash flow" "Burn rate")
          needs-runway (some #(neg? (:runway %)) finances-row-data)
          needs-cash-flow (has-revenues-or-costs finances-row-data)
          subsection-data {:section-data section-data
                           :read-only true
                           :currency currency}
          subsection-options {:opts options}]
      (dom/div {:class "section-container" :id "section-finances"}
        (dom/div {:class "composed-section finances"}
          (dom/div {:class (utils/class-set {:link-bar true})}
            (dom/a {:href "#"
                    :class cash-classes
                    :title "Cash"
                    :data-tab "cash"
                    :on-click #(subsection-click % owner)} "Cash")
            (when needs-cash-flow
              (dom/a {:href "#"
                      :class cash-flow-classes
                      :title first-title
                      :data-tab "cash-flow"
                      :on-click #(subsection-click % owner)} first-title))
            (when needs-runway
              (dom/a {:href "#"
                      :class runway-classes
                      :title "Runway"
                      :data-tab "runway"
                      :on-click #(subsection-click % owner)} "Runway")))
          (dom/div {:class (utils/class-set {:composed-section-body true})}
            (case focus

              "cash"
              (om/build cash subsection-data subsection-options)

              "cash-flow"
              (if (pos? sum-revenues)
                (om/build cash-flow subsection-data subsection-options)
                (om/build costs subsection-data subsection-options))

              "runway"
              (om/build runway subsection-data subsection-options))))))))