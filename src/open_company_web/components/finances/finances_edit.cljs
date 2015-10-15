(ns open-company-web.components.finances.finances-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.cell :refer [cell]]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :refer [app-state]]))

(defcomponent finances-edit-row [data owner]
  (render [_]
    (let [prefix (:prefix data)
          finances-data (:cursor data)
          is-new (:new finances-data)
          cell-state (if is-new :new :display)
          change-cb (:change-cb data)]
      (dom/tr {}
        (dom/td {:class "no-cell"} (utils/period-string (:period finances-data) (when is-new :force-year)))
        (dom/td {}
          (om/build cell {:value (:cash finances-data)
                          :placeholder (if is-new "at month end" "")
                          :prefix prefix
                          :cell-state cell-state
                          :draft-cb #(change-cb :cash %)}))
        (dom/td {}
          (om/build cell {:value (:revenue finances-data)
                          :placeholder (if is-new "entire month" "")
                          :prefix prefix
                          :cell-state cell-state
                          :draft-cb #(change-cb :revenue %)}))
        (dom/td {}
          (om/build cell {:value (:costs finances-data)
                          :placeholder (if is-new "entire month" "")
                          :prefix prefix
                          :cell-state cell-state
                          :draft-cb #(change-cb :costs %)}))
        (dom/td {:class (utils/class-set {:no-cell true :new-row-placeholder is-new})}
                (if is-new "calculated" (:burn-rate finances-data)))
        (dom/td {:class (utils/class-set {:no-cell true :new-row-placeholder is-new})}
                (if is-new "calculated" (:runway finances-data)))))))

(defn save-new-row? [finances-data]
  (let [new-period (first (filter #(and (contains? % :new) (true? (:new %))) finances-data))]
    (if (or (not (nil? (:cash new-period)))
            (not (nil? (:costs new-period)))
            (not (nil? (:revenue new-period))))
      true
      false)))

(defn row-ok? [row]
  (if (or (nil? (:cash row))
          (nil? (:costs row))
          (nil? (:revenue row)))
    false
    true))

(defn save-data [owner finances-data original-cursor close-cb]
  "Save the edit data that lives in the component state to the cursor and
  send the save signal"
  (let [array-data (to-array finances-data)
        new-row (first (filter #(and (contains? % :new) (true? (:new %))) array-data))
        new-index (.indexOf array-data new-row)
        should-save-new (save-new-row? array-data)
        to-save (filter #(not (:new %)) array-data)
        new-row (dissoc new-row :new)
        to-save (if should-save-new (into [] (conj to-save new-row)) (into [] to-save))]
    (if-not (every? row-ok? to-save)
      (.alert js/window "Check the finances values")
      (let [fixed-finances (utils/calc-runway to-save)
            slug (keyword (:slug @router/path))]
        (om/update! original-cursor :data fixed-finances)
        (api/update-finances-data (:finances (slug @app-state)))
        (close-cb)))))

(defn replace-row-in-data [owner finances-data row k v]
  "Find and replace the edited row"
  (let [array-data (js->clj (to-array finances-data))
        new-row (update row k (fn[_]v))]
    (loop [idx 0]
      (let [cur-row (get array-data idx)]
        (if (= (:period cur-row) (:period new-row))
            (let [new-rows (assoc array-data idx new-row)]
              (om/update-state! owner :data (fn [_] new-rows)))
          (recur (inc idx)))))))

(defcomponent finances-edit [data owner]
  (init-state [_]
    ; add a new line if necessary
    (let [company-data (:company-data data)
          finances-data (:finances company-data)
          initial-data (:data finances-data)
          cur-period (utils/current-period)]
      (if-not (utils/period-exists cur-period initial-data)
        (let [new-period {:period cur-period
                          :cash nil
                          :costs nil
                          :revenue nil
                          :new true}
              new-data (into [new-period] initial-data)]
          {:data new-data
           :initial-data initial-data})
          {:data initial-data
           :initial-data initial-data})))
  (render [_]
    (let [slug (:slug @router/path)
          finances-data (om/get-state owner :data)
          cur-symbol (utils/get-symbol-for-currency-code (:currency (:finances data)))
          rows-data (map (fn [row] 
                           (merge {:prefix cur-symbol
                                   :change-cb (fn [k v] (replace-row-in-data owner finances-data row k v))}
                                  {:cursor row}))
                         finances-data)]
      (if (:loading data)
        ; loading
        (dom/h4 {} "Loading data...")
        ; real component
        (dom/div {:class "row"}
          (dom/div {:class "finances"}
            (dom/h2 {} "Finances")
            (dom/div {:class "finances-body edit"}
              (dom/table {:class "table table-striped"}
                (dom/thead {}
                  (dom/tr {}
                    (dom/th {} "")
                    (dom/th {} "Cash")
                    (dom/th {} "Revenue")
                    (dom/th {} "Costs")
                    (dom/th {} "Burn")
                    (dom/th {} "Runway")))
                (dom/tbody {}
                  (for [row rows-data]
                    (om/build finances-edit-row row {:key (str (rand 4))}))))
              (dom/div {:class "finances-edit-buttons"}
                (dom/button {:class "btn btn-success"
                             :on-click #(save-data owner
                                                   (om/get-state owner :data)
                                                   (:finances (:company-data data))
                                                   (:close-edit-cb data))} "Save")
                (dom/button {:class "btn btn-default cancel"
                             :on-click (fn [_]
                                         (let [initial-data (om/get-state owner :initial-data)]
                                           (utils/handle-change (:finances (:company-data data)) initial-data :data))
                                         ((:close-edit-cb data)))}
                            "Cancel")))))))))