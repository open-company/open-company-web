(ns open-company-web.components.ui.sortable.sortable-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]))

(defn replace-item-in-vec [coll old-item new-item]
  (let [fix-coll (to-array coll)
        idx (.indexOf fix-coll old-item)]
    (when-not (neg? idx)
      (let [before (.slice fix-coll 0 idx)
            after (.slice fix-coll (inc idx) (count coll))]
        (concat before [new-item] after)))))

(defn insert-at-index [coll item idx]
  (let [fix-coll (to-array coll)
        before (.slice fix-coll 0 idx)
        after (.slice fix-coll (inc idx) (count coll))]
    (concat before [item] after)))

(defn drag-start [e owner]
  (println "drag-start" (.-id (.-dataset (.-currentTarget e))))
  (om/set-state! owner :dragged (.-currentTarget e))
  (set! (.-effectAllowed (.-dataTransfer e)) "move")
  
  ; Firenfox fix
  (.setData (.-dataTransfer e) "text/html" (.-currentTarget e)))

(defn drag-end [e owner]
  (let [dragged (om/get-state owner :dragged)]
    (set! (.-display (.-style dragged)) "block")
    (let [dragged-id (.-id (.-dataset dragged))
          items (om/get-state owner :sort)
          plc-idx (.indexOf (to-array items) "placeholder")
          new-items (insert-at-index items dragged-id plc-idx)]
      (println "drag-end" new-items)
      (doto owner
        (om/set-state! :sort new-items)
        (om/set-state! :dragged nil)))))

(defn get-li-parent [el]
  (if (= (.-tagName el) "LI")
    el
    (get-li-parent (.-parentNode el))))
  
(defn drag-over [e owner]
  (.preventDefault e)
  (set! (.-display (.-style (om/get-state owner :dragged))) "none")
  (println "target: " (get-li-parent (.-target e)))
  (when-not (= (.-className (get-li-parent (.-target e))) "placeholder")
    (let [over (.-id (.-dataset (get-li-parent (.-target e))))]
      (when-not (= over "placeholder")
        (let [items (om/get-state owner :sort)
              plc-idx (.indexOf (to-array items) "placeholder")
              over-idx (.indexOf (to-array items) over)
              new-items (if (neg? plc-idx) items (insert-at-index items over plc-idx))
              new-items (insert-at-index new-items "placeholder" over-idx)]
          (println "drag-over" over items plc-idx over-idx "->" new-items)
          (om/set-state! owner :sort new-items))))))

(defcomponent sortable-list [data owner options]

  (init-state [_]
    {:sort (:sort data)})

  (render [_]
    (println "sort: " (:sort data))
    (dom/ul #js {:onDragOver #(drag-over % owner)
                 :onDrop #(drag-end % owner)}
      (for [item (om/get-state owner :sort)]
        (if (= item "placeholder")
          (dom/li #js {:data-id "placeholder"
                       :className "placeholder"
                       :key "placeholder"})
          (let [items (:items data)
                item-data ((keyword item) items)]
            (dom/li #js {:data-id item
                         :key (:name item-data)
                         :draggable true
                         :onDragStart #(drag-start % owner)
                         :style #js {:display "block"}}
              (om/build (:item data) (merge {:id (:name item-data)
                                             :item-data item} (:to-item data))
                        {:opts options}))))))))

